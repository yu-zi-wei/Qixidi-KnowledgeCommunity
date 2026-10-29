<template>
  <div class="comment-page">
    <AdminPageHeader title="评论管理" description="巡查社区评论，处理违规内容" :stats="[{ label: '总数', value: total }]">
      <template #actions>
        <n-button quaternary circle :loading="loading" @click="reload">
          <template #icon><n-icon><Refresh /></n-icon></template>
        </n-button>
      </template>
    </AdminPageHeader>

    <n-card :bordered="false" class="table-card">
      <div class="toolbar">
        <div class="toolbar-left">
          <n-input
            v-model:value="keyword"
            class="search-input"
            round
            clearable
            placeholder="搜索评论内容"
            @keyup.enter="handleSearch"
            @clear="handleSearch"
          >
            <template #prefix><n-icon :component="Search" /></template>
          </n-input>
          <n-input-number
            v-model:value="articleIdFilter"
            class="article-input"
            clearable
            :show-button="false"
            placeholder="文章 ID"
            @update:value="handleSearch"
          />
        </div>

        <n-button v-if="checkedKeys.length" size="small" type="error" tertiary :loading="deleting" @click="confirmDeleteSelected">
          <template #icon><n-icon><Trash /></n-icon></template>
          删除所选（{{ checkedKeys.length }}）
        </n-button>
      </div>

      <n-data-table
        v-model:checked-row-keys="checkedKeys"
        remote
        :columns="columns"
        :data="rows"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: CommentRow) => row.id"
        :scroll-x="960"
        size="small"
        striped
      />
    </n-card>

    <!-- 评论详情：全文 + 元信息 -->
    <n-modal v-model:show="detailVisible" preset="card" title="评论详情" :style="{ width: '520px' }">
      <div v-if="detail" class="detail-body">
        <div class="detail-head">
          <AdminAvatar :src="detail.commentAvatar" :name="detail.commentName" :size="40" class="detail-avatar" />
          <div>
            <div class="detail-name">{{ detail.commentName || '—' }}</div>
            <div class="detail-time">{{ getFullDateTime(detail.createTime) }}</div>
          </div>
          <AdminStateTag :value="detail.state" :map="COMMENT_STATE_MAP" class="detail-state" />
        </div>
        <n-divider />
        <p class="detail-content">{{ detail.content }}</p>
        <div class="detail-meta">
          <span>文章 #{{ detail.articleId }}</span>
          <span v-if="detail.targetName">回复 @{{ detail.targetName }}</span>
        </div>
      </div>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton, NIcon, NTooltip, useDialog, useMessage } from 'naive-ui';
import { Eye, Refresh, Search, Trash } from '@vicons/tabler';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminStateTag, { type StateMeta } from '~/components/AdminStateTag.vue';
import AdminTimeText from '~/components/AdminTimeText.vue';
import { COMMENT_STATE_MAP, useCommentAdminApi, type CommentRow } from '~/composables/useCommentAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const dialog = useDialog();
const { getComments, getComment, deleteComments } = useCommentAdminApi();

const rows = ref<CommentRow[]>([]);
const loading = ref(false);
const total = ref(0);

const keyword = ref('');
const articleIdFilter = ref<number | null>(null);
const checkedKeys = ref<Array<number | string>>([]);

const pageNum = ref(1);
const pageSize = ref(10);
const pagination = computed(() => ({
  page: pageNum.value,
  pageSize: pageSize.value,
  itemCount: total.value,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  onChange: (page: number) => {
    pageNum.value = page;
    loadList();
  },
  onUpdatePageSize: (size: number) => {
    pageSize.value = size;
    pageNum.value = 1;
    loadList();
  }
}));

