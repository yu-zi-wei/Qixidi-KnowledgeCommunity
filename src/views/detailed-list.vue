<template>
  <div class="mt-20">
    <div class="text-center mb-32">
    </div>
    <!--    搜索-->
    <!--    <div class="text-center">-->
    <!--      <div class="search-box" tabindex="1">-->
    <!--        <input v-model="queryParams.name" type="text" class="search-txt" placeholder="清单标题..."-->
    <!--               style="color:#1e272e;outline:none;"/>-->
    <!--        <a class="search-btn">-->
    <!--          <svg t="1671602595081" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"-->
    <!--               p-id="1401" width="16" height="16">-->
    <!--            <path-->
    <!--                d="M953.474215 908.234504l-152.576516-163.241391c61.92508-74.48211 95.81186-167.36973 95.81186-265.073744 0-229.294809-186.63531-415.930119-416.102133-415.930119-229.294809 0-415.930119 186.63531-415.930119 415.930119s186.63531 415.930119 415.930119 415.930119c60.032925 0 118.00168-12.55703 172.186125-37.327062 16.169326-7.396607 23.221905-26.318159 15.825298-42.315471-7.396607-16.169326-26.318159-23.221905-42.315471-15.825298-45.927768 20.813707-94.951789 31.478582-145.695952 31.478582-194.031917 0-351.94087-157.908953-351.94087-351.94087 0-194.031917 157.908953-351.94087 351.94087-351.94087 194.031917 0 351.94087 157.908953 351.94087 351.94087 0 91.339493-34.918864 177.86259-98.048043 243.743995-12.213002 12.729044-11.868974 33.026709 0.860071 45.239711 1.032085 0.860071 2.236183 1.204099 3.268268 2.064169 0.860071 1.204099 1.376113 2.752226 2.408198 3.956325l165.477574 177.00252c6.192508 6.70855 14.793214 10.148833 23.393919 10.148833 7.912649 0 15.653284-2.92424 21.845792-8.600706C964.827146 941.433227 965.515202 921.135562 953.474215 908.234504z"-->
    <!--                fill="#ffffff" p-id="1402"></path>-->
    <!--          </svg>-->
    <!--        </a>-->
    <!--      </div>-->
    <!--    </div>-->
    <div class="mb-20">
      <a-spin :loading="loading" tip="正在赶来的路上..." style="width: 100%;" :size="28">
        <div class="detalide-flex">
          <div class="list-cl detalide-item" v-for="(item,index) in detailedList" :key="index">
            <div class="box-gou">
              <div style="line-height: 28px">
                <svg v-if="item.isComplete==1" t="1668952537738" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3765" width="16" height="16">
                  <path
                      d="M60.217477 633.910561c0 0 250.197342 104.557334 374.563838 330.628186 149.378146-279.762705 436.109566-540.713972 521.05012-560.013527 0-115.776863 0-163.394371 0-341.442486-342.237595 226.070852-506.576477 642.342604-506.576477 642.342604l-180.049702-191.614086L60.217477 633.910561z"
                      p-id="3766" data-spm-anchor-id="a313x.7781069.0.i0" class="selected" fill="#ffffff"></path>
                </svg>
                <svg v-else t="1721444671582" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg"
                     p-id="10282" width="16" height="16">
                  <path
                      d="M225.28 512m-81.92 0a81.92 81.92 0 1 0 163.84 0 81.92 81.92 0 1 0-163.84 0Z" p-id="10283"
                      fill="#fefefe"></path>
                  <path
                      d="M512 512m-81.92 0a81.92 81.92 0 1 0 163.84 0 81.92 81.92 0 1 0-163.84 0Z" p-id="10284"
                      fill="#fefefe"></path>
                  <path
                      d="M798.72 512m-81.92 0a81.92 81.92 0 1 0 163.84 0 81.92 81.92 0 1 0-163.84 0Z" p-id="10285"
                      fill="#fefefe"></path>
                </svg>
              </div>
            </div>
            <a-collapse :default-active-key="['1']" accordion>
              <a-collapse-item class="font-s-24" :header="item.name" :key="item.isComplete==1?'1':'2'">
                <template #extra>
                  <div class="font-s-13 disappear-789" style="color:#95a5a6;">
                    <span v-if="item.isComplete==1">
                      {{ parseTimes(item.updateTime, '{y}-{m}-{d}') }}
                    </span>
                    <span v-else>
                      待完成！
                    </span>
                  </div>
                </template>
                <div v-if="item.isComplete==1">
                  <div class="content-cl" v-html="item.content"></div>
                </div>
                <div v-else>
                  待完成！
                </div>
              </a-collapse-item>
              <!--                地址 时间-->
              <div style="display: flex;justify-content: left;margin-top: 10px">
                <div v-if="item.address" class="mr-10">
                  <svg t="1669690071448" class="icon svg-16-f" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="6300" data-spm-anchor-id="a313x.7781069.0.i4"
                       width="16" height="16" style="margin-top: 1px">
                    <path
                        d="M511.744 68.266667c-173.5168 0-314.026667 136.311467-314.7776 305.937066 0 60.910933 18.1248 118.903467 51.7632 168.465067l3.293867 4.693333 1.911466 3.1744 1.570134 2.389334c1.058133 1.553067 2.184533 3.037867 3.447466 4.5056l0.785067 0.853333 200.174933 232.823467a68.266667 68.266667 0 0 0 103.645867-0.170667L762.641067 558.08l-1.314134 1.450667a50.346667 50.346667 0 0 0 5.341867-6.621867l1.536-2.3552c0.631467-0.989867 1.860267-3.072 1.826133-3.003733 35.293867-49.322667 55.0912-109.431467 55.825067-172.782934C825.856 204.9536 684.970667 68.266667 511.744 68.266667z m0 68.266666c135.970133 0 245.845333 106.5984 245.845333 237.824a235.400533 235.400533 0 0 1-43.9808 134.775467l-2.952533 4.676267-198.997333 232.789333-200.192-232.823467-1.928534-3.191466-0.989866-1.450667a230.229333 230.229333 0 0 1-43.3152-134.775467C265.8304 242.858667 375.415467 136.533333 511.744 136.533333z"
                        fill="#95afc0" p-id="6301"></path>
                    <path
                        d="M783.803733 714.734933a34.133333 34.133333 0 0 1 45.243734 10.018134l1.4336 2.2528 73.386666 125.730133a68.266667 68.266667 0 0 1-54.784 102.5536l-4.5568 0.119467-666.043733-3.6352a68.266667 68.266667 0 0 1-60.654933-98.850134l2.133333-3.9424 69.9392-119.261866a34.133333 34.133333 0 0 1 60.16 32.170666l-1.262933 2.3552-69.9392 119.261867 666.043733 3.6352-73.386667-125.730133a34.133333 34.133333 0 0 1 12.288-46.677334z"
                        fill="#7f8c8d" p-id="6302"></path>
                    <path
                        d="M512 243.950933a136.533333 136.533333 0 1 0 0.034133 273.1008A136.533333 136.533333 0 0 0 512 243.950933z m0 68.266667a68.266667 68.266667 0 1 1-0.034133 136.567467A68.266667 68.266667 0 0 1 512 312.2176z"
                        fill="#3498db" p-id="6303" data-spm-anchor-id="a313x.7781069.0.i5"
                        class="selected"></path>
                  </svg>
                  <span class="di-cl-item" v-text="item.address"></span>
                </div>
                <div v-if="item.updateTime">
                  <svg t="1669798181975" class="icon svg-16-f" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="9622" width="16" height="16" style="margin-top: 1px;">
                    <path
                        d="M865.8 924.2H161.2c-34.3 0-62.2-27.9-62.2-62.2V217.2c0-34.3 27.9-62.2 62.2-62.2h704.6c34.3 0 62.2 27.9 62.2 62.2V862c0 34.3-27.9 62.2-62.2 62.2zM161.2 196.5c-11.4 0-20.7 9.3-20.7 20.7V862c0 11.4 9.3 20.7 20.7 20.7h704.6c11.4 0 20.7-9.3 20.7-20.7V217.2c0-11.4-9.3-20.7-20.7-20.7H161.2z"
                        fill="#95afc0" p-id="9623"></path>
                    <path
                        d="M119.8 390.2h772.6v41.4H119.8zM260 533.9h87.8v87.8H260zM469.6 533.9h87.8v87.8h-87.8zM679.3 533.9h87.8v87.8h-87.8z"
                        fill="#8e44ad" p-id="9624"></path>
                    <path
                        d="M260 685.8h87.8v87.8H260zM469.6 685.8h87.8v87.8h-87.8zM679.3 685.8h87.8v87.8h-87.8z"
                        fill="#8e44ad" p-id="9625"></path>
                    <path
                        d="M333.6 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7zM693.5 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7z"
                        fill="#1890ff" p-id="9626"></path>
                  </svg>
                  <span class="di-cl-item">
                    {{ parseTimes(item.updateTime, '{y}-{m}-{d}') }}
                  </span>
                </div>
              </div>
            </a-collapse>

          </div>
        </div>
        <div v-if="!loading && detailedList.length==0" class="text-center" style="color: #fefefe;font-size: 18px">
          <svg t="1670079174437" class="icon" viewBox="0 0 1567 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="11978" width="54" height="54">
            <path
                d="M156.662278 699.758173h21.097186A10.444152 10.444152 0 0 1 187.994733 710.202325c0 5.765172-4.490985 10.444152-10.235269 10.444152H156.662278v21.097186A10.444152 10.444152 0 0 1 146.218126 751.978932a10.277045 10.277045 0 0 1-10.444152-10.235269V720.646477H114.676787A10.444152 10.444152 0 0 1 104.441518 710.202325c0-5.765172 4.490985-10.444152 10.235269-10.444152H135.773974v-21.097187A10.444152 10.444152 0 0 1 146.218126 668.425717c5.765172 0 10.444152 4.490985 10.444152 10.235269v21.097187z m1378.628042-83.553215v-21.097186A10.277045 10.277045 0 0 0 1524.846168 584.872503a10.444152 10.444152 0 0 0-10.444152 10.235269v21.097186h-21.097186a10.277045 10.277045 0 0 0-10.235269 10.444152c0 5.598065 4.595427 10.444152 10.235269 10.444152h21.097186v21.097187c0 5.744284 4.67898 10.235269 10.444152 10.235268a10.444152 10.444152 0 0 0 10.444152-10.235268V637.093262h21.097187c5.744284 0 10.235269-4.67898 10.235268-10.444152a10.444152 10.444152 0 0 0-10.235268-10.444152H1535.29032zM776.460024 960.861969H250.596979A20.80475 20.80475 0 0 1 229.77134 939.973665c0-11.530344 9.462402-20.888304 20.825639-20.888303h94.728457A83.010119 83.010119 0 0 1 334.212859 877.413196v-605.96969A83.49055 83.49055 0 0 1 417.849627 187.994733H480.430984V167.001988A83.49055 83.49055 0 0 1 564.067752 83.553215h501.152182A83.448773 83.448773 0 0 1 1148.856702 167.001988v605.969689c0 15.185797-4.052331 29.410732-11.133466 41.672166h115.554096c11.551232 0 20.909192 9.274407 20.909192 20.888304 0 11.530344-9.295295 20.888304-20.888304 20.888304H1002.638576v20.992745c0 15.185797-4.052331 29.410732-11.133466 41.672166h11.196131c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304h-109.893365c9.545955 16.000441 7.478013 36.972297-6.41271 50.863019a41.672166 41.672166 0 0 1-59.072122 0L776.460024 960.861969z m76.367638-41.776607h66.424806c22.977134 0 41.609501-18.59059 41.609501-41.881049V270.461756c0-22.559368-18.047494-40.690416-40.314426-40.690416H416.303892c-22.266932 0-40.314426 18.214601-40.314426 40.690416v606.742557c0 23.123352 18.799473 41.881049 41.588613 41.881049h317.084449l-10.736588-10.757477a41.693054 41.693054 0 0 1-10.861918-40.377091l-19.718558-19.739447A146.259902 146.259902 0 0 1 502.363703 627.693525a146.218126 146.218126 0 0 1 220.517822 190.981761l19.739447 19.739447a41.630389 41.630389 0 0 1 40.377091 10.841029L852.827662 919.085362zM1002.638576 814.643843h62.852906A41.797496 41.797496 0 0 0 1107.080095 772.867236V167.106429c0-23.14424-18.632367-41.776607-41.588613-41.776607H563.775316A41.797496 41.797496 0 0 0 522.207592 167.106429v20.888304h396.794216A83.448773 83.448773 0 0 1 1002.638576 271.443506V814.643843zM266.325872 46.998683h31.123572c8.773088 0 15.875111 6.955805 15.875111 15.666228 0 8.647758-7.102023 15.666228-15.875111 15.666228h-31.123572v31.123572c0 8.773088-6.955805 15.875111-15.666228 15.875111a15.770669 15.770669 0 0 1-15.666228-15.875111V78.331139H203.869844A15.728893 15.728893 0 0 1 187.994733 62.664911c0-8.647758 7.102023-15.666228 15.875111-15.666228h31.123572V15.875111c0-8.773088 6.955805-15.875111 15.666228-15.875111 8.647758 0 15.666228 7.102023 15.666228 15.875111v31.123572zM20.888304 939.973665c0-11.530344 9.462402-20.888304 20.825638-20.888303h125.455152c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304H41.713942A20.80475 20.80475 0 0 1 20.888304 939.973665z m658.733544-135.021995a104.441518 104.441518 0 1 0-147.722083-147.722083 104.441518 104.441518 0 0 0 147.722083 147.722083zM459.542681 313.324555a20.888304 20.888304 0 0 1 20.867415-20.888304H710.202325a20.888304 20.888304 0 1 1 0 41.776608H480.430984A20.825639 20.825639 0 0 1 459.542681 313.324555z m0 104.441518c0-11.530344 9.295295-20.888304 20.742085-20.888303h334.505295c11.44679 0 20.742086 9.274407 20.742086 20.888303 0 11.530344-9.295295 20.888304-20.742086 20.888304H480.284766A20.762974 20.762974 0 0 1 459.542681 417.766073z m0 104.441519c0-11.530344 9.316183-20.888304 20.846527-20.888304h146.301679c11.509455 0 20.846527 9.274407 20.846527 20.888304 0 11.530344-9.316183 20.888304-20.846527 20.888303h-146.301679A20.80475 20.80475 0 0 1 459.542681 522.207592zM62.664911 396.87777a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911zM1357.739739 271.547948a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911z"
                fill="#ffffff" p-id="11979"></path>
          </svg>
          <div>暂无清单</div>
        </div>
        <div @click="getData" v-if="isList" style="margin: auto;text-align: center;">
          <div class="mt-20">
            <button class="button-bo-cl">
              <span class="fl-left">查看更多</span>
              <span class="fl-left ml-2">...</span>
            </button>
          </div>
        </div>
      </a-spin>
    </div>
  </div>
