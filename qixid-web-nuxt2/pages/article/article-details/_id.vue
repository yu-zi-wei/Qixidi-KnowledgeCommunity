<template>
  <div>
    <navigation-bar-module></navigation-bar-module>
    <div class="mt-30">
      <div class="page-content-width">
        <div class="flex-space-between flex-wrap-wrap">
          <div class="left-content-width">
            <div class="left-content-ul left-content-width">
              <ul class="left-content-auto">
                <li class="fa-li-class" title="点赞"
                    @click="spotFabulousFormula(articleInfo.id,articleInfo.id,articleInfo.userId,articleInfo.likeTimes,articleInfo.articleTitle,articleInfo.labelId)">
                  <div class="fa-li-div">
                    <div
                      :class="{'circular-div':true,'background-color-theme':isFabulous,'background-color-c2c8d1':!isFabulous}"
                      v-if="articleInfo.likeTimes!=0 && articleInfo.likeTimes!=null">
                      <span class="sum-span-div" v-text="articleInfo.likeTimes"></span>
                    </div>
                    <svg t="1700403032641"
                         :class="{'icon':!isFabulous,'icon-size-24':true,'mt-10':true,'icon-theme':isFabulous}"
                         viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="7591">
                      <path
                        d="M190.193225 471.411583c14.446014 0 26.139334-11.718903 26.139334-26.13831 0-14.44499-11.69332-26.164916-26.139334-26.164916-0.271176 0-0.490164 0.149403-0.73678 0.149403l-62.496379 0.146333c-1.425466-0.195451-2.90005-0.295735-4.373611-0.295735-19.677155 0-35.621289 16.141632-35.621289 36.114522L86.622358 888.550075c0 19.949354 15.96767 35.597753 35.670407 35.597753 1.916653 0 3.808746 0.292666 5.649674 0l61.022819 0.022513c0.099261 0 0.148379 0.048095 0.24764 0.048095 0.097214 0 0.146333-0.048095 0.24457-0.048095l0.73678 0 0-0.148379c13.413498-0.540306 24.174586-11.422144 24.174586-24.960485 0-13.55983-10.760065-24.441669-24.174586-24.981974l0-0.393973-50.949392 0 1.450025-402.275993L190.193225 471.409536z"
                        p-id="7592"></path>
                      <path
                        d="M926.52241 433.948343c-19.283182-31.445176-47.339168-44.172035-81.289398-45.546336-1.77032-0.246617-3.536546-0.39295-5.380544-0.39295l-205.447139-0.688685c13.462616-39.059598 22.698978-85.58933 22.698978-129.317251 0-28.349675-3.193739-55.962569-9.041934-82.542948l-0.490164 0.049119c-10.638291-46.578852-51.736315-81.31498-100.966553-81.31498-57.264215 0-95.466282 48.15065-95.466282 106.126063 0 3.241834-0.294712 6.387477 0 9.532097-2.996241 108.386546-91.240027 195.548698-196.23636 207.513194l0 54.881958-0.785899 222.227314 0 229.744521 10.709923 0 500.025271 0.222057 8.746198-0.243547c19.35686 0.049119 30.239721-4.817726 47.803749-16.116049 16.682961-10.761088 29.236881-25.50079 37.490869-42.156122 2.260483-3.341095 4.028757-7.075139 5.106298-11.20111l77.018118-344.324116c1.056052-4.053316 1.348718-8.181333 1.056052-12.160971C943.643346 476.446249 938.781618 453.944769 926.52241 433.948343zM893.82573 486.837924l-82.983993 367.783411-0.099261-0.049119c-2.555196 6.141884-6.879688 11.596106-12.872169 15.427364-4.177136 2.727111-8.773827 4.351098-13.414521 4.964058-1.49812-0.195451-3.046383 0-4.620227 0l-477.028511-0.540306-0.171915-407.408897c89.323375-40.266076 154.841577-79.670527 188.596356-173.661202 0.072655 0.024559 0.124843 0.049119 0.195451 0.072655 2.99931-9.137101 6.313799-20.73423 8.697079-33.164331 5.551436-29.185716 5.258771-58.123792 5.258771-58.123792-4.937452-37.98001 25.940812-52.965306 44.364417-52.965306 25.304316 0.860601 50.263777 33.656541 50.263777 52.326762 0 0 5.600555 27.563776 5.649674 57.190537 0.048095 37.366026-4.6673 56.847729-4.6673 56.847729l-0.466628 0c-5.872754 30.879288-16.214287 60.138682-30.464849 86.964654l0.36839 0.342808c-2.358721 4.815679-3.709485 10.220782-3.709485 15.943111 0 19.922748 19.088754 21.742187 38.765909 21.742187l238.761895 0.270153c0 0 14.666024 0.465604 14.690584 0.465604l0 0.100284c12.132318-0.638543 24.221658 5.207605 31.100322 16.409738 5.504364 9.016351 6.437619 19.6045 3.486404 28.988218L893.82573 486.837924z"
                        p-id="7593"></path>
                      <path
                        d="M264.827039 924.31872c0.319272 0.024559 0.441045 0.024559 0.295735-0.024559 0.243547-0.048095 0.367367-0.074701-0.295735-0.074701s-0.539282 0.026606-0.271176 0.074701C264.43409 924.343279 264.532327 924.343279 264.827039 924.31872z"
                        p-id="7594"></path>
                    </svg>
                  </div>
                </li>
                <li class="fa-li-class" title="评论">
                  <div class="fa-li-div" @click="goComment">
                    <svg t="1741407164890" class="icon icon-size-24 mt-10 icon-hover" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="27498">
                      <path
                        d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                        p-id="27499"></path>
                    </svg>

                  </div>
                </li>
                <li class="fa-li-class" title="收藏">
                  <div class="fa-li-div" @click="collection">
                    <svg t="1700406279634"
                         :class="{'icon':!articleInfo.isCollection,'icon-hover':true,'icon-size-24':true,'mt-10':true,'icon-theme':articleInfo.isCollection}"
                         viewBox="0 0 1024 1024" version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="9570">
                      <path
                        d="M949.888 457.258667c26.069333-29.824 13.866667-67.52-24.789333-76.309334L681.728 325.546667l-127.786667-214.677334c-20.266667-34.069333-59.925333-34.090667-80.213333 0l-127.786667 214.677334-243.370666 55.381333c-38.442667 8.746667-50.858667 46.506667-24.789334 76.309333l164.394667 188.053334-22.613333 248.917333c-3.584 39.466667 28.458667 62.805333 64.896 47.146667l237.781333-102.037334a21.333333 21.333333 0 0 0-16.810667-39.210666L267.626667 902.186667c-6.698667 2.88-6.229333 3.221333-5.568-4.096l24.277333-267.093334-176.426667-201.813333c-4.757333-5.461333-4.906667-5.034667 2.133334-6.634667l261.205333-59.434666 137.152-230.4c3.733333-6.293333 3.136-6.293333 6.869333 0l137.173334 230.4 261.205333 59.434666c7.125333 1.621333 6.954667 1.088 2.133333 6.613334l-176.426666 201.813333 24.256 267.093333a21.333333 21.333333 0 1 0 42.496-3.84l-22.613334-248.917333 164.394667-188.053333z"
                        p-id="9571"></path>
                    </svg>
                  </div>
                </li>
                <li class="fa-li-class" title="分享">
                  <div class="fa-li-div" @click="copyLink">
                    <svg t="1700407528486" class="icon icon-size-24 mt-10 icon-hover" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="12183">
                      <path
                        d="M696 376c52.8 0 96-43.2 96-96s-43.2-96-96-96-96 43.2-96 96 43.2 96 96 96z m0-144c27.2 0 48 20.8 48 48s-20.8 48-48 48-48-20.8-48-48 20.8-48 48-48zM696 648c-30.4 0-57.6 14.4-75.2 36.8l-208-128c-11.2-6.4-25.6-3.2-33.6 8-6.4 11.2-3.2 25.6 8 33.6l208 128c1.6 1.6 4.8 1.6 6.4 3.2-1.6 4.8-1.6 11.2-1.6 17.6 0 52.8 43.2 96 96 96s96-43.2 96-96-43.2-99.2-96-99.2z m0 144c-27.2 0-48-20.8-48-48s20.8-48 48-48 48 20.8 48 48-20.8 48-48 48zM380.8 500.8l208-128c11.2-6.4 14.4-22.4 8-33.6-6.4-11.2-22.4-14.4-33.6-8l-204.8 126.4C340.8 432 312 416 280 416c-52.8 0-96 43.2-96 96s43.2 96 96 96 96-43.2 96-96v-9.6c1.6 0 3.2-1.6 4.8-1.6zM280 560c-27.2 0-48-20.8-48-48s20.8-48 48-48 48 20.8 48 48-20.8 48-48 48z"
                        p-id="12184"></path>
                    </svg>

                  </div>
                </li>
              </ul>
            </div>
          </div>
          <el-skeleton class="mt-10" :rows="8" animated v-if="loading"/>
          <div>
            <!--          <div class="article-content" v-if="!loading">-->
            <div class="article-content">
              <div class="mb-10">
                <h2 class="article-title">{{ articleInfo.articleTitle }}</h2>
                <div class="article-user-info-two">
                  <div class="ml-8 flex-space-between align-items-center">
                    <div>
                    <span class="font-s-14 mr-10">
                      {{ $utils.parseTime(articleInfo.createTime, '{y}年{m}月{d}日 {h}:{s}') }}
                    </span>
                      <svg t="1741407328107" class="icon svg-translateY-3 icon-size-16"
                           viewBox="0 0 1024 1024"
                           version="1.1"
                           xmlns="http://www.w3.org/2000/svg" p-id="30371">
                        <path
                          d="M512 153.6c-123.477333 0-235.178667 51.9168-323.089067 137.557333C117.230933 360.96 68.266667 448.938667 68.266667 505.173333v13.6704c0 56.234667 48.981333 144.196267 120.644266 214.016C276.804267 818.466133 388.522667 870.4 512 870.4c123.613867 0 235.246933-51.234133 322.7648-135.970133 71.799467-69.5296 120.490667-157.320533 120.968533-215.296V505.173333c-0.477867-58.2656-49.169067-146.056533-120.968533-215.586133C747.246933 204.8512 635.5968 153.6 512 153.6z m0 68.266667c104.6528 0 199.799467 43.6736 275.268267 116.753066 59.886933 57.992533 99.8912 130.133333 100.1984 166.843734v13.380266c-0.3072 36.420267-40.311467 108.544-100.181334 166.536534C711.7824 758.459733 616.635733 802.133333 512 802.133333c-104.3456 0-199.560533-44.2368-275.456-118.186666C176.810667 625.7664 136.533333 553.4208 136.533333 518.843733V505.173333c0-34.577067 40.277333-106.922667 100.010667-165.12C312.439467 266.120533 407.6544 221.866667 512 221.866667z"
                          p-id="30372"></path>
                        <path
                          d="M512 341.333333c-94.2592 0-170.666667 76.407467-170.666667 170.666667s76.407467 170.666667 170.666667 170.666667 170.666667-76.407467 170.666667-170.666667-76.407467-170.666667-170.666667-170.666667z m0 68.266667a102.4 102.4 0 1 1 0 204.8 102.4 102.4 0 0 1 0-204.8z"
                          p-id="30373"></path>
                      </svg>
                      <span class="font-s-14"> {{ articleInfo.numberTimes }}</span>
                    </div>
                    <div v-if="isOwn" @click="articleEdit(articleInfo.id)"
                         class="font-s-13 cursor-pointer ml-10 color-theme">编辑
                    </div>
                  </div>
                </div>
              </div>
              <div class="mt-20">
                <!--          ai总结  -->
                <div class="article-summary-dev" v-if="articleInfo.articleSummary!=null">
                  <div class="font-bold mb-6">
                    <svg t="1741333845009" class="icon-theme-stand-out icon-size-16 svg-translateY-1"
                         viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="19948">
                      <path
                        d="M503.737 598.24C456.683 725.285 393.161 842.918 313.17 951.141c-47.054 56.465-89.401 75.286-127.044 56.465-28.232-18.822-23.527-61.17 14.116-127.045 37.643-70.581 131.749-169.392 282.321-296.437-197.626-14.116-338.785-47.053-423.481-98.812-47.054-32.938-65.875-65.875-56.464-98.812 14.116-37.643 49.406-51.759 105.87-42.348 84.697 28.232 216.445 103.517 395.249 225.857-37.643-141.161-56.464-272.91-56.464-395.249 0-103.518 21.174-159.982 63.522-169.393 42.348 9.411 63.522 65.874 63.522 169.393-4.705 117.634-23.527 249.383-56.464 395.249 174.099-108.224 301.142-176.45 381.133-204.683 61.17-18.822 101.165-11.764 119.986 21.174 14.116 37.643-7.057 75.285-63.522 112.928-70.581 42.348-190.566 70.58-359.959 84.696-32.938 4.705-56.464 7.058-70.58 7.058 127.045 84.697 232.914 185.861 317.611 303.495 28.233 61.17 23.527 101.165-14.116 119.987-32.938 14.116-75.285-9.411-127.044-70.581-65.876-84.697-129.397-199.977-190.567-345.843L503.737 598.24z"
                        p-id="19949"></path>
                    </svg>
                    AI总结：
                  </div>
                  <p class="font-bold-300 color-grey"> {{ articleInfo.articleSummary }}</p>
                </div>
                <!--            内容 -->
                <!--                <mavon-editor class="markdown"-->
                <!--                              id="detailDirectory"-->
                <!--                              v-if="!loading"-->
                <!--                              :value="articleInfo.articleContent"-->
                <!--                              :subfield="false"-->
                <!--                              :defaultOpen="prop.defaultOpen"-->
                <!--                              :boxShadow="prop.boxShadow"-->
                <!--                              :toolbarsFlag="prop.toolbarsFlag"-->
                <!--                              :editable="prop.editable"-->
                <!--                              :codeStyle="articleInfo.theme"-->
                <!--                              fontSize="18px"-->
                <!--                              previewBackground="#fefefe"-->
                <!--                              :scrollStyle="prop.scrollStyle"-->
                <!--                              :navigation="false"-->
                <!--                              ref="markdown"-->
                <!--                />-->
                <vditor-preview :id="'articleVditor'" :content="articleInfo.articleContent"
                                :outline.sync="tocArray"></vditor-preview>
              </div>
              <div class="mb-20 mt-40">
                <div v-if="articleInfo.type==2">
                  转载地址：
                  <a :href="articleInfo.reprintUrl" target="_blank"
                     class="color-blue text-underline-hover">{{ articleInfo.reprintUrl }}</a>
                </div>
                <div v-if="articleInfo.type==3">
                  翻译地址：
                  <a :href="articleInfo.reprintUrl" target="_blank"
                     class="color-blue text-underline-hover">{{ articleInfo.reprintUrl }}</a>
                </div>
              </div>
              <!--            文章标签-->
              <div class="mt-20">
                <span class="label-top-cl"> 分类:</span>
                <span class="label-top-cl-list">
                    <nuxt-link target="_blank" :to="'/external_info/label-group-info?data='+articleInfo.groupingId">
                      {{ articleInfo.groupingName }}</nuxt-link>
                  </span>
                <span class="label-top-cl">标签:</span>
                <span class="label-top-cl-list" v-for="labelName of articleInfo.labelList"
                      :title="labelName.labelName">
                    <nuxt-link target="_blank"
                               :to="'/external_info/label-info?data='+labelName.id+'&type='+2">
                     #{{ labelName.labelName }}
                    </nuxt-link>
                  </span>
              </div>
              <hr class="hr-item mb-30 mt-20 mr-20"/>
              <!--   ============================= 评论开始  =============================   -->
              <div class="mt-20" id="comment">
                <p class="font-s-18 font-bold mb-20">
                  评论<span class="ml-6" v-text="commentTotal"></span>
                </p>
                <div class="flex-left">
                  <div style="width: 40px">
                    <el-avatar v-if="userInfo && userInfo.avatar" :size="35" :src="userInfo.avatar"></el-avatar>
                    <el-avatar v-else :size="35" src="/img/tx.jpg"></el-avatar>
                  </div>
                  <div class="comment-input-div overflow-hidden flex-1" v-if="radio=='2'">
                    <mavon-editor-module v-if="commentTextLoading" :mdContent.sync="comment.content"
                                         :mavonHeight="300">
                    </mavon-editor-module>
                    <el-button size="small" plain type="primary" class="mt-6 fl-right mr-6" :loading="buttonLoading"
                               :disabled="comment.content==null || comment.content==''"
                               @click="sendComment(articleInfo.id,articleInfo.articleTitle,comment.content,1,articleInfo.id,articleInfo.userId,1)">
                      评 论
                    </el-button>
                  </div>
                  <div v-else class="flex-12">
                    <div class="comment-import" v-if="commentTextLoading">
                  <textarea style="white-space:pre-line" id="articleComment" v-model="comment.content"
                            placeholder="请输入内容..."
                            rows="3" class="news-comment-cl"/>
                      <div class="overflow-hidden">
                        <emoji-module :content.sync="comment.content" :id="'articleComment'" :placement="'bottom-start'"
                                      class="fl-left"></emoji-module>
                        <el-button plain type="primary" class="fl-right" size="small"
                                   :disabled="comment.content==null ||comment.content==''"
                                   :loading="buttonLoading"
                                   @click="sendComment(articleInfo.id,articleInfo.articleTitle,comment.content,1,articleInfo.id,articleInfo.userId,1)">
                          评 论
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
                <div style="margin-left: 50px">
                  <el-radio-group v-model="radio" size="mini">
                    <el-radio-button label="1">文本编辑器</el-radio-button>
                    <el-radio-button label="2">Markdown</el-radio-button>
                  </el-radio-group>
                </div>

                <div v-if="articleComment.length!=0">
                  <div class="mt-20 font-bold">热门评论</div>
                  <!--                      一级评论-->
                  <div style="margin-top: 10px;padding: 10px">
                    <div v-for="(item,ineex) in articleComment" class="mb-20" :key="ineex">
                      <div class="font-s-16 flex-left">
                        <div class="mr-2">
                          <el-avatar v-if="item.commentAvatar" :size="35" :src="item.commentAvatar"></el-avatar>
                          <el-avatar v-else :size="35" src="/img/tx.jpg"></el-avatar>
                        </div>
                        <div class="flex-8">
                          <div class="flex-left align-items-center">
                            <nuxt-link class="ml-6 cursor-pointer hover-cl"
                                       :to="'/user_home/article?uuid='+$base64.encode(item.commentUid)"
                                       target="_blank">
                              {{ item.commentName }}
                            </nuxt-link>
                            <el-tag v-if="item.commentUid==articleInfo.userId" type="info" effect="plain" size="mini"
                                    class="ml-6 mr-10">作者
                            </el-tag>
                            <span class="color-grey-2 font-s-12"
                                  v-text=" $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                          </div>
                          <div class="content-div mt-10">
                            <mavon-editor class="markdown"
                                          previewBackground="#fefefe"
                                          :value="item.content"
                                          :subfield="false"
                                          :defaultOpen="prop.defaultOpen"
                                          :boxShadow="prop.boxShadow"
                                          :toolbarsFlag="prop.toolbarsFlag"
                                          :editable="prop.editable"
                                          :codeStyle="articleInfo.theme"
                                          :scrollStyle="prop.scrollStyle"
                                          :navigation="false"
                                          ref="markdown"
                            />
                          </div>
                          <div class="flex-left ml-10">
                            <div v-if="userInfo!=null&& userInfo.uuid==item.commentUid"
                                 class="font-s-12 color-grey-2 cursor-pointer hover-cl icon-hover"
                                 @click="deleteComment(item)">
                              <svg t="1701957752378" class="icon icon-size-14 icon-theme-1 svg-translateY-2 icon-hover"
                                   viewBox="0 0 1024 1024" version="1.1"
                                   xmlns="http://www.w3.org/2000/svg" p-id="3681">
                                <path
                                  d="M847.743 223.953H640.639c-3.132-68.921-60.177-124.029-129.858-124.029s-126.726 55.108-129.858 124.029H173.256c-17.673 0-32 14.327-32 32s14.327 32 32 32h674.487c17.673 0 32-14.327 32-32s-14.327-32-32-32z m-336.962-60.03c34.379 0 62.689 26.426 65.718 60.029H445.064c3.029-33.603 31.338-60.029 65.717-60.029zM767.743 351.79c-17.673 0-32 14.327-32 32v478.173H288.256V383.79c0-17.673-14.327-32-32-32s-32 14.327-32 32v510.173c0 17.673 14.327 32 32 32h511.487c17.673 0 32-14.327 32-32V383.79c0-17.673-14.327-32-32-32z"
                                  fill="" p-id="3682"></path>
                                <path
                                  d="M449.306 732.802V448.208c0-17.673-14.327-32-32-32s-32 14.327-32 32v284.593c0 17.673 14.327 32 32 32s32-14.326 32-31.999zM640.84 732.802V448.208c0-17.673-14.327-32-32-32s-32 14.327-32 32v284.593c0 17.673 14.327 32 32 32s32-14.326 32-31.999z"
                                  fill="" p-id="3683"></path>
                              </svg>
                              删除
                            </div>
                            <div class="flex-left flex-12 ml-10">
                              <el-collapse accordion style="width: 100%;">
                                <el-collapse-item>
                                  <template slot="title">
                                    <div class="hover-cl icon-hover font-s-12 color-grey-2 svg-translateY-5">
                                      <svg t="1701957280525"
                                           class="icon icon-size-14 icon-theme-1 svg-translateY-2 icon-hover"
                                           viewBox="0 0 1024 1024" version="1.1"
                                           xmlns="http://www.w3.org/2000/svg" p-id="2658">
                                        <path
                                          d="M201.1 913.6c-1.2 0-2.4 0-3.6-0.1-11.1-1-21.6-5.2-31.2-12.5-9.3-7.7-15.9-17.2-19.7-28.4-4-10.7-4.8-22.3-2.4-34.5l24.2-111.3c-24.7-31-43.8-64.3-56.8-98.8C95.3 586.2 87 542.7 87 498.6c0-104.8 44.9-202.7 126.5-275.9 38.8-35.1 83.9-62.7 134.2-81.9 52-19.9 107.2-30 164-30 112.3 0 218 39.8 297.7 112 39.6 35.7 70.8 77.3 92.5 123.7 22.6 48.1 34 99.3 34 152.2 0 52.8-11.4 104-34 152.1-21.8 46.5-52.9 88.1-92.5 123.6-38.7 35.2-83.8 62.8-134.1 82-51.9 19.9-106.9 29.9-163.6 29.9-33.8 0-67.9-3.8-101.6-11.4-28.8-6.2-55.6-14.9-79.7-25.7l-100.5 56.9c-9.5 4.9-19.2 7.5-28.8 7.5z m29.4-223.2c9.1 9 12.8 22 10 34.7l-22.8 105.5 94.9-53.9c5.2-2.8 10.9-4.3 16.6-4.3 5 0 9.7 1.1 14.1 3.2 26.5 12.9 53.9 22.4 81.4 28.3 27.4 6.2 56.6 9.4 87 9.4 95.5 0 185.3-33.4 252.7-94.1 31.9-28.6 57-62.1 74.5-99.4 18-38.5 27.2-79.3 27.2-121.3s-9.1-82.8-27.2-121.3c-17.5-37.3-42.6-70.9-74.5-99.7-67.7-60.7-157.4-94.1-252.7-94.1-95.5 0-185.4 33.4-253.1 94.1-31.8 28.9-56.9 62.4-74.5 99.7-18.2 38.6-27.5 79.5-27.5 121.3 0 34.7 6.5 68.9 19.2 101.8 12.9 33.1 31.2 63.4 54.7 90.1z m453.3-124.8c-13.2 0-26.2-5.7-36.5-16.1-9.7-10.2-15-23.6-15-37.8 0-14.1 5.3-27.6 14.9-38.1 10.1-10.2 23.1-15.8 36.6-15.8s26.5 5.6 36.5 15.7c9.7 10.6 15 24.1 15 38.2 0 14.3-5.3 27.7-15 37.8-10.3 10.4-23.2 16.1-36.5 16.1z m-172 0c-13.7 0-26.6-5.7-36.5-16.1-9.8-10.3-15.1-23.7-15.1-37.8 0-13.9 5.4-27.5 15.1-38.1 9.7-10.2 22.7-15.8 36.6-15.8 13.7 0 26.5 5.6 36.2 15.7 9.6 10.3 15 23.9 15 38.2 0 14.5-5.3 27.9-15 37.8-10 10.4-22.8 16.1-36.3 16.1z m-168.7 0c-13.4 0-26.6-5.9-36.3-16.1-9.6-9.9-14.9-23.3-14.9-37.8 0-14.3 5.3-27.8 14.9-38.1 9.7-10.2 22.6-15.8 36.4-15.8 13.6 0 26.5 5.6 36.4 15.7 9.8 10.5 15.1 24 15.1 38.2 0 14.3-5.4 27.8-15.1 37.8-10.2 10.4-23.2 16.1-36.5 16.1z"
                                          p-id="2659"></path>
                                      </svg>
                                      回复
                                    </div>
                                  </template>
                                  <div class="comment-input-div overflow-hidden" v-if="radio2=='2'">
                                    <mavon-editor-module v-if="commentTextLoading" :mdContent.sync="item.recoverContent"
                                                         :mavonHeight="300">
                                    </mavon-editor-module>
                                    <el-button size="small" plain type="primary" class="mt-10 fl-right mr-6"
                                               :loading="buttonLoading"
                                               :disabled="item.recoverContent==null || item.recoverContent==''"
                                               @click="sendComment(item.id,item.content,item.recoverContent,2,item.id,item.commentUid,2)">
                                      回 复
                                    </el-button>
                                  </div>
                                  <div class="comment-import" v-if="commentTextLoading && radio2=='1'">
                                <textarea style="white-space:pre-line" :id="'articleComment2'+ineex"
                                          v-model="item.recoverContent"
                                          placeholder="请输入内容..." rows="3" class="news-comment-cl"/>
                                    <div class="overflow-hidden">
                                      <emoji-module :content.sync="item.recoverContent" :id="'articleComment2'+ineex"
                                                    :placement="'bottom-start'" class="fl-left"></emoji-module>
                                      <el-button plain type="primary" class="fl-right" size="small"
                                                 :disabled="item.recoverContent==null || item.recoverContent==''"
                                                 :loading="buttonLoading"
                                                 @click="sendComment(item.id,item.content,item.recoverContent,2,item.id,item.commentUid,2)">
                                        回 复
                                      </el-button>
                                    </div>
                                  </div>
                                  <el-radio-group v-model="radio2" size="mini" class="ml-10">
                                    <el-radio-button label="1">文本编辑器</el-radio-button>
                                    <el-radio-button label="2">Markdown</el-radio-button>
                                  </el-radio-group>

                                </el-collapse-item>
                              </el-collapse>
                            </div>
                          </div>
                        </div>
                      </div>
                      <!--二级评论-->
                      <div v-if="item.mountComment.length!=0" class="comment-li">
                        <div v-for="(items,index2) in item.mountComment" class="mb-20" :key="index2">
                          <div class="flex-left">
                            <div class="mr-2">
                              <el-avatar v-if="items.commentAvatar" :size="30" :src="items.commentAvatar"></el-avatar>
                              <el-avatar v-else :size="30" src="/img/tx.jpg"></el-avatar>
                            </div>
                            <div class="flex-8">
                              <div class="flex-left align-items-center">
                                <div class="ml-6 mr-10">
                                  <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(items.commentUid)"
                                             target="_blank" class="cursor-pointer hover-cl">
                                    {{ items.commentName }}
                                  </nuxt-link>
                                  <el-tag v-if="items.commentUid==articleInfo.userId" type="info" effect="plain"
                                          size="mini" class="ml-2">作者
                                  </el-tag>
                                </div>
                                <div v-if="items.commentGrade==3" class="mr-10">
                                  <span class="color-grey-3 font-s-12 mr-4">回复</span>
                                  <nuxt-link class="cursor-pointer hover-cl"
                                             :to="'/user_home/article?uuid='+$base64.encode(items.targetUid)"
                                             target="_blank">
                                    {{ items.targetName }}
                                  </nuxt-link>
                                  <el-tag v-if="items.targetUid==articleInfo.userId" type="info" effect="plain"
                                          size="mini"
                                          class="ml-2">作者
                                  </el-tag>
                                </div>
                                <span class="color-grey-2 font-s-12"
                                      v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                              </div>
                              <!--                          内容-->
                              <div class="content-div mt-6">
                                <mavon-editor class="markdown"
                                              previewBackground="#fefefe"
                                              :value="items.content"
                                              :subfield="false"
                                              :defaultOpen="prop.defaultOpen"
                                              :boxShadow="prop.boxShadow"
                                              :toolbarsFlag="prop.toolbarsFlag"
                                              :editable="prop.editable"
                                              :codeStyle="articleInfo.theme"
                                              :scrollStyle="prop.scrollStyle"
                                              :navigation="false"
                                              ref="markdown"
                                />
                              </div>
                              <div class="flex-left ml-10">
                                <div class="font-s-12 hover-cl cursor-pointer color-grey-2"
                                     v-if="userInfo!=null && userInfo.uuid==items.commentUid"
                                     @click="deleteComment(items)">
                                  <svg t="1701957752378"
                                       class="icon icon-size-14 icon-theme-1 svg-translateY-3 icon-hover"
                                       viewBox="0 0 1024 1024" version="1.1"
                                       xmlns="http://www.w3.org/2000/svg" p-id="3681">
                                    <path
                                      d="M847.743 223.953H640.639c-3.132-68.921-60.177-124.029-129.858-124.029s-126.726 55.108-129.858 124.029H173.256c-17.673 0-32 14.327-32 32s14.327 32 32 32h674.487c17.673 0 32-14.327 32-32s-14.327-32-32-32z m-336.962-60.03c34.379 0 62.689 26.426 65.718 60.029H445.064c3.029-33.603 31.338-60.029 65.717-60.029zM767.743 351.79c-17.673 0-32 14.327-32 32v478.173H288.256V383.79c0-17.673-14.327-32-32-32s-32 14.327-32 32v510.173c0 17.673 14.327 32 32 32h511.487c17.673 0 32-14.327 32-32V383.79c0-17.673-14.327-32-32-32z"
                                      p-id="3682"></path>
                                    <path
                                      d="M449.306 732.802V448.208c0-17.673-14.327-32-32-32s-32 14.327-32 32v284.593c0 17.673 14.327 32 32 32s32-14.326 32-31.999zM640.84 732.802V448.208c0-17.673-14.327-32-32-32s-32 14.327-32 32v284.593c0 17.673 14.327 32 32 32s32-14.326 32-31.999z"
                                      p-id="3683"></path>
                                  </svg>
                                  删除
                                </div>
                                <div class="flex-12 flex-left ml-10">
                                  <el-collapse accordion style="width: 100%;">
                                    <el-collapse-item>
                                      <template slot="title">
                                        <div class="hover-cl font-s-12 color-grey-2">
                                          <svg t="1701957280525"
                                               class="icon icon-size-14 icon-theme-1 svg-translateY-3 icon-hover"
                                               viewBox="0 0 1024 1024" version="1.1"
                                               xmlns="http://www.w3.org/2000/svg" p-id="2658">
                                            <path
                                              d="M201.1 913.6c-1.2 0-2.4 0-3.6-0.1-11.1-1-21.6-5.2-31.2-12.5-9.3-7.7-15.9-17.2-19.7-28.4-4-10.7-4.8-22.3-2.4-34.5l24.2-111.3c-24.7-31-43.8-64.3-56.8-98.8C95.3 586.2 87 542.7 87 498.6c0-104.8 44.9-202.7 126.5-275.9 38.8-35.1 83.9-62.7 134.2-81.9 52-19.9 107.2-30 164-30 112.3 0 218 39.8 297.7 112 39.6 35.7 70.8 77.3 92.5 123.7 22.6 48.1 34 99.3 34 152.2 0 52.8-11.4 104-34 152.1-21.8 46.5-52.9 88.1-92.5 123.6-38.7 35.2-83.8 62.8-134.1 82-51.9 19.9-106.9 29.9-163.6 29.9-33.8 0-67.9-3.8-101.6-11.4-28.8-6.2-55.6-14.9-79.7-25.7l-100.5 56.9c-9.5 4.9-19.2 7.5-28.8 7.5z m29.4-223.2c9.1 9 12.8 22 10 34.7l-22.8 105.5 94.9-53.9c5.2-2.8 10.9-4.3 16.6-4.3 5 0 9.7 1.1 14.1 3.2 26.5 12.9 53.9 22.4 81.4 28.3 27.4 6.2 56.6 9.4 87 9.4 95.5 0 185.3-33.4 252.7-94.1 31.9-28.6 57-62.1 74.5-99.4 18-38.5 27.2-79.3 27.2-121.3s-9.1-82.8-27.2-121.3c-17.5-37.3-42.6-70.9-74.5-99.7-67.7-60.7-157.4-94.1-252.7-94.1-95.5 0-185.4 33.4-253.1 94.1-31.8 28.9-56.9 62.4-74.5 99.7-18.2 38.6-27.5 79.5-27.5 121.3 0 34.7 6.5 68.9 19.2 101.8 12.9 33.1 31.2 63.4 54.7 90.1z m453.3-124.8c-13.2 0-26.2-5.7-36.5-16.1-9.7-10.2-15-23.6-15-37.8 0-14.1 5.3-27.6 14.9-38.1 10.1-10.2 23.1-15.8 36.6-15.8s26.5 5.6 36.5 15.7c9.7 10.6 15 24.1 15 38.2 0 14.3-5.3 27.7-15 37.8-10.3 10.4-23.2 16.1-36.5 16.1z m-172 0c-13.7 0-26.6-5.7-36.5-16.1-9.8-10.3-15.1-23.7-15.1-37.8 0-13.9 5.4-27.5 15.1-38.1 9.7-10.2 22.7-15.8 36.6-15.8 13.7 0 26.5 5.6 36.2 15.7 9.6 10.3 15 23.9 15 38.2 0 14.5-5.3 27.9-15 37.8-10 10.4-22.8 16.1-36.3 16.1z m-168.7 0c-13.4 0-26.6-5.9-36.3-16.1-9.6-9.9-14.9-23.3-14.9-37.8 0-14.3 5.3-27.8 14.9-38.1 9.7-10.2 22.6-15.8 36.4-15.8 13.6 0 26.5 5.6 36.4 15.7 9.8 10.5 15.1 24 15.1 38.2 0 14.3-5.4 27.8-15.1 37.8-10.2 10.4-23.2 16.1-36.5 16.1z"
                                              p-id="2659"></path>
                                          </svg>
                                          回复
                                        </div>
                                      </template>
                                      <div>
                                        <div class="comment-input-div overflow-hidden" v-if="radio3=='2'">
                                          <mavon-editor-module v-if="commentTextLoading"
                                                               :mdContent.sync="items.recoverContent"
                                                               :mavonHeight="300">
                                          </mavon-editor-module>
                                          <el-button size="small" type="primary" plain class="mt-10 fl-right mr-6"
                                                     :loading="buttonLoading"
                                                     :disabled="items.recoverContent==null || items.recoverContent==''"
                                                     @click="sendComment(item.id,item.content,items.recoverContent,3,items.id,items.commentUid,2)">
                                            回 复
                                          </el-button>
                                        </div>

                                        <div class="comment-import" v-if="commentTextLoading && radio3=='1'">
                                      <textarea style="white-space:pre-line" :id="'articleComment3'+index2"
                                                v-model="items.recoverContent"
                                                placeholder="请输入内容..." rows="3" class="news-comment-cl"/>
                                          <div class="overflow-hidden">
                                            <emoji-module :content.sync="items.recoverContent"
                                                          :id="'articleComment3'+index2"
                                                          :placement="'bottom-start'" class="fl-left"></emoji-module>
                                            <el-button plain type="primary" class="fl-right" size="small"
                                                       :disabled="items.recoverContent==null || items.recoverContent==''"
                                                       :loading="buttonLoading"
                                                       @click="sendComment(item.id,item.content,items.recoverContent,3,items.id,items.commentUid,2)">
                                              回 复
                                            </el-button>
                                          </div>
                                        </div>
                                        <el-radio-group v-model="radio3" size="mini" class="ml-10">
                                          <el-radio-button label="1">文本编辑器</el-radio-button>
                                          <el-radio-button label="2">Markdown</el-radio-button>
                                        </el-radio-group>
                                      </div>
                                    </el-collapse-item>
                                  </el-collapse>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div v-if="articleComment.length>commentTotal" class="text-center font-s-14 color-grey hover-cl">
                      <span class="cursor-pointer"> 查看更多
                      <i class="fa fa-angle-double-down" aria-hidden="true"></i></span>
                    </div>
                  </div>
                </div>
                <div v-else class="text-center mt-20 font-s-14 color-grey-3">
                  快来抢占沙发~
                </div>
              </div>
              <!--   ============================= 评论结束  =============================   -->
            </div>
          </div>
          <!--        右侧边栏-->
          <div class="right-content-width">
            <div :class="{'right-content-div':true,'right-content-width':true,
           'index-right-cl-true':goTopLoading,'index-right-cl-false':!goTopLoading}">
              <!--               作者简介-->
              <div class="auroora-card mb-10" v-if="!goTopLoading" style="padding: 20px 20px 10px 20px">
                <div class="flex-left">
                  <div>
                    <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(articleInfo.userId)" target="_blank">
                      <el-avatar :size="40" v-if="articleInfo.avatar" :src="articleInfo.avatar"/>
                      <el-avatar :size="40" v-else src="/img/tx.jpg"></el-avatar>
                    </nuxt-link>
                  </div>
                  <div class="ml-8">
                    <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(articleInfo.userId)" target="_blank">
                      <div class="cursor-pointer font-bold mb-4 overflow-nowrap-1" style="width: 100%;"
                           :title="articleInfo.nickname">
                        {{ articleInfo.nickname }}
                      </div>
                    </nuxt-link>
                    <div class="color-grey-2 font-s-13 mt-6"
                         v-text="articleInfo.occupation==null?'职业-~-':articleInfo.occupation"></div>
                  </div>
                </div>
                <div class="mt-8">
                  <el-button :disabled="isOwn" size="small" :loading="articleInfo.buttonLoading" type="primary"
                             @click="followClick(articleInfo)">{{ articleInfo.isFollow ? '已关注' : '关注Ta' }}
                  </el-button>
                  <el-button :disabled="isOwn" size="small" @click="privateLetter(articleInfo.userId)">私信
                  </el-button>
                </div>
                <!--              <hr class="hr-item mb-15 mt-15"/>-->
                <div class="font-s-14 mt-15">
                  <div class="mb-12">
                    <svg t="1700376482283" class="icon icon-size-18 svg-translateY-3 icon-hover" viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="2350">
                      <path
                        d="M512 85.333333c129.6 0 234.666667 105.066667 234.666667 234.666667 0 84.256-44.394667 158.133333-111.072 199.52a425.28 425.28 0 0 1 152.853333 83.466667 32 32 0 1 1-41.493333 48.736A361.045333 361.045333 0 0 0 512 565.333333c-188.672 0-345.429333 144.672-361.344 331.413334a32 32 0 0 1-63.765333-5.429334c15.114667-177.322667 138.048-322.346667 301.546666-371.786666C321.76 478.165333 277.333333 404.266667 277.333333 320c0-129.6 105.066667-234.666667 234.666667-234.666667z m415.946667 627.381334l1.066666 1.013333a29.824 29.824 0 0 1 0 43.413333l-162.261333 152.96a31.925333 31.925333 0 0 1-22.762667 8.704 31.925333 31.925333 0 0 1-22.773333-8.704l-93.184-87.84a29.824 29.824 0 0 1 0-43.413333l1.077333-1.013333a32 32 0 0 1 43.904 0l70.976 66.901333 140.053334-132.021333a32 32 0 0 1 43.904 0zM512 149.333333c-94.261333 0-170.666667 76.405333-170.666667 170.666667s76.405333 170.666667 170.666667 170.666667 170.666667-76.405333 170.666667-170.666667-76.405333-170.666667-170.666667-170.666667z"
                        p-id="2351"></path>
                    </svg>
                    <span class="user-count-cl">
                            关注Ta<span v-text="articleInfo.fansFollowCount" class="ml-10 color-grey"/></span>
                  </div>
                  <div class="mb-12">
                    <svg t="1700377690970" class="icon icon-size-18 svg-translateY-3 icon-hover color-grey"
                         viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="5557">
                      <path d="M506 894.2a33 32.9 0 1 0 66 0 33 32.9 0 1 0-66 0Z" fill="#4D4D4D" p-id="5558"></path>
                      <path
                        d="M864 388.7v-4.4c0-70.7-57.3-128-128-128H511.8l-82.5-110.9c-7.4-12.9-18-16.2-27.3-16l-0.1-0.1H192.1c-70.7 0-128 57.3-128 128v542.1c0 70.7 57.3 128 128 128H407v-0.4c18.2 0 33-14.7 33-32.9s-14.8-32.9-33-32.9c-1 0-2.1 0.1-3.1 0.1h-181c-35.3 0-64-28.7-64-64 0-5.5 0.7-10.9 2-16L234 498.9l0.2-0.1c6.7-28.1 31.9-49 62.1-49.1l0.2-0.1h532.2c1.3-0.1 2.5-0.1 3.8-0.1 35.3 0 64 28.7 64 64 0 6.7-1.1 13.3-3 19.4v0.1L821 812.8c-0.1 0.6-0.3 1.1-0.4 1.7l-1.5 5.8-0.5 0.4c-1.5 3.9-3.4 7.5-5.5 11h-1.3c-2.6 4.7-5.8 9.1-9.5 12.9-11.4 10.6-26.7 17.1-43.4 17.1-1.3 0-2.6 0-3.8-0.1h-80.8c-1-0.1-2.1-0.1-3.1-0.1-18.2 0-33 14.7-33 32.9s14.8 32.9 33 32.9v0.2H763l0.5-0.4c59.1-0.2 108.7-40.4 123.2-95l0.2-0.2 67.8-285.5c2.9-10.8 4.5-22.1 4.5-33.8-0.1-59.5-40.5-109.5-95.2-123.9z m-571.5-4.9c-62 0-113.7 44.2-125.5 102.7l-0.5 0.4-38 160.3V257c0-35.3 28.7-64 64-64H383l82.7 111.3c6.6 11.4 19.2 17.2 31.5 15.8h238.5c35.2 0 63.8 28.5 64 63.7H292.5z"
                        p-id="5559"></path>
                    </svg>
                    <nuxt-link class="user-count-cl" :to="'/user_home/special?uuid='+$base64.encode(articleInfo.userId)"
                               target="_blank">
                      专栏数<span v-text="articleInfo.specialColumnCount" class="ml-10 color-grey"/>
                    </nuxt-link>
                  </div>
                  <div class="mb-12">
                    <svg t="1700377727720" class="icon icon-size-20 svg-translateY-6 icon-hover color-grey"
                         viewBox="0 0 1024 1024"
                         version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6569">
                      <path
                        d="M288 416h192c17.67 0 32-14.33 32-32s-14.33-32-32-32H288c-17.67 0-32 14.33-32 32s14.33 32 32 32zM288 576h352c17.69 0 32-14.31 32-32s-14.31-32-32-32H288c-17.67 0-32 14.31-32 32s14.33 32 32 32zM480 672H288c-17.67 0-32 14.31-32 32s14.33 32 32 32h192c17.67 0 32-14.31 32-32s-14.33-32-32-32zM939.98 645.16L826.84 532.02c-6.25-6.25-14.44-9.37-22.63-9.37s-16.38 3.12-22.63 9.37L553.37 760.24c-6 6-9.37 14.14-9.37 22.63V896c0 17.67 14.33 32 32 32h113.14c8.49 0 16.63-3.37 22.63-9.37l228.21-228.21c12.49-12.5 12.49-32.76 0-45.26zM675.88 864H608v-67.88L804.21 599.9l67.88 67.88L675.88 864z"
                        p-id="6570"></path>
                      <path
                        d="M448 864H192V160h383.86l0.11 128.09c0.06 35.23 28.78 63.91 64 63.91H768v80c0 17.67 14.33 32 32 32s32-14.33 32-32V274.87c0-8.58-3.45-16.8-9.56-22.82L673.09 105.18A32.002 32.002 0 0 0 650.66 96H160c-17.67 0-32 14.33-32 32v768c0 17.67 14.33 32 32 32h288c17.67 0 32-14.33 32-32s-14.33-32-32-32z m319.72-576H639.97l-0.1-125.73L767.72 288z"
                        p-id="6571"></path>
                    </svg>
                    <nuxt-link class="user-count-cl" :to="'/user_home/article?uuid='+$base64.encode(articleInfo.userId)"
                               target="_blank">
                      创作的文章<span v-text="articleInfo.articleCount" class="ml-10 color-grey"/>
                    </nuxt-link>
                  </div>
                </div>
              </div>
              <!--               文章目录-->
              <div class="auroora-card" v-if="tocArray.length>0">
                <div class="ml-6 font-bold border-left-2-solid">文章目录</div>
                <hr class="hr-item mb-15 mt-15"/>
                <!-- 遍历目录 -->
                <ul class="article-catalogue">
                  <el-timeline>
                    <el-timeline-item v-for="(item, index) in tocArray" :key="index" size="normal"
                                      :type="highlightType()==index?'primary':''">
                    <span @click="gotoAnchor(item.pos)" class="font-s-14 line-height-18">
                    {{ item.text }}
                    </span>
                    </el-timeline-item>
                  </el-timeline>
                </ul>
              </div>
              <!--                相关文章-->
              <div class="auroora-card mt-10">
                <span class="ml-6 font-bold border-left-2-solid">相关文章</span>
                <el-divider></el-divider>
                <el-skeleton class="mt-10" :rows="3" animated v-if="loading"/>
                <div v-show="!loading">
                  <div v-for="item of listInformationList" :key="item.id"
                       class="ml-6 mr-6 cursor-pointer featured-articles">
                    <nuxt-link :to="`/article/article-details/`+$base64.encode(item.id)" target="_blank" rel="noopener">
                      <h4>
                        {{ item.articleTitle }}
                      </h4>
                    </nuxt-link>
                  </div>
                  <div v-if="listInformationList.length==0" class="color-grey-2 font-s-13 text-center">
                    暂无文章
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <el-dialog
        title="收藏夹"
        :visible.sync="collectionLoading"
        width="30%">
        <el-dialog title="创建收藏夹" :visible.sync="createCoDialog" width="400px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="收藏夹名称:" prop="collectionName">
              <el-input maxlength="20" show-word-limit v-model="form.collectionName" placeholder="收藏夹名称"/>
            </el-form-item>
            <el-form-item label="简介:">
              <el-input type="textarea" :rows="4" maxlength="200" show-word-limit v-model="form.collectionIntroduce"
                        placeholder="收藏夹简介"/>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="createCoDialog = false">取 消</el-button>
            <el-button :loading="buttonLoading" type="primary" @click="createCollecting">创 建</el-button>
          </div>
        </el-dialog>
        <div v-if="collectionList.length==0" style="text-align: center">
          您还未创建收藏夹!
        </div>
        <div v-for="item of collectionList" class="mt-15">
          <el-radio v-model="collectionParams.collectionId" :label="item.id" border size="medium" style="width: 100%">
            {{ item.collectionIntroduce }}
          </el-radio>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" icon="el-icon-plus"
                     @click="createCoDialog = true" class="fl-left">新建收藏夹
          </el-button>
          <el-button type="primary" :loading="buttonCoLoading" @click="addCollection()">确 定</el-button>
        </div>
      </el-dialog>
      <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
    </div>
  </div>
