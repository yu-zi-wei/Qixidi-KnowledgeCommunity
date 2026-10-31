<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getConfigs"
    row-key="configId"
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
      <n-dropdown trigger="click" :options="cacheOptions" @select="onRefreshCache">
        <n-button size="small" :loading="refreshing">刷新缓存</n-button>
      </n-dropdown>
      <n-button size="small" type="primary" @click="openAdd">新增参数</n-button>
    </template>
  </AdminListPage>

  <!-- 新增/编辑弹窗：编辑一律调详情接口回填（teleport 组件宽度必须 inline style；样式走非 scoped 块） -->
  <n-modal v-model:show="formVisible" :style="{ width: '460px' }" class="setting-form-modal-host" preset="card" :title="editing ? '编辑参数' : '新增参数'">
    <n-form label-placement="top" size="small">
      <n-form-item label="参数名称" required>
        <n-input v-model:value="form.configName" placeholder="如：是否开启验证码" :maxlength="100" />
      </n-form-item>
      <n-form-item label="参数键名" required>
        <n-input v-model:value="form.configKey" placeholder="如：sys.account.captchaEnabled" :maxlength="100" :disabled="editing" />
      </n-form-item>
      <n-form-item label="参数键值" required>
        <n-input v-model:value="form.configValue" type="textarea" :rows="3" placeholder="true / false / 自定义值" :maxlength="500" />
      </n-form-item>
      <n-form-item label="系统内置">
        <n-radio-group v-model:value="form.configType">
          <n-radio value="N">否</n-radio>
          <n-radio value="Y">是</n-radio>
        </n-radio-group>
      </n-form-item>
      <n-form-item label="备注">
        <n-input v-model:value="form.remark" type="textarea" :rows="2" placeholder="参数用途说明（可选）" />
      </n-form-item>
    </n-form>
    <p class="setting-form-tip">修改保存后立即生效并写入缓存；内置参数请谨慎操作。</p>
    <template #footer>
      <div class="setting-form-footer">
        <n-button size="small" @click="formVisible = false">取消</n-button>
        <n-button size="small" type="primary" :loading="saving" :disabled="!form.configName || !form.configKey || !form.configValue" @click="doSave">
          保存
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
import { h, ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns, DropdownOption } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  CONFIG_TYPE_MAP,
  SITE_CACHE_KINDS,
  useConfigAdminApi,
  type ConfigRow
} from '~/composables/useConfigAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getConfigs, getConfig, addConfig, updateConfig, deleteConfigs, refreshConfigCache, refreshSiteCache } =
  useConfigAdminApi();

// ---------- 搜索（configName/configKey like；configType eq） ----------
const searchFields: AdminSearchField[] = [
  { key: 'configName', label: '参数名称', type: 'input', placeholder: '请输入名称关键词' },
  { key: 'configKey', label: '参数键名', type: 'input', placeholder: '请输入键名关键词' },
  { key: 'configType', label: '内置标记', type: 'select', options: [
    { label: '内置', value: 'Y' },
    { label: '外置', value: 'N' }
  ] }
];

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteConfigs(ids);
  message.success(`已删除 ${ids.length} 项参数`);
  await listRef.value?.refresh();
};

