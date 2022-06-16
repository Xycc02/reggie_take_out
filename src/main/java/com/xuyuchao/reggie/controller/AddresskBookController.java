package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xuyuchao.reggie.common.BaseContext;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.AddressBook;
import com.xuyuchao.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-14-9:01
 * @Description:
 */
@RequestMapping("/addressBook")
@RestController
@Slf4j
public class AddresskBookController {
    @Resource
    private AddressBookService addressBookService;

    /**
     * 保存地址
     * @param addressBook
     * @return
     */
    @PostMapping("")
    public R<String> addAddress(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("添加地址成功!");
    }

    /**
     * 获取当前用户的所有地址信息,返回集合
     * @return
     */
    @GetMapping("/list")
    public R<List> list() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        List<AddressBook> addressBookList = addressBookService.list(queryWrapper);
        return R.success(addressBookList);
    }

    /**
     * 默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public R<String> defaultAddress(@RequestBody AddressBook addressBook) {
        addressBookService.defaultAddress(addressBook);
        return R.success("修改默认地址成功!");
    }

    /**
     * 根据地址id获取地址信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<AddressBook> getAddress(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 保存地址信息
     * @param addressBook
     * @return
     */
    @PutMapping("")
    public R<String> saveAddress(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("保存地址信息成功!");
    }

    /**
     * 删除地址
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public R<String> deleteAddress(Long ids) {
        addressBookService.removeById(ids);
        return R.success("删除地址成功!");
    }

    /**
     * 获取默认地址信息
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }
}
