package shun.start_web_managenment.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import shun.start_web_managenment.pojo.User;

/**
 * 系统用户 Mapper（user 表不存在时由 AuthServiceImpl 回退到 emp 表）
 */
@Mapper
public interface UserMapper {

    //根据用户名查询用户
    User findByUsername(@Param("username") String username);
}