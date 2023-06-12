package me.song.sys.shiro.utils;

import me.song.sys.system.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * Shiro 安全服务工具类
 *
 * @author Songwe
 * @since 2023/4/16 15:20
 */
public class ShiroSecurityUtils {

    /**
     * 获取当前登录用户ID
     * @return 当前登录用户id
     */
    public static Long getLoginUserId() {
        return getLoginUser().getId();
    }

    /**
     * 获取当前登录用户名
     * @return 当前登录用户名
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 是否管理员
     * @return boolean
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 获取当前登录用户
     * @return 当前登录用户
     */
    public static User getLoginUser() {
        return ((User) getSubject().getPrincipal());
    }

    /**
     * 是否经过认证
     * @return boolean
     */
    public static boolean isAuthenticated() {
        return getSubject().isAuthenticated();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}