</template>

<script>
import {listRepertoire} from "@/api/lover";
import './css/button.css'
import {parseTime} from '@/utils/dateUitls';

export default {
  name: "detailedList",
  data() {
    return {
      complete: false,
      loading: true,
      detailedList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 18,
        stata: 1,
        name: null,
      },
      total: 0,
      scrollLoading: true,
      isList: false,
    }
  },
  watch: {
    'queryParams.name': {
      handler: function () {
        this.listRepertoires();
      }
    }
  },
  methods: {
    parseTimes(value, args) {
      return parseTime(value, args);
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
          listRepertoire(this.queryParams).then(res => {
            res.rows.forEach(item => {
              this.detailedList.push(item)
            })
            this.total = res.total;
          }).finally(() => {
            this.scrollLoading = true
            if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
              this.isList = true;
              return;
            }
            this.isList = false;
          })
        }
      }
    },
    listRepertoires() {
      this.queryParams.pageNum = 1;
      listRepertoire(this.queryParams).then(res => {
        this.detailedList = res.rows;
        this.total = res.total;
        this.loading = false;
        if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
          this.isList = true;
          return;
        }
        this.isList = false;
      })
    },
  },
  mounted() {
    this.listRepertoires();
  },
  created() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
  },
  unmounted() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style>
