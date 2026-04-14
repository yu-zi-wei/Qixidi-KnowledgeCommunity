<template>
  <div class="collection-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="collection-loading">
      <n-spin size="large" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!collectionList.length" class="collection-empty">
      <n-empty description="暂无收藏夹" />
    </div>

    <!-- 收藏夹列表（可滚动） -->
    <div class="collection-scroll" v-else>
      <div class="collection-list">
        <NuxtLink
          v-for="item in collectionList"
          :key="item.id"
          :to="`/public/collection/${item.id}`"
          class="collection-card"
        >
          <div class="collection-card-icon">
            <n-icon size="28"><Folder /></n-icon>
          </div>
          <div class="collection-card-body">
            <h3 class="collection-card-name">{{ item.collectionName }}</h3>
            <p class="collection-card-intro" v-if="item.collectionIntroduce">{{ item.collectionIntroduce }}</p>
            <div class="collection-card-meta">
              <span class="collection-card-stat">
                <n-icon><FileText /></n-icon>
                {{ item.includedCount || 0 }} 篇
              </span>
              <span v-if="item.createTime" :title="getFullDateTime(item.createTime)">
                <n-icon><Calendar /></n-icon>
                {{ formatTime(item.createTime) }}
              </span>
            </div>
          </div>
        </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Folder, FileText, Calendar } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const collectionApi = useCollectionApi()

const uid = computed(() => route.params.uid as string)

const { data: collectionList, pending } = useAsyncData(
  `user-collection-list-${uid.value}`,
  () => collectionApi.getCollectionFolders(uid.value)
)
</script>

<style>
.collection-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.collection-loading,
.collection-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.collection-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.collection-scroll::-webkit-scrollbar {
  width: 4px;
}

.collection-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.collection-scroll::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.collection-scroll::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.collection-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 0 0 12px;
}

.collection-card {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
}

.collection-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.collection-card-icon {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary-light);
  color: var(--color-primary);
  flex-shrink: 0;
}

.collection-card-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.collection-card-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.collection-card-intro {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.collection-card-meta {
  display: flex;
  gap: 12px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin-top: auto;
}

.collection-card-stat {
  display: flex;
  align-items: center;
  gap: 3px;
}

.collection-card-meta span {
  display: flex;
  align-items: center;
  gap: 3px;
}

@media (max-width: 640px) {
  .collection-list {
    grid-template-columns: 1fr;
  }
}
</style>
