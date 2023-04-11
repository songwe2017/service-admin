package me.song.sys.security;

import me.song.common.util.BeanUtils;
import me.song.sys.system.model.User;
import me.song.sys.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Songwe
 * @since 2022/12/23 9:47
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserService userService;
    
    //@Autowired
    //private PermissionService permissionService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUserByName(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名错误");
        }

        //List<Permission> userPermission = permissionService.getUserPermission(user.getId());
        //List<String> permissionValues = userPermission.stream()
        //        .map(Permission::getPath)
        //        .collect(Collectors.toList());

        LoginUser loginUser = BeanUtils.copyProperties(user, LoginUser.class);
        //loginUser.setPermissions(permissionValues);
        
        return loginUser;
    }
}
