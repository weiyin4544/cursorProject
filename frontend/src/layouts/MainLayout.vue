<template>
  <el-container class="layout-container">
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '200px'">
      <el-menu
        :router="true"
        :default-active="route.path"
        class="el-menu-vertical"
        :collapse="appStore.sidebarCollapsed"
      >
        <el-menu-item
          v-for="route in routes"
          :key="route.path"
          :index="route.path"
        >
          <el-icon><component :is="route.meta.icon" /></el-icon>
          <template #title>{{ route.meta.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-left">
          <el-icon
            class="collapse-btn"
            @click="appStore.toggleSidebar"
          >
            <component :is="appStore.sidebarCollapsed ? 'Expand' : 'Fold'" />
          </el-icon>
          <h2>MGT系统</h2>
        </div>
        <div class="header-right">
          <el-switch
            v-model="theme"
            inline-prompt
            :active-icon="Sunny"
            :inactive-icon="Moon"
            @change="handleThemeChange"
          />
          <el-dropdown>
            <span class="user-info">
              <el-avatar :size="32" :icon="UserFilled" />
              <span>{{ userStore.userInfo.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleProfile">个人信息</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { computed, ref } from 'vue'
import { Sunny, Moon, UserFilled, Expand, Fold } from '@element-plus/icons-vue'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { logout } from '@/api/auth'

const router = useRouter()
const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const routes = computed(() => {
  return router.options.routes
    .find(route => route.path === '/' && route.children)
    ?.children?.filter(route => route.meta?.title) || []
})

const theme = ref(appStore.theme === 'dark')

const handleThemeChange = (value: boolean) => {
  appStore.setTheme(value ? 'dark' : 'light')
}

const handleProfile = () => {
  // TODO: 实现个人信息页面
  console.log('查看个人信息')
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logout()
      userStore.clearUserInfo()
      router.push('/login')
      ElMessage.success('退出登录成功')
    } catch (error) {
      console.error('退出登录失败:', error)
    }
  }).catch(() => {})
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;

  .el-aside {
    background-color: #304156;
    transition: width 0.3s;
    
    .el-menu {
      height: 100%;
      border-right: none;
      background-color: transparent;
    }
  }

  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #dcdfe6;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;

    .header-left {
      display: flex;
      align-items: center;

      .collapse-btn {
        font-size: 20px;
        cursor: pointer;
        margin-right: 16px;
        
        &:hover {
          color: var(--el-color-primary);
        }
      }

      h2 {
        margin: 0;
        color: #303133;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 16px;

      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;

        span {
          margin-left: 8px;
          color: #606266;
        }
      }
    }
  }

  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style> 