package shun.start_web_managenment.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shun.start_web_managenment.mapper.EmpExprMapper;
import shun.start_web_managenment.mapper.EmpMapper;
import shun.start_web_managenment.pojo.Emp;
import shun.start_web_managenment.pojo.EmpExpr;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.service.EmpService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
	@Autowired
	private EmpMapper empMapper;
	@Autowired
	private EmpExprMapper empExprMapper;

	@Override
	public PageResult<Emp> page(Integer page, Integer pageSize, String name , Integer gender, LocalDate begin, LocalDate end) {
		//设置分页参数
		PageHelper.startPage(page, pageSize);

		//执行查询
		List<Emp> list = empMapper.list(name, gender, begin, end);

		//解析查询结果,封装
		Page<Emp>p = (Page<Emp>) list;
		return new PageResult<Emp>(p.getTotal(), p.getResult());
	}

	@Override
	public List<Emp> listAll() {
		return empMapper.listAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(Emp emp) {
		emp.setCreateTime(LocalDateTime.now());
		emp.setUpdateTime(LocalDateTime.now());
		empMapper.insert(emp);
		//新增工作经历信息
		List<EmpExpr> exprList = emp.getExprList();
		if(exprList != null && !exprList.isEmpty()){
			//遍历集合,设置员工ID
			exprList.forEach(expr ->
				expr.setEmpId(emp.getId()));
			empExprMapper.insertBatch(exprList);
		}
	}

	@Override
	public Emp getById(Integer id) {
		Emp emp = empMapper.getById(id);
		if (emp != null) {
			//查询该员工的全部工作经历
			emp.setExprList(empExprMapper.listByEmpId(id));
		}
		return emp;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(Emp emp) {
		emp.setUpdateTime(LocalDateTime.now());
		//1. 更新员工主表
		empMapper.update(emp);
		//2. 先删除该员工原有经历，再批量插入新经历
		empExprMapper.deleteByEmpId(emp.getId());
		List<EmpExpr> exprList = emp.getExprList();
		if (exprList != null && !exprList.isEmpty()) {
			exprList.forEach(expr -> expr.setEmpId(emp.getId()));
			empExprMapper.insertBatch(exprList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteById(Integer id) {
		//先删子表经历，再删主表员工
		empExprMapper.deleteByEmpId(id);
		empMapper.deleteById(id);
	}
}