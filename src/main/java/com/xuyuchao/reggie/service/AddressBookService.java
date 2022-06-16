package com.xuyuchao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuyuchao.reggie.entity.AddressBook;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-11-13:56
 * @Description:
 */
public interface AddressBookService extends IService<AddressBook> {
    //修改默认地址
    public void defaultAddress(AddressBook addressBook);
}
