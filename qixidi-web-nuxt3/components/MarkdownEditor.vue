<template>
  <ClientOnly>
    <MdEditor
      v-model="content"
      :language="language"
      :preview="preview"
      :toolbars="toolbars"
      @on-save="handleSave"
    />
    <template #fallback>
      <n-spin size="large" />
    </template>
  </ClientOnly>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import MdEditor from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'

interface Props {
  modelValue?: string
  language?: string
  preview?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  language: 'zh-CN',
  preview: true
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'save': [value: string]
}>()

const content = ref(props.modelValue)

watch(() => props.modelValue, (val) => {
  content.value = val
})

watch(content, (val) => {
  emit('update:modelValue', val)
})

const toolbars = [
  'bold', 'underline', 'italic', '-',
  'title', 'strikeThrough', 'quote',
  'unorderedList', 'orderedList', 'task', '-',
  'codeRow', 'code', 'link', 'image', 'table', '-',
  'revoke', 'next', 'save', '=',
  'pageFullscreen', 'fullscreen', 'preview', 'catalog'
]

const handleSave = (value: string) => {
  emit('save', value)
}
</script>
