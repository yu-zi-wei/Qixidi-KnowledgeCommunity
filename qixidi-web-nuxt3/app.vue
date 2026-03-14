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
    primaryColor: colorMode.value === 'dark' ? '#40916c' : '#2d6a4f',
    primaryColorHover: colorMode.value === 'dark' ? '#52b788' : '#1b4332',
    primaryColorPressed: colorMode.value === 'dark' ? '#2d6a4f' : '#1b4332',
    primaryColorSuppl: '#40916c',
    borderRadius: '8px',
    borderRadiusSmall: '4px',
    fontFamily: "'Noto Sans SC', 'Source Han Sans SC', -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif",
    fontSize: '14.5px'
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
