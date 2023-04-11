package me.song.sys.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import me.song.sys.shiro.ShiroByteSource;
import me.song.sys.system.model.Menu;
import me.song.sys.system.model.Role;
import me.song.sys.system.model.User;
import me.song.sys.system.service.MenuService;
import me.song.sys.system.service.RoleService;
import me.song.sys.system.service.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Songwe
 * @date 2022/5/19 15:01
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

//    @Autowired
//    private CredentialsMatcher credentialsMatcher;
    
    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String loginUser = (String) principals.getPrimaryPrincipal();
        LambdaQueryWrapper<User> condition = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginUser);
        User user = userService.getOne(condition);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (null != user.getIsAdmin() && user.getIsAdmin()) {
            List<Role> roles = roleService.list();
            roles.forEach(role -> info.addRole(role.getRoleCode()));
            List<Menu> menus = menuService.list();
            menus.forEach(menu -> info.addStringPermission(menu.getPermission()));

            return info;
        }

        List<Role> roles = roleService.getUserRoles(user.getId());
        roles.forEach(role -> info.addRole(role.getRoleCode()));
        List<Menu> menus = menuService.getUserMenus(user.getId());
        menus.forEach(menu -> info.addStringPermission(menu.getPermission()));

        return info;
    }

    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String loginUser = (String) token.getPrincipal();
        LambdaQueryWrapper<User> condition = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginUser);

        User user = userService.getOne(condition);
        
        if (user == null) {
            throw new UnknownAccountException("账号不存在！");
        }

        if (user.getStatus()) {
            throw new LockedAccountException("帐号已被禁用！");
        }

        return new SimpleAuthenticationInfo(
                loginUser,
                user.getPassword(),
                ShiroByteSource.Util.bytes("595f81557f9e403990fecea2d2e177e8"),
                getName());
    }

//    @PostConstruct
//    public void setCredentialsMatcher() {
//        super.setCredentialsMatcher(credentialsMatcher);
//    }

}
