package me.song.sys.shiro.config;

import me.song.sys.redisson.RedissonManager;
import me.song.sys.shiro.cache.ShiroCacheManager;
import me.song.sys.shiro.filter.GiveUpSaveRequestFilter;
import me.song.sys.shiro.filter.KickOutAccessControlFilter;
import me.song.sys.shiro.realm.UserRealm;
import me.song.sys.shiro.session.ShiroSessionDao;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Songwe
 * @since 2022/5/21 1:09
 */
@Configuration
public class ShiroConfig{

    public static final String COOKIE_NAME = "sys-service";

    @Bean
    public CacheManager cacheManager() {
        return new ShiroCacheManager();
    }
    
    @Bean
    public UserRealm defaultRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(true);
        userRealm.setAuthenticationCacheName("AuthenticationCache");
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName("AuthorizationCache");
        return userRealm;
    }
    
    @Bean
    public SessionDAO sessionDao() {
        return new ShiroSessionDao();
    }

    /**
     * session 管理器
     * 这里重新设定 cookie name 是为了解决两个系统同时登陆后会出现相互干扰的情况
     * eg: 在同一个域名下部署两套不同的项目，端口号不同
     *     两个系统的 cookie名称相同（shiro默认的cookie名为 JSESSIONID）相互覆盖
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.getSessionIdCookie().setName(COOKIE_NAME + "_shiroSessionCookie");
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionDAO(sessionDao());
        sessionManager.setCacheManager(cacheManager());
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // securityManager 通过 realm 验证用户信息，这个属性是必须的
        securityManager.setRealm(defaultRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*
     * 开启 Shiro的注解(如 @RequiresRoles,@RequiresPermissions),需借助 SpringAOP
     * 扫描使用 Shiro 注解的类,并在必要时进行安全逻辑验证,配置以下两个 bean
     * {@link DefaultAdvisorAutoProxyCreator}(可选)
     * AuthorizationAttributeSourceAdvisor
     * 即可实现此功能
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
    
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }
    
    @Bean
    public ShiroFilterFactoryBean shiroFilter(RedissonManager redissonManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filters = new HashMap<>();
        filters.put("kick_out", new KickOutAccessControlFilter(redissonManager.getRedisson(), sessionDao(), sessionManager()));
        filters.put("no_save_req", new GiveUpSaveRequestFilter());
        shiroFilterFactoryBean.setFilters(filters);
        
        Map<String, String> filterMap = new HashMap<>(16);
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/swagger-ui/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/img/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/login.html", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/logout", "logout");
        filterMap.put("/**", "kick_out, no_save_req, authc");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        // 设置登录地址，未登录时访问未授权站点也调转到这里
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        // 设置登录后未授权站点跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        return shiroFilterFactoryBean;
    }
}
