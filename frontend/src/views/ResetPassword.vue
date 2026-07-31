<template>
  <div class="reset-password-page">
    <div class="background-gradient"></div>
    
    <div class="auth-container">
      <div class="auth-card" v-if="tokenValid">
        <div class="auth-header">
          <div class="logo">
            <img src="https://picsum.photos/seed/bloglogo/48/48" alt="博客社区" class="logo-image" />
            <span class="logo-text">博客社区</span>
          </div>
          <h1 class="auth-title">重置密码</h1>
          <p class="auth-subtitle">设置您的新密码</p>
        </div>

        <form @submit.prevent="handleSubmit" class="auth-form">
          <div class="form-group">
            <label class="form-label">新密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input 
                type="password" 
                v-model="password" 
                placeholder="请输入新密码（至少6个字符）"
                class="auth-input"
                required
              />
            </div>
            <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
          </div>

          <div class="form-group">
            <label class="form-label">确认密码</label>
            <div class="input-wrapper">
              <span class="input-icon">🔑</span>
              <input 
                type="password" 
                v-model="confirmPassword" 
                placeholder="请再次输入新密码"
                class="auth-input"
                required
              />
            </div>
            <span v-if="errors.confirmPassword" class="error-message">{{ errors.confirmPassword }}</span>
          </div>

          <button type="submit" class="auth-btn" :disabled="loading">
            {{ loading ? '重置中...' : '重置密码' }}
          </button>
        </form>

        <div class="auth-link">
          <a href="/login" class="link-btn">返回登录</a>
        </div>
      </div>

      <div v-else class="auth-card error-card">
        <div class="error-icon">❌</div>
        <h2 class="error-title">链接无效</h2>
        <p class="error-message">重置链接已过期或已被使用</p>
        <button class="auth-btn secondary" @click="goToForgotPassword">重新获取链接</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { authAPI } from '../api'

const route = useRoute()
const token = ref('')
const tokenValid = ref(false)
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const errors = reactive({ password: '', confirmPassword: '' })

onMounted(() => {
  token.value = route.params.token
  validateToken()
})

const validateToken = async () => {
  try {
    const response = await authAPI.validateResetToken(token.value)
    tokenValid.value = response.success && response.data
  } catch (error) {
    tokenValid.value = false
  }
}

const handleSubmit = async () => {
  errors.password = ''
  errors.confirmPassword = ''
  
  if (!password.value.trim()) {
    errors.password = '请输入新密码'
    return
  }
  
  if (password.value.length < 6) {
    errors.password = '密码长度至少为6个字符'
    return
  }
  
  if (!confirmPassword.value.trim()) {
    errors.confirmPassword = '请确认密码'
    return
  }
  
  if (password.value !== confirmPassword.value) {
    errors.confirmPassword = '两次输入的密码不一致'
    return
  }
  
  loading.value = true
  
  try {
    const response = await authAPI.resetPassword({ token: token.value, newPassword: password.value })
    if (response.success) {
      alert('密码重置成功！')
      window.location.href = '/login'
    }
  } catch (error) {
    if (error.response && error.response.data) {
      errors.password = error.response.data.message || '重置失败'
    } else {
      errors.password = '重置失败，请检查网络连接'
    }
  } finally {
    loading.value = false
  }
}

const goToForgotPassword = () => {
  window.location.href = '/forgot-password'
}
</script>

<style scoped>
.reset-password-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.background-gradient {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  animation: gradientShift 15s ease infinite;
}

@keyframes gradientShift {
  0%, 100% {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  }
  50% {
    background: linear-gradient(135deg, #764ba2 0%, #f093fb 50%, #667eea 100%);
  }
}

.auth-container {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 400px;
  padding: 20px;
}

.auth-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  padding: 40px;
}

.error-card {
  text-align: center;
}

.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.error-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.error-message {
  color: #909399;
  margin-bottom: 20px;
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.logo-image {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  object-fit: cover;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: bold;
  color: #303133;
}

.auth-title {
  font-size: 1.8rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.auth-subtitle {
  font-size: 0.95rem;
  color: #909399;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-weight: 600;
  margin-bottom: 8px;
  color: #303133;
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 1.1rem;
}

.auth-input {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 1rem;
  padding: 0 16px 0 45px;
  border: 1px solid #d9d9d9;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.auth-input:focus {
  outline: none;
  border-color: #409eff;
}

.error-message {
  display: block;
  color: #f56c6c;
  font-size: 0.85rem;
  margin-top: 5px;
}

.auth-btn {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  cursor: pointer;
  transition: background 0.3s;
}

.auth-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4191 100%);
}

.auth-btn:disabled {
  background: #a0cfff;
  cursor: not-allowed;
}

.auth-btn.secondary {
  background: #f5f7fa;
  color: #606266;
  border: 1px solid #d9d9d9;
}

.auth-btn.secondary:hover {
  background: #e4e7ed;
}

.auth-link {
  text-align: center;
  margin-top: 20px;
}

.link-btn {
  color: #667eea;
  font-weight: 500;
  text-decoration: none;
}

.link-btn:hover {
  text-decoration: underline;
}
</style>
