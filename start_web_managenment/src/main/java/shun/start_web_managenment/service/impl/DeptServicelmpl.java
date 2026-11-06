package shun.start_web_managenment.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shun.start_web_managenment.exception.BusinessException;
import shun.start_web_managenment.mapper.DeptMapper;
import shun.start_web_managenment.pojo.Dept;
import shun.start_web_managenment.service.DeptService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServicelmpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;



	@Override
	public List<Dept> findAll() {
		return deptMapper.findAll();
	}

	/**
	 * 删除部门：部门下还有员工时不允许删除（需求：功能完善）
	 */
	@Override
	public void delete(Integer id) {
		int count = deptMapper.countEmpByDeptId(id);
		if (count > 0) {
			throw new BusinessException("对不起，当前部门下有员工，不能直接删除！");
		}
		deptMapper.delete(id);
	}

	@Override
	public void add(Dept dept) {
		dept.setCreateTime(LocalDateTime.now());
		dept.setUpdateTime(LocalDateTime.now());
		deptMapper.insert(dept);
	}

	@Override
	public Dept findById(Integer id) {
		return deptMapper.select(id);
	}

	@Override
	public void update(Dept dept) {
		dept.setUpdateTime(LocalDateTime.now());
		dept.setCreateTime(LocalDateTime.now());
		deptMapper.update(dept);
	}
}