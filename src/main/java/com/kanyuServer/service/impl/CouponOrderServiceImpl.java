package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.CouponOrder;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.Order;
import com.kanyuServer.mapper.CouponMapper;
import com.kanyuServer.mapper.CouponOrderMapper;
import com.kanyuServer.service.CouponOrderService;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponOrderServiceImpl extends ServiceImpl<CouponOrderMapper, CouponOrder> implements CouponOrderService {

    @Resource
    CouponService couponService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;


    /*
    * 优惠券抢购
    * */
    @Transactional(rollbackFor = Exception.class) // 强制所有异常回滚 多线程操作会有库存但下单报错的情况
    public Result transactionalOrderCoupon(Long couponId,Long userId){
        try {
            // 业务逻辑
            //1，查看优惠券是否存在
            Coupon coupon = couponService.getById(couponId);
            if (coupon==null){
                return Result.fail("优惠券不存在",400);
            }
            //2,判断限时抢购时间是否开始 抢购开始时间比当前时间后 则抢购未开始
            if(coupon.getBeginTime().isAfter(LocalDateTime.now())){
                return Result.fail("优惠券抢购未开始",400);
            }
            //3,判断限时抢购时间是否结束 抢购开始时间比当前时间早 则抢购已结束
            if (coupon.getEndTime().isBefore(LocalDateTime.now())){
                return Result.fail("优惠券抢购已结束",400);
            }
            //4，判断库存是否充足
            if (coupon.getStock()<1){
                return Result.fail("优惠券已抢购空了",400);
            }
            //5,判断当前用户是否下过单
            int count = query().eq("user_id", userId).count();
            if (count>0){
                return Result.fail("用户已创建过订单，失败",1000001);
            }
            //6，库存扣减且使用乐观锁判断库存是否>0
            boolean success = couponService.update().setSql("stock =stock-1").eq("id", couponId).gt("stock",0).update();

            if (!success){
                return Result.fail("优惠券已抢购空了",400);
            }
            //7，创建优惠券订单
            CouponOrder couponOrder = new CouponOrder();
            couponOrder.setUserId(UserHolder.getUser().getId());
            //创建唯一订单id
            String uuid = UUID.randomUUID().toString();
            couponOrder.setOrderId(uuid);
            //订单关联优惠券
            couponOrder.setCouponId(couponId);
            save(couponOrder);
            return Result.ok(couponOrder);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚
            throw e;
        }

    }

    @Override
    public Result orderCoupon(Long couponId) {
        //1,判断当前用户是否下过单
        Long userId = UserHolder.getUser().getId();
        // 创建锁对象
        RLock redisLock = redissonClient.getLock("lock:order:" + userId);
        // 尝试获取锁
        boolean isLock = redisLock.tryLock();
        // 判断
        if(!isLock){
            // 获取锁失败，直接返回失败或者重试
            return Result.fail("不允许重复下单！",10000001);
        }
        try {
            // 执行业务逻辑（此处可调用事务方法）
            return transactionalOrderCoupon(couponId,userId);
        } finally {
            redisLock.unlock();
        }
    }

    @Override
    public Coupon queryCouponByGoods(Long goodsId) {
        //查询商品关联的优惠券id
        Coupon coupon = couponService.query().eq("goods_id", goodsId).one();

        //商品未关联优惠券
        if (coupon==null){
            return null;
        }
        //查询优惠券订单
        CouponOrder couponOrder = query().eq("coupon_id", coupon.getId()).one();
        if (couponOrder == null){
            return null;
        }
        if (couponOrder.getStatus()== 2){
            return null;
        }
        return coupon;
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "goods.coupon.order", durable = "true"),
            exchange = @Exchange(name = "goods.order"),
            key = "goods.order.pay"
    ))
    public void listenPaySuccessForupdateStatus(String order){
        System.out.println(order);
        Order order_value = JSONUtil.toBean(order, Order.class);
        updateStatus(order_value.getOrderId(),2);
    }
    @Override
    public Boolean updateStatus(String orderId, Integer status) {
        boolean isUpdate = update().eq("order_id", orderId).setSql("status = " + status).update();
        return isUpdate;
    }
}
