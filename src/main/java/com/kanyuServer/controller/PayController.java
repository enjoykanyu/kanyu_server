package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Order;
import com.kanyuServer.entity.Pay;
import com.kanyuServer.service.OrderService;
import com.kanyuServer.service.PayService;
import com.kanyuServer.utils.RateLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

;

/**
 * 订单支付控制器
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class PayController {
    @Resource
    PayService payService;

    @Resource
    private RateLimitService rateLimitService;

    @Resource
    private OrderService orderService;

    /**
     * 支付修改订单状态
     * @param pay 支付信息
     * @return 订单信息
     */
    @PostMapping("pay")
    public Result payOrder(@RequestBody Pay pay) {
        Order order = orderService.query().eq("order_id", pay.getOrderId()).one();
        Result result = payService.pay(pay.getOrderId(),order.getGoodsId(),pay.getPassword());
        return Result.ok(result);
    }
}
