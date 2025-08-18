<template>
  <div class="padding-10" style="height: 100%;">
    <div class="title-input-class">
      <div class="mb-10 flex-space-between flex-wrap-wrap">
        <div class="flex-6 mr-6">
          <el-input
            class="articleTitleInput"
            style="float: left;width: 100%;font-size: 16px;"
            maxlength="120"
            prefix-icon="el-icon-edit el-input__icon"
            show-word-limit
            placeholder="文章标题"
            v-model="article.articleTitle"
            clearable>
          </el-input>
        </div>
        <div class="flex-right align-items-center">
          <el-button class="mr-6" plain size="medium" :loading="buttonDraft" @click="saveDraft">草稿箱
          </el-button>
          <el-button v-if="isUpdate" class="mr-8" type="primary" size="medium" @click="articlePopup">
            更 新<i class="el-icon-refresh ml-4"></i>
          </el-button>
          <el-button v-else class="mr-8" type="primary" size="medium" @click="articlePopup">
            发 布<i class="el-icon-position ml-4"></i>
          </el-button>
          <el-dropdown trigger="click">
            <div class="el-dropdown-link mr-20 ml-5 cursor-pointer" title="最近文章">
              <svg t="1755508305247" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="39460" width="40" height="40">
                <path
                  d="M906.4 535.6c0 216.6-175.6 392.1-392.1 392.1S122.2 752.1 122.2 535.6s175.6-392.1 392.1-392.1S906.4 319 906.4 535.6"
                  fill="#E4EDFF" p-id="39461"></path>
                <path
                  d="M132.4 466.9H75.2c-6.2 0-11.2-5-11.2-11.2 0-6.2 5-11.2 11.2-11.2h57.2c6.2 0 11.2 5 11.2 11.2 0 6.2-5 11.2-11.2 11.2M948.8 466.98h-57.2c-6.2 0-11.2-5-11.2-11.2 0-6.2 5-11.2 11.2-11.2h57.2c6.2 0 11.2 5 11.2 11.2 0 6.2-5 11.2-11.2 11.2M512 165.1h-0.2c-6.2-0.1-11.1-5.2-11-11.4l0.8-57.2c0.1-6.2 4.8-11.1 11.4-11 6.2 0.1 11.1 5.2 11 11.3l-0.8 57.2c-0.1 6.2-5.1 11.1-11.2 11.1m246.1 77.6c-2.9 0-5.8-1.1-8-3.3-4.4-4.4-4.3-11.5 0.1-15.9l40.6-40.3c4.4-4.3 11.5-4.3 15.9 0.1 4.4 4.4 4.3 11.5-0.1 15.9L766 239.5c-2.2 2.1-5 3.2-7.9 3.2m-485.9 0c2.9 0 5.8-1.1 8-3.3 4.4-4.4 4.3-11.5-0.1-15.9l-40.6-40.3c-4.4-4.3-11.5-4.3-15.9 0.1-4.4 4.4-4.3 11.5 0.1 15.9l40.6 40.3c2.1 2.1 5 3.2 7.9 3.2"
                  fill="#1A192B" p-id="39462"></path>
                <path
                  d="M708.4 869.3H328.9c-37.5 0-67.9-30.1-67.9-67.2V354c0-37.1 30.4-67.2 67.9-67.2h379.5c37.5 0 67.9 30.1 67.9 67.2v448.1c0 37.1-30.4 67.2-67.9 67.2"
                  fill="#FFFFFF" p-id="39463"></path>
                <path
                  d="M332 295.3c-31.3 0-56.7 25.1-56.7 56v448.1c0 30.9 25.4 56 56.7 56h379.5c31.3 0 56.7-25.1 56.7-56V351.3c0-30.9-25.5-56-56.7-56H332z m379.5 582.6H332c-43.6 0-79.1-35.2-79.1-78.4V351.3c0-43.2 35.5-78.4 79.1-78.4h379.5c43.6 0 79.1 35.2 79.1 78.4v448.1c0 43.3-35.5 78.5-79.1 78.5z"
                  fill="#1A192B" p-id="39464"></path>
                <path
                  d="M698.4 397.4H339.1c-6.2 0-11.2-5-11.2-11.2s5-11.2 11.2-11.2h359.3c6.2 0 11.2 5 11.2 11.2s-5.1 11.2-11.2 11.2M559.1 528.6h-220c-6.2 0-11.2-5-11.2-11.2 0-6.2 5-11.2 11.2-11.2h220.1c6.2 0 11.2 5 11.2 11.2-0.1 6.2-5.1 11.2-11.3 11.2M435.9 660.2h-96.8c-6.2 0-11.2-5-11.2-11.2 0-6.2 5-11.2 11.2-11.2H436c6.2 0 11.2 5 11.2 11.2-0.1 6.2-5.1 11.2-11.3 11.2"
                  fill="#1A192B" p-id="39465"></path>
                <path
                  d="M583.4 743.9l258-258c10.5-10.5 10.5-27.8 0-38.3l-45.7-45.7c-10.5-10.5-27.8-10.5-38.3 0L501 658.2c-5.9 5.9-10.6 13-13.7 20.8l-37.6 94.6c-5 12.7 7.5 25.2 20.1 20.3l92.3-36.1c8.1-3.2 15.3-7.9 21.3-13.9"
                  fill="#FFC000" p-id="39466"></path>
                <path
                  d="M777.3 408.5c-4.1 0-8.1 1.6-11.2 4.6L509.8 669.4c-4.9 4.9-8.6 10.6-11.2 17L461.1 781c-0.9 2.3 0.2 3.9 1 4.7 0.8 0.8 2.4 1.9 4.7 1l92.3-36.1c6.5-2.5 12.3-6.3 17.3-11.3l258-258c6.2-6.2 6.2-16.3 0-22.5l-45.7-45.7c-3.2-3-7.3-4.6-11.4-4.6M465 809.5c-6.9 0-13.6-2.7-18.8-7.9-7.6-7.7-9.9-18.7-5.9-28.8l37.6-94.6c3.7-9.3 9.1-17.5 16.2-24.6l256.2-256.2c14.9-14.9 39.2-14.9 54.2 0l45.7 45.7c14.9 14.9 14.9 39.2 0 54.2l-258 258c-7.1 7.1-15.5 12.6-24.9 16.3L475 807.7c-3.3 1.1-6.7 1.8-10 1.8"
                  fill="#1A192B" p-id="39467"></path>
                <path d="M566.9 765l-83.8-83.8 15.8-15.9 83.9 83.9z m222.8-222.8l-83.8-83.8 15.8-15.9 83.9 83.9z"
                      fill="#1A192B" p-id="39468"></path>
              </svg>

            </div>
            <el-dropdown-menu slot="dropdown">
              <div class="lately-article-cl">
                <el-tabs v-model="activeName" @tab-click="handleTabsClick">
                  <el-tab-pane label="全部" name="-1"></el-tab-pane>
                  <el-tab-pane label="草稿" name="4"></el-tab-pane>
                  <el-tab-pane label="审核通过" name="2"></el-tab-pane>
                  <el-tab-pane label="审核中" name="1"></el-tab-pane>
                  <el-tab-pane label="审核未通过" name="3"></el-tab-pane>
                  <div v-for="(item,index) in latelyArticleList" @click="handleCommand(item.id)"
                       class="lately-article-item font-s-14 line-height-24 flex-left align-items-center" :key="index"
                       title="点击文章快速编辑">
                    <div style="width: 15px">
                      <div :style="'background-color:'+auditStateDotColor(item.auditState)"
                           class="aurora-dot mr-6"></div>
                    </div>
                    <div>{{ item.articleTitle }}</div>
                  </div>
                </el-tabs>
              </div>
            </el-dropdown-menu>
          </el-dropdown>
          <div v-if="userInfo!=null">
            <el-avatar v-if="userInfo.avatar" :src="userInfo.avatar"></el-avatar>
            <el-avatar v-else src="/img/tx.jpg"></el-avatar>
          </div>
        </div>
      </div>
    </div>
    <div>
      <VditorMd :height="'90vh'"
                :vditorId="'articlePublish'"
                :mdContent.sync="article.articleContent"
                :mode="'wysiwyg'"
                :content="article.articleContent" v-if="isClient"></VditorMd>
    </div>
    <el-dialog title="文章发布" :visible.sync="articlePopupDialog" width="800px">
      <el-form :model="article" :rules="rules" ref="article" label-width="100px" class="demo-ruleForm">
        <el-form-item label="分类：" prop="groupingId">
          <el-radio-group v-model="article.groupingId" size="small" :border="false">
            <el-radio-button border v-for="(item,index) in labelGroupList"
                             :key="index" :label="item.id" class="el-radio-button-border">
              {{ item.groupingName }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label=" 文章封面：">
          <div class="articleCoverCa">
            <imageUpload v-model="value" :limit="1"/>
          </div>
        </el-form-item>
        <el-row class="mb-8">
          <el-col :span="14">
            <el-form-item label="添加标签：" prop="labelLongList">
              <el-select style="width:100%" v-model="article.labelLongList" placeholder="关键字搜索" filterable multiple
                         :multiple-limit=3>
                <el-option
                  v-for="item in labelList"
                  :key="item.id"
                  :label="item.labelName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="收录专栏：">
              <el-select v-model="article.specialId" filterable placeholder="关键字搜索">
                <el-option
                  v-for="item in specialList"
                  :key="item.id"
                  :label="item.specialName"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="文章摘要：" prop="articleAbstract">
          <el-switch
            v-model="article.abstractSelect"
            active-text="AI生成"
            inactive-text="手动填写">
          </el-switch>
          <div v-if="!article.abstractSelect">
            <div class="border-all-1-DCDFE6 border-radius-4">
              <el-input type="textarea"
                        :rows="6"
                        placeholder="文章摘要 ..."
                        maxlength="400"
                        show-word-limit
                        v-model="article.articleAbstract">
              </el-input>
            </div>
          </div>
        </el-form-item>

        <el-form-item label="文章类型：" prop="type">
          <el-radio-group v-model="article.type">
            <el-radio :label=1>原创</el-radio>
            <el-radio :label=2>转载</el-radio>
            <el-radio :label=3>翻译</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="转载地址：" v-if="article.type==2" class="demo-input-suffix">
          <el-input style="width: 75%"
                    v-model="article.reprintUrl"
                    maxlength="200"
                    show-word-limit
                    :clearable="true"
                    placeholder="转载地址">
            <template slot="prepend">Http://</template>
          </el-input>
        </el-form-item>
        <el-form-item label="翻译地址：" v-if="article.type==3">
          <el-input style="width: 75%"
                    v-model="article.reprintUrl"
                    maxlength="40"
                    :clearable="true"
                    show-word-limit placeholder="翻译地址">
            <template slot="prepend">Http://</template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div class="fl-right">
            <el-button @click="articlePopupDialog = false">关 闭</el-button>
            <el-button type="primary" :loading="buttonLoading" @click="articlePopupConfirm()">发 布</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import VditorMd from "../../components/vditorComponents/Vditor-md.vue";

export default {
  name: "publishArticle",
  head: {
    title: `文章编写 - ${process.env.PROJECT_NAME}`,
  },
  components: {VditorMd},
  data() {
    return {
      isClient: false,
      rules: {
        labelLongList: [
          {required: true, message: '请选择文章标签', trigger: 'change'}
        ],
        groupingId: [
          {required: true, message: '请选择文章类别', trigger: 'change'}
        ],
        type: [
          {required: true, message: '请选择文章类型', trigger: 'change'}
        ]
      },
      value: [],
      labelGroupList: [],
      labelList: [],
      specialList: [],
      latelyArticleList: [],
      activeName: "-1",
      article: {
        articleTitle: null,//标题
        articleCover: null,//封面
        articleAbstract: null,//摘要
        articleContent: '',//内容
        articleSummary: '',//总结
        theme: null,//主题
        type: 1,//文章类型（1：原创，2：转载，3：翻译）
        reprintUrl: null,//转载地址
        sspecialId: null,//专栏id
        groupingId: null,//分类
        labelId: null,//标签（多个以逗号隔开）
        isPublic: 1,//是否公开（1：公开，2：不公开）
        classification: null,
        auditState: 4,//（1：审核中，2：审核通过，3：审核不通过,4:草稿）
        labelLongList: [],
        abstractSelect: true,//ai生成摘要
      },
      buttonLoading: false,
      buttonDraft: false,
      articlePopupDialog: false,
      userInfo: null,
      isUpdate: false,//是否更新
      latelyArticleQueryParams: {
        pageNum: 1,
        pageSize: 10,
        auditState: -1,
      },
      scrollTops: 0,
    }
  },
  methods: {
    handleTabsClick(tab, event) {
      this.latelyArticleQueryParams.auditState = parseInt(tab.name)
      this.getLatelyArticleList()
    },
    auditStateDotColor(auditState) {
      switch (auditState) {
        case 1://审核中
          return "#ffa502"
        case 2://审核通过
          return "#2ecc71"
        case 3://审核不通过
          return "#e74c3c"
        case 4://草稿
          return "#bdc3c7"
      }
    },
    handleCommand(id) {
      this.$router.push({
        path: '/article/publish-article',
        query: {id: this.$base64.encode(id)}
      })
      this.loadArticleInfo(this.$base64.encode(id))
    },
    articlePopupConfirm() {
      if (!this.article.abstractSelect && (this.article.articleAbstract === null || this.article.articleAbstract.trim().length === 0)) {
        this.$modal.msg("请填写文章摘要！");
        return;
      }

      this.$refs["article"].validate(valid => {
        if (valid) {
          if (this.value.length > 0) {
            this.article.articleCover = this.value[0].url;
          }
          this.buttonLoading = true;
          this.article.labelId = this.article.labelLongList.toString();
          this.article.auditState = 2;
          this.$API("/user/insert/article", this.$post(), null, this.article).then(res => {
            if (res.code === 200) {
              this.$modal.notifySuccess("发布成功");
              this.articlePopupDialog = false;
              this.$router.push({
                path: '/article/article-success',
                query: {id: this.$base64.encode(res.data.id)}
              })
            }
          }).finally(() => this.buttonLoading = false)
        }
      });
    },
    saveDraft() {
      if (this.article.articleTitle === null || this.article.articleTitle === '') {
        this.$modal.msg("请填写标题！");
        return;
      }
      if (this.article.articleContent == null || this.article.articleContent === '') {
        this.$modal.msgWarning("请填写内容！");
        return;
      }
      this.buttonDraft = true;
      this.article.auditState = 4;
      if (this.value.length > 0) {
        this.article.articleCover = this.value[0].url;
      }
      this.$API("/user/save/draft", this.$put(), null, this.article).then(res => {
        if (res.code === 200) {
          this.article.id = res.data.id;
          this.$modal.notifySuccess("保存成功");
          this.$router.push({
            path: '/article/article-success',
            query: {id: this.$base64.encode(res.data.id)}
          })
        }
      }).finally(() => this.buttonDraft = false)
    },

    articlePopup() {
      if (this.article.articleTitle === null || this.article.articleTitle.trim().length === 0) {
        this.$modal.msg("请填写标题！");
        return;
      }
      if (this.article.articleContent === null || this.article.articleContent.trim().length === 0) {
        this.$modal.msg("请填写内容！");
        return;
      }
      this.articlePopupDialog = true;
    },
    getLatelyArticleList() {
      this.$API("/user/lately/article/list", this.$get(), this.latelyArticleQueryParams).then(response => {
        this.latelyArticleList = response.data;
      });
    },
    loadArticleInfo(id) {
      this.isClient = false;
      if (id != null && id != "") {
        this.$API("/user/get/article/" + this.$base64.decode(id), this.$get()).then(res => {
          this.article = res.data;
          this.value = this.article.articleCover == null ? [] : [{url: this.article.articleCover,}];
          this.isClient = true;
        })
        this.isUpdate = true;
      } else {
        this.isUpdate = false;
        this.isClient = true;
      }
    },
    loadBasicData() {
      let id = this.$route.query.id;
      this.loadArticleInfo(id);
      this.$API("/front-desk/user/basics", this.$get()).then(res => {
        this.userInfo = res.data;
      })
      this.$API("/business/groupingInfo/list", this.$get()).then(response => {
        this.labelGroupList = response.rows;
      });
      this.$API("/business/label-info/list", this.$get()).then(response => {
        this.labelList = response.rows;
      });
      this.$API("/white/aut/special/list", this.$get()).then(response => {
        this.specialList = response.data;
      });
      this.getLatelyArticleList();
    },
  },
  mounted() {
    this.loadBasicData();
    window.addEventListener('scroll', this.highlightType, true);// 向页面添加滚动事件
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.highlightType, true)
  },
}
</script>

