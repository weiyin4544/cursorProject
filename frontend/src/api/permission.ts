import request from '@/utils/request'

// 权限查询参数类型
export interface PermissionQuery {
  name?: string
  code?: string
  type?: string
  status?: string
}

// 权限表单类型
export interface PermissionForm {
  id?: string
  name: string
  code: string
  type: string
  parentId?: string
  path?: string
  component?: string
  redirect?: string
  icon?: string
  sort?: number
  hidden?: boolean
  keepAlive?: boolean
  status: string
}

// 权限信息类型
export interface PermissionInfo extends PermissionForm {
  id: string
  createTime: string
  updateTime?: string
  children?: PermissionInfo[]
  hasChildren?: boolean
}

// 获取权限列表
export function getPermissionList(params: PermissionQuery) {
  return request.get<PermissionInfo[]>('/api/permissions', params)
}

// 获取权限树
export function getPermissionTree() {
  return request.get<PermissionInfo[]>('/api/permissions/tree')
}

// 获取权限详情
export function getPermissionDetail(id: string) {
  return request.get<PermissionInfo>(`/api/permissions/${id}`)
}

// 创建权限
export function createPermission(data: PermissionForm) {
  return request.post<PermissionInfo>('/api/permissions', data)
}

// 更新权限
export function updatePermission(id: string, data: PermissionForm) {
  return request.put<PermissionInfo>(`/api/permissions/${id}`, data)
}

// 删除权限
export function deletePermission(id: string) {
  return request.delete<void>(`/api/permissions/${id}`)
}

// 更新权限状态
export function updatePermissionStatus(id: string, status: string) {
  return request.put<void>(`/api/permissions/${id}/status?status=${status}`)
}

// 根据角色ID获取权限
export function getPermissionsByRoleId(roleId: string) {
  return request.get<PermissionInfo[]>(`/api/permissions/role/${roleId}`)
}

// 根据用户ID获取权限
export function getPermissionsByUserId(userId: string) {
  return request.get<PermissionInfo[]>(`/api/permissions/user/${userId}`)
}

// 检查权限编码是否存在
export function checkPermissionCode(code: string) {
  return request.get<boolean>('/api/permissions/check-code', { code })
} 