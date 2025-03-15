import request from '@/utils/request'

export interface UserQuery {
  page: number
  limit: number
  username?: string
  nickname?: string
  status?: string
}

export interface UserForm {
  id?: string
  username: string
  nickname: string
  password?: string
  email: string
  phone: string
  status: string
}

export interface UserInfo {
  id: string
  username: string
  nickname: string
  email: string
  phone: string
  status: string
  createTime: string
  updateTime: string
}

export interface UserListResult {
  data: UserInfo[]
  total: number
}

// 获取用户列表
export const getUserList = (params: UserQuery): Promise<UserListResult> => {
  return request.get('/user/list', params)
}

// 获取用户详情
export const getUserDetail = (id: string): Promise<UserInfo> => {
  return request.get(`/user/${id}`)
}

// 创建用户
export const createUser = (data: UserForm): Promise<void> => {
  return request.post('/user', data)
}

// 更新用户
export const updateUser = (id: string, data: UserForm): Promise<void> => {
  return request.put(`/user/${id}`, data)
}

// 删除用户
export const deleteUser = (id: string): Promise<void> => {
  return request.delete(`/user/${id}`)
} 