package com.watermelonhit.blog.controller;

import com.alibaba.fastjson.JSON;
import com.watermelonhit.blog.util.UserThreadLocal;
import com.watermelonhit.blog.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**专门用来测试用的
 * @Author watermelonhit
 * @DateTime 2021/8/27
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public Result test1(){
        return Result.success("test通过！");
    }
}
