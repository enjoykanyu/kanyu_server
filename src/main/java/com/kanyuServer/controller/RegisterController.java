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
 * 登录控制器
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
