package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//请求处理类
@RestController/*这一步是在告诉浏览器这是请求处理类*/
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        System.out.println("Hello world!");
        return "Hello world!喵";
    }
}
