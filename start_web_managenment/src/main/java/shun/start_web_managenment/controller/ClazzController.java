package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import shun.start_web_managenment.pojo.Clazz;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.ClazzService;

import java.time.LocalDate;
import java.util.List;

/**
 * 班级管理
 */
@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 条件分页查询班级列表
     * GET /clazzs?name=&begin=&end=&page=1&pageSize=10
     */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String name,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("分页查询班级: page={}, pageSize={}, name={}, begin={}, end={}", page, pageSize, name, begin, end);
        PageResult<Clazz> pageResult = clazzService.page(page, pageSize, name, begin, end);
        return Result.success(pageResult);
    }

    /**
     * 查询全部班级（学员管理下拉框用）
     * GET /clazzs/list
     */
    @GetMapping("/list")
    public Result listAll() {
        log.info("查询全部班级");
        List<Clazz> list = clazzService.listAll();
        return Result.success(list);
    }

    /**
     * 根据ID查询班级
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询班级: {}", id);
        return Result.success(clazzService.getById(id));
    }

    /**
     * 新增班级
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 修改班级
     */
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("修改班级: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * 删除班级：班级下有学员时返回 code=0 + 提示信息
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除班级: {}", id);
        clazzService.deleteById(id);
        return Result.success();
    }
}