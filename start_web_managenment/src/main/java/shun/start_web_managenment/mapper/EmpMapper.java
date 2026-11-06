package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import shun.start_web_managenment.pojo.Emp;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工Mapper
 */
@Mapper
public interface EmpMapper {

	List<Emp> list(@Param("name") String name, @Param("gender") Integer gender, @Param("begin") LocalDate begin, @Param("end") LocalDate end);

	//查询全部员工（下拉框）
	@Select("select id, name, username, gender, job from emp order by id")
	List<Emp> listAll();

	@Options(useGeneratedKeys = true, keyProperty = "id")
	//开启自动获取主键值
	@Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time)" +"values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
	void insert(Emp emp);

	//根据ID查询员工
	Emp getById(Integer id);

	//更新员工
	void update(Emp emp);

	//根据ID删除员工
	void deleteById(Integer id);

	//根据用户名查询员工（登录用）
	Emp findByUsername(@Param("username") String username);
}