package com.xuyuchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.DishFlavor;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:34
 * @Description:
 */
public interface DishFlavorService extends IService<DishFlavor> {
    //保存菜品并保存菜品对应口味,需要操作两张表dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);
}
