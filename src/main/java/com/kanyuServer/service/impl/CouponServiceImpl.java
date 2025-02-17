package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Coupon;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.mapper.CouponMapper;
import com.kanyuServer.service.CouponService;
import com.kanyuServer.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Resource
    GoodsService goodsService;
    @Override
    public Result bindCoupon(Long goodsId, Long couponId) {

        Goods goods = goodsService.query().eq("id", goodsId).one();
        goods.setCouponId(couponId);
        return null;
    }

    /*
    * 优惠券
    * */
    @Override
    public Result orderCoupon(Long goodsId, Long couponId) {
        return null;
    }
}
