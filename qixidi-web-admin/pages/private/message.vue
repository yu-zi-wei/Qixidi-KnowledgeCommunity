<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getPrivateMessageList"
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
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  PRIVATE_READ_MAP,
  usePrivateAdminApi,
  type PrivateMessageRow
} from '~/composables/usePrivateAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getPrivateMessageList, deletePrivateMessages } = usePrivateAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'uid', label: '发送方 uuid', type: 'input', placeholder: '请输入发送方 uuid' },
  { key: 'replyTargetUid', label: '接收方 uuid', type: 'input', placeholder: '请输入接收方 uuid' },
  { key: 'newsCommentLike', label: '消息关键词', type: 'input', placeholder: '请输入消息内容关键词' }
];

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deletePrivateMessages(ids);
  message.success(`已删除 ${ids.length} 条私信消息`);
  await listRef.value?.refresh();
};

const doDelete = (row: PrivateMessageRow) => {
  dialog.warning({
    title: '删除私信消息',
    content: `删除后「${row.nickname || row.uid}」发送的这条消息立即消失且不可恢复，确认删除？`,
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
    title: '批量删除私信消息',
    content: `删除后所选 ${ids.length} 条消息立即消失且不可恢复，确认删除？`,
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
const columns: DataTableColumns<PrivateMessageRow> = [
  {
    title: '发送方',
    key: 'uid',
    width: 170,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.userAvatar, name: row.nickname || row.uid || '?', size: 30 }),
        h('span', { class: 'private-msg-name' }, row.nickname || row.uid || '—')
      ])
  },
  {
    title: '接收方',
    key: 'replyTargetUid',
    width: 170,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.targetAvatar, name: row.targetName || row.replyTargetUid || '?', size: 30 }),
        h('span', { class: 'private-msg-name' }, row.targetName || row.replyTargetUid || '—')
      ])
  },
  {
    title: '消息内容',
    key: 'newsComment',
    minWidth: 260,
    ellipsis: { tooltip: true },
    render: (row) => row.newsComment || h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '已读',
    key: 'beenRead',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.beenRead, map: PRIVATE_READ_MAP })
  },
  {
    title: '发送时间',
    key: 'createTime',
    width: 150,
    render: (row) => getFullDateTime(row.createTime) || '—'
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
.private-msg-name {
  font-weight: 400;
}
</style>
