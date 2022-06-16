package com.xuyuchao.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuyuchao.reggie.common.R;
import com.xuyuchao.reggie.entity.User;
import com.xuyuchao.reggie.service.UserService;
import com.xuyuchao.reggie.utils.EmailUtils;
import com.xuyuchao.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: xuyuchao
 * @Date: 2022-06-05-13:01
 * @Description:
 */
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        if(StringUtils.isNotEmpty(user.getEmail())) {
            log.info("正在发送验证码...");
            log.info("user=>{}",user);
            //获取邮箱
            String email = user.getEmail();
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //发送验证码
            EmailUtils.sendMessage(email,code);
            //将验证码保存到Session
            httpSession.setAttribute(email,code);
            return R.success("发送成功!");
        }
        return R.error("发送失败!");
    }

    /**
     * 用户登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession) {
        //1.获取邮箱
        String email = (String) map.get("email");
        //2.获取验证码
        String code = (String) map.get("code");
        //3.将获取的验证码与session中的验证码比对
        String _code = (String) httpSession.getAttribute(email);
        if( !code.equals(_code)) {
            return R.error("验证码错误!");
        }
        //4.若该邮箱在数据库中不存在则完成注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        if(userService.getOne(queryWrapper) != null) {
            httpSession.setAttribute("user",userService.getOne(queryWrapper).getId());
            return R.success(userService.getOne(queryWrapper));
        }
        //将该用户保存到数据库中
        User user = new User();
        user.setEmail(email);
        userService.save(user);
        httpSession.setAttribute("user",user.getId());
        return R.success(user);
    }

    /**
     * 退出登录
     * @param httpSession
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession httpSession) {
        //移除登录时的session
        httpSession.removeAttribute("user");
        return R.success("退出登录成功!");
    }
}
