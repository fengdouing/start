package shun.start_web_managenment.service;

import shun.start_web_managenment.pojo.Clazz;
import shun.start_web_managenment.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface ClazzService {

    PageResult<Clazz> page(Integer page, Integer pageSize, String name, LocalDate begin, LocalDate end);

    List<Clazz> listAll();

    Clazz getById(Integer id);

    void save(Clazz clazz);

    void update(Clazz clazz);

    void deleteById(Integer id);
}