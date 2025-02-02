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
 * 登录控制器
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
