<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryPageApi, queryByIdApi, addApi, updateApi, deleteByIdApi, violationApi } from '@/api/stu'
import { queryAllApi as queryAllClazzApi } from '@/api/clazz'

const degreeOptions = [
  { value: 1, label: '初中' },
  { value: 2, label: '高中' },
  { value: 3, label: '大专' },
  { value: 4, label: '本科' },
  { value: 5, label: '硕士' },
  { value: 6, label: '博士' }
]
const degreeLabel = (v) => (degreeOptions.find((d) => d.value === v) || {}).label || ''

// ==================== 查询条件 ====================
const searchForm = reactive({ name: '', degree: null, clazzId: null })
const clazzList = ref([])

// ==================== 分页数据 ====================
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const loading = ref(false)

const fmt = (t) => (t ? String(t).replace('T', ' ') : '')

const loadData = async () => {
  loading.value = true
  try {
    const params = { page: page.value, pageSize: pageSize.value }
    if (searchForm.name) params.name = searchForm.name
    if (searchForm.degree) params.degree = searchForm.degree
    if (searchForm.clazzId) params.clazzId = searchForm.clazzId
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
const handleReset = () => { searchForm.name = ''; searchForm.degree = null; searchForm.clazzId = null; page.value = 1; loadData() }
const handleSizeChange = (val) => { pageSize.value = val; page.value = 1; loadData() }
const handleCurrentChange = (val) => { page.value = val; loadData() }

// ==================== 新增 / 修改 ====================
const dialogVisible = ref(false)
const formRef = ref()
const form = reactive({
  id: null, name: '', no: '', gender: 1, phone: '', idCard: '',
  isCollege: 1, address: '', degree: null, graduationDate: '', clazzId: null
})
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  isCollege: [{ required: true, message: '请选择是否来自院校', trigger: 'change' }],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }]
}

const resetForm = () => {
  Object.assign(form, {
    id: null, name: '', no: '', gender: 1, phone: '', idCard: '',
    isCollege: 1, address: '', degree: null, graduationDate: '', clazzId: null
  })
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
    const payload = { ...form }
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

// ==================== 违纪处理 ====================
const violationVisible = ref(false)
const violationForm = reactive({ id: null, name: '', score: 1 })

const openViolation = (row) => {
  violationForm.id = row.id
  violationForm.name = row.name
  violationForm.score = 1
  violationVisible.value = true
}

const submitViolation = async () => {
  if (!violationForm.score || violationForm.score < 1) {
    ElMessage.warning('请输入正确的扣分分数')
    return
  }
  const result = await violationApi(violationForm.id, violationForm.score)
  if (result.code) {
    ElMessage.success('违纪处理成功')
    violationVisible.value = false
    loadData()
  } else {
    ElMessage.error(result.msg)
  }
}

// ==================== 删除 ====================
const handleDelete = (id) => {
  ElMessageBox.confirm('您确认删除该学员吗?', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const result = await deleteByIdApi(id)
    if (result.code) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {})
}

onMounted(async () => {
  loadData()
  const r = await queryAllClazzApi()
  if (r.code) clazzList.value = r.data
})
</script>

<template>
  <div class="page">
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable style="width: 160px" />
      </el-form-item>
      <el-form-item label="学历">
        <el-select v-model="searchForm.degree" placeholder="请选择" clearable style="width: 140px">
          <el-option v-for="d in degreeOptions" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="班级">
        <el-select v-model="searchForm.clazzId" placeholder="请选择" clearable style="width: 180px">
          <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">清空</el-button>
      </el-form-item>
    </el-form>

    <el-button type="success" @click="openDialog()">+ 新增学员</el-button>

    <el-table :data="tableData" border stripe v-loading="loading" style="width: 100%; margin-top: 10px">
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="name" label="姓名" width="90" align="center" />
      <el-table-column prop="no" label="学号" width="120" align="center" />
      <el-table-column label="性别" width="70" align="center">
        <template #default="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" align="center" />
      <el-table-column label="学历" width="80" align="center">
        <template #default="{ row }">{{ degreeLabel(row.degree) }}</template>
      </el-table-column>
      <el-table-column prop="clazzName" label="班级" min-width="150" />
      <el-table-column prop="violationCount" label="违纪次数" width="90" align="center" />
      <el-table-column prop="violationScore" label="违纪扣分" width="90" align="center" />
      <el-table-column label="最后修改时间" width="170" align="center">
        <template #default="{ row }">{{ fmt(row.updateTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openDialog(row.id)">编辑</el-button>
          <el-button type="warning" size="small" @click="openViolation(row)">违纪</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="pager" background layout="total, sizes, prev, pager, next, jumper"
      :total="total" :page-size="pageSize" :current-page="page"
      :page-sizes="[10, 20, 50, 100]"
      @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <!-- 新增 / 编辑 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑学员' : '新增学员'" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="学号" prop="no">
          <el-input v-model="form.no" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="是否来自院校" prop="isCollege">
          <el-radio-group v-model="form.isCollege">
            <el-radio :label="1">是</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入联系地址" />
        </el-form-item>
        <el-form-item label="最高学历" prop="degree">
          <el-select v-model="form.degree" placeholder="请选择" style="width: 100%">
            <el-option v-for="d in degreeOptions" :key="d.value" :label="d.label" :value="d.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduationDate">
          <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="班级" prop="clazzId">
          <el-select v-model="form.clazzId" placeholder="请选择班级" style="width: 100%">
            <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </template>
    </el-dialog>

    <!-- 违纪处理 -->
    <el-dialog v-model="violationVisible" title="违纪处理" width="420px">
      <el-form :model="violationForm" label-width="90px">
        <el-form-item label="学员">
          <span>{{ violationForm.name }}</span>
        </el-form-item>
        <el-form-item label="扣分分数">
          <el-input-number v-model="violationForm.score" :min="1" :max="100" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="violationVisible = false">取消</el-button>
        <el-button type="primary" @click="submitViolation">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.page { padding: 4px; }
.search-form { margin-bottom: 6px; }
.pager { margin-top: 14px; justify-content: flex-end; }
</style>