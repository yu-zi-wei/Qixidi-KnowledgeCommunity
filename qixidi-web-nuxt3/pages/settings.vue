<template>
  <div class="settings-page">
    <div class="settings-container">
      <div class="settings-layout">
        <!-- 左侧菜单 -->
        <div class="settings-sidebar">
          <NuxtLink :to="`/user-home/article/${authStore.user?.uuid}`" class="settings-back">
            <n-icon size="16"><ArrowLeft /></n-icon>
            返回个人主页
          </NuxtLink>
          <div class="settings-menu">
            <button
              v-for="tab in tabs"
              :key="tab.value"
              class="settings-menu-item"
              :class="{ active: activeTab === tab.value }"
              @click="activeTab = tab.value"
            >
              <n-icon size="16"><component :is="tab.icon" /></n-icon>
              {{ tab.label }}
            </button>
          </div>
        </div>

        <!-- 中间内容 -->
        <div class="settings-content">
          <!-- 个人信息 -->
          <template v-if="activeTab === 1">
            <div class="settings-section">
              <!-- 移动端头像（在表单上方） -->
              <div class="mobile-avatar-area">
                <div class="avatar-card">
                  <div class="avatar-preview" v-if="form.avatar" @click="triggerAvatarUpload">
                    <img :src="form.avatar" alt="头像" />
                    <div class="avatar-mask">
                      <n-icon size="20" color="#fff"><Camera /></n-icon>
                    </div>
                  </div>
                  <button v-else class="avatar-upload-btn" @click="triggerAvatarUpload">
                    <n-icon size="28"><Camera /></n-icon>
                    <span>上传头像</span>
                  </button>
                </div>
              </div>

              <h2 class="settings-title">个人信息</h2>

              <div class="form-divider" />

              <!-- 昵称 -->
              <div class="form-row">
                <label class="form-label">昵称</label>
                <div class="form-field">
                  <n-input v-model:value="form.nickname" placeholder="请输入昵称" :maxlength="24" show-count />
                </div>
              </div>

              <!-- 职业 -->
              <div class="form-row">
                <label class="form-label">职业</label>
                <div class="form-field">
                  <n-input v-model:value="form.occupation" placeholder="请输入职业" :maxlength="20" show-count />
                </div>
              </div>

              <!-- 公司 -->
              <div class="form-row">
                <label class="form-label">公司</label>
                <div class="form-field">
                  <n-input v-model:value="form.company" placeholder="请输入公司" :maxlength="40" show-count />
                </div>
              </div>

              <!-- 个人主页 -->
              <div class="form-row">
                <label class="form-label">个人主页</label>
                <div class="form-field">
                  <n-input v-model:value="form.homepage" placeholder="请输入个人主页地址" :maxlength="60" show-count />
                </div>
              </div>

              <!-- 个人介绍 -->
              <div class="form-row">
                <label class="form-label">个人介绍</label>
                <div class="form-field">
                  <n-input v-model:value="form.introduce" type="textarea" placeholder="请输入个人介绍" :maxlength="200" show-count :rows="4" />
                </div>
              </div>

              <!-- 保存按钮 -->
              <div class="form-row">
                <label class="form-label" />
                <div class="form-field">
                  <n-button type="primary" :loading="saving" @click="handleSaveInfo">
                    保存修改
                  </n-button>
                </div>
              </div>
            </div>
          </template>

          <!-- 账号资料 -->
          <template v-if="activeTab === 2">
            <div class="settings-section">
              <h2 class="settings-title">账号资料</h2>

              <!-- 邮箱绑定 -->
              <div class="form-row">
                <label class="form-label">邮箱</label>
                <div class="form-field">
                  <div class="email-status" v-if="userInfo?.email">
                    <n-icon><Mail /></n-icon>
                    <span>{{ userInfo.email }}</span>
                    <n-button size="small" @click="showEmailDialog = true">换绑</n-button>
                  </div>
                  <div class="email-status" v-else>
                    <span class="email-unbind">未绑定</span>
                    <n-button size="small" type="primary" @click="showEmailDialog = true">绑定邮箱</n-button>
                  </div>
                </div>
              </div>

              <!-- 重置密码 -->
              <div class="form-row">
                <label class="form-label">密码</label>
                <div class="form-field">
                  <n-button size="small" :disabled="!userInfo?.email" @click="showPasswordDialog = true">
                    重置密码
                  </n-button>
                  <p class="form-hint" v-if="!userInfo?.email">绑定邮箱后才能重置密码</p>
                </div>
              </div>

              <div class="form-divider" />

              <!-- 账号注销 -->
              <div class="form-row">
                <label class="form-label danger-label">危险操作</label>
                <div class="form-field">
                  <n-button size="small" type="error" ghost @click="showCancelDialog = true">
                    注销账号
                  </n-button>
                  <p class="form-hint danger-hint">注销后账号数据不可恢复</p>
                </div>
              </div>
            </div>
          </template>
        </div>

        <!-- 右侧头像区域 -->
        <div class="settings-avatar-side" :class="{ 'is-hidden': activeTab !== 1 }">
          <div class="avatar-card">
            <div class="avatar-preview" v-if="form.avatar" @click="triggerAvatarUpload">
              <img :src="form.avatar" alt="头像" />
              <div class="avatar-mask">
                <n-icon size="20" color="#fff"><Camera /></n-icon>
              </div>
            </div>
            <button v-else class="avatar-upload-btn" @click="triggerAvatarUpload">
              <n-icon size="28"><Camera /></n-icon>
              <span>上传头像</span>
            </button>
            <p class="avatar-hint">支持 JPG、PNG、JPEG 格式</p>
            <p class="avatar-hint">最大 5MB</p>
          </div>
          <input
            ref="avatarInputRef"
            type="file"
            accept="image/jpeg,image/png,image/webp"
            style="display: none"
            @change="handleAvatarChange"
          />
        </div>
      </div>
    </div>

    <!-- 头像裁剪弹窗 -->
    <AvatarCropper
      v-model:show="showCropper"
      :image-src="cropperImageSrc"
      @confirm="handleCropConfirm"
    />

    <!-- 邮箱绑定/换绑弹窗 -->
    <n-modal v-model:show="showEmailDialog" preset="dialog" :title="userInfo?.email ? '换绑邮箱' : '绑定邮箱'" :show-icon="false" style="width: min(420px, 90vw);">
      <div class="dialog-form">
        <n-input v-model:value="emailForm.email" placeholder="请输入邮箱地址" size="medium" />
        <div class="code-row">
          <n-input v-model:value="emailForm.code" placeholder="验证码" size="medium" />
          <n-button size="medium" :disabled="emailCountdown > 0" :loading="sendingCode" @click="handleSendEmailCode(3)">
            {{ emailCountdown > 0 ? `${emailCountdown}s` : '获取验证码' }}
          </n-button>
        </div>
      </div>
      <template #action>
        <n-button @click="showEmailDialog = false">取消</n-button>
        <n-button type="primary" :loading="bindingEmail" @click="handleBindEmail">确定</n-button>
      </template>
    </n-modal>

    <!-- 重置密码弹窗 -->
    <n-modal v-model:show="showPasswordDialog" preset="dialog" title="重置密码" :show-icon="false" style="width: min(420px, 90vw);">
      <div class="dialog-form">
        <n-input v-model:value="passwordForm.password" type="password" show-password-on="click" placeholder="请输入新密码" size="medium" />
        <div class="code-row">
          <n-input v-model:value="passwordForm.code" placeholder="验证码" size="medium" />
          <n-button size="medium" :disabled="pwdCountdown > 0" :loading="sendingCode" @click="handleSendEmailCode(2)">
            {{ pwdCountdown > 0 ? `${pwdCountdown}s` : '获取验证码' }}
          </n-button>
        </div>
      </div>
      <template #action>
        <n-button @click="showPasswordDialog = false">取消</n-button>
        <n-button type="primary" :loading="resettingPwd" @click="handleResetPassword">确定</n-button>
      </template>
    </n-modal>

    <!-- 注销确认弹窗 -->
    <n-modal v-model:show="showCancelDialog" preset="dialog" title="确认注销" type="error" positive-text="确认注销" negative-text="取消" @positive-click="handleCancelAccount">
      <p>确认要注销账号吗？此操作不可恢复，所有数据将被永久删除。</p>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { User, Lock, Camera, Mail, ArrowLeft } from '@vicons/tabler'

