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
          :style="`background:${getColor(dateData.number)};`"
          :title="dateData.date+' '+sliderFormat(dateData.number)">
        </div>
      </div>
    </div>
    <div class="operation">
      <div class="slider">
        <div style="width: 30px;margin-left: 10px">0</div>
        <div style="width: 100px">
          <span class="color-theme">总贡献：{{ sumNumBer }}+</span>
        </div>
        <div class="">month：12</div>
      </div>
      <div class="legend">
        <div class="level-desc">少</div>
        <div class="level level-1"></div>
        <div class="level level-2"></div>
        <div class="level level-3"></div>
        <div class="level level-4"></div>
        <div class="level level-5"></div>
        <div class="level-desc">多</div>
      </div>
    </div>
  </div>
</template>

<script>
import moment from 'moment'

export default {
  name: 'submissionChart',
  data() {
    return {
      dateData: [],
      submissionRecord: this.profile,
      sliderValue: [0, 12],//显示最近12月的数据
      sumNumBer: 0,//总次数
    }
  },
  props: {
    profile: {
      default: {},
      type: Object
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      // 上一年信息
      let prevYear = moment().format('YYYY') - 1
      let prevTodayFormatStr = prevYear + '-' + moment().format('MM-DD')
      let prevToday = moment(prevTodayFormatStr).format('YYYY-MM-DD')
      // 上年今日的是星期几
      let prevTodayWeekNum = moment(prevToday).weekday() || 7
      // 初始日期（上年临近的星期一）
      let firstMondayDate = prevTodayWeekNum > 1 ? moment(prevToday).add(8 - prevTodayWeekNum, 'days').format('YYYY-MM-DD') : prevToday
      // 初始日期至今日的天数，包括今日
      let days = moment().diff(moment(firstMondayDate), 'days') + 1
      // 每周天数
      let columns = 7
      // 最大列数（周数）
      let lineNums = Math.ceil(days / columns)
      // 绘制图表的源数据
      let dateData = []
      let monthCN = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
      let startSliderNum = this.sliderValue[0]
      let endSliderNum = this.sliderValue[1]
      let sumNumBer = 0;
      for (let i = 0; i < lineNums; i++) {
        // 最近一星期不一定满的
        let weekColumn = (i === lineNums - 1 ? days % columns ? days % columns : columns : columns)
        // 开始计算title（月份的图例）
        // 思路：第一列直接根据第一天的月份
        //      之后的嘛列数根据上一周的最后一天减去第一天的月份，如果大于1代表月份发生了改变，下一列的title显示最新的月份
        let theWeekStartMonth = moment(firstMondayDate).add(i * 7, 'days').format('M')
        let theWeekEndMonth = moment(firstMondayDate).add(i * 7 + weekColumn, 'days').format('M')
        let title = (i === 0) ? monthCN[theWeekStartMonth - 1] : ''
        let ifSwitchMonth = false
        if (theWeekEndMonth - theWeekStartMonth) {
          ifSwitchMonth = true
        }
        if (i && dateData[i - 1].ifSwitchMonth) {
          title = monthCN[theWeekEndMonth - 1]
        }
        // 图表源数据格式：columns：列数，title：列标题，ifSwitchMonth：月份是否发生改变，data：每格数据
        dateData.push({
          columns: weekColumn,
          title: title,
          ifSwitchMonth: ifSwitchMonth,
          data: []
        })
        for (let j = 0; j < dateData[i].columns; j++) {
          let date = moment(firstMondayDate).add(i * 7 + j, 'days').format('YYYY-MM-DD')
          let number = 0
          // 提交次数在slider范围内再进行次数赋值
          if ((this.submissionRecord[date] >= startSliderNum && this.submissionRecord[date] < endSliderNum) ||
            (this.submissionRecord[date] > 12 && endSliderNum === 12)) {
            number = this.submissionRecord[date]
          }
          sumNumBer += number;//记录总次数
          // number：提交次数，date：提交日期
          dateData[i].data.push({
            number: number,
            date: date
          })
        }
      }
      this.sumNumBer = sumNumBer;
      this.dateData = dateData
    },
    getColor(number) {
      // 左闭右开
      let color = '#EBEDF0'
      if (number >= 12) {
        color = '#196127'
      } else if (number >= 8) {
        color = '#239A3B'
      } else if (number >= 4) {
        color = '#7BC96F'
      } else if (number >= 1) {
        color = '#C6E48B'
      } else {
        color = '#EBEDF0'
      }
      return color
    },
    sliderFormat(val) {
      return ' +' + val
    },
  }
}
</script>
<style scoped>
.submission-chart {
  width: 915px;
  height: 180px;
  background-color: #fff;
  margin: auto;
  margin-top: 20px;
  padding: 0;
  font-size: 12px;
}

.submission-chart .calendar {
  margin-left: 16px;
  margin-right: 16px;
  display: flex;
}

.submission-chart .calendar .weeks {
  width: 30px;
  margin-right: 3px;
  margin-top: 22px;
}

.submission-chart .calendar .weeks .week {
  margin-top: 13px;
  width: 30px;
  height: 14px;
}

.submission-chart .calendar .column {
  width: 13px;
  margin-right: 3px;
}

.submission-chart .calendar .column .title {
  width: 14px;
  height: 14px;
  margin-bottom: 8px;
  text-align: left;
  overflow: visible;
  white-space: nowrap;
}

.submission-chart .calendar .column .date-wrapper {
  width: 13px;
  height: 13px;
  background: #fefefe;
  border: 1px solid #fefefe;
  margin-bottom: 2px;
}

.submission-chart .calendar .column .date-wrapper .date {
  width: 13px;
  height: 13px;
}

.submission-chart .calendar .column .date-wrapper .date:hover {
  width: 13px;
  height: 13px;
}

.submission-chart .calendar .column .date-wrapper:hover {
  border: 1px solid var(--theme-color);
}

.submission-chart .operation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.submission-chart .operation .slider {
  display: flex;
  justify-content: left;
  align-items: center;
  margin: 0 8px;
}


.submission-chart .operation .legend {
  display: flex;
  justify-content: center;
  align-items: center;
}

.submission-chart .operation .legend .level-desc {
  margin-right: 6px;
  margin-left: 3px;
}

.submission-chart .operation .legend .level {
  margin-right: 3px;
  width: 11px;
  height: 11px;
}

.submission-chart .operation .legend .level-1 {
  background: #EBEDF0;
}

.submission-chart .operation .legend .level-2 {
  background: #C6E48B;
}

.submission-chart .operation .legend .level-3 {
  background: #7BC96F;
}

.submission-chart .operation .legend .level-4 {
  background: #239A3B;
}

.submission-chart .operation .legend .level-5 {
  background: #196127;
}
</style>
