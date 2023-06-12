package me.song.common.enums;

import lombok.Getter;

/**
 * @author Songwe
 * @since 2023/4/18 14:42
 */
public enum ComponentType {
    Layout("Layout", "Layout组件"),;

    @Getter
    private final String type;
    @Getter
    private final String desc;

    ComponentType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
