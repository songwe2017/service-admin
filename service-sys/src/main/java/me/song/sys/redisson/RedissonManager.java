package me.song.sys.redisson;

import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.constant.RedisConnectionType;
import me.song.sys.redisson.property.RedisProperties;
import me.song.sys.redisson.strategy.RedisConfigService;
import me.song.sys.redisson.strategy.impl.ClusterConfigImpl;
import me.song.sys.redisson.strategy.impl.MasterslaveConfigImpl;
import me.song.sys.redisson.strategy.impl.SentineConfigImpl;
import me.song.sys.redisson.strategy.impl.StandaloneConfigImpl;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.util.Assert;


/**
 * @Description: Redisson核心配置，用于提供初始化的redisson实例
 *
 * @author xub
 * @date 2019/6/19 下午10:16
 */
@Slf4j
public class RedissonManager {
    
    private final RedissonClient redisson;

    public RedissonManager(RedisProperties redisProperties) {
        try {
            //通过不同部署方式获得不同cofig实体
            Config config = RedisConfigFactory.getInstance().createConfig(redisProperties);
            redisson = (Redisson) Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson init error", e);
            throw new IllegalArgumentException("please input correct configurations," +
                    "connectionType must in standalone/sentinel/cluster/master-slave");
        }
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedisConfigFactory {
        private static volatile RedisConfigFactory factory = null;

        public static RedisConfigFactory getInstance() {
            if (factory == null) {
                synchronized (RedisConfigFactory.class) {
                    if (factory == null) {
                        factory = new RedisConfigFactory();
                    }
                }
            }
            return factory;
        }

        /**
         * 根据连接类型获取对应连接方式的配置,基于策略模式
         *
         * @param redisProperties redis连接信息
         * @return Config
         */
        Config createConfig(RedisProperties redisProperties) {
            Assert.notNull(redisProperties.getAddress(), "redis.lock.server.address cannot be null!");
            Assert.notNull(redisProperties.getType(), "redis.lock.server.password cannot be null!");
            Assert.notNull(redisProperties.getDatabase(), "redis.lock.server.database cannot be null!");
            String connectionType = redisProperties.getType();
            //声明配置上下文
            RedisConfigService redissonConfigService = null;
            if (connectionType.equals(RedisConnectionType.STANDALONE.getConnection_type())) {
                redissonConfigService = new StandaloneConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.SENTINEL.getConnection_type())) {
                redissonConfigService = new SentineConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER.getConnection_type())) {
                redissonConfigService = new ClusterConfigImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE.getConnection_type())) {
                redissonConfigService = new MasterslaveConfigImpl();
            } else {
                throw new IllegalArgumentException("创建Redisson连接Config失败！当前连接方式:" + connectionType);
            }
            return redissonConfigService.createRedisConfig(redisProperties);
        }
    }
}


