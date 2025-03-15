<template>
  <div class="permission-list">
    <base-search
      v-model="searchForm"
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    >
      <el-form-item label="权限名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入权限名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="权限编码">
        <el-input
          v-model="searchForm.code"
          placeholder="请输入权限编码"
          clearable
        />
      </el-form-item>
      <el-form-item label="权限类型">
        <el-select
          v-model="searchForm.type"
          placeholder="请选择权限类型"
          clearable
        >
          <el-option label="目录" value="0" />
          <el-option label="菜单" value="1" />
          <el-option label="按钮" value="2" />
        </el-select>
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
      :data="tableData"
      :loading="loading"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :default-expand-all="true"
    >
      <el-table-column prop="name" label="权限名称" />
      <el-table-column prop="code" label="权限编码" />
      <el-table-column prop="type" label="权限类型" width="100">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.type)">
            {{ getTypeLabel(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路径" />
      <el-table-column prop="icon" label="图标" width="80">
        <template #default="{ row }">
          <el-icon v-if="row.icon">
            <component :is="row.icon" />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="80" />
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
          @click="handleAdd(row)"
        >
          新增
        </el-button>
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
      width="600px"
      :confirm-loading="dialogLoading"
      @confirm="handleDialogConfirm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="上级权限">
          <el-tree-select
            v-model="form.parentId"
            :data="permissionTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择上级权限"
            clearable
          />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-radio-group v-model="form.type" @change="handleTypeChange">
            <el-radio label="0">目录</el-radio>
            <el-radio label="1">菜单</el-radio>
            <el-radio label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入权限名称"
          />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入权限编码"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item v-if="form.type !== '2'" label="路径" prop="path">
          <el-input
            v-model="form.path"
            placeholder="请输入路径"
          />
        </el-form-item>
        <el-form-item v-if="form.type === '1'" label="组件" prop="component">
          <el-input
            v-model="form.component"
            placeholder="请输入组件路径"
          />
        </el-form-item>
        <el-form-item v-if="form.type === '0'" label="重定向" prop="redirect">
          <el-input
            v-model="form.redirect"
            placeholder="请输入重定向路径"
          />
        </el-form-item>
        <el-form-item v-if="form.type !== '2'" label="图标" prop="icon">
          <el-input
            v-model="form.icon"
            placeholder="请输入图标"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="form.sort"
            :min="0"
            :max="999"
            placeholder="请输入排序号"
          />
        </el-form-item>
        <el-form-item v-if="form.type !== '2'" label="是否隐藏" prop="hidden">
          <el-switch v-model="form.hidden" />
        </el-form-item>
        <el-form-item v-if="form.type === '1'" label="是否缓存" prop="keepAlive">
          <el-switch v-model="form.keepAlive" />
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
import { ref, reactive, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getPermissionList, getPermissionTree, createPermission, updatePermission, deletePermission, checkPermissionCode } from '@/api/permission'
import type { PermissionQuery, PermissionForm, PermissionInfo } from '@/api/permission'

// 搜索表单
const searchForm = reactive<PermissionQuery>({
  name: '',
  code: '',
  type: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<PermissionInfo[]>([])
const permissionTree = ref<PermissionInfo[]>([])

// 弹窗表单
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<PermissionForm>({
  name: '',
  code: '',
  type: '0',
  parentId: '',
  path: '',
  component: '',
  redirect: '',
  icon: '',
  sort: 0,
  hidden: false,
  keepAlive: false,
  status: '1'
})

// 表单校验规则
const rules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_:]+$/, message: '只能包含字母、数字、下划线和冒号', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  path: [
    { required: true, message: '请输入路径', trigger: 'blur' }
  ],
  component: [
    { required: true, message: '请输入组件路径', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取权限列表
const getList = async () => {
  try {
    loading.value = true
    const params: PermissionQuery = {
      ...searchForm
    }
    const data = await getPermissionList(params)
    tableData.value = data
  } catch (error) {
    console.error('获取权限列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载权限树
const loadPermissionTree = async () => {
  try {
    const data = await getPermissionTree()
    permissionTree.value = data
  } catch (error) {
    console.error('获取权限树失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  getList()
}

// 重置
const handleReset = () => {
  getList()
}

// 获取权限类型标签类型
const getTypeTag = (type: string) => {
  const map: Record<string, string> = {
    '0': 'primary',
    '1': 'success',
    '2': 'warning'
  }
  return map[type] || 'info'
}

// 获取权限类型标签文本
const getTypeLabel = (type: string) => {
  const map: Record<string, string> = {
    '0': '目录',
    '1': '菜单',
    '2': '按钮'
  }
  return map[type] || '未知'
}

// 权限类型变更
const handleTypeChange = (type: string) => {
  if (type === '2') {
    form.path = ''
    form.component = ''
    form.redirect = ''
    form.icon = ''
    form.hidden = false
    form.keepAlive = false
  } else if (type === '0') {
    form.component = ''
    form.keepAlive = false
  }
}

// 新增
const handleAdd = (row?: PermissionInfo) => {
  dialogTitle.value = '新增权限'
  dialogVisible.value = true
  form.id = undefined
  form.name = ''
  form.code = ''
  form.type = '0'
  form.parentId = row ? row.id : ''
  form.path = ''
  form.component = ''
  form.redirect = ''
  form.icon = ''
  form.sort = 0
  form.hidden = false
  form.keepAlive = false
  form.status = '1'
}

// 编辑
const handleEdit = (row: PermissionInfo) => {
  dialogTitle.value = '编辑权限'
  dialogVisible.value = true
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.type = row.type
  form.parentId = row.parentId || ''
  form.path = row.path || ''
  form.component = row.component || ''
  form.redirect = row.redirect || ''
  form.icon = row.icon || ''
  form.sort = row.sort || 0
  form.hidden = row.hidden || false
  form.keepAlive = row.keepAlive || false
  form.status = row.status
}

// 删除
const handleDelete = (row: PermissionInfo) => {
  ElMessageBox.confirm('确定要删除该权限吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePermission(row.id)
      ElMessage.success('删除成功')
      getList()
      loadPermissionTree()
    } catch (error) {
      console.error('删除权限失败:', error)
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
        const exists = await checkPermissionCode(form.code)
        if (exists) {
          ElMessage.error('权限编码已存在')
          return
        }
      }
      
      if (form.id) {
        // 更新
        await updatePermission(form.id, form)
        ElMessage.success('更新成功')
      } else {
        // 新增
        await createPermission(form)
        ElMessage.success('创建成功')
      }
      
      dialogVisible.value = false
      getList()
      loadPermissionTree()
    } catch (error) {
      console.error('保存权限失败:', error)
    } finally {
      dialogLoading.value = false
    }
  })
}

// 初始化
onMounted(() => {
  getList()
  loadPermissionTree()
})
</script>

<style scoped>
.permission-list {
  padding: 20px;
}
</style> 