package com.ha.springbootdemo.modules.goods.entity;

import lombok.Data;

@Data
public class Goods {
    private Integer id;
    private String goodsName;
    private Long stock;
}
