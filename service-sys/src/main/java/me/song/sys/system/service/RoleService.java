package me.song.sys.system.service;

import me.song.sys.system.model.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author songwe
 * @since 2023-04-11
 */
public interface RoleService extends IService<Role> {
    List<Role> getUserRoles(Long userId);
}
