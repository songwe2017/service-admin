package me.song.sys.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import me.song.common.util.R;
import me.song.sys.system.model.User;
import me.song.sys.system.service.MenuService;
import me.song.sys.system.service.UserService;
import me.song.sys.system.model.vo.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author songwe
 * @since 2022-12-20
 */
@Api("用户管理")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/find/{id}")
    public R find(@PathVariable Long id) {
        User user = userService.getById(id);
        return R.success(user);
    }

    @GetMapping("/findAll")
    public R findAll() {
        List<User> list = userService.list();
        return R.success(list);
    }

    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Long id) {
        boolean flag = userService.removeById(id);
        return flag ? R.success() : R.failed();
    }

    @GetMapping("/page/{current}/{size}")
    public R page(@PathVariable long current,
                  @PathVariable long size) {

        Page<User> page = Page.of(current, size);
        page = userService.page(page);
        return R.success(page);
    }

    @RequiresPermissions("system:user:view")
    @PostMapping("/pageCondition/{current}/{size}")
    public R pageCondition(@PathVariable long current,
                           @PathVariable long size,
                           @RequestBody(required = false) UserVo userVo) {

        Page<User> userPage = userService.pageCondition(Page.of(current, size), userVo);
        return R.success(userPage);
    }

    @PostMapping("/add")
    public R add(@RequestBody User teacher) {
        boolean save = userService.save(teacher);
        return save ? R.success() : R.failed();
    }

    @PutMapping("/update")
    public R update(@RequestBody User teacher) {
        boolean flag = userService.updateById(teacher);
        return flag ? R.success() : R.failed();
    }

}