const doDelete = (row: ConfigRow) => {
  dialog.warning({
    title: '删除参数',
    content:
      row.configType === 'Y'
        ? `「${row.configName}」是系统内置参数，删除可能影响功能运行且不可恢复，确认删除？`
        : `删除「${row.configName}」后不可恢复，确认删除？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      deletingId.value = row.configId;
      try {
        await runDelete([row.configId]);
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
    title: '批量删除参数',
    content: `删除后不可恢复，且可能影响依赖该参数的功能，确认删除选中的 ${ids.length} 项？`,
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

// ---------- 缓存刷新（参数缓存全量重建 + 站点配置/屏蔽词） ----------
const refreshing = ref(false);
const cacheOptions: DropdownOption[] = [
  { label: '参数缓存（全量重建）', key: 'config' },
  ...SITE_CACHE_KINDS.map((k) => ({ label: `站点：${k.label}`, key: k.key }))
];

const onRefreshCache = (key: string | number) => {
  const label = cacheOptions.find((o) => o.key === key)?.label || String(key);
  dialog.info({
    title: '刷新缓存',
    content: `确认刷新「${label}」？`,
    positiveText: '刷新',
    negativeText: '取消',
    onPositiveClick: async () => {
      refreshing.value = true;
      try {
        if (key === 'config') {
          await refreshConfigCache();
        } else {
          await refreshSiteCache(String(key));
        }
        message.success(`已刷新：${label}`);
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        refreshing.value = false;
      }
    }
  });
};

// ---------- 新增/编辑（编辑先调详情接口回填，弹窗打开即清空防残留） ----------
const formVisible = ref(false);
const saving = ref(false);
const editing = ref(false);
const emptyForm = (): Partial<ConfigRow> => ({ configName: '', configKey: '', configValue: '', configType: 'N', remark: '' });
const form = ref<Partial<ConfigRow>>(emptyForm());

const openAdd = () => {
  editing.value = false;
  form.value = emptyForm();
  formVisible.value = true;
};

const openEdit = async (row: ConfigRow) => {
  editing.value = true;
  form.value = emptyForm(); // 先清空防残留上一项
  formVisible.value = true;
  try {
    form.value = await getConfig(row.configId);
  } catch {
    /* useApi 已弹错，表单留空 */
  }
};

const doSave = async () => {
  saving.value = true;
  try {
    if (editing.value) {
      await updateConfig(form.value);
      message.success('已保存并更新缓存');
    } else {
      await addConfig(form.value);
      message.success('已新增参数');
    }
    formVisible.value = false;
    if (editing.value) {
      await listRef.value?.refresh();
    } else {
      await listRef.value?.reload();
    }
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    saving.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<ConfigRow> = [
  {
    title: '参数名称',
    key: 'configName',
    minWidth: 160,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        // 名称即编辑入口；样式类在 admin-table.scss
        h('div', { class: 'cell-comment-content cell-clamp-2 is-link', onClick: () => openEdit(row), title: '编辑参数' }, row.configName || '—'),
        h('div', { class: 'cell-comment-sub cell-ellipsis', title: row.configKey || '' }, row.configKey || '')
      ])
  },
  {
    title: '参数键值',
    key: 'configValue',
    minWidth: 200,
    render: (row) =>
      h('span', { class: 'cell-ellipsis cell-mono', title: row.configValue || '' }, row.configValue || '—')
  },
  {
    title: '内置',
    key: 'configType',
    width: 80,
    align: 'center',
    render: (row) => h(AdminStateTag, { value: row.configType ?? 'N', map: CONFIG_TYPE_MAP })
  },
  {
    title: '备注',
    key: 'remark',
    minWidth: 140,
    render: (row) =>
      h('span', { class: 'cell-ellipsis', title: row.remark || '' }, row.remark || '—')
  },
  {
    title: '更新时间',
    key: 'updateTime',
    width: 150,
    render: (row) => getFullDateTime(row.updateTime) || '—'
  },
  {
    title: '操作',
    key: 'actions',
    width: 110,
    align: 'center',
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(
          NButton,
          { size: 'small', text: true, onClick: () => openEdit(row) },
          { default: () => '编辑' }
        ),
        h(
          NButton,
          { size: 'small', text: true, type: 'error', loading: deletingId.value === row.configId, onClick: () => doDelete(row) },
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<!-- n-modal teleport 到 body，scoped 选不中：表单弹窗样式放非 scoped 块 -->
<style lang="scss">
.setting-form-modal-host {
  .setting-form-tip {
    margin: 4px 0 0;
    font-size: 12px;
    opacity: 0.55;
  }

  .setting-form-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
