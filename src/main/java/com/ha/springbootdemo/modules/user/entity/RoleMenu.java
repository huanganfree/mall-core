package com.ha.springbootdemo.modules.user.entity;

import lombok.Data;

@Data
public class RoleMenu {
    Integer id;
    Integer roleId;
    String menuId;
    String createdAt;
    String updatedAt;
}
