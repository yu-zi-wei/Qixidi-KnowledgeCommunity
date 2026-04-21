<template>
  <div class="tool-page">
    <!-- 横向分类菜单 -->
    <nav ref="tabBarRef" class="tool-tab-bar" :class="{ 'is-sticky': isSticky }">
      <div v-if="pendingCategories" class="tool-tab-loading">
        <n-spin size="small" />
      </div>
      <template v-else>
        <button
          v-for="cat in categories"
          :key="cat.id"
          class="tool-tab-item"
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
          class="tool-card"
        >
          <div class="tool-card-icon" v-if="tool.icon">
            <img :src="tool.icon" :alt="tool.toolName" />
          </div>
          <div class="tool-card-icon tool-card-placeholder" v-else>
            <n-icon size="24"><Tool /></n-icon>
          </div>
          <div class="tool-card-body">
            <h3 class="tool-card-name">{{ tool.toolName }}</h3>
            <p class="tool-card-desc" v-if="tool.describe">{{ tool.describe }}</p>
          </div>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Tool } from '@vicons/tabler'

definePageMeta({ showTabBar: false })

useHead({
  bodyAttrs: {
    class: 'page-tool'
  }
})

const toolApi = useToolApi()

const tools = ref<any[]>([])
const loadingTools = ref(false)
const tabBarRef = ref<HTMLElement | null>(null)
const isSticky = ref(false)

// 加载分类列表
const { data: categoriesData, pending: pendingCategories } = await useAsyncData(
  'tool-categories',
  () => toolApi.getToolCategories()
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
}

/* 横向分类菜单 */
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
  border-radius: 2px 2px 10px 10px;
  transition: all 0.3s ease;
}

.tool-tab-bar::-webkit-scrollbar {
  display: none;
}

.tool-tab-bar.is-sticky {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(16px) saturate(180%);
  -webkit-backdrop-filter: blur(16px) saturate(180%);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

:root.dark .tool-tab-bar.is-sticky {
  background: rgba(13, 15, 17, 0.7);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.tool-tab-loading {
  display: flex;
  align-items: center;
  padding: 4px 0;
}

.tool-tab-item {
  padding: 7px 14px;
  font-size: 13px;
  color: var(--color-ink-light);
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  white-space: nowrap;
  transition: all var(--transition-fast);
  font-family: var(--font-body);
  font-weight: 400;
  line-height: 1.5;
}

.tool-tab-item:hover {
  background: var(--color-surface-dim);
  color: var(--color-ink);
}

.tool-tab-item.active {
  background: var(--color-surface-dim);
  color: var(--color-ink);
  font-weight: 500;
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

/* 工具网格 */
.tool-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.tool-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  border-radius: var(--radius-lg);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
  border: 1px solid var(--color-border-light);
}

.tool-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
  border-color: var(--color-primary);
}

.tool-card-icon {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-md);
  overflow: hidden;
  flex-shrink: 0;
}

.tool-card-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}

.tool-card-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.tool-card-body {
  text-align: center;
  width: 100%;
}

.tool-card-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tool-card-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 4px 0 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(16px);
    -webkit-backdrop-filter: blur(16px);
  }

  :root.dark .tool-tab-bar {
    background: rgba(13, 15, 17, 0.7);
  }

  .tool-content {
    padding-top: 50px;
  }

  .tool-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
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
