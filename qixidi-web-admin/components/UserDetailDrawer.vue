<template>
  <!-- 用户详情抽屉（共享）：各列表页用户/作者列点击打开，统一调详情接口。
       打开先清空防残留上一篇；失败留空态不回退列表数据（teleport 宽度必须 inline style）。 -->
  <n-drawer v-model:show="visible" :width="480" placement="right">
    <n-drawer-content :title="detail?.nickname || detail?.username || '用户详情'" closable>
      <n-spin :show="loading">
        <div v-if="detail" class="detail-body">
          <div class="detail-head">
            <AdminAvatar :src="detail.avatar" :name="detail.nickname || detail.username" :size="48" />
            <div class="detail-head-main">
              <div class="detail-name">{{ detail.nickname || '—' }}</div>
              <div class="detail-username">@{{ detail.username || '—' }}</div>
            </div>
            <AdminStateTag :value="detail.state" :map="USER_STATE_MAP" />
          </div>

          <div class="detail-meta">
            <div class="meta-row"><span class="meta-label">邮箱</span><span>{{ detail.email || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">手机</span><span>{{ detail.phone || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">来源</span><span>{{ detail.source || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">登录 IP</span><span>{{ detail.loginIp || '—' }}</span></div>
            <div class="meta-row"><span class="meta-label">注册时间</span><span>{{ getFullDateTime(detail.createTime) || '—' }}</span></div>
          </div>

          <n-divider>社区数据</n-divider>
          <div class="detail-stats">
            <div class="stat-item"><span class="stat-num">{{ detail.fabulousCount ?? 0 }}</span><span class="stat-label">获赞</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.collectionCount ?? 0 }}</span><span class="stat-label">收藏</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.followCount ?? 0 }}</span><span class="stat-label">关注</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.fansFollowCount ?? 0 }}</span><span class="stat-label">粉丝</span></div>
            <div class="stat-item"><span class="stat-num">{{ detail.commentCount ?? 0 }}</span><span class="stat-label">评论</span></div>
          </div>

          <template v-if="detail.introduce">
            <n-divider>个人介绍</n-divider>
            <p class="detail-introduce">{{ detail.introduce }}</p>
          </template>
        </div>
      </n-spin>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { USER_STATE_MAP, useUserAdminApi, type UserRow } from '~/composables/useUserAdminApi';
import { getFullDateTime } from '~/utils/format';

const { getUser } = useUserAdminApi();

const visible = ref(false);
const loading = ref(false);
const detail = ref<UserRow | null>(null);

/** 打开用户详情（uuid 空/缺省时不动作，列表 render 判空后才会挂点击） */
const open = async (uuid?: string) => {
  if (!uuid) return;
  visible.value = true;
  loading.value = true;
  detail.value = null; // 先清空，避免残留上一个用户内容
  try {
    detail.value = await getUser(uuid);
  } catch {
    detail.value = null; // useApi 已弹错，失败留空态，不回退列表数据
  } finally {
    loading.value = false;
  }
};

defineExpose({ open });
</script>

<style scoped lang="scss">
.detail-body {
  .detail-head {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .detail-head-main {
    flex: 1;
    min-width: 0;
  }

  .detail-name {
    font-weight: 600;
  }

  .detail-username {
    font-size: 12px;
    opacity: 0.5;
  }

  .detail-meta {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 14px;
  }

  .meta-row {
    display: flex;
    align-items: baseline;
    gap: 12px;
    font-size: 13px;
  }

  .meta-label {
    width: 60px;
    flex-shrink: 0;
    opacity: 0.5;
  }

  .detail-stats {
    display: flex;
    gap: 12px;
    flex-wrap: wrap;
  }

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 2px;
    min-width: 56px;
    padding: 10px 8px;
    // 抽屉 teleport 到 body，布局根主题变量不可用；中性半透明灰双主题通用
    background: rgba(127, 127, 127, 0.08);
    border-radius: 8px;
  }

  .stat-num {
    font-size: 16px;
    font-weight: 600;
    font-variant-numeric: tabular-nums;
  }

  .stat-label {
    font-size: 12px;
    opacity: 0.55;
  }

  .detail-introduce {
    margin: 0;
    font-size: 13.5px;
    line-height: 1.7;
    white-space: pre-wrap;
    word-break: break-word;
    opacity: 0.85;
  }
}
</style>
