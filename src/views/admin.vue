<template>
  <div style="z-index: 99">
    <div class="text-center info-admin">
      <p class="flow-text font-s-24" v-text="listInfoData.loverPrefix" v-if="listInfoData.loverPrefix"></p>
      <p v-else class="flow-text font-s-28">这是我们一起走过的</p>
      <div class="slow-time-cl font-s-28">
        <spna class="font-s-46 color-be2edd" v-text="dayDiff"></spna>
        <span>天&ensp;</span>
        <spna class="font-s-46 color-be2edd" v-text="minutes"></spna>
        <sapn>小时&ensp;</sapn>
        <spna class="font-s-46 color-be2edd" v-text="hours"></spna>
        <sapn>分钟&ensp;</sapn>
        <spna class="font-s-46 color-be2edd" v-text="seconds"></spna>
        <sapn>秒</sapn>
      </div>
    </div>

    <!--    轮播图-->
    <div class="disappear-789">
      <a-spin :loading="imageLoading" tip="正在加载..." style="width: 100%;" :size="28">
        <a-grid :cols="{ xs: 1, sm: 1, md: 1, lg: 1, xl: 1, xxl: 1 }" :colGap="40" :rowGap="16"
                class="grid-demo-grid">
          <a-grid-item>
            <div style="margin: 1.25rem 0 5rem 0">
              <a-carousel
                  indicator-type="line"
                  :auto-play="true"
                  animation-name="card"
                  show-arrow="never"
                  indicator-position="outer"
                  :style="{ width: '100%',height: '260px',}">
                <a-carousel-item v-for="(image,key) in images" :style="{ width: '60%' }" :key="key">
                  <img :src="image.img" :style="{width: '100%',}"/>
                </a-carousel-item>
              </a-carousel>
            </div>
          </a-grid-item>
        </a-grid>
      </a-spin>
    </div>
    <div class="button-admin">
      <div class="button-list-cl">
        <div class="demo-item">
          <router-link to="/blessing">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1668591837943" class="icon icon-transform" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="702" width="42" height="42">
                  <path
                      d="M482.771007 675.222579c65.026299 9.85247 231.204618-9.85247 321.847338-184.569596 89.985888-174.717126-27.586915-394.098781-27.586915-394.098782s18.391276 138.591405-58.457985 137.277743c-199.676716-3.940988-478.173188 1.313663-451.243105 254.193713 11.822963 112.318153 102.465683 170.119307 215.440667 187.196922"
                      fill="#DAEFF7" p-id="703"></path>
                  <path
                      d="M337.611289 673.252085l22.332265-41.380372 17.734445 9.852469 16.420782 7.881976-9.195638 40.066709-15.10712-31.527902z"
                      fill="#E29C4E" p-id="704"></path>
                  <path
                      d="M511.671584 45.978191s-176.68762 321.847338-39.409878 407.235407 217.411161-105.093008 221.352149-165.521488c3.940988-60.42848-27.586915-105.093008-27.586915-105.093008l-30.871071 22.989096s-11.166132-42.037203-36.125721-82.103913C575.384221 84.074407 511.671584 45.978191 511.671584 45.978191"
                      fill="#8e44ad" p-id="705"></path>
                  <path
                      d="M371.766517 592.461835c5.25465 1.970494 10.509301-0.656831 12.479795-5.911482 1.970494-5.25465-0.656831-11.166132-5.911482-13.793458-5.25465-1.970494-10.509301 0.656831-12.479795 5.911482-1.970494 5.25465 0.656831 11.822963 5.911482 13.793458"
                      fill="#040000" p-id="706"></path>
                  <path d="M487.368826 786.227069L299.515074 978.67864l-113.631816-129.395766 187.853753-191.794741z"
                        fill="#DAEFF7" p-id="707"></path>
                  <path d="M185.883258 849.282874l151.0712-31.527903 36.782553-160.266838z" fill="#D1D3D3"
                        p-id="708"></path>
                  <path
                      d="M357.97306 822.35279c0 9.195638-7.225144 16.420783-15.763952 16.420783l-32.184733-0.656831c-8.538807 0-15.10712-7.881976-15.10712-17.077614 0-9.195638 7.225144-16.420783 15.763951-16.420783l32.184734 0.656831c8.538807 0 15.763951 7.881976 15.10712 17.077614z"
                      fill="#E83828" p-id="709"></path>
                  <path
                      d="M344.179602 770.463117c8.538807 0 15.10712 7.881976 15.10712 17.077614l-0.656831 34.812059c0 9.195638-7.225144 16.420783-15.763951 16.420783-8.538807 0-15.10712-7.881976-15.10712-17.077614l0.656831-34.812059c0-9.195638 7.225144-16.420783 15.763951-16.420783z"
                      fill="#E83828" p-id="710"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20" style="line-height: 68px">祝福语</div>
            </div>
          </router-link>
        </div>
        <div class="demo-item">
          <router-link to="/record">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1668589585311" class="icon icon-transform" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="37761" data-spm-anchor-id="a313x.7781069.0.i45" width="42"
                     height="42">
                  <path
                      d="M638.46 298.5h-369a30 30 0 0 0 0 60h369a30 30 0 0 0 0-60zM651.44 539.05L665.51 525a30 30 0 0 0-27.05-43h-369a30 30 0 0 0 0 60h369a29.87 29.87 0 0 0 12.98-2.95zM538.1 665.5H269.46a30 30 0 0 0 0 60H510.4zM623.11 715.78l-21.05 9.72h36.4A30 30 0 0 0 662 676.9z"
                      p-id="37762" data-spm-anchor-id="a313x.7781069.0.i47" class="selected" fill="#fefefe"></path>
                  <path
                      d="M730 608.94V774a49.06 49.06 0 0 1-49 49H227a49.06 49.06 0 0 1-49-49V250a49.06 49.06 0 0 1 49-49h454a49.06 49.06 0 0 1 49 49v210.53l60-60V250a109.12 109.12 0 0 0-109-109H227a109.12 109.12 0 0 0-109 109v524a109.12 109.12 0 0 0 109 109h454a109.12 109.12 0 0 0 109-109V548.94zM930.73 401.81A48 48 0 0 0 862.89 334z"
                      p-id="37763" data-spm-anchor-id="a313x.7781069.0.i46" class="" fill="#8e44ad"></path>
                  <path
                      d="M558.106 638.704l292.742-292.742 67.882 67.882-292.742 292.743zM513.06 730.84l-12.87 26.63a5.37 5.37 0 0 0 7.18 7.18L534 751.78zM541.63 748.09l68.64-33.18-60.35-60.35-33.17 68.65 24.88 24.88z"
                      p-id="37764" data-spm-anchor-id="a313x.7781069.0.i48" class="" fill="#be2edd"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20" style="line-height: 68px">点点滴滴</div>
            </div>
          </router-link>
        </div>
        <div class="demo-item">
          <router-link to="/detailed-list">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1682080647712" class="icon icon-transform" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="1844" width="42" height="42"
                     data-spm-anchor-id="a313x.7781069.0.i3">
                  <path
                      d="M790.694885 129.309064l-560.567135 0c-28.141944 0-50.960649 22.817682-50.960649 50.960649l0 662.490479c0 28.141944 22.817682 50.960649 50.960649 50.960649l560.567135 0c28.141944 0 50.960649-22.818705 50.960649-50.960649L841.655534 180.269713C841.655534 152.127769 818.836829 129.309064 790.694885 129.309064zM790.694885 804.539706c0 21.113877-17.119913 38.22151-38.22151 38.22151L268.349259 842.761216c-21.126156 0-38.22151-17.107633-38.22151-38.22151L230.127749 218.491223c0-21.101597 17.095353-38.22151 38.22151-38.22151l484.125139 0c21.100574 0 38.22151 17.119913 38.22151 38.22151L790.695908 804.539706zM459.451692 307.672358l-152.881946 0 0 50.960649 152.881946 0L459.451692 307.672358zM561.371966 460.554304 306.569746 460.554304l0 50.960649 254.80222 0L561.371966 460.554304zM306.569746 664.397922l356.722494 0 0-50.960649L306.569746 613.437274 306.569746 664.397922z"
                      fill="#8e44ad" p-id="1845"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20 ml-5" style="line-height: 68px;">爱情清单</div>
            </div>
          </router-link>
        </div>
        <div class="demo-item">
          <router-link to="/album">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1668518781897" class="icon icon-transform" viewBox="0 0 1235 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg"
                     p-id="29893" width="42" height="42">
                  <path
                      d="M828.662132 617.650794m-406.349207 0a406.349206 406.349206 0 1 0 812.698413 0 406.349206 406.349206 0 1 0-812.698413 0Z"
                      fill="#8e44ad" p-id="29894"></path>
                  <path
                      d="M116.099773 116.099773m-116.099773 0a116.099773 116.099773 0 1 0 232.199546 0 116.099773 116.099773 0 1 0-232.199546 0Z"
                      fill="#daeff7" p-id="29895"></path>
                  <path
                      d="M867.555556 669.315193H102.748299a67.047619 67.047619 0 0 1-67.047619-67.047619V128A66.75737 66.75737 0 0 1 102.748299 60.952381h764.807257a66.75737 66.75737 0 0 1 67.047619 67.047619v474.267574a67.047619 67.047619 0 0 1-67.047619 67.047619zM102.748299 89.977324a38.022676 38.022676 0 0 0-38.022675 38.022676v474.267574a38.022676 38.022676 0 0 0 38.022675 38.022675h764.807257a38.022676 38.022676 0 0 0 38.022675-38.022675V128a38.022676 38.022676 0 0 0-38.022675-38.022676z"
                      fill="#fefefe" p-id="29896"></path>
                  <path
                      d="M992.943311 806.022676H227.845805a67.337868 67.337868 0 0 1-67.047619-67.047619v-78.947846a14.512472 14.512472 0 0 1 14.512472-14.512472 14.512472 14.512472 0 0 1 14.512471 14.512472v78.947846a38.022676 38.022676 0 0 0 38.022676 38.022675h765.097506a38.022676 38.022676 0 0 0 38.022675-38.022675V264.707483a38.022676 38.022676 0 0 0-38.022675-38.022676h-60.952381a14.512472 14.512472 0 0 1 0-29.024943h60.952381a67.337868 67.337868 0 0 1 67.047619 67.047619v474.267574a67.337868 67.337868 0 0 1-67.047619 67.047619zM232.199546 313.469388A81.269841 81.269841 0 1 1 313.469388 232.199546 81.560091 81.560091 0 0 1 232.199546 313.469388z m0-133.514739A52.244898 52.244898 0 1 0 284.444444 232.199546 52.535147 52.535147 0 0 0 232.199546 179.954649z"
                      fill="#fefefe" p-id="29897"></path>
                  <path
                      d="M702.693878 669.315193H165.442177a14.222222 14.222222 0 0 1-11.900227-6.385488 15.38322 15.38322 0 0 1-1.741496-13.351474c3.773243-11.029478 95.201814-249.904762 254.548752-255.70975a207.818594 207.818594 0 0 1 116.099774 38.022675 14.222222 14.222222 0 0 1 4.353741 20.027211 14.512472 14.512472 0 0 1-20.027211 4.353742A174.14966 174.14966 0 0 0 406.349206 422.893424c-114.648526 4.063492-194.176871 161.378685-219.138322 217.396825h515.482994a14.512472 14.512472 0 0 1 0 29.024944z"
                      fill="#fefefe" p-id="29898"></path>
                  <path
                      d="M867.555556 669.315193h-464.399093a14.512472 14.512472 0 0 1-14.222223-12.770975 16.253968 16.253968 0 0 1 2.321996-9.868481c0-1.741497 2.321995-5.22449 4.353741-10.15873C515.192744 353.23356 633.034014 292.281179 711.111111 290.249433c155.863946-2.321995 221.750567 139.319728 222.331066 140.770975a13.061224 13.061224 0 0 1 0 5.804989v165.151927a67.918367 67.918367 0 0 1-65.886621 67.337869zM426.666667 638.548753h440.888889c35.120181 0 38.022676-37.442177 38.312925-39.183674v-158.76644A217.687075 217.687075 0 0 0 715.755102 319.274376h-4.063492c-96.943311 2.321995-198.240363 116.099773-285.024943 319.274377z"
                      fill="#fefefe" p-id="29899"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20 ml-5" style="line-height: 68px">时光相册</div>
            </div>
          </router-link>
        </div>
        <div class="demo-item">
          <router-link to="/lover-tree">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1668590300920" class="icon icon-transform" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="44562" width="42" height="42">
                  <path
                      d="M464.213333 667.818667s227.157333 156.672 426.666667-223.232c0 0-421.205333-69.290667-426.666667 223.232m34.133334 194.218666c-68.266667-81.237333-126.293333-215.893333-15.872-401.237333C674.645333 138.069333 429.909333 78.848 429.909333 78.848s207.530667 80.554667 11.093334 354.304C499.541333 203.093333 133.12 134.997333 133.12 134.997333c6.826667 346.965333 203.264 358.4 268.458667 351.573334-120.832 166.4-85.333333 296.448-34.133334 376.832-85.333333 11.264-147.968 41.642667-155.648 79.018666h452.437334c-8.192-38.741333-76.117333-70.314667-166.058667-80.384"
                      fill="#8e44ad" p-id="44563"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20 ml-5" style="line-height: 68px">爱情树</div>
            </div>
          </router-link>
        </div>
        <div class="demo-item">
          <router-link to="/about">
            <div class="lo-card flex-content">
              <div class="mr-5">
                <svg t="1668589503813" class="icon icon-transform" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="34308" data-spm-anchor-id="a313x.7781069.0.i42" width="42"
                     height="42">
                  <path
                      d="M714 225.9c-86.9-85.4-225.4-88.6-315.9-6.9-90.6-81.6-229-78.6-315.9 6.9C39.2 268.2 15 326.1 15 386.4S39.2 504.7 82.1 547l264.6 262.3c13.5 13.7 32 21.5 51.3 21.5 19.3 0 37.8-7.7 51.3-21.5L714 547.2c42.9-42.5 67.2-100.2 67.2-160.8 0-60.3-24.3-118-67.2-160.5z m-34.3 287.4L415 775.6c-4.5 4.5-10.5 7.1-17 7.1-6.4 0-12.4-2.6-17-7.1L116.3 513.3c-33.9-33.5-53-79.2-53-126.8 0-47.6 19.1-93.4 53-126.8 68.9-67.2 178.1-68.9 249.2-4.1l32.6 28.1 32.4-28.1C502 191 611.2 193.4 679.7 261c33.5 33.5 52.4 78.8 52.4 126.2 0 47.3-18.7 92.6-52.4 126.1z"
                      fill="#ffffff" p-id="34309" data-spm-anchor-id="a313x.7781069.0.i40" class=""></path>
                  <path
                      d="M236.5 279.5c-56.2 0-101.7 45.5-101.7 101.7 0 6.7 5.4 12 12 12 6.7 0 12-5.4 12-12 0-20.6 8.2-40.4 22.8-54.9 14.6-14.6 34.3-22.8 54.9-22.8 6.7 0 12-5.4 12-12s-5.4-12-12-12z"
                      fill="#ffffff" p-id="34310" data-spm-anchor-id="a313x.7781069.0.i41"></path>
                  <path
                      d="M954.8 330l-1.9-1.9c-72.5-71.5-188-73.8-263.6-5.8-75.6-68-191-65.5-263.6 5.8-74 73-74.9 192.1-1.7 266.1l1.7 1.7 220.9 218.7c23.2 23.6 61.2 24 84.8 0.9l0.9-0.9 220.9-218.7c73.8-72.8 74.6-191.9 1.6-265.9z m-72.3 155.4c-5.4 0-9.9-4.5-9.9-9.9 0-35.8-29.2-65-65-65-2.6 0-5.2-1.1-7.1-3s-3-4.5-3-7.1c0-5.6 4.5-9.9 9.9-9.9 46.8 0 85 38 85 85 0 5.4-4.3 9.9-9.9 9.9z"
                      fill="#8e44ad" p-id="34311"></path>
                  <path d="M885.8 507.2m-11.9 0a11.9 11.9 0 1 0 23.8 0 11.9 11.9 0 1 0-23.8 0Z" fill="#FFFFFF"
                        p-id="34312"></path>
                  <path d="M146.6 419.7m-11.9 0a11.9 11.9 0 1 0 23.8 0 11.9 11.9 0 1 0-23.8 0Z" fill="#ffffff"
                        p-id="34313"></path>
                </svg>
              </div>
              <div class="hover-cl font-s-20 ml-5" style="line-height: 68px">关于我们</div>
            </div>
          </router-link>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import './css/index.css'
