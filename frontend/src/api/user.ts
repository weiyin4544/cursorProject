import request from '@/utils/request'

// 用户查询参数类型
export interface UserQuery {
  page?: number
  limit?: number
  username?: string
  name?: string
  status?: string
}

// 用户表单类型
export interface UserForm {
  id?: string
  username: string
  name: string
  password?: string
  email?: string
  phone?: string
  status: string
}

// 用户信息类型
export interface UserInfo {
  id: string
  username: string
  name: string
  email: string
  phone: string
  status: string
  createTime: string
  updateTime?: string
}

// 获取用户列表
export function getUserList(params: UserQuery) {
  return request.get<{list: UserInfo[], total: number}>('/api/users', params)
}

// 获取用户详情
export function getUserDetail(id: string) {
  return request.get<UserInfo>(`/api/users/${id}`)
}

// 创建用户
export function createUser(data: UserForm) {
  return request.post<UserInfo>('/api/users', data)
}

// 更新用户
export function updateUser(id: string, data: UserForm) {
  return request.put<UserInfo>(`/api/users/${id}`, data)
}

// 删除用户
export function deleteUser(id: string) {
  return request.delete<void>(`/api/users/${id}`)
}

// 更新用户状态
export function updateUserStatus(id: string, status: string) {
  return request.put<void>(`/api/users/${id}/status?status=${status}`)
}

// 检查用户名是否存在
export function checkUsername(username: string) {
  return request.get<boolean>('/api/users/check-username', { username })
}

// 重置密码
export function resetPassword(id: string, password: string) {
  return request.put<void>(`/api/users/${id}/password`, { password })
} 