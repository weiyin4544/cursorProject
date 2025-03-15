import request from '@/utils/request'

/**
 * 电话号码表单数据
 */
export interface PhoneNumberForm {
  id?: string
  employeeId: string
  phoneNumber: string
  type?: string
  isPrimary?: boolean
}

/**
 * 电话号码信息
 */
export interface PhoneNumberInfo {
  id: string
  employeeId: string
  phoneNumber: string
  type: string
  isPrimary: boolean
  createTime: string
  updateTime: string
}

/**
 * 添加电话号码
 * @param data 电话号码表单数据
 * @returns 添加结果
 */
export function addPhoneNumber(data: PhoneNumberForm) {
  return request.post<PhoneNumberInfo>('/api/phone-numbers', data)
}

/**
 * 更新电话号码
 * @param id 电话号码ID
 * @param data 电话号码表单数据
 * @returns 更新结果
 */
export function updatePhoneNumber(id: string, data: PhoneNumberForm) {
  return request.put<PhoneNumberInfo>(`/api/phone-numbers/${id}`, data)
}

/**
 * 获取电话号码详情
 * @param id 电话号码ID
 * @returns 电话号码详情
 */
export function getPhoneNumberDetail(id: string) {
  return request.get<PhoneNumberInfo>(`/api/phone-numbers/${id}`)
}

/**
 * 根据员工ID获取电话号码列表
 * @param employeeId 员工ID
 * @returns 电话号码列表
 */
export function getPhoneNumbersByEmployeeId(employeeId: string) {
  return request.get<PhoneNumberInfo[]>(`/api/phone-numbers/employee/${employeeId}`)
}

/**
 * 根据员工ID获取主要电话号码
 * @param employeeId 员工ID
 * @returns 主要电话号码
 */
export function getPrimaryPhoneNumberByEmployeeId(employeeId: string) {
  return request.get<PhoneNumberInfo>(`/api/phone-numbers/employee/${employeeId}/primary`)
}

/**
 * 根据电话号码查询
 * @param phoneNumber 电话号码
 * @returns 电话号码列表
 */
export function getPhoneNumbersByNumber(phoneNumber: string) {
  return request.get<PhoneNumberInfo[]>('/api/phone-numbers/search', { params: { phoneNumber } })
}

/**
 * 删除电话号码
 * @param id 电话号码ID
 * @returns 删除结果
 */
export function deletePhoneNumber(id: string) {
  return request.delete<void>(`/api/phone-numbers/${id}`)
}

/**
 * 根据员工ID删除所有电话号码
 * @param employeeId 员工ID
 * @returns 删除结果
 */
export function deletePhoneNumbersByEmployeeId(employeeId: string) {
  return request.delete<void>(`/api/phone-numbers/employee/${employeeId}`)
}

/**
 * 设置主要电话号码
 * @param id 电话号码ID
 * @param employeeId 员工ID
 * @returns 设置结果
 */
export function setPrimaryPhoneNumber(id: string, employeeId: string) {
  return request.put<PhoneNumberInfo>(`/api/phone-numbers/${id}/primary?employeeId=${employeeId}`)
} 