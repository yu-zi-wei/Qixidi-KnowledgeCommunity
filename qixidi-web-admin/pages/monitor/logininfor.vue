<template>
  <div class="logininfor-page">
    <n-tabs v-model:value="activeTab" type="line" :animated="false">
      <!-- 前台登录日志（默认）：b_sign_log -->
      <n-tab-pane name="front" tab="前台登录日志" display-directive="show:lazy">
        <AdminListPage
          ref="frontListRef"
          v-model:checked-keys="frontCheckedKeys"
          :search-fields="frontSearchFields"
          :columns="frontColumns"
          :fetch="getSignLogs"
          row-key="id"
          seq
          selectable
        >
          <template #toolbar-left>
            <n-button
              size="small"
              type="error"
              secondary
              :disabled="!frontCheckedKeys.length"
              :loading="frontBatchDeleting"
              @click="doFrontDeleteBatch"
            >
              删除所选（{{ frontCheckedKeys.length }}）
            </n-button>
            <n-button size="small" type="error" ghost :loading="frontCleaning" @click="doFrontClean">
              清空日志
            </n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>

      <!-- 后台登录日志：sys_logininfor -->
      <n-tab-pane name="backstage" tab="后台登录日志" display-directive="show:lazy">
        <AdminListPage
          ref="backListRef"
          v-model:checked-keys="backCheckedKeys"
          :search-fields="backSearchFields"
          :columns="backColumns"
          :fetch="getLogininfors"
          row-key="infoId"
          seq
          selectable
        >
          <template #toolbar-left>
            <n-button
              size="small"
              type="error"
              secondary
              :disabled="!backCheckedKeys.length"
              :loading="backBatchDeleting"
              @click="doBackDeleteBatch"
            >
              删除所选（{{ backCheckedKeys.length }}）
            </n-button>
            <n-button size="small" type="error" ghost :loading="backCleaning" @click="doBackClean">
              清空日志
            </n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>
    </n-tabs>
  </div>
</template>

<script setup lang="ts">
import { h, type Ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  LOGIN_STATUS_MAP,
  SIGN_LOG_TYPE_MAP,
  useMonitorAdminApi,
  type LogininforRow,
  type SignLogRow
} from '~/composables/useMonitorAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getLogininfors, deleteLogininfors, cleanLogininfors, getSignLogs, deleteSignLogs, cleanSignLogs } =
  useMonitorAdminApi();

/** 默认展示前台登录日志 */
const activeTab = ref('front');

// ---------- 删除/清空逻辑工厂（两个 tab 同构，仅 API/ref/文案不同） ----------
const makeLogActions = (opts: {
  /** 日志名（弹窗文案用，如「前台登录」） */
  label: string;
  listRef: Ref<InstanceType<typeof AdminListPage> | null>;
  checkedKeys: Ref<Array<string | number>>;
  deleteApi: (ids: Array<string | number>) => Promise<void>;
  cleanApi: () => Promise<void>;
}) => {
  const deletingId = ref<number | null>(null);
  const batchDeleting = ref(false);
  const cleaning = ref(false);

  const runDelete = async (ids: Array<string | number>) => {
    await opts.deleteApi(ids);
    message.success(`已删除 ${ids.length} 条记录`);
    opts.checkedKeys.value = [];
    await opts.listRef.value?.refresh();
  };

  const doDelete = (id: number) => {
    dialog.warning({
      title: `删除${opts.label}记录`,
      content: '删除后不可恢复，确认删除这条登录记录？',
      positiveText: '删除',
      negativeText: '取消',
      onPositiveClick: async () => {
        deletingId.value = id;
        try {
          await runDelete([id]);
        } catch {
          /* useApi 已统一弹错 */
        } finally {
          deletingId.value = null;
        }
      }
    });
  };

  const doDeleteBatch = () => {
    const ids = [...opts.checkedKeys.value];
    dialog.warning({
      title: `批量删除${opts.label}记录`,
      content: `删除后不可恢复，确认删除选中的 ${ids.length} 条登录记录？`,
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
      title: `清空${opts.label}日志`,
      content: `将删除全部${opts.label}记录且不可恢复，确认清空？`,
      positiveText: '清空',
      negativeText: '取消',
      onPositiveClick: async () => {
        cleaning.value = true;
        try {
          await opts.cleanApi();
          message.success(`已清空${opts.label}日志`);
          opts.checkedKeys.value = [];
          await opts.listRef.value?.reload();
        } catch {
          /* useApi 已统一弹错 */
        } finally {
          cleaning.value = false;
        }
      }
    });
  };

  return { deletingId, batchDeleting, cleaning, doDelete, doDeleteBatch, doClean };
};

