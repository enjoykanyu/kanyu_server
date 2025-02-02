package com.kanyuServer.listener;

import com.kanyuServer.service.AdminGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoodsStatusListener {
    //商品上架成功后，通知审核
    private final AdminGoodsService adminGoodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "goods_audit_queue", durable = "true"),
            exchange = @Exchange(name = "goods.direct"),
            key = "goods.audit"
    ))
    public void listenPaySuccess(String msg){
        // TODO 给管理员通知下
        System.out.println("消费者接收到topic.queue的消息：【" + msg + "】");
    }
}