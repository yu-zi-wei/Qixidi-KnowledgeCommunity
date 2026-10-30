<template>
  <div class="user-page">
    <AdminPageHeader
      title="前台用户"
      description="查看社区注册用户，管理账号可用状态"
      :stats="[
        { label: '冻结中', value: frozenCount, accent: (frozenCount ?? 0) > 0 },
        { label: '总数', value: total }
      ]"
    >
      <template #actions>
        <n-button quaternary circle :loading="loading" @click="reload">
          <template #icon><n-icon><Refresh /></n-icon></template>
        </n-button>
      </template>
    </AdminPageHeader>

    <n-card :bordered="false" class="table-card">
      <div class="toolbar">
        <n-input
          v-model:value="keyword"
          class="search-input"
          round
          clearable
          placeholder="搜索昵称 / 用户名"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix><n-icon :component="Search" /></template>
        </n-input>

        <n-radio-group v-model:value="stateFilter" size="small" @update:value="handleSearch">
          <n-radio-button v-for="o in stateOptions" :key="o.value ?? 'all'" :value="o.value ?? null" :label="o.label" />
        </n-radio-group>
      </div>

      <n-spin :show="loading">
        <n-empty v-if="!rows.length && !loading" description="暂无用户" class="list-empty" />
        <div v-else class="admin-row-list">
          <AdminRowCard v-for="row in rows" :key="row.uuid">
            <template #media>
              <AdminAvatar :src="row.avatar" :name="row.nickname || row.username" :size="[40, 40]" />
            </template>

            <div class="usr-title-line">
              <span class="usr-nickname">{{ row.nickname || '—' }}</span>
              <span class="usr-username">@{{ row.username || '—' }}</span>
            </div>
            <div class="row-meta">
              <span v-if="row.email">{{ row.email }}</span>
              <template v-if="row.loginIp">
                <span class="row-meta-dot">·</span>
                <span>IP {{ row.loginIp }}</span>
              </template>
              <template v-if="row.source">
                <span class="row-meta-dot">·</span>
                <span>{{ row.source }}</span>
              </template>
              <span class="row-meta-dot">·</span>
              <span>注册于 {{ getFullDateTime(row.createTime) }}</span>
            </div>

            <template #side>
              <AdminStateTag :value="row.state" :map="USER_STATE_MAP" />
              <div v-if="row.state !== 2" class="cell-actions">
                <n-button
                  size="tiny"
                  quaternary
                  :type="row.state !== 1 ? 'error' : 'success'"
                  :loading="switchingUuid === row.uuid"
                  @click="toggleState(row)"
                >
                  <template #icon><n-icon><component :is="row.state !== 1 ? UserOff : UserCheck" /></n-icon></template>
                  {{ row.state !== 1 ? '冻结' : '解冻' }}
                </n-button>
              </div>
            </template>
          </AdminRowCard>
        </div>
      </n-spin>

      <div v-if="total" class="list-footer">
        <span class="list-total">共 {{ total }} 人</span>
        <n-pagination
          :page="pageNum"
          :page-size="pageSize"
          :item-count="total"
          :page-sizes="[10, 20, 50]"
          show-size-picker
          @update:page="onPageChange"
          @update:page-size="onPageSizeChange"
        />
      </div>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { Refresh, Search, UserCheck, UserOff } from '@vicons/tabler';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminPageHeader from '~/components/AdminPageHeader.vue';
import AdminRowCard from '~/components/AdminRowCard.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { USER_STATE_MAP, useUserAdminApi, type UserRow } from '~/composables/useUserAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getUsers, updateUser } = useUserAdminApi();

const rows = ref<UserRow[]>([]);
const loading = ref(false);
const total = ref(0);
const frozenCount = ref<number | null>(null);

const keyword = ref('');
const stateFilter = ref<number | null>(null);
const stateOptions: Array<{ label: string; value: number | null }> = [
  { label: '全部', value: null },
  { label: '正常', value: 0 },
  { label: '已冻结', value: 1 }
];

const pageNum = ref(1);
const pageSize = ref(20);

const loadList = async () => {
  loading.value = true;
  try {
    const params: Record<string, any> = { pageNum: pageNum.value, pageSize: pageSize.value };
    if (keyword.value.trim()) params.nickname = keyword.value.trim();
    if (stateFilter.value !== null) params.state = stateFilter.value;
    const res = await getUsers(params);
    rows.value = res.rows || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
};

/** 冻结用户数（轻查询：state=1 取 total；依赖后端 buildQueryWrapper 的 state 条件） */
const loadFrozenCount = async () => {
  try {
    const res = await getUsers({ pageNum: 1, pageSize: 1, state: 1 });
    frozenCount.value = res.total || 0;
  } catch {
    frozenCount.value = null;
  }
};

const handleSearch = () => {
  pageNum.value = 1;
  loadList();
};

const reload = () => {
  loadList();
  loadFrozenCount();
};

const onPageChange = (page: number) => {
  pageNum.value = page;
  loadList();
};

const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  pageNum.value = 1;
  loadList();
};

/** 冻结/解冻：TripartiteUserBo 无 EditGroup 约束，只传 {uuid, state} */
const switchingUuid = ref<string | null>(null);
const toggleState = (row: UserRow) => {
  const freezing = row.state !== 1;
  dialog.warning({
    title: freezing ? '冻结用户' : '解冻用户',
    content: freezing
      ? `冻结后「${row.nickname || row.username}」将无法登录社区，确认冻结？`
      : `解冻后「${row.nickname || row.username}」可正常登录，确认解冻？`,
    positiveText: freezing ? '冻结' : '解冻',
    negativeText: '取消',
    onPositiveClick: async () => {
      switchingUuid.value = row.uuid;
      try {
        await updateUser({ uuid: row.uuid, state: freezing ? 1 : 0 });
        message.success(freezing ? '已冻结' : '已解冻');
        reload();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        switchingUuid.value = null;
      }
    }
  });
};

onMounted(() => {
  reload();
});
</script>

<style scoped lang="scss">
.table-card {
  border-radius: 10px;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 14px;
}

.search-input {
  width: 260px;
}

.list-empty {
  padding: 48px 0;
}

.usr-title-line {
  display: flex;
  align-items: baseline;
  gap: 8px;
  min-width: 0;
}

.usr-nickname {
  font-size: 14px;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.usr-username {
  font-size: 12px;
  opacity: 0.45;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
