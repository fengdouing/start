package shun.start_web_managenment.service;

import shun.start_web_managenment.pojo.LoginUser;

/**
 * 登录认证业务
 */
public interface AuthService {

    /**
     * 校验用户名密码，成功返回登录用户信息，失败返回 null
     */
    LoginUser checkLogin(String username, String password);
}