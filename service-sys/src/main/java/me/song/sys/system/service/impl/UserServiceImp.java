package me.song.sys.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.song.sys.system.mapper.UserMapper;
import me.song.sys.system.model.User;
import me.song.sys.system.service.UserService;
import me.song.sys.system.model.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author songwe
 * @since 2022-12-20
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUserByName(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public Page<User> pageCondition(Page page, UserVo condition) {
        LambdaQueryWrapper<User> search = new LambdaQueryWrapper<>();
        String name = condition.getUsername().trim();
        if (!StringUtils.isEmpty(name)) {
            search.like(User::getUsername, name);
        }
        String status = condition.getStatus();
        if (!StringUtils.isEmpty(status)) {
            search.eq(User::getStatus, status);
        }

        return page(page, search);
    }

}
