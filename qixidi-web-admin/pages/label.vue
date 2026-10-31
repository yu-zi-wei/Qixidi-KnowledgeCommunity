<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getLabels"
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
      <n-button size="small" type="primary" @click="openAdd">新增标签</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑弹窗：编辑一律调详情接口回填（teleport 组件宽度必须 inline style；样式走非 scoped 块） -->
  <n-modal v-model:show="formVisible" :style="{ width: '460px' }" class="label-form-modal-host" preset="card" :title="editing ? '编辑标签' : '新增标签'">
    <n-form label-placement="top" size="small">
      <n-form-item label="标签名称" required>
        <n-input v-model:value="form.labelName" placeholder="请输入标签名称" :maxlength="50" />
      </n-form-item>
      <n-form-item label="所属分类" required>
        <n-select v-model:value="form.labelGroupingId" filterable :options="groupingOptions" placeholder="请选择分类" />
      </n-form-item>
      <n-form-item label="标签描述">
        <n-input v-model:value="form.labelDescribe" type="textarea" :rows="3" placeholder="标签简介（可选）" :maxlength="200" />
      </n-form-item>
    </n-form>
    <p class="label-form-tip">标签文章数/关注数由系统定时重算，无需手工维护。</p>
    <template #footer>
      <div class="label-form-footer">
        <n-button size="small" @click="formVisible = false">取消</n-button>
        <n-button size="small" type="primary" :loading="saving" :disabled="!form.labelName || !form.labelGroupingId" @click="doSave">
          保存
        </n-button>
      </div>
    </template>
  </n-modal>

  <!-- 合并弹窗：源标签的文章挂载与关注迁入目标后删除源（teleport 组件宽度必须 inline style） -->
  <n-modal v-model:show="mergeVisible" :style="{ width: '460px' }" class="label-merge-modal-host" preset="card" title="合并标签">
    <div v-if="mergeSource" class="label-merge-body">
      <div class="label-merge-row">
        <span class="label-merge-label">源标签</span>
        <span>{{ mergeSource.labelName }}（合并后删除）</span>
      </div>
      <div class="label-merge-row">
        <span class="label-merge-label">并入目标</span>
        <n-select
          v-model:value="mergeTargetId"
          filterable
          clearable
          :options="mergeTargetOptions"
          placeholder="请选择要并入的标签"
          class="label-merge-select"
        />
      </div>
      <p class="label-merge-tip">源标签下文章的挂载与用户关注将迁入目标标签（已挂目标的自动去重），源标签随后删除。</p>
    </div>
    <template #footer>
      <div class="label-merge-footer">
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
import { LABEL_STATE_MAP, useLabelAdminApi, type GroupingRow, type LabelRow } from '~/composables/useLabelAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getLabels, getLabel, addLabel, updateLabel, deleteLabels, mergeLabel, getGroupings } = useLabelAdminApi();

// ---------- 搜索（labelName like / labelGroupingId eq / state eq） ----------
const groupingOptions = ref<SelectOption[]>([]);
const searchFields = ref<AdminSearchField[]>([
  { key: 'labelName', label: '标签名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'labelGroupingId',
    label: '所属分类',
    type: 'select',
    options: groupingOptions
  },
  {
    key: 'state',
    label: '状态',
    type: 'select',
    options: [
      { label: '正常', value: 0 },
      { label: '已删除', value: 1 }
    ]
  }
]);

onMounted(async () => {
  try {
    const { rows } = await getGroupings({ pageNum: 1, pageSize: 500, state: 0 });
    groupingOptions.value = (rows || []).map((g: GroupingRow) => ({ label: g.groupingName || `分类 ${g.id}`, value: g.id }));
  } catch {
    /* useApi 已统一弹错，下拉留空 */
  }
});

// ---------- 删除（物理删除，文章上的残留引用由前台忽略） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteLabels(ids);
  message.success(`已删除 ${ids.length} 个标签`);
  await listRef.value?.refresh();
};

