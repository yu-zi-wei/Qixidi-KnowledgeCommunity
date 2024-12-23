<template>
  <div>
    <el-dialog
      :title="loginTitle"
      :modal-append-to-body="false"
      :destroy-on-close="true"
      :visible.sync="isLogin"
      @close="loginClose()"
      :before-close="handleClose"
      width="400px">
      <!--      登录-->
      <div v-show="loginDialogType==1">
        <el-form :model="form" :rules="registerRules" ref="form" class="demo-ruleForm">
          <el-form-item prop="username">
            <el-input @keyup.enter.native="LoginButton('form')" v-model="form.username" autocomplete="off"
                      placeholder="手机号/邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item style="text-align: center;font-weight: bold;font-size: 20px" prop="password">
            <el-input @keyup.enter.native="LoginButton('form')" v-model="form.password" autocomplete="off"
                      placeholder="密码"
                      show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="LoginButton('form')">登 录
            </el-button>
          </el-form-item>
        </el-form>

        <div class="overflow-hidden ml-4 mr-4" style="height: 40px;line-height: 40px">
          <span class="fl-left cursor-pointer hover-color" @click="resetPasswordButton">忘记密码</span>
          <el-button type="text" class="fl-right" @click="registerDialog">前往注册
          </el-button>
        </div>
        <!--        第三方登录-->
        <div>
          <el-divider></el-divider>
          <div class="flex-left align-items-center">
            <div class="mr-15 font-bold">第三方登录</div>
            <div class="text-center flex-left">
              <div @click="login('gitee')" title="Gitee" class="cursor-pointer">
                <svg t="1659862936090" class="icon svg-translateY-3" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg"
                     p-id="736" width="21" height="21">
                  <path
                    d="M512 1024C229.222 1024 0 794.778 0 512S229.222 0 512 0s512 229.222 512 512-229.222 512-512 512z m259.149-568.883h-290.74a25.293 25.293 0 0 0-25.292 25.293l-0.026 63.206c0 13.952 11.315 25.293 25.267 25.293h177.024c13.978 0 25.293 11.315 25.293 25.267v12.646a75.853 75.853 0 0 1-75.853 75.853h-240.23a25.293 25.293 0 0 1-25.267-25.293V417.203a75.853 75.853 0 0 1 75.827-75.853h353.946a25.293 25.293 0 0 0 25.267-25.292l0.077-63.207a25.293 25.293 0 0 0-25.268-25.293H417.152a189.62 189.62 0 0 0-189.62 189.645V771.15c0 13.977 11.316 25.293 25.294 25.293h372.94a170.65 170.65 0 0 0 170.65-170.65V480.384a25.293 25.293 0 0 0-25.293-25.267z"
                    fill="#C71D23" p-id="737"></path>
                </svg>
              </div>
              <div @click="login('weibo')" title="微博" class="ml-15 cursor-pointer">
                <svg t="1664334516217" class="icon svg-translateY-1" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="6161" width="24" height="24">
                  <path
                    d="M851.4 590.193c-22.196-66.233-90.385-90.422-105.912-91.863-15.523-1.442-29.593-9.94-19.295-27.505 10.302-17.566 29.304-68.684-7.248-104.681-36.564-36.14-116.512-22.462-173.094 0.866-56.434 23.327-53.39 7.055-51.65-8.925 1.89-16.848 32.355-111.02-60.791-122.395C311.395 220.86 154.85 370.754 99.572 457.15 16 587.607 29.208 675.873 29.208 675.873h0.58c10.009 121.819 190.787 218.869 412.328 218.869 190.5 0 350.961-71.853 398.402-169.478 0 0 0.143-0.433 0.575-1.156 4.938-10.506 8.71-21.168 11.035-32.254 6.668-26.205 11.755-64.215-0.728-101.66z m-436.7 251.27c-157.71 0-285.674-84.095-285.674-187.768 0-103.671 127.82-187.76 285.674-187.76 157.705 0 285.673 84.089 285.673 187.76 0 103.815-127.968 187.768-285.673 187.768z"
                    fill="#E71F19" p-id="6162"></path>
                  <path
                    d="M803.096 425.327c2.896 1.298 5.945 1.869 8.994 1.869 8.993 0 17.7-5.328 21.323-14.112 5.95-13.964 8.993-28.793 8.993-44.205 0-62.488-51.208-113.321-114.181-113.321-15.379 0-30.32 3.022-44.396 8.926-11.755 4.896-17.263 18.432-12.335 30.24 4.933 11.662 18.572 17.134 30.465 12.238 8.419-3.46 17.268-5.33 26.41-5.33 37.431 0 67.752 30.241 67.752 67.247 0 9.068-1.735 17.857-5.369 26.202a22.832 22.832 0 0 0 12.335 30.236l0.01 0.01z"
                    fill="#F5AA15" p-id="6163"></path>
                  <path
                    d="M726.922 114.157c-25.969 0-51.65 3.744-76.315 10.942-18.423 5.472-28.868 24.622-23.5 42.91 5.509 18.29 24.804 28.657 43.237 23.329a201.888 201.888 0 0 1 56.578-8.064c109.253 0 198.189 88.271 198.189 196.696 0 19.436-2.905 38.729-8.419 57.16-5.508 18.289 4.79 37.588 23.212 43.053 3.342 1.014 6.817 1.442 10.159 1.442 14.943 0 28.725-9.648 33.37-24.48 7.547-24.906 11.462-50.826 11.462-77.175-0.143-146.588-120.278-265.813-267.973-265.813z"
                    fill="#F5AA15" p-id="6164"></path>
                  <path
                    d="M388.294 534.47c-84.151 0-152.34 59.178-152.34 132.334 0 73.141 68.189 132.328 152.34 132.328 84.148 0 152.337-59.182 152.337-132.328 0-73.15-68.19-132.334-152.337-132.334zM338.53 752.763c-29.454 0-53.39-23.755-53.39-52.987 0-29.228 23.941-52.989 53.39-52.989 29.453 0 53.39 23.76 53.39 52.989 0 29.227-23.937 52.987-53.39 52.987z m99.82-95.465c-6.382 11.086-19.296 15.696-28.726 10.219-9.43-5.323-11.75-18.717-5.37-29.803 6.386-11.09 19.297-15.7 28.725-10.224 9.43 5.472 11.755 18.864 5.37 29.808z"
                    fill="#040000" p-id="6165"></path>
                </svg>
              </div>
