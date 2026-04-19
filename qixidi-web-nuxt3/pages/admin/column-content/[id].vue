<template>
  <div class="column-content-page">
    <!-- 顶部导航 -->
    <div class="page-header">
      <div class="header-left">
        <n-button quaternary circle @click="router.back()">
          <template #icon>
            <n-icon :size="20"><ArrowLeft /></n-icon>
          </template>
        </n-button>
        <n-divider vertical />
        <div class="header-info">
          <h2 class="page-title">专栏内容管理</h2>
          <span class="column-name">{{ specialName }}</span>
        </div>
      </div>
      <n-button type="primary" :loading="saving" :disabled="!hasChanges" @click="handleSave">
        <template #icon>
          <n-icon><Check /></n-icon>
        </template>
        保存
      </n-button>
    </div>

    <!-- 穿梭框 -->
    <div class="transfer-wrapper">
      <n-spin :show="loading">
        <n-transfer
          v-model:value="selectedIds"
          :options="options"
          source-filterable
          target-filterable
        />
      </n-spin>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Check } from '@vicons/tabler'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const route = useRoute()
const router = useRouter()
const message = useMessage()
const authStore = useAuthStore()

const specialId = computed(() => Number(route.params.id))
const specialName = computed(() => decodeURIComponent(route.query.name as string || ''))

const loading = ref(false)
const saving = ref(false)
const options = ref<{ label: string; value: number }[]>([])
const selectedIds = ref<number[]>([])
const originalSelectedIds = ref<number[]>([])

// 是否有变更
const hasChanges = computed(() => {
  const current = [...selectedIds.value].sort()
  const original = [...originalSelectedIds.value].sort()
  return JSON.stringify(current) !== JSON.stringify(original)
})

const specialContentApi = useSpecialContentApi()

const loadData = async () => {
  if (!authStore.user?.uuid) return

  loading.value = true
  try {
    const [userArticlesRes, specialArticleIds] = await Promise.all([
      specialContentApi.getUserArticleList({ userId: authStore.user.uuid, pageSize: 1000 }),
      specialContentApi.getSpecialArticleIds(specialId.value, authStore.user.uuid)
    ])

    console.log('API返回数据:', { userArticlesRes, specialArticleIds })

    // 转换为穿梭框选项
    options.value = (userArticlesRes.rows || []).map(article => ({
      label: article.articleTitle,
      value: article.id
    }))

    // 已选中的 ID
    selectedIds.value = (specialArticleIds || []).map((article: any) => article.id)
    // 保存原始值用于比较
    originalSelectedIds.value = [...selectedIds.value]

    console.log('转换后options:', options.value.length, '条, selectedIds:', selectedIds.value.length, '条')
  } catch (e) {
    console.error('加载数据失败:', e)
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!authStore.user?.uuid) return

  saving.value = true
  try {
    // 接口：POST /white/update/special/{id}/{uid}，Body: List<String> 文章ID
    await specialContentApi.updateSpecialArticles(
      specialId.value,
      authStore.user.uuid,
      selectedIds.value.map(String)
    )
    message.success('保存成功')
    // 更新原始值
    originalSelectedIds.value = [...selectedIds.value]
    router.back()
  } catch (e) {
    console.error('保存失败:', e)
  } finally {
    saving.value = false
  }
}

onMounted(() => loadData())
</script>

<style scoped>
.column-content-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-info {
  display: flex;
  align-items: baseline;
  gap: 12px;
}

.page-title {
  font-size: var(--text-lg);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.column-name {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
}

.transfer-wrapper {
  flex: 1;
  min-height: 0;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 20px;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
}

.transfer-wrapper :deep(.n-spin) {
  flex: 1;
}

.transfer-wrapper :deep(.n-transfer) {
  height: 800px;
}
</style>
