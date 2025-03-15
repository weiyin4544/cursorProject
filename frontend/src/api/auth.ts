import request from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
  userInfo: {
    id: string
    username: string
    nickname: string
    avatar: string
    roles: string[]
  }
}

export const login = (data: LoginParams): Promise<LoginResult> => {
  return request.post('/auth/login', data)
}

export const logout = (): Promise<void> => {
  return request.post('/auth/logout')
}

export const getUserInfo = (): Promise<LoginResult['userInfo']> => {
  return request.get('/auth/user-info')
} 