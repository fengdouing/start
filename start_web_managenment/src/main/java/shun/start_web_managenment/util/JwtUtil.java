package shun.start_web_managenment.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import shun.start_web_managenment.config.JwtProperties;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类：签发、解析、校验
 */
@Component
public class JwtUtil {

    private final JwtProperties properties;
    private final SecretKey key;

    public JwtUtil(JwtProperties properties) {
        this.properties = properties;
        // 密钥长度不足 32 字节时 Keys.hmacShaKeyFor 会直接抛 WeakKeyException，启动即暴露配置问题
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签发 token：subject 放 userId，claims 放 username 和 role，并设置过期时间
     */
    public String createToken(Integer userId, String username, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + properties.getExpire() * 1000L))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析 token 并校验签名与有效期。
     * 过期会抛 ExpiredJwtException，格式/签名错误会抛 JwtException 的子类。
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 校验通过返回 true，失败直接抛出异常，由调用方区分异常类型给出不同提示
     */
    public boolean validate(String token) {
        parseToken(token);
        return true;
    }
}