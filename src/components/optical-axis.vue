<template>
  <div class="optical-main">
    <div style="width: 100%; display: flex;justify-content:center ">
      <!--        左边-->
      <div style="width: 10%;">
        <!-- 开头标记 -->
        <span data-v-jzl20210826="" class="header-radio"></span>
        <!-- 链接标记 -->
        <span v-for="index in numbers()" :key="index">
          <span v-if="index>0&& index%2==0" class="left-link"></span>
          </span>
        <!--          结尾标记-->
        <span data-v-jzl20210826="" v-if="lineNumber%2==0" class="left-end-icon"></span>
      </div>

      <!-- 中间数据 -->
      <div style="width: 75%;">
        <div class="timeline" style="display: flex;justify-content: space-around" v-for="index in numbers()"
             :key="index">
          <div v-for="(item, i) in getArrDate(index)" :key="i">
            <div style="width: 200px;">
              <div class="nodes-icon"></div>
              <div class="text-center">
                <div class="node-title" style="position: relative;bottom: 50px" :title="item.title"
                     @click="viewInfo(item)">
                  {{ item.title }}
                </div>
                <div class="node-time font-s-13" style="position: relative;bottom: 15px">
                  <span>{{ parseTimes(item.createTime, '{y}年{m}月{d}') }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!--右边-->
      <div style="width: 10%;">
        <!-- 第一个链接，默认存在 -->
        <span class="right-link-default" v-if="experienceData.length > number"></span>
        <!--          后续链接-->
        <span v-for="index in numbers()" :key="index">
          <span v-if="index%2!=0 && index+1!=lineNumber && index+2!=lineNumber" class="right-link"></span>
          </span>
        <!--          结尾标识-->
        <span data-v-jzl20210826="" v-if="lineNumber%2!=0" class="right-end-icon"></span>
      </div>
    </div>
  </div>
</template>
<script>
import {parseTime} from "@/utils/dateUitls";

export default {
  props: {
    data: [],
    number: {//每行展示X条数据
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      experienceData: this.data,
      lineNumber: (this.data.length % this.number) == 0
          ? Math.floor(this.data.length / this.number)
          : Math.floor((this.data.length / this.number) + 1),
    };
  },
  methods: {
    viewInfo(item) {
      this.$router.push({
        path: '/record-index',
        query: {id: item.id}
      })
    },
    parseTimes(value, args) {
      return parseTime(value, args);
    },
    numbers() {
      return Array.from({length: this.lineNumber}, (_, i) => i);
    },

    getArrDate(Num) {
      let arr = this.experienceData.slice(Num * this.number, this.number * (Num + 1));
      if (Num % 2 != 0) {
        arr.reverse();
      }
      return arr;
    },
  },
  watch: {
    data: function (newVal) {
      this.experienceData = newVal;
    },
  },
};
</script>
<style>

:root {
  --interval-height: 150px;
  --app-interval-height: 120px;
}

.optical-main {
  padding: 0px;
  margin: 0px;
}

.timeline {
  width: 100%;
  height: var(--interval-height);
  border-top: 2px solid var(--color-thme);
}

.nodes-icon {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #fefefe;
  border: 5px solid var(--color-thme);
  margin-left: 45%;
  transform: translate(0, -50%);
}

.node-title {
  white-space: nowrap;
  -webkit-line-clamp: 1;
  overflow: hidden; /* 隐藏超出范围的内容 */
  text-overflow: ellipsis; /* 使用省略号 */
  font-size: 16px;
  top: 50%;
}

.node-title:hover {
  text-decoration: underline;
  color: var(--color-thme);
}

.node-time {
  color: #636e72;
}

.right-link-default {
  display: block;
  width: 100%;
  height: var(--interval-height);
  margin-bottom: var(--interval-height);
  border: 2px solid var(--color-thme);
  border-radius: 0 80px 80px 0;
  border-left: 0px;
}

.right-link {
  display: block;
  width: 100%;
  height: var(--interval-height);
  margin-bottom: var(--interval-height);
  border: 2px solid var(--color-thme);
  border-radius: 0 80px 80px 0;
  border-left: 0px;
}

.right-end-icon {
  display: block;
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  position: relative;
}

.right-end-icon[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  top: -4px;
  right: -2px;
  border-top: 5px solid transparent;
  border-left: 12px solid var(--color-thme);
  border-bottom: 5px solid transparent;
}


.left-link {
  display: block;
  width: 100%;
  height: var(--interval-height);
  margin-bottom: var(--interval-height);
  border: 2px solid var(--color-thme);
  border-radius: 80px 0 0 80px;
  border-right: 0px;
}

.left-end-icon {
  display: block;
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  position: relative;
}

.left-end-icon[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  top: -4px;
  left: -2px;
  border-top: 5px solid transparent;
  border-right: 12px solid var(--color-thme);
  border-bottom: 5px solid transparent;
}

.header-radio {
  display: block;
  width: 100%;
  margin-bottom: var(--interval-height);
  border-bottom: 2px solid var(--color-thme);
  position: relative;
}

.header-radio[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  left: -2px;
  width: 1px;
  height: 1px;
  border-radius: 50%;
  border: 5px solid #27ae60;
  position: absolute;
  right: 35px;
  top: 50%;
  transform: translate(0, -50%);
}

@media (max-width: 400px) {
  .header-radio {
    margin-bottom: var(--app-interval-height);
  }

  .left-link,
  .right-link-default,
  .right-link {
    height: var(--app-interval-height);
    margin-bottom: var(--app-interval-height);
  }

  .timeline {
    height: var(--app-interval-height);
  }

}

</style>
