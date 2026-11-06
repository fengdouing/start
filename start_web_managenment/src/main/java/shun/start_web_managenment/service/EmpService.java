package shun.start_web_managenment.service;

import shun.start_web_managenment.pojo.Emp;
import shun.start_web_managenment.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface EmpService {
	PageResult<Emp> page(Integer page, Integer pageSize, String name, Integer gender, LocalDate begin, LocalDate end);

	List<Emp> listAll();

	void save(Emp emp);

	Emp getById(Integer id);

	void update(Emp emp);

	void deleteById(Integer id);
}