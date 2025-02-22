package com.kanyuServer.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.Order;
import com.kanyuServer.entity.User;
import com.kanyuServer.mapper.GoodsMapper;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.service.OrderService;
import com.kanyuServer.utils.RedisData;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.kanyuServer.constant.RedisConstants.*;

@Slf4j
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Resource
    OrderService orderService;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Result isPuchase(Long goodsId, Long user_id) {
        log.info(user_id.toString());
        Order order = orderService.query().eq("goods_id", goodsId).eq("user_id", user_id).eq("status", 2).one();
        if (order==null){
            return Result.fail("用户未购买过",415);
        }
        return Result.ok(true);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Result insertGoods(Goods goods){
        User user = UserHolder.getUser();
        goods.setUserId(user.getId());
        save(goods);
        //写入redis 使用string结构
        stringRedisTemplate.opsForValue().set("cache:"+goods.getId()+"key", JSONUtil.toJsonStr(goods));
        try {//这里独立出一个方法用于审核通过之后，用户上架商品，再给商品增加缓存 使用逻辑过期时间存储
            this.saveGood2redis(goods.getId(),20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //发送消息通知审核系统 审核系统来处理，包括新建审核
        // 发送消息
        rabbitTemplate.convertAndSend("goods.direct","goods.audit",JSONUtil.toJsonStr(goods));
        return Result.ok(goods);
    }

    //修改商铺操作
    @Override
    @Transactional//这里指的是删除缓存有异常可以把数据库回滚 单纯redis无法回滚
    public Result updateGoods(Goods goods) {
        log.info("进入updateGoods"+goods);
        //1，更新数据库
        Long id = goods.getId();
        if (id == null){
            log.info("null"+goods);
            return Result.fail("商品信息为空",400);
        }
        boolean flag = update().eq("id",id).update(goods);
        log.info("updateGoods"+goods);
        //2，删除缓存
        stringRedisTemplate.delete("cache:"+id+"key");
        return Result.ok();
    }

    //缓存穿透问题存入""
    @Override
    public Result queryGoodById(Long goodId) {
        String cache_key = CACHE_GOODS_KEY+goodId;
        //查询redis是否存在商铺信息
        String goods_json = stringRedisTemplate.opsForValue().get(cache_key);
        log.info(goods_json);
        //查询到了数据 看是否逻辑过期  但是返回的数据为json格式的 需返回成对象形式
        if (!StrUtil.isBlank(goods_json)){
            log.info("111");
            //解决缓存击穿问题
            Goods goods = queryWithLogicExpire(goods_json, goodId);
            return Result.ok(goods);
        }
        //redis找不到数据 为null和""才会进入到这一步
        if (goods_json != null){ //属于空字符串""的时候进入 会进入到这一步说明命中了之前缓存进redis的""value 不合法无需查询数据库
            return Result.fail("商品不存在",400);
        }

        //redis找不到数据且数值=null 说明未进入缓存且不是无效值 需查询数据库
        Goods good_result = getById(goodId);
        //mysql中找不到则存入空值进入redis 说明查询的属于无效数据 缓存为""
        if (good_result == null){//注意这里存入空字符串 redis好像不可以存入null为value 存入value=""
            stringRedisTemplate.opsForValue().set(cache_key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
            return Result.fail("商品不存在",400);
        }
        //存在则进缓存
        stringRedisTemplate.opsForValue().set(cache_key, JSONUtil.toJsonStr(good_result),CACHE_GOODS_TTL, TimeUnit.MINUTES);
        //返回
        return Result.ok(good_result);
    }

    @Override
    public void likeGoods(Long goodsId) {
        Long userId = UserHolder.getUser().getId();

        String key = "goods_like"+goodsId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        if (score == null){
            //redis增加用户存储id
            stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
        }else {
            //redis剔除用户存储id
            stringRedisTemplate.opsForZSet().remove(key, userId.toString());
        }
        Long size = stringRedisTemplate.opsForZSet().size(key);

    }

    @Override
    public Long likeCount(Long goodsId) {
        String key = "goods_like"+goodsId;
        Long size = stringRedisTemplate.opsForZSet().size(key);
        return size;
    }

    //定义线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    //逻辑过期解决缓存击穿问题
    public Goods queryWithLogicExpire(String goods,Long goodId) {
        //把存入redis 的json字符串反序列化成redisdata对象
        RedisData redisData = JSONUtil.toBean(goods, RedisData.class);
        JSONObject goods_json = (JSONObject) redisData.getData();
        Goods goods_result = JSONUtil.toBean(goods_json, Goods.class);
        LocalDateTime expireTime = redisData.getExpireTime();
        log.info(expireTime+"");
        log.info(LocalDateTime.now()+"");
        //判断是否过期 当前时间与redis的逻辑过期时间对比
        if (expireTime != null && expireTime.isAfter(LocalDateTime.now())){
            log.info("未过期，直接返回redis数据");
            //未过期，直接返回店铺信息
            return goods_result;
        }
        //过期了进行缓存重建，尝试获取互斥锁
        //定义互斥锁的key
        String lock_key = LOCK_GOODS_KEY+goodId;
        boolean trylock = trylock(lock_key);
        //判断互斥锁是否被获取
        //拿到互斥锁有两种情况
        //情况一：其它的线程刚好重建完缓存且释放互斥锁 这里需再次判断 判断了可以直接返回 假设不判断 线程A刚操作完成 线程B又重建缓存 线程C又判断 重建缓存的操作会循环创建
        //情况二：无其它的线程操作
        if (trylock) {
            //double check检测缓存是否存在
            if (expireTime.isAfter(LocalDateTime.now())){
                //未过期，直接返回店铺信息
                return goods_result;
            }
            //6。3 成功拿到互斥锁，进行缓存重建
            CACHE_REBUILD_EXECUTOR.submit(() ->{
                try {
                    this.saveGood2redis(goodId,1L);

                }catch (Exception e){
                    throw new RuntimeException(e);
                }
                //释放锁
                this.unlock(lock_key);
            });
        }

        //6。4获取不到锁 说明有线程在操作进行缓存重建，则直接返回旧数据
        return goods_result;
    }

    //互斥锁解决
    private boolean trylock(String key){
        //不存在则存入1 flag=flase 已有互斥锁锁住了 flag=true 无互斥锁开启互斥锁
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
        //BooleanUtil.isTrue(flag) 这里使用BooleanUtil工具由于Boolean返回为包装类
        return BooleanUtil.isTrue(flag);
    }


    //释放互斥锁
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }

    //缓存重建
    public void saveGood2redis(Long id,Long expireHours) throws InterruptedException {
        //1,查询商品数据
        Goods goods = getById(id);
        //模拟缓存重建耗时
        Thread.sleep(200);
        //2,封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(goods);
        //expireSeconds传入当前时间多少秒之后过期
        redisData.setExpireTime(LocalDateTime.now().plusHours(expireHours));
        //3,写入redis 使用string结构
        stringRedisTemplate.opsForValue().set(CACHE_GOODS_KEY+id,JSONUtil.toJsonStr(redisData));
    }
}