.di-cl-item {
  color: #636e72;
  margin-left: 4px;
  font-size: 13px;
}

.detalide-flex {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  width: 80%;
  margin: auto;
}

.detalide-item {
  width: 42%;
}

@media (max-width: 787px) {
  .detalide-item {
    width: 80%;
  }

  .detalide-flex {
    width: 96%;
  }

}

.search-box {
  /* 绝对定位 水平垂直居中 */
  position: absolute;
  left: 50%;
  outline: none;
  transform: translate(-50%, -50%);
  /*background-color: #770e9e;*/
  background-color: #fefefe;
  height: 30px;
  padding: 6px;
  border-radius: 30px;
}

.search-txt {
  border: none;
  background: none;
  outline: none;
  float: left;
  padding: 0;
  color: #fff;
  font-size: 14px;
  line-height: 30px;
  width: 0;
  /* 动画过渡 */
  transition: 0.6s;
}

.search-txt::-webkit-input-placeholder {
  color: #808e9b;
  font-size: 14px;
}

.search-btn {
  float: right;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #ce5fee;
  /* 弹性布局 水平垂直居中 */
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  /* 动画过渡 */
  transition: 0.4s;
}

.search-box:hover .search-txt {
  width: 140px;
  padding: 0 6px;
}

.search-box:hover .search-btn {
  background-color: #bc2dda;
}

