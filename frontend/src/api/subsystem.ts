import request from '@/utils/request'

/**
 * 子系统查询参数
 */
export interface SubsystemQuery {
  name?: string
  type?: string
  status?: string
  pageNum?: number
  pageSize?: number
}

/**
 * 子系统表单数据
 */
export interface SubsystemForm {
  id?: string
  name: string
  code: string
  type: string
  connectionInfo?: string
  status?: string
}

/**
 * 子系统信息
 */
export interface SubsystemInfo {
  id: string
  name: string
  code: string
  type: string
  connectionInfo: string
  status: string
  createTime: string
  updateTime: string
}

/**
 * 获取子系统列表
 * @param params 查询参数
 * @returns 子系统列表
 */
export function getSubsystemList(params: SubsystemQuery) {
  return request.get<{
    list: SubsystemInfo[]
    total: number
  }>('/api/subsystems/page', { params })
}

/**
 * 获取所有子系统
 * @returns 子系统列表
 */
export function getAllSubsystems() {
  return request.get<SubsystemInfo[]>('/api/subsystems')
}

/**
 * 根据类型获取子系统
 * @param type 子系统类型
 * @returns 子系统列表
 */
export function getSubsystemsByType(type: string) {
  return request.get<SubsystemInfo[]>(`/api/subsystems/type/${type}`)
}

/**
 * 根据状态获取子系统
 * @param status 状态
 * @returns 子系统列表
 */
export function getSubsystemsByStatus(status: string) {
  return request.get<SubsystemInfo[]>(`/api/subsystems/status/${status}`)
}

/**
 * 获取子系统详情
 * @param id 子系统ID
 * @returns 子系统详情
 */
export function getSubsystemDetail(id: string) {
  return request.get<SubsystemInfo>(`/api/subsystems/${id}`)
}

/**
 * 创建子系统
 * @param data 子系统表单数据
 * @returns 创建结果
 */
export function createSubsystem(data: SubsystemForm) {
  return request.post<SubsystemInfo>('/api/subsystems', data)
}

/**
 * 更新子系统
 * @param id 子系统ID
 * @param data 子系统表单数据
 * @returns 更新结果
 */
export function updateSubsystem(id: string, data: SubsystemForm) {
  return request.put<SubsystemInfo>(`/api/subsystems/${id}`, data)
}

/**
 * 删除子系统
 * @param id 子系统ID
 * @returns 删除结果
 */
export function deleteSubsystem(id: string) {
  return request.delete<void>(`/api/subsystems/${id}`)
}

/**
 * 更新子系统状态
 * @param id 子系统ID
 * @param status 状态
 * @returns 更新结果
 */
export function updateSubsystemStatus(id: string, status: string) {
  return request.put<SubsystemInfo>(`/api/subsystems/${id}/status?status=${status}`)
}

/**
 * 检查子系统编码是否存在
 * @param code 子系统编码
 * @returns 是否存在
 */
export function checkSubsystemCode(code: string) {
  return request.get<boolean>('/api/subsystems/check-code', { params: { code } })
}

/**
 * 检查子系统编码是否存在（排除指定ID）
 * @param code 子系统编码
 * @param excludeId 排除的ID
 * @returns 是否存在
 */
export function checkSubsystemCodeExcludeId(code: string, excludeId: string) {
  return request.get<boolean>('/api/subsystems/check-code-exclude', { params: { code, excludeId } })
} 