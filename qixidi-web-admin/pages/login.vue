<template>
  <div class="login-page h-screen flex items-center justify-center">
    <n-card class="login-card" :style="{ width: '380px' }" title="栖息地 · 管理后台">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top">
        <n-form-item label="用户名" path="username">
          <n-input v-model:value="form.username" placeholder="请输入管理员账号" @keyup.enter="handleLogin" />
        </n-form-item>
        <n-form-item label="密码" path="password">
          <n-input
            v-model:value="form.password"
            type="password"
            show-password-on="click"
            placeholder="请输入密码"
            @keyup.enter="handleLogin"
          />
        </n-form-item>
        <n-form-item v-if="captchaEnabled" label="验证码" path="code">
          <div class="captcha-row">
            <n-input
              v-model:value="form.code"
              placeholder="请输入验证码"
              @keyup.enter="handleLogin"
            />
            <img
              v-if="captchaImg"
              :src="captchaImg"
              class="captcha-img"
              title="点击刷新验证码"
              alt="验证码"
              @click="loadCaptcha"
            />
          </div>
        </n-form-item>
        <n-button type="primary" block :loading="loading" @click="handleLogin">登 录</n-button>
      </n-form>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import type { FormInst, FormRules } from 'naive-ui';

// 登录页不套 admin 布局
definePageMeta({ layout: false });

const formRef = ref<FormInst | null>(null);
const loading = ref(false);
const message = useMessage();
const router = useRouter();
const adminStore = useAdminStore();
const { getCaptcha, login, getAdminInfo } = useAuthApi();

const form = reactive({
  username: '',
  password: '',
  code: '',
  uuid: ''
});

// 验证码开关由后端配置（sys_config）决定，响应缺省时按开启兜底
const captchaEnabled = ref(false);
const captchaImg = ref('');

const rules = computed<FormRules>(() => ({
  username: { required: true, message: '请输入用户名', trigger: 'blur' },
  password: { required: true, message: '请输入密码', trigger: 'blur' },
  ...(captchaEnabled.value
    ? { code: { required: true, message: '请输入验证码', trigger: 'blur' } }
    : {})
}));

const loadCaptcha = async () => {
  try {
    const data = await getCaptcha();
    captchaEnabled.value = data?.captchaOnOff === undefined ? true : !!data.captchaOnOff;
    form.uuid = data?.uuid || '';
    captchaImg.value = data?.img ? `data:image/gif;base64,${data.img}` : '';
    form.code = '';
  } catch {
    // useApi 已弹错；验证码拉取失败不阻塞页面，登录时后端会再校验
  }
};

const handleLogin = () => {
  formRef.value?.validate(async (errors) => {
    if (errors) return;
    loading.value = true;
    try {
      const data = await login({
        username: form.username,
        password: form.password,
        code: form.code,
        uuid: form.uuid
      });
      adminStore.saveToken(data.token);
      // permissions 是 RBAC 权限码（超管 ['*'] 通配），存入 store 驱动菜单/按钮可见性；roles 已无消费方
      const info = await getAdminInfo();
      adminStore.saveAdminInfo({
        userId: info.user.userId,
        userName: info.user.userName,
        nickName: info.user.nickName,
        avatar: info.user.avatar
      });
      adminStore.savePermissions(info.permissions || []);
      message.success('登录成功');
      router.push('/');
    } catch {
      // useApi 已弹后端 msg；验证码是一次性的，失败后刷新
      if (captchaEnabled.value) loadCaptcha();
    } finally {
      loading.value = false;
    }
  });
};

onMounted(loadCaptcha);
</script>

<style scoped lang="scss">
/* 书签风：暖纸底 + 白纸片卡片（同内容区/纸片体系） */
.login-page {
  background: #FAF9F6;
}

.login-card {
  border-radius: 10px;
  box-shadow: 0 1px 3px rgba(180, 170, 150, 0.18);
}

.captcha-row {
  display: flex;
  width: 100%;
  gap: 8px;
  align-items: center;
}

.captcha-img {
  height: 34px;
  cursor: pointer;
  border-radius: 3px;
  flex-shrink: 0;
}
</style>
