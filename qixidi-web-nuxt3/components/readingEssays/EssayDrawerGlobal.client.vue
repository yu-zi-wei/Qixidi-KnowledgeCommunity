<template>
  <ReadingEssaysEssayDrawer
    v-model:show="visible"
    :edit-id="editId"
    @success="handleSuccess"
  />
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useEssayDrawerStore } from '~/stores/essayDrawer'

const store = useEssayDrawerStore()
const { visible, editId } = storeToRefs(store)

const handleSuccess = () => {
  store.close()
}
</script>
