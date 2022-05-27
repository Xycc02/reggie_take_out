package com.xuyuchao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuyuchao.reggie.entity.Employee;
import com.xuyuchao.reggie.mapper.EmployeeMapper;
import com.xuyuchao.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-23-22:01
 * @Description:
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
