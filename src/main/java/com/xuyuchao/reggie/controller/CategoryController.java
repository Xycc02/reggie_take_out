package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.Category;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-14:59
 * @Description: 分类管理
 */
@RequestMapping("/category")
@RestController
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 新增菜品或套餐
     * @param category
     * @return
     */
    @PostMapping("")
    public R<String> addCategory(@RequestBody Category category) {
        log.info("category=>{}",category);
        //其中type=1为添加菜品,type=2为添加套餐
        categoryService.save(category);
        return R.success("添加成功!");
    }

    /**
     * 菜品和套餐分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize) {
        log.info("page={},pageSize={},菜品分页...",page,pageSize);
        //1.分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //2.条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //根据菜品更新时间降序排序
        queryWrapper.orderByDesc(Category::getUpdateTime);
        //3.查询菜品
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 菜品套餐修改
     * @param category
     * @return
     */
    @PutMapping("")
    public R<String> update(@RequestBody Category category) {
        log.info("category={}",category);
        //通过id保存菜品或套餐
        categoryService.updateById(category);
        return R.success("保存成功!");
    }

    /**
     * 删除菜品或套餐
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public R<String> delete(@RequestParam String ids) {
        log.info("ids={}",ids);
        categoryService.removeById(ids);
        return R.success("删除成功!");
    }

    /**
     * 获取菜品分类
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> getDishList(@RequestParam(required = false,defaultValue = "0") Integer type) {
        log.info("菜品分类type={}",type);
        if(type == 0) {
            //type为0,即默认值,前端左侧栏展示菜品所有分类
            List<Category> dishList = categoryService.list();
            return R.success(dishList);
        }
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType,type)
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> dishList = categoryService.list(queryWrapper);
        log.info("dishList={}",dishList);
        return R.success(dishList);
    }
}
