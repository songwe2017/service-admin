package me.song.sys.system.controller;

import com.google.common.collect.Maps;
import me.song.common.util.R;
import me.song.sys.system.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author Songwe
 * @since 2021/2/1 15:43
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    public R login(@RequestBody UserVo userVo) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userVo.getUsername(), userVo.getPassword());
        try {
            subject.login(token);
        }
        catch (UnknownAccountException e) {
            return R.failed().msg("用户名错误");
        }
        catch (IncorrectCredentialsException e) {
            return R.failed().msg("密码错误");
        }
        catch (Exception e) {
            return R.failed().msg("账户验证失败");
        }
        return R.success().data("token", subject.getPrincipal());
    }

    @GetMapping("/getInfo")
    public R getInfo() {
        String name = (String) SecurityUtils.getSubject().getPrincipal();
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("name", name);
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return R.success(map);
    }

    @GetMapping("/logout")
    public R logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return R.success();
    }
    
}
