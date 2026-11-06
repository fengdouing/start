import { createRouter, createWebHistory } from 'vue-router'

import IndexView from '@/views/index/index.vue'
import ClazzView from '@/views/clazz/index.vue'
import DeptView from '@/views/dept/index.vue'
import EmpView from '@/views/emp/index.vue'
import StuView from '@/views/stu/index.vue'
import LayoutView from '@/views/layout/index.vue'
import LoginView from '@/views/login/index.vue'
import { isTokenValid, clearToken } from '@/utils/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
     path: '/', 
     name: '',
     component: LayoutView,
     redirect: '/index', //重定向
     children: [
      {path: 'index', name: 'index', component: IndexView},
      {path: 'clazz', name: 'clazz', component: ClazzView},
      {path: 'stu', name: 'stu', component: StuView},
      {path: 'dept', name: 'dept', component: DeptView},
      {path: 'emp', name: 'emp', component: EmpView},
     ]
    },
    {path: '/login', name: 'login', component: LoginView}
  ]
})

// 路由守卫：进入业务页前先判断 token 是否存在且未过期，避免页面闪一下再跳登录
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    if (isTokenValid()) {
      next('/index')
    } else {
      next()
    }
    return
  }
  if (!isTokenValid()) {
    clearToken()
    next('/login')
    return
  }
  next()
})

export default router