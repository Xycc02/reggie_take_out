package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuyuchao.reggie.common.BaseContext;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.Orders;
import com.xuyuchao.reggie.service.OrderDetailService;
import com.xuyuchao.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-10-11:16
 * @Description:
 */
@RequestMapping("/order")
@RestController
@Slf4j
public class OrdersController {

    @Resource
    private OrdersService ordersService;
    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 管理端订单分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize) {
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        //分页查询
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        Page<Orders> ordersPage = ordersService.page(pageInfo, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 提交订单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("支付成功!");
    }

    /**
     * 用户订单分页
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize) {
        //分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //分页查询订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        Page<Orders> ordersPage = ordersService.page(pageInfo, queryWrapper);
        return R.success(ordersPage);
    }

    /**
     * 根据id修改订单状态
     * @param orders
     * @return
     */
    @PutMapping("")
    public R<String> changeStatus(@RequestBody Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(orders.getStatus() != null,Orders::getStatus,orders.getStatus())
                .eq(orders.getId() != null,Orders::getId,orders.getId());
        ordersService.update(updateWrapper);
        return R.success("修改状态成功!");
    }
}
