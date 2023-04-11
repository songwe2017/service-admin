package me.song.sys.redisson.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.RedisLock;
import me.song.sys.redisson.RedissonManager;
import me.song.sys.redisson.annotation.DistributedLockHandler;
import me.song.sys.redisson.property.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Songwe
 * @since 2023/3/29 15:28
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    private final RedisProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RedissonManager redissonManager() {
        RedissonManager redissonManager = new RedissonManager(properties);
        log.info("[RedisManager]装配完成，当前连接方式:" + properties.getType() + ",连接地址:" + properties.getAddress());
        return redissonManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisLock redisLock(RedissonManager redissonManager) {
        return new RedisLock(redissonManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public DistributedLockHandler distributedLockHandler(RedisLock redisLock) {
        return new DistributedLockHandler(redisLock);
    }
}
