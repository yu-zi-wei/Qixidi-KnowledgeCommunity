<template>
  <div class="tool-index">
    <el-skeleton :rows="6" animated v-if="loading"/>
    <ul v-if="toolArray.length>0 && !loading" class="tool-ui">
      <li v-for="item of toolArray" class="fl-left">
        <div class="tool-list" :title="item.toolName">
          <a :href="item.toolUrl" target="_blank">
            <div class="flex-left align-items-center">
              <div class="mr-6">
                <el-image v-if="item.icon" style="width: 40px; height: 40px" fit="cover"
                          :src="item.icon"></el-image>
                <div v-else>
                  <svg t="1727666944637" class="icon svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="10786" width="40" height="40">
                    <path d="M0.038225 0.129008h1023.870992v1023.870992h-1023.870992z" fill="#FF0000" fill-opacity="0"
                          p-id="10787"></path>
                    <path
                      d="M512.00785 1024C170.649261 1024 0.004096 853.354835 0.004096 512.064504S170.649261 0.129008 512.00785 0.129008c341.290331 0 511.935496 170.645165 511.935496 511.935496s-170.645165 511.935496-511.935496 511.935496z"
                      fill="#EBF4FC" p-id="10788"></path>
                    <path
                      d="M676.85108 580.936893m-164.911488 0a164.911488 164.911488 0 1 0 329.822976 0 164.911488 164.911488 0 1 0-329.822976 0Z"
                      fill="#0277FF" fill-opacity=".3" p-id="10789"></path>
                    <path
                      d="M827.325987 630.355733a50.579227 50.579227 0 0 1-50.067292 50.033162h-239.244522v68.258066h118.359487a18.224904 18.224904 0 1 1 0 36.449807h-273.20291a18.224904 18.224904 0 1 1 0-36.415678h122.898648v-68.258066h-259.380651a50.579227 50.579227 0 0 1-50.067291-50.067291v-341.290331A50.613356 50.613356 0 0 1 246.654618 239.032239H772.685405a54.640582 54.640582 0 0 1 54.640582 50.033163v341.290331z m-598.862144-4.573291c0.682581 9.795032 8.464 17.576452 18.224904 18.224904H772.685405a19.590065 19.590065 0 0 0 18.224903-18.224904V293.638692a19.555936 19.555936 0 0 0-18.224903-18.190774H246.654618a19.555936 19.555936 0 0 0-18.190775 18.190774v332.14375z"
                      fill="#7C5DC7" p-id="10790"></path>
                    <path
                      d="M652.619466 239.134626H243.856037c-26.279355 0-47.234582 19.965484-47.234581 44.12884V614.451603S678.830564 239.134626 652.619466 239.134626z"
                      fill="#FFFFFF" fill-opacity=".2" p-id="10791"></path>
                    <path
                      d="M364.092621 484.488245a10.614129 10.614129 0 0 1 0-15.119162l75.52755-75.493421a10.682387 10.682387 0 0 1 15.119162 15.119162L386.754299 476.9116 454.773462 544.828376a10.648258 10.648258 0 1 1-15.119162 15.119161L364.12675 484.488245zM532.758302 349.746823a10.784774 10.784774 0 1 1 20.81871 5.563032L486.684108 604.895474a10.784774 10.784774 0 1 1-20.818711-5.563033L532.792431 349.746823z m126.618713 134.741422l-75.493421 75.425163a10.648258 10.648258 0 1 1-15.119162-15.085032L636.681208 476.9116 568.764432 408.994824a10.682387 10.682387 0 1 1 15.119162-15.119162l75.493421 75.459292a10.614129 10.614129 0 0 1 0 15.119162z"
                      fill="#333333" p-id="10792"></path>
                  </svg>
                </div>
              </div>
              <p class="tool-toolName text-underline-hover font-s-16 font-bold" v-text="item.toolName"></p>
            </div>
            <hr class="hr-item mt-4 mb-8"/>
          </a>
          <div class="tool-describe color-grey font-s-14">
            {{ item.describe }}
          </div>
        </div>
      </li>
    </ul>
    <div v-if="toolArray.length==0 && !loading" style="text-align: center">
      <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="40">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-12 color-grey">暂无数据~</div>
    </div>
  </div>
</template>

<script>

export default {
  name: "toolList",
  props: {
    id: Number,
    //是否为搜索行为
    isSearch: Boolean,
    toolName: String,
  },
  data() {
    return {
      loading: true,
      toolArray: [],
      query: {
        toolName: null,
        id: null,
      },
      debounceTimer: null,//防抖
      debounceTime: 400,//防抖时间
    }
  },
  watch: {
    id() {
      this.toolChildLists();
    },
    isSearch() {
      if (this.isSearch) {
        this.toolSearch();
      }
    },
    toolName() {
      if (this.isSearch) {
        this.toolSearch();
      }
    },
  },
  methods: {
    toolSearch() {
      clearTimeout(this.debounceTimer);
      this.loading = true;
      this.debounceTimer = setTimeout(() => {
        this.$API("/white/configure/tool/child/list", "get", {toolName: this.toolName, isParent: 2}).then(res => {
          this.toolArray = res;
          this.loading = false;
        })
      }, this.debounceTime);
    },
    toolChildLists() {
      this.loading = true;
      this.$API("/white/configure/tool/child/list", "get", {id: this.id, isParent: 2}).then(res => {
        this.toolArray = res;
        this.loading = false;
      })
    }
  },
  mounted() {
    this.toolChildLists();
  }
}
</script>

<style scoped>
@import url("static/css/server/pc/tool.css");
</style>
