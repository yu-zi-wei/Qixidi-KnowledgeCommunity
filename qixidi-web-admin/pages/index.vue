<template>
  <div class="dashboard">
    <n-spin :show="loading">
      <div class="dashboard-body">
        <!-- 指标卡片（4×2） -->
        <n-grid cols="2 s:4" responsive="screen" :x-gap="12" :y-gap="12">
          <n-gi v-for="card in cards" :key="card.label">
            <n-card size="small" class="stat-card">
              <div class="stat-row">
                <div class="stat-info">
                  <div class="stat-label">{{ card.label }}</div>
                  <div class="stat-value">{{ card.value ?? '—' }}</div>
                </div>
                <div class="stat-icon-wrap">
                  <n-icon :size="22">
                    <component :is="card.icon" />
                  </n-icon>
                </div>
              </div>
            </n-card>
          </n-gi>
        </n-grid>

        <!-- 月度趋势（全宽） -->
        <n-card title="月度趋势" size="small">
          <template #header-extra>
            <div class="trend-extra">
              <span class="trend-hint">近 6 个月 · 总量口径</span>
              <n-select
                v-model:value="selectedMetrics"
                multiple
                :options="metricOptions"
                placeholder="选择趋势指标"
                class="trend-select"
                :max-tag-count="2"
              />
            </div>
          </template>
          <BaseChart :option="trendOption" height="300px" />
        </n-card>

        <!-- 分类 / 标签分布（两列） -->
        <n-grid cols="1 l:2" responsive="screen" :x-gap="12" :y-gap="12">
          <n-gi>
            <n-card title="分类文章分布" size="small">
              <template #header-extra>
                <span class="trend-hint">{{ labelStat?.labelGroup?.length || 0 }} 个分类</span>
              </template>
              <BaseChart :option="groupOption" height="300px" />
            </n-card>
          </n-gi>
          <n-gi>
            <n-card title="标签文章数 Top 10" size="small">
              <template #header-extra>
                <span class="trend-hint">按文章数排序</span>
              </template>
              <BaseChart :option="labelOption" height="300px" />
            </n-card>
          </n-gi>
        </n-grid>
      </div>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { Bookmarks, Columns, FileText, Notebook, Quote, Tags, UserCheck, Users } from '@vicons/tabler';
import type { EChartsOption } from 'echarts';
import type { StatDataInfo, StatReturnItem } from '~/composables/useStatApi';

const { getStatInfo, getTheStat, getLabelStat } = useStatApi();

const loading = ref(false);
const statInfo = ref<StatDataInfo | null>(null);
const theStat = ref<StatReturnItem[]>([]);
const labelStat = ref<{ labelGroup: StatReturnItem[]; label: StatReturnItem[] } | null>(null);

// 指标卡片（字段与 StatDataInfoVo 一一对应）
const cards = computed(() => [
  { label: '用户总数', value: statInfo.value?.userCount, icon: Users },
  { label: '在线用户', value: statInfo.value?.onlineUserCount, icon: UserCheck },
  { label: '文章总数', value: statInfo.value?.articleCount, icon: FileText },
  { label: '专栏数量', value: statInfo.value?.specialCount, icon: Columns },
  { label: '收藏夹数', value: statInfo.value?.favoritesCount, icon: Bookmarks },
  { label: '标签数量', value: statInfo.value?.labelCount, icon: Tags },
  { label: '名言总数', value: statInfo.value?.dictumCount, icon: Quote },
  { label: '时光小记', value: statInfo.value?.timeNotesCount, icon: Notebook }
]);

// ---- 月度趋势 ----
// 接口按月倒序返回（ORDER BY stat_time DESC LIMIT 6），反转为时间正序
const trendTimes = computed(() => [...new Set(theStat.value.map((i) => i.time || ''))].reverse());

const metricOptions = computed(() =>
  [...new Set(theStat.value.map((i) => i.name))].map((name) => ({ label: name, value: name }))
);

const selectedMetrics = ref<string[]>([]);

const trendOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'axis' },
  legend: { bottom: 0, type: 'scroll' },
  grid: { left: 8, right: 16, top: 24, bottom: 48, containLabel: true },
  xAxis: { type: 'category', boundaryGap: false, data: trendTimes.value },
  yAxis: { type: 'value', minInterval: 1 },
  series: selectedMetrics.value.map((name) => ({
    name,
    type: 'line',
    smooth: true,
    symbolSize: 6,
    data: trendTimes.value.map(
      (t) => theStat.value.find((i) => i.name === name && i.time === t)?.data ?? null
    )
  }))
}));

// ---- 分类文章分布（饼图）----
const groupOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'item', formatter: '{b}：{c} 篇（{d}%）' },
  legend: { type: 'scroll', orient: 'vertical', right: 0, top: 'middle' },
  series: [
    {
      type: 'pie',
      radius: ['45%', '72%'],
      center: ['36%', '50%'],
      label: { show: false },
      itemStyle: { borderRadius: 4 },
      data: (labelStat.value?.labelGroup || []).map((i) => ({ name: i.name, value: i.data }))
    }
  ]
}));

// ---- 标签文章数 Top10（横向条形）----
const topLabels = computed(() =>
  (labelStat.value?.label || [])
    .filter((i) => i.twoName === '文章数')
    .sort((a, b) => b.data - a.data)
    .slice(0, 10)
);

const labelOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: 8, right: 40, top: 16, bottom: 8, containLabel: true },
  xAxis: { type: 'value', minInterval: 1 },
  yAxis: { type: 'category', inverse: true, data: topLabels.value.map((i) => i.name) },
  series: [
    {
      type: 'bar',
      barMaxWidth: 18,
      itemStyle: { borderRadius: [0, 4, 4, 0] },
      label: { show: true, position: 'right' },
      data: topLabels.value.map((i) => i.data)
    }
  ]
}));

onMounted(async () => {
  loading.value = true;
  try {
    // 一个接口失败不拖垮整板（useApi 已统一弹错）
    const [info, trend, label] = await Promise.allSettled([getStatInfo(), getTheStat(), getLabelStat()]);
    if (info.status === 'fulfilled') statInfo.value = info.value;
    if (trend.status === 'fulfilled') theStat.value = trend.value || [];
    if (label.status === 'fulfilled') labelStat.value = label.value;
    // 默认勾选：用户/文章趋势（数据里没有的指标不选）
    const available = new Set(theStat.value.map((i) => i.name));
    selectedMetrics.value = ['用户总数', '文章总数'].filter((n) => available.has(n));
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped lang="scss">
.dashboard-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.stat-card {
  transition: transform 0.15s ease, box-shadow 0.15s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--panel-shadow);
}

.stat-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-label {
  font-size: 13px;
  opacity: 0.6;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  line-height: 1.4;
  font-variant-numeric: tabular-nums;
}

/* 墨绿圆底图标（纸墨点缀色，与全站 --cell-link 同源） */
.stat-icon-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 10px;
  flex-shrink: 0;
  color: var(--cell-link);
  background: color-mix(in srgb, var(--cell-link) 12%, transparent);
}

.trend-extra {
  display: flex;
  align-items: center;
  gap: 12px;
}

.trend-hint {
  font-size: 12px;
  opacity: 0.5;
}

.trend-select {
  width: 240px;
}

@media (max-width: 768px) {
  .trend-extra {
    flex-direction: column;
    align-items: flex-end;
    gap: 6px;
  }

  .trend-select {
    width: 180px;
  }
}
</style>
