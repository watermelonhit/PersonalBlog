package com.watermelonhit.blog.config;

import com.watermelonhit.blog.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author watermelonhit
 * @DateTime 2021/8/25
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:9014","112.74.190.84");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //检查是否已经登录
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/test")
                .addPathPatterns("/articles/publish")
                .addPathPatterns("/updateInfo");
    }
}
