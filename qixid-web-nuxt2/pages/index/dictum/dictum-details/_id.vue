<template>
  <div class="mt-30">
    <div class="dictum-details-model">
      <div class="flex-left _module_explicit-padding-lf-20">
        <div class="flex-9 background-color-fefefe">
          <div class="dictum-details-content">
            <div class="dictum-text-content" v-text="dictumInfo.content"></div>
            <div class="flex-right mt-20">
              <div
                v-if="(dictumInfo.worksName!=null && dictumInfo.worksName!='')||(dictumInfo.author!=null && dictumInfo.author!='')"
                class="font-s-15 color-fb7299">——
                <span v-if="dictumInfo.author!=null" class="cursor-pointer text-underline-hover" title="作者"
                      @click="jumpUrlBaidu('www.baidu.com',dictumInfo.author)">
                {{ dictumInfo.author }}
                </span>
                <span v-if="dictumInfo.worksName!=null && dictumInfo.worksName!=''" title="随笔出处">
                《<span class="cursor-pointer text-underline-hover"
                       @click="jumpUrlBaidu('www.baidu.com',dictumInfo.worksName)">{{ dictumInfo.worksName }}</span>》
                </span>
              </div>
            </div>
          </div>
          <!--        评论-->
          <div style="margin-top: 60px">
            <div style="padding: 0px 6px 6px 6px;border: 1px solid #e2e2e5;border-radius: 4px;">
              <emoji-module :content.sync="dictumComment.content" :id="'dictum-comment'"></emoji-module>
              <textarea style="white-space:pre-line" id="dictum-comment" v-model="dictumComment.content"
                        placeholder="输入评论..."
                        rows="3" class="dictum-comment-cl">
            </textarea>
              <div class="overflow-hidden">
                <el-button plain class="fl-right" title="评论" size="small"
                           :disabled="dictumComment.content==null ||dictumComment.content==''"
                           :loading="buttonLoading" @click="addComment(dictumInfo.id,1,
                          dictumInfo.id,dictumInfo.uid,dictumComment.content,1)">评 论
                </el-button>
              </div>
            </div>
            <div class="mt-20 font-bold-s">热门评论</div>
            <!--          评论列表-->
            <div v-if="commentListLoading && commentList.length!=0" class="mt-20">
              <div v-for="(item,index) in commentList" :key="index" class="dictum-comment-item">
                <div class="padding-10">
                  <div class="flex-left align-items-center">
                    <div>
                      <el-avatar :size="38" v-if="item.avatar" :src="item.avatar"></el-avatar>
                      <el-avatar :size="38" v-else src="/img/tx.jpg"></el-avatar>
                    </div>
                    <div class="ml-15" style="width: 100%">
                      <div>
                        <nuxt-link class="font-s-15 font-bold-s mr-10 hover-cl"
                                   :to="'/user_home/article?uuid='+$base64.encode(item.commentUid)" target="_blank">
                          {{ item.nickname }}
                        </nuxt-link>
                        <span class="font-s-15 line-height-24">{{ item.content }}</span>
                      </div>
                      <div class="font-s-14 flex-space-between mt-4">
                        <div class="color-grey-2"> {{ $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{s}') }}</div>
                        <div class="flex-right align-items-center">
                          <el-popover
                            placement="bottom"
                            width="500"
                            trigger="click">
                            <div>
                              <div style="padding: 0px 6px 6px 6px;border: 1px solid #e2e2e5;border-radius: 4px;">
                                <emoji-module :content.sync="dictumComment2.content"
                                              :id="'dictum-comment-'+item.id"></emoji-module>
                                <textarea style="white-space:pre-line" :id="'dictum-comment-'+item.id"
                                          v-model="dictumComment2.content"
                                          placeholder="输入评论..."
                                          rows="3" class="dictum-comment-cl"></textarea>
                                <div class="overflow-hidden">
                                  <el-button plain class="fl-right" title="评论" size="small"
                                             :disabled="dictumComment2.content==null ||dictumComment2.content==''"
                                             :loading="buttonLoading2" @click="addComment(item.id,2,
                         item.id,item.commentUid,dictumComment2.content,2)">回 复
                                  </el-button>
                                </div>
                              </div>
                            </div>
                            <div slot="reference" class="cursor-pointer" title="回复" v-if="userInfo!=null">
                              <svg t="1734518848455" class="icon-theme-1 icon-hover icon-size-18"
                                   viewBox="0 0 1024 1024"
                                   version="1.1"
                                   xmlns="http://www.w3.org/2000/svg" p-id="4500">
                                <path
                                  d="M512.146827 268.510985c-250.902034 0-454.505322 157.57406-454.505322 359.095851 0 77.452531 32.639121 168.259723 83.298552 213.54786C166.24668 863.780505 88.131465 990.268512 109.075322 1018.193654c23.957624 31.943141 164.908706-79.128039 205.700895-61.760749C355.568406 973.800196 441.499439 987.04638 512.146827 987.04638c250.902034 0 447.742067-158.01764 454.089667-359.439545C972.069626 442.506103 768.001278 268.510985 512.146827 268.510985zM511.997535 950.528886C447.362645 950.528886 362.014817 933.770578 323.347087 920.455656c-38.351961-13.206444-179.982913 66.085924-179.982913 66.085924s52.530415-136.615167 29.151701-161.97227c-42.140114-45.708088-76.209862-124.513914-76.209862-195.375037 0-184.372531 186.277885-324.240977 415.827925-324.240977s415.35105 154.807323 415.447714 324.240977C927.690131 821.643613 741.54865 950.528886 511.997535 950.528886zM311.488568 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0ZM504.000204 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0ZM696.511839 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0Z"
                                  p-id="4501"></path>
                              </svg>
                            </div>
                          </el-popover>
                          <svg t="1734519579902" class="icon-theme-1 icon-hover ml-4 cursor-pointer"
                               viewBox="0 0 1024 1024"
                               version="1.1"
                               xmlns="http://www.w3.org/2000/svg" p-id="5536" width="18" height="18"
                               v-if="userInfo!=null && userInfo.uuid==item.commentUid" @click="deleteComment(item.id)">
                            <path
                              d="M512 146.286a365.714 365.714 0 1 1 0 731.428 365.714 365.714 0 0 1 0-731.428z m0 62.025a303.69 303.69 0 1 0 0.073 607.451A303.69 303.69 0 0 0 512 208.311zM647.022 376.32a6.555 6.555 0 0 1 6.583 6.583 6.583 6.583 0 0 1-1.463 4.169L545.865 513.609l106.057 126.537a6.802 6.802 0 0 1 1.536 4.17 6.555 6.555 0 0 1-6.583 6.582l-53.833-0.292L512 553.984l-81.042 96.695-53.98 0.292a6.583 6.583 0 0 1-4.973-10.825l106.203-126.464-106.203-126.537a6.802 6.802 0 0 1-1.536-4.242 6.477 6.477 0 0 1 6.51-6.51l53.979 0.293L512 473.234l81.189-96.768z"
                              p-id="5537"></path>
                          </svg>
                        </div>
                      </div>
                    </div>
                  </div>
                  <!--                二级评论列表-->
                  <div v-if="item.dictumCommentVoList!=null && item.dictumCommentVoList.length!=0"
                       class="level-comment">
                    <div v-for="(item2,index2) in item.dictumCommentVoList" :key="index2" class="mb-15">
                      <div class="align-items-center">
                        <div v-if="item2.commentGrade!=3">
                          <nuxt-link class="font-s-14 font-bold-s mr-10 hover-cl"
                                     :to="'/user_home/article?uuid='+$base64.encode(item2.commentUid)" target="_blank">
                            {{ item2.nickname }}
                          </nuxt-link>
                          <span class="font-s-14 line-height-24">{{ item2.content }}</span>
                        </div>
                        <div v-else>
                          <nuxt-link class="font-s-14 font-bold-s mr-10 hover-cl"
                                     :to="'/user_home/article?uuid='+$base64.encode(item2.commentUid)" target="_blank">
                            {{ item2.nickname }}
                          </nuxt-link>
                          <span class="font-s-14">
                          <span class="mr-4 font-s-13 color-grey-2">回复</span>
                           <nuxt-link class="font-bold-s mr-10 hover-cl"
                                      :to="'/user_home/article?uuid='+$base64.encode(item2.targetUid)" target="_blank">
                             {{ item2.targetNickname }}
                           </nuxt-link>
                        </span>
                          <span class="font-s-14 line-height-24">{{ item2.content }}</span>
                        </div>
                        <div class="font-s-13 flex-space-between mt-4">
                          <div class="color-grey-3"> {{ $utils.parseTime(item2.createTime, '{y}-{m}-{d} {h}:{s}') }}
                          </div>
                          <div class="flex-right align-items-center">
                            <el-popover
                              placement="bottom"
                              width="500"
                              trigger="click">
                              <div>
                                <div style="padding: 0px 6px 6px 6px;border: 1px solid #e2e2e5;border-radius: 4px;">
                                  <emoji-module :content.sync="dictumComment3.content"
                                                :id="'dictum-comment-'+item2.id"></emoji-module>
                                  <textarea style="white-space:pre-line" :id="'dictum-comment-'+item2.id"
                                            v-model="dictumComment3.content"
                                            placeholder="输入评论..."
                                            rows="3" class="dictum-comment-cl"></textarea>
                                  <div class="overflow-hidden">
                                    <el-button plain class="fl-right" title="评论" size="small"
                                               :disabled="dictumComment3.content==null ||dictumComment3.content==''"
                                               :loading="buttonLoading3" @click="addComment(item.id,3,
                         item2.id,item2.commentUid,dictumComment3.content,2)">回 复
                                    </el-button>
                                  </div>
                                </div>
                              </div>
                              <div slot="reference" class="cursor-pointer" title="回复" v-if="userInfo!=null">
                                <svg t="1734518848455" class="icon-theme-1 icon-hover icon-size-18"
                                     viewBox="0 0 1024 1024" version="1.1"
                                     xmlns="http://www.w3.org/2000/svg" p-id="4500">
                                  <path
                                    d="M512.146827 268.510985c-250.902034 0-454.505322 157.57406-454.505322 359.095851 0 77.452531 32.639121 168.259723 83.298552 213.54786C166.24668 863.780505 88.131465 990.268512 109.075322 1018.193654c23.957624 31.943141 164.908706-79.128039 205.700895-61.760749C355.568406 973.800196 441.499439 987.04638 512.146827 987.04638c250.902034 0 447.742067-158.01764 454.089667-359.439545C972.069626 442.506103 768.001278 268.510985 512.146827 268.510985zM511.997535 950.528886C447.362645 950.528886 362.014817 933.770578 323.347087 920.455656c-38.351961-13.206444-179.982913 66.085924-179.982913 66.085924s52.530415-136.615167 29.151701-161.97227c-42.140114-45.708088-76.209862-124.513914-76.209862-195.375037 0-184.372531 186.277885-324.240977 415.827925-324.240977s415.35105 154.807323 415.447714 324.240977C927.690131 821.643613 741.54865 950.528886 511.997535 950.528886zM311.488568 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0ZM504.000204 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0ZM696.511839 622.945485m-64.394304 0a59.955 59.955 0 1 0 128.788609 0 59.955 59.955 0 1 0-128.788609 0Z"
                                    p-id="4501"></path>
                                </svg>
                              </div>
                            </el-popover>
                            <svg t="1734519579902" class="icon-theme-1 icon-hover ml-4 cursor-pointer"
                                 viewBox="0 0 1024 1024"
                                 version="1.1"
                                 xmlns="http://www.w3.org/2000/svg" p-id="5536" width="18" height="18"
                                 v-if="userInfo!=null && userInfo.uuid==item2.commentUid"
                                 @click="deleteComment(item2.id)">
                              <path
                                d="M512 146.286a365.714 365.714 0 1 1 0 731.428 365.714 365.714 0 0 1 0-731.428z m0 62.025a303.69 303.69 0 1 0 0.073 607.451A303.69 303.69 0 0 0 512 208.311zM647.022 376.32a6.555 6.555 0 0 1 6.583 6.583 6.583 6.583 0 0 1-1.463 4.169L545.865 513.609l106.057 126.537a6.802 6.802 0 0 1 1.536 4.17 6.555 6.555 0 0 1-6.583 6.582l-53.833-0.292L512 553.984l-81.042 96.695-53.98 0.292a6.583 6.583 0 0 1-4.973-10.825l106.203-126.464-106.203-126.537a6.802 6.802 0 0 1-1.536-4.242 6.477 6.477 0 0 1 6.51-6.51l53.979 0.293L512 473.234l81.189-96.768z"
                                p-id="5537"></path>
                            </svg>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div v-else class="text-center mt-20 font-s-14 color-grey-3">
              快来抢占沙发~
            </div>

          </div>
        </div>
        <div class="flex-2 _module_hiding">
          <div class="padding-10 dictum-details-right">
            <div class="dictum-re-name-cl mt-20 mb-10">作 者</div>
            <div class="flex-left">
              <div>
                <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(dictumInfo.tripartiteUser.uuid)"
                           target="_blank">
                  <el-avatar v-if="dictumInfo.tripartiteUser.avatar" :src="dictumInfo.tripartiteUser.avatar"/>
                  <el-avatar v-else src="/img/tx.jpg"></el-avatar>
                </nuxt-link>
              </div>
              <div class="ml-6 mt-2">
                <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(dictumInfo.tripartiteUser.uuid)"
                           target="_blank">
                  <p class="text-underline-hover">{{ dictumInfo.tripartiteUser.nickname }}</p>
                </nuxt-link>
                <p class="font-s-14 color-grey mt-4 line-height-18">{{ dictumInfo.tripartiteUser.occupation }}</p>
              </div>
            </div>
            <!--          作者数据-->
            <div class="flex-space-between mt-20 font-s-15 mb-20" style="padding: 0 30px">
              <div class="text-center">
                <p class="color-grey">随笔</p>
                <p class="mt-6 font-bold-s">{{ dictumInfo.tripartiteUser.dictumCount }}</p>
              </div>
              <div class="text-center">
                <p class="color-grey">专辑</p>
                <p class="mt-6 font-bold-s">{{ dictumInfo.tripartiteUser.albumCount }}</p>
              </div>
              <div class="text-center">
                <p class="color-grey">关注</p>
                <P class="mt-6 font-bold-s">{{ dictumInfo.tripartiteUser.fansFollowCount }}</P>
              </div>
            </div>
            <div class="dictum-re-name-cl mt-40 mb-6">更 多...</div>
          </div>
        </div>
      </div>
      <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
    </div>
  </div>
