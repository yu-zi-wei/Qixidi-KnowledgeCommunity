<template>
  <div class="users-page">
    <!-- 页头：总用户数 + 排序一行（一张纸排版，无卡片底） -->
    <header class="users-header">
      <nav class="users-sort-bar">
        <span v-if="total > 0" class="users-total">总用户<span class="users-total-num">「{{ total }}」</span></span>
        <button
          v-for="opt in sortOptions"
          :key="opt.key"
          class="bm-tab"
          :class="{ active: sortKey === opt.key }"
          @click="sortKey = opt.key"
        >
          {{ opt.label }}
        </button>
      </nav>
    </header>

    <div v-if="pending" class="users-loading">
      <n-spin size="large" />
    </div>

    <div v-else-if="!sortedUsers.length" class="users-empty">
      <CommonEmptyState description="暂无用户" />
    </div>

    <!-- 用户列表：行式排版，hairline 分隔，hover 墨染 -->
    <div v-else class="users-list">
      <NuxtLink
        v-for="(user, index) in sortedUsers"
        :key="user.uuid"
        :to="`/user-home/article/${user.uuid}`"
        target="_blank"
        class="users-row"
      >
        <span class="users-rank">{{ index + 1 }}</span>

        <img
          :src="user.avatar || '/images/default-avatar.svg'"
          :alt="user.nickname"
          class="users-avatar"
          loading="lazy"
        />

        <div class="users-info">
          <div class="users-name-line">
            <span class="users-name">{{ user.nickname || user.username }}</span>
            <UserRoleBadge :role-id="user.roleId" />
            <span v-if="user.occupation" class="users-occupation">{{ user.occupation }}</span>
          </div>
          <p v-if="user.introduce" class="users-bio">{{ user.introduce }}</p>
          <div class="users-meta">
            <span v-if="user.createTime" class="users-meta-item">
              <n-icon size="11"><Calendar /></n-icon>{{ formatJoinTime(user.createTime) }} 加入
            </span>
            <span v-if="user.company" class="users-meta-item is-company">
              <n-icon size="11"><Building /></n-icon>{{ user.company }}
            </span>
            <span v-if="user.source" class="users-meta-item is-source">
              <n-icon size="11"><World /></n-icon>来自 {{ user.source }}
            </span>
            <span
              v-if="user.homepage"
              class="users-homepage"
              :title="user.homepage"
              @click.stop="openExternal(user.homepage)"
            >
              <n-icon size="11"><Link /></n-icon>{{ urlHost(user.homepage) }}
            </span>
          </div>
        </div>

        <div class="users-stats">
          <div class="users-stat">
            <span class="users-stat-num">{{ user.articleCount || 0 }}</span>
            <span class="users-stat-label">文章</span>
          </div>
          <div class="users-stat">
            <span class="users-stat-num">{{ user.timeNotesCount || 0 }}</span>
            <span class="users-stat-label">小记</span>
          </div>
          <div class="users-stat">
            <span class="users-stat-num">{{ user.dictumCount || 0 }}</span>
            <span class="users-stat-label">随笔</span>
          </div>
          <div class="users-stat">
            <span class="users-stat-num">{{ user.fansFollowCount || 0 }}</span>
            <span class="users-stat-label">粉丝</span>
          </div>
          <div class="users-stat">
            <span class="users-stat-num">{{ user.fansFabulousCount || 0 }}</span>
            <span class="users-stat-label">获赞</span>
          </div>
        </div>
      </NuxtLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Link, Calendar, Building, World } from '@vicons/tabler'

useHead({
  title: '用户',
  bodyAttrs: {
    class: 'page-users'
  }
})

const siteApi = useSiteApi()

// SSR 失败返回 null（勿 []，勿 default），水合后客户端自动重取
const { data: rawData, pending } = await useAsyncData(
  'site-users',
  async () => {
    try {
      return await siteApi.getUserAllList()
    } catch {
      return null
    }
  }
)

const users = computed(() => rawData.value?.rows || [])
const total = computed(() => rawData.value?.total || 0)

// 排序：数据全量在内存（个人博客量级），前端排序即可，不动后端
type SortKey = 'default' | 'newest' | 'articles' | 'likes' | 'fans'
const sortOptions: { key: SortKey; label: string }[] = [
  { key: 'articles', label: '文章最多' },
  { key: 'likes', label: '获赞最多' },
  { key: 'fans', label: '粉丝最多' },
  { key: 'newest', label: '最新加入' },
  { key: 'default', label: '最早加入' }
]
const sortKey = ref<SortKey>('articles')

const count = (v: unknown) => Number(v) || 0
// createTime 后端返回字符串日期（"2023-11-27 22:54:13"），直接相减是 NaN，须转时间戳
const tsOf = (v: unknown): number => {
  if (typeof v === 'number') return v
  if (typeof v === 'string') {
    const t = Date.parse(v.replace(' ', 'T'))
    return Number.isNaN(t) ? 0 : t
  }
  return 0
}

