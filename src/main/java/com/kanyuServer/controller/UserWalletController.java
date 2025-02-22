package com.kanyuServer.controller;

import com.kanyuServer.common.Result;
import com.kanyuServer.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

;import java.math.BigDecimal;

/**
 * 用户钱包控制器
 */
@Slf4j
@RestController
@RequestMapping("/wallet")
public class UserWalletController {
    @Resource
    UserWalletService userWalletService;



    /**
     * 查看当前用户金额
     * @param userId 用户id
     * @return 用户金额
     */
    @GetMapping("/query")
    public Result payOrder(@RequestParam("userId") Long userId) {
        BigDecimal result = userWalletService.queryByUserId(userId);
        return Result.ok(result);
    }

    /**
     * 查看当前用户金额
     * @param userId 用户id
     * @return 用户金额
     */
    @GetMapping("/update")
    public Result updateBalance(@RequestParam("userId") Long userId,@RequestParam("updatePrice")BigDecimal updatePrice,@RequestParam("type")Integer type) {
        BigDecimal result = userWalletService.updateBalance(userId,updatePrice,type);
        return Result.ok(result);
    }
}
