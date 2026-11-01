<template>
  <div class="tool-config-page">
    <n-tabs v-model:value="activeTab" type="line" class="tool-config-tabs">
      <!-- display-directive 必须写在 n-tab-pane 上（写在 n-tabs 上无效）：两个 pane 常驻 v-show 切换，
           各自 AdminListPage 实例独立；否则切 tab 时 pane 被无 key 就地复用，fetch prop 换了但数据不重拉 -->
      <n-tab-pane name="catalog" tab="工具目录" display-directive="show">
        <AdminListPage
          ref="catalogListRef"
          v-model:checked-keys="catalogChecked"
          :search-fields="catalogSearchFields"
          :columns="catalogColumns"
          :fetch="fetchCatalog"
          row-key="id"
          seq
          selectable
          fill
        >
          <template #toolbar-left>
            <n-button
              size="small"
              type="error"
              secondary
              :disabled="!catalogChecked.length"
              :loading="batchDeleting"
              @click="doDeleteBatch"
            >
              删除所选（{{ catalogChecked.length }}）
            </n-button>
          </template>
          <template #toolbar-right>
            <n-button type="primary" @click="openCatalogForm()">新增目录</n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>

      <n-tab-pane name="tool" tab="工具" display-directive="show">
        <AdminListPage
          ref="toolListRef"
          v-model:checked-keys="toolChecked"
          :search-fields="toolSearchFields"
          :columns="toolColumns"
          :fetch="fetchTool"
          row-key="id"
          seq
          selectable
          fill
        >
          <template #toolbar-left>
            <n-button
              size="small"
              type="error"
              secondary
              :disabled="!toolChecked.length"
              :loading="batchDeleting"
              @click="doDeleteBatch"
            >
              删除所选（{{ toolChecked.length }}）
            </n-button>
          </template>
          <template #toolbar-right>
            <n-button type="primary" @click="openToolForm()">新增工具</n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>
    </n-tabs>

    <!-- 目录新增/编辑抽屉：teleport 组件宽度必须 inline style -->
    <n-drawer v-model:show="catalogFormVisible" :width="480" placement="right">
      <n-drawer-content :title="editingId ? `编辑目录 · ${form.toolName}` : '新增目录'" closable>
        <n-form ref="formRef" :model="form" :rules="catalogRules" label-placement="top">
          <n-form-item label="目录名称" path="toolName">
            <n-input v-model:value="form.toolName" placeholder="请输入目录名称" :maxlength="50" />
          </n-form-item>
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="排序" path="order">
                <n-input-number v-model:value="form.order" :min="0" class="tool-full" />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="状态" path="state">
                <n-radio-group v-model:value="form.state">
                  <n-radio :value="0">正常</n-radio>
                  <n-radio :value="1">已失效</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
          </n-grid>
        </n-form>
        <template #footer>
          <n-space>
            <n-button @click="catalogFormVisible = false">取消</n-button>
            <n-button type="primary" :loading="saving" @click="handleSave">保存</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>

    <!-- 工具新增/编辑抽屉 -->
    <n-drawer v-model:show="toolFormVisible" :width="520" placement="right">
      <n-drawer-content :title="editingId ? `编辑工具 · ${form.toolName}` : '新增工具'" closable>
        <n-form ref="formRef" :model="form" :rules="toolRules" label-placement="top">
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
          <!-- n-grid 子节点必须是 n-gi（grid item）：直接放 n-form-item 会被静默丢弃不渲染 -->
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="所属目录" path="parentId">
                <n-select
                  v-model:value="form.parentId"
                  :options="catalogOptions"
                  placeholder="请选择所属目录"
                  class="tool-full"
                />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="排序" path="order">
                <n-input-number v-model:value="form.order" :min="0" class="tool-full" />
              </n-form-item>
            </n-gi>
          </n-grid>
          <n-form-item label="状态" path="state">
            <n-radio-group v-model:value="form.state">
              <n-radio :value="0">正常</n-radio>
              <n-radio :value="1">已失效</n-radio>
            </n-radio-group>
          </n-form-item>
        </n-form>
        <template #footer>
          <n-space>
            <n-button @click="toolFormVisible = false">取消</n-button>
            <n-button type="primary" :loading="saving" @click="handleSave">保存</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
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

