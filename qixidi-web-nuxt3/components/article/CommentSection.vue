<template>
  <div class="comment-section">
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
        :placeholder="replyTarget ? `回复 @${replyTarget.commentName}...` : '写下你的评论...'"
        rows="3"
        @keydown.ctrl.enter="handleSubmit"
        @keydown.esc="cancelReply"
      ></textarea>

      <div v-if="replyTarget" class="replying-info">
        <span class="replying-label">正在回复</span>
        <span class="replying-user">@{{ replyTarget.commentName }}</span>
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
        <p>加载中...</p>
      </div>

      <div v-else-if="comments.length > 0" class="comment-list">
        <!-- 一级评论 -->
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <img :src="comment.commentAvatar || '/images/default-avatar.svg'" :alt="comment.commentName" class="comment-avatar" />
          <div class="comment-content-wrapper">
            <div class="comment-meta">
              <span class="comment-author">{{ comment.commentName }}</span>
              <time class="comment-time" :title="getFullDateTime(comment.createTime)">{{ formatTime(comment.createTime) }}</time>
              <button v-if="canDelete(comment)" class="delete-btn" @click="handleDelete(comment)" title="删除评论">
                <Trash class="delete-icon" />
              </button>
            </div>
            <p class="comment-text">{{ comment.content }}</p>

            <!-- 子评论列表（二级及三级） -->
            <div v-if="comment.mountComment && comment.mountComment.length > 0" class="reply-list">
              <div v-for="reply in comment.mountComment" :key="reply.id" class="reply-item">
                <img :src="reply.commentAvatar || '/images/default-avatar.svg'" :alt="reply.commentName" class="reply-avatar" />
                <div class="reply-content">
                  <!-- 用户名 + 回复对象 -->
                  <div class="reply-header">
                    <span class="reply-author">{{ reply.commentName }}</span>
                    <span v-if="reply.commentGrade === 3 && reply.targetName" class="reply-to">
                      回复 <span class="reply-target">@{{ reply.targetName }}</span>
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
import type { ArticleCommentVo } from '~/types'
import { formatTime, getFullDateTime } from '~/utils/formatTime'
import { emojiCategories } from '~/utils/emoji'

interface Props {
  articleId: number
  articleUserId: string  // 文章作者 id
}

const props = defineProps<Props>()

const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()
const commentApi = useCommentApi()

const commentText = ref('')
const comments = ref<ArticleCommentVo[]>([])
const loading = ref(false)
const replyTarget = ref<ArticleCommentVo | null>(null)
const textareaRef = ref<HTMLTextAreaElement>()

