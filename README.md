<p align=center>
   <img src="./img/logo.png" alt="栖息地" style="width:160px;height:160px"/>
<h1 align=center>栖息地 v-2.0</h1>
<p align="center">
<img align=center src="https://gitee.com/yu-zi-wei/qixidi/badge/star.svg?theme=dark" ></img>
<img align=center src="https://gitee.com/yu-zi-wei/qixidi/badge/fork.svg?theme=dark" ></img>
</p>
</p>

## 前言

栖息地是一个开源的知识社区博客系统，它的诞生源于一个简单的想法——拥有一个完全属于自己的知识空间。(还在持续开发中，敬请期待......)

1.0 版本是基于 RuoYi 二次开发构建的，随着功能不断迭代，早期架构的局限逐渐暴露：代码耦合度高、难以维护、前端技术栈老化。与其在一个摇摇欲坠的地基上反复修补，不如推倒重来。

于是 2.0 诞生了。这不是一次简单的升级，而是从零开始的全面重构：

- **后端**：基于 Spring Boot 3 + Java 17 全新设计，严格遵循 MVC 分层架构
- **前端**：基于 Nuxt 3 + Vue 3 + Naive UI 全面重写，SSR 渲染 + 响应式设计
- **AI 驱动开发**：整个 2.0 版本从架构设计到代码实现，全程由 AI 参与开发和维护

> **1.0 版本**已归档至 [main-v1.0](https://gitee.com/yu-zi-wei/qixidi/tree/main-v1.0/) 分支，不再维护（bug除外）。

### 网站地址：https://qixidi.top/

### 开源地址（Gitee）：https://gitee.com/yu-zi-wei/qixidi

### 开源地址（GitHub）：https://github.com/yu-zi-wei/Qixidi-KnowledgeCommunity

## 目录结构

```text
qixidi/
├── qixidi-service        # 后端服务（Spring Boot 3 + Java 17）
│   ├── qixidi-startup    # 启动模块
│   ├── framework         # 核心框架
│   ├── qixidi-auth       # 认证模块
│   ├── qixidi-business   # 核心业务
│   ├── qixidi-system     # 系统管理
│   └── qixidi-common     # 公共模块
│
├── qixidi-web-nuxt3      # 门户网站（Nuxt 3 + Vue 3 + Naive UI）
│
└── qixidi-web-admin      # 后台管理（Vue.js）
```

## 主要技术栈

### 后端

| 名称 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.2 | 基础框架 |
| Sa-Token | 1.37.0 | 认证框架 |
| MyBatis-Plus | 3.5.7 | ORM 框架 |
| Spring WebSocket | 3.4.2 | 长连接 |
| Aliyun OSS | 3.14.0 | 云存储 |
| MinIO | 8.3.8 | 对象存储 |
| MySQL | 8.0 | 数据库 |
| Redis | 6.0 | 缓存 |

### 前端

| 名称 | 版本 | 说明 |
|------|------|------|
| Node.js | v18~v22 | 运行环境 |
| Nuxt | 3.17 | SSR 框架 |
| Vue | 3.x | 前端框架 |
| Naive UI | 2.43 | UI 组件库 |
| Pinia | 3.0 | 状态管理 |
| Tailwind CSS | 4.2 | 工具类样式 |
| @vicons/tabler | 0.13 | 图标库 |

### 技术架构图

![技术架构图.png](img%2F%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 功能点

### 前台功能点

![前台功能.png](img%2F%E5%89%8D%E5%8F%B0%E5%8A%9F%E8%83%BD.png)

### 后台功能点

![后台功能.png](img%2F%E5%90%8E%E5%8F%B0%E5%8A%9F%E8%83%BD.png)

### 后台页面截图

![后台首页.png](img%2F%E5%90%8E%E5%8F%B0%2F%E5%90%8E%E5%8F%B0%E9%A6%96%E9%A1%B5.png)

![用户管理.png](img%2F%E5%90%8E%E5%8F%B0%2F%E7%94%A8%E6%88%B7%E7%AE%A1%E7%90%86.png)

![文章列为.png](img%2F%E5%90%8E%E5%8F%B0%2F%E6%96%87%E7%AB%A0%E5%88%97%E4%B8%BA.png)

![系统任务.png](img%2F%E5%90%8E%E5%8F%B0%2F%E7%B3%BB%E7%BB%9F%E4%BB%BB%E5%8A%A1.png)

![导航配置.png](img%2F%E5%90%8E%E5%8F%B0%2F%E5%AF%BC%E8%88%AA%E9%85%8D%E7%BD%AE.png)

## 最后

"问题咨询可以添加我的微信：zsh2978824265"

![wx.png](img%2Fwx.png)

————帅哥美女们看到这了，点个 Star 再走吧！
