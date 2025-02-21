<template>
  <div class="index-admin">
    <div class="background-color-top-2">
      <!--        顶部默认-->
      <div class="index-title">
        <div class="cursor-pointer">
          <router-link style="color: #fefefe" to="/" title="首页">
            <span class="font-bold" v-text=" listInfoData.title==null?'爱情小站': listInfoData.title"></span>
          </router-link>
        </div>

        <div class="color-fe font-s-14 disappear-789">
          <span v-text="listInfoData.introduce==null?'爱情小站': listInfoData.introduce"></span>
        </div>
      </div>
      <!--        默认展示-->
      <div v-if="1==2">
        <div class="middle-info">
          <div>
            <div class="avatar-img">
              <img v-if="listInfoData.leftImg" :src="listInfoData.leftImg"/>
              <img v-else alt="avatar"
                   src="https://img1.baidu.com/it/u=2238292066,3745763495&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1671286226&t=476fc00114ed522fbb164fa0e186d921"/>
            </div>
            <div class="avatar-name"> {{ listInfoData.leftName }}</div>
          </div>

          <div>
            <router-link to="/" title="首页">
              <div class="aixing-cl">
                <img src="./img/aixin.png">
              </div>
            </router-link>
          </div>

          <div>
            <div class="avatar-img">
              <img v-if="listInfoData.rightImg" :src="listInfoData.rightImg"/>
              <img v-else alt="avatar"
                   src="https://img1.baidu.com/it/u=2238292066,3745763495&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1671286226&t=476fc00114ed522fbb164fa0e186d921"/>
            </div>
            <div class="avatar-name"> {{ listInfoData.rightName }}</div>
          </div>
          <!--                       src="https://img1.baidu.com/it/u=4010544844,625971989&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1671286226&t=cda94a523e0bc745eaee361c472dae94"/>-->
        </div>
      </div>
      <!--        下滑出现-->
      <div v-show="!topScrollBar.isTop" class="top-la-cl">
        <div class="middle-info-xhua">
          <div class="avatar-img-xhua">
            <img v-if="listInfoData.leftImg" alt="avatar" :src="listInfoData.leftImg"/>
            <img v-else alt="avatar"
                 src="https://img1.baidu.com/it/u=2238292066,3745763495&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1671286226&t=476fc00114ed522fbb164fa0e186d921"/>
          </div>
          <div>
            <router-link to="/" title="首页">
              <div class="aixing-ma-cl">
                <div class="aixing-cl-xhua">
                  <img src="./img/aixin.png">
                </div>
              </div>
            </router-link>
          </div>
          <div class="avatar-img-xhua">
            <img v-if="listInfoData.rightImg" alt="avatar" :src="listInfoData.rightImg"/>
            <img v-else alt="avatar"
                 src="https://img1.baidu.com/it/u=4010544844,625971989&fm=253&app=53&size=w500&n=0&g=0n&f=jpeg?sec=1671286226&t=cda94a523e0bc745eaee361c472dae94"/>
          </div>
        </div>
      </div>
      <!--        波浪-->
      <div class="wave-header">
        <div>
          <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
               viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
            <defs>
              <path id="gentle-wave"
                    d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z"/>
            </defs>
            <g class="parallax">
              <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7)"/>
              <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)"/>
              <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)"/>
              <use xlink:href="#gentle-wave" x="48" y="7" fill="rgba(255,255,255,0.9)"/>
            </g>
          </svg>
        </div>
      </div>
    </div>
    <div class="grid-view-class">
      <router-view></router-view>
    </div>
    <div class="go-top-cl" @click="goTop" title="回到顶部" v-show="!topScrollBar.isTop">
      <svg t="1669603221736" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="1303" width="20" height="20">
        <path
            d="M383.786667 640.853333a42.666667 42.666667 0 0 1-8.192 56.149334l-3.968 2.986666-64.64 42.496a42.666667 42.666667 0 0 0-16.085334 20.138667l-1.621333 5.034667L267.946667 853.333333H384a42.666667 42.666667 0 0 1 42.368 37.674667L426.666667 896a42.666667 42.666667 0 0 1-37.674667 42.368L384 938.666667H213.333333a42.666667 42.666667 0 0 1-42.325333-48.213334l0.938667-4.778666 34.56-138.666667a128.042667 128.042667 0 0 1 46.250666-70.570667l7.082667-5.12 64.853333-42.666666a42.666667 42.666667 0 0 1 59.093334 12.202666z m311.168-14.677333l4.352 2.517333 65.066666 42.794667a128 128 0 0 1 50.773334 67.2l2.346666 8.32 34.56 138.666667a42.666667 42.666667 0 0 1-36.565333 52.736L810.666667 938.666667h-170.666667a42.666667 42.666667 0 0 1-4.992-85.034667L640 853.333333h116.053333l-21.333333-85.674666a42.709333 42.709333 0 0 0-13.397333-21.888l-4.096-3.157334-64.853334-42.666666a42.666667 42.666667 0 0 1 37.76-75.861334l4.821334 2.133334z"
            fill="#ffffff" p-id="1304"></path>
        <path
            d="M512 102.570667a85.333333 85.333333 0 0 0-60.586667 25.258666l-30.165333 30.165334a469.333333 469.333333 0 0 0-131.882667 403.2l52.48 341.290666A42.666667 42.666667 0 0 0 384 938.666667h256a42.666667 42.666667 0 0 0 42.154667-36.181334l52.48-341.333333a469.333333 469.333333 0 0 0-131.84-403.157333l-30.293334-30.293334A85.248 85.248 0 0 0 512 102.570667z m-30.421333 115.754666L512 187.946667l30.421333 30.378666 9.088 9.386667a384 384 0 0 1 98.816 320.426667L603.306667 853.333333H420.608l-46.933333-305.152a384 384 0 0 1 107.946666-329.856z"
            fill="#ffffff" p-id="1305"></path>
        <path
            d="M512 384a42.666667 42.666667 0 0 1 42.368 37.674667L554.666667 426.666667v128a42.666667 42.666667 0 0 1-85.034667 4.992L469.333333 554.666667v-128a42.666667 42.666667 0 0 1 42.666667-42.666667z"
            fill="#ffffff" p-id="1306"></path>
      </svg>
    </div>
    <!--    备案信息-->
    <div class="filings-info background-color-end" v-if="$route.path=='/'">
      <i title="域名">
        <svg t="1675586783657" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
             p-id="4830" width="16" height="16" style="position:relative; top:4px;right: 2px">
          <path
              d="M319.9 428.9h37.7l-66.1 216.7H259c-28.9-98-43.9-149.1-44.9-154.3-2.1-4.6-3.1-12.4-3.1-23.2h-1c0 4.6-1 12.4-3.1 23.2-2.1 4.6-18.1 56.2-48 154.3h-32.5l-63-216.7h37.7c23.7 91.3 36.6 141.4 38.7 149.6 0 0.5 0.5 1.5 1 3.1 1.5 9.8 2.1 17 2.1 21.2h1c0-7.7 1.5-15.5 4.1-24.3 2.1-5.7 17-55.7 44.9-149.6h35.6c25.8 94.4 39.7 144.5 41.8 149.6 2.6 8.3 4.1 16.5 4.1 24.3h1c0-4.1 0.5-11.4 2.1-21.2 0.5-1.5 1-2.6 1-3.1 2.7-8.2 16.1-58.2 41.4-149.6z m300.8 0h37.7l-66.1 216.7h-32.5c-28.9-98-43.9-149.1-44.9-154.3-2.1-4.6-3.1-12.4-3.1-23.2h-1c0 4.6-1 12.4-3.1 23.2-2.1 4.6-18.1 56.2-48 154.3h-32.5l-63-216.7h37.7c23.7 91.3 36.6 141.4 38.7 149.6 0 0.5 0.5 1.5 1 3.1 1.5 9.8 2.1 17 2.1 21.2h0.5c0-7.7 1.5-15.5 4.1-24.3 2.1-5.7 17-55.7 44.9-149.6h35.6c25.8 94.4 39.7 144.5 41.8 149.6 2.6 8.3 4.1 16.5 4.1 24.3h1c0-4.1 0.5-11.4 2.1-21.2 0.5-1.5 1-2.6 1-3.1 3.2-8.2 16.6-58.2 41.9-149.6z m300.9 0h37.7l-66.1 216.7h-32.5c-28.9-98-43.9-149.1-44.9-154.3-2.1-4.6-3.1-12.4-3.1-23.2h-1c0 4.6-1 12.4-3.1 23.2-2.1 4.6-18.1 56.2-48 154.3h-32.5l-63-216.7h37.7c23.7 91.3 36.6 141.4 38.7 149.6 0 0.5 0.5 1.5 1 3.1 1.5 9.8 2.1 17 2.1 21.2h1c0-7.7 1.5-15.5 4.1-24.3 2.1-5.7 17-55.7 44.9-149.6h35.6C856 523.4 870 573.4 872 578.6c2.6 8.3 4.1 16.5 4.1 24.3h1c0-4.1 0.5-11.4 2.1-21.2 0.5-1.5 1-2.6 1-3.1 2.7-8.3 16.1-58.3 41.4-149.7z m-755.5-39.2C222.9 254 357 158.5 513.4 158.5S803.9 254 860.7 389.7h45.9c-58.8-160-212.6-274-393.2-274s-333.9 114-393.2 274h45.9z m691 299.3c-58.8 131.1-190.4 222.9-343.7 222.9-153.3 0-284.8-91.3-343.7-222.9h-46.4c61.4 155.3 212.6 265.2 390.1 265.2 177 0 328.7-109.9 390.1-265.2h-46.4z"
              fill="#ffffff" p-id="4831"></path>
          <path d="M513.4 217.8c-123.2 0-230.1 69.7-283.4 171.9h-14.9c54.4-109.8 167.6-185.2 298.4-185.2"
                fill="#ffffff"
                p-id="4832"></path>
        </svg>
        <span v-text="configInfoData.realmName"></span>
      </i>
      <i title="备案">
        <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">
          <svg t=" 1675586978710" class="icon" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg"
               p-id="5685" width="16" height="16" style="position:relative; top:4px;right: 2px">
            <path
                d="M778.24 163.84c-76.8-40.96-165.888-61.44-269.312-61.44s-192.512 20.48-269.312 61.44h-133.12l23.552 337.92c8.192 113.664 67.584 217.088 162.816 280.576l215.04 144.384 215.04-144.384c96.256-63.488 155.648-166.912 163.84-280.576l23.552-337.92H778.24z m47.104 333.824c-7.168 94.208-56.32 181.248-135.168 233.472l-181.248 120.832L327.68 731.136c-78.848-53.248-129.024-139.264-135.168-233.472L173.056 225.28h136.192v-26.624c58.368-23.552 124.928-34.816 199.68-34.816s141.312 12.288 199.68 34.816V225.28H844.8l-19.456 272.384z"
                fill="#ffffff" p-id="5686"></path>
            <path
                d="M685.056 328.704v-46.08H455.68c2.048-4.096 6.144-9.216 11.264-15.36 5.12-7.168 9.216-12.288 11.264-15.36L419.84 240.64c-31.744 46.08-75.776 87.04-133.12 123.904 4.096 4.096 10.24 11.264 18.432 21.504l17.408 17.408c23.552-15.36 45.056-31.744 63.488-50.176 26.624 25.6 49.152 43.008 67.584 51.2-46.08 15.36-104.448 27.648-175.104 35.84 2.048 5.12 6.144 13.312 9.216 24.576 4.096 11.264 6.144 19.456 7.168 24.576l39.936-7.168v218.112H389.12V680.96h238.592v19.456h54.272V481.28H348.16c60.416-12.288 114.688-27.648 163.84-46.08 49.152 19.456 118.784 34.816 210.944 46.08 5.12-17.408 10.24-34.816 17.408-51.2-62.464-4.096-116.736-12.288-161.792-24.576 38.912-20.48 74.752-46.08 106.496-76.8z m-150.528 194.56h94.208v41.984h-94.208v-41.984z m0 78.848h94.208v41.984h-94.208v-41.984z m-144.384-78.848h94.208v41.984H390.144v-41.984z m0 78.848h94.208v41.984H390.144v-41.984zM424.96 326.656h182.272c-26.624 22.528-57.344 41.984-94.208 57.344-31.744-15.36-61.44-34.816-88.064-57.344z"
                fill="#ffffff" p-id="5687"></path>
          </svg>
          <span v-text="configInfoData.filings"></span> .
          <span v-text="configInfoData.securityFilings"></span>
        </a>
      </i>
      <i title="反馈邮箱">
        <svg t="1675587156257" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
             p-id="7666" width="16" height="16" style="position:relative; top:4px;right: 2px">
          <path
              d="M838.954667 234.666667H170.666667c-3.626667 0-7.168 0.448-10.56 1.322666l323.690666 323.669334a21.333333 21.333333 0 0 0 30.165334 0L838.954667 234.666667z m46.144 14.186666l-260.693334 260.693334 262.933334 262.912c5.44-7.168 8.661333-16.106667 8.661333-25.792V277.333333c0-10.944-4.117333-20.906667-10.88-28.48zM843.861333 789.333333l-249.6-249.621333-50.133333 50.133333a64 64 0 0 1-90.517333 0l-50.112-50.133333L156.373333 786.88c4.48 1.578667 9.28 2.453333 14.314667 2.453333h673.194667zM128.661333 754.218667L373.333333 509.525333 129.578667 265.813333A42.709333 42.709333 0 0 0 128 277.333333v469.333334c0 2.56 0.213333 5.098667 0.661333 7.552zM170.666667 192h682.666666a85.333333 85.333333 0 0 1 85.333334 85.333333v469.333334a85.333333 85.333333 0 0 1-85.333334 85.333333H170.666667a85.333333 85.333333 0 0 1-85.333334-85.333333V277.333333a85.333333 85.333333 0 0 1 85.333334-85.333333z"
              fill="#ffffff" p-id="7667"></path>
        </svg>
        <span v-text="configInfoData.mail"></span>
      </i>
    </div>
  </div>