// 计算总评论数（包括子评论）
const totalComments = computed(() => {
  return comments.value.reduce((total, comment) => {
    return total + 1 + (comment.mountComment?.length || 0)
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
const loadComments = async () => {
  loading.value = true
  try {
    comments.value = await commentApi.getCommentList(props.articleId)
  } catch (error) {
    console.error('加载评论失败:', error)
    comments.value = []
  } finally {
    loading.value = false
  }
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
    let realId: number
    if (isReply) {
      realId = await submitReply()
    } else {
      realId = await submitTopLevelComment()
    }

    // 3. 用真实 id 替换临时 id
    if (newComment) {
      newComment.id = realId
    }

    // 清空输入框
    commentText.value = ''
    cancelReplyAndReturnTarget()

    // 聚焦输入框
    textareaRef.value?.focus()
  } catch (error) {
    console.error('提交评论失败:', error)
    // 失败则移除临时评论
    removeCommentFromList(tempId)
  }
}

// 乐观更新：在前端立即添加新评论，返回新评论的引用（用于更新 id）
const addCommentOptimistically = (content: string, target: ArticleCommentVo | null, tempId: number): ArticleCommentVo | null => {
  const user = authStore.user
  if (!user) return null

  const newComment: ArticleCommentVo = {
    id: tempId, // 临时 id，提交成功后会替换为真实 id
    articleId: props.articleId,
    uid: props.articleUserId,
    parentId: target ? (target.commentGrade === 1 ? target.id : Number(target.targetId)) : props.articleId,
    commentGrade: target ? (target.commentGrade === 1 ? 2 : 3) : 1,
    targetId: target ? String(target.id) : String(props.articleId),
    targetUid: target?.commentUid || props.articleUserId,
    commentUid: user.uuid,
    content,
    type: target ? 2 : 1,
    state: 0,
    createTime: new Date().toISOString(),
    commentName: user.nickname || '我',
    commentAvatar: user.avatar || '/images/default-avatar.svg',
    mountComment: []
  }

  if (target && target.commentGrade === 1) {
    // 回复一级评论：添加到 mountComment
    if (!comments.value.find(c => c.id === target.id)) {
      // 如果找不到对应的一级评论，刷新整个列表
      loadComments()
      return null
    }
    const topLevelComment = comments.value.find(c => c.id === target.id)!
    if (!topLevelComment.mountComment) {
      topLevelComment.mountComment = []
    }
    topLevelComment.mountComment.push(newComment)
  } else if (target) {
    // 回复二级/三级评论：添加到对应一级评论的 mountComment
    const topLevelComment = findTopLevelComment(target.id)
    if (topLevelComment && topLevelComment.mountComment) {
      topLevelComment.mountComment.push(newComment)
    } else {
      // 找不到对应评论，刷新整个列表
      loadComments()
      return null
    }
  } else {
    // 一级评论：添加到列表顶部
    comments.value.unshift(newComment)
  }

  return newComment
}

// 提交一级评论
const submitTopLevelComment = async (): Promise<number> => {
  return await commentApi.insertComment({
    articleId: props.articleId,
    uid: props.articleUserId,  // 文章作者 id
    parentId: props.articleId,  // 一级评论的 parentId = 文章 id
    commentGrade: 1,
    targetId: props.articleId,  // 目标 id = 文章 id
    targetUid: props.articleUserId,  // 目标用户 id = 文章作者 id
    content: commentText.value.trim(),
    type: 1  // 1=评论文章
  })
}

// 提交回复评论
const submitReply = async (): Promise<number> => {
  if (!replyTarget.value) return 0

  const target = replyTarget.value
  const topLevelComment = findTopLevelComment(target.id)

  if (!topLevelComment) return 0

  // 判断是二级回复还是三级回复
  const isLevel2 = target.commentGrade === 1
  const commentGrade = isLevel2 ? 2 : 3
  // targetId: 二级回复时用一级评论的 id，三级回复时用被回复评论的 targetId
  const targetId = isLevel2 ? target.id : Number(target.targetId)

  return await commentApi.insertComment({
    articleId: props.articleId,
    uid: props.articleUserId,  // 文章作者 id
    parentId: topLevelComment.id,  // parentId 始终是一级评论的 id
    commentGrade,
    targetId,
    targetUid: target.commentUid || '',  // 被回复的评论的作者 id
    content: commentText.value.trim(),
    type: 2  // 2=回复评论
  })
}

// 查找一级评论
const findTopLevelComment = (commentId: number): ArticleCommentVo | null => {
  for (const comment of comments.value) {
    if (comment.id === commentId) return comment
    if (comment.mountComment?.some(c => c.id === commentId)) return comment
  }
  return null
}

// 处理回复
const handleReply = (comment: ArticleCommentVo) => {
  replyTarget.value = comment
  commentText.value = ''
  nextTick(() => {
    // 聚焦输入框
    textareaRef.value?.focus()
    // 平滑滚动到输入框，确保用户能看到"正在回复"提示
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
const canDelete = (comment: ArticleCommentVo): boolean => {
  return authStore.isLoggedIn && comment.commentUid === authStore.user?.uuid
}

// 删除评论
const handleDelete = async (comment: ArticleCommentVo) => {
  if (!confirm('确定要删除这条评论吗？')) return

  try {
    await commentApi.deleteComment({
      id: comment.id,
      articleId: comment.articleId,
      uid: comment.uid,
      commentUid: comment.commentUid
    })

    // 从列表中移除评论
    removeCommentFromList(comment.id)
  } catch (error) {
    console.error('删除评论失败:', error)
  }
}

// 从列表中移除评论
const removeCommentFromList = (commentId: number) => {
  // 查找并移除一级评论
  const index = comments.value.findIndex(c => c.id === commentId)
  if (index !== -1) {
    comments.value.splice(index, 1)
    return
  }

  // 查找并移除二级/三级评论
  for (const comment of comments.value) {
    if (comment.mountComment) {
      const replyIndex = comment.mountComment.findIndex(c => c.id === commentId)
      if (replyIndex !== -1) {
        comment.mountComment.splice(replyIndex, 1)
        return
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
  refresh: loadComments
})
</script>

<style scoped>
.comment-section {
  margin-left: 92px; /* 与 ArticleDetail 的 article-main-content 保持一致，避开左侧操作栏 */
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: var(--space-6);
}

/* 响应式：移动端取消左边距 */
@media (max-width: 768px) {
  .comment-section {
    margin-left: 0;
  }
}

.comment-header {
  margin-bottom: var(--space-4);
}

.comment-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.title-icon {
  width: 20px;
  height: 20px;
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
  width: 40px;
  height: 40px;
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

.reply-avatar {
  width: 28px;
  height: 28px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
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
</style>
