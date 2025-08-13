// 列表项动画工具类
export class ListItemAnimator {
  constructor(component, options = {}) {
    this.component = component;
    this.options = {
      // 默认配置
      initialDelay: 80,        // 初始延迟
      itemDelay: 60,          // 项目间隔
      newItemDelay: 30,        // 新增项目延迟
      animationDuration: '0.6s', // 动画持续时间
      translateY: '30px',      // Y轴偏移距离
      transition: 'opacity 0.6s ease-out, transform 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
      ...options
    };
  }

  // 初始化所有项目为隐藏状态
  initializeItemsHidden(dataArray, refPrefix) {
    this.component.$nextTick(() => {
      dataArray.forEach((item, index) => {
        const element = this.component.$refs[`${refPrefix}${index}`]?.[0];
        if (element) {
          element.style.opacity = '0';
          element.style.transform = `translateY(${this.options.translateY})`;
          element.style.transition = 'none';
        }
      });
    });
  }

  // 触发全部项目动画
  triggerAllItemsAnimation(dataArray, refPrefix) {
    // 先初始化所有item为隐藏
    this.initializeItemsHidden(dataArray, refPrefix);

    this.component.$nextTick(() => {
      setTimeout(() => {
        let currentIndex = 0;
        const animate = () => {
          if (currentIndex < dataArray.length) {
            const element = this.component.$refs[`${refPrefix}${currentIndex}`]?.[0];
            if (element) {
              // 应用动画
              requestAnimationFrame(() => {
                element.style.transition = this.options.transition;
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
              });
            }
            currentIndex++;
            setTimeout(() => animate(), this.options.itemDelay);
          }
        };
        animate();
      }, this.options.initialDelay);
    });
  }

  // 为新增的项目触发动画
  triggerNewItemsAnimation(startIndex, count, refPrefix) {
    this.component.$nextTick(() => {
      // 先设置新增项目为隐藏状态
      setTimeout(() => {
        for (let i = 0; i < count; i++) {
          const index = startIndex + i;
          const element = this.component.$refs[`${refPrefix}${index}`]?.[0];
          if (element) {
            element.style.opacity = '0';
            element.style.transform = `translateY(${this.options.translateY})`;
            element.style.transition = 'none';
          }
        }

        // 然后逐个显示
        let currentIndex = 0;
        const animate = () => {
          if (currentIndex < count) {
            const index = startIndex + currentIndex;
            const element = this.component.$refs[`${refPrefix}${index}`]?.[0];
            if (element) {
              // 应用动画
              requestAnimationFrame(() => {
                element.style.transition = this.options.transition;
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
              });
            }
            currentIndex++;
            setTimeout(() => animate(), this.options.itemDelay);
          }
        };
        setTimeout(() => animate(), this.options.newItemDelay);
      }, this.options.newItemDelay);
    });
  }
}

// 为不同组件预设配置
export const animationConfigs = {
  // 主文章列表配置
  mainArticle: {
    initialDelay: 80,    // 减少初始延迟：150ms → 80ms
    itemDelay: 60,       // 减少项目间隔：100ms → 60ms
    newItemDelay: 30,     // 减少新项目延迟：50ms → 30ms
  },

  // 分组文章列表配置
  groupArticle: {
    initialDelay: 80,
    itemDelay: 60,
    newItemDelay: 30,
  },

  // 搜索文章列表配置
  searchArticle: {
    initialDelay: 80,
    itemDelay: 60,
    newItemDelay: 30,
  },

  // 通用列表配置
  commonList: {
    initialDelay: 80,    // 减少初始延迟：150ms → 80ms
    itemDelay: 60,       // 减少项目间隔：100ms → 60ms
    newItemDelay: 30,     // 减少新项目延迟：50ms → 30ms
  },
};

// 创建动画器的便捷方法
export function createAnimator(component, configName) {
  const config = animationConfigs[configName] || {};
  return new ListItemAnimator(component, config);
}

