package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.service.GoodsService;
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
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    CouponService couponService;
    /**
     * 新增抢购优惠券
     * @param coupon 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public Result addCoupon(@RequestBody Coupon coupon) {
        couponService.save(coupon);
        return Result.ok(coupon.getId());
    }

    /**
     * 查询商品的优惠券
     * @param goodsId 商品id 前端查询两次 一次商品信息包含优惠券id 前端查询包含优惠券id则调用该接口 展示优惠券信息 前端查询不包含优惠券id 则商品未绑定优惠券则用户只可以原价购买
     * @return 优惠券列表
     */
    @GetMapping("{goodsId}")
    public Result queryCoupon(@PathVariable("goodsId") Long goodsId) {
        Coupon coupon = couponService.query().eq("goods_id", goodsId).one();
        return Result.ok(coupon);
    }

    /**
     * 查询当前用户下所有优惠券
     * @param
     * @return 优惠券列表
     */
    @GetMapping("list")
    public Result couponList() {
        User user = UserHolder.getUser();
        List<Coupon> couponList = couponService.query().eq("user_id", user.getId()).list();
        return Result.ok(couponList);
    }

    /**
     * 将商品与优惠券绑定
     * @param goodsId 商品id couponId 优惠券id
     * @return 优惠券信息
     */
    @PostMapping("bindCoupon")
    public Result bindCoupon(@RequestParam("goodsId") Long goodsId,@RequestParam("couponId") Long couponId) {
        Result result = couponService.bindCoupon(goodsId,couponId);
        return Result.ok(result);
    }

    /**
     * 优惠券抢购
     * @param goodsId 商品id couponId 优惠券id
     * @return 优惠券信息
     */
    @PostMapping("setkillCoupon")
    public Result orderCoupon(@RequestParam("goodsId") Long goodsId,@RequestParam("couponId") Long couponId) {
        Result result = couponService.orderCoupon(goodsId,couponId);
        return Result.ok(result);
    }

}
