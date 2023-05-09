package com.example.demo.hander;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHander {

    @ExceptionHandler(Exception.class)
    public String execException(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            final BindingResult bindingResult = validException.getBindingResult();
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder msg = new StringBuilder("参数校验失败");
            for (FieldError error : fieldErrors) {
                msg.append(":【").append(error.getField()).append("=").append(error.getRejectedValue())
                        .append(",").append(error.getDefaultMessage()).append("】;");
            }
            return msg.toString();
        }
        log.error("系统异常:", e);
        return "系统繁忙";
    }
}
