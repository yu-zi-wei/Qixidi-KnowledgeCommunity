<template>
  <Transition name="fade-up">
    <button
      v-show="visible"
      class="scroll-to-top"
      @click="scrollToTop"
      title="回到顶部"
    >
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="18 15 12 9 6 15"></polyline>
      </svg>
    </button>
  </Transition>
</template>

<script setup lang="ts">
const visible = ref(false)
const SCROLL_THRESHOLD = 400

const checkScroll = () => {
  visible.value = window.scrollY > SCROLL_THRESHOLD
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  checkScroll()
  window.addEventListener('scroll', checkScroll, { passive: true })
  onUnmounted(() => window.removeEventListener('scroll', checkScroll))
})
</script>

<style scoped>
.scroll-to-top {
  position: fixed;
  right: 24px;
  bottom: 32px;
  z-index: 40;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: var(--radius-full);
  background: var(--color-surface);
  color: var(--color-ink-light);
  box-shadow: var(--shadow-md);
  cursor: pointer;
  transition: all 0.2s ease;
}

.scroll-to-top svg {
  width: 20px;
  height: 20px;
}

.scroll-to-top:hover {
  background: var(--color-primary);
  color: white;
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}

:root.dark .scroll-to-top {
  background: rgba(255, 255, 255, 0.08);
  color: var(--color-ink-light);
}

:root.dark .scroll-to-top:hover {
  background: var(--color-primary);
  color: white;
}

.fade-up-enter-active,
.fade-up-leave-active {
  transition: all 0.25s ease;
}

.fade-up-enter-from,
.fade-up-leave-to {
  opacity: 0;
  transform: translateY(12px);
}

@media (max-width: 768px) {
  .scroll-to-top {
    right: 16px;
    bottom: 24px;
    width: 36px;
    height: 36px;
  }

  .scroll-to-top svg {
    width: 18px;
    height: 18px;
  }
}
</style>
