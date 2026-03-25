<template>
  <div class="time-notes-page">
    <div class="time-notes-container">
      <!-- 左侧：时间线列表（固定高度，内部滚动） -->
      <div class="time-list-section" ref="listSectionRef" :style="{ top: `${stickyTop}px` }">
        <TimeNotesList
          :time-notes-groups="timeNotesGroups"
          :selected-id="selectedId"
          :loading="loading"
          :has-more="hasMore"
          :total="total"
          @select="handleSelect"
          @load-more="loadMore"
        />
      </div>

      <!-- 右侧：详情（不固定高度，浏览器滚动） -->
      <div class="time-detail-section">
        <TimeNotesDetail
          :detail="selectedDetail"
          :loading="detailLoading"
          @edit="handleEdit"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { TimeNotesVo, TimeNotesInfo } from '~/types'

definePageMeta({
  sidebar: false,
  showTabBar: false
})

const route = useRoute()
const router = useRouter()

const { getTimeNotesList, getTimeNotesDetail: getTimeNotesDetailApi } = useTimeNotesApi()

// 状态管理
const timeNotesGroups = ref<TimeNotesVo[]>([])
const selectedId = ref<number | null>(null)
const selectedDetail = ref<TimeNotesInfo | null>(null)
const loading = ref(false)
const detailLoading = ref(false)

const pageNum = ref(1)
const pageSize = 20
const total = ref(0)

// 列表区域 sticky top 值（动态计算）
const listSectionRef = ref<HTMLElement | null>(null)
// 初始值：navHeight(66) + gap(5) = 71
const stickyTop = ref(71)

const hasMore = computed(() => {
  let count = 0
  timeNotesGroups.value.forEach(group => {
    count += group.list?.length || 0
  })
  return count < total.value
})

// 更新 URL 参数（不产生历史记录）
const updateUrlParam = (id: number | null) => {
  if (!import.meta.client) return

  const query: Record<string, string> = { ...route.query }
  if (id) {
    query.id = String(id)
  } else {
    delete query.id
  }

  // 使用 replace 避免产生过多历史记录
  router.replace({ query })
}

// 监听滚动，动态调整列表的 sticky top 值
const handleScroll = () => {
  if (!import.meta.client) return

  const scrollY = window.scrollY
  const navHeight = 66  // 导航栏高度
  const gap = 5         // 期望的间距
  const layoutPaddingTop = 20  // layout 的 padding-top

  // 导航栏吸顶后，列表 sticky top = navHeight + gap
  if (scrollY >= layoutPaddingTop) {
    stickyTop.value = navHeight + gap  // 71px
  } else {
    // 导航栏未吸顶，保持列表紧贴导航栏底部
    stickyTop.value = layoutPaddingTop + navHeight + gap - scrollY
  }
}

onMounted(() => {
  if (import.meta.client) {
    window.addEventListener('scroll', handleScroll, { passive: true })
    // 初始化时计算一次
    handleScroll()
  }
})

onUnmounted(() => {
  if (import.meta.client) {
    window.removeEventListener('scroll', handleScroll)
  }
})

// 加载时光小记列表
const loadTimeNotes = async (reset = false) => {
  if (loading.value) return

  if (reset) {
    pageNum.value = 1
  }

  loading.value = true
  try {
    const result = await getTimeNotesList({
      pageNum: pageNum.value,
      pageSize
    })

    if (reset) {
      timeNotesGroups.value = result.rows || []
    } else {
      timeNotesGroups.value = [...timeNotesGroups.value, ...(result.rows || [])]
    }

    total.value = result.total || 0
    pageNum.value++

    // 如果没有选中项，默认选中第一条并加载详情
    if (!selectedId.value && timeNotesGroups.value.length > 0) {
      const firstGroup = timeNotesGroups.value[0]
      if (firstGroup.list && firstGroup.list.length > 0) {
        const firstNote = firstGroup.list[0]
        handleSelect(firstNote.id)
      }
    }
  } catch (error) {
    console.error('加载时光小记失败:', error)
    if (import.meta.client) {
      // @ts-ignore
      window.$message?.error('加载失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

// 加载详情
const loadDetail = async (id: number) => {
  if (!id) return

  detailLoading.value = true
  try {
    const detail = await getTimeNotesDetailApi(id)
    selectedDetail.value = detail
  } catch (error) {
    console.error('加载详情失败:', error)
    selectedDetail.value = null
  } finally {
    detailLoading.value = false
  }
}

// 选择时光小记
const handleSelect = (id: number) => {
  selectedId.value = id
  updateUrlParam(id)
  loadDetail(id)

  // 滚动到页面顶部
  if (import.meta.client) {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 加载更多
const loadMore = () => {
  if (hasMore.value && !loading.value) {
    loadTimeNotes(false)
  }
}

// 编辑时光小记
const handleEdit = (id: number) => {
  navigateTo(`/write/note/${id}`)
}

// 初始化
// 1. 先加载列表
await loadTimeNotes(true)

// 2. 从 URL 读取 id 参数，如果有则加载对应详情
const urlId = route.query.id
if (urlId) {
  const id = Number(urlId)
  if (!isNaN(id)) {
    selectedId.value = id
    await loadDetail(id)
  }
}
</script>

<style scoped>
.time-notes-page {
  /* 不限制高度，让内容撑开，由浏览器滚动条控制 */
}

.time-notes-container {
  display: flex;
  gap: 0;
  align-items: flex-start;
  padding-top: 5px; /* 与导航栏保持 5px 间隙 */
}

.dark .time-notes-container {
  background: rgba(30, 32, 35, 0.75);
  border-color: rgba(255, 255, 255, 0.06);
}

/* 左侧：时间线列表 - 固定高度，sticky 定位 */
.time-list-section {
  flex: 2;
  min-width: 0;
  position: sticky;
  top: 71px; /* navHeight(66) + gap(5) */
  height: calc(100vh - 91px);
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-right: none;
  border-radius: 16px 0 0 16px;
  overflow: hidden;
}

.dark .time-list-section {
  background: rgba(30, 32, 35, 0.75);
  border-color: rgba(255, 255, 255, 0.06);
}

/* 中间分隔线 */
.time-list-section::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: rgba(0, 0, 0, 0.05);
}

.dark .time-list-section::after {
  background: rgba(255, 255, 255, 0.06);
}

/* 右侧：详情 - 不固定高度，随内容增长 */
.time-detail-section {
  flex: 3;
  min-width: 0;
  background: rgba(255, 255, 255, 0.75);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.4);
  border-left: none;
  border-radius: 0 16px 16px 0;
  overflow: visible;
}

.dark .time-detail-section {
  background: rgba(30, 32, 35, 0.75);
  border-color: rgba(255, 255, 255, 0.06);
}

/* 移动端：上下布局 */
@media (max-width: 768px) {
  .time-notes-container {
    flex-direction: column;
  }

  .time-list-section {
    position: static;
    height: auto;
    max-height: 50vh;
    border-radius: 16px 16px 0 0;
    border: 1px solid rgba(255, 255, 255, 0.4);
    border-bottom: none;
  }

  .dark .time-list-section {
    border-color: rgba(255, 255, 255, 0.06);
  }

  .time-list-section::after {
    right: 20px;
    left: 20px;
    top: auto;
    bottom: 0;
    width: auto;
    height: 1px;
  }

  .time-detail-section {
    flex: 1;
    border-radius: 0 0 16px 16px;
    border: 1px solid rgba(255, 255, 255, 0.4);
    border-top: none;
  }

  .dark .time-detail-section {
    border-color: rgba(255, 255, 255, 0.06);
  }
}
</style>
