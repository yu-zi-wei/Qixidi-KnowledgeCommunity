<template>
  <div style="min-height: 85vh">
    <el-skeleton class="mt-10 ml-10" :rows="4" animated v-if="loading"/>
    <div v-for="(item,index) in newsList" class="follow-news-cl mb-20" :key="index" :ref="`newsFollowItem${index}`">
      <div class="flex-left">
        <div class="mr-15">
          <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(item.senderId)" target="_blank">
            <el-avatar v-if="item.senderAvatar" :src="item.senderAvatar"></el-avatar>
            <el-avatar v-else src="/img/tx.jpg"></el-avatar>
          </nuxt-link>
        </div>
        <div>
          <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(item.senderId)" target="_blank">
            <span class="mr-6 cursor-pointer hover-cl">{{ item.senderName }} </span>
            <span class="font-s-14 color-grey mr-6">关注了</span>您
          </nuxt-link>
          <div class="mt-8 font-s-14 color-grey">
            <svg t="1741416645564" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                 version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="38483">
              <path
                d="M512 64C264.946 64 64 264.946 64 512s200.946 448 448 448 448-200.946 448-448S759.054 64 512 64z m0 831.828c-211.613 0-383.828-172.215-383.828-383.828S300.216 128.172 512 128.172c211.785 0 383.828 172.044 383.828 383.828 0 211.785-172.215 383.828-383.828 383.828z"
                p-id="38484"></path>
              <path
                d="M672.172 512h-160V288.173c0-17.72-14.28-32.172-32-32.172s-32 14.451-32 32.172v256c0 17.719 14.28 31.999 32 31.999h192c17.72 0 32.173-14.28 32.173-31.999C704.172 526.28 689.721 512 672.172 512z"
                p-id="38485"></path>
            </svg>
            <span>{{ $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!loading && newsList.length==0" style="text-align: center;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme-2" viewBox="0 0 1024 1024" version="1.1"
           xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="50">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-12 color-grey-2">暂无消息</div>
    </div>
    <div v-show="newsList.length>20" class="pb-10 text-center font-s-14 color-grey-2">
      <div class="border-ts-class" v-if="moreLoading">加载中<i style="font-size: 13px"
                                                               class="fa fa-spinner fa-spin fa-3x fa-fw"></i></div>
      <div class="border-ts-class" v-if="!moreLoading">没有更多了...</div>
    </div>
  </div>
</template>

<script>
import {createAnimator} from '~/plugins/animationUtils';

export default {
  name: "follow",
  data() {
    return {
      newsList: [],
      loading: true,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        type: 3,
      },
      scrollLoading: true,
      moreLoading: true,
      animator: null, // 动画器实例
    }
  },
  methods: {
    userNewsFaLists() {
      this.$API("/frontDesk/news/list", "get", this.queryParams).then(res => {
        this.newsList = res.data.records;
        this.total = res.data.records.total;
        this.loading = false;
        this.animator.triggerAllItemsAnimation(this.newsList, 'newsFollowItem');
      })
    },
    userNewsReads() {
      this.$API("/frontDesk/news/news-read", "get", {type: 3, beenRead: 1,});
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
          this.animator.triggerNewItemsAnimation(startIndex, res.records.length, 'newsFollowItem');
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
    this.userNewsFaLists();
    this.userNewsReads();
  }
}
</script>

<style scoped>
.follow-news-cl {
  background-color: #FFFFFF;
  margin: 15px;
  padding-bottom: 10px;
  border-radius: 4px;
  border-bottom: 1px solid #dfe4ea;
}
</style>
