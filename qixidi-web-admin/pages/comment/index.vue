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

        <div class="toolbar-right">
          <n-checkbox v-if="rows.length" :checked="allChecked" :indeterminate="indeterminate" @update:checked="toggleAll">
            全选本页
          </n-checkbox>
          <n-button v-if="checkedKeys.length" size="small" type="error" tertiary :loading="deleting" @click="confirmDeleteSelected">
            <template #icon><n-icon><Trash /></n-icon></template>
            删除所选（{{ checkedKeys.length }}）
          </n-button>
        </div>
      </div>

      <n-spin :show="loading">
        <n-empty v-if="!rows.length && !loading" description="暂无评论" class="list-empty" />
        <div v-else class="admin-row-list">
          <AdminRowCard v-for="row in rows" :key="row.id">
            <template #leading>
              <n-checkbox :checked="checkedKeys.includes(row.id)" @update:checked="(v: boolean) => toggleOne(row.id, v)" />
            </template>
            <template #media>
              <AdminAvatar :src="row.commentAvatar" :name="row.commentName" :size="[40, 40]" />
            </template>

            <p class="cmt-content">{{ row.content }}</p>
            <div class="row-meta">
              <span class="cmt-name">{{ row.commentName || '—' }}</span>
              <template v-if="row.targetName">
                <span class="row-meta-dot">·</span>
                <span>回复 @{{ row.targetName }}</span>
              </template>
              <span class="row-meta-dot">·</span>
              <span>文章 #{{ row.articleId }}</span>
              <span class="row-meta-dot">·</span>
              <span>{{ getFullDateTime(row.createTime) }}</span>
            </div>

            <template #side>
              <AdminStateTag :value="row.state" :map="COMMENT_STATE_MAP" />
              <div class="cell-actions">
                <n-button size="tiny" quaternary @click="openDetail(row)">
                  <template #icon><n-icon><Eye /></n-icon></template>
                  详情
                </n-button>
                <n-button size="tiny" quaternary type="error" @click="deleteSingle(row.id)">
                  <template #icon><n-icon><Trash /></n-icon></template>
                  删除
                </n-button>
              </div>
            </template>
          </AdminRowCard>
        </div>
      </n-spin>

      <div v-if="total" class="list-footer">
        <span class="list-total">共 {{ total }} 条</span>
        <n-pagination
          :page="pageNum"
          :page-size="pageSize"
          :item-count="total"
          :page-sizes="[10, 20, 50]"
          show-size-picker
          @update:page="onPageChange"
          @update:page-size="onPageSizeChange"
        />
      </div>
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
import { Eye, Refresh, Search, Trash } from '@vicons/tabler';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminPageHeader from '~/components/AdminPageHeader.vue';
import AdminRowCard from '~/components/AdminRowCard.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
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
const pageSize = ref(20);

const allChecked = computed(() => rows.value.length > 0 && rows.value.every((r) => checkedKeys.value.includes(r.id)));
const indeterminate = computed(() => {
  const n = rows.value.filter((r) => checkedKeys.value.includes(r.id)).length;
  return n > 0 && n < rows.value.length;
});
const toggleAll = (v: boolean) => {
  const ids = rows.value.map((r) => r.id);
  checkedKeys.value = v ? Array.from(new Set([...checkedKeys.value, ...ids])) : checkedKeys.value.filter((k) => !ids.includes(k as number));
};
const toggleOne = (id: number | string, v: boolean) => {
  checkedKeys.value = v ? [...checkedKeys.value, id] : checkedKeys.value.filter((k) => k !== id);
};

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

const onPageChange = (page: number) => {
  pageNum.value = page;
  loadList();
};

const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  pageNum.value = 1;
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

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 14px;
}

.search-input {
  width: 240px;
}

.article-input {
  width: 130px;
}

.list-empty {
  padding: 48px 0;
}

.cmt-content {
  margin: 0;
  font-size: 13px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.cmt-name {
  font-weight: 500;
  opacity: 0.85;
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
