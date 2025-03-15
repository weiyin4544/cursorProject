import request from '@/utils/request'

/**
 * 员工查询参数
 */
export interface EmployeeQuery {
  name?: string
  departmentId?: string
  organizationId?: string
  position?: string
  status?: string
  pageNum?: number
  pageSize?: number
}

/**
 * 员工表单数据
 */
export interface EmployeeForm {
  id?: string
  name: string
  gender?: string
  birthDate?: string
  idCard?: string
  email?: string
  address?: string
  departmentId?: string
  organizationId?: string
  position?: string
  hireDate?: string
  status?: string
}

/**
 * 员工信息
 */
export interface EmployeeInfo {
  id: string
  name: string
  gender?: string
  birthDate?: string
  idCard?: string
  email?: string
  address?: string
  departmentId?: string
  organizationId?: string
  position?: string
  hireDate?: string
  status: string
  createTime: string
  updateTime: string
}

/**
 * 获取员工列表
 * @param params 查询参数
 * @returns 员工列表
 */
export function getEmployeeList(params: EmployeeQuery) {
  return request.get<{
    list: EmployeeInfo[]
    total: number
  }>('/api/employees/page', { params })
}

/**
 * 获取员工详情
 * @param id 员工ID
 * @returns 员工详情
 */
export function getEmployeeDetail(id: string) {
  return request.get<EmployeeInfo>(`/api/employees/${id}`)
}

/**
 * 创建员工
 * @param data 员工表单数据
 * @returns 创建结果
 */
export function createEmployee(data: EmployeeForm) {
  return request.post<EmployeeInfo>('/api/employees', data)
}

/**
 * 更新员工
 * @param id 员工ID
 * @param data 员工表单数据
 * @returns 更新结果
 */
export function updateEmployee(id: string, data: EmployeeForm) {
  return request.put<EmployeeInfo>(`/api/employees/${id}`, data)
}

/**
 * 删除员工
 * @param id 员工ID
 * @returns 删除结果
 */
export function deleteEmployee(id: string) {
  return request.delete<void>(`/api/employees/${id}`)
}

/**
 * 更新员工状态
 * @param id 员工ID
 * @param status 状态
 * @returns 更新结果
 */
export function updateEmployeeStatus(id: string, status: string) {
  return request.put<EmployeeInfo>(`/api/employees/${id}/status?status=${status}`)
}

/**
 * 根据部门ID获取员工列表
 * @param departmentId 部门ID
 * @returns 员工列表
 */
export function getEmployeesByDepartmentId(departmentId: string) {
  return request.get<EmployeeInfo[]>(`/api/employees/department/${departmentId}`)
}

/**
 * 根据组织ID获取员工列表
 * @param organizationId 组织ID
 * @returns 员工列表
 */
export function getEmployeesByOrganizationId(organizationId: string) {
  return request.get<EmployeeInfo[]>(`/api/employees/organization/${organizationId}`)
} 