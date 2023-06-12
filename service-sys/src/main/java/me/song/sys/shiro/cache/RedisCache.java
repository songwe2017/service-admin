package me.song.sys.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Songwe
 * @since 2022/6/3 1:03
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisCache<K, V> implements Cache<K, V>, Serializable {
    
    private final String name;
    
    private final RedisTemplate redisTemplate;
   
    public RedisCache(String name, RedisTemplate redisTemplate) {
        this.name = name;
        this.redisTemplate = redisTemplate;
    }

    public V get(K key) throws CacheException{
        return (V) redisTemplate.opsForHash().get(name, key);
    }

    public V put(K key, V value) throws CacheException {
        redisTemplate.opsForHash().put(name, key, value);
        return null;
    }

    public V remove(K key) throws CacheException {
        redisTemplate.opsForHash().delete(name, key);
        return null;
    }

    public void clear() throws CacheException {
        redisTemplate.opsForHash().delete(name);
    }

    public int size() {
        return redisTemplate.opsForHash().size(name).intValue();
    }

    public Set<K> keys() {
        Set<K> keys = redisTemplate.opsForHash().keys(name);
        return !keys.isEmpty() ? Collections.unmodifiableSet(keys) : Collections.emptySet();
    }

    public Collection<V> values() {
        List<V> values = redisTemplate.opsForHash().values(name);
        return(!values.isEmpty() ? Collections.unmodifiableCollection(values) : Collections.emptyList());
    }

    @Override
    public String toString() {
        return "RedisCache{" +
                "name='" + name + '\'' +
                '}';
    }
}
