<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getOperLogs"
    row-key="operId"
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
      <n-button size="small" type="error" ghost :loading="cleaning" @click="doClean">清空日志</n-button>
    </template>
  </AdminListPage>

  <!-- 详情抽屉：日志无单查接口，列表行已含全量字段（teleport 组件宽度必须 inline style） -->
  <n-drawer v-model:show="detailVisible" :width="560" placement="right">
    <n-drawer-content :title="`操作日志 #${detail?.operId ?? ''}`" closable>
      <div v-if="detail" class="detail-body">
        <div class="detail-head">
          <AdminStateTag :value="detail.businessType" :map="OPER_BIZ_TYPE_MAP" />
          <AdminStateTag :value="detail.status" :map="OPER_STATUS_MAP" />
        </div>

        <div class="detail-meta">
          <div class="meta-row"><span class="meta-label">操作模块</span><span>{{ detail.module || detail.title || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">操作功能</span><span>{{ detail.title || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">操作人员</span><span>{{ detail.operName || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">请求方式</span><span>{{ detail.requestMethod || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">操作 IP</span><span>{{ detail.operIp || '—' }}{{ detail.operLocation ? `（${detail.operLocation}）` : '' }}</span></div>
          <div class="meta-row"><span class="meta-label">操作时间</span><span>{{ getFullDateTime(detail.operTime) || '—' }}</span></div>
          <div class="meta-row meta-url"><span class="meta-label">请求地址</span><span class="meta-break">{{ detail.operUrl || '—' }}</span></div>
        </div>

        <n-divider>调用方法</n-divider>
        <pre class="detail-pre">{{ detail.method || '—' }}</pre>

        <template v-if="detail.operParam">
          <n-divider>请求参数</n-divider>
          <pre class="detail-pre">{{ formatJson(detail.operParam) }}</pre>
        </template>

        <template v-if="detail.jsonResult">
          <n-divider>返回结果</n-divider>
          <pre class="detail-pre">{{ formatJson(detail.jsonResult) }}</pre>
        </template>

        <template v-if="detail.errorMsg">
          <n-divider>错误消息</n-divider>
          <pre class="detail-pre detail-error">{{ detail.errorMsg }}</pre>
        </template>
      </div>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { OPER_BIZ_TYPE_MAP, OPER_STATUS_MAP, useMonitorAdminApi, type OperLogRow } from '~/composables/useMonitorAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getOperLogs, deleteOperLogs, cleanOperLogs } = useMonitorAdminApi();

// ---------- 搜索（module/title/operName 后端 like；businessType/status eq） ----------
const searchFields: AdminSearchField[] = [
  { key: 'module', label: '操作模块', type: 'input', placeholder: '请输入模块关键词' },
  { key: 'title', label: '操作功能', type: 'input', placeholder: '请输入功能关键词' },
  { key: 'operName', label: '操作人员', type: 'input', placeholder: '请输入人员账号' },
  { key: 'businessType', label: '业务类型', type: 'select', options: [
    { label: '新增', value: 1 },
    { label: '修改', value: 2 },
    { label: '删除', value: 3 },
    { label: '授权', value: 4 },
    { label: '导出', value: 5 },
    { label: '强退', value: 7 },
    { label: '清空', value: 9 }
  ] },
  { key: 'status', label: '操作状态', type: 'select', options: [
    { label: '正常', value: 0 },
    { label: '异常', value: 1 }
  ] }
];

// ---------- 删除/清空（批量/单条/全量共用删除接口，清空走 /clean） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);
const cleaning = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteOperLogs(ids);
  message.success(`已删除 ${ids.length} 条日志`);
  await listRef.value?.refresh();
};

const doDelete = (row: OperLogRow) => {
  dialog.warning({
    title: '删除日志',
    content: '删除后不可恢复，确认删除这条操作日志？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.operId;
      try {
        await runDelete([row.operId]);
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
    title: '批量删除日志',
    content: `删除后不可恢复，确认删除选中的 ${ids.length} 条操作日志？`,
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

const doClean = () => {
  dialog.warning({
    title: '清空操作日志',
    content: '将删除全部操作日志且不可恢复，确认清空？',
    positiveText: '清空',
    negativeText: '取消',
    onPositiveClick: async () => {
      cleaning.value = true;
      try {
        await cleanOperLogs();
        message.success('已清空操作日志');
        await listRef.value?.reload();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        cleaning.value = false;
      }
    }
  });
};

// ---------- 详情抽屉（日志无单查接口，行数据即全量；打开前清空防残留） ----------
const detailVisible = ref(false);
const detail = ref<OperLogRow | null>(null);
const openDetail = (row: OperLogRow) => {
  detail.value = row;
  detailVisible.value = true;
};

/** param/result 存的是 JSON 字符串，格式化展示；失败原样返回 */
const formatJson = (text: string) => {
  try {
    return JSON.stringify(JSON.parse(text), null, 2);
  } catch {
    return text;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<OperLogRow> = [
  {
    // 模块即详情入口；过渡期旧行 module 为空时兜底显示 title（旧值即模块名）
    title: '操作模块',
    key: 'module',
    minWidth: 140,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        h('div', { class: 'cell-comment-content cell-clamp-2 is-link', onClick: () => openDetail(row), title: '查看详情' }, row.module || row.title || '—'),
        h('div', { class: 'cell-comment-sub' }, row.operUrl || '')
      ])
  },
  {
    title: '操作功能',
    key: 'title',
    width: 100,
    render: (row) => row.title || '—'
  },
  {
    title: '业务类型',
    key: 'businessType',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.businessType ?? 0, map: OPER_BIZ_TYPE_MAP })
  },
  {
    title: '操作人员',
    key: 'operName',
    width: 110,
    ellipsis: { tooltip: true },
    render: (row) => row.operName || '—'
  },
  {
    title: '请求方式',
    key: 'requestMethod',
    width: 80,
    align: 'center',
    render: (row) => h('span', { class: 'monitor-operlog-request' }, row.requestMethod || '—')
  },
  {
    title: '操作 IP',
    key: 'operIp',
    width: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.operIp || '—'
  },
  {
    title: '操作地点',
    key: 'operLocation',
    width: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.operLocation || '—'
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.status, map: OPER_STATUS_MAP })
  },
  {
    title: '操作时间',
    key: 'operTime',
    width: 150,
    render: (row) => getFullDateTime(row.operTime) || '—'
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
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.operId, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<style scoped lang="scss">
.detail-body {
  .detail-head {
    display: flex;
    gap: 8px;
  }

  .detail-meta {
    display: flex;
    flex-direction: column;
    gap: 10px;
    margin-top: 14px;
  }

  .meta-row {
    display: flex;
    align-items: baseline;
    gap: 12px;
    font-size: 13px;
  }

  .meta-label {
    width: 60px;
    flex-shrink: 0;
    opacity: 0.5;
  }

  .meta-url .meta-break {
    word-break: break-all;
  }

  .detail-pre {
    margin: 0;
    padding: 10px;
    font-size: 12px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
    background: rgba(127, 127, 127, 0.08);
    border-radius: 8px;
    max-height: 260px;
    overflow-y: auto;
  }

  .detail-error {
    // 抽屉 teleport 到 body 主题变量不可用，半透明红双主题可辨
    color: rgba(224, 82, 110, 0.9);
  }
}
</style>

<!-- 请求方式徽标：表格 render 上下文 scoped 选不中，放非 scoped 块（中性底双主题通用，文字走布局根 --cell-link） -->
<style lang="scss">
.monitor-operlog-request {
  display: inline-block;
  padding: 1px 8px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.5px;
  border-radius: 4px;
  color: var(--cell-link, #3d7a5c);
  background: rgba(127, 127, 127, 0.12);
}
</style>
