import request from '@/utils/request'

// 条件分页查询学员
export const queryPageApi = (params) => request.get('/students', { params })

// 根据ID查询学员
export const queryByIdApi = (id) => request.get(`/students/${id}`)

// 新增学员
export const addApi = (student) => request.post('/students', student)

// 修改学员
export const updateApi = (student) => request.put('/students', student)

// 删除学员
export const deleteByIdApi = (id) => request.delete(`/students/${id}`)

// 违纪处理：违纪次数+1，违纪扣分+score
export const violationApi = (id, score) => request.put(`/students/violation/${id}/${score}`)