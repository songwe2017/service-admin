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
 * 角色表
 * </p>
 *
 * @author songwe
 * @since 2023-04-11
 */
@Getter
@Setter
@TableName("sys_role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态0-正常，1-禁用")
    private Boolean status;


}
