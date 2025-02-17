package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.Order;

public interface OrderService extends IService<Order> {

    Result orderCreate(Long goodsId);

    Result queryByOrderID(String orderId);


    Result deleteOrder(String orderId);

    Result payOrder(String orderId);
}
