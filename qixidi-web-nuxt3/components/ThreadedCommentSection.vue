<template>
  <div class="tcs-section">
    <div class="tcs-header">
      <h3 class="tcs-title">
        <MessageCircle class="tcs-title-icon" />
        评论 ({{ totalComments }})
      </h3>
    </div>

    <!-- 评论输入框 -->
    <div class="tcs-input-card">
      <textarea
        ref="textareaRef"
        v-model="commentText"
        class="tcs-textarea"
        :placeholder="replyTarget ? `回复 @${replyTarget.nickname}...` : '写下你的评论...'"
        rows="3"
        @keydown.ctrl.enter="handleSubmit"
        @keydown.esc="cancelReply"
      ></textarea>

      <div v-if="replyTarget" class="tcs-replying-info">
        <span class="tcs-replying-label">正在回复</span>
        <span class="tcs-replying-user">@{{ replyTarget.nickname }}</span>
        <span v-if="replyTarget.content" class="tcs-replying-content">{{ truncateContent(replyTarget.content, 20) }}</span>
        <button class="tcs-cancel-reply-btn" @click="cancelReply">
          <X class="tcs-cancel-icon" />
        </button>
      </div>

      <div class="tcs-input-footer">
        <div class="tcs-input-footer-left">
          <!-- 表情按钮（ClientOnly 避免 SSR 水合时 n-popover 点击事件未绑定） -->
          <ClientOnly>
            <n-popover placement="top-start" trigger="click" :show-arrow="false">
              <template #trigger>
                <button class="tcs-emoji-btn" title="表情">
                  <span class="tcs-emoji-trigger">😊</span>
                </button>
              </template>
              <div class="tcs-emoji-picker">
                <div class="tcs-emoji-category" v-for="(emojis, category) in emojiCategories" :key="category">
                  <div class="tcs-emoji-category-title">{{ category }}</div>
                  <div class="tcs-emoji-list">
                    <span
                      v-for="emoji in emojis"
                      :key="emoji"
                      class="tcs-emoji-item"
                      @click="insertEmoji(emoji)"
                    >{{ emoji }}</span>
                  </div>
                </div>
              </div>
            </n-popover>
          </ClientOnly>
          <span class="tcs-hint">Ctrl + Enter 发送</span>
        </div>
        <button class="tcs-submit-btn" :disabled="!canSubmit" @click="handleSubmit">
          {{ replyTarget ? '回复' : '发表评论' }}
        </button>
      </div>
    </div>

    <!-- 评论列表 -->
    <ClientOnly>
      <div v-if="loading" class="tcs-loading-state">
        <n-spin :size="20" />
      </div>

      <div v-else-if="comments.length > 0" class="tcs-list">
        <!-- 一级评论 -->
        <div v-for="comment in comments" :key="comment.id" class="tcs-item">
          <img :src="comment.avatar || '/images/default-avatar.svg'" :alt="comment.nickname" class="tcs-avatar" />
          <div class="tcs-content-wrapper">
            <div class="tcs-meta">
              <span class="tcs-author">{{ comment.nickname }}</span>
              <time class="tcs-time" :title="getFullDateTime(comment.createTime)">{{ formatTime(comment.createTime) }}</time>
              <button v-if="canDelete(comment)" class="tcs-delete-btn" @click="handleDelete(comment)" title="删除评论">
                <Trash class="tcs-delete-icon" />
              </button>
            </div>
            <p class="tcs-text">{{ comment.content }}</p>

            <!-- 子评论列表（二级及三级） -->
            <div v-if="comment.children && comment.children.length > 0" class="tcs-reply-list">
              <div v-for="reply in comment.children" :key="reply.id" class="tcs-reply-item">
                <!-- 回复头像（可选，文章评论区开启，随笔/时光小记默认关闭） -->
                <img v-if="showReplyAvatar" :src="reply.avatar || '/images/default-avatar.svg'" :alt="reply.nickname" class="tcs-reply-avatar" />
                <div class="tcs-reply-content">
                  <!-- 用户名 + 回复对象 -->
                  <div class="tcs-reply-header">
                    <span class="tcs-reply-author">{{ reply.nickname }}</span>
                    <span v-if="reply.commentGrade === 3 && reply.targetNickname" class="tcs-reply-to">
                      回复 <span class="tcs-reply-target">@{{ reply.targetNickname }}</span>
                    </span>
                    <time class="tcs-reply-time" :title="getFullDateTime(reply.createTime)">{{ formatTime(reply.createTime) }}</time>
                    <button v-if="canDelete(reply)" class="tcs-delete-btn tcs-delete-btn-small" @click="handleDelete(reply)" title="删除评论">
                      <Trash class="tcs-delete-icon" />
                    </button>
                  </div>
                  <!-- 评论内容 -->
                  <p class="tcs-reply-text">{{ reply.content }}</p>
                  <!-- 回复按钮 -->
                  <button class="tcs-reply-action-btn" @click="handleReply(reply)">
                    <CornerDownLeft class="tcs-btn-icon" />
                    回复
                  </button>
                </div>
              </div>
            </div>

            <!-- 回复按钮 -->
            <button class="tcs-reply-btn" @click="handleReply(comment)">
              <CornerDownLeft class="tcs-reply-icon" />
              回复
            </button>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore" class="tcs-load-more">
          <button class="tcs-load-more-btn" @click="loadMore" :disabled="loadingMore">
            {{ loadingMore ? '加载中...' : '加载更多评论' }}
          </button>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="tcs-empty">
        <MessageCircle class="tcs-empty-icon" />
        <p>暂无评论，快来抢沙发吧~</p>
      </div>
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
import { MessageCircle, X, CornerDownLeft, Trash } from '@vicons/tabler'
import type { CommentItem, CommentSubmitPayload, PageQuery, TableDataInfo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'
import { emojiCategories } from '~/utils/emoji'

/**
 * 通用楼层式评论区组件（输入框 + 一级评论分页 + 二级/三级回复）
 * 业务无关：数据获取/提交/删除通过函数 props 注入，业务 id 由调用方闭包组装
 */
interface Props {
  bizId: string | number                                        // 业务内容 id（如时光小记 id）
  bizUid: string                                                // 业务内容作者 id
  fetchList: (pageQuery: PageQuery) => Promise<TableDataInfo<CommentItem>>
  submit: (payload: CommentSubmitPayload) => Promise<Partial<CommentItem> | void> // 推荐返回新评论（至少含真实 id），组件据此原位落定乐观节点，避免提交后整列表刷新闪烁
  remove: (id: string | number) => Promise<void>
  showReplyAvatar?: boolean                                     // 二级/三级回复是否显示头像（文章评论区开启，随笔/时光小记默认关闭）
}

const props = defineProps<Props>()
const emit = defineEmits<{
  commentAdded: []
}>()

const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const message = useMessage()

const commentText = ref('')
const comments = ref<CommentItem[]>([])
const loading = ref(false)
const loadingMore = ref(false)
const replyTarget = ref<CommentItem | null>(null)
const textareaRef = ref<HTMLTextAreaElement>()
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)

