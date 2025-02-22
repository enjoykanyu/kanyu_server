package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Earning;
import com.kanyuServer.mapper.EarningMapper;
import com.kanyuServer.service.EarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class EarningServiceImpl extends ServiceImpl<EarningMapper, Earning> implements EarningService {


    @Override
    public Result createEarning(Earning earning) {

        return null;
    }

    @Override
    public Result updateStatus(String orderId, Long status) {
        return null;
    }
}
