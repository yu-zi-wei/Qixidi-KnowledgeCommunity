<template>
  <div style="margin: auto">
    <a-row :gutter="24">
      <a-col :span="2" style="height: 1px">
      </a-col>
      <a-col :span="20">
        <div class="content-index-cl">
          <router-link to="/record">
            <div class="button-return">
              <svg t="1670076593666" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg"
                   p-id="15532" width="12" height="12">
                <path
                    d="M475.591111 300.373333V175.217778c-11.377778-52.337778-54.613333-20.48-54.613333-20.48L120.604444 411.875556c-65.991111 45.511111-4.551111 79.644444-4.551111 79.644444l295.822223 254.862222c59.164444 43.235556 63.715556-22.755556 63.715555-22.755555v-116.053334C775.964444 514.275556 898.844444 887.466667 898.844444 887.466667c11.377778 20.48 18.204444 0 18.204445 0 116.053333-559.786667-441.457778-587.093333-441.457778-587.093334z"
                    fill="#ffffff" p-id="15533"></path>
              </svg>
              <span style="color: #fefefe"> 返回</span>
            </div>
          </router-link>

          <h2 class="mb-10 text-center" v-text="info.title"></h2>
          <div class="overflow-hidden mb-20 text-center">
            <div style="float: left;height: 30px;min-width: 10px;margin-left: 40%" v-if="info.address">
             <span style="float: left">
                    <svg t="1669690071448" class="icon" viewBox="0 0 1024 1024" version="1.1"
                         xmlns="http://www.w3.org/2000/svg" p-id="6300" data-spm-anchor-id="a313x.7781069.0.i4"
                         width="16" height="16"><path
                        d="M511.744 68.266667c-173.5168 0-314.026667 136.311467-314.7776 305.937066 0 60.910933 18.1248 118.903467 51.7632 168.465067l3.293867 4.693333 1.911466 3.1744 1.570134 2.389334c1.058133 1.553067 2.184533 3.037867 3.447466 4.5056l0.785067 0.853333 200.174933 232.823467a68.266667 68.266667 0 0 0 103.645867-0.170667L762.641067 558.08l-1.314134 1.450667a50.346667 50.346667 0 0 0 5.341867-6.621867l1.536-2.3552c0.631467-0.989867 1.860267-3.072 1.826133-3.003733 35.293867-49.322667 55.0912-109.431467 55.825067-172.782934C825.856 204.9536 684.970667 68.266667 511.744 68.266667z m0 68.266666c135.970133 0 245.845333 106.5984 245.845333 237.824a235.400533 235.400533 0 0 1-43.9808 134.775467l-2.952533 4.676267-198.997333 232.789333-200.192-232.823467-1.928534-3.191466-0.989866-1.450667a230.229333 230.229333 0 0 1-43.3152-134.775467C265.8304 242.858667 375.415467 136.533333 511.744 136.533333z"
                        fill="#ffffff" p-id="6301"></path><path
                        d="M783.803733 714.734933a34.133333 34.133333 0 0 1 45.243734 10.018134l1.4336 2.2528 73.386666 125.730133a68.266667 68.266667 0 0 1-54.784 102.5536l-4.5568 0.119467-666.043733-3.6352a68.266667 68.266667 0 0 1-60.654933-98.850134l2.133333-3.9424 69.9392-119.261866a34.133333 34.133333 0 0 1 60.16 32.170666l-1.262933 2.3552-69.9392 119.261867 666.043733 3.6352-73.386667-125.730133a34.133333 34.133333 0 0 1 12.288-46.677334z"
                        fill="#ffffff" p-id="6302"></path><path
                        d="M512 243.950933a136.533333 136.533333 0 1 0 0.034133 273.1008A136.533333 136.533333 0 0 0 512 243.950933z m0 68.266667a68.266667 68.266667 0 1 1-0.034133 136.567467A68.266667 68.266667 0 0 1 512 312.2176z"
                        fill="#f4ea2a" p-id="6303" data-spm-anchor-id="a313x.7781069.0.i5"
                        class="selected"></path></svg>
                  </span>
              <span class="span-2-cl" v-text="info.address"></span>
            </div>
            <div class="ml-10 fl-left" style="height: 30px;min-width: 10px;" v-if="info.createTime">
            <span style="float: left">
                  <svg t="1669798181975" class="icon" viewBox="0 0 1024 1024" version="1.1"
                       xmlns="http://www.w3.org/2000/svg" p-id="9622" width="16" height="16"><path
                      d="M865.8 924.2H161.2c-34.3 0-62.2-27.9-62.2-62.2V217.2c0-34.3 27.9-62.2 62.2-62.2h704.6c34.3 0 62.2 27.9 62.2 62.2V862c0 34.3-27.9 62.2-62.2 62.2zM161.2 196.5c-11.4 0-20.7 9.3-20.7 20.7V862c0 11.4 9.3 20.7 20.7 20.7h704.6c11.4 0 20.7-9.3 20.7-20.7V217.2c0-11.4-9.3-20.7-20.7-20.7H161.2z"
                      fill="#ffffff" p-id="9623"></path><path
                      d="M119.8 390.2h772.6v41.4H119.8zM260 533.9h87.8v87.8H260zM469.6 533.9h87.8v87.8h-87.8zM679.3 533.9h87.8v87.8h-87.8z"
                      fill="#ffffff" p-id="9624"></path><path
                      d="M260 685.8h87.8v87.8H260zM469.6 685.8h87.8v87.8h-87.8zM679.3 685.8h87.8v87.8h-87.8z"
                      fill="#ffffff" p-id="9625"></path><path
                      d="M333.6 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7zM693.5 251.7c-11.4 0-20.7-9.3-20.7-20.7V120.5c0-11.4 9.3-20.7 20.7-20.7s20.7 9.3 20.7 20.7V231c0 11.5-9.3 20.7-20.7 20.7z"
                      fill="#ffffff" p-id="9626"></path></svg>
                </span>

              <span class="span-2-cl" v-text="info.createTime"></span>
            </div>
          </div>
          <div class="content-img-cl" v-html=" info.content">
          </div>
        </div>
      </a-col>
      <a-col :span="2" style="height: 1px">
      </a-col>
    </a-row>
  </div>
</template>

<script>
import {listRecordInfo} from "@/api/lover";

export default {
  name: "recordIndex",
  data() {
    return {
      info: {
        title: null,
        content: null,
        address: null,
        createTime: null,
      },
    }
  },
  methods: {
    listRecordInfos() {
      let id = this.$route.query.id;
      listRecordInfo(id).then(res => {
        this.info = res.data;
      })
    }
  },
  mounted() {
    this.listRecordInfos();
  }
}
</script>

<style>
.content-img-cl {
  font-size: 16px;
  line-height: 20px;
}

.content-img-cl img {
  border: none;
  max-width: 100%;
  padding: 20px 0;
}

.span-2-cl {
  font-size: 12px;
  margin-left: 4px;
  float: left;
  margin-top: 2px
}

.button-return {
  background-color: #ce5fee;
  border-radius: 14px;
  padding: 8px 16px;
  cursor: pointer;
  width: 50px;
  font-size: 12px;
  margin-bottom: 10px;
}

.button-return:hover {
  background-color: #be2edd;
}

.content-index-cl {
  background-color: #c84be7;
  border-radius: 10px;
  color: #fefefe;
  padding: 20px 40px;
  margin-bottom: 40px;
}
</style>