package me.song.sys.redisson.strategy.impl;


import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.constant.GlobalConstant;
import me.song.sys.redisson.property.RedisProperties;
import me.song.sys.redisson.strategy.RedisConfigService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @Description: 单机部署Redisson配置
 *
 * @author songwe
 * @date 2019/6/19 下午10:04
 */
@Slf4j
public class StandaloneConfigImpl implements RedisConfigService {

    @Override
    public Config createRedisConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String address = redisProperties.getAddress();
            String password = redisProperties.getPassword();
            int database = redisProperties.getDatabase();
            String redisAddr = GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + address;
            
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            //密码可以为空
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            log.info("初始化[单机部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("单机部署 Redisson init error", e);
        }
        return config;
    }
}
