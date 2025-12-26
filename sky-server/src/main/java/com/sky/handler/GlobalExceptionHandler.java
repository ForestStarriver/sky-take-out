package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 处理sql  异常
     * @param exception
     * @return
     */
    // @ExceptionHandler
    // public Result exceptionHandler(PSQLException exception){
    //     //duplicate key value violates unique constraint "idx_username"
    //     String message = exception.getMessage();
    //     if(message.contains("duplicate key")){
    //         String[] sp = message.split(" ");
    //         String username = sp[2];
    //         String msg = username + MessageConstant.ALREADY_EXISTS;
    //         return Result.error(msg);
    //
    //     }else {
    //         return Result.error(MessageConstant.UNKNOWN_ERROR);
    //     }
    // }

    /**
     * 处理sql  异常
     * @param exception
     * @return
     */
    @ExceptionHandler(PSQLException.class)
    public Result exceptionHandler(PSQLException exception) throws PSQLException {
        // 23505 是 PostgreSQL 唯一约束冲突的标准错误码
        if ("23505".equals(exception.getSQLState())) {
            // PSQLException 有个非常强大的方法叫 getServerErrorMessage()
            // 它可以直接拿到细节，而不需要你手动解析字符串
            ServerErrorMessage serverError = exception.getServerErrorMessage();

            if (serverError != null) {
                String detail = serverError.getDetail(); // 拿到 "Key (username)=(zhangsan) already exists."

                // 使用正则提取括号里的值，比 split(" ") 稳健得多
                Pattern pattern = Pattern.compile("\\((.*?)\\)=\\((.*?)\\)");
                Matcher matcher = pattern.matcher(detail);
                if (matcher.find()) {
                    String columnName = matcher.group(1); // username
                    String columnValue = matcher.group(2); // zhangsan
                    return Result.error(columnValue + MessageConstant.ALREADY_EXISTS);
                }
            }
            return Result.error(MessageConstant.ALREADY_EXISTS);
        }
        // 2. 【关键修改】如果不是重复键错误，直接抛出！
        // 这样 SpringBoot 会检测到异常未被成功处理，从而在控制台打印完整的堆栈信息
        log.error("发现未处理的数据库异常：", exception); // 手动打一行日志，双重保险
        log.info("========================");
        throw exception;
    }
}
