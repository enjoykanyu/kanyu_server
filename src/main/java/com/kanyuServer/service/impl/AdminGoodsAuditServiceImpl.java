package com.kanyuServer.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.mapper.AdminGoodsAuditMapper;
import com.kanyuServer.mapper.AdminGoodsMapper;
import com.kanyuServer.service.AdminGoodsAuditService;
import com.kanyuServer.service.AdminGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class AdminGoodsAuditServiceImpl extends ServiceImpl<AdminGoodsAuditMapper, Goods> implements AdminGoodsAuditService {


}
