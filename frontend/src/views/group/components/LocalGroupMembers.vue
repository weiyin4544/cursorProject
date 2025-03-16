<template>
  <div class="local-group-members">
    <div class="operation-bar">
      <el-button type="primary" @click="handleAddMembers">添加调度员</el-button>
      <el-button type="danger" :disabled="!selectedRows.length" @click="handleBatchRemove">批量移除</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="调度员名称" width="150" />
      <el-table-column prop="code" label="调度员编码" width="150" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 'ONLINE' ? 'success' : 'danger'">
            {{ scope.row.status === 'ONLINE' ? '在线' : '离线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="添加时间" width="180" />
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button link type="danger" size="small" @click="handleRemove(scope.row)">移除</el-button>
          <el-button link type="primary" size="small" @click="handleDetail(scope.row)">详情</el-button>
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

    <!-- 添加调度员对话框 -->
    <el-dialog
      v-model="addMembersDialogVisible"
      title="添加调度员"
      width="800px"
      destroy-on-close
    >
      <div class="search-form">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
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
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="searchLoading"
        :data="searchTableData"
        border
        style="width: 100%"
        @selection-change="handleSearchSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="调度员名称" width="150" />
        <el-table-column prop="code" label="调度员编码" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'ONLINE' ? 'success' : 'danger'">
              {{ scope.row.status === 'ONLINE' ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="searchPagination.pageNum"
          v-model:page-size="searchPagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="searchPagination.total"
          @size-change="handleSearchSizeChange"
          @current-change="handleSearchCurrentChange"
        />
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addMembersDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAddMembers">确定</el-button>
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
          <span class="value">{{ detailData.userId || '无' }}</span>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, defineProps } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getDispatchersByLocalGroupId,
  addDispatcherToLocalGroup,
  removeDispatcherFromLocalGroup,
  batchAddDispatchersToLocalGroup
} from '@/api/group'
import { getAllDispatchers } from '@/api/dispatcher'

const props = defineProps({
  groupId: {
    type: String,
    required: true
  }
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

// 添加成员对话框
const addMembersDialogVisible = ref(false)
const searchForm = reactive({
  name: '',
  status: ''
})
const searchLoading = ref(false)
const searchTableData = ref<any[]>([])
const selectedSearchRows = ref<any[]>([])
const searchPagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 调度员详情对话框
const detailDialogVisible = ref(false)
const detailData = reactive<any>({})

// 组织选项
const organizationOptions = ref<any[]>([])

// 加载表格数据
async function loadTableData() {
  loading.value = true
  try {
    const res = await getDispatchersByLocalGroupId(props.groupId)
    tableData.value = res.data
    pagination.total = res.data.length
  } catch (error) {
    console.error('加载调度员列表失败', error)
    ElMessage.error('加载调度员列表失败')
  } finally {
    loading.value = false
  }
}

// 处理表格选择变化
function handleSelectionChange(rows: any[]) {
  selectedRows.value = rows
}

// 处理分页大小变化
function handleSizeChange(size: number) {
  pagination.pageSize = size
  // 由于是前端分页，不需要重新加载数据
}

// 处理页码变化
function handleCurrentChange(page: number) {
  pagination.pageNum = page
  // 由于是前端分页，不需要重新加载数据
}

// 处理添加成员
function handleAddMembers() {
  addMembersDialogVisible.value = true
  searchForm.name = ''
  searchForm.status = ''
  searchTableData.value = []
  selectedSearchRows.value = []
  searchPagination.pageNum = 1
  searchPagination.pageSize = 10
  searchPagination.total = 0
  loadSearchTableData()
}

// 加载搜索表格数据
async function loadSearchTableData() {
  searchLoading.value = true
  try {
    // 获取所有调度员
    const res = await getAllDispatchers()
    let data = res.data
    
    // 过滤已经添加的调度员
    const existingIds = tableData.value.map(item => item.id)
    data = data.filter((item: any) => !existingIds.includes(item.id))
    
    // 应用搜索条件
    if (searchForm.name) {
      data = data.filter((item: any) => item.name.includes(searchForm.name))
    }
    if (searchForm.status) {
      data = data.filter((item: any) => item.status === searchForm.status)
    }
    
    searchTableData.value = data
    searchPagination.total = data.length
  } catch (error) {
    console.error('加载调度员列表失败', error)
    ElMessage.error('加载调度员列表失败')
  } finally {
    searchLoading.value = false
  }
}

// 处理搜索
function handleSearch() {
  searchPagination.pageNum = 1
  loadSearchTableData()
}

// 重置搜索
function resetSearch() {
  searchForm.name = ''
  searchForm.status = ''
  handleSearch()
}

// 处理搜索表格选择变化
function handleSearchSelectionChange(rows: any[]) {
  selectedSearchRows.value = rows
}

// 处理搜索分页大小变化
function handleSearchSizeChange(size: number) {
  searchPagination.pageSize = size
  // 由于是前端分页，不需要重新加载数据
}

// 处理搜索页码变化
function handleSearchCurrentChange(page: number) {
  searchPagination.pageNum = page
  // 由于是前端分页，不需要重新加载数据
}

// 确认添加成员
async function confirmAddMembers() {
  if (selectedSearchRows.value.length === 0) {
    ElMessage.warning('请选择要添加的调度员')
    return
  }
  
  try {
    const dispatcherIds = selectedSearchRows.value.map(row => row.id)
    await batchAddDispatchersToLocalGroup(props.groupId, dispatcherIds)
    ElMessage.success('添加成功')
    addMembersDialogVisible.value = false
    loadTableData()
  } catch (error) {
    console.error('添加调度员失败', error)
    ElMessage.error('添加调度员失败')
  }
}

// 处理移除
async function handleRemove(row: any) {
  try {
    await ElMessageBox.confirm('确定要移除该调度员吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await removeDispatcherFromLocalGroup(props.groupId, row.id)
    ElMessage.success('移除成功')
    loadTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('移除失败', error)
      ElMessage.error('移除失败')
    }
  }
}

// 处理批量移除
async function handleBatchRemove() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要移除的调度员')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要移除选中的 ${selectedRows.value.length} 个调度员吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const promises = selectedRows.value.map(row => removeDispatcherFromLocalGroup(props.groupId, row.id))
    await Promise.all(promises)
    ElMessage.success('批量移除成功')
    loadTableData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量移除失败', error)
      ElMessage.error('批量移除失败')
    }
  }
}

// 处理详情
function handleDetail(row: any) {
  Object.assign(detailData, row)
  detailDialogVisible.value = true
}

// 获取组织名称
function getOrganizationName(id: string) {
  const org = organizationOptions.value.find(item => item.id === id)
  return org ? org.name : id
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

// 生命周期钩子
onMounted(() => {
  loadOrganizationOptions()
  loadTableData()
})
</script>

<style scoped>
.local-group-members {
  padding: 20px;
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

.search-form {
  margin-bottom: 20px;
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
</style> 