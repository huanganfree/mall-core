package com.ha.springbootdemo.modules.goods.mapper;

import com.ha.springbootdemo.modules.goods.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

    // 初始化库存，用于压测
    void initGoods(long goodsNum);

    // 扣减更新库存
    void updateGoods(long goodsNum);

    Integer reduceGoods(Long goodsId);

    Goods selectById(Long goodsId);
}
