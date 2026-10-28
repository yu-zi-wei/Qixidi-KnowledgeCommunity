<template>
  <div class="admin-mcp">
    <!-- 管理区：API key / 写作 skill tab 切换 -->
    <n-tabs type="segment" animated class="mcp-tabs">
      <n-tab-pane name="apiKey" tab="API Key">
        <section class="mcp-section">
      <div class="mcp-section-header">
        <h2 class="mcp-section-title"><n-icon><Key /></n-icon>API Key</h2>
        <n-button size="small" type="primary" @click="openKeyCreate">
          <template #icon><n-icon><Plus /></n-icon></template>
          新建 Key
        </n-button>
      </div>

      <div v-if="keyLoading" class="mcp-loading"><n-spin size="small" /></div>
      <CommonEmptyState v-else-if="!keyList.length" description="还没有 API key，新建一个接入你的 agent" />
      <div v-else class="mcp-key-list">
        <div v-for="key in keyList" :key="key.id" class="mcp-key-item">
          <div class="mcp-key-main">
            <div class="mcp-key-name-row">
              <span class="mcp-key-name">{{ key.keyName }}</span>
              <n-tag size="small" :type="key.status === 1 && !isKeyExpired(key) ? 'success' : 'error'" round>
                {{ key.status === 1 && !isKeyExpired(key) ? '有效' : '已失效' }}
              </n-tag>
            </div>
            <div class="mcp-key-meta">
              <span :title="key.expireTime ? getFullDateTime(key.expireTime) : ''">
                {{ key.expireTime ? `有效期至 ${formatTime(key.expireTime)}` : '永久有效' }}
              </span>
              <span :title="key.lastUsedTime ? getFullDateTime(key.lastUsedTime) : ''">
                {{ key.lastUsedTime ? `最近使用 ${formatTime(key.lastUsedTime)}` : '从未使用' }}
              </span>
              <span :title="getFullDateTime(key.createTime)">创建于 {{ formatTime(key.createTime) }}</span>
            </div>
          </div>
          <div class="mcp-key-actions">
            <n-button quaternary size="tiny" @click="openKeyRename(key)">
              <template #icon><n-icon><Edit /></n-icon></template>
              改名
            </n-button>
            <n-popconfirm @positive-click="handleRevoke(key.id)">
              <template #trigger>
                <n-button quaternary size="tiny" type="error">
                  <template #icon><n-icon><Trash /></n-icon></template>
                  吊销
                </n-button>
              </template>
              吊销后该 key 调用 MCP 立即失效，确定吊销「{{ key.keyName }}」？
            </n-popconfirm>
          </div>
        </div>
      </div>
        </section>
      </n-tab-pane>
      <n-tab-pane name="skill" tab="写作 Skill">
        <section class="mcp-section">
      <div class="mcp-section-header">
        <h2 class="mcp-section-title"><n-icon><Robot /></n-icon>写作 Skill</h2>
        <n-button size="small" type="primary" @click="openSkillCreate">
          <template #icon><n-icon><Plus /></n-icon></template>
          新建 Skill
        </n-button>
      </div>

      <div v-if="skillLoading" class="mcp-loading"><n-spin size="small" /></div>
      <CommonEmptyState v-else-if="!skillList.length" description="还没有自建 skill，新建一个沉淀你的写作偏好" />
      <div v-else class="mcp-skill-list">
        <div v-for="skill in skillList" :key="skill.id" class="mcp-skill-item">
          <div class="mcp-skill-main">
            <div class="mcp-key-name-row">
              <span class="mcp-key-name">{{ skill.name }}</span>
              <n-tag size="small" :type="skill.status === 1 ? 'success' : 'default'" round>
                {{ skill.status === 1 ? '启用' : '停用' }}
              </n-tag>
            </div>
            <p v-if="skill.description" class="mcp-skill-desc">{{ skill.description }}</p>
            <span class="mcp-key-meta" :title="getFullDateTime(skill.createTime)">
              创建于 {{ formatTime(skill.createTime) }}
            </span>
          </div>
          <div class="mcp-key-actions">
            <n-button quaternary size="tiny" @click="openSkillEdit(skill)">
              <template #icon><n-icon><Edit /></n-icon></template>
              编辑
            </n-button>
            <n-popconfirm @positive-click="handleSkillDelete(skill.id)">
              <template #trigger>
                <n-button quaternary size="tiny" type="error">
                  <template #icon><n-icon><Trash /></n-icon></template>
                  删除
                </n-button>
              </template>
              删除后 agent 将不再加载该 skill，确定删除「{{ skill.name }}」？
            </n-popconfirm>
          </div>
        </div>
      </div>
        </section>
      </n-tab-pane>
    </n-tabs>

    <!-- 文档区：概览 / 接入指南 / 工具清单 -->
    <n-tabs type="line" animated class="mcp-tabs">
      <n-tab-pane name="overview" tab="概览">
        <section class="mcp-section">
          <p class="mcp-overview-intro">
            栖息地 MCP 服务让 AI agent（Claude Code、Codex 等）以你的身份在栖息地读写内容：写文章、发小记、查询列表等。
            agent 的所有操作等同于你本人登录，请像保管密码一样保管 API key，不再使用时立即吊销。
          </p>
          <template v-if="mcpInfo">
            <div class="mcp-info-grid">
              <div class="mcp-info-item">
                <span class="mcp-info-label">端点地址</span>
                <div class="mcp-info-value-row">
                  <code class="mcp-info-code">{{ mcpInfo.endpoint }}</code>
                  <n-button quaternary circle size="tiny" @click="copyText(mcpInfo.endpoint)">
                    <template #icon><n-icon><Copy /></n-icon></template>
                  </n-button>
                </div>
              </div>
              <div class="mcp-info-item">
                <span class="mcp-info-label">认证请求头</span>
                <div class="mcp-info-value-row">
                  <code class="mcp-info-code">{{ mcpInfo.headerName }}</code>
                  <n-button quaternary circle size="tiny" @click="copyText(mcpInfo.headerName)">
                    <template #icon><n-icon><Copy /></n-icon></template>
                  </n-button>
                </div>
              </div>
            </div>
            <div class="mcp-notice">
              <n-icon class="mcp-notice-icon"><ShieldLock /></n-icon>
              <span>{{ mcpInfo.notice }}</span>
            </div>
          </template>
          <div v-else-if="infoLoading" class="mcp-loading">
            <n-spin size="small" />
          </div>
        </section>
      </n-tab-pane>
      <n-tab-pane name="guide" tab="接入指南">
        <section class="mcp-section">
          <template v-if="mcpInfo">
            <div class="mcp-snippets">
              <div v-for="item in snippetList" :key="item.label" class="mcp-snippet">
                <div class="mcp-snippet-header">
                  <span class="mcp-snippet-label"><n-icon><Code /></n-icon>{{ item.label }}</span>
                  <n-button quaternary circle size="tiny" @click="copyText(item.text)">
                    <template #icon><n-icon><Copy /></n-icon></template>
                  </n-button>
                </div>
                <pre class="mcp-snippet-code">{{ item.text }}</pre>
              </div>
            </div>
          </template>
          <div v-else-if="infoLoading" class="mcp-loading">
            <n-spin size="small" />
          </div>
        </section>
      </n-tab-pane>
      <n-tab-pane name="tools" :tab="toolsTabLabel">
        <section class="mcp-section">
          <div v-if="toolsLoading" class="mcp-loading"><n-spin size="small" /></div>
          <CommonEmptyState v-else-if="!mcpTools.length" description="工具清单加载失败或暂无工具" />
          <div v-else class="mcp-tool-list">
            <div v-for="tool in mcpTools" :key="tool.name" class="mcp-tool-item">
              <code class="mcp-tool-name">{{ tool.name }}</code>
              <p class="mcp-tool-desc">{{ tool.description }}</p>
            </div>
          </div>
        </section>
      </n-tab-pane>
    </n-tabs>

    <!-- 新建 API key -->
    <n-modal v-model:show="keyCreateVisible" preset="card" title="新建 API Key" :style="mcpModalStyle">
      <n-form ref="keyCreateFormRef" :model="keyCreateForm" :rules="keyCreateRules" label-placement="top">
        <n-form-item label="备注名" path="keyName">
          <n-input v-model:value="keyCreateForm.keyName" placeholder="如 workbuddy、claude-code" :maxlength="64" show-count />
        </n-form-item>
        <n-form-item label="有效期" path="validDays">
          <div class="mcp-valid-days">
            <n-switch v-model:value="keyCreateForm.permanent" size="small">
              <template #checked>永久</template>
              <template #unchecked>限期</template>
            </n-switch>
            <n-input-number
              v-model:value="keyCreateForm.validDays"
              :min="1"
              :max="3650"
              :disabled="keyCreateForm.permanent"
              placeholder="默认 365"
              class="mcp-full"
            />
          </div>
        </n-form-item>
      </n-form>
      <template #footer>
        <div class="mcp-modal-footer">
          <n-button @click="keyCreateVisible = false">取消</n-button>
          <n-button type="primary" :loading="keySubmitting" @click="handleKeyCreate">创建</n-button>
        </div>
      </template>
    </n-modal>

    <!-- 明文 key 展示（仅一次） -->
    <n-modal v-model:show="plaintextVisible" preset="card" title="API Key 创建成功" :style="mcpModalStyle">
      <n-alert type="warning" :show-icon="true">
        明文 key 仅此一次展示，关闭后无法再次查看，请立即复制保存。
      </n-alert>
      <div class="mcp-plaintext">
        <code>{{ plaintextKey }}</code>
        <n-button size="small" @click="copyText(plaintextKey)">
          <template #icon><n-icon><Copy /></n-icon></template>
          复制
        </n-button>
      </div>
    </n-modal>

    <!-- key 改名 -->
    <n-modal v-model:show="keyRenameVisible" preset="card" title="重命名 Key" :style="mcpModalStyle">
      <n-form ref="keyRenameFormRef" :model="keyRenameForm" :rules="keyCreateRules" label-placement="top">
        <n-form-item label="备注名" path="keyName">
          <n-input v-model:value="keyRenameForm.keyName" :maxlength="64" show-count />
        </n-form-item>
      </n-form>
      <template #footer>
        <div class="mcp-modal-footer">
          <n-button @click="keyRenameVisible = false">取消</n-button>
          <n-button type="primary" :loading="keySubmitting" @click="handleKeyRename">保存</n-button>
        </div>
      </template>
    </n-modal>

    <!-- skill 新建/编辑 -->
    <n-modal v-model:show="skillFormVisible" preset="card" :title="skillEditingId ? '编辑 Skill' : '新建 Skill'" :style="mcpModalStyle">
      <n-form ref="skillFormRef" :model="skillForm" :rules="skillRules" label-placement="top">
        <n-form-item label="名称" path="name">
          <n-input v-model:value="skillForm.name" placeholder="如 技术教程写作规范" :maxlength="100" show-count />
        </n-form-item>
        <n-form-item label="简介" path="description">
          <n-input
            v-model:value="skillForm.description"
            type="textarea"
            :rows="2"
            placeholder="一两句话说明用途，agent 通过它决定是否加载"
            :maxlength="500"
            show-count
          />
        </n-form-item>
        <n-form-item label="正文（Markdown 写作指令）" path="content">
          <n-input
            v-model:value="skillForm.content"
            type="textarea"
            :rows="10"
            placeholder="写入偏好、结构要求、语气示例等，agent 写作时会遵循"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <div class="mcp-modal-footer">
          <n-button @click="skillFormVisible = false">取消</n-button>
          <n-button type="primary" :loading="skillSubmitting" @click="handleSkillSubmit">保存</n-button>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { Code, Copy, Edit, Key, Plus, Robot, ShieldLock, Trash } from '@vicons/tabler'
