<template>
  <div class="admin-list-page">
    <!-- 搜索卡片：多条件 + 查询/重置按钮 -->
    <n-card :bordered="false" class="alp-search-card">
      <div class="alp-search-form">
        <template v-for="f in searchFields" :key="f.key">
          <!-- 平铺多选框（状态筛选等） -->
          <div v-if="f.type === 'checkbox'" class="alp-field alp-field--check">
            <span class="alp-field-label">{{ f.label }}</span>
            <n-checkbox-group v-model:value="formModel[f.key]">
              <n-space :size="14" :wrap="false">
                <n-checkbox v-for="o in f.options || []" :key="String(o.value)" :value="o.value" :label="o.label" />
              </n-space>
            </n-checkbox-group>
          </div>

          <n-input
            v-else-if="f.type === 'input'"
            v-model:value="formModel[f.key]"
            class="alp-field"
            :style="{ width: fieldWidth(f) }"
            clearable
            :placeholder="f.placeholder || `请输入${f.label}`"
            @keyup.enter="doSearch"
          />

          <n-select
            v-else
            v-model:value="formModel[f.key]"
            class="alp-field"
            :style="{ width: fieldWidth(f) }"
            clearable
            :multiple="f.type === 'multiple'"
            :options="f.options || []"
            :placeholder="f.placeholder || `请选择${f.label}`"
            @keyup.enter="doSearch"
          />
        </template>

        <div class="alp-search-actions">
          <n-button @click="resetSearch">重置</n-button>
          <n-button type="primary" :loading="loading" @click="doSearch">
            <template #icon><n-icon><Search /></n-icon></template>
            查询
          </n-button>
        </div>
      </div>
    </n-card>

    <!-- 列表卡片：撑满剩余高度，滚动只发生在表格内部 -->
    <n-card :bordered="false" class="alp-list-card" :content-style="LIST_CONTENT_STYLE">
      <div v-if="$slots['toolbar-left'] || $slots['toolbar-right']" class="alp-toolbar">
        <div class="alp-toolbar-left"><slot name="toolbar-left" /></div>
        <div class="alp-toolbar-right"><slot name="toolbar-right" /></div>
      </div>

      <n-data-table
        v-model:checked-row-keys="checkedKeys"
        class="alp-table"
        flex-height
        :columns="tableColumns"
        :data="rows"
        :loading="loading"
        :row-key="rowKeyFn"
        :scroll-x="scrollX"
      />

      <div class="alp-footer">
        <span class="alp-footer-total">
          共 {{ total }} 条<template v-if="checkedKeys.length"> · 已选 {{ checkedKeys.length }} 条</template>
        </span>
        <n-pagination
          :page="pageNum"
          :page-size="pageSize"
          :item-count="total"
          :page-sizes="pageSizes"
          show-size-picker
          @update:page="onPageChange"
          @update:page-size="onPageSizeChange"
        />
      </div>
    </n-card>
  </div>
</template>

<script lang="ts">
/** 通用列表页配置类型（搜索字段 / 列） */
export interface AdminSearchOption {
  label: string;
  value: string | number;
}

export interface AdminSearchField {
  /** 查询参数名（原样并入 fetch 参数） */
  key: string;
  label: string;
  type: 'input' | 'select' | 'multiple' | 'checkbox';
  options?: AdminSearchOption[];
  placeholder?: string;
  /** 控件宽度（checkbox 组无效，自适应） */
  width?: number | string;
  defaultValue?: unknown;
}
</script>

<script setup lang="ts">
import type { DataTableColumn, DataTableColumns } from 'naive-ui';
import { Search } from '@vicons/tabler';

interface ListResult<T = any> {
  total: number;
  rows: T[];
}

const props = withDefaults(
  defineProps<{
    /** 搜索字段配置 */
    searchFields: AdminSearchField[];
    /** 表格列（组件按需自动前置选择列 / 序号列） */
    columns: DataTableColumns<any>;
    /** 数据加载：(params) => { total, rows }，params = 分页 + 查询条件快照 */
    fetch: (params: Record<string, any>) => Promise<ListResult>;
    /** 行 key 字段名 */
    rowKey?: string;
    /** 显示复选框列 */
    selectable?: boolean;
    /** 显示序号列（跨页连续） */
    seq?: boolean;
    /** 表格横向滚动宽度（列多时传） */
    scrollX?: number;
    pageSizes?: number[];
    defaultPageSize?: number;
    /** 挂载即加载 */
    immediate?: boolean;
  }>(),
  {
    rowKey: 'id',
    selectable: false,
    seq: false,
    scrollX: undefined,
    pageSizes: () => [10, 20, 50],
    defaultPageSize: 20,
    immediate: true
  }
);

