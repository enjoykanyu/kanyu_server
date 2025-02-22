package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.entity.UserWallet;

import java.math.BigDecimal;

public interface UserWalletService extends IService<UserWallet> {

    BigDecimal queryByUserId(Long userId);

    BigDecimal updateBalance(Long userId,BigDecimal updatePrice,Integer type);
}
