package shun.start_web_managenment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shun.start_web_managenment.interceptor.JwtInterceptor;

/**
 * 静态资源映射：把 /images/** 映射到本地上传目录，上传后的图片可直接用浏览器访问
 * 同时注册 JWT 登录拦截器和 CORS 配置
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + uploadDir.replace("\\", "/");
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/images/**").addResourceLocations(location);
        log.info("静态资源映射：/images/** -> {}", location);
    }

    /**
     * 前端请求 /api/**，但 nginx 的 rewrite ^/api/(.*)$ /$1 和 vite 的 rewrite 都会把 /api 去掉，
     * 后端实际收到的是 /emps、/login 这样的无前缀路径，所以这里按 /** 拦截、再排除白名单。
     * 放行：登录接口、静态资源、上传后的图片、错误页。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/error",
                        "/images/**",
                        "/static/**",
                        "/favicon.ico",
                        "/index.html"
                );
    }

    /**
     * 前后端不同端口时需要 CORS：必须允许 Authorization 头。
     * 这里用 allowedOriginPatterns + allowCredentials(false)，避免 "*" 与携带凭证混用。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(false)
                .maxAge(3600);
    }
}