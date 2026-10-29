<template>
  <div class="dashboard">
    <n-spin :show="loading">
      <div class="flex flex-col gap-5">
        <!-- 指标卡片 -->
        <n-grid cols="2 s:3 m:6" responsive="screen" :x-gap="12" :y-gap="12">
          <n-gi v-for="card in cards" :key="card.label">
            <n-card size="small">
              <div class="flex items-center justify-between">
                <div>
                  <div class="stat-label">{{ card.label }}</div>
                  <div class="stat-value">{{ card.value ?? '—' }}</div>
                </div>
                <n-icon :size="28" class="stat-icon">
                  <component :is="card.icon" />
                </n-icon>
              </div>
            </n-card>
          </n-gi>
        </n-grid>

        <!-- 月度趋势 -->
        <n-card title="月度趋势" size="small">
          <template #header-extra>
            <span class="text-xs opacity-50">近 6 个月 · 总量口径</span>
          </template>
          <div class="mb-3 flex justify-end">
            <n-select
              v-model:value="selectedMetrics"
              multiple
              :options="metricOptions"
              placeholder="选择趋势指标"
              class="w-80"
              :max-tag-count="2"
            />
          </div>
          <BaseChart :option="trendOption" />
        </n-card>

        <!-- 标签/分类分布 -->
        <n-grid cols="1 l:2" responsive="screen" :x-gap="12">
          <n-gi>
            <n-card title="分类文章分布" size="small">
              <BaseChart :option="groupOption" />
            </n-card>
          </n-gi>
          <n-gi>
            <n-card title="标签文章数 Top 10" size="small">
              <BaseChart :option="labelOption" />
            </n-card>
          </n-gi>
        </n-grid>
      </div>
    </n-spin>
  </div>
</template>

<script setup lang="ts">
import { FileText, Notebook, Quote, Tags, UserCheck, Users } from '@vicons/tabler';
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
  { label: '标签数', value: statInfo.value?.labelCount, icon: Tags },
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
  xAxis: { type: 'category', data: trendTimes.value },
  yAxis: { type: 'value', minInterval: 1 },
  series: selectedMetrics.value.map((name) => ({
    name,
    type: 'line',
    smooth: true,
    data: trendTimes.value.map(
      (t) => theStat.value.find((i) => i.name === name && i.time === t)?.data ?? null
    )
  }))
}));

// ---- 分类文章分布（饼图）----
const groupOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'item' },
  legend: { type: 'scroll', orient: 'vertical', right: 0, top: 'middle' },
  series: [
    {
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['38%', '50%'],
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
  grid: { left: 8, right: 24, top: 16, bottom: 8, containLabel: true },
  xAxis: { type: 'value', minInterval: 1 },
  yAxis: { type: 'category', inverse: true, data: topLabels.value.map((i) => i.name) },
  series: [{ type: 'bar', barMaxWidth: 18, data: topLabels.value.map((i) => i.data) }]
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
.stat-label {
  font-size: 13px;
  opacity: 0.6;
}

.stat-value {
  font-size: 26px;
  font-weight: 600;
  line-height: 1.4;
}

.stat-icon {
  opacity: 0.45;
}
</style>
