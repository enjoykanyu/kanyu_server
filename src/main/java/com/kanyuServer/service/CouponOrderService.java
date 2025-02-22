package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.CouponOrder;

public interface CouponOrderService extends IService<CouponOrder> {

    Result orderCoupon(Long couponId);

    Coupon queryCouponByGoods(Long goodsId);

    Boolean updateStatus(String orderId,Integer status);

}
