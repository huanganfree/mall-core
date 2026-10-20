package com.ha.springbootdemo.modules.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private Long roleId;
    private String roleName;
    private String username;
    private String phone;
    private String password;
    private String nickname;
    private String avatar;
    private Integer status;
    private Date lastLoginTime;
}
