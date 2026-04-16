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

// 后台专辑列表项
export interface AdminSpecialItem {
  id: number
  specialName: string
  specialIntroduce?: string
  cover?: string
  articleNumber?: number
  includedCount?: number
  createTime?: string
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

// ==================== 随笔评论相关 ====================

// 随笔评论
export interface DictumCommentVo {
  id: string | number            // 评论 id（大整数，保持字符串避免精度丢失）
  dictumId: string | number    // 随笔 id（大整数）
  uid: string                   // 随笔用户 id
  parentId: string | number    // 父级评论 id（一级评论的 parentId = dictumId）
  commentGrade: number          // 评论等级：1=一级，2=二级，3=三级及以下
  targetId: string              // 目标 id（被回复的评论 id 或随笔 id）
  targetUid: string             // 目标用户 id
  commentUid: string            // 评论人 id
  content: string               // 评论内容
  type: number                  // 评论类型：1=名言，2=评论
  status: number                // 评论状态：0=正常，1=已删除
  createTime: string
  updateTime?: string
  username: string              // 评论用户名
  nickname: string              // 评论用户昵称
  avatar: string                // 评论用户头像
  targetUsername?: string       // 目标评论用户名
  targetNickname?: string       // 目标评论用户昵称
  targetAvatar?: string         // 目标评论用户头像
  dictumCommentVoList?: DictumCommentVo[]  // 次级评论集合
}

// 新增随笔评论请求
export interface DictumCommentBo {
  dictumId: string | number     // 随笔 id（大整数，保持字符串避免精度丢失）
  worksContent?: string         // 目标内容
  uid: string                   // 随笔用户 id
  parentId: string | number     // 父级评论 id（大整数）
  commentGrade: number          // 评论等级：1=一级，2=二级，3=三级及以下
  targetId: string | number     // 目标 id（大整数）
  targetUid: string             // 目标用户 id
  commentUid?: string           // 评论人 id（后端自动填充）
  content: string               // 评论内容
  type: number                  // 评论类型：1=名言，2=评论
  status?: number               // 评论状态
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
  includedCount?: number  // 收录数
}

// 收藏夹详情（后台管理用）
export interface CollectionItem {
  id: number
  collectionName: string
  collectionIntroduce: string
  state: number
  uid: string
  username: string
  updateId: string
  createTime: string
  includedCount: number
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

// ==================== 随笔相关 ====================

// 随笔信息
export interface ReadingEssaysInfo {
  id: number
  uid: string              // 用户 id
  content: string          // 内容
  contentMd?: string       // Markdown 内容
  groupId: number          // 分类 id
  groupName?: string       // 分类名称
  albumId?: number         // 专辑 id
  albumName?: string       // 专辑名称
  author?: string          // 作者
  worksName?: string       // 作品名称
  label?: string           // 标签（逗号分隔）
  labelList?: string[]     // 标签列表
  picture?: string         // 图片（逗号分隔）
  pictureList?: string[]   // 图片列表
  dictumState?: number     // 状态：1=公开，2=私有，3=关注可看
  state?: number           // 状态：0=正常，2=已删除
  createTime: string
  updateTime?: string
  nickname?: string        // 用户名称
  avatar?: string          // 头像
  occupation?: string      // 职业
  roleId?: string          // 角色
  location?: string        // 位置
  helpSum?: number         // 点赞总数
  commentSum?: number      // 评论总数
  tripartiteUser?: unknown // 用户详情信息
}

// 热门作者（后端返回的 Map 结构）
export interface ReadingEssaysAuthor {
  author: string           // 作者名称
  count: string            // 随笔数量（字符串格式）
}

// 热门标签（后端返回的 Map 结构）
export interface ReadingEssaysLabel {
  label: string            // 标签名称
  count: string            // 使用次数（字符串格式）
}

// 随笔分类
export interface ReadingEssaysGroup {
  id: number
  employSum: number        // 收录数
  name: string             // 分组名称
  cover?: string           // 封面
  briefIntroduction?: string // 简介
  state?: number           // 状态：0=正常，1=已删除
  createTime: string
  updateTime?: string
  nickname?: string        // 用户名称
  avatar?: string          // 头像
  occupation?: string      // 职业
  roleId?: string          // 角色
  location?: string        // 位置
}

// 随笔专辑
export interface ReadingEssaysAlbum {
  id: number
  uid: string              // 用户 id
  name: string             // 专辑名称
  cover?: string           // 封面
  briefIntroduction?: string // 简介
  albumState?: number      // 专辑状态：1=公开，2=私有，3=关注可看
  employSum?: number       // 收录总数
  recommendRate?: number   // 推荐率
  helpSum?: number         // 点赞总数
  followSum?: number       // 关注总数
  state?: number           // 状态：0=正常，1=已删除
  createTime: string
  updateTime?: string
}

// ==================== 时光小记相关 ====================

// 时光小记信息
export interface TimeNotes {
  id: number
  title: string            // 标题
  content: string          // 内容
  uid: string             // 用户 id
  recordTime: string      // 记录时间 (yyyy-MM-dd)
  isContent?: boolean     // 是否有详情内容
  createBy?: string       // 创建人
  createTime?: string     // 创建时间
  updateTime?: string     // 更新时间
}

// 时光小记列表项（按日期分组）
export interface TimeNotesVo {
  recordTime: string       // 记录时间
  list: TimeNotes[]        // 该日期下的小记列表
}

// 时光小记详情（继承 TimeNotes）
export interface TimeNotesInfo extends TimeNotes {
  isAuthor?: number         // 是否为作者（0=是，1=不是）
  createBy?: string         // 创建人名称
}

// 时光小记搜索请求
export interface TimeNotesSearchBo {
  pageNum?: number
  pageSize?: number
  startTime?: string       // 开始时间
  endTime?: string         // 结束时间
  title?: string           // 标题搜索
}

// 时光小记增删改请求
export interface TimeNotesBo {
  id?: number              // 编辑模式存在
  title: string            // 标题（必填）
  content?: string         // 内容
  uid?: string             // 用户id
  recordTime: string       // 记录时间（必填，yyyy-MM-dd）
}

// ==================== 用户统计相关 ====================

// 用户统计数据
export interface UserCensusCount {
  articleCount: number      // 文章总数
  columnCount: number       // 专栏数
  collectionCount: number   // 收藏夹数
  followCount: number       // 关注数
  commentCount: number      // 总获评论数
  albumCount: number        // 专辑总数
  essayCount: number        // 阅读随笔
  timeNotesCount: number    // 时光小记数
}

// 用户投稿记录项（接口返回格式）
export interface UserSubmissionItem {
  dateTimes: string         // 日期
  censusSum: number         // 投稿数量
}

// 用户投稿记录（日期 -> 数量映射，用于图表组件）
export type UserSubmissionRecord = Record<string, number>

// ==================== 后台文章管理相关 ====================

// 后台文章列表项
export interface AdminArticleItem {
  id: number
  articleTitle: string
  articleCover: string
  articleAbstract: string
  groupingName: string
  auditState: number       // 1=审核中, 2=已发布, 3=审核不通过, 4=草稿
  state: number            // 0=正常, 1=已删除
  isPublic: number         // 1=公开, 2=不公开
  likeTimes: number
  numberTimes: number
  commentTimes: number
  collectionTimes: number
  createTime: string
  updateTime: string
}

// 文章审核状态
export const ArticleAuditState = {
  REVIEWING: 1,    // 审核中
  PUBLISHED: 2,    // 已发布
  REJECTED: 3,     // 审核不通过
  DRAFT: 4         // 草稿
} as const

// ==================== 消息模块 ====================

// 消息汇总
export interface NewsUserSumVo {
  type: number           // 消息类型
  typeInfo: string       // 类型描述
  route: string          // 路由路径
  newsSum: number        // 未读数
}

// 通用消息（点赞 type=2、关注 type=3）
export interface NewsUserInfoVo {
  newsId: number
  targetId: number
  newsTitle: string
  newsContent: string
  type: number
  beenRead: number
  senderId: string
  senderName: string
  senderAvatar: string
  recipientId: string
  createTime: string
}

// 评论消息（type=1）
export interface ArticleCommentNewsVo {
  id: number
  newsId: number
  articleId: number
  articleTitle: string
  uid: string
  parentId: number
  commentGrade: number       // 评论等级（1：一级，2：二级，3：三级及以下）
  targetId: string
  targetUid: string
  commentUid: string
  commentName: string
  commentAvatar: string
  content: string
  type: number
  beenRead: number
  createTime: string
}

// ==================== 私信模块 ====================

// 私信用户列表项
export interface PrivateUserVo {
  id: number
  uid: string                // 当前用户 id
  targetUid: string          // 对方用户 id
  lastNews: string           // 最后一条消息
  unreadCount: number        // 未读消息条数
  targetName: string         // 对方用户名称
  targetAvatar: string       // 对方用户头像
  targetOccupation: string   // 对方职业
  createTime: string
  updateTime: string
}

// 私信消息记录
export interface PrivateNewsInfoVo {
  id: number
  uid: string                // 发送者 id
  nickname: string           // 发送者名称
  userAvatar: string         // 发送者头像
  newsComment: string        // 消息内容
  replyTargetUid: string     // 接收者 id
  timeMark: number           // 0:与上一条间隔<20分钟, 1:>20分钟
  beenRead: number           // 1:未读, 2:已读
  createTime: string
  updateTime: string
}

// 发送私信请求
export interface PrivateNewsInfoBo {
  newsComment: string        // 消息内容
  replyTargetUid: string     // 目标用户 id
}

// 用户在线状态
export interface UserSimpleInfoVo {
  isOnline: boolean
  username: string
  nickname: string
  avatar: string
  occupation: string
}
