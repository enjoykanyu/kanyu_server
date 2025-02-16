package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.service.OrderService;
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
    /**
     * 创建新订单
     * @param goodsId 虚拟商品信息
     * @return 订单信息
     */
    @PostMapping("create/{goodsId}")
    public Result createOrder(@PathVariable("goodsId") Long goodsId) {
        Result result = orderService.orderCreate(goodsId);
        return Result.ok(result);
    }

    /**
     * 查询订单信息
     * @param orderId 订单id
     * @return 订单信息
     */
    @PostMapping("query")
    public Result addCoupon(@RequestParam("orderId") String orderId) {
        Result result = orderService.queryByOrderID(orderId);
        return Result.ok(result);
    }
}
