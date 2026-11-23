<template>
  <div class="tool-page">
    <!-- 页头：衬线标题 + 工具数（一张纸排版） -->
    <header class="tool-header">
      <div class="tool-header-main">
        <h1 class="tool-title">工具箱</h1>
        <span v-if="tools.length" class="tool-count">共 {{ tools.length }} 个</span>
      </div>
      <p class="tool-lead">开发路上常用的工具与资源收藏</p>
    </header>

    <!-- 横向分类菜单 -->
    <nav ref="tabBarRef" class="tool-tab-bar" :class="{ 'is-sticky': isSticky }">
      <div v-if="pendingCategories" class="tool-tab-loading">
        <n-spin size="small" />
      </div>
      <template v-else>
        <button
          v-for="cat in categories"
          :key="cat.id"
          class="bm-tab"
          :class="{ active: activeCategoryId === cat.id }"
          @click="selectCategory(cat.id)"
        >
          {{ cat.toolName }}
        </button>
      </template>
    </nav>

    <!-- 工具列表 -->
    <div class="tool-content">
      <div v-if="loadingTools" class="tool-loading">
        <n-spin size="large" />
      </div>
      <div v-else-if="!tools.length" class="tool-empty">
        <CommonEmptyState description="暂无工具" />
      </div>
      <div v-else class="tool-grid">
        <a
          v-for="tool in tools"
          :key="tool.id"
          :href="tool.toolUrl"
          target="_blank"
          rel="noopener noreferrer"
          class="bm-card tool-card"
        >
          <span class="tool-card-icon" :class="{ 'is-placeholder': !tool.icon || iconErrors.has(tool.id) }">
            <img
              v-if="tool.icon && !iconErrors.has(tool.id)"
              :src="tool.icon"
              :alt="tool.toolName"
              loading="lazy"
              @error="iconErrors.add(tool.id)"
            />
            <n-icon v-else size="20"><Tool /></n-icon>
          </span>
          <span class="tool-card-body">
            <span class="tool-card-name bm-card-name">{{ tool.toolName }}</span>
            <span class="tool-card-desc" v-if="tool.describe">{{ tool.describe }}</span>
          </span>
          <n-icon class="tool-card-link" size="14"><ExternalLink /></n-icon>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Tool, ExternalLink } from '@vicons/tabler'

definePageMeta({ showTabBar: false })

useHead({
  title: '工具箱',
  bodyAttrs: {
    class: 'page-tool'
  }
})

const toolApi = useToolApi()

const tools = ref<any[]>([])
const loadingTools = ref(false)
const tabBarRef = ref<HTMLElement | null>(null)
const isSticky = ref(false)
// 外链图标加载失败的 id 集合（裂图兜底显示占位图标）
const iconErrors = reactive(new Set<number>())

// SSR 失败返回 null（勿 []，勿 default），水合后客户端自动重取
const { data: categoriesData, pending: pendingCategories } = await useAsyncData(
  'tool-categories',
  async () => {
    try {
      return await toolApi.getToolCategories()
    } catch {
      return null
    }
  }
)
const categories = computed(() => categoriesData.value || [])
const activeCategoryId = ref<number | null>(null)

const loadTools = async (parentId: number) => {
  loadingTools.value = true
  try {
    const res = await toolApi.getToolList(parentId)
    tools.value = res || []
  } finally {
    loadingTools.value = false
  }
}

const selectCategory = (id: number) => {
  if (activeCategoryId.value === id) return
  activeCategoryId.value = id
  loadTools(id)
}

// 分类数据到达后，默认选中第一个并加载工具
watch(categories, (cats) => {
  if (cats.length > 0 && activeCategoryId.value === null) {
    activeCategoryId.value = cats[0].id
    loadTools(cats[0].id)
  }
}, { immediate: true })

// 吸顶逻辑 - 与 ContentTabBar 一致
onMounted(() => {
  const checkSticky = () => {
    if (!tabBarRef.value) return
    const rect = tabBarRef.value.getBoundingClientRect()
    isSticky.value = rect.top <= 66
  }

  checkSticky()
  window.addEventListener('scroll', checkSticky, { passive: true })

  onUnmounted(() => {
    window.removeEventListener('scroll', checkSticky)
  })
})
</script>

<style>
.tool-page {
  display: flex;
  flex-direction: column;
  padding-top: 24px;
}

/* ===== 页头（一张纸排版：衬线标题 + 浓淡对比） ===== */
.tool-header {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 0 8px 16px;
}

.tool-header-main {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.tool-title {
  font-family: var(--font-display);
  font-size: var(--text-2xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  letter-spacing: 0.02em;
}

.tool-count {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.tool-lead {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin: 0;
}

/* 横向分类菜单（tab 样式走全局 .bm-tab 书签语言） */
.tool-tab-bar {
  position: sticky;
  top: 66px;
  z-index: 40;
  display: flex;
  flex-wrap: nowrap;
  align-items: center;
  gap: var(--space-2);
  padding: 10px 16px;
  margin-bottom: 16px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  -ms-overflow-style: none;
  background: transparent;
  transition: background-color 0.3s ease;
}

.tool-tab-bar::-webkit-scrollbar {
  display: none;
}

/* 吸顶 - 纸色+底部细线（与 ContentTabBar 一致，无磨玻璃无阴影） */
.tool-tab-bar.is-sticky {
  background: var(--color-page-bg);
  border-bottom: 1px solid var(--color-border);
}

.tool-tab-loading {
  display: flex;
  align-items: center;
  padding: 4px 0;
}

/* 工具内容 */
.tool-content {
  min-width: 0;
}

.tool-loading,
.tool-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* 工具网格：两列收录卡片（卡片底走全局 .bm-card） */
.tool-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

/* 单个卡片：垂直结构（图标在上），图标统一容器底 + 右上外链提示 */
.tool-card {
  position: relative;
  flex-direction: column;
  align-items: flex-start;
  gap: 0;
  padding: 20px 20px 18px;
}

.tool-card-icon {
  width: 46px;
  height: 46px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  background: color-mix(in srgb, var(--color-ink) 4%, transparent);
  color: var(--color-ink-muted);
  flex-shrink: 0;
  overflow: hidden;
  margin-bottom: 12px;
}

.tool-card-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}

.tool-card-icon.is-placeholder {
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.tool-card-body {
  width: 100%;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.tool-card-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-right: 20px;
}

.tool-card-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 外链角标：默认隐淡，卡片 hover 时墨蓝浮现 */
.tool-card-link {
  position: absolute;
  top: 14px;
  right: 12px;
  color: var(--color-ink-faint);
  flex-shrink: 0;
  transition: color var(--transition-fast);
}

.tool-card:hover .tool-card-link {
  color: var(--color-primary);
}

/* 移动端 */
@media (max-width: 768px) {
  .tool-tab-bar {
    position: fixed;
    top: 56px;
    left: 0;
    right: 0;
    z-index: 90;
    flex-wrap: wrap;
    padding: 10px 16px;
    margin-bottom: 0;
    background: var(--color-page-bg);
  }

  .tool-content {
    padding-top: 50px;
  }

  .tool-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
}

@media (max-width: 480px) {
  .tool-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  body.page-tool .home-main {
    padding-top: 140px !important;
  }
}
</style>
