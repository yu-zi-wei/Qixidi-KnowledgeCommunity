<template>
  <div>
    <div class="mt-15 page-content-width">
      <div class="flex-space-between flex-wrap-wrap">
        <div class="left-content-width">
          <div class="left-content-ul left-content-width">
            <ul class="left-content-auto">
              <li class="fa-li-class" title="申请友链">
                <div class="fa-li-div" @click="goComment">
                  <svg t="1726563114935" class="icon icon-size-24 mt-10 icon-hover" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="11360">
                    <path
                      d="M481.792 321.536c-13.312 13.312-35.328 13.312-48.64 0-13.312-13.312-13.312-35.328 0-48.64L578.56 127.488c43.52-43.008 100.352-64.512 157.184-64.512 122.368 0 220.672 98.816 220.672 221.696 0 56.832-21.504 113.664-64.512 156.672L735.232 598.016l-2.048 2.048c-43.008 41.984-99.328 62.976-155.136 62.976-78.336 0-150.016-41.472-189.952-107.52v-0.512c-9.728-15.872-16.896-32.768-22.528-50.176-5.12-17.92 5.12-37.376 23.04-42.496 18.432-5.12 37.376 5.12 43.008 23.04 3.584 11.776 8.704 23.552 15.36 34.816 27.136 46.08 77.824 74.752 131.072 74.752 38.912 0 77.824-14.848 107.52-43.52l1.536-1.536 156.672-156.672c29.696-29.696 44.544-69.12 44.544-108.032 0-84.992-68.096-152.576-152.576-152.576-38.912 0-78.336 14.848-108.032 44.544h-0.512L481.792 321.536z m56.832 375.296L393.216 842.24c-58.88 58.88-157.696 59.392-216.576 0-58.88-58.88-58.88-157.184 0-216.064l156.672-157.184c29.696-29.184 68.608-44.032 108.544-44.032s78.336 14.848 108.544 44.032v0.512c9.216 9.216 16.384 18.944 23.04 29.696 6.656 10.752 11.776 22.528 15.36 34.304 5.12 18.432 24.576 28.672 42.496 23.552 17.92-5.12 28.672-24.576 23.552-42.496-5.632-17.408-12.8-34.304-21.504-50.176-9.728-15.872-20.992-30.208-33.792-43.52-43.52-43.008-99.84-64.512-157.184-64.512-56.832 0-113.664 21.504-157.184 64.512L128 578.048c-85.504 85.504-85.504 227.84 0 313.856l2.56 1.536c86.016 83.456 225.28 82.944 310.784-1.024v-0.512l0.512-0.512 145.92-145.408c13.312-13.824 13.312-35.328-0.512-48.64-13.312-13.312-35.328-13.312-48.64-0.512z m290.816-13.824h30.72v256h-30.72z"
                      p-id="11361"></path>
                    <path d="M716.8 795.648h256v30.72h-256z" p-id="11362"></path>
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
        <div class="article-content-introduce" v-if="!loading">
          <div class="mb-20 flex-space-between">
            <div>
              <h3 class="article-title color-theme">
                <svg t="1726580146454" class="icon icon-size-26 svg-translateY-4 icon-theme" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="11849">
                  <path
                    d="M493.72583 514.27417C424.671007 445.219348 420.544805 338.553608 482.674524 276.423889L644.423891 114.674524C706.583821 52.514594 813.268826 56.720483 882.27417 125.72583 951.230726 194.68239 955.513645 301.387939 893.927181 362.974404L780.45079 476.450794C767.954016 488.947571 767.954016 509.20885 780.45079 521.705628 792.947571 534.202405 813.208851 534.202405 825.705626 521.705628L939.182016 408.229238C1026.609952 320.8013 1020.703456 173.645445 927.529005 80.470996 834.292646-12.765362 687.154784-18.566041 599.169055 69.41969L437.41969 231.169055C349.480384 319.108361 355.171544 466.229551 448.470996 559.529004 460.967773 572.025781 481.229053 572.025781 493.72583 559.529004 506.222607 547.032227 506.222607 526.770947 493.72583 514.27417L493.72583 514.27417Z"
                    p-id="11850"></path>
                  <path
                    d="M559.607503 443.059164C649.311059 532.762721 686.000877 634.90071 637.325476 683.576109L411.576111 909.325478C349.416181 971.485408 242.731178 967.27952 173.72583 898.27417 104.76927 829.317613 100.486352 722.612064 162.072817 661.025594L292.635347 530.463066C305.132125 517.966288 305.132125 497.705009 292.635347 485.208232 280.13857 472.711454 259.877291 472.711454 247.380513 485.208232L116.817983 615.770762C29.390045 703.198701 35.296547 850.354554 128.470996 943.529005 221.707354 1036.765363 368.845214 1042.566042 456.830945 954.580307L682.580307 728.830944C765.53168 645.879571 716.532512 509.474501 604.862337 397.80433 592.36556 385.307552 572.104281 385.307552 559.607503 397.80433 547.110726 410.301107 547.110726 430.562386 559.607503 443.059164L559.607503 443.059164Z"
                    p-id="11851"></path>
                </svg>
                {{ articleInfo.articleTitle }}
                (<span class="font-bold">{{ friendLinkList.length }}</span>)
              </h3>
            </div>
            <div>
            <span v-if="isOwn" @click="articleEdit(articleInfo.id)"
                  class="font-s-13 cursor-pointer color-theme mr-20">编辑</span>
            </div>
          </div>
          <hr class="hr-item mb-20 mt-10"/>
          <div class="flex-space-between flex-wrap-wrap" style="padding-right:20px">
            <div v-for="(item,index) in friendLinkList" :key="index" class="friend-link-item">
              <a :href="item.linkUrl" target="_blank">
                <div class="flex-left align-items-center" style="height: 100px">
                  <div class="lex-8">
                    <el-avatar :src="item.linkAvatar" :size="70"></el-avatar>
                  </div>
                  <div class="ml-10">
                    <p class="font-s-18 mb-10">{{ item.linkName }}</p>
                    <p class="font-s-16 overflow-nowrap-1 color-grey-2">{{ item.linkIntro }}</p>
                  </div>
                </div>
              </a>
            </div>
          </div>
          <ai-editor-module :ai-editor-id="'aiEditor-introduce'" v-if="!loading" :content="articleInfo.articleContent"
                            :editable="false"></ai-editor-module>
          <div class="" id="comment">
            <div class="font-s-18 font-bold mb-20">
              评论<span class="ml-6" v-text="commentTotal"></span>
              <div class="flex-left">
                <div style="width: 40px">
                  <el-avatar v-if="userInfo && userInfo.avatar" :src="userInfo.avatar"></el-avatar>
                  <el-avatar v-else src="/img/tx.jpg"></el-avatar>
                </div>
                <div class="flex-12">
                  <div class="comment-import">
                    <ai-editor-module v-if="commentTextLoading" :ai-editor-id="'aiEditor-introduce1'"
                                      :htmlContent.sync="comment.content"
                                      :content="comment.content" :editor-height="'200px'">
                    </ai-editor-module>
                    <div class="overflow-hidden mb-6">
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
            </div>
            <el-skeleton class="mt-10 mr-6" v-if="skeletonLoading" :rows="4" animated/>
            <div v-if="articleComment.length!=0 && !skeletonLoading">
              <div class="mt-20 font-bold">热门评论</div>
              <!--                      一级评论-->
              <div style="margin-top: 10px;padding: 10px">
                <div v-for="(item,index) in articleComment.slice(0,commentShow)" class="mb-20" :key="index">
                  <div class="font-s-16 flex-left">
                    <div class="mr-2">
                      <el-avatar v-if="item.commentAvatar" :src="item.commentAvatar"></el-avatar>
                      <el-avatar size="medium" v-else src="/img/tx.jpg"></el-avatar>
                    </div>
                    <div class="flex-8">
                      <div class="flex-left mt-6 align-items-center">
                        <nuxt-link class="ml-6 cursor-pointer hover-cl"
                                   :to="'/user_home/article?uuid='+$base64.encode(item.commentUid)"
                                   target="_blank">
                          {{ item.commentName }}
                        </nuxt-link>
                        <el-tag v-if="item.commentUid==articleInfo.userId" size="mini" class="ml-4">作者</el-tag>
                        <span class="color-grey ml-10 mr-8 font-s-14">|</span>
                        <span class="color-grey font-s-14"
                              v-text=" $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                      </div>
                      <div class="content-div mt-10">
                        <ai-editor-module v-if="commentTextLoading" :ai-editor-id="'aiEditor-introduce-content1-'+index"
                                          :content="item.content"
                                          :editable="false"></ai-editor-module>
                      </div>
                      <div class="flex-left">
                        <div v-if="userInfo!=null&& userInfo.uuid==item.commentUid"
                             class="font-s-13 color-grey cursor-pointer hover-cl icon-hover"
                             @click="deleteComment(item)" style="width: 60px">
                          <svg t="1701957752378" class="icon icon-size-16 icon-theme-1 svg-translateY-3 icon-hover"
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
                        <div class="flex-12 flex-left">
                          <el-collapse accordion style="width: 100%;">
                            <el-collapse-item v-if="commentTextLoading">
                              <template slot="title">
                                <div class="hover-cl icon-hover font-s-13 color-grey">
                                  <svg t="1701957280525"
                                       class="icon icon-size-16 icon-theme-1 svg-translateY-3 icon-hover"
                                       viewBox="0 0 1024 1024" version="1.1"
                                       xmlns="http://www.w3.org/2000/svg" p-id="2658">
                                    <path
                                      d="M201.1 913.6c-1.2 0-2.4 0-3.6-0.1-11.1-1-21.6-5.2-31.2-12.5-9.3-7.7-15.9-17.2-19.7-28.4-4-10.7-4.8-22.3-2.4-34.5l24.2-111.3c-24.7-31-43.8-64.3-56.8-98.8C95.3 586.2 87 542.7 87 498.6c0-104.8 44.9-202.7 126.5-275.9 38.8-35.1 83.9-62.7 134.2-81.9 52-19.9 107.2-30 164-30 112.3 0 218 39.8 297.7 112 39.6 35.7 70.8 77.3 92.5 123.7 22.6 48.1 34 99.3 34 152.2 0 52.8-11.4 104-34 152.1-21.8 46.5-52.9 88.1-92.5 123.6-38.7 35.2-83.8 62.8-134.1 82-51.9 19.9-106.9 29.9-163.6 29.9-33.8 0-67.9-3.8-101.6-11.4-28.8-6.2-55.6-14.9-79.7-25.7l-100.5 56.9c-9.5 4.9-19.2 7.5-28.8 7.5z m29.4-223.2c9.1 9 12.8 22 10 34.7l-22.8 105.5 94.9-53.9c5.2-2.8 10.9-4.3 16.6-4.3 5 0 9.7 1.1 14.1 3.2 26.5 12.9 53.9 22.4 81.4 28.3 27.4 6.2 56.6 9.4 87 9.4 95.5 0 185.3-33.4 252.7-94.1 31.9-28.6 57-62.1 74.5-99.4 18-38.5 27.2-79.3 27.2-121.3s-9.1-82.8-27.2-121.3c-17.5-37.3-42.6-70.9-74.5-99.7-67.7-60.7-157.4-94.1-252.7-94.1-95.5 0-185.4 33.4-253.1 94.1-31.8 28.9-56.9 62.4-74.5 99.7-18.2 38.6-27.5 79.5-27.5 121.3 0 34.7 6.5 68.9 19.2 101.8 12.9 33.1 31.2 63.4 54.7 90.1z m453.3-124.8c-13.2 0-26.2-5.7-36.5-16.1-9.7-10.2-15-23.6-15-37.8 0-14.1 5.3-27.6 14.9-38.1 10.1-10.2 23.1-15.8 36.6-15.8s26.5 5.6 36.5 15.7c9.7 10.6 15 24.1 15 38.2 0 14.3-5.3 27.7-15 37.8-10.3 10.4-23.2 16.1-36.5 16.1z m-172 0c-13.7 0-26.6-5.7-36.5-16.1-9.8-10.3-15.1-23.7-15.1-37.8 0-13.9 5.4-27.5 15.1-38.1 9.7-10.2 22.7-15.8 36.6-15.8 13.7 0 26.5 5.6 36.2 15.7 9.6 10.3 15 23.9 15 38.2 0 14.5-5.3 27.9-15 37.8-10 10.4-22.8 16.1-36.3 16.1z m-168.7 0c-13.4 0-26.6-5.9-36.3-16.1-9.6-9.9-14.9-23.3-14.9-37.8 0-14.3 5.3-27.8 14.9-38.1 9.7-10.2 22.6-15.8 36.4-15.8 13.6 0 26.5 5.6 36.4 15.7 9.8 10.5 15.1 24 15.1 38.2 0 14.3-5.4 27.8-15.1 37.8-10.2 10.4-23.2 16.1-36.5 16.1z"
                                      p-id="2659"></path>
                                  </svg>
                                  回复
                                </div>
                              </template>
                              <div class="comment-import">
                                <ai-editor-module :ai-editor-id="'aiEditorComment2-'+index"
                                                  :htmlContent.sync="item.recoverContent"
                                                  :content="item.recoverContent" :editor-height="'160px'">
                                </ai-editor-module>
                                <div class="overflow-hidden mb-6">
                                  <el-button plain type="primary" class="fl-right" size="small"
                                             :disabled="item.recoverContent==null || item.recoverContent==''"
                                             :loading="buttonLoading"
                                             @click="sendComment(item.id,item.content,item.recoverContent,2,item.id,item.commentUid,2)">
                                    评 论
                                  </el-button>
                                </div>
                              </div>
                            </el-collapse-item>
                          </el-collapse>
                        </div>
                      </div>
                    </div>
                  </div>
                  <!--二级评论-->
                  <div v-if="item.mountComment.length!=0" class="comment-li">
                    <div v-for="(items,index2) in item.mountComment" class="mb-18" :key="index2">
                      <div class="flex-left">
                        <div class="mr-2">
                          <el-avatar v-if="items.commentAvatar" size="medium" :src="items.commentAvatar"></el-avatar>
                          <el-avatar size="medium" v-else src="/img/tx.jpg"></el-avatar>
                        </div>
                        <div class="flex-8">
                          <div class="flex-left align-items-center mt-6">
                            <nuxt-link :to="'/user_home/article?uuid='+$base64.encode(items.commentUid)"
                                       target="_blank">
                              <div class="ml-6 cursor-pointer hover-cl" v-text="items.commentName">
                              </div>
                            </nuxt-link>
                            <el-tag v-if="items.commentUid==articleInfo.userId" size="mini" class="ml-4">作者</el-tag>
                            <div v-if="items.commentGrade==3">
                              <span class="ml-15 mr-15 color-grey font-s-13">回复</span>
                              <nuxt-link class="cursor-pointer hover-cl"
                                         :to="'/user_home/article?uuid='+$base64.encode(items.targetUid)"
                                         target="_blank">
                                {{ items.targetName }}
                              </nuxt-link>
                              <el-tag v-if="items.targetUid==articleInfo.userId" size="mini" class="ml-4">作者</el-tag>
                            </div>
                            <span class="color-grey ml-10 mr-8 font-s-13">|</span>
                            <span class="color-grey font-s-13"
                                  v-text="$utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                          </div>
                          <!--                          内容-->
                          <div class=" content-div mt-10">
                            <ai-editor-module v-if="commentTextLoading"
                                              :ai-editor-id="'aiEditor-introduce-content2-'+index+'-'+index2"
                                              :content="items.content"
                                              :editable="false"></ai-editor-module>
                          </div>
                          <div class="flex-left mt-4">
                            <div class="font-s-13 hover-cl cursor-pointer color-grey-2"
                                 v-if="userInfo!=null && userInfo.uuid==items.commentUid"
                                 @click="deleteComment(items)" style="width: 60px">
                              <svg t="1701957752378" class="icon icon-size-16 icon-theme-1 svg-translateY-3 icon-hover"
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
                            <div class="flex-12 flex-left">
                              <el-collapse accordion style="width: 100%;">
                                <el-collapse-item v-if="commentTextLoading">
                                  <template slot="title">
                                    <div class="hover-cl font-s-13 color-grey color-grey-2">
                                      <svg t="1701957280525"
                                           class="icon icon-size-16 icon-theme-1 svg-translateY-3 icon-hover"
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
                                    <div class="comment-import">
                                      <ai-editor-module :ai-editor-id="'aiEditor-introduce-comment3-'+index+'-'+index2"
                                                        :htmlContent.sync="items.recoverContent"
                                                        :content="items.recoverContent" :editor-height="'160px'">
                                      </ai-editor-module>
                                      <div class="overflow-hidden mb-6">
                                        <el-button plain type="primary" class="fl-right" size="small"
                                                   :disabled="items.recoverContent==null || items.recoverContent==''"
                                                   :loading="buttonLoading"
                                                   @click="sendComment(item.id,item.content,items.recoverContent,3,items.id,items.commentUid,2)">
                                          回 复
                                        </el-button>
                                      </div>
                                    </div>
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
                <div v-if="articleComment.length>commentShow" class="text-center font-s-14 color-grey hover-cl">
                      <span class="cursor-pointer"> 查看更多
                      <i class="fa fa-angle-double-down" aria-hidden="true"></i></span>
                </div>
              </div>
            </div>
            <div v-if="articleComment.length==0 && !skeletonLoading"
                 style="text-align: center;margin-top: 20px;font-size: 14px;color: #747d8c">快来抢占沙发~
            </div>
          </div>
        </div>
        <div style="min-height: 1px;width: 100px"></div>
      </div>
    </div>
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>

