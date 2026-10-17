package com.ha.springbootdemo.modules.user.controller;

import com.ha.springbootdemo.common.PageResult;
import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.user.entity.User;
import com.ha.springbootdemo.modules.user.service.UserService;
import com.ha.springbootdemo.modules.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public Result<PageResult<UserVO>> getUserSearch(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        PageResult<UserVO> userPage = userService.getUserSearch(page, size);
        return Result.success(userPage);
    }
}