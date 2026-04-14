<template>
  <div class="follow-page">
    <!-- 左侧菜单 -->
    <div class="follow-sidebar">
      <button
        v-for="tab in tabs"
        :key="tab.value"
        class="follow-menu-item"
        :class="{ active: activeTab === tab.value }"
        @click="activeTab = tab.value"
      >
        <n-icon size="16"><component :is="tab.icon" /></n-icon>
        {{ tab.label }}
        <span class="follow-menu-count" v-if="tab.value === 1 && userList">({{ userList.length }})</span>
        <span class="follow-menu-count" v-if="tab.value === 2 && tagList">({{ tagList.length }})</span>
      </button>
    </div>

    <!-- 右侧内容 -->
    <div class="follow-content">
      <!-- 加载状态 -->
      <div v-if="pending" class="follow-loading">
        <n-spin size="large" />
      </div>

      <!-- 关注用户列表 -->
      <template v-else-if="activeTab === 1">
        <div v-if="!userList?.length" class="follow-empty">
          <n-empty description="暂无关注的用户" />
        </div>
        <div class="follow-user-list" v-else>
          <div v-for="user in userList" :key="user.uuid" class="follow-user-card">
            <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="follow-user-avatar">
              <img v-if="(user as FollowUserVo).avatar" :src="(user as FollowUserVo).avatar" :alt="(user as FollowUserVo).nickname" />
              <div v-else class="follow-user-avatar-placeholder">
                <n-icon size="20"><User /></n-icon>
              </div>
            </NuxtLink>
            <div class="follow-user-info">
              <div class="follow-user-name-row">
                <NuxtLink :to="`/user-home/article/${user.uuid}`" target="_blank" class="follow-user-name">
                  {{ (user as FollowUserVo).nickname || '用户' }}
                </NuxtLink>
                <span v-if="(user as FollowUserVo).occupation" class="follow-user-occupation">
                  {{ (user as FollowUserVo).occupation }}
                </span>
              </div>
              <p class="follow-user-intro" v-if="(user as FollowUserVo).remark">{{ (user as FollowUserVo).remark }}</p>
              <div class="follow-user-meta">
                <span v-if="(user as FollowUserVo).location" class="follow-user-tag">
                  <n-icon size="12"><MapPin /></n-icon>
                  {{ (user as FollowUserVo).location }}
                </span>
                <span v-if="(user as FollowUserVo).createTime" class="follow-user-time" :title="getFullDateTime((user as FollowUserVo).createTime!)">
                  <n-icon size="12"><Calendar /></n-icon>
                  {{ formatTime((user as FollowUserVo).createTime!) }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </template>

      <!-- 关注标签列表 -->
      <template v-else-if="activeTab === 2">
        <div v-if="!tagList?.length" class="follow-empty">
          <n-empty description="暂无关注的标签" />
        </div>
        <div class="follow-tag-list" v-else>
          <NuxtLink
            v-for="tag in (tagList as FollowTagVo[])"
            :key="tag.id"
            :to="`/public/label/${tag.id}`"
            class="follow-tag-card"
          >
            <div class="follow-tag-cover" v-if="tag.labelCover" v-html="tag.labelCover" />
            <div class="follow-tag-cover follow-tag-placeholder" v-else>
              <n-icon size="20"><Tag /></n-icon>
            </div>
            <div class="follow-tag-body">
              <h3 class="follow-tag-name">{{ tag.labelName }}</h3>
              <p class="follow-tag-desc" v-if="tag.labelDescribe">{{ tag.labelDescribe }}</p>
              <div class="follow-tag-meta">
                <span>
                  <n-icon><FileText /></n-icon>
                  {{ tag.articleNumber || 0 }} 篇
                </span>
                <span v-if="tag.createTime" :title="getFullDateTime(tag.createTime)">
                  <n-icon><Calendar /></n-icon>
                  {{ formatTime(tag.createTime) }}
                </span>
              </div>
            </div>
          </NuxtLink>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { User, Briefcase, MapPin, Tag, FileText, Calendar } from '@vicons/tabler'
import { formatTime, getFullDateTime } from '~/utils/formatTime'

interface FollowUserVo {
  uuid: string
  nickname?: string
  avatar?: string
  location?: string
  remark?: string
  occupation?: string
  createTime?: string
}

interface FollowTagVo {
  id: number
  labelName: string
  labelDescribe?: string
  labelCover?: string
  followNumber?: number
  articleNumber?: number
  createTime?: string
}

definePageMeta({ layout: 'user-home' })

const route = useRoute()
const followApi = useFollowApi()

const uid = computed(() => route.params.uid as string)
const activeTab = ref(1)

const tabs = [
  { value: 1, label: '关注用户', icon: User },
  { value: 2, label: '关注标签', icon: Tag }
]

const { data: userList, pending: pendingUsers } = useAsyncData(
  `follow-users-${uid.value}`,
  () => followApi.getFollowList(uid.value, 1) as Promise<FollowUserVo[]>
)

const { data: tagList, pending: pendingTags } = useAsyncData(
  `follow-tags-${uid.value}`,
  () => followApi.getFollowList(uid.value, 2) as Promise<FollowTagVo[]>
)

const pending = computed(() => pendingUsers.value || pendingTags.value)
</script>

<style>
.follow-page {
  display: flex;
  flex: 1;
  min-height: 0;
  gap: 0;
}

/* 左侧菜单 */
.follow-sidebar {
  width: 140px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding-right: 12px;
  border-right: 1px solid var(--color-border-light);
}

.follow-menu-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  width: 100%;
}

