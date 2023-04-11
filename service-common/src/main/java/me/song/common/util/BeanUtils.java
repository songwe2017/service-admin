package me.song.common.util;

import com.baomidou.mybatisplus.core.toolkit.Assert;

/**
 * @author Songwe
 * @since 2022/12/29 9:23
 */
public class BeanUtils {
    
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        Assert.notNull(source, "Source must not be null");
        
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        org.springframework.beans.BeanUtils.copyProperties(source, t);
        
        return t;
    }
}
