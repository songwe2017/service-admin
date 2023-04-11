package me.song.sys.shiro.filter;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Shrio 中未经身份验证的用户访问需要身份验证的页面，会预先将该请求地址存放到 session
 * 待登录成功后在重定向到该页面 {@link AuthenticationFilter#issueSuccessRedirect}
 * 但是 {@link SavedRequest} 类没有默认构造方法导致 redis 反序列化失败，这个类的作用
 * 是不在缓存预先访问的地址，重写 saveRequestAndRedirectToLogin 方法。
 *
 * @author Songwe
 * @since 2023/4/2 21:11
 */
public class GiveUpSaveRequestFilter extends FormAuthenticationFilter {

    @Override
    protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        redirectToLogin(request, response);
    }
}
