<template>
  <div>
    <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
    <div v-if="loading" style="width: 100%;"></div>
    <ul class="content" v-if="collectionUserLoading" ref="dialogContent" @scroll="divScroll($event)">
      <li v-for="item of articleList" class="contentItem">
        <div class="font-s-13 color-grey mb-8">
          <span v-text="item.nickname"></span>
          <span>|</span>
          <span v-text="$utils.reckonTime(item.createTime)"
                :title="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
        </div>
        <nuxt-link class="text-underline-hover" :to="`/article/article-details/`+$base64.encode(item.id)"
                   target="_blank" rel="noopener">
          {{ item.articleTitle }}
        </nuxt-link>
        <div class="abstract-class mt-10 color-grey-2" v-text="item.articleAbstract"></div>
        <div class="font-s-13 color-grey bo-list-loc flex-left">
          <div class="mr-4">
            <svg t="1710640089142" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1036 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="2367">
              <path
                d="M1030.789565 494.525217c-128.24487-158.809043-313.121391-249.900522-507.213913-249.900522-193.892174 0-378.63513 90.935652-506.902261 249.499826-3.205565 3.962435-4.964174 8.904348-4.964174 14.002087l0 28.605217c0 5.097739 1.758609 10.039652 4.964174 14.002087 128.24487 158.541913 313.010087 249.477565 506.902261 249.477565 194.092522 0 378.946783-91.091478 507.213913-249.900522 3.205565-3.962435 4.941913-8.904348 4.941913-13.979826l0-27.781565C1035.731478 503.429565 1033.99513 498.509913 1030.789565 494.525217zM991.209739 528.361739c-119.54087 144.562087-289.52487 227.305739-467.634087 227.305739-177.90887 0-347.803826-82.610087-467.322435-226.905043l0-12.710957C175.771826 371.756522 345.689043 289.146435 523.597913 289.146435c178.086957 0 348.093217 82.743652 467.634087 227.305739L991.232 528.361739zM523.731478 351.098435c-94.45287 0-171.297391 76.844522-171.297391 171.319652 0 94.45287 76.844522 171.297391 171.297391 171.297391 25.956174 0 50.910609-5.654261 74.128696-16.806957 11.085913-5.320348 15.760696-18.610087 10.418087-29.696-5.320348-11.085913-18.654609-15.716174-29.696-10.418087-17.16313 8.236522-35.617391 12.421565-54.828522 12.421565-69.921391 0-126.775652-56.876522-126.775652-126.775652s56.876522-126.797913 126.775652-126.797913 126.775652 56.876522 126.775652 126.797913c0 11.887304-1.647304 23.641043-4.87513 34.949565-3.383652 11.820522 3.450435 24.130783 15.270957 27.536696 11.776 3.361391 24.130783-3.450435 27.536696-15.270957 4.385391-15.293217 6.589217-31.165217 6.589217-47.215304C695.05113 427.942957 618.206609 351.098435 523.731478 351.098435zM443.191652 491.341913c-3.82887 9.928348-5.765565 20.390957-5.765565 31.076174 0 6.144 4.986435 11.130435 11.130435 11.130435s11.130435-4.986435 11.130435-11.130435c0-7.94713 1.424696-15.716174 4.274087-23.062261 9.48313-24.531478 33.502609-41.004522 59.792696-41.004522 6.144 0 11.130435-4.986435 11.130435-11.130435s-4.986435-11.130435-11.130435-11.130435C488.314435 436.068174 455.94713 458.284522 443.191652 491.341913z"
                p-id="2368"></path>
            </svg>
            <span v-text="item.numberTimes==0?'浏览':item.numberTimes"></span>
          </div>
          <div class="mr-4">
            <svg t="1697947657508" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="1484">
              <path
                d="M857.28 344.992h-264.832c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-71.808-153.792-140.544-143.808-60.608 8.8-89.536 59.904-89.536 125.536v59.296c0 76.064-58.208 140.928-132.224 148.064l-117.728-0.192A67.36 67.36 0 0 0 64 483.04V872c0 37.216 30.144 67.36 67.36 67.36h652.192a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824zM128 872V483.04c0-1.856 1.504-3.36 3.36-3.36H208v395.68H131.36A3.36 3.36 0 0 1 128 872z m767.328-417.088l-73.728 388.96a38.72 38.72 0 0 1-38.048 31.488H272V476.864a213.312 213.312 0 0 0 173.312-209.088V208.512c0-37.568 12.064-58.912 34.72-62.176 27.04-3.936 67.36 38.336 67.36 80.48 0 37.312-9.504 84-28.864 139.712a32 32 0 0 0 30.24 42.496h308.512a38.72 38.72 0 0 1 38.048 45.888z"
                p-id="1485"></path>
            </svg>
            <span v-text="item.likeTimes==0?'点赞':item.likeTimes"></span>
          </div>
          <div class="mr-4">
            <svg t="1697955184702" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="2570">
              <path
                d="M850.879104 96.41591l-676.303067 0c-60.681034 0-110.049418 49.367361-110.049418 110.049418l0 446.200388c0 60.681034 49.367361 110.049418 110.049418 110.049418l90.307795 0L396.936381 931.129846c3.793396 4.838192 9.598612 7.66354 15.746636 7.66354s11.952216-2.825348 15.746636-7.66354l132.052548-168.414711 290.396903 0c60.681034 0 110.049418-49.367361 110.049418-110.049418L960.928522 206.465329C960.928522 145.784294 911.561162 96.41591 850.879104 96.41591zM920.91111 652.665717c0 38.614459-31.416524 70.030983-70.030983 70.030983L550.744419 722.6967c-6.147 0-11.952216 2.825348-15.745612 7.66354L412.683017 886.356107l-122.31579-155.995867c-3.792373-4.838192-9.597589-7.66354-15.745612-7.66354l-100.045577 0c-38.614459 0-70.030983-31.416524-70.030983-70.030983L104.545054 206.465329c0-38.614459 31.416524-70.030983 70.030983-70.030983l676.303067 0c38.614459 0 70.030983 31.416524 70.030983 70.030983L920.910087 652.665717z"
                p-id="2571"></path>
              <path
                d="M272.621051 344.526731c-44.132126 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.903721 80.036871 80.035848 80.036871s80.035848-35.904745 80.035848-80.036871C352.655875 380.430452 316.752154 344.526731 272.621051 344.526731zM272.621051 464.582037c-22.065552 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.952884-40.017412 40.017412-40.017412 22.065552 0 40.017412 17.951861 40.017412 40.017412C312.638463 446.629153 294.686602 464.582037 272.621051 464.582037z"
                p-id="2572"></path>
              <path
                d="M512.727571 344.526731c-44.132126 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.903721 80.036871 80.035848 80.036871 44.132126 0 80.035848-35.904745 80.035848-80.036871C592.763418 380.430452 556.859697 344.526731 512.727571 344.526731zM512.727571 464.582037c-22.065552 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.951861-40.017412 40.017412-40.017412 22.065552 0 40.017412 17.951861 40.017412 40.017412C552.746006 446.629153 534.793122 464.582037 512.727571 464.582037z"
                p-id="2573"></path>
              <path
                d="M752.836137 344.526731c-44.131103 0-80.035848 35.903721-80.035848 80.035848 0 44.132126 35.904745 80.036871 80.035848 80.036871s80.035848-35.904745 80.035848-80.036871C832.871985 380.430452 796.96724 344.526731 752.836137 344.526731zM752.836137 464.582037c-22.066575 0-40.017412-17.951861-40.017412-40.018436 0-22.065552 17.951861-40.017412 40.017412-40.017412s40.017412 17.951861 40.017412 40.017412C792.853549 446.629153 774.902712 464.582037 752.836137 464.582037z"
                p-id="2574"></path>
            </svg>
            <span v-text="item.commentTimes==0?'评论':item.commentTimes"></span>
          </div>
          <div title="文章状态" class="mr-4">
            <el-tag v-if="item.auditState==1" size="mini">审核中</el-tag>
            <el-tag v-if="item.auditState==2" type="success" size="mini">已发布</el-tag>
            <el-tag v-if="item.auditState==3" type="danger" size="mini">审核未通过</el-tag>
            <el-tag v-if="item.auditState==4" type="warning" size="mini">草稿</el-tag>
          </div>
          <div v-if="currentUser">
            <el-dropdown trigger="click">
              <div class="el-dropdown-link cursor-pointer" title="更多">
                <i class="el-icon-more"></i>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="articleEdit(item.id)">编辑</el-dropdown-item>
                <el-dropdown-item @click.native="deletes(item)">删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
        <div style="padding-right: 16px">
          <hr class="hr_gradient"/>
        </div>
      </li>
    </ul>
    <div v-else style="text-align: center;margin-right: 40px;margin-top: 10px">
      <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="2698" width="40" height="60">
        <path
          d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
          p-id="2699"></path>
      </svg>
      <div class="font-s-14 color-grey">暂无数据</div>
    </div>
  </div>