import type { FormInst, FormRules } from 'naive-ui'
import type { AgentApiKeyItem, AgentSkillItem, McpToolSummaryItem } from '~/composables/useAgentApi'

definePageMeta({ layout: 'admin', middleware: 'creator' })

const message = useMessage()
const agentApi = useAgentApi()

// ==================== MCP 接入信息 ====================
const infoLoading = ref(false)
const mcpInfo = ref<Awaited<ReturnType<typeof agentApi.getMcpInfo>> | null>(null)

const snippetList = computed(() => {
  if (!mcpInfo.value) return []
  const s = mcpInfo.value.snippets
  return [
    { label: 'Claude Code', text: s.claudeCode },
    { label: 'Codex', text: s.codex },
    { label: '通用 JSON', text: s.generic }
  ]
})

const fetchMcpInfo = async () => {
  infoLoading.value = true
  try {
    mcpInfo.value = await agentApi.getMcpInfo()
  } catch {
  } finally {
    infoLoading.value = false
  }
}

// ==================== MCP 工具清单 ====================
const toolsLoading = ref(false)
const mcpTools = ref<McpToolSummaryItem[]>([])

const toolsTabLabel = computed(() =>
  mcpTools.value.length ? `工具清单 (${mcpTools.value.length})` : '工具清单'
)

