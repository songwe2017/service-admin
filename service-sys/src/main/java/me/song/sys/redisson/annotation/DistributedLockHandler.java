package me.song.sys.redisson.annotation;


import lombok.extern.slf4j.Slf4j;
import me.song.sys.redisson.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;



/**
 * @Description: Redisson分布式锁注解解析器
 *
 * @author xub
 * @date 2019/6/20 下午9:34
 */
@Slf4j
@Aspect
public class DistributedLockHandler {

    private final RedisLock redisLock;

    public DistributedLockHandler(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    @Around("@annotation(distributedLock)")
    public void around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {
        log.info("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");
        //获取锁名称
        String lockName = distributedLock.value();
        //获取超时时间，默认10秒
        int leaseTime = distributedLock.leaseTime();
        redisLock.lock(lockName, leaseTime);
        try {
            log.info("获取Redis分布式锁[成功]，加锁完成");
            joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("获取Redis分布式锁[异常]，加锁失败", throwable);
            throwable.printStackTrace();
        } finally {
            //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
            if (redisLock.isHeldByCurrentThread(lockName)) {
                redisLock.unlock(lockName);
            }
        }
        log.info("释放Redis分布式锁[成功]，解锁完成");
    }
}
