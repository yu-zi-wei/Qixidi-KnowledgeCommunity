# Skills 索引

栖息地博客项目的业务模块知识库。

---

## 前端模块

### 文章模块
- **[article/article-form](article/article-form.md)** - 文章编辑发布模块
  - 接口定义
  - 字段映射表
  - 数据转换逻辑
  - 常见问题和解决方案

### 评论模块
- **[comment/threaded-comment-section](comment/threaded-comment-section.md)** - 通用评论区组件
  - 函数 props 注入 API 的使用方式
  - 新业务接入评论的步骤
  - 组件命名/类名前缀约束

### 表单弹窗
- **[form/create-dialog](form/create-dialog.md)** - 下拉旁新建 + 创建弹窗模式
  - 三层数据流（弹窗 → 表单组件 → 页面刷新）
  - 弹窗组件契约（show/success/重置/OSS 上传）
  - 后台前台创建接口配套（返回新 id 供自动选中）

---

## 后端模块

### 评论模块
- **[comment/comment-module](comment/comment-module.md)** - 评论后端通用模式
  - 扁平楼层表设计（parent_id 一级评论 = 内容 id）
  - 平行代码结构（entity/bo/vo/mapper/service/controller）
  - b_dictum_comment 已知坑（uids 收集遗漏 NPE、删除无本人校验）

### 消息通知模块
- **[news/news-module](news/news-module.md)** - 站内消息通知接入指南
  - 双表分工（b_news_user_record 才是互动通知主表）
  - 写入模板（异步线程池 + WebSocket 推送）
  - 子类型合并模式（随笔 6/小记 7 合并进"评论"）
  - /news 页二级 tab 模式

---

## 使用指南

当开发相关功能时，先加载对应的 skill 文件，了解：
1. 接口定义和位置
2. 字段映射关系
3. 常见陷阱和解决方案
4. 开发检查清单

这样可以避免重复踩坑，提高开发效率。
