package me.song.sys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.song.sys.system.model.Menu;
import me.song.sys.system.model.vo.RouterVo;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    List<Menu> getAllMenus();

    /**
     * 查询所有菜单树
     *
     * @return 菜单树
     */
    List<Menu> getAllMenusTree();

    /**
     * 递归删除菜单
     * @param id 菜单id
     */
    void removeMenuById(Long id);

    /**
     * 给角色分配权限
     * @param roleId 角色ID
     * @param menuId 权限ID
     */
    void doAssignMenu(Long roleId, Long[] menuId);

    /**
     * 获取用户权限列表
     * @param userId 用户id
     * @return list
     */
    List<Menu> getUserMenus(Long userId);

    /**
     * 获取用户菜单树
     * @param userId 用户id
     * @return 用户菜单树
     */
    List<Menu> getUserMenusTree(Long userId);

    /**
     * 构建前端路由菜单树
     * @param menus 路由树列表
     * @return 菜单树
     */
    public List<RouterVo> buildRouter(List<Menu> menus);
}
