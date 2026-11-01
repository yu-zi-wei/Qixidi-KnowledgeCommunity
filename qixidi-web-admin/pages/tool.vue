<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getToolList"
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
      <n-button type="primary" @click="openForm()">新增工具</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="520" placement="right">
    <n-drawer-content :title="editingId ? `编辑工具 · ${form.toolName}` : '新增工具'" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="工具名称" path="toolName">
          <n-input v-model:value="form.toolName" placeholder="请输入工具名称" :maxlength="50" />
        </n-form-item>
        <n-form-item label="工具地址" path="toolUrl">
          <n-input v-model:value="form.toolUrl" placeholder="请输入完整地址（https://…）" />
        </n-form-item>
        <n-form-item label="描述" path="describe">
          <n-input v-model:value="form.describe" type="textarea" :rows="2" placeholder="请输入工具描述（可选）" :maxlength="200" />
        </n-form-item>
        <n-form-item label="图标地址" path="icon">
          <n-input v-model:value="form.icon" placeholder="图标图片 URL（可选）" />
        </n-form-item>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="层级" path="isParent">
            <n-radio-group v-model:value="form.isParent">
              <n-radio :value="1">父级分组</n-radio>
              <n-radio :value="2">子级工具</n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="父级分组" path="parentId">
            <n-select
              v-model:value="form.parentId"
              :options="parentOptions"
              :disabled="form.isParent === 1"
              placeholder="父级分组（仅子级可选）"
              clearable
              class="tool-full"
            />
          </n-form-item>
        </n-grid>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="排序" path="order">
            <n-input-number v-model:value="form.order" :min="0" class="tool-full" />
          </n-form-item>
          <n-form-item label="状态" path="state">
            <n-radio-group v-model:value="form.state">
              <n-radio :value="0">正常</n-radio>
              <n-radio :value="1">已失效</n-radio>
            </n-radio-group>
          </n-form-item>
        </n-grid>
      </n-form>
      <template #footer>
        <n-space>
          <n-button @click="formVisible = false">取消</n-button>
          <n-button type="primary" :loading="saving" @click="handleSave">保存</n-button>
        </n-space>
      </template>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { TOOL_STATE_MAP, useConfigureAdminApi, type ToolRow } from '~/composables/useConfigureAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getToolList, getTool, addTool, editTool, deleteTools } = useConfigureAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'toolName', label: '工具名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'states',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '正常', value: 0 },
      { label: '已失效', value: 1 }
    ]
  },
  {
    key: 'isParents',
    label: '层级',
    type: 'multiple',
    options: [
      { label: '父级分组', value: 1 },
      { label: '子级工具', value: 2 }
    ]
  }
];

// ---------- 全量工具（父级名称映射 + 父级下拉；工具条目量小，一次拉取） ----------
const allRows = ref<ToolRow[]>([]);
const loadAll = async () => {
  try {
    const res = await getToolList({ pageNum: 1, pageSize: 200 });
    allRows.value = res.rows || [];
  } catch {
    /* useApi 已统一弹错 */
  }
};
onMounted(loadAll);

const parentName = (id?: number | null) => allRows.value.find((r) => r.id === id)?.toolName || '—';
const parentOptions = computed(() =>
  allRows.value
    .filter((r) => r.isParent === 1)
    .map((r) => ({ label: r.toolName || String(r.id), value: r.id }))
);

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteTools(ids);
  message.success(`已删除 ${ids.length} 个工具项`);
  await listRef.value?.refresh();
  await loadAll();
};

const doDelete = (row: ToolRow) => {
  dialog.warning({
    title: '删除工具项',
    content: `删除后「${row.toolName}」立即从前台工具页移除，确认删除？`,
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
    title: '批量删除工具项',
    content: `删除后所选 ${ids.length} 个工具项立即从前台工具页移除，确认删除？`,
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

// ---------- 表格列 ----------
const columns: DataTableColumns<ToolRow> = [
  { title: '工具名称', key: 'toolName', minWidth: 150, ellipsis: { tooltip: true } },
  {
    title: '层级',
    key: 'isParent',
    width: 90,
    render: (row) => (row.isParent === 1 ? '父级分组' : row.parentId ? `子级 · ${parentName(row.parentId)}` : '子级')
  },
  { title: '描述', key: 'describe', minWidth: 180, ellipsis: { tooltip: true }, render: (row) => row.describe || h('span', { class: 'cell-dim' }, '—') },
  { title: '地址', key: 'toolUrl', minWidth: 200, ellipsis: { tooltip: true } },
  { title: '排序', key: 'order', width: 70, align: 'center' },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: TOOL_STATE_MAP })
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
    width: 120,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(NButton, { size: 'small', text: true, type: 'primary', onClick: () => openForm(row) }, { default: () => '编辑' }),
        h(
          NButton,
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];

// ---------- 新增/编辑 ----------
const formVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  toolName: '',
  toolUrl: '',
  describe: '',
  icon: '',
  isParent: 2,
  parentId: null as number | null,
  order: 0,
  state: 0
});

const rules: FormRules = {
  toolName: { required: true, message: '请输入工具名称', trigger: 'blur' },
  toolUrl: { required: true, message: '请输入工具地址', trigger: 'blur' },
  isParent: { required: true, type: 'number', message: '请选择层级', trigger: ['blur', 'change'] },
  order: { required: true, type: 'number', message: '请输入排序', trigger: ['blur', 'change'] }
};

const openForm = async (row?: ToolRow) => {
  editingId.value = row?.id ?? null;
  formVisible.value = true;
  if (row) {
    try {
      const detail = await getTool(row.id);
      Object.assign(form, {
        toolName: detail.toolName || '',
        toolUrl: detail.toolUrl || '',
        describe: detail.describe || '',
        icon: detail.icon || '',
        isParent: detail.isParent ?? 2,
        parentId: detail.isParent === 1 ? null : detail.parentId ?? null,
        order: detail.order ?? 0,
        state: detail.state ?? 0
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  } else {
    Object.assign(form, { toolName: '', toolUrl: '', describe: '', icon: '', isParent: 2, parentId: null, order: 0, state: 0 });
  }
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      const data = { ...form, parentId: form.isParent === 1 ? null : form.parentId };
      if (editingId.value) {
        await editTool({ ...data, id: editingId.value });
      } else {
        await addTool({ ...data });
      }
      message.success('保存成功');
      formVisible.value = false;
      await listRef.value?.refresh();
      await loadAll();
    } catch {
      /* useApi 已统一弹错 */
    } finally {
      saving.value = false;
    }
  });
};
</script>

<style scoped lang="scss">
.tool-full {
  width: 100%;
}
</style>
