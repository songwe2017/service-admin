package me.song.sys.shiro;

import lombok.extern.slf4j.Slf4j;
import me.song.common.constant.ShiroConstant;
import me.song.sys.redisson.RedissonManager;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author Songwe
 * @since 2022/6/3 1:42
 */
@Slf4j
//@Component
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    
    private final RedissonClient redisson;

    public RetryLimitCredentialsMatcher(RedissonManager redissonManager) {
        this.redisson = redissonManager.getRedisson();
        super.setHashAlgorithmName(ShiroConstant.HASH_ALGORITHM);
        super.setHashIterations(ShiroConstant.HASH_ITERATIONS);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String loginUser = (String) token.getPrincipal();
        RAtomicLong retryTimes = redisson.getAtomicLong(ShiroConstant.RETRY_lIMIT_CACHE + loginUser);
        if (retryTimes.getAndIncrement() >= ShiroConstant.MAX_RETRY_TIMES) {
            // 驳回登录请求
            throw new RuntimeException("密码错误超过3次，请30分钟后重试");
        }
        retryTimes.expire(30, TimeUnit.MINUTES);
        // 验证账户密码，如果登录成功，清除缓存
        if (super.doCredentialsMatch(token, info)) {
            retryTimes.delete();
            return true;
        }
        log.error("密码错误，登录用户：{}，重试次数：{}",loginUser, retryTimes);
        // 密码验证失败
        return false;
    }
}
    