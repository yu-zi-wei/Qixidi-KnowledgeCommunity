<template>
  <AdminListPage
    ref="listRef"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getRoles"
    row-key="roleId"
    seq
  >
    <template #toolbar-right>
      <n-button v-if="hasPerm('role:add')" type="primary" @click="openForm()">新增角色</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="560" placement="right">
    <n-drawer-content :title="editingId ? `编辑角色 · ${form.roleName}` : '新增角色'" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="角色名称" path="roleName">
          <n-input v-model:value="form.roleName" placeholder="请输入角色名称" :maxlength="30" />
        </n-form-item>
        <n-form-item label="权限字符" path="roleKey">
          <n-input v-model:value="form.roleKey" placeholder="请输入权限字符（英文标识，如 editor）" :maxlength="100" />
        </n-form-item>
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="显示顺序" path="roleSort">
            <n-input-number v-model:value="form.roleSort" :min="0" class="role-full" />
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-radio-group v-model:value="form.status">
              <n-radio value="0">正常</n-radio>
              <n-radio value="1">停用</n-radio>
            </n-radio-group>
          </n-form-item>
        </n-grid>
        <n-form-item label="备注" path="remark">
          <n-input v-model:value="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </n-form-item>

        <n-divider>权限分配</n-divider>
        <n-spin :show="permLoading">
          <div class="role-perm-groups">
            <div v-for="g in permGroups" :key="g.module" class="role-perm-group">
              <n-checkbox
                :checked="isGroupAll(g)"
                :indeterminate="isGroupInd(g)"
                @update:checked="(v: boolean) => toggleGroup(g, v)"
              >
                {{ g.module }}
              </n-checkbox>
              <n-checkbox-group
                :value="form.perms"
                class="role-perm-items"
                @update:value="(v: Array<string | number>) => (form.perms = v.map(String))"
              >
                <n-checkbox v-for="p in g.perms" :key="p" :value="p" :label="p" />
              </n-checkbox-group>
            </div>
            <n-empty v-if="!permLoading && !permGroups.length" description="暂无权限码（后端扫描 @SaCheckPermission 注册）" />
          </div>
        </n-spin>
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
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  ROLE_STATUS_MAP,
  useRoleAdminApi,
  type PermGroup,
  type RoleRow
} from '~/composables/useRoleAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { hasPerm } = usePerm();
const { getRoles, getRole, addRole, editRole, removeRoles, getPermGroups } = useRoleAdminApi();

// ---------- 搜索条件 ----------
const searchFields = computed<AdminSearchField[]>(() => [
  { key: 'roleName', label: '角色名称', type: 'input' },
  { key: 'roleKey', label: '权限字符', type: 'input' },
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

/** 超管角色（roleId=1）由代码通配，不允许编辑/删除 */
const isSuperRole = (row: RoleRow) => row.roleId === 1;

const columns: DataTableColumns<RoleRow> = [
  { title: '角色名称', key: 'roleName', width: 140, ellipsis: { tooltip: true } },
  { title: '权限字符', key: 'roleKey', width: 140, ellipsis: { tooltip: true } },
  { title: '排序', key: 'roleSort', width: 70, align: 'center' },
  { title: '状态', key: 'status', width: 90, render: (row) => h(AdminStateTag, { value: row.status, map: ROLE_STATUS_MAP }) },
  { title: '备注', key: 'remark', minWidth: 140, ellipsis: { tooltip: true }, render: (row) => row.remark || h('span', { class: 'cell-dim' }, '—') },
  { title: '创建时间', key: 'createTime', width: 160, render: (row) => h('span', { class: 'cell-dim' }, getFullDateTime(row.createTime) || '—') },
  {
    title: '操作',
    key: 'actions',
    width: 120,
    align: 'center',
    render: (row) =>
      isSuperRole(row)
        ? h('span', { class: 'cell-dim' }, '超管内置')
        : h('div', { class: 'cell-btns' }, [
            hasPerm('role:edit')
              ? h(NButton, { size: 'small', text: true, type: 'primary', onClick: () => openForm(row) }, { default: () => '编辑' })
              : null,
            hasPerm('role:remove')
              ? h(
                  NButton,
                  { size: 'small', text: true, type: 'error', loading: operatingId.value === row.roleId, onClick: () => handleRemove(row) },
                  { default: () => '删除' }
                )
              : null
          ])
  }
];

const handleRemove = (row: RoleRow) => {
  dialog.warning({
    title: '删除确认',
    content: `确定删除角色「${row.roleName}」吗？其权限码关联将一并清除；已分配给用户的角色无法删除。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      operatingId.value = row.roleId;
      try {
        await removeRoles([row.roleId]);
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

// ---------- 权限码分组字典 ----------
const permGroups = ref<PermGroup[]>([]);
const permLoading = ref(false);

const loadPermGroups = async () => {
  permLoading.value = true;
  try {
    permGroups.value = (await getPermGroups()) || [];
  } catch {
    /* useApi 已统一弹错，抽屉内留空态 */
  } finally {
    permLoading.value = false;
  }
};

onMounted(loadPermGroups);

// ---------- 新增/编辑抽屉 ----------
const formVisible = ref(false);
const saving = ref(false);
const editingId = ref<number | null>(null);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  roleName: '',
  roleKey: '',
  roleSort: 0,
  status: '0',
  remark: '',
  perms: [] as string[]
});

const rules: FormRules = {
  roleName: { required: true, message: '请输入角色名称', trigger: 'blur' },
  roleKey: { required: true, message: '请输入权限字符', trigger: 'blur' },
  roleSort: { required: true, type: 'number', message: '请输入显示顺序', trigger: ['blur', 'change'] }
};

/** 组内是否全选 / 半选 */
const isGroupAll = (g: PermGroup) => g.perms.every((p) => form.perms.includes(p));
const isGroupInd = (g: PermGroup) => !isGroupAll(g) && g.perms.some((p) => form.perms.includes(p));
const toggleGroup = (g: PermGroup, checked: boolean) => {
  const set = new Set(form.perms);
  g.perms.forEach((p) => (checked ? set.add(p) : set.delete(p)));
  form.perms = [...set];
};

const openForm = async (row?: RoleRow) => {
  editingId.value = row?.roleId ?? null;
  formVisible.value = true;
  Object.assign(form, {
    roleName: row?.roleName || '',
    roleKey: row?.roleKey || '',
    roleSort: row?.roleSort ?? 0,
    status: row?.status || '0',
    remark: row?.remark || '',
    perms: [] as string[]
  });
  if (!permGroups.value.length) loadPermGroups();
  if (row) {
    // perms 不在列表接口返回，编辑必须调详情接口回显
    try {
      const detail = await getRole(row.roleId);
      form.perms = detail.perms || [];
    } catch {
      /* useApi 已统一弹错 */
    }
  }
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      if (editingId.value) {
        await editRole({ ...form, roleId: editingId.value });
      } else {
        await addRole({ ...form });
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

<style scoped lang="scss">
.role-full {
  width: 100%;
}

.role-perm-groups {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.role-perm-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.role-perm-items {
  display: flex;
  flex-wrap: wrap;
  gap: 4px 18px;
  padding-left: 24px;
}
</style>
