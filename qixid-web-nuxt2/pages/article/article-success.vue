<template>
  <div style="width: 1200px;margin: auto">
    <div class="box-wai">
      <span>发布成功，有了你的分享，{{websiteName}}会变得更好！</span>
    </div>
    <div class="box-two">
      <div>
        <nuxt-link class="box-title" :to="`/article/article-details/`+$base64.encode(article.id)"
                   rel="noopener">
          {{ article.articleTitle }}
        </nuxt-link>
        <br/>
        <span class="box-tips mr-20 color-theme">如何玩转？了解</span>
        <el-button size="small" round plain @click="articleEdit(article.id)">继续编辑</el-button>
      </div>
      <div>
        <span class="mr-20">分享到:</span>
        <el-button round>微信</el-button>
        <el-button round>QQ</el-button>
        <el-button round>微博</el-button>
        <el-button round @click="copyLink(article.id)">复制链接</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "articleSuccess",
  data() {
    return {
      websiteName: process.env.PROJECT_NAME,
      article: {
        articleTitle: null,//标题
      },
      state: false,
    }
  },
  methods: {
    articleEdit(id) {
      this.$router.push({
        path: '/article/publish-article',
        query: {id: this.$base64.encode(id)}
      })
    },
    basics() {
      let id = this.$base64.decode(this.$route.query.id)
      this.$API("/white/article/basic/" + id, this.$get()).then(res => {
        this.article = res.data;
        this.state = true;
      })
    },
    copyLink(id) {
      let ids = this.$base64.encode(id);
      const domainName = window.location.hostname;
      let url = domainName + "?id=" + ids;
      navigator.clipboard.writeText(url);
      this.$modal.notifySuccess("链接地址复制成功");
    },
  },
  mounted() {
    this.basics();
  }
}
</script>

<style scoped>
.box-tips {
  font-size: 16px;
  font-weight: 400;
  line-height: 22px;
  cursor: pointer;
  margin-bottom: 40px;
  display: block;
  display: inline-block;
}

.box-title {
  font-size: 24px;
  text-align: center;
  margin-bottom: 40px;
  display: block;
  cursor: pointer;
  color: inherit;
  text-decoration: underline;
}

.box-wai {
  min-height: 160px;
  text-align: center;
  height: 70px;
  padding-top: 150px;
  background-repeat: no-repeat;
  background-position: center 0;
  background-size: 100px;
  font-size: 16px;
  color: #666;
}

.box-two {
  margin-left: 100px;
  margin-right: 100px;
  padding: 40px 20px 60px;
  background-color: #fff;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);
  margin-bottom: 10px;
  text-align: center;
  border-radius: 4px;
}
</style>
