<template>
  <div class="message-page">
    <n-tabs v-model:value="activeTab" type="line" class="message-tabs">
      <!-- display-directive 必须写在 n-tab-pane 上（写在 n-tabs 上无效）：两个 pane 常驻 v-show 切换，
           各自 AdminListPage 实例独立；否则切 tab 时 pane 被无 key 就地复用，fetch prop 换了但数据不重拉 -->
      <n-tab-pane name="system" tab="系统消息" display-directive="show">
        <AdminListPage
          ref="systemListRef"
          v-model:checked-keys="systemChecked"
          :search-fields="searchFields"
          :columns="columns"
          :fetch="fetchList"
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
              :disabled="!systemChecked.length"
              :loading="batchDeleting"
              @click="doDeleteBatch"
            >
              删除所选（{{ systemChecked.length }}）
            </n-button>
          </template>
          <template #toolbar-right>
            <n-button type="primary" @click="openForm()">发新消息</n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>

      <n-tab-pane name="user" tab="用户消息" display-directive="show">
        <AdminListPage
          ref="userListRef"
          v-model:checked-keys="userChecked"
          :search-fields="userSearchFields"
          :columns="userColumns"
          :fetch="fetchUserList"
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
              :disabled="!userChecked.length"
              :loading="userBatchDeleting"
              @click="doDeleteUserBatch"
            >
              删除所选（{{ userChecked.length }}）
            </n-button>
          </template>
        </AdminListPage>
      </n-tab-pane>
    </n-tabs>

    <!-- 详情弹窗：teleport 组件宽度必须 inline style -->
    <n-modal v-model:show="detailVisible" :style="{ width: '560px' }" preset="card" title="消息详情">
      <template v-if="detail">
        <div class="message-detail-meta">
          <AdminStateTag :value="detail.state" :map="MESSAGE_STATE_MAP" />
          <AdminStateTag :value="detail.type" :map="MESSAGE_TYPE_MAP" />
          <AdminStateTag :value="detail.isMassAir" :map="MESSAGE_MASS_MAP" />
          <span v-if="detail.isMassAir === 2">接收人：{{ detail.recipientName || detail.uid || '—' }}</span>
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
          {{
            form.isMassAir === 2
              ? '保存后仅对指定用户生效，对方在线会立即收到新消息提醒。'
              : '保存后立即对全体用户生效，在线用户会收到新消息提醒。'
          }}
        </n-alert>
        <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
          <n-form-item label="发送方式" path="isMassAir">
            <n-radio-group v-model:value="form.isMassAir" @update:value="onMassAirChange">
              <n-radio :value="1">群发（全体用户）</n-radio>
              <n-radio :value="2">指定用户</n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item v-if="form.isMassAir === 2" label="接收用户" path="uid">
            <n-select
              v-model:value="form.uid"
              filterable
              clearable
              placeholder="搜索昵称或用户名选择接收用户"
              :options="userOptions"
              :loading="userLoading"
            />
          </n-form-item>
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
import AdminAvatar from '~/components/AdminAvatar.vue';
import {
  MESSAGE_MASS_MAP,
  MESSAGE_STATE_MAP,
  MESSAGE_TYPE_MAP,
  USER_NEWS_READ_MAP,
  USER_NEWS_TYPE_MAP,
  useSystemAdminApi,
  type SystemMessageRow,
  type UserNewsRow
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
  deleteSystemMessages,
  getUserNewsList,
  deleteUserNews
} = useSystemAdminApi();
const { getUsers } = useUserAdminApi();

const activeTab = ref('system');

// ==================== 系统消息 tab ====================
const systemListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const systemChecked = ref<Array<string | number>>([]);

