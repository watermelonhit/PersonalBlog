package com.watermelonhit.blog.controller;

import com.watermelonhit.blog.service.LoginService;
import com.watermelonhit.blog.vo.Result;
import com.watermelonhit.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/26
 */
@RestController
@RequestMapping("login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    /**
     * 用户登录业务
     * @param loginParams
     * @return
     */
    @PostMapping()
    public Result loginUser(@RequestBody LoginParams loginParams){
       return  loginService.loginUser(loginParams);
    }
}
