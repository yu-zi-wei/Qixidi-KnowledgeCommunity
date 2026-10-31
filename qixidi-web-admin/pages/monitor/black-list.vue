<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getBlackList"
    row-key="id"
    seq
    selectable
  >
    <template #toolbar-left>
      <n-button size="small" type="primary" secondary @click="openAdd">手动加黑</n-button>
      <n-button
        size="small"
        type="error"
        secondary
        :disabled="!checkedKeys.length"
        :loading="batchDeleting"
        @click="doDeleteBatch"
      >
        移出所选（{{ checkedKeys.length }}）
      </n-button>
    </template>
  </AdminListPage>

  <!-- 详情抽屉：黑名单无单查接口，行数据即全量（teleport 组件宽度必须 inline style） -->
  <n-drawer v-model:show="detailVisible" :width="480" placement="right">
    <n-drawer-content :title="`黑名单 #${detail?.id ?? ''}`" closable>
      <div v-if="detail" class="detail-body">
        <div class="detail-meta">
          <div class="meta-row"><span class="meta-label">IP</span><span class="cell-mono">{{ detail.ip || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">归属地</span><span>{{ detail.homePlace || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">UID</span><span class="cell-mono">{{ detail.uid || '—' }}</span></div>
          <div class="meta-row"><span class="meta-label">拉黑时间</span><span>{{ getFullDateTime(detail.createTime) || '—' }}</span></div>
        </div>
        <n-divider>拉黑原因</n-divider>
        <pre class="detail-pre">{{ formatDetail(detail.detail) }}</pre>
      </div>
    </n-drawer-content>
  </n-drawer>

  <!-- 手动加黑弹窗（teleport 组件宽度必须 inline style；内容样式走非 scoped 块） -->
  <n-modal v-model:show="addVisible" :style="{ width: '420px' }" class="black-add-modal-host" preset="card" title="手动加黑">
    <n-form label-placement="top" size="small">
      <n-form-item label="IP 地址">
        <n-input v-model:value="addForm.ip" placeholder="与 UID 至少填一项" clearable />
      </n-form-item>
      <n-form-item label="用户 UID">
        <n-input v-model:value="addForm.uid" placeholder="可选" clearable />
      </n-form-item>
      <n-form-item label="备注">
        <n-input v-model:value="addForm.detail" type="textarea" :rows="2" placeholder="拉黑原因（可选）" />
      </n-form-item>
    </n-form>
    <p class="black-add-tip">加入后该 IP/UID 的请求将被立即拦截；自动拉黑的防刷记录也会出现在列表中。</p>
    <template #footer>
      <div class="black-add-footer">
        <n-button size="small" @click="addVisible = false">取消</n-button>
        <n-button size="small" type="primary" :loading="adding" :disabled="!addForm.ip && !addForm.uid" @click="doAdd">
          确认加黑
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import { useBlackListAdminApi, type BlackListRow } from '~/composables/useBlackListAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getBlackList, addBlack, deleteBlack } = useBlackListAdminApi();

// ---------- 搜索（ip like / uid eq） ----------
const searchFields: AdminSearchField[] = [
  { key: 'ip', label: 'IP 地址', type: 'input', placeholder: '请输入 IP 关键词' },
  { key: 'uid', label: '用户 UID', type: 'input', placeholder: '请输入完整 UID' }
];

// ---------- 移出黑名单 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteBlack(ids);
  message.success(`已移出 ${ids.length} 条黑名单`);
  await listRef.value?.refresh();
};

const doDelete = (row: BlackListRow) => {
  dialog.warning({
    title: '移出黑名单',
    content: `移出后「${row.ip || row.uid}」的请求恢复放行，确认移出？`,
    positiveText: '移出',
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
    title: '批量移出黑名单',
    content: `移出后对应 IP/UID 的请求恢复放行，确认移出选中的 ${ids.length} 条？`,
    positiveText: '移出',
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

// ---------- 手动加黑 ----------
const addVisible = ref(false);
const adding = ref(false);
const addForm = ref({ ip: '', uid: '', detail: '' });

const openAdd = () => {
  addForm.value = { ip: '', uid: '', detail: '' };
  addVisible.value = true;
};

const doAdd = async () => {
  adding.value = true;
  try {
    await addBlack({
      ip: addForm.value.ip || undefined,
      uid: addForm.value.uid || undefined,
      detail: addForm.value.detail || undefined
    });
    message.success('已加入黑名单并生效');
    addVisible.value = false;
    await listRef.value?.reload();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    adding.value = false;
  }
};

// ---------- 详情抽屉 ----------
const detailVisible = ref(false);
const detail = ref<BlackListRow | null>(null);
const openDetail = (row: BlackListRow) => {
  detail.value = row;
  detailVisible.value = true;
};

/** 自动拉黑存 URL 计数 JSON，格式化展示；手动备注原样返回 */
const formatDetail = (text?: string) => {
  if (!text) return '—';
  try {
    return JSON.stringify(JSON.parse(text), null, 2);
  } catch {
    return text;
  }
};

// ---------- 表格列（render 元素样式走全局类，scoped 选不中） ----------
const columns: DataTableColumns<BlackListRow> = [
  {
    title: 'IP 地址',
    key: 'ip',
    width: 150,
    render: (row) =>
      // IP 即详情入口；样式类在 admin-table.scss
      h('span', { class: 'cell-ellipsis is-link cell-mono', onClick: () => openDetail(row), title: '查看详情' }, row.ip || '—')
  },
  {
    title: '归属地',
    key: 'homePlace',
    width: 150,
    ellipsis: { tooltip: true },
    render: (row) => row.homePlace || '—'
  },
  {
    title: '用户 UID',
    key: 'uid',
    width: 160,
    render: (row) => h('span', { class: 'cell-mono' }, row.uid || '—')
  },
  {
    title: '拉黑原因',
    key: 'detail',
    minWidth: 200,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.detail || '' }, row.detail || '—')
  },
  {
    title: '拉黑时间',
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
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.id, onClick: () => doDelete(row) },
          { default: () => '移出' }
        )
      ])
  }
];
</script>

<style scoped lang="scss">
.detail-body {
  .detail-meta {
    display: flex;
    flex-direction: column;
    gap: 10px;
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

  .detail-pre {
    margin: 0;
    padding: 10px;
    font-size: 12px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
    background: rgba(127, 127, 127, 0.08);
    border-radius: 8px;
    max-height: 300px;
    overflow-y: auto;
  }
}
</style>

<!-- n-modal teleport 到 body，scoped 选不中：加黑弹窗样式放非 scoped 块（页面前缀防跨页覆盖） -->
<style lang="scss">
.black-add-modal-host {
  .black-add-tip {
    margin: 4px 0 0;
    font-size: 12px;
    opacity: 0.55;
  }

  .black-add-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
