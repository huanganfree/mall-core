package com.ha.springbootdemo.modules.user.controller;

import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.user.dto.UserLoginDTO;
import com.ha.springbootdemo.modules.user.service.AuthService;
import com.ha.springbootdemo.modules.user.vo.MenuVO;
import com.ha.springbootdemo.modules.user.vo.UserDetailVO;
import com.ha.springbootdemo.modules.user.vo.UserLoginVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO loginDTO) {
        UserLoginVO vo = authService.login(loginDTO.getPhone(), loginDTO.getPassword());
        return Result.success(vo);
    }

    //    获取用户详情
    @GetMapping("/userInfo")
    public Result<UserDetailVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserDetailVO userDetailVO = authService.getUserInfo(userId);
        return Result.success(userDetailVO);
    }

    //  通过用户角色获取用户菜单权限
    @GetMapping("/menus")
    public Result<List<MenuVO>> getMenusByUserRole(HttpServletRequest request) {
        Long roleId = (Long) request.getAttribute("roleId");
        List<MenuVO> menulist = authService.getMenusByUserRole(roleId);
        return Result.success(menulist);
    }

}
