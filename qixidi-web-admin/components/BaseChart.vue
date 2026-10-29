<template>
  <div ref="chartRef" class="base-chart" :style="{ height }"></div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue';
import * as echarts from 'echarts/core';
import { BarChart, LineChart, PieChart } from 'echarts/charts';
import { GridComponent, LegendComponent, TooltipComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import type { EChartsOption } from 'echarts';
import type { GlobalTheme } from 'naive-ui';

// 按需注册：看板只用到折线/柱状/饼图
echarts.use([LineChart, BarChart, PieChart, GridComponent, TooltipComponent, LegendComponent, CanvasRenderer]);

const props = withDefaults(defineProps<{ option: EChartsOption; height?: string }>(), {
  height: '320px'
});

const chartRef = ref<HTMLDivElement>();
let chart: echarts.ECharts | null = null;
let resizeObserver: ResizeObserver | null = null;

// 跟随布局的主题切换（app.vue provide）重建图表，echarts 内置 dark 主题
const theme = inject<Ref<GlobalTheme | null> | null>('theme', null);
const isDark = computed(() => theme?.value?.name === 'dark');

const render = () => {
  if (!chartRef.value) return;
  if (!chart || chart.isDisposed()) {
    chart = echarts.init(chartRef.value, isDark.value ? 'dark' : undefined);
    resizeObserver = new ResizeObserver(() => chart?.resize());
    resizeObserver.observe(chartRef.value);
  }
  chart.setOption({ backgroundColor: 'transparent', ...props.option }, true);
};

// 主题切换需销毁重建（init 时传入的主题无法后置更改）
watch(isDark, () => {
  chart?.dispose();
  chart = null;
  render();
});

watch(
  () => props.option,
  () => render(),
  { deep: true }
);

onMounted(render);

onBeforeUnmount(() => {
  resizeObserver?.disconnect();
  resizeObserver = null;
  chart?.dispose();
  chart = null;
});
</script>

<style scoped lang="scss">
.base-chart {
  width: 100%;
}
</style>
