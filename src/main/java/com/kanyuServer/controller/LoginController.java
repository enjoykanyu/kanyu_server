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
import com.kanyuServer.dto.LoginForm;
import com.kanyuServer.entity.User;
import com.kanyuServer.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author 登录控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    //验证码动态登录前置发送验证码
    @Resource
    LoginService loginService;
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // TODO 发送短信验证码并保存验证码
        //打印日志
        log.info("前端请求手机"+phone);
        Result result = loginService.sendCode(phone,session);
        return result;
    }

    //手机号和密码登录
    @PostMapping("login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession session) {
        //打印日志
        log.info("前端请求登录信息"+loginForm);
        Result result = loginService.loginWithPassward(loginForm,session);
        return result;
    }
    //验证码登录
    @PostMapping("login/code")
    public Result loginWithCode(@RequestBody LoginForm loginForm, HttpSession session) {
        //打印日志
        log.info("前端请求登录信息"+loginForm);
        Result result = loginService.loginWithCode(loginForm,session);
        return result;
    }



}
