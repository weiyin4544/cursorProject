<template>
  <div class="dispatcher-list">
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
      <el-button type="primary" @click="handleAdd">新增调度员</el-button>
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
      <el-table-column prop="organizationName" label="所属组织" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ONLINE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ONLINE' ? '在线' : '离线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column fixed="right" label="操作" width="280">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
          <el-button link type="primary" size="small" @click="handleSubsystems(scope.row)">子系统配置</el-button>
          <el-button
            link
            :type="scope.row.status === 'ONLINE' ? 'danger' : 'success'"
            size="small"
            @click="handleStatusChange(scope.row)"
          >
            {{ scope.row.status === 'ONLINE' ? '下线' : '上线' }}
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

    <!-- 调度员表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增调度员' : '编辑调度员'"
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
        <el-form-item label="组织" prop="organizationId">
          <el-select v-model="form.organizationId" placeholder="请选择组织" style="width: 100%">
            <el-option
              v-for="item in organizationOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="关联用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户" style="width: 100%" clearable>
            <el-option
              v-for="item in userOptions"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            />
          </el-select>
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

    <!-- 调度员详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="调度员详情"
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
          <span class="label">组织：</span>
          <span class="value">{{ getOrganizationName(detailData.organizationId) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">关联用户：</span>
          <span class="value">{{ getUserName(detailData.userId) || '无' }}</span>
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

    <!-- 子系统配置对话框 -->
    <el-dialog
      v-model="subsystemDialogVisible"
      :title="`${currentDispatcher.name} - 子系统配置`"
      width="800px"
      destroy-on-close
    >
      <div class="subsystem-config">
        <div class="operation-bar">
          <el-button type="primary" @click="handleAddSubsystems">添加子系统</el-button>
        </div>

        <el-table
          v-loading="subsystemLoading"
          :data="subsystemData"
          border
          style="width: 100%"
        >
          <el-table-column prop="name" label="子系统名称" width="150" />
          <el-table-column prop="code" label="子系统编码" width="150" />
          <el-table-column prop="type" label="类型" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
                {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button link type="danger" size="small" @click="handleRemoveSubsystem(scope.row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 添加子系统对话框 -->
      <el-dialog
        v-model="addSubsystemDialogVisible"
        title="添加子系统"
        width="800px"
        append-to-body
        destroy-on-close
      >
        <div class="search-form">
          <el-form :inline="true" :model="subsystemSearchForm" class="demo-form-inline">
            <el-form-item label="名称">
              <el-input v-model="subsystemSearchForm.name" placeholder="请输入名称" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="subsystemSearchForm.status" placeholder="请选择状态" clearable>
                <el-option label="启用" value="ACTIVE" />
                <el-option label="禁用" value="INACTIVE" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSubsystemSearch">查询</el-button>
              <el-button @click="resetSubsystemSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table
          v-loading="subsystemSearchLoading"
          :data="subsystemSearchData"
          border
          style="width: 100%"
          @selection-change="handleSubsystemSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="子系统名称" width="150" />
          <el-table-column prop="code" label="子系统编码" width="150" />
          <el-table-column prop="type" label="类型" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'ACTIVE' ? 'success' : 'danger'">
                {{ scope.row.status === 'ACTIVE' ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="addSubsystemDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmAddSubsystems">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import {
  getDispatcherList,
  createDispatcher,
  updateDispatcher,
  deleteDispatcher,
  updateDispatcherStatus,
  getDispatcherDetail,
  checkDispatcherCode,
  checkDispatcherCodeExcludeId,
  assignSubsystems,
  removeSubsystem,
  getAssignedSubsystemIds,
  DispatcherForm,
  DispatcherInfo,
  DispatcherQuery
} from '@/api/dispatcher'
import { getAllSubsystems } from '@/api/subsystem'
import { getAllUsers } from '@/api/user'

// 搜索表单
const searchForm = reactive<DispatcherQuery>({
  name: '',
  organizationId: '',
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
const form = reactive<DispatcherForm>({
  name: '',
  code: '',
  organizationId: '',
  userId: '',
  status: 'ONLINE'
})

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive<DispatcherInfo>({} as DispatcherInfo)

// 子系统配置对话框
const subsystemDialogVisible = ref(false)
const currentDispatcher = reactive<any>({})
const subsystemLoading = ref(false)
const subsystemData = ref<any[]>([])

// 添加子系统对话框
const addSubsystemDialogVisible = ref(false)
const subsystemSearchForm = reactive({
  name: '',
  status: ''
})
const subsystemSearchLoading = ref(false)
const subsystemSearchData = ref<any[]>([])
const selectedSubsystems = ref<any[]>([])

// 组织选项
const organizationOptions = ref<any[]>([])

// 用户选项
const userOptions = ref<any[]>([])

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  organizationId: [{ required: true, message: '请选择组织', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 验证编码是否重复
async function validateCode(rule: any, value: string, callback: any) {
  if (!value) {
    callback()
    return
  }

  try {
    const res = await (dialogType.value === 'add'
      ? checkDispatcherCode(value)
      : checkDispatcherCodeExcludeId(value, form.id as string))
    
    if (res.data) {
      callback(new Error('编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 获取组织名称
function getOrganizationName(id: string) {
  const org = organizationOptions.value.find(item => item.id === id)
  return org ? org.name : id
}

// 获取用户名称
function getUserName(id: string) {
  if (!id) return ''
  const user = userOptions.value.find(item => item.id === id)
  return user ? user.username : id
}

// 加载组织选项
async function loadOrganizationOptions() {
  try {
    // 这里需要调用获取组织列表的API
    // const res = await getAllOrganizations()
    // organizationOptions.value = res.data
    
    // 临时使用模拟数据
    organizationOptions.value = [
      { id: '1', name: '总公司' },
      { id: '2', name: '分公司A' },
      { id: '3', name: '分公司B' }
    ]
  } catch (error) {
    console.error('加载组织选项失败', error)
  }
}

// 加载用户选项
async function loadUserOptions() {
  try {
    const res = await getAllUsers()
    userOptions.value = res.data
  } catch (error) {
    console.error('加载用户选项失败', error)
  }
}

// 加载表格数据
async function loadTableData() {
  loading.value = true
  try {
    const res = await getDispatcherList({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    
    // 处理组织名称
    tableData.value = res.data.content.map((item: any) => ({
      ...item,
      organizationName: getOrganizationName(item.organizationId)
    }))
    
    pagination.total = res.data.totalElements
  } catch (error) {
    console.error('加载调度员列表失败', error)
    ElMessage.error('加载调度员列表失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
function handleSearch() {
  pagination.pageNum = 1
  loadTableData()
}

// 重置搜索
function resetSearch() {
  Object.keys(searchForm).forEach(key => {
    searchForm[key as keyof DispatcherQuery] = ''
  })
  handleSearch()
}

// 处理表格选择变化
function handleSelectionChange(rows: any[]) {
  selectedRows.value = rows
}

// 处理分页大小变化
function handleSizeChange(size: number) {
  pagination.pageSize = size
  loadTableData()
}

// 处理页码变化
function handleCurrentChange(page: number) {
  pagination.pageNum = page
  loadTableData()
}

// 处理新增
function handleAdd() {
  dialogType.value = 'add'
  Object.keys(form).forEach(key => {
    if (key !== 'status') {
      form[key as keyof DispatcherForm] = ''
    }
  })
  form.status = 'ONLINE'
  dialogVisible.value = true
}

// 处理编辑
function handleEdit(row: any) {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 处理详情
async function handleDetail(row: any) {
  try {
    const res = await getDispatcherDetail(row.id)
    Object.assign(detailData, res.data)
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取调度员详情失败', error)
    ElMessage.error('获取调度员详情失败')
  }
}

// 处理子系统配置
function handleSubsystems(row: any) {
  Object.assign(currentDispatcher, row)
  subsystemDialogVisible.value = true
  loadSubsystemData(row.id)
}

// 加载子系统数据
async function loadSubsystemData(dispatcherId: string) {
  subsystemLoading.value = true
  try {
    const res = await getAssignedSubsystemIds(dispatcherId)
    const subsystemIds = res.data
    
    if (subsystemIds.length === 0) {
      subsystemData.value = []
      subsystemLoading.value = false
      return
    }
    
    // 获取所有子系统
    const subsystemsRes = await getAllSubsystems()
    const allSubsystems = subsystemsRes.data
    
    // 过滤出已分配的子系统
    subsystemData.value = allSubsystems.filter((item: any) => subsystemIds.includes(item.id))
  } catch (error) {
    console.error('加载子系统数据失败', error)
    ElMessage.error('加载子系统数据失败')
  } finally {
    subsystemLoading.value = false
  }
}

// 处理添加子系统
function handleAddSubsystems() {
  addSubsystemDialogVisible.value = true
  subsystemSearchForm.name = ''
  subsystemSearchForm.status = ''
  loadSubsystemSearchData()
}

// 加载子系统搜索数据
async function loadSubsystemSearchData() {
  subsystemSearchLoading.value = true
  try {
    // 获取所有子系统
    const res = await getAllSubsystems()
    let data = res.data
    
    // 过滤已经分配的子系统
    const assignedIds = subsystemData.value.map(item => item.id)
    data = data.filter((item: any) => !assignedIds.includes(item.id))
    
    // 应用搜索条件
    if (subsystemSearchForm.name) {
      data = data.filter((item: any) => item.name.includes(subsystemSearchForm.name))
    }
    if (subsystemSearchForm.status) {
      data = data.filter((item: any) => item.status === subsystemSearchForm.status)
    }
    
    subsystemSearchData.value = data
  } catch (error) {
    console.error('加载子系统列表失败', error)
    ElMessage.error('加载子系统列表失败')
  } finally {
    subsystemSearchLoading.value = false
  }
}

// 处理子系统搜索
function handleSubsystemSearch() {
  loadSubsystemSearchData()
}

// 重置子系统搜索
function resetSubsystemSearch() {
  subsystemSearchForm.name = ''
  subsystemSearchForm.status = ''
  handleSubsystemSearch()
}

// 处理子系统选择变化
function handleSubsystemSelectionChange(rows: any[]) {
  selectedSubsystems.value = rows
}

// 确认添加子系统
async function confirmAddSubsystems() {
  if (selectedSubsystems.value.length === 0) {
    ElMessage.warning('请选择要添加的子系统')
    return
  }
  
  try {
    const subsystemIds = selectedSubsystems.value.map(row => row.id)
    await assignSubsystems(currentDispatcher.id, subsystemIds)
    ElMessage.success('添加成功')
    addSubsystemDialogVisible.value = false
    loadSubsystemData(currentDispatcher.id)
  } catch (error) {
    console.error('添加子系统失败', error)
    ElMessage.error('添加子系统失败')
  }
}

// 处理移除子系统
async function handleRemoveSubsystem(row: any) {
  try {
    await ElMessageBox.confirm('确定要移除该子系统吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await removeSubsystem(currentDispatcher.id, row.id)
    ElMessage.success('移除成功')
    loadSubsystemData(currentDispatcher.id)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('移除失败', error)
      ElMessage.error('移除失败')
    }
  }
}

// 处理状态变更
async function handleStatusChange(row: any) {
  const newStatus = row.status === 'ONLINE' ? 'OFFLINE' : 'ONLINE'
  const statusText = newStatus === 'ONLINE' ? '上线' : '下线'
  
  try {
    await ElMessageBox.confirm(`确定要${statusText}该调度员吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateDispatcherStatus(row.id, newStatus)
    ElMessage.success(`${statusText}成功`)
    loadTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${statusText}失败`, error)
      ElMessage.error(`${statusText}失败`)
    }
  }
}

// 处理删除
async function handleDelete(row: any) {
  try {
    await ElMessageBox.confirm('确定要删除该调度员吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteDispatcher(row.id)
    ElMessage.success('删除成功')
    loadTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
      ElMessage.error('删除失败')
    }
  }
}

// 处理批量删除
async function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的调度员')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个调度员吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const promises = selectedRows.value.map(row => deleteDispatcher(row.id))
    await Promise.all(promises)
    ElMessage.success('批量删除成功')
    loadTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 提交表单
async function submitForm() {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      try {
        if (dialogType.value === 'add') {
          await createDispatcher(form)
          ElMessage.success('创建成功')
        } else {
          await updateDispatcher(form.id as string, form)
          ElMessage.success('更新成功')
        }
        dialogVisible.value = false
        loadTableData()
      } catch (error) {
        console.error(dialogType.value === 'add' ? '创建失败' : '更新失败', error)
        ElMessage.error(dialogType.value === 'add' ? '创建失败' : '更新失败')
      }
    } else {
      console.log('表单验证失败', fields)
    }
  })
}

// 生命周期钩子
onMounted(() => {
  loadOrganizationOptions()
  loadUserOptions()
  loadTableData()
})
</script>

<style scoped>
.dispatcher-list {
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
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.detail-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.detail-item {
  width: calc(50% - 5px);
  display: flex;
  margin-bottom: 10px;
}

.detail-item .label {
  width: 100px;
  font-weight: bold;
  color: #606266;
}

.detail-item .value {
  flex: 1;
  word-break: break-all;
}

.subsystem-config {
  padding: 20px;
}
</style> 