// 是否有更多评论
const hasMore = computed(() => comments.value.length < total.value)

// 计算总评论数（包括子评论）
const totalComments = computed(() => {
  return comments.value.reduce((total, comment) => {
    return total + 1 + (comment.children?.length || 0)
  }, 0)
})

// 是否可以提交
const canSubmit = computed(() => {
  return commentText.value.trim().length > 0
})

// 插入表情到光标位置
const insertEmoji = (emoji: string) => {
  const textarea = textareaRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const text = commentText.value

  // 在光标位置插入表情
  const before = text.substring(0, start)
  const after = text.substring(end)
  commentText.value = before + emoji + after

  // 设置光标位置到表情后面
  nextTick(() => {
    const newPos = start + emoji.length
    textarea.setSelectionRange(newPos, newPos)
    textarea.focus()
  })
}

// 加载评论列表
const loadComments = async (isLoadMore = false) => {
  if (isLoadMore) {
    loadingMore.value = true
  } else {
    loading.value = true
  }

  try {
    const result = await props.fetchList({
      // 刷新时从第 1 页拉回全部已加载内容（避免多页后刷新只剩最后一页），加载更多按当前页请求
      pageNum: isLoadMore ? pageNum.value : 1,
      pageSize: isLoadMore ? pageSize : pageNum.value * pageSize
    })

    if (isLoadMore) {
      comments.value = [...comments.value, ...result.rows]
    } else {
      comments.value = result.rows
    }
    total.value = result.total
  } catch (error) {
    console.error('加载评论失败:', error)
    if (!isLoadMore) {
      comments.value = []
    }
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 加载更多评论
const loadMore = () => {
  pageNum.value++
  loadComments(true)
}

// 提交评论
const handleSubmit = async () => {
  if (!canSubmit.value) return

  // 未登录则显示登录框
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog()
    return
  }

  const content = commentText.value.trim()
  const isReply = !!replyTarget.value
  const tempId = Date.now() // 记录临时 id

  try {
    // 1. 先乐观添加到列表（使用临时 id）
    addCommentOptimistically(content, isReply ? replyTarget.value : null, tempId)

    // 2. 提交到后端，拿回新评论（至少含真实 id）
    const created = await submitComment(content, isReply ? replyTarget.value : null)

    // 3. 用真实 id 原位落定乐观节点（不刷新列表，避免闪烁）
    finalizeOptimisticComment(tempId, created)
    if (!isReply) {
      // total 只统计一级评论
      total.value += 1
    }

    // 清空输入框
    commentText.value = ''
    cancelReplyAndReturnTarget()

    // 聚焦输入框
    textareaRef.value?.focus()

    message.success(isReply ? '回复成功' : '评论成功')
    emit('commentAdded')
  } catch (error) {
    console.error('提交评论失败:', error)
    // 失败回滚乐观节点并刷新列表兜底
    removeOptimisticComment(tempId)
    await loadComments()
  }
}

// 乐观更新：在前端立即添加新评论
const addCommentOptimistically = (content: string, target: CommentItem | null, tempId: number): CommentItem | null => {
  const user = authStore.user
  if (!user) return null

  const newComment: CommentItem = {
    id: tempId,
    uid: props.bizUid,
    parentId: target ? (target.commentGrade === 1 ? target.id : target.parentId) : props.bizId,
    commentGrade: target ? (target.commentGrade === 1 ? 2 : 3) : 1,
    targetId: target ? String(target.id) : String(props.bizId),
    targetUid: target?.commentUid || props.bizUid,
    commentUid: user.uuid,
    content,
    type: target ? 2 : 1,
    status: 0,
    createTime: new Date().toISOString(),
    nickname: user.nickname || '我',
    username: user.username || '',
    avatar: user.avatar || '/images/default-avatar.svg',
    targetNickname: target?.nickname || '',
    targetUsername: target?.username || '',
    targetAvatar: target?.avatar || '',
    children: []
  }

  if (target && target.commentGrade === 1) {
    // 回复一级评论：添加到 children
    const topLevelComment = comments.value.find(c => c.id === target.id)
    if (topLevelComment) {
      if (!topLevelComment.children) {
        topLevelComment.children = []
      }
      topLevelComment.children.push(newComment)
    }
  } else if (target) {
    // 回复二级/三级评论：添加到对应一级评论的 children
    const topLevelComment = findTopLevelComment(target.id)
    if (topLevelComment && topLevelComment.children) {
      topLevelComment.children.push(newComment)
    }
  } else {
    // 一级评论：添加到列表顶部
    comments.value.unshift(newComment)
  }

  return newComment
}

// 提交评论到后端
const submitComment = async (content: string, target: CommentItem | null): Promise<Partial<CommentItem> | null | void> => {
  const user = authStore.user
  if (!user) return

  const payload: CommentSubmitPayload = {
    uid: props.bizUid,
    parentId: target ? (target.commentGrade === 1 ? target.id : target.parentId) : props.bizId,
    commentGrade: target ? (target.commentGrade === 1 ? 2 : 3) : 1,
    targetId: target ? target.id : props.bizId,
    targetUid: target?.commentUid || props.bizUid,
    content,
    type: target ? 2 : 1
  }

  return await props.submit(payload)
}

// 用后端返回的真实 id 落定乐观节点（防止临时 id 被用于后续回复/删除）
const finalizeOptimisticComment = (tempId: number, created: Partial<CommentItem> | null | void) => {
  const realId = created?.id
  if (realId === undefined || realId === null) return

  const topLevel = comments.value.find(c => c.id === tempId)
  if (topLevel) {
    topLevel.id = realId
    return
  }
  for (const comment of comments.value) {
    const child = comment.children?.find(r => r.id === tempId)
    if (child) {
      child.id = realId
      return
    }
  }
}

// 移除乐观节点（提交失败时回滚）
const removeOptimisticComment = (tempId: number) => {
  const topIndex = comments.value.findIndex(c => c.id === tempId)
  if (topIndex > -1) {
    comments.value.splice(topIndex, 1)
    return
  }
  for (const comment of comments.value) {
    const childIndex = comment.children?.findIndex(r => r.id === tempId) ?? -1
    if (childIndex > -1) {
      comment.children!.splice(childIndex, 1)
      return
    }
  }
}

// 查找一级评论
const findTopLevelComment = (commentId: string | number): CommentItem | null => {
  for (const comment of comments.value) {
    if (comment.id === commentId) return comment
    if (comment.children?.some(c => c.id === commentId)) return comment
  }
  return null
}

// 处理回复
const handleReply = (comment: CommentItem) => {
  replyTarget.value = comment
  commentText.value = ''
  nextTick(() => {
    // 聚焦输入框
    textareaRef.value?.focus()
    // 平滑滚动到输入框
    const textarea = textareaRef.value
    if (textarea) {
      textarea.scrollIntoView({ behavior: 'smooth', block: 'center' })
    }
  })
}

// 取消回复
const cancelReplyAndReturnTarget = () => {
  const target = replyTarget.value
  replyTarget.value = null
  return target
}

// 截断评论内容预览
const truncateContent = (content: string, maxLength: number): string => {
  if (content.length <= maxLength) return content
  return content.substring(0, maxLength) + '...'
}

// 判断是否可以删除评论（登录用户是评论作者）
const canDelete = (comment: CommentItem): boolean => {
  return authStore.isLoggedIn && comment.commentUid === authStore.user?.uuid
}

// 删除评论
const handleDelete = async (comment: CommentItem) => {
  if (!confirm('确定要删除这条评论吗？')) return

  try {
    // 1. 乐观删除：先从本地移除
    removeCommentLocally(comment)

    // 2. 调用后端 API
    await props.remove(comment.id)
    message.success('删除成功')

    // 3. 更新总数
    total.value = Math.max(0, total.value - 1)
  } catch (error) {
    console.error('删除评论失败:', error)
    // 失败时重新加载列表恢复数据
    await loadComments()
  }
}

// 从本地列表中移除评论
const removeCommentLocally = (comment: CommentItem) => {
  if (comment.commentGrade === 1) {
    // 一级评论：从主列表移除
    const index = comments.value.findIndex(c => c.id === comment.id)
    if (index > -1) {
      const childCount = comments.value[index].children?.length || 0
      comments.value.splice(index, 1)
      // 同时更新总数（一级评论 + 子评论数量）
      total.value = Math.max(0, total.value - 1 - childCount)
    }
  } else {
    // 二级/三级评论：从父评论的子列表中移除
    for (const topComment of comments.value) {
      if (topComment.children) {
        const index = topComment.children.findIndex(r => r.id === comment.id)
        if (index > -1) {
          topComment.children.splice(index, 1)
          break
        }
      }
    }
  }
}

const cancelReply = () => {
  replyTarget.value = null
}

// 业务 id 变化时重置状态并重新加载（父组件不使用 :key 重建时也能正确切换）
watch(() => props.bizId, () => {
  pageNum.value = 1
  comments.value = []
  replyTarget.value = null
  loadComments()
})

// 组件挂载时加载评论
onMounted(() => {
  loadComments()
})

// 暴露刷新方法给父组件
defineExpose({
  refresh: () => loadComments()
})
</script>

<style scoped>
.tcs-section {
  background: transparent;
  padding: 0;
}

.tcs-header {
  margin-bottom: var(--space-4);
}

.tcs-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-base);
  font-weight: 500;
  color: var(--color-ink);
  margin: 0;
}

