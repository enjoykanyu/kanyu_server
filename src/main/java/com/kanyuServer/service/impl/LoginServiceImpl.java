package com.kanyuServer.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.User;
import com.kanyuServer.mapper.UserMapper;
import com.kanyuServer.service.LoginService;
import com.kanyuServer.utils.Validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_KEY;
import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_TTL;
import static com.kanyuServer.constant.ResponseConstant.PHONE_NOT_FOUND;
import static com.kanyuServer.constant.ResponseConstant.SUCCESS_CODE;

@Slf4j
@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService{
    //引入redis工具类
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result sendCode(String phone, HttpSession session) {

        //1，看手机号格式是否正确
        boolean is_valid = Validate.isPhoneInvalid(phone);
        if (!is_valid) {
            return Result.fail(PHONE_NOT_FOUND,SUCCESS_CODE);
        }
        //2，看手机号是否存在数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = getOne(queryWrapper.eq("phone", phone));
        //由于未做前端 这里的业务逻辑保持和视频一致 实际操作业务得校验手机号是否已注册过 注册过了则直接登录 不走注册的流程了
//        if (user == null) {
//            return Result.fail("用户未注册过，请注册");//这里需前端跳转到注册网页
//        }

        //3，生成验证码
        String code = RandomUtil.randomNumbers(6);
        log.info("请求验证码"+code);
        //4，存储进入redis 设置常量 key=登录业务前缀+邮箱 过期时间2分钟
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,LOGIN_CODE_TTL, TimeUnit.MINUTES );
        //5，返回验证码
        HashMap<String,String> codeValue = new HashMap<>();
        codeValue.put("code", code);
        //返回给前端验证码可以打开在新弹窗
        return Result.ok(codeValue);
    }
}