const fetchMcpTools = async () => {
  toolsLoading.value = true
  try {
    mcpTools.value = (await agentApi.getMcpTools()) || []
  } catch {
  } finally {
    toolsLoading.value = false
  }
}

// ==================== API key ====================
const keyLoading = ref(false)
const keyList = ref<AgentApiKeyItem[]>([])

const keyCreateVisible = ref(false)
const keyCreateFormRef = ref<FormInst | null>(null)
const keyCreateForm = ref({ keyName: '', permanent: false, validDays: 365 })

/** n-modal teleport 到 body，scoped CSS 选不中节点，宽度只能走 inline style */
const mcpModalStyle = { width: '560px', maxWidth: 'calc(100vw - 32px)' }

/** expireTime 空 = 永久，不算过期 */
const isKeyExpired = (key: AgentApiKeyItem) =>
  !!key.expireTime && new Date(key.expireTime).getTime() < Date.now()
const keyCreateRules: FormRules = {
  keyName: { required: true, message: '请输入备注名', trigger: ['blur', 'input'] }
}

const plaintextVisible = ref(false)
const plaintextKey = ref('')

const keyRenameVisible = ref(false)
const keyRenameFormRef = ref<FormInst | null>(null)
const keyRenameForm = ref({ keyName: '' })
const renamingId = ref<number | null>(null)

