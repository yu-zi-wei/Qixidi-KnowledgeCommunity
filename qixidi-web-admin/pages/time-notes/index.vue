<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="fetch"
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

  <!-- 小记详情抽屉：一律调详情接口，先清空防残留（teleport 组件宽度必须 inline style） -->
  <n-drawer v-model:show="detailVisible" :width="480" placement="right">
    <n-drawer-content :title="detail?.title || '小记详情'" closable>
      <n-spin :show="detailLoading">
        <div v-if="detail" class="detail-body">
          <div class="detail-head">
            <AdminAvatar :src="detail.avatar" :name="detail.nickname" :size="40" />
            <div class="detail-head-main">
              <div class="detail-name">{{ detail.nickname || '—' }}</div>
              <div class="detail-time">记录于 {{ detail.recordTime || '—' }}</div>
            </div>
          </div>
          <n-divider />
          <p class="detail-content">{{ detail.content || '（无内容）' }}</p>
          <div class="detail-meta">
            <a
              class="detail-link"
              :href="`https://qixidi.top/time-notes/${detail.id}`"
              target="_blank"
            >在前台查看 →</a>
          </div>
        </div>
      </n-spin>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import { useTimeNotesAdminApi, type TimeNotesRow } from '~/composables/useTimeNotesAdminApi';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getTimeNotesList, getTimeNote, deleteTimeNotes } = useTimeNotesAdminApi();

// ---------- 搜索（title 后端 like） ----------
const searchFields: AdminSearchField[] = [
  { key: 'title', label: '标题', type: 'input', placeholder: '请输入关键词' }
];

const fetch = (params: Record<string, any>) => getTimeNotesList(params);

// ---------- 删除（逻辑删 + 相册图片清理；批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteTimeNotes(ids);
  message.success(`已删除 ${ids.length} 条小记`);
  await listRef.value?.refresh();
};

const doDelete = (row: TimeNotesRow) => {
  dialog.warning({
    title: '删除小记',
    content: '删除后小记及其相册图片将一并清理，确认删除这条小记？',
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
    title: '批量删除小记',
    content: `删除后小记及其相册图片将一并清理，确认删除选中的 ${ids.length} 条小记？`,
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

// ---------- 详情抽屉（标题点击打开；接口不含作者信息，作者行用行数据补充） ----------
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<TimeNotesRow | null>(null);
const openDetail = async (row: TimeNotesRow) => {
  detailVisible.value = true;
  detailLoading.value = true;
  detail.value = null; // 先清空，避免残留上一条内容
  try {
    const info = await getTimeNote(row.id);
    // 详情接口（TimeNotesInfoVo）不返回作者昵称头像，用列表行数据补充展示
    detail.value = { ...info, nickname: row.nickname, avatar: row.avatar };
  } catch {
    detail.value = null; // useApi 已弹错，失败留空态，不回退列表数据
  } finally {
    detailLoading.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<TimeNotesRow> = [
  {
    title: '小记',
    key: 'title',
    minWidth: 320,
    render: (row) =>
      // 标题即详情入口；列表不展示内容，内容看详情抽屉
      h(
        'span',
        { class: 'cell-ellipsis is-link', onClick: () => openDetail(row), title: '查看详情' },
        row.title || '（无标题）'
      )
  },
  {
    title: '作者',
    key: 'nickname',
    minWidth: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.nickname || '—')
      ])
  },
  {
    title: '记录时间',
    key: 'recordTime',
    width: 120,
    render: (row) => row.recordTime || '—'
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
          { default: () => '删除' }
        )
      ])
  }
];
</script>

<style scoped lang="scss">
.detail-body {
  .detail-head {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .detail-head-main {
    flex: 1;
    min-width: 0;
  }

  .detail-name {
    font-weight: 600;
  }

  .detail-time {
    font-size: 12px;
    opacity: 0.5;
  }

  .detail-content {
    margin: 0;
    font-size: 13.5px;
    line-height: 1.7;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .detail-meta {
    margin-top: 16px;
  }

  // teleport 抽屉内主题变量不可用，用布局根注入的链接色变量兜底中性蓝
  .detail-link {
    color: var(--cell-link, #3d7a5c);
    font-size: 13px;
    text-decoration: none;
  }
}
</style>
