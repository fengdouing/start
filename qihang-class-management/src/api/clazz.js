import request from '@/utils/request'

// 条件分页查询班级
export const queryPageApi = (params) => request.get('/clazzs', { params })

// 查询全部班级（下拉框）
export const queryAllApi = () => request.get('/clazzs/list')

// 根据ID查询班级
export const queryByIdApi = (id) => request.get(`/clazzs/${id}`)

// 新增班级
export const addApi = (clazz) => request.post('/clazzs', clazz)

// 修改班级
export const updateApi = (clazz) => request.put('/clazzs', clazz)

// 删除班级
export const deleteByIdApi = (id) => request.delete(`/clazzs/${id}`)