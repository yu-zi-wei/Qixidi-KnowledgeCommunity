<template>
  <div>
    <el-popover
      v-show="loading"
      :placement="placementData"
      width="360"
      height="200">
      <div class="emoji-div">
        <div v-for="(item,index) in emojiData" :key="index"
             @click="selectEmoji(item)" class="emoji-item">
          {{ item }}
        </div>
      </div>
      <el-button slot="reference" type="text" title="表情" size="mini">
        <svg t="1720431359964" class="icon icon-size-22 icon-hover" viewBox="0 0 1024 1024" version="1.1"
             xmlns="http://www.w3.org/2000/svg"
             p-id="12040">
          <path
            d="M942.577061 507.011382c0-237.637282-192.632275-430.269557-430.269557-430.269557s-430.269557 192.632275-430.269557 430.269557c0 237.647515 192.632275 430.27979 430.269557 430.27979 97.553827 0 186.878225-33.189913 259.055081-87.88563 2.964518-3.363607 4.91187-7.655354 4.91187-12.479219 0-10.531867-8.541537-19.073404-19.072381-19.073404-5.465478 0-10.334369 2.168386-13.808494 5.842055l-0.242524 0c-64.584947 47.526433-144.086629 75.981509-230.422973 75.981509-215.179804 0-389.645324-174.489056-389.645324-389.656581 0-215.201294 174.466544-389.644301 389.645324-389.644301 215.202317 0 389.645324 174.443008 389.645324 389.644301 0 65.738213-15.423271 127.60219-44.186362 181.922353l0 0.321318c-0.533143 1.814322-1.108241 3.583618-1.108241 5.53097 0 10.531867 8.541537 19.073404 19.073404 19.073404 8.207939 0 15.06716-5.265934 17.745153-12.523221l0 0.13303C924.544359 645.279493 942.577061 578.258053 942.577061 507.011382zM399.574976 391.378805c0-24.028253-19.47147-43.499723-43.500746-43.499723-24.029276 0-43.478234 19.47147-43.478234 43.499723 0 24.029276 19.448958 43.479257 43.478234 43.479257C380.102483 434.858062 399.574976 415.408081 399.574976 391.378805zM669.735999 347.945597c-24.004717 0-43.476187 19.448958-43.476187 43.478234 0 24.029276 19.47147 43.500746 43.476187 43.500746 24.029276 0 43.50177-19.47147 43.50177-43.500746C713.237769 367.394554 693.766298 347.945597 669.735999 347.945597zM702.196295 630.742405c0-11.793604-9.558703-21.330818-21.350261-21.330818-1.88186 0-3.562129 0.620124-5.335519 1.084705l-0.441045 0c-49.473785 22.723537-104.548124 38.610366-162.562423 38.610366-57.130162 0-111.007233-16.130376-159.907966-38.233789l-0.177032 0c-2.300392-0.818645-4.690836-1.461281-7.2798-1.461281-11.880585 0-21.505803 9.602705-21.505803 21.485337 0 8.120958 4.513804 15.222703 11.129478 18.872836 54.209646 24.6494 113.883747 42.980907 177.319521 42.980907 63.037708 0 124.240629-18.686594 178.185238-43.046399 0.308015-0.149403 0.217964-0.11154 0.070608-0.045025C696.59574 645.928269 702.196295 638.545114 702.196295 630.742405z"
            p-id="12041"></path>
        </svg>
      </el-button>
    </el-popover>
  </div>
</template>

<script>

export default {
  name: "emojiModule",
  props: {
    content: '',
    id: '',
    placement: '',
  },
  data() {
    return {
      emojiData: {},
      loading: false,
      editedContent: this.content,
      placementData: this.placement == null ? 'top' : this.placement
    }
  },
  methods: {
    loadDate() {
      //加载表情数据
      const response = require('static/json/emoji.json');
      this.emojiData = response.data;
      this.loading = true;
      console.log("placementData:",this.placementData)
    },
    selectEmoji(emoji) {
      let input = document.getElementById(this.id)
      let startPos = input.selectionStart
      let endPos = input.selectionEnd
      let resultText = input.value.substring(0, startPos) + emoji + input.value.substring(endPos)
      input.value = resultText
      input.focus()
      input.selectionStart = startPos + emoji.length
      input.selectionEnd = startPos + emoji.length
      //更新数据
      this.editedContent = resultText
      //返回值给父组件
      this.$emit('update:content', this.editedContent)
    }
  },
  mounted() {
    this.loadDate();
  }
}
</script>

<style scoped>
.emoji-div {
  padding: 4px;
  height: 200px;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}

.emoji-item {
  margin: 4px;
  font-size: 24px;
  width: 30px;
  height: 30px;
  cursor: pointer;
  transition: .3s;
}
.emoji-item:active{
  font-size: 22px;
}
</style>
