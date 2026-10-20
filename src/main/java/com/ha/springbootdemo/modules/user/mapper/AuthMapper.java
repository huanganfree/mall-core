package com.ha.springbootdemo.modules.user.mapper;

import com.ha.springbootdemo.modules.user.entity.Menu;
import com.ha.springbootdemo.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {
    User login(String phone, String password);

    List<Menu> getMenusByUserRole(Long roleId);

    User getUserInfo(Long userId);
}
