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
const adminStore = useAdminStore();
const router = useRouter();

const form = reactive({
  username: '',
  password: ''
});

const rules: FormRules = {
  username: { required: true, message: '请输入用户名', trigger: 'blur' },
  password: { required: true, message: '请输入密码', trigger: 'blur' }
};

// TODO(P0-1): 对接管理员登录接口（sys 瘦身后的新认证链路，见 docs/开发需求与设计.md）
const handleLogin = () => {
  formRef.value?.validate((errors) => {
    if (errors) return;
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
      message.warning('登录接口待对接（P0-1），当前为骨架占位');
    }, 400);
  });
};
</script>

<style scoped lang="scss">
.login-page {
  background: #f2f3f5;
}

.login-card {
  border-radius: 8px;
}
</style>
