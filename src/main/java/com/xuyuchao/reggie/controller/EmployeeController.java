package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.Employee;
import com.xuyuchao.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-23-22:05
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.根据页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查到则返回登陆失败结果
        if(emp == null) {
            return R.error("用户名不存在!");
        }
        //4.密码比对,如果不一致则返回登陆失败结果
        if(!emp.getPassword().equals(password)) {
            return R.error("密码错误,请重新输入!");
        }
        //5.查看员工状态,若为禁用,则返回员工禁用结果
        if(emp.getStatus() != 1) {
            return R.error("该帐号已被禁用!");
        }
        //6.登录成功
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //移除Session
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }

    /**
     * 添加员工
     * @return
     */
    @PostMapping("")
    public R<String> addEmployee(@RequestBody Employee employee,HttpServletRequest request) {
        //由于数据库中username字段添加了唯一约束,故我们不做此处理,而是配置全局异常处理器
        // //1.查询数据库是否存在用户名
        // LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(Employee::getUsername,employee.getUsername());
        // Employee emp = employeeService.getOne(queryWrapper);
        // if(emp != null) {
        //     return R.error("该帐号已存在!");
        // }
        //2.保存至数据库
        //设置初始密码123456,并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //获取当前登录用户id
        Long id = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(id);
        employee.setUpdateUser(id);
        employeeService.save(employee);

        return R.success("新增员工成功!");
    }

}
