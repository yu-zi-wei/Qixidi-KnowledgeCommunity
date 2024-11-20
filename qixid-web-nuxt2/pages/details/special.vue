<template>
  <div class="admin-index">
    <div class="home-index">
      <div class="top-cl-index">
        <p class="font-s-24 color-theme text-center" v-text="specialInfo.specialName"></p>
        <el-row>
          <el-col :span="4">
            <el-image v-if="specialInfo.cover" :src="specialInfo.cover" fit="contain" :size="160"></el-image>
            <el-image v-else src="/img/shu.jpg" fit="contain" :size="160"></el-image>
          </el-col>
          <el-col :span="19" class="ml-15 color-grey4">
            <div class="font-s-14 flex-left align-items-center">
              <el-avatar v-if="userinfo.avatar" :src="userinfo.avatar" :size="40"></el-avatar>
              <el-avatar v-else src="/img/tx.jpg" :size="40"></el-avatar>
              <div class="ml-6 font-bold font-s-16" v-text="userinfo.nickname"></div>
            </div>
            <div class="info-b-cl fl-left">
              <span>暂无关注</span>
              <span>共{{ specialInfo.includedCount }}篇文章</span>
              <span>创建于：{{ $utils.parseTime(specialInfo.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </div>
            <el-button v-if="currentUser" class="fl-right" size="small" @click="administration(specialInfo)">专栏管理
            </el-button>
          </el-col>
        </el-row>
      </div>
      <div class="bottom-cl">
        <div>
          <el-tabs v-model="activeName">
            <el-tab-pane label="专 栏" name="special">
              <el-skeleton class="mt-10" v-if="loading" :rows="4" animated/>
              <ul class="content" v-if="collectionUserLoading">
                <li v-if="articleList.length>0" v-for="(item,index) in articleList" class="contentItem" :key="index">
                  <div class="font-s-13 mb-10">
                    <span class="font-bold" v-text="item.nickname"></span>
                    <span class="color-grey">|</span>
                    <span class="color-grey" v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                  </div>
                  <p :title="item.articleTitle" class="title-class text-underline-hover"
                     @click="articleDetails(item.id)"
                     v-text="item.articleTitle"></p>
                  <div class="abstract-class mt-10" v-text="item.articleAbstract"></div>
                  <div class="font-s-13 color-grey bo-list-loc flex-left">
                    <div>
                      <svg t="1719234506770" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024"
                           version="1.1"
                           xmlns="http://www.w3.org/2000/svg" p-id="1739">
                        <path
                          d="M512 280.901818c171.752727 0 328.145455 167.098182 387.956364 238.545455C839.68 591.127273 683.287273 757.992727 512 757.992727S183.389091 591.127273 123.578182 519.447273C183.389091 448 339.781818 280.901818 512 280.901818zM512 209.454545C298.356364 209.454545 117.76 412.858182 56.785455 490.123636a46.545455 46.545455 0 0 0 0 58.647273C117.76 626.036364 298.356364 829.44 512 829.44s393.309091-203.403636 454.283636-280.669091a46.545455 46.545455 0 0 0 0-58.647273C905.309091 412.858182 724.712727 209.454545 512 209.454545z m0 238.545455a71.68 71.68 0 1 1-69.818182 71.447273 69.818182 69.818182 0 0 1 69.818182-71.447273z m0-71.68A139.636364 139.636364 0 0 0 382.603636 465.454545a146.385455 146.385455 0 0 0 30.254546 155.927273 137.076364 137.076364 0 0 0 151.970909 30.254546A143.127273 143.127273 0 0 0 651.636364 519.447273a141.265455 141.265455 0 0 0-139.636364-143.127273z"
                          p-id="1740"></path>
                      </svg>
                      <span class="color-grey-2" v-text="item.likeTimes==0?'浏览':item.likeTimes"></span>
                    </div>
                    <div>
                      <svg t="1697947657508" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024"
                           version="1.1"
                           xmlns="http://www.w3.org/2000/svg"
                           p-id="1484">
                        <path
                          d="M857.28 344.992h-264.832c12.576-44.256 18.944-83.584 18.944-118.208 0-78.56-71.808-153.792-140.544-143.808-60.608 8.8-89.536 59.904-89.536 125.536v59.296c0 76.064-58.208 140.928-132.224 148.064l-117.728-0.192A67.36 67.36 0 0 0 64 483.04V872c0 37.216 30.144 67.36 67.36 67.36h652.192a102.72 102.72 0 0 0 100.928-83.584l73.728-388.96a102.72 102.72 0 0 0-100.928-121.824zM128 872V483.04c0-1.856 1.504-3.36 3.36-3.36H208v395.68H131.36A3.36 3.36 0 0 1 128 872z m767.328-417.088l-73.728 388.96a38.72 38.72 0 0 1-38.048 31.488H272V476.864a213.312 213.312 0 0 0 173.312-209.088V208.512c0-37.568 12.064-58.912 34.72-62.176 27.04-3.936 67.36 38.336 67.36 80.48 0 37.312-9.504 84-28.864 139.712a32 32 0 0 0 30.24 42.496h308.512a38.72 38.72 0 0 1 38.048 45.888z"
                          p-id="1485"></path>
                      </svg>
                      <span class="color-grey-2" v-text="item.numberTimes==0?'点赞':item.numberTimes"></span>
                    </div>
                    <div>
                      <svg t="1697955184702" class="icon icon-size-14 svg-translateY-2" viewBox="0 0 1024 1024"
                           version="1.1"
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
                      <span class="color-grey-2" v-text="item.commentTimes==0?'评论':item.commentTimes"></span>
                    </div>
                    <div title="文章状态">
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
              <div v-if="articleList.length==0">
                <div class="text-center">
                  <svg t="1666708559980" class="icon-theme" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg"
                       p-id="2698" width="40" height="40">
                    <path
                      d="M734.208 354.461538a78.769231 78.769231 0 0 1 73.216 49.703385L866.461538 552.881231V787.692308a78.769231 78.769231 0 0 1-78.76923 78.76923H236.307692a78.769231 78.769231 0 0 1-78.76923-78.76923v-231.699693l59.195076-151.433846A78.769231 78.769231 0 0 1 290.107077 354.461538h444.100923z m-355.209846 212.676924H189.046154L189.046154 787.692308a47.261538 47.261538 0 0 0 42.417231 47.02523L236.307692 834.953846h551.384616a47.261538 47.261538 0 0 0 47.02523-42.417231L834.953846 787.692308v-220.553846h-189.952a133.907692 133.907692 0 0 1-266.003692 0z m355.249231-181.169231H290.067692a47.261538 47.261538 0 0 0-41.865846 25.284923l-2.166154 4.765538L199.286154 535.630769h190.306461l-0.039384 0.590769A15.753846 15.753846 0 0 1 409.6 551.384615a102.4 102.4 0 0 0 204.8 0 15.753846 15.753846 0 0 1 14.848-15.753846h196.450462l-47.576616-119.847384a47.261538 47.261538 0 0 0-38.675692-29.538462l-5.238154-0.275692zM286.168615 106.417231l2.166154 2.363077 114.609231 155.254154a15.753846 15.753846 0 0 1-23.158154 21.070769l-2.166154-2.363077-114.60923-155.214769a15.753846 15.753846 0 0 1 23.158153-21.110154z m244.460308-4.017231a15.753846 15.753846 0 0 1 15.438769 12.603077l0.315077 3.150769v155.254154a15.753846 15.753846 0 0 1-31.192615 3.150769l-0.315077-3.150769V118.153846c0-8.664615 7.049846-15.753846 15.753846-15.753846z m265.688615 3.072a15.753846 15.753846 0 0 1 4.962462 19.298462l-1.614769 2.756923-114.333539 155.175384a15.753846 15.753846 0 0 1-26.978461-15.911384l1.575384-2.756923 114.372923-155.21477a15.753846 15.753846 0 0 1 22.016-3.347692z"
                      p-id="2699"></path>
                  </svg>
                  <div class="font-s-12 color-grey">暂无文章</div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "special",
  data() {
    return {
      activeName: 'special',
      profile: "",
      articleList: [],
      loading: false,
      currentUser: false,
      collectionUserLoading: false,
      scrollLoading: true,
      specialInfo: {
        specialName: null,
        cover: null,
      },
      uuid: null,
      total: null,
      userinfo: {
        avatar: null,
        nickname: null,
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        specialId: null,
        articleTitle: null,
      },
    }
  },
  methods: {
    administration(item) {
      this.$router.push({
        path: '/user_admin/special/special-administration',
        query: {id: this.$base64.encode(item.id), title: item.specialName}
      })
    },
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
    articleDetails(id) {
      let routeInfo = this.$router.resolve({
        path: "/article-details",
        name: "文章详情页",
        query: {id: this.$base64.encode(id)},
      });
      window.open(routeInfo.href, '_blank');
    },
    getInfos() {
      let specialId = this.$base64.decode(this.$route.query.id)
      this.$API("/white/special/" + specialId, "get").then(res => {
        this.specialInfo = res.data;
      })
    },
    getWebsiteInfos() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/white/user/info/" + uuid, "get").then(res => {
        this.userinfo = res.data;
      })
    },
    isCurrentUser() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      let specialId = this.$base64.decode(this.$route.query.id)
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null || res.data == null) {
          this.currentUser = false;
          return;
        }
        if (res.data != null && res.data.uuid == uuid) {
          this.currentUser = true;
        }
        this.queryParams.userId = uuid;
        this.queryParams.specialId = specialId;
        this.getArticleInfos();
      });
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
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
      this.loading = true;
      this.queryParams.pageNum = 1;
      this.$API("/white/article/user/list", "get", this.queryParams).then(res => {
        this.articleList = res.data.records;
        this.total = res.data.total;
        this.loading = false;
        this.collectionUserLoading = true;
        if (this.articleList.length == 0) {
          this.collectionUserLoading = false;
        }
      })
    }
  },
  mounted() {
    this.isCurrentUser();
    this.getWebsiteInfos();
    this.getInfos();
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style scoped>
@import url("~/assets/userHome/specialIndex.css");

.top-cl-index {
  padding: 10px 0 20px 20px;
  border-radius: 4px;
  background-color: #dcdde1;
}
</style>