</template>

<script>
import './css/index.css'
import './css/index-media.css'
import './css/wave.css'
import {configInfo, listInfo} from "@/api/lover";

export default {
  name: "index",
  data() {
    return {
      loading: true,
      listInfoData: {},
      topScrollBar: {
        isTop: true,
        inlineTop: '',
      },
      configInfoData: {
        filings: null,
        mail: null,
        realmName: null,
        securityFilings: null,
      },
      urlList: ['/'],
      availHeight: window.screen.availHeight / 2,
    }
  },
  methods: {
    configInfos() {
      configInfo().then(res => {
        this.configInfoData = res;
      });
    },
    goTop() {
      window.scrollTo({top: 0, behavior: "smooth"})
    },
    listInfos() {
      let data = {state: 0}
      listInfo(data).then(res => {
        this.listInfoData = res;
        this.loading = false;
      })
    },
    getTopData() {
      let scrollTop = document.documentElement.scrollTop
      let isIncludes = this.urlList.includes(window.location.pathname);
      if (!isIncludes && scrollTop >= (this.availHeight)) {
        this.topScrollBar.isTop = false;
      } else {
        this.topScrollBar.isTop = true;
      }
      // console.log('底部:', "scrollTop:", scrollTop,
      //     "this.availHeight:", this.availHeight,
      //     " this.topScrollBar.isTop:", this.topScrollBar.isTop,
      // )
    },
  },
  mounted() {
    this.listInfos();
    this.configInfos();
  },
  created() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getTopData, true);
  },
  unmounted() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getTopData, true)
  }
}
</script>
<style>
.grid-view-class {
  background-size: 46px 46px;
  background-position: center center;
  background-image: linear-gradient(to right, #cbd5e1 1px, transparent 1px),
  linear-gradient(to bottom, #cbd5e1 1px, transparent 1px);
  -webkit-mask-image: linear-gradient(to bottom, transparent, #fff 50px calc(100% - 50px), transparent),
  linear-gradient(to right, transparent, #fff 50px calc(100% - 50px), transparent);
  mask-image: linear-gradient(to bottom, transparent, #fff 50px calc(100% - 50px), transparent),
  linear-gradient(to right, transparent, #fff 50px calc(100% - 50px), transparent);
  mask-composite: intersect;
  -webkit-mask-composite: source-in, xor;
  padding-bottom: 60px;
  padding-top: 40px;
}

.sbw-cl {
  margin-top: 40px;
  height: 100px;
  background: url("data:image/svg+xml;base64,IDxzdmcgd2lkdGg9IjEwMCUiIGhlaWdodD0iMTAwJSIgaWQ9InN2ZyIgdmlld0JveD0iMCAwIDE0NDAgNDAwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiAgICAgICAgICAgICBjbGFzcz0idHJhbnNpdGlvbiBkdXJhdGlvbi0zMDAgZWFzZS1pbi1vdXQgZGVsYXktMTUwIiBzdHlsZT0icGFkZGluZzogMHB4O2JvcmRlcjogbm9uZSI+CiAgICAgICAgICA8cGF0aAogICAgICAgICAgICAgIGQ9Ik0gMCw0MDAgQyAwLDQwMCAwLDEwMCAwLDEwMCBDIDI0LjUyNDQ3ODY0NDE1Nzg0LDk1LjUzNzQzNTQwOTQzNjk3IDQ5LjA0ODk1NzI4ODMxNTY4LDkxLjA3NDg3MDgxODg3Mzk1IDgzLDkwIEMgMTE2Ljk1MTA0MjcxMTY4NDMyLDg4LjkyNTEyOTE4MTEyNjA1IDE2MC4zMjg2NDk0OTA4OTUxNSw5MS4yMzc5NTIxMzM5NDExOCAxOTEsODIgQyAyMjEuNjcxMzUwNTA5MTA0ODUsNzIuNzYyMDQ3ODY2MDU4ODIgMjM5LjYzNjQ0NDc0ODEwMzc3LDUxLjk3MzMyMDY0NTM2MTM1NSAyNzAsNjMgQyAzMDAuMzYzNTU1MjUxODk2MjMsNzQuMDI2Njc5MzU0NjM4NjUgMzQzLjEyNTU3MTUxNjY4OTYsMTE2Ljg2ODc2NTI4NDYxMzM3IDM3OSwxMjIgQyA0MTQuODc0NDI4NDgzMzEwNCwxMjcuMTMxMjM0NzE1Mzg2NjMgNDQzLjg2MTI2OTE4NTEzNzg3LDk0LjU1MTYxODIxNjE4NTIyIDQ4MSw4OSBDIDUxOC4xMzg3MzA4MTQ4NjIxLDgzLjQ0ODM4MTc4MzgxNDc4IDU2My40MjkzNTE3NDI3NTkxLDEwNC45MjQ3NjE4NTA2NDU3IDU5MCwxMTcgQyA2MTYuNTcwNjQ4MjU3MjQwOSwxMjkuMDc1MjM4MTQ5MzU0MyA2MjQuNDIxMzIzODQzODI1NywxMzEuNzQ5MzM0MzgxMjMyMDIgNjU1LDEyOSBDIDY4NS41Nzg2NzYxNTYxNzQzLDEyNi4yNTA2NjU2MTg3Njc5OCA3MzguODg1MzUyODgxOTM4MiwxMTguMDc3OTAwNjI0NDI2MjIgNzc0LDExNSBDIDgwOS4xMTQ2NDcxMTgwNjE4LDExMS45MjIwOTkzNzU1NzM3OCA4MjYuMDM3MjY0NjI4NDIxNywxMTMuOTM5MDYzMTIxMDYzMTEgODU3LDExNiBDIDg4Ny45NjI3MzUzNzE1NzgzLDExOC4wNjA5MzY4Nzg5MzY4OSA5MzIuOTY1NTg4NjA0Mzc1NSwxMjAuMTY1ODQ2ODkxMzIxMzUgOTcwLDExMyBDIDEwMDcuMDM0NDExMzk1NjI0NSwxMDUuODM0MTUzMTA4Njc4NjUgMTAzNi4xMDAzODA5NTQwNzY0LDg5LjM5NzU0OTMxMzY1MTQ3IDEwNjYsNzYgQyAxMDk1Ljg5OTYxOTA0NTkyMzYsNjIuNjAyNDUwNjg2MzQ4NTMgMTEyNi42MzI4ODc1NzkzMTk3LDUyLjI0Mzk1NTg1NDA3Mjc5NCAxMTU4LDY2IEMgMTE4OS4zNjcxMTI0MjA2ODAzLDc5Ljc1NjA0NDE0NTkyNzIgMTIyMS4zNjgwNjg3Mjg2NDQ2LDExNy42MjY2MjcyNzAwNTczMyAxMjU2LDEyNiBDIDEyOTAuNjMxOTMxMjcxMzU1NCwxMzQuMzczMzcyNzI5OTQyNjcgMTMyNy44OTQ4Mzc1MDYxMDEzLDExMy4yNDk1MzUwNjU2OTc5IDEzNTksMTA0IEMgMTM5MC4xMDUxNjI0OTM4OTg3LDk0Ljc1MDQ2NDkzNDMwMjEgMTQxNS4wNTI1ODEyNDY5NDkzLDk3LjM3NTIzMjQ2NzE1MTA1IDE0NDAsMTAwIEMgMTQ0MCwxMDAgMTQ0MCw0MDAgMTQ0MCw0MDAgWiIKICAgICAgICAgICAgICBzdHJva2U9Im5vbmUiIHN0cm9rZS13aWR0aD0iMCIgZmlsbD0iI2JlMmVkZDY2IgogICAgICAgICAgICAgIGNsYXNzPSJ0cmFuc2l0aW9uLWFsbCBkdXJhdGlvbi0zMDAgZWFzZS1pbi1vdXQgZGVsYXktMTUwIHBhdGgtMCI+PC9wYXRoPgogICAgICAgICAgPHBhdGgKICAgICAgICAgICAgICBkPSJNIDAsNDAwIEMgMCw0MDAgMCwyMDAgMCwyMDAgQyAyNi4yNjUyNTc4MjcxMjA4OTgsMjA1LjI2ODk3MTQ4Njk3NzgyIDUyLjUzMDUxNTY1NDI0MTc5NiwyMTAuNTM3OTQyOTczOTU1NjcgODgsMjE4IEMgMTIzLjQ2OTQ4NDM0NTc1ODIsMjI1LjQ2MjA1NzAyNjA0NDMzIDE2OC4xNDMxOTUyMTAxNTM3LDIzNS4xMTcxOTk1OTExNTUyIDIwMCwyMjcgQyAyMzEuODU2ODA0Nzg5ODQ2MywyMTguODgyODAwNDA4ODQ0OCAyNTAuODk2NzAzNTA1MTQzNDgsMTkyLjk5MzI1ODY2MTQyMzUgMjc1LDE5MyBDIDI5OS4xMDMyOTY0OTQ4NTY1LDE5My4wMDY3NDEzMzg1NzY1IDMyOC4yNjk5OTA3NjkyNzIxNywyMTguOTA5NzY1NzYzMTUwNzIgMzY2LDIyMCBDIDQwMy43MzAwMDkyMzA3Mjc4MywyMjEuMDkwMjM0MjM2ODQ5MjggNDUwLjAyMzMzMzQxNzc2NzczLDE5Ny4zNjc2NzgyODU5NzM2NSA0OTEsMTk4IEMgNTMxLjk3NjY2NjU4MjIzMjMsMTk4LjYzMjMyMTcxNDAyNjM1IDU2Ny42MzY2NzU1NTk2NTcxLDIyMy42MTk1MjEwOTI5NTQ2NCA1OTMsMjIzIEMgNjE4LjM2MzMyNDQ0MDM0MjksMjIyLjM4MDQ3ODkwNzA0NTM2IDYzMy40Mjk5NjQzNDM2MDQxLDE5Ni4xNTQyMzczNDIyMDc2NCA2NjIsMTgyIEMgNjkwLjU3MDAzNTY1NjM5NTksMTY3Ljg0NTc2MjY1Nzc5MjM2IDczMi42NDM0NjcwNjU5MjcxLDE2NS43NjM1Mjk1MzgyMTQ4NCA3NjksMTc2IEMgODA1LjM1NjUzMjkzNDA3MjksMTg2LjIzNjQ3MDQ2MTc4NTE2IDgzNS45OTYxNjczOTI2ODc4LDIwOC43OTE2NDQ1MDQ5MzI5IDg3MCwyMDQgQyA5MDQuMDAzODMyNjA3MzEyMiwxOTkuMjA4MzU1NDk1MDY3MSA5NDEuMzcxODYzMzYzMzIxNCwxNjcuMDY5ODkyNDQyMDUzNSA5NzIsMTcwIEMgMTAwMi42MjgxMzY2MzY2Nzg2LDE3Mi45MzAxMDc1NTc5NDY1IDEwMjYuNTE2Mzc5MTU0MDI3NCwyMTAuOTI4Nzg1NzI2ODUzMDggMTA1MiwyMTUgQyAxMDc3LjQ4MzYyMDg0NTk3MjYsMjE5LjA3MTIxNDI3MzE0NjkyIDExMDQuNTYyNjIwMDIwNTcsMTg5LjIxNDk2NDY1MDUzNDIzIDExMzYsMTgxIEMgMTE2Ny40MzczNzk5Nzk0MywxNzIuNzg1MDM1MzQ5NDY1NzcgMTIwMy4yMzMxNDA3NjM2OTI1LDE4Ni4yMTEzNTU2NzEwMTAwNCAxMjM1LDE5MiBDIDEyNjYuNzY2ODU5MjM2MzA3NSwxOTcuNzg4NjQ0MzI4OTg5OTYgMTI5NC41MDQ4MTY5MjQ2NTksMTk1LjkzOTYxMjY2NTQyNTcgMTMyOCwxOTYgQyAxMzYxLjQ5NTE4MzA3NTM0MSwxOTYuMDYwMzg3MzM0NTc0MyAxNDAwLjc0NzU5MTUzNzY3MDQsMTk4LjAzMDE5MzY2NzI4NzE2IDE0NDAsMjAwIEMgMTQ0MCwyMDAgMTQ0MCw0MDAgMTQ0MCw0MDAgWiIKICAgICAgICAgICAgICBzdHJva2U9Im5vbmUiIHN0cm9rZS13aWR0aD0iMCIgZmlsbD0iI2JlMmVkZDg4IgogICAgICAgICAgICAgIGNsYXNzPSJ0cmFuc2l0aW9uLWFsbCBkdXJhdGlvbi0zMDAgZWFzZS1pbi1vdXQgZGVsYXktMTUwIHBhdGgtMSI+PC9wYXRoPgogICAgICAgICAgPHBhdGgKICAgICAgICAgICAgICBkPSJNIDAsNDAwIEMgMCw0MDAgMCwzMDAgMCwzMDAgQyAyMy43NDExMDk4NzM1MDEzOCwzMTMuMDEwMTk5MTEyMTYxNDQgNDcuNDgyMjE5NzQ3MDAyNzYsMzI2LjAyMDM5ODIyNDMyMjkgODAsMzIwIEMgMTEyLjUxNzc4MDI1Mjk5NzI0LDMxMy45Nzk2MDE3NzU2NzcxIDE1My44MTIyMzA4ODU0OTAzNSwyODguOTI4NjA2MjE0ODY5OSAxOTAsMjgzIEMgMjI2LjE4Nzc2OTExNDUwOTY1LDI3Ny4wNzEzOTM3ODUxMzAxIDI1Ny4yNjg4NTY3MTEwMzU3NywyOTAuMjY1MTc2OTE2MTk3NSAyODQsMjk4IEMgMzEwLjczMTE0MzI4ODk2NDIzLDMwNS43MzQ4MjMwODM4MDI1IDMzMy4xMTIzNDIyNzAzNjY2LDMwOC4wMTA2ODYxMjAzMzk5IDM3MSwzMTIgQyA0MDguODg3NjU3NzI5NjMzNCwzMTUuOTg5MzEzODc5NjYwMSA0NjIuMjgxNzc0MjA3NDk4MSwzMjEuNjkyMDc4NjAyNDQyNzMgNDk2LDMyNiBDIDUyOS43MTgyMjU3OTI1MDE5LDMzMC4zMDc5MjEzOTc1NTcyNyA1NDMuNzYwNTYwODk5NjQxMSwzMzMuMjIwOTk5NDY5ODg5MjUgNTY3LDMyNSBDIDU5MC4yMzk0MzkxMDAzNTg5LDMxNi43NzkwMDA1MzAxMTA3NSA2MjIuNjc1OTgyMTkzOTM3NiwyOTcuNDIzOTIzNTE4MDAwNCA2NjIsMjg4IEMgNzAxLjMyNDAxNzgwNjA2MjQsMjc4LjU3NjA3NjQ4MTk5OTYgNzQ3LjUzNTUxMDMyNDYwODksMjc5LjA4MzMwNjQ1ODEwOTIzIDc4NSwyOTIgQyA4MjIuNDY0NDg5Njc1MzkxMSwzMDQuOTE2NjkzNTQxODkwNzcgODUxLjE4MTk3NjUwNzYyNjYsMzMwLjI0Mjg1MDY0OTU2MjUgODgwLDMyOSBDIDkwOC44MTgwMjM0OTIzNzM0LDMyNy43NTcxNDkzNTA0Mzc1IDkzNy43MzY1ODM2NDQ4ODQ3LDI5OS45NDUyOTA5NDM2NDA1NCA5NjgsMjg0IEMgOTk4LjI2MzQxNjM1NTExNTMsMjY4LjA1NDcwOTA1NjM1OTQ2IDEwMjkuODcxNjg4OTEyODM0OCwyNjMuOTc1OTg1NTc1ODc1MzcgMTA2MCwyNzEgQyAxMDkwLjEyODMxMTA4NzE2NTIsMjc4LjAyNDAxNDQyNDEyNDYzIDExMTguNzc2NjYwNzAzNzc2MywyOTYuMTUwNzY2NzUyODU4MSAxMTQ2LDI5NCBDIDExNzMuMjIzMzM5Mjk2MjIzNywyOTEuODQ5MjMzMjQ3MTQxOSAxMTk5LjAyMTY2ODI3MjA2MDIsMjY5LjQyMDk0NzQxMjY5MjA0IDEyMzMsMjc2IEMgMTI2Ni45NzgzMzE3Mjc5Mzk4LDI4Mi41NzkwNTI1ODczMDc5NiAxMzA5LjEzNjY2NjIwNzk4MjcsMzE4LjE2NTQ0MzU5NjM3MzcgMTM0NSwzMjcgQyAxMzgwLjg2MzMzMzc5MjAxNzMsMzM1LjgzNDU1NjQwMzYyNjMgMTQxMC40MzE2NjY4OTYwMDg3LDMxNy45MTcyNzgyMDE4MTMxMyAxNDQwLDMwMCBDIDE0NDAsMzAwIDE0NDAsNDAwIDE0NDAsNDAwIFoiCiAgICAgICAgICAgICAgc3Ryb2tlPSJub25lIiBzdHJva2Utd2lkdGg9IjAiIGZpbGw9IiNiZTJlZGRmZiIKICAgICAgICAgICAgICBjbGFzcz0idHJhbnNpdGlvbi1hbGwgZHVyYXRpb24tMzAwIGVhc2UtaW4tb3V0IGRlbGF5LTE1MCBwYXRoLTIiPjwvcGF0aD4KICAgICAgICA8L3N2Zz4=")
}

.dto {
  background-color: #1890ff;
  position: fixed;
  top: 10px;
  left: 0px;
  bottom: 0px;
  width: 100%;
  z-index: 99;
  padding-top: 20px;
  height: 80px;
  display: none;
  overflow: auto;
  display: inline-block;
  opacity: 0.4;
}
</style>
