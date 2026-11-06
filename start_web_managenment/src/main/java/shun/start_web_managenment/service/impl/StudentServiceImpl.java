package shun.start_web_managenment.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shun.start_web_managenment.mapper.StudentMapper;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.pojo.Student;
import shun.start_web_managenment.service.StudentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> page(Integer page, Integer pageSize, String name, Integer degree, Integer clazzId) {
        PageHelper.startPage(page, pageSize);
        List<Student> list = studentMapper.list(name, degree, clazzId);
        Page<Student> p = (Page<Student>) list;
        return new PageResult<Student>(p.getTotal(), p.getResult());
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    @Override
    public void save(Student student) {
        LocalDateTime now = LocalDateTime.now();
        student.setCreateTime(now);
        student.setUpdateTime(now);
        // 新增时违纪次数/扣分默认 0，避免前端不传导致数据库非空约束报错
        if (student.getViolationCount() == null) {
            student.setViolationCount((short) 0);
        }
        if (student.getViolationScore() == null) {
            student.setViolationScore((short) 0);
        }
        studentMapper.insert(student);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void violation(Integer id, Short score) {
        studentMapper.updateViolation(id, score == null ? (short) 0 : score);
    }

    @Override
    public List<Map<String, Object>> countStudentByClazz() {
        return studentMapper.countStudentByClazz();
    }

    @Override
    public List<Map<String, Object>> countStudentByDegree() {
        return studentMapper.countStudentByDegree();
    }
}