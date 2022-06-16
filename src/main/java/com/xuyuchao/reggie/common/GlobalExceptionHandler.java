package com.xuyuchao.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-28-18:50
 * @Description:
 *
 */
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
        //Duplicate entry 'xuyuchao' for key 'employee.idx_username'
        //判断是否是用户名存在异常
        if(ex.getMessage().contains("Duplicate entry")) {
            //根据空格把字符串隔开
            String[] strs = ex.getMessage().split(" ");
            String res = "帐号 " + strs[2] + " 已存在!";
            return R.error(res);
        }
        return R.error("出现异常!");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public R<String> exceptionHandler(FileNotFoundException ex) {
        log.error(ex.getMessage());
        return R.error("图片文件加载失败!");
    }

    @ExceptionHandler(NullPointerException.class)
    public R<String> exceptionHandler(NullPointerException ex) {
        log.error("空指针异常!" + ex.getMessage());
        return R.error("发生错误");
    }
}
