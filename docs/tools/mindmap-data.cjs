/**
 * 脑图数据源：front = 前台功能（qixidi-web-nuxt3）/ admin = 后台功能（qixidi-web-admin）
 *
 * 结构约定：
 * - children 顺序即渲染顺序，前 split 个分支排右侧，其余排左侧
 * - color 为一级分支主题色（连线 + 分支名着色）
 * - label 中的括号一律全角（），半角 () 保留给未来扩展
 */
module.exports = {
  /* ---------------- 前台：按 qixidi-web-nuxt3 实际路由核对，2026-10-29 ---------------- */
  front: {
    key: '前台功能',
    label: '栖息地前台',
    note: 'qixidi-web-nuxt3 · 52 个页面逐一核对 · 2026-10',
    split: 5,
    children: [
      // 右侧 5 分支：内容消费 → 创作 → AI → 账号
      { label: '内容发现', color: '#3E7C59', children: [
        { label: '首页', children: [
          { label: '推荐流 · 权重排序' }, { label: '站点信息侧栏' },
        ]},
        { label: '精选 / 最新 / 关注动态' },
        { label: '分类浏览' },
        { label: '标签广场 + 标签内容页' },
        { label: '文章归档 · 时间轴' },
        { label: '全局搜索', children: [
          { label: '文章 · 用户 · 标签 · 小记' },
        ]},
        { label: '工具箱 · 分类 Tab 动态下发' },
        { label: '友情链接' },
        { label: '关于站点 / 关于作者' },
      ]},
      { label: '文章与互动', color: '#4A6FA5', children: [
        { label: '文章详情', children: [
          { label: 'Markdown 渲染' }, { label: '目录导航' }, { label: '代码高亮' },
        ]},
        { label: '点赞 / 收藏' },
        { label: '评论', children: [
          { label: '三级嵌套回复' }, { label: '表情选择器' },
        ]},
        { label: '文章合集页' },
        { label: '专题页' },
      ]},
      { label: '创作中心', color: '#B0713A', children: [
        { label: '写文章', children: [
          { label: 'Markdown 编辑器' },
          { label: '视频嵌入 · B站/YouTube/本地' },
          { label: 'AI 自动生成摘要' },
          { label: '草稿箱' },
        ]},
        { label: '写时光小记' },
        { label: '发布与审核', children: [
          { label: '敏感词自动审核' },
          { label: '命中阈值转人工 + 邮件提醒' },
          { label: '审核结果站内信 + WS 推送' },
        ]},
      ]},
      // README 头号卖点：AI agent 经 MCP 以用户身份创作，与人工发布同一审核流
      { label: 'AI Agent 接入 · MCP', color: '#B23A78', children: [
        { label: '无状态 /mcp 端点' },
        { label: 'API Key 管理 · 创建/重命名/吊销' },
        { label: '两级写作 Skill · 官方规范 + 个人偏好' },
        { label: 'AI Agent 接入指引 · Claude Code 等' },
        { label: '创作能力全开放 · 写文章/发小记/传图' },
        { label: 'agent 产出走同一审核流程' },
      ]},
      { label: '账号与认证', color: '#A5563E', children: [
        { label: '登录 / 注册弹窗 · 页内弹不跳转' },
        { label: '第三方 OAuth 登录' },
        { label: '创作者申请 · 含 MCP 权益引导' },
        { label: '个人设置' },
      ]},
      // 左侧 4 分支：内容形态与用户侧
      { label: '轻内容', color: '#8A5A8F', children: [
        { label: '随笔 · 金句', children: [
          { label: '分类 / 作者 / 作品 / 专辑' }, { label: '随笔专辑管理' },
        ]},
        { label: '阅读随笔', children: [
          { label: '读书笔记列表 / 详情' }, { label: '按专辑归组' },
        ]},
        { label: '时光小记', children: [
          { label: '时间线列表 / 详情' }, { label: '评论' },
        ]},
        { label: '相册', children: [
          { label: '照片墙瀑布流' },
        ]},
      ]},
      { label: '用户与社交', color: '#2F7E86', children: [
        { label: '用户列表 · 发现用户' },
        { label: '用户主页', children: [
          { label: '文章 / 点赞' }, { label: '关注 · 粉丝' },
          { label: '小记 / 阅读随笔 / 随笔' }, { label: '专题 / 合集' }, { label: '最近动态' },
        ]},
        { label: '关注 / 粉丝关系' },
        { label: '实时私信 · WebSocket' },
        { label: '消息中心', children: [
          { label: '系统通知 / 审核通知' }, { label: '站内信 + 实时红点推送' },
        ]},
      ]},
      { label: '用户后台 admin', color: '#6B7F3A', children: [
        { label: '数据看板', children: [
          { label: '浏览 / 互动统计卡片' }, { label: '趋势图' }, { label: '投稿贡献图' },
        ]},
        { label: '内容管理', children: [
          { label: '文章管理' }, { label: '专栏 + 专栏内容' },
          { label: '随笔 / 随笔专辑' }, { label: '时光小记' }, { label: '收藏' },
        ]},
        { label: '帮助 · FAQ / 意见反馈' },
      ]},
      { label: '全局体验', color: '#7A6A55', children: [
        { label: '浅色 / 深色主题' },
        { label: '响应式 + 移动端适配' },
        { label: 'SSR 首屏直出 · SEO 友好' },
        { label: '无限滚动分页' },
      ]},
    ],
  },

  /* ---------------- 后台：按 qixidi-web-admin/src/views 目录核对，2026-10-29 ----------------
   * 说明：lover（情侣小站：相册/清单/照片树）为私人彩蛋模块，README 未宣传，不收 */
  admin: {
    key: '后台功能',
    label: '栖息地后台',
    note: 'qixidi-web-admin · Vue 2 + Element UI · 2026-10',
    split: 4,
    children: [
      // 右侧 4 分支：内容与社区业务
      { label: '内容管理', color: '#3E7C59', children: [
        { label: '文章管理' },
        { label: '专栏管理' },
        { label: '专题管理' },
        { label: '合集管理' },
        { label: '标签管理' },
      ]},
      { label: '社区互动', color: '#4A6FA5', children: [
        { label: '评论管理' },
        { label: '点赞管理' },
        { label: '随笔（金句）管理' },
        { label: '敏感词管理' },
        { label: '意见反馈' },
      ]},
      { label: '用户与消息', color: '#B0713A', children: [
        { label: '前台用户管理' },
        { label: '私信会话管理' },
        { label: '站内消息管理' },
        { label: '搜索记录' },
      ]},
      { label: '站点配置', color: '#B23A78', children: [
        { label: '导航配置' },
        { label: '侧栏配置' },
        { label: '站点文件' },
        { label: '充值配置' },
        { label: '系统任务配置' },
      ]},
      // 左侧 3 分支：系统与运维（RuoYi 体系）
      { label: '系统管理', color: '#6B7F3A', children: [
        { label: '管理员账号' },
        { label: '角色管理 · RBAC 权限' },
        { label: '菜单管理' },
        { label: '部门 / 岗位管理' },
        { label: '字典 / 参数设置' },
        { label: '通知公告' },
        { label: 'OSS 文件管理' },
      ]},
      { label: '系统监控', color: '#2F7E86', children: [
        { label: '在线用户' },
        { label: '缓存监控' },
        { label: '数据监控 · Druid' },
        { label: '登录 / 操作日志' },
        { label: '定时任务 · XXL-Job' },
      ]},
      { label: '开发工具', color: '#7A6A55', children: [
        { label: '表单构建' },
        { label: '代码生成' },
        { label: '接口文档 · Swagger' },
      ]},
    ],
  },
};
