package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.pojo.Student;
import shun.start_web_managenment.service.StudentService;

/**
 * 学员管理
 */
@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 条件分页查询学员列表
     * GET /students?name=&degree=&clazzId=&page=1&pageSize=10
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name,
                       Integer degree,
                       Integer clazzId) {
        log.info("分页查询学员: page={}, pageSize={}, name={}, degree={}, clazzId={}",
                page, pageSize, name, degree, clazzId);
        PageResult<Student> pageResult = studentService.page(page, pageSize, name, degree, clazzId);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询学员
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询学员: {}", id);
        return Result.success(studentService.getById(id));
    }

    /**
     * 新增学员
     */
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("新增学员: {}", student);
        studentService.save(student);
        return Result.success();
    }

    /**
     * 修改学员
     */
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("修改学员: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * 删除学员
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除学员: {}", id);
        studentService.deleteById(id);
        return Result.success();
    }

    /**
     * 违纪处理：违纪次数 +1，违纪扣分 + 前端输入的分数
     * PUT /students/violation/1/5
     */
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id, @PathVariable Short score) {
        log.info("违纪处理: id={}, score={}", id, score);
        studentService.violation(id, score);
        return Result.success();
    }
}