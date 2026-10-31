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
import { useTimeNotesAdminApi, type TimeNotesRow } from '~/composables/useTimeNotesAdminApi';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getTimeNotesList, deleteTimeNotes } = useTimeNotesAdminApi();

// ---------- 搜索（title 后端 like） ----------
const searchFields: AdminSearchField[] = [
  { key: 'title', label: '标题', type: 'input', placeholder: '请输入关键词' }
];

const fetch = (params: Record<string, any>) => getTimeNotesList(params);

// ---------- 删除（逻辑删 + 相册图片清理；批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteTimeNotes(ids);
  message.success(`已删除 ${ids.length} 条小记`);
  await listRef.value?.refresh();
};

const doDelete = (row: TimeNotesRow) => {
  dialog.warning({
    title: '删除小记',
    content: '删除后小记及其相册图片将一并清理，确认删除这条小记？',
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
    title: '批量删除小记',
    content: `删除后小记及其相册图片将一并清理，确认删除选中的 ${ids.length} 条小记？`,
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
const columns: DataTableColumns<TimeNotesRow> = [
  {
    title: '小记',
    key: 'title',
    minWidth: 320,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        h('div', { class: 'cell-comment-content' }, row.title || '（无标题）'),
        row.content
          ? h('div', { class: 'cell-comment-sub cell-clamp-2', style: 'white-space:normal;line-height:1.5;' }, row.content)
          : null
      ])
  },
  {
    title: '作者',
    key: 'nickname',
    width: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.nickname || '—')
      ])
  },
  {
    title: '记录时间',
    key: 'recordTime',
    width: 120,
    render: (row) => row.recordTime || '—'
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
