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
import com.kanyuServer.service.AdminGoodsService;
import com.kanyuServer.service.LoginService;
import lombok.extern.slf4j.Slf4j;
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
    AdminGoodsService adminGoodsService;

    @GetMapping("list")
    public Result sendCode(HttpSession session) {
        //打印日志
        List<Goods> list = adminGoodsService.query().list();
        return Result.ok(list);
    }

    /**
     * 查询商品详情页
     * @param goodId 商品id
     * @return 商品详情页
     */
    @GetMapping("/{goodId}")
    public Result queryVoucherOfShop(@PathVariable("goodId") Long goodId) {
        Goods goods = adminGoodsService.query().eq("id", goodId).one();
        return Result.ok(goods);
    }


}