// ---------- 前台登录日志（b_sign_log） ----------
const frontListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const frontCheckedKeys = ref<Array<string | number>>([]);
const {
  deletingId: frontDeletingId,
  batchDeleting: frontBatchDeleting,
  cleaning: frontCleaning,
  doDelete: doFrontDelete,
  doDeleteBatch: doFrontDeleteBatch,
  doClean: doFrontClean
} = makeLogActions({
  label: '前台登录',
  listRef: frontListRef,
  checkedKeys: frontCheckedKeys,
  deleteApi: deleteSignLogs,
  cleanApi: cleanSignLogs
});

const frontSearchFields: AdminSearchField[] = [
  { key: 'userName', label: '用户名', type: 'input', placeholder: '请输入用户名关键词' },
  { key: 'ipAddress', label: '登录 IP', type: 'input', placeholder: '请输入 IP 关键词' },
  { key: 'type', label: '日志类型', type: 'select', options: [
    { label: '登录', value: 0 },
    { label: '登出', value: 1 }
  ] }
];

const frontColumns: DataTableColumns<SignLogRow> = [
  {
    title: '用户名',
    key: 'userName',
    minWidth: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.userName || '—'
  },
  {
    title: '用户 ID',
    key: 'uid',
    minWidth: 170,
    ellipsis: { tooltip: true },
    render: (row) => row.uid || '—'
  },
  {
    title: '登录 IP',
    key: 'ipAddress',
    width: 130,
    render: (row) => row.ipAddress || '—'
  },
  {
    title: '登录地点',
    key: 'location',
    minWidth: 150,
    ellipsis: { tooltip: true },
    render: (row) => row.location || '—'
  },
  {
    title: '类型',
    key: 'type',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.type ?? '', map: SIGN_LOG_TYPE_MAP })
  },
  {
    title: '时间',
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
          { size: 'small', text: true, type: 'error', loading: frontDeletingId.value === row.id, onClick: () => doFrontDelete(row.id) },
          { default: () => '删除' }
        )
      ])
  }
];

// ---------- 后台登录日志（sys_logininfor，原有功能） ----------
const backListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const backCheckedKeys = ref<Array<string | number>>([]);
const {
  deletingId: backDeletingId,
  batchDeleting: backBatchDeleting,
  cleaning: backCleaning,
  doDelete: doBackDelete,
  doDeleteBatch: doBackDeleteBatch,
  doClean: doBackClean
} = makeLogActions({
  label: '后台登录',
  listRef: backListRef,
  checkedKeys: backCheckedKeys,
  deleteApi: deleteLogininfors,
  cleanApi: cleanLogininfors
});

const backSearchFields: AdminSearchField[] = [
  { key: 'userName', label: '登录账号', type: 'input', placeholder: '请输入账号关键词' },
  { key: 'ipaddr', label: '登录 IP', type: 'input', placeholder: '请输入 IP 关键词' },
  { key: 'status', label: '登录状态', type: 'select', options: [
    { label: '成功', value: '0' },
    { label: '失败', value: '1' }
  ] }
];

const backColumns: DataTableColumns<LogininforRow> = [
  {
    title: '登录账号',
    key: 'userName',
    minWidth: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.userName || '—'
  },
  {
    title: '登录 IP',
    key: 'ipaddr',
    width: 130,
    render: (row) => row.ipaddr || '—'
  },
  {
    title: '登录地点',
    key: 'loginLocation',
    minWidth: 150,
    ellipsis: { tooltip: true },
    render: (row) => row.loginLocation || '—'
  },
  {
    title: '浏览器',
    key: 'browser',
    minWidth: 110,
    ellipsis: { tooltip: true },
    render: (row) => row.browser || '—'
  },
  {
    title: '操作系统',
    key: 'os',
    minWidth: 130,
    ellipsis: { tooltip: true },
    render: (row) => row.os || '—'
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.status ?? '', map: LOGIN_STATUS_MAP })
  },
  {
    title: '提示消息',
    key: 'msg',
    minWidth: 160,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.msg || '' }, row.msg || '—')
  },
  {
    title: '登录时间',
    key: 'loginTime',
    width: 150,
    render: (row) => getFullDateTime(row.loginTime) || '—'
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
          { size: 'small', text: true, type: 'error', loading: backDeletingId.value === row.infoId, onClick: () => doBackDelete(row.infoId) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<style lang="scss">
/*
 * 高度链（页面 → n-tabs → pane → AdminListPage）：pane/admin-list-page 由 Naive/子组件渲染，
 * scoped hash 选不中，统一走非 scoped 页面专属类；
 * AdminListPage 默认 calc(100vh - 88px) 在 tab 内会超界（tab 头额外占高），覆盖为 flex 填充。
 */
.logininfor-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
}

.logininfor-page > .n-tabs {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.logininfor-page .n-tab-pane {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-top: 12px;
}

.logininfor-page .admin-list-page {
  flex: 1;
  min-height: 0;
  /* 覆盖组件默认 calc(100vh - 88px)（含其移动端 auto 分支） */
  height: auto !important;
}

@media (max-width: 768px) {
  .logininfor-page {
    height: auto;
  }
}
</style>
