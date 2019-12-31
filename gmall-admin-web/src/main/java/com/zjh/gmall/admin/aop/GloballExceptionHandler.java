package com.zjh.gmall.admin.aop;

import com.zjh.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by 24923 on 2019/12/24 14:37
 *
 * 统一处理所有异常，给前端返回500的json
 */
@Slf4j
//@ControllerAdvice+@ResponseBody
@RestControllerAdvice
public class GloballExceptionHandler {


    @ExceptionHandler(value =  {ArithmeticException.class})
    public Object handlerExeption(Exception exception){
        log.error("系统全局异常感知，信息：{}",exception.getMessage());
        return new CommonResult().validateFailed("数学没学好...");
    }

    @ExceptionHandler(value =  {NullPointerException.class})
    public Object handlerExeption02(Exception exception){
        log.error("系统全局异常感知，信息：{}",exception.getMessage());
        return new CommonResult().validateFailed("空指针了...");
    }

}