<style>
.el-radio-button__inner {
  border: 0 !important;
  box-shadow: none !important;
}

.el-radio-button:first-child .el-radio-button__inner {
  border: 0 !important;
  box-shadow: none !important;
}

.el-radio-button-border {
  margin: 0 10px 10px 0;
  border: 1px solid #95a5a6;
}

.lately-article-cl {
  padding: 15px 10px;
  max-height: 400px;
  overflow-y: auto;
  width: 300px;
}

.lately-article-item {
  padding: 8px;
  background-color: #f1f2f6;
  border-radius: 4px;
  margin: 10px 0;
  cursor: pointer;
}

.lately-article-item :hover {
  color: #ff7f50;
}

.publish-article-catalogue {
  overflow: auto;
  max-height: 600px;
  padding: 6px;
  width: 300px;
}

.publish-article-catalogue::-webkit-scrollbar {
  width: 4px;
  height: 4px;
  background-color: #FFFFFF;
}

.publish-article-catalogue::-webkit-scrollbar-track {
  border-radius: 2px;
}

.publish-article-catalogue::-webkit-scrollbar-thumb {
  border-radius: 2px;
}

.title-input-class {
  width: 100%;
  background-color: #FFFFFF;
  z-index: 99;
}

.el-timeline-item__wrapper {
  padding-left: 24px;
}

.el-timeline-item__wrapper :hover {
  color: var(--theme-color);
  cursor: pointer;
}

.el-button--medium {
  padding: 10px 20px;
}

.el-button {
  padding: 12px 20px;
}
</style>
