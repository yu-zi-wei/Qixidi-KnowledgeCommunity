<template>
  <div>
    <el-skeleton class="mt-10 ml-10 mr-10" :rows="6" animated v-if="userLoading"/>
    <div v-show="!userLoading" style="height: 80%">
      <!--          头部-->
      <div class="user-top-div flex-center">
        <div>
          <el-avatar :size="35" v-if="positionUserInfo.avatar" :src="positionUserInfo.avatar"></el-avatar>
          <el-avatar :size="35" v-else src="/img/tx.jpg"></el-avatar>
        </div>
        <div class="ml-6" v-if="positionUserInfo.nickname!=null">
          <p class="font-s-14 font-bold">{{ positionUserInfo.nickname }}</p>
          <p class="font-s-10 mt-4 color-grey">{{ positionUserInfo.isOnline ? '在线' : '已下线' }}</p>
        </div>
      </div>
      <div class="flex-left">
        <!--        侧边栏-->
        <div style="width: 240px">
          <div class="user-list-left">
            <div class="flex-space-between mt-6">
              <div class="ml-10 mb-6 font-s-14 font-bold">私信列表</div>
              <div class="mr-8">
                <el-dropdown trigger="click">
                  <div class="el-dropdown-link">
                    <i class="el-icon-more el-icon--right cursor-pointer"></i>
                  </div>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item @click.native="deleteAll">清空列表</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
            <hr class="hr-item"/>
            <el-menu class="el-menu-vertical-demo"
                     :default-active="$route.path+'?code='+$route.query.code"
                     :router="true">
              <el-menu-item v-for="(item,inxdex) in privateUserList"
                            :index="$route.path+'?code='+Base64Encryption(item.targetUid)" :key="inxdex">
                <div class="user-li flex-space-between">
                  <div class="flex-left align-items-center">
                    <div>
                      <el-avatar :size="30" v-if="item.targetAvatar" :src="item.targetAvatar"></el-avatar>
                      <el-avatar :size="30" v-else src="/img/tx.jpg"></el-avatar>
                      <div class="news-div-red" v-if="item.unreadCount>0">
                        <span>{{ item.unreadCount }}</span>
                      </div>
                    </div>
                    <div class="ml-4">
                      <div class="font-s-12 color-grey" :title="item.targetName">{{ item.targetName }}</div>
                      <div class="font-s-14 mt-4 news-content">
                        {{ item.lastNews }}
                      </div>
                    </div>
                  </div>
                  <div><i class="el-icon-delete-solid deleteIco" style="font-size: 14px" title="删除"
                          @click="deletePrivate(item)"></i></div>
                </div>
              </el-menu-item>
              <div v-if="privateUserList.length==0" class="color-grey-2 mt-20 font-s-14 text-center">
                暂无私信!
              </div>
            </el-menu>
          </div>
        </div>
        <div class="content-div flex-1">
          <div class="comment-li-cl" ref="mianscroll">
            <el-skeleton :rows="6" class="mr-10" animated v-if="newsLoading"/>
            <div v-show="!newsLoading">
              <div v-if="newsList.length!=0 &&isLogin">
                <div v-for="item of newsList">
                  <!--                  右边-->
                  <div v-if="item.uid==userInfo.uuid" class="flex-space-between">
                    <div style="display: flex"></div>
                    <div>
                      <div class="" style="display: flex;align-items: center;justify-content: right;">
                        <div class="mr-4">
                          <span class="color-grey-2 font-s-12">{{ $utils.reckonTime(item.createTime) }}</span>
                          <span>{{ item.nickname }}</span>
                        </div>
                        <el-avatar v-if="item.userAvatar" :src="item.userAvatar"></el-avatar>
                        <el-avatar v-else src="/img/tx.jpg"></el-avatar>
                      </div>
                      <div class="tooltip-right"> {{ item.newsComment }}</div>
                    </div>
                  </div>
                  <div v-else>
                    <div class="flex-left align-items-center">
                      <el-avatar v-if="item.userAvatar" :src="item.userAvatar"></el-avatar>
                      <el-avatar v-else src="/img/tx.jpg"></el-avatar>
                      <div class="ml-4">
                        <span>{{ item.nickname }}</span>
                        <span class="color-grey-2 font-s-12">{{ $utils.reckonTime(item.createTime) }}</span>
                      </div>
                    </div>
                    <div class="tooltip-left">{{ item.newsComment }}</div>
                  </div>
                  <br/>
                  <div class="time-mark-cl" v-if="item.timeMark==1">
                    <span class="text-center">
                    -------- {{ $utils.cutChatTime(item.createTime) }} --------
                    </span>
                  </div>
                </div>
              </div>
              <div v-else class="color-grey font-s-14 text-center">
                还没有聊天记录哟......
              </div>
            </div>
          </div>
          <hr class="hr-item mt-10"/>
          <div class="news-import">
            <emoji-module :content.sync="replyNews.newsComment" :id="'newsContent'"></emoji-module>
            <textarea style="white-space:pre-line" id="newsContent" v-model="replyNews.newsComment"
                      placeholder="请输入内容..."
                      rows="3" class="news-comment-cl">
            </textarea>
            <div class="overflow-hidden">
              <el-button plain class="fl-right" title="发送消息"
                         :disabled="replyNews.newsComment==null ||replyNews.newsComment==''"
                         :loading="buttonLoading" @click="sendMessage">发 送
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import EmojiModule from "../../components/emoji-module";

