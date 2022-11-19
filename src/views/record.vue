<template>
  <div class="mt-20">
    <div style="margin-bottom: 60px">
      <div style="margin: auto" class="dd-button">
        <span>
        <svg t="1668589585311" class="icon" viewBox="0 0 1024 1024" version="1.1"
             xmlns="http://www.w3.org/2000/svg" p-id="37761" data-spm-anchor-id="a313x.7781069.0.i45" width="42"
             height="50">
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
        <span style="line-height: 50px">点点滴滴</span>
      </div>
    </div>
    <div>
      <a-row v-for="(item,i) in  recordList" style="width: 100%;margin: auto;color: #fefefe">
        <a-col :span="3">
        </a-col>
        <a-col :span="8">
          <div v-if="i % 2 ==1" class="padding-10 mt-2">
            <div class="card">
              <h2>{{ item.title }}</h2>
              <hr class="hr-twill-colorful"/>
              <p>{{ item.content }}</p>
            </div>
            <div style="height: 20px;overflow: hidden">
              <svg t="1668670124651" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="7732" data-spm-anchor-id="a313x.7781069.0.i3" width="20"
                   height="20" style="float: left">
                <path d="M480 896h64v128h-64zM0 480h128v64H0zM768 544h-288V256h64v224h224v64z" fill="#f4ea2a"
                      p-id="7733" data-spm-anchor-id="a313x.7781069.0.i4" class="selected"></path>
                <path d="M512 1024v-64A448 448 0 1 0 64 512H0A512 512 0 0 1 512 0a512 512 0 0 1 0 1024z" fill="#fefefe"
                      p-id="7734" data-spm-anchor-id="a313x.7781069.0.i5" class=""></path>
                <path
                    d="M0 546.24V512h64v29.76zM411.2 1014.08a544 544 0 0 1-64-17.28l20.48-60.48a475.2 475.2 0 0 0 56.64 15.04z m-126.08-42.88A523.84 523.84 0 0 1 227.2 937.6l35.52-53.12a472.32 472.32 0 0 0 50.56 29.44zM174.4 896A472.64 472.64 0 0 1 128 849.6l48-42.24a506.24 506.24 0 0 0 41.28 41.6zM86.4 796.8a523.84 523.84 0 0 1-33.6-57.92l57.28-28.48a416 416 0 0 0 29.44 50.56zM27.2 677.12a506.88 506.88 0 0 1-17.28-64l64-12.48a470.08 470.08 0 0 0 15.04 56.32zM512 1024h-33.92l3.84-64H512z"
                    fill="#f4ea2a" p-id="7735"></path>
              </svg>
              <div style="line-height: 20px;margin-left: 10px;float: left;font-size: 13px">{{ item.updateTime }}</div>
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
              <p>{{ item.content }}</p>
            </div>
            <div style="height: 20px;overflow: hidden">
              <svg t="1668670124651" class="icon" viewBox="0 0 1024 1024" version="1.1"
                   xmlns="http://www.w3.org/2000/svg" p-id="7732" data-spm-anchor-id="a313x.7781069.0.i3" width="20"
                   height="20" style="float: left">
                <path d="M480 896h64v128h-64zM0 480h128v64H0zM768 544h-288V256h64v224h224v64z" fill="#f4ea2a"
                      p-id="7733" data-spm-anchor-id="a313x.7781069.0.i4" class="selected"></path>
                <path d="M512 1024v-64A448 448 0 1 0 64 512H0A512 512 0 0 1 512 0a512 512 0 0 1 0 1024z" fill="#fefefe"
                      p-id="7734" data-spm-anchor-id="a313x.7781069.0.i5" class=""></path>
                <path
                    d="M0 546.24V512h64v29.76zM411.2 1014.08a544 544 0 0 1-64-17.28l20.48-60.48a475.2 475.2 0 0 0 56.64 15.04z m-126.08-42.88A523.84 523.84 0 0 1 227.2 937.6l35.52-53.12a472.32 472.32 0 0 0 50.56 29.44zM174.4 896A472.64 472.64 0 0 1 128 849.6l48-42.24a506.24 506.24 0 0 0 41.28 41.6zM86.4 796.8a523.84 523.84 0 0 1-33.6-57.92l57.28-28.48a416 416 0 0 0 29.44 50.56zM27.2 677.12a506.88 506.88 0 0 1-17.28-64l64-12.48a470.08 470.08 0 0 0 15.04 56.32zM512 1024h-33.92l3.84-64H512z"
                    fill="#f4ea2a" p-id="7735"></path>
              </svg>
              <div style="line-height: 20px;margin-left: 10px;float: left;font-size: 13px">{{ item.updateTime }}</div>
            </div>
          </div>
        </a-col>
        <a-col :span="3">
        </a-col>

      </a-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "record",
  setup() {
    return {
      recordList: [
        {title: "跟宝一起去看博物馆", content: "跟宝一起去看博物馆", createTime: '2022年11月22日', updateTime: '2022年11月22日'},
        {title: "跟宝一起去游乐场", content: "一起玩大摆锤", createTime: '2022年11月22日', updateTime: '2022年11月22日'},
        {title: "一起去骑天府绿道", content: "一起挥洒汗水，累并伴随着快乐！", createTime: '2022年11月22日', updateTime: '2022年11月22日'},
        {title: "丫头18岁生日", content: "宝，生日快乐！", createTime: '2022年11月22日', updateTime: '2022年11月22日'},
        {title: "标题", content: "内容.....", createTime: '2022年11月22日', updateTime: '2022年11月22日'},
      ]
    }
  },
}
</script>

<style>
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
</style>