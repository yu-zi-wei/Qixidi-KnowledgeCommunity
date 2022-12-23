<template>
  <div class="mt-20">
    <div style="margin-bottom: 60px">
      <div class="text-center">
        <button class="record-button">
          <span class="fl-left">
          <svg t="1668589585311" class="icon" viewBox="0 0 1024 1024" version="1.1"
               xmlns="http://www.w3.org/2000/svg" p-id="37761" data-spm-anchor-id="a313x.7781069.0.i45" width="40"
               height="36">
            <path
                d="M638.46 298.5h-369a30 30 0 0 0 0 60h369a30 30 0 0 0 0-60zM651.44 539.05L665.51 525a30 30 0 0 0-27.05-43h-369a30 30 0 0 0 0 60h369a29.87 29.87 0 0 0 12.98-2.95zM538.1 665.5H269.46a30 30 0 0 0 0 60H510.4zM623.11 715.78l-21.05 9.72h36.4A30 30 0 0 0 662 676.9z"
                p-id="37762" data-spm-anchor-id="a313x.7781069.0.i47" class="selected" fill="#fefefe"></path>
            <path
                d="M730 608.94V774a49.06 49.06 0 0 1-49 49H227a49.06 49.06 0 0 1-49-49V250a49.06 49.06 0 0 1 49-49h454a49.06 49.06 0 0 1 49 49v210.53l60-60V250a109.12 109.12 0 0 0-109-109H227a109.12 109.12 0 0 0-109 109v524a109.12 109.12 0 0 0 109 109h454a109.12 109.12 0 0 0 109-109V548.94zM930.73 401.81A48 48 0 0 0 862.89 334z"
                p-id="37763" data-spm-anchor-id="a313x.7781069.0.i46" class="" fill="#fff200"></path>
            <path
                d="M558.106 638.704l292.742-292.742 67.882 67.882-292.742 292.743zM513.06 730.84l-12.87 26.63a5.37 5.37 0 0 0 7.18 7.18L534 751.78zM541.63 748.09l68.64-33.18-60.35-60.35-33.17 68.65 24.88 24.88z"
                p-id="37764" data-spm-anchor-id="a313x.7781069.0.i48" class="" fill="#55efc4"></path>
          </svg>
          </span>
          <span style="line-height: 36px; letter-spacing: 3px;">点点滴滴</span>
        </button>
      </div>
    </div>
    <div class="text-center">
      <div class="search-box" tabindex="1">
        <input v-model="queryParams.title" type="text" class="search-txt" placeholder="标题关键字..."
               style="color:#636e72;outline:none;"/>
        <a class="search-btn">
          <svg t="1671602595081" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
               p-id="1401" width="16" height="16">
            <path
                d="M953.474215 908.234504l-152.576516-163.241391c61.92508-74.48211 95.81186-167.36973 95.81186-265.073744 0-229.294809-186.63531-415.930119-416.102133-415.930119-229.294809 0-415.930119 186.63531-415.930119 415.930119s186.63531 415.930119 415.930119 415.930119c60.032925 0 118.00168-12.55703 172.186125-37.327062 16.169326-7.396607 23.221905-26.318159 15.825298-42.315471-7.396607-16.169326-26.318159-23.221905-42.315471-15.825298-45.927768 20.813707-94.951789 31.478582-145.695952 31.478582-194.031917 0-351.94087-157.908953-351.94087-351.94087 0-194.031917 157.908953-351.94087 351.94087-351.94087 194.031917 0 351.94087 157.908953 351.94087 351.94087 0 91.339493-34.918864 177.86259-98.048043 243.743995-12.213002 12.729044-11.868974 33.026709 0.860071 45.239711 1.032085 0.860071 2.236183 1.204099 3.268268 2.064169 0.860071 1.204099 1.376113 2.752226 2.408198 3.956325l165.477574 177.00252c6.192508 6.70855 14.793214 10.148833 23.393919 10.148833 7.912649 0 15.653284-2.92424 21.845792-8.600706C964.827146 941.433227 965.515202 921.135562 953.474215 908.234504z"
                fill="#ffffff" p-id="1402"></path>
          </svg>
        </a>
      </div>
    </div>
    <a-spin :loading="loading" tip="正在赶来的路上..." style="width: 100%;margin-top: 40px" :size="28">
      <div v-if="recordList.length>0">
        <a-row v-for="(item,i) in  recordList" style="width: 100%;margin: auto;color: #fefefe">
          <a-col :span="3">
          </a-col>
          <a-col :span="8">
            <div v-if="i % 2 ==1" class="padding-10 mt-2">
              <div class="card">
                <h2>{{ item.title }}</h2>
                <hr class="hr-twill-colorful"/>
                <div @click="recordIndex(item.id)" style="padding: 0 10px 10px 6px" class="content-cl cursor-pointer"
                     title="点击查看"
                     v-html="item.content"></div>
              </div>
              <div style="height: 20px;overflow: hidden">
                <div class="fl-left mr-15" v-if="item.address">
                 <span class="fl-left">
                    <svg t="1669690071448" class="icon" viewBox="0 0 1024 1024" version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6300" data-spm-anchor-id="a313x.7781069.0.i4"
                         width="20" height="20"><path
                        d="M511.744 68.266667c-173.5168 0-314.026667 136.311467-314.7776 305.937066 0 60.910933 18.1248 118.903467 51.7632 168.465067l3.293867 4.693333 1.911466 3.1744 1.570134 2.389334c1.058133 1.553067 2.184533 3.037867 3.447466 4.5056l0.785067 0.853333 200.174933 232.823467a68.266667 68.266667 0 0 0 103.645867-0.170667L762.641067 558.08l-1.314134 1.450667a50.346667 50.346667 0 0 0 5.341867-6.621867l1.536-2.3552c0.631467-0.989867 1.860267-3.072 1.826133-3.003733 35.293867-49.322667 55.0912-109.431467 55.825067-172.782934C825.856 204.9536 684.970667 68.266667 511.744 68.266667z m0 68.266666c135.970133 0 245.845333 106.5984 245.845333 237.824a235.400533 235.400533 0 0 1-43.9808 134.775467l-2.952533 4.676267-198.997333 232.789333-200.192-232.823467-1.928534-3.191466-0.989866-1.450667a230.229333 230.229333 0 0 1-43.3152-134.775467C265.8304 242.858667 375.415467 136.533333 511.744 136.533333z"
                        fill="#ffffff" p-id="6301"></path><path
                        d="M783.803733 714.734933a34.133333 34.133333 0 0 1 45.243734 10.018134l1.4336 2.2528 73.386666 125.730133a68.266667 68.266667 0 0 1-54.784 102.5536l-4.5568 0.119467-666.043733-3.6352a68.266667 68.266667 0 0 1-60.654933-98.850134l2.133333-3.9424 69.9392-119.261866a34.133333 34.133333 0 0 1 60.16 32.170666l-1.262933 2.3552-69.9392 119.261867 666.043733 3.6352-73.386667-125.730133a34.133333 34.133333 0 0 1 12.288-46.677334z"
                        fill="#ffffff" p-id="6302"></path><path
                        d="M512 243.950933a136.533333 136.533333 0 1 0 0.034133 273.1008A136.533333 136.533333 0 0 0 512 243.950933z m0 68.266667a68.266667 68.266667 0 1 1-0.034133 136.567467A68.266667 68.266667 0 0 1 512 312.2176z"
                        fill="#f4ea2a" p-id="6303" data-spm-anchor-id="a313x.7781069.0.i5"
                        class="selected"></path></svg>
                  </span>
                  <span style="font-size: 14px;margin-left: 4px;line-height: 20px" v-text="item.address"></span>
                </div>
                <div>
                  <svg t="1669798181975" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="9622" width="18" height="18"
                       style="float: left">
                    <path
                        d="M865.8 924.2H161.2c-34.3 0-62.2-27.9-62.2-62.2V217.2c0-34.3 27.9-62.2 62.2-62.2h704.6c34.3 0 62.2 27.9 62.2 62.2V862c0 34.3-27.9 62.2-62.2 62.2zM161.2 196.5c-11.4 0-20.7 9.3-20.7 20.7V862c0 11.4 9.3 20.7 20.7 20.7h704.6c11.4 0 20.7-9.3 20.7-20.7V217.2c0-11.4-9.3-20.7-20.7-20.7H161.2z"
                        fill="#ffffff" p-id="9623"></path>
                    <path
                        d="M119.8 390.2h772.6v41.4H119.8zM260 533.9h87.8v87.8H260zM469.6 533.9h87.8v87.8h-87.8zM679.3 533.9h87.8v87.8h-87.8z"
                        fill="#ffffff" p-id="9624"></path>
                    <path
                        d="M260 685.8h87.8v87.8H260zM469.6 685.8h87.8v87.8h-87.8zM679.3 685.8h87.8v87.8h-87.8z"
                        fill="#ffffff" p-id="9625"></path>
                    <path
                        d="M333.6 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7zM693.5 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7z"
                        fill="#ffffff" p-id="9626"></path>
                  </svg>
                  <span style="line-height: 20px;margin-left: 4px;float: left;font-size: 13px"
                        v-text="parseTimes(item.createTime,'{y}-{m}-{d}')"></span>
                </div>
              </div>
            </div>
          </a-col>
          <a-col :span="1">
          </a-col>
          <a-col :span="1">
            <div class="outer" id="xuanzhun">
              <div class="inner"></div>
            </div>
            <div style="background-color: #05fcf8;min-height: 250px;width: 2px;border-radius: 2px;margin: 10px"></div>
          </a-col>
          <a-col :span="8">
            <div v-if="i % 2 ==0" class="padding-10 mt-2">
              <div class="card">
                <h2>{{ item.title }}</h2>
                <hr class="hr-twill-colorful"/>
                <div style="padding: 0 10px 10px 6px" @click="recordIndex(item.id)" class="content-cl cursor-pointer"
                     title="点击查看"
                     v-html="item.content"></div>
              </div>
              <div style="height: 20px;overflow: hidden">
                <div class="fl-left mr-15" v-if="item.address">
                 <span class="fl-left">
                    <svg t="1669690071448" class="icon" viewBox="0 0 1024 1024" version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6300" data-spm-anchor-id="a313x.7781069.0.i4"
                         width="20" height="20"><path
                        d="M511.744 68.266667c-173.5168 0-314.026667 136.311467-314.7776 305.937066 0 60.910933 18.1248 118.903467 51.7632 168.465067l3.293867 4.693333 1.911466 3.1744 1.570134 2.389334c1.058133 1.553067 2.184533 3.037867 3.447466 4.5056l0.785067 0.853333 200.174933 232.823467a68.266667 68.266667 0 0 0 103.645867-0.170667L762.641067 558.08l-1.314134 1.450667a50.346667 50.346667 0 0 0 5.341867-6.621867l1.536-2.3552c0.631467-0.989867 1.860267-3.072 1.826133-3.003733 35.293867-49.322667 55.0912-109.431467 55.825067-172.782934C825.856 204.9536 684.970667 68.266667 511.744 68.266667z m0 68.266666c135.970133 0 245.845333 106.5984 245.845333 237.824a235.400533 235.400533 0 0 1-43.9808 134.775467l-2.952533 4.676267-198.997333 232.789333-200.192-232.823467-1.928534-3.191466-0.989866-1.450667a230.229333 230.229333 0 0 1-43.3152-134.775467C265.8304 242.858667 375.415467 136.533333 511.744 136.533333z"
                        fill="#ffffff" p-id="6301"></path><path
                        d="M783.803733 714.734933a34.133333 34.133333 0 0 1 45.243734 10.018134l1.4336 2.2528 73.386666 125.730133a68.266667 68.266667 0 0 1-54.784 102.5536l-4.5568 0.119467-666.043733-3.6352a68.266667 68.266667 0 0 1-60.654933-98.850134l2.133333-3.9424 69.9392-119.261866a34.133333 34.133333 0 0 1 60.16 32.170666l-1.262933 2.3552-69.9392 119.261867 666.043733 3.6352-73.386667-125.730133a34.133333 34.133333 0 0 1 12.288-46.677334z"
                        fill="#ffffff" p-id="6302"></path><path
                        d="M512 243.950933a136.533333 136.533333 0 1 0 0.034133 273.1008A136.533333 136.533333 0 0 0 512 243.950933z m0 68.266667a68.266667 68.266667 0 1 1-0.034133 136.567467A68.266667 68.266667 0 0 1 512 312.2176z"
                        fill="#f4ea2a" p-id="6303" data-spm-anchor-id="a313x.7781069.0.i5"
                        class="selected"></path></svg>
                  </span>
                  <span style="font-size: 14px;margin-left: 4px;line-height: 20px" v-text="item.address"></span>
                </div>
                <div>
                  <svg t="1669798181975" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="9622" width="18" height="18" style="float: left">
                    <path
                        d="M865.8 924.2H161.2c-34.3 0-62.2-27.9-62.2-62.2V217.2c0-34.3 27.9-62.2 62.2-62.2h704.6c34.3 0 62.2 27.9 62.2 62.2V862c0 34.3-27.9 62.2-62.2 62.2zM161.2 196.5c-11.4 0-20.7 9.3-20.7 20.7V862c0 11.4 9.3 20.7 20.7 20.7h704.6c11.4 0 20.7-9.3 20.7-20.7V217.2c0-11.4-9.3-20.7-20.7-20.7H161.2z"
                        fill="#ffffff" p-id="9623"></path>
                    <path
                        d="M119.8 390.2h772.6v41.4H119.8zM260 533.9h87.8v87.8H260zM469.6 533.9h87.8v87.8h-87.8zM679.3 533.9h87.8v87.8h-87.8z"
                        fill="#ffffff" p-id="9624"></path>
                    <path
                        d="M260 685.8h87.8v87.8H260zM469.6 685.8h87.8v87.8h-87.8zM679.3 685.8h87.8v87.8h-87.8z"
                        fill="#ffffff" p-id="9625"></path>
                    <path
                        d="M333.6 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7zM693.5 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7z"
                        fill="#ffffff" p-id="9626"></path>
                  </svg>
                  <span style="line-height: 20px;margin-left: 4px;float: left;font-size: 13px"
                        v-text="parseTimes(item.createTime,'{y}-{m}-{d}')"></span>
                </div>
              </div>
            </div>
          </a-col>
          <a-col :span="3">
          </a-col>

        </a-row>
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
.search-box {
  /* 绝对定位 水平垂直居中 */
  position: absolute;
  left: 50%;
  outline: none;
  transform: translate(-50%, -50%);
  background-color: #05fcf8;
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
  color: #636e72;
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

.content-cl img {
  border: none;
  max-width: 100%;
  padding: 20px 0;
}


@-webkit-keyframes rotate {
  from {
    -webkit-transform: rotate(0deg)
  }
  to {
    -webkit-transform: rotate(360deg)
  }
}

@-moz-keyframes rotate {
  from {
    -moz-transform: rotate(0deg)
  }
  to {
    -moz-transform: rotate(359deg)
  }
}

@-o-keyframes rotate {
  from {
    -o-transform: rotate(0deg)
  }
  to {
    -o-transform: rotate(359deg)
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg)
  }
  to {
    transform: rotate(359deg)
  }
}

