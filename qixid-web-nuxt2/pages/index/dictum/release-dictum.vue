<template>
  <div>
    <div class="module-main">
      <el-skeleton class="mt-10" :rows="8" animated v-if="loading"/>
      <div v-show="!loading" class="flex-left">
        <div class="flex-2">
          <div class="font-s-18">
            <svg t="1682398969024" class="icon-size-20 svg-translateY-4 icon-theme-stand-out" viewBox="0 0 1024 1024"
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
            <svg t="1735800738498" class="icon" viewBox="0 0 1470 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                 p-id="52524" width="120" height="120">
              <path
                d="M274.519149 135.080851m-82.791489 0a82.791489 82.791489 0 1 0 165.582978 0 82.791489 82.791489 0 1 0-165.582978 0Z"
                fill="#FF6584" p-id="52525"></path>
              <path
                d="M679.761702 448.381277C679.761702 645.721328 562.711966 714.621277 418.314894 714.621277c-144.397072 0-261.446809-68.899949-261.446809-266.24C156.868085 251.041226 418.314894 0 418.314894 0s261.446809 251.041226 261.446808 448.381277z"
                fill="#E8E8F5" p-id="52526"></path>
              <path
                d="M409.469277 680.092868l2.658042-164.101447L522.893617 312.95183 412.545634 490.238911l1.202655-73.789005 76.333754-146.880817-76.020017 127.359456 2.152578-132.7104 81.745702-116.945158-81.405821 96.072987L417.896579 0l-8.453447 322.146043 0.697191-13.290213-83.11394-127.455319 81.780562 152.963812-7.738826 148.214196-0.235302-3.921702L305.021277 344.517174l95.515234 148.022469-0.958639 18.536578-0.174298 0.278877 0.078435 1.516391L379.829923 888.919149h26.24926l3.154791-194.228834 95.288647-147.665158z"
                fill="#3F3D56" p-id="52527"></path>
              <path
                d="M1464.102128 531.939677C1464.102128 693.182638 1368.50846 749.480851 1250.587234 749.480851S1037.07234 693.182638 1037.07234 531.939677C1037.07234 370.70543 1250.587234 165.582979 1250.587234 165.582979S1464.102128 370.70543 1464.102128 531.939677z"
                fill="#E8E8F5" p-id="52528"></path>
              <path
                d="M1233.924357 718.987438l2.126434-133.538315L1324.66383 420.2496l-88.281873 144.257634 0.958639-60.045617 61.073974-119.524766-60.812527 103.637515 1.716834-107.994962 65.396561-95.157923-65.1264 78.172596 1.080647-198.011098-6.762757 262.144 0.557753-10.815183-66.494638-103.715949 65.422706 124.474825-6.187575 120.605413-0.191727-3.207081L1150.365957 445.941106l76.420903 120.448545-0.784341 15.076766-0.130723 0.235302 0.061004 1.237515L1210.211132 888.919149h21.002894l2.527319-158.053311 76.220459-120.152238z"
                fill="#3F3D56" p-id="52529"></path>
              <path d="M0 897.634043a723.33617 34.859574 0 1 0 1446.67234 0 723.33617 34.859574 0 1 0-1446.67234 0Z"
                    fill="#3F3D56" p-id="52530"></path>
              <path
                d="M1037.07234 962.995745a126.365957 8.714894 0 1 0 252.731915 0 126.365957 8.714894 0 1 0-252.731915 0Z"
                fill="#3F3D56" p-id="52531"></path>
              <path
                d="M592.612766 1015.285106a126.365957 8.714894 0 1 0 252.731915 0 126.365957 8.714894 0 1 0-252.731915 0Z"
                fill="#3F3D56" p-id="52532"></path>
              <path
                d="M87.148936 962.995745a287.591489 13.07234 0 1 0 575.182979 0 287.591489 13.07234 0 1 0-575.182979 0Z"
                fill="#3F3D56" p-id="52533"></path>
              <path
                d="M130.723404 749.480851m-104.578723 0a104.578723 104.578723 0 1 0 209.157447 0 104.578723 104.578723 0 1 0-209.157447 0Z"
                fill="#2F2E41" p-id="52534"></path>
              <path
                d="M78.826213 824.594519l31.347472 0.366026-0.662332 56.193634-31.347472-0.374741zM141.512443 825.32657l31.347472 0.374741-0.662332 56.193634-31.347472-0.374741z"
                fill="#2F2E41" p-id="52535"></path>
              <path
                d="M104.166916 891.550366a26.118536 9.804255 0.675 1 0 0.231002-19.607149 26.118536 9.804255 0.675 1 0-0.231002 19.607149Z"
                fill="#2F2E41" p-id="52536"></path>
              <path
                d="M166.861861 890.975183a26.118536 9.804255 0.675 1 0 0.231002-19.607149 26.118536 9.804255 0.675 1 0-0.231002 19.607149Z"
                fill="#2F2E41" p-id="52537"></path>
              <path
                d="M130.723404 714.621277m-34.859574 0a34.859574 34.859574 0 1 0 69.719149 0 34.859574 34.859574 0 1 0-69.719149 0Z"
                fill="#FFFFFF" p-id="52538"></path>
              <path
                d="M126.365957 718.978723m-13.07234 0a13.07234 13.07234 0 1 0 26.144681 0 13.07234 13.07234 0 1 0-26.144681 0Z"
                fill="#3F3D56" p-id="52539"></path>
              <path
                d="M27.47806 654.497226c-7.686536-38.589549 18.667302-77.161668 58.842961-86.146724 40.17566-8.97634 78.983081 15.015762 86.660902 53.605311 7.677821 38.598264-19.042043 52.46366-59.217702 61.44-40.17566 8.99377-78.60834 9.690962-86.286161-28.898587zM287.591489 803.478332C287.591489 873.2672 244.670638 897.634043 191.72766 897.634043a170.419745 170.419745 0 0 1-10.954622-0.357311C132.989277 893.999932 95.86383 868.378145 95.86383 803.478332c0-67.1744 88.804766-151.92674 95.462944-158.201464l0.01743-0.01743L191.72766 644.902128s95.86383 88.787336 95.863829 158.576204z"
                fill="#55D4AF" p-id="52540"></path>
              <path
                d="M185.757957 887.001872l34.214673-47.374161-34.301821 52.568238-0.087149 5.438094a154.053174 154.053174 0 0 1-7.111354-0.322452l3.6864-69.832442-0.026144-0.540323 0.061004-0.104579 0.348596-6.597175L148.153191 767.538111l34.493549 47.757617 0.087149 1.394383 2.788766-52.759966-29.447625-54.459371 29.804936 45.195439 2.893345-109.389345 0.017429-0.37474v0.35731l-0.488034 86.268732 29.308188-34.197242-29.430196 41.622332-0.775626 47.243438 27.364766-45.334877-27.478059 52.289362-0.435745 26.257974L226.587234 756.313328l-39.879353 72.263898-0.958638 58.415931z"
                fill="#3F3D56" p-id="52541"></path>
              <path
                d="M932.493617 749.480851m-104.578723 0a104.578723 104.578723 0 1 0 209.157446 0 104.578723 104.578723 0 1 0-209.157446 0Z"
                fill="#2F2E41" p-id="52542"></path>
              <path
                d="M909.399149 834.276766l29.961804-9.211643 16.497294 53.718605-29.961804 9.211642zM969.322757 815.862196l29.961805-9.220358 16.497293 53.718605-29.961804 9.211642z"
                fill="#2F2E41" p-id="52543"></path>
              <path
                d="M926.090148 888.617517a9.804255 26.118536 72.917 1 0 49.932384-15.344991 9.804255 26.118536 72.917 1 0-49.932384 15.344991Z"
                fill="#2F2E41" p-id="52544"></path>
              <path
                d="M985.630302 868.939287a9.804255 26.118536 72.917 1 0 49.932384-15.344991 9.804255 26.118536 72.917 1 0-49.932384 15.344991Z"
                fill="#2F2E41" p-id="52545"></path>
              <path
                d="M923.778723 714.621277m-34.859574 0a34.859574 34.859574 0 1 0 69.719149 0 34.859574 34.859574 0 1 0-69.719149 0Z"
                fill="#FFFFFF" p-id="52546"></path>
              <path
                d="M928.13617 710.26383m-13.07234 0a13.07234 13.07234 0 1 0 26.144681 0 13.07234 13.07234 0 1 0-26.144681 0Z"
                fill="#3F3D56" p-id="52547"></path>
              <path
                d="M801.395472 683.909991c-18.937464-35.826928-4.54046-83.140085 32.157958-105.668085 36.689702-22.536715 81.789277-11.756391 100.72674 24.079251 18.937464 35.818213-3.128647 58.659949-39.827064 81.196664-36.689702 22.528-74.111455 36.219098-93.057634 0.39217zM749.480851 792.532426C749.480851 857.528102 710.464272 880.204255 662.331915 880.204255a151.290553 151.290553 0 0 1-9.961124-0.339881C608.935762 876.822877 575.182979 852.970213 575.182979 792.54114c0-62.538077 80.72606-141.451438 86.791625-147.290417v-0.008714L662.331915 644.902128s87.148936 82.660766 87.148936 147.630298z"
                fill="#55D4AF" p-id="52548"></path>
              <path
                d="M652.179064 870.295421l30.414979-44.097361-30.493413 48.942842-0.078434 5.063353a130.827983 130.827983 0 0 1-6.318298-0.296306l3.2768-65.021821-0.026145-0.505464 0.061004-0.087149 0.305022-6.144L618.757447 759.084664l30.658996 44.454672 0.069719 1.307234 2.483744-49.125855-26.170825-50.703251 26.493276 42.075506 2.570894-101.842247 0.008715-0.348595v0.339881l-0.42703 80.307744 26.057532-31.835506-26.162111 38.755132-0.697191 43.984068 24.331983-42.214945-24.427847 48.690111-0.383455 24.445276L688.476596 748.626791l-35.443473 67.278979-0.854059 54.389651z"
                fill="#3F3D56" p-id="52549"></path>
              <path
                d="M1089.361702 820.908119c0 69.788868-42.920851 94.155711-95.86383 94.155711a170.419745 170.419745 0 0 1-10.954621-0.357311c-47.783762-3.2768-84.909209-28.898587-84.909208-93.7984 0-67.1744 88.804766-151.92674 95.462944-158.201464l0.01743-0.017429L993.497872 662.331915s95.86383 88.787336 95.86383 158.576204z"
                fill="#55D4AF" p-id="52550"></path>
              <path
                d="M996.243064 904.43166l34.214672-47.374162-34.301821 52.568238-0.087149 5.438094a154.053174 154.053174 0 0 1-7.111353-0.322451l3.6864-69.832443-0.026145-0.540323 0.061004-0.104579 0.348596-6.597174L958.638298 784.967898l34.493549 47.757617 0.087149 1.394383 2.788766-52.759966-29.447626-54.45937 29.804936 45.195438 2.893345-109.389345 0.01743-0.37474v0.357311l-0.488034 86.268731 29.308187-34.197242-29.430196 41.622332-0.775625 47.243438 27.364766-45.334876-27.47806 52.289361-0.435745 26.257975 39.7312-63.09583-39.879353 72.263898-0.958638 58.415932z"
                fill="#3F3D56" p-id="52551"></path>
              <path
                d="M883.556908 797.318532a51.59217 16.200987 25.374 1 0 13.885061-29.276151 51.59217 16.200987 25.374 1 0-13.885061 29.276151Z"
                fill="#2F2E41" p-id="52552"></path>
              <path
                d="M89.069369 791.195113a51.59217 16.200987 21.842 1 0 12.055101-30.075945 51.59217 16.200987 21.842 1 0-12.055101 30.075945Z"
                fill="#2F2E41" p-id="52553"></path>
            </svg>

          </div>
          <div class="mr-20 line-height-24">
            <div class="mb-20 color-stand-out"><i class="el-icon-edit mr-6"></i>名言分类
              <hr class="hr-item mt-6 mb-8"/>
              <p class="font-s-14 color-grey">
                选择对应的分类，更方便大家筛选到名言。
              </p>
            </div>
            <div class="mb-20 color-stand-out"><i class="el-icon-edit mr-6"></i>专辑
              <hr class="hr-item mt-6 mb-8"/>
              <p class="font-s-14 color-grey">
                合理创建专辑更方便名言的管理。
              </p>
            </div>
            <div class="color-stand-out"><i class="el-icon-edit mr-6"></i>标签
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
              <div style="border: 1px solid #DCDFE6;border-radius: 2px">
                <VditorMd :height="'300px'"
                          :outline="false"
                          :vditorId="'releaseDictum'"
                          :mdContent.sync="dictumInfo.content"
                          :content="dictumInfo.contentMd" v-if="!loading"></VditorMd>
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
import VditorMd from "../../../components/Vditor-md.vue";

export default {
  name: "releaseDictum",
  components: {VditorMd},
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
