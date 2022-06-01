package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.DishFlavor;
import com.xuyuchao.reggie.mapper.DishFlavorMapper;
import com.xuyuchao.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:35
 * @Description:
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

    @Resource
    private DishFlavorService dishFlavorService;
    /**
     * 添加菜品并添加菜品所对应口味
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        dishFlavorService.saveBatch(dishDto.getFlavors());
    }
}
