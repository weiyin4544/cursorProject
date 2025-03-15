<template>
  <div class="dept-list">
    <base-search
      v-model="searchForm"
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    >
      <el-form-item label="部门名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入部门名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="部门编码">
        <el-input
          v-model="searchForm.code"
          placeholder="请输入部门编码"
          clearable
        />
      </el-form-item>
      <el-form-item label="所属组织">
        <el-select
          v-model="searchForm.organizationId"
          placeholder="请选择所属组织"
          clearable
        >
          <el-option
            v-for="org in orgOptions"
            :key="org.id"
            :label="org.name"
            :value="org.id"
          />
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
      v-model:current-page="page"
      v-model:page-size="limit"
      :data="tableData"
      :loading="loading"
      :total="total"
      :pagination="true"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :default-expand-all="true"
      @page-change="handlePageChange"
      @size-change="handleSizeChange"
    >
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="code" label="部门编码" />
      <el-table-column prop="organizationName" label="所属组织" />
      <el-table-column prop="description" label="部门描述" show-overflow-tooltip />
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
        <el-form-item label="所属组织" prop="organizationId">
          <el-select
            v-model="form.organizationId"
            placeholder="请选择所属组织"
            @change="handleOrgChange"
          >
            <el-option
              v-for="org in orgOptions"
              :key="org.id"
              :label="org.name"
              :value="org.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上级部门">
          <el-tree-select
            v-model="form.parentId"
            :data="deptTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择上级部门"
            clearable
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入部门名称"
          />
        </el-form-item>
        <el-form-item label="部门编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入部门编码"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="部门描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入部门描述"
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
import { getDeptList, getDeptTreeByOrgId, createDept, updateDept, deleteDept, checkDeptCode, checkDeptCodeExcludeId } from '@/api/department'
import { getOrgTree } from '@/api/organization'
import type { DeptQuery, DeptForm, DeptInfo } from '@/api/department'
import type { OrgInfo } from '@/api/organization'

// 搜索表单
const searchForm = reactive({
  name: '',
  code: '',
  organizationId: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<DeptInfo[]>([])
const page = ref(1)
const limit = ref(10)
const total = ref(0)

// 组织选项
const orgOptions = ref<OrgInfo[]>([])

// 部门树数据
const deptTree = ref<DeptInfo[]>([])

// 弹窗表单
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<DeptForm>({
  name: '',
  code: '',
  description: '',
  organizationId: '',
  parentId: '',
  sort: 0,
  status: '1'
})

// 表单校验规则
const rules = {
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入部门编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_-]+$/, message: '只能包含字母、数字、下划线和中划线', trigger: 'blur' }
  ],
  organizationId: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取部门列表
const getList = async () => {
  try {
    loading.value = true
    const params: DeptQuery = {
      ...searchForm
    }
    const data = await getDeptList(params)
    tableData.value = data
    total.value = data.length
  } catch (error) {
    console.error('获取部门列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取组织树
const loadOrgTree = async () => {
  try {
    const data = await getOrgTree()
    orgOptions.value = data
  } catch (error) {
    console.error('获取组织树失败:', error)
  }
}

// 根据组织ID获取部门树
const loadDeptTree = async (organizationId: string) => {
  try {
    if (!organizationId) {
      deptTree.value = []
      return
    }
    const data = await getDeptTreeByOrgId(organizationId)
    deptTree.value = data
  } catch (error) {
    console.error('获取部门树失败:', error)
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

// 组织变更
const handleOrgChange = (val: string) => {
  form.parentId = ''
  loadDeptTree(val)
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增部门'
  dialogVisible.value = true
  form.id = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.organizationId = ''
  form.parentId = ''
  form.sort = 0
  form.status = '1'
  deptTree.value = []
}

// 编辑
const handleEdit = (row: DeptInfo) => {
  dialogTitle.value = '编辑部门'
  dialogVisible.value = true
  form.id = row.id
  form.name = row.name
  form.code = row.code
  form.description = row.description || ''
  form.organizationId = row.organizationId
  form.parentId = row.parentId || ''
  form.sort = row.sort || 0
  form.status = row.status
  loadDeptTree(row.organizationId)
}

// 删除
const handleDelete = (row: DeptInfo) => {
  ElMessageBox.confirm('确定要删除该部门吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDept(row.id)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除部门失败:', error)
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
        const exists = await checkDeptCode(form.code)
        if (exists) {
          ElMessage.error('部门编码已存在')
          return
        }
      } else {
        const exists = await checkDeptCodeExcludeId(form.code, form.id)
        if (exists) {
          ElMessage.error('部门编码已存在')
          return
        }
      }
      
      if (form.id) {
        // 更新
        await updateDept(form.id, form)
        ElMessage.success('更新成功')
      } else {
        // 新增
        await createDept(form)
        ElMessage.success('创建成功')
      }
      
      dialogVisible.value = false
      getList()
    } catch (error) {
      console.error('保存部门失败:', error)
    } finally {
      dialogLoading.value = false
    }
  })
}

// 初始化
onMounted(() => {
  loadOrgTree()
  getList()
})
</script>

<style scoped>
.dept-list {
  padding: 20px;
}
</style> 