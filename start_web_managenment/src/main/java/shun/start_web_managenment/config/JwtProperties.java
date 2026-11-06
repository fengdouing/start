package shun.start_web_managenment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 相关配置，全部来自 application.yml 的 jwt.* 前缀，禁止硬编码在 Java 代码里
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** 签名密钥，HS256 要求 >= 32 字节 */
    private String secret;

    /** 过期时间，单位：秒 */
    private long expire;

    /** 携带 token 的请求头名称 */
    private String header = "Authorization";

    /** token 前缀，注意带一个空格 */
    private String prefix = "Bearer ";
}