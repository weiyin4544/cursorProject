<template>
  <div class="terminal-list">
    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="组织">
          <el-select v-model="searchForm.organizationId" placeholder="请选择组织" clearable>
            <el-option
              v-for="item in organizationOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="子系统">
          <el-select v-model="searchForm.subsystemId" placeholder="请选择子系统" clearable>
            <el-option
              v-for="item in subsystemOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="operation-bar">
      <el-button type="primary" @click="handleAdd">新增终端</el-button>
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
      <el-table-column label="所属组织" width="150">
        <template #default="scope">
          {{ getOrganizationName(scope.row.organizationId) }}
        </template>
      </el-table-column>
      <el-table-column label="所属子系统" width="150">
        <template #default="scope">
          {{ getSubsystemName(scope.row.subsystemId) }}
        </template>
      </el-table-column>
      <el-table-column label="使用人员" width="150">
        <template #default="scope">
          {{ scope.row.personId ? getPersonName(scope.row.personId) : '未分配' }}
        </template>
      </el-table-column>
      <el-table-column prop="model" label="型号" width="120" />
      <el-table-column prop="serialNumber" label="序列号" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ONLINE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ONLINE' ? '在线' : '离线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column fixed="right" label="操作" width="250">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
          <el-button
            link
            :type="scope.row.status === 'ONLINE' ? 'danger' : 'success'"
            size="small"
            @click="handleStatusChange(scope.row)"
          >
            {{ scope.row.status === 'ONLINE' ? '设为离线' : '设为在线' }}
          </el-button>
          <el-button link type="primary" size="small" @click="handleAssign(scope.row)">分配</el-button>
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

    <!-- 终端表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增终端' : '编辑终端'"
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
        <el-form-item label="所属组织" prop="organizationId">
          <el-select v-model="form.organizationId" placeholder="请选择组织" style="width: 100%">
            <el-option
              v-for="item in organizationOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属子系统" prop="subsystemId">
          <el-select v-model="form.subsystemId" placeholder="请选择子系统" style="width: 100%">
            <el-option
              v-for="item in subsystemOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="使用人员" prop="personId">
          <el-select v-model="form.personId" placeholder="请选择人员" style="width: 100%" clearable>
            <el-option
              v-for="item in personOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="型号" prop="model">
          <el-input v-model="form.model" placeholder="请输入型号" />
        </el-form-item>
        <el-form-item label="序列号" prop="serialNumber">
          <el-input v-model="form.serialNumber" placeholder="请输入序列号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="在线" value="ONLINE" />
            <el-option label="离线" value="OFFLINE" />
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

    <!-- 终端详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="终端详情"
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
          <span class="label">所属组织：</span>
          <span class="value">{{ getOrganizationName(detailData.organizationId) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">所属子系统：</span>
          <span class="value">{{ getSubsystemName(detailData.subsystemId) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">使用人员：</span>
          <span class="value">{{ detailData.personId ? getPersonName(detailData.personId) : '未分配' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">型号：</span>
          <span class="value">{{ detailData.model || '无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">序列号：</span>
          <span class="value">{{ detailData.serialNumber || '无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">状态：</span>
          <span class="value">
            <el-tag :type="detailData.status === 'ONLINE' ? 'success' : 'danger'">
              {{ detailData.status === 'ONLINE' ? '在线' : '离线' }}
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
      </div>
    </el-dialog>

    <!-- 分配终端对话框 -->
    <el-dialog
      v-model="assignDialogVisible"
      title="分配终端"
      width="500px"
      destroy-on-close
    >
      <el-form
        ref="assignFormRef"
        :model="assignForm"
        label-width="100px"
      >
        <el-form-item label="终端名称">
          <el-input v-model="assignForm.terminalName" disabled />
        </el-form-item>
        <el-form-item label="使用人员" prop="personId">
          <el-select v-model="assignForm.personId" placeholder="请选择人员" style="width: 100%" clearable>
            <el-option
              v-for="item in personOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitAssignForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import {
  getTerminalList,
  createTerminal,
  updateTerminal,
  deleteTerminal,
  updateTerminalStatus,
  getTerminalDetail,
  assignTerminalToPerson,
  unassignTerminal,
  checkTerminalCode,
  checkTerminalCodeExcludeId,
  TerminalForm,
  TerminalInfo,
  TerminalQuery
} from '@/api/terminal'
import { getAllSubsystems } from '@/api/subsystem'
import { getAllOrganizations } from '@/api/organization'
import { getAllPersons } from '@/api/person'

// 搜索表单
const searchForm = reactive<TerminalQuery>({
  name: '',
  organizationId: '',
  subsystemId: '',
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
const form = reactive<TerminalForm>({
  name: '',
  code: '',
  organizationId: '',
  subsystemId: '',
  personId: '',
  model: '',
  serialNumber: '',
  status: 'OFFLINE'
})

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive<TerminalInfo>({} as TerminalInfo)

// 分配对话框
const assignDialogVisible = ref(false)
const assignFormRef = ref<FormInstance>()
const assignForm = reactive({
  terminalId: '',
  terminalName: '',
  personId: ''
})

// 选项数据
const organizationOptions = ref<any[]>([])
const subsystemOptions = ref<any[]>([])
const personOptions = ref<any[]>([])

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  organizationId: [{ required: true, message: '请选择组织', trigger: 'change' }],
  subsystemId: [{ required: true, message: '请选择子系统', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 初始化
onMounted(() => {
  fetchData()
  fetchOptions()
})

// 获取表格数据
const fetchData = async () => {
  loading.value = true
  try {
    const params: TerminalQuery = {
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    }
    const res = await getTerminalList(params)
    if (res.data) {
      tableData.value = res.data.list
      pagination.total = res.data.total
    }
  } catch (error) {
    console.error('获取终端列表失败', error)
    ElMessage.error('获取终端列表失败')
  } finally {
    loading.value = false
  }
}

// 获取选项数据
const fetchOptions = async () => {
  try {
    // 获取组织选项
    const orgRes = await getAllOrganizations()
    if (orgRes.data) {
      organizationOptions.value = orgRes.data
    }

    // 获取子系统选项
    const subsystemRes = await getAllSubsystems()
    if (subsystemRes.data) {
      subsystemOptions.value = subsystemRes.data
    }

    // 获取人员选项
    const personRes = await getAllPersons()
    if (personRes.data) {
      personOptions.value = personRes.data
    }
  } catch (error) {
    console.error('获取选项数据失败', error)
    ElMessage.error('获取选项数据失败')
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
    searchForm[key as keyof TerminalQuery] = ''
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

// 新增终端
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 编辑终端
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  resetForm()
  Object.keys(form).forEach(key => {
    form[key as keyof TerminalForm] = row[key]
  })
  dialogVisible.value = true
}

// 查看终端详情
const handleDetail = async (row: any) => {
  try {
    const res = await getTerminalDetail(row.id)
    if (res.data) {
      Object.assign(detailData, res.data)
      detailDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取终端详情失败', error)
    ElMessage.error('获取终端详情失败')
  }
}

// 分配终端
const handleAssign = (row: any) => {
  assignForm.terminalId = row.id
  assignForm.terminalName = row.name
  assignForm.personId = row.personId || ''
  assignDialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.id = ''
  form.name = ''
  form.code = ''
  form.organizationId = ''
  form.subsystemId = ''
  form.personId = ''
  form.model = ''
  form.serialNumber = ''
  form.status = 'OFFLINE'
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await createTerminal(form)
          ElMessage.success('新增终端成功')
        } else {
          await updateTerminal(form.id!, form)
          ElMessage.success('更新终端成功')
        }
        dialogVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交终端表单失败', error)
        ElMessage.error('提交终端表单失败')
      }
    }
  })
}

// 提交分配表单
const submitAssignForm = async () => {
  try {
    if (assignForm.personId) {
      await assignTerminalToPerson(assignForm.terminalId, assignForm.personId)
      ElMessage.success('分配终端成功')
    } else {
      await unassignTerminal(assignForm.terminalId)
      ElMessage.success('取消分配终端成功')
    }
    assignDialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error('分配终端失败', error)
    ElMessage.error('分配终端失败')
  }
}

// 删除终端
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该终端吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTerminal(row.id)
      ElMessage.success('删除终端成功')
      fetchData()
    } catch (error) {
      console.error('删除终端失败', error)
      ElMessage.error('删除终端失败')
    }
  }).catch(() => {})
}

// 批量删除终端
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的终端')
    return
  }
  ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个终端吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const promises = selectedRows.value.map(row => deleteTerminal(row.id))
      await Promise.all(promises)
      ElMessage.success('批量删除终端成功')
      fetchData()
    } catch (error) {
      console.error('批量删除终端失败', error)
      ElMessage.error('批量删除终端失败')
    }
  }).catch(() => {})
}

// 更改终端状态
const handleStatusChange = (row: any) => {
  const newStatus = row.status === 'ONLINE' ? 'OFFLINE' : 'ONLINE'
  const statusText = newStatus === 'ONLINE' ? '在线' : '离线'
  ElMessageBox.confirm(`确定要将终端状态设置为${statusText}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await updateTerminalStatus(row.id, newStatus)
      ElMessage.success(`设置终端状态为${statusText}成功`)
      fetchData()
    } catch (error) {
      console.error(`设置终端状态失败`, error)
      ElMessage.error(`设置终端状态失败`)
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
      ? await checkTerminalCode(value)
      : await checkTerminalCodeExcludeId(value, form.id!)
    
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

// 获取组织名称
function getOrganizationName(id: string) {
  const org = organizationOptions.value.find(item => item.id === id)
  return org ? org.name : '未知组织'
}

// 获取子系统名称
function getSubsystemName(id: string) {
  const subsystem = subsystemOptions.value.find(item => item.id === id)
  return subsystem ? subsystem.name : '未知子系统'
}

// 获取人员名称
function getPersonName(id: string) {
  const person = personOptions.value.find(item => item.id === id)
  return person ? person.name : '未知人员'
}
</script>

<style scoped>
.terminal-list {
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

.label {
  font-weight: bold;
  margin-right: 8px;
  color: #606266;
  min-width: 80px;
}

.value {
  color: #303133;
}
</style> 