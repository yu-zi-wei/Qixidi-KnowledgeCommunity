<template>
  <div class="menu-config-page">
    <n-tabs v-model:value="activeTab" type="line" display-directive="show" class="menu-config-tabs">
      <!-- 导航栏 tab -->
      <n-tab-pane name="navigation" tab="导航栏">
        <AdminListPage
          ref="navListRef"
          v-model:checked-keys="navChecked"
          :search-fields="searchFields"
          :columns="navColumns"
          :fetch="fetchNav"
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
              :disabled="!navChecked.length"
              :loading="batchDeleting"
              @click="doDeleteBatch"
            >
              删除所选（{{ navChecked.length }}）
            </n-button>
          </template>
          <template #toolbar-right>
            <n-button type="primary" @click="openForm()">新增导航项</n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>

      <!-- 侧边栏 tab -->
      <n-tab-pane name="sidebar" tab="侧边栏">
        <AdminListPage
          ref="sidebarListRef"
          v-model:checked-keys="sidebarChecked"
          :search-fields="searchFields"
          :columns="sidebarColumns"
          :fetch="fetchSidebar"
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
              :disabled="!sidebarChecked.length"
              :loading="batchDeleting"
              @click="doDeleteBatch"
            >
              删除所选（{{ sidebarChecked.length }}）
            </n-button>
          </template>
          <template #toolbar-right>
            <n-button type="primary" @click="openForm()">新增侧边栏项</n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>
    </n-tabs>

    <!-- 新增/编辑抽屉：两个 tab 共用一套表单，字段名按 activeTab 映射；teleport 组件宽度必须 inline style -->
    <n-drawer v-model:show="formVisible" :width="560" placement="right">
      <n-drawer-content :title="drawerTitle" closable>
        <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
          <n-form-item :label="`${moduleLabel}名称`" path="name">
            <n-input v-model:value="form.name" :placeholder="`请输入${moduleLabel}名称`" :maxlength="30" />
          </n-form-item>
          <n-form-item label="路由地址" path="route">
            <n-input v-model:value="form.route" placeholder="请输入路由地址（如 /articles）" />
          </n-form-item>

          <n-form-item label="图标（SVG 内容，可预览）" path="icon">
            <div class="menu-config-icon-row">
              <span class="menu-config-icon-preview" :title="form.icon ? '当前图标' : '无图标'">
                <span v-if="form.icon" class="menu-config-icon-svg" v-html="form.icon"></span>
                <span v-else class="menu-config-icon-empty">无</span>
              </span>
              <n-input
                v-model:value="form.icon"
                type="textarea"
                :rows="3"
                class="menu-config-icon-input"
                placeholder="粘贴 iconfont 的 SVG 代码（留空表示无图标）"
              />
              <n-button quaternary size="small" type="error" :disabled="!form.icon" @click="form.icon = ''">
                清空
              </n-button>
            </div>
          </n-form-item>
          <n-form-item label="图标颜色" path="iconColor">
            <n-color-picker
              v-model:value="form.iconColor"
              :show-alpha="false"
              :modes="['hex']"
              class="menu-config-color"
              clearable
            />
          </n-form-item>

          <!-- n-grid 子节点必须是 n-gi（grid item）：直接放 n-form-item 会被静默丢弃不渲染 -->
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="排序（小的在前）" path="order">
                <n-input-number v-model:value="form.order" :min="0" class="menu-config-full" />
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item :label="`父级${moduleLabel}`" path="parentId">
                <n-select
                  v-model:value="form.parentId"
                  :options="parentOptions"
                  placeholder="顶级留空"
                  clearable
                  class="menu-config-full"
                />
              </n-form-item>
            </n-gi>
          </n-grid>
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="下拉" path="isList">
                <n-radio-group v-model:value="form.isList">
                  <n-radio :value="0">无下拉</n-radio>
                  <n-radio :value="1">有下拉</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="状态" path="status">
                <n-radio-group v-model:value="form.status">
                  <n-radio :value="0">有效</n-radio>
                  <n-radio :value="1">失效</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
          </n-grid>
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="可见范围" path="jurisdiction">
                <n-radio-group v-model:value="form.jurisdiction">
                  <n-radio :value="0">公开</n-radio>
                  <n-radio :value="1">普通用户</n-radio>
                  <n-radio :value="2">VIP</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="路由类型" path="type">
                <n-radio-group v-model:value="form.type">
                  <n-radio :value="1">主页路由</n-radio>
                  <n-radio :value="2">用户页面</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
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
  </div>
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
  type NavigationRow,
  type SidebarRow
} from '~/composables/useConfigureAdminApi';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getNavigationList, getNavigation, addNavigation, editNavigation, deleteNavigations, getSidebarList, getSidebar, addSidebar, editSidebar, deleteSidebars } =
  useConfigureAdminApi();

