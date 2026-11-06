package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import shun.start_web_managenment.pojo.Student;

import java.util.List;
import java.util.Map;

/**
 * 学员 Mapper
 */
@Mapper
public interface StudentMapper {

    /**
     * 条件查询学员列表（分页由 PageHelper 完成）
     */
    List<Student> list(@Param("name") String name,
                       @Param("degree") Integer degree,
                       @Param("clazzId") Integer clazzId);

    /**
     * 根据 ID 查询学员
     */
    Student getById(@Param("id") Integer id);

    /**
     * 新增学员
     */
    void insert(Student student);

    /**
     * 修改学员
     */
    void update(Student student);

    /**
     * 根据 ID 删除学员
     */
    void deleteById(@Param("id") Integer id);

    /**
     * 违纪处理：违纪次数 +1，违纪扣分 + 本次扣分
     */
    void updateViolation(@Param("id") Integer id, @Param("score") Short score);

    /**
     * 统计某班级下的学员数量，用于删除班级前的校验
     */
    int countByClazzId(@Param("clazzId") Integer clazzId);

    /**
     * 班级人数统计：每个班级有多少学员
     */
    List<Map<String, Object>> countStudentByClazz();

    /**
     * 学员学历统计：每个学历有多少人
     */
    List<Map<String, Object>> countStudentByDegree();
}