package shun.start_web_managenment.pojo;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    private String username;
    private String password;
}