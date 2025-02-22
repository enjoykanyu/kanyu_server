package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.CouponOrder;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.CouponOrderService;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 优惠券
 */
@Slf4j
@RestController
@RequestMapping("/couponOrder")
public class CouponOrderController {



    @Resource
    CouponOrderService couponOrderService;

    /**
     * 下单查询用户是否有改商品的优惠券
     * @param goodsId 商品id 前端查询两次 一次商品信息包含优惠券id 前端查询包含优惠券id则调用该接口 展示优惠券信息 前端查询不包含优惠券id 则商品未绑定优惠券则用户只可以原价购买
     * @return 优惠券列表
     */
    @GetMapping("{goodsId}")
    public Result queryCouponByGoods(@PathVariable("goodsId") Long goodsId) {
        Coupon coupon = couponOrderService.queryCouponByGoods(goodsId);
        return Result.ok(coupon);
    }

    /**
     * 优惠券抢购
     * @param couponId 优惠券id
     * @return 优惠券信息
     */
    @PostMapping("/orderCoupon")
    public Result orderCoupon(@RequestParam("couponId") Long couponId) {
        Result result = couponOrderService.orderCoupon(couponId);
        return Result.ok(result);
    }

}
