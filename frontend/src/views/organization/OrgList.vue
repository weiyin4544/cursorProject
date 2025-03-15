<template>
  <div class="org-list">
    <base-search
      v-model="searchForm"
      :loading="loading"
      @search="handleSearch"
      @reset="handleReset"
    >
      <el-form-item label="组织名称">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入组织名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="组织编码">
        <el-input
          v-model="searchForm.code"
          placeholder="请输入组织编码"
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
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      :default-expand-all="true"
      @page-change="handlePageChange"
      @size-change="handleSizeChange"
    >
      <el-table-column prop="name" label="组织名称" />
      <el-table-column prop="code" label="组织编码" />
      <el-table-column prop="description" label="组织描述" show-overflow-tooltip />
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
        <el-form-item label="上级组织">
          <el-tree-select
            v-model="form.parentId"
            :data="orgTree"
            :props="{ label: 'name', value: 'id' }"
            placeholder="请选择上级组织"
            clearable
          />
        </el-form-item>
        <el-form-item label="组织名称" prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入组织名称"
          />
        </el-form-item>
        <el-form-item label="组织编码" prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入组织编码"
            :disabled="!!form.id"
          />
        </el-form-item>
        <el-form-item label="组织描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入组织描述"
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
import { getOrgList, getOrgTree, createOrg, updateOrg, deleteOrg } from '@/api/organization'
import type { OrgQuery, OrgForm, OrgInfo } from '@/api/organization'

// 搜索表单
const searchForm = reactive({
  name: '',
  code: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<OrgInfo[]>([])
const page = ref(1)
const limit = ref(10)
const total = ref(0)

// 组织树数据
const orgTree = ref<OrgInfo[]>([])

// 弹窗表单
const dialogVisible = ref(false)
const dialogLoading = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const form = reactive<OrgForm>({
  name: '',
  code: '',
  description: '',
  parentId: '',
  status: '1'
})

// 表单校验规则
const rules = {
  name: [
    { required: true, message: '请输入组织名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入组织编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_-]+$/, message: '只能包含字母、数字、下划线和中划线', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 获取组织列表
const getList = async () => {
  try {
    loading.value = true
    const params: OrgQuery = {
      page: page.value,
      limit: limit.value,
      ...searchForm
    }
    const { data, total: totalCount } = await getOrgList(params)
    tableData.value = data
    total.value = totalCount
  } catch (error) {
    console.error('获取组织列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取组织树
const loadOrgTree = async () => {
  try {
    orgTree.value = await getOrgTree()
  } catch (error) {
    console.error('获取组织树失败:', error)
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
  dialogTitle.value = '新增组织'
  dialogVisible.value = true
  form.id = undefined
  form.name = ''
  form.code = ''
  form.description = ''
  form.parentId = ''
  form.status = '1'
}

// 编辑
const handleEdit = (row: OrgInfo) => {
  dialogTitle.value = '编辑组织'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 删除
const handleDelete = (row: OrgInfo) => {
  ElMessageBox.confirm('确认删除该组织吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await deleteOrg(row.id)
      ElMessage.success('删除成功')
      getList()
      loadOrgTree()
    } catch (error) {
      console.error('删除组织失败:', error)
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
      await updateOrg(form.id, form)
      ElMessage.success('编辑成功')
    } else {
      await createOrg(form)
      ElMessage.success('新增成功')
    }
    
    dialogVisible.value = false
    getList()
    loadOrgTree()
  } catch (error) {
    console.error('保存组织失败:', error)
  } finally {
    dialogLoading.value = false
  }
}

// 初始化
onMounted(() => {
  getList()
  loadOrgTree()
})
</script>

<style scoped lang="scss">
.org-list {
  .el-button {
    .el-icon {
      margin-right: 4px;
    }
  }
}
</style> 