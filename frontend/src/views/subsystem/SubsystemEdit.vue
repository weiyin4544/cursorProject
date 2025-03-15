<template>
  <div class="subsystem-edit-container">
    <div class="header">
      <div class="title">{{ isEdit ? '编辑子系统' : '新增子系统' }}</div>
      <div class="actions">
        <el-button @click="goBack">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </div>

    <el-card v-loading="loading" class="form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        label-position="right"
        status-icon
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="子系统名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入子系统名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="子系统编码" prop="code">
              <el-input 
                v-model="form.code" 
                placeholder="请输入子系统编码" 
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="子系统类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择子系统类型" style="width: 100%">
                <el-option label="eChat" value="ECHAT" />
                <el-option label="PDT" value="PDT" />
                <el-option label="BTrunC" value="BTRUNC" />
                <el-option label="iCS" value="ICS" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" value="ENABLED" />
                <el-option label="禁用" value="DISABLED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="连接信息" prop="connectionInfo">
          <el-input
            v-model="form.connectionInfo"
            type="textarea"
            :rows="10"
            placeholder="请输入连接信息，JSON格式"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="formatJSON">格式化JSON</el-button>
          <el-button @click="generateSampleJSON">生成示例</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus'
import {
  getSubsystemDetail,
  createSubsystem,
  updateSubsystem,
  checkSubsystemCode,
  checkSubsystemCodeExcludeId,
  SubsystemForm
} from '@/api/subsystem'

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

// 判断是编辑还是新增
const isEdit = computed(() => !!route.params.id)

// 表单数据
const form = reactive<SubsystemForm>({
  name: '',
  code: '',
  type: '',
  connectionInfo: '',
  status: 'ENABLED'
})

// 表单验证规则
const rules = reactive({
  name: [
    { required: true, message: '请输入子系统名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入子系统编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择子系统类型', trigger: 'change' }
  ],
  connectionInfo: [
    { required: true, message: '请输入连接信息', trigger: 'blur' },
    { validator: validateJSON, trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
})

onMounted(async () => {
  // 如果是编辑模式，获取子系统详情
  if (isEdit.value) {
    await fetchSubsystemDetail(route.params.id as string)
  }
})

// 获取子系统详情
const fetchSubsystemDetail = async (id: string) => {
  loading.value = true
  try {
    const res = await getSubsystemDetail(id)
    if (res.data) {
      // 填充表单数据
      Object.keys(form).forEach(key => {
        if (key in res.data) {
          form[key as keyof SubsystemForm] = res.data[key as keyof SubsystemForm]
        }
      })
    }
  } catch (error) {
    console.error('获取子系统详情失败', error)
    ElMessage.error('获取子系统详情失败')
    goBack()
  } finally {
    loading.value = false
  }
}

// 验证子系统编码
async function validateCode(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }
  
  try {
    // 如果是编辑模式，需要排除当前ID
    const res = isEdit.value
      ? await checkSubsystemCodeExcludeId(value, route.params.id as string)
      : await checkSubsystemCode(value)
    
    if (res.data) {
      callback(new Error('子系统编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('验证子系统编码失败', error)
    callback()
  }
}

// 验证JSON格式
function validateJSON(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }
  try {
    JSON.parse(value)
    callback()
  } catch (error) {
    callback(new Error('请输入有效的JSON格式'))
  }
}

// 格式化JSON
const formatJSON = () => {
  if (!form.connectionInfo) {
    ElMessage.warning('请先输入连接信息')
    return
  }
  
  try {
    const parsed = JSON.parse(form.connectionInfo)
    form.connectionInfo = JSON.stringify(parsed, null, 2)
  } catch (error) {
    ElMessage.error('JSON格式不正确，无法格式化')
  }
}

// 生成示例JSON
const generateSampleJSON = () => {
  const samples: Record<string, any> = {
    'ECHAT': {
      serverUrl: 'https://echat-server.example.com',
      apiKey: 'your-api-key',
      apiSecret: 'your-api-secret',
      port: 8443,
      useSSL: true,
      timeout: 30000
    },
    'PDT': {
      serverAddress: '192.168.1.100',
      port: 5060,
      username: 'admin',
      password: 'password',
      deviceId: 'PDT-001',
      protocol: 'SIP'
    },
    'BTRUNC': {
      controllerIp: '10.0.0.1',
      controlPort: 9000,
      mediaPort: 9001,
      authToken: 'auth-token',
      encryptionEnabled: true,
      encryptionKey: 'encryption-key'
    },
    'ICS': {
      apiEndpoint: 'https://ics-api.example.com/v1',
      clientId: 'client-id',
      clientSecret: 'client-secret',
      region: 'us-east-1',
      maxConnections: 10,
      connectionTimeout: 5000
    }
  }
  
  if (form.type && form.type in samples) {
    form.connectionInfo = JSON.stringify(samples[form.type], null, 2)
  } else {
    form.connectionInfo = JSON.stringify({
      serverUrl: 'https://example.com/api',
      username: 'username',
      password: 'password',
      apiKey: 'your-api-key',
      timeout: 30000
    }, null, 2)
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit.value) {
          // 编辑模式
          await updateSubsystem(route.params.id as string, form)
          ElMessage.success('更新子系统成功')
        } else {
          // 新增模式
          await createSubsystem(form)
          ElMessage.success('创建子系统成功')
        }
        
        // 返回列表页
        goBack()
      } catch (error) {
        console.error('提交表单失败', error)
        ElMessage.error('提交表单失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 返回列表页
const goBack = () => {
  router.push('/subsystem/list')
}
</script>

<style scoped>
.subsystem-edit-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.form-card {
  margin-bottom: 20px;
}
</style> 