<template>
  <div class="terminal-detail-container">
    <div class="header">
      <div class="title">终端详情</div>
      <div class="actions">
        <el-button @click="goBack">返回</el-button>
        <el-button type="primary" @click="handleEdit">编辑</el-button>
      </div>
    </div>

    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>基本信息</span>
        </div>
      </template>
      <div class="detail-content">
        <div class="detail-item">
          <span class="label">终端名称：</span>
          <span class="value">{{ terminalInfo.name }}</span>
        </div>
        <div class="detail-item">
          <span class="label">终端编码：</span>
          <span class="value">{{ terminalInfo.code }}</span>
        </div>
        <div class="detail-item">
          <span class="label">所属组织：</span>
          <span class="value">{{ organizationName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">所属子系统：</span>
          <span class="value">{{ subsystemName }}</span>
        </div>
        <div class="detail-item">
          <span class="label">使用人员：</span>
          <span class="value">{{ personName || '未分配' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">终端型号：</span>
          <span class="value">{{ terminalInfo.model || '无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">序列号：</span>
          <span class="value">{{ terminalInfo.serialNumber || '无' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">状态：</span>
          <span class="value">
            <el-tag :type="terminalInfo.status === 'ONLINE' ? 'success' : 'danger'">
              {{ terminalInfo.status === 'ONLINE' ? '在线' : '离线' }}
            </el-tag>
          </span>
        </div>
        <div class="detail-item">
          <span class="label">创建时间：</span>
          <span class="value">{{ terminalInfo.createTime }}</span>
        </div>
        <div class="detail-item">
          <span class="label">更新时间：</span>
          <span class="value">{{ terminalInfo.updateTime }}</span>
        </div>
      </div>
    </el-card>

    <el-card class="detail-card">
      <template #header>
        <div class="card-header">
          <span>操作记录</span>
        </div>
      </template>
      <div class="operation-log">
        <el-timeline>
          <el-timeline-item
            v-for="(activity, index) in activities"
            :key="index"
            :timestamp="activity.timestamp"
            :type="activity.type"
          >
            {{ activity.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTerminalDetail, TerminalInfo } from '@/api/terminal'
import { getOrganizationDetail } from '@/api/organization'
import { getSubsystemDetail } from '@/api/subsystem'
import { getPersonDetail } from '@/api/person'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const terminalInfo = reactive<TerminalInfo>({} as TerminalInfo)
const organizationName = ref('')
const subsystemName = ref('')
const personName = ref('')

// 模拟操作记录数据
const activities = [
  {
    content: '创建终端',
    timestamp: '2023-01-10 10:00:00',
    type: 'primary'
  },
  {
    content: '更新终端信息',
    timestamp: '2023-01-15 14:30:00',
    type: 'success'
  },
  {
    content: '分配给张三',
    timestamp: '2023-01-20 09:15:00',
    type: 'warning'
  },
  {
    content: '状态变更为离线',
    timestamp: '2023-02-01 16:45:00',
    type: 'danger'
  },
  {
    content: '状态变更为在线',
    timestamp: '2023-02-05 08:30:00',
    type: 'success'
  }
]

onMounted(async () => {
  const id = route.params.id as string
  if (id) {
    await fetchTerminalDetail(id)
  } else {
    ElMessage.error('终端ID不存在')
    goBack()
  }
})

// 获取终端详情
const fetchTerminalDetail = async (id: string) => {
  loading.value = true
  try {
    const res = await getTerminalDetail(id)
    if (res.data) {
      Object.assign(terminalInfo, res.data)
      
      // 获取关联信息
      if (terminalInfo.organizationId) {
        await fetchOrganizationName(terminalInfo.organizationId)
      }
      
      if (terminalInfo.subsystemId) {
        await fetchSubsystemName(terminalInfo.subsystemId)
      }
      
      if (terminalInfo.personId) {
        await fetchPersonName(terminalInfo.personId)
      }
    }
  } catch (error) {
    console.error('获取终端详情失败', error)
    ElMessage.error('获取终端详情失败')
  } finally {
    loading.value = false
  }
}

// 获取组织名称
const fetchOrganizationName = async (id: string) => {
  try {
    const res = await getOrganizationDetail(id)
    if (res.data) {
      organizationName.value = res.data.name
    }
  } catch (error) {
    console.error('获取组织信息失败', error)
    organizationName.value = '未知组织'
  }
}

// 获取子系统名称
const fetchSubsystemName = async (id: string) => {
  try {
    const res = await getSubsystemDetail(id)
    if (res.data) {
      subsystemName.value = res.data.name
    }
  } catch (error) {
    console.error('获取子系统信息失败', error)
    subsystemName.value = '未知子系统'
  }
}

// 获取人员名称
const fetchPersonName = async (id: string) => {
  try {
    const res = await getPersonDetail(id)
    if (res.data) {
      personName.value = res.data.name
    }
  } catch (error) {
    console.error('获取人员信息失败', error)
    personName.value = '未知人员'
  }
}

// 返回列表页
const goBack = () => {
  router.push('/terminal/list')
}

// 跳转到编辑页
const handleEdit = () => {
  router.push(`/terminal/edit/${terminalInfo.id}`)
}
</script>

<style scoped>
.terminal-detail-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  font-weight: bold;
}

.detail-content {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
}

.label {
  font-weight: bold;
  color: #606266;
  min-width: 100px;
}

.value {
  color: #303133;
}

.operation-log {
  padding: 10px;
}

@media (max-width: 768px) {
  .detail-content {
    grid-template-columns: 1fr;
  }
}
</style> 