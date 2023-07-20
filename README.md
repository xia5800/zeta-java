# zeta-java 基础开发框架

## 简介
zeta-java是使用java语言基于`spring boot`、`mybatis-plus`、`sa-token`等框架开发的项目脚手架。

zeta-java目前只提供了一个最基础的RBAC用户角色权限功能。不像其它开源项目那样大而全，zeta-java项目相当精简。

## 项目结构

| 包                         | 说明                                                                    |
| -------------------------- | ---------------------------------------------------------------------- |
| com.zeta                  | 业务包，专注于业务代码的编写                                               |
| org.zetaframework         | zeta框架核心配置包，包含sa-token、redis、mybatis-plus、knife4j等框架的配置   |


## 技术选型

| 技术                         | 说明                                                         |
|----------------------------| ------------------------------------------------------------ |
| spring boot                | 核心框架                                                     |
| sa-token                   | 权限认证框架                                                     |
| mybatis-plus               | [MyBatis扩展](https://doc.xiaominfo.com/)                      |
| Redis                      | 分布式缓存数据库                                             |
| knife4j                    | [一个增强版本的Swagger 前端UI](https://doc.xiaominfo.com/knife4j/)  |
| hutool                     | [Java工具类大全](https://hutool.cn/docs/#/)                  |
| RedisUtil                  | [最全的Java操作Redis的工具类](https://gitee.com/whvse/RedisUtil) |
| EasyPoi                    | [简单方便的导入导出Excel](https://gitee.com/lemur/easypoi) |

## 配套项目

| 名称                    | 说明                                 | 项目地址                                                     |
|-----------------------|------------------------------------| ------------------------------------------------------------ |
| zeta-kotlin           | 本项目的kotlin语言版                | [gitee](https://gitee.com/xia5800/zeta-kotlin) [github](https://github.com/xia5800/zeta-kotlin)|
| zeta-kotlin-generator | 专门为zeta-kotlin和zeta-java项目定做的代码生成器 | [gitee](https://gitee.com/xia5800/zeta-java-generator)  [github](https://github.com/xia5800/zeta-java-generator) |
| zeta-kotlin-module    | zeta-kotlin项目多模块版                  | [gitee](https://gitee.com/xia5800/zeta-java-module) [github](https://github.com/xia5800/springboot-kotlin-module)|

## 前端
[zeta-web-layui](https://gitee.com/xia5800/zeta-web-layui)

## 后端
[http://localhost:8080/doc.html](http://localhost:8080/doc.html)

账号：zetaAdmin

密码：dDEWFk6fJKwZ55cL3zVUsQ==

## 已有功能

- 用户管理
- 角色管理
- 操作日志
- 登录日志
- 数据字典
- 文件管理
- websocket
- XSS防护
- Ip2region离线IP地址查询
- Excel导入导出
- 数据脱敏

## 写在后面

这世界上总有人墨守成规不肯接触新事物, 因此本人将zeta-kotlin项目用java语言重写了一遍。

使用java重写的过程中，深有体会的是：java语言有java语言的优点，kotlin语言也有kotlin语言的优点。

两种语言都有很爽的写法，也有不爽的写法。故十分好奇，世界上真有那种集合了各种编程语言爽点的编程语言吗？

如果你是一个java程序员想学习Kotlin，我建议将本项目对比着zeta-kotlin项目来学习。

你将了解到java实现的功能如何用kotlin来实现。

## 友情链接 & 特别鸣谢

- lamp-boot：[https://github.com/zuihou/lamp-boot](https://github.com/zuihou/lamp-boot)
- sa-token [https://sa-token.dev33.cn/](https://sa-token.dev33.cn/)
- mybatis-plus：[https://baomidou.com/](https://baomidou.com/)
- knife4j：[https://doc.xiaominfo.com/](https://doc.xiaominfo.com/)
- Hutool：[https://hutool.cn/](https://hutool.cn/)
- EasyPoi：[http://www.wupaas.com/](http://doc.wupaas.com/docs/easypoi)
