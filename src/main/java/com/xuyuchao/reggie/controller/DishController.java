package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.Category;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.service.CategoryService;
import com.xuyuchao.reggie.service.DishFlavorService;
import com.xuyuchao.reggie.service.DishService;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-21:37
 * @Description:
 */
@RequestMapping("/dish")
@RestController
@Slf4j
public class DishController {

    @Resource
    private DishService dishService;
    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private CategoryService categoryService;

    /**
     * 添加菜品
     * @param
     * @return
     */
    @PostMapping("")
    public R<String> save(@RequestBody DishDto dishDto) {
        //使用在业务层封装的saveWithFlavor方法,使其可以操作两个表
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品添加成功!");
    }

    /**
     * 菜品分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name) {
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        //分页构造器
        Page<Dish> pageDish = new Page<>(page,pageSize);
        //最后返回返回dto,因为dish类中只有分类id,没有分类名
        Page<DishDto> pageDishDto = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //分页查询,给pageDish赋上值
        dishService.page(pageDish, queryWrapper);

        //对象拷贝,除了records之外的属性全部拷贝过去
        BeanUtils.copyProperties(pageDish,pageDishDto,"records");

        //得到pageDish当前分页中所有的菜品集合
        List<Dish> dishRecords = pageDish.getRecords();

        //遍历dishRecords中的每个菜品,根据每一个菜品分类id查询每个菜品分类名称,并为dishDto中的菜品名称赋上值.(这段需要认真理解,绕)  用到了lambda表达式,stream遍历
        //最后的到pageDishDto中的records属性
        List<DishDto> dishDtoRecords = dishRecords.stream().map((item)->{
            DishDto dishDto = new DishDto();
            //对象拷贝,将dishRecords中每个菜品对应属性都赋给dishDto对象
            BeanUtils.copyProperties(item,dishDto);
            //得到dishRecords每个菜品的分类id
            Long categoryId = item.getCategoryId();
            //根据菜品分类id查找到对应菜品分类
            Category category = categoryService.getById(categoryId);
            //得到菜品分类名称
            String categoryName = category.getName();
            //将菜品名称赋给dishDto中的categoryName
            dishDto.setCategoryName(categoryName);
            //返回dishDto对象
            return dishDto;
        }).collect(Collectors.toList());

        //将上面得到的dishDtoRecords赋给pageDishDto
        pageDishDto.setRecords(dishDtoRecords);
        return R.success(pageDishDto);
    }

    /**
     * 修改菜品回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> queryDishById(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishWithFlavorById(id);
        return R.success(dishDto);
    }

    /**
     * 保存修改
     * @return
     */
    @PutMapping("")
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("disDto={}",dishDto);
        dishService.updateWithFalvor(dishDto);
        return R.success("修改成功!");
    }

    /**
     * 菜品停售,支持批量
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> status0(String ids) {
        log.info("ids={}",ids);
        //将ids以 , 组成集合,方便批量停售
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);

        //将list菜品的status置为0,停售
        list.forEach(item -> {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Dish::getStatus,0 )
                            .eq(Dish::getId,item);
            dishService.update(updateWrapper);
        });
        return R.success("菜品停售成功!");
    }

    /**
     * 菜品起售,支持批量
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> status1(String ids) {
        log.info("ids={}",ids);
        //将ids以 , 组成集合,方便批量起售
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);

        //将list菜品的status置为1,起售
        list.forEach(item -> {
            LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Dish::getStatus,1 )
                    .eq(Dish::getId,item);
            dishService.update(updateWrapper);
        });
        return R.success("菜品起售成功!");
    }

    /**
     * 菜品删除,支持批量
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R<String> delete(String ids) {
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);

        list.forEach(item->{
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getId,item);
            dishService.remove(queryWrapper);
        });

        return R.success("删除成功!");
    }
}
