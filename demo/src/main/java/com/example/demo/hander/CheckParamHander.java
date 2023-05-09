package com.example.demo.hander;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Configuration
//@Order(Integer.MIN_VALUE)
public class CheckParamHander {

//    @Pointcut("@annotation(com.example.demo.annotation.CheckParam)")
    @Pointcut("execution(* com.example.demo.controller.*.*(..))")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void around(JoinPoint joinPoint){
        System.out.println("before:" + joinPoint.toString());

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final Method method = signature.getMethod();
        final Validated annotation = method.getAnnotation(Validated.class);


    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around:" + joinPoint.toString());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        final Method method = signature.getMethod();
        method.setAccessible(true);
        final Validated annotation = method.getAnnotation(Validated.class);
        final Object proceed = joinPoint.proceed();
        return proceed;
    }


}
