package me.song.common.exception;

import me.song.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Songwe
 * @since 2022/5/30 1:43
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {
    
    @ExceptionHandler(GuliException.class)
    public R handleGuliException(GuliException e) {
        log.error(e.getMsg(), e);
        return R.failed().code(e.getStatus()).msg(e.getMsg());
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return R.failed().msg("数据库已存在该记录!");
    }
    
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.failed().msg(e.getMessage());
    }
}
