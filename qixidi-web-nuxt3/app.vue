<template>
  <NaiveConfig :theme="naiveTheme" :theme-config="themeOverrides">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <NuxtLayout>
            <NuxtPage />
          </NuxtLayout>
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>

    <!-- 全局登录对话框 -->
    <AuthDialog />
  </NaiveConfig>
</template>

<script setup lang="ts">
import { darkTheme } from 'naive-ui'

const colorMode = useColorMode()

const naiveTheme = computed(() =>
  colorMode.value === 'dark' ? darkTheme : null
)

const themeOverrides = computed(() => ({
  common: {
    primaryColor: colorMode.value === 'dark' ? '#5D8AA8' : '#3D5A80',
    primaryColorHover: colorMode.value === 'dark' ? '#7BA3C1' : '#4A6B94',
    primaryColorPressed: colorMode.value === 'dark' ? '#4A6B94' : '#2C4A66',
    primaryColorSuppl: colorMode.value === 'dark' ? '#6B95B5' : '#4A6B94',
    borderRadius: '8px',
    borderRadiusSmall: '4px'
  },
  Button: {
    borderRadiusMedium: '8px',
    borderRadiusSmall: '6px',
    borderRadiusLarge: '10px'
  },
  Input: {
    borderRadius: '8px'
  },
  Card: {
    borderRadius: '12px'
  }
}))
</script>
