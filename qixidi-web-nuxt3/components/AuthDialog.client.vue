<template>
  <n-modal
    v-model:show="visible"
    :bordered="false"
    :mask-closable="true"
    :closable="true"
    preset="card"
    title=""
    :style="{ width: '800px' }"
    @after-leave="resetView"
  >
    <div class="auth-dialog">
      <!-- 左侧装饰 -->
      <div class="auth-aside">
        <div class="aside-content">
          <svg class="aside-icon" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
            <path d="M844.288 514.56c-52.736-27.392-201.216-35.84-201.216-35.84s152.576-4.352 240.896-33.536c0 0 102.656-40.96 68.864-162.048 0 0-20.736-63.488-132.864-75.264 0 0 9.984-83.968-70.656-132.352 0 0-56.832-34.304-141.056 17.92-71.936 52.224-80.64 234.752-81.152 246.272 0.512-11.008 6.144-177.664-33.792-249.088 0 0-42.752-99.072-164.096-48.128 0 0-102.912 33.024-81.664 128 0 0-150.272-31.744-171.008 111.104 0 0-17.92 116.224 109.568 141.568 41.472 10.24 182.016 32.768 182.016 32.768S79.872 429.312 65.792 569.344c0 0-24.32 131.584 125.184 128.256 0 0-16.128 98.816 79.36 131.328 0.256-0.256 127.232 36.608 187.648-124.928 13.312-37.632 24.576-63.488 30.976-128.256 0 0 2.304 256.512-214.272 387.072l72.192 43.008s157.696-154.88 160-409.6c-0.256-13.568-0.256-21.76-0.256-21.76 0.256 7.424 0.256 14.592 0.256 21.76 1.024 50.176 8.704 173.056 52.992 219.392 0 0 63.232 87.04 172.544 41.984 0 0 65.28-23.808 65.792-109.824 0 0 100.864 15.104 122.88-88.832-0.512 0 29.184-91.648-76.8-144.384z m0 0" fill="currentColor"/>
          </svg>
          <h2 class="aside-title">{{ siteName }}</h2>
          <p class="aside-desc">在文字里，找到栖身之所</p>
          <p class="aside-quote">记录思考，分享生活</p>
        </div>
        <div class="aside-pattern"></div>
      </div>

      <!-- 右侧表单 -->
      <div class="auth-form-area">
        <h3 class="form-title">{{ viewTitle }}</h3>
        <p class="form-subtitle">{{ viewSubtitle }}</p>

        <!-- 登录 -->
        <n-form v-if="currentView === 'login'" ref="loginFormRef" :model="loginForm" :rules="loginRules">
          <n-form-item path="username" :show-label="false">
            <n-input v-model:value="loginForm.username" placeholder="邮箱地址" size="large" />
          </n-form-item>
          <n-form-item path="password" :show-label="false">
            <n-input
              v-model:value="loginForm.password"
              type="password"
              show-password-on="click"
              placeholder="密码"
              size="large"
              @keyup.enter="handleLogin"
            />
          </n-form-item>
          <n-button type="primary" block size="large" :loading="loading" @click="handleLogin">
            登 录
          </n-button>
          <div class="auth-links">
            <button type="button" class="text-btn" @click="currentView = 'reset'">忘记密码？</button>
            <button type="button" class="text-btn primary" @click="currentView = 'register'">创建账号</button>
          </div>
        </n-form>

        <!-- 注册 -->
        <n-form v-else-if="currentView === 'register'" ref="registerFormRef" :model="registerForm" :rules="registerRules">
          <n-form-item path="email" :show-label="false">
            <n-input v-model:value="registerForm.email" placeholder="邮箱地址" size="large" />
          </n-form-item>
          <n-form-item path="password" :show-label="false">
            <n-input
              v-model:value="registerForm.password"
              type="password"
              show-password-on="click"
              placeholder="设置密码（至少6位）"
              size="large"
            />
          </n-form-item>
          <n-form-item path="code" :show-label="false">
            <div class="code-row">
              <n-input v-model:value="registerForm.code" placeholder="邮箱验证码" size="large" />
              <button
                type="button"
                class="code-btn"
                :disabled="registerCountdown > 0 || sendingCode"
                @click="sendCode('register')"
              >
                {{ registerCountdown > 0 ? `${registerCountdown}s` : '获取验证码' }}
              </button>
            </div>
          </n-form-item>
          <n-button type="primary" block size="large" :loading="loading" @click="handleRegister">
            创建账号
          </n-button>
          <div class="auth-links auth-links-center">
            <button type="button" class="text-btn" @click="currentView = 'login'">
              已有账号？<span class="primary">立即登录</span>
            </button>
          </div>
        </n-form>

        <!-- 重置密码 -->
        <n-form v-else ref="resetFormRef" :model="resetForm" :rules="resetRules">
          <n-form-item path="email" :show-label="false">
            <n-input v-model:value="resetForm.email" placeholder="邮箱地址" size="large" />
          </n-form-item>
          <n-form-item path="password" :show-label="false">
            <n-input
              v-model:value="resetForm.password"
              type="password"
              show-password-on="click"
              placeholder="新密码（至少6位）"
              size="large"
            />
          </n-form-item>
          <n-form-item path="code" :show-label="false">
            <div class="code-row">
              <n-input v-model:value="resetForm.code" placeholder="邮箱验证码" size="large" />
              <button
                type="button"
                class="code-btn"
                :disabled="resetCountdown > 0 || sendingCode"
                @click="sendCode('reset')"
              >
                {{ resetCountdown > 0 ? `${resetCountdown}s` : '获取验证码' }}
              </button>
            </div>
          </n-form-item>
          <n-button type="primary" block size="large" :loading="loading" @click="handleReset">
            重置密码
          </n-button>
          <div class="auth-links auth-links-center">
            <button type="button" class="text-btn" @click="currentView = 'login'">
              <span class="icon">←</span> 返回登录
            </button>
          </div>
        </n-form>
      </div>
    </div>
  </n-modal>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { FormInst, FormRules } from 'naive-ui'

