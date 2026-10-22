package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shun.start_web_managenment.pojo.Dept;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.DeptService;

import java.util.List;
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

	@Autowired
	private DeptService deptService;



	@RequestMapping
	public Result list(){
		log.info("查询全部的部门数据");
		List<Dept> deptList = deptService.findAll();
		return Result.success(deptList);
	}

	@DeleteMapping
	public Result delete( Integer id){
		deptService.delete(id);
		return Result.success();
	}

	//新增部门
	@PostMapping
	public Result add(@RequestBody Dept dept){
		deptService.add(dept);
		return Result.success();
	}
	//查询部门详情
	@GetMapping("/{id}")
	public Result detail(@PathVariable Integer id){
		Dept dept = deptService.findById(id);
		return Result.success(dept);
	}
	//更新部门
	@PutMapping
	public Result update(@RequestBody Dept dept){
		deptService.update(dept);
		return Result.success();
	}
}

