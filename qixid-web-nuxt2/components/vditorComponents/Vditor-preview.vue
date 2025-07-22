<template>
  <div>
    <div :id="id"></div>
    <!-- 图片放大弹窗 -->
    <div v-if="showImgModal" class="img-modal" @click="closeImgModal">
      <img :src="currentImgUrl" class="modal-img" @click.stop>
      <button class="modal-close" @click="closeImgModal">×</button>
    </div>
  </div>
</template>

<script>
//官网：https://gitee.com/vanessali/vditor#-%E7%AE%80%E4%BB%8B

import Vditor from 'static/vditor/dist/index.min.js'

export default {
  name: "VditorPreview",
  props: {
    // 接收父组件传递的 Markdown 内容
    content: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      default: 'contentId'
    }
  },
  data() {
    return {
      showImgModal: false,  // 控制弹窗显示
      currentImgUrl: ''     // 当前放大的图片地址
    }
  },
  methods: {
    initVditor() {
      this.$nextTick(() => {
        let tocArray = [];//目录
        let content = this.content;
        let id = this.id;
        const that = this;  // 保存当前实例引用
        Vditor.preview(document.getElementById(id), content, {
          speech: {
            enable: false,
          },
          cache: {
            enable: false
          },
          width: '100%',
          hljs: {
            style: 'atom-one-dark',//主题：androidstudio,atom-one-dark
            lineNumber: true,// 显示行号
            enable: true,// 启用代码高亮
          },
          anchor: 1,
          after() {
            // 让所有超链接在新标签页打开
            document.querySelectorAll('.vditor-reset a').forEach(link => {
              link.setAttribute('target', '_blank');
              link.setAttribute('rel', 'noopener noreferrer');
            });
            // 处理图片点击放大
            document.querySelectorAll('.vditor-reset img').forEach(img => {
              // 给图片添加可点击样式
              img.style.cursor = 'zoom-in';
              img.style.maxWidth = '100%';  // 确保图片不溢出容器

              // 绑定点击事件
              img.addEventListener('click', function () {
                that.currentImgUrl = this.src;  // 获取原图地址
                that.showImgModal = true;       // 显示弹窗
              });
            });


            //生成目录
            let tocTags = ["H1", "H2", "H3", "H4"];//筛选目录
            let element = document.getElementById(id);
            let childNodes = element.children;
            for (let i = 0; i < childNodes.length; i++) {
              let node = childNodes[i];
              // 判断节点是否为 H 标签
              if (tocTags.includes(node.tagName)) {
                let id = node.getAttribute("id");
                tocArray.push({
                  id: id,
                  text: node.textContent.trim(),
                  level: Number.parseInt(node.tagName.substring(1)),
                  pos: node.offsetTop,
                })
              }
            }
          },
        });
        this.$emit('update:outline', tocArray);
      });
    },
    closeImgModal() {
      this.showImgModal = false;
      this.currentImgUrl = '';
    }
  },
  mounted() {
    this.initVditor();
  }
}
</script>

<style>
@import url("../css/vditor-components.css");
@import url("../css/vditor-hljs.css");

/* 图片放大弹窗样式 */
.img-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
  padding: 20px;
  box-sizing: border-box;
}

.modal-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.modal-close {
  position: absolute;
  top: 20px;
  right: 30px;
  color: #ffffff;
  font-size: 30px;
  border: none;
  cursor: pointer;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
}

.modal-close:hover {
  background: rgba(255, 255, 255, 0.1);
}
</style>