// 嵌套数据动画工具函数 - 针对复杂嵌套结构
export function triggerNestedAnimation(component, nestedData, refPattern, options = {}) {
  const config = {
    itemDelay: 60,        // 减少项目间隔：100ms → 60ms
    initialDelay: 80,     // 减少初始延迟：150ms → 80ms
    transition: 'opacity 0.5s ease-out, transform 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94)', // 缩短动画时长：0.6s → 0.5s
    translateY: '30px',
    ...options
  };

  // 收集所有需要动画的元素
  const allElements = [];
  for (let groupIndex = 0; groupIndex < nestedData.length; groupIndex++) {
    const group = nestedData[groupIndex];
    const subArray = group.list || group.items || group.children || [];

    for (let subIndex = 0; subIndex < subArray.length; subIndex++) {
      const refKey = refPattern.replace('{groupIndex}', groupIndex).replace('{subIndex}', subIndex);
      const element = component.$refs[refKey]?.[0];
      if (element) {
        allElements.push(element);
      }
    }
  }

  // 初始化所有元素为隐藏状态
  allElements.forEach(element => {
    element.style.opacity = '0';
    element.style.transform = `translateY(${config.translateY})`;
    element.style.transition = 'none';
  });

  // 逐个显示动画
  let currentIndex = 0;
  const animate = () => {
    if (currentIndex < allElements.length) {
      const element = allElements[currentIndex];
      requestAnimationFrame(() => {
        element.style.transition = config.transition;
        element.style.opacity = '1';
        element.style.transform = 'translateY(0)';
      });
      currentIndex++;
      setTimeout(() => animate(), config.itemDelay);
    }
  };

  // 延迟开始动画
  setTimeout(() => animate(), config.initialDelay);
}

// 嵌套数据增量动画工具函数 - 针对新增的嵌套项目
export function triggerNestedIncrementalAnimation(component, nestedData, refPattern, newItems, options = {}) {
  const config = {
    itemDelay: 100,
    initialDelay: 50,
    transition: 'opacity 0.6s ease-out, transform 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94)',
    translateY: '30px',
    ...options
  };

  // 收集所有新增的元素
  const newElements = [];

  // 计算新增项目在整个列表中的位置
  let totalItemsBefore = 0;
  for (let groupIndex = 0; groupIndex < nestedData.length; groupIndex++) {
    const group = nestedData[groupIndex];
    const subArray = group.list || group.items || group.children || [];

    // 检查这个组是否是新增的
    const isNewGroup = newItems.some(newItem =>
      (newItem.recordTime || newItem.id || newItem.key) === (group.recordTime || group.id || group.key)
    );

    if (isNewGroup) {
      // 整个组是新的，所有子项都需要动画
      for (let subIndex = 0; subIndex < subArray.length; subIndex++) {
        const refKey = refPattern.replace('{groupIndex}', groupIndex).replace('{subIndex}', subIndex);
        const element = component.$refs[refKey]?.[0];
        if (element) {
          newElements.push(element);
        }
      }
    } else {
      // 检查是否有新增的子项（比较复杂，基于数组长度变化推断）
      // 这里假设新项目总是添加在子数组的末尾
      const estimatedOldLength = subArray.length - (newItems.find(item =>
        (item.recordTime || item.id || item.key) === (group.recordTime || group.id || group.key)
      )?.list?.length || 0);

      // 只对新增的子项添加动画
      for (let subIndex = Math.max(0, estimatedOldLength); subIndex < subArray.length; subIndex++) {
        const refKey = refPattern.replace('{groupIndex}', groupIndex).replace('{subIndex}', subIndex);
        const element = component.$refs[refKey]?.[0];
        if (element) {
          newElements.push(element);
        }
      }
    }
  }

  if (newElements.length === 0) return;

  // 初始化新元素为隐藏状态
  newElements.forEach(element => {
    element.style.opacity = '0';
    element.style.transform = `translateY(${config.translateY})`;
    element.style.transition = 'none';
  });

  // 逐个显示动画
  let currentIndex = 0;
  const animate = () => {
    if (currentIndex < newElements.length) {
      const element = newElements[currentIndex];
      requestAnimationFrame(() => {
        element.style.transition = config.transition;
        element.style.opacity = '1';
        element.style.transform = 'translateY(0)';
      });
      currentIndex++;
      setTimeout(() => animate(), config.itemDelay);
    }
  };

  // 延迟开始动画
  setTimeout(() => animate(), config.initialDelay);
}
