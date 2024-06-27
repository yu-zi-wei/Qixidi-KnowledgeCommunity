<template>
  <div class="mt-20">
    <!--    搜索-->
    <!--    <div class="text-center">-->
    <!--      <div class="search-box" tabindex="1">-->
    <!--        <input v-model="queryParams.title" type="text" class="search-txt" placeholder="标题关键字..."-->
    <!--               style="color:#636e72;outline:none;"/>-->
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
    <!--    手机-->
    <div class="mb-20">
      <a-spin :loading="loading" tip="正在赶来的路上..." style="width: 100%;margin-top: 40px" :size="28">
        <div class="disappear-789-an">
          <div v-for="(items,index) in  recordList" style="width: 80%;margin: auto" :key="index">
            <div style="margin-bottom: 60px">
              <div class="card">
                <h2 class="text-center">{{ items.title }}</h2>
                <hr class="hr-twill-colorful"/>
                <div @click="recordIndex(items.id)"
                     style="padding: 0 10px 10px 6px" class="content-cl cursor-pointer"
                     title="点击查看"
                     v-html="items.content"></div>
              </div>
            </div>
          </div>
        </div>
        <!--        pc-->
        <div v-if="recordList.length>0">
          <div class="disappear-789">
            <div class="record-pc-content">
              <div v-for="(item,i) in recordList" :key="i" class="card">
                <h2 class="text-center">{{ item.title }}</h2>
                <hr class="hr-twill-colorful"/>
                <div @click="recordIndex(item.id)" style="padding: 0 10px 10px 6px"
                     class="content-cl cursor-pointer"
                     title="点击查看"
                     v-html="item.content"></div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="!loading && recordList.length==0" class="text-center" style="color: #fefefe;font-size: 18px">
          <svg t="1670079174437" class="icon" viewBox="0 0 1567 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="11978" width="54" height="54">
            <path
                d="M156.662278 699.758173h21.097186A10.444152 10.444152 0 0 1 187.994733 710.202325c0 5.765172-4.490985 10.444152-10.235269 10.444152H156.662278v21.097186A10.444152 10.444152 0 0 1 146.218126 751.978932a10.277045 10.277045 0 0 1-10.444152-10.235269V720.646477H114.676787A10.444152 10.444152 0 0 1 104.441518 710.202325c0-5.765172 4.490985-10.444152 10.235269-10.444152H135.773974v-21.097187A10.444152 10.444152 0 0 1 146.218126 668.425717c5.765172 0 10.444152 4.490985 10.444152 10.235269v21.097187z m1378.628042-83.553215v-21.097186A10.277045 10.277045 0 0 0 1524.846168 584.872503a10.444152 10.444152 0 0 0-10.444152 10.235269v21.097186h-21.097186a10.277045 10.277045 0 0 0-10.235269 10.444152c0 5.598065 4.595427 10.444152 10.235269 10.444152h21.097186v21.097187c0 5.744284 4.67898 10.235269 10.444152 10.235268a10.444152 10.444152 0 0 0 10.444152-10.235268V637.093262h21.097187c5.744284 0 10.235269-4.67898 10.235268-10.444152a10.444152 10.444152 0 0 0-10.235268-10.444152H1535.29032zM776.460024 960.861969H250.596979A20.80475 20.80475 0 0 1 229.77134 939.973665c0-11.530344 9.462402-20.888304 20.825639-20.888303h94.728457A83.010119 83.010119 0 0 1 334.212859 877.413196v-605.96969A83.49055 83.49055 0 0 1 417.849627 187.994733H480.430984V167.001988A83.49055 83.49055 0 0 1 564.067752 83.553215h501.152182A83.448773 83.448773 0 0 1 1148.856702 167.001988v605.969689c0 15.185797-4.052331 29.410732-11.133466 41.672166h115.554096c11.551232 0 20.909192 9.274407 20.909192 20.888304 0 11.530344-9.295295 20.888304-20.888304 20.888304H1002.638576v20.992745c0 15.185797-4.052331 29.410732-11.133466 41.672166h11.196131c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304h-109.893365c9.545955 16.000441 7.478013 36.972297-6.41271 50.863019a41.672166 41.672166 0 0 1-59.072122 0L776.460024 960.861969z m76.367638-41.776607h66.424806c22.977134 0 41.609501-18.59059 41.609501-41.881049V270.461756c0-22.559368-18.047494-40.690416-40.314426-40.690416H416.303892c-22.266932 0-40.314426 18.214601-40.314426 40.690416v606.742557c0 23.123352 18.799473 41.881049 41.588613 41.881049h317.084449l-10.736588-10.757477a41.693054 41.693054 0 0 1-10.861918-40.377091l-19.718558-19.739447A146.259902 146.259902 0 0 1 502.363703 627.693525a146.218126 146.218126 0 0 1 220.517822 190.981761l19.739447 19.739447a41.630389 41.630389 0 0 1 40.377091 10.841029L852.827662 919.085362zM1002.638576 814.643843h62.852906A41.797496 41.797496 0 0 0 1107.080095 772.867236V167.106429c0-23.14424-18.632367-41.776607-41.588613-41.776607H563.775316A41.797496 41.797496 0 0 0 522.207592 167.106429v20.888304h396.794216A83.448773 83.448773 0 0 1 1002.638576 271.443506V814.643843zM266.325872 46.998683h31.123572c8.773088 0 15.875111 6.955805 15.875111 15.666228 0 8.647758-7.102023 15.666228-15.875111 15.666228h-31.123572v31.123572c0 8.773088-6.955805 15.875111-15.666228 15.875111a15.770669 15.770669 0 0 1-15.666228-15.875111V78.331139H203.869844A15.728893 15.728893 0 0 1 187.994733 62.664911c0-8.647758 7.102023-15.666228 15.875111-15.666228h31.123572V15.875111c0-8.773088 6.955805-15.875111 15.666228-15.875111 8.647758 0 15.666228 7.102023 15.666228 15.875111v31.123572zM20.888304 939.973665c0-11.530344 9.462402-20.888304 20.825638-20.888303h125.455152c11.488567 0 20.825639 9.274407 20.825639 20.888303 0 11.530344-9.462402 20.888304-20.825639 20.888304H41.713942A20.80475 20.80475 0 0 1 20.888304 939.973665z m658.733544-135.021995a104.441518 104.441518 0 1 0-147.722083-147.722083 104.441518 104.441518 0 0 0 147.722083 147.722083zM459.542681 313.324555a20.888304 20.888304 0 0 1 20.867415-20.888304H710.202325a20.888304 20.888304 0 1 1 0 41.776608H480.430984A20.825639 20.825639 0 0 1 459.542681 313.324555z m0 104.441518c0-11.530344 9.295295-20.888304 20.742085-20.888303h334.505295c11.44679 0 20.742086 9.274407 20.742086 20.888303 0 11.530344-9.295295 20.888304-20.742086 20.888304H480.284766A20.762974 20.762974 0 0 1 459.542681 417.766073z m0 104.441519c0-11.530344 9.316183-20.888304 20.846527-20.888304h146.301679c11.509455 0 20.846527 9.274407 20.846527 20.888304 0 11.530344-9.316183 20.888304-20.846527 20.888303h-146.301679A20.80475 20.80475 0 0 1 459.542681 522.207592zM62.664911 396.87777a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911zM1357.739739 271.547948a62.664911 62.664911 0 1 1 0-125.329822 62.664911 62.664911 0 0 1 0 125.329822z m0-31.332456a31.332456 31.332456 0 1 0 0-62.664911 31.332456 31.332456 0 0 0 0 62.664911z"
                fill="#ffffff" p-id="11979"></path>
          </svg>
          <div>暂无记录</div>
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
import {listRecord} from "@/api/lover";
import './css/button.css'
import {parseTime} from '@/utils/dateUitls';

