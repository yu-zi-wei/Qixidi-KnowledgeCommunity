<p align=center>
   <img src="./img/logo.png" alt="栖息地" width="160" height="160"/>
</p>
<h1 align=center>栖息地</h1>
<p align="center">
<img align=center src="https://gitee.com/yu-zi-wei/qixidi/badge/star.svg?theme=dark" ></img>
<img align=center src="https://gitee.com/yu-zi-wei/qixidi/badge/fork.svg?theme=dark" ></img>
</p>

## 前言

栖息地是一个开源的多人知识社区博客系统，它的诞生源于一个简单的想法——拥有一个完全属于自己的知识空间。

这是 **AI 时代的知识社区和博客**：除了常规的创作与社交，你可以把自己的账号安全地交给 AI agent——创建一个 API key，Claude Code 等 agent 即可通过 MCP 协议以你的身份写文章、发小记、上传配图、管理内容，并遵循平台官方写作规范与你的个人写作偏好。AI 帮你写，你来审。

技术上，后端 Spring Boot 4 + Java 17 + Spring AI2.0 前端 Nuxt 3 + Vue 3 + Naive UI，全程 AI 驱动开发。

### 网站地址：https://qixidi.top/

### 开源地址

Gitee：https://gitee.com/yu-zi-wei/qixidi

GitHub：https://github.com/yu-zi-wei/Qixidi-KnowledgeCommunity

### 默认账号密码（仅限本地）
> 前台：123@qq.com/123456
> 
> 后台：admin/123456
---

## 目录结构

```text
qixidi/
│
├── qixidi-service  # 项目后端（Spring Boot 4 + Java 17）
│   │
│   ├── qixidi-startup  # 启动模块
│   │
│   ├── framework  # 核心框架
│   │
│   ├── qixidi-auth  # 认证模块（Sa-Token）
│   │
│   ├── qixidi-ai  # AI 能力模块（Spring AI：对话、摘要生成）
│   │
│   ├── qixidi-mcp  # MCP 服务模块（无状态 /mcp 端点 + API key + skill 管理）
│   │
│   ├── qixidi-business  # 核心业务
│   │
│   ├── qixidi-system  # 系统管理
│   │
│   ├── qixidi-common  # 公共模块
│   │
│   └── db  # sql文件（需要可加微信（请注明来意，sql是有偿的：169元/包售后）：zsh2978824265）
│
├── qixidi-web-nuxt3  # 门户 + 用户后台（Nuxt 3 SSR）
│
└── qixidi-web-admin  # 旧版管理后台（Vue 2，维护模式）

```

## 亮点特性

除了常规博客功能，栖息地还有一些让你眼前一亮的特性：

**AI Agent 接入（MCP）**

- 标准无状态 `/mcp` 端点：Claude Code 等 agent 配置一个 API key 即可接入，创作能力全开放给 AI
- agent 以你的身份写文章、发小记、上传配图、查改内容，产出与人工发布走同一套审核流程
- 两级写作 skill：官方「栖息地写作规范」保证 AI 产出能被站点正确渲染，个人自建 skill 沉淀你的写作偏好，AI 越用越懂你

**Markdown 编辑器内置多媒体支持**

- 直接嵌入 **B 站视频**，输入视频链接即可预览播放
- 支持 YouTube、本地视频上传等多种视频来源
- 一键切换 **AI 自动生成摘要**，告别手动写摘要的烦恼

**实时社交互动**

- 基于 WebSocket 的**实时私信**和**消息通知**
- 三级嵌套评论、表情选择器，互动体验不打折
- 关注、点赞、收藏，完整的社区互动链路

**现代阅读体验**

- SSR 服务端渲染，首屏秒开，SEO 友好
- 浅色/深色主题一键切换，护眼不打烊
- 全站响应式设计，手机端也能舒适阅读

**内容管理**

