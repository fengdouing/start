import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getToken, clearToken } from '@/utils/auth'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 600000
})

//请求拦截器：每个请求自动带 Authorization: Bearer <token>
request.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => Promise.reject(error)
)

//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    const status = error.response && error.response.status
    if (status === 401) {
      clearToken()
      // 已经在登录页就不再跳转，避免 401 -> 跳登录 -> 再发请求 的死循环
      if (router.currentRoute.value.path !== '/login') {
        const msg = (error.response.data && error.response.data.msg) || '登录已失效，请重新登录'
        ElMessage.error(msg)
        router.push('/login')
      }
    }
    return Promise.reject(error)
  }
)

export default request