const confirmDelete = (ids: Array<string | number>, names: string) => {
  dialog.warning({
    title: '删除标签',
    content: `删除后不可恢复，文章上残留的标签挂载将被前台忽略（建议改用「合并」保留数据），确认删除${names}？`,
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

const doDelete = (row: LabelRow) => {
  deletingId.value = row.id;
  confirmDelete([row.id], `「${row.labelName}」`);
};

const doDeleteBatch = () => {
  const ids = [...checkedKeys.value];
  batchDeleting.value = true;
  confirmDelete(ids, `选中的 ${ids.length} 个标签`);
};

// ---------- 合并（源的文章挂载与关注迁入目标后删除源） ----------
const mergeVisible = ref(false);
const merging = ref(false);
const mergeSource = ref<LabelRow | null>(null);
const mergeTargetId = ref<number | null>(null);
const mergeTargetOptions = ref<SelectOption[]>([]);

const openMerge = async (row: LabelRow) => {
  mergeSource.value = row;
  mergeTargetId.value = null;
  mergeVisible.value = true;
  try {
    // 目标下拉：正常状态且排除源自身
    const { rows } = await getLabels({ pageNum: 1, pageSize: 500, state: 0 });
    mergeTargetOptions.value = (rows || [])
      .filter((l: LabelRow) => l.id !== row.id)
      .map((l: LabelRow) => ({ label: l.labelName || `标签 ${l.id}`, value: l.id }));
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
    title: '合并标签',
    content: `「${source.labelName}」的文章挂载与用户关注将迁入「${targetName}」，随后「${source.labelName}」被删除，确认合并？`,
    positiveText: '合并',
    negativeText: '取消',
    onPositiveClick: async () => {
      merging.value = true;
      try {
        await mergeLabel(source.id, targetId);
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
const emptyForm = (): Partial<LabelRow> => ({ labelName: '', labelGroupingId: undefined, labelDescribe: '' });
const form = ref<Partial<LabelRow>>(emptyForm());

const openAdd = () => {
  editing.value = false;
  form.value = emptyForm();
  formVisible.value = true;
};

const openEdit = async (row: LabelRow) => {
  editing.value = true;
  form.value = emptyForm(); // 先清空防残留上一项
  formVisible.value = true;
  try {
    form.value = await getLabel(row.id);
  } catch {
    /* useApi 已弹错，表单留空 */
  }
};

const doSave = async () => {
  saving.value = true;
  try {
    if (editing.value) {
      await updateLabel(form.value);
      message.success('已保存');
    } else {
      await addLabel(form.value);
      message.success('已新增标签');
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
const columns: DataTableColumns<LabelRow> = [
  {
    title: '标签名称',
    key: 'labelName',
    minWidth: 180,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        // 名称即编辑入口；样式类在 admin-table.scss
        h('div', { class: 'cell-comment-content is-link', onClick: () => openEdit(row), title: '编辑标签' }, row.labelName || '—'),
        h('div', { class: 'cell-comment-sub cell-ellipsis', title: row.labelDescribe || '' }, row.labelDescribe || '')
      ])
  },
  {
    title: '所属分类',
    key: 'groupingName',
    minWidth: 140,
    render: (row) => row.groupingName || h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '文章数',
    key: 'articleNumber',
    width: 90,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.articleNumber ?? 0))
  },
  {
    title: '关注数',
    key: 'followNumber',
    width: 90,
    align: 'right',
    render: (row) => h('span', { class: 'cell-stats' }, String(row.followNumber ?? 0))
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
.label-form-modal-host {
  .label-form-tip {
    margin: 4px 0 0;
    font-size: 12px;
    opacity: 0.55;
  }

  .label-form-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}

.label-merge-modal-host {
  .label-merge-body {
    display: flex;
    flex-direction: column;
    gap: 14px;
  }

  .label-merge-row {
    display: flex;
    flex-direction: column;
    gap: 6px;
    font-size: 13px;
  }

  .label-merge-label {
    font-size: 12px;
    opacity: 0.5;
  }

  .label-merge-select {
    width: 100%;
  }

  .label-merge-tip {
    margin: 0;
    font-size: 12px;
    line-height: 1.6;
    opacity: 0.55;
  }

  .label-merge-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
