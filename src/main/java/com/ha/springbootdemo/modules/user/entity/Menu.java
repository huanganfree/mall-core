package com.ha.springbootdemo.modules.user.entity;

import lombok.Data;

@Data
public class Menu {
    Integer id;
    Integer parentId;
    String name;
    String type;
    String path;
    String component;
    String permission;
}