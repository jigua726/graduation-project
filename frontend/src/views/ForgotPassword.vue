<template>
  <div class="forgot-password-page">
    <div class="background-gradient"></div>
    
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <div class="logo">
            <img src="https://picsum.photos/seed/bloglogo/48/48" alt="博客社区" class="logo-image" />
            <span class="logo-text">博客社区</span>
          </div>
          <h1 class="auth-title">忘记密码</h1>
          <p class="auth-subtitle">输入您的邮箱，我们将发送重置链接</p>
        </div>

        <form @submit.prevent="handleSubmit" class="auth-form">
          <div class="form-group">
            <label class="form-label">邮箱地址</label>
            <div class="input-wrapper">
              <span class="input-icon">📧</span>
              <input 
                type="email" 
                v-model="email" 
                placeholder="请输入您的邮箱"
                class="auth-input"
                required
              />
            </div>
            <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
          </div>

          <button type="submit" class="auth-btn" :disabled="loading">
            {{ loading ? '发送中...' : '发送重置链接' }}
          </button>
        </form>

        <div class="auth-link">
          <span>记得密码了？</span>
          <a href="/login" class="link-btn">立即登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { authAPI } from '../api'

const email = ref('')
const loading = ref(false)
const errors = reactive({ email: '' })

const handleSubmit = async () => {
  errors.email = ''
  
  if (!email.value.trim()) {
    errors.email = '请输入邮箱地址'
    return
  }
  
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email.value)) {
    errors.email = '请输入有效的邮箱地址'
    return
  }
  
  loading.value = true
  
  try {
    const response = await authAPI.requestPasswordReset({ email: email.value })
    if (response.success) {
      alert('密码重置链接已发送，请检查邮箱')
      window.location.href = '/login'
    } else {
      errors.email = response.message || '发送失败'
    }
  } catch (error) {
    console.error('Password reset error:', error)
    if (error.response) {
      errors.email = error.response.data?.message || `请求失败 (${error.response.status})`
    } else if (error.message) {
      errors.email = `请求失败: ${error.message}`
    } else {
      errors.email = '发送失败，请检查网络连接'
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.forgot-password-page {
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

.auth-link {
  text-align: center;
  margin-top: 20px;
  color: #909399;
  font-size: 0.95rem;
}

.link-btn {
  color: #667eea;
  font-weight: 500;
  margin-left: 4px;
  text-decoration: none;
}

.link-btn:hover {
  text-decoration: underline;
}
</style>
