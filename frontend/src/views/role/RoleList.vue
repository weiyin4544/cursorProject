<template>
  <div class="role-list">
    <base-search
      v-model="searchForm"
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    >
      <el-form-item label="角色名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入角色名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="角色编码">
        <el-input
          v-model="searchForm.code"
          placeholder="请输入角色编码"
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
      <el-table-column prop="name" label="角色名称" />
      <el-table-column prop="code" label="角色编码" />
      <el-table-column prop="description" label="角色描述" show-overflow-tooltip />
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
          @click="handleEdit(row)"
        >
          编辑
        </el-button>
        <el-button
          type="primary"
          link
          @click="handlePermission(row)"
        >
          权限
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
        <el-form-item label="角色名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入角色名称"
          />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入角色编码"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入角色描述"
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

    <base-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="500px"
      :confirm-loading="permissionDialogLoading"
      @confirm="handlePermissionConfirm"
    >
      <div v-loading="permissionTreeLoading">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          :props="{ label: 'name', children: 'children' }"
          show-checkbox
          node-key="id"
          default-expand-all
        />
      </div>
    </base-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import type { FormInstance, ElTree } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getRoleList, createRole, updateRole, deleteRole, checkRoleCode, getRolePermissions, assignPermissions } from '@/api/role'
import { getPermissionTree } from '@/api/permission'
import type { RoleQuery, RoleForm, RoleInfo } from '@/api/role'
import type { PermissionInfo } from '@/api/permission'

// 搜索表单
const searchForm = reactive<RoleQuery>({
  name: '',
  code: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<RoleInfo[]>([])
const page = ref(1)
const limit = ref(10)
const total = ref(0)

// 弹窗表单
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<RoleForm>({
  name: '',
  code: '',
  description: '',
  status: '1'
})

// 权限弹窗
const permissionDialogVisible = ref(false)
const permissionDialogLoading = ref(false)
const permissionTreeLoading = ref(false)
const permissionTree = ref<PermissionInfo[]>([])
const permissionTreeRef = ref<InstanceType<typeof ElTree>>()
const currentRoleId = ref('')

// 表单校验规则
const rules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_-]+$/, message: '只能包含字母、数字、下划线和中划线', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取角色列表
const getList = async () => {
  try {
    loading.value = true
    const params: RoleQuery = {
      ...searchForm,
      page: page.value,
      limit: limit.value
    }
    const { list, total: totalCount } = await getRoleList(params)
    tableData.value = list
    total.value = totalCount
  } catch (error) {
    console.error('获取角色列表失败:', error)
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
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
  form.id = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.status = '1'
}

// 编辑
const handleEdit = (row: RoleInfo) => {
  dialogTitle.value = '编辑角色'
  dialogVisible.value = true
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description || ''
  form.status = row.status
}

// 删除
const handleDelete = (row: RoleInfo) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除角色失败:', error)
    }
  }).catch(() => {
    // 取消删除
  })
}

// 弹窗确认
const handleDialogConfirm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      dialogLoading.value = true
      
      // 检查编码是否存在
      if (!form.id) {
        const exists = await checkRoleCode(form.code)
        if (exists) {
          ElMessage.error('角色编码已存在')
          return
        }
      }
      
      if (form.id) {
        // 更新
        await updateRole(form.id, form)
        ElMessage.success('更新成功')
      } else {
        // 新增
        await createRole(form)
        ElMessage.success('创建成功')
      }
      
      dialogVisible.value = false
      getList()
    } catch (error) {
      console.error('保存角色失败:', error)
    } finally {
      dialogLoading.value = false
    }
  })
}

// 加载权限树
const loadPermissionTree = async () => {
  try {
    permissionTreeLoading.value = true
    const data = await getPermissionTree()
    permissionTree.value = data
  } catch (error) {
    console.error('获取权限树失败:', error)
  } finally {
    permissionTreeLoading.value = false
  }
}

// 权限分配
const handlePermission = async (row: RoleInfo) => {
  currentRoleId.value = row.id
  permissionDialogVisible.value = true
  
  try {
    permissionTreeLoading.value = true
    
    // 加载权限树
    await loadPermissionTree()
    
    // 获取角色已有权限
    const permissionIds = await getRolePermissions(row.id)
    
    // 设置选中节点
    if (permissionTreeRef.value) {
      permissionTreeRef.value.setCheckedKeys(permissionIds)
    }
  } catch (error) {
    console.error('加载权限数据失败:', error)
  } finally {
    permissionTreeLoading.value = false
  }
}

// 权限分配确认
const handlePermissionConfirm = async () => {
  if (!permissionTreeRef.value) return
  
  try {
    permissionDialogLoading.value = true
    
    // 获取选中的权限ID
    const permissionIds = permissionTreeRef.value.getCheckedKeys() as string[]
    
    // 分配权限
    await assignPermissions(currentRoleId.value, permissionIds)
    
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    console.error('权限分配失败:', error)
  } finally {
    permissionDialogLoading.value = false
  }
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.role-list {
  padding: 20px;
}
</style> 