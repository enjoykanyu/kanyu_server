package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 优惠券
 */
@Slf4j
@RestController
@RequestMapping("/coupon")
@CrossOrigin(origins = "http://localhost:5177")
public class CouponController {

    @Resource
    CouponService couponService;
    /**
     * 新增秒杀券
     * @param coupon 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public Result addCoupon(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return Result.ok(coupon.getId());
    }

    /**
     * 查询商品的优惠券列表
     * @param goodsId 店铺id
     * @return 优惠券列表
     */
    @GetMapping("{goodsId}")
    public Result queryCouponOfGoods(@PathVariable("goodsId") Long goodsId) {
        return Result.ok();
    }

}