const loadList = async () => {
  loading.value = true;
  try {
    const params: Record<string, any> = { pageNum: pageNum.value, pageSize: pageSize.value };
    if (keyword.value.trim()) params.content = keyword.value.trim();
    if (articleIdFilter.value) params.articleId = articleIdFilter.value;
    const res = await getComments(params);
    rows.value = res.rows || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  pageNum.value = 1;
  checkedKeys.value = [];
  loadList();
};

const reload = () => {
  checkedKeys.value = [];
  loadList();
};

/** 批量/单条删除共用（路径参数逗号分隔）；级联影响统一走 dialog 确认 */
const deleting = ref(false);
const runDelete = async (ids: Array<number | string>) => {
  deleting.value = true;
  try {
    await deleteComments(ids);
    message.success(`已删除 ${ids.length} 条评论`);
    reload();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    deleting.value = false;
  }
};

const confirmDeleteSelected = () => {
  dialog.warning({
    title: '批量删除评论',
    content: `删除后其子评论将一并不可见，确认删除选中的 ${checkedKeys.value.length} 条评论？`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: () => runDelete(checkedKeys.value)
  });
};

const deleteSingle = (id: number | string) => {
  dialog.warning({
    title: '删除评论',
    content: '删除后其子评论将一并不可见，确认删除这条评论？',
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: () => runDelete([id])
  });
};

const detailVisible = ref(false);
const detail = ref<CommentRow | null>(null);
const openDetail = async (row: CommentRow) => {
  detailVisible.value = true;
  detail.value = row;
  try {
    detail.value = await getComment(row.id);
  } catch {
    /* 保留行数据兜底 */
  }
};

const columns: DataTableColumns<CommentRow> = [
  { type: 'selection' },
  {
    title: '评论人',
    key: 'commentName',
    width: 140,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.commentAvatar, name: row.commentName, size: 22 }),
        h('span', { class: 'cell-author-name' }, row.commentName || '—')
      ])
  },
  {
    title: '内容',
    key: 'content',
    minWidth: 240,
    ellipsis: { tooltip: false },
    render: (row) =>
      h(
        NTooltip,
        { trigger: 'hover', style: { maxWidth: '420px' } },
        {
          trigger: () => h('span', { class: 'cell-ellipsis' }, row.content),
          default: () => row.content
        }
      )
  },
  {
    title: '回复对象',
    key: 'targetName',
    width: 120,
    render: (row) => (row.targetName ? h('span', { class: 'comment-reply-cell' }, `@${row.targetName}`) : h('span', { class: 'cell-dim' }, '—'))
  },
  { title: '文章', key: 'articleId', width: 90, render: (row) => h('span', { class: 'cell-dim' }, `#${row.articleId}`) },
  {
    title: '状态',
    key: 'state',
    width: 90,
    render: (row) => h(AdminStateTag, { value: row.state, map: COMMENT_STATE_MAP as Record<string | number, StateMeta> })
  },
  {
    title: '时间',
    key: 'createTime',
    width: 110,
    render: (row) => h(AdminTimeText, { time: row.createTime })
  },
  {
    title: '操作',
    key: 'actions',
    width: 140,
    fixed: 'right',
    render: (row) =>
      h('div', { class: 'cell-actions' }, [
        h(NButton, { size: 'tiny', quaternary: true, onClick: () => openDetail(row) }, { icon: () => h(NIcon, null, { default: () => h(Eye) }), default: () => '详情' }),
        h(
          NButton,
          { size: 'tiny', quaternary: true, type: 'error', onClick: () => deleteSingle(row.id) },
          { icon: () => h(NIcon, null, { default: () => h(Trash) }), default: () => '删除' }
        )
      ])
  }
];

onMounted(() => {
  loadList();
});
</script>

<style scoped lang="scss">
.table-card {
  border-radius: 10px;
}

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 14px;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.search-input {
  width: 240px;
}

.article-input {
  width: 130px;
}

.detail-body {
  .detail-head {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .detail-name {
    font-weight: 600;
  }

  .detail-time {
    font-size: 12px;
    opacity: 0.5;
  }

  .detail-state {
    margin-left: auto;
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

<!-- 列 render 函数里的元素带不上 scoped hash，列内专属样式放非 scoped 块 -->
<style lang="scss">
.comment-reply-cell {
  font-size: 12.5px;
  opacity: 0.75;
}
</style>
