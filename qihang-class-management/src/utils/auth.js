// 登录态存储工具：token 统一存 localStorage（关闭浏览器仍保留，用户点"退出登录"才清除）
const TOKEN_KEY = 'qihang_class_token'
const USER_KEY = 'qihang_class_user'

export const getToken = () => localStorage.getItem(TOKEN_KEY)
export const setToken = (token) => localStorage.setItem(TOKEN_KEY, token)

export const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export const setUser = (user) => localStorage.setItem(USER_KEY, JSON.stringify(user))

export const getUser = () => {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY) || 'null')
  } catch (e) {
    return null
  }
}

// 解析 JWT payload 的 exp 判断是否过期（不验签，只做前端体验优化，真正的校验在后端）
export const isTokenValid = () => {
  const token = getToken()
  if (!token) return false
  const parts = token.split('.')
  if (parts.length !== 3) return false
  try {
    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/')
    const json = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    const payload = JSON.parse(json)
    if (!payload.exp) return true
    // 留 5 秒余量，避免临界时刻请求到后端才过期
    return payload.exp * 1000 > Date.now() + 5000
  } catch (e) {
    return false
  }
}