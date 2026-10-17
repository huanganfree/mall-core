package com.ha.springbootdemo.modules.user.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String phone;
    private String password;
}
