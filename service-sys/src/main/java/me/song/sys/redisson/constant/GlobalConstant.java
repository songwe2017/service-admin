package me.song.sys.redisson.constant;


import lombok.Getter;

/**
 * @author xub
 * @Description: 全局常量枚举 用来拼接完整的URL
 * @date 2019/6/19 下午9:09
 */
public enum GlobalConstant {

    REDIS_CONNECTION_PREFIX("redis://", "Redis地址配置前缀");

    @Getter
    private final String constant_value;
    @Getter
    private final String constant_desc;

    GlobalConstant(String constant_value, String constant_desc) {
        this.constant_value = constant_value;
        this.constant_desc = constant_desc;
    }
}
