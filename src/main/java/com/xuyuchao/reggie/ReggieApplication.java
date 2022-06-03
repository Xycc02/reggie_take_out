package com.xuyuchao.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-23-21:05
 * @Description:
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.xuyuchao.reggie.mapper")
@ServletComponentScan
@EnableTransactionManagement//开启事务注解支持
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功...");
    }
}
