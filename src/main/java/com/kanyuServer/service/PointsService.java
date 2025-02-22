package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Points;

public interface PointsService extends IService<Points> {

    Result updatePoints(Long userId,Integer points,Integer status);

    Result queryPoints(Long userId);

}