/*--------------------------*/

.content-cl {
  text-align: center;
}

.content-cl img {
  border: none;
  max-width: 86%;
  padding: 20px;
}

.box-out {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  /*background: linear-gradient(0deg, rgba(211, 226, 219, 1) 0%, rgba(223, 187, 237, 1) 22%, rgba(254, 254, 254, 1) 100%);*/
  background: #f1f2f6;
  margin: 6px 0 0 6px;
  transition: all 0.4s;
}

.box-out:hover {
  transform: scale(1.4);
  background: #8e44ad;
}

.box-gou {
  margin-top: 6px;
  margin-right: 6px;
  overflow: hidden;
  background-color: #8e44ad;
  width: 24px;
  height: 24px;
  border-radius: 50%;;
  text-align: center;
  float: left;
  cursor: pointer;
}

.container {
  left: 0.45em;
  top: 0.25em;
  width: 0.25em;
  height: 0.5em;
  border: solid #f0f0f0;
  border-width: 0 0.15em 0.15em 0;
  transform: rotate(45deg);
}


.list-cl {
  background-color: rgba(223, 228, 234);
  margin: 20px 0 20px 0;
  padding: 20px;
  color: #fefefe;
  border-radius: 30px;
  border-bottom: 2px solid #9b59b6;
  transition: 0.2s;
}

.list-cl:hover {
  transform: translateY(-6px);
}

.arco-collapse-item-header {
  background-color: #f4f5f5;
  border: none;
  border-radius: 20px;
  color: #2c3e50;
}

.arco-collapse-item .arco-collapse-item-expand-icon {
  color: #2c3e50;
}

.arco-collapse-item .arco-collapse-item-expand-icon:hover {

}

.arco-checkbox-label {
  color: #fefefe;
  font-weight: bold;
}

.arco-checkbox-checked .arco-checkbox-icon {
  /*background-color: #be2edd;*/
}


.arco-collapse-item-active > .arco-collapse-item-header {
  background-color: #fefefe;
  color: #8e44ad;
  font-size: 20px;
  line-height: 20px;
  border-bottom: 2px solid #9b59b6;
}

.arco-collapse-item-content {
  background-color: #dfe4ea;
  color: #2c3e50;
}

.arco-collapse {
  border: none;
  min-width: 160px;
}

.arco-spin-tip, .arco-spin-icon {
  color: #fefefe;
}

.arco-spin-mask {
  background-color: transparent;
}

</style>
