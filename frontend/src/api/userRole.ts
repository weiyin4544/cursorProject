import request from '@/utils/request'
import type { RoleInfo } from './role'

// 用户角色关联信息类型
export interface UserRoleInfo {
  id: string
  userId: string
  roleId: string
  createTime: string
  roleName?: string
  roleCode?: string
}

// 分配用户角色
export function assignUserRoles(userId: string, roleIds: string[]) {
  return request.post<void>(`/api/users/${userId}/roles`, roleIds)
}

// 获取用户角色
export function getUserRoles(userId: string) {
  return request.get<RoleInfo[]>(`/api/users/${userId}/roles`)
}

// 获取角色用户
export function getRoleUsers(roleId: string) {
  return request.get<UserRoleInfo[]>(`/api/roles/${roleId}/users`)
}

// 检查用户是否有指定角色
export function hasRole(userId: string, roleId: string) {
  return request.get<boolean>(`/api/users/${userId}/roles/${roleId}`)
}

// 获取用户角色列表
export function getUserRoleList(userId: string) {
  return request.get<UserRoleInfo[]>(`/api/users/${userId}/role-list`)
} 