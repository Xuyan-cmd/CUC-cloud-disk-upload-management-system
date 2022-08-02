package com.chen.service.excepyionhandler;

import com.chen.service.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        System.out.println(e);
        return R.error().message("执行了全局异常处理...");
    }

    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e) {
        return R.error().message("执行了ArithmeticException异常处理...");
    }

    @ResponseBody
    @ExceptionHandler(SpaceException.class)
    public R error(SpaceException e) {
        log.error(e.getMessage());//写进指定文件中
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
