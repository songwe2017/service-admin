package me.song.sys.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author songwe
 * @since 2019/6/19 下午10:26
 */
@Slf4j
public class RedisLock {
    
    private final RedissonClient redisson;

    public RedisLock(RedissonManager manager) {
        this.redisson = manager.getRedisson();
    }
    
    /**
     * 加锁操作 （设置锁的有效时间）
     * 如果获取不到锁，线程进入休眠状态直到获取锁
     *
     * @param lockName 锁名称
     * @param leaseTime  锁有效时间
     */
    public void lock(String lockName, long leaseTime) {
        RLock rLock = redisson.getLock(lockName);
        rLock.lock(leaseTime,TimeUnit.SECONDS);
    }

    /**
     * 加锁操作 (锁有效时间采用默认时间30秒）
     *
     * @param lockName 锁名称
     */
    public void lock(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        rLock.lock();
    }

    /**
     * 加锁操作(tryLock锁，没有等待时间）
     * 获取成功则返回true否则返回false
     *
     * @param lockName  锁名称
     * @param leaseTime 锁有效时间
     */
    public boolean tryLock(String lockName, long leaseTime) {
        RLock rLock = redisson.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock( leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 加锁操作(tryLock锁，有等待时间）
     * @param lockName   锁名称
     * @param leaseTime  锁有效时间
     * @param waitTime   等待时间
     */
    public  boolean tryLock(String lockName, long leaseTime,long waitTime) {

        RLock rLock = redisson.getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock( waitTime,leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     * @param lockName  锁名称
     */
    public void unlock(String lockName) {
        redisson.getLock(lockName).unlock();
    }

    /**
     * 判断该锁是否已经被线程持有
     * @param lockName  锁名称
     */
    public boolean isLock(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isLocked();
    }


    /**
     * 判断该线程是否持有当前锁
     * @param lockName  锁名称
     */
    public boolean isHeldByCurrentThread(String lockName) {
        RLock rLock = redisson.getLock(lockName);
        return rLock.isHeldByCurrentThread();
    }
}
