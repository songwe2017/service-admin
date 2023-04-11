package me.song.common.util;

import me.song.common.constant.Constant;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Songwe
 * @date 2022/5/29 22:28
 */
@Data
@ApiModel("返回结构")
public class R {
    @ApiModelProperty("是否成功")
    private boolean success;
    
    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("状态描述")
    private String msg;

    @ApiModelProperty("返回数据")
    private Map<String, Object> data = new HashMap<>();
    
    private R(){}
    
    private R(RCode rCode) {
        this.code = rCode.getCode();
        this.msg = rCode.getMsg();
    }
    
    public static R success() {
        R r = new R(RCode.SUCCESS);
        r.setSuccess(true);
        return r;
    }

    public static <T> R success(T data) {
        R r = new R(RCode.SUCCESS);
        r.setSuccess(true);
        r.data.put(Constant.DATA, data);
        return r;
    }

    public static R success(List data) {
        R r = new R(RCode.SUCCESS);
        r.setSuccess(true);
        r.data.put(Constant.ITEMS, data);
        return r;
    }

    public static R success(Page data) {
        R r = new R(RCode.SUCCESS);
        r.setSuccess(true);
        r.data.put(Constant.TOTAL, data.getTotal());
        r.data.put(Constant.ITEMS, data.getRecords());
        return r;
    }

    public static R success(Map<String, Object> data) {
        R r = new R(RCode.SUCCESS);
        r.setSuccess(true);
        r.data.putAll(data);
        return r;
    }

    public static R failed() {
        R r = new R(RCode.FAILED);
        r.setSuccess(false);
        return r;
    }

    public static R failed(RCode rCode) {
        R r = new R(rCode);
        r.setSuccess(false);
        return r;
    }

    public static R failed(Integer code, String msg) {
        R r = new R();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }
    
    public R success(Boolean success) {
        this.success = success;
        return this;
    }

    public R code(Integer code) {
        this.code = code;
        return this;
    }
    
    public R msg(String msg) {
        this.msg = msg; 
        return this;
    }
    
    public R data(Map<String, Object> data) {
        this.data.putAll(data);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
    
    
}
