<template>
  <div class="shield-word-page">
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
        <n-button type="primary" @click="openForm()">新增屏蔽词</n-button>
      </template>
    </AdminListPage>

    <!-- 新增/编辑抽屉：teleport 组件宽度必须 inline style -->
    <n-drawer v-model:show="formVisible" :width="460" placement="right">
      <n-drawer-content :title="editingId ? `编辑屏蔽词 · ${form.keyword}` : '新增屏蔽词'" closable>
        <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
          <n-form-item label="屏蔽词" path="keyword">
            <n-input
              v-model:value="form.keyword"
              placeholder="请输入屏蔽词"
              :maxlength="50"
              @input="form.keyword = form.keyword.replace(/\s+/g, '')"
            />
          </n-form-item>
          <n-grid :cols="2" :x-gap="16">
            <n-gi>
              <n-form-item label="适用类型" path="type">
                <n-radio-group v-model:value="form.type">
                  <n-radio :value="1">文章</n-radio>
                  <n-radio :value="2">评论</n-radio>
                </n-radio-group>
              </n-form-item>
            </n-gi>
            <n-gi>
              <n-form-item label="权重" path="weight">
                <n-input-number v-model:value="form.weight" :min="1" class="shield-full" />
              </n-form-item>
            </n-gi>
          </n-grid>
          <n-form-item label="状态" path="state">
            <n-radio-group v-model:value="form.state">
              <n-radio :value="0">正常</n-radio>
              <n-radio :value="1">已失效</n-radio>
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
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns, FormInst, FormRules } from 'naive-ui';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import {
  SHIELD_STATE_MAP,
  SHIELD_TYPE_MAP,
  useSystemAdminApi,
  type ShieldWordRow
} from '~/composables/useSystemAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getShieldWordList, getShieldWord, addShieldWord, editShieldWord, deleteShieldWords } =
  useSystemAdminApi();

const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checked = ref<Array<string | number>>([]);

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'keyword', label: '屏蔽词', type: 'input', placeholder: '请输入关键词' },
  {
    key: 'types',
    label: '适用类型',
    type: 'multiple',
    options: [
      { label: '文章', value: 1 },
      { label: '评论', value: 2 }
    ]
  },
  {
    key: 'states',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '正常', value: 0 },
      { label: '已失效', value: 1 }
    ]
  }
];

const fetchList = (params: Record<string, any>) => {
  const { types, states, ...rest } = params;
  return getShieldWordList({ ...rest, type: types?.[0], state: states?.[0] });
};

// ---------- 删除 ----------
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteShieldWords(ids);
  message.success(`已删除 ${ids.length} 个屏蔽词`);
  await listRef.value?.refresh();
};

const doDelete = (row: ShieldWordRow) => {
  dialog.warning({
    title: '删除确认',
    content: `删除后「${row.keyword}」立即从词库移除并刷新缓存，相关内容将不再命中，确认删除？`,
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
    content: `删除后所选 ${ids.length} 个屏蔽词立即从词库移除并刷新缓存，确认删除？`,
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

// ---------- 表格列 ----------
const columns: DataTableColumns<ShieldWordRow> = [
  { title: '屏蔽词', key: 'keyword', minWidth: 180, ellipsis: { tooltip: true } },
  {
    title: '适用类型',
    key: 'type',
    width: 100,
    align: 'center',
    render: (row) => h(AdminStateTag, { value: row.type, map: SHIELD_TYPE_MAP })
  },
  { title: '权重', key: 'weight', width: 80, align: 'center' },
  {
    title: '状态',
    key: 'state',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.state, map: SHIELD_STATE_MAP })
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
    width: 110,
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
            type: 'error',
            loading: deletingId.value === row.id,
            onClick: () => doDelete(row)
          },
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
  keyword: '',
  type: 1,
  weight: 1,
  state: 0
});

const rules: FormRules = {
  keyword: { required: true, message: '请输入屏蔽词', trigger: 'blur' },
  type: { required: true, type: 'number', message: '请选择适用类型', trigger: ['blur', 'change'] },
  weight: { required: true, type: 'number', message: '请输入权重', trigger: ['blur', 'change'] }
};

const openForm = async (row?: ShieldWordRow) => {
  editingId.value = row?.id ?? null;
  Object.assign(form, { keyword: '', type: 1, weight: 1, state: 0 });
  formVisible.value = true;
  if (row) {
    try {
      const detail = await getShieldWord(row.id!);
      Object.assign(form, {
        keyword: detail.keyword || '',
        type: detail.type ?? 1,
        weight: detail.weight ?? 1,
        state: detail.state ?? 0
      });
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
        await editShieldWord({ ...form, id: editingId.value });
      } else {
        await addShieldWord({ ...form });
      }
      message.success('保存成功，词库缓存已刷新');
      formVisible.value = false;
      await listRef.value?.refresh();
    } catch {
      /* useApi 已统一弹错 */
    } finally {
      saving.value = false;
    }
  });
};

// 首次进入预载列表（AdminListPage 自挂载拉取，这里留空保持骨架一致）
onMounted(() => {});
</script>

<style scoped lang="scss">
.shield-word-page {
  height: calc(100vh - 88px);
}

.shield-full {
  width: 100%;
}

@media (max-width: 768px) {
  .shield-word-page {
    height: auto;
    min-height: calc(100vh - 88px);
  }
}
</style>
