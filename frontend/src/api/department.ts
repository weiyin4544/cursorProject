import request from '@/utils/request'

// 部门查询参数类型
export interface DeptQuery {
  page?: number
  limit?: number
  name?: string
  code?: string
  organizationId?: string
  parentId?: string
  status?: string
}

// 部门表单类型
export interface DeptForm {
  id?: string
  name: string
  code: string
  description?: string
  organizationId: string
  parentId?: string
  managerId?: string
  sort?: number
  status: string
}

// 部门信息类型
export interface DeptInfo extends DeptForm {
  id: string
  createTime: string
  updateTime: string
  children?: DeptInfo[]
  hasChildren?: boolean
  organizationName?: string
  parentName?: string
  managerName?: string
}

// 获取部门列表
export function getDeptList(params: DeptQuery) {
  return request.get<DeptInfo[]>('/api/departments', params)
}

// 获取部门树
export function getDeptTree() {
  return request.get<DeptInfo[]>('/api/departments/tree')
}

// 根据组织ID获取部门树
export function getDeptTreeByOrgId(organizationId: string) {
  return request.get<DeptInfo[]>(`/api/departments/tree/organization/${organizationId}`)
}

// 获取部门详情
export function getDeptDetail(id: string) {
  return request.get<DeptInfo>(`/api/departments/${id}`)
}

// 创建部门
export function createDept(data: DeptForm) {
  return request.post<void>('/api/departments', data)
}

// 更新部门
export function updateDept(id: string, data: DeptForm) {
  return request.put<void>(`/api/departments/${id}`, data)
}

// 删除部门
export function deleteDept(id: string) {
  return request.delete<void>(`/api/departments/${id}`)
}

// 更新部门状态
export function updateDeptStatus(id: string, status: string) {
  return request.put<void>(`/api/departments/${id}/status?status=${status}`)
}

// 检查部门编码是否存在
export function checkDeptCode(code: string) {
  return request.get<boolean>('/api/departments/check-code', { code })
}

// 检查部门编码是否存在（排除指定ID）
export function checkDeptCodeExcludeId(code: string, excludeId: string) {
  return request.get<boolean>(`/api/departments/check-code/${excludeId}`, { code })
} 