// ---------- tab ----------
const activeTab = ref<'navigation' | 'sidebar'>('navigation');
/** 当前 tab 的模块语义（label / 字段名映射） */
const moduleLabel = computed(() => (activeTab.value === 'navigation' ? '导航项' : '侧边栏项'));

// ---------- 搜索（两模块同构，共用配置） ----------
const searchFields: AdminSearchField[] = [
  { key: 'name', label: '名称', type: 'input', placeholder: '请输入名称关键词' },
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
/** 搜索 key 是通用 name，fetch 前按模块固定映射为 navigationName / sidebarName（与 activeTab 无关，避免两个常驻 pane 互相干扰） */
const mapParams = (params: Record<string, any>, nameKey: string) => {
  const { name, ...rest } = params;
  if (name) rest[nameKey] = name;
  return rest;
};
const fetchNav = (params: Record<string, any>) => getNavigationList(mapParams(params, 'navigationName'));
const fetchSidebar = (params: Record<string, any>) => getSidebarList(mapParams(params, 'sidebarName'));

// ---------- 全量数据（父级名称映射 + 父级下拉，条目量小一次拉取） ----------
const navAll = ref<NavigationRow[]>([]);
const sidebarAll = ref<SidebarRow[]>([]);
const loadAll = async () => {
  try {
    const [nav, side] = await Promise.all([
      getNavigationList({ pageNum: 1, pageSize: 200 }),
      getSidebarList({ pageNum: 1, pageSize: 200 })
    ]);
    navAll.value = nav.rows || [];
    sidebarAll.value = side.rows || [];
  } catch {
    /* useApi 已统一弹错 */
  }
};
onMounted(loadAll);

const currentAll = computed(() => (activeTab.value === 'navigation' ? navAll.value : sidebarAll.value));
/** 指定模块列表内查父级名（不依赖 activeTab，两个常驻 pane 各自正确） */
const parentNameIn = (list: Array<NavigationRow | SidebarRow>, id?: number | null) => {
  const hit = list.find((r) => r.id === id);
  return hit ? rowName(hit) : '—';
};
/** 父级下拉：仅有下拉（isList=1）的项可作父级 */
const parentOptions = computed(() =>
  (currentAll.value as Array<NavigationRow | SidebarRow>)
    .filter((r) => r.isList === 1)
    .map((r) => ({ label: rowName(r), value: r.id }))
);

// ---------- 删除（按当前 tab 调对应 API） ----------
const navListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const sidebarListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const navChecked = ref<Array<string | number>>([]);
const sidebarChecked = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const activeListRef = () => (activeTab.value === 'navigation' ? navListRef.value : sidebarListRef.value);
const activeChecked = () => (activeTab.value === 'navigation' ? navChecked.value : sidebarChecked.value);

const runDelete = async (ids: Array<string | number>) => {
  await (activeTab.value === 'navigation' ? deleteNavigations(ids) : deleteSidebars(ids));
  message.success(`已删除 ${ids.length} 个${moduleLabel.value}`);
  await activeListRef()?.refresh();
  await loadAll();
};

const doDelete = (row: NavigationRow | SidebarRow) => {
  dialog.warning({
    title: `删除${moduleLabel.value}`,
    content: `删除后「${rowName(row)}」立即从前台${activeTab.value === 'navigation' ? '导航栏' : '侧边栏'}移除，确认删除？`,
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
    title: `批量删除${moduleLabel.value}`,
    content: `删除后所选 ${ids.length} 个${moduleLabel.value}立即从前台${activeTab.value === 'navigation' ? '导航栏' : '侧边栏'}移除，确认删除？`,
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

const rowName = (row: NavigationRow | SidebarRow) =>
  (row as NavigationRow).navigationName || (row as SidebarRow).sidebarName || String(row.id);

// ---------- 表格列（两模块同构，仅名称/图标字段不同） ----------
const baseColumns = (
  nameKey: 'navigationName' | 'sidebarName',
  iconKey: 'navigationIcon' | 'sidebarIcon',
  allList: () => Array<NavigationRow | SidebarRow>
): DataTableColumns<NavigationRow | SidebarRow> => [
    { title: '名称', key: nameKey, minWidth: 140, ellipsis: { tooltip: true }, render: (row) => rowName(row) },
    { title: '路由地址', key: 'route', minWidth: 140, ellipsis: { tooltip: true } },
    { title: '父级', key: 'parentId', width: 110, render: (row) => (row.parentId ? parentNameIn(allList(), row.parentId) : '—') },
    {
      title: '图标',
      key: iconKey,
      width: 70,
      align: 'center',
      render: (row) => ((row as any)[iconKey] ? '有' : h('span', { class: 'cell-dim' }, '—'))
    },
    {
      title: '图标颜色',
      key: 'iconColor',
      width: 90,
      align: 'center',
      render: (row) =>
        row.iconColor
          ? h('span', { class: 'menu-config-color-dot', style: { background: row.iconColor } }, row.iconColor)
          : h('span', { class: 'cell-dim' }, '—')
    },
    { title: '排序', key: 'order', width: 70, align: 'center' },
    { title: '下拉', key: 'isList', width: 70, align: 'center', render: (row) => (row.isList === 1 ? '有' : '无') },
    {
      title: '可见范围',
      key: 'jurisdiction',
      width: 100,
      render: (row) => h(AdminStateTag, { value: row.jurisdiction, map: CONFIG_JURISDICTION_MAP })
    },
    { title: '状态', key: 'status', width: 80, render: (row) => h(AdminStateTag, { value: row.status, map: CONFIG_STATUS_MAP }) },
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

const navColumns = computed(() => baseColumns('navigationName', 'navigationIcon', () => navAll.value));
const sidebarColumns = computed(() => baseColumns('sidebarName', 'sidebarIcon', () => sidebarAll.value));

// ---------- 新增/编辑（共享表单，字段按 tab 映射） ----------
const formVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  name: '',
  route: '',
  icon: '',
  iconColor: null as string | null,
  order: 0,
  parentId: null as number | null,
  isList: 0,
  status: 0,
  jurisdiction: 0,
  type: 1
});

const drawerTitle = computed(() =>
  editingId.value ? `编辑${moduleLabel.value} · ${form.name}` : `新增${moduleLabel.value}`
);

const rules: FormRules = {
  name: { required: true, message: '请输入名称', trigger: 'blur' },
  route: { required: true, message: '请输入路由地址', trigger: 'blur' },
  order: { required: true, type: 'number', message: '请输入排序', trigger: ['blur', 'change'] },
  isList: { required: true, type: 'number', message: '请选择是否有下拉', trigger: ['blur', 'change'] },
  status: { required: true, type: 'number', message: '请选择状态', trigger: ['blur', 'change'] }
};

const openForm = async (row?: NavigationRow | SidebarRow) => {
  editingId.value = row?.id ?? null;
  formVisible.value = true;
  if (row) {
    try {
      const detail =
        activeTab.value === 'navigation'
          ? await getNavigation(row.id)
          : await getSidebar(row.id);
      Object.assign(form, {
        name: rowName(detail),
        route: detail.route || '',
        icon: ((detail as any)[activeTab.value === 'navigation' ? 'navigationIcon' : 'sidebarIcon']) || '',
        iconColor: detail.iconColor ?? null,
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
      name: '',
      route: '',
      icon: '',
      iconColor: null,
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
      const isNav = activeTab.value === 'navigation';
      const payload: Record<string, any> = {
        route: form.route,
        order: form.order,
        parentId: form.parentId,
        isList: form.isList,
        status: form.status,
        jurisdiction: form.jurisdiction,
        type: form.type
      };
      payload[isNav ? 'navigationName' : 'sidebarName'] = form.name;
      // 清空图标/颜色须传空串：null 会被 MyBatis Plus 非 null 更新策略忽略，旧值清不掉
      payload[isNav ? 'navigationIcon' : 'sidebarIcon'] = form.icon || '';
      payload.iconColor = form.iconColor || '';
      if (editingId.value) payload.id = editingId.value;
      await (isNav
        ? editingId.value
          ? editNavigation(payload)
          : addNavigation(payload)
        : editingId.value
          ? editSidebar(payload)
          : addSidebar(payload));
      message.success('保存成功');
      formVisible.value = false;
      await activeListRef()?.refresh();
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
/* 外层零滚动：tab 头占一行，两个 pane 各自撑满剩余高度 */
.menu-config-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
}

.menu-config-tabs {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

/* 非 animated 时 pane 直挂 tabs 根下，直接约束 pane（见 qixidi-web-admin 规范） */
.menu-config-tabs :deep(.n-tab-pane) {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-top: 12px;
}

.menu-config-full {
  width: 100%;
}

/* 图标编辑行：预览 + SVG 输入 + 清空 */
.menu-config-icon-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  width: 100%;
}

.menu-config-icon-preview {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--hairline, #e0e0e0);
  border-radius: 8px;
}

.menu-config-icon-svg {
  display: inline-flex;
}

.menu-config-icon-svg :deep(svg) {
  width: 22px;
  height: 22px;
}

.menu-config-icon-empty {
  font-size: 12px;
  opacity: 0.4;
}

.menu-config-icon-input {
  flex: 1;
}

.menu-config-color {
  width: 200px;
}

@media (max-width: 768px) {
  .menu-config-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }
}
</style>

<style lang="scss">
/* 列 render 里的颜色示例点（render 元素 scoped 选不中，走页面语义前缀全局类） */
.menu-config-color-dot {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 9999px;
  font-size: 12px;
  color: #fff;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}
</style>
