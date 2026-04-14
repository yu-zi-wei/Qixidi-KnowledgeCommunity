<template>
  <Teleport to="body">
    <Transition name="preview">
      <div
        v-if="visible"
        class="image-preview-overlay"
        @click.self="close"
        @touchmove.prevent
      >
        <!-- 关闭按钮 -->
        <button class="close-btn" @click="close">
          <X class="close-icon" />
        </button>

        <!-- 图片容器 -->
        <div
          class="image-container"
          @wheel="handleWheel"
          @mousedown="startDrag"
          @dblclick="reset"
        >
          <img
            ref="imgRef"
            :src="imageSrc"
            :alt="alt"
            class="preview-image"
            :style="imageStyle"
            draggable="false"
          />
        </div>

        <!-- 控制按钮 -->
        <div class="control-btns">
          <button class="control-btn" @click="zoomOut" :disabled="scale <= 0.5">
            <Minus class="control-icon" />
          </button>
          <span class="scale-text">{{ Math.round(scale * 100) }}%</span>
          <button class="control-btn" @click="zoomIn" :disabled="scale >= 3">
            <Plus class="control-icon" />
          </button>
          <div class="control-divider" />
          <button class="control-btn" @click="rotateRight" title="旋转">
            <RotateClockwise class="control-icon" />
          </button>
        </div>

        <!-- 提示 -->
        <div class="hint">滚轮缩放 · 拖拽移动 · 双击重置</div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { X, Plus, Minus, RotateClockwise } from '@vicons/tabler'

interface Props {
  visible: boolean
  imageSrc: string
  alt?: string
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
}>()

const imgRef = ref<HTMLImageElement | null>(null)

// 缩放和平移状态
const scale = ref(1)
const translateX = ref(0)
const translateY = ref(0)
const rotation = ref(0)

// 拖拽状态
const isDragging = ref(false)
const dragStartX = ref(0)
const dragStartY = ref(0)
const dragStartTranslateX = ref(0)
const dragStartTranslateY = ref(0)

// 计算图片样式
const imageStyle = computed(() => ({
  transform: `translate(${translateX.value}px, ${translateY.value}px) scale(${scale.value}) rotate(${rotation.value}deg)`,
  cursor: scale.value > 1 ? (isDragging.value ? 'grabbing' : 'grab') : 'default'
}))

// 缩放
const zoomIn = () => {
  if (scale.value < 3) {
    scale.value = Math.min(3, scale.value + 0.25)
  }
}

const zoomOut = () => {
  if (scale.value > 0.5) {
    scale.value = Math.max(0.5, scale.value - 0.25)
    // 缩小后如果图片超出边界，重置位置
    if (scale.value <= 1) {
      translateX.value = 0
      translateY.value = 0
    }
  }
}

// 重置
const reset = () => {
  scale.value = 1
  translateX.value = 0
  translateY.value = 0
  rotation.value = 0
}

// 旋转
const rotateRight = () => {
  rotation.value += 90
}

// 滚轮缩放
const handleWheel = (e: WheelEvent) => {
  e.preventDefault()
  if (e.deltaY < 0) {
    zoomIn()
  } else {
    zoomOut()
  }
}

// 开始拖拽
const startDrag = (e: MouseEvent) => {
  if (scale.value <= 1) return

  isDragging.value = true
  dragStartX.value = e.clientX
  dragStartY.value = e.clientY
  dragStartTranslateX.value = translateX.value
  dragStartTranslateY.value = translateY.value

  document.addEventListener('mousemove', onDrag)
  document.addEventListener('mouseup', stopDrag)
}

// 拖拽中
const onDrag = (e: MouseEvent) => {
  if (!isDragging.value) return

  const deltaX = e.clientX - dragStartX.value
  const deltaY = e.clientY - dragStartY.value

  translateX.value = dragStartTranslateX.value + deltaX
  translateY.value = dragStartTranslateY.value + deltaY
}

// 停止拖拽
const stopDrag = () => {
  isDragging.value = false
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
}

// 关闭
const close = () => {
  reset()
  emit('close')
}

// ESC 键关闭
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    close()
  }
}

// 监听 visible 变化，重置状态
watch(() => props.visible, (val) => {
  if (val) {
    reset()
  }
})

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.image-preview-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.75);
  user-select: none;
}

/* 关闭按钮 */
.close-btn {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-fast);
  color: #fff;
  z-index: 20;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.close-icon {
  width: 24px;
  height: 24px;
}

/* 图片容器 */
.image-container {
  max-width: 90vw;
  max-height: calc(90vh - 100px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.preview-image {
  max-width: 90vw;
  max-height: calc(90vh - 100px);
  object-fit: contain;
  border-radius: var(--radius-md);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
  transition: transform 0.1s ease-out;
}

/* 控制按钮 */
.control-btns {
  position: absolute;
  bottom: 50px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.6);
  border-radius: var(--radius-full);
  backdrop-filter: blur(8px);
  z-index: 20;
}

.control-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: var(--radius-full);
  cursor: pointer;
  transition: all var(--transition-fast);
  color: #fff;
}

.control-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.2);
}

.control-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.control-divider {
  width: 1px;
  height: 20px;
  background: rgba(255, 255, 255, 0.2);
  margin: 0 4px;
}

.control-icon {
  width: 20px;
  height: 20px;
}

.scale-text {
  min-width: 50px;
  text-align: center;
  font-size: 13px;
  color: #fff;
  font-weight: 500;
}

/* 提示 */
.hint {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 12px;
  color: rgba(255, 255, 255, 0.4);
  white-space: nowrap;
  z-index: 20;
}

/* 过渡动画 */
.preview-enter-active,
.preview-leave-active {
  transition: opacity 0.3s ease;
}

.preview-enter-active .image-container,
.preview-leave-active .image-container {
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.preview-enter-from,
.preview-leave-to {
  opacity: 0;
}

.preview-enter-from .image-container,
.preview-leave-to .image-container {
  transform: scale(0.9);
  opacity: 0;
}
</style>
