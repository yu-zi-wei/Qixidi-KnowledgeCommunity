<template>
  <div class="mt-20 mb-20">
    <!--    <div style="text-align: center;margin-bottom: 20px">-->
    <!--      <div style="margin: auto" class="album-button">-->
    <!--        <span>-->
    <!--      <svg t="1668518781897" class="icon" viewBox="0 0 1235 1024" version="1.1"-->
    <!--           xmlns="http://www.w3.org/2000/svg"-->
    <!--           p-id="29893" width="46" height="50" style="margin-top: 10px">-->
    <!--                <path-->
    <!--                    d="M828.662132 617.650794m-406.349207 0a406.349206 406.349206 0 1 0 812.698413 0 406.349206 406.349206 0 1 0-812.698413 0Z"-->
    <!--                    fill="#f368e0" p-id="29894"></path>-->
    <!--                <path-->
    <!--                    d="M116.099773 116.099773m-116.099773 0a116.099773 116.099773 0 1 0 232.199546 0 116.099773 116.099773 0 1 0-232.199546 0Z"-->
    <!--                    fill="#daeff7" p-id="29895"></path>-->
    <!--                <path-->
    <!--                    d="M867.555556 669.315193H102.748299a67.047619 67.047619 0 0 1-67.047619-67.047619V128A66.75737 66.75737 0 0 1 102.748299 60.952381h764.807257a66.75737 66.75737 0 0 1 67.047619 67.047619v474.267574a67.047619 67.047619 0 0 1-67.047619 67.047619zM102.748299 89.977324a38.022676 38.022676 0 0 0-38.022675 38.022676v474.267574a38.022676 38.022676 0 0 0 38.022675 38.022675h764.807257a38.022676 38.022676 0 0 0 38.022675-38.022675V128a38.022676 38.022676 0 0 0-38.022675-38.022676z"-->
    <!--                    fill="#fefefe" p-id="29896"></path>-->
    <!--                <path-->
    <!--                    d="M992.943311 806.022676H227.845805a67.337868 67.337868 0 0 1-67.047619-67.047619v-78.947846a14.512472 14.512472 0 0 1 14.512472-14.512472 14.512472 14.512472 0 0 1 14.512471 14.512472v78.947846a38.022676 38.022676 0 0 0 38.022676 38.022675h765.097506a38.022676 38.022676 0 0 0 38.022675-38.022675V264.707483a38.022676 38.022676 0 0 0-38.022675-38.022676h-60.952381a14.512472 14.512472 0 0 1 0-29.024943h60.952381a67.337868 67.337868 0 0 1 67.047619 67.047619v474.267574a67.337868 67.337868 0 0 1-67.047619 67.047619zM232.199546 313.469388A81.269841 81.269841 0 1 1 313.469388 232.199546 81.560091 81.560091 0 0 1 232.199546 313.469388z m0-133.514739A52.244898 52.244898 0 1 0 284.444444 232.199546 52.535147 52.535147 0 0 0 232.199546 179.954649z"-->
    <!--                    fill="#fefefe" p-id="29897"></path>-->
    <!--                <path-->
    <!--                    d="M702.693878 669.315193H165.442177a14.222222 14.222222 0 0 1-11.900227-6.385488 15.38322 15.38322 0 0 1-1.741496-13.351474c3.773243-11.029478 95.201814-249.904762 254.548752-255.70975a207.818594 207.818594 0 0 1 116.099774 38.022675 14.222222 14.222222 0 0 1 4.353741 20.027211 14.512472 14.512472 0 0 1-20.027211 4.353742A174.14966 174.14966 0 0 0 406.349206 422.893424c-114.648526 4.063492-194.176871 161.378685-219.138322 217.396825h515.482994a14.512472 14.512472 0 0 1 0 29.024944z"-->
    <!--                    fill="#fefefe" p-id="29898"></path>-->
    <!--                <path-->
    <!--                    d="M867.555556 669.315193h-464.399093a14.512472 14.512472 0 0 1-14.222223-12.770975 16.253968 16.253968 0 0 1 2.321996-9.868481c0-1.741497 2.321995-5.22449 4.353741-10.15873C515.192744 353.23356 633.034014 292.281179 711.111111 290.249433c155.863946-2.321995 221.750567 139.319728 222.331066 140.770975a13.061224 13.061224 0 0 1 0 5.804989v165.151927a67.918367 67.918367 0 0 1-65.886621 67.337869zM426.666667 638.548753h440.888889c35.120181 0 38.022676-37.442177 38.312925-39.183674v-158.76644A217.687075 217.687075 0 0 0 715.755102 319.274376h-4.063492c-96.943311 2.321995-198.240363 116.099773-285.024943 319.274377z"-->
    <!--                    fill="#fefefe" p-id="29899"></path>-->
    <!--              </svg>-->
    <!--        </span>-->
    <!--        <span style="line-height: 50px">时光相册</span>-->
    <!--      </div>-->

    <!--    </div>-->
    <!--    <div class="mb-20">-->
    <!--      <hr class="hr-wavy">-->
    <!--    </div>-->
    <!--    <div class="container">-->
    <!--      <div class="parent">-->
    <!--        <div class="box" v-for="(item,i) of imgList">-->
    <!--          <div class="album-wrapper">-->
    <!--            <div class="album-thumb">-->
    <!--              <a-image-->
    <!--                  width="300"-->
    <!--                  style="border-radius: 4px"-->
    <!--                  :preview-visible="visible1"-->
    <!--                  @preview-visible-change="() => { visible1= false }"-->
    <!--                  :src="item.img"-->
    <!--              />-->
    <!--              <div class="view-icon">-->
    <!--                <div class="content-album-cl">{{ item.remarks }}</div>-->
    <!--                <div class="work-tag">-->
    <!--                  <a href="#" class="mr-10">{{ item.address }}</a>-->
    <!--                  <a href="#">{{ item.createTime }}</a>-->
    <!--                </div>-->
    <!--                <div @click="() => { visible1 = true }" class="cursor-pointer" title="查看大图">-->
    <!--                  <svg t="1669720960035" class="icon" viewBox="0 0 1024 1024" version="1.1"-->
    <!--                       xmlns="http://www.w3.org/2000/svg" p-id="13368" width="22" height="22">-->
    <!--                    <path-->
    <!--                        d="M917.1 59.5H712.4c-19.3 0-35 15.7-35 35s15.7 35 35 35h169.8v169.8c0 19.3 15.7 35 35 35s35-15.7 35-35V94.5c-0.1-19.3-15.7-35-35.1-35zM106.9 334.3c19.3 0 35-15.7 35-35V129.5h169.8c19.3 0 35-15.7 35-35s-15.7-35-35-35H106.9c-19.3 0-35 15.7-35 35v204.8c0 19.3 15.6 35 35 35zM311.6 894.5H141.9V724.7c0-19.3-15.7-35-35-35s-35 15.7-35 35v204.8c0 19.3 15.7 35 35 35h204.8c19.3 0 35-15.7 35-35s-15.7-35-35.1-35zM917.1 689.7c-19.3 0-35 15.7-35 35v169.8H712.4c-19.3 0-35 15.7-35 35s15.7 35 35 35h204.8c19.3 0 35-15.7 35-35V724.7c-0.1-19.3-15.7-35-35.1-35zM769.1 794.7c9.2 0 18.4-3.6 25.3-10.8 13.3-14 12.8-36.1-1.2-49.5L705 650.2c26.3-37.5 43-82.4 46-131.5 9-141.6-98.7-263.7-240.3-272.6-5.5-0.3-10.9-0.5-16.3-0.5-134.6 0-247.7 104.8-256.3 241-8.9 141.7 98.8 263.7 240.4 272.6 5.5 0.3 10.9 0.5 16.3 0.5 61.3 0 118.1-21.7 162.6-58.2l87.5 83.5c6.8 6.5 15.5 9.7 24.2 9.7z m-87.9-280.3c-3 47.8-23.9 92.1-58.8 124.8-34.8 32.6-80.1 50.5-127.6 50.5-4 0-8-0.1-12-0.4-49.9-3.1-95.5-25.5-128.6-63-33-37.5-49.5-85.6-46.4-135.4 3-47.8 23.9-92.1 58.8-124.8 34.8-32.6 80.1-50.5 127.6-50.5 4 0 8 0.1 11.9 0.4 49.9 3.1 95.5 25.5 128.6 63 33.2 37.4 49.7 85.5 46.5 135.4z"-->
    <!--                        p-id="13369" fill="#b20cd3"></path>-->
    <!--                  </svg>-->
    <!--                </div>-->
    <!--              </div>-->
    <!--            </div>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>

