<template>
  <div style="margin: 20px 0 0 10px; padding: 0 10px 20px 0">
    <div style="font-size: 22px;font-weight: bold">账户设置</div>
    <el-divider></el-divider>
    <ul class="ul-li-cl" v-loading="loading">
      <li>
        <div class="flex-space-between">
          <div class="fl-li-cl">
            邮箱绑定
            <span class="font-s-14 color-grey ml-20">
              {{
                (userInfo.email == null || userInfo.email == '') ? '未绑定' : $utils.handleEmail(userInfo.email)
              }}</span>
          </div>
          <div class="fr-li-cl">
            <el-button type="text" v-if="userInfo.email==null ||userInfo.email==''" @click="emailBinding(1)">
              绑定
            </el-button>
            <el-button type="text" v-else @click="emailBinding(2)">换绑</el-button>
          </div>
        </div>
        <hr class="hr_gradient1"/>
      </li>
      <li>
        <div class="flex-space-between">
          <div class="fl-li-cl">手机号换绑
            <span class="font-s-14 color-grey ml-20">
              {{
                (userInfo.phone == null || userInfo.phone == '') ? '未绑定' : $utils.handlePhone(userInfo.phone)
              }}</span>
          </div>
          <div>
            <el-button type="text" v-if="userInfo.phone==null  ||userInfo.phone==''" @click="phoneBinding(4)">
              绑定
            </el-button>
            <el-button type="text" v-else @click="phoneBinding(3)">换绑</el-button>
          </div>
        </div>
        <hr class="hr_gradient1"/>
      </li>
      <li>
        <div class="flex-space-between">
          <div class="fl-li-cl">密码重置</div>
          <div>
            <el-button type="text" @click="passwordReset">重置</el-button>
          </div>
        </div>
        <hr class="hr_gradient1"/>
      </li>
      <li>
        <div class="flex-space-between">
          <div class="fl-li-cl">账号注销</div>
          <div>
            <el-button type="text" @click="accountCancellations">注销</el-button>
          </div>
        </div>
        <hr class="hr_gradient1"/>
      </li>
    </ul>
    <!--    邮箱-->
    <el-dialog
      title="绑定邮箱"
      :visible.sync="emailLoading"
      width="400px">
      <el-form :model="userBindBo" :rules="bindRules" ref="userBindBo">
        <el-form-item prop="email">
          <el-input v-model="userBindBo.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-row>
            <el-col :span="15">
              <el-input v-model="userBindBo.code" placeholder="验证码"></el-input>
            </el-col>
            <el-col :span="1" style="height: 1px">
            </el-col>
            <el-col :span="8" v-if="userBindBo.type==2">
              <el-button :loading="registerLoading" type="primary" key="key" :disabled="disable"
                         @click="getEmailCodes(userBindBo.originalData,3)">{{ getCode }}
              </el-button>
            </el-col>
            <el-col :span="8" v-if="userBindBo.type==1">
              <el-button :loading="registerLoading" type="primary" key="key" :disabled="disable"
                         @click="getEmailCodes(userBindBo.email,3)">{{ getCode }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="bindEmails">确定绑定
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--    绑定手机号-->
    <el-dialog
      title="绑定手机号"
      :visible.sync="phoneLoading"
      width="400px">
      <el-form :model="userBindBo" :rules="bindRules" ref="userBindBo">
        <el-form-item prop="phone">
          <el-input v-model="userBindBo.phone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-row v-if="userBindBo.type==3">
            <el-col :span="15">
              <el-input v-model="userBindBo.code" placeholder="验证码"></el-input>
            </el-col>
            <el-col :span="1" style="height: 1px">
            </el-col>
            <el-col :span="8">
              <el-button :loading="registerLoading" type="primary" key="key" :disabled="disable"
                         @click="setPhoneCode(userBindBo.originalData,4)">{{ getCode }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="bindEmails">确定绑定
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!--    重置密码-->
    <el-dialog
      title="重置密码"
      :visible.sync="dialogVisible"
      width="400px">
      <el-form :model="registerFrom" :rules="registerRules" ref="registerFrom">
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
                         @click="getVerifyCodes(registerFrom.phone,'密码重置')">{{ getCode }}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="buttonLoading" style="width: 100%" @click="resetPasswords">确定重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "accountNumber",
  data() {
    return {
      loading: true,
      dialogVisible: false,
      buttonLoading: false,
      registerLoading: false,
      emailLoading: false,
      phoneLoading: false,
      getCode: '获取验证码',
      count: 60,
      disable: false,
      userInfo: {},
      registerFrom: {
        phone: '',
        password: null,
        code: null,
      },
      registerRules: {
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
      },
      bindRules: {
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
        ],
        email: [
          {required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
        ],
      },
      userBindBo: {
        type: null,
        email: null,
        phone: null,
        code: null,
        originalData: null,
      },
    }

  },
  methods: {
    passwordReset() {
      if (this.userInfo.phone == null || this.userInfo.phone == '') {
        this.$modal.notifyError("请先绑定手机号！");
        return;
      }
      this.dialogVisible = true;
      this.registerFrom.phone = this.userInfo.phone;
    },
    phoneBinding(type) {
      if (type == 3) {
        this.userBindBo.originalData = this.userInfo.phone;
      }
      this.userBindBo.type = type;
      this.phoneLoading = true;
    },
    emailBinding(type) {
      if (this.userInfo.phone == null || this.userInfo.phone == '') {
        this.$modal.notifyError("请先绑定手机号！");
        return;
      }
      if (type == 2) {
        this.userBindBo.originalData = this.userInfo.email;
      }
      this.userBindBo.type = type;
      this.emailLoading = true;
    },
    bindEmails() {
      this.$refs['userBindBo'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          this.$API("/update/user/email", "put", null, this.userBindBo).then(res => {
            if (res.code == 200) {
              this.$modal.notifySuccess("绑定成功！");
              this.emailLoading = false;
              this.phoneLoading = false;
              this.getBasicsUsers();
            }
          }).finally(() => {
            this.buttonLoading = false;
          })
        }
      });
    },
    accountCancellations() {
      this.$confirm('该操作会导致账号永久注销，请谨慎操作！', '提示', {
        confirmButtonText: '确定注销',
        cancelButtonText: '取 消',
        type: 'error'
      }).then(() => {
        this.$API("/oauth/account/cancellation", "get").then(res => {
          if (res.code == 200) {
            this.$modal.notifySuccess("账号已注销！");
            this.notRefreshLogOut();
            this.$router.push("/out/transfer");
          }
        })
      });
    },
    // 退出
    notRefreshLogOut() {
      this.$API("/oauth/logout", "post").then(res => {
        this.pathname = this.$route.fullPath;
        //保存当前url
        sessionStorage.setItem('url', this.pathname);
        this.$router.push(process.env.LOG_OUT_TRANSFER);
      })
    },
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
              this.$modal.notifySuccess("密码重置成功，重新登录生效！");
            }
          }).finally(() => {
            this.buttonLoading = false
            this.dialogVisible = false
          });
        }
      });
    },
    getVerifyCodes(phone, mag) {
      this.$refs['registerFrom'].validate((valid) => {
        if (valid) {
          this.setPhoneCode(phone, mag);
        }
      });
    },
    setPhoneCode(phone, mag) {
      if (phone == "" || phone == null) {
        this.$modal.notifyError("请先绑定手机号！");
        return;
      }
      this.registerLoading = true;
      this.getCode = "发送中";
      this.$API(`/oauth/phone/code/${phone}/${mag}`, "get").then().finally(() => this.registerLoading = false)
      let countDown = setInterval(() => {
        this.registerLoading = false;
        if (this.count < 1) {
          this.disable = false;
          this.getCode = '获取验证码';
          this.count = 60;
          clearInterval(countDown);
        } else {
          this.disable = true;
          this.getCode = this.count-- + 's后重发';
        }
      }, 1000)
    },
    getEmailCodes(email, type) {
      this.$refs['userBindBo'].validate((valid) => {
        if (valid) {
          this.registerLoading = true;
          this.getCode = "发送中";
          this.$API(`/oauth/front-desk/code/${email}/${type}`, "get").then().finally(() => this.registerLoading = false)
          let countDown = setInterval(() => {
            this.registerLoading = false;
            if (this.count < 1) {
              this.disable = false;
              this.getCode = '获取验证码';
              this.count = 60;
              clearInterval(countDown);
            } else {
              this.disable = true;
              this.getCode = this.count-- + 's后重发';
            }
          }, 1000)
        }
      });
    },
    getBasicsUsers() {
      this.loading = true;
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res != null) {
          this.userInfo = res.data;
        }
      }).finally(() => this.loading = false);
    },

  },
  mounted() {
    this.getBasicsUsers();
  }

}
</script>

<style scoped>
.fl-li-cl {
  font-size: 15px;
  line-height: 40px;
}

.ul-li-cl {
  padding: 0 10px;
}

.hr_gradient1 {
  border: 0;
  height: 1px;
  background: #dcdfe6;
  margin-top: 6px;
  margin-bottom: 20px;
}

li {
  list-style-type: none;
}
</style>
