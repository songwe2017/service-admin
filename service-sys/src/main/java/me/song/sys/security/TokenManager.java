package me.song.sys.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.collections.MapUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * token 的生成、验证、解析
 *
 * @author Songwe
 * @since 2022/6/3 3:18
 */
public class TokenManager {
    
    private static final Long TTL = 12*60*60*1000L;
    private static final String SECRET_KEY = "base64EncodeSecretKey";
    private static final UUID uuid = UUID.randomUUID();

    public static String getUUID() {
        return Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return SecretKey
     */
    private static String generalKey() {
        return Base64.getEncoder().encodeToString(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 1、header：
     * 密钥：用来对比
     * 算法：将 header 和 payload 加密成 signature
     * 2、payload
     * 存储很多东西，基本信息有如下几个
     * 签发人：当前令牌属于那个用户，一般是 userId
     * 签发时间
     * 失效时间：令牌的失效时间
     * 唯一标识 （jti =====> uuid）
     * @param issuer 签发人
     * @param claims jwt存储的一些非隐私信息
     * @return token
     */
    public static String issueToken(String issuer, Map<String, Object> claims) {
        if (MapUtils.isEmpty(claims)) {
            claims = new HashMap<>();
        }

        long now = System.currentTimeMillis();
        
        // 构建令牌
        return JWT.create()
                .withPayload(claims)
                .withJWTId(getUUID())
                .withIssuedAt(new Date(now))
                .withSubject(issuer)
                .withExpiresAt(new Date(now + TTL))
                .sign(Algorithm.HMAC256(generalKey()));
    }

    /**
     * 签发令牌
     * 
     * @param issuer 签发人
     * @return token
     */
    public static String issueToken(String issuer) {
        long now = System.currentTimeMillis();

        // 构建令牌
        return JWT.create()
                .withJWTId(getUUID())
                .withIssuedAt(new Date(now))
                .withSubject(issuer)
                .withIssuer(issuer)
                .withExpiresAt(new Date(now + TTL))
                .sign(Algorithm.HMAC256(generalKey()));
    }
    
    public static boolean verifyToken(String token) {
        // 加密签名
        JWT.require(Algorithm.HMAC256(generalKey()))
                .build()
                .verify(token);
        return true;
    }
    
    public static DecodedJWT parseToken(String token) throws JWTDecodeException {
        return JWT.decode(token);
    }
    

    public static String parseIssuer(String token) throws JWTDecodeException {
        return JWT.decode(token).getIssuer();
    }
    
}
