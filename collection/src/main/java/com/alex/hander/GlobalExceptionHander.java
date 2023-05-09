package com.alex.hander;

import com.alex.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHander {

    @ExceptionHandler(Exception.class)
    public Response processHander(Exception e){
        Response response = new Response();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;
            FieldError fieldError = validException.getBindingResult().getFieldError();
            assert fieldError != null;
            String msg = fieldError.getField() + ":" + fieldError.getDefaultMessage();
            response.setCode("203");
            response.setMessage(msg);
            return response;
        }
        response.setCode("202");
        response.setMessage("exceptionHander");
        log.error("error:", e);
        return response;
    }
}
