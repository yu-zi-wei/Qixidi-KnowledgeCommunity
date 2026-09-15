<template>
  <div class="comment-section">
    <!-- 通用楼层式评论区（数据通过适配函数注入，文章评论字段在此映射） -->
    <ThreadedCommentSection
      :biz-id="articleId"
      :biz-uid="articleUserId"
      :fetch-list="fetchCommentList"
      :submit="submitComment"
      :remove="removeComment"
      show-reply-avatar
    />
  </div>
</template>

<script setup lang="ts">
import type { ArticleCommentVo, CommentItem, CommentSubmitPayload, PageQuery, TableDataInfo } from '~/types'

/**
 * 文章评论区（通用 ThreadedCommentSection 的业务适配壳）
 * - 布局：与 ArticleDetail 的 article-main-content 对齐（margin-left 避开左侧操作栏）
 * - 数据：后端文章评论接口不分页（全量返回）、字段名与通用 CommentItem 不同，在此做映射包装
 */
interface Props {
  articleId: number
  articleUserId: string  // 文章作者 id
}

const props = defineProps<Props>()

const commentApi = useCommentApi()

// ArticleCommentVo → 通用 CommentItem（字段名映射 + 递归子评论）
const toCommentItem = (vo: ArticleCommentVo): CommentItem => ({
  id: vo.id,
  uid: vo.uid,
  parentId: vo.parentId,
  commentGrade: vo.commentGrade,
  targetId: vo.targetId,
  targetUid: vo.targetUid || '',
  commentUid: vo.commentUid || '',
  content: vo.content,
  type: vo.type || 1,
  status: vo.state ?? 0,
  createTime: vo.createTime,
  updateTime: vo.updateTime,
  username: vo.commentName,
  nickname: vo.commentName,
  avatar: vo.commentAvatar,
  targetUsername: vo.targetName,
  targetNickname: vo.targetName,
  targetAvatar: vo.targetAvatar,
  children: vo.mountComment?.map(toCommentItem) || []
})

// 后端一次性返回全量评论，包装为 TableDataInfo（total = 一级评论数，组件据此不再显示“加载更多”）
const fetchCommentList = async (_pageQuery: PageQuery): Promise<TableDataInfo<CommentItem>> => {
  const list = await commentApi.getCommentList(props.articleId)
  const rows = list.map(toCommentItem)
  return { total: rows.length, rows }
}

const submitComment = async (payload: CommentSubmitPayload): Promise<Partial<CommentItem>> => {
  const id = await commentApi.insertComment({
    articleId: props.articleId,
    ...payload,
    parentId: Number(payload.parentId),
    targetId: Number(payload.targetId)
  })
  return { id }
}

const removeComment = (id: string | number) =>
  commentApi.deleteComment(Number(id))
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
</style>
