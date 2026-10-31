<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="fetch"
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
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  TIME_NOTES_COMMENT_STATUS_MAP,
  useTimeNotesAdminApi,
  type TimeNotesCommentRow
} from '~/composables/useTimeNotesAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getTimeNotesCommentList, deleteTimeNotesComments } = useTimeNotesAdminApi();

// ---------- 搜索（content 后端 like；timeNotesId 输入是字符串，fetch 里转数字） ----------
const searchFields: AdminSearchField[] = [
  { key: 'content', label: '评论内容', type: 'input', placeholder: '请输入关键词' },
  { key: 'timeNotesId', label: '小记 ID', type: 'input', placeholder: '请输入数字 ID', width: 140 },
  {
    key: 'status',
    label: '状态',
    type: 'select',
    width: 140,
    options: [
      { label: '正常', value: 0 },
      { label: '已删除', value: 1 }
    ]
  }
];

const fetch = (params: Record<string, any>) =>
  getTimeNotesCommentList({
    ...params,
    timeNotesId: params.timeNotesId != null && params.timeNotesId !== '' ? Number(params.timeNotesId) : undefined
  });

// ---------- 删除（软删 + 通知清理；批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteTimeNotesComments(ids);
  message.success(`已删除 ${ids.length} 条评论`);
  await listRef.value?.refresh();
};

const doDelete = (row: TimeNotesCommentRow) => {
  dialog.warning({
    title: '删除评论',
    content: '删除后该评论在前台不可见，确认删除这条评论？',
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
    title: '批量删除评论',
    content: `删除后评论在前台不可见，确认删除选中的 ${ids.length} 条评论？`,
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

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<TimeNotesCommentRow> = [
  {
    title: '评论内容',
    key: 'content',
    minWidth: 300,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        h('div', { class: 'cell-comment-content cell-clamp-2' }, row.content),
        row.targetNickname ? h('div', { class: 'cell-comment-sub' }, `回复 @${row.targetNickname}`) : null
      ])
  },
  {
    title: '评论人',
    key: 'nickname',
    width: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.nickname || '—')
      ])
  },
  {
    title: '所在小记',
    key: 'timeNotesTitle',
    width: 160,
    render: (row) =>
      h(
        'a',
        {
          class: 'cell-ellipsis is-link',
          href: `https://qixidi.top/time-notes/${row.timeNotesId}`,
          target: '_blank',
          title: row.timeNotesTitle || `小记 #${row.timeNotesId}`
        },
        row.timeNotesTitle || `#${row.timeNotesId}`
      )
  },
  {
    title: '状态',
    key: 'status',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.status, map: TIME_NOTES_COMMENT_STATUS_MAP })
  },
  {
    title: '发布时间',
    key: 'createTime',
    width: 150,
    render: (row) => getFullDateTime(row.createTime) || '—'
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
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>
