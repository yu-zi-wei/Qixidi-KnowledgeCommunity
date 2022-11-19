<template>
  <div class="mt-20">
    <div style="text-align: center;margin-bottom: 20px">
      <button class="album-button"><span class="text">相册集</span>
        <span class="blob"></span>
        <span class="blob"></span>
        <span class="blob"></span>
        <span class="blob"></span>
      </button>
    </div>
    <div class="mb-20">
      <hr class="hr-wavy">
    </div>
    <div class="cont">
      <img v-for="(item,i) of imgList"
           :src="item.img"
           class="ims" :style="judge(i)">
    </div>
  </div>
</template>

<script>
import './css/album.css'

export default {
  name: "album",
  setup() {
    return {
      imgList: [
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
        {img: 'https://img1.baidu.com/it/u=31749929,1483746105&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', state: ''},
      ],
    }
  },
  methods: {
    carouselChart(item) {
      // background-image: url();
      return 'background-image: url(' + item.img + ')'
    },
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
    }
  },
  mounted() {
// 获取所有.item元素
    let items = document.querySelectorAll('.item');

// 设置选中态样式
    function setActive() {
      // 遍历所有.item元素，移出active样式
      items.forEach((item) => {
        item.classList.remove('active');
      })
      // 为当前选中项添加active样式
      this.classList.add('active');
    }

// 遍历所有.item元素，分别为其设置点击事件
    items.forEach((item) => {
      item.addEventListener('click', setActive);
    })
  }
}
</script>

<style>

</style>