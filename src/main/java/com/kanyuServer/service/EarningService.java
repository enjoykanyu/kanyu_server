package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Earning;

import java.math.BigDecimal;

public interface EarningService extends IService<Earning> {

    Result createEarning(Earning earning);//增加收益表记录 关联卖家和买家 关联订单 收益金额 状态待结算 已结算 已退款

    Result updateStatus(String orderId,Long status);//修改收益表记录 状态待结算 已结算 已退款

}