.tcs-title-icon {
  width: 16px;
  height: 16px;
  color: var(--color-primary);
}

/* 评论输入框 */
.tcs-input-card {
  padding: var(--space-4);
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-6);
}

.tcs-textarea {
  width: 100%;
  padding: var(--space-3);
  font-size: var(--text-base);
  line-height: var(--leading-normal);
  color: var(--color-ink);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  resize: vertical;
  font-family: inherit;
  transition: border-color var(--transition-base);
}

.tcs-textarea:focus {
  outline: none;
  border-color: var(--color-primary);
}

.tcs-textarea::placeholder {
  color: var(--color-ink-faint);
}

.tcs-replying-info {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  margin-top: var(--space-3);
  background: var(--color-primary-light);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
}

.tcs-replying-label {
  color: var(--color-primary);
  font-weight: 500;
}

.tcs-replying-user {
  color: var(--color-ink);
  font-weight: 500;
}

.tcs-replying-content {
  color: var(--color-ink-light);
  font-size: var(--text-sm);
  margin-left: var(--space-1);
}

.tcs-cancel-reply-btn {
  margin-left: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  padding: 0;
  background: transparent;
  border: none;
  color: var(--color-ink-muted);
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tcs-cancel-reply-btn:hover {
  background: rgba(var(--color-primary-rgb, 59, 130, 246), 0.1);
  color: var(--color-primary);
}

.tcs-cancel-icon {
  width: 14px;
  height: 14px;
}

.tcs-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--space-3);
}

