package me.song.sys.redisson.strategy.impl;


import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.constant.GlobalConstant;
import me.song.sys.redisson.property.RedisProperties;
import me.song.sys.redisson.strategy.RedisConfigService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;


/**
 * @Description: 哨兵集群部署Redis连接配置
 *
 * @author songwe
 * @date 2019/6/19 下午9:17
 */
@Slf4j
public class SentineConfigImpl implements RedisConfigService {



    @Override
    public Config createRedisConfig(RedisProperties redisProperties) {
        Config config = new Config();
        try {
            String address = redisProperties.getAddress();
            String password = redisProperties.getPassword();
            int database = redisProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String sentinelAliasName = addrTokens[0];
            //设置redis配置文件sentinel.conf配置的sentinel别名
            config.useSentinelServers().setMasterName(sentinelAliasName);
            config.useSentinelServers().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSentinelServers().setPassword(password);
            }
            //设置sentinel节点的服务IP和端口
            for (int i = 1; i < addrTokens.length; i++) {
                config.useSentinelServers().addSentinelAddress(GlobalConstant.REDIS_CONNECTION_PREFIX.getConstant_value() + addrTokens[i]);
            }
            log.info("初始化[哨兵部署]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("哨兵部署 Redisson init error", e);

        }
        return config;
    }
}
