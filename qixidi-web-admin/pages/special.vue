<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getSpecialList"
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
  </AdminListPage>

  <!-- 详情抽屉：一律调详情接口，先清空防残留 -->
  <n-drawer :show="detailVisible" :width="480" placement="right" @update:show="(v: boolean) => (detailVisible = v)">
    <n-drawer-content :title="detail?.specialName ? `专栏 · ${detail.specialName}` : '专栏详情'" closable>
      <n-spin :show="detailLoading">
        <n-empty v-if="!detailLoading && !detail" description="专栏不存在或已删除" />
        <div v-else-if="detail" class="special-detail">
          <AdminAvatar :src="detail.cover" :name="detail.specialName" :size="[120, 80]" shape="square" />
          <n-descriptions :column="1" label-placement="left" bordered size="small">
            <n-descriptions-item label="专栏名称">{{ detail.specialName || '—' }}</n-descriptions-item>
            <n-descriptions-item label="简介">{{ detail.specialIntroduce || '—' }}</n-descriptions-item>
            <n-descriptions-item label="状态">
              <AdminStateTag :value="detail.state" :map="SPECIAL_STATE_MAP" />
            </n-descriptions-item>
            <n-descriptions-item label="所属用户">{{ detail.username || detail.uid || '—' }}</n-descriptions-item>
            <n-descriptions-item label="收录文章">{{ detail.includedCount ?? 0 }} 篇</n-descriptions-item>
            <n-descriptions-item label="创建时间">{{ getFullDateTime(detail.createTime) || '—' }}</n-descriptions-item>
            <n-descriptions-item label="更新时间">{{ getFullDateTime(detail.updateTime) || '—' }}</n-descriptions-item>
          </n-descriptions>
        </div>
      </n-spin>
    </n-drawer-content>
  </n-drawer>

  <!-- 编辑抽屉：teleport 组件宽度必须 inline style -->
  <n-drawer v-model:show="formVisible" :width="520" placement="right">
    <n-drawer-content :title="`编辑专栏 · ${form.specialName}`" closable>
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="专栏名称" path="specialName">
          <n-input v-model:value="form.specialName" placeholder="请输入专栏名称" :maxlength="50" />
        </n-form-item>
        <n-form-item label="简介" path="specialIntroduce">
          <n-input
            v-model:value="form.specialIntroduce"
            type="textarea"
            :rows="3"
            placeholder="请输入专栏简介"
            :maxlength="500"
          />
        </n-form-item>
        <n-form-item label="封面地址" path="cover">
          <n-input v-model:value="form.cover" placeholder="请输入封面图片地址" />
        </n-form-item>
        <n-form-item label="状态" path="state">
          <n-radio-group v-model:value="form.state">
            <n-radio :value="0">正常</n-radio>
            <n-radio :value="1">未启用</n-radio>
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
import { SPECIAL_STATE_MAP, useSpecialAdminApi, type SpecialRow } from '~/composables/useSpecialAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getSpecialList, getSpecial, editSpecial, deleteSpecials } = useSpecialAdminApi();

// ---------- 搜索 ----------
const searchFields: AdminSearchField[] = [
  { key: 'specialName', label: '专栏名称', type: 'input', placeholder: '请输入名称关键词' },
  { key: 'username', label: '所属用户', type: 'input', placeholder: '请输入用户昵称' },
  {
    key: 'states',
    label: '状态',
    type: 'multiple',
    options: [
      { label: '正常', value: 0 },
      { label: '未启用', value: 1 }
    ]
  }
];

// ---------- 详情 ----------
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<SpecialRow | null>(null);

const openDetail = async (row: SpecialRow) => {
  detailVisible.value = true;
  detail.value = null;
  detailLoading.value = true;
  try {
    detail.value = await getSpecial(row.id);
  } catch {
    /* useApi 已统一弹错，抽屉留空态 */
  } finally {
    detailLoading.value = false;
  }
};

// ---------- 删除 ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteSpecials(ids);
  message.success(`已删除 ${ids.length} 个专栏`);
  await listRef.value?.refresh();
};

const doDelete = (row: SpecialRow) => {
  dialog.warning({
    title: '删除专栏',
    content: `删除后「${row.specialName}」及其收录关系不可恢复（收录文章本身不受影响），确认删除？`,
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
    title: '批量删除专栏',
    content: `删除后所选 ${ids.length} 个专栏及其收录关系不可恢复（收录文章本身不受影响），确认删除？`,
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

// ---------- 表格列（行内主标题即详情入口） ----------
const columns: DataTableColumns<SpecialRow> = [
  {
    title: '专栏名称',
    key: 'specialName',
    minWidth: 180,
    render: (row) =>
      h(
        NButton,
        { size: 'small', text: true, type: 'primary', onClick: () => openDetail(row) },
        { default: () => row.specialName || '—' }
      )
  },
  {
    title: '封面',
    key: 'cover',
    width: 110,
    render: (row) => h(AdminAvatar, { src: row.cover, name: row.specialName, size: [80, 48], shape: 'square' })
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: SPECIAL_STATE_MAP })
  },
  {
    title: '所属用户',
    key: 'username',
    minWidth: 130,
    render: (row) => row.username || row.uid || '—'
  },
  { title: '收录文章', key: 'includedCount', width: 90, align: 'center', render: (row) => row.includedCount ?? 0 },
  {
    title: '创建时间',
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

// ---------- 编辑（详情接口补全后提交，EditGroup 必填名称/简介） ----------
const formVisible = ref(false);
const saving = ref(false);
const formRef = ref<FormInst | null>(null);

const form = reactive({
  id: null as number | null,
  specialName: '',
  specialIntroduce: '',
  cover: '',
  state: 0 as number
});

const rules: FormRules = {
  specialName: { required: true, message: '请输入专栏名称', trigger: 'blur' },
  specialIntroduce: { required: true, message: '请输入简介', trigger: 'blur' }
};

const openForm = async (row: SpecialRow) => {
  formVisible.value = true;
  Object.assign(form, { id: row.id, specialName: '', specialIntroduce: '', cover: '', state: 0 });
  try {
    const detailRow = await getSpecial(row.id);
    Object.assign(form, {
      id: detailRow.id,
      specialName: detailRow.specialName || '',
      specialIntroduce: detailRow.specialIntroduce || '',
      cover: detailRow.cover || '',
      state: detailRow.state ?? 0
    });
  } catch {
    /* useApi 已统一弹错 */
  }
};

const handleSave = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    saving.value = true;
    try {
      await editSpecial({ ...form, id: form.id! });
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
.special-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
</style>
