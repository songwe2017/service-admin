package me.song.sys.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.song.sys.system.model.User;
import me.song.sys.system.model.vo.UserVo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author songwe
 * @since 2022-12-20
 */
public interface UserService extends IService<User> {

    /**
     * 检索用户
     * @param username 用户名
     * @return 登录用户
     */
    User getUserByName(String username);

    /**
     * page 条件分页查询
     * @param page
     * @param condition
     * @return
     */
    Page<User> pageCondition(Page page, UserVo condition);

}
