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
  DICTUM_STATE_MAP,
  DICTUM_VISIBLE_MAP,
  useDictumAdminApi,
  type DictumRow
} from '~/composables/useDictumAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getDictumList, deleteDictums } = useDictumAdminApi();

// ---------- 搜索（后端 wrapper：dictumState/state 均 eq） ----------
const searchFields: AdminSearchField[] = [
  {
    key: 'dictumState',
    label: '可见性',
    type: 'select',
    width: 140,
    options: [
      { label: '公开', value: 1 },
      { label: '私有', value: 2 },
      { label: '关注可看', value: 3 }
    ]
  },
  {
    key: 'state',
    label: '状态',
    type: 'select',
    width: 140,
    options: [
      { label: '正常', value: 0 },
      { label: '已删除', value: 2 }
    ]
  }
];

const fetch = (params: Record<string, any>) => getDictumList(params);

// ---------- 删除（批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteDictums(ids);
  message.success(`已删除 ${ids.length} 条随笔`);
  await listRef.value?.refresh();
};

const doDelete = (row: DictumRow) => {
  dialog.warning({
    title: '删除随笔',
    content: '删除后不可恢复，确认删除这条随笔？',
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
    title: '批量删除随笔',
    content: `删除后不可恢复，确认删除选中的 ${ids.length} 条随笔？`,
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
const columns: DataTableColumns<DictumRow> = [
  {
    title: '随笔内容',
    key: 'content',
    minWidth: 300,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        // cell-comment-sub 自带 nowrap，与 cell-clamp-2 同挂需 inline 覆白空间折行
        h(
          'div',
          { class: 'cell-clamp-2', style: 'font-size:13px;line-height:1.5;' },
          row.content || row.contentMd || '—'
        ),
        row.author ? h('div', { class: 'cell-comment-sub' }, `—— ${row.author}${row.worksName ? `《${row.worksName}》` : ''}`) : null
      ])
  },
  {
    title: '分组',
    key: 'groupName',
    width: 110,
    render: (row) => row.groupName || '—'
  },
  {
    title: '相册',
    key: 'albumName',
    width: 110,
    render: (row) => row.albumName || '—'
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
    title: '可见性',
    key: 'dictumState',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.dictumState, map: DICTUM_VISIBLE_MAP })
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: DICTUM_STATE_MAP })
  },
  {
    title: '评论',
    key: 'commentSum',
    width: 80,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.commentSum ?? 0))
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
