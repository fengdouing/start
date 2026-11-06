<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { getUser } from '@/utils/auth'

const router = useRouter()
const username = computed(() => (getUser() && getUser().username) || '管理员')

const modules = [
  { title: '班级管理', desc: '维护班级、教室、班主任和开结课状态', path: '/clazz', short: '班', color: '#409eff' },
  { title: '学员管理', desc: '管理学员资料、班级归属和违纪记录', path: '/stu', short: '学', color: '#67c23a' },
  { title: '部门管理', desc: '维护组织架构与部门基础信息', path: '/dept', short: '部', color: '#e6a23c' },
  { title: '员工管理', desc: '管理员工资料、职位与工作经历', path: '/emp', short: '员', color: '#909399' }
]
</script>

<template>
  <div class="dashboard">
    <section class="hero">
      <div>
        <p class="eyebrow">QIHANG CLASS MANAGEMENT</p>
        <h1>启航的班级管理系统</h1>
        <p class="subtitle">欢迎回来，{{ username }}。在这里统一管理班级、学员、部门和员工信息。</p>
      </div>
      <div class="hero-badge">启航</div>
    </section>

    <el-row :gutter="18">
      <el-col v-for="item in modules" :key="item.path" :xs="24" :sm="12" :lg="6">
        <el-card class="module-card" shadow="hover" @click="router.push(item.path)">
          <div class="module-icon" :style="{ backgroundColor: item.color + '18', color: item.color }">
            {{ item.short }}
          </div>
          <h3>{{ item.title }}</h3>
          <p>{{ item.desc }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard { padding: 4px; }
.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 220px;
  padding: 36px 44px;
  margin-bottom: 20px;
  color: #fff;
  border-radius: 18px;
  background: linear-gradient(120deg, #0f4c81 0%, #1b7f8c 58%, #36b37e 100%);
  box-shadow: 0 16px 40px rgba(15, 76, 129, 0.22);
}
.eyebrow { margin: 0 0 10px; font-size: 12px; letter-spacing: 3px; opacity: 0.78; }
.hero h1 { margin: 0; font-size: 38px; letter-spacing: 1px; }
.subtitle { max-width: 640px; margin: 16px 0 0; line-height: 1.8; opacity: 0.9; }
.hero-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 112px;
  height: 112px;
  flex: 0 0 112px;
  margin-left: 32px;
  border: 1px solid rgba(255, 255, 255, 0.55);
  border-radius: 50%;
  font-family: 楷体, serif;
  font-size: 28px;
  font-weight: 700;
  background: rgba(255, 255, 255, 0.12);
}
.module-card { min-height: 190px; cursor: pointer; border: none; border-radius: 14px; }
.module-card h3 { margin: 18px 0 8px; color: #303133; }
.module-card p { margin: 0; line-height: 1.7; color: #7a7f87; }
.module-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 54px;
  height: 54px;
  border-radius: 14px;
  font-size: 24px;
  font-weight: 700;
}
</style>