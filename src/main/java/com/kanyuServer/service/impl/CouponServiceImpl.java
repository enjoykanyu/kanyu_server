package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.CouponOrder;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.mapper.CouponMapper;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Resource
    GoodsService goodsService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result bindCoupon(Long goodsId, Long couponId) {

        Goods goods = goodsService.query().eq("id", goodsId).one();
        goods.setCouponId(couponId);
        return null;
    }

    /*
    * 优惠券抢购
    * */
    @Override
    public Result orderCoupon(Long goodsId, Long couponId) {
        //1，查看优惠券是否存在
        Coupon coupon = query().eq("id", couponId).one();
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
        //5，库存扣减且使用乐观锁判断库存与当前库存是否相等
        boolean success = update().setSql("stock =stock-1").eq("id", couponId).eq("stock", coupon.getStock()).update();

        if (!success){
            return Result.fail("优惠券已抢购空了",400);
        }
        //6，创建优惠券订单
        CouponOrder couponOrder = new CouponOrder();
        couponOrder.setUserId(UserHolder.getUser().getId());
        //创建唯一订单id
        String uuid = UUID.randomUUID().toString();
        couponOrder.setOrderId(uuid);

        return null;
    }

    public Result addCoupon(Coupon coupon){
        // 保存抢购优惠券
        save(coupon);
        // Redis
        stringRedisTemplate.opsForValue().set("coupon:stock:" + coupon.getId(), coupon.getStock().toString());
        return Result.ok();
    }
}
