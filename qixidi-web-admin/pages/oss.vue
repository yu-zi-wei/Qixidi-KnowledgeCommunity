<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getOssList"
    row-key="ossId"
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
import { NButton, NImage } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import { isImageSuffix, useOssAdminApi, type OssRow } from '~/composables/useOssAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getOssList, deleteOss, downloadOss } = useOssAdminApi();

// ---------- 搜索（originalName/fileName 后端 like；fileSuffix eq 带点） ----------
const searchFields: AdminSearchField[] = [
  { key: 'originalName', label: '文件原名', type: 'input', placeholder: '请输入文件名关键词' },
  { key: 'fileSuffix', label: '文件后缀', type: 'input', placeholder: '如 .png' }
];

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteOss(ids);
  message.success(`已删除 ${ids.length} 个文件`);
  await listRef.value?.refresh();
};

const doDelete = (row: OssRow) => {
  dialog.warning({
    title: '删除文件',
    content: `「${row.originalName || row.fileName}」删除后不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.ossId;
      try {
        await runDelete([row.ossId]);
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
    title: '批量删除文件',
    content: `删除后不可恢复，确认删除选中的 ${ids.length} 个文件？`,
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

// ---------- 下载（blob 手动保存，token 走 header 无法 window.open） ----------
const downloadingId = ref<number | null>(null);
const doDownload = async (row: OssRow) => {
  downloadingId.value = row.ossId;
  try {
    await downloadOss(row);
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    downloadingId.value = null;
  }
};

// ---------- 表格列（render 元素样式走全局类，scoped 选不中） ----------
const columns: DataTableColumns<OssRow> = [
  {
    title: '文件',
    key: 'fileName',
    minWidth: 220,
    render: (row) =>
      h('div', { class: 'oss-file-cell' }, [
        isImageSuffix(row.fileSuffix)
          ? h(NImage, {
              src: row.url,
              width: 44,
              height: 44,
              objectFit: 'cover',
              lazy: true,
              fallbackSrc: undefined
            })
          : h('span', { class: 'oss-file-badge', title: row.fileSuffix || '' }, (row.fileSuffix || 'file').replace('.', '').slice(0, 4).toUpperCase()),
        h('div', { class: 'oss-file-main' }, [
          h('div', { class: 'cell-ellipsis', title: row.fileName || '' }, row.fileName || '—'),
          h('div', { class: 'cell-comment-sub cell-ellipsis', title: row.originalName || '' }, row.originalName || '')
        ])
      ])
  },
  {
    title: '后缀',
    key: 'fileSuffix',
    width: 80,
    align: 'center',
    render: (row) => row.fileSuffix || '—'
  },
  {
    title: '服务商',
    key: 'service',
    width: 100,
    render: (row) => row.service || '—'
  },
  {
    title: '上传时间',
    key: 'createTime',
    width: 150,
    render: (row) => getFullDateTime(row.createTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 110,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          { size: 'small', text: true, loading: downloadingId.value === row.ossId, onClick: () => doDownload(row) },
          { default: () => '下载' }
        ),
        h(
          NButton,
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.ossId, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<!-- n-image 与文件徽标在表格 render 上下文，scoped 选不中：非 scoped 块（页面前缀防跨页覆盖） -->
<style lang="scss">
.oss-file-cell {
  display: flex;
  align-items: center;
  gap: 10px;

  .n-image {
    flex-shrink: 0;
    border-radius: 6px;
    overflow: hidden;
  }
}

.oss-file-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.oss-file-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: var(--cell-link, #3d7a5c);
  background: rgba(127, 127, 127, 0.12);
  border-radius: 6px;
}
</style>