<!--              <div @click="login('baidu')" title="百度" class="ml-15 cursor-pointer">-->
<!--                <svg t="1732527806620" class="icon svg-translateY-3" viewBox="0 0 1024 1024" version="1.1"-->
<!--                     xmlns="http://www.w3.org/2000/svg" p-id="7308" width="22" height="22">-->
<!--                  <path-->
<!--                    d="M609.20746667 688.85333333h-44.78933334s-5.74506667-3.55413333-5.74506666-13.5712v-93.96266666l-50.51413334 0.64426666v104.53546667s4.86826667 27.62133333 41.48906667 27.62133333h84.82773333V587.8048h-25.26933333V688.85333333z m-151.584-101.04853333h-47.376s-43.728-1.73013333-59.05066667 47.85813333c-5.30666667 33.0912 4.7552 45.7152 6.5088 49.89013334 1.776 4.13013333 15.8976 28.56853333 51.36533333 28.56853333h73.81866667V524.04586667l-25.2672-0.55466667v64.3136z m0 101.04853333h-37.35893334s-23.008-0.64746667-30.09173333-27.13813333c-3.55413333-11.8368 0.55466667-23.14666667 2.4-28.4736 1.73013333-5.30986667 9.43786667-20.17066667 25.35893333-20.17066667h39.69173334V688.85333333z m53.85813333-651.84853333C249.15413333 37.0048 36.50986667 249.67253333 36.50986667 512s212.64533333 474.9952 474.97173333 474.9952S986.45226667 774.32746667 986.45226667 512 773.80906667 37.0048 511.4816 37.0048z m28.96106667 252.08106667c3.78346667-34.544 45.09013333-87.54986667 78.2048-79.9808 32.99733333 7.5232 63.11253333 51.32053333 56.99733333 88.98133333-5.95413333 37.728-35.92853333 87.504-82.56746667 81.41226667-46.5664-5.95413333-57.11253333-48.13653333-52.63466666-90.4128z m-97.68-95.072c34.89066667 0 63.11146667 40.17493333 63.11146666 89.76533333 0 49.63626667-28.2208 89.81226667-63.11146666 89.81226667-34.8448 0-63.11253333-40.17493333-63.11253334-89.81226667 0-49.58933333 28.26666667-89.76533333 63.11253334-89.76533333zM245.92426667 406.704s7.45493333-73.91146667 58.58986666-78.48213333c40.63573333-3.48266667 70.544 40.98346667 73.65866667 66.41386666 1.91573333 16.49813333 10.49813333 92.00213333-52.68266667 105.59466667-63.0432 13.54666667-86.4864-59.39626667-79.56586666-93.5264z m480.9472 318.28693333c-34.56746667 81.456-160.88533333 39.136-160.88533334 39.136s-46.5888-14.9536-100.63253333-2.97813333c-54.0672 12.09386667-100.6816 7.49866667-100.6816 7.49866667s-63.2288 1.52533333-81.2512-78.36373334c-17.99893333-79.98186667 63.11253333-123.7792 69.184-131.232 5.97546667-7.56906667 48.08746667-36.20586667 75.088-81.43573333 27.136-45.27466667 108.272-81.48053333 165.38453333 7.47733333 42.1152 60.36586667 114.27306667 116.21013333 114.27306667 116.21013334s54.04373333 42.2528 19.52 123.68746666z m-10.54506667-185.57866666c-69.20426667 1.57226667-72.1344-46.75093333-72.1344-81.36426667 0-36.27626667 7.4528-87.57333333 63.15946667-87.57333333 55.6128 0 70.5888 54.3424 70.5888 72.38933333 0 18.16213333 7.59146667 95.0272-61.61386667 96.54826667z"-->
<!--                    fill="#00a8ff" p-id="7309" data-spm-anchor-id="a313x.search_index.0.i3.7d613a81FWmXFc"-->
<!--                    class="selected"></path>-->
<!--                </svg>-->
<!--              </div>-->

            </div>
          </div>
        </div>
      </div>
      <!--      注册-->
      <div v-show="loginDialogType==2">
        <el-form :model="registerFrom" :rules="registerRules" ref="registerFrom" class="demo-ruleForm">
          <el-form-item prop="phone">
            <el-input v-model="registerFrom.phone" placeholder="手机号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerFrom.password" placeholder="密码" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="15">
                <el-input v-model="registerFrom.code" placeholder="验证码"></el-input>
              </el-col>
              <el-col :span="1" style="height: 1px">
              </el-col>
              <el-col :span="8">
                <el-button :loading="registerLoading" type="primary" key="key" :disabled="disable"
                           @click="getVerifyCodes('注册')">{{ getCode }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="registerButton">注 册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="mb-10 flex-right">
          <el-button type="text" @click="backToLogin">
            返回登录 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
      </div>
      <!--      密码重置-->
      <div v-show="loginDialogType==3">
        <el-form :model="registerFrom" :rules="registerRules" ref="registerFrom" class="demo-ruleForm">
          <el-form-item prop="phone">
            <el-input v-model="registerFrom.phone" placeholder="手机号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerFrom.password" placeholder="新密码" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-row>
              <el-col :span="15">
                <el-input v-model="registerFrom.code" placeholder="验证码"></el-input>
              </el-col>
              <el-col :span="1" style="height: 1px">
              </el-col>
              <el-col :span="8">
                <el-button :loading="registerLoading" type="primary" key="key" :disabled="disable"
                           @click="getVerifyCodes('密码重置')">{{ getCode }}
                </el-button>
              </el-col>

            </el-row>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="resetPasswords">确定重置
            </el-button>
          </el-form-item>
        </el-form>
        <div class="mb-10 flex-left">
          <el-button type="text" class="fl-right" @click="backToLogin">
            <i class="el-icon-arrow-left"></i>
            返回登录
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "LoginModule",
  props: {
    isLogin: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      registerRules: {
        // username: [
        //   {required: true, message: '请输入账号', trigger: 'blur'},
        // ],
        // password: [
        //   {required: true, message: '请输入密码', trigger: 'blur'},
        // ],
      },
      registerFrom: {
        phone: '',
        password: null,
        code: null,
      },
      getCode: '获取验证码',
      count: 60,
      disable: false,
      registerLoading: false,
      loginTitle: "登 录",
      loginDialogType: 1,
      buttonLoading: false,
      form: {
        username: null,
        password: null,
      },
    }
  },
  methods: {
    resetPasswords() {
      this.$refs['registerFrom'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let fromData = {
            phone: this.registerFrom.phone == null ? null : this.$base64.encode(this.registerFrom.phone),
            password: this.registerFrom.password == null ? null : this.$base64.encode(this.registerFrom.password),
            code: this.registerFrom.code == null ? null : this.$base64.encode(this.registerFrom.code),
            registerType: 2
          }
          this.$API("/oauth/reset/password", "post", null, fromData).then(res => {
            if (res.code === 200) {
              this.$modal.notifySuccess("密码重置成功！");
              this.backToLogin();
            }
          }).finally(() => this.buttonLoading = false);
        }
      });
    },
    backToLogin() {
      this.clearData();
      this.loginTitle = "登 录";
      this.loginDialogType = 1;
    },
    getVerifyCodes(mag) {
      this.$refs["registerFrom"].validate((valid) => {
        if (valid) {
          const pathonJy = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;
          if (!pathonJy.test(this.registerFrom.phone)) {
            this.$modal.msgError("手机号格式错误");
            return;
          }
          this.registerLoading = true;
          this.getCode = "发送中";
          this.$API(`/oauth/phone/code/${this.registerFrom.phone}/${mag}`, "get").then().finally(() => this.registerLoading = false)
          let countDown = setInterval(() => {
            this.registerLoading = false;
            if (this.count < 1) {
              this.disable = false;
              this.getCode = '获取验证码';
              this.count = 60;
              clearInterval(countDown);
              //发送邮件
            } else {
              this.disable = true;
              this.getCode = this.count-- + 's后重发';
            }
          }, 1000)
        }
      });
    },
    registerButton() {
      this.$refs['registerFrom'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let fromData = {
            phone: this.registerFrom.phone == null ? null : this.$base64.encode(this.registerFrom.phone),
            password: this.registerFrom.password == null ? null : this.$base64.encode(this.registerFrom.password),
            code: this.registerFrom.code == null ? null : this.$base64.encode(this.registerFrom.code),
            registerType: 1
          }
          this.$API('/oauth/front-desk/register', this.$post(), null, fromData).then(res => {
            if (res.code === 200) {
              this.$modal.notifySuccess("注册成功，欢迎加入这个大家庭！");
              let fromData = {
                username: this.$base64.encode(this.registerFrom.phone),
                password: this.$base64.encode(this.registerFrom.password),
              }
              this.$API("/oauth/front-desk/login", this.$post(), null, fromData).then(res => {
                if (res.code == 200) {
                  this.urlSplicing();
                  this.$router.push(process.env.LOGIN_TRANSFER + "?key=" + res.data.uuid + "&token=" + res.data.token);
                  this.handleClose();
                }
              }).finally(() => this.buttonLoading = false)
            }
          }).finally(() => this.buttonLoading = false);
        }
      });
    },
    clearData() {
      this.registerFrom.phone = null;
      this.registerFrom.code = null;
      this.registerFrom.password = null;
      this.form.username = null;
      this.form.password = null;
    },
    resetPasswordButton() {
      this.clearData();
      this.loginTitle = "重置密码";
      this.loginDialogType = 3;
    },
    registerDialog() {
      this.clearData();
      this.loginTitle = "注 册";
      this.loginDialogType = 2;
    },
    //登录
    login(type) {
      this.notRefreshLogOut();//退出
      this.urlSplicing();
      if (type === "qq") {
        this.$modal.notify("待开发！");
        return
      }
      this.$API(`/oauth/render/${type}`, this.$post()).then(res => {
        if (res.code == 200) {
          window.location.href = res.data.url;
        } else {
          this.$modal.alertError(res.msg);
        }
      })
    },
    // 退出
    notRefreshLogOut() {
      this.$API("/oauth/logout", this.$post()).then(res => {
        this.$store.commit('removeToken')//删除 token
      })
    },
    // 账号登录
    LoginButton(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          let fromData = {
            username: this.form.username == null ? null : this.$base64.encode(this.form.username),
            password: this.form.password == null ? null : this.$base64.encode(this.form.password),
          }
          this.$API("/oauth/front-desk/login", this.$post(), null, fromData).then(res => {
            if (res.code == 200) {
              this.urlSplicing();
              this.$router.push(process.env.LOGIN_TRANSFER + "?key=" + res.data.uuid + "&token=" + res.data.token);
              this.handleClose();
            }
          }).finally(() => this.buttonLoading = false)
        }
      });
    },
    //保存url
    urlSplicing() {
      this.pathname = this.$route.fullPath;
      //保存当前url
      sessionStorage.setItem('url', this.pathname);
    },
    loginClose() {
      this.form.username = null;
      this.form.password = null;
    },

    handleClose() {
      //触发父组件事件（取消弹框)
      this.$emit('loginDialogMethod', false)
    },
  },
  mounted() {

  }
}
</script>

<style>

</style>
