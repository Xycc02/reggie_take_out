package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.mapper.DishMapper;
import com.xuyuchao.reggie.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:33
 * @Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
