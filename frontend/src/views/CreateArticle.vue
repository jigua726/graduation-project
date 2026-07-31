<template>
  <div class="create-article-page">
    <div class="container">
      <h1 class="page-title">写文章</h1>
      
      <form @submit.prevent="handleSubmit" class="article-form">
        <div class="form-group">
          <label class="form-label">标题</label>
          <input 
            type="text" 
            v-model="articleForm.title" 
            placeholder="请输入文章标题"
            class="form-input"
            required
          />
        </div>

        <div class="form-group">
          <label class="form-label">封面图片</label>
          <div class="upload-area" @click="triggerFileInput">
            <img v-if="articleForm.coverImage" :src="articleForm.coverImage" class="cover-preview" />
            <div v-else class="upload-placeholder">
              <span class="upload-icon">📷</span>
              <span class="upload-text">点击上传封面</span>
            </div>
            <button v-if="articleForm.coverImage" type="button" class="remove-btn" @click.stop="removeCover">×</button>
          </div>
          <input 
            type="file" 
            accept="image/*" 
            ref="fileInput"
            class="hidden-file-input"
            @change="handleFileUpload"
          />
          <div v-if="uploading" class="upload-progress">
            <div class="progress-bar" :style="{ width: uploadProgress + '%' }"></div>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">分类</label>
          <select v-model="articleForm.categoryId" class="form-select" required>
            <option value="">请选择分类</option>
            <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </select>
        </div>

        <div class="form-group">
          <label class="form-label">标签</label>
          <input 
            type="text" 
            v-model="tagInput" 
            placeholder="输入标签后按回车添加"
            class="form-input"
            @keyup.enter="addTag"
          />
          <div class="tags-container">
            <span v-for="(tag, index) in articleForm.tags" :key="index" class="tag-item">
              {{ tag }}
              <span class="tag-remove" @click="removeTag(index)">×</span>
            </span>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">摘要</label>
          <textarea 
            v-model="articleForm.summary" 
            placeholder="文章摘要（可选，最多200字）"
            class="form-textarea"
            rows="3"
            maxlength="200"
          ></textarea>
          <span class="char-count">{{ articleForm.summary.length }}/200</span>
        </div>

        <div class="form-group">
          <label class="form-label">内容</label>
          <textarea 
            v-model="articleForm.content" 
            placeholder="写下你的文章内容...&#10;&#10;支持 Markdown 格式：&#10;- 使用 # 表示标题&#10;- 使用 **文字** 表示加粗&#10;- 使用 - 表示列表&#10;- 使用 > 表示引用"
            class="form-textarea large"
            rows="15"
            required
          ></textarea>
          <span class="char-count">{{ articleForm.content.length }} 字</span>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? '发布中...' : '发布文章' }}
          </button>
          <button type="button" class="btn btn-secondary" @click="goBack">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { articleAPI, categoryAPI, fileAPI } from '../api'

const articleForm = ref({
  title: '',
  content: '',
  summary: '',
  categoryId: '',
  tags: [],
  coverImage: ''
})

const tagInput = ref('')
const categories = ref([])
const uploading = ref(false)
const uploadProgress = ref(0)
const submitting = ref(false)
const fileInput = ref(null)

onMounted(() => {
  loadCategories()
})

const loadCategories = async () => {
  try {
    const response = await categoryAPI.getAll()
    if (response.success) {
      categories.value = response.data
    }
  } catch (error) {
    console.error('Failed to load categories:', error)
  }
}

const addTag = () => {
  const tag = tagInput.value.trim()
  if (tag && !articleForm.value.tags.includes(tag)) {
    articleForm.value.tags.push(tag)
    tagInput.value = ''
  }
}

const removeTag = (index) => {
  articleForm.value.tags.splice(index, 1)
}

const triggerFileInput = () => {
  if (fileInput.value && !articleForm.value.coverImage) {
    fileInput.value.click()
  }
}

const handleFileUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  uploading.value = true
  uploadProgress.value = 0
  
  try {
    const response = await fileAPI.upload(file)
    if (response.success) {
      articleForm.value.coverImage = response.data
      uploadProgress.value = 100
    } else {
      alert('图片上传失败: ' + response.message)
    }
  } catch (error) {
    console.error('Upload failed:', error)
    alert('图片上传失败，请稍后重试')
  } finally {
    uploading.value = false
  }
}

const removeCover = () => {
  articleForm.value.coverImage = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const handleSubmit = async () => {
  const errors = []
  
  if (!articleForm.value.title.trim()) {
    errors.push('• 请输入文章标题')
  }
  
  if (!articleForm.value.categoryId) {
    errors.push('• 请选择文章分类')
  }
  
  if (!articleForm.value.content.trim()) {
    errors.push('• 请输入文章内容')
  } else if (articleForm.value.content.trim().length < 100) {
    errors.push('• 文章内容至少需要100字（当前 ' + articleForm.value.content.trim().length + ' 字）')
  }
  
  if (errors.length > 0) {
    alert('请完善以下内容后再发布：\n\n' + errors.join('\n'))
    return
  }
  
  submitting.value = true
  
  try {
    const response = await articleAPI.create(articleForm.value)
    if (response.success) {
      window.location.href = `/article/${response.data.id}`
    }
  } catch (error) {
    if (error.response && error.response.data) {
      alert(error.response.data.message || '发布失败')
    } else {
      alert('发布失败，请检查网络连接')
    }
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  window.history.back()
}
</script>

<style scoped>
.create-article-page {
  min-height: 100vh;
  padding: 20px;
  background: #f5f7fa;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-label {
  display: block;
  font-weight: 600;
  margin-bottom: 8px;
  color: #333;
}

.form-input {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.form-input:focus {
  outline: none;
  border-color: #409eff;
}

.form-select {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
  background: #fff;
}

.form-select:focus {
  outline: none;
  border-color: #409eff;
}

.form-textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
  box-sizing: border-box;
  font-family: inherit;
  resize: vertical;
}

.form-textarea:focus {
  outline: none;
  border-color: #409eff;
}

.form-textarea.large {
  min-height: 250px;
}

.char-count {
  display: block;
  text-align: right;
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.upload-area {
  position: relative;
  width: 300px;
  height: 180px;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
}

.upload-area:hover {
  border-color: #409eff;
  background: #f5f7fa;
}

.upload-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.upload-icon {
  font-size: 48px;
  margin-bottom: 10px;
}

.upload-text {
  color: #909399;
  font-size: 14px;
}

.cover-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  font-size: 18px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hidden-file-input {
  display: none;
}

.upload-progress {
  width: 300px;
  height: 6px;
  background: #e4e7ed;
  border-radius: 3px;
  margin-top: 10px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: #409eff;
  transition: width 0.3s;
}

.tags-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 20px;
  font-size: 14px;
}

.tag-remove {
  margin-left: 8px;
  cursor: pointer;
  color: #909399;
}

.tag-remove:hover {
  color: #f56c6c;
}

.form-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-top: 30px;
}

.btn {
  padding: 12px 30px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-primary {
  background: #409eff;
  color: #fff;
}

.btn-primary:hover:not(:disabled) {
  background: #66b1ff;
}

.btn-primary:disabled {
  background: #a0cfff;
  cursor: not-allowed;
}

.btn-secondary {
  background: #f5f7fa;
  color: #606266;
  border: 1px solid #d9d9d9;
}

.btn-secondary:hover {
  background: #e4e7ed;
}
</style>