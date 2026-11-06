package shun.start_web_managenment.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import shun.start_web_managenment.config.JwtProperties;
import shun.start_web_managenment.util.JwtUtil;
import shun.start_web_managenment.util.UserContext;

import java.io.IOException;

/**
 * 登录认证拦截器：没有引入 Spring Security，直接用 HandlerInterceptor 做 JWT 校验
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final JwtProperties properties;

    public JwtInterceptor(JwtUtil jwtUtil, JwtProperties properties) {
        this.jwtUtil = jwtUtil;
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // 1. 跨域预检请求直接放行，否则浏览器 OPTIONS 会全部 401
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        // 2. 取 Authorization 头，必须是 "Bearer xxx"
        String header = request.getHeader(properties.getHeader());
        if (!StringUtils.hasText(header) || !header.startsWith(properties.getPrefix())) {
            return writeUnauthorized(response, "未登录或缺少 token");
        }
        String token = header.substring(properties.getPrefix().length()).trim();
        if (!StringUtils.hasText(token)) {
            return writeUnauthorized(response, "未登录或缺少 token");
        }
        // 3. 校验并解析，三种失败情况分别给不同提示
        try {
            Claims claims = jwtUtil.parseToken(token);
            Integer userId = Integer.valueOf(claims.getSubject());
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);
            UserContext.set(userId, username, role);
            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("token 已过期: {}", e.getMessage());
            return writeUnauthorized(response, "token 已过期，请重新登录");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("token 校验失败: {}", e.getMessage());
            return writeUnauthorized(response, "token 无效或签名错误");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 必须清理，防止 Tomcat 线程复用导致用户信息串号
        UserContext.clear();
    }

    /**
     * 统一返回 401 JSON（结构与 Result 一致：code/msg/data），绝不 302 重定向到登录页。
     * 这里手工拼接 JSON，避免依赖具体版本的 Jackson。
     */
    private boolean writeUnauthorized(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = "{\"code\":0,\"msg\":\"" + msg + "\",\"data\":null}";
        response.getWriter().write(json);
        return false;
    }
}