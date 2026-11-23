<template>
  <div class="changelog-page">
    <!-- 页头：衬线标题 + 说明（一张纸排版） -->
    <header class="changelog-header">
      <h1 class="changelog-title">版本迭代</h1>
      <p class="changelog-lead">与 Gitee 发行版保持同步</p>
    </header>

    <!-- 加载状态 -->
    <div v-if="pending" class="changelog-loading">
      <n-spin size="large" />
    </div>

    <!-- 空状态（Gitee 不可达且无缓存时兜底） -->
    <div v-else-if="!releases.length" class="changelog-empty">
      <CommonEmptyState description="暂无版本信息" />
    </div>

    <!-- 双栏：左侧版本导航（sticky）+ 右侧版本内容 -->
    <div v-else class="changelog-layout">
      <nav class="changelog-nav">
        <button
          v-for="(release, index) in releases"
          :key="release.tag"
          class="changelog-nav-item"
          :class="{ active: activeIndex === index }"
          @click="scrollToRelease(index)"
        >
          <span class="nav-item-tag">{{ release.tag }}</span>
          <span class="nav-item-name">{{ release.name }}</span>
        </button>
      </nav>

      <!-- 版本时间线 -->
      <div class="changelog-list">
        <section
          v-for="(release, index) in releases"
          :key="release.tag"
          :ref="el => releaseEls[index] = el as HTMLElement | null"
          class="changelog-release"
        >
          <div class="release-head">
            <span class="release-tag">{{ release.tag }}</span>
            <time class="release-date" :title="release.publishedAt">{{ formatDate(release.publishedAt) }}</time>
          </div>
          <h2 class="release-name">{{ release.name }}</h2>
          <MarkdownRenderer :html="release.html" class="release-body" />
        </section>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface RenderedRelease {
  tag: string
  name: string
  body: string
  publishedAt: string
  html: string
}

definePageMeta({ showTabBar: false })

useHead({
  title: '版本迭代',
  bodyAttrs: {
    class: 'page-changelog'
  }
})

const { renderMarkdown } = useMarkdown()

// SSR 拉 /changelog-data（Nitro 代理 Gitee，带缓存）并渲染各版本 Markdown 正文
const { data, pending } = await useAsyncData(
  'changelog',
  async () => {
    try {
      const result = await $fetch<{ current: string; releases: Omit<RenderedRelease, 'html'>[] }>('/changelog-data')
      const releases = await Promise.all(
        (result.releases || []).map(async r => ({ ...r, html: await renderMarkdown(r.body || '') }))
      )
      return { current: result.current, releases }
    } catch {
      return { current: '', releases: [] as RenderedRelease[] }
    }
  }
)

const releases = computed<RenderedRelease[]>(() => data.value?.releases || [])

// ISO 时间 → yyyy-MM-dd
const formatDate = (iso: string) => {
  if (!iso) return ''
  return iso.substring(0, 10)
}

// ===== 左侧版本导航 =====
const releaseEls = ref<(HTMLElement | null)[]>([])
const activeIndex = ref(0)

const scrollToRelease = (index: number) => {
  const el = releaseEls.value[index]
  if (!el) return
  // 顶部导航栏吸顶遮挡：锚点上移一档（scroll-margin-top 兜底，这里显式计算）
  const top = el.getBoundingClientRect().top + window.scrollY - 90
  window.scrollTo({ top, behavior: 'smooth' })
  activeIndex.value = index
}

// 滚动高亮：取「顶边已过视口 1/3 线的最后一个版本块」（判定线取视口中带，避免懒加载撑高漂移）
const handleScroll = () => {
  const line = window.innerHeight / 3
  let current = 0
  releaseEls.value.forEach((el, i) => {
    if (el && el.getBoundingClientRect().top <= line) {
      current = i
    }
  })
  activeIndex.value = current
}

onMounted(() => {
  handleScroll()
  window.addEventListener('scroll', handleScroll, { passive: true })
  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
})
</script>

<style>
.changelog-page {
  display: flex;
  flex-direction: column;
  padding-top: 24px;
}

/* ===== 页头（一张纸排版：衬线标题 + 浓淡对比） ===== */
.changelog-header {
  padding: 0 8px 20px;
  border-bottom: 1px solid var(--color-border-light);
}

.changelog-title {
  font-family: var(--font-display);
  font-size: var(--text-2xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  letter-spacing: 0.02em;
}

.changelog-lead {
  margin: 6px 0 0;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.changelog-loading,
.changelog-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* ===== 双栏布局：导航 + 内容 ===== */
.changelog-layout {
  display: flex;
  align-items: flex-start;
  gap: 36px;
}

/* 左侧版本导航（sticky 跟随，排版线语言：hairline 分隔 + 激活墨染） */
.changelog-nav {
  position: sticky;
  top: 86px;
  width: 240px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  padding: 16px 0;
  max-height: calc(100vh - 120px);
  overflow-y: auto;
  scrollbar-width: none;
}

.changelog-nav::-webkit-scrollbar {
  display: none;
}

.changelog-nav-item {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
  padding: 10px 12px;
  border: none;
  border-left: 2px solid transparent;
  background: none;
  cursor: pointer;
  text-align: left;
  transition: all var(--transition-fast);
  font-family: var(--font-body);
}

.changelog-nav-item:hover {
  background: color-mix(in srgb, var(--color-ink) 3%, transparent);
}

.changelog-nav-item.active {
  border-left-color: var(--color-primary);
  background: color-mix(in srgb, var(--color-primary) 6%, transparent);
}

.nav-item-tag {
  font-size: var(--text-xs);
  font-weight: 600;
  color: var(--color-ink-light);
  font-variant-numeric: tabular-nums;
}

.changelog-nav-item.active .nav-item-tag {
  color: var(--color-primary);
}

.nav-item-name {
  font-size: var(--text-2xs);
  color: var(--color-ink-faint);
  line-height: var(--leading-normal);
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  max-width: 100%;
}

.changelog-nav-item.active .nav-item-name {
  color: var(--color-ink-muted);
}

/* ===== 版本块 ===== */
.changelog-list {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.changelog-release {
  padding: 28px 8px 32px;
  border-bottom: 1px solid var(--color-border-light);
  /* 锚点滚动时避开吸顶导航（与 scrollToRelease 的 -90 计算双保险） */
  scroll-margin-top: 90px;
}

.changelog-release:last-child {
  border-bottom: none;
}

.release-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

/* 版本徽章：品牌浅底胶囊（与 .bm-tab 激活态同语言） */
.release-tag {
  padding: 2px 12px;
  font-size: var(--text-xs);
  font-weight: 600;
  color: var(--color-primary);
  background: var(--color-primary-light);
  border-radius: var(--radius-full);
  font-variant-numeric: tabular-nums;
  white-space: nowrap;
}

.release-date {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  font-variant-numeric: tabular-nums;
}

.release-name {
  margin: 12px 0 0;
  font-size: var(--text-lg);
  font-weight: 600;
  line-height: var(--leading-tight);
  color: var(--color-ink);
}

/* 正文沿用 MarkdownRenderer 全局样式，收窄版心 */
.release-body {
  margin-top: 12px;
  max-width: 760px;
}

/* ===== 移动端：隐藏左侧导航，内容单栏 ===== */
@media (max-width: 768px) {
  body.page-changelog .home-main {
    padding-top: 70px !important;
  }

  .changelog-nav {
    display: none;
  }

  .changelog-layout {
    display: block;
  }

  .changelog-release {
    padding: 22px 0 26px;
  }

  .release-name {
    font-size: var(--text-base);
  }
}
</style>