const authStore = useAuthStore()
const { siteName } = useRuntimeConfig().public
const authApi = useAuthApi()
const authDialogStore = useAuthDialogStore()

// 使用本地 ref 管理 visible，通过 watch 同步 store
const visible = ref(false)
const currentView = ref<'login' | 'register' | 'reset'>('login')
const loading = ref(false)
const sendingCode = ref(false)
const registerCountdown = ref(0)
const resetCountdown = ref(0)

// 监听 store 状态变化，同步到本地
watch(() => authDialogStore.visible, (val) => {
  if (visible.value !== val) {
    visible.value = val
  }
}, { immediate: true })

// 监听本地状态变化（用户点击关闭），同步到 store
watch(visible, (val) => {
  if (authDialogStore.visible !== val) {
    authDialogStore.visible = val
  }
})

// 表单引用
const loginFormRef = ref<FormInst>()
const registerFormRef = ref<FormInst>()
const resetFormRef = ref<FormInst>()

// 表单数据
const loginForm = ref({
  username: '',
  password: ''
})

const registerForm = ref({
  email: '',
  password: '',
  code: ''
})

const resetForm = ref({
  email: '',
  password: '',
  code: ''
})

// 表单校验规则
const loginRules: FormRules = {
  username: {
    required: true,
    message: '请输入邮箱地址',
    trigger: 'blur'
  },
  password: {
    required: true,
    message: '请输入密码',
    trigger: 'blur'
  }
}

const registerRules: FormRules = {
  email: {
    required: true,
    message: '请输入邮箱地址',
    trigger: 'blur'
  },
  password: [
    {
      required: true,
      message: '请设置密码',
      trigger: 'blur'
    },
    {
      min: 6,
      message: '密码至少6位',
      trigger: 'blur'
    }
  ],
  code: {
    required: true,
    message: '请输入验证码',
    trigger: 'blur'
  }
}

const resetRules: FormRules = {
  email: {
    required: true,
    message: '请输入邮箱地址',
    trigger: 'blur'
  },
  password: [
    {
      required: true,
      message: '请输入新密码',
      trigger: 'blur'
    },
    {
      min: 6,
      message: '密码至少6位',
      trigger: 'blur'
    }
  ],
  code: {
    required: true,
    message: '请输入验证码',
    trigger: 'blur'
  }
}

// 视图标题
const viewTitle = computed(() => {
  const map = { login: '欢迎回来', register: `加入${siteName}`, reset: '找回密码' }
  return map[currentView.value]
})

const viewSubtitle = computed(() => {
  const map = { login: '登录您的账号以继续', register: `创建您的${siteName}账号`, reset: '重置您的密码' }
  return map[currentView.value]
})

// 重置视图
const resetView = () => {
  currentView.value = 'login'
  loading.value = false
  loginForm.value = { username: '', password: '' }
  registerForm.value = { email: '', password: '', code: '' }
  resetForm.value = { email: '', password: '', code: '' }
}

// 发送验证码
const sendCode = async (type: 'register' | 'reset') => {
  const email = type === 'register' ? registerForm.value.email : resetForm.value.email
  if (!email) {
    window.$message?.warning('请先输入邮箱')
    return
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email)) {
    window.$message?.warning('邮箱格式不正确')
    return
  }

  sendingCode.value = true
  try {
    await authApi.sendEmailCode(email, type === 'register' ? 1 : 2)
    window.$message?.success('验证码已发送')
    startCountdown(type)
  } catch (e: any) {
    const errorMsg = e?.statusMessage || e?.message || '发送失败'
    window.$message?.error(errorMsg)
  } finally {
    sendingCode.value = false
  }
}

