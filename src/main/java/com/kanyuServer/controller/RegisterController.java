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

import cn.hutool.json.JSON;
import com.kanyuServer.common.Result;
import com.kanyuServer.dto.RegisterForm;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.LoginService;
import com.kanyuServer.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author 登录控制器
 */
@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Resource
    RegisterService registerService;
    @PostMapping("user")
    public Result sendCode(@RequestBody RegisterForm registerForm, HttpSession session) {
        //打印日志
        log.info("用户注册信息"+ registerForm.toString());
        Result result = registerService.register(registerForm,session);
        return result;
    }



}
