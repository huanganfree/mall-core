package com.ha.springbootdemo.modules.goods.entity;

import lombok.Data;

@Data
public class Order {
    Long id;
    Long goodsId;
    Long userId;
}
