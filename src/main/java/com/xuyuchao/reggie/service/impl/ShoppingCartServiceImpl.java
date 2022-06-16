package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.ShoppingCart;
import com.xuyuchao.reggie.mapper.ShoppingCartMapper;
import com.xuyuchao.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-12-16:46
 * @Description:
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
