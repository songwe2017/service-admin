package me.song.sys.redisson.constant;

import lombok.Getter;

/**
 * Redis连接方式
 *
 * @author songwe
 * @date 2019/6/20 下午4:21
 */
public enum RedisConnectionType {

    STANDALONE("standalone", "单节点部署方式"),
    SENTINEL("sentinel", "哨兵部署方式"),
    CLUSTER("cluster", "集群方式"),
    MASTERSLAVE("master-slave", "主从部署方式");

    @Getter
    private final String connection_type;
    @Getter
    private final String connection_desc;

    RedisConnectionType(String connection_type, String connection_desc) {
        this.connection_type = connection_type;
        this.connection_desc = connection_desc;
    }
}
