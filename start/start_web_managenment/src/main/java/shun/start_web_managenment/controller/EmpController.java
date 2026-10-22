package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shun.start_web_managenment.pojo.Emp;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.EmpService;

@Slf4j
@RestController
@RequestMapping("/emp")
public class EmpController {
	@Autowired
	private EmpService empService;

@GetMapping
	public Result page(Integer page, Integer pageSize){
	log.info("分页查询员工数据");
	Result<Emp> pageResult = empService.page(page, pageSize);

	return Result.success(pageResult);
}

}
