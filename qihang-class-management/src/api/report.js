import request from '@/utils/request'

// 班级人数统计
export const studentCountApi = () => request.get('/report/studentCountData')

// 学员学历统计
export const studentDegreeApi = () => request.get('/report/studentDegreeData')