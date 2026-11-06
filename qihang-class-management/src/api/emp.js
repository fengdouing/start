import request from "@/utils/request";

//查询员工列表数据
export const queryPageApi = (name,gender,begin,end,page,pageSize) => 
  request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)

//新增
export const addApi = (emp) =>  request.post('/emps', emp);

//根据ID查询
export const queryByIdApi = (id) =>  request.get(`/emps/${id}`);

//修改
export const updateApi = (emp) =>  request.put('/emps', emp);

//删除
export const deleteByIdApi = (id) =>  request.delete(`/emps/${id}`);

// 查询全部员工（班主任下拉框）
export const queryAllEmpApi = () => request.get('/emps/list')
