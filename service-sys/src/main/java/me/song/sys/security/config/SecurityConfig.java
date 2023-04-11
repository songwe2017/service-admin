package me.song.sys.security.config;

import lombok.RequiredArgsConstructor;
import me.song.sys.security.filter.LoginAuthenticationFilter;
import me.song.sys.security.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Songwe
 * @since 2022/12/22 11:29
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RedisTemplate redisTemplate;

    /**
     * 密码加密算法，决定用户密码的加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 配置在这里不会经过过滤器链，配置在 http 里会经过过滤器链
        web.ignoring().antMatchers("/swagger-ui.html","/swagger-resources/**","/webjars/**","/v2/**","/api/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers().permitAll()
                .antMatchers("/ucservice/permission/**").hasAuthority("menu/list")
                .anyRequest().authenticated()
                .and()
                .addFilter(new LoginAuthenticationFilter(authenticationManager(), redisTemplate))
                .addFilterAfter(new TokenFilter(redisTemplate), LoginAuthenticationFilter.class)
                // 允许跨域
                .cors();

    }
}
