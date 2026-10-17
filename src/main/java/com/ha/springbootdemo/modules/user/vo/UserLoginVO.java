package com.ha.springbootdemo.modules.user.vo;

import lombok.Data;

@Data
public class UserLoginVO {
    private Long id;
    private String username;
    private String nickname;
    private String token;
}
