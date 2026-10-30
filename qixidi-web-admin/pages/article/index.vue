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

      <n-spin :show="loading">
        <n-empty v-if="!rows.length && !loading" description="暂无文章" class="list-empty" />
        <div v-else class="admin-row-list">
          <AdminRowCard v-for="row in rows" :key="row.id">
            <template #media>
              <AdminAvatar :src="row.articleCover" :name="row.articleTitle" :size="[72, 48]" shape="square" />
            </template>

            <div class="art-title-line">
              <span class="art-title">{{ row.articleTitle }}</span>
              <n-tag size="tiny" :bordered="false" :type="row.type === 2 ? 'warning' : 'default'">
                {{ row.type === 2 ? '转载' : '原创' }}
              </n-tag>
            </div>
            <p class="art-abstract">{{ row.articleAbstract || '（无摘要）' }}</p>
            <div class="row-meta">
              <AdminAvatar :src="row.avatar" :name="row.nickname || row.username" :size="18" />
              <span>{{ row.nickname || row.username || '—' }}</span>
              <span class="row-meta-dot">·</span>
              <span>{{ row.groupingName || '未分类' }}</span>
              <n-tag v-for="t in (row.labelNameList || []).slice(0, 3)" :key="t" size="tiny" :bordered="false">{{ t }}</n-tag>
              <span v-if="(row.labelNameList || []).length > 3" class="row-meta-dot">+{{ row.labelNameList!.length - 3 }}</span>
              <span class="row-meta-dot">·</span>
              <span>{{ getFullDateTime(row.createTime) }}</span>
            </div>

            <template #side>
              <div class="art-side-top">
                <span class="art-stat">{{ row.numberTimes ?? 0 }} 浏览</span>
                <span class="art-stat">{{ row.likeTimes ?? 0 }} 赞</span>
                <span class="art-stat">{{ row.commentTimes ?? 0 }} 评</span>
                <AdminStateTag :value="row.auditState" :map="AUDIT_STATE_MAP" />
              </div>
              <div class="cell-actions">
                <n-button size="tiny" quaternary @click="openDetail(row)">
                  <template #icon><n-icon><Eye /></n-icon></template>
                  详情
                </n-button>
                <template v-if="row.auditState === 1">
                  <n-button size="tiny" quaternary type="success" :loading="auditingId === row.id" @click="doAudit(row, 2)">
                    <template #icon><n-icon><Check /></n-icon></template>
                    通过
                  </n-button>
                  <n-button size="tiny" quaternary type="error" :loading="auditingId === row.id" @click="doAudit(row, 3)">
                    <template #icon><n-icon><X /></n-icon></template>
                    拒绝
                  </n-button>
                </template>
              </div>
            </template>
          </AdminRowCard>
        </div>
      </n-spin>

      <div v-if="total" class="list-footer">
        <span class="list-total">共 {{ total }} 篇</span>
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
import { Check, Eye, Refresh, Search, X } from '@vicons/tabler';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminPageHeader from '~/components/AdminPageHeader.vue';
import AdminRowCard from '~/components/AdminRowCard.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
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
const pageSize = ref(20);

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

const onPageChange = (page: number) => {
  pageNum.value = page;
  loadList();
};

const onPageSizeChange = (size: number) => {
  pageSize.value = size;
  pageNum.value = 1;
  loadList();
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

.list-empty {
  padding: 48px 0;
}

.art-title-line {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.art-title {
  font-size: 14px;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.art-abstract {
  margin: 0;
  font-size: 12.5px;
  line-height: 1.5;
  opacity: 0.55;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.art-side-top {
  display: flex;
  align-items: center;
  gap: 10px;
}

.art-stat {
  font-size: 12px;
  opacity: 0.6;
  font-variant-numeric: tabular-nums;
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
