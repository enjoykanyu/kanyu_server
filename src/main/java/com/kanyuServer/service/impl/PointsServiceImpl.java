package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Order;
import com.kanyuServer.entity.Points;
import com.kanyuServer.entity.UserWallet;
import com.kanyuServer.mapper.PointsMapper;
import com.kanyuServer.mapper.UserWalletMapper;
import com.kanyuServer.service.PointsService;
import com.kanyuServer.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {


    @Override
    public Result updatePoints(Long userId, Long points) {
        boolean isUpdate = update().eq("user_id", userId).setSql("points = points +" + points).update();
        return Result.ok(isUpdate);
    }

    @Override
    public Result queryPoints(Long userId) {
        Points points = query().eq("user_id", userId).one();
        return Result.ok(points.getPoints());
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "goods.points", durable = "true"),
            exchange = @Exchange(name = "goods.order"),
            key = "goods.order.pay"
    ))
    public void listenPaySuccessForUpdatePoints(String order){
        Order order_value = JSONUtil.toBean(order, Order.class);
        updatePoints(order_value.getUserId(),order_value.getActualPrice());
        System.out.println(order);
//        updatePoints();
    }
}
