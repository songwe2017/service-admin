package me.song.sys.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.song.common.constant.Constant;
import me.song.common.enums.ComponentType;
import me.song.common.enums.MenuType;
import me.song.sys.shiro.utils.ShiroSecurityUtils;
import me.song.sys.system.mapper.MenuMapper;
import me.song.sys.system.model.Menu;
import me.song.sys.system.model.RoleMenu;
import me.song.sys.system.model.vo.MetaVo;
import me.song.sys.system.model.vo.RouterVo;
import me.song.sys.system.service.MenuService;
import me.song.sys.system.service.RoleMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
@Service
public class MenuServiceImp extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Menu> getAllMenus() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getType, "0", "1");
        queryWrapper.orderByAsc(Menu::getPid, Menu::getId);
        return menuMapper.selectList(queryWrapper);
    }

    @Override
    public List<Menu> getAllMenusTree() {
        List<Menu> allMenus = getAllMenus();

        // 第一种写法
        List<Menu> result = new ArrayList<>();
        allMenus.forEach(menu -> {
            if (menu.getPid().equals(0L)) {
                // 只处理一级菜单，也就是找到一个入口
                Menu menuLevelOne = getChildren(allMenus, menu);
                // 所有一级菜单处理完毕，子菜单也就处理完了
                result.add(menuLevelOne);
            }
        });

        return result;

        // 第二种写法 getChildrenTwo()
        // 第一种写法是找到一级菜单作为入口，处理下级子菜单，第二种写法直接处理所有菜单，缺点是每递归一次都要创建列表
    }

    public Menu getChildren(final List<Menu> menus, final Menu currentMenu) {
        // 单层递归逻辑
        // 1、拿到当前菜单
        currentMenu.setChildren(new ArrayList<>());
        // 2、将列表中当前菜单的子菜单放到当前菜单子菜单列表
        for (Menu menu : menus) {
            if (!menu.getPid().equals(currentMenu.getId())) continue;
            currentMenu.getChildren().add(getChildren(menus, menu));
        }
        // 3、返回当前菜单
        return currentMenu;
    }

    @Override
    public void removeMenuById(Long id) {
        List<Long> idList = new ArrayList<>();

        getChildList(id, idList);
        idList.add(id);
        menuMapper.deleteBatchIds(idList);
    }

    private void getChildList(Long pid, List<Long> idList) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getPid, pid)
                .select(Menu::getId);
        List<Menu> menus = menuMapper.selectList(queryWrapper);

        menus.forEach(e -> {
            idList.add(e.getId());
            getChildList(e.getId(), idList);
        });
    }

    @Override
    public void doAssignMenu(Long roleId, Long[] menuId) {
        List<RoleMenu> roleMenus = Arrays.stream(menuId).map(e -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(e);
            return roleMenu;
        }).collect(Collectors.toList());
        // 这个方法是批量插入，不是便利插入
        roleMenuService.saveBatch(roleMenus);
    }

    @Override
    public List<Menu> getUserMenus(Long userId) {
        if (ShiroSecurityUtils.isAdmin(userId)) {
            return getAllMenus();
        }
        return getBaseMapper().listByUserId(userId);
    }

    @Override
    public List<Menu> getUserMenusTree(Long userId) {
        if (ShiroSecurityUtils.isAdmin(userId)) {
            return getAllMenusTree();
        }

        List<Menu> menus = getBaseMapper().listByUserId(userId);
        return getChildrenTwo(menus, 0L);
    }

    public List<Menu> getChildrenTwo(final List<Menu> menus, final Long pid) {
        List<Menu> result = new ArrayList<>();
        menus.forEach(menu -> {
            if (!Objects.equals(menu.getPid(), pid)) return;

            menu.setChildren(getChildrenTwo(menus, menu.getId()));
            result.add(menu);
        });
        return result;
    }

    @Override
    public List<RouterVo> buildRouter(List<Menu> menus) {
        List<RouterVo> routers = new ArrayList<>();

        menus.forEach(menu -> {
            RouterVo routerVo = new RouterVo();
            routerVo.setName(menu.getName());
            routerVo.setPath(getRouterPath(menu));
            routerVo.setHidden(menu.getHidden());
            routerVo.setComponent(getRouterComponent(menu));
            routerVo.setMetaVo(new MetaVo(menu.getTitle(), menu.getIcon()));
            List<Menu> children = menu.getChildren();

            if (CollectionUtils.isNotEmpty(children) && menu.getType().equals(MenuType.Dir.getType())) {
                routerVo.setAlwaysShow(true);
                routerVo.setRedirect(Constant.NO_REDIRECT);
                routerVo.setChildren(buildRouter(children));
            }
            routers.add(routerVo);
        });

        return routers;
    }

    /**
     * 获取路由 path
     * @param menu 菜单信息
     * @return path
     */
    public String getRouterPath(Menu menu) {
        String path = menu.getPath();
        if (menu.getPid().intValue() == 0 && menu.getType().equals(MenuType.Dir.getType())) {
            return "/" + path;
        }
        return path;
    }

    public String getRouterComponent(Menu menu) {
        if (menu.getType().equals(MenuType.Dir.getType())) {
            return ComponentType.Layout.name();
        }
        return menu.getComponent();
    }
}
