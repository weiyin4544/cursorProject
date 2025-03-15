import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export interface UserInfo {
  id: string
  username: string
  nickname: string
  avatar: string
  roles: string[]
}

export const useUserStore = defineStore('user', () => {
  const router = useRouter()
  const token = ref('')
  const userInfo = ref<UserInfo>({
    id: '',
    username: '',
    nickname: '',
    avatar: '',
    roles: []
  })

  // 设置 token
  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 设置用户信息
  function setUserInfo(info: UserInfo) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  // 清除用户信息
  function clearUserInfo() {
    token.value = ''
    userInfo.value = {
      id: '',
      username: '',
      nickname: '',
      avatar: '',
      roles: []
    }
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  // 退出登录
  function logout() {
    clearUserInfo()
    router.push('/login')
  }

  // 初始化用户信息
  function initUserInfo() {
    const storedToken = localStorage.getItem('token')
    const storedUserInfo = localStorage.getItem('userInfo')
    if (storedToken) {
      token.value = storedToken
    }
    if (storedUserInfo) {
      userInfo.value = JSON.parse(storedUserInfo)
    }
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    clearUserInfo,
    logout,
    initUserInfo
  }
}) 