</template>

<script>

export default {
  name: "dictumDetails",
  head() {
    return {
      title: "随笔详情" + ' - ' + process.env.PROJECT_NAME,
      meta: [
        {
          hid: 'description',
          name: 'description',
          content: this.dictumInfo.content
        },
        {hid: 'keywords', name: 'keywords', content: this.dictumInfo.content}
      ]
    }
  },
  data() {
    return {
      dictumInfo: {
        tripartiteUser: {}
      },
      loginDialog: false,
      id: null,
      //评论相关
      dictumComment: {
        dictumId: null,
        uid: null,
        parentId: null,
        commentGrade: null,
        targetId: null,
        targetUid: null,
        content: '',
        type: null,
      },
      dictumComment2: {
        content: '',
      },
      dictumComment3: {
        content: '',
      },
      buttonLoading: false,
      buttonLoading2: false,
      buttonLoading3: false,
      commentList: [],
      commentListLoading: false,
      userInfo: {},
    }
  },
  async asyncData({app, params, store}) {
    const id = Buffer.from(params.id, 'base64').toString('utf-8');
    let token = store.state.token;
    const https = require('https');
    const response = await fetch(`${process.env.SERVICE_PROTOCOL}${process.env.SERVER_URL}/white/dictum/info/${id}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      },
      //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
      agent: new https.Agent({rejectUnauthorized: false})
    });
    const data = await response.json();
    return {
      dictumInfo: data.data,
      id: id,
    }
  },
  methods: {
    deleteComment(id) {
      if (this.userInfo == null) {
        this.loginDialog = true;
        return;
      }
      this.$confirm('确定要删除该评论吗!', '提示', {
        confirmButtonText: '确 定',
        cancelButtonText: '取 消',
        type: 'warning'
      }).then(() => {
        this.$API(`/frontDesk/dictum/comment/delete/${id}`, "delete").then(res => {
          if (res.code == 200) {
            this.$modal.msgSuccess("删除成功");
            this.getCommentList();
          }
        })
      });
    },
    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    getCommentList() {
      this.$API(`/white/dictum/comment/list/${this.dictumInfo.id}`, "get").then(res => {
        this.commentList = [];
        if (res.code == 200) {
          this.commentList = res.rows;
        }
        this.commentListLoading = true;
      })
    },
    /**
     *
     * @param parentId 父级评论id
     * @param commentGrade 评论等级（1：一级，2：二级，3：三级及以下）
     * @param targetId 目标id
     * @param targetUid 目标用户id
     * @param content 评论内容
     * @param type 评论类型（1：名言，2：评论）
     */
    addComment(parentId, commentGrade, targetId, targetUid, content, type) {
      if (this.userInfo == null) {
        this.loginDialog = true;
        return;
      }
      let dictumComment = {};
      dictumComment.dictumId = this.dictumInfo.id;
      dictumComment.uid = this.dictumInfo.uid;
      dictumComment.parentId = parentId;
      dictumComment.commentGrade = commentGrade;
      dictumComment.targetId = targetId;
      dictumComment.targetUid = targetUid;
      dictumComment.content = content;
      dictumComment.type = type;
      this.buttonLoading = true;
      this.buttonLoading2 = true;
      this.buttonLoading3 = true;
      this.$API("/frontDesk/dictum/comment/add", "post", null, dictumComment).then(res => {
        if (res.code == 200) {
          this.$modal.msgSuccess("评论成功");
          this.getCommentList();
          this.dictumComment = {};
          this.dictumComment2 = {};
          this.dictumComment3 = {};
        }
      }).finally(() => {
        this.buttonLoading = false;
        this.buttonLoading2 = false;
        this.buttonLoading3 = false;
      })
    },
    jumpUrlBaidu(url, keywords) {
      let path = window.location.protocol + '//' + url + '/s?wd=' + keywords
      window.open(path, '_blank');
    },
    getBasicsUsers() {
      this.$API('/front-desk/user/basics', this.$get()).then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
        this.getCommentList();
      });
    },
  },
  mounted() {
    this.getBasicsUsers();
  }
}
</script>
<style>
@import url("components/css/pc/dictum-comment.css");

.dictum-re-name-cl {
  padding-left: 6px;
  font-size: 16px;
  margin-bottom: 20px;
  border-left: 3px solid #FB7299;
}

.dictum-details-right {
  width: 260px;
  background-color: #FFFFFF;
  margin-left: 20px;
}

.el-button--small {
  padding: 9px 15px;
}

.dictum-details-model {
  width: 1000px;
  margin: auto;
}

.dictum-details-content {
  padding: 40px 40px 80px 40px;
  border-radius: 10px;
  border: 1px solid #e2e2e5;
}

@media (max-width: 510px) {
  .dictum-details-model {
    width: 100%;
    margin: auto;
  }

  .dictum-details-content {
    padding: 15px 15px 40px 15px;
  }

}
</style>
