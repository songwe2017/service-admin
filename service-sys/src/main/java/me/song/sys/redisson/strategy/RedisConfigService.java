package me.song.sys.redisson.strategy;


import me.song.sys.redisson.property.RedisProperties;
import org.redisson.config.Config;

/**
 * Redis 策略接口
 * 
 * @author songwe
 * @date 2019/6/20 下午3:35
 */
public interface RedisConfigService {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redisProperties
     * @return Config
     */
    Config createRedisConfig(RedisProperties redisProperties);
}
