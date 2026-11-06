package shun.start_web_managenment.service;

import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.pojo.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {

    PageResult<Student> page(Integer page, Integer pageSize, String name, Integer degree, Integer clazzId);

    Student getById(Integer id);

    void save(Student student);

    void update(Student student);

    void deleteById(Integer id);

    /**
     * 违纪处理：次数 +1，扣分 + score
     */
    void violation(Integer id, Short score);

    List<Map<String, Object>> countStudentByClazz();

    List<Map<String, Object>> countStudentByDegree();
}