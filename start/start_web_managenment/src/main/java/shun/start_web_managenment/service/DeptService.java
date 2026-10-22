package shun.start_web_managenment.service;

import shun.start_web_managenment.mapper.DeptMapper;
import shun.start_web_managenment.pojo.Dept;

import java.time.LocalDateTime;
import java.util.List;

public interface DeptService {
	/**
	 * 查询全部的部门数据
	 * @return
	 */
	 List<Dept> findAll();

	void delete(Integer id);

	void add(Dept dept);

	Dept findById(Integer id);

	void update(Dept dept);
}
