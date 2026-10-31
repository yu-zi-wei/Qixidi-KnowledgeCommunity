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
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import { useDictumAdminApi, type DictumGroupRow } from '~/composables/useDictumAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const router = useRouter();
const { getDictumGroupList, deleteDictumGroups } = useDictumAdminApi();

/** 收录数点击 → 随笔列表按该分组筛选 */
const goDictumList = (groupId: number) => {
  router.push({ path: '/dictum', query: { groupId } });
};

// ---------- 搜索（name 后端 like） ----------
const searchFields: AdminSearchField[] = [
  { key: 'name', label: '分组名称', type: 'input', placeholder: '请输入关键词' }
];

const fetch = (params: Record<string, any>) => getDictumGroupList(params);

// ---------- 删除（分组被随笔引用时前端先确认；批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteDictumGroups(ids);
  message.success(`已删除 ${ids.length} 个分组`);
  await listRef.value?.refresh();
};

const doDelete = (row: DictumGroupRow) => {
  dialog.warning({
    title: '删除分组',
    content: '删除后不可恢复，该分组下的随笔将失去分组归属，确认删除？',
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
    title: '批量删除分组',
    content: `删除后不可恢复，该分组下的随笔将失去分组归属，确认删除选中的 ${ids.length} 个分组？`,
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

// ---------- 表格列（render 元素样式走全局类，scoped 选不中） ----------
const columns: DataTableColumns<DictumGroupRow> = [
  {
    title: '分组名称',
    key: 'name',
    minWidth: 160,
    render: (row) => h('span', { class: 'cell-ellipsis' }, row.name || '—')
  },
  {
    title: '简介',
    key: 'briefIntroduction',
    minWidth: 240,
    render: (row) =>
      row.briefIntroduction
        ? h('span', { class: 'cell-clamp-2', style: 'font-size:13px;line-height:1.5;' }, row.briefIntroduction)
        : h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '收录数',
    key: 'employSum',
    width: 90,
    align: 'right',
    // 点击跳随笔列表并按该分组筛选（is-link 主色可点击样式在 admin-table.scss）
    render: (row) =>
      h(
        'span',
        { class: 'cell-stats is-link', onClick: () => goDictumList(row.id), title: '查看该分组下的随笔' },
        String(row.employSum ?? 0)
      )
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