const keySubmitting = ref(false)

const fetchKeyList = async () => {
  keyLoading.value = true
  try {
    keyList.value = (await agentApi.getKeyList()) || []
  } catch {
  } finally {
    keyLoading.value = false
  }
}

const openKeyCreate = () => {
  keyCreateForm.value = { keyName: '', permanent: false, validDays: 365 }
  keyCreateVisible.value = true
}

const handleKeyCreate = async () => {
  try {
    await keyCreateFormRef.value?.validate()
  } catch {
    return
  }
  keySubmitting.value = true
  try {
    const result = await agentApi.createKey({
      keyName: keyCreateForm.value.keyName.trim(),
      validDays: keyCreateForm.value.permanent ? 0 : keyCreateForm.value.validDays
    })
    keyCreateVisible.value = false
    plaintextKey.value = result.apiKey
    plaintextVisible.value = true
    fetchKeyList()
  } catch {
  } finally {
    keySubmitting.value = false
  }
}

const openKeyRename = (key: AgentApiKeyItem) => {
  renamingId.value = key.id
  keyRenameForm.value = { keyName: key.keyName }
  keyRenameVisible.value = true
}

const handleKeyRename = async () => {
  if (renamingId.value == null) return
  try {
    await keyRenameFormRef.value?.validate()
  } catch {
    return
  }
  keySubmitting.value = true
  try {
    await agentApi.renameKey(renamingId.value, { keyName: keyRenameForm.value.keyName.trim() })
    message.success('已重命名')
    keyRenameVisible.value = false
    fetchKeyList()
  } catch {
  } finally {
    keySubmitting.value = false
  }
}

const handleRevoke = async (id: number) => {
  try {
    await agentApi.revokeKey(id)
    message.success('已吊销')
    fetchKeyList()
  } catch {}
}

// ==================== 写作 skill ====================
const skillLoading = ref(false)
const skillList = ref<AgentSkillItem[]>([])

const skillFormVisible = ref(false)
const skillFormRef = ref<FormInst | null>(null)
const skillEditingId = ref<number | null>(null)
const skillSubmitting = ref(false)
const skillForm = ref({ name: '', description: '', content: '' })
const skillRules: FormRules = {
  name: { required: true, message: '请输入名称', trigger: ['blur', 'input'] },
  content: { required: true, message: '请输入正文', trigger: ['blur', 'input'] }
}

const fetchSkillList = async () => {
  skillLoading.value = true
  try {
    skillList.value = (await agentApi.getSkillList()) || []
  } catch {
  } finally {
    skillLoading.value = false
  }
}

const openSkillCreate = () => {
  skillEditingId.value = null
  skillForm.value = { name: '', description: '', content: '' }
  skillFormVisible.value = true
}

const openSkillEdit = (skill: AgentSkillItem) => {
  skillEditingId.value = skill.id
  skillForm.value = {
    name: skill.name,
    description: skill.description || '',
    content: skill.content
  }
  skillFormVisible.value = true
}

const handleSkillSubmit = async () => {
  try {
    await skillFormRef.value?.validate()
  } catch {
    return
  }
  skillSubmitting.value = true
  try {
    const payload = {
      name: skillForm.value.name.trim(),
      description: skillForm.value.description.trim() || undefined,
      content: skillForm.value.content
    }
    if (skillEditingId.value != null) {
      await agentApi.updateSkill(skillEditingId.value, payload)
      message.success('已保存')
    } else {
      await agentApi.createSkill(payload)
      message.success('已创建')
    }
    skillFormVisible.value = false
    fetchSkillList()
  } catch {
  } finally {
    skillSubmitting.value = false
  }
}

