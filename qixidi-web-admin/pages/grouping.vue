<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getGroupings"
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
    <template #toolbar-right>
      <n-button size="small" type="primary" @click="openAdd">新增分类</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑弹窗：编辑一律调详情接口回填（teleport 组件宽度必须 inline style；样式走非 scoped 块） -->
  <n-modal v-model:show="formVisible" :style="{ width: '460px' }" class="grouping-form-modal-host" preset="card" :title="editing ? '编辑分类' : '新增分类'">
    <n-form label-placement="top" size="small">
      <n-form-item label="分类名称" required>
        <n-input v-model:value="form.groupingName" placeholder="请输入分类名称" :maxlength="50" />
      </n-form-item>
      <n-form-item label="分类描述" required>
        <n-input v-model:value="form.groupingDescribe" type="textarea" :rows="3" placeholder="请输入分类描述" :maxlength="200" />
      </n-form-item>
    </n-form>
    <p class="grouping-form-tip">分类收录数由系统定时重算，无需手工维护。</p>
    <template #footer>
      <div class="grouping-form-footer">
        <n-button size="small" @click="formVisible = false">取消</n-button>
        <n-button size="small" type="primary" :loading="saving" :disabled="!form.groupingName || !form.groupingDescribe" @click="doSave">
          保存
        </n-button>
      </div>
    </template>
  </n-modal>

  <!-- 合并弹窗：源分类下的标签与文章归属迁入目标后删除源（teleport 组件宽度必须 inline style） -->
  <n-modal v-model:show="mergeVisible" :style="{ width: '460px' }" class="grouping-merge-modal-host" preset="card" title="合并分类">
    <div v-if="mergeSource" class="grouping-merge-body">
      <div class="grouping-merge-row">
        <span class="grouping-merge-label">源分类</span>
        <span>{{ mergeSource.groupingName }}（合并后删除）</span>
      </div>
      <div class="grouping-merge-row">
        <span class="grouping-merge-label">并入目标</span>
        <n-select
          v-model:value="mergeTargetId"
          filterable
          clearable
          :options="mergeTargetOptions"
          placeholder="请选择要并入的分类"
          class="grouping-merge-select"
        />
      </div>
      <p class="grouping-merge-tip">源分类下的标签归属与文章分类将迁入目标分类，源分类随后删除。</p>
    </div>
    <template #footer>
      <div class="grouping-merge-footer">
        <n-button size="small" @click="mergeVisible = false">取消</n-button>
        <n-button size="small" type="warning" :loading="merging" :disabled="!mergeTargetId" @click="doMerge">
          确认合并
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns, SelectOption } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { LABEL_STATE_MAP, useLabelAdminApi, type GroupingRow } from '~/composables/useLabelAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getGroupings, getGrouping, addGrouping, updateGrouping, deleteGroupings, mergeGrouping } = useLabelAdminApi();

// ---------- 搜索（groupingName like / state eq） ----------
const searchFields: AdminSearchField[] = [
  { key: 'groupingName', label: '分类名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'state',
    label: '状态',
    type: 'select',
    options: [
      { label: '正常', value: 0 },
      { label: '已删除', value: 1 }
    ]
  }
];

// ---------- 删除（物理删除，分类下标签将失去归属） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteGroupings(ids);
  message.success(`已删除 ${ids.length} 个分类`);
  await listRef.value?.refresh();
};

