# 考勤实战项目学习节选（脱敏整理）

日期：2026-11-05。

> 来源与作者归属：本页是学习一个已有考勤实战项目时，对特定源码片段的节选、脱敏与教学改写；原项目代码不是本仓库学习者编写的，也不是学习者独立实现的成果。AI 协助整理说明和示意写法，不代表原代码作者。原项目作者与版权归原权利人，本页不为原项目授予开源许可。
>
> 公开范围仅为本次知识点所需的短片段，不提供完整类、工程、业务算法或原项目入口。机构信息、包名、内部角色来源、服务地址、部门编号、接口细节及真实人员数据已省略或泛化。示例人名和编号均为虚构。
>
> 下列代码块分别阅读，不构成可以直接编译运行的工程。未给出的方法、依赖和字段均有意省略；教学改写不是原项目逐字源码。

## 1. 身份数据：同一类型的不同对象

保留原片段的数据结构形式，去掉原项目说明和包名：

```java
public record Identity(
        String campusId,
        String name,
        boolean leadership,
        boolean administrator) {}
```

教学示例（虚构数据，并非原项目人员）：

```java
Identity ordinary = new Identity("demo001", "示例甲", false, false);
Identity admin = new Identity("demo002", "示例乙", false, true);
```

两者是同一类型的不同对象，没有创建“管理员子类”。record 是简洁的数据类写法，不能因此把对象内容与权限规则混为一谈。

## 2. 名单判断：先查信息，再创建身份对象

下面是脱敏教学改写：只演示名单匹配，不公开原项目名单来源、客户端调用及异常流程。演示管理员名单是通用虚构规则，不表示原机构实际管理规则。

```java
boolean leadership = leaderIds.contains(campusId);
boolean administrator = administratorIds.contains(campusId);
return new Identity(campusId, name, leadership, administrator);
```

前提：name、leaderIds、administratorIds 已由调用方取得。两个布尔值分别判断，一个人可以同时有两个角色。身份由后端依据可信资料判断，不能信任客户端自称的角色。

## 3. 权限数据与条件返回

保留权限数据形式，省略其余方法：

```java
public record Permission(
        Identity identity,
        ViewScope scope,
        Set<String> visibleCampusIds) {

    public enum ViewScope {
        ALL, SECTION, SELF
    }

    public boolean canSee(String campusId) {
        return visibleCampusIds.contains(campusId);
    }
}
```

枚举表示可见范围，不是身份子类。下面仅节选“没有负责科室”的一个分支，不包含完整权限算法。前提是调用方已完成身份、成员资格等必要检查：

```java
if (headedSectionIds.isEmpty()) {
    return new Permission(identity, ViewScope.SELF, Set.of(campusId));
}
```

集合为空时 isEmpty() 为 true；return 结束方法。identity 是已有对象的引用值，构造 Permission 不会自动复制一个 Identity。canSee 返回 false 本身不会自动阻止请求，业务代码还要使用结果限制访问。

## 4. Controller 与 Service 的对象协作

节选字段与注解形式，去除原项目路由、请求头及其他服务依赖：

```java
@RestController
@RequiredArgsConstructor
class DashboardController {
    private final DashboardService dashboardService;
    // 其余成员与接口方法省略
}
```

@RequiredArgsConstructor 由 Lombok 在编译时生成相应构造方法；Spring 在组件扫描等配置成立时创建、管理对象，并向构造方法提供依赖。字段声明不等于创建对象，final 不代表所引用对象的内部状态自动不可变。

下面是对原嵌套调用的拆行教学写法，permissionService、campusId 均假设来自调用上下文：

```java
Permission permission = permissionService.permit(campusId);
DashboardResponse response = dashboardService.now(permission);
return response;
```

先取得权限，再调用看板服务，再返回结果。声明 response 接收引用本身不是 new，也不能单凭这句赋值判断是否涉及多态。看板响应对象具体如何生成，今天尚未深入阅读。

## 5. Service 向组织功能取得成员列表

仅节选成员读取相关语句；没有公开看板计算、外部数据读取、排班及返回结构：

```java
var organisation = organisationService.members();
```

对应组织服务的短方法：

```java
public List<OrgMember> members() {
    return memberRepository.findAll();
}
```

Repository 承担本例数据访问职责；不同业务服务之间通过服务协作。查询返回后 organisation 是 List<OrgMember>。调用方法不会把代码执行位置永久“搬到”另一层，return 后回到调用处继续执行。

## 6. JPA 实体、Repository 与 JDBC

保留框架映射写法，表名和字段改为通用演示值；这是教学改写，不是原项目数据库结构：

```java
@Entity
@Table(name = "demo_member")
class DemoMember {
    @Id
    @Column(name = "member_id")
    private String id;
    // 其余部分省略
}
```

```java
interface DemoMemberRepository
        extends JpaRepository<DemoMember, String> {
}
```

```java
repository.findAll();             // 全部记录，不保证顺序
repository.findById("demo001");   // 按 @Id 对应的主键查询
```

findAll、findById 是框架已有约定和实现的方法。泛型指定实体与主键类型，映射指定表和列。Spring Data JPA 提供 Repository 的运行时实现；Hibernate 根据映射组织 SQL 与结果映射；底层通过 JDBC 和驱动访问数据库。

“告知意图”不是自然语言理解：此处调用已有方法就选择已有操作；带条件查询仍要传入值。框架处理参数绑定，不应自行拼接不可信输入。复杂查询、效率以及业务规则仍由开发者负责。

与自己的旧练习对照：手写 StudentDao 需要显式写 SQL、使用 PreparedStatement、遍历 ResultSet、封装对象；这里将重复数据访问工作交给框架。自定义方法命名查询与 @Query 尚未在本次课程展开。

## 阅读与验证边界

本页只做了原片段对应关系、脱敏、文字与链接检查。没有编译这些不完整代码块，也没有运行考勤项目、访问其数据库或验证业务结果；原项目未做修改。代码阅读理解不等于学习者已经可以独立完成同样功能。
