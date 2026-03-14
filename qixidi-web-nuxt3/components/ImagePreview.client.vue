<template>
  <Teleport to="body">
    <Transition name="preview">
      <div v-if="visible" class="image-preview-overlay" @click.self="close">
        <button class="close-btn" @click="close">
          <X class="close-icon" />
        </button>

        <div class="image-container">
          <img :src="imageSrc" :alt="alt" class="preview-image" @click.self="close" />
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { X } from '@vicons/tabler'

interface Props {
  visible: boolean
  imageSrc: string
  alt?: string
}

const props = defineProps<Props>()

const emit = defineEmits<{
  close: []
}>()

const close = () => {
  emit('close')
}

// ESC 键关闭
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape') {
    close()
  }
}

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})

// 禁止背景滚动
watch(() => props.visible, (visible) => {
  if (visible) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
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
  background: rgba(0, 0, 0, 0.9);
  cursor: zoom-out;
}

.image-container {
  max-width: 90vw;
  max-height: 90vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.preview-image {
  max-width: 100%;
  max-height: 90vh;
  object-fit: contain;
  cursor: default;
  border-radius: var(--radius-md);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

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
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

.close-icon {
  width: 24px;
  height: 24px;
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
