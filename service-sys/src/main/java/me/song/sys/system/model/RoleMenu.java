package me.song.sys.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import me.song.common.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色菜单表
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
@Getter
@Setter
@TableName("sys_role_menu")
@ApiModel(value = "RoleMenu对象", description = "角色菜单表")
public class RoleMenu extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;


}
