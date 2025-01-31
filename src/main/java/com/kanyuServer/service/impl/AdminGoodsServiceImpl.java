package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.mapper.AdminGoodsMapper;
import com.kanyuServer.service.AdminGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class AdminGoodsServiceImpl extends ServiceImpl<AdminGoodsMapper, Goods> implements AdminGoodsService {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result insertGoods(Goods goods){
        save(goods);
        //写入redis 使用string结构
        stringRedisTemplate.opsForValue().set("cache:"+goods.getId()+"key", JSONUtil.toJsonStr(goods));
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
}
