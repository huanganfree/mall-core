package com.ha.springbootdemo.modules.user.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ha.springbootdemo.common.PageResult;
import com.ha.springbootdemo.modules.user.mapper.UserMapper;
import com.ha.springbootdemo.modules.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserMapper userMapper;

    public PageResult<UserVO> getUserSearch(int page, int size) {
        try (Page<Object> ignored = PageHelper.startPage(page, size)) {
            List<UserVO> userList = userMapper.getUserSearch().stream().map(user -> {
                UserVO vo = new UserVO();
                BeanUtils.copyProperties(user, vo);
                vo.setStatusText(user.getStatus() == 1 ? "启用" : "禁用");
                return vo;
            }).collect(Collectors.toList());
            PageInfo<UserVO> pageInfo = new PageInfo<>(userList);
            return PageResult.success(userList, pageInfo.getTotal());
        }
    }
}
