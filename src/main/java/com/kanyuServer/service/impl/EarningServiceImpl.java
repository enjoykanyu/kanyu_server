package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Earning;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.Order;
import com.kanyuServer.mapper.EarningMapper;
import com.kanyuServer.service.EarningService;
import com.kanyuServer.service.GoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class EarningServiceImpl extends ServiceImpl<EarningMapper, Earning> implements EarningService {


    @Resource
    GoodsService goodsService;
    @Override
    public Result createEarning(Earning earning) {

        return null;
    }

    @Override
    public Result updateStatus(String orderId, Long status) {
        return null;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "goods.earnings", durable = "true"),
            exchange = @Exchange(name = "goods.order"),
            key = "goods.order.pay"
    ))
    public void listenPaySuccessForCreateEarning(String order){
        Order order_value = JSONUtil.toBean(order, Order.class);
        Earning earning = new Earning();
        earning.setBuyerId(order_value.getUserId());
        Goods goods = goodsService.query().eq("id", order_value.getGoodsId()).one();
        earning.setSellerId(goods.getUserId());
        earning.setRevenueAmount(order_value.getActualPrice());
        earning.setStatus(1);
        createEarning(earning);
        System.out.println(order);
//        createEarning();
    }
}
