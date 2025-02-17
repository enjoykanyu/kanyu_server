package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;

public interface CouponService extends IService<Coupon> {

    Result bindCoupon(Long goodsId, Long couponId);

    Result orderCoupon(Long goodsId, Long couponId);
}
