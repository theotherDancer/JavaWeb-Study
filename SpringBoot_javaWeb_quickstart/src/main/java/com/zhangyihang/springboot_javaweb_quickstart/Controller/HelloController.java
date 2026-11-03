package com.zhangyihang.springboot_javaweb_quickstart.Controller;

import com.zhangyihang.springboot_javaweb_quickstart.pojo.Address;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.Result;
import com.zhangyihang.springboot_javaweb_quickstart.pojo.User;
import org.springframework.web.bind.annotation.*;

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
   /* @GetMapping("/user-info")
    public String userInfo(User user) {
        return "姓名：" + user.getName() + "，年龄：" + user.getAge();
    }*/
   @GetMapping("/user-info")
   public String userInfo(User user) {
       Address address = user.getAddress();

       if (address == null) {
           return "姓名：" + user.getName() + "，地址未填写";
       }
       System.out.println(address.getCity());     // 武汉
       System.out.println(address.getDistrict()); // 洪山区
       return "姓名：" + user.getName()
               + "，城市：" + address.getCity()
               + "，区县：" + address.getDistrict();

   }
    @GetMapping("/hobbies")
    public String hobbies(@RequestParam("hobby") String[] hobbies) {
        return java.util.Arrays.toString(hobbies);
    }
    @GetMapping("/hobby-list")
    public String hobbyList(
            @RequestParam("hobby") java.util.List<String> hobbies) {

        System.out.println("收到的爱好：" + hobbies);
        return hobbies.toString();
    }
    @GetMapping("/meeting")
    public String meeting(
            @RequestParam("time")
            @org.springframework.format.annotation.DateTimeFormat(
                    pattern = "yyyy-MM-dd HH:mm:ss")
            java.time.LocalDateTime time) {

        return "预约时间：" + time;
    }
    @PostMapping("/user-json")
    public String userJson(@RequestBody User user) {
        Address address = user.getAddress();

        if (address == null) {
            return "姓名：" + user.getName() + "，地址未填写";
        }

        return "姓名：" + user.getName()
                + "，城市：" + address.getCity()
                + "，区县：" + address.getDistrict();
    }
    @GetMapping("/students/{studentId}")
    public String studentDetail(
            @PathVariable("studentId") String id) {

        return "你查询的学号是：" + id;
    }
    @GetMapping("/sample-user")
    public User sampleUser() {
        User user = new User();
        user.setName("小林");
        user.setAge(20);

        return user;
    }
    @GetMapping("/sample-users")
    public Result sampleUsers() {
        User first = new User();
        first.setName("小林");
        first.setAge(20);

        User second = new User();
        second.setName("小周");
        second.setAge(21);

        java.util.List<User> users = new java.util.ArrayList<>();
        users.add(first);
        users.add(second);

        return new Result(true,"查询成功",users);
    }
}
