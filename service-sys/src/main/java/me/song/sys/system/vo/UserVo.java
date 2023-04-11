package me.song.sys.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Songwe
 * @since 2023/4/6 9:53
 */
@Data
public class UserVo {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("状态0-正常 1-删除")
    private String status;
}
