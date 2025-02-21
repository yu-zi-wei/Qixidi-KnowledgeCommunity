<template>
  <div class="padding-10" style="height: 100%;">
    <div style="height: 60px;">
      <div class="title-input-class">
        <div class="mb-10 flex-space-between flex-wrap-wrap padding-10">
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
              <div class="el-dropdown-link" title="最近文章">
                <svg t="1729826374304" class="icon icon-theme icon-hover-theme-stand-out ml-5 mr-12 cursor-pointer"
                     viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                     p-id="13656" width="32" height="32">
                  <path
                    d="M172.032 704.512c-40.96 0-73.728 32.768-73.728 73.728s32.768 73.728 73.728 73.728S249.856 819.2 249.856 778.24 212.992 704.512 172.032 704.512zM172.032 815.104c-20.48 0-36.864-16.384-36.864-36.864s16.384-36.864 36.864-36.864 36.864 16.384 36.864 36.864S192.512 815.104 172.032 815.104zM172.032 176.128c-40.96 0-73.728 32.768-73.728 73.728S131.072 327.68 172.032 327.68s73.728-32.768 73.728-73.728S212.992 176.128 172.032 176.128zM172.032 286.72C151.552 286.72 135.168 270.336 135.168 249.856c0-20.48 16.384-36.864 36.864-36.864s36.864 16.384 36.864 36.864C208.896 270.336 192.512 286.72 172.032 286.72zM851.968 438.272l-450.56 0c-40.96 0-73.728 32.768-73.728 73.728 0 40.96 32.768 73.728 73.728 73.728l450.56 0c40.96 0 73.728-32.768 73.728-73.728C925.696 471.04 892.928 438.272 851.968 438.272zM851.968 552.96l-450.56 0c-20.48 0-36.864-16.384-36.864-36.864 0-20.48 16.384-36.864 36.864-36.864l450.56 0c20.48 0 36.864 16.384 36.864 36.864C888.832 536.576 872.448 552.96 851.968 552.96zM851.968 704.512l-450.56 0c-40.96 0-73.728 32.768-73.728 73.728s32.768 73.728 73.728 73.728l450.56 0c40.96 0 73.728-32.768 73.728-73.728S892.928 704.512 851.968 704.512zM851.968 815.104l-450.56 0c-20.48 0-36.864-16.384-36.864-36.864 0-20.48 16.384-36.864 36.864-36.864l450.56 0c20.48 0 36.864 16.384 36.864 36.864C888.832 798.72 872.448 815.104 851.968 815.104zM397.312 327.68l450.56 0c40.96 0 73.728-32.768 73.728-73.728s-32.768-73.728-73.728-73.728l-450.56 0c-40.96 0-73.728 32.768-73.728 73.728S356.352 327.68 397.312 327.68zM397.312 212.992l450.56 0c20.48 0 36.864 16.384 36.864 36.864 0 20.48-16.384 36.864-36.864 36.864l-450.56 0C376.832 286.72 360.448 270.336 360.448 249.856 360.448 229.376 376.832 212.992 397.312 212.992zM172.032 438.272c-40.96 0-73.728 32.768-73.728 73.728s32.768 73.728 73.728 73.728 73.728-32.768 73.728-73.728S212.992 438.272 172.032 438.272zM172.032 552.96c-20.48 0-36.864-16.384-36.864-36.864 0-20.48 16.384-36.864 36.864-36.864s36.864 16.384 36.864 36.864C208.896 536.576 192.512 552.96 172.032 552.96z"
                    p-id="13657"></path>
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
    </div>
    <div>
      <mavon-editor-module :mdContent.sync="article.articleContentMd"
                           :htmlContent.sync="article.articleContent"
                           :theme.sync="article.theme"
                           :content="article.articleContentMd"
                           v-if="isClient"></mavon-editor-module>
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
          <el-input type="textarea"
                    :rows="6"
                    placeholder="文章摘要 ..."
                    maxlength="400"
                    show-word-limit
                    v-model="article.articleAbstract">
          </el-input>
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
import AiEditorModule from "../../components/AiEditor-module";
import MavonEditorModule from "../../components/mavon-editor-module.vue";

export default {
  name: "publishArticle",
  components: {MavonEditorModule, AiEditorModule},
  data() {
    return {
      isClient: false,
      rules: {
        specialId: [
          {required: true, message: '文章专栏', trigger: 'change'}
        ],
        labelLongList: [
          {required: true, message: '请添加标签', trigger: 'change'}
        ],
        groupingId: [
          {required: true, message: '请选择分类', trigger: 'change'}
        ],
        articleAbstract: [
          {required: true, message: '请填写文章摘要', trigger: 'change'}
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
        articleContentMd: '',//内容
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
      //自动生成文章摘要
      if (this.article.articleAbstract == null || this.article.articleAbstract == '') {
        let articleContent = this.filtersHtmlText(this.article.articleContent);
        this.article.articleAbstract = articleContent.replace(/\s*/g, "").substring(0, 120)
      }
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
      if (this.article.articleTitle === null || this.article.articleTitle === '') {
        this.$modal.msg("请填写标题！");
        return;
      }
      if (this.article.articleContent === null || this.article.articleContent === '') {
        this.$modal.msg("请填写内容！");
        return;
      }
      if (this.article.articleAbstract == null || this.article.articleAbstract == '') {
        let articleContent = this.filtersHtmlText(this.article.articleContent);
        this.article.articleAbstract = articleContent.replace(/\s*/g, "").substring(0, 120)
      }
      this.articlePopupDialog = true;
    },
    //html解析为纯文本
    filtersHtmlText(html) {
      return html.replace(/<(style|script|iframe)[^>]*?>[\s\S]+?<\/\1\s*>/gi, '').replace(/<[^>]+?>/g, '').replace(/\s+/g, ' ').replace(/ /g, ' ').replace(/>/g, ' ');
    },
    // md 解析为纯文本
    filtersMdText(md) {
      if (md != null && md != '') {
        let reg = /[\u4e00-\u9fa5]|[\u3002|\uff1f|\uff01|\uff0c|\u3001|\uff1b|\uff1a|\u201c|\u201d|\u2018|\u2019|\uff08|\uff09|\u300a|\u300b|\u3008|\u3009|\u3010|\u3011|\u300e|\u300f|\u300c|\u300d|\ufe43|\ufe44|\u3014|\u3015|\u2026|\u2014|\uff5e|\ufe4f|\uffe5]/g;
        let names = md.match(reg);
        md = names ? names.join('') : '';
        return md;
      } else return '';
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
  background-color: #fefefe;
}

.publish-article-catalogue::-webkit-scrollbar-track {
  border-radius: 2px;
}

.publish-article-catalogue::-webkit-scrollbar-thumb {
  border-radius: 2px;
}

.title-input-class {
  position: fixed;
  width: 100%;
  top: 0;
  left: 0;
  right: 0;
  background-color: #fefefe;
  height: 65px;
  z-index: 99;
}

.el-timeline-item__wrapper {
  padding-left: 24px;
}

.el-timeline-item__wrapper :hover {
  color: var(--theme-color);
  cursor: pointer;
}
</style>
