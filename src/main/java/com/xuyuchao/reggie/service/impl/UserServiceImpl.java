package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.User;
import com.xuyuchao.reggie.mapper.UserMapper;
import com.xuyuchao.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-05-12:44
 * @Description:
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
