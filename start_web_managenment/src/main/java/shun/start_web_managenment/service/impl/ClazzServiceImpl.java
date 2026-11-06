package shun.start_web_managenment.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shun.start_web_managenment.exception.BusinessException;
import shun.start_web_managenment.mapper.ClazzMapper;
import shun.start_web_managenment.mapper.StudentMapper;
import shun.start_web_managenment.pojo.Clazz;
import shun.start_web_managenment.pojo.PageResult;
import shun.start_web_managenment.service.ClazzService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Clazz> page(Integer page, Integer pageSize, String name, LocalDate begin, LocalDate end) {
        PageHelper.startPage(page, pageSize);
        List<Clazz> list = clazzMapper.list(name, begin, end);
        Page<Clazz> p = (Page<Clazz>) list;
        return new PageResult<Clazz>(p.getTotal(), p.getResult());
    }

    @Override
    public List<Clazz> listAll() {
        return clazzMapper.listAll();
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void save(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.insert(clazz);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    /**
     * 删除班级：班级下还有学员时不允许删除（需求文档要求）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        int count = studentMapper.countByClazzId(id);
        if (count > 0) {
            throw new BusinessException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteById(id);
    }
}