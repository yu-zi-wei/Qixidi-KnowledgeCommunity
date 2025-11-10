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
      currentImgUrl: '',    // 当前放大的图片地址
      tocArray: [],         // 目录数组
      observer: null,       // MutationObserver实例
      loadedImages: 0,      // 已加载的图片数量
      totalImages: 0,       // 总图片数量
      scrollTimeout: null,  // 滚动防抖定时器
      resizeTimeout: null,  // 窗口大小变化防抖定时器
      tocTimeout: null      // 目录生成防抖定时器
    }
  },
  methods: {
    // 生成目录（带防抖）
    generateToc() {
      if (this.tocTimeout) {
        clearTimeout(this.tocTimeout);
      }

      this.tocTimeout = setTimeout(() => {
        const tocTags = ["H1", "H2", "H3", "H4"];
        const element = document.getElementById(this.id);
        if (!element) return;

        // 获取固定导航栏的高度（如果有的话）
        const navHeight = document.querySelector('.navigation-bar') ?
          document.querySelector('.navigation-bar').offsetHeight : 0;

        const childNodes = element.children;
        const newTocArray = [];

        for (let i = 0; i < childNodes.length; i++) {
          const node = childNodes[i];
          if (tocTags.includes(node.tagName)) {
            const id = node.getAttribute("id");
            // 计算标题的实际位置，考虑页面滚动和导航栏高度
            const pos = node.getBoundingClientRect().top + window.pageYOffset - navHeight - 20; // 额外偏移20px作为缓冲
            newTocArray.push({
              id: id,
              text: node.textContent.trim(),
              level: Number.parseInt(node.tagName.substring(1)),
              pos: pos,
            });
          }
        }
        this.tocArray = newTocArray;
        this.$emit('update:outline', newTocArray);
      }, 150); // 150ms 的防抖延迟
    },

    // 处理滚动事件（带防抖）
    handleScroll() {
      if (this.scrollTimeout) {
        clearTimeout(this.scrollTimeout);
      }

      this.scrollTimeout = setTimeout(() => {
        this.generateToc();
      }, 150);
    },

    // 处理窗口大小变化（带防抖）
    handleResize() {
      if (this.resizeTimeout) {
        clearTimeout(this.resizeTimeout);
      }

      this.resizeTimeout = setTimeout(() => {
        this.generateToc();
      }, 150);
    },


    // 监听DOM变化
    setupMutationObserver() {
      if (this.observer) {
        this.observer.disconnect();
      }

      const element = document.getElementById(this.id);
      if (!element) return;

      this.observer = new MutationObserver(() => {
        this.generateToc();
      });

      this.observer.observe(element, {
        childList: true,
        subtree: true,
        attributes: true,
        characterData: true
      });
    },

    // 处理图片加载
    handleImageLoad() {
      this.loadedImages++;
      if (this.loadedImages === this.totalImages) {
        // 所有图片加载完成后，重新生成目录
        setTimeout(() => {
          this.generateToc();
        }, 100);
      }
    },

    initVditor() {
      this.$nextTick(() => {
        const content = this.content;
        const id = this.id;
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
            // 处理图片加载动画和点击放大
            document.querySelectorAll('.vditor-reset img').forEach(img => {
              // 创建图片容器
              const container = document.createElement('div');
              container.className = 'img-container';
              img.parentNode.insertBefore(container, img);
              container.appendChild(img);

              // 创建加载蒙层
              const overlay = document.createElement('div');
              overlay.className = 'loading-overlay';

              // 创建加载状态容器
              const loadingStatus = document.createElement('div');
              loadingStatus.className = 'loading-status';

              // 创建加载图标
              const loadingIcon = document.createElement('div');
              loadingIcon.className = 'loading-icon';
              loadingStatus.appendChild(loadingIcon);

              // 创建加载文本
              const loadingText = document.createElement('div');
              loadingText.className = 'loading-text';
              loadingText.textContent = '正在加载图片...';
              loadingStatus.appendChild(loadingText);

              // 将加载状态容器添加到蒙层
              overlay.appendChild(loadingStatus);

              container.appendChild(overlay);

              // 保存原始图片地址
              const originalSrc = img.src;

              // 使用 XMLHttpRequest 监听真实下载进度
              const xhr = new XMLHttpRequest();
              let overlayHidden = false;

              const hideOverlayOnce = () => {
                if (!overlayHidden) {
                  overlayHidden = true;
                  loadingText.textContent = '正在渲染...';

                  setTimeout(() => {
                    img.src = originalSrc;
                    img.classList.add('img-loaded');
                    overlay.classList.add('hidden');
                  }, 100);
                }
              };

              // 监听下载进度
              xhr.addEventListener('progress', function (e) {
                if (e.lengthComputable) {
                  const percentComplete = (e.loaded / e.total) * 100;
                  loadingText.textContent = `正在加载 ${Math.round(percentComplete)}%`;

                  // 当下载进度达到 20% 时隐藏蒙层
                  if (percentComplete >= 20) {
                    hideOverlayOnce();
                  }
                } else {
                  // 无法获取总大小时，检查已下载的字节数
                  if (e.loaded > 8192) { // 8KB 数据后隐藏蒙层
                    hideOverlayOnce();
                  }
                }
              });

              // 处理加载成功
              xhr.addEventListener('load', function () {
                if (xhr.status === 200) {
                  // 确保蒙层已隐藏
                  hideOverlayOnce();
                } else {
                  // 处理HTTP错误
                  overlay.classList.add('error');
                  loadingText.textContent = '加载失败，点击重试';
                  loadingIcon.style.animation = 'none';
                }
              });

              // 处理网络错误
              xhr.addEventListener('error', function () {
                overlay.classList.add('error');
                loadingText.textContent = '网络错误，点击重试';
                loadingIcon.style.animation = 'none';
              });

              // 重试函数
              const startLoading = () => {
                overlayHidden = false;
                overlay.classList.remove('error');
                loadingStatus.title = '';
                loadingText.textContent = '正在加载...';
                loadingIcon.style.animation = 'spin 1s linear infinite';

                xhr.open('GET', originalSrc, true);
                xhr.responseType = 'blob';
                xhr.send();
              };

              // 点击重试
              loadingStatus.onclick = function () {
                if (overlay.classList.contains('error')) {
                  startLoading();
                }
              };

              // 开始首次加载
              startLoading();
              // 给图片添加可点击样式
              img.style.cursor = 'zoom-in';
              // 绑定点击事件
              img.addEventListener('click', function () {
                that.currentImgUrl = originalSrc;  // 使用原始图片地址
                that.showImgModal = true;       // 显示弹窗
              });
            });

            // 统计图片总数并添加加载事件
            const images = document.querySelectorAll('.vditor-reset img');
            that.totalImages = images.length;
            that.loadedImages = 0;
            images.forEach(img => {
              if (img.complete) {
                that.handleImageLoad();
              } else {
                img.addEventListener('load', () => that.handleImageLoad());
              }
            });

            // 初始生成目录
            that.generateToc();
            // 设置DOM监听
            that.setupMutationObserver();
          },
        });
      });
    },
    closeImgModal() {
      this.showImgModal = false;
      this.currentImgUrl = '';
    }
  },
  mounted() {
    this.initVditor();
    // 监听窗口大小变化和滚动事件
    window.addEventListener('resize', this.handleResize);
    window.addEventListener('scroll', this.handleScroll);
  },

  beforeDestroy() {
    // 清理所有监听器和定时器
    if (this.observer) {
      this.observer.disconnect();
    }
    window.removeEventListener('resize', this.handleResize);
    window.removeEventListener('scroll', this.handleScroll);

    if (this.scrollTimeout) {
      clearTimeout(this.scrollTimeout);
    }
    if (this.resizeTimeout) {
      clearTimeout(this.resizeTimeout);
    }
    if (this.tocTimeout) {
      clearTimeout(this.tocTimeout);
    }
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

/* 图片容器样式 */
.vditor-reset .img-container {
  position: relative;
  background: #f8f9fa;
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  margin: 15px auto;
  max-width: 80%;
}

/* 图片加载动画样式 */
.vditor-reset .img-container img {
  opacity: 0;
  transition: all 0.3s ease-in-out;
  max-width: 100%;
  display: block;
}

.vditor-reset .img-container img.img-loaded {
  opacity: 1;
  transform: scale(1);
}

/* 加载蒙层样式 */
.vditor-reset .img-container .loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #ffffff;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease-in-out;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.vditor-reset .img-container .loading-overlay.hidden {
  opacity: 0;
  pointer-events: none;
  transform: scale(0.95);
}

