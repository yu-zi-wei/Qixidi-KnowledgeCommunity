<template>
  <div class="module-main">
    <el-skeleton class="mt-20" :rows="16" animated v-if="loading"/>
    <div v-if="!loading" class="flex-space-between">
      <!--      类容-->
      <div class="flex-8">
        <el-menu
          :default-active="$route.path+($route.query.code==null?'':'?code='+$route.query.code)"
          :router="true"
          mode="horizontal"
          text-color="#2c3e50"
          style="padding: 6px;border-radius: 10px;
  background: rgb(238,174,202);
  background: radial-gradient(circle, rgba(238,174,202,0.4) 0%, rgba(148,187,233,0.4) 100%);"
          :active-text-color="themeColor"
          class="el-menu-demo">
          <el-menu-item index="/dictum" title="推荐" class="dictum-special-cl">
            <svg t="1682669961696" class="icon mr-4" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="4762" width="24" height="24">
              <path d="M0 0h1024v1024H0V0z" fill="#202425" opacity=".01" p-id="4763"></path>
              <path
                d="M136.533333 68.266667a34.133333 34.133333 0 0 1 34.133334-34.133334h682.666666a34.133333 34.133333 0 0 1 34.133334 34.133334v170.666666a34.133333 34.133333 0 0 1-34.133334 34.133334H170.666667a34.133333 34.133333 0 0 1-34.133334-34.133334V68.266667z"
                fill="#FFAA44" p-id="4764"></path>
              <path
                d="M102.4 170.666667a34.133333 34.133333 0 0 1 34.133333-34.133334h750.933334a34.133333 34.133333 0 0 1 34.133333 34.133334v593.3056a34.133333 34.133333 0 0 1-18.875733 30.5152l-360.209067 180.1216a68.266667 68.266667 0 0 1-61.0304 0L121.275733 794.487467A34.133333 34.133333 0 0 1 102.4 763.972267V170.666667z"
                :fill="themeColor" p-id="4765" data-spm-anchor-id="a313x.7781069.0.i17" class="selected"></path>
              <path
                d="M496.605867 305.3568a17.066667 17.066667 0 0 1 30.788266 0L576.853333 408.917333a17.066667 17.066667 0 0 0 13.175467 9.557334l113.800533 15.018666a17.066667 17.066667 0 0 1 9.5232 29.2864l-83.2512 79.018667a17.066667 17.066667 0 0 0-5.051733 15.496533l20.923733 112.8448a17.066667 17.066667 0 0 1-24.917333 18.090667l-100.864-54.749867a17.066667 17.066667 0 0 0-16.315733 0l-100.864 54.749867a17.066667 17.066667 0 0 1-24.917334-18.090667l20.8896-112.8448a17.066667 17.066667 0 0 0-5.0176-15.496533l-83.217066-79.018667a17.066667 17.066667 0 0 1 9.489066-29.2864l113.800534-15.018666a17.066667 17.066667 0 0 0 13.175466-9.557334l49.425067-103.560533z"
                fill="#FFFFFF" p-id="4766"></path>
            </svg>
            <span>推荐</span>
          </el-menu-item>
          <el-menu-item v-for="(item,index) in dictumSpecial"
                        :index="'/dictum?code='+$base64.encode(item.id)"
                        :title="item.name" class="dictum-special-cl" :key="index">
            <span>{{ item.name }}</span>
          </el-menu-item>
        </el-menu>
        <div class="mt-10">
          <dictumList :groupId="groupId==null?null:parseInt(groupId)"
                      :content="contents"
                      :label="label"
                      :author="author"></dictumList>
        </div>
      </div>
      <!--      右边-->
      <div class="flex-4" style="min-height: 1px;">
        <div style="margin-left: 40px">
          <div :class="positionCss" style="margin-top: 40px">
            <div>
              <el-input
                placeholder="名言关键字"
                v-model="content"
                @keyup.enter.native="searchDictum"
                suffix-icon="el-icon-search">
              </el-input>
            </div>
            <hr class="hr-item mb-20 mt-10"/>
            <div class="re-name-cl border-left-3-solid">推荐专辑</div>
            <el-skeleton class="mt-10" :rows="4" animated v-if="albumLoading"/>
            <div class="overflow-hidden" v-if="!albumLoading">
              <div v-for="item of recommendedAlbumArr" class="album-tj-cl">
                <nuxt-link :to="`/details/album-info?data=`+item.id" target="_blank" rel="noopener">
                  <div class="dictum-cover-div">
                    <el-image class="dictum-cover-img" v-if="item.cover"
                              :src="item.cover" fit="cover"></el-image>
                    <el-image class="dictum-cover-img" v-else src="/img/shu.jpg" fit="cover"></el-image>
                  </div>
                  <div class="mt-10 text-underline hover-cl" style="width: 90px" :title="item.name">
                    {{ item.name }}
                  </div>
                </nuxt-link>
              </div>
            </div>
          </div>
          <div class="re-name-cl mt-20 border-left-3-solid">热门作者</div>
          <el-skeleton class="mt-10" :rows="4" animated v-if="authorLoading"/>
          <div class="overflow-hidden" v-if="!authorLoading">
            <div v-for="item of popularAuthorsArr" class="mr-10 mb-10 fl-left">
              <el-button plain size="medium" @click="selectDictum('authors',item.author)">
                {{ item.author }}&ensp;+{{ item.count }}
              </el-button>
            </div>
          </div>

          <div class="re-name-cl mt-20 border-left-3-solid">热门标签</div>
          <el-skeleton class="mt-10" :rows="4" animated v-if="authorLoading"/>
          <div class="overflow-hidden" v-if="!authorLoading">
            <div v-for="item of popularLabelArr"
                 class="mr-10 mb-10 cursor-pointer fl-left hover-fw-bold">
              <el-tag effect="plain" :type="randomType()" @click="selectDictum('label',item.label)">
                #{{ item.label }}+{{ item.count }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "index",
  head: {
    title: `名言广场 - ${process.env.PROJECT_NAME}`,
  },
  data() {
    return {
      themeColor: process.env.THEME_COLOR,
      author: null,
      label: null,
      content: null,
      contents: null,
      loading: true,
      albumLoading: true,
      authorLoading: true,
      groupId: this.$route.query.code == null ? null : this.$base64.decode(this.$route.query.code),
      dictumSpecial: [],
      recommendedAlbumArr: [],
      popularAuthorsArr: [],
      popularLabelArr: [],
      positionCss: '',
      positionType: false,
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.dataDefault();
      this.groupId = this.$route.query.code == null ? null : this.$base64.decode(this.$route.query.code);
    }
  },
  methods: {
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if (scrollTop > (clientHeight)) {
        // if (scrollTop > 10 && (scrollHeight - (scrollTop + clientHeight) <= scrollHeight / 2)) {
        this.positionType = true;
        this.positionCss = "positionCss";
      } else {
        if (this.positionType) {
          this.positionCss = "positionCssTop";
          this.positionType = false;
        }
      }
    },
    dataDefault() {
      this.author = null;
      this.label = null;
      this.contents = null;
    },
    selectDictum(type, item) {
      this.$router.push('/dictum')
      this.dataDefault();
      switch (type) {
        case "authors":
          this.author = item;
          break
        case "label":
          this.label = item;
          break
        default:
          break
      }
    },
    searchDictum() {
      this.$router.push('/dictum')
      this.dataDefault();
      this.contents = this.content;
    },
    randomType() {
      let items = ["''", "success", "info", "warning", "danger"];
      let item = items[Math.floor(Math.random() * items.length)];
      return item;
    },
    recommendedAlbums() {
      this.$API("/white/dictum/recommended/album", "get").then(res => {
        this.recommendedAlbumArr = res.data;
      }).finally(() => this.albumLoading = false);

      this.$API("/white/dictum/popular/authors", "get").then(res => {
        this.popularAuthorsArr = res.data;
        this.$API("/white/dictum/popular/label", "get").then(res => {
          this.popularLabelArr = res.data;
        })
      }).finally(() => this.authorLoading = false);
    },
    dictumGroupLists() {
      this.$API("/white/dictum/group/list", "get").then(res => {
        this.dictumSpecial = res.rows;
      }).finally(() => {
        this.loading = false;
      })
    },
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  },
  mounted() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
    this.dictumGroupLists();
    this.recommendedAlbums();
  }
}
</script>

