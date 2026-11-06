<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { studentCountApi, studentDegreeApi } from '@/api/report'

const clazzData = ref([])
const degreeData = ref([])

const maxClazz = computed(() => Math.max(1, ...clazzData.value.map((i) => Number(i.num) || 0)))
const maxDegree = computed(() => Math.max(1, ...degreeData.value.map((i) => Number(i.value) || 0)))
const totalStu = computed(() => clazzData.value.reduce((s, i) => s + (Number(i.num) || 0), 0))

const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#9b59b6', '#16a085', '#d35400']

const loadData = async () => {
  const [c, d] = await Promise.all([studentCountApi(), studentDegreeApi()])
  if (c.code) clazzData.value = c.data || []
  else ElMessage.error(c.msg)
  if (d.code) degreeData.value = d.data || []
  else ElMessage.error(d.msg)
}

onMounted(loadData)
</script>

<template>
  <div class="report-page">
    <el-row :gutter="16">
      <el-col :span="13">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>班级人数统计</span>
              <el-tag type="info">共 {{ totalStu }} 名学员</el-tag>
            </div>
          </template>
          <div v-for="(item, idx) in clazzData" :key="idx" class="bar-row">
            <div class="bar-label" :title="item.clazzName">{{ item.clazzName }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: ((Number(item.num) || 0) / maxClazz) * 100 + '%', background: colors[idx % colors.length] }"></div>
            </div>
            <div class="bar-value">{{ item.num }} 人</div>
          </div>
          <el-empty v-if="clazzData.length === 0" description="暂无数据" />
        </el-card>
      </el-col>

      <el-col :span="11">
        <el-card shadow="hover">
          <template #header><span>学员学历统计</span></template>
          <div v-for="(item, idx) in degreeData" :key="idx" class="bar-row">
            <div class="bar-label">{{ item.name }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: ((Number(item.value) || 0) / maxDegree) * 100 + '%', background: colors[idx % colors.length] }"></div>
            </div>
            <div class="bar-value">{{ item.value }} 人</div>
          </div>
          <el-empty v-if="degreeData.length === 0" description="暂无数据" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.report-page { padding: 4px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.bar-row { display: flex; align-items: center; margin: 12px 0; }
.bar-label { width: 150px; font-size: 13px; color: #606266; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.bar-track { flex: 1; height: 16px; background: #f2f3f5; border-radius: 8px; overflow: hidden; margin: 0 10px; }
.bar-fill { height: 100%; border-radius: 8px; transition: width 0.4s; }
.bar-value { width: 60px; text-align: right; font-size: 13px; color: #303133; }
</style>