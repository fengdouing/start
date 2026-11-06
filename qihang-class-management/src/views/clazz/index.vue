<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryPageApi, queryByIdApi, addApi, updateApi, deleteByIdApi } from '@/api/clazz'
import { queryAllEmpApi } from '@/api/emp'

// ==================== 查询条件 ====================
const searchForm = reactive({ name: '', dateRange: [] })

// ==================== 分页数据 ====================
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const loading = ref(false)

const fmt = (t) => (t ? String(t).replace('T', ' ') : '')

const statusType = (status) => {
  if (status === '已结课') return 'warning'
  if (status === '未开班') return 'info'
  return 'success'
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: page.value, pageSize: pageSize.value }
    if (searchForm.name) params.name = searchForm.name
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.begin = searchForm.dateRange[0]
      params.end = searchForm.dateRange[1]
    }
    const result = await queryPageApi(params)
    if (result.code) {
      tableData.value = result.data.rows
      total.value = result.data.total
    } else {
      ElMessage.error(result.msg)
    }
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { page.value = 1; loadData() }
const handleReset = () => { searchForm.name = ''; searchForm.dateRange = []; page.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; page.value = 1; loadData() }
const handleCurrentChange = (val) => { page.value = val; loadData() }

// ==================== 新增 / 修改 ====================
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({ id: null, name: '', room: '', beginDate: '', endDate: '', masterId: null, subject: null })
const empList = ref([])
const subjectOptions = [
  { value: 1, label: 'Java' },
  { value: 2, label: '前端' },
  { value: 3, label: '大数据' },
  { value: 4, label: 'Python' },
  { value: 5, label: 'Go' },
  { value: 6, label: '嵌入式' }
]
const rules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  beginDate: [{ required: true, message: '请选择开课时间', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课时间', trigger: 'change' }],
  masterId: [{ required: true, message: '请选择班主任', trigger: 'change' }],
  subject: [{ required: true, message: '请选择学科', trigger: 'change' }]
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.room = ''
  form.beginDate = ''
  form.endDate = ''
  form.masterId = null
  form.subject = null
  if (formRef.value) formRef.value.clearValidate()
}

const openDialog = async (id) => {
  resetForm()
  if (id) {
    const result = await queryByIdApi(id)
    if (result.code) Object.assign(form, result.data)
    else ElMessage.error(result.msg)
  }
  dialogVisible.value = true
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    const payload = {
      id: form.id,
      name: form.name,
      room: form.room,
      beginDate: form.beginDate,
      endDate: form.endDate,
      masterId: form.masterId,
      subject: form.subject
    }
    const result = form.id ? await updateApi(payload) : await addApi(payload)
    if (result.code) {
      ElMessage.success(form.id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(result.msg)
    }
  })
}

// ==================== 删除 ====================
const handleDelete = (id) => {
  ElMessageBox.confirm('您确认删除该班级吗?', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteByIdApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      // 班级下有学员时，后端返回 code=0 + 提示信息
      ElMessage.error(result.msg)
    }
  }).catch(() => {})
}

onMounted(async () => {
  loadData()
  const r = await queryAllEmpApi()
  if (r.code) empList.value = r.data
})
</script>

<template>
  <div class="page">
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="班级名称">
        <el-input v-model="searchForm.name" placeholder="请输入班级名称" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="结课时间">
        <el-date-picker v-model="searchForm.dateRange" type="daterange" range-separator="至"
          start-placeholder="开始时间" end-placeholder="结束时间" value-format="YYYY-MM-DD" style="width: 260px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">清空</el-button>
      </el-form-item>
    </el-form>

    <el-button type="success" @click="openDialog()">+ 新增班级</el-button>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%; margin-top: 10px">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="name" label="班级名称" min-width="170" />
      <el-table-column prop="room" label="班级教室" width="110" align="center" />
      <el-table-column prop="masterName" label="班主任" width="110" align="center" />
      <el-table-column prop="beginDate" label="开课时间" width="120" align="center" />
      <el-table-column prop="endDate" label="结课时间" width="120" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最后修改时间" width="180" align="center">
        <template #default="{ row }">{{ fmt(row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openDialog(row.id)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="pager" background layout="total, sizes, prev, pager, next, jumper"
      :total="total" :page-size="pageSize" :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑班级' : '新增班级'" width="540px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="班级教室" prop="room">
          <el-input v-model="form.room" placeholder="请输入班级教室" />
        </el-form-item>
        <el-form-item label="开课时间" prop="beginDate">
          <el-date-picker v-model="form.beginDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结课时间" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="班主任" prop="masterId">
          <el-select v-model="form.masterId" placeholder="请选择班主任" style="width: 100%">
            <el-option v-for="e in empList" :key="e.id" :label="e.name" :value="e.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="学科" prop="subject">
          <el-select v-model="form.subject" placeholder="请选择学科" style="width: 100%">
            <el-option v-for="s in subjectOptions" :key="s.value" :label="s.label" :value="s.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page { padding: 4px; }
.search-form { margin-bottom: 6px; }
.pager { margin-top: 14px; justify-content: flex-end; }
</style>