<template>
  <div class="user-role">
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
      <el-form-item label="姓名">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入姓名"
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
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="status" label="状态" width="80">
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
          @click="handleAssignRole(row)"
        >
          分配角色
        </el-button>
      </template>
    </base-table>

    <base-dialog
      v-model="dialogVisible"
      title="分配角色"
      width="500px"
      :confirm-loading="dialogLoading"
      @confirm="handleDialogConfirm"
    >
      <div v-loading="roleLoading">
        <el-transfer
          v-model="selectedRoles"
          :data="roleOptions"
          :titles="['未分配角色', '已分配角色']"
          :props="{
            key: 'id',
            label: 'name'
          }"
          filterable
        />
      </div>
    </base-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList } from '@/api/user'
import { getAllRoles } from '@/api/role'
import { getUserRoles, assignUserRoles } from '@/api/userRole'
import type { UserQuery, UserInfo } from '@/api/user'
import type { RoleInfo } from '@/api/role'

// 搜索表单
const searchForm = reactive<UserQuery>({
  username: '',
  name: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<UserInfo[]>([])
const page = ref(1)
const limit = ref(10)
const total = ref(0)

// 角色数据
const roleLoading = ref(false)
const roleOptions = ref<RoleInfo[]>([])
const selectedRoles = ref<string[]>([])
const currentUserId = ref('')

// 弹窗
const dialogVisible = ref(false)
const dialogLoading = ref(false)

// 获取用户列表
const getList = async () => {
  try {
    loading.value = true
    const params: UserQuery = {
      ...searchForm,
      page: page.value,
      limit: limit.value
    }
    const { list, total: totalCount } = await getUserList(params)
    tableData.value = list
    total.value = totalCount
  } catch (error) {
    console.error('获取用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载角色列表
const loadRoles = async () => {
  try {
    roleLoading.value = true
    const data = await getAllRoles()
    roleOptions.value = data
  } catch (error) {
    console.error('获取角色列表失败:', error)
  } finally {
    roleLoading.value = false
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

// 分配角色
const handleAssignRole = async (row: UserInfo) => {
  currentUserId.value = row.id
  dialogVisible.value = true
  selectedRoles.value = []
  
  try {
    roleLoading.value = true
    
    // 加载角色列表
    await loadRoles()
    
    // 获取用户已有角色
    const userRoles = await getUserRoles(row.id)
    selectedRoles.value = userRoles.map(role => role.id)
  } catch (error) {
    console.error('加载角色数据失败:', error)
  } finally {
    roleLoading.value = false
  }
}

// 弹窗确认
const handleDialogConfirm = async () => {
  try {
    dialogLoading.value = true
    
    // 分配角色
    await assignUserRoles(currentUserId.value, selectedRoles.value)
    
    ElMessage.success('角色分配成功')
    dialogVisible.value = false
  } catch (error) {
    console.error('角色分配失败:', error)
  } finally {
    dialogLoading.value = false
  }
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.user-role {
  padding: 20px;
}

.el-transfer {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}
</style> 