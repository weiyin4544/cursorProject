<template>
  <div class="subsystem-list">
    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="eChat" value="ECHAT" />
            <el-option label="PDT" value="PDT" />
            <el-option label="BTrunC" value="BTRUNC" />
            <el-option label="iCS" value="ICS" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增子系统</el-button>
      <el-button type="danger" :disabled="!selectedRows.length" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="名称" width="150" />
      <el-table-column prop="code" label="编码" width="150" />
      <el-table-column prop="type" label="类型" width="120">
        <template #default="scope">
          <el-tag :type="getTypeTag(scope.row.type)">
            {{ getTypeText(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column fixed="right" label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            link
            :type="scope.row.status === 'ACTIVE' ? 'danger' : 'success'"
            size="small"
            @click="handleStatusChange(scope.row)"
          >
            {{ scope.row.status === 'ACTIVE' ? '禁用' : '启用' }}
          </el-button>
          <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 子系统表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增子系统' : '编辑子系统'"
      width="600px"
      destroy-on-close
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-height: 500px; overflow-y: auto"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="eChat" value="ECHAT" />
            <el-option label="PDT" value="PDT" />
            <el-option label="BTrunC" value="BTRUNC" />
            <el-option label="iCS" value="ICS" />
          </el-select>
        </el-form-item>
        <el-form-item label="连接信息" prop="connectionInfo">
          <el-input
            v-model="form.connectionInfo"
            type="textarea"
            :rows="5"
            placeholder="请输入连接信息（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="启用" value="ACTIVE" />
            <el-option label="禁用" value="INACTIVE" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 子系统详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="子系统详情"
      width="600px"
      destroy-on-close
    >
      <div class="detail-container">
        <div class="detail-item">
          <span class="label">名称：</span>
          <span class="value">{{ detailData.name }}</span>
        </div>
        <div class="detail-item">
          <span class="label">编码：</span>
          <span class="value">{{ detailData.code }}</span>
        </div>
        <div class="detail-item">
          <span class="label">类型：</span>
          <span class="value">
            <el-tag :type="getTypeTag(detailData.type)">
              {{ getTypeText(detailData.type) }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">状态：</span>
          <span class="value">
            <el-tag :type="detailData.status === 'ACTIVE' ? 'success' : 'danger'">
              {{ detailData.status === 'ACTIVE' ? '启用' : '禁用' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">创建时间：</span>
          <span class="value">{{ detailData.createTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">更新时间：</span>
          <span class="value">{{ detailData.updateTime }}</span>
        </div>
        <div class="detail-item full-width">
          <span class="label">连接信息：</span>
          <div class="value">
            <pre>{{ formatJson(detailData.connectionInfo) }}</pre>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import {
  getSubsystemList,
  createSubsystem,
  updateSubsystem,
  deleteSubsystem,
  updateSubsystemStatus,
  getSubsystemDetail,
  checkSubsystemCode,
  checkSubsystemCodeExcludeId,
  SubsystemForm,
  SubsystemInfo,
  SubsystemQuery
} from '@/api/subsystem'

// 搜索表单
const searchForm = reactive<SubsystemQuery>({
  name: '',
  type: '',
  status: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref<any[]>([])
const selectedRows = ref<any[]>([])

// 分页
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const formRef = ref<FormInstance>()
const form = reactive<SubsystemForm>({
  name: '',
  code: '',
  type: 'ECHAT',
  connectionInfo: '',
  status: 'ACTIVE'
})

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive<SubsystemInfo>({} as SubsystemInfo)

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
  connectionInfo: [
    { validator: validateJson, trigger: 'blur' }
  ],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 初始化
onMounted(() => {
  fetchData()
})

// 获取表格数据
const fetchData = async () => {
  loading.value = true
  try {
    const params: SubsystemQuery = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getSubsystemList(params)
    if (res.data) {
      tableData.value = res.data.list
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取子系统列表失败', error)
    ElMessage.error('获取子系统列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key as keyof SubsystemQuery] = ''
  })
  pagination.pageNum = 1
  fetchData()
}

// 表格选择变化
const handleSelectionChange = (rows: any[]) => {
  selectedRows.value = rows
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  fetchData()
}

// 页码变化
const handleCurrentChange = (page: number) => {
  pagination.pageNum = page
  fetchData()
}

// 新增子系统
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑子系统
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  resetForm()
  Object.keys(form).forEach(key => {
    form[key as keyof SubsystemForm] = row[key]
  })
  dialogVisible.value = true
}

// 查看子系统详情
const handleDetail = async (row: any) => {
  try {
    const res = await getSubsystemDetail(row.id)
    if (res.data) {
      Object.assign(detailData, res.data)
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取子系统详情失败', error)
    ElMessage.error('获取子系统详情失败')
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.id = ''
  form.name = ''
  form.code = ''
  form.type = 'ECHAT'
  form.connectionInfo = ''
  form.status = 'ACTIVE'
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await createSubsystem(form)
          ElMessage.success('新增子系统成功')
        } else {
          await updateSubsystem(form.id!, form)
          ElMessage.success('更新子系统成功')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交子系统表单失败', error)
        ElMessage.error('提交子系统表单失败')
      }
    }
  })
}

// 删除子系统
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该子系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSubsystem(row.id)
      ElMessage.success('删除子系统成功')
      fetchData()
    } catch (error) {
      console.error('删除子系统失败', error)
      ElMessage.error('删除子系统失败')
    }
  }).catch(() => {})
}

// 批量删除子系统
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的子系统')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个子系统吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const promises = selectedRows.value.map(row => deleteSubsystem(row.id))
      await Promise.all(promises)
      ElMessage.success('批量删除子系统成功')
      fetchData()
    } catch (error) {
      console.error('批量删除子系统失败', error)
      ElMessage.error('批量删除子系统失败')
    }
  }).catch(() => {})
}

