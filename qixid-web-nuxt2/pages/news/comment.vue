<template>
  <div style="min-height: 200px">
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
          <div class="color-green cursor-pointer" title="查看详情">
            <nuxt-link class="hover-cl" :to="`/article/article-details/`+$base64.encode(item.articleId)"
                       target="_blank">
              [{{ item.articleTitle }}]
            </nuxt-link>
          </div>
        </div>
        <div class="comment-div" title="查看详情">
          <nuxt-link :to="`/article/article-details/`+$base64.encode(item.articleId)" target="_blank">
            <ai-editor-module :ai-editor-id="'aiEditor-news-introduce-'+index" v-if="!loading"
                              :content="item.content"
                              :editable="false"></ai-editor-module>
          </nuxt-link>
        </div>
        <div class="flex-left color-grey-2" style="margin-left: 45px">
          <div class="font-s-13 mr-15">
            <svg t="1673695199778" class="icon icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="4659">
              <path
                d="M480 640l64 0C561.92 640 576 625.92 576 608S561.92 576 544 576l-64 0C462.08 576 448 590.08 448 608S462.08 640 480 640zM480 768l64 0C561.92 768 576 753.92 576 736 576 718.08 561.92 704 544 704l-64 0C462.08 704 448 718.08 448 736 448 753.92 462.08 768 480 768zM832 192l-128 0L704 160C704 142.08 689.92 128 672 128 654.08 128 640 142.08 640 160L640 192 384 192 384 160C384 142.08 369.92 128 352 128S320 142.08 320 160L320 192 192 192C121.6 192 64 249.6 64 320l0 512c0 70.4 57.6 128 128 128l640 0c70.4 0 128-57.6 128-128L960 320C960 249.6 902.4 192 832 192zM896 832c0 35.2-28.8 64-64 64L192 896c-35.2 0-64-28.8-64-64L128 448l768 0L896 832zM896 384 128 384 128 320c0-35.2 28.8-64 64-64l128 0 0 32C320 305.92 334.08 320 352 320S384 305.92 384 288L384 256l256 0 0 32C640 305.92 654.08 320 672 320 689.92 320 704 305.92 704 288L704 256l128 0c35.2 0 64 28.8 64 64L896 384zM288 768l64 0C369.92 768 384 753.92 384 736 384 718.08 369.92 704 352 704l-64 0C270.08 704 256 718.08 256 736 256 753.92 270.08 768 288 768zM672 640l64 0c17.92 0 32-14.08 32-32S753.92 576 736 576l-64 0C654.08 576 640 590.08 640 608S654.08 640 672 640zM288 640l64 0C369.92 640 384 625.92 384 608S369.92 576 352 576l-64 0C270.08 576 256 590.08 256 608S270.08 640 288 640zM672 768l64 0c17.92 0 32-14.08 32-32 0-17.92-14.08-32-32-32l-64 0c-17.92 0-32 14.08-32 32C640 753.92 654.08 768 672 768z"
                p-id="4660"></path>
            </svg>
            <span class="ml-2" v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{s}')"></span>
          </div>
          <div class="cursor-pointer" title="赞">
            <svg t="1672582016825" class="icon icon-size-16 svg-translateY-3" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="2706">
              <path
                d="M190.193225 471.411583c14.446014 0 26.139334-11.718903 26.139334-26.13831 0-14.44499-11.69332-26.164916-26.139334-26.164916-0.271176 0-0.490164 0.149403-0.73678 0.149403l-62.496379 0.146333c-1.425466-0.195451-2.90005-0.295735-4.373611-0.295735-19.677155 0-35.621289 16.141632-35.621289 36.114522L86.622358 888.550075c0 19.949354 15.96767 35.597753 35.670407 35.597753 1.916653 0 3.808746 0.292666 5.649674 0l61.022819 0.022513c0.099261 0 0.148379 0.048095 0.24764 0.048095 0.097214 0 0.146333-0.048095 0.24457-0.048095l0.73678 0 0-0.148379c13.413498-0.540306 24.174586-11.422144 24.174586-24.960485 0-13.55983-10.760065-24.441669-24.174586-24.981974l0-0.393973-50.949392 0 1.450025-402.275993L190.193225 471.409536z"
                p-id="2707"></path>
              <path
                d="M926.52241 433.948343c-19.283182-31.445176-47.339168-44.172035-81.289398-45.546336-1.77032-0.246617-3.536546-0.39295-5.380544-0.39295l-205.447139-0.688685c13.462616-39.059598 22.698978-85.58933 22.698978-129.317251 0-28.349675-3.193739-55.962569-9.041934-82.542948l-0.490164 0.049119c-10.638291-46.578852-51.736315-81.31498-100.966553-81.31498-57.264215 0-95.466282 48.15065-95.466282 106.126063 0 3.241834-0.294712 6.387477 0 9.532097-2.996241 108.386546-91.240027 195.548698-196.23636 207.513194l0 54.881958-0.785899 222.227314 0 229.744521 10.709923 0 500.025271 0.222057 8.746198-0.243547c19.35686 0.049119 30.239721-4.817726 47.803749-16.116049 16.682961-10.761088 29.236881-25.50079 37.490869-42.156122 2.260483-3.341095 4.028757-7.075139 5.106298-11.20111l77.018118-344.324116c1.056052-4.053316 1.348718-8.181333 1.056052-12.160971C943.643346 476.446249 938.781618 453.944769 926.52241 433.948343zM893.82573 486.837924l-82.983993 367.783411-0.099261-0.049119c-2.555196 6.141884-6.879688 11.596106-12.872169 15.427364-4.177136 2.727111-8.773827 4.351098-13.414521 4.964058-1.49812-0.195451-3.046383 0-4.620227 0l-477.028511-0.540306-0.171915-407.408897c89.323375-40.266076 154.841577-79.670527 188.596356-173.661202 0.072655 0.024559 0.124843 0.049119 0.195451 0.072655 2.99931-9.137101 6.313799-20.73423 8.697079-33.164331 5.551436-29.185716 5.258771-58.123792 5.258771-58.123792-4.937452-37.98001 25.940812-52.965306 44.364417-52.965306 25.304316 0.860601 50.263777 33.656541 50.263777 52.326762 0 0 5.600555 27.563776 5.649674 57.190537 0.048095 37.366026-4.6673 56.847729-4.6673 56.847729l-0.466628 0c-5.872754 30.879288-16.214287 60.138682-30.464849 86.964654l0.36839 0.342808c-2.358721 4.815679-3.709485 10.220782-3.709485 15.943111 0 19.922748 19.088754 21.742187 38.765909 21.742187l238.761895 0.270153c0 0 14.666024 0.465604 14.690584 0.465604l0 0.100284c12.132318-0.638543 24.221658 5.207605 31.100322 16.409738 5.504364 9.016351 6.437619 19.6045 3.486404 28.988218L893.82573 486.837924z"
                p-id="2708"></path>
              <path
                d="M264.827039 924.31872c0.319272 0.024559 0.441045 0.024559 0.295735-0.024559 0.243547-0.048095 0.367367-0.074701-0.295735-0.074701s-0.539282 0.026606-0.271176 0.074701C264.43409 924.343279 264.532327 924.343279 264.827039 924.31872z"
                p-id="2709"></path>
            </svg>
            <span class="font-s-13"> 点赞</span>
          </div>
          <div class="ml-15 ri-ioc cursor-pointer" title="回复">
            <el-popover
              placement="bottom-start"
              width="800"
              trigger="click">
              <div>
                <ai-editor-module v-if="commentState" :ai-editor-id="'aiEditor-news-introduce1-'+index"
                                  :htmlContent.sync="comment.content"
                                  :content="comment.content" :editor-height="'200px'">
                </ai-editor-module>
                <el-button class="fl-right mt-10" type="primary" plain @click="sendComment(item)" size="medium">回复
                </el-button>
              </div>
              <div v-if="!commentState">
                评论已删除
              </div>
              <div slot="reference" class="hover-cl icon-hover">
                <svg t="1672582075200" class="icon icon-hover icon-size-16 svg-translateY-4" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3664">
                  <path
                    d="M834.37778 716.13833l-85.74999 0c-11.879562 0-21.505803-9.657964-21.505803-21.441335 0-11.879562 9.625218-21.537526 21.505803-21.537526l85.74999 0c23.663956 0 42.97886-19.266809 42.97886-42.97886l0-429.788603c0-23.712051-19.314904-42.97886-42.97886-42.97886l-644.682905 0c-23.712051 0-42.97886 19.266809-42.97886 42.97886l0 429.788603c0 23.712051 19.266809 42.97886 42.97886 42.97886l365.224122 0c5.692652 0 11.17655 2.318812 15.173584 6.299473l193.46934 193.404871c8.394181 8.425903 8.394181 22.032806 0 30.379914-4.14132 4.204765-9.673313 6.347568-15.141862 6.347568-5.516644 0-11.048637-2.142803-15.189957-6.347568L546.029536 716.13833 189.694875 716.13833c-47.455825 0-86.005816-38.533618-86.005816-85.957721l0-429.788603c0-47.376007 38.549991-85.957721 86.005816-85.957721l644.682905 0c47.424102 0 85.957721 38.581714 85.957721 85.957721l0 429.788603C920.335501 677.604712 881.801882 716.13833 834.37778 716.13833L834.37778 716.13833zM318.631456 458.265168c-23.760147 0-42.97886-19.218714-42.97886-42.97886s19.218714-42.97886 42.97886-42.97886c23.712051 0 42.97886 19.218714 42.97886 42.97886S342.343507 458.265168 318.631456 458.265168L318.631456 458.265168zM512.004605 458.265168c-23.743774 0-42.97886-19.218714-42.97886-42.97886s19.235087-42.97886 42.97886-42.97886c23.760147 0 42.97886 19.218714 42.97886 42.97886S535.763728 458.265168 512.004605 458.265168L512.004605 458.265168zM705.441199 458.265168c-23.760147 0-42.97886-19.218714-42.97886-42.97886s19.218714-42.97886 42.97886-42.97886c23.712051 0 42.97886 19.218714 42.97886 42.97886S729.15325 458.265168 705.441199 458.265168L705.441199 458.265168z"
                    p-id="3665"></path>
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
        <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
             xmlns="http://www.w3.org/2000/svg"
             p-id="2698" width="40" height="40">
          <path
            d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
            p-id="2699"></path>
        </svg>
        <div class="font-s-12 color-grey">暂无消息</div>
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

export default {
  name: "comment",
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
</style>
