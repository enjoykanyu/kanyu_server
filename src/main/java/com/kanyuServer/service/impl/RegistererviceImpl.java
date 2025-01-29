package com.kanyuServer.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.dto.RegisterForm;
import com.kanyuServer.entity.User;
import com.kanyuServer.mapper.UserMapper;
import com.kanyuServer.service.LoginService;
import com.kanyuServer.service.RegisterService;
import com.kanyuServer.utils.Validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.kanyuServer.utils.PasswordUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_KEY;
import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_TTL;
import static com.kanyuServer.constant.ResponseConstant.*;

@Slf4j
@Service
public class RegistererviceImpl extends ServiceImpl<UserMapper, User> implements RegisterService {
    //引入redis工具类
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result register(RegisterForm registerForm, HttpSession session) {
        // 1.校验手机号
        String phone = registerForm.getPhone();
        if (Validate.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！",400);
        }
        User user_query = query().eq("phone", phone).one();
        if (user_query!=null){
            return Result.fail("用户已注册过，请登录",400);
        }
        registerForm.setPassword(PasswordUtil.hashPassword(registerForm.getPassword()));
        User user = new User();
        user.setPassword(registerForm.getPassword());
        user.setUserName(registerForm.getUserName());
        user.setPhone(registerForm.getPhone());
        save(user);
        return Result.ok();
    }
}