definePageMeta({
  layout: 'blank',
  middleware: 'auth'
})

const authStore = useAuthStore()
const settingsApi = useSettingsApi()
const ossApi = useOssApi()
const message = useMessage()

const activeTab = ref(1)
const avatarInputRef = ref<HTMLInputElement | null>(null)
const showCropper = ref(false)
const cropperImageSrc = ref('')

const tabs = [
  { value: 1, label: '个人信息', icon: User },
  { value: 2, label: '账号资料', icon: Lock }
]

// --- 用户信息 ---
const { data: userInfo, refresh: refreshInfo } = await useAsyncData(
  'settings-user-info',
  () => settingsApi.getUserInfo()
)

// --- 个人信息表单 ---
const form = reactive({
  avatar: '',
  nickname: '',
  occupation: '',
  company: '',
  homepage: '',
  introduce: ''
})

watch(() => userInfo.value, (info) => {
  if (info) {
    form.avatar = info.avatar || ''
    form.nickname = info.nickname || ''
    form.occupation = info.occupation || ''
    form.company = info.company || ''
    form.homepage = info.homepage || ''
    form.introduce = info.introduce || ''
  }
}, { immediate: true })

// 头像上传
const triggerAvatarUpload = () => {
  avatarInputRef.value?.click()
}

