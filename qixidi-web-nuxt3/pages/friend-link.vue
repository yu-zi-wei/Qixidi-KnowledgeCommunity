<template>
  <div class="friend-link-page">
    <!-- 简单 tab 切换 -->
    <div class="fl-tabs">
      <button
        class="fl-tab-item"
        :class="{ active: activeTab === 'apply' }"
        @click="activeTab = 'apply'"
      >
        友链申请
      </button>
      <button
        class="fl-tab-item"
        :class="{ active: activeTab === 'list' }"
        @click="activeTab = 'list'"
      >
        友链列表
      </button>
    </div>

    <!-- 友链申请：复用文章详情 + 评论区 -->
    <div v-if="activeTab === 'apply'">
      <div v-if="pendingArticle" class="fl-loading">
        <n-spin size="large" />
      </div>
      <template v-else-if="articleData">
        <ArticleDetail
          :article="articleData"
          :show-edit="true"
          :show-collect="false"
          @comment="scrollToComments"
          @like="handleLike"
          @edit="handleEdit"
        />
        <ArticleCommentSection :article-id="articleData.id" :article-user-id="articleData.userId" />
      </template>
    </div>

    <!-- 友链列表 -->
    <div v-else>
      <div v-if="loadingList" class="fl-loading">
        <n-spin size="large" />
      </div>
      <div v-else-if="!friendLinks.length" class="fl-empty">
        <n-empty description="暂无友链" />
      </div>
      <div v-else class="fl-grid">
        <a
          v-for="link in friendLinks"
          :key="link.id"
          :href="link.linkUrl"
          target="_blank"
          rel="noopener noreferrer"
          class="fl-card"
        >
          <div class="fl-card-avatar">
            <img v-if="link.linkAvatar" :src="link.linkAvatar" :alt="link.linkName" />
            <span v-else class="fl-card-avatar-placeholder">{{ link.linkName?.charAt(0) }}</span>
          </div>
          <div class="fl-card-body">
            <h3 class="fl-card-name">{{ link.linkName }}</h3>
            <p class="fl-card-desc" v-if="link.linkIntro">{{ link.linkIntro }}</p>
          </div>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ArticleDetail from '~/components/article/ArticleDetail.vue'
import ArticleCommentSection from '~/components/article/CommentSection.vue'

definePageMeta({ showTabBar: false })

useHead({
  bodyAttrs: {
    class: 'page-friend-link'
  }
})

const activeTab = ref('apply')

// 友链申请：获取文章详情
const articleApi = useArticleApi()
const fabulousApi = useFabulousApi()
const authStore = useAuthStore()
const authDialogStore = useAuthDialogStore()

const { data: articleData, pending: pendingArticle } = await useAsyncData(
  'friend-link-article',
  () => articleApi.getArticleDetail(-12)
)

// 编辑文章
const handleEdit = () => {
  if (!articleData.value) return
  navigateTo(`/write/article/${articleData.value.id}`)
}

// 滚动到评论区
const scrollToComments = () => {
  nextTick(() => {
    const el = document.querySelector('.comment-section')
    if (el) el.scrollIntoView({ behavior: 'smooth' })
  })
}

// 点赞
const handleLike = async () => {
  if (!authStore.isLoggedIn) {
    authDialogStore.showLoginDialog('/friend-link')
    return
  }
  if (!articleData.value) return

  if (!articleData.value.fabulousUserSet || !(articleData.value.fabulousUserSet instanceof Set)) {
    articleData.value.fabulousUserSet = new Set()
  }

  const userId = authStore.user?.uuid
  const hasLiked = userId && articleData.value.fabulousUserSet.has(userId) || false
  const newState = !hasLiked
  const oldLikeTimes = articleData.value.likeTimes || 0

  try {
    const requestData = {
      typeId: articleData.value.id,
      targetId: articleData.value.id,
      targetUid: articleData.value.userId,
      type: 1,
      state: 0,
      fabulousSum: oldLikeTimes,
      targetTitle: articleData.value.articleTitle || '',
      labelId: articleData.value.labelNameList?.join(',') || ''
    }

    if (newState) {
      await fabulousApi.likeArticle(requestData)
    } else {
      await fabulousApi.cancelLikeArticle(requestData)
    }

    if (newState && userId) {
      articleData.value.fabulousUserSet.add(userId)
    } else if (userId) {
      articleData.value.fabulousUserSet.delete(userId)
    }
    articleData.value.likeTimes = oldLikeTimes + (newState ? 1 : -1)
  } catch (err) {
    console.error('点赞操作失败:', err)
  }
}

// 友链列表：懒加载
const friendLinkApi = useFriendLinkApi()
const friendLinks = ref<any[]>([])
const loadingList = ref(false)

const loadFriendLinks = async () => {
  loadingList.value = true
  try {
    const res = await friendLinkApi.getFriendLinkList()
    friendLinks.value = res?.rows || []
  } finally {
    loadingList.value = false
  }
}

watch(activeTab, (tab) => {
  if (tab === 'list' && friendLinks.value.length === 0) {
    loadFriendLinks()
  }
})
</script>

<style>
.friend-link-page {
  display: flex;
  flex-direction: column;
  padding-top: 10px;
}

/* 简单 tab 切换 */
.fl-tabs {
  display: flex;
  gap: var(--space-2);
  padding-top: 16px;
  margin-bottom: 15px;
  margin-left: 82px;
  border-bottom: 1px solid var(--color-border-light);
}

.fl-tab-item {
  padding: 6px 16px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  font-family: var(--font-body);
  font-weight: 400;
}

.fl-tab-item:hover {
  color: var(--color-ink);
}

.fl-tab-item.active {
  color: var(--color-primary);
  font-weight: 600;
  background: var(--color-primary-light);
}

/* 加载/空状态 */
.fl-loading,
.fl-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* 友链卡片网格 */
.fl-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-left: 92px;
}

.fl-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 20px;
  border-radius: var(--radius-lg);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
  border: 1px solid var(--color-border-light);
}

.fl-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
  border-color: var(--color-primary);
}

.fl-card-avatar {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-full);
  overflow: hidden;
  flex-shrink: 0;
}

.fl-card-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.fl-card-avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: var(--color-primary-light);
  color: var(--color-primary);
  font-size: var(--text-lg);
  font-weight: 600;
}

.fl-card-body {
  flex: 1;
  min-width: 0;
}

.fl-card-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.fl-card-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 4px 0 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

@media (max-width: 768px) {
  .fl-tabs {
    margin-left: 0;
  }

  .fl-grid {
    grid-template-columns: 1fr;
    gap: 12px;
    margin-left: 0;
  }
}

@media (max-width: 768px) {
  body.page-friend-link .home-main {
    padding-top: 60px !important;
  }
}
</style>