</template>

<script>

export default {
  name: "userArticle",
  data() {
    return {
      loading: true,
      currentUser: false,
      collectionUserLoading: true,
      articleList: [],
      uuid: null,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        userId: null,
        articleTitle: null,
      },
      total: 0,
      scrollLoading: true,
    }
  },
  methods: {
    articleEdit(id) {
      this.$router.push({
        path: '/article/publish-article',
        query: {id: this.$base64.encode(id)}
      })
    },
    deletes(item) {
      this.$modal.confirm('确认要删除《' + item.articleTitle + '》文章吗？').then(() => {
        this.loading = true;
        return this.$API("/user/delete/article/" + item.id, "delete");
      }).then(() => {
        this.getArticleInfos();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => this.loading = false)
    },
    isCurrentUser() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null || res.data == null) {
          this.currentUser = false;
          return;
        }
        if (res.data.uuid == uuid) {
          this.currentUser = true;
        }
      }).finally(() => this.getArticleInfos())
    },
    divScroll(e) {
      let scrollHeight = this.$refs.dialogContent.scrollHeight;
      let clientHeight = this.$refs.dialogContent.clientHeight;
      let scrollTop = this.$refs.dialogContent.scrollTop;
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
          this.scrollLoading = false;
          this.queryParams.pageNum = this.queryParams.pageNum + 1;
          this.$API("/white/article/user/list", "get", this.queryParams).then(res => {
            res.data.records.forEach(item => {
              this.articleList.push(item)
            })
            this.total = res.data.total;
          }).finally(() => this.scrollLoading = true)
        }
      }
    },
    getArticleInfos() {
      this.uuid = this.$base64.decode(this.$route.query.uuid)
      this.queryParams.userId = this.uuid;
      this.$API("/white/article/user/list", "get", this.queryParams).then(res => {
        this.articleList = res.data.records;
        this.total = res.data.total;
        this.loading = false;
        if (this.articleList.length == 0) {
          this.collectionUserLoading = false;
        }
      })
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.isCurrentUser();
    }
  },
  mounted() {
    this.isCurrentUser();
  }
}
</script>

<style scoped>
.hr_gradient {
  border: 0;
  height: 1px;
  background: #dcdfe6;
  margin-top: 15px;
  margin-bottom: 15px;
}

.bo-list-loc {
  margin-top: 10px;
}

.bo-list-loc span {
  margin-right: 14px;
  cursor: pointer;
}

.abstract-class {
  white-space: nowrap;
  overflow: hidden;
  color: #5a5e66;
  font-size: 14px;
  text-overflow: ellipsis;
  padding: 6px 20px 6px 0;
}

.content {
  height: calc(100vh - 400px);
  background-color: #fefefe;
  overflow: scroll;
  overflow-x: hidden;
}

.contentItem {
  margin: 8px 0;
  padding: 16px 0 4px 16px;
  border-radius: 4px;
  transition: .2s;
}

.contentItem:hover {
  transform: translateY(-4px);
  background-color: #f4f5f5;
}

.content::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: #ced6e0;
}

.content::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 2px;
}

.content::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 2px;
}
</style>
