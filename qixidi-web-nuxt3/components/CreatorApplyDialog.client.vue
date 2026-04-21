<template>
  <n-modal
    v-model:show="store.visible"
    preset="card"
    title="申请成为创作者"
    :style="{ maxWidth: '520px' }"
    :bordered="false"
    :segmented="{ content: true }"
    size="medium"
  >
    <div class="apply-info">
      <div class="info-section">
        <h4 class="info-title">创作者身份为什么需要申请？</h4>
        <p class="info-text">网站是面向所有用户的，为了保证内容的质量和减少测试内容的产生，导致其他正常用户的阅读体验。我们需要对创作者做一个简单的认证。</p>
      </div>
      <div class="info-section">
        <h4 class="info-title">如何申请？</h4>
        <p class="info-text">请说明你的创作方向（如生活分享、知识科普等），申请内容 ≥ 20 字。管理员会在 48 小时内审核反馈，审核通过后所发布的内容需积极健康、合规合法哦～</p>
      </div>
    </div>
    <n-input
      v-model:value="content"
      type="textarea"
      placeholder="请说明你的创作方向和申请理由..."
      :rows="4"
      :maxlength="500"
      show-count
    />
    <div v-if="content.trim().length > 0 && content.trim().length < 20" class="content-warn">
      还需输入 {{ 20 - content.trim().length }} 字
    </div>
    <template #action>
      <div class="apply-footer">
        <n-button @click="store.hide()">取消</n-button>
        <n-button type="primary" :loading="submitting" :disabled="content.trim().length < 20" @click="handleSubmit">
          提交申请
        </n-button>
      </div>
    </template>
  </n-modal>
</template>

<script setup lang="ts">
const store = useCreatorApplyStore()
const api = useApi()
const message = useMessage()

const content = ref('')
const submitting = ref(false)

const handleSubmit = async () => {
  if (content.value.trim().length < 20) return
  submitting.value = true
  try {
    await api.post('/frontDesk/user/creator/application', {
      applicationContent: content.value.trim()
    })
    message.success('申请已提交，请耐心等待审核')
    content.value = ''
    store.hide()
  } catch {
  } finally {
    submitting.value = false
  }
}

watch(() => store.visible, (val) => {
  if (!val) content.value = ''
})
</script>

<style scoped>
.apply-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
  padding: 16px;
  background: var(--color-surface-dim);
  border-radius: var(--radius-md);
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.info-title {
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink);
  margin: 0;
}

.info-text {
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  line-height: var(--leading-relaxed);
  margin: 0;
}

.content-warn {
  margin-top: 6px;
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
}

.apply-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
