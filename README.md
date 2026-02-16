# 🌌 Sky Take-Out 餐饮外卖 SaaS

一个涵盖 **微信小程序 + 管理后台 + Spring Boot 后端** 的外卖 SaaS 学习项目。  
支持实时来单提醒、订单全流程、工作台统计等功能。

---

## ✨ 主要特性

| 模块            | 说明                                                                              |
| --------------- | --------------------------------------------------------------------------------- |
| 用户端接口      | 小程序登录、下单、微信支付沙箱/Mock、催单、历史订单、再来一单                       |
| 管理端接口      | 员工登录、菜品/套餐 CRUD、订单派送流转、工作台数据统计                              |
| 实时提醒        | WebSocket + 前端音效：新订单、客户催单                                            |
| 定时任务        | `@Scheduled`：<br>• 待付款 15 min 自动取消<br>• 派送 60 min 自动完成               |
| 统一异常        | 业务异常、全局异常处理                                                             |
| 通用组件        | 微信支付封装、阿里云 OSS、JWT、Redis 缓存                                         |
| 接口文档        | Knife4j (Swagger3) — `http://localhost:8080/doc.html`                              |

---

## 🛠️ 技术栈

* Spring Boot 2.7 • Spring MVC • MyBatis  
* MySQL 8 • Redis 6 • Druid  
* PageHelper • Lombok • MapStruct  
* WebSocket (JSR-356)  
* Knife4j • FastJSON • Maven 多模块  

---

## 📂 目录结构

```
sky-take-out
├─ sky-server               # Web 服务
│  ├─ java/com/sky
│  │  ├─ controller         # admin / user / websocket / notify
│  │  ├─ service / impl
│  │  ├─ mapper
│  │  ├─ task               # 定时任务
│  │  ├─ config             # Spring 配置
│  │  └─ utils              # 支付、OSS、JWT...
│  ├─ resources
│  │  ├─ mapper/*.xml
│  │  └─ static/backend     # 管理后台静态页面
│  └─ application-*.yml
├─ sky-common               # 通用工具、常量、异常
├─ sky-pojo                 # DTO / VO / Entity
└─ docs                     # 设计文档与截图
```

---

## 🚀 快速启动

### 环境要求

| 组件 | 版本 | 备注 |
| ---- | ---- | ---- |
| JDK  | 11   |      |
| MySQL| 8.x  | 创建库 `sky_take_out` |
| Redis| 6.x  | 默认 6379 |
| Maven| 3.6+ |      |

> `application-dev.yml` 内含微信沙箱 / OSS 等配置，请根据实际修改。

### 编译运行

```bash
git clone https://github.com/<yourname>/sky-take-out.git
cd sky-take-out
mvn clean package -DskipTests
java -jar sky-server/target/sky-server.jar --spring.profiles.active=dev
```

### 访问地址

| URL                              | 说明         |
| -------------------------------- | ------------ |
| `http://localhost:8080`          | 管理后台页面 |
| `http://localhost:8080/doc.html` | API 文档     |
| 微信小程序                       | 配置后台域名指向 8080 |

---

## 🔄 订单流程

1. 小程序下单 `/user/order/submit`  
2. 付款 `/user/order/payment` → `paySuccess`  
3. WebSocket 推送 “来单提醒” 至后台  
4. 管理员接单 → 派送 → 完成  
5. 工作台数据实时更新，定时任务兜底超时流程  

---

## ⚠️ 常见问题

| 问题                               | 解决方案 |
| ---------------------------------- | -------- |
| 工作台待接单 / 待派送数量不变      | 确认 `orders.status` 更新 & `countByMap` SQL 过滤 `status` |
| 来单提示音持续循环                 | 注释 `WebSocketTask` 的 `@Scheduled`，改由业务事件触发 |
| 访问 `/` 404                       | `static/backend/index.html` 未打包或资源映射缺失 |
| Springfox NPE (`documentationPluginsBootstrapper`) | `PathMatchConfigurer#setPatternParser(null)` |

---

## 📜 License

[MIT](LICENSE)

> 本项目仅供学习与交流，禁止商业用途。
