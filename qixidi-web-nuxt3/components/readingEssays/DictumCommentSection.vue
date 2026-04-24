<template>
  <div class="dictum-comment-section">
    <div class="comment-header">
      <h3 class="comment-title">
        <MessageCircle class="title-icon" />
        评论 ({{ totalComments }})
      </h3>
    </div>

    <!-- 评论输入框 -->
    <div class="comment-input-card">
      <textarea
        ref="textareaRef"
        v-model="commentText"
        class="comment-textarea"
        :placeholder="replyTarget ? `回复 @${replyTarget.nickname}...` : '写下你的评论...'"
        rows="3"
        @keydown.ctrl.enter="handleSubmit"
        @keydown.esc="cancelReply"
      ></textarea>

      <div v-if="replyTarget" class="replying-info">
        <span class="replying-label">正在回复</span>
        <span class="replying-user">@{{ replyTarget.nickname }}</span>
        <span v-if="replyTarget.content" class="replying-content">{{ truncateContent(replyTarget.content, 20) }}</span>
        <button class="cancel-reply-btn" @click="cancelReply">
          <X class="cancel-icon" />
        </button>
      </div>

      <div class="comment-input-footer">
        <div class="input-footer-left">
          <!-- 表情按钮 -->
          <n-popover placement="top-start" trigger="click" :show-arrow="false">
            <template #trigger>
              <button class="emoji-btn" title="表情">
                <span class="emoji-trigger">😊</span>
              </button>
            </template>
            <div class="emoji-picker">
              <div class="emoji-category" v-for="(emojis, category) in emojiCategories" :key="category">
                <div class="emoji-category-title">{{ category }}</div>
                <div class="emoji-list">
                  <span
                    v-for="emoji in emojis"
                    :key="emoji"
                    class="emoji-item"
                    @click="insertEmoji(emoji)"
                  >{{ emoji }}</span>
                </div>
              </div>
            </div>
          </n-popover>
          <span class="comment-hint">Ctrl + Enter 发送</span>
        </div>
        <button class="submit-btn" :disabled="!canSubmit" @click="handleSubmit">
          {{ replyTarget ? '回复' : '发表评论' }}
        </button>
      </div>
    </div>

    <!-- 评论列表 -->
    <ClientOnly>
      <div v-if="loading" class="loading-state">
        <n-spin :size="20" />
      </div>

      <div v-else-if="comments.length > 0" class="comment-list">
        <!-- 一级评论 -->
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <img :src="comment.avatar || '/images/default-avatar.svg'" :alt="comment.nickname" class="comment-avatar" />
          <div class="comment-content-wrapper">
            <div class="comment-meta">
              <span class="comment-author">{{ comment.nickname }}</span>
              <time class="comment-time" :title="getFullDateTime(comment.createTime)">{{ formatTime(comment.createTime) }}</time>
              <button v-if="canDelete(comment)" class="delete-btn" @click="handleDelete(comment)" title="删除评论">
                <Trash class="delete-icon" />
              </button>
            </div>
            <p class="comment-text">{{ comment.content }}</p>

            <!-- 子评论列表（二级及三级） -->
            <div v-if="comment.dictumCommentVoList && comment.dictumCommentVoList.length > 0" class="reply-list">
              <div v-for="reply in comment.dictumCommentVoList" :key="reply.id" class="reply-item">
                <!-- 不显示头像 -->
                <div class="reply-content">
                  <!-- 用户名 + 回复对象 -->
                  <div class="reply-header">
                    <span class="reply-author">{{ reply.nickname }}</span>
                    <span v-if="reply.commentGrade === 3 && reply.targetNickname" class="reply-to">
                      回复 <span class="reply-target">@{{ reply.targetNickname }}</span>
                    </span>
                    <time class="reply-time" :title="getFullDateTime(reply.createTime)">{{ formatTime(reply.createTime) }}</time>
                    <button v-if="canDelete(reply)" class="delete-btn delete-btn-small" @click="handleDelete(reply)" title="删除评论">
                      <Trash class="delete-icon" />
                    </button>
                  </div>
                  <!-- 评论内容 -->
                  <p class="reply-text">{{ reply.content }}</p>
                  <!-- 回复按钮 -->
                  <button class="reply-action-btn" @click="handleReply(reply)">
                    <CornerDownLeft class="btn-icon" />
                    回复
                  </button>
                </div>
              </div>
            </div>

            <!-- 回复按钮 -->
            <button class="reply-btn" @click="handleReply(comment)">
              <CornerDownLeft class="reply-icon" />
              回复
            </button>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore" class="load-more">
          <button class="load-more-btn" @click="loadMore" :disabled="loadingMore">
            {{ loadingMore ? '加载中...' : '加载更多评论' }}
          </button>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-comments">
        <MessageCircle class="empty-icon" />
        <p>暂无评论，快来抢沙发吧~</p>
      </div>
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
import { MessageCircle, X, CornerDownLeft, Trash } from '@vicons/tabler'
import type { DictumCommentVo, DictumCommentBo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'
import { emojiCategories } from '~/utils/emoji'

interface Props {
  dictumId: string | number      // 随笔 id
  dictumUid: string              // 随笔作者 id
}

const props = defineProps<Props>()
const emit = defineEmits<{
  commentAdded: []
}>()

const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const dictumCommentApi = useDictumCommentApi()
const message = useMessage()

const commentText = ref('')
const comments = ref<DictumCommentVo[]>([])
const loading = ref(false)
const loadingMore = ref(false)
const replyTarget = ref<DictumCommentVo | null>(null)
const textareaRef = ref<HTMLTextAreaElement>()
const pageNum = ref(1)
const pageSize = 10
const total = ref(0)

// 是否有更多评论
const hasMore = computed(() => comments.value.length < total.value)

// 计算总评论数（包括子评论）
const totalComments = computed(() => {
  return comments.value.reduce((total, comment) => {
    return total + 1 + (comment.dictumCommentVoList?.length || 0)
  }, 0)
})

// 是否可以提交
const canSubmit = computed(() => {
  return commentText.value.trim().length > 0
})

// 表情分类（公共数据）

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
    const result = await dictumCommentApi.getCommentList(props.dictumId, {
      pageNum: pageNum.value,
      pageSize
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
    const newComment = addCommentOptimistically(content, isReply ? replyTarget.value : null, tempId)

    // 2. 提交到后端
    await submitComment(content, isReply ? replyTarget.value : null)

    // 3. 刷新评论列表以获取真实数据（包括 id）
    await loadComments()

    // 清空输入框
    commentText.value = ''
    cancelReplyAndReturnTarget()

    // 聚焦输入框
    textareaRef.value?.focus()

    message.success(isReply ? '回复成功' : '评论成功')
    emit('commentAdded')
  } catch (error) {
    console.error('提交评论失败:', error)
    // 失败则刷新列表
    await loadComments()
  }
}

// 乐观更新：在前端立即添加新评论
const addCommentOptimistically = (content: string, target: DictumCommentVo | null, tempId: number): DictumCommentVo | null => {
  const user = authStore.user
  if (!user) return null

  const newComment: DictumCommentVo = {
    id: tempId,
    dictumId: props.dictumId,  // 保持原值
    uid: props.dictumUid,
    parentId: target ? (target.commentGrade === 1 ? target.id : target.parentId) : props.dictumId,
    commentGrade: target ? (target.commentGrade === 1 ? 2 : 3) : 1,
    targetId: target ? String(target.id) : String(props.dictumId),
    targetUid: target?.commentUid || props.dictumUid,
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
    dictumCommentVoList: []
  }

  if (target && target.commentGrade === 1) {
    // 回复一级评论：添加到 dictumCommentVoList
    const topLevelComment = comments.value.find(c => c.id === target.id)
    if (topLevelComment) {
      if (!topLevelComment.dictumCommentVoList) {
        topLevelComment.dictumCommentVoList = []
      }
      topLevelComment.dictumCommentVoList.push(newComment)
    }
  } else if (target) {
    // 回复二级/三级评论：添加到对应一级评论的 dictumCommentVoList
    const topLevelComment = findTopLevelComment(target.id)
    if (topLevelComment && topLevelComment.dictumCommentVoList) {
      topLevelComment.dictumCommentVoList.push(newComment)
    }
  } else {
    // 一级评论：添加到列表顶部
    comments.value.unshift(newComment)
  }

  return newComment
}

// 提交评论到后端
const submitComment = async (content: string, target: DictumCommentVo | null) => {
  const user = authStore.user
  if (!user) return

  const bo: DictumCommentBo = {
    dictumId: props.dictumId,  // 保持原值，避免精度丢失
    uid: props.dictumUid,
    parentId: target ? (target.commentGrade === 1 ? target.id : target.parentId) : props.dictumId,
    commentGrade: target ? (target.commentGrade === 1 ? 2 : 3) : 1,
    targetId: target ? target.id : props.dictumId,
    targetUid: target?.commentUid || props.dictumUid,
    content,
    type: target ? 2 : 1
  }

  await dictumCommentApi.addComment(bo)
}

// 查找一级评论
const findTopLevelComment = (commentId: number): DictumCommentVo | null => {
  for (const comment of comments.value) {
    if (comment.id === commentId) return comment
    if (comment.dictumCommentVoList?.some(c => c.id === commentId)) return comment
  }
  return null
}

// 处理回复
const handleReply = (comment: DictumCommentVo) => {
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
const canDelete = (comment: DictumCommentVo): boolean => {
  return authStore.isLoggedIn && comment.commentUid === authStore.user?.uuid
}

// 删除评论
const handleDelete = async (comment: DictumCommentVo) => {
  if (!confirm('确定要删除这条评论吗？')) return

  try {
    // 1. 乐观删除：先从本地移除
    removeCommentLocally(comment)

    // 2. 调用后端 API
    await dictumCommentApi.deleteComment(comment.id)
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
const removeCommentLocally = (comment: DictumCommentVo) => {
  if (comment.commentGrade === 1) {
    // 一级评论：从主列表移除
    const index = comments.value.findIndex(c => c.id === comment.id)
    if (index > -1) {
      const childCount = comments.value[index].dictumCommentVoList?.length || 0
      comments.value.splice(index, 1)
      // 同时更新总数（一级评论 + 子评论数量）
      total.value = Math.max(0, total.value - 1 - childCount)
    }
  } else {
    // 二级/三级评论：从父评论的子列表中移除
    for (const topComment of comments.value) {
      if (topComment.dictumCommentVoList) {
        const index = topComment.dictumCommentVoList.findIndex(r => r.id === comment.id)
        if (index > -1) {
          topComment.dictumCommentVoList.splice(index, 1)
          break
        }
      }
    }
  }
}

const cancelReply = () => {
  replyTarget.value = null
}

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
.dictum-comment-section {
  background: transparent;
  padding: 0;
  margin-top: var(--space-6);  /* 与统计信息保持距离 */
}

.comment-header {
  margin-bottom: var(--space-4);
}

.comment-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.title-icon {
  width: 18px;
  height: 18px;
  color: var(--color-primary);
}

/* 评论输入框 */
.comment-input-card {
  padding: var(--space-4);
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
  margin-bottom: var(--space-6);
}

.comment-textarea {
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

.comment-textarea:focus {
  outline: none;
  border-color: var(--color-primary);
}

.comment-textarea::placeholder {
  color: var(--color-ink-faint);
}

.replying-info {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-3);
  margin-top: var(--space-3);
  background: var(--color-primary-light);
  border-radius: var(--radius-sm);
  font-size: var(--text-sm);
}

.replying-label {
  color: var(--color-primary);
  font-weight: 500;
}

.replying-user {
  color: var(--color-ink);
  font-weight: 500;
}

.replying-content {
  color: var(--color-ink-light);
  font-size: var(--text-sm);
  margin-left: var(--space-1);
}

.cancel-reply-btn {
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

.cancel-reply-btn:hover {
  background: rgba(var(--color-primary-rgb, 59, 130, 246), 0.1);
  color: var(--color-primary);
}

.cancel-icon {
  width: 14px;
  height: 14px;
}

.comment-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--space-3);
}

.input-footer-left {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

/* 表情按钮 */
.emoji-btn {
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

.emoji-btn:hover {
  color: var(--color-primary);
  background: var(--color-surface-dim);
}

.emoji-trigger {
  font-size: 18px;
  line-height: 1;
}

/* 表情选择器 */
.emoji-picker {
  padding: var(--space-3);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  max-width: 320px;
  max-height: 300px;
  overflow-y: auto;
}

.emoji-category {
  margin-bottom: var(--space-3);
}

.emoji-category:last-child {
  margin-bottom: 0;
}

.emoji-category-title {
  font-size: var(--text-xs);
  font-weight: 500;
  color: var(--color-ink-muted);
  margin-bottom: var(--space-2);
  padding-bottom: var(--space-1);
  border-bottom: 1px solid var(--color-border-light);
}

.emoji-list {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: var(--space-1);
}

.emoji-item {
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

.emoji-item:hover {
  background: var(--color-surface-dim);
  transform: scale(1.15);
}

.comment-hint {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.submit-btn {
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

.submit-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 评论列表 */
.comment-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

/* 一级评论 */
.comment-item {
  display: flex;
  gap: var(--space-3);
}

.comment-avatar {
  width: 32px;
  height: 32px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
}

.comment-content-wrapper {
  flex: 1;
  min-width: 0;
}

.comment-meta {
  display: flex;
  align-items: baseline;
  gap: var(--space-2);
  margin-bottom: var(--space-1);
}

.comment-author {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.comment-time {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 删除按钮 */
.delete-btn {
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

.delete-btn:hover {
  color: var(--color-danger);
  background: rgba(239, 68, 68, 0.1);
}

.delete-icon {
  width: 14px;
  height: 14px;
}

.delete-btn-small .delete-icon {
  width: 12px;
  height: 12px;
}

.comment-text {
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  color: var(--color-ink-light);
  margin: 0 0 var(--space-2) 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 回复列表 */
.reply-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
  padding: var(--space-3);
  background: var(--color-surface-dim);
  border-radius: var(--radius-sm);
  margin-bottom: var(--space-2);
}

/* 二级/三级评论 */
.reply-item {
  display: flex;
  gap: var(--space-2);
}

.reply-content {
  flex: 1;
  min-width: 0;
}

.reply-header {
  display: flex;
  align-items: baseline;
  gap: var(--space-1);
  flex-wrap: wrap;
  margin-bottom: var(--space-1);
}

.reply-author {
  font-size: var(--text-sm);
  font-weight: 500;
  color: var(--color-ink);
}

.reply-to {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.reply-target {
  color: var(--color-primary);
  font-weight: 500;
}

.reply-time {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

/* 回复列表中的删除按钮 */
.reply-header .delete-btn {
  margin-left: var(--space-1);
}

.reply-text {
  font-size: var(--text-sm);
  line-height: var(--leading-normal);
  color: var(--color-ink-light);
  margin: 0 0 var(--space-2) 0;
  white-space: pre-wrap;
  word-break: break-word;
}

/* 回复按钮 */
.reply-btn {
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

.reply-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.reply-icon {
  width: 14px;
  height: 14px;
}

.reply-action-btn {
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

.reply-action-btn:hover {
  color: var(--color-primary);
}

.btn-icon {
  width: 12px;
  height: 12px;
}

/* 加载更多 */
.load-more {
  text-align: center;
  padding: var(--space-4) 0;
}

.load-more-btn {
  padding: var(--space-2) var(--space-5);
  font-size: var(--text-sm);
  color: var(--color-primary);
  background: transparent;
  border: 1px solid var(--color-primary);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.load-more-btn:hover:not(:disabled) {
  background: var(--color-primary-light);
}

.load-more-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 加载和空状态 */
.loading-state,
.empty-comments {
  padding: var(--space-10) var(--space-5);
  text-align: center;
  color: var(--color-ink-muted);
}

.empty-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto var(--space-4);
  opacity: 0.5;
}

.empty-comments p {
  font-size: var(--text-sm);
  margin: 0;
}

/* 响应式 */
@media (max-width: 640px) {
  .comment-input-card {
    padding: var(--space-3);
  }

  .comment-item {
    gap: var(--space-2);
  }

  .comment-avatar {
    width: 28px;
    height: 28px;
  }
}
</style>
