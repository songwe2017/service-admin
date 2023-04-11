package me.song.sys.shiro.session;

import lombok.extern.slf4j.Slf4j;
import me.song.common.constant.ShiroConstant;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @author Songwe
 * @since 2022/6/3 21:50
 */
@Slf4j
public class ShiroSessionManager extends DefaultWebSessionManager {
    
    private static final String JWT_TOKEN_SESSION_ID_SOURCE = "jwt_token";
    
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(ShiroConstant.TOKEN);
//        if (StringUtils.isEmpty(token)) {
            // 走默认从 cookie 获取 sessionId
            return super.getSessionId(request, response);
//        }
//        else {
//            // 从token 解析 sessionId
//            DecodedJWT decodedJWT = JwtTokenManager.resolveToken(token);;
//            String id = decodedJWT.getId();
//
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, JWT_TOKEN_SESSION_ID_SOURCE);
//
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
//            //automatically mark it valid here.  If it is invalid, the
//            //onUnknownSession method below will be invoked and we'll remove the attribute at that time.
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
//
//            return id;
//        }
    }
}
