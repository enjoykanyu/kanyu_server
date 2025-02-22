package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Pay;

public interface PayService extends IService<Pay> {

    Result pay(String orderId,Long goodsId,String password);
}
