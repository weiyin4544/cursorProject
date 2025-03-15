import request from '@/utils/request'

// 角色查询参数类型
export interface RoleQuery {
  page?: number
  limit?: number
  name?: string
  code?: string
  status?: string
}

// 角色表单类型
export interface RoleForm {
  id?: string
  name: string
  code: string
  description?: string
  status: string
}

// 角色信息类型
export interface RoleInfo extends RoleForm {
  id: string
  createTime: string
  updateTime?: string
}

// 获取角色列表
export function getRoleList(params: RoleQuery) {
  return request.get<{list: RoleInfo[], total: number}>('/api/roles', params)
}

// 获取所有角色
export function getAllRoles() {
  return request.get<RoleInfo[]>('/api/roles/all')
}

// 获取角色详情
export function getRoleDetail(id: string) {
  return request.get<RoleInfo>(`/api/roles/${id}`)
}

// 创建角色
export function createRole(data: RoleForm) {
  return request.post<RoleInfo>('/api/roles', data)
}

// 更新角色
export function updateRole(id: string, data: RoleForm) {
  return request.put<RoleInfo>(`/api/roles/${id}`, data)
}

// 删除角色
export function deleteRole(id: string) {
  return request.delete<void>(`/api/roles/${id}`)
}

// 更新角色状态
export function updateRoleStatus(id: string, status: string) {
  return request.put<void>(`/api/roles/${id}/status?status=${status}`)
}

// 分配权限
export function assignPermissions(roleId: string, permissionIds: string[]) {
  return request.post<void>(`/api/roles/${roleId}/permissions`, permissionIds)
}

// 获取角色权限
export function getRolePermissions(roleId: string) {
  return request.get<string[]>(`/api/roles/${roleId}/permissions`)
}

// 检查角色编码是否存在
export function checkRoleCode(code: string) {
  return request.get<boolean>('/api/roles/check-code', { code })
} 