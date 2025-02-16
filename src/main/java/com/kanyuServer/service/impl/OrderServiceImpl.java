package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.Order;
import com.kanyuServer.entity.User;
import com.kanyuServer.mapper.OrderMapper;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.service.OrderService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    GoodsService goodsService;
    @Override
    public Result orderCreate(Long goodsId) {
        //1，拿到当前下单用户信息
        User user = UserHolder.getUser();
        //2,在数据库新建订单
        Order order = new Order();
         //3,查询关联商品信息
        Goods goods = goodsService.query().eq("id", goodsId).one();
        if (goods==null){
            //未关联到商品，返回报错
            return Result.fail("未找到关联商品，请重试",400);
        }
        //4,订单关联支付金额
        Long price = goods.getPrice();
        //5,创建唯一订单id
        String uuid = UUID.randomUUID().toString();
        order.setGoodsId(goodsId);
        order.setUserId(user.getId());
        order.setOrderId(uuid);
        order.setOriginalPrice(price);
        order.setActualPrice(price);
        save(order);

        return Result.ok(order);
    }

    @Override
    public Result queryByOrderID(String orderId) {
        //1,拿到用户信息
        User user = UserHolder.getUser();

        Order order = query().eq("user_id", user.getId()).eq("order_id", orderId).one();
        //关联商品一起返回前端
        Goods goods = goodsService.query().eq("id", order.getGoodsId()).one();
        log.info(order.toString());
        if (order == null){
            return Result.fail("当前用户未找到改订单",400);
        }
        Map<String,Object> result = new HashMap<>();
        result.put("order",order);
        result.put("goods",goods);
        return Result.ok(result);
    }
}
