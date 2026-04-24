<template>
  <div class="news-page">
    <!-- 竖向菜单 -->
    <nav class="news-sidebar">
      <template v-for="tab in tabs" :key="tab.type">
        <span
          v-if="tab.type === 4 && isMobile"
          class="news-tab disabled"
        >
          <span class="tab-label">{{ tab.label }}</span>
          <span v-if="getUnreadCount(tab.type) > 0" class="tab-badge">
            {{ getUnreadCount(tab.type) > 99 ? '99+' : getUnreadCount(tab.type) }}
          </span>
        </span>
        <NuxtLink
          v-else
          :to="{ path: '/news', query: tab.type === 1 ? undefined : { type: tab.type } }"
          class="news-tab"
          :class="{ active: currentType === tab.type }"
          @click.prevent="switchTab(tab.type)"
        >
          <span class="tab-label">{{ tab.label }}</span>
          <span v-if="getUnreadCount(tab.type) > 0" class="tab-badge">
            {{ getUnreadCount(tab.type) > 99 ? '99+' : getUnreadCount(tab.type) }}
          </span>
        </NuxtLink>
      </template>
    </nav>

    <!-- 内容区 -->
    <div class="news-content">
      <!-- 私信 -->
      <div v-if="currentType === 4" class="chat-wrapper">
        <!-- 左侧：用户列表 -->
        <div class="chat-user-list">
          <div v-if="chatUserLoading" class="chat-loading"><n-spin size="small" /></div>
          <div v-else-if="!chatUserList.length" class="chat-empty"><CommonEmptyState description="暂无私信" size="small" /></div>
          <template v-else>
            <div
              v-for="user in chatUserList"
              :key="user.targetUid"
              class="chat-user-item"
              :class="{ active: selectedTargetUid === user.targetUid }"
              @click="selectChatUser(user)"
            >
              <div class="chat-user-avatar-wrap">
                <img :src="user.targetAvatar || defaultAvatar" class="chat-user-avatar" alt="" @error="onAvatarError" />
                <span class="chat-online-dot" v-if="onlineMap[user.targetUid]"></span>
              </div>
              <div class="chat-user-info">
                <div class="chat-user-top">
                  <span class="chat-user-name">{{ user.targetName }}</span>
                  <time class="chat-user-time">{{ formatTime(user.updateTime) }}</time>
                </div>
                <div class="chat-user-bottom">
                  <span class="chat-user-last">{{ user.lastNews }}</span>
                  <span v-if="user.unreadCount > 0" class="chat-unread-badge">{{ user.unreadCount > 99 ? '99+' : user.unreadCount }}</span>
                </div>
              </div>
              <button class="chat-user-delete" title="删除" @click.stop="handleDeleteUser(user)">×</button>
            </div>
          </template>
        </div>

        <!-- 右侧：聊天区域 -->
        <div class="chat-panel">
          <!-- 未选择用户 -->
          <div v-if="!selectedTargetUid" class="chat-panel-empty">
            <CommonEmptyState description="选择一个用户开始聊天" size="small" />
          </div>

          <template v-else>
            <!-- 头部 -->
            <div class="chat-panel-header">
              <NuxtLink :to="`/user-home/article/${selectedTargetUid}`" class="chat-target-link">
                <img :src="selectedUser?.targetAvatar || defaultAvatar" class="chat-target-avatar" alt="" @error="onAvatarError" />
                {{ selectedUser?.targetName || '' }}
              </NuxtLink>
              <span class="chat-online-status" :class="{ online: onlineMap[selectedTargetUid] }">
                {{ onlineMap[selectedTargetUid] ? '在线' : '离线' }}
              </span>
            </div>

            <!-- 消息列表 -->
            <div ref="chatMsgRef" class="chat-messages">
              <div v-if="chatMsgLoading" class="chat-loading"><n-spin size="small" /></div>
              <template v-else>
                <template v-for="(msg, idx) in chatMessageList" :key="msg.id">
                  <!-- 时间分隔 -->
                  <div v-if="msg.timeMark === 1" class="chat-time-divider">
                    {{ formatTime(msg.createTime) }}
                  </div>
                  <!-- 消息行 -->
                  <div class="chat-msg-row" :class="{ mine: msg.uid === authStore.user?.uuid }">
                    <div class="chat-msg-bubble">
                      <span class="chat-msg-text">{{ msg.newsComment }}</span>
                      <span class="chat-msg-time">{{ formatTime(msg.createTime) }}</span>
                    </div>
                  </div>
                </template>
              </template>
            </div>

            <!-- 输入区域 -->
            <div class="chat-input-area">
              <div class="chat-input-box">
                <textarea
                  ref="chatTextareaRef"
                  v-model="chatInput"
                  class="chat-textarea"
                  placeholder="输入消息..."
                  rows="1"
                  @input="autoResize"
                  @keydown.enter.exact.prevent="handleSendMsg"
                ></textarea>
                <div class="chat-input-footer">
                  <div class="chat-input-left">
                    <n-popover placement="top-start" trigger="click" :show-arrow="false">
                      <template #trigger>
                        <button class="emoji-btn" type="button" title="表情"><span class="emoji-trigger">😊</span></button>
                      </template>
                      <div class="emoji-picker">
                        <div class="emoji-category" v-for="(emojis, category) in emojiCategories" :key="category">
                          <div class="emoji-category-title">{{ category }}</div>
                          <div class="emoji-list">
                            <span v-for="emoji in emojis" :key="emoji" class="emoji-item" @click="insertEmoji(emoji)">{{ emoji }}</span>
                          </div>
                        </div>
                      </div>
                    </n-popover>
                    <span class="chat-input-hint">Enter 发送，Shift+Enter 换行</span>
                  </div>
                  <button class="chat-send-btn" :disabled="!chatInput.trim() || sendingMsg" @click="handleSendMsg">发送</button>
                </div>
              </div>
            </div>
          </template>
        </div>
      </div>

      <!-- 消息列表 -->
      <template v-else>
        <!-- 加载状态 -->
        <div v-if="pending" class="loading-state">
          <n-spin size="large" />
        </div>

        <!-- 空状态 -->
        <div v-else-if="!newsList.length" class="empty-state">
          <CommonEmptyState :description="`暂无${currentTabLabel}消息`" />
        </div>

        <!-- 列表内容 -->
        <template v-else>
          <div class="news-list">
            <!-- 评论消息 (type=1) -->
            <template v-if="currentType === 1">
              <div
                v-for="item in (newsList as ArticleCommentNewsVo[])"
                :key="item.id"
                class="news-item"
                :class="{ unread: !item.beenRead }"
              >
                <NuxtLink :to="`/user-home/article/${item.commentUid}`" class="news-avatar-link">
                  <img :src="item.commentAvatar || defaultAvatar" class="news-avatar" alt="" @error="onAvatarError" />
                </NuxtLink>
                <div class="news-body">
                  <div class="news-header">
                    <NuxtLink :to="`/user-home/article/${item.commentUid}`" class="news-user">{{ item.commentName }}</NuxtLink>
                    <template v-if="item.type === 2">
                      <span class="news-action">回复了你的评论，来源于文章</span>
                    </template>
                    <template v-else>
                      <span class="news-action">评论了你的文章</span>
                    </template>
                    <NuxtLink :to="`/articles/${item.articleId}`" class="news-target">《{{ item.articleTitle }}》</NuxtLink>
                  </div>
                  <div class="news-comment-content">{{ item.content }}</div>
                  <time class="news-time" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
                </div>
              </div>
            </template>

            <!-- 点赞消息 (type=2) -->
            <template v-else-if="currentType === 2">
              <div
                v-for="item in (newsList as NewsUserInfoVo[])"
                :key="item.newsId"
                class="news-item"
                :class="{ unread: !item.beenRead }"
              >
                <NuxtLink :to="`/user-home/article/${item.senderId}`" class="news-avatar-link">
                  <img :src="item.senderAvatar || defaultAvatar" class="news-avatar" alt="" @error="onAvatarError" />
                </NuxtLink>
                <div class="news-body">
                  <div class="news-header">
                    <NuxtLink :to="`/user-home/article/${item.senderId}`" class="news-user">{{ item.senderName }}</NuxtLink>
                    <span class="news-action">赞了你的文章</span>
                    <NuxtLink v-if="item.newsContent" :to="`/articles/${item.targetId}`" class="news-target">《{{ item.newsContent }}》</NuxtLink>
                  </div>
                  <time class="news-time" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
                </div>
              </div>
            </template>

            <!-- 关注消息 (type=3) -->
            <template v-else-if="currentType === 3">
              <div
                v-for="item in (newsList as NewsUserInfoVo[])"
                :key="item.newsId"
                class="news-item"
                :class="{ unread: !item.beenRead }"
              >
                <NuxtLink :to="`/user-home/article/${item.senderId}`" class="news-avatar-link">
                  <img :src="item.senderAvatar || defaultAvatar" class="news-avatar" alt="" @error="onAvatarError" />
                </NuxtLink>
                <div class="news-body">
                  <div class="news-header">
                    <NuxtLink :to="`/user-home/article/${item.senderId}`" class="news-user">{{ item.senderName }}</NuxtLink>
                    <span class="news-action">关注了你</span>
                  </div>
                  <time class="news-time" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
                </div>
              </div>
            </template>

            <!-- 系统消息 (type=5) -->
            <template v-else-if="currentType === 5">
              <div
                v-for="(item, idx) in (newsList as NewsUserInfoVo[])"
                :key="idx"
                class="news-item system-item"
              >
                <div class="system-icon">
                  <n-icon size="20"><Bell /></n-icon>
                </div>
                <div class="news-body">
                  <div class="news-header">
                    <span class="news-title">{{ item.newsTitle }}</span>
                    <time class="news-time" :title="getFullDateTime(item.createTime)">{{ formatTime(item.createTime) }}</time>
                  </div>
                  <div class="news-content">{{ item.newsContent }}</div>
                </div>
              </div>
            </template>
          </div>

          <!-- 分页 -->
          <div v-if="total > pageSize" class="news-pagination">
            <n-pagination
              :page="currentPage"
              :item-count="total"
              :page-size="pageSize"
              :page-slot="5"
              size="small"
              @update:page="handlePageChange"
            />
          </div>
        </template>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Bell } from '@vicons/tabler'