/* 加载图标样式 */
.vditor-reset .img-container .loading-overlay .loading-status {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 200px;
  z-index: 1;
}

.vditor-reset .img-container .loading-overlay .loading-icon {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 加载文本样式 */
.vditor-reset .img-container .loading-overlay .loading-text {
  margin-top: 12px;
  font-size: 13px;
  color: #666;
  font-weight: 500;
  text-shadow: 0 1px 0 #fff;
  transition: all 0.3s ease;
  min-width: 120px;
  text-align: center;
}

/* 加载失败状态样式 */
.vditor-reset .img-container .loading-overlay.error {
  background: rgba(255, 242, 240, 0.95);
}

.vditor-reset .img-container .loading-overlay.error .loading-status {
  cursor: pointer;
}

.vditor-reset .img-container .loading-overlay.error .loading-icon {
  animation: none;
  transform: scale(0.9);
  opacity: 0.8;
  transition: transform 0.3s ease;
}

.vditor-reset .img-container .loading-overlay.error .loading-status:hover .loading-icon {
  transform: scale(1);
}

.vditor-reset .img-container .loading-overlay.error .loading-text {
  color: #ff4d4f;
  transition: color 0.3s ease;
}

.vditor-reset .img-container .loading-overlay.error .loading-status:hover .loading-text {
  color: #ff7875;
}

.vditor-reset .img-container .loading-overlay.error .loading-icon {
  opacity: 0.8;
  transform: scale(0.9);
  transition: all 0.3s ease;
}

.vditor-reset .img-container .loading-overlay.error .loading-status:hover .loading-icon {
  opacity: 1;
  transform: scale(1);
}
</style>
