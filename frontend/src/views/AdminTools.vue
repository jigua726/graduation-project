<template>
  <div class="admin-tools">
    <div class="container">
      <h1 class="page-title">管理工具</h1>
      
      <div class="tool-card">
        <h3>🏷️ 为所有文章生成标签</h3>
        <p class="tool-description">
          系统将自动读取每篇文章的标题和内容，为符合内容的文章添加相关标签。
          支持的标签包括：技术、前端、后端、编程、Java、Vue、React、Python等。
        </p>
        <button 
          class="btn btn-primary" 
          @click="generateTags"
          :disabled="processing"
        >
          {{ processing ? '处理中...' : '开始生成标签' }}
        </button>
        <div v-if="result" class="result-message" :class="result.success ? 'success' : 'error'">
          {{ result.message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { adminAPI } from '../api'

const processing = ref(false)
const result = ref(null)

const generateTags = async () => {
  processing.value = true
  result.value = null
  
  try {
    const response = await adminAPI.autoGenerateTags()
    if (response.success) {
      result.value = {
        success: true,
        message: `✅ 成功！为 ${response.data.updatedArticles} 篇文章添加了标签，创建了 ${response.data.createdTags} 个新标签。`
      }
    } else {
      result.value = {
        success: false,
        message: '❌ 失败：' + (response.message || '未知错误')
      }
    }
  } catch (error) {
    result.value = {
      success: false,
      message: '❌ 失败：网络错误，请检查后端服务是否正常运行。'
    }
    console.error('生成标签失败:', error)
  } finally {
    processing.value = false
  }
}
</script>

<style scoped>
.admin-tools {
  min-height: 100vh;
  padding: 20px;
  background: #f5f7fa;
}

.container {
  max-width: 600px;
  margin: 0 auto;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.tool-card {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.tool-card h3 {
  font-size: 1.25rem;
  margin-bottom: 15px;
  color: #303133;
}

.tool-description {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 20px;
}

.btn {
  padding: 12px 30px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.result-message {
  margin-top: 20px;
  padding: 15px;
  border-radius: 8px;
  font-size: 14px;
}

.result-message.success {
  background: #f0f9eb;
  color: #67c23a;
  border: 1px solid #b3e6ab;
}

.result-message.error {
  background: #fef0f0;
  color: #f56c6c;
  border: 1px solid #fbc4c4;
}
</style>