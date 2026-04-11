<template>
  <div class="submission-chart">
    <div class="calendar">
      <div class="weeks">
        <div class="week">周二</div>
        <div class="week">周四</div>
        <div class="week">周六</div>
      </div>
      <div class="column" v-for="(columnData, columnIndex) in dateData" :key="columnIndex">
        <div class="title">{{ columnData.title }}</div>
        <div
          class="date-wrapper"
          v-for="(dateData, dateIndex) in columnData.data"
          :key="dateIndex"
          :style="{ background: getColor(dateData.number) }"
          :title="`${dateData.date} +${dateData.number}`"
        />
      </div>
    </div>
    <div class="operation">
      <div class="slider">
        <div class="slider-label">0</div>
        <div class="slider-total">
          <span class="total-text">总贡献：{{ sumNumber }}+</span>
        </div>
        <div class="slider-month">month：12</div>
      </div>
      <div class="legend">
        <div class="level-desc">少</div>
        <div class="level level-1" />
        <div class="level level-2" />
        <div class="level level-3" />
        <div class="level level-4" />
        <div class="level level-5" />
        <div class="level-desc">多</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'

const props = defineProps<{
  profile?: Record<string, number>
}>()

interface DateItem {
  number: number
  date: string
}

interface ColumnData {
  columns: number
  title: string
  ifSwitchMonth: boolean
  data: DateItem[]
}

const dateData = ref<ColumnData[]>([])
const sumNumber = ref(0)

const monthCN = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']

const init = () => {
  const now = new Date()
  const currentYear = now.getFullYear()

  // 上一年信息
  const prevYear = currentYear - 1
  const prevToday = new Date(prevYear, now.getMonth(), now.getDate())

  // 上年今日是星期几（0=周日，1=周一...）
  let prevTodayWeekNum = prevToday.getDay()
  // 转换为周一=1，周日=7
  if (prevTodayWeekNum === 0) prevTodayWeekNum = 7

  // 初始日期（上年临近的星期一）
  const firstMonday = new Date(prevToday)
  if (prevTodayWeekNum > 1) {
    firstMonday.setDate(prevToday.getDate() + (8 - prevTodayWeekNum))
  }

  // 初始日期至今日的天数
  const days = Math.floor((now.getTime() - firstMonday.getTime()) / (1000 * 60 * 60 * 24)) + 1

  // 每周天数
  const columns = 7
  // 最大列数（周数）
  const lineNums = Math.ceil(days / columns)

  // 绘制图表的源数据
  const result: ColumnData[] = []
  let totalSum = 0
  const record = props.profile || {}

  for (let i = 0; i < lineNums; i++) {
    // 最近一星期不一定满的
    const weekColumn = (i === lineNums - 1 ? days % columns || columns : columns)

    // 计算月份标题
    const weekStart = new Date(firstMonday)
    weekStart.setDate(firstMonday.getDate() + i * 7)
    const weekEnd = new Date(firstMonday)
    weekEnd.setDate(firstMonday.getDate() + i * 7 + weekColumn)

    const startMonth = weekStart.getMonth() + 1
    const endMonth = weekEnd.getMonth() + 1

    let title = i === 0 ? monthCN[startMonth - 1] : ''
    const ifSwitchMonth = endMonth !== startMonth

    if (i > 0 && result[i - 1].ifSwitchMonth) {
      title = monthCN[endMonth - 1]
    }

    const columnData: ColumnData = {
      columns: weekColumn,
      title,
      ifSwitchMonth,
      data: []
    }

    for (let j = 0; j < columnData.columns; j++) {
      const date = new Date(firstMonday)
      date.setDate(firstMonday.getDate() + i * 7 + j)
      const dateStr = formatDate(date)

      const number = record[dateStr] || 0
      totalSum += number

      columnData.data.push({
        number,
        date: dateStr
      })
    }

    result.push(columnData)
  }

  dateData.value = result
  sumNumber.value = totalSum
}

const formatDate = (date: Date): string => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const getColor = (number: number): string => {
  if (number >= 12) return 'var(--chart-level-5)'
  if (number >= 8) return 'var(--chart-level-4)'
  if (number >= 4) return 'var(--chart-level-3)'
  if (number >= 1) return 'var(--chart-level-2)'
  return 'var(--chart-level-1)'
}

watch(() => props.profile, () => {
  init()
}, { immediate: false })

onMounted(() => {
  init()
})
</script>

<style scoped>
.submission-chart {
  width: 100%;
  padding: 16px;
  font-size: var(--text-sm);
}

.calendar {
  display: flex;
  justify-content: center;
}

.weeks {
  width: 30px;
  margin-right: 3px;
  margin-top: 22px;
  flex-shrink: 0;
}

.week {
  margin-top: 13px;
  width: 60px;
  height: 14px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.column {
  width: 13px;
  margin-right: 3px;
  flex-shrink: 0;
}

.title {
  width: 14px;
  height: 14px;
  margin-bottom: 8px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  text-align: left;
  overflow: visible;
  white-space: nowrap;
}

.date-wrapper {
  width: 13px;
  height: 13px;
  border-radius: 2px;
  margin-bottom: 2px;
  cursor: pointer;
  transition: transform var(--transition-fast);
}

.date-wrapper:hover {
  transform: scale(1.2);
  outline: 2px solid var(--color-primary);
  outline-offset: 1px;
}

.operation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  padding: 0 16px;
}

.slider {
  display: flex;
  align-items: center;
  gap: 12px;
}

.slider-label {
  width: 30px;
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.slider-total {
  width: 120px;
}

.total-text {
  font-size: var(--text-sm);
  color: var(--color-primary);
  font-weight: 500;
}

.slider-month {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.legend {
  display: flex;
  align-items: center;
  gap: 3px;
}

.level-desc {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
  margin: 0 6px;
}

.level {
  width: 11px;
  height: 11px;
  border-radius: 2px;
}

.level-1 {
  background: var(--chart-level-1);
}

.level-2 {
  background: var(--chart-level-2);
}

.level-3 {
  background: var(--chart-level-3);
}

.level-4 {
  background: var(--chart-level-4);
}

.level-5 {
  background: var(--chart-level-5);
}
</style>
