package com.xuyuchao.reggie.common;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-31-14:34
 * @Description:
 */

/**
 * 基于ThreadLocal封装工具类,用户存取当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 存入当前用户id
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