/** 列表卡片 content 布局：n-card 天生 flex column，content 再立 flex 列让表格撑满剩余 */
const LIST_CONTENT_STYLE = { display: 'flex', flexDirection: 'column' as const, minHeight: 0 };

/** 选中行 keys（v-model:checked-keys，配合工具栏批量操作） */
const checkedKeys = defineModel<Array<string | number>>('checkedKeys', { default: () => [] });

// ---------- 搜索表单 ----------
const formModel = reactive<Record<string, any>>(
  Object.fromEntries(props.searchFields.map((f) => [f.key, f.defaultValue ?? (f.type === 'multiple' || f.type === 'checkbox' ? [] : null)]))
);

const fieldWidth = (f: AdminSearchField) => (typeof f.width === 'number' ? `${f.width}px` : f.width);

/** 查询条件快照：翻页沿用最近一次查询的值，输入框实时变化不影响已出结果 */
let querySnapshot: Record<string, any> = {};

const takeSnapshot = () => {
  querySnapshot = {};
  for (const f of props.searchFields) {
    const v = formModel[f.key];
    if (v === null || v === undefined || v === '' || (Array.isArray(v) && !v.length)) continue;
    querySnapshot[f.key] = v;
  }
};

// ---------- 列表数据 ----------
const rows = ref<any[]>([]);
const loading = ref(false);
const total = ref(0);
const pageNum = ref(1);
const pageSize = ref(props.defaultPageSize);

const loadList = async () => {
  loading.value = true;
  try {
    const res = await props.fetch({ pageNum: pageNum.value, pageSize: pageSize.value, ...querySnapshot });
    rows.value = res.rows || [];
    total.value = res.total || 0;
    checkedKeys.value = [];
  } finally {
    loading.value = false;
  }
};

const doSearch = () => {
  takeSnapshot();
  pageNum.value = 1;
  loadList();
};

const resetSearch = () => {
  for (const f of props.searchFields) {
    formModel[f.key] = f.defaultValue ?? (f.type === 'multiple' || f.type === 'checkbox' ? [] : null);
  }
  doSearch();
};

const onPageChange = (page: number) => {
  pageNum.value = page;
  loadList();
};

const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  pageNum.value = 1;
  loadList();
};

// ---------- 列组装：选择列 / 序号列 前置 ----------
const rowKeyFn = (row: any) => row[props.rowKey];

const tableColumns = computed<DataTableColumns<any>>(() => {
  const cols: DataTableColumns<any> = [];
  if (props.selectable) cols.push({ type: 'selection' });
  if (props.seq) {
    cols.push({
      title: '#',
      key: '__seq',
      width: 56,
      align: 'center',
      render: (_row, index) => (pageNum.value - 1) * pageSize.value + index + 1
    } as DataTableColumn<any>);
  }
  return [...cols, ...props.columns];
});

onMounted(() => {
  if (props.immediate) {
    takeSnapshot();
    loadList();
  }
});

defineExpose({
  /** 重新加载当前页（保留查询条件） */
  refresh: loadList,
  /** 重置到第 1 页并重新查询（含条件） */
  reload: () => {
    takeSnapshot();
    pageNum.value = 1;
    return loadList();
  }
});
</script>

<style scoped lang="scss">
/* 外层零滚动：高度 = 视口 - header(56) - 布局 padding(20×2)，内部 flex 分配 */
.admin-list-page {
  height: calc(100vh - 96px);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.alp-search-card {
  flex-shrink: 0;
  border-radius: 10px;
}

.alp-search-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 14px 16px;
}

.alp-field {
  flex-shrink: 0;
}

.alp-field--check {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.alp-field-label {
  font-size: 13px;
  opacity: 0.65;
  white-space: nowrap;
}

.alp-search-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
  flex-shrink: 0;
}

/* n-card 天生 flex column、content 自带 flex:1；content 内再立 flex 列让表格撑满 */
.alp-list-card {
  flex: 1;
  min-height: 0;
  border-radius: 10px;
}

.alp-toolbar {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
  padding-bottom: 12px;
}

.alp-table {
  flex: 1;
  min-height: 0;
}

.alp-footer {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding-top: 12px;
}

.alp-footer-total {
  font-size: 13px;
  opacity: 0.6;
}

@media (max-width: 768px) {
  .admin-list-page {
    height: auto;
    min-height: calc(100vh - 96px);
  }

  .alp-search-actions {
    margin-left: 0;
    width: 100%;
  }
}
</style>
