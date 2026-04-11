<template>
  <div class="layout-blank">
    <slot />
  </div>
</template>

<style scoped>
.layout-blank {
  min-height: 100vh;
  background: var(--color-surface-warm);
}
</style>