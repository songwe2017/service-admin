package me.song.common.enums;

import lombok.Getter;

/**
 * @author Songwe
 * @since 2023/4/18 13:54
 */
public enum MenuType {

    Dir("0", "目录"),

    Menu("1", "菜单"),
    Button("2", "按钮"),;

    @Getter
    private final String type;
    @Getter
    private final String desc;

    MenuType(String type, String desc){
        this.type = type;
        this.desc = desc;
    }

}
