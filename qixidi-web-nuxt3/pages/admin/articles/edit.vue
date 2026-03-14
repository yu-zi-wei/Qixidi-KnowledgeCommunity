<template>
  <div class="edit-page">
    <div class="edit-header">
      <div class="header-left">
        <h1 class="page-title">{{ isEdit ? '编辑文章' : '写文章' }}</h1>
      </div>
      <div class="header-right">
        <n-button text @click="handleBack">
          <template #icon>
            <ArrowLeft class="icon" />
          </template>
          返回
        </n-button>
      </div>
    </div>

    <div class="edit-container">
      <ArticleEditor
        ref="editorRef"
        :article-id="articleId"
        :initial-data="initialArticle"
        :groupings="groupings"
        :labels="labels"
        :specials="specials"
        @publish="handlePublishSuccess"
        @save-draft="handleSaveDraftSuccess"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onBeforeUnmount } from 'vue'
import { ArrowLeft } from '@vicons/tabler'
import type { ArticleForm, GroupingInfo, LabelInfo, SpecialInfo } from '~/types'

definePageMeta({
  layout: 'admin',
  middleware: 'auth'
})

const route = useRoute()
const router = useRouter()
const message = useMessage()
const dialog = useDialog()

const articleApi = useArticleApi()
const groupingApi = useGroupingApi()
const labelApi = useLabelApi()
const specialApi = useSpecialApi()

const editorRef = ref<InstanceType<typeof ArticleEditor>>()

// 判断编辑模式
const articleId = computed(() => route.params.id as string | undefined)
const isEdit = computed(() => !!articleId.value)

// 加载选项数据
const { data: groupingsData } = await useAsyncData('groupings', () =>
  groupingApi.getList()
)

const { data: labelsData } = await useAsyncData('labels', () =>
  labelApi.getList()
)

const { data: specialsData } = await useAsyncData('specials', () =>
  specialApi.getList()
)

// 使用计算属性确保返回数组
const groupings = computed(() => groupingsData.value ?? [])
const labels = computed(() => labelsData.value ?? [])
const specials = computed(() => specialsData.value ?? [])

// 加载文章数据（编辑模式）
const initialArticle = ref<Partial<ArticleForm>>({})

if (articleId.value) {
  const { data: articleDetail } = await useAsyncData(
    `article-${articleId.value}`,
    () => articleApi.getDetail(articleId.value!)
  )

  watch(() => articleDetail.value, (newData) => {
    if (newData) {
      initialArticle.value = {
        id: newData.id,
        articleTitle: newData.articleTitle || '',
        articleContent: newData.articleContent || '',
        articleCover: newData.articleCover || '',
        articleAbstract: newData.articleAbstract || '',
        groupingId: newData.groupingId,
        labelIds: newData.labelIds || [],
        specialId: newData.specialId
      }
    }
  }, { immediate: true })
}

// 发布成功
const handlePublishSuccess = (data: ArticleForm) => {
  message.success('文章发布成功！')
  setTimeout(() => {
    router.push('/admin/articles')
  }, 500)
}

// 保存草稿成功
const handleSaveDraftSuccess = (data: ArticleForm) => {
  // 草稿保存成功，可以跳转到草稿列表或留在当前页面
  setTimeout(() => {
    router.push('/admin/articles?status=draft')
  }, 500)
}

// 返回
const handleBack = () => {
  // 检查是否有未保存的修改
  if (editorRef.value?.hasChanges()) {
    dialog.warning({
      title: '确认离开',
      content: '您有未保存的内容，确定要离开吗？',
      positiveText: '离开',
      negativeText: '取消',
      onPositiveClick: () => {
        router.back()
      }
    })
  } else {
    router.back()
  }
}

// 路由离开前确认
onBeforeRouteLeave((to, from, next) => {
  if (editorRef.value?.hasChanges()) {
    dialog.warning({
      title: '确认离开',
      content: '您有未保存的内容，确定要离开吗？',
      positiveText: '离开',
      negativeText: '取消',
      onPositiveClick: () => {
        next()
      },
      onNegativeClick: () => {
        next(false)
      }
    })
  } else {
    next()
  }
})
</script>

<style scoped>
.edit-page {
  min-height: 100vh;
  background: var(--color-surface);
}

.edit-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-5) var(--space-6);
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.page-title {
  font-size: var(--text-xl);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.icon {
  width: 18px;
  height: 18px;
  stroke-width: 1.5;
}

.edit-container {
  max-width: 900px;
  margin: 0 auto;
  padding: var(--space-6);
}

/* 响应式 */
@media (max-width: 768px) {
  .edit-container {
    padding: var(--space-4);
  }

  .edit-header {
    padding: var(--space-4) var(--space-4);
  }

  .page-title {
    font-size: var(--text-lg);
  }
}
</style>
