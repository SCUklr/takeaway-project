package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /*
        完善新增用户功能1：处理输入重复用户名异常
    */

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        // Duplicate entry 'lisi' for key 'employee.idx_username'
        String message = ex.getMessage();
        if (message.contains("Duplicate entry")) {
            // 给前端一个提示：用户名已存在
            String[] split = message.split(" ");
            String username = split[2];
            // MessageConstant MessageConstant = new MessageConstant();
            // 不需要实例化常量类
            String msg = username + MessageConstant.USERNAME_EXISTS;
            return Result.error(msg);
            /*
            *
            * {
                "code": 0,
                "msg": "'lisi'（用户名）已存在",
                "data": null
            }
            * */
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }

}
