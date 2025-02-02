package com.kanyuServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kanyuServer.common.Result;
import com.kanyuServer.entity.Goods;

public interface GoodsService extends IService<Goods> {


    Result isPuchase(Long goodId, Long id);
    Result insertGoods(Goods goods);
    Result updateGoods(Goods goods);

    Result queryGoodById(Long goodId);
}
