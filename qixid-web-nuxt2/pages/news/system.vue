<template>
  <div style="min-height: 85vh">
    <el-skeleton class="mt-10 ml-10" :rows="4" animated v-if="loading"/>
    <ul class="background-color-fefefe padding-10" v-if="newsList.length!=0 && !loading">
      <li v-for="(item,index) in newsList" class="news-system-cl" :key="index" :ref="`newsSystemItem${index}`">
        <div title="系统消息" class="text-left">
          <svg t="1681980494552" class="icon icon-size-20 svg-translateY-4" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="2130">
            <path
              d="M592.384 111.889067L288.2048 324.266667H170.666667a68.266667 68.266667 0 0 0-68.266667 68.266666v238.933334a68.266667 68.266667 0 0 0 68.266667 68.266666h117.538133L592.384 912.110933a68.266667 68.266667 0 0 0 107.349333-55.978666V167.867733a68.266667 68.266667 0 0 0-107.349333-55.978666zM327.287467 380.245333L631.466667 167.867733v688.264534L327.287467 643.754667a68.266667 68.266667 0 0 0-39.082667-12.288H170.666667V392.533333h117.538133a68.266667 68.266667 0 0 0 39.082667-12.288z"
              fill="#444444" p-id="2131"></path>
            <path
              d="M895.249067 290.133333c35.84 61.474133 55.842133 141.277867 55.842133 225.6896 0 77.533867-16.878933 151.210667-47.4624 210.432l-3.754667 7.031467-59.904-32.7168c27.2896-49.988267 42.837333-115.2 42.837334-184.746667 0-69.802667-15.701333-135.2192-43.0592-185.156266l-3.4816-6.144L895.249067 290.133333z"
              fill="#00B386" p-id="2132"></path>
          </svg>
          {{ item.newsContent }}
        </div>
        <div class="mt-4 color-grey-2 text-right">{{ $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{s}') }}</div>
        <hr class="hr-item mt-4"/>
      </li>
    </ul>
    <div v-if="newsList.length==0 && !loading" style="text-align: center;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="60">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-12 color-grey-2">暂无消息</div>
    </div>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils';

export default {
  name: "system",
  data() {
    return {
      newsList: [],
      loading: true,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        type: 5,
      },
      scrollLoading: true,
      moreLoading: true,
      animator: null, // 动画器实例
    }
  },
  methods: {
    userNewsReads() {
      this.$API("/frontDesk/news/news-read", "get", {type: 5, beenRead: 1,});
    },
    userNewsFaLists() {
      this.$API("/frontDesk/news/list", "get", this.queryParams).then(res => {
        this.newsList = res.data.records;
        this.total = res.data.records.total;
        this.loading = false;
        this.animator.triggerAllItemsAnimation(this.newsList, 'newsSystemItem');
      })
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        this.load()
      }
    },
    load() {
      if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
        this.scrollLoading = false;
        this.queryParams.pageNum = this.queryParams.pageNum + 1;
        this.moreLoading = true;
        const startIndex = this.newsList.length; // 记录新增前的索引
        this.$API("/frontDesk/news/list", "get", this.queryParams).then(res => {
          res.records.forEach(item => {
            this.newsList.push(item)
          })
          this.total = res.total;
          this.animator.triggerNewItemsAnimation(startIndex, res.records.length, 'newsSystemItem');
        }).finally(() => this.scrollLoading = true)
      } else {
        this.moreLoading = false;
      }
    },
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  },
  mounted() {
    this.animator = createAnimator(this, 'commonList');
    window.addEventListener('scroll', this.getData, true);
    this.userNewsFaLists()
    this.userNewsReads()
  }
}
</script>

<style scoped>
.news-system-cl {
  font-size: 14px;
  padding: 8px 15px;
}
</style>
