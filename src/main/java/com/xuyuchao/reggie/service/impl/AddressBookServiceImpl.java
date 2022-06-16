package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.AddressBook;
import com.xuyuchao.reggie.mapper.AddressBookMapper;
import com.xuyuchao.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-11-13:57
 * @Description:
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 修改默认地址
     * @param addressBook
     */
    @Override
    @Transactional
    public void defaultAddress(AddressBook addressBook) {
        //1.先将所有的地址默认地址字段is_default改为0
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(AddressBook::getIsDefault,0);
        this.update(wrapper);
        //2.将该id的地址设为默认地址
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(addressBook.getId() != null,AddressBook::getIsDefault,1)
                .eq(AddressBook::getId,addressBook.getId());
        this.update(updateWrapper);
    }
}