export default {
  name: "privateLetter",
  components: {EmojiModule},
  data() {
    return {
      buttonLoading: false,
      newsList: [],
      userInfo: null,
      isLogin: false,
      positionUserInfo: {
        isOnline: null,
        nickname: null,
        avatar: null,
      },
      userLoading: true,
      newsLoading: true,
      replyNews: {
        uid: null,
        newsComment: '',
        replyTargetUid: null,
      },
      privateUserList: [],
      userSocket: null,
      //私信链接
      socket: null,
    }
  },
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.newsList = [];
      this.messageList()
    }
  },
  methods: {
    deleteAll() {
      this.$confirm('确定要清空私信列表吗!', '提示', {
        confirmButtonText: '确 定',
        cancelButtonText: '取 消',
        type: 'warning'
      }).then(() => {
        this.$API("/frontDesk/private/user/all", "delete").then(res => {
          this.$modal.msgSuccess("已清空!");
          this.$router.push("/user/news/private-letter");
          //获取 私信用户
          this.$API("/frontDesk/private/user/list", "get", {uid: this.userInfo.uuid}).then(res => {
            this.privateUserList = res.rows;
          })
        });
      });
    },
    deletePrivate(item) {
      this.$confirm('确定要删除用户' + item.targetName + '吗!', '提示', {
        confirmButtonText: '确 定',
        cancelButtonText: '取 消',
        type: 'warning'
      }).then(() => {
        this.$API("/frontDesk/private/user/" + item.targetUid, "delete").then(res => {
          if (res.code == 200) {
            this.$modal.msgSuccess("删除成功!");
            this.$router.push("/news/private-letter");
            //获取 私信用户
            this.$API("/frontDesk/private/user/list", "get", {uid: this.userInfo.uuid}).then(res => {
              this.privateUserList = res.rows;
            })
          }
        })
      });
    },
    selectEmoji(emoji) {
      let input = document.getElementById("emoji")
      let startPos = input.selectionStart
      let endPos = input.selectionEnd
      let resultText = input.value.substring(0, startPos) + emoji + input.value.substring(endPos)
      input.value = resultText
      input.focus()
      input.selectionStart = startPos + emoji.length
      input.selectionEnd = startPos + emoji.length
      this.replyNews.newsComment = resultText
    },
    webSocketLink(uuid) {
      if (uuid == null) return;
      const url = process.env.WEBSOCKET_PROTOCOL + process.env.SERVER_URL + `/websocket/${uuid}/${4}`;
      this.socket = new WebSocket(url);
      this.socket.onopen = () => {
      };

      this.socket.onmessage = (event) => {
        let res = JSON.parse(event.data);
        this.privateUserList = res.rows;
      };
      this.socket.onclose = (event) => {
      };
    },
    webSocketUserLink(uuid) {
      if (uuid == null) return;
      //获取用户列表
      const url = process.env.WEBSOCKET_PROTOCOL + process.env.SERVER_URL + `/websocket/${uuid}/${3}`;
      this.userSocket = new WebSocket(url);

      this.userSocket.onopen = () => {
        //滚动条在底部
        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({top: scrollEl.scrollHeight, behavior: 'smooth'});
        });
      };

      this.userSocket.onmessage = (event) => {
        let res = JSON.parse(event.data)
        this.newsList = res.newsList;
        this.privateUserList = res.userList;
        //滚动条在底部
        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({top: scrollEl.scrollHeight, behavior: 'smooth'});
        });
      };
      this.userSocket.onclose = (event) => {
      };
    },

    Base64Encryption(id) {
      return this.$base64.encode(id)
    },
    messageList() {
      this.newsLoading = true;
      if (this.$route.query.code == null) {
        this.newsLoading = false;
        return;
      }
      //删除原链接
      if (this.userSocket != null) {
        this.userSocket.close();
      }
      this.replyNews.replyTargetUid = this.$base64.decode(this.$route.query.code);
      //建立用户链接
      this.webSocketUserLink(this.userInfo.uuid + ":" + this.replyNews.replyTargetUid);
      //消息已读
      this.$API("/frontDesk/private/newsInfo/been/read/" + this.replyNews.replyTargetUid, "get").finally(() => {
        //  刷新用户列表
        // this.$API("/frontDesk/private/user/list", "get", {uid: this.userInfo.uuid}).then(res => {
        //   this.privateUserList = res.rows;
        // })
      });
      //获取聊天数据
      this.$API("/frontDesk/private/newsInfo/list", "get", this.replyNews).then(res => {
        this.newsList = res.rows;
        // console.log("成功")
        //滚动条在底部
        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({top: scrollEl.scrollHeight, behavior: 'smooth'});
        });
      }).finally(() => {
        this.newsLoading = false
      });
      //是否在线
      this.$API("/websocket/is-online/" + this.replyNews.replyTargetUid, "get").then(res => {
        this.positionUserInfo = res.data;
      })
    },
    sendMessage() {
      if (this.replyNews.newsComment == null || this.replyNews.newsComment == "") {
        this.$modal.msgWarning("内容不能为空!");
        return;
      }
      let decode = this.$route.query.code;
      if (decode == null) {
        this.$modal.msgWarning("请选择聊天对象!");
        return;
      }
      this.buttonLoading = true;
      this.replyNews.replyTargetUid = this.$base64.decode(decode);
      this.$API("/frontDesk/private/newsInfo", "post", null, this.replyNews).then(res => {
        this.newsList.push({
          uid: this.userInfo.uuid,
          nickname: this.userInfo.nickname,
          userAvatar: this.userInfo.avatar,
          newsComment: this.replyNews.newsComment,
          createTime: this.$utils.dateTransitionStr(new Date()),
        })
        this.replyNews.newsComment = null;
      }).finally(() => {
        this.$nextTick(() => {
          let scrollEl = this.$refs.mianscroll;
          scrollEl.scrollTo({top: scrollEl.scrollHeight, behavior: 'smooth'});
        });
        this.buttonLoading = false
      })
    },
    getBasicsUsers() {
      this.$API("/front-desk/user/basics").then(res => {
        if (res != null) {
          this.userInfo = res.data;
          if (this.userInfo != null || this.userInfo.userType == "tripartite_user") {
            this.isLogin = true;
            //获取 私信用户
            this.$API("/frontDesk/private/user/list", "get", {uid: this.userInfo.uuid}).then(res => {
              this.privateUserList = res.rows;
            })
            //获取私信信息
            this.messageList();
            //建立私信页面红点链接
            this.webSocketLink(this.userInfo.uuid + ":sx");
            return;
          }
        }
      }).finally(() => this.userLoading = false)
    },
  },
  //离开页面关闭链接
  destroyed() {
    if (this.userSocket != null) {
      this.userSocket.close();
    }
    if (this.socket != null) {
      this.socket.close();
    }
  },
  mounted() {
    this.getBasicsUsers();
  }
}
</script>

