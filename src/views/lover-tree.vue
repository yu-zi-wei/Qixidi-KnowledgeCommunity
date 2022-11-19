<template>
  <div>
    <div class="ml-20 mt-20">
      <router-link to="/">
        <button class="tree-button">
          <svg t="1668694033951" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="15411" width="16" height="16">
            <path
                d="M764.28847787 408.96539307c51.24041387 30.5463296 114.11019093 112.9447424 140.2667008 212.12910933 26.15978667 99.19092053 21.82239573 188.00858453 21.82239573 188.00858453s-33.86682027-54.6963456-47.59770453-73.87327146c-13.73088427-19.17146453-71.58934187-90.61116587-161.3955072-132.0157184-89.80616533-41.40346027-239.91855787-34.03066027-239.91855787-34.03066027v172.4121088L63.75560533 448.55241387l413.7091072-293.04531627v173.99808s99.38315947 7.4809344 151.3324544 18.23757653c87.49929813 18.12179627 135.49131093 61.22263893 135.49131094 61.22263894z m0 0"
                fill="#ffffff" p-id="15412"></path>
          </svg>
          <span>Return</span>
        </button>
      </router-link>
    </div>
    <div style="width: 100%;height: 100%;">
      <iframe style="width: 100%; height: 800px;" src="https://jade_purple.gitee.io/lover-1l/"
              allowfullscreen="allowfullscreen" frameborder="0" data-id="1"></iframe>
    </div>
  </div>
</template>

<script>

export default {
  name: "loverTree",
  data() {
    return {
      dayDiff: '',
      hours: '',
      minutes: '',
      seconds: '',
    }
  },
  methods: {

    // 时间差计算
    difference(beginTime) {
      let dateBegin = new Date(beginTime);
      // let dateEnd = new Date(endTime);
      //结束时间
      let dateEnd = new Date();
      let dateDiff = dateEnd.getTime() - dateBegin.getTime(); //时间差的毫秒数
      let dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数
      let leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
      let hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
      //计算相差分钟数
      let leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
      let minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
      //计算相差秒数
      let leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
      let seconds = Math.round(leave3 / 1000);
      this.dayDiff = dayDiff;
      this.hours = hours;
      this.minutes = minutes;
      this.seconds = seconds;
    },
  },
  mounted() {
    // let timer = setInterval(() => {
    //   this.difference('2022-4-3 20:00:00'); //每秒更新一次时间
    // }, 1000);
  }
}
</script>

<style>
body {
  background-color: #b20cd3;
}

/* From www.lingdaima.com */
.tree-button {
  display: flex;
  height: 3em;
  width: 100px;
  align-items: center;
  justify-content: center;
  border-radius: 3px;
  letter-spacing: 1px;
  color: #fefefe;
  transition: all 0.2s linear;
  cursor: pointer;
  border: none;
  background: #b20cd3;
}

.tree-button > svg {
  margin-right: 5px;
  margin-left: 5px;
  font-size: 20px;
  transition: all 0.4s ease-in;
}

.tree-button:hover > svg {
  font-size: 1.2em;
  transform: translateX(-5px);
}

.tree-button:hover {
  box-shadow: 9px 9px 33px #d1d1d1, -9px -9px 33px #ffffff;
  transform: translateY(-2px);
}
</style>