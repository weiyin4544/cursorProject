import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: {
          title: '仪表盘',
          icon: 'Odometer'
        }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/user/UserList.vue'),
        meta: {
          title: '用户管理',
          icon: 'User'
        }
      },
      {
        path: 'user/role',
        name: 'UserRole',
        component: () => import('../views/user/UserRole.vue'),
        meta: {
          title: '用户角色',
          icon: 'UserFilled',
          hidden: true
        }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/role/RoleList.vue'),
        meta: {
          title: '角色管理',
          icon: 'Key'
        }
      },
      {
        path: 'permission',
        name: 'Permission',
        component: () => import('../views/permission/PermissionList.vue'),
        meta: {
          title: '权限管理',
          icon: 'Lock'
        }
      },
      {
        path: 'organization',
        name: 'Organization',
        component: () => import('../views/organization/OrgList.vue'),
        meta: {
          title: '组织管理',
          icon: 'Office'
        }
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('../views/department/DeptList.vue'),
        meta: {
          title: '部门管理',
          icon: 'Coordinate'
        }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('../views/employee/EmployeeList.vue'),
        meta: {
          title: '人员管理',
          icon: 'UserFilled'
        }
      },
      {
        path: 'employee/detail/:id',
        name: 'EmployeeDetail',
        component: () => import('../views/employee/EmployeeDetail.vue'),
        meta: {
          title: '员工详情',
          icon: 'UserFilled',
          hidden: true
        }
      },
      {
        path: 'employee/edit/:id',
        name: 'EmployeeEdit',
        component: () => import('../views/employee/EmployeeEdit.vue'),
        meta: {
          title: '编辑员工',
          icon: 'UserFilled',
          hidden: true
        }
      },
      {
        path: 'employee/add',
        name: 'EmployeeAdd',
        component: () => import('../views/employee/EmployeeEdit.vue'),
        meta: {
          title: '新增员工',
          icon: 'UserFilled',
          hidden: true
        }
      },
      {
        path: 'terminal',
        name: 'Terminal',
        component: () => import('../views/terminal/TerminalList.vue'),
        meta: {
          title: '终端管理',
          icon: 'Monitor'
        }
      },
      {
        path: 'group',
        name: 'Group',
        component: () => import('../views/group/GroupList.vue'),
        meta: {
          title: '群组管理',
          icon: 'Collection'
        }
      },
      {
        path: 'dispatcher',
        name: 'Dispatcher',
        component: () => import('../views/dispatcher/DispatcherList.vue'),
        meta: {
          title: '调度员管理',
          icon: 'Service'
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: {
      title: '404',
      requiresAuth: false
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = `${to.meta.title} - MGT系统` || 'MGT系统'
  
  // 判断是否需要登录权限
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      next({ name: 'Login', query: { redirect: to.fullPath } })
      return
    }
  }
  next()
})

export default router 