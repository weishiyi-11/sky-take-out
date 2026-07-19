# 苍穹外卖 - Spring Boot 外卖系统

基于 Spring Boot 的企业级外卖平台后端系统，涵盖菜品管理、套餐管理、订单管理、用户管理、实时消息推送等核心业务功能，并在原有基础上进行了多项性能优化与安全增强。

---

## 项目简介

苍穹外卖是一套完整的外卖管理系统，分为**管理端**和**用户端（微信小程序）** 两大模块：

- **管理端**：员工登录、菜品管理、套餐管理、分类管理、订单管理、数据统计报表等
- **用户端**：微信登录、浏览菜品/套餐、购物车、下单支付、订单查询、地址管理、催单等

---

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 2.7.3 | 核心框架 |
| MyBatis | 数据访问层，动态 SQL |
| MySQL | 关系型数据库 |
| Redis | 缓存中间件，用于热点数据缓存 |
| Spring Cache | 缓存抽象层，简化缓存操作 |
| Spring Task | 定时任务，处理超时订单等 |
| WebSocket | 服务端推送，实现来单提醒、催单功能 |
| JWT | 无状态身份认证，双端 Token 管理 |
| Nginx | 反向代理、静态资源部署 |
| 阿里云 OSS | 对象存储，用于图片上传 |
| 微信支付 | 在线支付集成 |
| 百度地图 API | 配送距离校验、预计送达时间计算 |
| Knife4j | 接口文档（基于 Swagger） |
| Druid | 数据库连接池 |
| PageHelper | MyBatis 分页插件 |
| Apache POI | Excel 报表导出 |
| Guava | 布隆过滤器（防缓存穿透） |
| AOP + 自定义注解 | 公共字段自动填充、全局日志记录 |

---

## 项目结构

```
sky-take-out
├── sky-common          # 公共模块：工具类、常量、异常处理、拦截器
├── sky-pojo            # 实体模块：Entity、DTO、VO
└── sky-server          # 服务模块：Controller、Service、Mapper、配置
    └── src/main/java/com/sky/
        ├── controller/     # 控制层（管理端 + 用户端接口）
        ├── service/        # 业务逻辑层
        │   └── impl/       # 业务实现
        ├── mapper/         # 数据访问层（MyBatis Mapper）
        ├── config/         # 配置类（Redis、WebSocket、OSS、JWT 等）
        ├── interceptor/    # 拦截器（登录校验、公共字段填充）
        ├── annotation/     # 自定义注解（AutoFill 等）
        ├── aspect/         # AOP 切面
        ├── context/        # ThreadLocal 上下文（用户 ID 传递）
        ├── exception/      # 自定义业务异常
        └── utils/          # 工具类（JWT、百度地图、微信支付等）
```

---

## 核心功能

### 1. 员工管理
- 员工登录/登出（JWT 认证）
- 密码加密（BCrypt 替代 MD5，自带随机盐防彩虹表攻击）

### 2. 菜品管理
- 菜品 CRUD、上下架管理
- 图片上传（阿里云 OSS）
- **缓存优化**：Redis 缓存菜品数据，布隆过滤器防穿透，随机 TTL 防雪崩

### 3. 套餐管理
- 套餐 CRUD、套餐内菜品联动
- 套餐价格自动计算
- **数据一致性**：菜品更新时同步更新套餐菜品表数据

### 4. 分类管理
- 菜品分类、套餐分类
- 分类关联校验（有关联数据时禁止删除）

### 5. 购物车
- 菜品/套餐加入购物车
- 数量增减、清空购物车

### 6. 订单管理
- 用户下单（地址校验 + 百度地图距离检测）
- 订单状态流转：待付款 → 待接单 → 已接单 → 派送中 → 已完成
- 订单取消/退款、商家拒单
- 订单超时自动取消（Spring Task 定时处理）
- 再来一单功能
- **实时通知**：WebSocket 推送来单提醒和用户催单消息

### 7. 用户管理
- 微信小程序登录（获取 openid）
- 收货地址管理

### 8. 数据统计
- 营业额统计、订单统计、用户统计
- Excel 报表导出（Apache POI）

---

## 优化与增强

### 缓存优化

| 优化项 | 方案 | 效果 |
|--------|------|------|
| 缓存穿透 | Guava BloomFilter 预检非法 ID | 无效请求直接返回，不查缓存和 DB |
| 缓存雪崩 | 随机 TTL（基础时间 + 随机偏移量） | 避免大量 Key 同时过期导致 DB 瞬时压力 |
| 热点数据缓存 | Redis 缓存套餐/菜品查询 | DB 查询量降低约 70% |

### 安全增强

| 优化项 | 方案 |
|--------|------|
| 密码加密 | MD5 → BCrypt，自带随机盐，防彩虹表和批量破解 |
| 接口文档 | Knife4j 自动生成 API 文档 |

### 业务优化

| 优化项 | 说明 |
|--------|------|
| 百度地图集成 | 下单时校验配送距离，超出范围自动拦截 |
| 源码 Bug 修复 | 修复地址修改字段不更新、套餐菜品数据不同步等问题 |

---

## 快速开始

### 环境要求

- JDK 8+
- MySQL 5.7+
- Redis 5.0+
- Maven 3.6+

### 启动步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/weishiyi-11/sky-take-out.git
   ```

2. **初始化数据库**
   - 创建 MySQL 数据库
   - 执行 `sql/` 目录下的建表脚本

3. **修改配置**
   - 编辑 `sky-server/src/main/resources/application-dev.yml`
   - 配置数据库连接信息、Redis 连接信息
   - 配置阿里云 OSS、百度地图 API 等第三方服务密钥

4. **启动项目**
   ```bash
   mvn clean install
   cd sky-server
   mvn spring-boot:run
   ```

5. **访问接口文档**
   ```
   http://localhost:8080/doc.html
   ```

---

## 接口文档

项目集成了 Knife4j，启动后访问 `http://localhost:8080/doc.html` 可查看和测试所有 API 接口。

接口分为两大模块：
- **管理端接口**：`/admin/**`（菜品、套餐、订单、员工、报表等）
- **用户端接口**：`/user/**`（购物车、下单、地址、微信登录等）

---

## 数据库设计

核心数据表：

| 表名 | 说明 |
|------|------|
| employee | 员工表 |
| category | 分类表 |
| dish | 菜品表 |
| dish_flavor | 菜品口味表 |
| setmeal | 套餐表 |
| setmeal_dish | 套餐菜品关联表 |
| user | 用户表 |
| address_book | 地址簿 |
| shopping_cart | 购物车 |
| orders | 订单表 |
| order_detail | 订单明细表 |

---

## License

本项目仅供学习参考使用。

---
