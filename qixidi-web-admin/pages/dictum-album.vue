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
  type DictumAlbumRow
} from '~/composables/useDictumAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getDictumAlbumList, deleteDictumAlbums } = useDictumAdminApi();

// ---------- 搜索（name 后端 like；albumState eq） ----------
const searchFields: AdminSearchField[] = [
  { key: 'name', label: '相册名称', type: 'input', placeholder: '请输入关键词' },
  {
    key: 'albumState',
    label: '可见性',
    type: 'select',
    width: 140,
    options: [
      { label: '公开', value: 1 },
      { label: '私有', value: 2 },
      { label: '关注可看', value: 3 }
    ]
  }
];

const fetch = (params: Record<string, any>) => getDictumAlbumList(params);

// ---------- 删除（批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteDictumAlbums(ids);
  message.success(`已删除 ${ids.length} 个相册`);
  await listRef.value?.refresh();
};

const doDelete = (row: DictumAlbumRow) => {
  dialog.warning({
    title: '删除相册',
    content: '删除后不可恢复，该相册下的随笔将失去相册归属，确认删除？',
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
    title: '批量删除相册',
    content: `删除后不可恢复，该相册下的随笔将失去相册归属，确认删除选中的 ${ids.length} 个相册？`,
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
const columns: DataTableColumns<DictumAlbumRow> = [
  {
    title: '相册',
    key: 'name',
    minWidth: 200,
    render: (row) =>
      h('div', { class: 'cell-article' }, [
        h(AdminAvatar, { src: row.cover, name: row.name, size: [40, 30], shape: 'square' }),
        h('div', { class: 'cell-article-main' }, [
          h('span', { class: 'cell-article-name' }, row.name || '—')
        ])
      ])
  },
  {
    title: '简介',
    key: 'briefIntroduction',
    minWidth: 220,
    render: (row) =>
      row.briefIntroduction
        ? h('span', { class: 'cell-clamp-2', style: 'font-size:13px;line-height:1.5;' }, row.briefIntroduction)
        : h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '可见性',
    key: 'albumState',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.albumState, map: DICTUM_VISIBLE_MAP })
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: DICTUM_STATE_MAP })
  },
  {
    title: '收录',
    key: 'employSum',
    width: 70,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.employSum ?? 0))
  },
  {
    title: '点赞',
    key: 'helpSum',
    width: 70,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.helpSum ?? 0))
  },
  {
    title: '关注',
    key: 'followSum',
    width: 70,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.followSum ?? 0))
  },
  {
    title: '创建时间',
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
