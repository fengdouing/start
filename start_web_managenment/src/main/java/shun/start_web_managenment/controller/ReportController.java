package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.StudentService;

import java.util.List;
import java.util.Map;

/**
 * 数据统计
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private StudentService studentService;

    /**
     * 班级人数统计：[{clazzName: 'JavaEE就业163期', num: 20}, ...]
     */
    @GetMapping("/studentCountData")
    public Result studentCountData() {
        log.info("班级人数统计");
        List<Map<String, Object>> list = studentService.countStudentByClazz();
        return Result.success(list);
    }

    /**
     * 学员学历统计：[{name: '本科', value: 12}, ...]
     */
    @GetMapping("/studentDegreeData")
    public Result studentDegreeData() {
        log.info("学员学历统计");
        List<Map<String, Object>> list = studentService.countStudentByDegree();
        return Result.success(list);
    }
}