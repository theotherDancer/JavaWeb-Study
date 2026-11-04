# JavaWeb 学习记录

从“浏览器怎样调用我写的 Java 方法”出发，整理 AI 全栈成长营 Level 2「JavaWeb 核心技术」的学习过程。这里既保存进度，也把学习中真正困惑过的问题讲清楚，供以后回看。

不要求一开始记住所有框架名字。先抓住主线：**Maven 准备项目依赖，Spring Boot 帮助启动应用，Tomcat 接收请求，Spring MVC 安排调用我们写的方法。**

> 仓库记录起始：2026-10-28（Maven 历史记录）；JavaWeb 正式记录起始：2026-10-29。
> 最近更新：2026-11-04。后续按实际学习日期持续追加；日期表示学习时间，补录或复制日期另行标明。

> 示例说明：本文中的人名、学号均已改为示例化表述，仅用于解释代码与请求格式，不保证与本机数据库记录对应。运行验收只记录条数、字段及行为，不公开数据库名单原值。

## 日期导航与代码目录

| 学习日期 | 主题 | 笔记与代码 |
|---|---|---|
| 2026-10-28 | Maven 环境、依赖、POM 和下载故障排查 | [原记录副本](notes/2026-10-28/maven.md) · [原项目配置](MavenStudy/pom.xml) |
| 2026-10-29 | JavaWeb 入门、请求响应和 Servlet 基础 | [当天记录](#2026-10-29web-入门与请求处理链路) · [Spring Boot 工程](SpringBoot_javaWeb_quickstart) |
| 2026-11-02 | Postman、简单参数、实体参数、默认值与可选参数 | [当天问答总结](#day-2026-11-02) · [HelloController](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/Controller/HelloController.java) · [User](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/pojo/User.java) |
| 2026-11-03 | 嵌套对象、数组/List、日期/JSON/路径参数、响应包装、JDBC 接口整合 | [当天总结](#day-2026-11-03) · [接口清单](#endpoints-2026-11-03) · [完整流程图](#flow-2026-11-03) |
| 2026-11-04 | 三层架构、接口解耦、IOC/DI、真实/模拟查询切换与验证 | [当天问答总结](#day-2026-11-04) · [分层与注入流程图](#flow-2026-11-04) · [AI Coding 与验收](#ai-coding-2026-11-04) |


10 月 28 日的 Maven 内容原先归档在 Database-Basics，已于 10 月 29 日复制到本仓库，保留原始来源和日期。今后 JavaWeb 及其 Maven 前置学习统一在这里接续记录；数据库仓库中的历史内容保留。

### 如何查看和运行代码

- [SpringBoot_javaWeb_quickstart](SpringBoot_javaWeb_quickstart)：当前本机工程快照，最近同步日期 2026-11-04，包含 POM、启动类、Controller、资源配置、生成的测试类和 Maven Wrapper 文件。
- IDEA 打开该子目录的 pom.xml 加载工程；运行 SpringBootJavaWebQuickstartApplication 启动类，再访问 http://localhost:8080/hello。
- 当前 POM 为 Spring Boot 4.1.2、目标 Java 17，使用 spring-boot-starter-webmvc；本机学习时选用 JDK 25。可继续使用 IDEA 配置的 Maven。Wrapper 自身配置了发行版，不等于此前 IDEA 的内置 Maven 版本。
- [HelloController.java](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/Controller/HelloController.java) 当前返回 `Hello world!喵`。下文 `Hello world!` 是讲解时的示例，二者不矛盾。
- 源码保留原貌。其中旧注释“告诉浏览器这是请求处理类”不准确，应理解为“告诉 Spring 这个类用于处理请求”；以下概念说明以此为准。
- 生成的 contextLoads 测试随工程保存，本次未运行该测试。/greet、/introduce、/user-info 已保存到源码，学习者反馈相应 Postman/浏览器测试成功。/introduce 当前生效的是可选年龄版本，必填与默认 18 的旧版本保留为注释。
- 真实学生查询：确保本机 MySQL 的 campus_learning.student 已存在（需要 student_id、name 列），在 Spring Boot 启动类运行配置中设置 DB_PASSWORD，再请求 GET /students。账号 root、localhost:3306 为本次学习环境，不应直接照搬到其他环境；不提交密码、运行配置或数据库物理文件。
- 当前 GET /students 采用 Controller → Service → 静态 DAO → MySQL；StudentService 启用 @Service，MockStudentService 的 @Service 已注释。切换实现后需重新构建/启动并检查响应内容，不只看 200。
- Service 代码：[查询接口](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/service/StudentQueryService.java) · [数据库实现](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/service/StudentService.java) · [模拟实现](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/service/MockStudentService.java)。
- 新增对象/查询类：[Address](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/pojo/Address.java)、[Result](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/pojo/Result.java)、[Student](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/pojo/Student.java)、[StudentDao](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/dao/StudentDao.java)、[StudentController](SpringBoot_javaWeb_quickstart/src/main/java/com/zhangyihang/springboot_javaweb_quickstart/Controller/StudentController.java)。
- [MavenStudy](MavenStudy)：2026-10-28 从数据库仓库复制的历史快照，只有 pom.xml，包含 Logback 依赖；不要把它当作当前本机 MavenStudy 的完整镜像。
- 两个子目录是独立工程，不是已配置好的 Maven 父子模块。根目录 README 是学习入口。
- 本次未上传 IDEA 私有配置、target、依赖缓存或密码；只归档学习源码与必要构建文件。

## 当前进度

- 最近学习：2026-11-04，对应网课 Day05-09～13 的三层架构、分层解耦及 IOC/DI 基础；已做指导实践，不代表相关细节全部掌握。
- 已完成请求响应基础：简单与实体参数、嵌套对象、数组/List、日期/JSON/路径参数、对象/List 响应及 Result 包装。
- 当前结构：Controller 接收请求、包装响应；Service 组织查询并管理本例 JDBC 连接；DAO 执行 SQL、读取结果。
- 解耦实践：Controller 依赖 StudentQueryService 接口；Spring 通过唯一构造方法注入实现对象。已体验数据库与模拟实现切换，当前恢复数据库实现。
- 当日运行证据：Service 源文件 21:23:32 修改，8080 对应 Java 进程 21:35:57 启动；随后实际 GET /students 返回 HTTP 200、success=true 和 5 条非模拟学生记录。未逐条对照数据库原始记录，不扩大为异常/并发或全部接口验收通过。
- 当前仅查询全部学生；GET /students/{studentId} 仍只回显学号，真正按学号查库尚未实现。
- 掌握边界：能在指导下改造和测试、解释基本调用链；独立拆需求和完整编码仍需练习。DAO 仍是静态调用；统一异常处理、连接池、多实现选择等尚未实践。
- 学习方式：围绕实际小功能推进，并加入如何指挥 AI、限定改动、核验运行版本和验收结果。
- 下一步：按学号真实查询，分别验证查到、未找到与查询失败，沿用现有三层结构。
- 会话管理、文件上传下载、前端页面与联调仍待正式学习；签到程序仍为讨论。题外交流不记为 JavaWeb 核心进度。
- 历史记录保留各自日期的代码状态；旧章节中的“当前/尚未”请结合日期阅读，最新状态以本节和 11 月 4 日章节为准。

## 学习路线

| 目录板块 | 内容 | 状态 |
|---|---|---|
| Web 应用概览 | HTTP、Tomcat、创建与访问应用 | 已完成首轮入门，持续巩固 |
| Servlet 核心技术 | 概述、生命周期、配置、请求响应、乱码、ServletContext | 已理解调用关系和生命周期；当前借助 Spring MVC 实践请求参数，其余未完成 |
| 分层与 Spring 基础 | Controller/Service/DAO、接口解耦、IOC、构造方法 DI | 2026-11-04 已完成首轮指导实践；多实现选择等待巩固 |
| 会话状态管理 | Cookie、Session、登录状态保持 | 待正式学习 |
| 文件上传与下载 | 上传与下载实操 | 待学习 |
| 前后端分离技术 | MVC、JSON、Axios、跨域 | 待学习 |

目前借助 Spring Boot 搭建可运行的 Web 示例；这不代表已经完成原目录 Level 3 的 Spring Boot 系统学习。

## 知识点索引

- [日期导航与代码目录](#日期导航与代码目录)
- [2026-10-28 Maven 原始记录副本](notes/2026-10-28/maven.md)
- [工具和框架的关系](#工具和框架的关系)
- [请求如何到达自己的方法](#请求如何到达自己的方法)
- [控制器和注解](#控制器和注解)
- [HTTP 请求与响应](#http-请求与响应)
- [Servlet 生命周期](#servlet-生命周期)
- [2026-10-29 思维导图](#今天的思维导图)
- [Postman、简单参数与实体参数问答](#day-2026-11-02)
- [2026-11-02 参数接收思维导图](#params-map-2026-11-02)
- [拓展了解：为什么登录一次后不用重复输入密码](#拓展了解为什么登录一次后不用重复输入密码)
- [11 月 3 日：参数、响应、JDBC 问答总结](#day-2026-11-03)
- [当前接口清单与调用示例](#endpoints-2026-11-03)
- [HTTP 与 JDBC 完整流程图](#flow-2026-11-03)
- [11 月 4 日：三层架构与分层解耦问答](#day-2026-11-04)
- [Spring 创建对象与构造方法注入流程图](#flow-2026-11-04)
- [AI Coding 提示词与证据验收](#ai-coding-2026-11-04)
- [每日学习记录](#每日学习记录)

## 工具和框架的关系

| 名称 | 作用 |
|---|---|
| Java | 编写类、接口和业务方法的语言 |
| JDK | 提供 Java 编译和运行工具 |
| Maven | 根据 pom.xml 管理依赖，组织编译、测试和打包 |
| Spring Framework | 第三方 Java 框架，不是 JDK 自带功能 |
| Spring MVC | Spring 中负责 Web 请求处理的框架 |
| Spring Boot | 简化 Spring 应用配置和启动 |
| Tomcat | 接收网络连接、解析 HTTP 请求、调用 Servlet、发送响应 |

Maven 可以用于没有 Spring Boot 的项目；Spring Boot 也可以使用其他构建工具。

### Maven 和 Spring Boot 都像“帮我准备项目”，区别在哪里？

可以把开发应用想成开餐厅：Maven 按清单采购设备、组织备料和打包；Spring Boot 则帮助把厨房配置好并启动营业。采购工具本身不接单做菜，厨房方案也不代替我们的业务代码。

例如，以前在 JdbcStudy 中声明 MySQL 驱动，Maven 就负责准备它；当时没有 Spring Boot，Maven 仍然能工作。现在声明 Spring Boot 的 Web 依赖，Maven 负责下载相关库，启动应用时才由 Spring Boot 的配置机制把相关组件组织起来。

```text
pom.xml 写需要什么 → Maven 准备依赖
运行启动类        → Spring Boot 配置并启动应用
浏览器访问接口    → Web 组件和我们写的代码处理请求
```

### Spring MVC 属于 Java 吗？

它是**用 Java 编写的第三方框架代码**，属于 Spring Framework 的一部分，不属于 Java 语言或 JDK 自带的类库。引入依赖后，这些框架代码与我们自己的 Java 代码一起运行。

### 还需要单独下载 Tomcat 吗？

当前采用内嵌 Tomcat 的 Spring Boot Web 项目，不需要另行安装独立 Tomcat。相关库通过项目依赖引入，应用启动时一起启动，应用停止时一起停止。不能把它理解成 IDEA 或 JDK 内置了 Tomcat。

本次使用 JDK 25，项目 java.version 设置为 17：前者是实际工具，后者在 Spring Boot 默认构建配置下指定目标 Java 标准。修改 java.version 不会自动安装或切换 JDK。

这就像使用新版文档编辑器，按旧版兼容要求保存：工具可以更新，但项目不能因此随意使用超出目标版本的语法和标准 API；依赖也需要满足相应运行环境要求。

## 请求如何到达自己的方法

```mermaid
flowchart TD
    A[浏览器 GET /hello] --> B[Tomcat 接收并解析请求]
    B --> C[已注册的 DispatcherServlet]
    C --> D[Spring MVC 查找并安排调用处理方法]
    D --> E[HelloController.hello]
    E --> F[框架处理返回值]
    F --> G[Tomcat 发送响应]
    G --> H[浏览器接收响应]
```

- Servlet 是接口；DispatcherServlet 是 Spring MVC 提供的类，间接实现 Servlet 接口。
- 启动过程中 DispatcherServlet 注册给 Tomcat，并建立 Servlet 路径映射。
- Spring MVC 识别控制器方法上的映射；请求到来后，按预先建立的关系处理。
- 所谓“唤醒”实际是处理线程调用对象的方法，不是类自动执行。

### Tomcat 已经能处理 HTTP，为什么还需要 DispatcherServlet？

Tomcat 知道如何接收网络连接、解析 HTTP、按 Servlet 标准调用对象，但不会单凭我们写了一个 hello() 就自动认识它。`@RequestMapping` 等映射规则由 Spring MVC 识别。

DispatcherServlet 是 Spring MVC 的统一入口，协调路径匹配、方法调用和返回结果处理等组件。可以把 Tomcat 想成接待入口，DispatcherServlet 是分诊台，Controller 方法则负责具体业务。

### 都是 Java 代码，为什么 DispatcherServlet 先被调用？

因为**启动时提前登记了不同层次的映射**，不是它天然比我们的类“更靠近 Java 内部”。

```text
Tomcat 的登记：匹配 Servlet 映射的请求 → DispatcherServlet
Spring MVC 的登记：/hello → HelloController 的 hello() 方法
```

请求来到后，Tomcat 按登记关系调用 DispatcherServlet 的 service()，传入请求和响应对象；再由 Spring MVC 的处理机制找到并安排调用我们的 hello()。

### Servlet 是接口，为什么它还能做这些事情？

接口只规定契约，不会自动获得“查找方法”的能力。`Servlet` 和 JavaSE 中学过的接口使用同一种机制；`DispatcherServlet` 是通过继承间接实现它的具体类。查找、调用、转换等功能由框架的实现代码和协作组件完成。

## 控制器和注解

```java
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("Hello world!");
        return "Hello world!";
    }
}
```

- HelloController 是自己创建的 Java 类，承担请求处理职责，因此称为控制器。
- 类名包含 Controller 只是命名习惯，不会使它自动生效。
- @RestController 标记控制器，方法返回值用作响应内容；类需被 Spring 扫描识别。
- @RequestMapping 建立请求映射；此例未限定 HTTP 方法。
- @GetMapping 只匹配 GET；@PostMapping 只匹配 POST。
- println 输出到服务端控制台；return 的结果经过框架处理后成为响应内容。

### HelloController 到底是普通类，还是 Controller 类？

**两者说的是同一个类。** HelloController 是我们创建的 Java 类；“控制器”是按职责对它的称呼，就像称 Student 为“学生类”。没有因此多出另一个叫 Controller 的对象，也不要求它继承一个叫 Controller 的类。

`@RestController` 是注解标记，不是一次方法调用。Spring 识别这个类后，将它作为控制器管理。类名换成其他合法名字，配套引用调整好后也可以承担同样职责。

### 注解为什么一个写在类外，一个写在类里？

因为修饰的对象不同。注解写在要修饰的声明上方：`@RestController` 标记类，方法上的 `@RequestMapping` 标记方法。

`@RequestMapping` 也可以写在类上，设置共享路径前缀；不是规定它只能写在类里面。

### 普通方法不行吗，为什么要控制器？

普通方法当然能用，hello() 本来就是普通 Java 方法。但浏览器发送的是 HTTP 请求，不会直接执行 Java 的 `对象.hello()`。

框架通过控制器和映射把网络请求接到 Java 方法上。辅助方法仍然可以按普通方式调用；并非每个方法都需要变成网络接口。

## HTTP 请求与响应

- URL 是资源地址，例如 http://localhost:8080/hello。
- localhost 表示本机，8080 是端口，/hello 是路径。
- GET 通常用于获取数据，条件常放在 URL 中。
- POST 通常用于提交处理数据，数据常放在请求体；URL 也能带查询参数。
- POST 不会自动加密；传输加密依赖 HTTPS。
- 状态码表示处理情况，响应体装实际内容。返回文字“失败”不会自动改变状态码。

### URL 和参数是什么关系？

```text
http://localhost:8080/students?studentId=DEMO001
│      │         │    │        └─ 查询参数：指定学号
│      │         │    └─ 请求路径
│      │         └─ 端口
│      └─ 主机：自己的电脑
└─ 通信协议
```

### GET 就是地址栏，POST 就是请求体吗？

这是常见用法，但不能画等号。GET、POST 是请求方式；URL 查询参数、请求体是携带数据的位置。浏览器地址栏输入地址并回车通常发送 GET，POST 也可以在 URL 中携带参数。POST 不因为数据没有显示在地址栏就变得加密。

| 状态码 | 含义 |
|---|---|
| 200 | 请求成功 |
| 201 | 创建成功 |
| 400 | 请求不符合要求 |
| 401 | 缺少有效认证 |
| 403 | 拒绝访问 |
| 404 | 找不到目标资源 |
| 405 | 不支持该请求方式 |
| 500 | 服务器内部错误 |

## Servlet 生命周期

| 方法 | 职责 | 频率 |
|---|---|---|
| init | 初始化准备 | 通常每个实例一次 |
| service | 处理交给该 Servlet 的请求 | 每次请求调用 |
| destroy | 移出服务时清理 | 正常销毁实例时调用 |

初始化可以发生在启动时或首次使用时。通常复用 Servlet 实例；三次请求交给同一个实例，service 对应执行三次，不代表初始化三次。

就像员工入职准备一次，之后接待多次，最后办理离职交接。生命周期描述的是 Servlet 实例的工作过程，不是每个用户各自的登录过程。

## 今天的思维导图

### 工具与框架：谁帮我做什么？

```mermaid
mindmap
  root((开发工具与框架))
    Java与JDK
      Java是编程语言和平台
      JDK提供编译和运行工具
      JDK25是工具版本
      目标Java17限制项目使用标准
    Maven
      按pom.xml准备依赖
      处理依赖传递
      组织编译测试和打包
      不代替业务代码
    Spring体系
      Spring是第三方Java框架
      Spring MVC属于Spring
        处理Web请求
        协调参数与返回值处理
      Spring Boot
        简化配置和启动
        可启动内嵌Tomcat
```

### 一次请求：谁先收到，谁具体执行？

```mermaid
mindmap
  root((浏览器访问hello))
    启动时
      DispatcherServlet注册给Tomcat
      Spring MVC识别控制器和映射
    浏览器发送
      URL指定地址与路径
      GET指定请求方式
    Tomcat
      接收网络连接
      解析HTTP请求
      按登记信息调用Servlet
    DispatcherServlet
      是类而不是接口
      间接实现Servlet标准
      协调Spring MVC处理机制
    HelloController
      是我们创建的类
      控制器是职责名称
      RestController标记类
      RequestMapping映射请求
      hello方法执行具体逻辑
    返回结果
      println到IDEA控制台
      return交给框架处理
      Tomcat发送响应
      浏览器收到状态码与响应体
```

### Servlet：接口和对象生命周期

```mermaid
mindmap
  root((Servlet基础))
    身份关系
      Servlet是标准接口
      DispatcherServlet是框架提供的实现类
      Controller是自己写的请求处理类
    初始化init
      准备工作
      通常每个实例一次
      可在启动时或首次使用时发生
    服务service
      请求交给该Servlet时调用
      同一个实例可以反复处理请求
      三个请求对应三次调用
    销毁destroy
      正常移出服务时清理
      例如应用正常关闭
```

## 拓展了解：为什么登录一次后不用重复输入密码

**本节只是回答学习过程中顺带提出的问题，不计入今天正式完成内容，不要求当前掌握。** 会话管理和并发问题以后再系统练习。

常见 Cookie + Session 方案中，登录成功后，服务器通过 Session 保存登录信息，浏览器通过 Cookie 保存对应的 Session ID；后续请求自动携带符合条件的 Cookie，服务器据此找到并验证会话。

这不是因为复用了同一个 Servlet。Servlet 像很多人共用的服务窗口，凭证才用于关联身份。凭证过期、退出登录、清除 Cookie 后可能需要重新登录；长期“记住我”也可能使用额外凭证。浏览器自动填写密码是另一种功能。

Cookie 还可以保存语言偏好、关联购物车，并非只用于登录。不应把明文密码当成普通 Cookie 数据保存。

同一 Servlet 实例可能并发处理多人的请求，因此不能随意用实例字段保存“当前用户名”；请求临时数据优先放局部变量。这一点仅作提醒，暂不展开线程安全知识。

## 每日学习记录

### 2026-10-29：Web 入门与请求处理链路

**实践与证据**

- 创建 Spring Boot Web 项目，编写 HelloController。
- 浏览器访问 /hello，在 Network 面板确认 GET 和 200 OK。
- 理解控制台打印与返回浏览器内容的区别。

**今天的理解**

- Maven 准备依赖和构建项目，Spring Boot 简化配置和启动；两者不是同一种工具。
- Tomcat 与 Spring MVC 分工协作；DispatcherServlet 是注册给容器的入口。
- Controller 是自己创建的类所承担的职责，注解标记用途与请求映射。
- Servlet 接口使用的仍是 JavaSE 中学过的接口机制。

**易混淆点**

- 不把 DispatcherServlet 当成接口：接口叫 Servlet。
- 不把 Spring MVC 当成 JDK 内置功能：它是第三方 Java 框架。
- 不把“都在 Java 程序里”当成自动调用原因：需要注册、映射和实际方法调用。
- 不把内嵌 Tomcat 理解为 IDEA 或 JDK 内置：它随项目依赖引入。

**仅作了解**

Cookie、Session、登录保持和多用户并发数据问题，本日不作为正式完成内容。

**下次接续**

在现有项目中实践 /greet?name=示例甲，学习 @RequestParam 如何接收参数；尚未确认该示例完成。


<a id="day-2026-11-02"></a>

### 2026-11-02：从 Postman 到 Java 参数，弄清数据究竟交给谁

#### 白天：了解路线和准备工具

- 查看 AIFoundry 的公开课程介绍：AI 原生开发、全栈、AI 应用/RAG、Agent 四阶段，主要是 TypeScript/Next.js、PostgreSQL/Prisma 路线，与原 Java/Spring Boot/MySQL 路线不同；今天只了解目录，不记为已完成课程任务。
- 本机检查：Node.js v24.22.1、npm 11.19.1，node 命令位置 D:/node.exe，已经可用，没有新建 Node 项目。
- 下载官方 Windows x64 Postman 12.34.4，安装包保留在 D:/Postman；验证官方数字签名，创建桌面快捷方式并修正白色图标。安装目录受 Windows 应用重定向影响，与安装包位置不同，不将个人安装目录作为项目运行要求。
- 随后继续原 JavaWeb 网课，停在 Day05-03 的简单参数、实体参数。签到项目仍是讨论，不算开发成果。

#### Postman 和浏览器不是同一个接收窗口

Postman 是发送请求、检查响应的客户端工具，不是 Java 服务端，也不代替 IDEA。当前没有编写前端页面，Postman 在代替它测试接口。

```text
运行启动类 → Spring Boot 启动应用并识别控制器
Postman 发 GET /hello → 服务器返回给 Postman
浏览器发 GET /hello  → 服务器返回给浏览器
```

不单独运行 HelloController；它由框架在请求到来时调用。应用正常运行期间可以反复接收请求，不需要每次重新启动；修改代码后则需要确认运行中的应用已加载新代码，本次练习采用重启。

浏览器地址栏也能测试简单 GET。Postman 的价值在于方便选择请求方式、填写请求头和请求体、保存并重复测试请求；POST、JSON 等功能的实际练习还在后面。

#### 同样传 name、age，为什么会调用不同的方法？

先按路径和请求方式等映射条件选择处理方法，再绑定数据，不是看参数名字猜方法。

| 请求 | 对应方法 | 当前接收方式 |
|---|---|---|
| GET /greet?name=示例甲 | greet | 单个必填字符串 |
| GET /introduce?name=示例甲&age=20 | introduce | 简单参数分别接收 |
| GET /user-info?name=示例甲&age=20 | userInfo | User 对象接收 |

`/hello` 保留原来的 @RequestMapping，未限制为 GET；以上三个新增方法采用 @GetMapping。

#### 简单参数：请求参数名与 Java 形参名不必相同

```java
@RequestParam("name") String username
```

```text
请求里的 name → 按注解名字找到“示例甲” → 交给形参 username
```

可改名的是 Java 形参；请求要按注解指定的 name 传。只传 username 不等于提供了必需的 name。请求参数顺序可以交换，绑定仍然按名字进行。

请求把 age=20 作为文本形式传入，Spring 尝试转换成目标 int/Integer；age=abc 无法转换时，正常默认处理返回 400，不进入对应方法体。值被成功接收不代表业务校验也完成：例如当前方法尚未限制年龄范围。

#### 必填、默认值和“没填就是未知”

| 接收形式 | 没传 age | age=20 | age=abc |
|---|---|---|---|
| @RequestParam("age") int age | 默认必填，通常 400 | 20 | 通常 400 |
| defaultValue="18"，类型 int | 18；空值也使用默认值 | 20 | 通常 400 |
| required=false，类型 Integer | null | 20 | 通常 400 |

默认值不是非法输入的兜底替换。Integer 可以为 null，int 不能。对“年龄未知”的场景，不应随意用 18 或 0 假装已知。

当前实际生效的代码是可选版本：

```java
@GetMapping("/introduce")
public String introduce(
        @RequestParam("name") String username,
        @RequestParam(value = "age", required = false) Integer age) {
    if (age == null) {
        return "你好，我是" + username + "，年龄未填写";
    }
    return "你好，我是" + username + "，今年" + age + "岁";
}
```

必填版、默认 18 版已测试后被注释保存，不能说三种 /introduce 同时存在并运行。

#### 实体参数：声明一个 User，Spring 帮忙装数据

User 是自己写的普通 Java 类，pojo 只是包名习惯。它保存 name、Integer age，提供 getter/setter。

```java
@GetMapping("/user-info")
public String userInfo(User user) {
    return "姓名：" + user.getName() + "，年龄：" + user.getAge();
}
```

在当前默认构造和属性绑定方式下，可理解为以下过程；这是说明性示意，不是框架源码：

```java
User user = new User();
user.setName("示例甲");
user.setAge(20);
userInfo(user);
```

- 方法形参 user 可以改为 student，但请求仍传 name、age。
- 请求名匹配的是可绑定属性，对应 setName、setAge，不是 User 变量的名字。
- 不需要传 user.name、user.age 来接收这两个顶层属性。
- 未提供 age 时，当前 User 的 Integer age 保持 null；如果字段用 int，其默认值是 0。
- 对象创建发生在后端，不自动返回页面，也不自动保存数据库；客户端只能收到处理方法实际返回的结果。

#### 两种“默认值”为何看起来一样，实际不同？

| 对比 | @RequestParam 的 defaultValue | User 空参构造赋初始值 |
|---|---|---|
| 发生环节 | Spring 接收对应请求参数时 | 通过该构造方法创建对象时 |
| 得到值的位置 | 对应方法形参 | 对象字段 |
| 普通 Java 调用 | 不会自动执行请求参数默认规则 | new User() 会执行构造逻辑 |
| 后续请求绑定 | 已确定传入该形参的值 | setter 可能覆盖对象初始值 |

例如在构造中写 this.age=18，没传 age 时通常保留 18；传 age=20 后会覆盖成 20。传 age= 与完全不传也不能简单等同：默认绑定下空值可能转换成 null 并覆盖初始值。

只写空构造而不赋值，不会把年龄变成 18。当前 User 没有显式构造方法，Java 隐式提供空参构造；本轮源码尚未实现构造默认值，相关问答不能计为已完成实验。

#### 多个客户端同时访问，会混参数吗？

每次方法调用的形参和局部变量各自独立，浏览器的示例甲与 Postman 的示例乙分别处理、分别返回。通常请求由服务器工作线程并发处理，完成顺序不保证等同发送顺序。

Controller 通常是单例，实例字段可能共享，不应随意把“当前姓名”存进字段。这是概念提醒，本日未进行多线程实验，不记为系统掌握并发。

#### 今天实际踩过的坑：Params 重复添加

实际出现过：

```text
/introduce?name=示例甲&age=&name=示例甲&age=20
```

姓名在响应中重复，年龄却显示未填写。不能假设后一个同名参数总覆盖前一个；简单值、数组等目标类型会影响转换。

Postman 地址栏与 Params 表格是同步的同一份数据，不要两边各加一遍。删除重复行，只留 name 和 age；测试缺失年龄时取消勾选 age，不要把“空值”当作所有场景下都等于“未传”。

#### HTTP 状态码回顾

| 状态码 | 本轮理解 |
|---|---|
| 200 | 请求成功处理 |
| 400 | 参数缺失、无法按要求转换等请求错误 |
| 401 | 缺少有效身份认证 |
| 403 | 拒绝访问 |
| 404 | 找不到目标资源 |
| 405 | 请求方式不支持 |
| 500 | 服务端内部错误 |

今天实际测试了正常响应和非法年龄的 400，其余是解释回顾。状态码与响应体不同，return 一段“失败”文字不自动产生错误状态码。应用可以自定义错误处理，因此示例不作为所有系统固定行为。

<a id="params-map-2026-11-02"></a>

#### 参数接收思维导图

```mermaid
mindmap
  root((2026-11-02 请求参数))
    客户端测试
      Postman代替尚未写的页面
      谁发请求谁收到响应
      Params与地址栏同步
    先选择方法
      路径区分接口
      请求方式参与匹配
      启动类运行即可接待请求
    简单参数
      注解指定请求参数名
      Java形参名可以不同
      默认必填
      defaultValue补缺失或空值
      required为false允许不传
      Integer允许null
    实体参数
      Spring创建User
      请求名匹配属性
      setter设置值
      User变量名不参与属性匹配
    两种默认值
      收请求时补值
      创建对象时初始化
      后续绑定可覆盖初始值
    实际排错
      非法年龄返回400
      重复参数不是自动覆盖
      未传和空值要区分
```

#### 本次上传验证与下次起点

- 归档实际新增/修改：HelloController.java 与 pojo/User.java；源码保持用户写法和注释，不借上传重构。启动类、POM、配置和生成测试类没有新改动。
- 学习者反馈：Postman 与浏览器正常请求、默认值、可选参数、实体参数与缺失年龄均测试成功；截图记录了重复参数错误并在调整后反馈成功。
- 本次上传前 JDK 25 使用 --release 17、UTF-8 和本地缓存的框架二进制库编译 3 个正式源码文件通过。
- 隔离快照的离线 Maven compile 未通过：父 POM 在缓存中，但仓库来源标记与本次 central 上下文不匹配，离线模式不能重新确认。这是本次构建环境限制，不等同于已证明业务代码错误；未修改用户依赖版本或缓存。
- 本次未重新启动服务、未复测 HTTP、未运行生成的 contextLoads 测试，不把源码编译检查说成 Maven 全构建或完整验收通过。
- 下一步先复看实体参数代码，再确认网课是否有嵌套对象内容，然后继续 Day05-04 数组、集合参数；日期、JSON、路径参数和响应处理仍在后面。



<a id="day-2026-11-03"></a>

### 2026-11-03：从复杂参数到真实数据库，把请求响应接成一条业务流程

今天的重点不是记住更多注解，而是看清楚：客户端送来的数据怎样成为 Java 对象，数据库记录怎样成为返回客户端的 JSON。以下区分实际源码、运行反馈和只讨论过的完善方向。

#### 一、嵌套对象：User 里面的地址在哪？

新增 Address（city、district），在 User 中增加 address 属性及 getter/setter。GET 查询参数使用 address.city=武汉、address.district=洪山区，默认绑定机制可为可写的嵌套属性创建对象并填充数据。

```text
user → User 对象
          ├─ name
          ├─ age
          └─ address → Address 对象
                          ├─ city
                          └─ district
```

- getAddress 返回已有地址对象的引用，不创建第二个地址。
- user.getAddress().getCity() 先取地址再取城市；未传地址可能为 null，所以当前代码先判空。
- 这些是运行时内存对象，不会在项目目录生成“用户对象文件”，也不会自动保存数据库。
- 打印地址应放在持有该局部变量的接口方法内；复制到启动类 main 不能直接访问局部变量。
- return 结束当前方法，无条件 return 后再打印不可达；判空分支 return 后的其他路径可以继续执行。
- 跨包引用公开类使用 import；跨项目还需建立依赖。Maven install 与声明依赖是复用独立项目代码的一种方式，不是仅把文件夹摆在一起。

#### 二、启动类与 Controller：为什么重启了还没打印？

启动类负责启动应用，Controller 方法在匹配的请求到达时才执行。修改 Java 代码后，本轮采用保存、停止旧应用、重新启动、再次发请求的流程。

```text
main 启动：开门营业
HTTP 请求：有人来办业务
Controller 执行：读取参数、打印或返回
```

println 显示在 IDEA 控制台；return 的结果交给框架处理后成为客户端收到的响应。新 /hobbies 方法曾误放启动类，只有 @SpringBootApplication 而非控制器注解，没有注册成预期请求映射，移入 HelloController 后反馈 200 和预期内容。

#### 三、数组和 List：同名参数这次为什么可以重复？

hobby=读书&hobby=篮球&hobby=编程，用 String[] 或 @RequestParam("hobby") List<String> 接收时，本来就需要把多个值收集起来。与单值 name、age 误加重复参数不同。

当前 /hobbies 使用 Arrays.toString，/hobby-list 使用 List.toString，二者返回类型是 String；看到方括号不代表它们返回了真正的 JSON 数组。后面的 /sample-users 才演示 List 对象经转换器生成 JSON。

Postman 没填的占位行不等于 new User()；取消勾选不会发送，hobby= 则是提供空值。未形成参数的空白行不影响接收，没有必需的 hobby 通常 400。

#### 四、日期、JSON 与路径：从不同位置取得数据

| 内容 | 当前代码 | 要点 |
|---|---|---|
| 日期 | LocalDateTime + @DateTimeFormat | 输入格式 yyyy-MM-dd HH:mm:ss；MM 是月、mm 是分 |
| JSON | @RequestBody User user | Body → raw → JSON，发送 Content-Type: application/json |
| 路径 | @PathVariable("studentId") String id | /students/DEMO001 中取出学号，不是查询参数 |

日期参数曾返回 400，截图中值前有多余空白，清理后反馈成功。我们讨论了客户端选择器、后端 strip 清理、解析异常给出友好 400，但**当前源码未实现这些容错改动**。不能解析时不应偷偷替换成当前时间。LocalDateTime 默认转字符串可能出现 T，与输入解析格式不是同一回事。

JSON 也可包含 address 对象，映射到嵌套 Address；/user-json 当前检查地址 null 后返回文字。只选择 POST 不代表自动读取 JSON，需要对应的接收配置。

#### 五、响应：JSON 是给客户端程序读的，不是让用户读原文

```text
@RequestBody：请求体 JSON → Java 对象
返回普通对象并使用 @ResponseBody：Java 对象 → JSON 响应
```

@RestController 组合 @Controller 与 @ResponseBody 的效果，当前方法不用每次重复写响应注解。返回 String 通常直接写文字，不是所有返回值都变成 JSON。

/sample-user 创建并返回一个 User；/sample-users 从返回 List 进一步改成返回 Result。Result 是自己写的普通类：

| 字段 | 当前含义 |
|---|---|
| success | 代码设置的业务成功标记 |
| message | 提示信息 |
| data | 实际数据，当前为 Object，可引用不同对象 |

Result 通过构造赋值、getter 暴露数据；框架生成 JSON，Postman 的缩进和颜色只是显示排版。名单引用放进 data，不会因为包装就复制一遍名单。业务 success 不自动决定 HTTP 状态码。

前端以后可以读取 JSON 并生成名单页面；今天使用 Postman，没有编写前端。GET 与 POST 都能返回 JSON，区别主要在请求语义：查询通常 GET，创建/提交通常 POST；请求方式不是响应格式开关。当前 GET 接口只在客户端改 POST 通常 405，不能假设两者自动兼容。

#### 六、复用以前的 JDBC：StudentDao、Connection 又是什么？

| 名称 | 当前作用 |
|---|---|
| Student | 保存查出的 studentId、name |
| Connection | JDBC 接口类型，连接对象由驱动实现 |
| connection | 引用已建立数据库连接的变量 |
| StudentDao | 普通数据访问类，当前只有静态 findAll |
| PreparedStatement | 通过已有连接准备、执行 SQL |
| ResultSet | 读取本次查询结果 |
| Result | 包装业务结果，交给响应转换 |

Student 构造方法把查到的学号、姓名赋给字段，getter 用于读取。暂不写 setter 是因为当前只查询展示，不代表对象今后永远不能增加修改方法；内存对象的修改也不会自动执行数据库 UPDATE。

Web 项目新增 com.mysql:mysql-connector-j:9.7.0，与 JdbcStudy 原驱动版本一致。本地已有 JAR 也仍需当前项目声明依赖。

#### 七、两个通信维度：终于查到了真实名单

1. **客户端 ↔ Java 后端**：HTTP 请求与响应。
2. **Java 后端 ↔ MySQL**：JDBC 连接、SQL 执行、结果读取。

Controller 读取 DB_PASSWORD，使用 root 和 jdbc:mysql://localhost:3306/campus_learning 建立连接，把连接传给 DAO。DAO 执行：

```sql
SELECT student_id, name FROM student ORDER BY student_id
```

ResultSet 初始位置在第一行之前；next 为 true 时读取当前行，每行 new Student(id,name)，放进 List，再把集合返回 Controller。

DAO 创建的 ResultSet、PreparedStatement 由 DAO 的 try-with-resources 关闭；Controller 创建的 Connection 由 Controller 的 try-with-resources 关闭。即使 return 在 try 内，返回前仍执行清理。资源所有权是本例的分工，不是 Java 强制“谁创建谁关闭”。

**关闭数据库连接不等于关闭 Web 应用，也不阻止 HTTP 响应。** 学生数据已经装进普通对象，数据库连接关闭后仍能序列化成 JSON。

当日实际工具请求 GET /students 确认：HTTP 200、success=true、message=查询成功，data 中 5 名学生，每项有 studentId、name。不是 Controller 中手写的假名单。空表则返回空数组，不自动视为异常。

当前异常还未统一处理：环境变量缺失抛 IllegalStateException，连接/查询失败可抛 SQLException；框架默认通常返回 500，不会自动生成 Result(false,...)。

<a id="endpoints-2026-11-03"></a>

#### 八、当前接口清单

| 方法与路径 | 输入/用途 | 响应 |
|---|---|---|
| /hello（原 @RequestMapping） | 无参数，未限制为 GET | 固定问候字符串 |
| GET /greet | name 必填 | 问候字符串 |
| GET /introduce | name 必填、age 可选 Integer | 介绍字符串；旧必填/默认值版本已注释 |
| GET /user-info | name、address.city、address.district | 地址文字或“地址未填写” |
| GET /hobbies | 多个 hobby | 数组内容的字符串 |
| GET /hobby-list | 多个 hobby | 集合内容的字符串，另在控制台打印 |
| GET /meeting | time=2026-11-03 14:30:00 | 日期时间文字 |
| POST /user-json | User 的 JSON 请求体，可含 address | 地址文字或“地址未填写” |
| GET /students/{studentId} | 路径学号 | 仅回显学号，不查数据库 |
| GET /sample-user | 无参数 | 固定 User 的 JSON |
| GET /sample-users | 无参数 | Result 包装固定的两个用户 |
| GET /students | 无参数，读取数据库 | Result 包装真实 List<Student> |

GET /students 与 GET /students/{studentId} 是不同接口，不能把后者误记成按学号查询数据库已完成。

<a id="flow-2026-11-03"></a>

#### 九、完整流程图：HTTP、Java 对象和 JDBC

```mermaid
flowchart TD
    subgraph START["启动应用"]
        A["运行启动类 main"] --> B["Spring Boot 初始化，启动内嵌 Tomcat"]
        B --> C["识别控制器，建立 GET /students 映射"]
        B --> D["DispatcherServlet 注册给 Tomcat"]
        C --> E["监听 8080，等待请求"]
        D --> E
    end
    subgraph HTTP["客户端与后端：HTTP"]
        F["Postman 发送 GET /students"] --> G["Tomcat 接收并解析请求"]
        G --> H["调用 DispatcherServlet，协调 Spring MVC"]
        H --> I["找到 StudentController.findAll"]
    end
    E -. "准备好后接待" .-> G
    subgraph JDBC["后端与数据库：JDBC"]
        I --> J["读取 DB_PASSWORD，检查非空"]
        J --> K["DriverManager.getConnection 建立连接"]
        K --> L["把 connection 传给 StudentDao.findAll"]
        L --> M["创建空 List，准备 SELECT"]
        M --> N["connection.prepareStatement"]
        N --> O["executeQuery"]
        O --> P[("MySQL student 表")]
        P --> Q["得到 ResultSet"]
        Q --> R{"next 是否有下一行"}
        R -- "true" --> S["读取 student_id、name"]
        S --> T["new Student，加入 List"]
        T --> R
        R -- "false" --> U["关闭 ResultSet、statement"]
        U --> V["返回 List，不在 DAO 关闭 connection"]
    end
    subgraph RESPONSE["Java 结果与 HTTP 响应"]
        V --> W["Controller 创建 Result，data 引用 List"]
        W --> X["退出 try，自动关闭 connection"]
        X --> Y["将 Result 返回框架"]
        Y --> Z["JSON 转换器通过 getter 读取对象数据"]
        Z --> ZA["Result 为对象，data 为数组，Student 为数组元素"]
        ZA --> ZB["正常 HTTP 200，Tomcat 发送 JSON"]
        ZB --> ZC["Postman 显示：success、message、5 个学生"]
    end
    J -. "变量缺失" .-> ER["当前未统一处理异常，默认通常 500"]
    K -. "连接失败" .-> ER
    O -. "查询失败" .-> ER
```

Result 是内存对象，JSON 是对象数据的文本表示，不是把 Java 对象本身搬到客户端。两次 return 也不同：DAO 在 Java 内部返回引用；Controller 返回值最终才经框架成为网络响应。

#### 十、今天的排错与学习方式调整

- Postman 更新到 12.35.0 的日志出现路径过长，程序位于 Windows 重定向后的较长目录。桌面入口打不开，手动将快捷方式目标改到保留的 app-12.34.4/Postman.exe 后反馈成功；属于临时恢复旧版本，不代表更新路径问题彻底修复。
- Spring Boot 运行配置需单独设置 DB_PASSWORD，旧 JdbcStudy 的配置不会自动套用。检查只确认变量存在且非空，不显示密码；随后真实查询成功。
- 认识 SQL 和连接代码的用途，不等于已经能独立编写。以后围绕 AI Coding 采用“需求 → 小范围实现 → 解释关键改动 → 亲自验收 → 保存版本”，让 AI 生成重复代码，人把关业务、数据、权限、修改范围和验证结果。
- 网课先讲三层、后讲数据库是教学安排；本次利用旧 JDBC 知识提前接库，不代表三层、IOC/DI 已学完。
- 按用户要求，今天不正式学习三层架构；尚无 Service、依赖注入和连接池，也未开发签到程序。

#### 十一、上传范围与验证边界

- 保存当前 POM 和 src 源文件原貌；新增 Address、Result、Student、StudentDao、StudentController，更新 User、HelloController 和启动类。启动类仍保留未使用导入，HelloController 旧注释“告诉浏览器”应理解为“告诉 Spring”，不借归档重构。
- 正式源码共 8 个 Java 文件，生成的 contextLoads 测试仍保留。
- 当日已实际验证 /students 的数据库结果（5 条）；其他练习成功主要来自学习者反馈，不扩大成全面接口验收。
- 归档时另做源码编译检查，javac 访问 JAR 时出现 AccessDeniedException，复制到工作区后仍受访问限制，未记为独立编译验证通过，也未重复尝试全量 Maven 构建。不能把编译器异常后的退出码误记为通过。
- 不上传 .idea、DB_PASSWORD 实值、target、依赖缓存或数据库物理文件。本次未执行数据库增删改。
- 历史记录按原日期保留，顶部进度以本日更新为准；后续按用户节奏继续，不默认三层架构已经开始。



<a id="day-2026-11-04"></a>

### 2026-11-04：把查询学生整理成三层，理解对象为何能协作

今天围绕已运行的 GET /students 完成结构改造。重点是理解职责、引用和调用，不要求默写框架样板代码。聊天存储、长期记忆和多 Agent 上下文只作题外了解，不计入本日核心进度。

#### 一、Controller 是整体业务负责人吗？最后一层是什么？

Controller 负责 HTTP 请求与 Java 业务之间的衔接；Service 负责组织业务；DAO 负责具体数据库操作。MySQL 是外部存储，不是 Java 三层中的一层。

| 层 | 当前类 | 本例具体工作 |
|---|---|---|
| Controller | StudentController | 匹配 GET /students，调用 Service，把列表包装为 Result |
| Service | StudentService | 读取连接配置，建立连接，调用 DAO，返回列表并关闭连接 |
| DAO | StudentDao | 准备并执行 SELECT，读取 ResultSet，生成 Student 列表 |

当前 Service 业务规则很少，主要转交查询，仍可用于认识分工。以后签到业务中，Service 可以判断是否有排班、是否重复签到，DAO 才执行查询和保存。当前连接管理写在 Service 是沿用 JDBC 的学习安排，不是说真实项目必须都这么写。

HTTP 的解析、请求分发、JSON 转换和响应发送，由 Tomcat、Spring MVC 等组件与我们的代码配合完成；Controller 不必手动实现整套 HTTP。

#### 二、Service 与 DAO 不同包，为什么能调用？

当前调用为：

```java
return StudentDao.findAll(connection);
```

- StudentDao 类和 findAll 方法都是 public，允许跨包访问。
- import 指明短类名对应哪个完整类名，不授予访问权限；也可以直接写完整类名。
- findAll 是 static，通过类名调用，无须先 new DAO。
- connection 是参数：Service 提供已有数据库连接，DAO 使用后返回列表；DAO 不需要知道是谁调用自己。

对比：

```java
studentService.findAll();          // 实例方法，通过对象引用调用
StudentDao.findAll(connection);    // 静态方法，通过类名调用
```

DAO 创建并关闭 statement、resultSet；Service 创建并关闭 connection。return 在 try-with-resources 内也仍会执行资源关闭。当前 DAO 没有注入，不必把所有层间调用都想成 Spring 自动完成。

| 成员权限 | 本类 | 同包其他类 | 跨包子类 | 跨包普通类 |
|---|---|---|---|---|
| private | 可以 | 不可以 | 不可以 | 不可以 |
| 不写修饰符（包级） | 可以 | 可以 | 不可以 | 不可以 |
| protected | 可以 | 可以 | 有继承及访问方式限制 | 不可以 |
| public | 可以 | 可以 | 可以 | 可以 |

以上是普通成员访问的基础概览；普通顶层类只可使用 public 或包级访问。子包与父包也不是同一个包。不同模块还可能涉及模块导出规则，本日未展开。

static 更适合不依赖某个实例状态的操作，例如 Math.max。静态方法没有 this，不能直接访问实例成员，也不参与实例方法的重写和动态选择；明确持有对象引用时仍可以访问允许访问的实例成员。static 不意味着天然更快或更好。

#### 三、类已经实现接口，变量前面为什么还写接口名？

新增 StudentQueryService：

```java
public interface StudentQueryService {
    List<Student> findAll() throws SQLException;
}
```

无方法体的这条接口方法声明默认是 public abstract。接口和抽象方法不是二选一；抽象类也可定义抽象方法并支持多态。当前只是约定查询能力，无共同实例状态要继承，选择接口即可。

```java
public class StudentService implements StudentQueryService {
    // 实现 findAll()
}
```

implements 声明类的实现关系；下面则是变量声明与赋值：

```java
StudentQueryService studentService = new StudentService();
```

| 部分 | 作用 |
|---|---|
| StudentQueryService | 引用变量的类型，编译时据此检查可调用的方法 |
| studentService | 变量名字，可以换成其他合法名字 |
| new StudentService() | 创建真实的实现类对象 |
| studentService.findAll() | 实际执行该对象对应的实例方法实现 |

类可以增加接口未声明的方法，但不能直接通过接口类型变量调用额外方法。多个接口可共用同一个兼容实现；无继承关系的接口若提供冲突 default 方法，需要类明确重写；返回类型不兼容的同签名要求不能这样同时实现；业务规则矛盾则需人设计，编译器未必能发现。

#### 四、IOC 与 DI：字段哪里来，谁把对象交给 Controller？

改造前，Controller 自己创建对象：

```java
private final StudentQueryService studentService = new StudentService();
```

改造后，Controller 接收对象引用：

```java
private final StudentQueryService studentService;

public StudentController(StudentQueryService studentService) {
    this.studentService = studentService;
}
```

- 字段由我们声明，不是 Spring 自动往类里加变量。
- 构造参数是暂时接收引用的变量；this.studentService 是当前对象的字段。
- 赋值保存引用，没有复制出第二个 Service 对象。
- final 表示字段赋值后不能再指向另一个对象，不代表对象内部状态自动不可变。
- Spring 安排创建对象并传入参数；普通构造方法本身不会主动寻找依赖。

如果手动组织，理解上类似下面两行，不必复制到启动类：

```java
StudentQueryService service = new StudentService();
StudentController controller = new StudentController(service);
```

| 概念 | 当前实践 |
|---|---|
| IOC（控制反转） | Service 创建、管理交给 Spring IOC 容器 |
| DI（依赖注入） | Spring 把符合接口类型的对象引用传入 Controller 构造方法 |

Spring Boot 帮助配置、启动 Spring 应用，具体对象管理由 Spring 容器承担。StudentService 的 @Service、StudentController 的 @RestController 使默认扫描能发现并管理它们；本例包位于启动类所在包之下。Controller 只有一个构造方法，因此可以省略构造方法上的 @Autowired。其他注入方式只是提及，未要求本日掌握。

<a id="flow-2026-11-04"></a>

#### 五、流程图：启动时安排对象，请求时调用方法

```mermaid
flowchart TD
    subgraph START["启动：创建与注入"]
        A["运行 Spring Boot 启动类"] --> B["组件扫描"]
        B --> C["发现启用 @Service 的实现类"]
        C --> D["创建并管理 StudentService Bean"]
        B --> E["发现 @RestController"]
        E --> F["检查 StudentController 唯一构造方法"]
        D --> G["按 StudentQueryService 类型提供对象"]
        F --> G
        G --> H["构造参数接收引用"]
        H --> I["this.studentService 保存引用"]
        I --> J["建立请求映射，等待访问"]
    end
    subgraph REQUEST["请求：三层调用"]
        K["客户端 GET /students"] --> L["Tomcat 与 Spring MVC"]
        L --> M["StudentController.findAll"]
        M --> N["studentService.findAll"]
        N --> O["Service 创建 JDBC Connection"]
        O --> P["StudentDao.findAll(connection)"]
        P --> Q[("MySQL")]
        Q --> R["DAO 读取结果，生成 List<Student>"]
        R --> S["DAO 关闭 statement 和 ResultSet"]
        S --> T["Service 返回列表并关闭 Connection"]
        T --> U["Controller 包装 Result"]
        U --> V["框架转换 JSON 并发送响应"]
    end
    J -. "准备完成" .-> L
```

Controller 与 Service 的联系是 Java 对象引用；Service 与数据库的连接是 JDBC Connection，两者不要混为一谈。默认作用域下 Controller、Service 通常复用，但本例每次数据库查询仍新建、关闭连接；复用 Bean 不意味着字段自动线程安全。

#### 六、分层解耦有什么实际作用？模拟实验给出了答案

新增 MockStudentService，同样实现 StudentQueryService，直接返回 mock001、mock002 两个虚构学生，不调用 DAO，也不必声明 SQLException。实现方法可以不声明接口允许抛出的受检异常。

```mermaid
flowchart TD
    A["StudentController<br/>通过接口调用 findAll"] --> B["StudentQueryService<br/>统一查询约定"]
    B --> C["StudentService<br/>DAO 查询数据库"]
    B --> D["MockStudentService<br/>直接返回虚构名单"]
```

切换过程：注释数据库实现 @Service，为模拟实现添加 @Service，重启后请求同一个 /students；Controller 查询代码不变，学习者反馈得到两个模拟学生。随后反向切换并恢复数据库实现，作为本次归档状态。

@Service 可以标在多个类上，Spring 可以创建多个对象。遇到同一接口的多个候选，问题在于单个参数该选谁；@Primary、@Qualifier 和集合注入仅介绍，未实践。构造参数名 studentService 也可能匹配原实现默认 Bean 名，所以不能断言两个实现同时注册一定报错；需要明确选择时应明确配置。

解耦不是取消依赖，而是减少对具体实现及创建方式的依赖。现在 Controller 只要求查询能力；创建与选择交给容器，模拟测试、替换数据来源更方便。当前 Service 仍直接调用静态 DAO，不宣称所有层都完全解耦。

<a id="ai-coding-2026-11-04"></a>

#### 七、AI Coding：如何指挥实现，怎样严谨验收？

以后每次小功能加入提示词训练：说清输入输出和业务目标，要求先读现有代码、限定修改范围、复用已有结构，再按可观察结果验收。AI 可写重复代码，人负责业务、关键判断和证据。

可参考的提示词：

> 阅读现有学生查询代码，沿用三层结构，让 Controller 通过接口和构造方法注入 Service。只修改必要文件，保持请求路径与响应结构一致。先解释方案，再修改。验证时确认当前运行服务加载了本次代码，检查响应实际数据，分别报告源码检查、构建、运行测试和未验证项。

本日重要教训：首次仅看见注解切换正确，又收到“请求成功”的反馈，却没有确认用户是否重启。用户指出当时没有重启，因此该次成功可能是旧模拟程序的响应。不能据此宣布新配置生效。

后续取得的实际证据如下（2026-11-04，本机时间）：

| 证据 | 观察 |
|---|---|
| 源文件 | 两个 Service 文件最后修改于 21:23:32 |
| 运行进程 | 8080 对应 Java 进程在 21:35:57 启动，晚于修改 |
| 实际工具请求 | GET /students 返回 HTTP 200、success=true、message=查询成功 |
| 响应内容 | 5 条学生记录，每条有 studentId、name，不是两个模拟学生 |
| 源码配置 | StudentService 启用 @Service，MockStudentService 注释 @Service |

这些证据支持“修改后启动的服务已正常返回数据库实现对应名单”。本次没有逐条直接对照数据库原始记录，没有系统验证空表、断库、并发或全部接口；也未单独重新执行 Maven 全构建或生成的 contextLoads 测试。过程没有由工具替用户重启程序或修改数据库。

验收习惯：

1. 源码检查、编译、运行版本、用户反馈和独立运行测试分开记录。
2. 需要重新启动/构建/部署的修改，核对实际运行目标及生效证据；磁盘文件不等于运行中的程序。
3. 检查内容和行为，不只看 200 或代码设置的 success=true。
4. 按改动覆盖必要的成功、失败、边界场景；未执行的不能声称通过。
5. 工具权限失败说明无法取得证据，不能直接判断应用故障或成功。

#### 八、今日掌握程度、归档状态与下次起点

- 已完成基础指导实践：三层职责、接口多态、单构造方法注入、真实与模拟实现切换。能解释关系，不等于已独立掌握复杂业务设计。
- 新增 StudentQueryService、StudentService、MockStudentService；修改 StudentController。其他已跟踪源码和构建文件与上次归档对照，忽略 CRLF/LF 差异后无内容变化。
- 代码保留用户原貌，MockStudentService 的 @Service 注释及未使用 import 也保留，不借上传重构。
- 当前恢复数据库实现；DAO 仍是 static findAll。按学号查库尚未实现，/students/{studentId} 仍只回显学号。
- 后续围绕“按学号查询”继续：Controller 接收 → Service 组织 → DAO 参数化 SQL；区分未找到与查询异常，验收存在/不存在等情况。
- 归档只保存必要源码与学习文档，不包含 .idea、密码值、target、依赖缓存或数据库物理文件。
- 题外话仅涉及聊天存储、长期记忆、多 Agent 上下文协作；不展开为本日 JavaWeb 知识点。


## 记录约定

1. 按 YYYY-MM-DD 标注实际学习日期，新增日期的内容另设小节；保留旧记录，不反复覆盖历史。
2. 补录、复制或迁入历史内容时，分别注明原学习日期、归档日期及来源。
3. 每次更新同步维护顶部日期导航、当前进度、知识索引和代码入口，避免只追加日志。
4. 保留概念讲解、关键问答和思维导图，围绕实际困惑解释，而非只列学习流水账。
5. 区分“讲解过”“写过”“运行验证过”；预告和拓展了解不算已完成。
6. 学习代码保留原貌；若讲解中的旧例子与后来的源码不同，注明版本和差异。
7. 每次按用户实际学习与更新请求整理提交；本约定不是定时自动上传任务。
