/**
 * 全局类型定义
 */

/**
 * 非分页接口响应（被 R.java 包裹）
 */
export interface R<T = any> {
  code: number
  msg: string
  data: T
}

/**
 * 分页接口响应（TableDataInfo 直接返回，不被 R 包裹）
 */
export interface TableDataInfo<T = any> {
  total: number
  rows: T[]
}

export interface PageQuery {
  pageNum?: number
  pageSize?: number
  [key: string]: any
}

// 导航栏
export interface Navigation {
  id: number
  parentId: number
  navigationName: string
  navigationIcon: string
  iconColor: string
  route: string
  order: number
  jurisdiction: number
  isList: number
  status: number
  type: number
  levelList: Navigation[]
}

// 侧边栏（二级导航）
export interface Sidebar {
  id: number
  parentId: number | null
  sidebarName: string
  sidebarIcon: string
  iconColor: string
  order: number
  jurisdiction: number
  route: string
  isList: number
  status: number
  type: number
  levelList: Sidebar[]
}

// 标签分组
export interface LabelGrouping {
  id: number
  groupingName: string
  icon: string
  articleNumber: number
  state: number
  groupingDescribe: string
  createBy: number
  createTime: string
}

// 站点统计数据
export interface SiteStats {
  userCount: number
  articleCount: number
  specialCount: number
  labelCount: number
  labelGroupCount: number
  dictumCount: number
  timeNotesCount: number
}

// 站点信息（备案等）
export interface SiteInfo {
  realmName: string
  filings: string
  securityFilings: string
  mailbox: string
  createTime: string
}

// 文章信息（列表用）
export interface ArticleInfo {
  id: number
  userId: string
  articleTitle: string
  articleCover: string
  articleAbstract: string
  groupingName: string
  numberTimes: number
  likeTimes: number
  commentTimes: number
  createTime: string
  nickname: string
  username: string
  avatar: string
}

// 关注用户
export interface FollowUser {
  id: number
  userId: string
  nickname: string
  avatar: string
  username: string
  introduce?: string
}

// 登录请求
export interface LoginForm {
  username: string
  password: string
}

// 登录响应
export interface LoginResult {
  token: string
  uuid: string
}

// 注册 / 重置密码请求
export interface RegisterForm {
  registerType: number  // 1: 注册, 2: 重置密码
  email: string
  password: string
  code: string
}

// 用户信息（getInfo 响应）
export interface UserInfo {
  uuid: string
  username: string
  nickname: string
  avatar: string
  email?: string
  phone?: string
  occupation?: string
  introduce?: string
  gender?: string
  location?: string
  company?: string
  blog?: string
  grade?: string
  empirical?: number
  articleCount?: number
  collectionCount?: number
  followCount?: number
  fansFollowCount?: number
  dictumCount?: number
  timeNotesCount?: number
  specialColumnCount?: number
}

// getInfo 响应结构
export interface GetInfoResult {
  isLogin: boolean
  user?: UserInfo
  roles?: string[]
  permissions?: string[]
}

// ==================== 文章编辑相关 ====================

// 文章表单（新增/编辑）
export interface ArticleForm {
  id?: number                    // 编辑模式存在
  articleTitle: string           // 标题
  articleContent: string         // Markdown 内容
  articleCover?: string          // 封面图 URL
  articleAbstract?: string       // 摘要
  abstractSelect?: boolean       // 是否使用 AI 生成摘要（后端字段名）
  type?: 1 | 2 | 3              // 文章类型：1原创 2转载 3翻译
  reprintUrl?: string            // 转载/翻译地址
  groupingId?: number            // 分类 ID
  labelId?: string               // 标签 ID（逗号分隔，如："1,2,3"）
  specialId?: number             // 专栏 ID（可选）
  status?: 0 | 1 | 2            // 0:草稿 1:已发布 2:下架
}

// 分类信息
export interface GroupingInfo {
  id: number
  groupingName: string
  icon?: string
  articleNumber?: number
  state?: number
  groupingDescribe?: string
}

// 标签信息
export interface LabelInfo {
  id: number
  labelName: string
  labelColor?: string
  articleNumber?: number
  state?: number
}

// 专栏信息
export interface SpecialInfo {
  id: number
  specialName: string
  specialDescribe?: string
  articleNumber?: number
  coverImage?: string
}

// 搜索记录
export interface SearchRecordsVo {
  id: number
  uid: string
  content: string
  createTime: string
}

// ==================== 评论相关 ====================

// 文章评论
export interface ArticleCommentVo {
  id: number
  articleId: number
  uid: string              // 文章作者 id
  parentId: number         // 父级评论 id（一级评论的 parentId = articleId）
  commentGrade: number     // 评论等级：1=一级，2=二级，3=三级及以下
  targetId: string         // 目标 id（被回复的评论 id）
  targetUid?: string       // 目标用户 id
  commentUid?: string      // 评论人 id
  content: string          // 评论内容
  type?: number            // 评论类型：1=文章，2=评论
  state?: number           // 评论状态：0=正常，1=已删除
  createTime: string
  updateTime?: string
  targetAvatar?: string    // 目标用户头像
  targetName?: string      // 目标用户名称
  commentName: string      // 评论人用户名称
  commentAvatar: string    // 评论人用户头像
  mountComment: ArticleCommentVo[]  // 子评论列表（二级及以下）
  commentTotal?: number    // 评论总数
}

// 新增评论请求
export interface ArticleCommentBo {
  id?: number
  articleId: number        // 文章 id
  worksContent?: string    // 目标内容
  uid: string              // 文章用户 id
  parentId: number         // 父级评论 id
  commentGrade: number     // 评论等级：1=一级，2=二级，3=三级及以下
  targetId: number         // 目标 id
  targetUid: string        // 目标用户 id
  commentUid?: string      // 评论人 id
  content: string          // 评论内容
  type: number             // 评论类型：1=文章，2=评论
  state?: number           // 评论状态
}

// ==================== 点赞相关 ====================

// 点赞/取消点赞请求
export interface FabulousBo {
  typeId: number          // 类型 id（同文章 id）
  targetId: number         // 目标 id（文章 id）
  targetUid: string        // 目标用户 id（文章作者 id）
  type: number            // 类型：1=文章
  state: number           // 状态：0=正常
  fabulousSum: number     // 点赞总数
  targetTitle: string     // 目标标题（文章标题）
  labelId?: string        // 标签 id（可选，文章标签）
}

// ==================== 收藏相关 ====================

// 收藏夹
export interface CollectionFolder {
  id: number
  collectionName: string
  collectionIntroduce?: string
}

// 创建收藏夹请求
export interface CreateCollectionBo {
  collectionName: string
  collectionIntroduce?: string
}

// 收藏文章请求
export interface AddCollectionBo {
  collectionId: number    // 收藏夹 id
  targetId: number        // 目标 id（文章 id）
  type: number            // 类型：1=文章
  labelId?: string        // 标签 id（文章标签）
}
