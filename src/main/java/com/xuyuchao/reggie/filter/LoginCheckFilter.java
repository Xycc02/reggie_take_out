package com.xuyuchao.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.xuyuchao.reggie.common.BaseContext;
import com.xuyuchao.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xuyuchao
 * @Date: 2022-05-25-13:15
 * @Description:    检查用户是否登录过滤器
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter方法线程id=>{}",Thread.currentThread().getId());
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的URI
        log.info("拦截到本次请求的URI: {}",request.getRequestURI());
        String URI = request.getRequestURI();
        //2.判断本次请求是否需要处理
        //定义不需要处理的请求路径
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, URI);
        //3.若不需要处理,直接放行
        if(check) {
            log.info("本次不需要处理的路径: {}",URI);
            filterChain.doFilter(request,response);
            return;
        }
        //4.1.判断登录状态,若已登录,放行
        if(URI.contains("employee")||URI.contains("category")||URI.contains("dish")||URI.contains("setmeal")||URI.contains("order")) {
            if(request.getSession().getAttribute("employee") != null) {
                //将当前用户id存入ThreadLocal,便于MP自动填充
                Long id = (Long) request.getSession().getAttribute("employee");
                BaseContext.setCurrentId(id);

                log.info("用户 {} 已登录,直接放行",request.getSession().getAttribute("employee"));
                filterChain.doFilter(request,response);
                return;
            }
        }else{
            //4.2.判断登录状态,若已登录,放行
            if (request.getSession().getAttribute("user") != null) {
                //将当前用户id存入ThreadLocal,便于MP自动填充
                Long id = (Long) request.getSession().getAttribute("user");
                BaseContext.setCurrentId(id);

                log.info("用户 {} 已登录,直接放行", request.getSession().getAttribute("user"));
                filterChain.doFilter(request, response);
                return;
            }
        }
        //5.若未登录则返回登录结果,通过输出流方式向客户端响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 检查本次请求是否需要放行
     * @param urls
     * @param URI
     * @return
     */
    public boolean check(String[] urls,String URI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, URI);
            if(match) {
                return true;
            }
        }
        return false;
    }
}
