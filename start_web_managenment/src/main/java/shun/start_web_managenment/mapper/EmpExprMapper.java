package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import shun.start_web_managenment.pojo.EmpExpr;

import java.util.List;

/**
 * 员工工作经历
 */
@Mapper
public interface EmpExprMapper {

	void insertBatch(@Param("exprList") List<EmpExpr> exprList);
	//根据员工ID查询经历（按开始时间排序）
	List<EmpExpr> listByEmpId(@Param("empId") Integer empId);

	//根据员工ID删除经历
	void deleteByEmpId(@Param("empId") Integer empId);
}