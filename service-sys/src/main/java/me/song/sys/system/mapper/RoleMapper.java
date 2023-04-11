package me.song.sys.system.mapper;

import me.song.sys.system.model.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author songwe
 * @since 2023-04-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> listByUserId(@Param("userId") Long userId);
}