const activeTab = ref<'catalog' | 'tool'>('catalog');

// ---------- 目录全量（工具 tab 的所属目录下拉 + 父级名称映射；目录条目量小，一次拉取） ----------
const catalogRows = ref<ToolRow[]>([]);
const loadCatalogs = async () => {
  try {
    const res = await getToolList({ pageNum: 1, pageSize: 200, isParents: [1] });
    catalogRows.value = res.rows || [];
  } catch {
    /* useApi 已统一弹错 */
  }
};
onMounted(loadCatalogs);

const catalogName = (id?: number | null) => catalogRows.value.find((r) => r.id === id)?.toolName || '—';
const catalogOptions = computed(() =>
  catalogRows.value.map((r) => ({ label: r.toolName || String(r.id), value: r.id }))
);

// ---------- 搜索（fetch 闭包固定 isParents，与 activeTab 无关，避免两个常驻 pane 互相干扰） ----------
const stateField: AdminSearchField = {
  key: 'states',
  label: '状态',
  type: 'multiple',
  options: [
    { label: '正常', value: 0 },
    { label: '已失效', value: 1 }
  ]
};
const catalogSearchFields: AdminSearchField[] = [
  { key: 'toolName', label: '目录名称', type: 'input', placeholder: '请输入名称关键词' },
  stateField
];
const toolSearchFields = computed<AdminSearchField[]>(() => [
  { key: 'toolName', label: '工具名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'parentIds',
    label: '所属目录',
    type: 'multiple',
    options: catalogOptions.value
  },
  stateField
]);

const fetchCatalog = (params: Record<string, any>) => getToolList({ ...params, isParents: [1] });
const fetchTool = (params: Record<string, any>) => getToolList({ ...params, isParents: [2] });

// ---------- 删除（activeTab 分流对应列表与勾选） ----------
const catalogListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const toolListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const catalogChecked = ref<Array<string | number>>([]);
const toolChecked = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const activeListRef = () => (activeTab.value === 'catalog' ? catalogListRef.value : toolListRef.value);
const activeChecked = () => (activeTab.value === 'catalog' ? catalogChecked.value : toolChecked.value);
const activeRefresh = async () => {
  await activeListRef()?.refresh();
  await loadCatalogs();
};

const runDelete = async (ids: Array<string | number>) => {
  await deleteTools(ids);
  message.success(`已删除 ${ids.length} 个条目`);
  await activeRefresh();
};

