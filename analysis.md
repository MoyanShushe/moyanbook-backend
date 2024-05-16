# 概览

## 需求

我们的二手书交易平台是一个在线市场，旨在连接那些想要出售不再需要的书籍的用户和寻找实惠书籍的读者。
本平台致力于促进知识的共享和资源的循环利用，同时为用户提供便捷、安全的交易体验

# 用户端

## 账号

### 注册

* 通过邮箱进行注册（手机号暂无实现）
* 密码加密存储
* 邮箱验证码验证

> 发送时需要有用户名与邮箱
> 用户名不可为空
> 检查邮箱，用户名，密码合法性

### 登录

* 通过邮箱或者用户名登录
* 密码加密
* 登录成功返回jwt token（每次操作都需要jwt与服务端双重验证）
* 登录后服务端记录登录信息，设置用户登录有效时间

### 登出

* 登出后服务端删除登录信息

### 数据库表

| 字段名             | 数据类型         | 是否可为空 | 默认值       | 备注                |
|-----------------|--------------|-------|-----------|-------------------|
| id              | int          | NO    | PRI       | auto_increment    |
| name            | varchar(20)  | NO    |           |                   |
| age             | int          | YES   | 18        |                   |
| gender          | tinyint      | YES   |           |                   |
| email           | varchar(30)  | YES   | null      |                   |
| phone           | varchar(20)  | YES   |           |                   |
| password        | varchar(64)  | NO    |           |                   |
| status          | tinyint      | YES   |           |                   |
| create_time     | date         | NO    | curdate() | DEFAULT_GENERATED |
| update_time     | date         | NO    | curdate() | DEFAULT_GENERATED |
| profile_url     | varchar(128) | YES   |           |                   |
| last_login_time | date         | NO    | curdate() | DEFAULT_GENERATED |

## 书籍

### 展示

#### 主分页展示信息
* 名称
* 图片
* 描述

##### 筛选信息

#### 分类主页显示信息

* 名称
* 图片
* 标签
* 价格
* 分类
* 描述
* 原书主人标签
* 留言

#### 书籍页详情显示信息

* 名称
* 图片
* 标签
* 价格
* 分类
* 描述
* 原书主人标签
* 留言
* 送书时间
















