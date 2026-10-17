package com.ha.springbootdemo.modules.user.mapper;

import com.ha.springbootdemo.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getUserSearch();

}