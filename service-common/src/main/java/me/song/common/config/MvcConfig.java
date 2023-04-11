package me.song.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Songwe
 * @since 2022/12/29 15:02
 */
//@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许跨域的路径
                //当客户端跨域并需要传递cookie时，需要设置Access-Control-Allow-Origin，并且值为不能为“*”，需要具体配置
                .allowedOriginPatterns("http://localhost:9528")
                .allowCredentials(true) // 是否允许 cookie
                //当客户端跨域并需要传递cookie时，需要设置Access-Control-Allow-Headers，并且值为不能为“*”，需要具体配置  代表允许上传的请求头字段
                .allowedHeaders("token,Content-Type,Content-Length, Authorization, Accept,X-Requested-With,domain,zdy")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS")
                .maxAge(3600); // 跨域允许时间
    }
}
