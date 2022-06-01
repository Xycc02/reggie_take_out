package com.xuyuchao.reggie.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xuyuchao.reggie.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-28-18:22
 * @Description:    自动填充,创建和修改时间
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入时策略
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充insertFill");
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "createUser", () -> BaseContext.getCurrentId(), Long.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateUser", () -> BaseContext.getCurrentId(), Long.class); // 起始版本 3.3.3(推荐)
    }

    /**
     * 更新时策略
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充updateFill");
        log.info("updateFill方法线程id=>{}",Thread.currentThread().getId());
        this.strictInsertFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "updateUser", () -> BaseContext.getCurrentId(), Long.class); // 起始版本 3.3.3(推荐)
    }
}
