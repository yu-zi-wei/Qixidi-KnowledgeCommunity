<template>
  <div>
    <navigation-bar-module :isConceal="false"></navigation-bar-module>
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
                <svg t="1684217091163" class="icon-theme icon-size-20 svg-translateY-6 mr-2" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3691" data-spm-anchor-id="a313x.7781069.0.i7">
                  <path
                    d="M150.588235 542.117647a30.117647 30.117647 0 1 1 60.235294 0 271.058824 271.058824 0 0 0 271.058824 271.058824 30.117647 30.117647 0 1 1 0 60.235294C298.917647 873.411765 150.588235 725.082353 150.588235 542.117647z m722.82353-60.235294a30.117647 30.117647 0 1 1-60.235294 0 271.058824 271.058824 0 0 0-271.058824-271.058824 30.117647 30.117647 0 1 1 0-60.235294c182.964706 0 331.294118 148.329412 331.294118 331.294118zM512 1024C229.225412 1024 0 794.774588 0 512S229.225412 0 512 0s512 229.225412 512 512-229.225412 512-512 512z m0-60.235294c249.494588 0 451.764706-202.270118 451.764706-451.764706 0-249.494588-202.270118-451.764706-451.764706-451.764706C262.505412 60.235294 60.235294 262.505412 60.235294 512c0 249.494588 202.270118 451.764706 451.764706 451.764706z m0-331.294118a120.470588 120.470588 0 1 1 0-240.941176 120.470588 120.470588 0 0 1 0 240.941176z m0-60.235294a60.235294 60.235294 0 1 0 0-120.470588 60.235294 60.235294 0 0 0 0 120.470588z"
                    p-id="3692" data-spm-anchor-id="a313x.7781069.0.i6" class="selected"></path>
                </svg>
                创建的专辑 {{ userData.albumCount == 0 ? '--' : userData.albumCount }}
              </p>
              <p>
                <svg t="1684217048924" class="icon-theme icon-size-20 svg-translateY-6 mr-2" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3058">
                  <path
                    d="M176.056576 768c-15.36 0-29.184-4.096-40.96-12.8-17.92-13.312-28.672-38.4-31.744-74.24-9.216-98.816 36.864-260.608 142.848-378.88C453.048576 71.68 699.320576-13.824 999.352576 40.448c12.288 2.048 22.016 11.776 24.576 23.552 2.56 12.288-2.56 24.576-12.8 31.744-284.16 190.464-385.024 341.504-474.112 475.136-16.896 25.088-32.256 48.64-49.152 72.192-6.656 9.216-17.408 13.824-28.16 12.8-31.744-3.584-88.576 29.184-107.008 39.424l-1.024 0.512C280.504576 736.256 220.600576 768 176.056576 768z m-3.584-60.416z m-0.512-0.512c2.56 0.512 15.872 2.56 54.784-13.824 30.72-13.312 64.512-32.768 94.208-50.176l1.024-0.512c37.376-21.504 83.968-45.056 125.44-47.616 12.8-18.432 25.6-37.888 38.912-57.856C571.320576 409.6 667.064576 266.24 912.824576 89.6 669.112576 66.048 465.336576 148.992 291.256576 343.04c-134.656 150.016-139.776 339.456-119.296 364.032z"
                    p-id="3059"></path>
                  <path
                    d="M30.648576 980.992c-4.096 0-8.192-1.024-12.288-2.56-15.36-6.656-22.528-24.576-15.872-39.936 155.648-358.4 485.888-620.032 500.224-630.784 13.312-10.24 32.256-8.192 43.008 5.12 10.24 13.312 8.192 32.256-5.12 43.008-3.072 2.56-332.288 263.168-481.792 607.232-5.12 11.264-16.384 17.92-28.16 17.92z"
                    p-id="3060"></path>
                  <path
                    d="M331.192576 537.6h-1.536c-16.896-1.024-29.696-14.848-29.184-31.744l11.776-253.952c1.024-16.896 14.848-29.696 31.744-29.184 16.896 1.024 29.696 14.848 29.184 31.744l-11.776 253.952c-0.512 16.384-13.824 29.184-30.208 29.184zM905.656576 999.424c-3.584 0-7.68-0.512-11.264-2.048-44.032-17.92-68.608-46.592-88.064-69.12-23.04-26.624-35.84-41.984-71.68-40.448-31.232 1.024-50.688 20.992-72.704 44.544-27.648 28.672-61.952 64.512-120.32 51.712-47.616-10.24-71.68-39.936-91.648-63.488-20.992-25.6-35.328-42.496-74.24-42.496h-0.512c-68.096 0.512-142.336 62.464-165.376 86.528-11.776 12.288-30.72 12.8-43.008 1.024-12.288-11.776-12.8-30.72-1.024-43.008 4.096-4.096 101.376-104.448 208.896-104.96h1.024c67.584 0 97.28 35.84 121.344 64.512 17.408 20.992 30.72 37.376 57.856 43.008 23.04 5.12 35.84-5.632 63.488-34.304 26.112-27.136 58.88-61.44 114.688-63.488 65.536-2.56 94.208 31.744 120.32 61.44 17.92 20.48 34.304 39.936 65.024 52.736 15.36 6.144 23.04 24.064 16.896 39.936-6.144 10.752-17.408 17.92-29.696 17.92z"
                    p-id="3061"></path>
                </svg>

                记录名言数 {{ userData.dictumCount == 0 ? '--' : userData.dictumCount }}
              </p>
              <p>
                <svg t="1676383484417" class="icon icon-size-20 svg-translateY-6 mr-2" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3708">
                  <path d="M0 0h1024v1024H0V0z" fill="#202425" opacity=".01" p-id="3709"></path>
                  <path
                    d="M682.666667 204.8h238.933333a34.133333 34.133333 0 0 1 34.133333 34.133333v648.533334a68.266667 68.266667 0 0 1-68.266666 68.266666h-204.8V204.8z"
                    fill="#ffa502" p-id="3710" data-spm-anchor-id="a313x.7781069.0.i3" class="selected"></path>
                  <path
                    d="M68.266667 921.6a34.133333 34.133333 0 0 0 34.133333 34.133333h785.066667a68.266667 68.266667 0 0 1-68.266667-68.266666V102.4a34.133333 34.133333 0 0 0-34.133333-34.133333H102.4a34.133333 34.133333 0 0 0-34.133333 34.133333v819.2z"
                    fill="#5fb8e7" p-id="3711" data-spm-anchor-id="a313x.7781069.0.i2" class=""></path>
                  <path
                    d="M238.933333 307.2a34.133333 34.133333 0 0 0 0 68.266667h136.533334a34.133333 34.133333 0 1 0 0-68.266667H238.933333z m0 204.8a34.133333 34.133333 0 1 0 0 68.266667h409.6a34.133333 34.133333 0 1 0 0-68.266667H238.933333z m0 204.8a34.133333 34.133333 0 1 0 0 68.266667h204.8a34.133333 34.133333 0 1 0 0-68.266667H238.933333z"
                    fill="#FFFFFF" p-id="3712"></path>
                </svg>
                发布文章 {{ userData.articleCount == 0 ? '--' : userData.articleCount }}
              </p>
              <p>
                <svg t="1676383543481" class="icon-theme icon-size-20 svg-translateY-6 mr-2" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="4831">
                  <path
                    d="M512 85.333333c129.6 0 234.666667 105.066667 234.666667 234.666667 0 84.256-44.394667 158.133333-111.072 199.52a425.28 425.28 0 0 1 152.853333 83.466667 32 32 0 1 1-41.493333 48.736A361.045333 361.045333 0 0 0 512 565.333333c-188.672 0-345.429333 144.672-361.344 331.413334a32 32 0 0 1-63.765333-5.429334c15.114667-177.322667 138.048-322.346667 301.546666-371.786666C321.76 478.165333 277.333333 404.266667 277.333333 320c0-129.6 105.066667-234.666667 234.666667-234.666667z m415.946667 627.381334l1.066666 1.013333a29.824 29.824 0 0 1 0 43.413333l-162.261333 152.96a31.925333 31.925333 0 0 1-22.762667 8.704 31.925333 31.925333 0 0 1-22.773333-8.704l-93.184-87.84a29.824 29.824 0 0 1 0-43.413333l1.077333-1.013333a32 32 0 0 1 43.904 0l70.976 66.901333 140.053334-132.021333a32 32 0 0 1 43.904 0zM512 149.333333c-94.261333 0-170.666667 76.405333-170.666667 170.666667s76.405333 170.666667 170.666667 170.666667 170.666667-76.405333 170.666667-170.666667-76.405333-170.666667-170.666667-170.666667z"
                    p-id="4832"></path>
                </svg>
                被关注 {{ userData.fansFollowCount == 0 ? '--' : userData.fansFollowCount }}
              </p>
              <p>
                <svg t="1676383606938" class="icon icon-size-20 svg-translateY-6 mr-2" viewBox="0 0 1024 1024"
                     version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="777">
                  <path
                    d="M896 170.7H128c-23.6 0-42.7 19.1-42.7 42.7v597.3c0 23.6 19.1 42.7 42.7 42.7h768c23.6 0 42.7-19.1 42.7-42.7V213.3c0-23.5-19.1-42.6-42.7-42.6zM633.5 534.3l-89 89c-8.7 8.7-20.2 13.5-32.5 13.5s-23.8-4.8-32.5-13.5l-89-89c-16.4-16.3-25.4-38-25.5-61.2 0-23.2 9-45 25.4-61.4s38.2-25.4 61.3-25.4c22.7 0 44.1 8.7 60.3 24.5 16.3-15.8 37.7-24.5 60.4-24.5 23.2 0 44.9 9 61.3 25.5 16.4 16.4 25.4 38.2 25.4 61.4-0.1 23-9.2 44.8-25.6 61.1z"
                    fill="#5fb8e7" p-id="778" data-spm-anchor-id="a313x.7781069.0.i0" class=""></path>
                  <path
                    d="M810.7 768H213.3c-23.6 0-42.7-19.1-42.7-42.7s19.1-42.7 42.7-42.7h597.3c23.6 0 42.7 19.1 42.7 42.7S834.2 768 810.7 768zM421.5 442.8c16.7-16.7 43.7-16.7 60.3 0l28.8 28.8c0.8 0.8 2.1 0.8 2.8 0l28.8-28.8c16.7-16.7 43.7-16.7 60.3 0 16.7 16.7 16.7 43.7 0 60.3l-89.1 89.1c-0.8 0.8-2 0.8-2.8 0l-89.1-89.1c-16.7-16.6-16.7-43.6 0-60.3zM810.7 341.3H213.3c-23.6 0-42.7-19.1-42.7-42.7s19.1-42.7 42.7-42.7h597.3c23.6 0 42.7 19.1 42.7 42.7s-19.1 42.7-42.6 42.7z"
                    fill="#fefefe" p-id="779" data-spm-anchor-id="a313x.7781069.0.i1" class="selected"></path>
                </svg>
                专栏 {{ userData.specialColumnCount == 0 ? '--' : userData.specialColumnCount }}
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
      <el-dialog :title="'订单ID：'+orderInformations.id" append-to-body :visible.sync="orderInfoLoading" width="650px">
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
        this.userData = res.data;
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
        if (res == null) {
          this.currentUser = false;
          return;
        }
        this.userInfos = res.data;
        if (res.data != null && res.data.uuid == uuid) {
          this.currentUser = true;
          return;
        }
      })
    }
    ,
    routeJump(url) {
      this.$router.push(url);
    }
    ,
    routeJumpUuid(url, id) {
      this.$router.push({
        path: url,
        query: {uuid: this.$base64.encode(id)}
      })
    }
    ,
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
</style>
