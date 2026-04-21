<template>
  <div class="special-page">
    <!-- 加载状态 -->
    <div v-if="pending" class="special-loading">
      <n-spin size="large" />
    </div>

    <!-- 空状态 -->
    <div v-else-if="!specialList.length" class="special-empty">
      <CommonEmptyState description="暂无专栏" />
    </div>

    <!-- 专栏列表（可滚动） -->
    <div class="special-scroll" v-else>
      <div class="special-list">
      <NuxtLink
        v-for="item in specialList"
        :key="item.id"
        :to="`/public/special/${item.id}`"
        class="special-card"
      >
        <div class="special-card-cover" v-if="item.cover">
          <img :src="item.cover" :alt="item.specialName" />
        </div>
        <div class="special-card-cover special-card-placeholder" v-else>
          <n-icon size="28"><Folder /></n-icon>
        </div>
        <div class="special-card-body">
          <h3 class="special-card-name">{{ item.specialName }}</h3>
          <p class="special-card-intro" v-if="item.specialIntroduce">{{ item.specialIntroduce }}</p>
          <div class="special-card-meta">
            <span class="special-card-stat">
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
const specialDetailApi = useSpecialDetailApi()

const uid = computed(() => route.params.uid as string)

const { data: specialList, pending } = useAsyncData(
  `user-special-list-${uid.value}`,
  () => specialDetailApi.getSpecialList(uid.value)
)
</script>

<style>
.special-page {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-height: 0;
}

.special-loading,
.special-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.special-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.special-scroll::-webkit-scrollbar {
  width: 4px;
}

.special-scroll::-webkit-scrollbar-track {
  background: transparent;
}

.special-scroll::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.special-scroll::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.special-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 0 0 12px;
}

.special-card {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
}

.special-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.special-card-cover {
  width: 72px;
  height: 52px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.special-card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.special-card-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.special-card-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.special-card-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.special-card-intro {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.special-card-meta {
  display: flex;
  gap: 12px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin-top: auto;
}

.special-card-stat {
  display: flex;
  align-items: center;
  gap: 3px;
}

.special-card-meta span {
  display: flex;
  align-items: center;
  gap: 3px;
}

@media (max-width: 640px) {
  .special-list {
    grid-template-columns: 1fr;
  }
}
</style>