// 倒计时
const startCountdown = (type: 'register' | 'reset') => {
  const counterRef = type === 'register' ? registerCountdown : resetCountdown
  counterRef.value = 60
  const timer = setInterval(() => {
    counterRef.value--
    if (counterRef.value <= 0) clearInterval(timer)
  }, 1000)
}

// 登录
const handleLogin = async () => {
  try {
    await loginFormRef.value?.validate()
  } catch { return }

  loading.value = true
  try {
    const result = await authApi.login(loginForm.value)
    authStore.setToken(result.token)
    await authStore.fetchUser()
    window.$message?.success('登录成功')

    // 获取重定向路径
    const redirect = authDialogStore.redirectAfterLogin || ''
    if (redirect) {
      authDialogStore.redirectAfterLogin = ''
    }

    // 直接设置本地状态关闭弹窗
    visible.value = false

    // 跳转
    if (redirect) {
      await navigateTo(redirect)
    }

    loginForm.value = { username: '', password: '' }
  } catch (e: any) {
    const errorMsg = e?.statusMessage || e?.message || '登录失败，请重试'
    window.$message?.error(errorMsg)
  } finally {
    loading.value = false
  }
}

// 注册
const handleRegister = async () => {
  try {
    await registerFormRef.value?.validate()
  } catch { return }

  loading.value = true
  try {
    await authApi.register({
      registerType: 1,
      email: registerForm.value.email,
      password: registerForm.value.password,
      code: registerForm.value.code
    })
    window.$message?.success('注册成功，请登录')
    registerForm.value = { email: '', password: '', code: '' }
    currentView.value = 'login'
  } catch (e: any) {
    const errorMsg = e?.statusMessage || e?.message || '注册失败，请重试'
    window.$message?.error(errorMsg)
  } finally {
    loading.value = false
  }
}

// 重置密码
const handleReset = async () => {
  try {
    await resetFormRef.value?.validate()
  } catch { return }

  loading.value = true
  try {
    await authApi.resetPassword({
      registerType: 2,
      email: resetForm.value.email,
      password: resetForm.value.password,
      code: resetForm.value.code
    })
    window.$message?.success('密码重置成功，请登录')
    resetForm.value = { email: '', password: '', code: '' }
    currentView.value = 'login'
  } catch (e: any) {
    const errorMsg = e?.statusMessage || e?.message || '重置失败，请重试'
    window.$message?.error(errorMsg)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-dialog {
  display: flex;
  min-height: 450px;
}

.auth-aside {
  flex: 1;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-hover) 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  padding: 48px;
  position: relative;
  overflow: hidden;
}

.aside-content {
  z-index: 1;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  width: 100%;
}

.aside-icon {
  width: 100px;
  height: 100px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  margin: 0;
}

.aside-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.aside-desc {
  font-size: 15px;
  opacity: 1;
  line-height: 1.6;
  margin: 0;
}

.aside-quote {
  font-size: 14px;
  opacity: 0.9;
  font-style: italic;
  margin: 0;
  padding-top: var(--space-2);
  position: relative;
}

.aside-quote::before {
  content: '"';
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 24px;
  opacity: 0.3;
}

.aside-pattern {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120px;
  background-image:
    radial-gradient(circle at 20% 50%, rgba(255, 255, 255, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255, 255, 255, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 50% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  opacity: 0.6;
}

.auth-form-area {
  flex: 1.3;
  padding: 44px;
  display: flex;
  flex-direction: column;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--color-ink);
  margin-bottom: 8px;
}

.form-subtitle {
  font-size: 14px;
  color: var(--color-ink-light);
  margin-bottom: 32px;
}

.input-icon {
  font-size: 18px;
  opacity: 0.6;
}

.code-row {
  display: flex;
  gap: 12px;
}

.code-btn {
  flex-shrink: 0;
  padding: 0 20px;
  background: var(--color-primary);
  color: white;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 14px;
  transition: opacity 0.2s;
}

.code-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.auth-links {
  display: flex;
  justify-content: space-between;
  margin-top: 24px;
}

.auth-links-center {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.text-btn {
  background: none;
  border: none;
  color: var(--color-ink-light);
  cursor: pointer;
  font-size: 14px;
  transition: color 0.2s;
}

.text-btn:hover {
  color: var(--color-ink);
}

.text-btn.primary {
  color: var(--color-primary);
}

.text-btn .icon {
  margin-right: 4px;
}

/* 深色模式 */
:deep(.dark) .auth-aside {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-hover) 100%);
}

:deep(.dark) .form-title {
  color: var(--color-ink);
}
</style>
