<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :key="listKey"
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
  type DictumAlbumRow,
  type DictumGroupRow,
  type DictumRow
} from '~/composables/useDictumAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const route = useRoute();
const { getDictumList, getDictumGroupList, getDictumAlbumList, deleteDictums } = useDictumAdminApi();

// ---------- 分组/专辑下拉选项（全量拉取，量小） ----------
const groupOptions = ref<Array<{ label: string; value: number }>>([]);
const albumOptions = ref<Array<{ label: string; value: number }>>([]);
onMounted(async () => {
  try {
    const [groups, albums] = await Promise.all([
      getDictumGroupList({ pageNum: 1, pageSize: 500 }),
      getDictumAlbumList({ pageNum: 1, pageSize: 500 })
    ]);
    groupOptions.value = (groups.rows || []).map((g: DictumGroupRow) => ({ label: g.name, value: g.id }));
    albumOptions.value = (albums.rows || []).map((a: DictumAlbumRow) => ({ label: a.name, value: a.id }));
  } catch {
    /* useApi 已统一弹错，选项留空不影响主列表 */
  }
});

// ---------- URL 条件注入：分组/专辑页收录数跳转带 query，作为对应下拉初始值 ----------
// query 变化时 :key 重建 AdminListPage（defaultValue 重新生效，翻页状态归零属预期）
const toId = (v: unknown) => (v != null && v !== '' && !Number.isNaN(Number(v)) ? Number(v) : null);
const initialGroupId = toId(route.query.groupId);
const initialAlbumId = toId(route.query.albumId);
const listKey = computed(() => `${route.query.groupId ?? ''}-${route.query.albumId ?? ''}`);

// ---------- 搜索（后端 wrapper：groupId/albumId/dictumState/state 均 eq） ----------
// computed 让分组/专辑 options 在异步拉取完成后响应更新（defaultValue 只在挂载初始化时生效）
const searchFields = computed<AdminSearchField[]>(() => [
  { key: 'groupId', label: '分组', type: 'select', width: 160, defaultValue: initialGroupId, options: groupOptions.value },
  { key: 'albumId', label: '专辑', type: 'select', width: 160, defaultValue: initialAlbumId, options: albumOptions.value },
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
]);

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
    title: '专辑',
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
