package me.song.sys.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.song.sys.system.mapper.MenuMapper;
import me.song.sys.system.model.Menu;
import me.song.sys.system.model.RoleMenu;
import me.song.sys.system.service.MenuService;
import me.song.sys.system.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public List<Menu> queryAllMenu() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getId);
        return menuMapper.selectList(queryWrapper);
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
        return getBaseMapper().listByUserId(userId);
    }
}
