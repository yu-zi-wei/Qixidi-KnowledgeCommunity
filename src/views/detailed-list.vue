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
              {{ item.name }}
              <div style="font-size: 10px;margin-top: 10px;margin-bottom: 4px;overflow:hidden;">
                <svg t="1668670124651" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="7732" data-spm-anchor-id="a313x.7781069.0.i3" width="12"
                     height="12" style="float: left">
                  <path d="M480 896h64v128h-64zM0 480h128v64H0zM768 544h-288V256h64v224h224v64z" fill="#f4ea2a"
                        p-id="7733" data-spm-anchor-id="a313x.7781069.0.i4" class="selected"></path>
                  <path d="M512 1024v-64A448 448 0 1 0 64 512H0A512 512 0 0 1 512 0a512 512 0 0 1 0 1024z"
                        fill="#fefefe"
                        p-id="7734" data-spm-anchor-id="a313x.7781069.0.i5" class=""></path>
                  <path
                      d="M0 546.24V512h64v29.76zM411.2 1014.08a544 544 0 0 1-64-17.28l20.48-60.48a475.2 475.2 0 0 0 56.64 15.04z m-126.08-42.88A523.84 523.84 0 0 1 227.2 937.6l35.52-53.12a472.32 472.32 0 0 0 50.56 29.44zM174.4 896A472.64 472.64 0 0 1 128 849.6l48-42.24a506.24 506.24 0 0 0 41.28 41.6zM86.4 796.8a523.84 523.84 0 0 1-33.6-57.92l57.28-28.48a416 416 0 0 0 29.44 50.56zM27.2 677.12a506.88 506.88 0 0 1-17.28-64l64-12.48a470.08 470.08 0 0 0 15.04 56.32zM512 1024h-33.92l3.84-64H512z"
                      fill="#f4ea2a" p-id="7735"></path>
                </svg>
                <div style="float: left;line-height: 12px;margin-left: 6px">
                  {{ item.updateTime }}
                </div>
              </div>
            </div>
            <div v-else>
              待完成！
            </div>
          </a-collapse-item>
        </a-collapse>
        <!--        </a-checkbox>-->
        <div></div>
      </a-grid-item>
    </a-grid>
  </div>
</template>

<script>
export default {
  name: "detailedList",
  setup() {
    return {
      complete: false,
      detailedList: [
        {name: '一起做饭', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起爬山', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起看电影', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起看过生日', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起看看星星', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起给对方写信，然后读给对方听', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去旅游', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起坐一次摩天轮', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去看一次海，牵着手沙滩散步', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起看书，分享自己喜欢的书籍', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起看烟花', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去一次教堂', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起换发型，剪头发', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起露营，住一次帐篷', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去上过的小学，中学，大学', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起睡个懒觉，赖个床', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起放一次风筝', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起敷面膜', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去看一场演唱会', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起玩一次真心话大冒险', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起去见父母', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起挑选戒指', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '一起挑选婚纱', state: 1, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
        {name: '在一起啊，也不算太久，就余生吧', state: 0, createTime: '2022年11月18日', updateTime: '2022年11月18日'},
      ]
    }
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