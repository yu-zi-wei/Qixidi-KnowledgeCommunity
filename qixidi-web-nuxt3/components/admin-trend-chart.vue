<template>
  <div class="trend-chart">
    <div class="trend-header">
      <h4 class="trend-title">{{ title }}</h4>
      <div class="trend-legend">
        <span v-for="(s, i) in series" :key="s.name" class="trend-legend-item">
          <span class="trend-legend-dot" :style="{ background: seriesColors[i] }" />
          {{ s.name }}
        </span>
      </div>
    </div>

    <div v-if="dates.length === 0" class="trend-empty">
      <n-empty size="small" description="暂无数据" />
    </div>

    <div v-else class="trend-body">
      <div class="trend-plot" @mousemove="handleMove" @mouseleave="hoverIndex = -1">
        <svg class="trend-svg" viewBox="0 0 100 40" preserveAspectRatio="none">
          <!-- 水平网格线 -->
          <line v-for="y in gridYs" :key="y" x1="0" :y1="y" x2="100" :y2="y" class="trend-grid" />
          <!-- 序列折线 -->
          <polyline
            v-for="(s, i) in series"
            :key="s.name"
            :points="toPoints(s.values)"
            class="trend-line"
            :style="{ stroke: seriesColors[i] }"
          />
          <!-- 悬浮竖线 -->
          <line v-if="hoverIndex >= 0" :x1="hoverX" y1="0" :x2="hoverX" y2="40" class="trend-cursor" />
        </svg>

        <!-- 悬浮提示 -->
        <div v-if="hoverIndex >= 0" class="trend-tooltip" :style="tooltipStyle">
          <div class="trend-tooltip-date">{{ dates[hoverIndex] }}</div>
          <div v-for="(s, i) in series" :key="s.name" class="trend-tooltip-row">
            <span class="trend-legend-dot" :style="{ background: seriesColors[i] }" />
            {{ s.name }}：{{ s.values[hoverIndex] || 0 }}{{ unit }}
          </div>
        </div>
      </div>

      <div class="trend-labels">
        <span v-for="label in axisLabels" :key="label">{{ label }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface TrendSeries {
  name: string
  values: number[]
}

const props = withDefaults(defineProps<{
  title: string
  dates: string[]
  series: TrendSeries[]
  unit?: string
}>(), {
  unit: '次'
})

const seriesColors = ['var(--color-primary)', 'var(--color-accent)']
const hoverIndex = ref(-1)

// y 轴上限：峰值向上留 15% 空间，最低 4 保证全 0 时平线不贴顶
const yMax = computed(() => {
  const max = Math.max(4, ...props.series.flatMap(s => s.values || []))
  return Math.ceil(max * 1.15)
})

const toPoints = (values: number[]) => {
  const n = props.dates.length
  if (n === 0) return ''
  return (values || []).map((v, i) => {
    const x = n === 1 ? 0 : (i / (n - 1)) * 100
    const y = 38 - (Math.min(v, yMax.value) / yMax.value) * 36
    return `${x.toFixed(2)},${y.toFixed(2)}`
  }).join(' ')
}

const gridYs = [2, 13.3, 24.6, 36]

// x 轴标签：等间隔取 5 个日期（MM-DD）
const axisLabels = computed(() => {
  const n = props.dates.length
  if (n === 0) return []
  const count = Math.min(5, n)
  return Array.from({ length: count }, (_, i) => {
    const idx = Math.round((i / (count - 1)) * (n - 1))
    return props.dates[idx].slice(5)
  })
})

const hoverX = computed(() => {
  const n = props.dates.length
  if (hoverIndex.value < 0 || n === 0) return 0
  return n === 1 ? 0 : (hoverIndex.value / (n - 1)) * 100
})

const tooltipStyle = computed(() => ({
  left: `${Math.min(Math.max(hoverX.value, 12), 88)}%`
}))

const handleMove = (e: MouseEvent) => {
  const rect = (e.currentTarget as HTMLElement).getBoundingClientRect()
  const ratio = (e.clientX - rect.left) / rect.width
  const n = props.dates.length
  if (n === 0) return
  hoverIndex.value = Math.min(n - 1, Math.max(0, Math.round(ratio * (n - 1))))
}
</script>

<style scoped>
.trend-chart {
  min-width: 0;
}

.trend-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}

.trend-title {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.trend-legend {
  display: flex;
  gap: 14px;
}

.trend-legend-item {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.trend-legend-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.trend-empty {
  padding: 24px 0;
}

.trend-plot {
  position: relative;
  height: 150px;
  cursor: crosshair;
}

.trend-svg {
  width: 100%;
  height: 100%;
  display: block;
}

.trend-grid {
  stroke: var(--color-border-light);
  stroke-width: 0.15;
}

.trend-line {
  fill: none;
  stroke-width: 1;
  vector-effect: non-scaling-stroke;
  stroke-linejoin: round;
  stroke-linecap: round;
}

.trend-cursor {
  stroke: var(--color-ink-faint);
  stroke-width: 0.2;
}

.trend-tooltip {
  position: absolute;
  top: 4px;
  transform: translateX(-50%);
  background: var(--color-surface);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-md);
  padding: 6px 10px;
  pointer-events: none;
  white-space: nowrap;
  z-index: 1;
}

.trend-tooltip-date {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin-bottom: 2px;
}

.trend-tooltip-row {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-xs);
  color: var(--color-ink);
}

.trend-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}
</style>
