package com.kanyuServer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.dto.LoginForm;
import com.kanyuServer.entity.User;
import com.kanyuServer.mapper.UserMapper;
import com.kanyuServer.service.LoginService;
import com.kanyuServer.utils.PasswordUtil;
import com.kanyuServer.utils.Validate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_KEY;
import static com.kanyuServer.constant.RedisConstants.LOGIN_CODE_TTL;
import static com.kanyuServer.constant.ResponseConstant.*;

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
        if (is_valid) {
            return Result.fail(PHONE_NOT_FOUND,SUCCESS_CODE);
        }
        //2，看手机号是否存在数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        User user = getOne(queryWrapper.eq("phone", phone));
        //校验手机号是否注册过，未注册过进入注册网页
        if (user == null) {
            return Result.fail(USER_NOT_FOUND,USER_NOT_FOUNT_CODE);//这里需前端跳转到注册网页
        }

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

    @Override
    public Result loginWithPassward(LoginForm loginForm, HttpSession session) {
        // 1.校验手机号
        String phone = loginForm.getPhone();
        if (Validate.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！",400);
        }
        User user = query().eq("phone", loginForm.getPhone()).one();
        if (user==null){
            //返回用户不存在 这里预期进入注册网页
            return Result.fail(USER_NOT_FOUND,USER_NOT_FOUNT_CODE);
        }
        //校验密码是否正确 这里直接对比原始的密码与加密的密码是否相同
        boolean flag = PasswordUtil.checkPassword(loginForm.getPassword(),user.getPassword());
        //不正确
        if (!flag){
         return Result.fail(USER_PASSWORD_NOT_CORRECT,USER_PASSWORD_NOT_CORRECT_CODE);
        }
        // 保存用户信息到 redis中
        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // 7.2.将User对象转为HashMap存储
        LoginForm loginForm1 = BeanUtil.copyProperties(user, LoginForm.class);
        System.out.println(loginForm1);
        log.info(loginForm1.toString());
        Map<String, Object> userMap = BeanUtil.beanToMap(loginForm1, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true));
        // 7.3.存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        // 7.4.设置token有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        // 8.返回token
        return Result.ok(token);
    }

    @Override
    public Result loginWithCode(LoginForm loginForm, HttpSession session) {
        // 1.校验手机号
        String phone = loginForm.getPhone();
        if (Validate.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.fail("手机号格式错误！",400);
        }
        //3,获取验证码
        String code = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + loginForm.getPhone());
        String code_user = loginForm.getCode();
        //redis拿不到相关验证码，说明过期或者手机号不存在
        if (code==null){
            return Result.fail(USER_NOT_FOUND,USER_NOT_FOUNT_CODE);
        }
        //redis保存的验证码与输入的验证码不匹配
        if (!code.equals(code_user)){
            return Result.fail(CODE_NOT_MATCH,CODE_NOT_MATCH_OCDE);
        }
        User user = query().eq("phone", loginForm.getPhone()).one();
        if (user==null){
            //返回用户不存在 这里预期进入注册网页
            return Result.fail(USER_NOT_FOUND,USER_NOT_FOUNT_CODE);
        }
        // 保存用户信息到 redis中
        // 随机生成token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        // 将User对象转为HashMap存储
        LoginForm loginForm1 = BeanUtil.copyProperties(user, LoginForm.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(loginForm1, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true));
        // 存储
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        //设置token有效期
        stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);

        //返回token
        return Result.ok(token);
    }
}