const handleAvatarChange = (e: Event) => {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  if (file.size > 5 * 1024 * 1024) {
    message.warning('图片大小不能超过 5MB')
    return
  }
  // 读取为 data URL，传给裁剪组件
  const reader = new FileReader()
  reader.onload = (ev) => {
    cropperImageSrc.value = ev.target?.result as string
    showCropper.value = true
  }
  reader.readAsDataURL(file)
  // 重置 input，允许再次选择同一文件
  ;(e.target as HTMLInputElement).value = ''
}

const handleCropConfirm = async (blob: Blob) => {
  try {
    const file = new File([blob], 'avatar.png', { type: 'image/png' })
    const url = await ossApi.uploadFile(file)
    form.avatar = url
    message.success('头像上传成功')
  } catch {}
}

// 保存个人信息
const saving = ref(false)
const handleSaveInfo = async () => {
  if (!form.nickname.trim()) {
    message.warning('昵称不能为空')
    return
  }
  saving.value = true
  try {
    await settingsApi.updateUserInfo({
      uuid: authStore.user!.uuid,
      nickname: form.nickname,
      avatar: form.avatar || undefined,
      occupation: form.occupation || undefined,
      company: form.company || undefined,
      homepage: form.homepage || undefined,
      introduce: form.introduce || undefined
    })
    message.success('保存成功')
    await refreshInfo()
    // 重新获取用户信息，更新 authStore 缓存（头像、昵称等所有字段）
    await authStore.fetchUser()
  } catch {} finally {
    saving.value = false
  }
}

// --- 邮箱绑定/换绑 ---
const showEmailDialog = ref(false)
const emailForm = reactive({ email: '', code: '' })
const bindingEmail = ref(false)
const emailCountdown = ref(0)
const sendingCode = ref(false)

const startCountdown = (seconds: number, target: Ref<number>) => {
  target.value = seconds
  const timer = setInterval(() => {
    target.value--
    if (target.value <= 0) clearInterval(timer)
  }, 1000)
}

const handleSendEmailCode = async (type: number) => {
  const email = type === 3 ? emailForm.email : userInfo.value?.email
  if (!email) {
    message.warning('请输入邮箱地址')
    return
  }
  sendingCode.value = true
  try {
    await settingsApi.sendEmailCode(email, type)
    message.success('验证码已发送')
    startCountdown(60, type === 3 ? emailCountdown : pwdCountdown)
  } catch {} finally {
    sendingCode.value = false
  }
}

const handleBindEmail = async () => {
  if (!emailForm.email.trim()) {
    message.warning('请输入邮箱地址')
    return
  }
  if (!emailForm.code.trim()) {
    message.warning('请输入验证码')
    return
  }
  bindingEmail.value = true
  try {
    await settingsApi.bindEmail({
      type: userInfo.value?.email ? 2 : 1,
      email: emailForm.email,
      code: emailForm.code
    })
    message.success(userInfo.value?.email ? '换绑成功' : '绑定成功')
    showEmailDialog.value = false
    emailForm.email = ''
    emailForm.code = ''
    await refreshInfo()
  } catch {} finally {
    bindingEmail.value = false
  }
}

// --- 重置密码 ---
const showPasswordDialog = ref(false)
const passwordForm = reactive({ password: '', code: '' })
const resettingPwd = ref(false)
const pwdCountdown = ref(0)

const handleResetPassword = async () => {
  if (!passwordForm.password.trim()) {
    message.warning('请输入新密码')
    return
  }
  if (!passwordForm.code.trim()) {
    message.warning('请输入验证码')
    return
  }
  resettingPwd.value = true
  try {
    await settingsApi.resetPassword({
      registerType: 2,
      email: userInfo.value!.email,
      password: passwordForm.password,
      phone: '',
      code: passwordForm.code
    })
    message.success('密码重置成功')
    showPasswordDialog.value = false
    passwordForm.password = ''
    passwordForm.code = ''
  } catch {} finally {
    resettingPwd.value = false
  }
}

// --- 账号注销 ---
const showCancelDialog = ref(false)
const handleCancelAccount = async () => {
  try {
    await settingsApi.cancelAccount()
    message.success('账号已注销')
    authStore.logout()
    navigateTo('/')
  } catch {}
}
</script>

<style>
.settings-page {
  min-height: 100vh;
  background: var(--color-surface-warm);
  padding: 40px 24px;
}

.settings-container {
  max-width: 840px;
  width: 100%;
  margin: 0 auto;
}

.settings-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

