package shun.start_web_managenment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shun.start_web_managenment.mapper.EmpMapper;
import shun.start_web_managenment.mapper.UserMapper;
import shun.start_web_managenment.pojo.Emp;
import shun.start_web_managenment.pojo.LoginUser;
import shun.start_web_managenment.pojo.User;
import shun.start_web_managenment.service.AuthService;

/**
 * 登录认证：优先查 user 表（BCrypt 密文），user 表还没建时回退到 emp 表，保证现有系统能正常登录
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmpMapper empMapper;

    @Override
    public LoginUser checkLogin(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return null;
        }
        // 1. 优先使用 user 表（执行 sql/init_user.sql 后生效，密码是 BCrypt 密文）
        try {
            User user = userMapper.findByUsername(username);
            if (user != null) {
                if (user.getStatus() != null && user.getStatus() == 0) {
                    log.warn("账号已被禁用: {}", username);
                    return null;
                }
                if (!matches(password, user.getPassword())) {
                    return null;
                }
                return new LoginUser(user.getId(), user.getUsername(), user.getRole());
            }
        } catch (Exception e) {
            // user 表不存在时不能直接 500，回退到 emp 表
            log.warn("查询 user 表失败（可能还没执行 sql/init_user.sql），回退到 emp 表：{}", e.getMessage());
        }
        // 2. 兼容存量：emp 表里已有账号（当前是明文密码）
        Emp emp = empMapper.findByUsername(username);
        if (emp == null) {
            return null;
        }
        if (!matches(password, emp.getPassword())) {
            return null;
        }
        String role = emp.getJob() == null ? "" : String.valueOf(emp.getJob());
        return new LoginUser(emp.getId(), emp.getUsername(), role);
    }

    /**
     * 密文用 BCrypt 比对；历史明文数据只做等值比较并告警（不偷偷改库）
     */
    private boolean matches(String raw, String stored) {
        if (!StringUtils.hasText(stored)) {
            return false;
        }
        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return ENCODER.matches(raw, stored);
        }
        log.warn("检测到明文密码，建议尽快迁移为 BCrypt 密文");
        return stored.equals(raw);
    }
}