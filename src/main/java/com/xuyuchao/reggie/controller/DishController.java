package com.xuyuchao.reggie.controller;

import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.dto.DishDto;
import com.xuyuchao.reggie.entity.Dish;
import com.xuyuchao.reggie.entity.DishFlavor;
import com.xuyuchao.reggie.service.DishFlavorService;
import com.xuyuchao.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    /**
     * 添加菜品
     * @param dishDto
     * @return
     */
    @PostMapping("")
    public R<String> save(@RequestBody DishDto dishDto) {

        return null;
    }
}
