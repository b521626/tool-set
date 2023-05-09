package com.example.demo.hander;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Aspect
@Configuration
@Order(2)
public class ValidatedHander {

    @Pointcut("@annotation(org.springframework.validation.annotation.Validated)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){
        System.out.println(joinPoint);

    }
}
