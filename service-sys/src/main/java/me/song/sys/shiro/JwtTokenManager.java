//package me.song.sys.shiro;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.apache.commons.collections.MapUtils;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Songwe
// * @since 2022/6/3 3:18
// */
//public class JwtTokenManager {
//
//    private static final String JWT_SECRET_KEY = "base64EncodeSecretKey";
//
//    /**
//     * 1、header：
//     * 密钥：用来对比
//     * 算法：将 header 和 payload 加密成 signature
//     * 2、payload
//     * 存储很多东西，基本信息有如下几个
//     * 签发人：当前令牌属于那个用户，一般是 userId
//     * 创建时间
//     * 失效时间：session 的失效时间
//     * 唯一标识 （jti =====> sessionId）
//     * @param iss 签发人
//     * @param ttiMills 过期时间
//     * @param sessionId sessionId
//     * @param claims jwt存储的一些非隐私信息
//     * @return
//     */
//    public static String issueToken(String iss, Long ttiMills, String sessionId, Map<String, Object> claims) {
//        if (MapUtils.isEmpty(claims)) {
//            claims = new HashMap<>();
//        }
//
//        long now = System.currentTimeMillis();
//        // 加密签名
//        String signature = EncodeUtils.encodeBase64(JWT_SECRET_KEY.getBytes());
//
//        // 构建令牌
//        String sign = JWT.create()
//                .withPayload(claims)
//                .withJWTId(sessionId)
//                .withIssuedAt(new Date(now))
//                .withSubject(iss)
//                .withExpiresAt(new Date(now + ttiMills))
//                .sign(Algorithm.HMAC256(signature));
//        return sign;
//    }
//
//    public static boolean verifyToken(String token) {
//        // 加密签名
//        String signature = EncodeUtils.encodeBase64(JWT_SECRET_KEY.getBytes());
//        JWT.require(Algorithm.HMAC256(signature))
//                .build()
//                .verify(token);
//        return true;
//    }
//
//    public static DecodedJWT resolveToken(String token) {
//        return JWT.decode(token);
//    }
//}
