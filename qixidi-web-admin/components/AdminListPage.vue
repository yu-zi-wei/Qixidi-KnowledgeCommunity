<template>
  <div class="admin-list-page">
    <!-- 搜索区：书签纸片（--panel-bg + 暖灰细边框，--hairline 由布局根注入） -->
    <div class="alp-search-card">
      <div class="alp-search-form">
        <!-- 字段统一「label + 控件」结构（参考图样式），下拉可配多选 -->
        <div v-for="f in searchFields" :key="f.key" class="alp-field">
          <span class="alp-field-label">{{ f.label }}</span>
          <n-input
            v-if="f.type === 'input'"
            v-model:value="formModel[f.key]"
            :style="{ width: fieldWidth(f) }"
            clearable
            :placeholder="f.placeholder || `请输入${f.label}`"
            @keyup.enter="doSearch"
          />
          <n-select
            v-else
            v-model:value="formModel[f.key]"
            :style="{ width: fieldWidth(f) }"
            clearable
            multiple
            max-tag-count="responsive"
            :options="f.options || []"
            :placeholder="f.placeholder || `请选择${f.label}`"
            @keyup.enter="doSearch"
          />
        </div>

        <div class="alp-search-actions">
          <n-button @click="resetSearch">重置</n-button>
          <n-button type="primary" :loading="loading" @click="doSearch">
            <template #icon><n-icon><Search /></n-icon></template>
            查询
          </n-button>
        </div>
      </div>
    </div>

    <!-- 列表区：书签纸片撑满剩余高度，滚动只发生在表格内部 -->
    <div class="alp-list-card">
      <div v-if="$slots['toolbar-left'] || $slots['toolbar-right']" class="alp-toolbar">
        <div class="alp-toolbar-left"><slot name="toolbar-left" /></div>
        <div class="alp-toolbar-right"><slot name="toolbar-right" /></div>
      </div>

      <n-data-table
        v-model:checked-row-keys="checkedKeys"
        class="alp-table"
        flex-height
        size="small"
        :columns="tableColumns"
        :data="rows"
        :loading="loading"
        :row-key="rowKeyFn"
        :scroll-x="scrollX ?? autoScrollX"
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
    </div>
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
  /** input 输入框 / multiple 多选下拉（GET 数组 key=a&key=b，后端 BO List 字段 + in 条件） */
  type: 'input' | 'multiple';
  options?: AdminSearchOption[];
  placeholder?: string;
  /** 控件宽度（label 之外的部分）；不配则统一默认宽 160px */
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
    /** 表格横向滚动宽度；不传则自动按列宽（width/minWidth）合计，容器更窄时表格内部出横向滚动条 */
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
    pageSizes: () => [50, 100, 200],
    defaultPageSize: 50,
    immediate: true
  }
);

/** 列表区布局：透明容器自带 flex 列（去卡片化后不再依赖 n-card 的 content 结构） */

/** 选中行 keys（v-model:checked-keys，配合工具栏批量操作） */
const checkedKeys = defineModel<Array<string | number>>('checkedKeys', { default: () => [] });

// ---------- 搜索表单 ----------
const formModel = reactive<Record<string, any>>(
  Object.fromEntries(props.searchFields.map((f) => [f.key, f.defaultValue ?? (f.type === 'input' ? null : [])]))
);

/** 控件统一默认宽度（不配 width 即等宽，需差异化时才在字段配置里覆盖） */
const DEFAULT_FIELD_WIDTH = '200px';
const fieldWidth = (f: AdminSearchField) => (typeof f.width === 'number' ? `${f.width}px` : f.width || DEFAULT_FIELD_WIDTH);

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
    formModel[f.key] = f.defaultValue ?? (f.type === 'multiple' ? [] : null);
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

/** 默认横向滚动宽度：列宽合计（含前置选择/序号列），小屏容器更窄时表格内部横向滚动而非压缩列。
 *  含 minWidth 自适应列时不传——scroll-x 会把表格总宽钉死在合计值，容器更宽时 minWidth 列无法拉伸填满（实测留白 300px+）；
 *  此时小屏保护由列自身 minWidth 承担（表格容器 overflow-x 出滚动条） */
const autoScrollX = computed<number | undefined>(() => {
  let sum = 0;
  let hasFlex = false;
  for (const col of tableColumns.value as any[]) {
    if (col.type === 'selection') {
      sum += 50; // Naive 选择列无 width 定义，按渲染实际宽度估
      continue;
    }
    if (typeof col.width !== 'number' && typeof col.minWidth === 'number') hasFlex = true;
    const w = col.width ?? col.minWidth;
    if (typeof w === 'number') sum += w;
  }
  return sum > 0 && !hasFlex ? sum : undefined;
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
/* 外层零滚动：高度 = 视口 - header(56) - 布局 padding(16×2)，内部 flex 分配 */
.admin-list-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 搜索区：书签纸片（无边框，靠底色明度差 + 轻投影分层） */
.alp-search-card {
  flex-shrink: 0;
  padding: 14px 16px;
  background: var(--panel-bg);
  border-radius: 10px;
  box-shadow: var(--panel-shadow, 0 1px 3px rgba(180, 170, 150, 0.18));
}

.alp-search-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 14px 16px;
}

/* 搜索字段：label 在前、控件在后（参考图样式） */
.alp-field {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
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

/* 列表区：书签纸片撑满剩余高度，滚动只在表格内部（无边框，同搜索区） */
.alp-list-card {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 12px 16px 14px;
  background: var(--panel-bg);
  border-radius: 10px;
  box-shadow: var(--panel-shadow, 0 1px 3px rgba(180, 170, 150, 0.18));
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

/* 左侧批量操作 / 右侧新增编辑等，各自可放多个元素 */
.alp-toolbar-left,
.alp-toolbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.alp-table {
  flex: 1;
  min-height: 0;
}

/* footer 靠右：「共 N 条」紧挨分页组件在前 */
.alp-footer {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 16px;
  padding-top: 12px;
}

/* 表头加粗；正文列与 footer 文字一律正常浓度（不做浅色弱化） */
.alp-table :deep(.n-data-table-th) {
  font-weight: 600;
}

.alp-footer-total {
  font-size: 13px;
}

@media (max-width: 768px) {
  .admin-list-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }

  .alp-search-actions {
    margin-left: 0;
    width: 100%;
  }
}
</style>
