package com.xuyuchao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyuchao.reggie.dto.SetmealDto;
import com.xuyuchao.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-05-23:18
 * @Description:
 */
public interface SetmealService extends IService<Setmeal> {
    //保存添加套餐和套餐对应菜品
    public void saveSetmealWithDish(SetmealDto setmealDto);
    //套餐分页
    public Page page(Integer page, Integer pageSize,String name);
    //套餐修改信息回显,通过套餐id获得套餐信息
    public SetmealDto getSetmealById(Long id);
    //保存修改的套餐信息
    public void saveUpdate(SetmealDto setmealDto);
}
