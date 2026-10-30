<template>
  <AdminListPage
    ref="listRef"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getArticles"
    row-key="id"
    seq
  >
    <template #toolbar-left>
      <n-tag size="small" :bordered="false" :type="(pendingCount ?? 0) > 0 ? 'warning' : 'default'">
        审核中 {{ pendingCount ?? '—' }} 篇
      </n-tag>
    </template>
  </AdminListPage>

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
            <div class="meta-row">
              <span class="meta-label">前台地址</span>
              <n-button v-if="frontUrl" text type="primary" tag="a" :href="frontUrl" target="_blank" class="detail-front-url">
                {{ frontUrl }}
              </n-button>
              <span v-else>—</span>
            </div>
            <div class="meta-row"><span class="meta-label">数据</span><span>{{ detail.numberTimes ?? 0 }} 浏览 · {{ detail.likeTimes ?? 0 }} 赞 · {{ detail.commentTimes ?? 0 }} 评论</span></div>
            <div v-if="detail.reprintUrl" class="meta-row"><span class="meta-label">转载自</span><span class="reprint-url">{{ detail.reprintUrl }}</span></div>
          </div>
          <n-divider>摘要</n-divider>
          <p class="detail-abstract">{{ detail.articleAbstract || '（无摘要）' }}</p>
        </div>
      </n-spin>
    </n-drawer-content>
  </n-drawer>
</template>

<script setup lang="ts">
import { h } from 'vue';
import { NButton, NTag } from 'naive-ui';
import type { DataTableColumns } from 'naive-ui';
import AdminAvatar from '~/components/AdminAvatar.vue';
import AdminListPage, { type AdminSearchField } from '~/components/AdminListPage.vue';
import AdminStateTag from '~/components/AdminStateTag.vue';
import { AUDIT_STATE_MAP, useArticleAdminApi, type ArticleRow, type GroupingRow } from '~/composables/useArticleAdminApi';
import { getFullDateTime } from '~/utils/format';

definePageMeta({ layout: 'default' });

const message = useMessage();
const { getArticles, getGroupings, getArticle, updateArticle } = useArticleAdminApi();

// ---------- 搜索条件 ----------
const groupingOptions = ref<Array<{ label: string; value: number }>>([]);

const searchFields = computed<AdminSearchField[]>(() => [
  { key: 'articleTitle', label: '文章标题', type: 'input', placeholder: '请输入文章标题' },
  { key: 'nickname', label: '作者', type: 'input', placeholder: '请输入作者昵称' },
  { key: 'types', label: '类型', type: 'multiple', options: [
    { label: '原创', value: 1 },
    { label: '转载', value: 2 }
  ] },
  { key: 'groupingIds', label: '分类', type: 'multiple', placeholder: '请选择分类', options: groupingOptions.value },
  { key: 'auditStates', label: '审核状态', type: 'multiple', options: [
    { label: '审核中', value: 1 },
    { label: '已通过', value: 2 },
    { label: '未通过', value: 3 },
    { label: '草稿', value: 4 }
  ] }
]);

onMounted(async () => {
  try {
    const res = await getGroupings();
    groupingOptions.value = (res.rows || []).map((g: GroupingRow) => ({ label: g.groupingName, value: g.id }));
  } catch {
    /* useApi 已统一弹错，下拉留空即可 */
  }
  loadPendingCount();
});

// ---------- 待审核统计（轻查询） ----------
const pendingCount = ref<number | null>(null);
const loadPendingCount = async () => {
  try {
    const res = await getArticles({ pageNum: 1, pageSize: 1, auditState: 1 });
    pendingCount.value = res.total || 0;
  } catch {
    pendingCount.value = null;
  }
};

// ---------- 审核动作（EditGroup 必填字段需齐全，行内缺 content 时先取详情） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
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
    await listRef.value?.refresh();
    loadPendingCount();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    auditingId.value = null;
  }
};

// ---------- 详情抽屉（一律调详情接口，禁止拿列表行数据展示） ----------
const FRONT_ARTICLE_BASE = 'https://qixidi.top/articles';

const detailVisible = ref(false);
const detailLoading = ref(false);
const detail = ref<ArticleRow | null>(null);
const frontUrl = computed(() => (detail.value?.id != null ? `${FRONT_ARTICLE_BASE}/${detail.value.id}` : ''));
const openDetail = async (row: ArticleRow) => {
  detailVisible.value = true;
  detailLoading.value = true;
  detail.value = null; // 先清空，避免残留上一篇文章内容
  try {
    detail.value = await getArticle(row.id);
  } catch {
    detail.value = null; // useApi 已弹错，失败留空态，不回退列表数据
  } finally {
    detailLoading.value = false;
  }
};

// ---------- 表格列（render 元素样式走全局类/内聚组件，scoped 选不中） ----------
const columns: DataTableColumns<ArticleRow> = [
  {
    title: '文章',
    key: 'articleTitle',
    minWidth: 300,
    render: (row) =>
      h('div', { class: 'cell-article' }, [
        h(AdminAvatar, { src: row.articleCover, name: row.articleTitle, size: [48, 36], shape: 'square' }),
        h('div', { class: 'cell-article-main' }, [
          h('div', { class: 'cell-article-title' }, [
            h('span', { class: 'cell-article-name' }, row.articleTitle),
            h(NTag, { size: 'tiny', bordered: false, type: row.type === 2 ? 'warning' : 'default' }, { default: () => (row.type === 2 ? '转载' : '原创') })
          ]),
          h('div', { class: 'cell-article-abstract' }, row.articleAbstract || '（无摘要）')
        ])
      ])
  },
  {
    title: '作者',
    key: 'nickname',
    width: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname || row.username, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.nickname || row.username || '—')
      ])
  },
  {
    title: '分类',
    key: 'groupingName',
    width: 110,
    ellipsis: { tooltip: true },
    render: (row) => row.groupingName || h('span', { class: 'cell-dim' }, '未分类')
  },
  {
    title: '浏览 / 赞 / 评',
    key: 'numberTimes',
    width: 130,
    render: (row) =>
      h('span', { class: 'cell-stats' }, `${row.numberTimes ?? 0} / ${row.likeTimes ?? 0} / ${row.commentTimes ?? 0}`)
  },
  {
    title: '审核状态',
    key: 'auditState',
    width: 100,
    render: (row) => h(AdminStateTag, { value: row.auditState, map: AUDIT_STATE_MAP })
  },
  {
    title: '发布时间',
    key: 'createTime',
    width: 150,
    render: (row) => h('span', { class: 'cell-dim' }, getFullDateTime(row.createTime) || '—')
  },
  {
    // 操作列按参考图用纯文字链接（带图标时 3 按钮约 160px+，超出列宽会被截断）
    title: '操作',
    key: 'actions',
    width: 150,
    align: 'center',
    render: (row) =>
      h('div', { class: 'cell-btns' }, [
        h(NButton, { size: 'small', text: true, type: 'primary', onClick: () => openDetail(row) }, { default: () => '详情' }),
        ...(row.auditState === 1
          ? [
              h(
                NButton,
                { size: 'small', text: true, type: 'success', loading: auditingId.value === row.id, onClick: () => doAudit(row, 2) },
                { default: () => '通过' }
              ),
              h(
                NButton,
                { size: 'small', text: true, type: 'error', loading: auditingId.value === row.id, onClick: () => doAudit(row, 3) },
                { default: () => '拒绝' }
              )
            ]
          : [])
      ])
  }
];
</script>

<style scoped lang="scss">
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

  .detail-front-url {
    font-size: 13px;
    word-break: break-all;
    text-align: left;
    padding: 0;
    height: auto;
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
