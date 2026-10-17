package com.ha.springbootdemo.modules.user.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private Long roleId;
    private String phone;
    private String password;
    private String nickname;
    private String avatar;
    private Integer status;
    private String lastLoginTime;
}
