package com.ha.springbootdemo.modules.user.controller;

import com.ha.springbootdemo.common.PageResult;
import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.user.service.UserService;
import com.ha.springbootdemo.modules.user.vo.UserListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/search")
    public Result<PageResult<UserListVO>> getUserSearch(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        PageResult<UserListVO> userPage = userService.getUserSearch(page, size);
        return Result.success(userPage);
    }
}