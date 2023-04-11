package me.song.sys.shiro.cache;


import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Songwe
 * @since 2022/6/4 22:15
 */
@SuppressWarnings("rawtypes")
public class ShiroCacheManager extends AbstractCacheManager {
    
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Cache createCache(String name) throws CacheException {
        return new RedisCache(name, redisTemplate);
    }
}
