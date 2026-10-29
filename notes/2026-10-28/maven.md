# 2026-10-28：Maven 入门（历史记录副本）

> 原学习日期：2026-10-28。复制归档日期：2026-10-29。
> 来源：[Database-Basics 的原始记录](https://github.com/theotherDancer/Database-Basics/blob/c6807b05c3e5b2a547a8e4e3fa6d9ae8040f140f/README.md#maven-2026-10-28)。
> 对应源码：[MavenStudy/pom.xml](../../MavenStudy/pom.xml)。
> 以下保留原文，描述的是 10 月 28 日当时的状态，不代表 10 月 29 日电脑上的 MavenStudy 仍没有源码；本次复制的是原仓库归档内容，并未拿今天的本地工程替换历史快照。

<a id="maven-2026-10-28"></a>

#### Maven：环境、创建项目、依赖与故障排查

**环境检查与配置：**

- 已读取 product-info.json，IDEA 版本为 2026.2.3，构建号 262.10968.63。
- IDEA 自带 Maven 3.9.16，目录位于 IDEA 的 plugins/maven-plugin/lib/maven3，无需另行安装。
- JDK 位于 D:/develop/jdk；JAVA_HOME 已配置，终端 PATH 当时未配置 Maven。
- 在 IDEA 中选择 Bundled Maven 即可，不要求终端能直接识别 mvn。
- 实际查看设置确认：Runner JRE 为已安装 JDK 25；JDK for importer 使用 Project JDK；pom.xml 的编译版本为 25；离线模式未勾选。
- 默认本地仓库为用户目录下 .m2/repository，存放依赖和插件，不是源码。DB_PASSWORD 只用于 JDBC 程序，与 Maven 环境配置不是一回事。

**创建与验证：**

1. IDEA 新建 Java 项目，Build system 选择 Maven，使用已有 JDK。
2. 练习项目 MavenStudy，坐标 com.zhangyihang:maven-study:1.0-SNAPSHOT。
3. 识别 pom.xml、src/main/java、resources、test、target 的用途。
4. 学习者反馈 package 打包成功；添加依赖后又在 IDEA 执行 mvn -U compile，实际观察到 BUILD SUCCESS。
5. 当前 MavenStudy 的 src 未保存 Java 源文件，因此不宣称已经运行 HelloWorld 或日志示例；构建成功不等同于已有业务代码。

**依赖：**

依赖是项目使用的外部代码库；Maven 帮助获取和管理。JdbcStudy 的 MySQL 驱动与本项目 Logback 都是依赖。
当前声明如下：

```xml
<dependencies>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.6.6</version>
    </dependency>
</dependencies>
```

- groupId 为组织标识，artifactId 为组件标识，version 为所需版本。
- dependencies 是全部依赖的容器，dependency 是其中一个依赖；不能误将 dependency 直接放在 project 下。
- Logback 用于记录日志；声明依赖不等于自动产生日志业务逻辑，还要在代码中调用。
- 版本在本次排查中已与中央仓库版本清单核对，1.6.6 存在，不能把下载报错直接判为版本不存在。
- package/compile 及 Maven 生命周期目前初步接触，仓库、依赖范围和完整生命周期继续跟课学习。

**这次报错的过程：**

| 阶段 | 现象 | 定位与处理 |
|---|---|---|
| 依赖声明不完整 | groupId 写成 com.，未写 version | 改为完整坐标 |
| 标签层级错误 | dependency 直接处于 project 下 | 保留外层 dependencies |
| 编辑区与磁盘状态不一致 | 用户尚未手动保存时，磁盘读取不足以说明编辑区状态 | 结合 IDEA 实际界面；编辑器会实时检查，IDEA 也可能自动保存 |
| 依赖下载失败 | Remote host terminated the handshake | HTTPS/TLS 连接被中断，不是 Java 语法问题 |
| 失败缓存 | 普通重载暂不重新尝试 | -U 强制检查缺失依赖并重试 |
| 最终验证 | IDEA 输出 BUILD SUCCESS，POM 红色错误消失 | 此次依赖解析和编译成功 |

-U 只强制重试，并不能修复持续的网络握手故障。这次重试成功，不需要更改版本、关闭 TLS 校验或清空整个仓库。
Maven 工具窗口的重载与执行构建不同；可连续两次 Ctrl 打开 Run Anything，再输入 mvn -U compile，使用 IDEA 配置的 Maven。

参考：[IDEA Maven](https://www.jetbrains.com/help/idea/maven-support.html)、[Maven goals](https://www.jetbrains.com/help/idea/work-with-maven-goals.html)、[Maven -U 参数](https://maven.apache.org/ref/3-LATEST/maven-embedder/cli.html)、[Logback 设置](https://logback.qos.ch/setup.html)。
