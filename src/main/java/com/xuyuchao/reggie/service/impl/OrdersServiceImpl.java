package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.common.BaseContext;
import com.xuyuchao.reggie.entity.*;
import com.xuyuchao.reggie.mapper.OrdersMapper;
import com.xuyuchao.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-10-11:13
 * @Description:
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private AddressBookService addressBookService;

    /**
     * 提交订单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        log.info("orders={}",orders);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setAmount(orders.getAmount());
        //根据用户id获得默认地址信息
        LambdaQueryWrapper<AddressBook> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper1);
        orders.setUserName(BaseContext.getCurrentId().toString());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        //保存订单数据
        this.save(orders);
        //根据用户id查询购物车中的数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list();
        //将购物车数据遍历保存到数据库中
        List<OrderDetail> orderDetails = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(orders.getId());
            return orderDetail;
        }).collect(Collectors.toList());
        //保存订单明细
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        LambdaQueryWrapper<ShoppingCart> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper2);
    }
}
