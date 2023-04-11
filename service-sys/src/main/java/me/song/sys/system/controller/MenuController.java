package me.song.sys.system.controller;


import me.song.common.util.R;
import me.song.sys.system.model.Menu;
import me.song.sys.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/getAllMenu")
    //@PreAuthorize("isAnonymous()")
    public R getAllMenu() {
        List<Menu> menus = menuService.queryAllMenu();
        return R.success(menus);
    }

    @DeleteMapping("/removeMenu/{id}")
    public R removeMenu(@PathVariable("id") Long id) {
        menuService.removeMenuById(id);
        return R.success();
    }

    @PutMapping("/assignPermission")
    public R assignPermission(Long roleId, Long[] permissionId) {
        menuService.doAssignMenu(roleId, permissionId);
        return R.success();
    }
}

