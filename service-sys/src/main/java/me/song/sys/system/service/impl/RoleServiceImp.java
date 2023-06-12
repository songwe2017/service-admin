package me.song.sys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.song.sys.shiro.utils.ShiroSecurityUtils;
import me.song.sys.system.mapper.RoleMapper;
import me.song.sys.system.model.Role;
import me.song.sys.system.model.User;
import me.song.sys.system.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author songwe
 * @since 2023-04-11
 */
@Service
public class RoleServiceImp extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> getUserRoles(Long userId) {
        if (ShiroSecurityUtils.isAdmin(userId)) {
            return super.list();
        }

        return getBaseMapper().listByUserId(userId);
    }
}
