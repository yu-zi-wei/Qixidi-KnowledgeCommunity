<template>
  <div class="optical-main">
    <div style="width: 100%; display: flex; position: relative;">
      <!--        左边-->
      <div style="width: 10%; margin-left: 30px">
        <!-- 开头标记 -->
        <span data-v-jzl20210826="" class="headerRadio"></span>
        <!-- 链接标记 -->
        <span v-for="index in numbers()">
          <span v-if="index>0&& index%2==0" class="hingelisHeard"></span>
          </span>
        <!--          结尾标记-->
        <span data-v-jzl20210826="" v-if="lineNumber%2==0" class="hingeorgerHeard"></span>
      </div>

      <!-- 中间数据 -->
      <div style="width: 75%">
        <div style="display: flex" v-for="index in numbers()" :key="index">
          <div class="timeline" v-for="(item, i) in getArrDate(index)" :key="i">
            <div class="border" style="top: 50px"></div>
            <div class="Nodes" style="top: 50px"></div>
            <div class="timeNodes">
              <el-tooltip class="item" effect="dark" placement="right">
                <p class="nodeTimelis font-s-16" :title="item.title" @click="viewInfo(item)">
                  <span>{{ item.title }}</span>
                </p>
              </el-tooltip>
              <p class="nodeTimes font-s-12">
                <span>{{ parseTimes(item.createTime, '{y}年{m}月{d}') }}</span>
              </p>
            </div>
          </div>
        </div>
      </div>

      <!--右边-->
      <div style="width: 10%; margin-right: 30px">
        <!-- 第一个链接，默认存在 -->
        <span class="hingelis-default" v-if="experienceData.length > number"></span>
        <!--          后续链接-->
        <span v-for="index in numbers()">
          <span v-if="index%2!=0 && index+1!=lineNumber && index+2!=lineNumber" class="hingelis"></span>
          </span>
        <!--          结尾标识-->
        <span data-v-jzl20210826="" v-if="lineNumber%2!=0" class="hingeorger"></span>
      </div>
    </div>
  </div>
</template>
<script>
import {parseTime} from "@/utils/dateUitls";

export default {
  props: {
    data: [],
    number: 0, //每行展示X条数据
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
    data: function (newVal, oldVal) {
      this.experienceData = newVal;
    },
  },
};
</script>
<style scoped>
.optical-main {
  /*max-width: 1180px;*/
  /*margin: auto;*/
}

.timeline {
  width: 100%;
  height: 100px;
  position: relative;
}

.border {
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.Nodes {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #fefefe;
  border: 5px solid var(--color-thme);
  position: absolute;
  right: 35px;
  top: 50%;
  transform: translate(0, -50%);
}

.timeNodes {
  position: absolute;
  text-align: center;
  right: -5px;
  width: 105px;
  top: 50%;
  transform: translate(0, -50%);
}

.nodeTimelis {
  width: 120px;
  margin-bottom: 20px;
  overflow: hidden;
  word-break: keep-all;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.nodeTimelis:hover {
  text-decoration: underline;
  color: var(--color-thme);
}

.nodeTimes {
  margin-top: 15px;
  color: #636e72;
}

.hingelis {
  content: "";
  display: block;
  width: 100%;
  height: 98.5px;
  margin-top: 98.4px;
  border: 2px solid var(--color-thme);
  border-radius: 0 50px 50px 0;
  border-left: 0px;
}

.hingelis-default {
  content: "";
  display: block;
  width: 100%;
  height: 98.5px;
  margin-top: 49.5px;
  border: 2px solid var(--color-thme);
  border-radius: 0 50px 50px 0;
  border-left: 0px;
}

.hingelisHeard {
  content: "";
  display: block;
  width: 100%;
  height: 98.5px;
  margin-top: 98.4px;
  border: 2px solid var(--color-thme);
  border-radius: 50px 0 0 50px;
  border-right: 0px;
}

.hingeorger {
  display: block;
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  margin-top: 98px;
  position: relative;
}

.hingeorgerHeard {
  display: block;
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  position: relative;
  margin-top: 98px;
}

.hingeorgerHeard[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  top: -4px;
  left: -2px;
  border-top: 5px solid transparent;
  border-right: 12px solid var(--color-thme);
  border-bottom: 5px solid transparent;
}

.hingeorger[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  top: -4px;
  right: -2px;
  border-top: 5px solid transparent;
  border-left: 12px solid var(--color-thme);
  border-bottom: 5px solid transparent;
}

.headerRadio {
  display: block;
  width: 100%;
  border-bottom: 2px solid var(--color-thme);
  position: relative;
  margin-top: 49.5px;
}

.headerRadio[data-v-jzl20210826]:after {
  content: "";
  position: absolute;
  top: -4px;
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
  .headerRadio,
  .hingelis-default {
    margin-top: 49px;
  }

  .hingelisHeard,
  .hingelis {
    margin-top: 98.5px;
    height: 98.5px;
  }


  .hingeorgerHeard {
    margin-top: 99px;
  }
}

</style>