- 阅读随笔：打造你的专属读书笔记空间
- 随笔：金句摘录，按分类 / 作者 / 作品 / 专辑沉淀
- 小记功能：随时记录灵感碎片
- 相册：照片墙瀑布流展示
- 文章合集：自定义收藏夹，分类整理文章

---

## 主要技术栈

### 后端

| 名称               | 版本     | 说明     |
|------------------|--------|--------|
| Spring Boot      | 4.0.1  | 基础框架   |
| Spring AI        | 2.0.1  | AI 能力（对话/摘要） |
| qixidi-mcp       | 自研模块  | MCP 端点 + API key 会话桥，AI agent 接入 |
| Sa-Token         | 1.46.0 | 认证框架   |
| MyBatis-Plus     | 3.5.17 | ORM 框架 |
| Spring WebSocket | 7.0.2  | 长连接    |
| Aliyun OSS       | 3.14.0 | 云存储    |
| MinIO            | 8.3.8  | 对象存储   |
| MySQL            | 8.0    | 数据库    |
| Redis            | 6.0    | 缓存     |

### 前端

| 名称             | 版本   | 说明     |
|----------------|------|--------|
| Node.js        | v21+ | 运行环境   |
| Nuxt           | 3.21 | SSR 框架 |
| Vue            | 3.x  | 前端框架   |
| Naive UI       | 2.43 | UI 组件库 |
| Pinia          | 3.0  | 状态管理   |
| Tailwind CSS   | 4.2  | 工具类样式  |
| @vicons/tabler | 0.13 | 图标库    |

### 技术架构图

![技术架构图.png](img%2F技术架构图.png)

---

## 页面预览

### 前台

<table>
<tr>
<td><img src="img/前台/首页.png" alt="首页" /></td>
<td><img src="img/前台/文章详情页.png" alt="文章详情页" /></td>
</tr>
<tr>
<td align="center">首页</td>
<td align="center">文章详情页</td>
</tr>
<tr>
<td><img src="img/前台/文章编辑发布页.png" alt="文章编辑发布页" /></td>
<td><img src="img/前台/阅读随笔.png" alt="阅读随笔" /></td>
</tr>
<tr>
<td align="center">文章编辑发布（支持 B 站视频嵌入）</td>
<td align="center">阅读随笔</td>
</tr>
<tr>
<td><img src="img/前台/小记.png" alt="小记" /></td>
<td><img src="img/前台/私信页面.png" alt="私信" /></td>
</tr>
<tr>
<td align="center">小记</td>
<td align="center">实时私信</td>
</tr>
<tr>
<td><img src="img/前台/用户主页.png" alt="用户主页" /></td>
<td><img src="img/前台/用户后台.png" alt="用户后台" /></td>
</tr>
<tr>
<td align="center">用户主页</td>
<td align="center">用户后台</td>
</tr>
</table>

### 后台

<table>
<tr>
<td><img src="img/后台/后台首页.png" alt="后台首页" /></td>
<td><img src="img/后台/用户管理.png" alt="用户管理" /></td>
</tr>
<tr>
<td align="center">后台首页</td>
<td align="center">用户管理</td>
</tr>
<tr>
<td><img src="img/后台/文章列为.png" alt="文章管理" /></td>
<td><img src="img/后台/系统任务.png" alt="系统任务" /></td>
</tr>
<tr>
<td align="center">文章管理</td>
<td align="center">系统任务</td>
</tr>
<tr>
<td><img src="img/后台/导航配置.png" alt="导航配置" /></td>
<td></td>
</tr>
<tr>
<td align="center">导航配置</td>
<td></td>
</tr>
</table>

---

## 功能点

### 前台功能点

![前台功能](img/前台功能.svg)

### 后台功能点

![后台功能](img/后台功能.svg)

---

---

## 最后

问题咨询可以添加我的微信：zsh2978824265

![wx.png](img%2Fwx.png)

————帅哥美女们看到这了，点个 Star 再走吧！
