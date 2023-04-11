package me.song.sys.system.model;

import me.song.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author songwe
 * @since 2022-12-20
 */
@Getter
@Setter
@TableName("sys_user_role")
@ApiModel(value = "UserRole对象", description = "")
public class UserRole extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("用户id")
    private String userId;


}