.tcs-input-footer-left {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

/* 表情按钮 */
.tcs-emoji-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  padding: 0;
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tcs-emoji-btn:hover {
  color: var(--color-primary);
  background: var(--color-surface-dim);
}

.tcs-emoji-trigger {
  font-size: 18px;
  line-height: 1;
}

/* 表情选择器 */
.tcs-emoji-picker {
  padding: var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  max-width: 320px;
  max-height: 300px;
  overflow-y: auto;
}

.tcs-emoji-category {
  margin-bottom: var(--space-3);
}

.tcs-emoji-category:last-child {
  margin-bottom: 0;
}

.tcs-emoji-category-title {
  font-size: var(--text-xs);
  font-weight: 500;
  color: var(--color-ink-muted);
  margin-bottom: var(--space-2);
  padding-bottom: var(--space-1);
  border-bottom: 1px solid var(--color-border-light);
}

.tcs-emoji-list {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: var(--space-1);
}

.tcs-emoji-item {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  font-size: 18px;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tcs-emoji-item:hover {
  background: var(--color-surface-dim);
  transform: scale(1.15);
}

.tcs-hint {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.tcs-submit-btn {
  padding: var(--space-2) var(--space-5);
  font-size: var(--text-sm);
  font-weight: 500;
  color: #fff;
  background: var(--color-primary);
  border: none;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tcs-submit-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.tcs-submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 评论列表 */
.tcs-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

/* 一级评论 */
.tcs-item {
  display: flex;
  gap: var(--space-3);
}

.tcs-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
}

.tcs-content-wrapper {
  flex: 1;
  min-width: 0;
}

.tcs-meta {
  display: flex;
  align-items: baseline;
  gap: var(--space-2);
  margin-bottom: var(--space-1);
}

.tcs-author {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.tcs-time {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 删除按钮 */
.tcs-delete-btn {
  margin-left: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 4px;
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  cursor: pointer;
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tcs-delete-btn:hover {
  color: var(--color-danger);
  background: rgba(239, 68, 68, 0.1);
}

.tcs-delete-icon {
  width: 14px;
  height: 14px;
}

.tcs-delete-btn-small .tcs-delete-icon {
  width: 12px;
  height: 12px;
}

.tcs-text {
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  color: var(--color-ink-light);
  margin: 0 0 var(--space-2) 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 回复列表 */
.tcs-reply-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  padding: var(--space-3);
  background: var(--color-surface-dim);
  border-radius: var(--radius-sm);
  margin-bottom: var(--space-2);
}

/* 二级/三级评论 */
.tcs-reply-item {
  display: flex;
  gap: var(--space-2);
}

.tcs-reply-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
}

.tcs-reply-content {
  flex: 1;
  min-width: 0;
}

.tcs-reply-header {
  display: flex;
  align-items: baseline;
  gap: var(--space-1);
  flex-wrap: wrap;
  margin-bottom: var(--space-1);
}

.tcs-reply-author {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.tcs-reply-to {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.tcs-reply-target {
  color: var(--color-primary);
  font-weight: 500;
}

.tcs-reply-time {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 回复列表中的删除按钮 */
.tcs-reply-header .tcs-delete-btn {
  margin-left: var(--space-1);
}

.tcs-reply-text {
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  color: var(--color-ink-light);
  margin: 0 0 var(--space-2) 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 回复按钮 */
.tcs-reply-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-3);
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  background: transparent;
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tcs-reply-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.tcs-reply-icon {
  width: 14px;
  height: 14px;
}

.tcs-reply-action-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  padding: var(--space-1) var(--space-2);
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: color var(--transition-fast);
}

.tcs-reply-action-btn:hover {
  color: var(--color-primary);
}

.tcs-btn-icon {
  width: 12px;
  height: 12px;
}

/* 加载更多 */
.tcs-load-more {
  text-align: center;
  padding: var(--space-4) 0;
}

.tcs-load-more-btn {
  padding: var(--space-2) var(--space-5);
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: transparent;
  border: 1px solid var(--color-primary);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.tcs-load-more-btn:hover:not(:disabled) {
  background: var(--color-primary-light);
}

.tcs-load-more-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 加载和空状态 */
.tcs-loading-state,
.tcs-empty {
  padding: var(--space-10) var(--space-5);
  text-align: center;
  color: var(--color-ink-muted);
}

.tcs-empty-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto var(--space-4);
  opacity: 0.5;
}

.tcs-empty p {
  font-size: var(--text-sm);
  margin: 0;
}

/* 响应式 */
@media (max-width: 640px) {
  .tcs-input-card {
    padding: var(--space-3);
  }

  .tcs-item {
    gap: var(--space-2);
  }

  .tcs-avatar {
    width: 28px;
    height: 28px;
  }
}
</style>
