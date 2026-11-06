package shun.start_web_managenment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shun.start_web_managenment.pojo.LoginDTO;
import shun.start_web_managenment.pojo.LoginUser;
import shun.start_web_managenment.pojo.Result;
import shun.start_web_managenment.service.AuthService;
import shun.start_web_managenment.util.JwtUtil;
import shun.start_web_managenment.util.UserContext;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 登录接口。前端请求 /api/login，经 nginx/vite 去掉 /api 前缀后到达这里
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO) {
        LoginUser loginUser = authService.checkLogin(loginDTO.getUsername(), loginDTO.getPassword());
        if (loginUser == null) {
            // 不抛异常，统一返回错误结构，避免 500
            return Result.error("用户名或密码错误");
        }
        String token = jwtUtil.createToken(loginUser.getId(), loginUser.getUsername(), loginUser.getRole());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("token", token);
        data.put("userId", loginUser.getId());
        data.put("username", loginUser.getUsername());
        log.info("登录成功: username={}, userId={}", loginUser.getUsername(), loginUser.getId());
        return Result.success(data);
    }

    /**
     * 当前登录用户信息（受 JWT 拦截器保护，userId 从 token 里取）
     */
    @GetMapping("/me")
    public Result me() {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            return Result.error("未登录");
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userId", userId);
        data.put("username", UserContext.getUsername());
        data.put("role", UserContext.getRole());
        return Result.success(data);
    }
}