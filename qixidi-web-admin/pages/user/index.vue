<template>
  <div class="user-page">
    <AdminPageHeader
      title="前台用户"
      description="查看社区注册用户，管理账号可用状态"
      :stats="[
        { label: '冻结中', value: frozenCount, accent: frozenCount > 0 },
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

      <n-data-table
        remote
        :columns="columns"
        :data="rows"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: UserRow) => row.uuid"
        :scroll-x="1020"
        size="small"
        striped
      />
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton, NIcon, useDialog, useMessage } from 'naive-ui';
import { Refresh, Search, UserCheck, UserOff } from '@vicons/tabler';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminStateTag, { type StateMeta } from '~/components/AdminStateTag.vue';
import AdminTimeText from '~/components/AdminTimeText.vue';
import { USER_STATE_MAP, useUserAdminApi, type UserRow } from '~/composables/useUserAdminApi';

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
const pageSize = ref(10);
const pagination = computed(() => ({
  page: pageNum.value,
  pageSize: pageSize.value,
  itemCount: total.value,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  onChange: (page: number) => {
    pageNum.value = page;
    loadList();
  },
  onUpdatePageSize: (size: number) => {
    pageSize.value = size;
    pageNum.value = 1;
    loadList();
  }
}));

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

/** 冻结用户数（轻查询：state=1 取 total） */
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

const columns: DataTableColumns<UserRow> = [
  {
    title: '用户',
    key: 'nickname',
    minWidth: 180,
    render: (row) =>
      h('div', { class: 'user-cell' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname || row.username, size: 30 }),
        h('div', { class: 'user-names' }, [
          h('span', { class: 'user-nickname' }, row.nickname || '—'),
          h('span', { class: 'user-username' }, row.username || '')
        ])
      ])
  },
  { title: '来源', key: 'source', width: 90, render: (row) => row.source || h('span', { class: 'cell-dim' }, '—') },
  {
    title: '邮箱',
    key: 'email',
    width: 180,
    ellipsis: { tooltip: true },
    render: (row) => row.email || h('span', { class: 'cell-dim' }, '—')
  },
  { title: '最近登录 IP', key: 'loginIp', width: 130, render: (row) => row.loginIp || h('span', { class: 'cell-dim' }, '—') },
  {
    title: '注册时间',
    key: 'createTime',
    width: 110,
    render: (row) => h(AdminTimeText, { time: row.createTime })
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: USER_STATE_MAP as Record<string | number, StateMeta> })
  },
  {
    title: '操作',
    key: 'actions',
    width: 110,
    fixed: 'right',
    render: (row) => {
      if (row.state === 2) return h('span', { class: 'cell-dim' }, '—');
      const freezing = row.state !== 1;
      return h(
        NButton,
        {
          size: 'tiny',
          quaternary: true,
          type: freezing ? 'error' : 'success',
          loading: switchingUuid.value === row.uuid,
          onClick: () => toggleState(row)
        },
        {
          icon: () => h(NIcon, null, { default: () => h(freezing ? UserOff : UserCheck) }),
          default: () => (freezing ? '冻结' : '解冻')
        }
      );
    }
  }
];

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
</style>

<!-- 列 render 函数里的元素带不上 scoped hash，列内专属样式放非 scoped 块 -->
<style lang="scss">
.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.user-names {
  display: flex;
  flex-direction: column;
  min-width: 0;
  line-height: 1.35;

  .user-nickname {
    font-weight: 500;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .user-username {
    font-size: 11.5px;
    opacity: 0.45;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
