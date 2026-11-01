<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getAlbumList"
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
        移除所选（{{ checkedKeys.length }}）
      </n-button>
    </template>
    <template #toolbar-right>
      <n-button :loading="backfilling" @click="doBackfill">存量回填</n-button>
    </template>
  </AdminListPage>

  <!-- 用户详情抽屉（归属用户列点击打开） -->
  <UserDetailDrawer ref="userDrawerRef" />
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import UserDetailDrawer from '~/components/UserDetailDrawer.vue';
import { ALBUM_SOURCE_MAP, useAlbumAdminApi, type AlbumRow } from '~/composables/useAlbumAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getAlbumList, deleteAlbums, backfillAlbum } = useAlbumAdminApi();

// ---------- 搜索（month 格式 yyyy-MM 按月 / yyyy 按年） ----------
const searchFields: AdminSearchField[] = [
  { key: 'uid', label: '用户 ID', type: 'input', placeholder: '请输入归属用户 uuid' },
  {
    key: 'sources',
    label: '来源',
    type: 'multiple',
    options: [
      { label: '时光小记', value: 1 },
      { label: '文章', value: 2 }
    ]
  },
  { key: 'month', label: '时间', type: 'input', placeholder: '如 2026-10 或 2026' }
];

// ---------- 删除/回填 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
// 用户详情抽屉（归属用户列点击打开）
const userDrawerRef = ref<InstanceType<typeof UserDetailDrawer> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);
const backfilling = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteAlbums(ids);
  message.success(`已移除 ${ids.length} 张图片`);
  await listRef.value?.refresh();
};

const doDelete = (row: AlbumRow) => {
  dialog.warning({
    title: '移除图片',
    content: '移除后该图片不再出现在相册；若来源内容再次编辑或触发回填，会按内容重新解析入库。确认移除？',
    positiveText: '移除',
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
    title: '批量移除图片',
    content: `移除后所选 ${ids.length} 张图片不再出现在相册；来源内容再次编辑或回填时会重新解析入库。确认移除？`,
    positiveText: '移除',
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

const doBackfill = () => {
  dialog.warning({
    title: '存量回填',
    content: '将全量扫描时光小记内容重建相册（先清空小记来源的相册记录再重插，可重复触发）。确认执行？',
    positiveText: '执行',
    negativeText: '取消',
    onPositiveClick: async () => {
      backfilling.value = true;
      try {
        await backfillAlbum();
        message.success('回填完成，入库数量见服务端日志');
        await listRef.value?.reload();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        backfilling.value = false;
      }
    }
  });
};

// ---------- 表格列 ----------
const columns: DataTableColumns<AlbumRow> = [
  {
    title: '图片',
    key: 'url',
    width: 120,
    render: (row) =>
      h('a', { href: row.url, target: '_blank', rel: 'noopener' }, [
        h(AdminAvatar, { src: row.url, name: '', size: [96, 64], shape: 'square' })
      ])
  },
  {
    title: '归属用户',
    key: 'nickname',
    minWidth: 140,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname, size: 24 }),
        // 归属用户昵称可点开用户详情（无 uid 的行不可点）
        row.uid
          ? h('span', { class: 'cell-author-name is-link', onClick: () => userDrawerRef.value?.open(row.uid), title: '查看用户详情' }, row.nickname || row.uid || '—')
          : h('span', { class: 'cell-author-name' }, row.nickname || row.uid || '—')
      ])
  },
  {
    title: '来源',
    key: 'source',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.source, map: ALBUM_SOURCE_MAP })
  },
  {
    title: '来源内容 ID',
    key: 'sourceId',
    width: 110,
    align: 'center',
    render: (row) => (row.sourceId != null ? String(row.sourceId) : '—')
  },
  {
    title: '来源时间',
    key: 'sourceTime',
    width: 120,
    render: (row) => row.sourceTime || '—'
  },
  {
    title: '入库时间',
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
          { default: () => '移除' }
        )
      ])
  }
];
</script>
