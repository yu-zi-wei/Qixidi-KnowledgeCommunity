<template>
  <div style="min-height: 200px">
    <el-skeleton :rows="4" animated v-if="loading"/>
    <ul class="background-color-fefefe padding-10">
      <li v-for="item of newsList" class="news-system-cl">
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
        <div class="mt-4 color-grey text-right">{{ $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{s}') }}</div>
        <hr class="hr-item mt-4"/>
      </li>
    </ul>
  </div>
</template>

<script>

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
      })
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        // this.queryParams.pageNum = this.queryParams.pageNum + 1;
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
