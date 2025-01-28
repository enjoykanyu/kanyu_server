package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.User;

import javax.servlet.http.HttpSession;

public interface LoginService extends IService<User> {
    Result sendCode(String phone, HttpSession session);
}