<style scoped>
.positionCss {
  width: 380px;
  position: fixed;
  margin-top: 10px !important;
  transform: translateY(-80px);
  transition: 0.4s;
}

.positionCssTop {
  transform: translateY(-6px);
  transition: 0.6s;
}

.dictum-cover-div {
  width: 90px;
  height: 86px;
  border-radius: 4px;
  overflow: hidden;
}

.dictum-cover-img {
  width: 100%;
  height: 100%;
  transition: .4s;
}

.album-tj-cl:hover .dictum-cover-img {
  transform: scale(1.2);
}

.album-tj-cl:hover {
  border: 1px solid #dfe4ea;
}

.album-tj-cl {
  margin: 0 10px 10px 0;
  float: left;
  background-color: #fefefe;
  padding: 10px;
  font-size: 13px;
  cursor: pointer;
  text-align: center;
  border-radius: 4px;
  height: 120px;
  border: 1px solid #fefefe;
}

.re-name-cl {
  padding-left: 10px;
  font-size: 18px;
  margin-bottom: 20px;
}

.dictum-special-cl {
  cursor: pointer;
  width: 15%;
  text-align: center;
  background-color: #fefefe;
  margin: 6px;
  height: 50px;
  line-height: 50px;
  border-radius: 4px;
}
</style>