#xuanzhun {
  -webkit-transition-property: -webkit-transform;
  -webkit-transition-duration: 1s;
  -moz-transition-property: -moz-transform;
  -moz-transition-duration: 1s;
  -webkit-animation: rotate 3s linear infinite;
  -moz-animation: rotate 3s linear infinite;
  -o-animation: rotate 3s linear infinite;
  animation: rotate 3s linear infinite;
}

.outer {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  border: 2px solid #05fcf8;
  border-style: dashed;
}

.outer .inner {
  position: relative;
  top: 50%;
  left: 50%;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background-color: #05fcf8;
  transform: translate(-50%, -50%);
}

/* 现代浏览器only */
.hr-twill-colorful {
  border: 0;
  margin: 10px 0 20px 0;
  padding: 3px;
  background: linear-gradient(135deg, #fefefe, #fff200, #05fcf8, #fff200, #fefefe);
  --mask-image: repeating-linear-gradient(135deg, #000 0px, #000 1px, transparent 1px, transparent 6px);
  -webkit-mask-image: var(--mask-image);
  mask-image: var(--mask-image);
}

.dd-button {
  border: none;
  width: 140px;
  height: 50px;
  cursor: pointer;
  color: #fff;
  z-index: 1;
  display: flex;
  background: #8792eb;
  position: relative;
  border-radius: 6px;
}

.dd-button span {
  margin: 0 auto;
  align-self: center;
  font-size: 17px;
  font-weight: bold;
  text-align: center;
}

.dd-button::after {
  position: absolute;
  content: "";
  width: 100%;
  z-index: -1;
  height: 10%;
  bottom: 0;
  clip-path: polygon(0% 74%, 4% 75%, 8% 76%, 11% 77%, 15% 78%, 20% 78%, 25% 77%, 32% 77%, 37% 75%, 40% 74%, 43% 74%, 46% 73%, 52% 72%, 57% 72%, 65% 74%, 66% 75%, 71% 78%, 75% 82%, 81% 86%, 83% 88%, 88% 91%, 90% 94%, 94% 96%, 98% 98%, 100% 100%, 82% 100%, 0 100%);
  background: #b20cd3;
  transition: 0.2s ease;
  border-radius: 6px;
}

.dd-button::before {
  position: absolute;
  content: "";
  /*   bottom: 80%; */
  transform: rotate(180deg);
  width: 100%;
  height: 10%;
  transition: 0.2s ease;
  /*   bottom:; */
  z-index: -1;
  clip-path: polygon(0% 74%, 4% 75%, 8% 76%, 11% 77%, 15% 78%, 20% 78%, 25% 77%, 32% 77%, 37% 75%, 40% 74%, 43% 74%, 46% 73%, 52% 72%, 57% 72%, 65% 74%, 66% 75%, 71% 78%, 75% 82%, 81% 86%, 83% 88%, 88% 91%, 90% 94%, 94% 96%, 98% 98%, 100% 100%, 82% 100%, 0 100%);
  background: #b20cd3;
  border-radius: 6px;
}

.dd-button:hover::after {
  clip-path: polygon(0 30%, 9% 34%, 7% 39%, 11% 43%, 13% 33%, 17% 30%, 24% 34%, 25% 35%, 30% 31%, 30% 38%, 39% 33%, 35% 43%, 43% 45%, 55% 46%, 65% 74%, 67% 66%, 81% 57%, 75% 82%, 81% 86%, 83% 88%, 88% 91%, 90% 94%, 94% 96%, 98% 98%, 100% 100%, 82% 100%, 0 100%);
  height: 80%;
}

.dd-button:hover::before {
  clip-path: polygon(0 30%, 9% 34%, 7% 39%, 11% 43%, 13% 33%, 17% 30%, 24% 34%, 25% 35%, 30% 31%, 30% 38%, 39% 33%, 35% 43%, 43% 45%, 55% 46%, 65% 74%, 67% 66%, 81% 57%, 75% 82%, 81% 86%, 83% 88%, 88% 91%, 90% 94%, 94% 96%, 98% 98%, 100% 100%, 82% 100%, 0 100%);
  height: 80%;
}

/* From www.lingdaima.com */
.record-button {
  --color-1: rgb(178, 12, 211);
  --color-2: rgb(5, 252, 248);
  --color-3: rgb(28, 0, 48);
  border: none;
  padding: 4px 10px;
  overflow: hidden;
  color: var(--color-2);
  background: #c23be1;
  box-shadow: -10px -10px var(--color-1),
  15px 10px var(--color-2);
  font-weight: bold;
  transition: box-shadow 0.1s ease-in-out,
  transform 0.1s ease-in-out;
}

.record-button:hover {
  box-shadow: -8.275px -8.275px var(--color-1),
  10.4px 8.275px var(--color-2);
}

.record-button:active {
  transform: scale(0.975);
}


/* From www.lingdaima.com */
.card {
  padding: 6px;
  --color-1: rgb(178, 12, 211);
  --color-2: rgb(5, 252, 248);
  --color-3: rgb(28, 0, 48);
  min-width: 40px;
  min-height: 154px;
  margin-bottom: 20px;
  background: #c23be1;
  box-shadow: -10px -10px var(--color-1),
  15px 10px var(--color-2);
  transition: box-shadow 0.25s ease-in-out,
  transform 0.25s ease-in-out;
}

.card:hover {
  /*box-shadow: -10.275px -10.275px var(--color-1),*/
  /*15.4px 10.275px var(--color-2);*/
  transform: scale(1.025);
}


.arco-timeline-item-label {
  color: #fefefe;
}

.arco-card {
  background-color: #c84be7;
  border: 1px solid #ce5fee;
}

.arco-card-header {
  border-bottom: 1px solid #be2edd;
}

.arco-spin-tip, .arco-spin-icon {
  color: #fefefe;
}

.arco-spin-mask {
  background-color: transparent;
}

</style>