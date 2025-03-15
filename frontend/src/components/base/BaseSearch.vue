<template>
  <div class="base-search">
    <el-form
      ref="formRef"
      :model="formData"
      :inline="true"
      @keyup.enter="handleSearch"
    >
      <slot></slot>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSearch">
          <el-icon><Search /></el-icon>
          <span>搜索</span>
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
          <span>重置</span>
        </el-button>
        <slot name="buttons"></slot>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { FormInstance } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'

interface Props {
  modelValue: Record<string, any>
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: Record<string, any>): void
  (e: 'search', formData: Record<string, any>): void
  (e: 'reset'): void
}>()

const formRef = ref<FormInstance>()
const formData = ref({ ...props.modelValue })

const handleSearch = () => {
  emit('search', formData.value)
}

const handleReset = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
  emit('update:modelValue', { ...formData.value })
  emit('reset')
}
</script>

<style scoped lang="scss">
.base-search {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  :deep(.el-form-item) {
    margin-bottom: 0;
  }

  .el-button {
    .el-icon {
      margin-right: 4px;
    }
  }
}
</style> 