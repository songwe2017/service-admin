package me.song.sys.system.controller;

import me.song.common.constant.ShiroConstant;
import me.song.common.util.R;
import me.song.sys.shiro.utils.ShiroSecurityUtils;
import me.song.sys.system.model.Menu;
import me.song.sys.system.model.Role;
import me.song.sys.system.model.User;
import me.song.sys.system.model.vo.UserVo;
import me.song.sys.system.service.MenuService;
import me.song.sys.system.service.RoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @author Songwe
 * @since 2021/2/1 15:43
 */
@RestController
public class LoginController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/login")
    public R login(@RequestBody UserVo userVo) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userVo.getUsername(), userVo.getPassword());
        try {
            subject.login(token);
        }
        catch (UnknownAccountException e) {
            return R.failed("用户名错误");
        }
        catch (IncorrectCredentialsException e) {
            return R.failed("密码错误");
        }
        catch (Exception e) {
            return R.failed("账户验证失败");
        }
        return R.success().put(ShiroConstant.TOKEN, ((User) subject.getPrincipal()).getUsername());
    }

    @GetMapping("/getInfo")
    public R getInfo() {
        User loginUser = ShiroSecurityUtils.getLoginUser();
        List<Role> roles = roleService.getUserRoles(loginUser.getId());

        List<Menu> menus = menuService.getUserMenus(loginUser.getId());
        return R.success().put("user", loginUser).put("roles", roles).put("menus", menus);
    }

    @GetMapping("/logout")
    public R logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return R.success();
    }

}
