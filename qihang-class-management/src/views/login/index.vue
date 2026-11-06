<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import { login } from '@/api/login'
  import { setToken, setUser } from '@/utils/auth'

  const router = useRouter()
  let loginForm = ref({username:'', password:''})
  // 请求期间按钮 loading，防止重复提交
  const loading = ref(false)

  const loginHandler = async () => {
    if (!loginForm.value.username || !loginForm.value.password) {
      ElMessage.error('用户名和密码不能为空')
      return
    }
    if (loading.value) {
      return
    }
    loading.value = true
    try {
      const result = await login(loginForm.value)
      if (result.code === 1) {
        // 存 token，后续请求由 request.js 统一带上
        setToken(result.data.token)
        setUser({ userId: result.data.userId, username: result.data.username })
        ElMessage.success('登录成功')
        router.push('/index')
      } else {
        ElMessage.error(result.msg || '登录失败')
      }
    } catch (e) {
      const msg = (e && e.response && e.response.data && e.response.data.msg) || '登录失败，请稍后重试'
      ElMessage.error(msg)
    } finally {
      loading.value = false
    }
  }

  // 重置按钮清空表单
  const resetHandler = () => {
    loginForm.value = { username: '', password: '' }
  }
</script>

<template>
  <div id="container">
    <div class="login-form">
      <el-form :model="loginForm" label-width="80px" @submit.prevent>
        <p class="title">启航的班级管理系统</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" @keyup.enter="loginHandler"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="button" type="primary" :loading="loading" @click="loginHandler">登 录</el-button>
          <el-button class="button" type="info" @click="resetHandler">重 置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
#container {
  padding: 10%;
  height: 410px;
  background-image: url('../../assets/bg1.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.login-form {
  max-width: 400px;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  background-color: white;
}

.title {
  font-size: 30px;
  font-family: '楷体';
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}
</style>