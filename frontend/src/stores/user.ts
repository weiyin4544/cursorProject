import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({
    id: '',
    username: '',
    nickname: '',
    avatar: '',
    roles: [] as string[]
  })

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info: typeof userInfo.value) => {
    userInfo.value = info
  }

  const clearUserInfo = () => {
    token.value = ''
    userInfo.value = {
      id: '',
      username: '',
      nickname: '',
      avatar: '',
      roles: []
    }
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    setToken,
    setUserInfo,
    clearUserInfo
  }
}) 