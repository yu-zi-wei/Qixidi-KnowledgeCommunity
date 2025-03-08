<template>
  <div style="min-height: 85vh">
    <el-skeleton class="mt-10 ml-10" :rows="4" animated v-if="loading"/>
    <div class="ml-10">
      <div v-if="newsList.length>0" v-for="(item,index) in newsList" class="mb-20 for-div">
        <div class="flex-left align-items-center">
          <div class="mr-10" @click="routeWindow('/user_home/article',item.commentUid)">
            <el-avatar v-if="item.commentAvatar" :src="item.commentAvatar" :size="35"></el-avatar>
            <el-avatar v-else src="/img/tx.jpg" :size="35"></el-avatar>
          </div>
          <div class="hover-cl cursor-pointer mr-8"
               @click="routeWindow('/user_home/article',item.commentUid)">{{ item.commentName }}
          </div>
          <div class="font-s-14 color-grey">{{ item.type == 1 ? "评论了你的文章：" : "回复了你的评论：" }}</div>
          <div class="cursor-pointer text-underline hover-cl" title="查看详情">
            <nuxt-link :to="`/article/article-details/`+$base64.encode(item.articleId)" target="_blank">
              [ {{ item.articleTitle }} ]
            </nuxt-link>
          </div>
        </div>
        <div class="comment-div padding-10" title="查看详情">
          <nuxt-link :to="`/article/article-details/`+$base64.encode(item.articleId)" target="_blank">
            <vditor-preview :id="'aiEditor-news-introduce-'+index" :content="item.content"
                            v-if="!loading"></vditor-preview>
          </nuxt-link>
        </div>
        <div class="flex-left color-grey-2" style="margin-left: 45px">
          <div class="font-s-13" title="时间">
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
            <span v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{s}')"></span>
          </div>
          <div class="ml-15 ri-ioc cursor-pointer" title="回复">
            <el-popover
              placement="bottom-start"
              width="800"
              trigger="click">
              <div>
                <vditor-md v-if="commentState" :vditor-id="'aiEditor-news-introduce1-'+index"
                           :mdContent.sync="comment.content"
                           :content="comment.content"
                           :outline="false"
                           :height="'200px'"
                ></vditor-md>
                <el-button class="fl-right mt-10" type="primary" plain @click="sendComment(item)" size="medium">回复
                </el-button>
              </div>
              <div v-if="!commentState">
                评论已删除
              </div>
              <div slot="reference" class="hover-cl icon-hover">
                <svg t="1741407164890" class="icon-theme-1 icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="27498">
                  <path
                    d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                    p-id="27499"></path>
                </svg>
                <span class="font-s-13" @click="replyComment(item.newsId)">回复</span>
              </div>
            </el-popover>
          </div>
        </div>
        <div>
        </div>
      </div>
      <div v-if="!loading && newsList.length==0" style="text-align: center;margin-top: 10px;">
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
    <div v-show="newsList.length>20" class="pb-10 text-center font-s-14 color-grey-2">
      <div class="border-ts-class" v-if="moreLoading">加载中<i style="font-size: 13px"
                                                               class="fa fa-spinner fa-spin fa-3x fa-fw"></i></div>
      <div class="border-ts-class" v-if="!moreLoading">没有更多了...</div>
    </div>
  </div>
</template>

<script>

import VditorMd from "../../components/Vditor-md.vue";
import VditorPreview from "../../components/Vditor-preview.vue";

export default {
  name: "comment",
  components: {VditorPreview, VditorMd},
  data() {
    return {
      newsList: [],
      loading: true,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        type: 1,
      },
      commentState: true,
      total: 0,
      buttonLoading: false,
      comment: {
        articleId: null,
        uid: null,
        parentId: null,
        commentGrade: null,
        targetId: null,
        targetUid: null,
        type: null,
        content: '',
        contentTwo: '',
        worksContent: '',
      },
      scrollLoading: true,
      moreLoading: true,
    }
  },
  methods: {
    routeArticle(id) {

    },
    routeWindow(url, id) {
      let routeInfo = this.$router.resolve({
        path: url,
        query: {uuid: this.$base64.encode(id)},
      });
      window.open(routeInfo.href, '_blank');
    },
    userNewsReads() {
      this.$API("/frontDesk/news/news-read", "get", {type: 1, beenRead: 1,});
    },
    replyComment(id) {
      this.commentState = true;
      this.$API("/get/comment/" + id, "get").then(res => {
        if (res.data == null) {
          this.commentState = false;
          this.$modal.notifyError("评论已删除!");
        } else {
          this.commentState = true;
        }
      })
    },
    sendComment(item) {
      console.log("itemL", item)
      this.buttonLoading = true;
      this.comment.articleId = item.articleId;
      this.comment.uid = item.targetUid;
      this.comment.worksContent = item.articleTitle;
      this.comment.parentId = item.commentGrade >= 2 ? item.parentId : item.id;
      this.comment.commentGrade = item.commentGrade >= 2 ? 3 : 2;
      this.comment.targetId = item.id;
      this.comment.targetUid = item.commentUid;
      this.comment.type = 2;
      this.$API("/article/comment/insert", "post", null, this.comment).then(res => {
        if (res.code === 200) {
          this.$modal.msg("回复成功！");
        }
      }).finally(() => this.buttonLoading = false)
    },
    userNewsLists() {
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
    this.userNewsLists();
    this.userNewsReads();
  }
}
</script>

<style>
@import url("~/assets/news/comment.css");

.el-menu-demo {
  font-size: 16px;
}

.el-button {
  padding: 9px 15px;
}
</style>
