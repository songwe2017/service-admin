package me.song.common.util;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author Songwe
 * @since 2022/5/29 22:28
 */
@Getter
@Setter
@ToString
@ApiModel("返回结构")
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private static final String SUCCESS = "success";
    
    private static final String CODE = "code";

    private static final String MSG = "msg";

    private static final String DATA = "data";
    
    public R(int code, boolean success){
        this(code, success, null);
    }

    public R(int code, boolean success, String msg){
        this(code, success, msg, null);
    }

    public R(int code, boolean success, String msg, Object data){
        super.put(CODE, code);
        super.put(SUCCESS, success);
        super.put(MSG, msg);
        if (Objects.nonNull(data)) {
            super.put("data", data);
        }
    }

    public static R success() {
        return R.success(null);
    }

    public static R success(Object data) {
        return R.success(RCode.SUCCESS, "操作成功", data);
    }

    public static R success(Integer code, String msg) {
        return R.success(code, msg, null);
    }

    public static R success(Integer code, String msg, Object data) {
        return new R(code, true, msg, data);
    }

    public static R failed() {
        return R.failed("操作失败");
    }

    public static R failed(String msg) {
        return R.failed(RCode.ERROR, msg);
    }

    public static R failed(Object data) {
        return R.failed(RCode.ERROR, "操作失败", data);
    }

    public static R failed(Integer code, String msg) {
        return R.failed(code, msg, null);
    }

    public static R failed(Integer code, String msg, Object data) {
        return new R(code, true, msg, data);
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
