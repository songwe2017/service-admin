package me.song.sys.shiro;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * @author Songwe
 * @since 2023/4/1 16:53
 */
@JsonFilter("shiroJsonFilter")
public interface ExcludeSerializedField {
}
