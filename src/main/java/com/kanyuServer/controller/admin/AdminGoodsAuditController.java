/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kanyuServer.controller.admin;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.kanyuServer.common.Result;
import com.kanyuServer.dto.GoodsAudit;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.service.AdminGoodsAuditService;
import com.kanyuServer.service.AdminGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 管理员文件资源审核控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin/audit")
public class AdminGoodsAuditController {

    @Resource
    private AdminGoodsAuditService adminGoodsAuditService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 审核通过
     * @param goodsAudit 商品
     * @return 商品详情页
     */
    @PostMapping("/approve")
    public Result approveGoods(@RequestBody GoodsAudit goodsAudit) {
        //置为审核通过
        boolean isSuccess = adminGoodsAuditService.update().setSql("status = 1").eq("id", goodsAudit.getGoodsId()).update();
        if (isSuccess){
            //通知用户审核通过 TODO 消费者为用户拿到消息存入消息db还是用websocket
            rabbitTemplate.convertAndSend("goods.direct","goods.audit.approve", JSONUtil.toJsonStr(goodsAudit));
        }
        return Result.ok();
    }

    /**
     * 审核拒绝
     * @param goodsAudit 商品
     * @return 商品详情页
     */
    @PostMapping("/reject")
    public Result rejectGoods(@RequestBody GoodsAudit goodsAudit) {
        //置为审核拒绝
        boolean isSuccess = adminGoodsAuditService.update().set("status", 2).set("reject_reason",goodsAudit.getRejectReason()).eq("id", goodsAudit.getGoodsId()).update();
        if (isSuccess){
            //通知用户审核拒绝 用户可以重新修改再次上架给管理员审核
            rabbitTemplate.convertAndSend("goods.direct","goods.audit.reject", JSONUtil.toJsonStr(goodsAudit));
        }
        return Result.ok();
    }


}