const handleSkillDelete = async (id: number) => {
  try {
    await agentApi.deleteSkill(id)
    message.success('已删除')
    fetchSkillList()
  } catch {}
}

// ==================== 通用 ====================
const copyText = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text)
    message.success('已复制')
  } catch {
    message.error('复制失败，请手动复制')
  }
}

onMounted(() => {
  fetchMcpInfo()
  fetchMcpTools()
  fetchKeyList()
  fetchSkillList()
})
</script>

<style scoped>
.admin-mcp {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
  max-width: 960px;
}

.mcp-tabs {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  padding: var(--space-4) var(--space-5);
  box-shadow: var(--shadow-sm);
}

.mcp-section {
  background: var(--color-surface);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-lg);
  padding: var(--space-5);
  box-shadow: var(--shadow-sm);
}

.mcp-section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-4);
}

.mcp-section-title {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-xl);
  font-family: var(--font-display);
  color: var(--color-ink);
  margin: 0;
}

.mcp-loading {
  display: flex;
  justify-content: center;
  padding: var(--space-8) 0;
}

/* ---- 接入信息 ---- */
.mcp-info-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  gap: var(--space-4);
}

.mcp-info-item {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.mcp-info-label {
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.mcp-info-value-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  min-width: 0;
}

.mcp-info-code {
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  color: var(--color-ink);
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-sm);
  padding: var(--space-1) var(--space-2);
  overflow-wrap: anywhere;
}

.mcp-notice {
  display: flex;
  align-items: flex-start;
  gap: var(--space-2);
  margin-top: var(--space-4);
  padding: var(--space-3);
  border-radius: var(--radius-md);
  background: var(--color-surface-warm);
  border: 1px solid var(--color-border);
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
}

.mcp-notice-icon {
  flex-shrink: 0;
  margin-top: 2px;
  color: var(--color-accent);
}

.mcp-overview-intro {
  margin: 0 0 var(--space-4);
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-relaxed);
}

.mcp-snippets {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.mcp-snippet {
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  overflow: hidden;
}

.mcp-snippet-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--space-2) var(--space-3);
  background: var(--color-surface-dim);
}

.mcp-snippet-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-ink-light);
}

.mcp-snippet-code {
  margin: 0;
  padding: var(--space-3);
  font-family: var(--font-mono);
  font-size: var(--text-xs);
  line-height: var(--leading-normal);
  color: var(--color-ink);
  overflow-x: auto;
  white-space: pre-wrap;
  word-break: break-all;
}

/* ---- key / skill 列表 ---- */
.mcp-key-list,
.mcp-skill-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.mcp-key-item,
.mcp-skill-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-3);
  padding: var(--space-3) var(--space-4);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
}

.mcp-key-main,
.mcp-skill-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.mcp-key-name-row {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.mcp-key-name {
  font-size: var(--text-base);
  font-weight: 600;
  color: var(--color-ink);
}

.mcp-key-meta {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-1) var(--space-4);
  font-size: var(--text-xs);
  color: var(--color-ink-muted);
}

.mcp-key-actions {
  display: flex;
  flex-shrink: 0;
  gap: var(--space-1);
}

.mcp-skill-desc {
  margin: 0;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* ---- 工具清单 ---- */
.mcp-tool-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.mcp-tool-item {
  padding: var(--space-3) var(--space-4);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
}

.mcp-tool-name {
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  font-weight: 600;
  color: var(--color-primary);
}

.mcp-tool-desc {
  margin: var(--space-1) 0 0;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: var(--leading-normal);
  white-space: pre-line;
}

/* ---- 弹窗 ---- */
.mcp-valid-days {
  display: flex;
  flex: 1;
  align-items: center;
  gap: var(--space-3);
}

.mcp-modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-3);
}

.mcp-plaintext {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-top: var(--space-4);
}

.mcp-plaintext code {
  flex: 1;
  min-width: 0;
  font-family: var(--font-mono);
  font-size: var(--text-sm);
  color: var(--color-ink);
  background: var(--color-surface-dim);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-sm);
  padding: var(--space-2) var(--space-3);
  overflow-wrap: anywhere;
}

.mcp-full {
  width: 100%;
}

@media (max-width: 640px) {
  .mcp-info-grid {
    grid-template-columns: minmax(0, 1fr);
  }

  .mcp-key-item,
  .mcp-skill-item {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
