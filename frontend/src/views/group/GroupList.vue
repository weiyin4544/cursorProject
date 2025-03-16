<template>
  <div class="group-list">
    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="派接组" value="PATCH" />
            <el-option label="原生组" value="NATIVE" />
            <el-option label="本地组" value="LOCAL" />
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
      <el-button type="primary" @click="handleAdd">新增群组</el-button>
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
      <el-table-column fixed="right" label="操作" width="280">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
          <el-button link type="primary" size="small" @click="handleMembers(scope.row)">成员管理</el-button>
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

    <!-- 群组表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增群组' : '编辑群组'"
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
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="派接组" value="PATCH" />
            <el-option label="原生组" value="NATIVE" />
            <el-option label="本地组" value="LOCAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
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

    <!-- 群组详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="群组详情"
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
          <span class="label">描述：</span>
          <div class="value">{{ detailData.description || '无' }}</div>
        </div>
      </div>
    </el-dialog>

    <!-- 成员管理对话框 -->
    <el-dialog
      v-model="memberDialogVisible"
      :title="getMemberDialogTitle()"
      width="800px"
      destroy-on-close
    >
      <div v-if="currentGroup.type === 'PATCH'">
        <patch-group-members :group-id="currentGroup.id" />
      </div>
      <div v-else-if="currentGroup.type === 'NATIVE'">
        <native-group-members :group-id="currentGroup.id" />
      </div>
      <div v-else-if="currentGroup.type === 'LOCAL'">
        <local-group-members :group-id="currentGroup.id" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus'
import {
  getGroupList,
  createGroup,
  updateGroup,
  deleteGroup,
  updateGroupStatus,
  getGroupDetail,
  checkGroupCode,
  checkGroupCodeExcludeId,
  GroupForm,
  GroupInfo,
  GroupQuery
} from '@/api/group'
import PatchGroupMembers from './components/PatchGroupMembers.vue'
import NativeGroupMembers from './components/NativeGroupMembers.vue'
import LocalGroupMembers from './components/LocalGroupMembers.vue'

// 搜索表单
const searchForm = reactive<GroupQuery>({
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
const form = reactive<GroupForm>({
  name: '',
  code: '',
  organizationId: '',
  type: 'PATCH',
  description: '',
  status: 'ACTIVE'
})

// 详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive<GroupInfo>({} as GroupInfo)

// 成员管理对话框
const memberDialogVisible = ref(false)
const currentGroup = reactive<GroupInfo>({} as GroupInfo)

// 组织选项
const organizationOptions = ref<any[]>([])

// 表单验证规则
const rules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  code: [
    { required: true, message: '请输入编码', trigger: 'blur' },
    { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' },
    { validator: validateCode, trigger: 'blur' }
  ],
  organizationId: [{ required: true, message: '请选择组织', trigger: 'change' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }],
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
      ? checkGroupCode(value)
      : checkGroupCodeExcludeId(value, form.id as string))
    
    if (res.data) {
      callback(new Error('编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 获取类型标签样式
function getTypeTag(type: string) {
  switch (type) {
    case 'PATCH':
      return 'success'
    case 'NATIVE':
      return 'primary'
    case 'LOCAL':
      return 'warning'
    default:
      return 'info'
  }
}

// 获取类型文本
function getTypeText(type: string) {
  switch (type) {
    case 'PATCH':
      return '派接组'
    case 'NATIVE':
      return '原生组'
    case 'LOCAL':
      return '本地组'
    default:
      return '未知'
  }
}

// 获取组织名称
function getOrganizationName(id: string) {
  const org = organizationOptions.value.find(item => item.id === id)
  return org ? org.name : id
}

// 获取成员管理对话框标题
function getMemberDialogTitle() {
  switch (currentGroup.type) {
    case 'PATCH':
      return `派接组成员管理 - ${currentGroup.name}`
    case 'NATIVE':
      return `原生组成员管理 - ${currentGroup.name}`
    case 'LOCAL':
      return `本地组成员管理 - ${currentGroup.name}`
    default:
      return '成员管理'
  }
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

// 加载表格数据
async function loadTableData() {
  loading.value = true
  try {
    const res = await getGroupList({
      ...searchForm,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = res.data.content
    pagination.total = res.data.totalElements
  } catch (error) {
    console.error('加载群组列表失败', error)
    ElMessage.error('加载群组列表失败')
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
    searchForm[key as keyof GroupQuery] = ''
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
    if (key !== 'type' && key !== 'status') {
      form[key as keyof GroupForm] = ''
    }
  })
  form.type = 'PATCH'
  form.status = 'ACTIVE'
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
    const res = await getGroupDetail(row.id)
    Object.assign(detailData, res.data)
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取群组详情失败', error)
    ElMessage.error('获取群组详情失败')
  }
}

// 处理成员管理
function handleMembers(row: any) {
  Object.assign(currentGroup, row)
  memberDialogVisible.value = true
}

// 处理状态变更
async function handleStatusChange(row: any) {
  const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const statusText = newStatus === 'ACTIVE' ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(`确定要${statusText}该群组吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateGroupStatus(row.id, newStatus)
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
    await ElMessageBox.confirm('确定要删除该群组吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteGroup(row.id)
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
    ElMessage.warning('请选择要删除的群组')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个群组吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const promises = selectedRows.value.map(row => deleteGroup(row.id))
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
          await createGroup(form)
          ElMessage.success('创建成功')
        } else {
          await updateGroup(form.id as string, form)
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
  loadTableData()
})
</script>

<style scoped>
.group-list {
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

.detail-item.full-width {
  width: 100%;
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
</style> 