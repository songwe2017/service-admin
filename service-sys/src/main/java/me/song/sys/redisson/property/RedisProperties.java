package me.song.sys.redisson.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author songwe
 * @date 2019/6/19 下午9:35
 */
@Data
@ConfigurationProperties(prefix = "redis.lock")
public class RedisProperties {

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address = "127.0.0.1:6379";

    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type = "standalone";

    /**
     * redis 连接密码
     */
    private String password = "1234";

    /**
     * 选取那个数据库
     */
    private int database = 0;
}
