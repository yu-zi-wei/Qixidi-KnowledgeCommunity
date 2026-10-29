<template>
  <div class="article-page">
    <AdminPageHeader
      title="文章管理"
      description="审核社区文章内容，维护文章元信息"
      :stats="[
        { label: '审核中', value: pendingCount, accent: pendingCount > 0 },
        { label: '总数', value: total }
      ]"
    >
      <template #actions>
        <n-button quaternary circle :loading="loading" @click="reload">
          <template #icon><n-icon><Refresh /></n-icon></template>
        </n-button>
      </template>
    </AdminPageHeader>

    <n-card :bordered="false" class="table-card">
      <div class="toolbar">
        <n-input
          v-model:value="keyword"
          class="search-input"
          round
          clearable
          placeholder="搜索文章标题"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix><n-icon :component="Search" /></template>
        </n-input>

        <n-radio-group v-model:value="auditFilter" size="small" @update:value="handleSearch">
          <n-radio-button v-for="o in auditOptions" :key="o.value ?? 'all'" :value="o.value ?? null" :label="o.label" />
        </n-radio-group>
      </div>

      <n-data-table
        remote
        :columns="columns"
        :data="rows"
        :loading="loading"
        :pagination="pagination"
        :row-key="(row: ArticleRow) => row.id"
        :scroll-x="1180"
        size="small"
        striped
      />
    </n-card>

    <!-- 文章详情抽屉：teleport 组件宽度必须 inline style -->
    <n-drawer v-model:show="detailVisible" :width="520" placement="right">
      <n-drawer-content :title="detail?.articleTitle || '文章详情'" closable>
        <n-spin :show="detailLoading">
          <div v-if="detail" class="detail-body">
            <div class="detail-meta">
              <div class="meta-row"><span class="meta-label">作者</span><span>{{ detail.nickname || detail.username || '—' }}</span></div>
              <div class="meta-row"><span class="meta-label">分类</span><span>{{ detail.groupingName || '—' }}</span></div>
              <div class="meta-row">
                <span class="meta-label">标签</span>
                <span>
                  <n-tag v-for="t in detail.labelNameList || []" :key="t" size="small" :bordered="false" class="detail-tag">{{ t }}</n-tag>
                  <span v-if="!detail.labelNameList?.length">—</span>
                </span>
              </div>
              <div class="meta-row"><span class="meta-label">审核状态</span><AdminStateTag :value="detail.auditState" :map="AUDIT_STATE_MAP" /></div>
              <div class="meta-row"><span class="meta-label">发布时间</span><span>{{ getFullDateTime(detail.createTime) || '—' }}</span></div>
              <div class="meta-row"><span class="meta-label">数据</span><span>{{ detail.numberTimes ?? 0 }} 浏览 · {{ detail.likeTimes ?? 0 }} 赞 · {{ detail.commentTimes ?? 0 }} 评论</span></div>
              <div v-if="detail.reprintUrl" class="meta-row"><span class="meta-label">转载自</span><span class="reprint-url">{{ detail.reprintUrl }}</span></div>
            </div>
            <n-divider>摘要</n-divider>
            <p class="detail-abstract">{{ detail.articleAbstract || '（无摘要）' }}</p>
          </div>
        </n-spin>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton, NIcon, NTag, NTooltip, useMessage } from 'naive-ui';
import { Check, Eye, Refresh, Search, X } from '@vicons/tabler';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminStateTag, { type StateMeta } from '~/components/AdminStateTag.vue';
import AdminTimeText from '~/components/AdminTimeText.vue';
import { AUDIT_STATE_MAP, useArticleAdminApi, type ArticleRow } from '~/composables/useArticleAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const { getArticles, getArticle, updateArticle } = useArticleAdminApi();

const rows = ref<ArticleRow[]>([]);
const loading = ref(false);
const total = ref(0);
const pendingCount = ref<number | null>(null);

const keyword = ref('');
const auditFilter = ref<number | null>(null);
const auditOptions: Array<{ label: string; value: number | null }> = [
  { label: '全部', value: null },
  { label: '审核中', value: 1 },
  { label: '已通过', value: 2 },
  { label: '未通过', value: 3 },
  { label: '草稿', value: 4 }
];

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
    if (keyword.value.trim()) params.articleTitle = keyword.value.trim();
    if (auditFilter.value !== null) params.auditState = auditFilter.value;
    const res = await getArticles(params);
    rows.value = res.rows || [];
    total.value = res.total || 0;
  } finally {
    loading.value = false;
  }
};

/** 待审核数（轻查询：auditState=1 取 total） */
const loadPendingCount = async () => {
  try {
    const res = await getArticles({ pageNum: 1, pageSize: 1, auditState: 1 });
    pendingCount.value = res.total || 0;
  } catch {
    pendingCount.value = null;
  }
};

const handleSearch = () => {
  pageNum.value = 1;
  loadList();
};

const reload = () => {
  loadList();
  loadPendingCount();
};

