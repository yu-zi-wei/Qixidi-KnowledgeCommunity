<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getPrivateSessionList"
    row-key="id"
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
    </template>
  </AdminListPage>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminAvatar from '~/components/AdminAvatar.vue';
import { usePrivateAdminApi, type PrivateSessionRow } from '~/composables/usePrivateAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getPrivateSessionList, deletePrivateSessions } = usePrivateAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'uid', label: '发起方 uuid', type: 'input', placeholder: '请输入发起方 uuid' },
  { key: 'targetUid', label: '接收方 uuid', type: 'input', placeholder: '请输入接收方 uuid' },
  { key: 'lastNewsLike', label: '消息关键词', type: 'input', placeholder: '请输入消息内容关键词' }
];

// ---------- 删除（删除会话仅清会话记录，消息明细在「私信消息」页管理） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deletePrivateSessions(ids);
  message.success(`已删除 ${ids.length} 个私信会话`);
  await listRef.value?.refresh();
};

const doDelete = (row: PrivateSessionRow) => {
  dialog.warning({
    title: '删除私信会话',
    content: `删除后「${row.uidName || row.uid}」与「${row.targetName || row.targetUid}」的会话记录立即消失，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.id;
      try {
        await runDelete([row.id]);
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
    title: '批量删除私信会话',
    content: `删除后所选 ${ids.length} 个会话记录立即消失，确认删除？`,
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

// ---------- 表格列 ----------
const columns: DataTableColumns<PrivateSessionRow> = [
  {
    title: '发起方',
    key: 'uid',
    width: 180,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.uidAvatar, name: row.uidName || row.uid || '?', size: 30 }),
        h('span', { class: 'private-name' }, row.uidName || row.uid || '—')
      ])
  },
  {
    title: '接收方',
    key: 'targetUid',
    width: 180,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.targetAvatar, name: row.targetName || row.targetUid || '?', size: 30 }),
        h('span', { class: 'private-name' }, row.targetName || row.targetUid || '—')
      ])
  },
  {
    title: '最近一条消息',
    key: 'lastNews',
    minWidth: 220,
    ellipsis: { tooltip: true },
    render: (row) => row.lastNews || h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '最近活跃',
    key: 'updateTime',
    width: 150,
    render: (row) => getFullDateTime(row.updateTime) || getFullDateTime(row.createTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 90,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h(
        NButton,
        { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
        { default: () => '删除' }
      )
  }
];
</script>

<style lang="scss">
/* cell-author 内的昵称样式（列 render 元素 scoped 选不中，全局类 + 页面前缀类） */
.private-name {
  font-weight: 400;
}
</style>
