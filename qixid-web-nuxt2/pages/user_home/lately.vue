<template>
  <div>
    <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
    <ul ref="dialogContent" @scroll="divScroll($event)" v-if="latelyList.length!=0">
      <li v-for="item of latelyList">
        <div class="browsing-list">
          <div>
            <span class="font-s-16 color-grey">浏览了[{{ item.targetType == 1 ? '文章' : '帖子' }}]：</span>
            <nuxt-link class="font-s-16 cursor-pointer text-underline hover-cl"
                       :to="`/article/article-details/`+$base64.encode(item.targetId)" target="_blank" rel="noopener">
              [ {{ item.targetTitle }} ]
            </nuxt-link>
          </div>
          <div class="font-s-13 mt-10 color-grey">
            <svg t="1682313364070" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="2551">
              <path
                d="M512 29.9008A482.0992 482.0992 0 1 0 994.0992 512 482.7136 482.7136 0 0 0 512 29.9008z m0 915.2512A433.152 433.152 0 1 1 945.152 512 433.5616 433.5616 0 0 1 512 945.152z"
                p-id="2552"></path>
              <path
                d="M755.0976 487.5264H536.4736V279.1424a24.4736 24.4736 0 0 0-49.0496 0V512a24.4736 24.4736 0 0 0 24.576 24.4736h243.0976a24.4736 24.4736 0 1 0 0-49.0496z"
                p-id="2553"></path>
            </svg>
            {{ $utils.parseTime(item.updateTime, '{y}-{m}-{d} {h}:{i}') }}
          </div>
        </div>
      </li>
    </ul>
    <div v-if="latelyList.length==0 && !loading" style="text-align: center;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="60">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-14 color-grey-2">暂无数据</div>
    </div>
  </div>
</template>

<script>

export default {
  name: "lately",
  data() {
    return {
      latelyList: [],
      loading: true,
      scrollLoading: true,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
    }
  },
  methods: {
    divScroll(e) {
      let scrollHeight = this.$refs.dialogContent.scrollHeight;
      let clientHeight = this.$refs.dialogContent.clientHeight;
      let scrollTop = this.$refs.dialogContent.scrollTop;
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
          this.scrollLoading = false;
          this.queryParams.pageNum = this.queryParams.pageNum + 1;
          this.$API("/frontDesk/browsing/history/list", "get", this.queryParams).then(res => {
            res.data.records.forEach(item => {
              this.latelyList.push(item)
            })
            this.total = res.data.total;
          }).finally(() => this.scrollLoading = true)
        }
      }
    },
    browsingHistoryLists() {
      let uuid = this.$base64.decode(this.$route.query.uuid);
      this.queryParams.uid = uuid;
      this.$API("/frontDesk/browsing/history/list", "get", this.queryParams).then(res => {
        if (res.code == 200) {
          this.latelyList = res.rows;
          this.total = res.data.total;
        }
      }).finally(() => this.loading = false)
    }
  },
  mounted() {
    this.browsingHistoryLists();
  }
}
</script>

<style scoped>
.browsing-list {
  background-color: rgba(241, 242, 246, 0.5);
  padding: 16px 20px;
  margin: 20px 0;
  border-radius: 4px;
  transition: .2s;
  border: 1px solid #FFFFFF;
}

.browsing-list:hover {
  border-bottom: 1px solid var(--hover-color);
}

.dialogContent li {
  list-style: none;
}
</style>
