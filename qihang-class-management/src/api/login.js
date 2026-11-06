import request from '@/utils/request'

// 登录（前端 /api/login，后端实际路径 /login）
export const login = (data) => request.post('/login', data)

// 获取当前登录用户信息（需要带 token，后端 /me）
export const getUserInfo = () => request.get('/me')