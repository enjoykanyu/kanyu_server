package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.CouponOrder;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.mapper.CouponMapper;
import com.kanyuServer.mapper.CouponOrderMapper;
import com.kanyuServer.service.CouponOrderService;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class CouponOrderServiceImpl extends ServiceImpl<CouponOrderMapper, CouponOrder> implements CouponOrderService {

    @Resource
    CouponService couponService;

    /*
    * 优惠券抢购
    * */
    @Override
    @Transactional//多线程操作会有库存但下单报错的情况
    public Result orderCoupon(Long couponId) {
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
        int count = query().eq("user_id", UserHolder.getUser().getId()).count();
        if (count>0){
            return Result.fail("用户已创建过订单，失败",1000001);
        }
        //6，库存扣减且使用乐观锁判断库存与当前库存是否相等
        boolean success = couponService.update().setSql("stock =stock-1").eq("id", couponId).eq("stock", coupon.getStock()).update();

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

    @Override
    public Boolean updateStatus(String orderId, Integer status) {
        boolean isUpdate = update().eq("order_id", orderId).setSql("status = " + status).update();
        return isUpdate;
    }
}