// ---------- 搜索（接收人昵称：后端用户域查 uuid 集合后 in，指定人发消息才有效） ----------
const searchFields: AdminSearchField[] = [
  { key: 'newsTitle', label: '标题', type: 'input', placeholder: '请输入标题关键词' },
  { key: 'recipientNickname', label: '接收人', type: 'input', placeholder: '请输入接收人昵称' },
  {
    key: 'isMassAirs',
    label: '发送方式',
    type: 'multiple',
    options: [
      { label: '群发', value: 1 },
      { label: '指定人', value: 2 }
    ]
  },
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

const fetchList = (params: Record<string, any>) => getSystemMessageList(params);

// ---------- 删除 ----------
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteSystemMessages(ids);
  message.success(`已删除 ${ids.length} 条系统消息`);
  await systemListRef.value?.refresh();
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
  const ids = [...systemChecked.value];
  dialog.warning({
    title: '批量删除',
    content: `删除后所选 ${ids.length} 条系统消息立即从所有用户的消息列表移除且不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      batchDeleting.value = true;
      try {
        await runDelete(ids);
        systemChecked.value = [];
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
      await systemListRef.value?.refresh();
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
    minWidth: 220,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.newsContent }, row.newsContent || '—')
  },
  {
    title: '类型',
    key: 'type',
    width: 80,
    render: (row) => h(AdminStateTag, { value: row.type, map: MESSAGE_TYPE_MAP })
  },
  {
    title: '发送方式',
    key: 'isMassAir',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.isMassAir, map: MESSAGE_MASS_MAP })
  },
  {
    title: '接收人',
    key: 'recipientName',
    width: 130,
    render: (row) =>
      row.isMassAir === 2
        ? renderUser(row.recipientName, row.recipientAvatar)
        : h('span', { class: 'cell-dim' }, '全体用户')
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
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
  newsContent: '',
  /** 发送方式（1：群发，2：指定人发），可选——后端空时默认群发 */
  isMassAir: 1 as number,
  /** 指定人发的接收用户 uuid */
  uid: null as string | null
});

const rules: FormRules = {
  newsTitle: { required: true, message: '请输入消息标题', trigger: 'blur' },
  newsContent: { required: true, message: '请输入消息内容', trigger: 'blur' },
  uid: {
    validator: (_rule, value) => {
      if (form.isMassAir === 2 && !value) return new Error('请选择接收用户');
      return true;
    },
    trigger: ['blur', 'change']
  }
};

// ---------- 接收用户候选（前台用户量小，一次拉全量本地过滤：点开即有完整候选，输入即过滤） ----------
const userOptions = ref<Array<{ label: string; value: string }>>([]);
const userLoading = ref(false);
let userOptionsPromise: Promise<void> | null = null;

const loadUserOptions = () => {
  if (!userOptionsPromise) {
    userLoading.value = true;
    userOptionsPromise = (async () => {
      try {
        const { rows } = await getUsers({ pageNum: 1, pageSize: 500 });
        userOptions.value = (rows || [])
          .filter((u) => u.state !== 2) // 已删除用户不可选
          .map((u) => ({
            label: `${u.nickname || u.username}（${u.username || '-'}）`,
            value: u.uuid
          }));
      } catch {
        userOptionsPromise = null; // 失败允许重试
        /* useApi 已统一弹错 */
      } finally {
        userLoading.value = false;
      }
    })();
  }
  return userOptionsPromise;
};

const onMassAirChange = (value: number) => {
  if (value === 2) loadUserOptions();
};

const openForm = async (row?: SystemMessageRow) => {
  editingId.value = row?.id ?? null;
  Object.assign(form, { newsTitle: '', newsContent: '', isMassAir: 1, uid: null });
  formVisible.value = true;
  if (row) {
    try {
      const d = await getSystemMessage(row.id!);
      Object.assign(form, {
        newsTitle: d.newsTitle || '',
        newsContent: d.newsContent || '',
        isMassAir: d.isMassAir ?? 1,
        uid: d.isMassAir === 2 ? d.uid || null : null
      });
      if (form.uid) {
        await loadUserOptions();
        // 候选中无当前接收人（已注销/已删除）时补一项，保证选择器直接显示昵称
        if (!userOptions.value.some((o) => o.value === form.uid)) {
          userOptions.value = [{ label: d.recipientName || form.uid, value: form.uid }, ...userOptions.value];
        }
      }
    } catch {
      /* useApi 已统一弹错 */
    }
  }
};

const handleSave = () => {
  formRef.value?.validate((errors) => {
    if (errors) return;
    const designated = form.isMassAir === 2;
    dialog.info({
      title: '发布确认',
      content: editingId.value
        ? designated
          ? '保存后消息仅对指定用户生效，确认提交？'
          : '保存后立即对全体用户生效，确认提交？'
        : designated
          ? '发布后仅指定用户可见并推送提醒，确认发布？'
          : '发布后立即对全体用户可见并推送提醒，确认发布？',
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        saving.value = true;
        try {
          // type/isDetails 由后端兜底（前台消费只认 type=2、isDetails 默认无详情）
          const body = {
            newsTitle: form.newsTitle,
            newsContent: form.newsContent,
            isMassAir: form.isMassAir,
            ...(designated ? { uid: form.uid } : {}),
            ...(editingId.value ? { id: editingId.value } : {})
          };
          if (editingId.value) {
            await editSystemMessage(body);
          } else {
            await addSystemMessage(body);
          }
          message.success(
            editingId.value ? '已保存并生效' : designated ? '已发布，指定用户可见' : '已发布，全体用户可见'
          );
          formVisible.value = false;
          await systemListRef.value?.refresh();
        } catch {
          /* useApi 已统一弹错 */
        } finally {
          saving.value = false;
        }
      }
    });
  });
};

// ==================== 用户消息 tab（b_news_user_record，用户间互动消息） ====================
const userListRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const userChecked = ref<Array<string | number>>([]);

const userSearchFields: AdminSearchField[] = [
  {
    key: 'types',
    label: '类型',
    type: 'multiple',
    options: Object.entries(USER_NEWS_TYPE_MAP).map(([value, meta]) => ({
      label: meta.label,
      value: Number(value)
    }))
  },
  {
    key: 'username',
    label: '用户名',
    type: 'input',
    placeholder: '发送者/接收者昵称或用户名'
  }
];

const fetchUserList = (params: Record<string, any>) => getUserNewsList(params);

// ---------- 删除 ----------
const userDeletingId = ref<number | null>(null);
const userBatchDeleting = ref(false);

const runUserDelete = async (ids: Array<string | number>) => {
  await deleteUserNews(ids);
  message.success(`已删除 ${ids.length} 条用户消息`);
  await userListRef.value?.refresh();
};

const doDeleteUser = (row: UserNewsRow) => {
  dialog.warning({
    title: '删除确认',
    content: `删除后该消息从接收者的消息列表移除且不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      userDeletingId.value = row.id!;
      try {
        await runUserDelete([row.id!]);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        userDeletingId.value = null;
      }
    }
  });
};

