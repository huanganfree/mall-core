package com.ha.springbootdemo.modules.goods.mapper;

import com.ha.springbootdemo.modules.goods.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void generateOrder(Order order);
}
