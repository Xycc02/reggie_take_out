package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuyuchao.reggie.common.BaseContext;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.ShoppingCart;
import com.xuyuchao.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-12-16:48
 * @Description:
 */
@RequestMapping("/shoppingCart")
@RestController
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 获取购物车内商品集合
     * @return
     */
    @GetMapping("/list")
    public R<List> cartList() {
        //获取当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        return R.success(shoppingCartList);
    }

    /**
     * 添加购物车
     * @return
     * 第一次加入购物车时,number为1
     * 后面加入了相同商品number自增1
     */
    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //根据 dishId或setmealId 在购物车表中查询是否存在相同id
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId())
                        .eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shopping = shoppingCartService.getOne(queryWrapper);
        //判断该商品是否存在
        if(shopping == null) {
            //若商品不存在则直接保存该商品,且数量赋为1
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }else{
            //商品存在则修改商品number自增1
            LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(ShoppingCart::getNumber, shopping.getNumber()+1)
                            .eq(ShoppingCart::getId,shopping.getId() );
            shoppingCartService.update(updateWrapper);
        }
        return R.success("添加购物车成功!");
    }

    /**
     * 减少购物车商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        //根据dishId或setmealId在购物车表中得到相同数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId() != null,ShoppingCart::getDishId,shoppingCart.getDishId())
                .eq(shoppingCart.getSetmealId() != null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shopping = shoppingCartService.getOne(queryWrapper);

        //判断该商品个数情况
        if(shopping.getNumber() == 1) {
            shoppingCartService.removeById(shopping.getId());
            return R.success("该商品已在购物车移除");
        }
        //number减1
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ShoppingCart::getNumber, shopping.getNumber()-1)
                .eq(ShoppingCart::getId,shopping.getId() );
        shoppingCartService.update(updateWrapper);
        return R.success("删除成功!");
    }

    /**
     * 清空购物车,删除所有商品
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功!");
    }
}
