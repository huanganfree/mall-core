package com.ha.springbootdemo.modules.user.vo;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private String statusText;
    private String lastLoginTime;
}
