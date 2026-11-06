package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import shun.start_web_managenment.pojo.Emp;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.EmpService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
	@Autowired
	private EmpService empService;

	@GetMapping
	public Result page(@RequestParam(defaultValue = "1") Integer page,
	                   @RequestParam(defaultValue = "10") Integer pageSize,
	                   String name,
	                   Integer gender,
	                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
	                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		log.info("分页查询员工数据: page={}, pageSize={}, name={}, gender={}, begin={}, end={}",
				page, pageSize, name, gender, begin, end);
		PageResult<Emp> pageResult = empService.page(page, pageSize, name, gender, begin, end);
		return Result.success(pageResult);
	}

	/**
	 * 查询全部员工（班级管理的班主任下拉框用）
	 * 注意：必须写在 /{id} 之前，Spring 会优先匹配字面量路径
	 */
	@GetMapping("/list")
	public Result listAll() {
		log.info("查询全部员工");
		List<Emp> list = empService.listAll();
		return Result.success(list);
	}

	@PostMapping
	public Result save(@RequestBody Emp emp){
		log.info("新增员工:{}",emp);
		empService.save(emp);
		return Result.success();
	}


	@GetMapping("/{id}")
	public Result getById(@PathVariable Integer id) {
		log.info("根据ID查询员工: {}", id);
		return Result.success(empService.getById(id));
	}

	@PutMapping
	public Result update(@RequestBody Emp emp) {
		log.info("更新员工: {}", emp);
		empService.update(emp);
		return Result.success();
	}

	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Integer id) {
		log.info("删除员工: {}", id);
		empService.deleteById(id);
		return Result.success();
	}
}