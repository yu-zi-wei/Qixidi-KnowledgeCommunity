<template>
  <AdminListPage
    ref="listRef"
    v-model:checked-keys="checkedKeys"
    :search-fields="searchFields"
    :columns="columns"
    :fetch="getArticles"
    row-key="id"
    seq
    selectable
  >
    <!-- 工具栏约定：批量操作在左，新增/编辑等在右 -->
    <template #toolbar-left>
      <n-button
        v-if="hasPerm('article:information:remove')"
        size="small"
        type="error"
        secondary
        :disabled="!checkedKeys.length"
        :loading="batchRemoving"
        @click="doRemoveBatch"
      >
        删除所选（{{ checkedKeys.length }}）
      </n-button>
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
const dialog = useDialog();
const { hasPerm } = usePerm();
const { getArticles, getGroupings, getArticle, auditArticles, removeArticles } = useArticleAdminApi();

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
});

// ---------- 审核动作（独立 audit 接口，只传 ids + 结果，无需文章全字段） ----------
const listRef = ref<InstanceType<typeof AdminListPage> | null>(null);
const auditingId = ref<number | null>(null);

// 操作列所有按钮执行前一律二次确认
const doAudit = (row: ArticleRow, auditState: number) => {
  const approve = auditState === 2;
  dialog[approve ? 'info' : 'warning']({
    title: approve ? '通过审核' : '拒绝审核',
    content: approve
      ? `确定通过《${row.articleTitle}》的审核吗？通过后文章将对外可见。`
      : `确定拒绝《${row.articleTitle}》的审核吗？该文章将标记为未通过。`,
    positiveText: approve ? '通过' : '拒绝',
    negativeText: '取消',
    onPositiveClick: () => auditExec(row, auditState)
  });
};

const auditExec = async (row: ArticleRow, auditState: number) => {
  auditingId.value = row.id;
  try {
    await auditArticles([row.id], auditState);
    message.success(auditState === 2 ? '已通过审核' : '已拒绝');
    await listRef.value?.refresh();
  } catch {
    /* useApi 已统一弹错 */
  } finally {
    auditingId.value = null;
  }
};

// ---------- 删除（物理删除，不可恢复） ----------
const removingId = ref<number | null>(null);
const batchRemoving = ref(false);
// AdminListPage defineModel 双向绑定的选中 keys（loadList 后组件侧自动清空）
const checkedKeys = ref<Array<string | number>>([]);

const doRemove = (row: ArticleRow) => {
  dialog.warning({
    title: '删除文章',
    content: `确定删除《${row.articleTitle}》吗？删除后不可恢复。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      removingId.value = row.id;
      try {
        await removeArticles([row.id]);
        message.success('已删除');
        await listRef.value?.refresh();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        removingId.value = null;
      }
    }
  });
};

const doRemoveBatch = () => {
  const ids = checkedKeys.value as number[];
  dialog.warning({
    title: '批量删除文章',
    content: `确定删除所选 ${ids.length} 篇文章吗？删除后不可恢复。`,
    positiveText: '删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      batchRemoving.value = true;
      try {
        await removeArticles(ids);
        message.success('已删除');
        await listRef.value?.refresh();
      } catch {
        /* useApi 已统一弹错 */
      } finally {
        batchRemoving.value = false;
      }
    }
  });
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
            // 标题即详情入口（详情按钮已取消），样式类在 admin-table.scss
            h('span', { class: 'cell-article-name is-link', onClick: () => openDetail(row), title: '查看详情' }, row.articleTitle),
            h(NTag, { size: 'tiny', bordered: false, type: row.type === 2 ? 'warning' : 'default' }, { default: () => (row.type === 2 ? '转载' : '原创') })
          ]),
          h('div', { class: 'cell-article-abstract' }, row.articleAbstract || '（无摘要）')
        ])
      ])
  },
  {
    title: '作者',
    key: 'nickname',
    minWidth: 130,
    render: (row) =>
      h('div', { class: 'cell-author' }, [
        h(AdminAvatar, { src: row.avatar, name: row.nickname || row.username, size: 24 }),
        h('span', { class: 'cell-author-name' }, row.nickname || row.username || '—')
      ])
  },
  {
    title: '分类',
    key: 'groupingName',
    minWidth: 110,
    ellipsis: { tooltip: true },
    render: (row) => row.groupingName || '未分类'
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
    render: (row) => getFullDateTime(row.createTime) || '—'
  },
  {
    // 操作列用纯文字链接（带图标时按钮易超列宽被截断）；fixed 随横向滚动常驻右侧
    title: '操作',
    key: 'actions',
    width: 130,
    align: 'center',
    fixed: 'right',
    render: (row) => {
      const btns = [];
      if (row.auditState === 1 && hasPerm('article:information:edit')) {
        btns.push(
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
        );
      }
      if (hasPerm('article:information:remove')) {
        btns.push(
          h(
            NButton,
            { size: 'small', text: true, type: 'error', loading: removingId.value === row.id, onClick: () => doRemove(row) },
            { default: () => '删除' }
          )
        );
      }
      return h('div', { class: 'cell-btns' }, btns);
    }
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
