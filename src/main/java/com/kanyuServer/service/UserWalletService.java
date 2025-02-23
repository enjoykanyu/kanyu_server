package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.entity.UserWallet;

import java.math.BigDecimal;

public interface UserWalletService extends IService<UserWallet> {

    Long queryByUserId(Long userId);

    Long updateBalance(Long userId,Long updatePrice,Integer type);
}
