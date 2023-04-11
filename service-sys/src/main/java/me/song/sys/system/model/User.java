package me.song.sys.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.song.common.base.BaseModel;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author songwe
 * @since 2023-04-11
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "用户表")
public class User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别1男2女")
    private String gender;

    @ApiModelProperty("是否管理员")
    private Boolean isAdmin;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态 1（true）禁用， 0（false）正常")
    private Boolean status;


}
