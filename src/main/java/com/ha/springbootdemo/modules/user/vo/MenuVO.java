package com.ha.springbootdemo.modules.user.vo;

import lombok.Data;

@Data
public class MenuVO {
    Integer id;
    Integer parentId;
    String name;
    String type;
    String path;
    String component;
    String permission;
}