// 更改子系统状态
const handleStatusChange = (row: any) => {
  const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const statusText = newStatus === 'ACTIVE' ? '启用' : '禁用'
  ElMessageBox.confirm(`确定要${statusText}该子系统吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateSubsystemStatus(row.id, newStatus)
      ElMessage.success(`${statusText}子系统成功`)
      fetchData()
    } catch (error) {
      console.error(`${statusText}子系统失败`, error)
      ElMessage.error(`${statusText}子系统失败`)
    }
  }).catch(() => {})
}

// 验证编码是否存在
async function validateCode(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }
  try {
    const res = dialogType.value === 'add'
      ? await checkSubsystemCode(value)
      : await checkSubsystemCodeExcludeId(value, form.id!)
    
    if (res.data) {
      callback(new Error('编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('验证编码失败', error)
    callback()
  }
}

// 验证JSON格式
function validateJson(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }
  try {
    JSON.parse(value)
    callback()
  } catch (error) {
    callback(new Error('请输入有效的JSON格式'))
  }
}

// 格式化JSON
function formatJson(jsonString: string) {
  if (!jsonString) return ''
  try {
    const obj = JSON.parse(jsonString)
    return JSON.stringify(obj, null, 2)
  } catch (error) {
    return jsonString
  }
}

// 获取类型标签样式
function getTypeTag(type: string) {
  switch (type) {
    case 'ECHAT':
      return 'success'
    case 'PDT':
      return 'primary'
    case 'BTRUNC':
      return 'warning'
    case 'ICS':
      return 'danger'
    default:
      return 'info'
  }
}

// 获取类型文本
function getTypeText(type: string) {
  switch (type) {
    case 'ECHAT':
      return 'eChat'
    case 'PDT':
      return 'PDT'
    case 'BTRUNC':
      return 'BTrunC'
    case 'ICS':
      return 'iCS'
    default:
      return '未知'
  }
}
</script>

<style scoped>
.subsystem-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.detail-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
}

.full-width {
  grid-column: span 2;
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

pre {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  overflow: auto;
  margin: 0;
  width: 100%;
}
</style> 