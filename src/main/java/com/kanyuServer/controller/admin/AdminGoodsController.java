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

import com.kanyuServer.common.Result;
import com.kanyuServer.dto.LoginForm;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.service.AdminGoodsService;
import com.kanyuServer.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author 管理员文件资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminGoodsController {

    //商品所有资源列表
    @Resource
    AdminGoodsService adminGoodsService;

    /**
     * 增加商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("/goods/insert")
    public Result insertGoods(@RequestBody Goods goods) {
        return adminGoodsService.insertGoods(goods);
    }



    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostMapping("/test")
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "queue_test";
        // 消息
        String message = "hello, spring amqp!";
        String exchange = "exchange_test";
        // 发送消息
        rabbitTemplate.convertAndSend(exchange,"pay.test",message);
    }

    /**
     * 管理员删除商品
     * @param goodsId 商品状态改成已删除
     * @return 是否删除成功
     */
    @GetMapping("goods/{goodsId}")
    public Result getAdminGoods(@RequestParam("goodsId") Long goodsId) {
        return null;
    }

    /**
     * 管理员下架商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("goods/{goodsId}")
    public Result deleteAdminGoods(@RequestBody Goods goods) {
        return null;
    }

    /**
     * 管理员修改商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("goods/update")
    public Result updateAdminGoods(@RequestBody Goods goods) {
        log.info("updateAdminGoods修改"+goods);
        //写入数据库
        adminGoodsService.updateGoods(goods);
        return Result.ok();
    }

}