<style scoped>
.time-mark-cl {
  text-align: center;
  font-size: 12px;
  color: #c0c4cc;
  margin-bottom: 20px;
}

.news-div-red {
  position: relative;
  background-color: #ff4757;
  border-radius: 10px;
  display: inline-block;
  font-size: 8px;
  height: 16px;
  width: 16px;
  color: #fefefe;
  margin-left: -12px;
  margin-bottom: 28px;
  line-height: 14px;
  text-align: center;
  white-space: nowrap;
  border: 1px solid #fefefe;
}

.news-comment-cl {
  width: 100%;
  white-space: pre-wrap;
  border: unset;
  font-size: 16px;
  padding: unset;
  margin: unset;
  resize: none;
}

textarea:focus {
  outline: none;
}

.comment-li-cl {
  height: calc(100vh - 400px);
  overflow: scroll;
  overflow-x: hidden;
  padding: 10px;
}

.el-aside::-webkit-scrollbar {
  width: 0px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 0px;
}

.comment-li-cl::-webkit-scrollbar,
.news-comment-cl::-webkit-scrollbar,
.user-list-left::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background-color: #ced6e0;
  border-radius: 4px;
}

.comment-li-cl::-webkit-scrollbar-track,
.news-comment-cl::-webkit-scrollbar-track,
.user-list-left::-webkit-scrollbar-track {
  background: #fefefe;
  border-radius: 4px;
}

