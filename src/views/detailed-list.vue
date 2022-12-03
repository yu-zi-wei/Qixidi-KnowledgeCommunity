<template>
  <div class="mt-20">
    <div class="text-center mb-20">
      <button class="detailed-button">
  <span class="button-content">
    <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M0 0H24V24H0z" fill="none"></path><path
        d="M12.001 4.529c2.349-2.109 5.979-2.039 8.242.228 2.262 2.268 2.34 5.88.236 8.236l-8.48 8.492-8.478-8.492c-2.104-2.356-2.025-5.974.236-8.236 2.265-2.264 5.888-2.34 8.244-.228z"
        fill="currentColor"></path></svg>
    <span style="color: #ce5fee">爱情清单</span>
  </span>
      </button>
    </div>
    <a-grid :cols="{ xs: 1, sm: 1, md: 2, lg: 2, xl: 2, xxl: 3 }" :colGap="40" :rowGap="16" class="grid-demo-grid">
      <a-grid-item class="list-cl" v-for="item of detailedList">
        <div class="box-gou" v-if="item.state==0">
          <span style="line-height: 28px">
            <svg t="1668952537738" class="icon" viewBox="0 0 1024 1024" version="1.1"
                 xmlns="http://www.w3.org/2000/svg" p-id="3765" width="16" height="16"><path
                d="M60.217477 633.910561c0 0 250.197342 104.557334 374.563838 330.628186 149.378146-279.762705 436.109566-540.713972 521.05012-560.013527 0-115.776863 0-163.394371 0-341.442486-342.237595 226.070852-506.576477 642.342604-506.576477 642.342604l-180.049702-191.614086L60.217477 633.910561z"
                p-id="3766" data-spm-anchor-id="a313x.7781069.0.i0" class="selected" fill="#ffffff"></path></svg>
          </span>
        </div>
        <div class="box-gou" v-else>
          <div class="box-out"></div>
        </div>
        <a-collapse :default-active-key="[1]" accordion>
          <a-collapse-item :header="item.name" key="1">
            <div v-if="item.state==0">
              <div class="content-cl" v-html="item.content"></div>
              <div style="font-size: 10px;margin-top: 10px;margin-bottom: 4px;overflow:hidden;">
                <div v-if="item.address" class="fl-left mr-15" style="height: 20px">
                  <span class="fl-left">
                    <svg t="1669690071448" class="icon" viewBox="0 0 1024 1024" version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6300" data-spm-anchor-id="a313x.7781069.0.i4"
                         width="16" height="16" style="margin-top: 1px"><path
                        d="M511.744 68.266667c-173.5168 0-314.026667 136.311467-314.7776 305.937066 0 60.910933 18.1248 118.903467 51.7632 168.465067l3.293867 4.693333 1.911466 3.1744 1.570134 2.389334c1.058133 1.553067 2.184533 3.037867 3.447466 4.5056l0.785067 0.853333 200.174933 232.823467a68.266667 68.266667 0 0 0 103.645867-0.170667L762.641067 558.08l-1.314134 1.450667a50.346667 50.346667 0 0 0 5.341867-6.621867l1.536-2.3552c0.631467-0.989867 1.860267-3.072 1.826133-3.003733 35.293867-49.322667 55.0912-109.431467 55.825067-172.782934C825.856 204.9536 684.970667 68.266667 511.744 68.266667z m0 68.266666c135.970133 0 245.845333 106.5984 245.845333 237.824a235.400533 235.400533 0 0 1-43.9808 134.775467l-2.952533 4.676267-198.997333 232.789333-200.192-232.823467-1.928534-3.191466-0.989866-1.450667a230.229333 230.229333 0 0 1-43.3152-134.775467C265.8304 242.858667 375.415467 136.533333 511.744 136.533333z"
                        fill="#ffffff" p-id="6301"></path><path
                        d="M783.803733 714.734933a34.133333 34.133333 0 0 1 45.243734 10.018134l1.4336 2.2528 73.386666 125.730133a68.266667 68.266667 0 0 1-54.784 102.5536l-4.5568 0.119467-666.043733-3.6352a68.266667 68.266667 0 0 1-60.654933-98.850134l2.133333-3.9424 69.9392-119.261866a34.133333 34.133333 0 0 1 60.16 32.170666l-1.262933 2.3552-69.9392 119.261867 666.043733 3.6352-73.386667-125.730133a34.133333 34.133333 0 0 1 12.288-46.677334z"
                        fill="#ffffff" p-id="6302"></path><path
                        d="M512 243.950933a136.533333 136.533333 0 1 0 0.034133 273.1008A136.533333 136.533333 0 0 0 512 243.950933z m0 68.266667a68.266667 68.266667 0 1 1-0.034133 136.567467A68.266667 68.266667 0 0 1 512 312.2176z"
                        fill="#f4ea2a" p-id="6303" data-spm-anchor-id="a313x.7781069.0.i5"
                        class="selected"></path></svg>
                  </span>
                  <span style="line-height: 20px;font-size: 12px;margin-left: 4px" v-text="item.address"></span>
                </div>
                <div v-if="item.address" class="fl-left mr-10" style="height: 20px">
                <span style="line-height: 20px" class="fl-left">
                  <svg t="1669798181975" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="9622" width="16" height="16" style="margin-top: 1px;"><path
                      d="M865.8 924.2H161.2c-34.3 0-62.2-27.9-62.2-62.2V217.2c0-34.3 27.9-62.2 62.2-62.2h704.6c34.3 0 62.2 27.9 62.2 62.2V862c0 34.3-27.9 62.2-62.2 62.2zM161.2 196.5c-11.4 0-20.7 9.3-20.7 20.7V862c0 11.4 9.3 20.7 20.7 20.7h704.6c11.4 0 20.7-9.3 20.7-20.7V217.2c0-11.4-9.3-20.7-20.7-20.7H161.2z"
                      fill="#ffffff" p-id="9623"></path><path
                      d="M119.8 390.2h772.6v41.4H119.8zM260 533.9h87.8v87.8H260zM469.6 533.9h87.8v87.8h-87.8zM679.3 533.9h87.8v87.8h-87.8z"
                      fill="#ffffff" p-id="9624"></path><path
                      d="M260 685.8h87.8v87.8H260zM469.6 685.8h87.8v87.8h-87.8zM679.3 685.8h87.8v87.8h-87.8z"
                      fill="#ffffff" p-id="9625"></path><path
                      d="M333.6 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7zM693.5 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7z"
                      fill="#ffffff" p-id="9626"></path></svg>
                </span>
                  <span style="line-height: 20px;font-size: 12px;margin-left: 4px">{{ item.updateTime }}</span>
                </div>
              </div>
            </div>
            <div v-else>
              待完成！
            </div>
          </a-collapse-item>
        </a-collapse>
        <div></div>
      </a-grid-item>
    </a-grid>
    <div @click="getData" v-if="isList" style="margin: auto;text-align: center;">
      <div class="mt-20">
        <button class="button-bo-cl">
          <span class="fl-left">查看更多</span>
          <span class="fl-left ml-2">...</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {listRepertoire} from "@/api/lover";
