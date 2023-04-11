package me.song.sys.redisson.strategy.impl;


import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.constant.GlobalConstant;
import me.song.sys.redisson.property.RedisProperties;
import me.song.sys.redisson.strategy.RedisConfigService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;

/**
 * @Description: 集群方式Redisson部署
 *      地址格式：
 *          cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
 *          格式为: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384
 *
 * @author songwe
 * @date 2019/6/19 下午4:24
 */
@Slf4j
public class ClusterConfigImpl implements RedisConfigService {

    @Override
    public Config createRedisConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String address = redisProperties.getAddress();
            String password = redisProperties.getPassword();
            String[] addrTokens = address.split(",");
            //设置cluster节点的服务IP和端口
            for (int i = 0; i < addrTokens.length; i++) {
                config.useClusterServers()
                        .addNodeAddress(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
                if (StringUtils.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("初始化[集群部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("集群部署 Redisson init error", e);
            e.printStackTrace();
        }
        return config;
    }
}
