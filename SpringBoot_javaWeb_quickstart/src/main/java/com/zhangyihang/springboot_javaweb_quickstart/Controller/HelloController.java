package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//请求处理类
@RestController/*这一步是在告诉浏览器这是请求处理类*/
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        System.out.println("Hello world!");
        return "Hello world!喵";
    }
    @GetMapping("/greet")
    public String greet(@RequestParam("name") String name) {
        return "你好，" + name;
    }
   /* @GetMapping("/introduce")
    public String introduce(
            @RequestParam("name") String username,
            @RequestParam("age") int age) {

        return "你好，我是" + username + "，今年" + age + "岁";
    }*/

   /*@GetMapping("/introduce")
   public String introduce(
           @RequestParam("name") String username,
           @RequestParam(value = "age", defaultValue = "18") int age) {

       return "你好，我是" + username + "，今年" + age + "岁";
   }*/
   @GetMapping("/introduce")
   public String introduce(
           @RequestParam("name") String username,
           @RequestParam(value = "age", required = false) Integer age) {
       /*这里使用包装类因为int不能为空，但是Integer可以*/
       if (age == null) {
           return "你好，我是" + username + "，年龄未填写";
       }

       return "你好，我是" + username + "，今年" + age + "岁";
   }
    @GetMapping("/user-info")
    public String userInfo(User user) {
        return "姓名：" + user.getName() + "，年龄：" + user.getAge();
    }
}
