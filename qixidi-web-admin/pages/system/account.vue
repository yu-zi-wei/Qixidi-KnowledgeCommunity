<template>
  <AdminListPage
    ref="listRef"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getAccounts"
    row-key="userId"
    seq
  >
    <template #toolbar-right>
      <n-button v-if="hasPerm('user:add')" type="primary" @click="openForm()">新增账号</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="560" placement="right">
    <n-drawer-content :title="editingId ? `编辑账号 · ${form.userName}` : '新增账号'" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="登录账号" path="userName">
          <n-input v-model:value="form.userName" placeholder="请输入登录账号" :maxlength="30" :disabled="!!editingId" />
        </n-form-item>
        <n-form-item v-if="!editingId" label="登录密码" path="password">
          <n-input v-model:value="form.password" type="password" show-password-on="click" placeholder="请输入登录密码" />
        </n-form-item>
        <n-form-item label="用户昵称" path="nickName">
          <n-input v-model:value="form.nickName" placeholder="请输入用户昵称" :maxlength="30" />
        </n-form-item>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="手机号码" path="phonenumber">
            <n-input v-model:value="form.phonenumber" placeholder="请输入手机号码" />
          </n-form-item>
          <n-form-item label="邮箱" path="email">
            <n-input v-model:value="form.email" placeholder="请输入邮箱" />
          </n-form-item>
          <n-form-item label="性别" path="sex">
            <n-select v-model:value="form.sex" :options="SEX_OPTIONS" />
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-radio-group v-model:value="form.status">
              <n-radio value="0">正常</n-radio>
              <n-radio value="1">停用</n-radio>
            </n-radio-group>
          </n-form-item>
        </n-grid>
        <n-form-item label="角色" path="roleIds">
          <n-select
            v-model:value="form.roleIds"
            multiple
            clearable
            :options="roleOptions"
            placeholder="请选择角色（可多选）"
          />
        </n-form-item>
        <n-form-item label="备注" path="remark">
          <n-input v-model:value="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
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

  <!-- 重置密码弹窗 -->
  <n-modal
    v-model:show="pwdVisible"
    preset="dialog"
    title="重置密码"
    positive-text="确认"
    negative-text="取消"
    :loading="pwdSaving"
    @positive-click="handleResetPwd"
  >
    <div class="account-pwd-body">
      <p class="account-pwd-tip">为账号「{{ pwdTarget?.userName }}」设置新密码：</p>
      <n-input
        v-model:value="pwdForm.password"
        type="password"
        show-password-on="click"
        placeholder="请输入新密码（5-20 位）"
        @keyup.enter="handleResetPwd"
      />
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton, NTag } from 'naive-ui';
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  ACCOUNT_STATUS_MAP,
  useAccountAdminApi,
  type AccountRow
} from '~/composables/useAccountAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { hasPerm } = usePerm();
const { getAccounts, getAccount, addAccount, editAccount, resetAccountPwd, removeAccounts } =
  useAccountAdminApi();

const SEX_OPTIONS = [
  { label: '男', value: '0' },
  { label: '女', value: '1' },
  { label: '未知', value: '2' }
];

// ---------- 搜索条件 ----------
const searchFields = computed<AdminSearchField[]>(() => [
  { key: 'userName', label: '登录账号', type: 'input' },
  { key: 'phonenumber', label: '手机号码', type: 'input' },
  {
    key: 'status',
    label: '状态',
    type: 'select',
    options: [
      { label: '正常', value: '0' },
      { label: '停用', value: '1' }
    ]
  }
]);

// ---------- 列表 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const operatingId = ref<number | null>(null);

/** 超管账号（userId=1）由代码通配权限，不允许编辑/删除/重置密码 */
const isSuperAccount = (row: AccountRow) => row.userId === 1;

const columns: DataTableColumns<AccountRow> = [
  { title: '登录账号', key: 'userName', width: 130, ellipsis: { tooltip: true } },
  { title: '昵称', key: 'nickName', width: 120, ellipsis: { tooltip: true }, render: (row) => row.nickName || h('span', { class: 'cell-dim' }, '—') },
  { title: '手机号码', key: 'phonenumber', width: 130, render: (row) => row.phonenumber || h('span', { class: 'cell-dim' }, '—') },
  { title: '邮箱', key: 'email', minWidth: 160, ellipsis: { tooltip: true }, render: (row) => row.email || h('span', { class: 'cell-dim' }, '—') },
  {
    title: '角色',
    key: 'roles',
    width: 150,
    render: (row) =>
      row.roles?.length
        ? h('div', { class: 'account-role-tags' }, row.roles.map((r) => h(NTag, { size: 'small', bordered: false }, { default: () => r.roleName })))
        : h('span', { class: 'cell-dim' }, '—')
  },
  { title: '状态', key: 'status', width: 90, render: (row) => h(AdminStateTag, { value: row.status, map: ACCOUNT_STATUS_MAP }) },
  { title: '创建时间', key: 'createTime', width: 160, render: (row) => h('span', { class: 'cell-dim' }, getFullDateTime(row.createTime) || '—') },
  {
    title: '操作',
    key: 'actions',
    width: 170,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      isSuperAccount(row)
        ? h('span', { class: 'cell-dim' }, '超管内置')
        : h('div', { class: 'cell-btns' }, [
            hasPerm('user:edit')
              ? h(NButton, { size: 'small', text: true, type: 'primary', onClick: () => openForm(row) }, { default: () => '编辑' })
              : null,
            hasPerm('user:resetPwd')
              ? h(NButton, { size: 'small', text: true, type: 'warning', onClick: () => openResetPwd(row) }, { default: () => '重置密码' })
              : null,
            hasPerm('user:remove')
              ? h(
                  NButton,
                  { size: 'small', text: true, type: 'error', loading: operatingId.value === row.userId, onClick: () => handleRemove(row) },
                  { default: () => '删除' }
                )
              : null
          ])
  }
];

