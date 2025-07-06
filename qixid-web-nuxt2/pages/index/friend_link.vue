<template>
  <div class="mt-30">
    <div class="page-content-width">
      <div class="flex-space-between flex-wrap-wrap">
        <div class="left-content-width _module_hiding">
          <div class="left-content-ul left-content-width">
            <ul class="left-content-auto">
              <li class="fa-li-class" title="申请友链">
                <div class="fa-li-div" @click="goComment">
                  <svg t="1726563114935" class="icon-theme-1 icon-size-20 mt-10 icon-hover" viewBox="0 0 1024 1024"
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
                  <svg t="1700407528486" class="icon-theme-1 icon-size-20 mt-10 icon-hover" viewBox="0 0 1024 1024"
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
        <div class="article-content-introduce">
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
          <VditorPreview :id="'friendLinkVditor'" :content="articleInfo.articleContent"></VditorPreview>
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
                                class="ml-6">作者
                        </el-tag>
                        <span class="color-grey-2 font-s-12 ml-10"
                              v-text=" $utils.parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}')"></span>
                      </div>
                      <div class="content-div mt-10">
                        <mavon-editor class="markdown"
                                      previewBackground="#FFFFFF"
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
                      <div class="flex-left">
                        <div v-if="userInfo!=null&& userInfo.uuid==item.commentUid"
                             class="font-s-12 color-grey-2 cursor-pointer hover-cl icon-hover mr-10"
                             @click="deleteComment(item)">
                          <svg t="1742632616926" class="icon-size-14 icon-theme-1 svg-translateY-2 icon-hover"
                               viewBox="0 0 1024 1024" version="1.1"
                               xmlns="http://www.w3.org/2000/svg" p-id="6428">
                            <path
                              d="M525.696 590.336l-168.96 169.024a35.968 35.968 0 0 1-50.816-0.896 35.904 35.904 0 0 1-0.96-50.816l169.024-168.96-181.952-181.952a35.968 35.968 0 0 1 0.96-50.816 35.968 35.968 0 0 1 50.816-0.96l181.888 181.952 181.952-181.952a35.904 35.904 0 0 1 50.816 0.96c14.272 14.272 14.72 36.992 0.896 50.752L577.408 538.624l169.024 169.024a35.904 35.904 0 0 1-0.896 50.816 35.904 35.904 0 0 1-50.816 0.896l-168.96-168.96zM512 950.848A438.848 438.848 0 1 0 512 73.152a438.848 438.848 0 0 0 0 877.696zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z"
                              p-id="6429"></path>
                          </svg>
                          删除
                        </div>
                        <div class="flex-left flex-12">
                          <el-collapse accordion style="width: 100%;">
                            <el-collapse-item>
                              <template slot="title">
                                <div class="hover-cl icon-hover font-s-12 color-grey-2 svg-translateY-5">
                                  <svg t="1741407164890"
                                       class="icon-hover icon-theme-1 icon-size-14 svg-translateY-2"
                                       viewBox="0 0 1024 1024"
                                       version="1.1"
                                       xmlns="http://www.w3.org/2000/svg" p-id="27498">
                                    <path
                                      d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                                      p-id="27499"></path>
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
                                          previewBackground="#FFFFFF"
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
                          <div class="flex-left mb-10">
                            <div class="font-s-12 hover-cl cursor-pointer color-grey-2 mr-10"
                                 v-if="userInfo!=null && userInfo.uuid==items.commentUid"
                                 @click="deleteComment(items)">
                              <svg t="1742632616926" class="icon-size-14 icon-theme-1 svg-translateY-2 icon-hover"
                                   viewBox="0 0 1024 1024" version="1.1"
                                   xmlns="http://www.w3.org/2000/svg" p-id="6428">
                                <path
                                  d="M525.696 590.336l-168.96 169.024a35.968 35.968 0 0 1-50.816-0.896 35.904 35.904 0 0 1-0.96-50.816l169.024-168.96-181.952-181.952a35.968 35.968 0 0 1 0.96-50.816 35.968 35.968 0 0 1 50.816-0.96l181.888 181.952 181.952-181.952a35.904 35.904 0 0 1 50.816 0.96c14.272 14.272 14.72 36.992 0.896 50.752L577.408 538.624l169.024 169.024a35.904 35.904 0 0 1-0.896 50.816 35.904 35.904 0 0 1-50.816 0.896l-168.96-168.96zM512 950.848A438.848 438.848 0 1 0 512 73.152a438.848 438.848 0 0 0 0 877.696zM512 1024A512 512 0 1 1 512 0a512 512 0 0 1 0 1024z"
                                  p-id="6429"></path>
                              </svg>
                              删除
                            </div>
                            <div class="flex-12 flex-left">
                              <el-collapse accordion style="width: 100%;">
                                <el-collapse-item>
                                  <template slot="title">
                                    <div class="hover-cl font-s-12 color-grey-2">
                                      <svg t="1741407164890"
                                           class="icon-hover icon-theme-1 icon-size-14 svg-translateY-2"
                                           viewBox="0 0 1024 1024"
                                           version="1.1"
                                           xmlns="http://www.w3.org/2000/svg" p-id="27498">
                                        <path
                                          d="M157.568 751.296c-11.008-18.688-18.218667-31.221333-21.802667-37.909333A424.885333 424.885333 0 0 1 85.333333 512C85.333333 276.362667 276.362667 85.333333 512 85.333333s426.666667 191.029333 426.666667 426.666667-191.029333 426.666667-426.666667 426.666667a424.778667 424.778667 0 0 1-219.125333-60.501334 2786.56 2786.56 0 0 0-20.053334-11.765333l-104.405333 28.48c-23.893333 6.506667-45.802667-15.413333-39.285333-39.296l28.437333-104.288z m65.301333 3.786667l-17.258666 63.306666 63.306666-17.258666a32 32 0 0 1 24.522667 3.210666 4515.84 4515.84 0 0 1 32.352 18.944A360.789333 360.789333 0 0 0 512 874.666667c200.298667 0 362.666667-162.368 362.666667-362.666667S712.298667 149.333333 512 149.333333 149.333333 311.701333 149.333333 512c0 60.586667 14.848 118.954667 42.826667 171.136 3.712 6.912 12.928 22.826667 27.370667 47.232a32 32 0 0 1 3.338666 24.714667z m145.994667-70.773334a32 32 0 1 1 40.917333-49.205333A159.189333 159.189333 0 0 0 512 672c37.888 0 73.674667-13.173333 102.186667-36.885333a32 32 0 0 1 40.917333 49.216A223.178667 223.178667 0 0 1 512 736a223.178667 223.178667 0 0 1-143.136-51.690667z"
                                          p-id="27499"></path>
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
        <div class="_module_hiding" style="min-height: 1px;width: 100px"></div>
      </div>
    </div>
    <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
  </div>
</template>

<script>

import VditorPreview from "../../components/vditorComponents/Vditor-preview.vue";

export default {
  name: "friendLink",
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
      queryParams: {
        pageNum: 0,
        pageSize: 20,
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
      friendLinkList: [],
      //评论相关
      buttonLoading: false,
      commentTotal: 0,
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
      //评论编辑器切换
      radio: '1',
      radio2: '1',
      radio3: '1',
    }
  },
  async asyncData({app, params, store}) {
    const id = -12;//友链页面
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
      pageSize: 20,
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
        this.commentTextLoading = true;
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