export default {
  name: "friendLink",
  head() {
    return {
      title: `${this.articleInfo.articleTitle == undefined ? process.env.PROJECT_NAME : this.articleInfo.articleTitle + '-' + process.env.PROJECT_NAME}`,
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
  data() {
    return {
      articleInfo: {},
      //文章目录
      tocArray: [],
      //默认头像
      profile: '',
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
      isOwn: false,
      isFabulous: false,
      loginDialog: false,
      loading: true,
      goTopLoading: false,
      tocArrayLoading: false,
      collectionList: [],
      collectionLoading: false,
      createCoDialog: false,
      form: {
        collectionName: undefined,
        collectionIntroduce: undefined,
      },
      buttonLoading: false,
      buttonCoLoading: false,
      commentTotal: 0,
      commentShow: 3,
      commentShow2: 3,
      skeletonLoading: true,
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
      friendLinkList: [],
    }
  },
  async asyncData({app, params, store}) {
    const id = -12;//友链页面
    let token = store.state.token;
    const response = await fetch(`http://${process.env.SERVER_URL}/white/article/details/${id}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
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
          this.$modal.msg("感谢你的评论！");
          this.articleCommentLists()
        }
      }).finally(() => {
        this.comment.content = null;
        this.buttonLoading = false
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

    loginDialogMethod(val) {
      this.loginDialog = val;
    },
    gotoAnchor(id) {
      let dom = document.querySelector("#" + id);
      dom.scrollIntoView({
        /*平滑滚动*/
        behavior: "smooth",
        block: "start",
      })
    },
    handleScroll() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      if (scrollTop > (clientHeight / 2)) {
        this.goTopLoading = true;
      } else {
        this.goTopLoading = false;
      }
    },
    articleDetailsInfo() {
      //获取用户基本信息
      this.getBasicsUsers();
      this.$API('/white/site/friend-link', this.$get()).then(res => {
        this.friendLinkList = res.rows;
        this.loading = false;
      });
      //  生成文章目录
      // this.articleDirectory();
      //获取评论
      this.articleCommentLists();
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
        this.skeletonLoading = false;
        this.commentTextLoading = true;
      })
    },
    articleDirectory() {
      this.tocArray = [];
      let tocTags = ["H1", "H2", "H3", "H4"]
      this.$nextTick(() => {
        let element = document.querySelector("#detailDirectory");
        let index = 0;
        let childNodes = element.childNodes[2].childNodes[2].childNodes[0].childNodes;
        childNodes.forEach(item => {
          let tagName = item.tagName;
          //是否包含 tocTags存在的标签
          if (tagName == undefined || !tocTags.includes(tagName.toUpperCase())) {
            return true;
          }
          index++;
          //获取锚点
          let elementsByTagName = item.getElementsByTagName("a");
          let attribute = elementsByTagName[0].getAttribute("id");
          this.tocArray.push({
            id: attribute,
            title: item.innerText,
            level: Number.parseInt(tagName.substring(1)),
            offsetTop: item.offsetTop,
          })
        });
        this.tocArrayLoading = true;
      });
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

.friend-link-item {
  width: 290px;
  border: 1px solid #ced6e0;
  margin-bottom: 20px;
  padding: 0px 15px;
  border-radius: 20px;
  cursor: pointer;
  transition: .4s;
}

.friend-link-item:hover {
  background-color: #fad8cf;
  border: 1px solid #ff4757;
}
</style>
