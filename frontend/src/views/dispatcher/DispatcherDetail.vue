<template>
  <div class="dispatcher-detail">
    <div class="page-header">
      <div class="header-left">
        <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
        <h2>调度员详情</h2>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="handleEdit">编辑</el-button>
      </div>
    </div>

    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
        </div>
      </template>
      <div class="detail-container">
        <div class="detail-item">
          <span class="label">名称：</span>
          <span class="value">{{ dispatcherInfo.name }}</span>
        </div>
        <div class="detail-item">
          <span class="label">编码：</span>
          <span class="value">{{ dispatcherInfo.code }}</span>
        </div>
        <div class="detail-item">
          <span class="label">组织：</span>
          <span class="value">{{ getOrganizationName(dispatcherInfo.organizationId) }}</span>
        </div>
        <div class="detail-item">
          <span class="label">关联用户：</span>
          <span class="value">{{ getUserName(dispatcherInfo.userId) || '无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">状态：</span>
          <span class="value">
            <el-tag :type="dispatcherInfo.status === 'ONLINE' ? 'success' : 'danger'">
              {{ dispatcherInfo.status === 'ONLINE' ? '在线' : '离线' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">创建时间：</span>
          <span class="value">{{ dispatcherInfo.createTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">更新时间：</span>
          <span class="value">{{ dispatcherInfo.updateTime }}</span>
        </div>
      </div>
    </el-card>

    <el-card class="subsystem-card">
      <template #header>
        <div class="card-header">
          <span>关联子系统</span>
          <el-button type="primary" size="small" @click="handleManageSubsystems">管理子系统</el-button>
        </div>
      </template>
      <div class="subsystem-container">
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
          <el-table-column prop="createTime" label="创建时间" width="180" />
        </el-table>
        <div v-if="subsystemData.length === 0" class="empty-data">
          <el-empty description="暂无关联子系统" />
        </div>
      </div>
    </el-card>

    <!-- 子系统配置对话框 -->
    <el-dialog
      v-model="subsystemDialogVisible"
      title="子系统配置"
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
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getDispatcherDetail,
  assignSubsystems,
  removeSubsystem,
  getAssignedSubsystemIds,
  DispatcherInfo
} from '@/api/dispatcher'
import { getAllSubsystems } from '@/api/subsystem'
import { getAllUsers } from '@/api/user'

const route = useRoute()
const router = useRouter()
const dispatcherId = route.params.id as string

// 调度员信息
const loading = ref(false)
const dispatcherInfo = reactive<DispatcherInfo>({} as DispatcherInfo)

// 子系统数据
const subsystemLoading = ref(false)
const subsystemData = ref<any[]>([])

// 子系统配置对话框
const subsystemDialogVisible = ref(false)

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

// 加载调度员详情
async function loadDispatcherDetail() {
  loading.value = true
  try {
    const res = await getDispatcherDetail(dispatcherId)
    Object.assign(dispatcherInfo, res.data)
  } catch (error) {
    console.error('加载调度员详情失败', error)
    ElMessage.error('加载调度员详情失败')
  } finally {
    loading.value = false
  }
}

// 加载子系统数据
async function loadSubsystemData() {
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

// 返回上一页
function goBack() {
  router.back()
}

// 处理编辑
function handleEdit() {
  router.push(`/dispatcher/edit/${dispatcherId}`)
}

// 处理管理子系统
function handleManageSubsystems() {
  subsystemDialogVisible.value = true
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
    await assignSubsystems(dispatcherId, subsystemIds)
    ElMessage.success('添加成功')
    addSubsystemDialogVisible.value = false
    loadSubsystemData()
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
    
    await removeSubsystem(dispatcherId, row.id)
    ElMessage.success('移除成功')
    loadSubsystemData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('移除失败', error)
      ElMessage.error('移除失败')
    }
  }
}

// 生命周期钩子
onMounted(() => {
  loadOrganizationOptions()
  loadUserOptions()
  loadDispatcherDetail()
  loadSubsystemData()
})
</script>

<style scoped>
.dispatcher-detail {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.subsystem-card {
  margin-bottom: 20px;
}

.subsystem-container {
  min-height: 200px;
}

.empty-data {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.search-form {
  margin-bottom: 20px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-start;
  gap: 10px;
}

.subsystem-config {
  padding: 20px;
}
</style> 