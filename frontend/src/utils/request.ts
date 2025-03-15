import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${userStore.token}`
      }
    }
    return config
  },
  (error: any) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data

    // 如果没有code，说明是直接返回的数据
    if (code === undefined) {
      return response.data
    }

    // 根据code处理响应
    switch (code) {
      case 200:
        return data
      case 401:
        ElMessage.error('登录已过期，请重新登录')
        const userStore = useUserStore()
        userStore.clearUserInfo()
        router.push(`/login?redirect=${router.currentRoute.value.fullPath}`)
        return Promise.reject(new Error(message || '登录已过期'))
      case 403:
        ElMessage.error('没有权限访问该资源')
        return Promise.reject(new Error(message || '没有权限'))
      default:
        ElMessage.error(message || '请求失败')
        return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error: any) => {
    console.error('响应错误:', error)
    const message = error.response?.data?.message || error.message
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

// 封装请求方法
export const request = {
  get<T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.get(url, { params, ...config })
  },

  post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.post(url, data, config)
  },

  put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.put(url, data, config)
  },

  delete<T = any>(url: string, params?: any, config?: AxiosRequestConfig): Promise<T> {
    return service.delete(url, { params, ...config })
  }
}

export default request 