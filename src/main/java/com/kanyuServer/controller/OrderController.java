package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.service.OrderService;
import com.kanyuServer.utils.RateLimitService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

;

/**
 * 文件资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;

    @Resource
    private RateLimitService rateLimitService;
    /**
     * 创建新订单
     * @param goodsId 虚拟商品信息
     * @return 订单信息
     */
    @PostMapping("create/{goodsId}")
    public Result createOrder(@PathVariable("goodsId") Long goodsId) {
        Result result = new Result();
        if (rateLimitService.tryRequest()){
            result = orderService.orderCreate(goodsId);
        }else {
            log.info("请求太多了，请重试");
            return Result.fail("请求太多了，请稍后重试",400);
        }
        return Result.ok(result);
    }

    /**
     * 查询订单信息
     * @param orderId 订单id
     * @return 订单信息
     */
    @PostMapping("query")
    public Result queryOrder(@RequestParam("orderId") String orderId) {
        Result result = orderService.queryByOrderID(orderId);
        return Result.ok(result);
    }

    /**
     * 删除订单信息
     * @param orderId 订单id
     * @return 订单信息
     */
    @PostMapping("delete")
    public Result deleteOrder(@RequestParam("orderId") String orderId) {
        Result result = orderService.deleteOrder(orderId);
        return Result.ok(result);
    }


    /**
     * 支付修改订单状态
     * @param orderId 订单id
     * @return 订单信息
     */
    @PostMapping("pay")
    public Result payOrder(@RequestParam("orderId") String orderId) {
        Result result = orderService.payOrder(orderId);
        return Result.ok(result);
    }
}
