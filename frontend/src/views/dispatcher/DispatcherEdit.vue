<template>
  <div class="dispatcher-edit">
    <div class="page-header">
      <div class="header-left">
        <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
        <h2>{{ isEdit ? '编辑调度员' : '新增调度员' }}</h2>
      </div>
    </div>

    <el-card v-loading="loading" class="edit-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 600px; margin: 0 auto;"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="组织" prop="organizationId">
          <el-select v-model="form.organizationId" placeholder="请选择组织" style="width: 100%">
            <el-option
              v-for="item in organizationOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关联用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户" style="width: 100%" clearable>
            <el-option
              v-for="item in userOptions"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus'
import {
  getDispatcherDetail,
  createDispatcher,
  updateDispatcher,
  checkDispatcherCode,
  checkDispatcherCodeExcludeId,
  DispatcherForm
} from '@/api/dispatcher'
import { getAllUsers } from '@/api/user'

const route = useRoute()
const router = useRouter()
const dispatcherId = route.params.id as string
const isEdit = !!dispatcherId

// 表单
const loading = ref(false)
const formRef = ref<FormInstance>()
const form = reactive<DispatcherForm>({
  name: '',
  code: '',
  organizationId: '',
  userId: '',
  status: 'ONLINE'
})

// 组织选项
const organizationOptions = ref<any[]>([])

// 用户选项
const userOptions = ref<any[]>([])

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  organizationId: [{ required: true, message: '请选择组织', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 验证编码是否重复
async function validateCode(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }

  try {
    const res = await (isEdit
      ? checkDispatcherCodeExcludeId(value, dispatcherId)
      : checkDispatcherCode(value))
    
    if (res.data) {
      callback(new Error('编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 加载组织选项
async function loadOrganizationOptions() {
  try {
    // 这里需要调用获取组织列表的API
    // const res = await getAllOrganizations()
    // organizationOptions.value = res.data
    
    // 临时使用模拟数据
    organizationOptions.value = [
      { id: '1', name: '总公司' },
      { id: '2', name: '分公司A' },
      { id: '3', name: '分公司B' }
    ]
  } catch (error) {
    console.error('加载组织选项失败', error)
  }
}

// 加载用户选项
async function loadUserOptions() {
  try {
    const res = await getAllUsers()
    userOptions.value = res.data
  } catch (error) {
    console.error('加载用户选项失败', error)
  }
}

// 加载调度员详情
async function loadDispatcherDetail() {
  if (!isEdit) return
  
  loading.value = true
  try {
    const res = await getDispatcherDetail(dispatcherId)
    Object.assign(form, res.data)
  } catch (error) {
    console.error('加载调度员详情失败', error)
    ElMessage.error('加载调度员详情失败')
  } finally {
    loading.value = false
  }
}

// 返回上一页
function goBack() {
  router.back()
}

// 提交表单
async function submitForm() {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      loading.value = true
      try {
        if (isEdit) {
          await updateDispatcher(dispatcherId, form)
          ElMessage.success('更新成功')
        } else {
          await createDispatcher(form)
          ElMessage.success('创建成功')
        }
        router.push('/dispatcher')
      } catch (error) {
        console.error(isEdit ? '更新失败' : '创建失败', error)
        ElMessage.error(isEdit ? '更新失败' : '创建失败')
      } finally {
        loading.value = false
      }
    } else {
      console.log('表单验证失败', fields)
    }
  })
}

// 生命周期钩子
onMounted(() => {
  loadOrganizationOptions()
  loadUserOptions()
  loadDispatcherDetail()
})
</script>

<style scoped>
.dispatcher-edit {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.edit-card {
  margin-bottom: 20px;
  padding: 20px;
}
</style> 