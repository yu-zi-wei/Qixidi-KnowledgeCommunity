<template>
  <div class="module-main">
    <el-skeleton class="mt-30" :rows="16" animated v-if="loading"/>
    <div v-if="!loading" class="flex-space-between mt-30">
      <!--      类容-->
      <div class="flex-8">
        <div class="mt-10" style="padding: 15px 15px;border-radius: 4px;">
          <el-collapse accordion v-model="activeName1">
            <el-collapse-item name="1">
              <template slot="title">
                <div class="collapse-title">
                  <svg t="1742608687334" class="icon-theme-1 svg-translateY-4 mr-10" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="71871" width="20" height="20">
                    <path
                      d="M945.11 945.25c-10.18 10.88-27.29 11.47-38.26 1.29L795.81 835.51c-10.58-10.98-10.48-28.38 0.3-39.26 10.78-10.78 28.28-10.97 39.15-0.29L946.3 906.99c10.28 10.98 9.69 27.99-1.19 38.26z m-455.6-36.18c-231.66 0-419.42-187.76-419.42-419.42 0-231.66 187.76-419.52 419.52-419.52 231.66 0 419.52 187.76 419.52 419.52 0 111.23-44.19 217.92-122.9 296.62-78.71 78.6-185.49 122.8-296.72 122.8z m0-782.97c-147.03 0-279.61 88.59-335.87 224.44-56.26 135.85-25.11 292.17 78.8 396.19 104.02 104.01 260.33 135.06 396.19 78.8 135.85-56.26 224.44-188.85 224.44-335.87-0.01-200.82-162.75-363.56-363.56-363.56z m0 0"
                      p-id="71872"></path>
                  </svg>
                  <span class="font-bold font-s-16 color-grey font-style-italic">
                    推荐专辑
                    <span class="color-grey-3 ml-4 mr-4">/</span>
                    热门作者
                  <span class="color-grey-3 ml-4 mr-4">/</span>
                    热门标签
                  </span>
                </div>
              </template>
              <div class="mt-15 mb-10">
                <div class="mb-20" style="width: 600px">
                  <el-input
                    placeholder="名言关键字"
                    v-model="content"
                    @keyup.enter.native="searchDictum"
                    suffix-icon="el-icon-search">
                  </el-input>
                </div>
                <div class="font-s-16 font-bold hot-title-solid mb-10">推荐专辑</div>
                <div class="flex-left mb-20">
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
            </el-collapse-item>
          </el-collapse>
        </div>
        <el-menu
          :default-active="$route.path+($route.query.code==null?'':'?code='+$route.query.code)"
          :router="true"
          mode="horizontal"
          text-color="#2c3e50"
          :active-text-color="themeColor"
          class="el-menu-demo dictum-group-list">
          <el-menu-item index="/dictum" title="推荐" class="dictum-special-cl">
            <svg t="1740386025248" class="icon mr-2 icon-size-22 svg-translateY-1-" viewBox="0 0 1024 1024"
                 version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="6749">
              <path
                d="M545.23858719 614.39524812c35.21850281 0 52.97774812 28.13880375 53.09774344 66.83715938 0 47.69797312-17.63925 66.77716219-52.91775094 66.77716219-35.21850281 0-52.79775656-19.07918906-52.79775563-66.77716219 0-38.21837531 26.21888531-66.83715937 52.61776313-66.83715938z m177.17247-410.50255312c52.85775375 47.69797312 325.96614656 400.90296187 88.01625937 658.77200156-255.46914281 229.07026406-502.0586625 104.9955375-598.95454406 0-52.79775656-76.43675156-70.49700375-152.75350781-52.79775656-248.26944844 17.57925281-105.11553281 96.89588156-200.51147812 105.65551031-315.10660781 26.51887313 57.29756531 35.27850094 104.9955375 35.2785 162.29310281C396.44491062 347.10660781 484.58116531 184.813505 467.0019125 32c0 0 167.33288813 57.29756531 237.76989469 343.72539188 44.03812875-38.09838094 35.27850094-114.5351325 17.63925-171.83269688z m-290.56765032 582.33525l8.819625-209.991075h-43.97813062v85.85635125H308.54864656V576.23687h-43.97813062v209.991075h43.97813062v-85.91634844H387.80527813v85.91634844h44.03812874z m114.47513438 0c61.61738156 0 96.83588438-38.09838094 96.83588437-104.9955375 0-66.83715937-35.21850281-104.9955375-96.83588437-104.9955375-61.61738156 0-96.89588156 38.15837812-96.89588156 104.9955375 0 66.77716219 35.27850094 104.9955375 96.89588156 104.9955375z m220.19064187-171.83269687h61.79737407v-38.15837813h-167.39288625v38.15837813h61.61738156v171.83269687h43.97813062V614.39524812z"
                fill="#FF3B30" p-id="6750"></path>
            </svg>
            <span class="">推 荐</span>
          </el-menu-item>
          <el-menu-item v-for="(item,index) in dictumSpecial"
                        :index="'/dictum?code='+$base64.encode(item.id)"
                        :title="item.name" class="dictum-special-cl" :key="index">
            <span class="">{{ item.name }}</span>
          </el-menu-item>
        </el-menu>

        <div class="mt-40">
          <dictumList :groupId="groupId==null?null:parseInt(groupId)"
                      :content="contents"
                      :label="label"
                      :typography="1"
                      :author="author"></dictumList>
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
      activeName1: '2',
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

<style>
.dictum-group-list {
  border-radius: 25px;
  padding: 6px;
  background-color: #F3F3F3;
  //background: rgb(238, 174, 202);
  //background: radial-gradient(circle, rgba(238, 174, 202, 0.4) 0%, rgba(148, 187, 233, 0.4) 100%);
}

.hot-title-solid {
  padding-left: 6px;
  align-items: center;
  border-left: 2px solid #7f8c8d;
  //border-bottom: 1px solid #fb7299;
}

.collapse-title {
  font-size: 16px;
  padding-bottom: 12px;
  margin-bottom: 5px;
  width: 100%;
  border-bottom: 1px solid #ced6e0;
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
  margin: 0 15px 5px 0;
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
  font-size: 14px !important;
  background-color: #FFFFFF;
  margin: 4px 15px !important;
  height: 40px !important;
  line-height: 36px !important;
  border-radius: 25px;
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
