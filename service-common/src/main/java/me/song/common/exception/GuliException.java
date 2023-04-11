package me.song.common.exception;

import me.song.common.util.RCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Songwe
 * @date 2022/5/30 13:33
 */
@Getter
@Setter
public class GuliException extends RuntimeException {
    private int status = 500;
    private String msg;
    
    public GuliException(String msg) {
        super(msg);
        this.msg = msg;
    }
    
    public GuliException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
    
    public GuliException(RCode rCode) {
        super(rCode.getMsg());
        this.status = rCode.getCode();
        this.msg = rCode.getMsg();
    }

    public GuliException(RCode rCode, Throwable e) {
        super(rCode.getMsg(), e);
        this.status = rCode.getCode();
        this.msg = rCode.getMsg();
    }
}
