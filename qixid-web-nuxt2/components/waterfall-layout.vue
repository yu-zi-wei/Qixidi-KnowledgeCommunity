<template>
  <div class="waterfall-container" ref="container" :class="{ 'layout-calculating': !isInitialLayoutDone }">
    <div 
      v-for="(item, index) in items" 
      :key="item.id || index"
      class="waterfall-item"
      :ref="`item${index}`"
    >
      <slot :item="item" :index="index"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "WaterfallLayout",
  props: {
    items: {
      type: Array,
      default: () => []
    },
    columns: {
      type: Number,
      default: 4
    },
    gap: {
      type: Number,
      default: 20
    }
  },
  data() {
    return {
      columnHeights: [],
      isLayoutReady: false,
      resizeTimer: null,
      actualColumns: 4, // 根据响应式逻辑的实际列数
      columnWidth: 0,
      containerWidth: 0,
      isInitialLayoutDone: false // 标记初始布局是否完成
    }
  },
  watch: {
    items: {
      handler(newItems, oldItems) {
        // 只在初始加载或数据重置/过滤时重新布局
        if (!oldItems || oldItems.length === 0 || newItems.length < oldItems.length) {
          this.$nextTick(() => {
            this.layout();
          });
        }
      },
      deep: true
    }
  },
  mounted() {
    this.init();
    window.addEventListener('resize', this.handleResize);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize);
  },
  methods: {
    init() {
      this.$nextTick(() => {
        this.calculateDimensions();
        this.layout();
      });
    },
    
    calculateDimensions() {
      if (!this.$refs.container) return;
      
      this.containerWidth = this.$refs.container.clientWidth;
      
      // 响应式列数
      if (this.containerWidth < 480) {
        this.actualColumns = 1;
      } else if (this.containerWidth < 768) {
        this.actualColumns = 2;
      } else if (this.containerWidth < 1200) {
        this.actualColumns = 3;
      } else {
        this.actualColumns = this.columns;
      }
      
      this.columnWidth = (this.containerWidth - (this.actualColumns - 1) * this.gap) / this.actualColumns;
      
      console.log(`容器宽度: ${this.containerWidth}, 列数: ${this.actualColumns}, 列宽: ${this.columnWidth}`);
    },
    
    layout() {
      if (!this.containerWidth || !this.items.length) return;
      
      // 初始化列高度数组
      this.columnHeights = new Array(this.actualColumns).fill(0);
      
      // 等待一点时间确保DOM渲染完成
      setTimeout(() => {
        this.items.forEach((item, index) => {
          this.positionItem(index);
        });
        
        // 设置容器高度
        const maxHeight = Math.max(...this.columnHeights);
        this.$refs.container.style.height = `${maxHeight}px`;
        
        // 标记布局完成，显示容器
        this.isInitialLayoutDone = true;
        
        // 触发动画
        this.triggerAnimations();
        
      }, 100);
    },
    
    positionItem(index) {
      const element = this.$refs[`item${index}`]?.[0];
      if (!element) return;
      
      // 一次性设置所有样式，减少重排
      const shortestColumn = this.columnHeights.indexOf(Math.min(...this.columnHeights));
      const left = shortestColumn * (this.columnWidth + this.gap);
      const top = this.columnHeights[shortestColumn];
      
      // 批量应用样式，新元素有更平稳的初始状态
      Object.assign(element.style, {
        position: 'absolute',
        width: `${this.columnWidth}px`,
        left: `${left}px`,
        top: `${top}px`,
        opacity: '0',
        transform: 'translateY(20px) translateZ(0)', // 减少位移，降低视觉冲击
        willChange: 'opacity, transform',
        backfaceVisibility: 'hidden',
        visibility: 'visible' // 确保元素可见
      });
      
      // 等待一帧确保内容完全渲染，然后获取高度
      element.offsetHeight; // 触发重排
      
      // 再次获取准确高度
      const height = element.offsetHeight;
      
      // 更新列高度
      this.columnHeights[shortestColumn] += height + this.gap;
      
      console.log(`Item ${index}: 列${shortestColumn}, left=${left}, top=${top}, height=${height}`);
    },
    
    triggerAnimations() {
      // 确保布局完成后再开始动画
      setTimeout(() => {
        let currentIndex = 0;
        const animate = () => {
          if (currentIndex < this.items.length) {
            const element = this.$refs[`item${currentIndex}`]?.[0];
            if (element) {
              // 确保初始状态一致
              element.style.transition = 'none';
              element.style.opacity = '0';
              element.style.transform = 'translateY(20px) translateZ(0)';
              
              // 强制重排
              element.offsetHeight;
              
              // 下一帧开始动画
              requestAnimationFrame(() => {
                element.style.transition = 'opacity 0.4s ease-out, transform 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
                element.style.opacity = '1';
                element.style.transform = 'translateY(0) translateZ(0)';
              });
            }
            currentIndex++;
            
            // 保持一致的延迟
            setTimeout(() => animate(), 80);
          }
        };
        
        animate();
      }, 50); // 给布局一些时间
    },
    
    handleResize() {
      clearTimeout(this.resizeTimer);
      this.resizeTimer = setTimeout(() => {
        this.calculateDimensions();
        this.layout();
      }, 300);
    },
    
    // 公共方法：增量添加新项目
    addItems(newItems) {
      if (!newItems || newItems.length === 0) return;
      
      const startIndex = this.items.length;
      const isFirstLoad = startIndex === 0; // 判断是否为第一次加载
      
      // 如果是第一次加载，隐藏容器直到布局完成
      if (isFirstLoad) {
        this.isInitialLayoutDone = false;
      }
      
      // 添加到items数组（内部操作，不会触发watch）
      this.items.push(...newItems);
      
      // 等待DOM更新
      this.$nextTick(() => {
        // 第一次加载需要更多时间来初始化瀑布流
        const delay = isFirstLoad ? 150 : 50;
        
        setTimeout(() => {
          // 只布局新添加的元素
          newItems.forEach((item, i) => {
            const index = startIndex + i;
            this.positionItem(index);
          });
          
          // 更新容器高度
          const maxHeight = Math.max(...this.columnHeights);
          this.$refs.container.style.height = `${maxHeight}px`;
          
          // 确保容器可见（对于增量添加）
          if (!isFirstLoad) {
            this.isInitialLayoutDone = true;
          }
          
          // 只为新元素触发动画
          this.triggerNewItemAnimations(startIndex, newItems.length, isFirstLoad);
          
        }, delay);
      });
    },
    
    // 只为新添加的元素触发动画
    triggerNewItemAnimations(startIndex, count, isFirstLoad = false) {
      // 第一次加载需要更多时间确保稳定
      const animationDelay = isFirstLoad ? 200 : 100;
      
      setTimeout(() => {
        let currentIndex = 0;
        const animate = () => {
          if (currentIndex < count) {
            const index = startIndex + currentIndex;
            const element = this.$refs[`item${index}`]?.[0];
            if (element) {
              // 预设动画开始状态，避免闪烁
              element.style.transition = 'none';
              element.style.opacity = '0';
              element.style.transform = 'translateY(20px) translateZ(0)';
              
              // 强制重排，确保状态生效
              element.offsetHeight;
              
              // 使用双重RAF确保状态完全应用（特别是第一次加载）
              requestAnimationFrame(() => {
                requestAnimationFrame(() => {
                  element.style.transition = 'opacity 0.4s ease-out, transform 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
                  element.style.opacity = '1';
                  element.style.transform = 'translateY(0) translateZ(0)';
                });
              });
            }
            currentIndex++;
            
            // 第一次加载稍微慢一些，确保稳定
            const itemDelay = isFirstLoad ? 100 : 80;
            setTimeout(() => animate(), itemDelay);
          }
        };
        
        animate();
      }, animationDelay);
    }
  }
}
</script>

<style scoped>
.waterfall-container {
  position: relative;
  width: 100%;
  /* GPU 加速容器 */
  transform: translateZ(0);
  backface-visibility: hidden;
}

/* 布局计算中的状态 - 隐藏元素防止闪烁 */
.waterfall-container.layout-calculating {
  visibility: hidden;
}

/* 布局完成后平滑显示 */
.waterfall-container:not(.layout-calculating) {
  visibility: visible;
  transition: visibility 0s ease 0s;
}

.waterfall-item {
  transition: none;
  /* 为动画优化 */
  will-change: opacity, transform;
  backface-visibility: hidden;
  /* 确保硬件加速 */
  transform: translateZ(0);
}

/* 针对大量元素的性能优化 */
.waterfall-item * {
  /* 防止子元素触发不必要的重排 */
  box-sizing: border-box;
}

/* 避免动画期间的布局抖动 */
.waterfall-item img,
.waterfall-item video {
  display: block;
  max-width: 100%;
  height: auto;
}
</style>