export default {
  name: "record",
  data() {
    return {
      recordList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        state: 0,
        title: null,
      },
      total: 0,
      scrollLoading: true,
      isList: false,
      loading: true,
    }
  },
  watch: {
    'queryParams.title': {
      handler: function () {
        this.listRecords();
      }
    }
  },
  methods: {
    parseTimes(value, args) {
      return parseTime(value, args);
    },
    recordIndex(id) {
      this.$router.push({
        path: '/record-index',
        query: {id: id}
      })
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
          listRecord(this.queryParams).then(res => {
            res.rows.forEach(item => {
              this.recordList.push(item)
            })
            this.total = res.total;
          }).finally(() => {
            this.scrollLoading = true;
            if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
              this.isList = true;
              return;
            }
            this.isList = false;
          })
        }
      }
    },
    listRecords() {
      this.queryParams.pageNum = 1;
      listRecord(this.queryParams).then(res => {
        this.recordList = res.rows;
        this.total = res.total;
        this.loading = false;
        if (this.total > (this.queryParams.pageNum) * this.queryParams.pageSize) {
          this.isList = true;
          return;
        }
        this.isList = false;
      })
    }
  },
  mounted() {
    this.listRecords();
  },
  created() {
    //添加滚动监听事件
    window.addEventListener('scroll', this.getData, true);
  },
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style>
.content-cl {
  text-align: center;
  overflow-y: auto;
}

.content-cl img {
  border: none;
  max-width: 200px;
}
/* 现代浏览器only */
.hr-twill-colorful {
  border: 0;
  margin: 10px 0 20px 0;
  padding: 3px;
  background: linear-gradient(135deg, #9b59b6, #ff4757, #05fcf8, #fff200, #1e90ff);
  --mask-image: repeating-linear-gradient(135deg, #000 0px, #000 1px, transparent 1px, transparent 6px);
  -webkit-mask-image: var(--mask-image);
  mask-image: var(--mask-image);
}

/* From www.lingdaima.com */
.card {
  padding: 6px;
  --color-1: #c7ecee;
  --color-2: #c7ecee;
  --color-3: rgb(28, 0, 48);
  color: black;
  min-height: 480px;
  margin-bottom: 60px;
  background: #fefefe;
  width: 500px;
  height: 400px;
  box-shadow: -10px -10px var(--color-1),
  15px 10px var(--color-2);
  transition: box-shadow 0.25s ease-in-out,
  transform 0.25s ease-in-out;
}

.card:hover {
  transform: scale(1.025);
}

.record-pc-content {
  width: 90%;
  margin: auto;
  color: #fefefe;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
}

@media (max-width: 789px) {
  .card {
    max-width: 100%;
  }

  .record-pc-content {
    padding: 0px;
  }

  .content-cl img {
    max-width: 150px;
  }
}
</style>