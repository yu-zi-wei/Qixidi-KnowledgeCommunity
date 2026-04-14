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
        :show-collect="false"
        @like="handleLike"
        @comment="scrollToComments"
        @edit="handleEdit"
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

// 固定的关于作者文章 ID
const ABOUT_AUTHOR_ARTICLE_ID = '-13'

const articleApi = useArticleApi()
const { siteName } = useRuntimeConfig().public
const fabulousApi = useFabulousApi()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()

// 使用独立的 state 存储侧边栏数据（与 ArticleSidebar 组件共享）
const sidebarData = useState('article-sidebar-data', () => ({
  author: null as any,
  articleContent: '',
  showToc: true,
  specialCard: null as any
}))

// 评论默认显示
const showComments = ref(true)

// 获取文章详情
const { data: article, pending, error } = await useAsyncData(
  'about-author-article',
  () => articleApi.getArticleDetail(ABOUT_AUTHOR_ARTICLE_ID),
  {
    transform: (data) => {
      if (data?.fabulousUserSet && !(data.fabulousUserSet instanceof Set)) {
        data.fabulousUserSet = new Set(data.fabulousUserSet)
      }
      return data
    }
  }
)

// 设置侧边栏数据（显示作者社交卡片）
watch(() => article.value, (newArticle) => {
  if (newArticle) {
    sidebarData.value = {
      author: null,
      articleContent: newArticle.articleContent,
      showToc: true,
      specialCard: {
        type: 'author-social',
        data: {
          nickname: newArticle.nickname,
          avatar: newArticle.avatar,
          motto: '他强任他强，清风拂山岗',
          github: 'https://github.com/yu-zi-wei',
          gitee: 'https://gitee.com/yu-zi-wei',
          email: '2978824265@qq.com'
        }
      }
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

// 点赞文章
const handleLike = async () => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog('/regarding/about-author')
    return
  }

  if (!article.value) return

  if (!article.value.fabulousUserSet || !(article.value.fabulousUserSet instanceof Set)) {
    article.value.fabulousUserSet = new Set()
  }

  const userId = authStore.user?.uuid
  const hasLiked = userId && article.value.fabulousUserSet.has(userId) || false
  const newState = !hasLiked
  const oldLikeTimes = article.value.likeTimes || 0

  try {
    const requestData = {
      typeId: article.value.id,
      targetId: article.value.id,
      targetUid: article.value.userId,
      type: 1,
      state: 0,
      fabulousSum: oldLikeTimes,
      targetTitle: article.value.articleTitle || '',
      labelId: article.value.labelNameList?.join(',') || ''
    }

    if (newState) {
      await fabulousApi.likeArticle(requestData)
    } else {
      await fabulousApi.cancelLikeArticle(requestData)
    }

    if (newState && userId) {
      article.value.fabulousUserSet.add(userId)
    } else if (userId) {
      article.value.fabulousUserSet.delete(userId)
    }
    article.value.likeTimes = oldLikeTimes + (newState ? 1 : -1)
  } catch (err) {
    console.error('点赞操作失败:', err)
  }
}

// 编辑文章
const handleEdit = () => {
  if (!article.value) return
  navigateTo(`/write/article/${article.value.id}`)
}

// SEO 设置
useHead({
  title: () => article.value?.articleTitle || '关于作者',
  meta: [
    {
      name: 'description',
      content: () => article.value?.articleAbstract || `了解${siteName}博客作者`
    }
  ],
  bodyAttrs: {
    class: 'page-regarding'
  }
})

// 页面挂载时滚动到顶部
onMounted(() => {
  window.scrollTo({ top: 0, behavior: 'instant' })
})
</script>

<style scoped>
.article-detail-page {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-top: 24px;
}

.article-detail-page :deep(.article-main-content) {
  padding-top: 28px;
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

<style>
@media (max-width: 768px) {
  body.page-regarding .home-main {
    padding-top: 50px !important;
  }
}
</style>