/* 左侧菜单 */
.settings-sidebar {
  width: 160px;
  flex-shrink: 0;
  position: sticky;
  top: 40px;
}

.settings-back {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  text-decoration: none;
  padding: 8px 14px;
  margin-bottom: 12px;
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
}

.settings-back:hover {
  color: var(--color-primary);
  background: var(--color-surface);
}

.settings-menu {
  display: flex;
  flex-direction: column;
}

.settings-menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: var(--text-sm);
  color: var(--color-ink-muted);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  white-space: nowrap;
  text-align: left;
  width: 100%;
}

.settings-menu-item:hover {
  color: var(--color-ink);
  background: var(--color-surface-dim);
}

.settings-menu-item.active {
  color: var(--color-primary);
  font-weight: 600;
  background: var(--color-primary-light);
}

/* 右侧内容 */
.settings-content {
  width: 480px;
  flex-shrink: 0;
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-sm);
  padding: 28px;
}

.settings-section {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.settings-title {
  font-size: var(--text-xl);
  font-weight: 700;
  color: var(--color-ink);
  margin: 0;
}

/* 右侧头像区域 */
.settings-avatar-side {
  width: 140px;
  flex-shrink: 0;
  position: sticky;
  top: 40px;
}

.settings-avatar-side.is-hidden {
  visibility: hidden;
  pointer-events: none;
}

.avatar-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.avatar-preview {
  width: 110px;
  height: 110px;
  border-radius: var(--radius-full);
  overflow: hidden;
  position: relative;
  cursor: pointer;
  flex-shrink: 0;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.avatar-mask {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--transition-fast);
}

.avatar-preview:hover .avatar-mask {
  opacity: 1;
}

.avatar-upload-btn {
  width: 110px;
  height: 110px;
  border-radius: var(--radius-full);
  border: 2px dashed var(--color-border);
  background: var(--color-surface-dim);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: var(--color-ink-muted);
  font-size: var(--text-xs);
  transition: all var(--transition-fast);
}

.avatar-upload-btn:hover {
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.avatar-hint {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin: 0;
  text-align: center;
  line-height: 1.4;
}

/* 分割线 */
.form-divider {
  height: 1px;
  background: var(--color-border-light);
  margin: 4px 0;
}

/* 表单行 */
.form-row {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.form-label {
  width: 80px;
  flex-shrink: 0;
  font-size: var(--text-sm);
  color: var(--color-ink-light);
  line-height: 34px;
  text-align: right;
}

.danger-label {
  color: var(--color-danger);
}

.form-field {
  flex: 1;
  min-width: 0;
}

.form-hint {
  font-size: var(--text-xs);
  color: var(--color-ink-faint);
  margin: 6px 0 0;
}

.danger-hint {
  color: var(--color-danger);
}

/* 邮箱状态 */
.email-status {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: var(--text-sm);
  color: var(--color-ink);
}

.email-unbind {
  color: var(--color-ink-faint);
}

/* 弹窗表单 */
.dialog-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
}

.code-row {
  display: flex;
  gap: 8px;
}

.code-row .n-input {
  flex: 1;
}

/* 移动端头像（默认隐藏） */
.mobile-avatar-area {
  display: none;
}

/* 响应式 */
@media (max-width: 768px) {
  .settings-page {
    padding: 16px 12px;
  }

  .settings-layout {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }

  .settings-sidebar {
    width: 100%;
    position: static;
  }

  .settings-back {
    margin-bottom: 8px;
  }

  .settings-menu {
    flex-direction: row;
    border-bottom: 1px solid var(--color-border-light);
    padding-bottom: 4px;
  }

  .settings-menu-item {
    flex: 1;
    justify-content: center;
    padding: 8px 12px;
    font-size: var(--text-xs);
    border-radius: var(--radius-sm);
  }

  .settings-menu-item.active {
    border-bottom: 2px solid var(--color-primary);
    border-radius: 0;
  }

  .settings-content {
    width: 100%;
    padding: 20px 16px;
  }

  /* 移动端显示头像区域 */
  .mobile-avatar-area {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
  }

  .mobile-avatar-area .avatar-preview,
  .mobile-avatar-area .avatar-upload-btn {
    width: 80px;
    height: 80px;
  }

  .mobile-avatar-area .avatar-upload-btn {
    font-size: 10px;
  }

  .mobile-avatar-area .avatar-upload-btn .n-icon {
    font-size: 20px;
  }

  /* 桌面端头像侧栏隐藏 */
  .settings-avatar-side {
    display: none;
  }

  .form-row {
    flex-direction: column;
    gap: 6px;
  }

  .form-label {
    width: auto;
    text-align: left;
    line-height: 1.4;
  }

  .email-status {
    flex-wrap: wrap;
  }
}
</style>
