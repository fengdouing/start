package shun.start_web_managenment.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录成功后用于签发 token 的最小用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Integer id;
    private String username;
    private String role;
}