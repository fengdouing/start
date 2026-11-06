package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import shun.start_web_managenment.pojo.Clazz;

import java.time.LocalDate;
import java.util.List;

/**
 * 班级 Mapper
 */
@Mapper
public interface ClazzMapper {

    /**
     * 条件查询班级列表（分页由 PageHelper 完成）
     * status 由 SQL 根据当前时间和开课/结课时间计算
     */
    List<Clazz> list(@Param("name") String name,
                     @Param("begin") LocalDate begin,
                     @Param("end") LocalDate end);

    /**
     * 查询全部班级（学员管理的新增/编辑下拉框用）
     */
    List<Clazz> listAll();

    /**
     * 根据 ID 查询班级
     */
    Clazz getById(@Param("id") Integer id);

    /**
     * 新增班级
     */
    void insert(Clazz clazz);

    /**
     * 修改班级
     */
    void update(Clazz clazz);

    /**
     * 根据 ID 删除班级
     */
    void deleteById(@Param("id") Integer id);
}