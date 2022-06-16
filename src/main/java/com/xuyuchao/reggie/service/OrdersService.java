package com.xuyuchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyuchao.reggie.entity.Orders;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-10-11:12
 * @Description:
 */
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
