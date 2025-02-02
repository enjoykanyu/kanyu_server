//package com.kanyuServer.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfig {
//    public static final String GOODS_AUDIT_QUEUE = "goodsAuditQueue";
//    public static final String GOODS_AUDIT_EXCHANGE = "goodsAuditExchange";
//    public static final String GOODS_AUDIT_ROUTING_KEY = "goodsAuditRoutingKey";
//
//    @Bean
//    Queue adAuditQueue() {
//        return new Queue(GOODS_AUDIT_QUEUE, false);
//    }
//
//    @Bean
//    TopicExchange adAuditExchange() {
//        return new TopicExchange(GOODS_AUDIT_EXCHANGE);
//    }
//
//    @Bean
//    Binding binding(Queue adAuditQueue, TopicExchange adAuditExchange) {
//        return BindingBuilder.bind(adAuditQueue).to(adAuditExchange).with(GOODS_AUDIT_ROUTING_KEY);
//    }
//}