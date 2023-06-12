package me.song.sys.shiro.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.song.common.constant.ShiroConstant;
import me.song.sys.system.model.User;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.redisson.api.RDeque;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 在线并发人数控制
 * @author Songwe
 * @since 2022/6/3 2:02
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KickOutAccessControlFilter extends AccessControlFilter {
    
    private final RedissonClient redisson;
    
    private final SessionDAO sessionDAO;
    
    private final SessionManager sessionManager;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated()) {
            return true;
        }
        User currentUser = (User) subject.getPrincipal();
        String sessionId = subject.getSession().getId().toString();

        RDeque<String> deque = redisson.getDeque(ShiroConstant.LOGIN_NUM_CACHE + currentUser.getUsername());
        // 判断当前 sessionId 是否存在此用户队列
        boolean contains = deque.contains(sessionId);
        if (!contains) {
            deque.addLast(sessionId);
        }
        
        if (deque.size() > ShiroConstant.MAX_LOGIN_NUM) {
            // 获取队列头部 sessionId
            String first = deque.getFirst();
            Session session = null;
            try {
                session = sessionManager.getSession(new DefaultSessionKey(first));
            } 
            catch (UnknownSessionException e) {
                log.warn("session已失效，sessionId：{}", first);
            }
            catch (ExpiredSessionException e) {
                log.warn("session已过期，sessionId：{}", first);
            }
            // 删除 session
            if (session != null) {
                log.debug("移除 session 信息，sessionId：{}", first);
                sessionDAO.delete(session);
            }
            // 移除队列头部 sessionId
            deque.removeFirst();
        }
        
        return true;
    }
}
