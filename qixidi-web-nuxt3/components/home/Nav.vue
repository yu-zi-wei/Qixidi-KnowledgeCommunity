<template>
  <div>
    <h1>你好~👋</h1>
    <n-button type="primary" dashed @click="switchTheme(null)">
      白天
    </n-button>
    <span class="ml-10 mr-10" :class="themeClass('ft-color')">不懂</span>
    <n-button type="info" dashed @click="switchTheme(darkTheme)">
      夜的黑
    </n-button>
  </div>
</template>

<script setup lang='ts'>
import {darkTheme} from 'naive-ui';
import type {GlobalTheme} from 'naive-ui';
import {themeClass} from "~/util/naiveUiUtil";

const switchTheme = inject('switchTheme') as (style: GlobalTheme | null) => void;

</script>

<style lang='scss' scoped></style>
