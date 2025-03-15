<template>
  <div class="employee-detail">
    <div class="page-header">
      <el-page-header @back="goBack" title="返回员工列表" :content="employee.name ? `员工详情: ${employee.name}` : '员工详情'" />
    </div>

    <div class="detail-container" v-loading="loading">
      <el-card class="basic-info-card">
        <template #header>
          <div class="card-header">
            <span>基本信息</span>
            <el-button type="primary" size="small" @click="handleEdit">编辑</el-button>
          </div>
        </template>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">姓名：</span>
            <span class="value">{{ employee.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">性别：</span>
            <span class="value">{{ employee.gender }}</span>
          </div>
          <div class="info-item">
            <span class="label">出生日期：</span>
            <span class="value">{{ employee.birthDate || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">身份证号：</span>
            <span class="value">{{ employee.idCard || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">邮箱：</span>
            <span class="value">{{ employee.email || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">地址：</span>
            <span class="value">{{ employee.address || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">部门：</span>
            <span class="value">{{ departmentName }}</span>
          </div>
          <div class="info-item">
            <span class="label">组织：</span>
            <span class="value">{{ organizationName }}</span>
          </div>
          <div class="info-item">
            <span class="label">职位：</span>
            <span class="value">{{ employee.position || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">入职日期：</span>
            <span class="value">{{ employee.hireDate || '未设置' }}</span>
          </div>
          <div class="info-item">
            <span class="label">状态：</span>
            <span class="value">
              <el-tag :type="employee.status === 'ACTIVE' ? 'success' : 'danger'">
                {{ employee.status === 'ACTIVE' ? '在职' : '离职' }}
              </el-tag>
            </span>
          </div>
          <div class="info-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ employee.createTime }}</span>
          </div>
          <div class="info-item">
            <span class="label">更新时间：</span>
            <span class="value">{{ employee.updateTime }}</span>
          </div>
        </div>
      </el-card>

      <el-card class="phone-info-card">
        <template #header>
          <div class="card-header">
            <span>电话号码</span>
            <el-button type="primary" size="small" @click="handleAddPhone">添加电话</el-button>
          </div>
        </template>
        <el-table :data="phoneList" border style="width: 100%">
          <el-table-column prop="phoneNumber" label="电话号码" width="150" />
          <el-table-column prop="type" label="类型" width="120">
            <template #default="scope">
              <el-tag :type="getPhoneTypeTag(scope.row.type)">
                {{ getPhoneTypeText(scope.row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimary" label="主要联系方式" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.isPrimary ? 'success' : 'info'">
                {{ scope.row.isPrimary ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button link type="primary" size="small" @click="handleEditPhone(scope.row)">编辑</el-button>
              <el-button
                v-if="!scope.row.isPrimary"
                link
                type="success"
                size="small"
                @click="handleSetPrimary(scope.row)"
              >
                设为主要
              </el-button>
              <el-button link type="danger" size="small" @click="handleDeletePhone(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 电话号码表单对话框 -->
    <el-dialog
      v-model="phoneFormDialogVisible"
      :title="phoneDialogType === 'add' ? '新增电话' : '编辑电话'"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="phoneFormRef"
        :model="phoneForm"
        :rules="phoneRules"
        label-width="100px"
      >
        <el-form-item label="电话号码" prop="phoneNumber">
          <el-input v-model="phoneForm.phoneNumber" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="phoneForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="手机" value="MOBILE" />
            <el-option label="办公电话" value="OFFICE" />
            <el-option label="家庭电话" value="HOME" />
          </el-select>
        </el-form-item>
        <el-form-item label="主要联系方式" prop="isPrimary">
          <el-switch v-model="phoneForm.isPrimary" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="phoneFormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitPhoneForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import { getEmployeeDetail, EmployeeInfo } from '@/api/employee'
import {
  getPhoneNumbersByEmployeeId,
  addPhoneNumber,
  updatePhoneNumber,
  deletePhoneNumber,
  setPrimaryPhoneNumber,
  PhoneNumberForm,
  PhoneNumberInfo
} from '@/api/phoneNumber'
import { getDeptDetail } from '@/api/department'
import { getOrgDetail } from '@/api/organization'

const route = useRoute()
const router = useRouter()
const loading = ref(false)

// 员工信息
const employee = ref<EmployeeInfo>({} as EmployeeInfo)
const departmentName = ref('')
const organizationName = ref('')

// 电话号码列表
const phoneList = ref<PhoneNumberInfo[]>([])

// 电话号码表单
const phoneFormDialogVisible = ref(false)
const phoneDialogType = ref<'add' | 'edit'>('add')
const phoneFormRef = ref<FormInstance>()
const phoneForm = reactive<PhoneNumberForm>({
  employeeId: '',
  phoneNumber: '',
  type: 'MOBILE',
  isPrimary: false
})

// 电话号码表单验证规则
const phoneRules = reactive({
  phoneNumber: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$|^0\d{2,3}-\d{7,8}$/, message: '请输入正确的电话号码', trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
})

// 初始化
onMounted(async () => {
  const employeeId = route.params.id as string
  if (employeeId) {
    await fetchEmployeeDetail(employeeId)
    await loadPhoneNumbers(employeeId)
  }
})

// 获取员工详情
const fetchEmployeeDetail = async (id: string) => {
  loading.value = true
  try {
    const res = await getEmployeeDetail(id)
    if (res.data) {
      employee.value = res.data
      
      // 获取部门和组织名称
      if (employee.value.departmentId) {
        await fetchDepartmentName(employee.value.departmentId)
      }
      
      if (employee.value.organizationId) {
        await fetchOrganizationName(employee.value.organizationId)
      }
    }
  } catch (error) {
    console.error('获取员工详情失败', error)
    ElMessage.error('获取员工详情失败')
  } finally {
    loading.value = false
  }
}

// 获取部门名称
const fetchDepartmentName = async (departmentId: string) => {
  try {
    const res = await getDeptDetail(departmentId)
    if (res.data) {
      departmentName.value = res.data.name
    }
  } catch (error) {
    console.error('获取部门名称失败', error)
  }
}

// 获取组织名称
const fetchOrganizationName = async (organizationId: string) => {
  try {
    const res = await getOrgDetail(organizationId)
    if (res.data) {
      organizationName.value = res.data.name
    }
  } catch (error) {
    console.error('获取组织名称失败', error)
  }
}

// 加载电话号码列表
const loadPhoneNumbers = async (employeeId: string) => {
  try {
    const res = await getPhoneNumbersByEmployeeId(employeeId)
    if (res.data) {
      phoneList.value = res.data
    }
  } catch (error) {
    console.error('加载电话号码列表失败', error)
    ElMessage.error('加载电话号码列表失败')
  }
}

// 返回员工列表
const goBack = () => {
  router.push('/employee')
}

// 编辑员工
const handleEdit = () => {
  router.push(`/employee/edit/${employee.value.id}`)
}

// 新增电话号码
const handleAddPhone = () => {
  phoneDialogType.value = 'add'
  resetPhoneForm()
  phoneForm.employeeId = employee.value.id
  phoneFormDialogVisible.value = true
}

// 编辑电话号码
const handleEditPhone = (row: PhoneNumberInfo) => {
  phoneDialogType.value = 'edit'
  resetPhoneForm()
  Object.keys(phoneForm).forEach(key => {
    phoneForm[key as keyof PhoneNumberForm] = row[key]
  })
  phoneFormDialogVisible.value = true
}

// 重置电话号码表单
const resetPhoneForm = () => {
  if (phoneFormRef.value) {
    phoneFormRef.value.resetFields()
  }
  phoneForm.id = ''
  phoneForm.employeeId = employee.value.id
  phoneForm.phoneNumber = ''
  phoneForm.type = 'MOBILE'
  phoneForm.isPrimary = false
}

// 提交电话号码表单
const submitPhoneForm = async () => {
  if (!phoneFormRef.value) return
  await phoneFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (phoneDialogType.value === 'add') {
          await addPhoneNumber(phoneForm)
          ElMessage.success('新增电话号码成功')
        } else {
          await updatePhoneNumber(phoneForm.id!, phoneForm)
          ElMessage.success('更新电话号码成功')
        }
        phoneFormDialogVisible.value = false
        await loadPhoneNumbers(employee.value.id)
      } catch (error) {
        console.error('提交电话号码表单失败', error)
        ElMessage.error('提交电话号码表单失败')
      }
    }
  })
}

// 删除电话号码
const handleDeletePhone = (row: PhoneNumberInfo) => {
  ElMessageBox.confirm('确定要删除该电话号码吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePhoneNumber(row.id)
      ElMessage.success('删除电话号码成功')
      await loadPhoneNumbers(employee.value.id)
    } catch (error) {
      console.error('删除电话号码失败', error)
      ElMessage.error('删除电话号码失败')
    }
  }).catch(() => {})
}

// 设置主要电话号码
const handleSetPrimary = async (row: PhoneNumberInfo) => {
  try {
    await setPrimaryPhoneNumber(row.id, row.employeeId)
    ElMessage.success('设置主要电话号码成功')
    await loadPhoneNumbers(employee.value.id)
  } catch (error) {
    console.error('设置主要电话号码失败', error)
    ElMessage.error('设置主要电话号码失败')
  }
}

// 获取电话类型标签样式
const getPhoneTypeTag = (type: string) => {
  switch (type) {
    case 'MOBILE':
      return 'success'
    case 'OFFICE':
      return 'primary'
    case 'HOME':
      return 'warning'
    default:
      return 'info'
  }
}

// 获取电话类型文本
const getPhoneTypeText = (type: string) => {
  switch (type) {
    case 'MOBILE':
      return '手机'
    case 'OFFICE':
      return '办公电话'
    case 'HOME':
      return '家庭电话'
    default:
      return '未知'
  }
}
</script>

<style scoped>
.employee-detail {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.detail-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
}

.label {
  font-weight: bold;
  margin-right: 8px;
  color: #606266;
  min-width: 80px;
}

.value {
  color: #303133;
}

.phone-info-card {
  margin-top: 20px;
}

@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 576px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style> 