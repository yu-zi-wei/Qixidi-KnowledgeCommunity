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
    <template #toolbar-right>
      <n-button type="primary" @click="openForm()">新增导航项</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="520" placement="right">
    <n-drawer-content :title="editingId ? `编辑导航项 · ${form.navigationName}` : '新增导航项'" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="导航名称" path="navigationName">
          <n-input v-model:value="form.navigationName" placeholder="请输入导航名称" :maxlength="30" />
        </n-form-item>
        <n-form-item label="路由地址" path="route">
          <n-input v-model:value="form.route" placeholder="请输入路由地址（如 /articles）" />
        </n-form-item>
        <n-form-item label="图标（SVG 内容，一般无需修改）" path="navigationIcon">
          <n-input v-model:value="form.navigationIcon" type="textarea" :rows="2" placeholder="留空表示无图标" />
        </n-form-item>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="排序" path="order">
            <n-input-number v-model:value="form.order" :min="0" class="navigation-full" />
          </n-form-item>
          <n-form-item label="父级项" path="parentId">
            <n-select
              v-model:value="form.parentId"
              :options="parentOptions"
              placeholder="顶级留空"
              clearable
              class="navigation-full"
            />
          </n-form-item>
        </n-grid>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="下拉" path="isList">
            <n-radio-group v-model:value="form.isList">
              <n-radio :value="0">无下拉</n-radio>
              <n-radio :value="1">有下拉</n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-radio-group v-model:value="form.status">
              <n-radio :value="0">有效</n-radio>
              <n-radio :value="1">失效</n-radio>
            </n-radio-group>
          </n-form-item>
        </n-grid>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="可见范围" path="jurisdiction">
            <n-radio-group v-model:value="form.jurisdiction">
              <n-radio :value="0">公开</n-radio>
              <n-radio :value="1">普通用户</n-radio>
              <n-radio :value="2">VIP</n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="路由类型" path="type">
            <n-radio-group v-model:value="form.type">
              <n-radio :value="1">主页路由</n-radio>
              <n-radio :value="2">用户页面</n-radio>
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
import {
  CONFIG_JURISDICTION_MAP,
  CONFIG_STATUS_MAP,
  useConfigureAdminApi,
  type NavigationRow
} from '~/composables/useConfigureAdminApi';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getNavigationList, getNavigation, addNavigation, editNavigation, deleteNavigations } =
  useConfigureAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'navigationName', label: '导航名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'statuses',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '有效', value: 0 },
      { label: '失效', value: 1 }
    ]
  }
];

const fetch = (params: Record<string, any>) => getNavigationList(params);

// ---------- 全量导航（父级名称映射 + 父级下拉；导航项量小，一次拉取） ----------
const allRows = ref<NavigationRow[]>([]);
const loadAll = async () => {
  try {
    const res = await getNavigationList({ pageNum: 1, pageSize: 200 });
    allRows.value = res.rows || [];
  } catch {
    /* useApi 已统一弹错 */
  }
};
onMounted(loadAll);

const parentName = (id?: number | null) => allRows.value.find((r) => r.id === id)?.navigationName || '—';
const parentOptions = computed(() =>
  allRows.value
    .filter((r) => r.isList === 1)
    .map((r) => ({ label: r.navigationName || String(r.id), value: r.id }))
);

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteNavigations(ids);
  message.success(`已删除 ${ids.length} 个导航项`);
  await listRef.value?.refresh();
  await loadAll();
};

const doDelete = (row: NavigationRow) => {
  dialog.warning({
    title: '删除导航项',
    content: `删除后「${row.navigationName}」立即从前台导航栏移除，确认删除？`,
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
    title: '批量删除导航项',
    content: `删除后所选 ${ids.length} 个导航项立即从前台导航栏移除，确认删除？`,
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
const columns: DataTableColumns<NavigationRow> = [
  { title: '导航名称', key: 'navigationName', minWidth: 140, ellipsis: { tooltip: true } },
  { title: '路由地址', key: 'route', minWidth: 150, ellipsis: { tooltip: true } },
  { title: '父级', key: 'parentId', width: 110, render: (row) => (row.parentId ? parentName(row.parentId) : '—') },
  {
    title: '图标',
    key: 'navigationIcon',
    width: 80,
    align: 'center',
    render: (row) => (row.navigationIcon ? '有' : h('span', { class: 'cell-dim' }, '—'))
  },
  { title: '排序', key: 'order', width: 70, align: 'center' },
  {
    title: '下拉',
    key: 'isList',
    width: 80,
    align: 'center',
    render: (row) => (row.isList === 1 ? '有' : '无')
  },
  {
    title: '可见范围',
    key: 'jurisdiction',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.jurisdiction, map: CONFIG_JURISDICTION_MAP })
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.status, map: CONFIG_STATUS_MAP })
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
  navigationName: '',
  route: '',
  navigationIcon: '',
  order: 0,
  parentId: null as number | null,
  isList: 0,
  status: 0,
  jurisdiction: 0,
  type: 1
});

const rules: FormRules = {
  navigationName: { required: true, message: '请输入导航名称', trigger: 'blur' },
  route: { required: true, message: '请输入路由地址', trigger: 'blur' },
  order: { required: true, type: 'number', message: '请输入排序', trigger: ['blur', 'change'] },
  isList: { required: true, type: 'number', message: '请选择是否有下拉', trigger: ['blur', 'change'] },
  status: { required: true, type: 'number', message: '请选择状态', trigger: ['blur', 'change'] }
};

const openForm = async (row?: NavigationRow) => {
  editingId.value = row?.id ?? null;
  formVisible.value = true;
  if (row) {
    try {
      const detail = await getNavigation(row.id);
      Object.assign(form, {
        navigationName: detail.navigationName || '',
        route: detail.route || '',
        navigationIcon: detail.navigationIcon || '',
        order: detail.order ?? 0,
        parentId: detail.parentId ?? null,
        isList: detail.isList ?? 0,
        status: detail.status ?? 0,
        jurisdiction: detail.jurisdiction ?? 0,
        type: detail.type ?? 1
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  } else {
    Object.assign(form, {
      navigationName: '',
      route: '',
      navigationIcon: '',
      order: 0,
      parentId: null,
      isList: 0,
      status: 0,
      jurisdiction: 0,
      type: 1
    });
  }
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      if (editingId.value) {
        await editNavigation({ ...form, id: editingId.value });
      } else {
        await addNavigation({ ...form });
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
.navigation-full {
  width: 100%;
}
</style>
