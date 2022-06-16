package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.dto.SetmealDto;
import com.xuyuchao.reggie.entity.Category;
import com.xuyuchao.reggie.entity.Setmeal;
import com.xuyuchao.reggie.entity.SetmealDish;
import com.xuyuchao.reggie.mapper.SetmealMapper;
import com.xuyuchao.reggie.service.CategoryService;
import com.xuyuchao.reggie.service.SetmealDishService;
import com.xuyuchao.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-05-23:19
 * @Description:
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;

    /**
     * 保存添加套餐和套餐对应菜品
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveSetmealWithDish(SetmealDto setmealDto) {
        //1.保存添加的套餐
        this.save(setmealDto);
        //2.保存套餐中的对应所有菜品到setmeal_dish表,注意套餐中的菜品是以集合形式存储的
        if(!setmealDto.getSetmealDishes().isEmpty()) {
            //得到套餐id,用于作为保存套餐对应菜品的标识
            Long setmealId = setmealDto.getId();
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            //遍历套餐的菜品集合,将每个菜品的setmealId赋上值
            setmealDishes.forEach(item -> {
                item.setSetmealId(setmealId);
            });
            //批量保存套裁对应菜品列表
            setmealDishService.saveBatch(setmealDishes);
        }
    }

    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page page(Integer page, Integer pageSize, String name) {
        //1.分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        //创建dto对象,因为setmeal对象中没有套餐分类名称
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //2.条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        //3.分页查询
        this.page(setmealPage,queryWrapper);

        //对象拷贝,将setmealPage中除了records属性都拷贝到setmealDtoPage对象中
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        //获得setmealPage对象中的records,遍历records,并将套餐名称赋给setmealDto对象中的categoryName属性,并整理为List集合
        List<SetmealDto> setmealDtoList = setmealPage.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            //将setmealPage对象中的records中的每个setmeal对象拷贝给setmealDto
            BeanUtils.copyProperties(item, setmealDto);
            //根据套餐的分类id,获得套餐分类名称
            LambdaQueryWrapper<Category> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Category::getId, item.getCategoryId());
            Category category = categoryService.getOne(queryWrapper1);
            String categoryName = category.getName();
            //将套餐名称赋给每个setmealDto对象中的categoryName属性
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        //将上面整理的List集合放入page对象中
        setmealDtoPage.setRecords(setmealDtoList);

        return setmealDtoPage;
    }

    /**
     * 套餐修改信息回显,通过套餐id获得套餐信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto getSetmealById(Long id) {
        //通过id查询套餐信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        //通过套餐id查询套餐对应菜品信息,并封装到dto对象中
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        //封装到dto对象中
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * 保存修改的套餐信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveUpdate(SetmealDto setmealDto) {
        //1.更新套餐信息
        this.updateById(setmealDto);
        //2.根据套餐id删除该套餐对应的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        //3.保存新的菜品信息
        //保存之前需将套餐id遍历赋给菜品集合的套餐id
        List<SetmealDish> dishes = setmealDto.getSetmealDishes().stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }
}

