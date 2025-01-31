package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;

public interface AdminGoodsService extends IService<Goods> {
    Result insertGoods(Goods goods);
    Result updateGoods(Goods goods);
}
