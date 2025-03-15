import request from '@/utils/request'

/**
 * 终端查询参数
 */
export interface TerminalQuery {
  name?: string
  organizationId?: string
  subsystemId?: string
  personId?: string
  status?: string
  pageNum?: number
  pageSize?: number
}

/**
 * 终端表单数据
 */
export interface TerminalForm {
  id?: string
  name: string
  code: string
  organizationId: string
  subsystemId: string
  personId?: string
  model?: string
  serialNumber?: string
  status?: string
}

/**
 * 终端信息
 */
export interface TerminalInfo {
  id: string
  name: string
  code: string
  organizationId: string
  subsystemId: string
  personId?: string
  model?: string
  serialNumber?: string
  status: string
  createTime: string
  updateTime: string
}

/**
 * 获取终端列表
 * @param params 查询参数
 * @returns 终端列表
 */
export function getTerminalList(params: TerminalQuery) {
  return request.get<{
    list: TerminalInfo[]
    total: number
  }>('/api/terminals/page', { params })
}

/**
 * 获取所有终端
 * @returns 终端列表
 */
export function getAllTerminals() {
  return request.get<TerminalInfo[]>('/api/terminals')
}

/**
 * 根据组织ID获取终端
 * @param organizationId 组织ID
 * @returns 终端列表
 */
export function getTerminalsByOrganizationId(organizationId: string) {
  return request.get<TerminalInfo[]>(`/api/terminals/organization/${organizationId}`)
}

/**
 * 根据子系统ID获取终端
 * @param subsystemId 子系统ID
 * @returns 终端列表
 */
export function getTerminalsBySubsystemId(subsystemId: string) {
  return request.get<TerminalInfo[]>(`/api/terminals/subsystem/${subsystemId}`)
}

/**
 * 根据人员ID获取终端
 * @param personId 人员ID
 * @returns 终端列表
 */
export function getTerminalsByPersonId(personId: string) {
  return request.get<TerminalInfo[]>(`/api/terminals/person/${personId}`)
}

/**
 * 根据状态获取终端
 * @param status 状态
 * @returns 终端列表
 */
export function getTerminalsByStatus(status: string) {
  return request.get<TerminalInfo[]>(`/api/terminals/status/${status}`)
}

/**
 * 获取终端详情
 * @param id 终端ID
 * @returns 终端详情
 */
export function getTerminalDetail(id: string) {
  return request.get<TerminalInfo>(`/api/terminals/${id}`)
}

/**
 * 创建终端
 * @param data 终端表单数据
 * @returns 创建结果
 */
export function createTerminal(data: TerminalForm) {
  return request.post<TerminalInfo>('/api/terminals', data)
}

/**
 * 更新终端
 * @param id 终端ID
 * @param data 终端表单数据
 * @returns 更新结果
 */
export function updateTerminal(id: string, data: TerminalForm) {
  return request.put<TerminalInfo>(`/api/terminals/${id}`, data)
}

/**
 * 删除终端
 * @param id 终端ID
 * @returns 删除结果
 */
export function deleteTerminal(id: string) {
  return request.delete<void>(`/api/terminals/${id}`)
}

/**
 * 更新终端状态
 * @param id 终端ID
 * @param status 状态
 * @returns 更新结果
 */
export function updateTerminalStatus(id: string, status: string) {
  return request.put<TerminalInfo>(`/api/terminals/${id}/status?status=${status}`)
}

/**
 * 分配终端给人员
 * @param id 终端ID
 * @param personId 人员ID
 * @returns 更新结果
 */
export function assignTerminalToPerson(id: string, personId: string) {
  return request.put<TerminalInfo>(`/api/terminals/${id}/assign?personId=${personId}`)
}

/**
 * 取消分配终端
 * @param id 终端ID
 * @returns 更新结果
 */
export function unassignTerminal(id: string) {
  return request.put<TerminalInfo>(`/api/terminals/${id}/unassign`)
}

/**
 * 检查终端编码是否存在
 * @param code 终端编码
 * @returns 是否存在
 */
export function checkTerminalCode(code: string) {
  return request.get<boolean>('/api/terminals/check-code', { params: { code } })
}

/**
 * 检查终端编码是否存在（排除指定ID）
 * @param code 终端编码
 * @param excludeId 排除的ID
 * @returns 是否存在
 */
export function checkTerminalCodeExcludeId(code: string, excludeId: string) {
  return request.get<boolean>('/api/terminals/check-code-exclude', { params: { code, excludeId } })
} 