<template>
  <div class="terminal-edit-container">
    <div class="header">
      <div class="title">{{ isEdit ? '编辑终端' : '新增终端' }}</div>
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
            <el-form-item label="终端名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入终端名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="终端编码" prop="code">
              <el-input 
                v-model="form.code" 
                placeholder="请输入终端编码" 
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属组织" prop="organizationId">
              <el-select 
                v-model="form.organizationId" 
                placeholder="请选择所属组织" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="item in organizationOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属子系统" prop="subsystemId">
              <el-select 
                v-model="form.subsystemId" 
                placeholder="请选择所属子系统" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="item in subsystemOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="使用人员" prop="personId">
              <el-select 
                v-model="form.personId" 
                placeholder="请选择使用人员" 
                style="width: 100%"
                filterable
                clearable
              >
                <el-option
                  v-for="item in personOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="终端状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择终端状态" style="width: 100%">
                <el-option label="在线" value="ONLINE" />
                <el-option label="离线" value="OFFLINE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="终端型号" prop="model">
              <el-input v-model="form.model" placeholder="请输入终端型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="序列号" prop="serialNumber">
              <el-input v-model="form.serialNumber" placeholder="请输入序列号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="4"
            placeholder="请输入备注信息"
          />
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
  getTerminalDetail,
  createTerminal,
  updateTerminal,
  checkTerminalCode,
  checkTerminalCodeExcludeId,
  TerminalForm
} from '@/api/terminal'
import { getAllOrganizations } from '@/api/organization'
import { getAllSubsystems } from '@/api/subsystem'
import { getAllPersons } from '@/api/person'

const route = useRoute()
const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

// 判断是编辑还是新增
const isEdit = computed(() => !!route.params.id)

// 表单数据
const form = reactive<TerminalForm & { remark?: string }>({
  name: '',
  code: '',
  organizationId: '',
  subsystemId: '',
  personId: '',
  model: '',
  serialNumber: '',
  status: 'OFFLINE',
  remark: ''
})

// 选项数据
const organizationOptions = ref<any[]>([])
const subsystemOptions = ref<any[]>([])
const personOptions = ref<any[]>([])

// 表单验证规则
const rules = reactive({
  name: [
    { required: true, message: '请输入终端名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入终端编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  organizationId: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ],
  subsystemId: [
    { required: true, message: '请选择所属子系统', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择终端状态', trigger: 'change' }
  ]
})

onMounted(async () => {
  // 获取选项数据
  await fetchOptions()
  
  // 如果是编辑模式，获取终端详情
  if (isEdit.value) {
    await fetchTerminalDetail(route.params.id as string)
  }
})

// 获取选项数据
const fetchOptions = async () => {
  loading.value = true
  try {
    // 获取组织选项
    const orgRes = await getAllOrganizations()
    if (orgRes.data) {
      organizationOptions.value = orgRes.data
    }

    // 获取子系统选项
    const subsystemRes = await getAllSubsystems()
    if (subsystemRes.data) {
      subsystemOptions.value = subsystemRes.data
    }

    // 获取人员选项
    const personRes = await getAllPersons()
    if (personRes.data) {
      personOptions.value = personRes.data
    }
  } catch (error) {
    console.error('获取选项数据失败', error)
    ElMessage.error('获取选项数据失败')
  } finally {
    loading.value = false
  }
}

// 获取终端详情
const fetchTerminalDetail = async (id: string) => {
  loading.value = true
  try {
    const res = await getTerminalDetail(id)
    if (res.data) {
      // 填充表单数据
      Object.keys(form).forEach(key => {
        if (key in res.data) {
          form[key as keyof TerminalForm] = res.data[key as keyof TerminalForm]
        }
      })
    }
  } catch (error) {
    console.error('获取终端详情失败', error)
    ElMessage.error('获取终端详情失败')
    goBack()
  } finally {
    loading.value = false
  }
}

// 验证终端编码
async function validateCode(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }
  
  try {
    // 如果是编辑模式，需要排除当前ID
    const res = isEdit.value
      ? await checkTerminalCodeExcludeId(value, route.params.id as string)
      : await checkTerminalCode(value)
    
    if (res.data) {
      callback(new Error('终端编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('验证终端编码失败', error)
    callback()
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 移除表单中的额外字段
        const submitData = { ...form }
        delete submitData.remark
        
        if (isEdit.value) {
          // 编辑模式
          await updateTerminal(route.params.id as string, submitData)
          ElMessage.success('更新终端成功')
        } else {
          // 新增模式
          await createTerminal(submitData)
          ElMessage.success('创建终端成功')
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
  router.push('/terminal/list')
}
</script>

<style scoped>
.terminal-edit-container {
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