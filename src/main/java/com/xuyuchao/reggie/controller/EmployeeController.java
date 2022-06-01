package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.Employee;
import com.xuyuchao.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
        //获取当前登录用户id,使用MP的自动填充实现,当前用户id使用ThreadLocal共享到MyMetaObjectHandler类
        // Long id = (Long) request.getSession().getAttribute("employee");
        //设置当前创建和修改的用户
        // employee.setCreateUser(id);
        // employee.setUpdateUser(id);
        employeeService.save(employee);

        return R.success("新增员工成功!");
    }

    /**
     * 员工分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name) {
        log.info("员工分页=> page={},pageSize={},name={}",page,pageSize,name);
        //1.分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        //2.条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件(模糊查询),注意判断前端传过来的name是否为空
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName,name);
        //排序,根据员工更新时间来降序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //3.查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 员工帐号启用和禁用    员工修改
     * @param employee
     * @return
     */
    @PutMapping("")
    public R<String> update(@RequestBody Employee employee,HttpServletRequest request) {
        log.info("update方法线程id=>{}",Thread.currentThread().getId());
        //前端传来要变更的员工id和状态status,其中status已经帮我们转化好要变化的值,我们不需要更改
        log.info("employee.id={},employee.status={}",employee.getId(),employee.getStatus());
        //根据员工id修改员工status,此处因为js和雪花算法的Long类型转换会有精度缺失问题,所以使用了自己配置的消息转换器,将服务端传过去的Long类型等转换为字符串
        // employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改员工成功!");
    }


    /**
     * 修改员工时,根据传过来该员工的id来回显员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> displayEmployee(@PathVariable Long id) {
        //根据id查找员工信息
        log.info("要修改员工id={}",id);
        Employee emp = employeeService.getById(id);
        log.info("emp={}",emp);
        return R.success(emp);
    }
}