/** 审核动作：EditGroup 必填字段需齐全，行内缺 content 时先取详情 */
const auditingId = ref<number | null>(null);
const doAudit = async (row: ArticleRow, auditState: number) => {
  auditingId.value = row.id;
  try {
    let payload: Partial<ArticleRow> = { ...row };
    if (!payload.articleContent) {
      const detail = await getArticle(row.id);
      payload = { ...payload, ...detail };
    }
    await updateArticle({ ...payload, auditState });
    message.success(auditState === 2 ? '已通过审核' : '已拒绝');
    reload();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    auditingId.value = null;
  }
};

/** 详情抽屉 */
const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<ArticleRow | null>(null);
const openDetail = async (row: ArticleRow) => {
  detailVisible.value = true;
  detailLoading.value = true;
  try {
    detail.value = await getArticle(row.id);
  } catch {
    detail.value = row;
  } finally {
    detailLoading.value = false;
  }
};

const columns: DataTableColumns<ArticleRow> = [
  {
    title: '文章',
    key: 'articleTitle',
    minWidth: 260,
    render: (row) =>
      h('div', { class: 'article-cell' }, [
        h(AdminAvatar, { src: row.articleCover, name: row.articleTitle, size: [48, 32], shape: 'square' }),
        h(
          NTooltip,
          { trigger: 'hover' },
          {
            trigger: () => h('span', { class: 'article-title' }, row.articleTitle),
            default: () => row.articleTitle
          }
        )
      ])
  },
  {
    title: '作者',
    key: 'nickname',
    width: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname || row.username, size: 22 }),
        h('span', { class: 'cell-author-name' }, row.nickname || row.username || '—')
      ])
  },
  { title: '分类', key: 'groupingName', width: 100, render: (row) => row.groupingName || h('span', { class: 'cell-dim' }, '—') },
  {
    title: '标签',
    key: 'labelNameList',
    width: 140,
    render: (row) =>
      (row.labelNameList || []).length
        ? h(
            'div',
            { class: 'article-label-cell' },
            (row.labelNameList || []).slice(0, 2).map((t) => h(NTag, { size: 'tiny', bordered: false }, { default: () => t }))
          )
        : h('span', { class: 'cell-dim' }, '—')
  },
  {
    title: '互动',
    key: 'numberTimes',
    width: 150,
    render: (row) =>
      h('span', { class: 'article-stat-cell' }, `${row.numberTimes ?? 0} 浏览 · ${row.likeTimes ?? 0} 赞 · ${row.commentTimes ?? 0} 评`)
  },
  {
    title: '审核状态',
    key: 'auditState',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.auditState, map: AUDIT_STATE_MAP as Record<string | number, StateMeta> })
  },
  {
    title: '发布时间',
    key: 'createTime',
    width: 110,
    render: (row) => h(AdminTimeText, { time: row.createTime })
  },
  {
    title: '操作',
    key: 'actions',
    width: 170,
    fixed: 'right',
    render: (row) => {
      const buttons = [
        h(
          NButton,
          { size: 'tiny', quaternary: true, onClick: () => openDetail(row) },
          { icon: () => h(NIcon, null, { default: () => h(Eye) }), default: () => '详情' }
        )
      ];
      if (row.auditState === 1) {
        buttons.push(
          h(
            NButton,
            {
              size: 'tiny',
              quaternary: true,
              type: 'success',
              loading: auditingId.value === row.id,
              onClick: () => doAudit(row, 2)
            },
            { icon: () => h(NIcon, null, { default: () => h(Check) }), default: () => '通过' }
          ),
          h(
            NButton,
            {
              size: 'tiny',
              quaternary: true,
              type: 'error',
              loading: auditingId.value === row.id,
              onClick: () => doAudit(row, 3)
            },
            { icon: () => h(NIcon, null, { default: () => h(X) }), default: () => '拒绝' }
          )
        );
      }
      return h('div', { class: 'cell-actions' }, buttons);
    }
  }
];

onMounted(() => {
  reload();
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

.search-input {
  width: 260px;
}

.detail-body {
  .detail-meta {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .meta-row {
    display: flex;
    align-items: baseline;
    gap: 12px;
    font-size: 13px;
  }

  .meta-label {
    width: 60px;
    flex-shrink: 0;
    opacity: 0.5;
  }

  .detail-tag {
    margin-right: 4px;
  }

  .reprint-url {
    word-break: break-all;
    opacity: 0.7;
  }

  .detail-abstract {
    margin: 0;
    font-size: 13.5px;
    line-height: 1.7;
    opacity: 0.85;
    white-space: pre-wrap;
  }
}
</style>

<!-- 列 render 函数里的元素带不上 scoped hash，列内专属样式放非 scoped 块（类名带页面语义前缀防跨页冲突） -->
<style lang="scss">
.article-cell {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.article-title {
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.article-label-cell {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.article-stat-cell {
  font-size: 12px;
  opacity: 0.65;
  font-variant-numeric: tabular-nums;
}
</style>
