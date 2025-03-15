<template>
  <div class="dashboard">
    <h1>仪表盘</h1>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>用户总数</span>
              <el-icon><User /></el-icon>
            </div>
          </template>
          <div class="card-content">
            <span class="number">0</span>
            <span class="unit">人</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>组织总数</span>
              <el-icon><Office /></el-icon>
            </div>
          </template>
          <div class="card-content">
            <span class="number">0</span>
            <span class="unit">个</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>终端总数</span>
              <el-icon><Monitor /></el-icon>
            </div>
          </template>
          <div class="card-content">
            <span class="number">0</span>
            <span class="unit">台</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>群组总数</span>
              <el-icon><Collection /></el-icon>
            </div>
          </template>
          <div class="card-content">
            <span class="number">0</span>
            <span class="unit">个</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { User, Office, Monitor, Collection } from '@element-plus/icons-vue'
</script>

<style scoped lang="scss">
.dashboard {
  padding: 20px;

  .el-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-content {
    text-align: center;
    padding: 20px 0;

    .number {
      font-size: 36px;
      font-weight: bold;
      color: #409EFF;
      margin-right: 8px;
    }

    .unit {
      font-size: 14px;
      color: #909399;
    }
  }
}
</style> 