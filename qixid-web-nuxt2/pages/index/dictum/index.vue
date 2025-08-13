<template>
  <div class="module-main">
    <div v-if="!loading" class="mt-30">
      <div class="font-s-15 line-height-34 mb-15 ml-10 font-style-italic">
        这是一个「思想笔记本」——不追求权威，只在乎真实与触动......
        <br/>
        为什么做这个模块？
        <br/>
        <p>
          对抗遗忘：好观点转瞬即逝，记录能让它们真正成为自己的养分； 共鸣：如果某句话也曾让你心头一动，欢迎在评论区聊聊你的版本
        </p>
      </div>
      <!--      类容-->
      <div class="_module_hiding">
        <div class="flex-space-between collapse-title align-items-center" @click="searchPage"
             title="点击弹出检索页面">
          <div>
            <svg t="1742608687334" class="icon-theme-1 svg-translateY-4 mr-10" viewBox="0 0 1024 1024"
                 version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="71871" width="20" height="20">
              <path
                d="M945.11 945.25c-10.18 10.88-27.29 11.47-38.26 1.29L795.81 835.51c-10.58-10.98-10.48-28.38 0.3-39.26 10.78-10.78 28.28-10.97 39.15-0.29L946.3 906.99c10.28 10.98 9.69 27.99-1.19 38.26z m-455.6-36.18c-231.66 0-419.42-187.76-419.42-419.42 0-231.66 187.76-419.52 419.52-419.52 231.66 0 419.52 187.76 419.52 419.52 0 111.23-44.19 217.92-122.9 296.62-78.71 78.6-185.49 122.8-296.72 122.8z m0-782.97c-147.03 0-279.61 88.59-335.87 224.44-56.26 135.85-25.11 292.17 78.8 396.19 104.02 104.01 260.33 135.06 396.19 78.8 135.85-56.26 224.44-188.85 224.44-335.87-0.01-200.82-162.75-363.56-363.56-363.56z m0 0"
                p-id="71872"></path>
            </svg>
            <span class="font-bold font-s-16 font-style-italic">
                     搜索
                    <span class="color-grey-3 ml-6 mr-6">/</span>
                    名言分类
                    <span class="color-grey-3 ml-6 mr-6">/</span>
                    推荐专辑
                    <span class="color-grey-3 ml-6 mr-6">/</span>
                    热门作者
                    <span class="color-grey-3 ml-6 mr-6">/</span>
                    热门标签
                  </span>
          </div>
          <div>
            <svg t="1744249988705" class="icon-theme-1 icon-size-20" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg"
                 p-id="2813">
              <path
                d="M511.916 34.987c-194.753 0-353.196 158.046-353.196 352.309 0 25.338 2.716 50.645 8.071 75.219 2.431 11.16 13.267 45.648 14.424 48.714 18.978 49.515 46.993 91.731 82.336 126.526l-50.121-62.033c4.143 6.517 252.836 371.253 252.836 371.253 10.070 14.644 26.733 24.123 45.61 24.123 0.015 0 0.029-0 0.044-0 0.010 0 0.025 0 0.039 0 18.878 0 35.542-9.48 45.491-23.939s263.926-389.896 265.122-392.098c19.718-35.585 33.739-77.143 39.569-121.284-23.867 88.45-9.101 47.089-5.095 28.733 5.127-22.593 8.065-48.538 8.065-75.17 0-0.016-0-0.031-0-0.047 0-194.261-158.444-352.307-353.196-352.307zM817.025 453.788c-3.533 16.189-16.574 52.718-17.601 55.128-3.903 9.161-14.973 30.454-16.075 32.379-2.21 3.865-259.631 382.622-259.631 382.622-3.824 5.614-9.525 6.222-11.801 6.222s-7.975-0.608-11.796-6.218c0 0-248.871-365.001-252.799-371.237l-0.344-0.549c-22.738-35.745-38.608-78.067-44.655-123.494 16.213 65.466 6.634 34.988 4.49 25.153-4.533-19.96-7.131-42.882-7.131-66.41 0-0.030 0-0.061 0-0.091-0.002-171.675 140.067-311.347 312.234-311.347s312.236 139.672 312.236 311.349c0 22.403-2.398 44.777-7.127 66.492z"
                p-id="2814"></path>
              <path
                d="M511.916 237.968c-39.969 0-77.55 15.526-105.814 43.717-28.283 28.205-43.858 65.712-43.858 105.611 0 10.881 1.153 21.688 3.428 32.123 3.631 16.667 10.131 32.514 19.327 47.116 11.081 17.378 25.052 31.876 41.385 43.209l-35.374-34.397c13.699 18.663 31.756 34.126 52.224 44.714 21.254 10.996 44.364 16.572 68.686 16.572 24.312 0 47.417-5.577 68.676-16.574 29.47-15.53 52.678-39.575 66.758-68.842l-14.435 24.003c11.108-14.94 19.502-32.615 24.048-51.779-8.938 28.503-2.432 12.637 1.204-4.054 2.176-9.61 3.422-20.646 3.422-31.974 0-0.042-0-0.083-0-0.125 0-39.887-15.573-77.392-43.856-105.601-28.275-28.193-65.853-43.721-105.82-43.721zM618.146 410.668c-3.955 17.708-11.857 33.164-22.756 45.994 1.231-0.061-8.305 9.854-19.463 17.595l23.882-23.158c-9.986 13.605-23.142 24.873-38.040 32.584-15.383 7.956-32.154 11.991-49.852 11.991-17.707 0-34.484-4.035-49.865-11.991-14.891-7.703-28.039-18.964-38.021-32.567l-16.511 12.118 16.388-12.284c-1.479-2.015-5.671-8.542-6.343-9.699-7.723-13.065-12.775-28.564-14.007-45.126 0.553 7.579-0.354-0.456-0.354-8.701 0-0.044 0-0.088 0-0.131-0-28.929 11.301-56.137 31.822-76.603 20.535-20.48 47.841-31.758 76.89-31.758 29.047 0 56.355 11.28 76.89 31.762 20.523 20.468 31.822 47.673 31.822 76.605 0 7.948-0.836 15.811-2.482 23.37z"
                p-id="2815"></path>
            </svg>
          </div>
        </div>
        <div class="mt-40">
          <dictumList :groupId="groupId==null?null:parseInt(groupId)"
                      :content="contents"
                      :label="label"
                      :typography="1"
                      :author="author"></dictumList>
        </div>
      </div>
      <div class="_module_explicit" style="padding: 6px 16px">
        <hr class="hr-item mb-25"/>
        <dictumList :groupId="groupId==null?null:parseInt(groupId)"
                    :content="contents"
                    :label="label"
                    :typography="2"
                    :author="author"></dictumList>
      </div>
    </div>
    <el-drawer
      size="45%"
      direction="rtl"
      :modal="false"
      :visible.sync="searchPageDrawer"
      style="padding: 20px"
      :with-header="false">
      <div style="margin: 40px">
        <div class="mt-15 mb-10">
          <div class="mb-20" style="width: 600px">
            <el-input
              placeholder="名言关键字"
              v-model="content"
              @keyup.enter.native="searchDictum"
              suffix-icon="el-icon-search">
            </el-input>
          </div>
          <div class="font-s-16 font-bold hot-title-solid mb-10">名言分类</div>
          <div class="mb-20">
            <el-menu
              :default-active="$route.path+($route.query.code==null?'':'?code='+$route.query.code)"
              :router="true"
              mode="horizontal"
              text-color="#000000"
              :active-text-color="themeColor"
              class="el-menu-demo dictum-group-list">
              <el-menu-item index="/dictum" title="全 部" class="dictum-special-cl">
                <span class="">全 部</span>
              </el-menu-item>
              <el-menu-item v-for="(item,index) in dictumSpecial"
                            :index="'/dictum?code='+$base64.encode(item.id)"
                            :title="item.name" class="dictum-special-cl" :key="index">
                <span class="">{{ item.name }}</span>
              </el-menu-item>
            </el-menu>
          </div>
          <div class="font-s-16 font-bold hot-title-solid mb-10">推荐专辑</div>
          <div class="flex-left mb-20 flex-wrap-wrap">
            <div v-for="item of recommendedAlbumArr" class="album-tj-cl">
              <nuxt-link :to="`/external_info/album-info?data=`+item.id" target="_blank" rel="noopener">
                <div class="dictum-cover-div">
                  <el-image class="dictum-cover-img" v-if="item.cover"
                            :src="item.cover" fit="cover"></el-image>
                  <el-image class="dictum-cover-img" v-else src="/img/shu.jpg" fit="cover"></el-image>
                </div>
                <div class="mt-10 hover-cl overflow-nowrap-1" style="width: 80px" :title="item.name">
                  {{ item.name }}
                </div>
              </nuxt-link>
            </div>
          </div>
          <div class="font-s-16 font-bold hot-title-solid">热门作者</div>
          <div class="mt-15 mb-20 flex-left flex-wrap-wrap">
            <div v-for="item of popularAuthorsArr" class="mr-10 mb-10">
              <el-button plain size="medium" @click="selectDictum('authors',item.author)">
                {{ item.author }}&ensp;+{{ item.count }}
              </el-button>
            </div>
          </div>
          <div class="font-s-16 font-bold hot-title-solid">热门标签</div>
          <div class="mt-15 flex-left flex-wrap-wrap mb-10">
            <div v-for="item of popularLabelArr"
                 class="mr-10 mb-10 cursor-pointer hover-fw-bold">
              <el-tag effect="plain" :type="randomType()" @click="selectDictum('label',item.label)">
                {{ item.label }}+{{ item.count }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
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
      searchPageDrawer: false,
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
    searchPage() {
      this.searchPageDrawer = true
    },
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

<style>
.dictum-group-list {
  border-radius: 2px;
  padding: 6px;
  margin-bottom: 10px;
  background: #F3F3F3;
}

.hot-title-solid {
  padding-left: 6px;
  align-items: center;
  border-left: 2px solid #7f8c8d;
  //border-bottom: 1px solid #fb7299;
}

.collapse-title {
  font-size: 16px;
  width: 100%;
  background-color: #F3F3F3;
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
  border: 1px solid #F3F3F3;
  transition: .3s;;
}

.collapse-title:hover {
  background-color: #ecf0f1;
  border: 1px solid #bdc3c7;
}

.dictum-cover-div {
  width: 80px;
  height: 76px;
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
  border: 1px solid #fb7299;
}

.album-tj-cl {
  margin: 8px;
  background-color: #FFFFFF;
  padding: 10px 10px 6px 10px;
  cursor: pointer;
  text-align: center;
  border-radius: 4px;
  border: 1px solid #dfe4ea;
}

.re-name-cl {
  padding-left: 10px;
  font-size: 18px;
  margin-bottom: 20px;
}

.dictum-special-cl {
  cursor: pointer;
  width: 100px;
  text-align: center;
  background-color: #FFFFFF;
  margin: 4px 15px !important;
  height: 40px !important;
  line-height: 40px !important;
  transition: .2s;
}

/*隐藏折叠面板 尖括号图标*/
.el-collapse-item__arrow {
  display: none;
}

.el-collapse-item__header {
  border: none !important;
  height: initial !important;
  line-height: initial !important;
  background-color: initial !important;
}

.el-collapse {
  border: none !important;
}

.el-collapse-item__wrap {
  border: none !important;
}

.el-collapse-item__content {
  padding-bottom: 0px;
}

.el-button--medium {
  padding: 10px 20px;
}
</style>