const handleRemove = (row: AccountRow) => {
  dialog.warning({
    title: '删除确认',
    content: `确定删除账号「${row.userName}」吗？删除后该账号将无法登录后台。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      operatingId.value = row.userId;
      try {
        await removeAccounts([row.userId]);
        message.success('删除成功');
        await listRef.value?.refresh();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        operatingId.value = null;
      }
    }
  });
};

// ---------- 新增/编辑抽屉 ----------
const formVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);
const roleOptions = ref<Array<{ label: string; value: number }>>([]);
/** 详情回显的脱敏敏感字段原值：未修改则不回传，防止脱敏串写回数据库 */
const originalSensitive = reactive({ phonenumber: '', email: '' });

const form = reactive({
  userName: '',
  nickName: '',
  password: '',
  phonenumber: '',
  email: '',
  sex: '2',
  status: '0',
  remark: '',
  roleIds: [] as number[]
});

const rules = computed<FormRules>(() => ({
  userName: { required: true, message: '请输入登录账号', trigger: 'blur' },
  password: editingId.value ? undefined : { required: true, message: '请输入登录密码', trigger: 'blur' }
}));

const loadRoleOptions = async () => {
  if (roleOptions.value.length) return;
  try {
    // GET /system/user/（不传 id）只返回全量角色，作新增/编辑下拉数据源
    const data = await getAccount();
    roleOptions.value = (data.roles || []).map((r) => ({ label: r.roleName, value: r.roleId }));
  } catch {
    /* useApi 已统一弹错，下拉留空 */
  }
};

const openForm = async (row?: AccountRow) => {
  editingId.value = row?.userId ?? null;
  formVisible.value = true;
  Object.assign(form, {
    userName: row?.userName || '',
    nickName: row?.nickName || '',
    password: '',
    phonenumber: row?.phonenumber || '',
    email: row?.email || '',
    sex: row?.sex ?? '2',
    status: row?.status || '0',
    remark: row?.remark || '',
    roleIds: [] as number[]
  });
  loadRoleOptions();
  if (row) {
    // 角色勾选不在列表行完整回显，编辑调详情接口取 roleIds
    try {
      const detail = await getAccount(row.userId);
      Object.assign(form, {
        nickName: detail.user?.nickName || form.nickName,
        phonenumber: detail.user?.phonenumber || '',
        email: detail.user?.email || '',
        sex: detail.user?.sex ?? '2',
        status: detail.user?.status || '0',
        remark: detail.user?.remark || '',
        roleIds: detail.roleIds || []
      });
    } catch {
      /* useApi 已统一弹错 */
    }
  }
  originalSensitive.phonenumber = form.phonenumber;
  originalSensitive.email = form.email;
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      if (editingId.value) {
        const payload: Partial<AccountRow> & { roleIds?: number[] } = {
          userId: editingId.value,
          userName: form.userName,
          nickName: form.nickName,
          sex: form.sex,
          status: form.status,
          remark: form.remark,
          roleIds: form.roleIds
        };
        // 敏感字段未修改不回传（非超管回显的是脱敏串）
        if (form.phonenumber !== originalSensitive.phonenumber) payload.phonenumber = form.phonenumber;
        if (form.email !== originalSensitive.email) payload.email = form.email;
        await editAccount(payload);
      } else {
        await addAccount({ ...form });
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

// ---------- 重置密码 ----------
const pwdVisible = ref(false);
const pwdSaving = ref(false);
const pwdTarget = ref<AccountRow | null>(null);
const pwdForm = reactive({ password: '' });

const openResetPwd = (row: AccountRow) => {
  pwdTarget.value = row;
  pwdForm.password = '';
  pwdVisible.value = true;
};

const handleResetPwd = async () => {
  const pwd = pwdForm.password.trim();
  if (!pwdTarget.value) return false;
  if (pwd.length < 5 || pwd.length > 20) {
    message.warning('密码长度须为 5-20 位');
    return false; // 阻止弹窗关闭
  }
  pwdSaving.value = true;
  try {
    await resetAccountPwd(pwdTarget.value.userId, pwd);
    message.success('密码已重置');
  } catch {
    return false; // 失败保持弹窗打开（useApi 已弹错）
  } finally {
    pwdSaving.value = false;
  }
};
</script>

<!-- 重置密码弹窗内容在 teleport 容器内、角色 tag 在 NDataTable 渲染上下文，scoped 均选不中，统一用页面语义前缀的非 scoped 类 -->
<style lang="scss">
.account-pwd-body {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 4px;
}

.account-pwd-tip {
  margin: 0;
  font-size: 13px;
  opacity: 0.75;
}

.account-role-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}
</style>