const sortedUsers = computed(() => {
  const list = [...users.value]
  switch (sortKey.value) {
    case 'newest':
      return list.sort((a, b) => tsOf(b.createTime) - tsOf(a.createTime))
    case 'articles':
      return list.sort((a, b) => count(b.articleCount) - count(a.articleCount))
    case 'likes':
      return list.sort((a, b) => count(b.fansFabulousCount) - count(a.fansFabulousCount))
    case 'fans':
      return list.sort((a, b) => count(b.fansFollowCount) - count(a.fansFollowCount))
    default:
      return list
  }
})

const formatJoinTime = (timestamp: number) => {
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 个人主页外链（行本身是 NuxtLink，内层不能嵌 a，span 模拟链接）
const normalizeUrl = (url: string) => /^https?:\/\//i.test(url) ? url : `https://${url}`
const urlHost = (url: string) => {
  try {
    return new URL(normalizeUrl(url)).hostname
  } catch {
    return url.length > 20 ? `${url.slice(0, 20)}…` : url
  }
}
const openExternal = (url: string) => {
  window.open(normalizeUrl(url), '_blank', 'noopener')
}
</script>

<style>
.users-page {
  display: flex;
  flex-direction: column;
  padding-top: 24px;
}

/* ===== 页头：总用户数 + 排序一行（一张纸排版） ===== */
.users-header {
  padding-bottom: 16px;
  border-bottom: 1px solid var(--color-border-light);
}

.users-total {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  margin-right: 10px;
  white-space: nowrap;
}

.users-total-num {
  font-family: var(--font-display);
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  font-variant-numeric: tabular-nums;
  margin-left: 2px;
}

.users-sort-bar {
  display: flex;
  align-items: center;
  gap: 2px;
  flex-wrap: wrap;
}

.users-loading,
.users-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

/* ===== 用户行列表：透明容器 + hairline 分隔 + hover 墨染 ===== */
.users-list {
  display: flex;
  flex-direction: column;
}

.users-row {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 20px 10px;
  text-decoration: none;
  border-bottom: 1px solid var(--color-border-light);
  transition: background-color var(--transition-base);
}

.users-row:last-child {
  border-bottom: none;
}

.users-row:hover {
  background: color-mix(in srgb, var(--color-ink) 3%, transparent);
}

/* 衬线序号：排行锚点（纸上排版线语言） */
.users-rank {
  font-family: var(--font-display);
  font-size: var(--text-xl);
  font-weight: 600;
  line-height: 1;
  color: var(--color-ink-faint);
  min-width: 30px;
  text-align: center;
  flex-shrink: 0;
  font-variant-numeric: tabular-nums;
  transition: color var(--transition-fast);
}

.users-row:hover .users-rank {
  color: var(--color-ink-muted);
}

.users-avatar {
  width: 52px;
  height: 52px;
  border-radius: var(--radius-full);
  object-fit: cover;
  background: var(--color-surface-dim);
  flex-shrink: 0;
}

.users-info {
  flex: 1;
  min-width: 0;
}

.users-name-line {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.users-name {
  display: block;
  min-width: 0;
  flex-shrink: 1;
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--transition-fast);
}

.users-row:hover .users-name {
  color: var(--color-primary);
}

.users-occupation {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.users-bio {
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  margin: 4px 0 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* meta 行：每段图标 + 语义墨色，四段四色一眼可辨（灰/赭/青/绿） */
.users-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-top: 4px;
}

.users-meta-item {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  white-space: nowrap;
}

.users-meta-item.is-company {
  color: var(--tag-amber);
}

.users-meta-item.is-source {
  color: var(--tag-teal);
}

/* 主页外链：墨绿点状链接 */
.users-homepage {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  max-width: 160px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: var(--text-xs);
  color: var(--tag-green);
  cursor: pointer;
  transition: filter var(--transition-fast);
}

.users-homepage:hover {
  filter: brightness(0.85);
  text-decoration: underline;
}

/* ===== 统计区：凹槽纸片底 + 衬线数字锚点 ===== */
.users-stats {
  display: flex;
  align-items: center;
  flex-shrink: 0;
  margin-left: 12px;
  padding: 8px 2px;
  background: var(--color-sunken);
  border-radius: var(--radius-md);
}

.users-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1px;
  min-width: 46px;
  padding: 0 14px;
}

.users-stat-num {
  font-family: var(--font-display);
  font-size: var(--text-lg);
  font-weight: 600;
  line-height: 1.2;
  color: var(--color-ink);
  font-variant-numeric: tabular-nums;
}

.users-stat-label {
  font-size: var(--text-2xs);
  color: var(--color-ink-muted);
}

/* ===== 响应式 ===== */
@media (max-width: 1024px) {
  /* 窄桌面：统计收窄间距 */
  .users-stats {
    gap: 16px;
  }
}

@media (max-width: 768px) {
  body.page-users .home-main {
    padding-top: 70px !important;
  }

  .users-row {
    flex-wrap: wrap;
    gap: 10px;
    padding: 16px 0;
  }

  .users-rank {
    min-width: 22px;
    font-size: var(--text-lg);
  }

  .users-avatar {
    width: 44px;
    height: 44px;
  }

  .users-stats {
    order: 3;
    width: 100%;
    margin-left: 0;
    padding: 7px 2px;
    justify-content: space-between;
  }

  .users-stat {
    padding: 0;
    min-width: 0;
    flex: 1;
  }
}
</style>
