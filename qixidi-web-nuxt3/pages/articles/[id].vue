<template>
  <div class="article-detail-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="loading-state">
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <p>{{ error.message }}</p>
    </div>

    <!-- 文章内容 -->
    <template v-else-if="article">
      <ArticleDetail
        :article="article"
        @collect="handleCollect"
        @like="handleLike"
        @comment="scrollToComments"
      />

      <!-- 收藏夹弹窗 -->
      <ArticleCollectionDialog
        v-model:show="showCollectionDialog"
        :article-id="article.id"
        :article-label-ids="article.labelNameList?.join(',') || ''"
        @success="onCollectionSuccess"
      />

      <!-- 评论区 -->
      <ArticleCommentSection :article-id="article.id" :article-user-id="article.userId" />
    </template>
  </div>
</template>

<script setup lang="ts">
// 配置页面 meta
definePageMeta({
  showTabBar: false,
  sidebar: 'article'
})

const route = useRoute()
const articleApi = useArticleApi()
const fabulousApi = useFabulousApi()
const collectionApi = useCollectionApi()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()

// 使用独立的 state 存储侧边栏数据，避免与 pageMeta 冲突
const sidebarData = useState('article-sidebar-data', () => ({
  author: null,
  articleContent: '',
  showToc: true
}))

const articleId = computed(() => route.params.id as string)
// 评论默认显示
const showComments = ref(true)
// 收藏弹窗显示
const showCollectionDialog = ref(false)

// 获取文章详情
const { data: article, pending, error, refresh } = await useAsyncData(
  `article-${articleId.value}`,
  () => articleApi.getArticleDetail(articleId.value),
  {
    transform: (data) => {
      // 确保 fabulousUserSet 是 Set 类型（SSR 序列化后会变成数组）
      if (data?.fabulousUserSet && !(data.fabulousUserSet instanceof Set)) {
        data.fabulousUserSet = new Set(data.fabulousUserSet)
      }
      return data
    }
  }
)

// 设置侧边栏数据（使用 watch 而不是 watchEffect，避免循环）
watch(() => article.value, (newArticle) => {
  if (newArticle) {
    sidebarData.value = {
      author: {
        userId: newArticle.userId,
        nickname: newArticle.nickname,
        avatar: newArticle.avatar,
        occupation: newArticle.occupation,
        isFollow: newArticle.isFollow,
        // 统计数据
        articleCount: newArticle.articleCount,
        fansFollowCount: newArticle.fansFollowCount,
        fabulousCount: newArticle.fabulousCount
      },
      articleContent: newArticle.articleContent,
      showToc: true
    }
  }
}, { immediate: true })

// 滚动到评论区
const scrollToComments = () => {
  showComments.value = true
  nextTick(() => {
    const commentsSection = document.querySelector('.comment-section')
    if (commentsSection) {
      commentsSection.scrollIntoView({ behavior: 'smooth' })
    }
  })
}

// 收藏文章
const handleCollect = async () => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }

  if (!article.value) return

  // 已收藏状态：取消收藏
  if (article.value.isCollection && article.value.collectionRecordId) {
    try {
      await collectionApi.removeArticleFromCollection(
        article.value.collectionRecordId,
        article.value.labelNameList?.join(',') || ''
      )
      // 取消成功，更新状态
      article.value.isCollection = false
      article.value.collectionRecordId = undefined
      article.value.collectionTimes = (article.value.collectionTimes || 0) - 1
    } catch (err) {
      console.error('取消收藏失败:', err)
    }
    return
  }

  // 未收藏状态：显示收藏夹弹窗
  showCollectionDialog.value = true
}

// 收藏成功回调
const onCollectionSuccess = () => {
  if (!article.value) return

  // 更新收藏状态
  article.value.isCollection = true
  article.value.collectionTimes = (article.value.collectionTimes || 0) + 1

  // 注意：collectionRecordId 需要从后端返回，或者刷新文章详情获取
  // 这里简单处理：刷新文章详情
  refresh()
}

// 点赞文章
const handleLike = async () => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog(route.fullPath)
    return
  }

  if (!article.value) return

  // 确保 fabulousUserSet 是 Set 类型
  if (!article.value.fabulousUserSet || !(article.value.fabulousUserSet instanceof Set)) {
    article.value.fabulousUserSet = new Set()
  }

  // 判断当前是否已点赞（使用 uuid）
  const userId = authStore.user?.uuid
  const hasLiked = userId && article.value.fabulousUserSet.has(userId) || false
  const newState = !hasLiked

  // 保存旧状态，用于失败回滚
  const oldLikedState = hasLiked
  const oldLikeTimes = article.value.likeTimes || 0

  try {
    // 1. 调用后端接口（传递点赞前的数量）
    const requestData = {
      typeId: article.value.id,
      targetId: article.value.id,
      targetUid: article.value.userId,
      type: 1,
      state: 0,
      fabulousSum: oldLikeTimes,  // 传递点赞前的数量
      targetTitle: article.value.articleTitle || '',
      labelId: article.value.labelNameList?.join(',') || ''
    }

    if (newState) {
      await fabulousApi.likeArticle(requestData)
    } else {
      await fabulousApi.cancelLikeArticle(requestData)
    }

    // 2. 接口成功后，更新 UI
    if (newState && userId) {
      article.value.fabulousUserSet.add(userId)
    } else if (userId) {
      article.value.fabulousUserSet.delete(userId)
    }
    article.value.likeTimes = oldLikeTimes + (newState ? 1 : -1)

    // 3. 成功，保持更新状态
  } catch (err) {
    console.error('点赞操作失败:', err)
    // 失败不需要回滚，因为还没更新 UI
  }
}

// SEO 设置
useHead({
  title: () => article.value?.articleTitle || '文章详情',
  meta: [
    {
      name: 'description',
      content: () => article.value?.articleAbstract || ''
    }
  ]
})

// 页面挂载时立即滚动到顶部（避免从首页滚动的位置过渡）
onMounted(() => {
  window.scrollTo({ top: 0, behavior: 'instant' })
})
</script>

<style scoped>
.article-detail-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.loading-state,
.error-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  color: var(--color-ink-muted);
}
</style>
