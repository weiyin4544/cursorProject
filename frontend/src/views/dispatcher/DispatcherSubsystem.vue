<template>
  <div class="dispatcher-subsystem">
    <div class="page-header">
      <div class="header-left">
        <el-button icon="ArrowLeft" @click="goBack">返回</el-button>
        <h2>调度员子系统配置</h2>
      </div>
    </div>

    <el-card v-loading="loading" class="info-card">
      <div class="dispatcher-info">
        <h3>调度员信息</h3>
        <div class="info-item">
          <span class="label">名称：</span>
          <span>{{ dispatcherInfo.name }}</span>
        </div>
        <div class="info-item">
          <span class="label">编码：</span>
          <span>{{ dispatcherInfo.code }}</span>
        </div>
        <div class="info-item">
          <span class="label">组织：</span>
          <span>{{ dispatcherInfo.organizationName }}</span>
        </div>
        <div class="info-item">
          <span class="label">状态：</span>
          <el-tag :type="dispatcherInfo.status === 'ONLINE' ? 'success' : 'danger'">
            {{ dispatcherInfo.status === 'ONLINE' ? '在线' : '离线' }}
          </el-tag>
        </div>
      </div>
    </el-card>

    <el-card class="subsystem-card">
      <div class="operation-bar">
        <el-button type="primary" @click="openAddDialog">添加子系统</el-button>
        <el-button type="danger" :disabled="!selectedSubsystems.length" @click="batchRemove">
          批量移除
        </el-button>
      </div>

      <el-table
        v-loading="tableLoading"
        :data="subsystemList"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="子系统名称" min-width="150" />
        <el-table-column prop="code" label="子系统编码" min-width="120" />
        <el-table-column prop="type" label="类型" min-width="100">
          <template #default="{ row }">
            <el-tag>{{ getSubsystemTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ONLINE' ? 'success' : 'danger'">
              {{ row.status === 'ONLINE' ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link @click="removeSubsystem(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加子系统对话框 -->
    <el-dialog v-model="addDialogVisible" title="添加子系统" width="700px">
      <div class="dialog-content">
        <div class="search-form">
          <el-form :model="searchForm" inline>
            <el-form-item label="名称">
              <el-input v-model="searchForm.name" placeholder="请输入名称" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                <el-option label="在线" value="ONLINE" />
                <el-option label="离线" value="OFFLINE" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="searchAvailableSubsystems">搜索</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table
          v-loading="availableTableLoading"
          :data="availableSubsystemList"
          @selection-change="handleAvailableSelectionChange"
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="name" label="子系统名称" min-width="150" />
          <el-table-column prop="code" label="子系统编码" min-width="120" />
          <el-table-column prop="type" label="类型" min-width="100">
            <template #default="{ row }">
              <el-tag>{{ getSubsystemTypeName(row.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 'ONLINE' ? 'success' : 'danger'">
                {{ row.status === 'ONLINE' ? '在线' : '离线' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
            v-model:current-page="searchParams.pageNum"
            v-model:page-size="searchParams.pageSize"
            :total="availableTotal"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleAvailableSizeChange"
            @current-change="handleAvailableCurrentChange"
          />
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" :disabled="!selectedAvailableSubsystems.length" @click="addSubsystems">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDispatcherDetail } from '@/api/dispatcher'
import {
  getDispatcherSubsystems,
  getAvailableSubsystems,
  addDispatcherSubsystems,
  removeDispatcherSubsystem
} from '@/api/dispatcher'

const route = useRoute()
const router = useRouter()
const dispatcherId = route.params.id as string

// 调度员信息
const loading = ref(false)
const dispatcherInfo = ref<any>({
  name: '',
  code: '',
  organizationName: '',
  status: ''
})

// 子系统列表
const tableLoading = ref(false)
const subsystemList = ref<any[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10
})
const selectedSubsystems = ref<any[]>([])

// 可用子系统列表
const addDialogVisible = ref(false)
const availableTableLoading = ref(false)
const availableSubsystemList = ref<any[]>([])
const availableTotal = ref(0)
const searchParams = reactive({
  pageNum: 1,
  pageSize: 10
})
const searchForm = reactive({
  name: '',
  status: ''
})
const selectedAvailableSubsystems = ref<any[]>([])

// 获取子系统类型名称
function getSubsystemTypeName(type: string) {
  const typeMap: Record<string, string> = {
    NATIVE: '本地子系统',
    PATCH: '补丁子系统',
    LOCAL: '本地子系统'
  }
  return typeMap[type] || type
}

// 返回上一页
function goBack() {
  router.back()
}

// 加载调度员详情
async function loadDispatcherDetail() {
  loading.value = true
  try {
    const res = await getDispatcherDetail(dispatcherId)
    dispatcherInfo.value = res.data
  } catch (error) {
    console.error('加载调度员详情失败', error)
    ElMessage.error('加载调度员详情失败')
  } finally {
    loading.value = false
  }
}

// 加载调度员子系统列表
async function loadDispatcherSubsystems() {
  tableLoading.value = true
  try {
    const res = await getDispatcherSubsystems(dispatcherId, queryParams)
    subsystemList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    console.error('加载调度员子系统列表失败', error)
    ElMessage.error('加载调度员子系统列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 表格选择变化
function handleSelectionChange(selection: any[]) {
  selectedSubsystems.value = selection
}

// 分页大小变化
function handleSizeChange() {
  loadDispatcherSubsystems()
}

// 页码变化
function handleCurrentChange() {
  loadDispatcherSubsystems()
}

// 打开添加对话框
function openAddDialog() {
  addDialogVisible.value = true
  searchParams.pageNum = 1
  searchParams.pageSize = 10
  searchForm.name = ''
  searchForm.status = ''
  searchAvailableSubsystems()
}

// 搜索可用子系统
async function searchAvailableSubsystems() {
  availableTableLoading.value = true
  try {
    const params = {
      ...searchParams,
      ...searchForm,
      dispatcherId
    }
    const res = await getAvailableSubsystems(params)
    availableSubsystemList.value = res.data.list
    availableTotal.value = res.data.total
  } catch (error) {
    console.error('搜索可用子系统失败', error)
    ElMessage.error('搜索可用子系统失败')
  } finally {
    availableTableLoading.value = false
  }
}

// 重置搜索
function resetSearch() {
  searchForm.name = ''
  searchForm.status = ''
  searchAvailableSubsystems()
}

// 可用子系统表格选择变化
function handleAvailableSelectionChange(selection: any[]) {
  selectedAvailableSubsystems.value = selection
}

// 可用子系统分页大小变化
function handleAvailableSizeChange() {
  searchAvailableSubsystems()
}

// 可用子系统页码变化
function handleAvailableCurrentChange() {
  searchAvailableSubsystems()
}

// 添加子系统
async function addSubsystems() {
  if (selectedAvailableSubsystems.value.length === 0) {
    ElMessage.warning('请选择要添加的子系统')
    return
  }

  try {
    const subsystemIds = selectedAvailableSubsystems.value.map(item => item.id)
    await addDispatcherSubsystems(dispatcherId, subsystemIds)
    ElMessage.success('添加成功')
    addDialogVisible.value = false
    loadDispatcherSubsystems()
  } catch (error) {
    console.error('添加子系统失败', error)
    ElMessage.error('添加子系统失败')
  }
}

// 移除子系统
async function removeSubsystem(row: any) {
  ElMessageBox.confirm('确定要移除该子系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await removeDispatcherSubsystem(dispatcherId, row.id)
      ElMessage.success('移除成功')
      loadDispatcherSubsystems()
    } catch (error) {
      console.error('移除子系统失败', error)
      ElMessage.error('移除子系统失败')
    }
  }).catch(() => {})
}

// 批量移除子系统
async function batchRemove() {
  if (selectedSubsystems.value.length === 0) {
    ElMessage.warning('请选择要移除的子系统')
    return
  }

  ElMessageBox.confirm(`确定要移除选中的 ${selectedSubsystems.value.length} 个子系统吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const promises = selectedSubsystems.value.map(item => 
        removeDispatcherSubsystem(dispatcherId, item.id)
      )
      await Promise.all(promises)
      ElMessage.success('批量移除成功')
      loadDispatcherSubsystems()
    } catch (error) {
      console.error('批量移除子系统失败', error)
      ElMessage.error('批量移除子系统失败')
    }
  }).catch(() => {})
}

// 生命周期钩子
onMounted(() => {
  loadDispatcherDetail()
  loadDispatcherSubsystems()
})
</script>

<style scoped>
.dispatcher-subsystem {
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

.info-card {
  margin-bottom: 20px;
}

.dispatcher-info {
  padding: 10px;
}

.info-item {
  margin-bottom: 10px;
  display: flex;
  align-items: center;
}

.label {
  font-weight: bold;
  width: 80px;
}

.subsystem-card {
  margin-bottom: 20px;
}

.operation-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-content {
  max-height: 600px;
  overflow-y: auto;
}

.search-form {
  margin-bottom: 20px;
}
</style> 