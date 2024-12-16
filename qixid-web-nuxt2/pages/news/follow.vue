<template>
  <div style="min-height: 200px">
    <el-skeleton class="mt-10 ml-10" :rows="4" animated v-if="loading"/>
    <div v-for="item of newsList" class="follow-news-cl mb-20">
      <div class="flex-left">
        <div class="mr-15">
          <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(item.senderId)" target="_blank">
            <el-avatar :src="item.senderAvatar"></el-avatar>
          </nuxt-link>
        </div>
        <div>
          <nuxt-link :to="`/user_home/article?uuid=`+$base64.encode(item.senderId)" target="_blank">
            <span class="mr-6 cursor-pointer hover-cl">{{ item.senderName }} </span>
            <span class="font-s-13 color-grey">关注了</span>
            您
          </nuxt-link>
          <div class="mt-8 font-s-13 color-grey-2">
            <svg t="1673695199778" class="icon fl-left" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="4659" width="16" height="16">
              <path
                d="M480 640l64 0C561.92 640 576 625.92 576 608S561.92 576 544 576l-64 0C462.08 576 448 590.08 448 608S462.08 640 480 640zM480 768l64 0C561.92 768 576 753.92 576 736 576 718.08 561.92 704 544 704l-64 0C462.08 704 448 718.08 448 736 448 753.92 462.08 768 480 768zM832 192l-128 0L704 160C704 142.08 689.92 128 672 128 654.08 128 640 142.08 640 160L640 192 384 192 384 160C384 142.08 369.92 128 352 128S320 142.08 320 160L320 192 192 192C121.6 192 64 249.6 64 320l0 512c0 70.4 57.6 128 128 128l640 0c70.4 0 128-57.6 128-128L960 320C960 249.6 902.4 192 832 192zM896 832c0 35.2-28.8 64-64 64L192 896c-35.2 0-64-28.8-64-64L128 448l768 0L896 832zM896 384 128 384 128 320c0-35.2 28.8-64 64-64l128 0 0 32C320 305.92 334.08 320 352 320S384 305.92 384 288L384 256l256 0 0 32C640 305.92 654.08 320 672 320 689.92 320 704 305.92 704 288L704 256l128 0c35.2 0 64 28.8 64 64L896 384zM288 768l64 0C369.92 768 384 753.92 384 736 384 718.08 369.92 704 352 704l-64 0C270.08 704 256 718.08 256 736 256 753.92 270.08 768 288 768zM672 640l64 0c17.92 0 32-14.08 32-32S753.92 576 736 576l-64 0C654.08 576 640 590.08 640 608S654.08 640 672 640zM288 640l64 0C369.92 640 384 625.92 384 608S369.92 576 352 576l-64 0C270.08 576 256 590.08 256 608S270.08 640 288 640zM672 768l64 0c17.92 0 32-14.08 32-32 0-17.92-14.08-32-32-32l-64 0c-17.92 0-32 14.08-32 32C640 753.92 654.08 768 672 768z"
                p-id="4660"></path>
            </svg>
            <span class="mt-2 ml-4 fl-left">{{ $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!loading && newsList.length==0" style="text-align: center;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="40">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-12 color-grey">暂无消息</div>
    </div>
    <div v-show="newsList.length>20" class="pb-10 text-center font-s-14 color-grey-2">
      <div class="border-ts-class" v-if="moreLoading">加载中<i style="font-size: 13px"
                                                               class="fa fa-spinner fa-spin fa-3x fa-fw"></i></div>
      <div class="border-ts-class" v-if="!moreLoading">没有更多了...</div>
    </div>
  </div>
</template>

<script>

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
    }
  },
  methods: {
    userNewsFaLists() {
      this.$API("/frontDesk/news/list", "get", this.queryParams).then(res => {
        this.newsList = res.data.records;
        this.total = res.data.records.total;
        this.loading = false;
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
        this.$API("/frontDesk/news/list", "get", this.queryParams).then(res => {
          res.records.forEach(item => {
            this.newsList.push(item)
          })
          this.total = res.total;
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
    window.addEventListener('scroll', this.getData, true);
    this.userNewsFaLists();
    this.userNewsReads();
  }
}
</script>

<style scoped>
.follow-news-cl {
  background-color: #FEFEFE;
  padding: 16px 40px 8px 16px;
  border-radius: 4px;
}
</style>
