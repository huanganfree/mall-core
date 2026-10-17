package com.ha.springbootdemo.modules.user.service;

import com.ha.springbootdemo.common.JwtUtils;
import com.ha.springbootdemo.modules.user.entity.Menu;
import com.ha.springbootdemo.modules.user.entity.RoleMenu;
import com.ha.springbootdemo.modules.user.entity.User;
import com.ha.springbootdemo.modules.user.mapper.AuthMapper;
import com.ha.springbootdemo.modules.user.vo.MenuVO;
import com.ha.springbootdemo.modules.user.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthMapper authMapper;

    private final JwtUtils jwtUtils;

    public UserLoginVO login(String phone, String password) {
        User user = authMapper.login(phone, password);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        UserLoginVO vo = new UserLoginVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());

        String token = jwtUtils.generateToken(user.getId(), user.getRoleId(), user.getPhone());
        vo.setToken(token);
        return vo;
    }

    //    数据库的实体类字段转为给前端的vo类
    public List<MenuVO> getMenusByUserRole(Long roleId) {
        List<Menu> menuList = authMapper.getMenusByUserRole(roleId);
        List<MenuVO> menuVOList = menuList.stream().map(menu -> {
            MenuVO menuVO = new MenuVO();
            BeanUtils.copyProperties(menu, menuVO);
            return menuVO;
        }).collect(Collectors.toList());
        return menuVOList;
    }
}