const doDeleteUserBatch = () => {
  const ids = [...userChecked.value];
  dialog.warning({
    title: '批量删除',
    content: `删除后所选 ${ids.length} 条用户消息从对应接收者的消息列表移除且不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      userBatchDeleting.value = true;
      try {
        await runUserDelete(ids);
        userChecked.value = [];
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        userBatchDeleting.value = false;
      }
    }
  });
};

const renderUser = (name?: string, avatar?: string) =>
  h('div', { class: 'cell-author' }, [
    h(AdminAvatar, { src: avatar, name: name || '?', size: 24 }),
    h('span', { class: 'cell-author-name' }, name || '—')
  ]);

const userColumns: DataTableColumns<UserNewsRow> = [
  {
    title: '发送者',
    key: 'senderName',
    minWidth: 130,
    render: (row) => renderUser(row.senderName, row.senderAvatar)
  },
  {
    title: '接收者',
    key: 'recipientName',
    minWidth: 130,
    render: (row) => renderUser(row.recipientName, row.recipientAvatar)
  },
  {
    title: '类型',
    key: 'type',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.type, map: USER_NEWS_TYPE_MAP })
  },
  {
    title: '消息内容',
    key: 'content',
    minWidth: 240,
    render: (row) => h('span', { class: 'cell-ellipsis', title: row.content }, row.content || '—')
  },
  {
    title: '已读',
    key: 'beenRead',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.beenRead, map: USER_NEWS_READ_MAP })
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
    width: 90,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h(
        NButton,
        {
          size: 'small',
          text: true,
          type: 'error',
          loading: userDeletingId.value === row.id,
          onClick: () => doDeleteUser(row)
        },
        { default: () => '删除' }
      )
  }
];
</script>

<style scoped lang="scss">
/* 双 tab 零滚动布局（同 tool 页骨架）：tab 头占一行，两个 pane 各自撑满剩余高度 */
.message-page {
  height: calc(100vh - 88px);
  display: flex;
  flex-direction: column;
}

.message-tabs {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

/* 非 animated 时 pane 直挂 tabs 根下，直接约束 pane（见 qixidi-web-admin 规范） */
.message-tabs :deep(.n-tab-pane) {
  flex: 1;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding-top: 12px;
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