<script>
// import './css/album.css'
// import './css/responsive.css'
// import './css/style.css'
import {listAlbum} from "@/api/lover";

export default {
  name: "album",
  data() {
    return {
      visible1: false,
      imgList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        state: 0,
      },
      total: 0,
      scrollLoading: true,
    }
  },
  methods: {
    judge(i) {
      let number = i % 3;
      if (number == 0) {
        return 'transform: rotate(-5deg)';
      } else if (number == 1) {
        return 'transform: rotate(-10deg)';
      } else if (number == 2) {
        return 'transform: rotate(8deg)';
      } else {
        return 'transform: rotate(-5deg)';
      }
    },
    getData() {
      let scrollTop = document.documentElement.scrollTop
      let clientHeight = document.documentElement.clientHeight
      let scrollHeight = document.documentElement.scrollHeight
      if (scrollHeight - (scrollTop + clientHeight) <= 1) {
        if (!this.scrollLoading) return;
        if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
          this.scrollLoading = false;
          this.queryParams.pageNum = this.queryParams.pageNum + 1;
          listAlbum(this.queryParams).then(res => {
            res.rows.forEach(item => {
              this.imgList.push(item)
            })
            this.total = res.total;
          }).finally(() => {
            this.scrollLoading = true
            this.setUpActive();
          })
        }
      }
    },
    listAlbums() {
      this.queryParams.pageNum = 1;
      listAlbum(this.queryParams).then(res => {
        this.imgList = res.rows;
        this.total = res.total;
        // this.setUpActive();
      })
    },
    // setUpActive() {
    //   // 获取所有.item元素
    //   let items = document.querySelectorAll('.item');
    //
    //   // 设置选中态样式
    //   function setActive() {
    //     // 遍历所有.item元素，移出active样式
    //     items.forEach((item) => {
    //       item.classList.remove('active');
    //     })
    //     // 为当前选中项添加active样式
    //     this.classList.add('active');
    //   }
    //
    //   // 遍历所有.item元素，分别为其设置点击事件
    //   items.forEach((item) => {
    //     item.addEventListener('click', setActive);
    //   })
    //
    // }
  },
  mounted() {
    this.listAlbums();
  },
  // created() {
  //   //添加滚动监听事件
  //   window.addEventListener('scroll', this.getData, true);
  // },
  // destroyed() {
  //   //离开页面时删除该监听
  //   window.removeEventListener('scroll', this.getData, true)
  // }
}
</script>

<style>
.grid-demo-grid .demo-item,
.grid-demo-grid .demo-suffix {
  height: 48px;
  line-height: 48px;
  color: var(--color-white);
  text-align: center;
}

.grid-demo-grid .demo-item:nth-child(2n) {

  background-color: rgba(var(--arcoblue-6), 0.9);
}

.grid-demo-grid .demo-item:nth-child(2n + 1) {
  background-color: var(--color-primary-light-4);
}
</style>