import './css/index-media.css'
import {listInfo, listCarousel} from "@/api/lover";

export default {
  name: "admin",
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 5,
        state: 0,
      },
      images: [],
      SampLingTime: '第 999天 00小时 00分钟 00秒',
      dayDiff: 999,
      hours: 0,
      minutes: 0,
      seconds: 0,
      listInfoData: {},
      imageLoading: true,
    }
  },
  methods: {
    listCarousels() {
      listCarousel(this.queryParams).then(res => {
        this.images = res;
        this.imageLoading = false;
      })
    },
    listInfos() {
      let data = {state: 0}
      listInfo(data).then(res => {
        this.listInfoData = res;
        this.difference(this.listInfoData.loverTime);
        this.loading = false;
      })
    },
    // 时间差计算
    difference(beginTime) {
      let dateBegin = new Date(beginTime);
      // let dateEnd = new Date(endTime);
      //结束时间
      let dateEnd = new Date();
      let dateDiff = dateEnd.getTime() - dateBegin.getTime(); //时间差的毫秒数
      let dayDiff = Math.floor(dateDiff / (24 * 3600 * 1000)); //计算出相差天数
      let leave1 = dateDiff % (24 * 3600 * 1000); //计算天数后剩余的毫秒数
      let hours = Math.floor(leave1 / (3600 * 1000)); //计算出小时数
      //计算相差分钟数
      let leave2 = leave1 % (3600 * 1000); //计算小时数后剩余的毫秒数
      let minutes = Math.floor(leave2 / (60 * 1000)); //计算相差分钟数
      //计算相差秒数
      let leave3 = leave2 % (60 * 1000); //计算分钟数后剩余的毫秒数
      let seconds = Math.round(leave3 / 1000);
      this.dayDiff = dayDiff;
      this.hours = hours;
      this.minutes = minutes;
      this.seconds = seconds;
      this.SampLingTime = "第 " + dayDiff + "天 " + hours + "小时 " + minutes + "分钟 " + seconds + "秒";

    },
  },
  mounted() {
    this.listInfos()
    this.listCarousels()
    setInterval(() => {
      this.difference(this.listInfoData.loverTime); //每秒更新一次时间
    }, 1000);
  },
  unmounted() {
    if (this.timer) {  // 注意在vue实例销毁前，清除我们的定时器
      clearInterval(this.timer);
    }
  }
}

</script>

<style>
.arco-carousel-card .arco-carousel-item-current {
  border-radius: 4px;
}

.arco-carousel-card .arco-carousel-item-current img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.arco-spin-tip, .arco-spin-icon {
  color: #fefefe;
}

.arco-spin-mask {
  background-color: transparent;
}
</style>
