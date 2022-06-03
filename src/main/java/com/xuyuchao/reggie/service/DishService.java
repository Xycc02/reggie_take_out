package com.xuyuchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.Dish;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:32
 * @Description:
 */
public interface DishService extends IService<Dish> {
    //添加菜品和对应菜品口味
    void saveWithFlavor(DishDto dishDto);
    //菜品回显表单
    DishDto getDishWithFlavorById(Long id);
    //保存修改菜品以及对应口味
    void updateWithFalvor(DishDto dishDto);
}
