<template>
  <div class="lazy-image-container" :class="[{ 'loading': loading, 'fill-container': fill, 'center-image': center }, containerClass]" :style="containerStyle">
    <img
      v-show="loaded"
      :src="currentSrc"
      :alt="alt"
      :class="imageClass"
      :style="{ objectFit: fit }"
      @load="onLoad"
      @error="onError"
    />

    <!-- 加载状态 -->
    <div v-show="loading" class="lazy-loading-placeholder">
      <div class="lazy-loading-spinner"></div>
      <span class="lazy-loading-text">{{ loadingText }}</span>
    </div>

    <!-- 错误状态 -->
    <div v-show="error" class="lazy-error-placeholder" @click="retry">
      <div class="lazy-error-icon">⚠</div>
      <span class="lazy-error-text">加载失败，点击重试</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LazyImage',
  props: {
    src: {
      type: String,
      required: true
    },
    alt: {
      type: String,
      default: ''
    },
    imageClass: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    loadingText: {
      type: String,
      default: '正在加载...'
    },
    // 是否填充满父容器
    fill: {
      type: Boolean,
      default: false
    },
    // 自定义容器样式类
    containerClass: {
      type: String,
      default: ''
    },
    // 图片填充方式，类似 element 的 fit 属性
    fit: {
      type: String,
      default: 'cover',
      validator: value => ['fill', 'contain', 'cover', 'none', 'scale-down'].includes(value)
    },
    // 组件宽度
    width: {
      type: [String, Number],
      default: ''
    },
    // 组件高度
    height: {
      type: [String, Number],
      default: ''
    },
    // 图片是否在容器中居中显示
    center: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      loading: true,
      loaded: false,
      error: false,
      currentSrc: '',
      observer: null
    }
  },
  computed: {
    // 容器样式
    containerStyle() {
      const style = {}
      if (this.width) {
        style.width = typeof this.width === 'number' ? `${this.width}px` : this.width
      }
      if (this.height) {
        style.height = typeof this.height === 'number' ? `${this.height}px` : this.height
        // 当传入高度时，移除最小高度限制
        style.minHeight = 'unset'
      }
      return style
    }
  },
  mounted() {
    this.initIntersectionObserver();
  },
  beforeDestroy() {
    if (this.observer) {
      this.observer.disconnect();
    }
  },
  methods: {
    // 初始化 Intersection Observer
    initIntersectionObserver() {
      if ('IntersectionObserver' in window) {
        this.observer = new IntersectionObserver((entries) => {
          entries.forEach(entry => {
            if (entry.isIntersecting) {
              this.loadImage();
              this.observer.unobserve(entry.target);
            }
          });
        }, {
          threshold: 0.1, // 当10%的元素可见时触发
          rootMargin: '50px' // 提前50px开始加载
        });

        this.observer.observe(this.$el);
      } else {
        // 兜底：不支持 Intersection Observer 时立即加载
        this.loadImage();
      }
    },

    // 加载图片
    loadImage() {
      if (this.currentSrc) return; // 防止重复加载

      this.loading = true;
      this.error = false;
      this.currentSrc = this.src;
    },

    // 图片加载成功
    onLoad() {
      this.loading = false;
      this.loaded = true;
      this.error = false;
      this.$emit('load');
    },

    // 图片加载失败
    onError() {
      this.loading = false;
      this.loaded = false;
      this.error = true;
      this.$emit('error');
    },

    // 重试加载
    retry() {
      this.currentSrc = '';
      this.loadImage();
    }
  }
}
</script>

<style scoped>
.lazy-image-container {
  position: relative;
  display: block;
  width: 100%;
  min-height: 120px;
  background: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

/* 填充父容器模式 */
.lazy-image-container.fill-container {
  height: 100%;
  min-height: unset;
  /* 宽度继承父容器，不强制设置100% */
}

.lazy-image-container img {
  transition: opacity 0.3s ease;
}

/* 普通模式下的图片样式 */
.lazy-image-container:not(.fill-container) img {
  width: 100%;
  height: auto;
  max-width: 100%;
}

/* 填充模式下的图片样式 */
.lazy-image-container.fill-container img {
  width: 100%;
  height: 100%;
}

/* 当传入了宽高时，图片填充整个容器 */
.lazy-image-container[style*="width"][style*="height"] img {
  width: 100%;
  height: 100%;
}

/* 图片居中模式 */
.lazy-image-container.center-image {
  display: flex;
  align-items: center;
  justify-content: center;
}

.lazy-image-container.center-image img {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
}

/* 加载状态样式 */
.lazy-loading-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
}

.lazy-loading-spinner {
  width: 24px;
  height: 24px;
  border: 2px solid #e1e6eb;
  border-top: 2px solid #409eff;
  border-radius: 50%;
  animation: lazy-spin 1s linear infinite;
  margin-bottom: 8px;
}

.lazy-loading-text {
  font-size: 12px;
  color: #909399;
}

/* 错误状态样式 */
.lazy-error-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fef0f0;
  cursor: pointer;
  transition: background 0.3s ease;
}

.lazy-error-placeholder:hover {
  background: #fde2e2;
}

.lazy-error-icon {
  font-size: 24px;
  color: #f56c6c;
  margin-bottom: 8px;
}

.lazy-error-text {
  font-size: 12px;
  color: #f56c6c;
}

/* 动画 */
@keyframes lazy-spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 响应式适配 */
@media (max-width: 768px) {
  .lazy-image-container {
    min-height: 100px;
  }

  .lazy-loading-spinner {
    width: 20px;
    height: 20px;
  }

  .lazy-loading-text,
  .lazy-error-text {
    font-size: 11px;
  }
}

/* 文章封面图片样式适配 */
.lazy-image-container :deep(.article-cover-img) {
  border-radius: 4px;
}

.lazy-image-container :deep(.search-cover-img) {
  border-radius: 4px;
}
</style>
