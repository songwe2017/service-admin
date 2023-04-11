package me.song.common.util;

import lombok.Getter;

/**
 * @author Songwe
 * @date 2022/5/29 22:35
 */
public enum RCode {
    
    SUCCESS(0, "成功"),
    FAILED(1, "失败");
    
    RCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    @Getter
    private Integer code;
    @Getter
    private String msg;
}
