package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.entity.UserWallet;
import com.kanyuServer.mapper.UserWalletMapper;
import com.kanyuServer.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Override
    public BigDecimal queryByUserId(Long userId) {
        UserWallet userWallet = query().eq("user_id", userId).one();
        BigDecimal balance = userWallet.getBalance();
        return balance;
    }

    @Override
    public BigDecimal updateBalance(Long userId,BigDecimal updatePrice,Integer type) {
        UserWallet userWallet = query().eq("user_id", userId).one();
        BigDecimal newPrice =null;
        if (type==1){//扣减用户钱包
            newPrice = userWallet.getBalance().divide(updatePrice);
        }
        if (type==0){//退款增加用户钱包
            newPrice = userWallet.getBalance().add(updatePrice);
        }
        boolean flag = update().eq("user_id", userId).setSql("balance = " + newPrice).update();
        return newPrice;
    }
}