const doDelete = (row: ToolRow) => {
  dialog.warning({
    title: '删除确认',
    content:
      activeTab.value === 'catalog'
        ? `删除后「${row.toolName}」目录立即从前台工具页移除（目录下有工具时将被拒绝），确认删除？`
        : `删除后「${row.toolName}」立即从前台工具页移除，确认删除？`,
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
  const ids = [...activeChecked()];
  dialog.warning({
    title: '批量删除',
    content: `删除后所选 ${ids.length} 个条目立即从前台工具页移除，确认删除？`,
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
const actionColumn = {
  title: '操作',
  key: 'actions',
  width: 120,
  align: 'center' as const,
  fixed: 'right' as const,
  render: (row: ToolRow) =>
    h('div', { class: 'cell-btns' }, [
      h(
        NButton,
        {
          size: 'small',
          text: true,
          type: 'primary',
          onClick: () => (activeTab.value === 'catalog' ? openCatalogForm(row) : openToolForm(row))
        },
        { default: () => '编辑' }
      ),
      h(
        NButton,
        { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
        { default: () => '删除' }
      )
    ])
};

const catalogColumns: DataTableColumns<ToolRow> = [
  { title: '目录名称', key: 'toolName', minWidth: 180, ellipsis: { tooltip: true } },
  { title: '排序', key: 'order', width: 70, align: 'center', render: (row) => row.order ?? '—' },
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
  actionColumn
];

const toolColumns: DataTableColumns<ToolRow> = [
  { title: '工具名称', key: 'toolName', minWidth: 150, ellipsis: { tooltip: true } },
  { title: '所属目录', key: 'parentId', width: 110, render: (row) => catalogName(row.parentId) },
  { title: '描述', key: 'describe', minWidth: 180, ellipsis: { tooltip: true }, render: (row) => row.describe || h('span', { class: 'cell-dim' }, '—') },
  { title: '地址', key: 'toolUrl', minWidth: 200, ellipsis: { tooltip: true }, render: (row) => row.toolUrl || h('span', { class: 'cell-dim' }, '—') },
  { title: '排序', key: 'order', width: 70, align: 'center', render: (row) => row.order ?? '—' },
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
  actionColumn
];

// ---------- 新增/编辑（两套表单共用一个 form 状态，打开时按类型初始化） ----------
const catalogFormVisible = ref(false);
const toolFormVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  toolName: '',
  toolUrl: '',
  describe: '',
  icon: '',
  parentId: null as number | null,
  order: 0,
  state: 0
});

const catalogRules: FormRules = {
  toolName: { required: true, message: '请输入目录名称', trigger: 'blur' },
  order: { required: true, type: 'number', message: '请输入排序', trigger: ['blur', 'change'] }
};

const toolRules: FormRules = {
  toolName: { required: true, message: '请输入工具名称', trigger: 'blur' },
  toolUrl: { required: true, message: '请输入工具地址', trigger: 'blur' },
  parentId: { required: true, type: 'number', message: '请选择所属目录', trigger: ['blur', 'change'] },
  order: { required: true, type: 'number', message: '请输入排序', trigger: ['blur', 'change'] }
};

const resetForm = () =>
  Object.assign(form, { toolName: '', toolUrl: '', describe: '', icon: '', parentId: null, order: 0, state: 0 });

const openCatalogForm = async (row?: ToolRow) => {
  editingId.value = row?.id ?? null;
  resetForm();
  catalogFormVisible.value = true;
  if (row) {
    try {
      const detail = await getTool(row.id);
      Object.assign(form, {
        toolName: detail.toolName || '',
        order: detail.order ?? 0,
        state: detail.state ?? 0
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  }
};

const openToolForm = async (row?: ToolRow) => {
  editingId.value = row?.id ?? null;
  resetForm();
  toolFormVisible.value = true;
  if (row) {
    try {
      const detail = await getTool(row.id);
      Object.assign(form, {
        toolName: detail.toolName || '',
        toolUrl: detail.toolUrl || '',
        describe: detail.describe || '',
        icon: detail.icon || '',
        parentId: detail.parentId ?? null,
        order: detail.order ?? 0,
        state: detail.state ?? 0
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  }
};

const handleSave = () => {
  const isCatalog = activeTab.value === 'catalog';
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      // isParent 由所在 tab 固定：目录=1、工具=2（Service 层同步校验/归一）
      const data: Record<string, any> = {
        ...form,
        isParent: isCatalog ? 1 : 2,
        toolUrl: isCatalog ? '' : form.toolUrl,
        parentId: isCatalog ? null : form.parentId
      };
      if (editingId.value) {
        await editTool({ ...data, id: editingId.value });
      } else {
        await addTool({ ...data });
      }
      message.success('保存成功');
      isCatalog ? (catalogFormVisible.value = false) : (toolFormVisible.value = false);
      await activeRefresh();
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

/* 双 tab 零滚动布局（同 navigation 页骨架）：tab 头占一行，两个 pane 各自撑满剩余高度 */
.tool-config-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
}

.tool-config-tabs {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

/* 非 animated 时 pane 直挂 tabs 根下，直接约束 pane（见 qixidi-web-admin 规范） */
.tool-config-tabs :deep(.n-tab-pane) {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-top: 12px;
}

@media (max-width: 768px) {
  .tool-config-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }
}
</style>
