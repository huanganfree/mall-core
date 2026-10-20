package com.ha.springbootdemo.modules.goods.service;

import com.ha.springbootdemo.common.Result;

public interface SeckillService {
    Result<String> executeSeckill(Long userId, Long goodsId);
}