import type { NewsUserSumVo, NewsUserInfoVo, ArticleCommentNewsVo, PrivateUserVo, PrivateNewsInfoVo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'
import { emojiCategories } from '~/utils/emoji'

definePageMeta({
  showTabBar: false,
  middleware: 'auth'
})

const route = useRoute()
const newsApi = useNewsApi()
const defaultAvatar = '/images/default-avatar.svg'

// 移动端判断
const isMobile = ref(false)
if (import.meta.client) {
  const checkMobile = () => { isMobile.value = window.innerWidth <= 768 }
  onMounted(() => {
    checkMobile()
    window.addEventListener('resize', checkMobile)
  })
  onUnmounted(() => window.removeEventListener('resize', checkMobile))
}

const onAvatarError = (e: Event) => {
  ;(e.target as HTMLImageElement).src = defaultAvatar
}

// Tab 配置
const tabs = [
  { type: 1, label: '评论' },
  { type: 2, label: '点赞' },
  { type: 3, label: '关注' },
  { type: 4, label: '私信' },
  { type: 5, label: '系统' }
]

// 当前选中的 Tab 类型（默认评论）
const currentType = computed(() => Number(route.query.type) || 1)

// 移动端直接访问私信页时重定向到评论
watch(isMobile, (mobile) => {
  if (mobile && currentType.value === 4) {
    navigateTo({ path: '/news' })
  }
})

const currentTabLabel = computed(() => {
  return tabs.find(t => t.type === currentType.value)?.label || ''
})

// 分页
const pageSize = 20
const currentPage = ref(1)

// 未读汇总（使用 WebSocket 实时数据）
const { unreadMap, connected: wsConnected } = useWebSocket()
const getUnreadCount = (type: number) => unreadMap.value[type] || 0

// WebSocket 未连接时，HTTP 兜底
const fetchUnreadSum = async () => {
  if (wsConnected.value) return
  try {
    const list = await newsApi.getNewsSum()
    const map: Record<number, number> = {}
    list.forEach((item: NewsUserSumVo) => {
      map[item.type] = item.newsSum
    })
    unreadMap.value = map
  } catch {
    // useApi 统一处理错误
  }
}

// 消息列表
const cacheKey = computed(() => `news-${currentType.value}-${currentPage.value}`)

const fetchNewsList = (): Promise<any> => {
  const type = currentType.value
  if (type === 1) return newsApi.getCommentList(currentPage.value, pageSize)
  if (type === 2) return newsApi.getFabulousList(currentPage.value, pageSize)
  if (type === 3) return newsApi.getFollowList(currentPage.value, pageSize)
  if (type === 5) return newsApi.getSystemList(currentPage.value, pageSize)
  return Promise.resolve({ rows: [], total: 0 })
}

const { data: pageData, pending } = await useAsyncData(cacheKey, fetchNewsList)

const newsList = computed(() => (pageData.value as any)?.rows || [])
const total = computed(() => (pageData.value as any)?.total || 0)

// Tab 切换
const switchTab = (type: number) => {
  currentPage.value = 1
  if (type === 1) {
    navigateTo({ path: '/news' })
  } else {
    navigateTo({ path: '/news', query: { type } })
  }
}

// 从 URL 获取选中的聊天用户
const chatTargetUid = computed(() => route.query.uid as string || '')

// 切换 Tab 时重置页码
watch(currentType, () => {
  currentPage.value = 1
  markReadIfNeeded()
})

// 分页切换
const handlePageChange = (page: number) => {
  currentPage.value = page
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 标记已读
const markReadIfNeeded = async () => {
  const type = currentType.value
  if (type === 4) return
  if (getUnreadCount(type) > 0) {
    try {
      await newsApi.markRead(type)
      // 更新本地未读数
      unreadMap.value = { ...unreadMap.value, [type]: 0 }
    } catch {
      // useApi 统一处理错误
    }
  }
}

// 页面初始化
onMounted(async () => {
  await fetchUnreadSum()
  markReadIfNeeded()
})

// ==================== 私信功能 ====================
const authStore = useAuthStore()
const privateApi = usePrivateMessageApi()

// 用户列表
const chatUserList = ref<PrivateUserVo[]>([])
const chatUserLoading = ref(false)
const selectedTargetUid = computed(() => chatTargetUid.value)
const selectedUser = computed(() => chatUserList.value.find(u => u.targetUid === selectedTargetUid.value))
const onlineMap = ref<Record<string, boolean>>({})

// 消息列表
const chatMessageList = ref<PrivateNewsInfoVo[]>([])
const chatMsgLoading = ref(false)
const chatMsgRef = ref<HTMLElement>()
const chatTextareaRef = ref<HTMLTextAreaElement>()
const chatInput = ref('')
const sendingMsg = ref(false)

const insertEmoji = (emoji: string) => {
  const ta = chatTextareaRef.value
  if (!ta) return
  const start = ta.selectionStart
  const end = ta.selectionEnd
  chatInput.value = chatInput.value.substring(0, start) + emoji + chatInput.value.substring(end)
  nextTick(() => {
    const pos = start + emoji.length
    ta.setSelectionRange(pos, pos)
    ta.focus()
  })
}

const autoResize = () => {
  const ta = chatTextareaRef.value
  if (!ta) return
  ta.style.height = 'auto'
  ta.style.height = Math.min(ta.scrollHeight, 120) + 'px'
}

// 加载用户列表
const fetchChatUsers = async () => {
  if (!authStore.isLoggedIn) return
  chatUserLoading.value = true
  try {
    const res = await privateApi.getUserList(authStore.user!.uuid)
    chatUserList.value = res.rows || []
    // 检查在线状态
    chatUserList.value.forEach(async (user) => {
      try {
        const info = await privateApi.isOnline(user.targetUid)
        onlineMap.value = { ...onlineMap.value, [user.targetUid]: !!info.isOnline }
      } catch {}
    })
    // URL 指定了用户，自动加载聊天消息
    if (chatTargetUid.value && chatUserList.value.some(u => u.targetUid === chatTargetUid.value)) {
      await loadChatMessages(chatTargetUid.value)
    }
  } catch {} finally {
    chatUserLoading.value = false
  }
}

// 防止 WebSocket 回调与主动操作重复加载
let lastMsgLoadTime = 0

// 选中用户 → 写入 URL + 加载消息 + 标记已读
const selectChatUser = async (user: PrivateUserVo) => {
  navigateTo({ path: '/news', query: { type: 4, uid: user.targetUid } })
  await loadChatMessages(user.targetUid)
}

const loadChatMessages = async (targetUid: string) => {
  chatMsgLoading.value = true
  lastMsgLoadTime = Date.now()
  try {
    await privateApi.markRead(targetUid)
    const user = chatUserList.value.find(u => u.targetUid === targetUid)
    if (user) user.unreadCount = 0
    const res = await privateApi.getMessageList(targetUid)
    chatMessageList.value = res.rows || []
  } catch {} finally {
    chatMsgLoading.value = false
    nextTick(() => scrollToBottom())
  }
}

// 发送消息
const handleSendMsg = async () => {
  const text = chatInput.value.trim()
  if (!text || !selectedTargetUid.value || sendingMsg.value) return
  sendingMsg.value = true
  lastMsgLoadTime = Date.now()
  try {
    await privateApi.sendMessage({ newsComment: text, replyTargetUid: selectedTargetUid.value })
    chatInput.value = ''
    const res = await privateApi.getMessageList(selectedTargetUid.value)
    chatMessageList.value = res.rows || []
    const user = chatUserList.value.find(u => u.targetUid === selectedTargetUid.value)
    if (user) user.lastNews = text
    nextTick(() => scrollToBottom())
  } catch {} finally {
    sendingMsg.value = false
  }
}

const scrollToBottom = () => {
  const el = chatMsgRef.value
  if (el) el.scrollTop = el.scrollHeight
}

// 删除私信用户
const dialog = useDialog()

const handleDeleteUser = async (user: PrivateUserVo) => {
  dialog.warning({
    title: '删除确认',
    content: `确定删除与 ${user.targetName} 的私信记录吗？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      await privateApi.deleteUser(user.targetUid)
      chatUserList.value = chatUserList.value.filter(u => u.targetUid !== user.targetUid)
      // 如果删除的是当前聊天用户，清空右侧面板
      if (selectedTargetUid.value === user.targetUid) {
        navigateTo({ path: '/news', query: { type: 4 } })
        chatMessageList.value = []
      }
    }
  })
}

// 切换到私信 tab 时加载用户列表
watch(currentType, (type) => {
  if (type === 4) fetchChatUsers()
}, { immediate: true })

// ==================== WebSocket 实时更新 ====================
const { onPrivateMessage, offPrivateMessage } = useWebSocket()

// 注册 WebSocket 私信回调
onPrivateMessage((wsUserList: PrivateUserVo[]) => {
  // 只在私信 tab 激活时处理
  if (currentType.value !== 4) return

  // 合并 WebSocket 数据到用户列表（保留在线状态等本地数据）
  const merged = wsUserList.map((wsUser) => {
    const local = chatUserList.value.find(u => u.targetUid === wsUser.targetUid)
    return { ...(local || {}), ...wsUser }
  })
  chatUserList.value = merged

  // 如果当前正在和某人聊天，重新加载消息（跳过刚主动加载过的，避免重复调用）
  if (selectedTargetUid.value && Date.now() - lastMsgLoadTime > 2000) {
    privateApi.getMessageList(selectedTargetUid.value).then((res) => {
      chatMessageList.value = res.rows || []
      nextTick(() => scrollToBottom())
    }).catch(() => {})

    // 清除当前聊天用户的未读数
    const cur = chatUserList.value.find(u => u.targetUid === selectedTargetUid.value)
    if (cur) cur.unreadCount = 0
  }
})

onUnmounted(() => offPrivateMessage())

useHead({
  title: '消息中心',
  bodyAttrs: {
    class: 'page-news'
  }
})
</script>

<style scoped>
.news-page {
  display: flex;
  gap: 16px;
  max-width: 960px;
  margin: 0 auto;
  padding: 24px 8px 32px 4px;
}

/* ==================== 竖向菜单 ==================== */
.news-sidebar {
  width: 100px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-top: 16px;
  position: sticky;
  top: 86px;
  align-self: flex-start;
}

.news-tab {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  font-size: 14px;
  color: var(--color-ink-light);
  text-decoration: none;
  border-radius: var(--radius-md);
  border-left: 3px solid transparent;
  cursor: pointer;
  transition: all var(--transition-fast);
  user-select: none;
  white-space: nowrap;
}

.news-tab:hover:not(.disabled) {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.news-tab.active {
  background: var(--color-primary-light);
  color: var(--color-primary);
  font-weight: 500;
  border-left-color: var(--color-primary);
}

.news-tab.disabled {
  opacity: 0.4;
  cursor: default;
}

.tab-label {
  flex: 1;
}

/* 未读红点 */
.tab-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 18px;
  padding: 0 5px;
  font-size: 11px;
  font-weight: 600;
  line-height: 1;
  color: #fff;
  background: var(--color-danger);
  border-radius: 9px;
}

/* ==================== 内容区 ==================== */
.news-content {
  flex: 1;
  min-width: 0;
}

/* 加载/空状态 */
.loading-state,
.empty-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 300px;
}

/* ==================== 消息列表 ==================== */
.news-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* 消息卡片 */
.news-item {
  display: flex;
  gap: 14px;
  padding: 16px;
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-left: 3px solid transparent;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.news-item:hover {
  border-color: var(--color-border);
  box-shadow: var(--shadow-sm);
}

.news-item.unread {
  border-left-color: var(--color-primary);
  background: var(--color-primary-light);
}

.news-item.unread:hover {
  background: color-mix(in srgb, var(--color-primary-light) 90%, var(--color-surface-dim));
}

/* 头像 */
.news-avatar {
  width: 42px;
  height: 42px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
}

.news-avatar-link {
  flex-shrink: 0;
}

/* 系统消息图标 */
.system-icon {
  width: 42px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: color-mix(in srgb, var(--color-primary) 12%, transparent);
  color: var(--color-primary);
  border-radius: var(--radius-full);
  flex-shrink: 0;
}

/* 消息主体 */
.news-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.news-header {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px 6px;
  font-size: 14px;
  line-height: 1.5;
}

.news-user {
  font-weight: 500;
  color: var(--color-ink);
  text-decoration: none;
}

.news-user:hover {
  color: var(--color-primary);
}

.news-action {
  color: var(--color-ink-muted);
}

.news-target {
  font-size: 14px;
  color: var(--color-primary);
  text-decoration: none;
}

.news-target:hover {
  text-decoration: underline;
}

.news-title {
  font-size: 15px;
  font-weight: 500;
  color: var(--color-ink);
  line-height: 1.5;
  flex: 1;
  min-width: 0;
}

.news-time {
  font-size: 12px;
  color: var(--color-ink-faint);
  white-space: nowrap;
  margin-top: 2px;
}

.news-content {
  font-size: 13px;
  color: var(--color-ink-muted);
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 评论内容：保留换行，与文章详情页格式一致 */
.news-comment-content {
  font-size: 14px;
  color: var(--color-ink-light);
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
  margin: 0;
}

/* 分页 */
.news-pagination {
  display: flex;
  justify-content: center;
  padding: 24px 0 8px;
}

/* ==================== 私信聊天 ==================== */
.chat-wrapper {
  display: flex;
  height: calc(100vh - 160px);
  min-height: 400px;
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--color-surface);
}

/* 左侧用户列表 */
.chat-user-list {
  width: 260px;
  flex-shrink: 0;
  border-right: 1px solid var(--color-border-light);
  overflow-y: auto;
}

.chat-user-item {
  display: flex;
  gap: 10px;
  padding: 12px 14px;
  cursor: pointer;
  transition: background var(--transition-fast);
}

.chat-user-item:hover {
  background: var(--color-surface-dim);
}

.chat-user-item.active {
  background: var(--color-primary-light);
}

.chat-user-avatar-wrap {
  position: relative;
  flex-shrink: 0;
}

.chat-user-avatar {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.chat-online-dot {
  position: absolute;
  bottom: 1px;
  right: 1px;
  width: 10px;
  height: 10px;
  background: #52c41a;
  border: 2px solid var(--color-surface);
  border-radius: 50%;
}

.chat-user-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
  justify-content: center;
}

.chat-user-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 4px;
}

.chat-user-name {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.chat-user-time {
  font-size: 11px;
  color: var(--color-ink-faint);
  flex-shrink: 0;
}

.chat-user-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 6px;
}

.chat-user-last {
  font-size: 12px;
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.chat-unread-badge {
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  font-size: 10px;
  font-weight: 600;
  line-height: 16px;
  text-align: center;
  color: #fff;
  background: var(--color-danger);
  border-radius: 8px;
  flex-shrink: 0;
}

/* 删除按钮 */
.chat-user-delete {
  display: none;
  position: absolute;
  top: 6px;
  right: 6px;
  width: 20px;
  height: 20px;
  font-size: 14px;
  line-height: 1;
  color: var(--color-ink-faint);
  background: var(--color-surface-dim);
  border: none;
  border-radius: 50%;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-fast);
}

.chat-user-delete:hover {
  color: var(--color-danger);
  background: var(--color-surface);
}

.chat-user-item {
  position: relative;
}

.chat-user-item:hover .chat-user-delete {
  display: flex;
}

/* 右侧聊天面板 */
.chat-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.chat-panel-empty {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-panel-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-bottom: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.chat-target-link {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: var(--color-ink);
  text-decoration: none;
}

.chat-target-link:hover {
  color: var(--color-primary);
}

.chat-target-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  object-fit: cover;
}

.chat-online-status {
  font-size: 12px;
  color: var(--color-ink-faint);
}

.chat-online-status.online {
  color: #52c41a;
}

/* 消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

/* 时间分隔 */
.chat-time-divider {
  text-align: center;
  font-size: 11px;
  color: var(--color-ink-faint);
  padding: 12px 0 8px;
}

/* 消息行 */
.chat-msg-row {
  display: flex;
  margin-bottom: 4px;
}

.chat-msg-row.mine {
  justify-content: flex-end;
}

/* 消息气泡 */
.chat-msg-bubble {
  max-width: 65%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.chat-msg-text {
  padding: 8px 12px;
  border-radius: 12px;
  font-size: 14px;
  line-height: 1.6;
  word-break: break-word;
  white-space: pre-wrap;
  background: var(--color-surface-dim);
  color: var(--color-ink);
  border-top-left-radius: 4px;
}

.chat-msg-row.mine .chat-msg-text {
  background: var(--color-primary);
  color: #fff;
  border-top-left-radius: 12px;
  border-top-right-radius: 4px;
}

.chat-msg-time {
  font-size: 11px;
  color: var(--color-ink-faint);
  padding: 0 4px;
}

.chat-msg-row.mine .chat-msg-time {
  text-align: right;
}

/* 输入区域 */
.chat-input-area {
  padding: 12px 16px;
  border-top: 1px solid var(--color-border-light);
  flex-shrink: 0;
}

.chat-input-box {
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
  padding: 10px 12px 8px;
}

.chat-textarea {
  width: 100%;
  padding: 0;
  font-size: 14px;
  line-height: 1.5;
  color: var(--color-ink);
  background: transparent;
  border: none;
  outline: none;
  resize: none;
  font-family: inherit;
  max-height: 120px;
  overflow-y: auto;
}

.chat-textarea::placeholder {
  color: var(--color-ink-faint);
}

.chat-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.chat-input-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.emoji-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  padding: 0;
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.emoji-btn:hover {
  color: var(--color-primary);
  background: var(--color-surface);
}

.emoji-trigger {
  font-size: 16px;
  line-height: 1;
}

.emoji-picker {
  padding: 12px;
  background: var(--color-surface);
  border-radius: var(--radius-md);
  max-width: 300px;
  max-height: 280px;
  overflow-y: auto;
}

.emoji-category {
  margin-bottom: 10px;
}

.emoji-category:last-child {
  margin-bottom: 0;
}

.emoji-category-title {
  font-size: 11px;
  font-weight: 500;
  color: var(--color-ink-muted);
  margin-bottom: 6px;
  padding-bottom: 4px;
  border-bottom: 1px solid var(--color-border-light);
}

.emoji-list {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 2px;
}

.emoji-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 30px;
  height: 30px;
  font-size: 16px;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.emoji-item:hover {
  background: var(--color-surface-dim);
  transform: scale(1.15);
}

.chat-input-hint {
  font-size: 11px;
  color: var(--color-ink-faint);
}

.chat-send-btn {
  padding: 5px 16px;
  font-size: 13px;
  font-weight: 500;
  color: #fff;
  background: var(--color-primary);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.chat-send-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.chat-send-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.chat-loading,
.chat-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
}

/* ==================== 移动端适配 ==================== */
@media (max-width: 768px) {
  .news-page {
    flex-direction: column;
    gap: 0;
    padding: 0 12px 24px;
  }

  .news-sidebar {
    width: 100%;
    flex-direction: row;
    gap: 4px;
    padding: 12px 0;
    position: sticky;
    top: 56px;
    overflow-x: auto;
    border-bottom: 1px solid var(--color-border-light);
    margin-bottom: 12px;
    background: var(--color-surface);
    z-index: 40;
  }

  .news-tab {
    padding: 6px 14px;
    font-size: 13px;
    border-left: none;
    border-bottom: 2px solid transparent;
    border-radius: var(--radius-sm);
  }

  .news-tab.active {
    border-left-color: transparent;
    border-bottom-color: var(--color-primary);
  }

  .news-item {
    padding: 14px 12px;
    gap: 12px;
  }

  .news-avatar,
  .system-icon {
    width: 38px;
    height: 38px;
  }

  /* 移动端私信 */
  .chat-wrapper {
    height: calc(100vh - 130px);
  }

  .chat-user-list {
    width: 200px;
  }

  .chat-user-avatar {
    width: 36px;
    height: 36px;
  }
}
</style>

<!-- 非 scoped：覆盖移动端布局间距 -->
<style>
@media (max-width: 768px) {
  body.page-news .home-main {
    padding-top: 70px !important;
  }
}
</style>
