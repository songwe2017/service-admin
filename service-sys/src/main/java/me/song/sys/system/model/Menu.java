package me.song.sys.system.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import me.song.common.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author songwe
 * @since 2023-01-06
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu对象", description = "菜单表")
public class Menu extends BaseModel {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属上级")
    private Long pid;

    @ApiModelProperty("组件名称")
    private String name;

    @ApiModelProperty("菜单名称")
    private String title;

    @ApiModelProperty("类型(0-目录1-菜单,2-按钮)")
    private String type;

    @ApiModelProperty("权限值")
    private String permission;

    @ApiModelProperty("是否隐藏(0-否，1-是)")
    private Boolean hidden;

    @ApiModelProperty("访问路径")
    private String path;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态(0:禁止,1:正常)")
    private Boolean status;

    @TableField(exist = false)
    @ApiModelProperty("子节点")
    private List<Menu> children;
}
