<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getFriendLinkList"
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
      <n-button type="primary" @click="openForm()">新增友链</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="520" placement="right">
    <n-drawer-content :title="editingId ? `编辑友链 · ${form.linkName}` : '新增友链'" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="名称" path="linkName">
          <n-input v-model:value="form.linkName" placeholder="请输入站点名称" :maxlength="50" />
        </n-form-item>
        <n-form-item label="地址" path="linkUrl">
          <n-input v-model:value="form.linkUrl" placeholder="请输入完整地址（https://…）" />
        </n-form-item>
        <n-form-item label="头像地址" path="linkAvatar">
          <n-input v-model:value="form.linkAvatar" placeholder="请输入头像图片地址（可选）" />
        </n-form-item>
        <n-form-item label="简介" path="linkIntro">
          <n-input
            v-model:value="form.linkIntro"
            type="textarea"
            :rows="2"
            placeholder="请输入站点简介（可选）"
            :maxlength="200"
          />
        </n-form-item>
        <n-form-item label="状态" path="status">
          <n-radio-group v-model:value="form.status">
            <n-radio :value="0">上架（前台展示）</n-radio>
            <n-radio :value="1">下架</n-radio>
          </n-radio-group>
        </n-form-item>
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
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { FRIEND_LINK_STATUS_MAP, useFriendLinkAdminApi, type FriendLinkRow } from '~/composables/useFriendLinkAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getFriendLinkList, getFriendLink, addFriendLink, editFriendLink, deleteFriendLinks } =
  useFriendLinkAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'linkName', label: '名称', type: 'input', placeholder: '请输入名称关键词' },
  {
    key: 'statuses',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '上架', value: 0 },
      { label: '下架', value: 1 }
    ]
  }
];

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteFriendLinks(ids);
  message.success(`已删除 ${ids.length} 条友链`);
  await listRef.value?.refresh();
};

const doDelete = (row: FriendLinkRow) => {
  dialog.warning({
    title: '删除友链',
    content: `删除后「${row.linkName}」将从前台友链页移除且不可恢复，确认删除？`,
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
    title: '批量删除友链',
    content: `删除后所选友链将从前台友链页移除且不可恢复，确认删除 ${ids.length} 条？`,
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
const columns: DataTableColumns<FriendLinkRow> = [
  {
    title: '站点',
    key: 'linkName',
    minWidth: 200,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.linkAvatar, name: row.linkName, size: 28 }),
        h(
          'div',
          { class: 'friend-link-site' },
          [
            h('a', { class: 'friend-link-name', href: row.linkUrl, target: '_blank', rel: 'noopener' }, row.linkName || '—'),
            h('div', { class: 'cell-dim friend-link-intro' }, row.linkIntro || '暂无简介')
          ]
        )
      ])
  },
  { title: '地址', key: 'linkUrl', minWidth: 220, ellipsis: { tooltip: true } },
  {
    title: '状态',
    key: 'status',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.status, map: FRIEND_LINK_STATUS_MAP })
  },
  {
    title: '加入时间',
    key: 'createTime',
    width: 150,
    render: (row) => getFullDateTime(row.createTime) || '—'
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
  linkName: '',
  linkUrl: '',
  linkAvatar: '',
  linkIntro: '',
  status: 0 as number
});

const rules: FormRules = {
  linkName: { required: true, message: '请输入名称', trigger: 'blur' },
  linkUrl: { required: true, message: '请输入地址', trigger: 'blur' },
  status: { required: true, type: 'number', message: '请选择状态', trigger: ['blur', 'change'] }
};

const openForm = async (row?: FriendLinkRow) => {
  editingId.value = row?.id ?? null;
  formVisible.value = true;
  if (row) {
    // 详情接口补全（列表行字段虽全，以详情为准回显）
    try {
      const detail = await getFriendLink(row.id);
      Object.assign(form, {
        linkName: detail.linkName || '',
        linkUrl: detail.linkUrl || '',
        linkAvatar: detail.linkAvatar || '',
        linkIntro: detail.linkIntro || '',
        status: detail.status ?? 0
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  } else {
    Object.assign(form, { linkName: '', linkUrl: '', linkAvatar: '', linkIntro: '', status: 0 });
  }
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      if (editingId.value) {
        await editFriendLink({ ...form, id: editingId.value });
      } else {
        await addFriendLink({ ...form });
      }
      message.success('保存成功');
      formVisible.value = false;
      await listRef.value?.refresh();
    } catch {
      /* useApi 已统一弹错 */
    } finally {
      saving.value = false;
    }
  });
};
</script>

<style lang="scss">
/* 列 render 元素：scoped 选不中，页面专属类带语义前缀 */
.friend-link-site {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
}

.friend-link-name {
  color: var(--cell-link);
  font-size: 13px;
  text-decoration: none;
}

.friend-link-name:hover {
  text-decoration: underline;
}

.friend-link-intro {
  font-size: 12px;
  max-width: 260px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
