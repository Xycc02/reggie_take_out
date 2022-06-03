package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.entity.DishFlavor;
import com.xuyuchao.reggie.mapper.DishMapper;
import com.xuyuchao.reggie.service.DishFlavorService;
import com.xuyuchao.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:33
 * @Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 添加菜品和对应菜品口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //1.保存菜品
        this.save(dishDto);
        //2.保存dishDto中的菜品口味,注意菜品对应口味是以集合形式存储的
        if(!dishDto.getFlavors().isEmpty()) {
            //3.在插入菜品口味之前,dish_flavor表的dish_id是没有值的,我们在此遍历口味集合为其赋值
            Long dishId = dishDto.getId();//菜品id
            List<DishFlavor> flavors = dishDto.getFlavors();
            flavors.stream().map((item)->{
                item.setDishId(dishId);
                return item;
            }).collect(Collectors.toList());

            dishFlavorService.saveBatch(dishDto.getFlavors());
        }
    }

    /**
     * 修改菜品回显表单
     * @param id
     * @return
     */
    @Override
    public DishDto getDishWithFlavorById(Long id) {
        //创建DishDto对象
        DishDto dishDto = new DishDto();
        //根据菜品id得到菜品
        Dish dish = this.getById(id);
        //赋值对象
        BeanUtils.copyProperties(dish,dishDto);
        //得到菜品id对应菜品的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);

        //将得到的菜品口味集合赋给dishDto的flavors属性
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }

    /**
     * 保存修改菜品以及对应口味
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFalvor(DishDto dishDto) {
        //保存修改菜品
        this.updateById(dishDto);

        //删除当前菜品id对应菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //保存修改菜品口味,遍历菜品口味集合
        dishDto.getFlavors().forEach((item)->{
            //给每个口味对象的菜品id赋值
            item.setDishId(dishDto.getId());
            //保存口味
            dishFlavorService.save(item);
        });
    }
}
