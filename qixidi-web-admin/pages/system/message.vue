<template>
  <div class="message-page">
    <AdminListPage
      ref="listRef"
      v-model:checked-keys="checked"
      :search-fields="searchFields"
      :columns="columns"
      :fetch="fetchList"
      row-key="id"
      seq
      selectable
    >
      <template #toolbar-left>
        <n-button
          size="small"
          type="error"
          secondary
          :disabled="!checked.length"
          :loading="batchDeleting"
          @click="doDeleteBatch"
        >
          删除所选（{{ checked.length }}）
        </n-button>
      </template>
      <template #toolbar-right>
        <n-button type="primary" @click="openForm()">发新消息</n-button>
      </template>
    </AdminListPage>

    <!-- 详情弹窗：teleport 组件宽度必须 inline style -->
    <n-modal v-model:show="detailVisible" :style="{ width: '560px' }" preset="card" title="消息详情">
      <template v-if="detail">
        <div class="message-detail-meta">
          <AdminStateTag :value="detail.state" :map="MESSAGE_STATE_MAP" />
          <span>{{ getFullDateTime(detail.createTime) }}</span>
        </div>
        <h4 class="message-detail-title">{{ detail.newsTitle }}</h4>
        <p class="message-detail-content">{{ detail.newsContent }}</p>
      </template>
      <n-empty v-else description="加载中…" />
    </n-modal>

    <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
    <n-drawer v-model:show="formVisible" :width="520" placement="right">      <n-drawer-content :title="editingId ? '编辑系统消息' : '发送系统消息'" closable>
        <n-alert type="info" :bordered="false" class="message-tip">
          保存后立即对全体用户生效，在线用户会收到新消息提醒。
        </n-alert>
        <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
          <n-form-item label="标题" path="newsTitle">
            <n-input v-model:value="form.newsTitle" placeholder="请输入消息标题" :maxlength="100" />
          </n-form-item>
          <n-form-item label="内容" path="newsContent">
            <n-input
              v-model:value="form.newsContent"
              type="textarea"
              placeholder="请输入消息内容"
              :rows="8"
              :maxlength="2000"
              show-count
            />
          </n-form-item>
        </n-form>
        <template #footer>
          <n-space>
            <n-button @click="formVisible = false">取消</n-button>
            <n-button type="primary" :loading="saving" @click="handleSave">保存并发布</n-button>
          </n-space>
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { h, reactive, ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  MESSAGE_STATE_MAP,
  useSystemAdminApi,
  type SystemMessageRow
} from '~/composables/useSystemAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const {
  getSystemMessageList,
  getSystemMessage,
  addSystemMessage,
  editSystemMessage,
  updateMessageState,
  deleteSystemMessages
} = useSystemAdminApi();

const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checked = ref<Array<string | number>>([]);

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'newsTitle', label: '标题', type: 'input', placeholder: '请输入标题关键词' },
  {
    key: 'states',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '生效中', value: 0 },
      { label: '已失效', value: 1 }
    ]
  }
];

const fetchList = (params: Record<string, any>) => {
  const { states, ...rest } = params;
  return getSystemMessageList({ ...rest, state: states?.[0] });
};

// ---------- 删除 ----------
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteSystemMessages(ids);
  message.success(`已删除 ${ids.length} 条系统消息`);
  await listRef.value?.refresh();
};

const doDelete = (row: SystemMessageRow) => {
  dialog.warning({
    title: '删除确认',
    content: `删除后「${row.newsTitle}」立即从所有用户的消息列表移除且不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.id!;
      try {
        await runDelete([row.id!]);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        deletingId.value = null;
      }
    }
  });
};

const doDeleteBatch = () => {
  const ids = [...checked.value];
  dialog.warning({
    title: '批量删除',
    content: `删除后所选 ${ids.length} 条系统消息立即从所有用户的消息列表移除且不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      batchDeleting.value = true;
      try {
        await runDelete(ids);
        checked.value = [];
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        batchDeleting.value = false;
      }
    }
  });
};

// ---------- 失效/恢复 ----------
const operatingId = ref<number | null>(null);

