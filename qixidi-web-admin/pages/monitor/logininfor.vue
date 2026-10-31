<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getLogininfors"
    row-key="infoId"
    seq
    selectable
  >
    <template #toolbar-left>
      <n-button
        size="small"
        type="error"
        secondary
        :disabled="!checkedKeys.length"
        :loading="batchDeleting"
        @click="doDeleteBatch"
      >
        删除所选（{{ checkedKeys.length }}）
      </n-button>
      <n-button size="small" type="error" ghost :loading="cleaning" @click="doClean">清空日志</n-button>
    </template>
  </AdminListPage>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { LOGIN_STATUS_MAP, useMonitorAdminApi, type LogininforRow } from '~/composables/useMonitorAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getLogininfors, deleteLogininfors, cleanLogininfors } = useMonitorAdminApi();

// ---------- 搜索（userName/ipaddr 后端 like；status eq 且是字符串） ----------
const searchFields: AdminSearchField[] = [
  { key: 'userName', label: '登录账号', type: 'input', placeholder: '请输入账号关键词' },
  { key: 'ipaddr', label: '登录 IP', type: 'input', placeholder: '请输入 IP 关键词' },
  { key: 'status', label: '登录状态', type: 'select', options: [
    { label: '成功', value: '0' },
    { label: '失败', value: '1' }
  ] }
];

// ---------- 删除/清空 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);
const cleaning = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteLogininfors(ids);
  message.success(`已删除 ${ids.length} 条记录`);
  await listRef.value?.refresh();
};

const doDelete = (row: LogininforRow) => {
  dialog.warning({
    title: '删除登录记录',
    content: '删除后不可恢复，确认删除这条登录记录？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.infoId;
      try {
        await runDelete([row.infoId]);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        deletingId.value = null;
      }
    }
  });
};

const doDeleteBatch = () => {
  const ids = [...checkedKeys.value];
  dialog.warning({
    title: '批量删除登录记录',
    content: `删除后不可恢复，确认删除选中的 ${ids.length} 条登录记录？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      batchDeleting.value = true;
      try {
        await runDelete(ids);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        batchDeleting.value = false;
      }
    }
  });
};

const doClean = () => {
  dialog.warning({
    title: '清空登录日志',
    content: '将删除全部登录记录且不可恢复，确认清空？',
    positiveText: '清空',
    negativeText: '取消',
    onPositiveClick: async () => {
      cleaning.value = true;
      try {
        await cleanLogininfors();
        message.success('已清空登录日志');
        await listRef.value?.reload();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        cleaning.value = false;
      }
    }
  });
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<LogininforRow> = [
  {
    title: '登录账号',
    key: 'userName',
    minWidth: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.userName || '—'
  },
  {
    title: '登录 IP',
    key: 'ipaddr',
    width: 130,
    render: (row) => row.ipaddr || '—'
  },
  {
    title: '登录地点',
    key: 'loginLocation',
    minWidth: 150,
    ellipsis: { tooltip: true },
    render: (row) => row.loginLocation || '—'
  },
  {
    title: '浏览器',
    key: 'browser',
    minWidth: 110,
    ellipsis: { tooltip: true },
    render: (row) => row.browser || '—'
  },
  {
    title: '操作系统',
    key: 'os',
    minWidth: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.os || '—'
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.status ?? '', map: LOGIN_STATUS_MAP })
  },
  {
    title: '提示消息',
    key: 'msg',
    minWidth: 160,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.msg || '' }, row.msg || '—')
  },
  {
    title: '登录时间',
    key: 'loginTime',
    width: 150,
    render: (row) => getFullDateTime(row.loginTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 80,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.infoId, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>
