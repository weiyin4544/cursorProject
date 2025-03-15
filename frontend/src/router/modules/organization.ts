import { RouteRecordRaw } from 'vue-router'

const organizationRoutes: RouteRecordRaw = {
  path: '/organization',
  component: () => import('@/layouts/MainLayout.vue'),
  meta: {
    title: '组织管理',
    icon: 'Organization',
    order: 2
  },
  children: [
    {
      path: 'list',
      name: 'OrgList',
      component: () => import('@/views/organization/OrgList.vue'),
      meta: {
        title: '组织列表',
        icon: 'List',
        keepAlive: true
      }
    }
  ]
}

export default organizationRoutes 