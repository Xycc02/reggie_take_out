package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.dto.SetmealDto;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.entity.Setmeal;
import com.xuyuchao.reggie.service.SetmealDishService;
import com.xuyuchao.reggie.service.SetmealService;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-05-23:22
 * @Description:
 */
@RequestMapping("/setmeal")
@RestController
@Slf4j
public class SetmealController {

    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;

    /**
     * 套餐分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize,String name) {
        log.info("page={},pageSize={}",page,pageSize);
        Page p = setmealService.page(page, pageSize, name);
        return R.success(p);
    }

    /**
     * 保存添加套餐和套餐对应菜品
     * @param setmealDto
     * @return
     */
    @PostMapping("")
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}",setmealDto);
        setmealService.saveSetmealWithDish(setmealDto);
        return R.success("保存套餐成功!");
    }

    /**
     * 套餐修改,通过id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable Long id) {
        log.info("SetmealId={}",id);
        SetmealDto setmealDto = setmealService.getSetmealById(id);
        return R.success(setmealDto);
    }

    /**
     * 保存修改的套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping("")
    public R<String> saveUpdate(@RequestBody SetmealDto setmealDto) {
        log.info("setmealDto={}",setmealDto);
        setmealService.saveUpdate(setmealDto);
        return R.success("保存成功!");
    }

    /**
     * 套餐停售和批量停售
     * @param ids
     * @return
     */
    @PostMapping("/status/0")
    public R<String> status0(String ids) {
        log.info("ids={}",ids);
        String[] split = ids.split(",");
        //获得要停售的套餐id集合
        List<String> setmealIds = Arrays.asList(split);
        //遍历要停售的套餐id集合,将对应套餐状态改为停售0
        setmealIds.forEach(item->{
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Setmeal::getStatus,0)
                    .eq(Setmeal::getId,item);
            setmealService.update(updateWrapper);
        });
        return R.success("状态修改成功!");
    }

    /**
     * 套餐启售和批量停售
     * @param ids
     * @return
     */
    @PostMapping("/status/1")
    public R<String> status1(String ids) {
        log.info("ids={}",ids);
        String[] split = ids.split(",");
        //获得要启售的套餐id集合
        List<String> setmealIds = Arrays.asList(split);
        //遍历要启售的套餐id集合,将对应套餐状态改为停售0
        setmealIds.forEach(item->{
            LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Setmeal::getStatus,1)
                    .eq(Setmeal::getId,item);
            setmealService.update(updateWrapper);
        });
        return R.success("状态修改成功!");
    }

    /**
     * 删除套餐 批量删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public R<String> delete(String ids) {
        String[] split = ids.split(",");
        List<String> list = Arrays.asList(split);
        log.info("list={}",list);
        if(list.size() == 1 && list.indexOf("") == 0) {
            return R.error("请选择删除对象!");
        }
        setmealService.removeByIds(list);
        return R.success("删除成功!");
    }

    /**
     * 获取菜品分类对应的套餐
     * @return
     */
    @GetMapping("/list")
    public R<List> setmealList(Long categoryId,Integer status) {
        log.info("categoryId={},status={}",categoryId,status);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId)
                        .eq(Setmeal::getStatus,status);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);
        return R.success(setmealList);
    }
}
