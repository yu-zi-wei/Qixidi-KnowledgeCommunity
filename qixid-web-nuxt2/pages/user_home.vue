<template>
  <div>
    <navigation-bar-module :isConceal="false"></navigation-bar-module>
    <div class="mt-30">
      <div class="module-main">
        <el-row>
          <el-col :span="1" style="min-height: 1px"/>
          <el-col :span="16">
            <div class="home-top-cl">
              <div class="flex-left">
                <div>
                  <el-avatar v-if="userInfo.avatar" :src="userInfo.avatar" :size="100"></el-avatar>
                  <el-avatar v-else src="/img/tx.jpg" :size="100"></el-avatar>
                </div>
                <div class="flex-left">
                  <div class="ml-12 flex-10">
                    <p v-text="userInfo.nickname" style="font-size: 24px;font-weight: bold" class="mb-15"></p>
                    <div class="mb-10 color-grey font-s-14">
                      <svg t="1710986117946" class="icon icon-size-16 svg-translateY-2" viewBox="0 0 1024 1024"
                           version="1.1"
                           xmlns="http://www.w3.org/2000/svg" p-id="4317">
                        <path
                          d="M383.616 291.712c1.536 4.736 7.808 7.808 12.544 7.808h117.248c4.736 0 9.344-3.072 12.544-7.808l92.16-151.68c1.536-4.736 1.536-9.344 0-14.08-3.072-4.736-6.272-6.272-10.88-6.272H314.752c-4.736 0-9.344 1.536-10.88 6.272-3.072 3.072-3.072 9.344-1.536 12.544l81.28 153.216zM547.2 344.96l112.512 300.032h54.784v-64zM572.16 477.312l-282.88 282.88 50.048 48.384 256.256-256.256zM606.336 577.152L356.224 825.6l53.248 51.584 220.416-220.416zM538.112 372.736L311.424 599.424l-39.04 134.4c-1.536 3.072 0 6.272 0 7.808l289.152-289.152-23.424-79.744zM383.616 338.688l-9.344 32.768 42.24-42.24h-20.352c-6.272 0.128-11.008 4.736-12.544 9.472zM317.952 557.44L527.36 348.032l-3.072-7.808c-1.536-6.272-7.808-9.344-12.544-9.344h-70.4l-79.744 79.744-43.648 146.816zM642.048 683.136L427.904 895.744l35.968 35.968c3.072 3.072 6.272 4.736 9.344 4.736s6.272-1.536 9.344-4.736l175.104-170.368c3.072-3.072 4.736-9.344 3.072-14.08l-18.688-64.128z"
                          p-id="4318"></path>
                      </svg>

                      <span v-text="userInfo.occupation==null?'职业-~-':userInfo.occupation"></span>
                    </div>
                    <div class="color-grey font-s-14">
                      <svg t="1710986208928" class="icon icon-size-16 svg-translateY-2" viewBox="0 0 1024 1024"
                           version="1.1"
                           xmlns="http://www.w3.org/2000/svg" p-id="5322">
                        <path
                          d="M829.44 71.68H194.56A122.88 122.88 0 0 0 71.68 194.56v634.88a122.88 122.88 0 0 0 122.88 122.88h634.88a122.88 122.88 0 0 0 122.88-122.88V194.56A122.88 122.88 0 0 0 829.44 71.68z m81.92 757.76a81.92 81.92 0 0 1-81.92 81.92H194.56a81.92 81.92 0 0 1-81.92-81.92V194.56a81.92 81.92 0 0 1 81.92-81.92h634.88a81.92 81.92 0 0 1 81.92 81.92z"
                          p-id="5323"></path>
                        <path d="M276.48 348.16m-40.96 0a40.96 40.96 0 1 0 81.92 0 40.96 40.96 0 1 0-81.92 0Z"
                              p-id="5324"></path>
                        <path d="M430.08 348.16m-40.96 0a40.96 40.96 0 1 0 81.92 0 40.96 40.96 0 1 0-81.92 0Z"
                              p-id="5325"></path>
                        <path d="M583.68 348.16m-40.96 0a40.96 40.96 0 1 0 81.92 0 40.96 40.96 0 1 0-81.92 0Z"
                              p-id="5326"></path>
                        <path
                          d="M256 573.44h409.6a20.48 20.48 0 0 0 0-40.96H256a20.48 20.48 0 0 0 0 40.96zM768 706.56H256a20.48 20.48 0 0 0 0 40.96h512a20.48 20.48 0 0 0 0-40.96z"
                          p-id="5327"></path>
                      </svg>

                      <span v-text="userInfo.introduce==null?'简介':userInfo.introduce"></span>
                    </div>
                  </div>
                  <div class="align-self-end">
                    <el-button v-show="currentUser" plain size="small" @click="routeJump('/user_info/settings')">编辑个人资料
                    </el-button>
                    <el-button size="small" v-show="!currentUser" :loading="userInfo.buttonLoading"
                               @click="followClick(userInfo)"
                               :type="userInfo.isFollow?'primary':''">{{ userInfo.isFollow ? '已关注' : '关注Ta' }}
                    </el-button>
                  </div>
                </div>
              </div>
              <el-divider></el-divider>
            </div>
            <div class="assembly-top-cl">
              <el-menu :default-active="$route.path+'?uuid='+escapeUrl($route.query.uuid)"
                       text-color="#0a3d62"
                       :router="true"
                       :active-text-color="themeColor"
                       class="el-menu-demo" mode="horizontal">
                <el-menu-item style="margin-right: 4px;margin-left:8px;font-size: 18px;"
                              v-for="(item,index) in userListNavigation" :key="index"
                              v-if="item.jurisdiction==0 ||(item.jurisdiction==1 && currentUser)"
                              :index="item.route+'?uuid='+escapeUrl($route.query.uuid)">
                  <div v-text="item.navigationName"></div>
                </el-menu-item>
              </el-menu>
              <div>
                <nuxt-child/>
              </div>
            </div>
          </el-col>
          <el-col :span="1" style="min-height: 1px"/>
          <el-col :span="4">
            <div class="home-left-cl">
              <div class="flex-space-between">
                <div class="font-bold color-grey5"
                     style="line-height: 34px">个人成就
                </div>
                <div>
                  <el-button v-if="userInfos.uuid!=userData.uuid"
                             size="small" @click="privateLetter(userData.uuid)">私信
                  </el-button>
                  <div v-if="currentUser" class="font-s-14" title="平台币">
                    <el-dropdown>
                      <div class="cursor-pointer">
                        <svg t="1680423108793" class="icon icon-size-24 svg-translateY-4" viewBox="0 0 1024 1024"
                             version="1.1"
                             xmlns="http://www.w3.org/2000/svg" p-id="7773">
                          <path
                            d="M517.418444 965.859231c-9.63279 0-19.437594-0.344028-29.070385-0.860071-59.00084-3.612296-115.593482-18.577524-168.2298-43.863598-54.700487-26.318159-103.036452-62.78515-143.631782-108.884932-40.59533-45.927768-70.869814-98.392071-90.135394-156.016798-18.577524-55.560558-26.146145-113.529313-22.533848-172.530153s18.577524-115.593482 43.863598-168.2298c26.318159-54.700487 62.78515-103.036452 108.884932-143.631782 45.927768-40.59533 98.392071-70.869814 156.016798-90.135394 46.271796-15.48127 94.607761-23.393919 143.459768-23.393919 9.63279 0 19.437594 0.344028 29.070385 0.860071 58.828826 3.612296 115.593482 18.577524 168.401814 43.863598 54.700487 26.318159 103.036452 62.78515 143.631782 108.884932 47.991937 54.356459 81.19066 118.345708 99.252142 189.903578l0 0.172014 0 0.172014c3.612296 11.180917 0.344028 23.393919-8.256677 31.134554-5.332437 4.644381-12.213002 7.396607-19.26558 7.396607-8.428691 0-16.341341-3.612296-22.017806-9.976818-1.720141-1.892155-2.752226-3.956325-3.612296-5.676466l-0.172014-0.344028-0.860071-1.032085-0.516042-2.236183c-14.793214-61.409037-44.379641-117.657652-88.071225-166.853687-69.665715-78.610449-165.649588-125.570301-270.062154-131.934823-8.256677-0.516042-16.685369-0.860071-24.942046-0.860071-47.647909 0-94.263733 8.428691-138.299345 25.11406-44.207626 16.685369-84.974971 41.283387-120.92592 72.933983-78.610449 69.321687-125.570301 165.133546-131.934823 269.890139-6.536536 104.584579 28.210314 205.728876 97.532001 284.339325 69.149672 78.610449 165.133546 125.570301 269.890139 132.106837 8.256677 0.516042 16.685369 0.860071 24.942046 0.860071 47.647909 0 94.263733-8.428691 138.299345-25.11406 44.207626-16.685369 84.974971-41.283387 120.92592-72.933983 62.78515-55.560558 105.44465-128.322526 123.506131-211.061314l0.172014-0.344028 0-0.344028c0.688056-6.70855 3.78431-12.901058 8.77272-17.889467 0.172014-0.172014 0.344028-0.344028 0.344028-0.344028 0.172014-0.172014 0.344028-0.344028 0.516042-0.516042 5.332437-4.644381 12.213002-7.224593 19.437594-7.224593 7.568621 0 14.793214 2.752226 20.125651 7.912649 5.504452 5.160423 8.77272 12.55703 8.944734 20.985721 0 0.860071-0.172014 1.720141-0.172014 2.408198-19.953637 98.392071-69.665715 184.743155-143.631782 249.764488-45.755753 40.423316-98.220057 70.697799-156.016798 90.135394C614.606417 957.946582 566.270452 965.859231 517.418444 965.859231zM470.630606 729.339829 470.630606 485.939862l-3.612296 1.032085c-21.501764 6.192508-40.939358 12.213002-59.172854 18.233496-7.912649 2.580212-18.233496 5.332437-29.070385 8.084663-14.965228 3.956325-30.618512 7.912649-40.423316 12.040988l-1.720141 0.688056 0 127.806484-70.525785 0 0-199.536368 70.353771 0 0 29.414413 3.78431-1.376113c5.84848-2.236183 16.513355-4.644381 27.866286-7.224593 11.69696-2.752226 24.942046-5.84848 35.090879-9.116748 19.437594-6.192508 41.455401-12.213002 65.365362-17.373425l2.236183-0.516042 0-51.088191-2.92424 0.172014c-38.531161 2.064169-74.654124 3.268268-106.992777 3.440282-16.685369 0.172014-32.510667 0.860071-46.271796 1.376113-11.69696 0.516042-22.877877 1.032085-32.854695 1.032085l-12.901058-50.572148c17.029397 0.172014 34.402822 1.376113 51.260205 2.408198 17.717453 1.204099 35.950949 2.408198 53.15236 2.408198 5.676466 0 10.836889-0.172014 15.997312-0.344028 38.359147-2.064169 77.062322-5.504452 115.07744-10.492861 38.015118-4.988409 75.858223-11.352931 112.325214-19.26558 35.778935-7.396607 72.761969-16.857383 110.261045-27.694272 6.020494 14.793214 10.836889 26.146145 14.965228 36.466991 0.860071 2.064169 1.720141 4.128339 2.408198 6.020494 1.204099 2.752226 2.236183 5.504452 3.440282 8.084663 1.548127 3.78431 3.096254 7.396607 4.472367 10.492861l0.688056 1.720141c1.032085 2.92424 2.408198 6.020494 3.956325 9.116748-32.682681 5.332437-66.569461 9.804804-100.456241 13.245087-24.942046 2.580212-56.764656 4.816395-84.802956 6.880564-9.804804 0.688056-18.921552 1.376113-27.006215 2.064169l-2.580212 0.172014 0 35.950949 3.096254-0.344028c11.008903-1.032085 25.630102-1.548127 41.283387-1.548127 15.653284 0 30.446498 0.516042 41.627415 1.548127 31.82261 2.752226 57.452713 8.428691 76.202251 17.029397 18.749538 8.428691 33.198723 17.889467 43.003528 27.866286 9.804804 10.148833 16.169326 20.469679 19.26558 30.618512 3.440282 10.836889 5.332437 24.081975 6.020494 30.274483 0.344028 8.77272 0.860071 18.921552 1.204099 30.274483 0.344028 11.008903 0 22.361834-1.204099 33.714766-1.204099 11.352931-3.440282 22.361834-6.70855 32.682681-3.268268 10.492861-7.912649 19.26558-13.761129 26.490173-6.192508 7.224593-13.933143 12.55703-23.909961 16.169326-5.84848 2.064169-12.901058 3.096254-20.469679 3.096254-5.332437 0-11.008903-0.516042-17.029397-1.376113-13.589115-2.064169-25.630102-4.644381-36.122963-7.912649-11.352931-3.440282-20.469679-6.70855-27.694272-9.804804-8.600706-3.612296-15.653284-6.880564-21.32975-9.976818l3.268268-31.650596c13.073072 6.364522 24.942046 10.492861 35.090879 11.868974 2.580212 0.344028 5.160423 0.516042 7.740635 0.516042 2.408198 0 4.988409-0.172014 7.396607-0.688056 4.644381-0.860071 9.116748-2.752226 13.589115-5.676466 4.300353-2.92424 7.912649-7.396607 10.664875-13.417101 2.92424-6.020494 4.644381-13.417101 5.504452-22.18982 2.064169-23.393919 2.580212-41.455401 1.720141-53.324374-0.344028-6.536536-0.860071-8.600706-1.204099-9.63279 0-3.440282-0.344028-7.740635-0.860071-12.901058-0.516042-5.160423-2.064169-9.804804-4.300353-13.761129-4.300353-7.396607-12.385016-14.277171-24.598018-20.813707-9.976818-5.332437-23.909961-7.912649-40.939358-7.912649-3.440282 0-6.880564 0.172014-10.492861 0.344028l-0.172014 0c-7.912649 0.860071-15.997312 1.720141-23.565933 2.408198-2.92424 0.344028-7.568621 0.688056-13.073072 1.032085-9.804804 0.860071-20.985721 1.548127-26.834201 2.752226l-2.236183 0.344028 0 252.860742-70.869814 0L470.630606 729.339829 470.630606 729.339829 470.630606 729.339829z"
                            fill="#ffa502" p-id="7774"></path>
                        </svg>
                        <span class="color-grey4" style="position: relative;bottom: 0.18em; "> {{
                            userInfos.acurrency
                          }}</span>
                      </div>
                      <!--                    <el-dropdown-menu slot="dropdown" style="padding: 15px;height: 100px">-->
                      <!--                      <el-button size="small" @click="rechargeCentreMethod">-->
                      <!--                        <span class="color-ffc312 font-bold">A币 中心</span>-->
                      <!--                      </el-button>-->
                      <!--                      <el-button size="small" type="primary" @click="recharge">充 值</el-button>-->
                      <!--                    </el-dropdown-menu>-->
                    </el-dropdown>
                  </div>
                </div>
              </div>
              <hr class="hr-item mb-10 mt-10"/>
              <div class="user-data-cl">
                <p>
                  <svg t="1684217091163" class="icon icon-size-20 svg-translateY-5 mr-4" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="3691" data-spm-anchor-id="a313x.7781069.0.i7">
                    <path
                      d="M150.588235 542.117647a30.117647 30.117647 0 1 1 60.235294 0 271.058824 271.058824 0 0 0 271.058824 271.058824 30.117647 30.117647 0 1 1 0 60.235294C298.917647 873.411765 150.588235 725.082353 150.588235 542.117647z m722.82353-60.235294a30.117647 30.117647 0 1 1-60.235294 0 271.058824 271.058824 0 0 0-271.058824-271.058824 30.117647 30.117647 0 1 1 0-60.235294c182.964706 0 331.294118 148.329412 331.294118 331.294118zM512 1024C229.225412 1024 0 794.774588 0 512S229.225412 0 512 0s512 229.225412 512 512-229.225412 512-512 512z m0-60.235294c249.494588 0 451.764706-202.270118 451.764706-451.764706 0-249.494588-202.270118-451.764706-451.764706-451.764706C262.505412 60.235294 60.235294 262.505412 60.235294 512c0 249.494588 202.270118 451.764706 451.764706 451.764706z m0-331.294118a120.470588 120.470588 0 1 1 0-240.941176 120.470588 120.470588 0 0 1 0 240.941176z m0-60.235294a60.235294 60.235294 0 1 0 0-120.470588 60.235294 60.235294 0 0 0 0 120.470588z"
                      p-id="3692" data-spm-anchor-id="a313x.7781069.0.i6" class="selected" fill="#6c5ce7"></path>
                  </svg>
                  专辑 <span class="font-bold ml-4">
                {{ userData.albumCount == 0 ? '--' : userData.albumCount }}
              </span>
                </p>
                <p>
                  <svg t="1742980910184" class="icon svg-translateY-5 mr-4" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="65897" width="20" height="20">
                    <path d="M97 96.61v704.5a126.67 126.67 0 0 0 126.67 126.67L222 96.61z" fill="#fcc692"
                          p-id="65898"></path>
                    <path
                      d="M774.46 96.41H271.73v831.37h502.73c84.1 0 152.54-75.92 152.54-169.22V265.64c0-93.31-68.44-169.23-152.54-169.23z"
                      fill="#F5A659" p-id="65899"></path>
                    <path d="M432.19 367.34h334.34v83.01H432.19zM432.19 563.34h334.34v83.01H432.19z" fill="#FFFFFF"
                          p-id="65900"></path>
                  </svg>
                  名言 <span class="font-bold ml-4">
                {{ userData.dictumCount == 0 ? '--' : userData.dictumCount }}
              </span>
                </p>
                <p>
                  <svg t="1742980371939" class="icon svg-translateY-4 icon-size-18 mr-4" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="51922">
                    <path
                      d="M788.551 292.895c-74.801 0-135.633-60.832-135.633-135.605v-156.171h-487.636c-45.595 0-82.548 36.982-82.548 82.549v856.719c0 45.567 36.955 82.493 82.548 82.493h693.46c45.571 0 82.523-36.926 82.523-82.493v-647.493h-152.714zM805.084 839.116h-586.199v-55.301h586.199v55.301zM805.084 662.15h-586.199v-55.301h586.199v55.301zM805.084 485.184h-586.199v-55.301h586.199v55.301z"
                      p-id="51923" fill="#487eb0"></path>
                    <path d="M706.002 1.119v156.171c0 45.567 36.955 82.493 82.549 82.493h152.714l-235.263-238.662z"
                          p-id="51924" fill="#27ae60"></path>
                  </svg>
                  文章<span class="font-bold ml-4">
                {{ userData.articleCount == 0 ? '--' : userData.articleCount }}
              </span>
                </p>
                <p>
                  <svg t="1742980690883" class="icon svg-translateY-4 mr-4" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="56608" width="20" height="20">
                    <path
                      d="M784.786286 570.477714a128 128 0 0 0-199.387429 114.029715 218.916571 218.916571 0 0 0 74.057143 174.957714L658.285714 859.392l62.683429 54.857143H73.142857c0-223.634286 167.314286-408.210286 383.561143-435.419429l26.038857 48.822857h58.514286l26.038857-48.822857c81.554286 10.24 156.16 42.898286 217.490286 91.611429z m-334.628572-103.862857a219.428571 219.428571 0 1 1 123.648 0l0.877715-1.645714h-125.366858l0.841143 1.609143z m-0.841143 309.394286L512 841.142857l62.683429-65.097143-33.426286-217.014857h-58.514286l-33.426286 217.014857zM804.571429 899.949714l-90.148572-78.189714c-29.622857-26.806857-56.137143-58.806857-56.137143-102.473143v-12.214857C658.285714 648.411429 688.786286 621.714286 735.926857 621.714286c23.881143 0 53.138286 24.466286 68.644572 40.228571 15.506286-15.762286 44.361143-40.228571 68.242285-40.228571 47.140571 0 78.043429 25.6 78.043429 85.321143v12.214857c0 43.666286-28.16 83.273143-58.514286 110.08l-87.771428 70.582857z"
                      p-id="56609" fill="#f78fb3"></path>
                  </svg>
                  关注Ta <span class="font-bold ml-4">
                {{ userData.fansFollowCount == 0 ? '--' : userData.fansFollowCount }}
              </span>
                </p>
                <p>
                  <svg t="1742980617097" class="icon icon-size-20 svg-translateY-5 mr-4" viewBox="0 0 1024 1024"
                       version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="52970">
                    <path
                      d="M0 0m170.666667 0l682.666666 0q170.666667 0 170.666667 170.666667l0 682.666666q0 170.666667-170.666667 170.666667l-682.666666 0q-170.666667 0-170.666667-170.666667l0-682.666666q0-170.666667 170.666667-170.666667Z"
                      fill="#74b9ff" p-id="52971"></path>
                    <path
                      d="M315.733333 281.6h392.533334a34.133333 34.133333 0 0 1 34.133333 34.133333v430.506667a34.133333 34.133333 0 0 1-44.919467 32.392533L522.786133 720.384a34.133333 34.133333 0 0 0-21.572266 0l-174.6944 58.231467a34.133333 34.133333 0 0 1-44.919467-32.392534V315.733333a34.133333 34.133333 0 0 1 34.133333-34.133333z m89.6 136.533333a21.333333 21.333333 0 0 0 0 42.666667h213.333334a21.333333 21.333333 0 0 0 0-42.666667h-213.333334z m0 119.466667a21.333333 21.333333 0 0 0 0 42.666667h213.333334a21.333333 21.333333 0 0 0 0-42.666667h-213.333334z"
                      fill="#ffffff" p-id="52972"></path>
                  </svg>
                  专栏 <span class="font-bold ml-4">
                {{ userData.specialColumnCount == 0 ? '--' : userData.specialColumnCount }}
              </span>
                </p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--    充值页面-->
      <el-dialog title="获取 A币" :visible.sync="rechargeVisible" width="650px">
        <div class="overflow-hidden">
          <div class="button-item color-theme" v-for="item of rechargeListArr" @click="pays(item)">
            <div class="discount-cl" v-if="!item.isFinish">{{ $utils.numberToText(item.discount) }}折</div>
            <div style="padding-top: 6px" class="mb-6">
          <span v-if="item.isFinish">
            {{ item.money }}￥
          </span>
              <span v-if="!item.isFinish" style="text-decoration:line-through;font-size: 14px;color:#34495e">
            {{ item.money }}￥
          </span>
              <span v-if="!item.isFinish" style="margin-left: 4px" class="color-theme">
          {{ item.moveMoney }}￥
            </span>
            </div>
            <div class="font-s-13 color-theme">可获得 <span
              class="color-ffc312 font-bold">{{ item.currency }} A币</span></div>
          </div>
        </div>

      </el-dialog>
      <!--    A币 中心-->
      <el-drawer
        title="A币 中心"
        direction="ttb"
        :wrapperClosable="false"
        :visible.sync="rechargeCentre"
        size="100%">
        <div style="padding: 0 30px">
          <el-card class="box-card">
            <el-descriptions title="个人信息">
              <el-descriptions-item label="用户名">{{ userInfos.nickname }}</el-descriptions-item>
              <el-descriptions-item label="手机号">{{ userInfos.phone == null ? '--' : userInfos.phone }}
              </el-descriptions-item>
              <el-descriptions-item label="A币">
                <span class="color-ffc312">{{ userInfos.acurrency }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="职业">
                {{ userInfos.occupation == null ? '--' : userInfos.occupation }}
              </el-descriptions-item>
              <el-descriptions-item label="注册时间">
                {{ $utils.parseTime(userInfos.createTime, '{y}年{m}月{d}日') }}
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
          <div class="mb-10 mt-20 font-bold ml-4">订单列表</div>
          <el-table :data="gridData" v-loading="orderListLoading">
            <el-table-column property="id" label="订单ID"/>
            <el-table-column property="nickname" label="购买用户"/>
            <el-table-column property="commodityName" label="商品名称"/>
            <el-table-column property="rechargeMoney" label="订单金额">
              <template v-slot="scope">
                <span>{{ scope.row.rechargeMoney }}￥</span>
              </template>
            </el-table-column>
            <el-table-column property="rechargeCurrency" label="收益A币数量"/>
            <el-table-column property="discount" label="折扣">
              <template v-slot="scope">
                <span>{{ $utils.numberToText(scope.row.discount) }}折</span>
              </template>
            </el-table-column>
            <el-table-column property="paymentState" label="订单状态">
              <template v-slot="scope">
                <el-tag type="warning" v-if="scope.row.paymentState==1">待支付</el-tag>
                <el-tag type="success" v-if="scope.row.paymentState==2">支付完成</el-tag>
                <el-tag type="info" v-if="scope.row.paymentState==3">已过期</el-tag>
                <el-tag type="danger" v-if="scope.row.paymentState==4">支付失败</el-tag>
              </template>
            </el-table-column>
            <el-table-column property="paymentMethod" label="支付方式"/>
            <el-table-column property="paymentTime" label="订单创建时间">
              <template v-slot="scope">
                <span>{{ $utils.parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
            <el-table-column property="paymentTime" label="支付时间">
              <template v-slot="scope">
                <span>{{ $utils.parseTime(scope.row.paymentTime, '{y}-{m}-{d} {h}:{i}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160px">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="orderInformation(scope.row)">详情</el-button>
                <el-button v-if="scope.row.paymentState==1||scope.row.paymentState==4" type="text" size="small"
                           @click="orderPay(scope.row)">支付
                </el-button>
                <el-button type="text" size="small" @click="orderDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="total>0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="rechargeCentreMethod"
          />
        </div>
        <!--      订单详情-->
        <el-dialog :title="'订单ID：'+orderInformations.id" append-to-body :visible.sync="orderInfoLoading"
                   width="650px">
          <el-descriptions title="订单详情">
            <el-descriptions-item label="用户头像">
              <el-avatar v-if="orderInformations.avatar" :src="orderInformations.avatar" :size="30"></el-avatar>
              <el-avatar v-else src="/img/tx.jpg" :size="30"></el-avatar>
            </el-descriptions-item>
            <el-descriptions-item label="用户名称">{{ orderInformations.nickname }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ orderInformations.phone == null ? '--' : orderInformations.phone }}
            </el-descriptions-item>
            <el-descriptions-item label="职业">
              {{ orderInformations.occupation == null ? '--' : orderInformations.occupation }}
            </el-descriptions-item>
          </el-descriptions>
          <el-divider></el-divider>
          <el-descriptions>
            <el-descriptions-item label="商品名称">{{ orderInformations.commodityName }}</el-descriptions-item>
            <el-descriptions-item label="订单金额">{{ orderInformations.rechargeMoney }}￥</el-descriptions-item>
            <el-descriptions-item label="折扣">{{
                $utils.numberToText(orderInformations.discount)
              }}折
            </el-descriptions-item>
            <el-descriptions-item label="获得A币数量">{{ orderInformations.rechargeCurrency }}</el-descriptions-item>
            <el-descriptions-item label="支付方式">{{ orderInformations.paymentMethod }}</el-descriptions-item>
            <el-descriptions-item label="支付状态">
              <el-tag type="warning" size="small" v-if="orderInformations.paymentState==1">待支付</el-tag>
              <el-tag type="success" size="small" v-if="orderInformations.paymentState==2">支付完成</el-tag>
              <el-tag type="info" size="small" v-if="orderInformations.paymentState==3">已过期</el-tag>
              <el-tag type="danger" size="small" v-if="orderInformations.paymentState==4">支付失败</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="订单创建时间">
              {{ $utils.parseTime(orderInformations.createTime, '{y}-{m}-{d} {h}:{i}') }}
            </el-descriptions-item>
            <el-descriptions-item label="支付时间">
              {{ $utils.parseTime(orderInformations.paymentTime, '{y}-{m}-{d} {h}:{i}') }}
            </el-descriptions-item>
          </el-descriptions>
        </el-dialog>
      </el-drawer>
      <!--                  登陆弹层组件-->
      <LoginModule :isLogin="loginDialog" @loginDialogMethod="loginDialogMethod"></LoginModule>
    </div>
  </div>
</template>

<script>

export default {
  name: "userHome",
  data() {
    return {
      themeColor: process.env.THEME_COLOR,
      gridData: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      orderListLoading: true,
      total: 0,
      rechargeListArr: [],
      rechargeCentre: false,
      rechargeVisible: false,
      paymentInnerVisible: false,
      paymentInnerText: null,
      userListNavigation: null,
      uuid: null,
      userData: {
        uuid: null,
      },
      userDataLoading: true,
      userInfo: {
        nickname: null,
        avatar: null,
        occupation: null,
        introduce: null,
        uuid: null,
      },
      userInfos: {
        uuid: null,
      },
      currentUser: false,
      loginDialog: false,
      orderInformations: {
        id: null,
      },
      orderInfoLoading: false,
    }
  },
  methods: {
    orderInformation(item) {
      this.orderInformations = item;
      this.orderInfoLoading = true;
    },
    orderPay(item) {
      let path = this.$route.path + '?uuid=' + this.$route.query.uuid;
      sessionStorage.setItem('payUrl', path);
      let routeInfo = this.$router.resolve({
        path: "/api/white/payment/order/pay/" + item.id,
        name: "支付页面",
      });
      window.open(routeInfo.href, '_blank');
    },
    orderDelete(item) {
      this.$confirm('确定要删除该订单吗!', '提示', {
        confirmButtonText: '确 定',
        cancelButtonText: '取 消',
        type: 'warning'
      }).then(() => {
        this.$API("/frontDesk/order/delete/" + item.id, "delete").then(res => {
          if (res.code == 200) {
            this.$modal.msgSuccess("删除成功!");
            this.queryParams.pageNum = 1;
            this.orderListLoading = false;
            this.rechargeCentreMethod();
          }
        })
      });
    },
    pays(item) {
      let path = this.$route.path + '?uuid=' + this.$route.query.uuid;
      sessionStorage.setItem('payUrl', path);
      let routeInfo = this.$router.resolve({
        path: "/api/white/payment/pay/" + this.userInfos.uuid + "/" + item.id,
        name: "支付页面",
      });
      window.open(routeInfo.href, '_blank');
    },
    recharge() {
      this.$API("/frontDesk/rechargeInfo/list", "get").then(res => {
        this.rechargeListArr = res.rows;
        this.rechargeVisible = true;
      });
    },
    rechargeCentreMethod() {
      this.$API("/frontDesk/order/list", "get", this.queryParams).then(res => {
        this.gridData = res.rows;
        this.total = res.total;
        this.orderListLoading = false;
      })
      this.rechargeCentre = true;
    },
    privateLetter(uuid) {
      if (this.userInfos == null || this.userInfos.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      //添加私信记录
      this.$API("/frontDesk/private/user/add/" + uuid, "get").then(res => {
        this.$router.push({
          path: "/news/private-letter?code=" + this.$base64.encode(uuid),
        });
      })
    }
    ,
    loginDialogMethod(val) {
      this.loginDialog = val;
    }
    ,
    followClick(item) {
      if (this.userInfos == null || this.userInfos.userType != "tripartite_user") {
        this.loginDialog = true;
        return;
      }
      item.buttonLoading = true;
      if (item.isFollow) {
        item.isFollow = false;
        this.$API("/user/follow/cancel", "post", null, {targetId: item.uuid, type: 1,})
          .finally(() => {
            this.getWebsiteInfos()
            this.fdUserDatas()
            item.buttonLoading = false;
          });
      } else {
        item.isFollow = true;
        this.$API("/user/follow/add", "post", null, {targetId: item.uuid, type: 1,}).finally(() => {
          this.getWebsiteInfos()
          this.fdUserDatas()
          item.buttonLoading = false;
        });
      }
    }
    ,
    fdUserDatas() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/white/user/data/" + uuid, "get").then(res => {
        if (res.code == 200) {
          this.userData = res.data;
        }
      }).finally(() => this.userDataLoading = false)
    }
    ,
    //路由转译
    escapeUrl(url) {
      let urls = url.replace(/=/g, "%3D")
      return urls;
    }
    ,
    isCurrentUser() {
      let uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/front-desk/user/basics", "get").then(res => {
        if (res == null || res.data == null) {
          this.currentUser = false;
          return;
        }
        this.userInfos = res.data;
        if (res.data.uuid == uuid) {
          this.currentUser = true;
          return;
        }
      })
    }
    ,
    routeJump(url) {
      this.$router.push(url);
    },
    getWebsiteInfos() {
      this.uuid = this.$base64.decode(this.$route.query.uuid)
      this.$API("/white/user/info/" + this.uuid, "get").then(res => {
        this.userInfo = res.data;
      })
    },
    listNavigations() {
      let data = {type: 2, status: 0}
      this.$API("/white/configure/navigation/list", "get", data).then(res => {
        this.userListNavigation = res.rows;
      })
    }
    ,
  }
  ,
  watch: {
    // 监听路由是否变化
    '$route'() {
      this.getWebsiteInfos();
      this.isCurrentUser();
      this.fdUserDatas();
    }
  }
  ,
  mounted() {
    this.getWebsiteInfos();
    this.listNavigations();
    this.isCurrentUser();
    this.fdUserDatas();
  }
}
;
</script>

<style scoped>
@import url("~/assets/userHome/userHomeindex.css");

.user-data-cl p {
  margin-bottom: 15px;
}
</style>
