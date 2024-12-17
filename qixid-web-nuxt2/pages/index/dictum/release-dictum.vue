<template>
  <div>
    <div class="module-main">
      <el-skeleton class="mt-10" :rows="8" animated v-if="loading"/>
      <div v-show="!loading" class="flex-left">
        <div class="flex-2">
          <div class="font-s-18">
            <svg t="1682398969024" class="icon-size-20 svg-translateY-4 icon-theme" viewBox="0 0 1024 1024"
                 version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="2153">
              <path
                d="M683.05408 73.19552c-47.64672-26.75712-80.65536-37.49888-113.14688-41.33888-46.56128-6.90688-88.05888-7.76704-126.94016 3.49184-229.84704 66.5344-319.24736 257.82272-206.9504 467.78368 54.62528 102.144 245.888 361.7024 277.66272 388.41344 23.48032-18.78528 232.74496-289.11616 296.10496-420.98688 78.91968-164.25984-10.7776-332.24704-126.73024-397.3632z m71.35232 370.75456c-45.52704 94.75072-172.72832 267.91936-240.60416 353.33632-65.18272-83.48672-184.02304-249.11872-223.60576-323.13344-44.4416-83.08736-53.0432-162.31936-24.87808-229.12512C294.51776 175.7696 363.6736 122.26048 460.0576 94.3616c14.34112-4.15232 30.09024-6.1696 48.1536-6.1696 15.32416 0 32.55808 1.45408 52.68992 4.43904l1.81248 0.24064c19.67104 2.32448 44.22656 8.0384 90.2656 33.8944 80.60416 45.27104 168.59136 177.38752 101.4272 317.184z"
                p-id="2154"></path>
              <path
                d="M512.55808 188.04224c-89.18528 0.65024-161.82272 73.83552-161.82272 163.05152 0.00512 91.20256 75.66336 165.13536 166.90176 163.10272 88.99584-1.98656 160.5632-76.40576 159.2576-165.62176-1.28512-89.10848-75.06944-161.1776-164.33664-160.53248z m3.70688 264.7296c-0.76288 0.01536-1.536 0.0256-2.304 0.0256-56.12032 0-101.78048-45.62432-101.7856-101.7088 0-55.62368 45.23008-101.20704 100.82816-101.61152h0.74752c55.29088 0 100.9152 44.85632 101.7088 99.98336 0.81408 55.73632-43.68384 102.07232-99.19488 103.31136zM770.45248 608.11264a3239.42912 3239.42912 0 0 1-35.68128 51.72736c12.0576 4.8384 23.57248 10.05056 34.304 15.70304 52.224 27.50464 83.4048 62.41792 83.4048 93.40416 0 30.98112-31.1808 65.8944-83.4048 93.39904C703.75936 896.74752 611.93216 916.48 517.13536 916.48s-186.624-19.73248-251.94496-54.12864c-52.224-27.50464-83.4048-62.41792-83.4048-93.39904s31.1808-65.89952 83.40992-93.40416a359.61856 359.61856 0 0 1 30.0544-13.97248 3924.16256 3924.16256 0 0 1-35.57888-51.56352c-85.22752 38.32832-139.32544 95.29344-139.32544 158.94528 0 115.40992 177.64864 208.96768 396.78976 208.96768s396.78464-93.55776 396.78464-208.96768c0-64.69632-55.82336-122.50624-143.46752-160.8448z"
                p-id="2155"></path>
            </svg>
            当前页面&nbsp;/&nbsp;<span class="font-s-14 color-grey">{{ update.pageTitle }}</span>
          </div>
          <div class="mt-20 mb-10 text-center">
            <svg t="1734320218672" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="9153" width="128" height="128">
              <path
                d="M327.111 312.889V139.378h34.133v-28.445H261.69v28.445h34.133v173.51h31.29z m142.222 0v-28.445H415.29v-59.733H460.8v-28.444h-45.511v-56.89h54.044v-28.444H384V312.89h85.333z m48.356 0l22.755-73.956L563.2 312.89h34.133l-39.822-105.245 39.822-96.71H563.2L540.444 179.2l-19.91-68.267H486.4l36.978 96.711-39.822 105.245h34.133z m156.444 0V139.378h34.134v-28.445H608.71v28.445h34.133v173.51h31.29z"
                fill="#A8CFFF" p-id="9154"></path>
              <path
                d="M591.644 881.778V759.467H614.4v-19.911h-71.111v19.91h25.6v122.312h22.755z m99.556 0v-19.911h-36.978v-39.823h31.29v-19.91h-31.29V762.31H691.2v-22.755h-59.733v142.222H691.2z m34.133 0l17.067-51.2 17.067 51.2h25.6l-28.445-73.956 25.6-68.266h-25.6l-17.066 48.355-14.223-48.355h-25.6l25.6 68.266-28.444 73.956h28.444z m108.09 0V759.467h22.755v-19.911h-71.111v19.91h22.755v122.312h25.6z"
                fill="#1071E2" opacity=".7" p-id="9155"></path>
              <path
                d="M128 654.222V409.6h48.356v-39.822H36.978V409.6h48.355v244.622H128z m199.111 0V614.4h-73.955v-82.489h65.422V492.09h-65.422v-79.645h73.955v-42.666H207.644v284.444h119.467z m68.267 0l31.289-102.4 31.289 102.4h48.355l-56.889-147.91 54.045-136.534H455.11l-28.444 93.866-28.445-93.866h-48.355l51.2 136.533-56.89 147.911h51.2z m219.022 0V409.6h48.356v-39.822H523.378V409.6h48.355v244.622H614.4z"
                fill="#1071E2" p-id="9156"></path>
              <path
                d="M768 341.333v-68.266h14.222v-11.378H742.4v11.378h14.222v68.266H768z m56.889 0v-11.377h-19.911V307.2h17.066v-11.378h-17.066v-22.755h19.91v-11.378h-34.132v79.644h34.133z m19.911 0l8.533-28.444 8.534 28.444h14.222l-17.067-42.666 14.222-39.823h-14.222l-8.533 28.445-8.533-28.445h-14.223l14.223 39.823-11.378 42.666H844.8z m62.578 0v-68.266H921.6v-11.378h-39.822v11.378H896v68.266h11.378z"
                fill="#1071E2" opacity=".5" p-id="9157"></path>
              <path
                d="M733.867 540.444v-122.31h22.755v-19.912h-71.11v19.911h25.6v122.311h22.755z m99.555 0v-19.91h-36.978V480.71h31.29V460.8h-31.29v-39.822h36.978v-22.756H773.69v142.222h59.733z m34.134 0l17.066-51.2 17.067 51.2h25.6l-28.445-73.955 25.6-68.267h-25.6l-17.066 48.356-14.222-48.356h-25.6l25.6 68.267-28.445 73.955h28.445z m108.088 0v-122.31H998.4v-19.912h-71.111v19.911h22.755v122.311h25.6z"
                fill="#1071E2" opacity=".8" p-id="9158"></path>
              <path
                d="M292.978 824.889V736.71h17.066V722.49h-51.2v14.222h17.067v88.178h17.067z m71.11 0v-14.222h-25.6v-28.445h22.756V768H338.49v-28.444h25.6v-14.223h-42.667v99.556h42.667z m22.756 0l11.378-36.978L409.6 824.89h17.067l-19.911-51.2 19.91-48.356H409.6l-11.378 31.29-11.378-34.134h-17.066l17.066 48.355-17.066 54.045h17.066z m79.645 0V736.71h17.067V722.49h-51.2v14.222h17.066v88.178h17.067z"
                fill="#1071E2" opacity=".5" p-id="9159"></path>
              <path
                d="M705.422 682.667v-51.2H716.8v-8.534h-31.289v8.534h11.378v51.2h8.533z m42.667 0v-8.534h-17.067v-17.066h14.222v-8.534h-14.222v-17.066h17.067v-8.534h-25.6v59.734h25.6z m14.222 0L768 659.91l5.689 22.756h11.378l-11.378-31.29 11.378-28.444h-11.378L768 642.844l-5.689-19.91h-11.378l11.378 28.444-11.378 31.289h11.378z m45.511 0v-51.2H819.2v-8.534h-31.289v8.534h11.378v51.2h8.533z"
                fill="#1071E2" opacity=".6" p-id="9160"></path>
              <path
                d="M164.978 312.889v-51.2h11.378v-8.533h-31.29v8.533h11.378v51.2h8.534z m42.666 0v-8.533h-17.066v-17.067H204.8v-8.533h-14.222v-17.067h17.066v-8.533h-25.6v59.733h25.6z m14.223 0l5.689-22.756 8.533 22.756h11.378L233.244 281.6l11.378-28.444h-11.378l-5.688 19.91-5.69-19.91H210.49l11.378 28.444-11.378 31.289h11.378z m45.51 0v-51.2h11.379v-8.533h-31.29v8.533h11.378v51.2h8.534z"
                fill="#1071E2" opacity=".3" p-id="9161"></path>
            </svg>

          </div>
          <div class="mr-20 line-height-24">
            <div class="mb-20 color-stand-out"><i class="el-icon-edit"></i>名言分类
              <hr class="hr-item mt-6 mb-8"/>
              <p class="font-s-14 color-grey">
                选择对应的分类，更方便大家筛选到名言。
              </p>
            </div>
            <div class="mb-20 color-stand-out"><i class="el-icon-edit"></i>专辑
              <hr class="hr-item mt-6 mb-8"/>
              <p class="font-s-14 color-grey">
                合理创建专辑更方便名言的管理。
              </p>
            </div>
            <div class="color-stand-out"><i class="el-icon-edit"></i>标签
              <hr class="hr-item mt-6 mb-8"/>
              <p class="font-s-14 color-grey">
                添加与名言相关的标签，如作者，作品名称，与作品相关的热词，名言更容易被检索到。
              </p>
            </div>
          </div>
        </div>
        <div class="release-dictum-index flex-10">
          <el-form :model="dictumInfo" :rules="rules" ref="dictumInfo" label-width="100px" class="demo-ruleForm">
            <el-form-item label="名言分类：" prop="groupId">
              <div>
                <el-radio-group :border="false" v-model="dictumInfo.groupId">
                  <el-radio-button v-for="(item,index) in dictumGroupListArr" :key="index" :label="item.id"
                                   style="margin:0 10px 10px 0;">
                    {{ item.name }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </el-form-item>
            <el-row>
              <el-col :span="10">
                <el-tooltip class="item" content="名言的作者名称" placement="top-end" effect="light">
                  <el-form-item label="名言作者：" prop="author">
                    <el-input v-model="dictumInfo.author" clearable></el-input>
                  </el-form-item>
                </el-tooltip>
              </el-col>
              <el-col :span="14">
                <el-tooltip class="item" content="名言出处的作品名称" placement="top-end" effect="light">
                  <el-form-item label="作品名称：" prop="worksName">
                    <el-input v-model="dictumInfo.worksName" clearable></el-input>
                  </el-form-item>
                </el-tooltip>
              </el-col>
            </el-row>
            <el-form-item label="名言：" prop="content">
              <!--              <el-input type="textarea" maxlength="500"-->
              <!--                        :rows="8"-->
              <!--                        clearable-->
              <!--                        show-word-limit v-model="dictumInfo.content"></el-input>-->
              <div style="border: 1px solid #DCDFE6;border-radius: 2px">
                <ai-editor-module
                  v-if="!loading"
                  :id="'dictum-editor'"
                  :htmlContent.sync="dictumInfo.content"
                  :mdContent.sync="dictumInfo.contentMd"
                  :content="dictumInfo.content"
                  :minimalistMode="false"
                  :toolbar-size="'medium'"
                  :editor-height="'300px'"></ai-editor-module>
              </div>
            </el-form-item>
            <el-row>
              <el-col :span="10">
                <el-form-item label="收录专辑：" prop="albumId">
                  <el-select v-model="dictumInfo.albumId" filterable placeholder="请选择">
                    <el-option
                      v-for="item in dictumAlbumListArr"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                    </el-option>
                  </el-select>
                  <el-button class="ml-6" size="medium" icon="el-icon-plus" circle title="添加专辑"
                             @click="dialogVisible = true"></el-button>
                </el-form-item>
              </el-col>
              <el-col :span="14">
                <el-form-item label="标签：" prop="albumId">
                  <el-tag
                    v-for="(tag,index) in dynamicTags"
                    closable
                    :disable-transitions="false"
                    @close="handleClose(tag)" :key="index">
                    {{ tag }}
                  </el-tag>
                  <el-input
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    @keyup.enter.native="handleInputConfirm"
                    @blur="handleInputConfirm"
                  >
                  </el-input>
                  <el-button v-if="!inputVisible && addVisible" class="button-new-tag" size="small"
                             @click="showInput">+
                    添加标签
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="名言类型：" prop="dictumState">
              <el-radio-group v-model="dictumInfo.dictumState">
                <el-radio :label="1">公开</el-radio>
                <el-radio :label="2">私有</el-radio>
                <!--                  <el-radio :label="3">粉丝可看</el-radio>-->
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button class="fl-right" type="primary" @click="releaseDictum" :loading="buttonLoading">
                {{ update.buttonTitle }}
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <el-dialog
        title="添加专辑"
        :visible.sync="dialogVisible"
        width="30%">
        <el-form :model="albumInfo" :rules="albumInfoRules" ref="albumInfo" label-width="100px" class="demo-ruleForm">
          <el-form-item label="专辑名称：" prop="name">
            <el-input v-model="albumInfo.name" clearable></el-input>
          </el-form-item>
          <el-form-item label="简介：" prop="briefIntroduction">
            <el-input type="textarea" maxlength="200"
                      :rows="6"
                      clearable
                      show-word-limit v-model="albumInfo.briefIntroduction"></el-input>
          </el-form-item>
          <el-form-item label="封面：">
            <div class="articleCoverCa">
              <imageUpload v-model="albumValue" :limit="1"/>
            </div>
          </el-form-item>
          <el-form-item label="专辑状态：" prop="albumState">
            <el-radio-group v-model="albumInfo.albumState">
              <el-radio :label="1">公开</el-radio>
              <el-radio :label="2">私有</el-radio>
              <!--            <el-radio :label="3">粉丝可看</el-radio>-->
            </el-radio-group>
          </el-form-item>
          <el-form-item class="text-right">
            <el-button size="medium" @click="dialogVisible = false">取消</el-button>
            <el-button size="medium" type="primary" @click="addAlbum()" :loading="buttonLoading">添加专辑</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>

</template>

<script>
export default {
  name: "releaseDictum",
  data() {
    return {
      update: {
        id: this.$route.query.code == null ? null : this.$base64.decode(this.$route.query.code),
        info: null,
        buttonTitle: '记录名言',
        pageTitle: '名言发布',
      },
      albumInfo: {
        albumState: 1,
      },
      albumInfoRules: {
        name: [
          {required: true, message: '请输入专辑名称', trigger: 'blur'},
        ],
        albumState: [
          {required: true, message: '请选择专辑状态', trigger: 'blur'},
        ],
      },
      dynamicTags: [],
      inputVisible: false,
      dialogVisible: false,
      addVisible: true,
      buttonLoading: false,
      inputValue: '',
      dictumInfo: {
        content: null,
        contentMd: null,
        albumId: null,
        dictumState: 1,
      },
      value: [],
      albumValue: [],
      dictumGroupListArr: [],
      dictumAlbumListArr: [],
      loading: true,
      userInfos: null,
      rules: {
        content: [
          {required: true, message: '请输入名言内容', trigger: 'blur'},
        ],
        groupId: [
          {required: true, message: '请选择名言分类', trigger: 'blur'}
        ],
        dictumState: [
          {required: true, message: '请选择发布状态', trigger: 'blur'}
        ],
      },
    }
  },
  methods: {
    addAlbum() {
      this.$refs["albumInfo"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.albumValue.length > 0) {
            this.albumInfo.cover = this.albumValue[0].url;
          }
          this.$API("/frontDesk/dictum/album", "post", null, this.albumInfo).then(res => {
            if (res.code == 200) {
              this.$modal.msgSuccess("添加成功!");
              this.dictumAlbumLists();
            }
          }).finally(() => {
            this.dialogVisible = false;
            this.buttonLoading = false;
          })
        }
      });
    },
    releaseDictum() {
      this.$refs["dictumInfo"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.dynamicTags.length > 0) {
            this.dictumInfo.label = this.dynamicTags.toString();
          }
          this.$API("/frontDesk/dictum/info", "post", null, this.dictumInfo).then(res => {
            if (res.code == 200) {
              this.$router.push({
                path: '/dictum/space/content-list',
                query: {uuid: this.$base64.encode(this.userInfos.uuid)}
              })
            }
          })
        }

      });
    },
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      if (this.dynamicTags.length <= 2) {
        this.addVisible = true;
      }
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
        if (this.dynamicTags.length >= 2) {
          this.addVisible = false;
        }
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
    },

    dictumAlbumLists() {
      this.$API("/frontDesk/dictum/album/role/list", "get").then(res => {
        this.dictumAlbumListArr = res.rows;
      }).finally(() => this.loading = false);
    },
    dictumGroupLists() {
      this.$API("/white/dictum/group/list", "get").then(res => {
        this.dictumGroupListArr = res.rows;
      })
    },
    getBasicsUsers() {
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null) {
          return;
        }
        this.userInfos = res.data;
      })
    },
  },
  mounted() {
    if (this.update.id != null) {
      this.loading = true;
      this.update.buttonTitle = "更新名言";
      this.update.pageTitle = "名言更新";
      this.$API("/frontDesk/dictum/info/" + this.update.id, "get").then(res => {
        this.dictumInfo = res.data;
        if (this.dictumInfo.label != null) {
          this.dynamicTags = this.dictumInfo.label.split(",");
        }
      }).finally(() => this.loading = false);
    } else {

    }
    this.dictumAlbumLists()
    this.getBasicsUsers();
    this.dictumGroupLists()
  }
}
</script>

<style>
.release-dictum-index {
  padding: 20px;
  background-color: #fefefe;
  border-radius: 4px;
  box-shadow: 0 4px 8px 0 #ecf0f1;
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.el-radio-button__inner {
  border: 1px solid #DCDFE6;
}
</style>