</template>

<script>
import VditorPreview from "../../../components/Vditor-preview.vue";

export default {
  name: "articleId",
  components: {VditorPreview},
  head() {
    return {
      title: `${this.articleInfo.articleTitle == undefined ? process.env.PROJECT_NAME : this.articleInfo.articleTitle + ' - ' + process.env.PROJECT_NAME}`,
      meta: [
        {
          hid: 'description',
          name: 'description',
          content: this.articleInfo.articleAbstract || this.articleInfo.articleTitle
        },
        {hid: 'keywords', name: 'keywords', content: this.articleInfo.articleTitle || this.articleInfo.articleAbstract}
      ]
    }
  },
  computed: {
    prop() {
      let data = {
        subfield: false,// 单双栏模式
        defaultOpen: 'preview',//edit： 默认展示编辑区域 ， preview： 默认展示预览区域
        editable: false,
        toolbarsFlag: false,
        scrollStyle: true,
        boxShadow: false,  //边框阴影
      }
      return data
    },
  },
  data() {
    return {
      articleInfo: {},
      //文章目录
      tocArray: [],
      id: null,
      queryParams: {
        pageNum: 0,
        pageSize: 10,
        articleTitle: null,
        id: null,
      },
      userInfo: {
        uuid: null,
        avatar: null,
      },
      listInformationList: [],
      isOwn: false,
      isFabulous: false,
      loginDialog: false,
      loading: true,
      goTopLoading: false,
      scrollTops: 0,
      collectionList: [],
      collectionLoading: false,
      createCoDialog: false,
      form: {
        collectionName: undefined,
        collectionIntroduce: undefined,
      },
      rules: {
        collectionName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
      },
      buttonLoading: false,
      collectionParams: {
        collectionId: null,
        targetId: null,
        type: null,
      },
      //评论相关
      buttonCoLoading: false,
      articleComment: [],
      comment: {
        articleId: null,
        uid: null,
        parentId: null,
        commentGrade: null,
        targetId: null,
        targetUid: null,
        type: null,
        content: '',
        contentTwo: '',
        worksContent: '',
      },
      commentTextLoading: true,
      commentTotal: 0,
      //评论编辑器切换
      radio: '1',
      radio2: '1',
      radio3: '1',
    }
  },
  async asyncData({app, params, store}) {
    const id = Buffer.from(params.id, 'base64').toString('utf-8');
    let token = store.state.token;
    const https = require('https');
    const response = await fetch(`${process.env.SERVICE_PROTOCOL}${process.env.SERVER_URL}/white/article/details/${id}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      },
      //不做https校验，如果你的https是被信任的建议注释该代码，因为http是不安全的
      agent: new https.Agent({rejectUnauthorized: false})
    });
    const data = await response.json();
    let articleInfo = data.data;
    let queryParams = {
      pageNum: 0,
      pageSize: 10,
      articleTitle: null,
      id: null,
    };
    queryParams.articleTitle = articleInfo.articleTitle;
    queryParams.id = articleInfo.id;
    return {
      articleInfo: articleInfo,
      queryParams: queryParams,
      id: id,
    }
  },
  methods: {
    //mavon-editor 编辑器 目录生成
    generateDirectory() {
      //客户端执行
      this.$nextTick(() => {
        let tocTags = ["H1", "H2", "H3", "H4"];//筛选目录
        let tocArray = [];
        let element = document.getElementById("detailDirectory");
        let childNodes = element.childNodes[2].childNodes[2].childNodes[0].childNodes;
        childNodes.forEach(item => {
          let tagName = item.tagName;
          //是否包含 tocTags存在的标签
          if (tagName == undefined || !tocTags.includes(tagName.toUpperCase())) {
            return true;
          }
          let elementsByTagName = item.getElementsByTagName("a");
          let id = elementsByTagName[0].getAttribute("id");
          let offsetTop = document.getElementById(id).offsetTop;
          tocArray.push({
            id: id,
            text: item.innerText,
            level: Number.parseInt(tagName.substring(1)),
            pos: offsetTop,
          })
        });
        this.tocArray = tocArray;
      });
    },
    highlightType() {
      let scrollTops = this.scrollTops + 1;
      if (scrollTops <= this.tocArray[0].pos) {
        return 0;
      }
      if (this.tocArray[this.tocArray.length - 1].pos <= scrollTops) {
        return this.tocArray.length - 1;
      }
      for (let i = 0; i < this.tocArray.length; i++) {
        if (this.tocArray[i].pos <= scrollTops
          && this.tocArray[i + 1] != null && scrollTops < this.tocArray[i + 1].pos) {
          return i;
        }
      }
    },
    privateLetter(uuid) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      //添加私信记录
      this.$API("/frontDesk/private/user/add/" + uuid, "get");
      let routeInfo = this.$router.resolve({
        path: "/news/private-letter",
        query: {code: this.$base64.encode(uuid),}
      });
      window.open(routeInfo.href, '_blank');
    },
    articleEdit(id) {
      this.$router.push({
        path: '/article/publish-article',
        query: {id: this.$base64.encode(id)}
      })
    },
    deleteComment(item) {
      this.$modal.confirm('确定要删除当前评论吗？').then(() => {
        this.$API('/article/comment', 'delete', {
          id: item.id,
          articleId: item.articleId,
          uid: item.uid,
          commentUid: item.commentUid,
        }).then(res => {
          if (res.code == 200) {
            this.articleCommentLists();
            this.$modal.msgSuccess("删除成功");
          }
        })
      });
    },

    sendComment(parentId, worksContent, commentContent, commentGrade, targetId, targetUid, type) {
      this.buttonLoading = true;
      this.comment.articleId = this.articleInfo.id;
      this.comment.uid = this.articleInfo.userId;
      this.comment.worksContent = worksContent;
      this.comment.content = commentContent;
      this.comment.parentId = parentId;
      this.comment.commentGrade = commentGrade;
      this.comment.targetId = targetId;
      this.comment.targetUid = targetUid;
      this.comment.type = type;
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        this.buttonLoading = false;
        return;
      }
      this.$API('/article/comment/insert', this.$post(), null, this.comment).then(res => {
        if (res.code === 200) {
          this.commentTextLoading = false;
          this.$modal.msg("评论成功！");
          this.articleCommentLists()
        }
      }).finally(() => {
        this.comment.content = null;
        this.buttonLoading = false;
        this.commentTextLoading = true;
      })
    },
    goComment() {
      let dom = document.querySelector("#comment");
      dom.scrollIntoView({
        /*平滑滚动*/
        behavior: "smooth",
        block: "start",
      })
    },
    copyLink() {
      const currentURL = window.location.origin + this.$nuxt.$route.fullPath;
      this.copyToClip(currentURL)
      this.$modal.notifySuccess("链接地址已复制！")
    },
    copyToClip(content) {
      let aux = document.createElement("input");
      aux.setAttribute("value", content);
      document.body.appendChild(aux);
      aux.select();
      document.execCommand("copy");
      document.body.removeChild(aux);
    },
    addCollection() {
      if (this.collectionParams.collectionId == null) {
        return this.$modal.msgWarning("请选择收藏夹！");
      }
      this.buttonCoLoading = true;
      this.collectionParams.targetId = this.articleInfo.id;
      this.collectionParams.type = 1;
      this.collectionParams.labelId = this.articleInfo.labelId;
      this.$API('/frontDesk/add/collection/data', this.$post(), null, this.collectionParams).then(res => {
        if (res.code == 200) {
          this.$modal.notifySuccess("收藏成功！");
          this.articleInfo.isCollection = true;
          this.articleDetailsInfoTwo();
        } else {
          this.$modal.notifyError("收藏失败！");
        }
      }).finally(() => {
        this.buttonCoLoading = false;
        this.collectionLoading = false;
      })
    },
    createCollecting() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.$API('/frontDesk/add/collection', this.$post(), null, this.form).then(() => {
            this.$modal.msgSuccess("创建成功");
            this.$API(`/white/collection/list/${this.userInfo.uuid}`, this.$get()).then(res => {
              this.collectionList = res.data;
            })
          }).finally(() => {
            this.buttonLoading = false;
            this.createCoDialog = false;
          });
        }
      });
    },
    collection() {
      if (this.userInfo == null) {
        this.loginDialog = true;
        return;
      }
      if (this.articleInfo.isCollection) {
        this.$API(`/frontDesk/delete/collection/data/${this.articleInfo.collectionRId}/${this.articleInfo.labelId}`, this.$get()).then(res => {
          if (res.code == 200) {
            this.articleInfo.isCollection = false;
            this.$modal.notifySuccess("已取消收藏！");
          }
        })
        return;
      }
      this.$API(`/white/collection/list/${this.userInfo.uuid}`, this.$get()).then(res => {
        this.collectionList = res.data;
        this.collectionLoading = true;
      })
    },

    spotFabulousFormula(typeId, targetId, targetUid, fabulousSum, targetTitle, labelId) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      let data = {
        typeId: typeId,
        targetId: targetId,
        targetUid: targetUid,
        type: 1,
        state: 0,
        fabulousSum: fabulousSum,
        targetTitle: targetTitle,
        labelId: labelId,
      };
      if (this.isFabulous) {
        this.isFabulous = false;
        this.articleInfo.likeTimes = this.articleInfo.likeTimes - 1;
        this.$API('/frontDesk/fabulous/cancel', this.$post(), null, data);
      } else {
        this.isFabulous = true;
        this.articleInfo.likeTimes = this.articleInfo.likeTimes + 1;
        this.$API('/frontDesk/fabulous/spot', this.$post(), null, data);
      }
    },

    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    followClick(item) {
      if (this.userInfo == null || this.userInfo.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      item.buttonLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API('/user/follow/cancel', this.$post(), null, {targetId: item.userId, type: 1,})
          .finally(() => {
            this.getBasicsUsers()
            item.buttonLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API('/user/follow/add', this.$post(), null, {targetId: item.userId, type: 1,})
          .finally(() => {
            this.getBasicsUsers()
            item.buttonLoading = false;
          });
      }
    },
    gotoAnchor(pos) {
      window.scrollTo({
        top: pos,
        behavior: "smooth"  // 平滑滚动
      });
    },
    handleScroll() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      if (scrollTop > (clientHeight / 2)) {
        this.goTopLoading = true;
      } else {
        this.goTopLoading = false;
      }
      this.scrollTops = scrollTop;
      this.highlightType();
    },
    articleDetailsInfoTwo() {
      this.id = this.$base64.decode(this.$route.params.id);
      this.$API('/white/article/details/' + this.id, this.$get()).then(res => {
        this.articleInfo = res.data;
        this.queryParams.articleTitle = this.articleInfo.articleTitle;
        this.queryParams.id = this.articleInfo.id;
        //获取用户基本信息
        this.getBasicsUsers();
      });
    },
    articleDetailsInfo() {
      //获取用户基本信息
      this.getBasicsUsers();
      this.loading = false;
      // this.generateDirectory();
      this.$API(`/white/article/add/browse-count/${this.articleInfo.id}/${this.articleInfo.labelId}`);
      //获取相关文章
      this.$API('/white/article/related/list', this.$get(), this.queryParams).then(res => {
        this.listInformationList = res;
        //  记录用户浏览历史
        this.$API('/frontDesk/browsing/history', this.$post(), null, {
          targetId: this.articleInfo.id,
          targetUid: this.articleInfo.userId,
          targetTitle: this.articleInfo.articleTitle,
          targetType: 1,
        });
        this.articleCommentLists();
      });
    },
    articleCommentLists() {
      let query = {
        articleId: this.articleInfo.id
      }
      this.$API('/white/article/comment/list', this.$get(), query).then(res => {
        this.articleComment = res.data;
        if (this.articleComment.length > 0) {
          this.commentTotal = this.articleComment[0].commentTotal;
        }
      })
    },
    getBasicsUsers() {
      this.$API('/front-desk/user/basics', this.$get(), null, null).then(res => {
        if (res != null) {
          this.userInfo = res.data;
          if (res.data != null && res.data.uuid === this.articleInfo.userId) {
            this.isOwn = true;
          } else {
            this.isOwn = false;
          }
        }
        if (this.articleInfo.fabulousUserSet == null) {
          return;
        }
        this.articleInfo.fabulousUserSet.forEach(item => {
          if (item == this.userInfo.uuid) {
            this.isFabulous = true;
          }
        });
      });
    },
  },

  mounted() {
    this.articleDetailsInfo();
    window.addEventListener('scroll', this.handleScroll, true);// 向页面添加滚动事件
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.handleScroll, true)
  },
}
</script>

<style>
@import url("static/css/server/pc/article/article-details.css");

.el-timeline-item {
  padding-bottom: 14px;
}

.el-timeline-item__wrapper {
  padding-left: 24px;
}

.el-timeline-item__wrapper :hover {
  color: var(--theme-color);
  cursor: pointer;
}

</style>
