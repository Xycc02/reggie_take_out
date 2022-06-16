package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.OrderDetail;
import com.xuyuchao.reggie.mapper.OrdersDetailMapper;
import com.xuyuchao.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-10-11:15
 * @Description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrdersDetailMapper, OrderDetail> implements OrderDetailService {
}