.comment-li-cl::-webkit-scrollbar-thumb,
.news-comment-cl::-webkit-scrollbar-thumb,
.user-list-left::-webkit-scrollbar-thumb {
  background: #ced6e0;
  border-radius: 4px;
}

.news-import {
  /*background-color: #fefefe;*/
  padding: 10px 15px 15px 15px;
}

.el-aside {
  background-color: #fefefe;
  margin-right: 20px;
  max-height: 70vh;
  padding: 4px;
  width: 260px !important;
  border-radius: 4px;
  overflow: scroll;
  overflow-x: hidden;
}

.el-menu-item {
  line-height: initial;
  height: initial;
  border-radius: 4px;
  padding: 0px 10px !important;
  border-bottom: 1px solid #dfe6ec;
}

.user-top-div {
  width: 100%;
  background: #fefefe;
  padding: 6px 0;
  height: 40px;
  border-bottom: 1px solid #F0F2F5;
}

.user-list-left {
  background-color: #fefefe;
  height: 100%;
  padding-left: 4px;
  overflow: scroll;
  overflow-x: hidden;
}

.deleteIco {
  display: none;
  float: right;
  z-index: 99;
}

.user-li:hover .deleteIco {
  display: block;
}

.user-li {
  width: 100%;
  padding: 6px;
  height: 46px;
  cursor: pointer;
}

.content-div {
  //height: 100%;
  //background-color: #fefefe;
  //border-radius: 4px;
  //border-bottom: 1px solid #F0F2F5;
  //border-left: 1px solid #F0F2F5;
}

.news-content {
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 控制溢出部分隐藏 */
  text-overflow: ellipsis; /* 显示省略号 */
  width: 160px
}

/*-================================聊天框样式============================*/
/*----------------右边-------------------*/
.tooltip-right {
  color: #fefefe;
  font-size: 16px;
  max-width: 40ch;
  text-align: right;
  line-height: 20px;
  margin-right: 40px;
}

.tooltip-right {
  --b: 20px; /* tail dimension */
  --p: 50%; /* main position (0%:top 100%:bottom) */
  --r: 10px; /* the radius */
  padding: 8px;
  border-radius: var(--r)/var(--r) min(var(--r), var(--p) - var(--b) / 4) min(var(--r), 100% - var(--p) - var(--b) / 4) var(--r);
  background: var(--theme-color);
  position: relative;
}

.tooltip-right:before {
  content: "";
  position: absolute;
  left: 100%;
  top: clamp(var(--b) / -2, var(--p) - 3 * var(--b) / 4, 100% - var(--b));
  width: var(--b);
  aspect-ratio: 1;
  background: inherit;
  --g: #000 calc(100% - 1px), #0000;
  -webkit-mask: radial-gradient(circle closest-side at 88% 12%, var(--g)),
  radial-gradient(20% 20% at 55% 52%, var(--g)),
  radial-gradient(25% 25% at 0 75%, var(--g));
}

/*----------------左边-------------------*/
.tooltip-left {
  color: #fefefe;
  font-size: 16px;
  line-height: 20px;
  max-width: 40ch;
  text-align: left;
  margin-left: 40px;
}

.tooltip-left {
  --b: 20px; /* tail dimension */
  --p: 50%; /* main position (0%:top 100%:bottom) */
  --r: 10px; /* the radius */
  padding: 8px;
  border-radius: var(--r)/min(var(--r), var(--p) - var(--b) / 4) var(--r) var(--r) min(var(--r), 100% - var(--p) - var(--b) / 4);
  background: #27ae60;
  position: relative;
}

.tooltip-left:before {
  content: "";
  position: absolute;
  right: 100%;
  top: clamp(var(--b) / -2, var(--p) - 3 * var(--b) / 4, 100% - var(--b));
  width: var(--b);
  aspect-ratio: 1;
  background: inherit;
  --g: #000 calc(100% - 1px), #0000;
  -webkit-mask: radial-gradient(circle closest-side at 12% 12%, var(--g)),
  radial-gradient(20% 20% at 45% 52%, var(--g)),
  radial-gradient(25% 25% at 100% 75%, var(--g));
}
</style>
