package com.kanyuServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Points;
import com.kanyuServer.entity.UserWallet;
import com.kanyuServer.mapper.PointsMapper;
import com.kanyuServer.mapper.UserWalletMapper;
import com.kanyuServer.service.PointsService;
import com.kanyuServer.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PointsServiceImpl extends ServiceImpl<PointsMapper, Points> implements PointsService {


    @Override
    public Result updatePoints(Long userId, Integer points, Integer status) {
        boolean isUpdate = update().eq("user_id", userId).setSql("points = points +" + points).update();
        return Result.ok(isUpdate);
    }

    @Override
    public Result queryPoints(Long userId) {
        Points points = query().eq("user_id", userId).one();
        return Result.ok(points.getPoints());
    }
}
