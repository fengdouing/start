package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import shun.start_web_managenment.pojo.Emp;

import java.util.List;

/**
 * 员工Mapper
 */
@Mapper
public interface EmpMapper {
	@Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id or e.dept_id is null order by e.update_time desc limit #{start},#{pageSize}")
	public Long count();

	public List<Emp> list(Integer start, Integer pageSize);
}