.follow-menu-item:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.follow-menu-item.active {
  color: var(--color-primary);
  font-weight: 600;
  background: var(--color-primary-light);
}

.follow-menu-count {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.follow-menu-item.active .follow-menu-count {
  color: var(--color-primary);
}

/* 右侧内容 */
.follow-content {
  flex: 1;
  min-width: 0;
  min-height: 0;
  overflow-y: auto;
  padding-left: 16px;
}

.follow-content::-webkit-scrollbar {
  width: 4px;
}

.follow-content::-webkit-scrollbar-track {
  background: transparent;
}

.follow-content::-webkit-scrollbar-thumb {
  background: var(--color-border);
  border-radius: 4px;
}

.follow-content::-webkit-scrollbar-thumb:hover {
  background: var(--color-ink-faint);
}

.follow-loading,
.follow-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* 关注用户列表 */
.follow-user-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-bottom: 12px;
}

.follow-user-card {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  transition: all var(--transition-base);
}

.follow-user-card:hover {
  box-shadow: var(--shadow-sm);
}

.follow-user-avatar {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-full);
  overflow: hidden;
  flex-shrink: 0;
}

.follow-user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.follow-user-avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-primary-light);
  color: var(--color-primary);
}

.follow-user-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.follow-user-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  text-decoration: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.follow-user-name:hover {
  color: var(--color-primary);
}

.follow-user-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.follow-user-occupation {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.follow-user-intro {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.follow-user-meta {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 2px;
}

.follow-user-tag {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 11px;
  padding: 1px 8px;
  border-radius: var(--radius-full);
  color: var(--color-primary);
  background: var(--color-primary-light);
}

.follow-user-time {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 11px;
  color: var(--color-ink-faint);
}

/* 关注标签列表 */
.follow-tag-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding-bottom: 12px;
}

.follow-tag-card {
  display: flex;
  gap: 10px;
  padding: 12px;
  border-radius: var(--radius-md);
  background: var(--color-surface);
  text-decoration: none;
  transition: all var(--transition-base);
}

.follow-tag-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-1px);
}

.follow-tag-cover {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
}

.follow-tag-cover :deep(svg) {
  width: 100%;
  height: 100%;
  display: block;
}

.follow-tag-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-surface-dim);
  color: var(--color-ink-muted);
}

.follow-tag-body {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.follow-tag-name {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.follow-tag-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0;
  line-height: var(--leading-relaxed);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.follow-tag-meta {
  display: flex;
  gap: 12px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin-top: auto;
}

.follow-tag-meta span {
  display: flex;
  align-items: center;
  gap: 3px;
}

@media (max-width: 640px) {
  .follow-page {
    flex-direction: column;
  }

  .follow-sidebar {
    width: 100%;
    flex-direction: row;
    border-right: none;
    border-bottom: 1px solid var(--color-border-light);
    padding-right: 0;
    padding-bottom: 8px;
  }

  .follow-content {
    padding-left: 0;
    padding-top: 12px;
  }

  .follow-tag-list {
    grid-template-columns: 1fr;
  }
}
</style>
