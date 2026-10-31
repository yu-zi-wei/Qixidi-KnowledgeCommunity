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

  <!-- 评论详情抽屉：一律调详情接口，先清空防残留（teleport 组件宽度必须 inline style） -->
  <n-drawer v-model:show="detailVisible" :width="480" placement="right">
    <n-drawer-content :title="detail?.commentName || '评论详情'" closable>
      <n-spin :show="detailLoading">
        <div v-if="detail" class="detail-body">
          <div class="detail-head">
            <AdminAvatar :src="detail.commentAvatar" :name="detail.commentName" :size="40" />
            <div class="detail-head-main">
              <div class="detail-name">{{ detail.commentName || '—' }}</div>
              <div class="detail-time">{{ getFullDateTime(detail.createTime) }}</div>
            </div>
            <AdminStateTag :value="detail.state" :map="COMMENT_STATE_MAP" class="detail-state" />
          </div>
          <n-divider />
          <p class="detail-content">{{ detail.content }}</p>
          <div class="detail-meta">
            <span>所在文章：{{ detail.articleName || '—' }}（#{{ detail.articleId }}）</span>
            <span v-if="detail.targetName">回复 @{{ detail.targetName }}</span>
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
import AdminStateTag from '~/components/AdminStateTag.vue';
import { COMMENT_STATE_MAP, useCommentAdminApi, type CommentRow } from '~/composables/useCommentAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getComments, getComment, deleteComments } = useCommentAdminApi();

// ---------- 搜索（content 后端 like；articleId 输入是字符串，fetch 里转数字） ----------
const searchFields: AdminSearchField[] = [
  { key: 'content', label: '评论内容', type: 'input', placeholder: '请输入关键词' },
  { key: 'articleId', label: '文章 ID', type: 'input', placeholder: '请输入数字 ID', width: 140 },
  { key: 'state', label: '状态', type: 'select', width: 140, options: [
    { label: '正常', value: 0 },
    { label: '已删除', value: 1 }
  ] }
];

const fetch = (params: Record<string, any>) =>
  getComments({
    ...params,
    articleId: params.articleId != null && params.articleId !== '' ? Number(params.articleId) : undefined
  });

// ---------- 删除（物理级联：子评论一并不可见；批量/单条共用） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const checkedKeys = ref<Array<string | number>>([]);
const deletingId = ref<number | null>(null);
const batchDeleting = ref(false);

const runDelete = async (ids: Array<string | number>) => {
  await deleteComments(ids);
  message.success(`已删除 ${ids.length} 条评论`);
  await listRef.value?.refresh();
};

const doDelete = (row: CommentRow) => {
  dialog.warning({
    title: '删除评论',
    content: '删除后其子评论将一并不可见，确认删除这条评论？',
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
    title: '批量删除评论',
    content: `删除后其子评论将一并不可见，确认删除选中的 ${ids.length} 条评论？`,
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

// ---------- 详情抽屉（内容列点击打开） ----------
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<CommentRow | null>(null);
const openDetail = async (row: CommentRow) => {
  detailVisible.value = true;
  detailLoading.value = true;
  detail.value = null; // 先清空，避免残留上一条内容
  try {
    detail.value = await getComment(row.id);
  } catch {
    detail.value = null; // useApi 已弹错，失败留空态，不回退列表数据
  } finally {
    detailLoading.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<CommentRow> = [
  {
    title: '评论内容',
    key: 'content',
    minWidth: 300,
    render: (row) =>
      h('div', { class: 'cell-comment' }, [
        // 内容即详情入口，两行截断；样式类在 admin-table.scss
        h('div', { class: 'cell-comment-content cell-clamp-2 is-link', onClick: () => openDetail(row), title: '查看详情' }, row.content),
        row.targetName ? h('div', { class: 'cell-comment-sub' }, `回复 @${row.targetName}`) : null
      ])
  },
  {
    title: '评论人',
    key: 'commentName',
    width: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.commentAvatar, name: row.commentName, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.commentName || '—')
      ])
  },
  {
    title: '所在文章',
    key: 'articleName',
    width: 160,
    render: (row) =>
      h(
        'a',
        {
          class: 'cell-ellipsis is-link',
          href: `https://qixidi.top/articles/${row.articleId}`,
          target: '_blank',
          title: row.articleName || `文章 #${row.articleId}`
        },
        row.articleName || `#${row.articleId}`
      )
  },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: COMMENT_STATE_MAP })
  },
  {
    title: '发布时间',
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
    display: flex;
    gap: 16px;
    margin-top: 12px;
    font-size: 12px;
    opacity: 0.55;
  }
}
</style>
