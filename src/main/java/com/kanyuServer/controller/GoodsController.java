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

package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.AdminGoodsService;
import com.kanyuServer.service.GoodsService;
import com.kanyuServer.service.LoginService;
import com.kanyuServer.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

;import java.util.List;

/**
 * @author 文件资源控制器
 */
@Slf4j
@RestController
@RequestMapping("/goods")
@CrossOrigin(origins = "http://localhost:5177")
public class GoodsController {

    //商品所有资源列表
    @Resource
    GoodsService goodsService;


    @GetMapping("list")
    public Result list(HttpSession session) {
        //打印日志
        List<Goods> list = goodsService.query().list();
        return Result.ok(list);
    }

    /**
     * 查询商品详情页 通过缓存来解决
     * @param goodId 商品id
     * @return 商品详情页
     */
    @GetMapping("/{goodId}")
    public Result queryGoodById(@PathVariable("goodId") Long goodId) {

        return goodsService.queryGoodById(goodId);
    }

    /**
     * 查询用户是否购买过改商品
     * @param goodId 商品id
     * @return 商品详情页
     */
    @GetMapping("/{goodId}/isPuchase")
    public Result isPuchase(@PathVariable("goodId") Long goodId) {
        User user = UserHolder.getUser();
        return goodsService.isPuchase(goodId,user.getId());
    }




    /**
     * 用户上架商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("/goods/insert")
    public Result insertGoods(@RequestBody Goods goods) {
        return goodsService.insertGoods(goods);
    }


    /**
     * 用户下架商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("goods/{goodsId}")
    public Result deleteAdminGoods(@RequestBody Goods goods) {
        return null;
    }

    /**
     * 用户修改商品
     * @param goods 商品
     * @return 商品详情页
     */
    @PostMapping("goods/update")
    public Result updateAdminGoods(@RequestBody Goods goods) {
        log.info("updateAdminGoods修改"+goods);
        //写入数据库
        goodsService.updateGoods(goods);
        return Result.ok();
    }
}