const doToggleState = (row: SystemMessageRow) => {
  const toFail = row.state === 0;
  const confirm = toFail
    ? dialog.warning({
        title: '失效确认',
        content: `失效后「${row.newsTitle}」立即从所有用户的消息列表隐藏，恢复后重新可见，确认失效？`,
        positiveText: '失效',
        negativeText: '取消'
      })
    : dialog.info({
        title: '恢复确认',
        content: `恢复后「${row.newsTitle}」重新对全体用户可见，确认恢复？`,
        positiveText: '恢复',
        negativeText: '取消'
      });
  confirm.onPositiveClick(async () => {
    operatingId.value = row.id!;
    try {
      await updateMessageState(row.id!, toFail ? 1 : 0);
      message.success(toFail ? '已失效' : '已恢复');
      await listRef.value?.refresh();
    } catch {
      /* useApi 已统一弹错 */
    } finally {
      operatingId.value = null;
    }
  });
};

// ---------- 表格列 ----------
const columns: DataTableColumns<SystemMessageRow> = [
  {
    title: '标题',
    key: 'newsTitle',
    minWidth: 200,
    render: (row) =>
      h(
        'span',
        { class: 'cell-ellipsis is-link', onClick: () => openDetail(row), title: '查看详情' },
        row.newsTitle || '—'
      )
  },
  {
    title: '内容',
    key: 'newsContent',
    minWidth: 260,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.newsContent }, row.newsContent || '—')
  },
  {
    title: '状态',
    key: 'state',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.state, map: MESSAGE_STATE_MAP })
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
    width: 170,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          { size: 'small', text: true, type: 'primary', onClick: () => openForm(row) },
          { default: () => '编辑' }
        ),
        h(
          NButton,
          {
            size: 'small',
            text: true,
            type: row.state === 0 ? 'warning' : 'primary',
            loading: operatingId.value === row.id,
            onClick: () => doToggleState(row)
          },
          { default: () => (row.state === 0 ? '失效' : '恢复') }
        ),
        h(
          NButton,
          {
            size: 'small',
            text: true,
            type: 'error',
            loading: deletingId.value === row.id,
            onClick: () => doDelete(row)
          },
          { default: () => '删除' }
        )
      ])
  }
];

// ---------- 详情查看（标题点击） ----------
const detailVisible = ref(false);
const detail = ref<SystemMessageRow | null>(null);

const openDetail = async (row: SystemMessageRow) => {
  detail.value = null;
  detailVisible.value = true;
  try {
    detail.value = await getSystemMessage(row.id!);
  } catch {
    /* useApi 已统一弹错 */
  }
};

// ---------- 新增/编辑 ----------
const formVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  newsTitle: '',
  newsContent: ''
});

const rules: FormRules = {
  newsTitle: { required: true, message: '请输入消息标题', trigger: 'blur' },
  newsContent: { required: true, message: '请输入消息内容', trigger: 'blur' }
};

const openForm = async (row?: SystemMessageRow) => {
  editingId.value = row?.id ?? null;
  Object.assign(form, { newsTitle: '', newsContent: '' });
  formVisible.value = true;
  if (row) {
    try {
      const d = await getSystemMessage(row.id!);
      Object.assign(form, {
        newsTitle: d.newsTitle || '',
        newsContent: d.newsContent || ''
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  }
};

const handleSave = () => {
  formRef.value?.validate((errors) => {
    if (errors) return;
    dialog.info({
      title: '发布确认',
      content: editingId.value
        ? '保存后立即对全体用户生效，确认提交？'
        : '发布后立即对全体用户可见并推送提醒，确认发布？',
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        saving.value = true;
        try {
          // 后端固化群发：type=2 广播 / isDetails=1 无详情页（BO AddGroup 必填项）
          const body = {
            ...form,
            type: 2,
            isDetails: 1,
            ...(editingId.value ? { id: editingId.value } : {})
          };
          if (editingId.value) {
            await editSystemMessage(body);
          } else {
            await addSystemMessage(body);
          }
          message.success(editingId.value ? '已保存并生效' : '已发布，全体用户可见');
          formVisible.value = false;
          await listRef.value?.refresh();
        } catch {
          /* useApi 已统一弹错 */
        } finally {
          saving.value = false;
        }
      }
    });
  });
};
</script>

<style scoped lang="scss">
.message-page {
  height: calc(100vh - 88px);
}

.message-tip {
  margin-bottom: 16px;
}

.message-detail-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 13px;
  opacity: 0.75;
}

.message-detail-title {
  margin: 0 0 12px;
  font-size: 16px;
  font-weight: 600;
}

.message-detail-content {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 1.7;
}

@media (max-width: 768px) {
  .message-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }
}
</style>
