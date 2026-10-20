package com.ha.springbootdemo.modules.user.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailVO {
    private Long id;
    private Long roleId;
    private String username;
    private String phone;
    private String nickname;
    private String roleName;
    private String avatar;
    private Integer status;
    private Date lastLoginTime;
}