const confirmDelete = (ids: Array<string | number>, names: string) => {
  dialog.warning({
    title: '删除分类',
    content: `删除后不可恢复，分类下标签将失去归属（建议改用「合并」保留数据），确认删除${names}？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await runDelete(ids);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        deletingId.value = null;
        batchDeleting.value = false;
      }
    }
  });
};

const doDelete = (row: GroupingRow) => {
  deletingId.value = row.id;
  confirmDelete([row.id], `「${row.groupingName}」`);
};

const doDeleteBatch = () => {
  const ids = [...checkedKeys.value];
  batchDeleting.value = true;
  confirmDelete(ids, `选中的 ${ids.length} 个分类`);
};

// ---------- 合并（源分类下的标签与文章归属迁入目标后删除源） ----------
const mergeVisible = ref(false);
const merging = ref(false);
const mergeSource = ref<GroupingRow | null>(null);
const mergeTargetId = ref<number | null>(null);
const mergeTargetOptions = ref<SelectOption[]>([]);

const openMerge = async (row: GroupingRow) => {
  mergeSource.value = row;
  mergeTargetId.value = null;
  mergeVisible.value = true;
  try {
    // 目标下拉：正常状态且排除源自身
    const { rows } = await getGroupings({ pageNum: 1, pageSize: 500, state: 0 });
    mergeTargetOptions.value = (rows || [])
      .filter((g: GroupingRow) => g.id !== row.id)
      .map((g: GroupingRow) => ({ label: g.groupingName || `分类 ${g.id}`, value: g.id }));
  } catch {
    /* useApi 已统一弹错，下拉留空 */
  }
};

const doMerge = () => {
  const source = mergeSource.value;
  const targetId = mergeTargetId.value;
  if (!source || !targetId) return;
  const targetName = mergeTargetOptions.value.find((o) => o.value === targetId)?.label || `#${targetId}`;
  dialog.warning({
    title: '合并分类',
    content: `「${source.groupingName}」下的标签归属与文章分类将迁入「${targetName}」，随后「${source.groupingName}」被删除，确认合并？`,
    positiveText: '合并',
    negativeText: '取消',
    onPositiveClick: async () => {
      merging.value = true;
      try {
        await mergeGrouping(source.id, targetId);
        message.success(`已合并入「${targetName}」`);
        mergeVisible.value = false;
        await listRef.value?.refresh();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        merging.value = false;
      }
    }
  });
};

// ---------- 新增/编辑（编辑先调详情接口回填，弹窗打开即清空防残留） ----------
const formVisible = ref(false);
const saving = ref(false);
const editing = ref(false);
const emptyForm = (): Partial<GroupingRow> => ({ groupingName: '', groupingDescribe: '' });
const form = ref<Partial<GroupingRow>>(emptyForm());

const openAdd = () => {
  editing.value = false;
  form.value = emptyForm();
  formVisible.value = true;
};

const openEdit = async (row: GroupingRow) => {
  editing.value = true;
  form.value = emptyForm(); // 先清空防残留上一项
  formVisible.value = true;
  try {
    form.value = await getGrouping(row.id);
  } catch {
    /* useApi 已弹错，表单留空 */
  }
};

const doSave = async () => {
  saving.value = true;
  try {
    if (editing.value) {
      await updateGrouping(form.value);
      message.success('已保存');
    } else {
      await addGrouping(form.value);
      message.success('已新增分类');
    }
    formVisible.value = false;
    if (editing.value) {
      await listRef.value?.refresh();
    } else {
      await listRef.value?.reload();
    }
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    saving.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<GroupingRow> = [
  {
    title: '分类名称',
    key: 'groupingName',
    minWidth: 160,
    render: (row) =>
      // 名称即编辑入口；样式类在 admin-table.scss
      h('span', { class: 'cell-ellipsis is-link', onClick: () => openEdit(row), title: '编辑分类' }, row.groupingName || '—')
  },
  {
    title: '收录数',
    key: 'entriesNumber',
    width: 90,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.entriesNumber ?? 0))
  },
  {
    title: '分类描述',
    key: 'groupingDescribe',
    minWidth: 240,
    render: (row) =>
      row.groupingDescribe
        ? h('span', { class: 'cell-clamp-2', style: 'font-size:13px;line-height:1.5;' }, row.groupingDescribe)
        : h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    align: 'center',
    render: (row) => h(AdminStateTag, { value: row.state ?? 0, map: LABEL_STATE_MAP })
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 160,
    render: (row) => getFullDateTime(row.createTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          { size: 'small', text: true, type: 'warning', onClick: () => openMerge(row) },
          { default: () => '合并' }
        ),
        h(
          NButton,
          { size: 'small', text: true, onClick: () => openEdit(row) },
          { default: () => '编辑' }
        ),
        h(
          NButton,
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<!-- n-modal teleport 到 body，scoped 选不中：表单/合并弹窗样式放非 scoped 块（页面前缀防跨页覆盖） -->
<style lang="scss">
.grouping-form-modal-host {
  .grouping-form-tip {
    margin: 4px 0 0;
    font-size: 12px;
    opacity: 0.55;
  }

  .grouping-form-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}

.grouping-merge-modal-host {
  .grouping-merge-body {
    display: flex;
    flex-direction: column;
    gap: 14px;
  }

  .grouping-merge-row {
    display: flex;
    flex-direction: column;
    gap: 6px;
    font-size: 13px;
  }

  .grouping-merge-label {
    font-size: 12px;
    opacity: 0.5;
  }

  .grouping-merge-select {
    width: 100%;
  }

  .grouping-merge-tip {
    margin: 0;
    font-size: 12px;
    line-height: 1.6;
    opacity: 0.55;
  }

  .grouping-merge-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
