package me.song.sys.system.mapper;

import me.song.sys.system.model.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> listByUserId(@Param("userId") Long userId);
}
