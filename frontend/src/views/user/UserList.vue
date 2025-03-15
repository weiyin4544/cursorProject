<template>
  <div class="user-list">
    <base-search
      v-model="searchForm"
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    >
      <el-form-item label="用户名">
        <el-input
          v-model="searchForm.username"
          placeholder="请输入用户名"
          clearable
        />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input
          v-model="searchForm.nickname"
          placeholder="请输入昵称"
          clearable
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </el-form-item>
      <template #buttons>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          <span>新增</span>
        </el-button>
      </template>
    </base-search>

    <base-table
      ref="tableRef"
      v-model:current-page="page"
      v-model:page-size="limit"
      :data="tableData"
      :loading="loading"
      :total="total"
      :pagination="true"
      @page-change="handlePageChange"
      @size-change="handleSizeChange"
    >
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === '1' ? 'success' : 'danger'">
            {{ row.status === '1' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <template #operation="{ row }">
        <el-button
          type="primary"
          link
          @click="handleEdit(row)"
        >
          编辑
        </el-button>
        <el-button
          type="primary"
          link
          @click="handleDelete(row)"
        >
          删除
        </el-button>
      </template>
    </base-table>

    <base-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :confirm-loading="dialogLoading"
      @confirm="handleDialogConfirm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="请输入昵称"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
          />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="1">启用</el-radio>
            <el-radio label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </base-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'
import type { UserQuery, UserForm, UserInfo } from '@/api/user'

interface UserForm {
  id?: string
  username: string
  nickname: string
  password?: string
  email: string
  phone: string
  status: string
}

// 搜索表单
const searchForm = reactive({
  username: '',
  nickname: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<UserInfo[]>([])
const page = ref(1)
const limit = ref(10)
const total = ref(0)

// 弹窗表单
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<UserForm>({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  status: '1'
})

// 表单校验规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取用户列表
const getList = async () => {
  try {
    loading.value = true
    const params: UserQuery = {
      page: page.value,
      limit: limit.value,
      ...searchForm
    }
    const { data, total: totalCount } = await getUserList(params)
    tableData.value = data
    total.value = totalCount
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  page.value = 1
  getList()
}

// 重置
const handleReset = () => {
  page.value = 1
  getList()
}

// 分页
const handleSizeChange = (val: number) => {
  limit.value = val
  getList()
}

const handlePageChange = (val: number) => {
  page.value = val
  getList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
  form.id = undefined
  form.username = ''
  form.nickname = ''
  form.password = ''
  form.email = ''
  form.phone = ''
  form.status = '1'
}

// 编辑
const handleEdit = (row: UserInfo) => {
  dialogTitle.value = '编辑用户'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 删除
const handleDelete = (row: UserInfo) => {
  ElMessageBox.confirm('确认删除该用户吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除用户失败:', error)
    }
  })
}

// 弹窗确认
const handleDialogConfirm = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    dialogLoading.value = true
    
    if (form.id) {
      await updateUser(form.id, form)
      ElMessage.success('编辑成功')
    } else {
      await createUser(form)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    getList()
  } catch (error) {
    console.error('保存用户失败:', error)
  } finally {
    dialogLoading.value = false
  }
}

// 初始化
getList()
</script>

<style scoped lang="scss">
.user-list {
  .el-button {
    .el-icon {
      margin-right: 4px;
    }
  }
}
</style> 