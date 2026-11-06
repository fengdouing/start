package shun.start_web_managenment.pojo;

import lombok.Data;

/**
 * 系统用户表 user 的实体（建表 SQL 见 sql/schema.sql）
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String role;
    private Integer status;
}