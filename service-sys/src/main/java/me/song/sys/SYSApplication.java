package me.song.sys;

import lombok.extern.slf4j.Slf4j;
import me.song.common.config.MvcConfig;
import me.song.sys.redisson.config.RedissonConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@MapperScan(basePackages = "me.song.sys.*.mapper")
// 这里关闭 security 功能使它不进入登录界面
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"me.song"}, excludeFilters = {
//	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RedissonConfig.class}),
	// 这里排除自己创建的 security 包
	@ComponentScan.Filter(type = FilterType.REGEX, pattern = {"me.song.sys.security.*"})
})
public class SYSApplication {
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SYSApplication.class, args);
	}
}