import './css/button.css'

export default {
  name: "detailedList",
  data() {
    return {
      complete: false,
      detailedList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      scrollLoading: true,
      isList: false,
    }
  },
  methods: {
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
  destroyed() {
    //离开页面时删除该监听
    window.removeEventListener('scroll', this.getData, true)
  }
}
</script>

<style>
.box-out {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  /*background: linear-gradient(0deg, rgba(211, 226, 219, 1) 0%, rgba(223, 187, 237, 1) 22%, rgba(254, 254, 254, 1) 100%);*/
  background: #d980fa;
  margin: 6px 0 0 6px;
  transition: all 0.4s;
}

.box-out:hover {
  transform: scale(1.4);
  background: #ce5fee;
}

.box-gou {
  margin-top: 8px;
  margin-right: 6px;
  overflow: hidden;
  background-color: #be2edd;
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
  background-color: #ce5fee;
  margin-top: 15px;
  padding: 10px;
  color: #fefefe;
  border-radius: 4px;
  /*-webkit-box-shadow:0px 0px 105px 45px #d87cf9;*/
  /*-moz-box-shadow: 0px 0px 105px 45px #d87cf9;*/
  /*box-shadow: 0px 0px 105px 45px #d87cf9;*/
}

.arco-checkbox-label {
  color: #fefefe;
  font-weight: bold;
}

.arco-checkbox-checked .arco-checkbox-icon {
  background-color: #be2edd;
}

.arco-collapse-item-header {
  background-color: #c84be7;
  border: none;
  color: #fefefe;
}

.arco-collapse-item .arco-collapse-item-expand-icon {
  /*color: #be2edd;*/
  color: #fefefe;
}

.arco-collapse-item .arco-collapse-item-expand-icon:hover {

}

.arco-collapse-item-active > .arco-collapse-item-header {
  background-color: #be2edd;
  color: #fefefe;
}

.arco-collapse-item-content {
  background-color: #be2edd;
  color: #fefefe;
}

.arco-collapse {
  border: none;
  min-width: 160px;
}

/* From www.lingdaima.com */
.detailed-button {
  font-size: 17px;
  font-family: inherit;
  font-weight: 700;
  padding: 4px;
  border-radius: 20px;
  border: none;
  color: black;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 25px -5px, rgba(0, 0, 0, 0.04) 0px 10px 10px -5px;
  background: linear-gradient(0deg, #ce5fee 0%, rgba(255, 213, 0, 1) 47%, #ce5fee 47%, #ce5fee 100%);
}

.detailed-button .button-content {
  display: flex;
  align-items: center;
  background: white;
  cursor: pointer;
  padding: 0.7em 1.5em;
  padding-left: 1.2em;
  border-radius: 16px;
}

.detailed-button svg {
  width: 22px;
  height: 22px;
  margin-right: 6px;
  color: #be2edd;
  transition: transform 0.3s;
}

.detailed-button:hover svg {
  transform: scale(1.2);
}


</style>