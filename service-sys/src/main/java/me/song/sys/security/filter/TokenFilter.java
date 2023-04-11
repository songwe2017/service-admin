package me.song.sys.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.song.common.util.R;
import me.song.common.util.ResponseUtils;
import me.song.sys.security.LoginUser;
import me.song.sys.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Songwe
 * @since 2022/12/28 15:06
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TokenFilter extends OncePerRequestFilter {

    private final RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            ResponseUtils.output(response, R.failed(HttpStatus.UNAUTHORIZED.value(), "Token not found"));
            return;
        }
        
        // 解析 token
        String userId;
        try {
            userId = TokenManager.parseIssuer(token);
        } catch (JWTDecodeException e) {
            throw new AuthenticationServiceException("Illegal token");
        }
        
        // 这里获取到 token 之后可以直接从 redis 取登录用户，前提是 login 方法认证完成后把 登录用户放到 redis 里
        // 也可以再注入一个 UserDetailService 然后用它来加载登录用户信息，让我们自定义的 UserDetailService 实现 
        // CachingUserDetailsService 接口以获取缓存能力，这里我使用前者
        LoginUser loginUser = (LoginUser)redisTemplate.opsForValue().get(userId);
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("User not login [Redis expired]");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        // 把认证信息存入上下文，授权时也要从上下文获取权限信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        
        filterChain.doFilter(request, response);
    }
}
