<template>
  <div class="employee-edit">
    <div class="page-header">
      <el-page-header @back="goBack" title="返回员工列表" :content="isEdit ? '编辑员工' : '新增员工'" />
    </div>

    <div class="form-container">
      <el-card>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          v-loading="loading"
        >
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入姓名" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="form.gender">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="出生日期" prop="birthDate">
            <el-date-picker
              v-model="form.birthDate"
              type="date"
              placeholder="请选择出生日期"
              style="width: 100%"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="身份证号" prop="idCard">
            <el-input v-model="form.idCard" placeholder="请输入身份证号" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address" placeholder="请输入地址" />
          </el-form-item>
          <el-form-item label="部门" prop="departmentId">
            <el-select v-model="form.departmentId" placeholder="请选择部门" style="width: 100%">
              <el-option v-for="item in departmentOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="组织" prop="organizationId">
            <el-select v-model="form.organizationId" placeholder="请选择组织" style="width: 100%">
              <el-option v-for="item in organizationOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="职位" prop="position">
            <el-input v-model="form.position" placeholder="请输入职位" />
          </el-form-item>
          <el-form-item label="入职日期" prop="hireDate">
            <el-date-picker
              v-model="form.hireDate"
              type="date"
              placeholder="请选择入职日期"
              style="width: 100%"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="在职" value="ACTIVE" />
              <el-option label="离职" value="INACTIVE" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm">保存</el-button>
            <el-button @click="goBack">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus'
import {
  getEmployeeDetail,
  createEmployee,
  updateEmployee,
  EmployeeForm
} from '@/api/employee'
import { getDeptTree } from '@/api/department'
import { getOrgTree } from '@/api/organization'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const formRef = ref<FormInstance>()

// 判断是编辑还是新增
const isEdit = computed(() => !!route.params.id)

// 部门和组织数据
const departmentOptions = ref<any[]>([])
const organizationOptions = ref<any[]>([])

// 表单数据
const form = reactive<EmployeeForm>({
  name: '',
  gender: '男',
  birthDate: '',
  idCard: '',
  email: '',
  address: '',
  departmentId: '',
  organizationId: '',
  position: '',
  hireDate: '',
  status: 'ACTIVE'
})

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  idCard: [
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 初始化
onMounted(async () => {
  await Promise.all([loadDepartments(), loadOrganizations()])
  
  // 如果是编辑模式，加载员工详情
  if (isEdit.value) {
    await fetchEmployeeDetail(route.params.id as string)
  }
})

// 加载部门数据
const loadDepartments = async () => {
  try {
    const res = await getDeptTree()
    if (res.data) {
      departmentOptions.value = flattenTree(res.data)
    }
  } catch (error) {
    console.error('加载部门数据失败', error)
  }
}

// 加载组织数据
const loadOrganizations = async () => {
  try {
    const res = await getOrgTree()
    if (res.data) {
      organizationOptions.value = flattenTree(res.data)
    }
  } catch (error) {
    console.error('加载组织数据失败', error)
  }
}

// 将树形结构扁平化
const flattenTree = (tree: any[]): any[] => {
  const result: any[] = []
  const traverse = (nodes: any[], level = 0) => {
    nodes.forEach(node => {
      result.push({
        id: node.id,
        name: '　'.repeat(level) + node.name,
        rawName: node.name
      })
      if (node.children && node.children.length > 0) {
        traverse(node.children, level + 1)
      }
    })
  }
  traverse(tree)
  return result
}

// 获取员工详情
const fetchEmployeeDetail = async (id: string) => {
  loading.value = true
  try {
    const res = await getEmployeeDetail(id)
    if (res.data) {
      // 将员工详情数据填充到表单中
      Object.keys(form).forEach(key => {
        form[key as keyof EmployeeForm] = res.data[key as keyof EmployeeForm]
      })
    }
  } catch (error) {
    console.error('获取员工详情失败', error)
    ElMessage.error('获取员工详情失败')
  } finally {
    loading.value = false
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
          await updateEmployee(route.params.id as string, form)
          ElMessage.success('更新员工成功')
        } else {
          // 新增模式
          await createEmployee(form)
          ElMessage.success('新增员工成功')
        }
        goBack()
      } catch (error) {
        console.error('提交员工表单失败', error)
        ElMessage.error('提交员工表单失败')
      } finally {
        loading.value = false
      }
    }
  })
}

// 返回员工列表
const goBack = () => {
  router.push('/employee')
}
</script>

<style scoped>
.employee-edit {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.form-container {
  max-width: 800px;
  margin: 0 auto;
}
</style> 