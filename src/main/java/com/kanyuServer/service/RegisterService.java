package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.dto.RegisterForm;
import com.kanyuServer.entity.User;

import javax.servlet.http.HttpSession;

public interface RegisterService extends IService<User> {
    Result register(RegisterForm registerForm, HttpSession session);
}
