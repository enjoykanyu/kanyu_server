package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.entity.Order;
import com.kanyuServer.entity.UserWallet;
import com.kanyuServer.mapper.UserWalletMapper;
import com.kanyuServer.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Override
    public Long queryByUserId(Long userId) {
        UserWallet userWallet = query().eq("user_id", userId).one();
        Long balance = userWallet.getBalance();
        return balance;
    }

    @Override
    public Long updateBalance(Long userId,Long updatePrice,Integer type) {
        UserWallet userWallet = query().eq("user_id", userId).one();
        Long newPrice =0L;
        if (type==1){//扣减用户钱包
            newPrice = userWallet.getBalance()-updatePrice;
        }
        if (type==0){//退款增加用户钱包
            newPrice = userWallet.getBalance()+updatePrice;
        }
        boolean flag = update().eq("user_id", userId).setSql("balance = " + newPrice).update();
        return newPrice;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "goods.wallet", durable = "true"),
            exchange = @Exchange(name = "goods.order"),
            key = "goods.order.pay"
    ))
    public void listenPaySuccessForBalance(String order){
        System.out.println(order);
        Order order_value = JSONUtil.toBean(order, Order.class);
        updateBalance(order_value.getUserId(),order_value.getActualPrice(),1);
    }
}
