//package com.alex.aspect;
//
//import com.alex.model.Employee;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.validation.annotation.Validated;
//
//import javax.validation.groups.Default;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.util.Map;
//
//@Configuration
//@Aspect
//@Slf4j
//public class AspectHander {
//
//    @Pointcut("execution(* com.alex.controller.*.*(..))")
//    public void validateParam() {
//
//    }
//
//    @Around("validateParam()")
//    public void validateGroup(ProceedingJoinPoint joinPoint) throws Throwable {
////        System.out.println("beforeController:" + joinPoint);
////        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
////        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
////        assert sra != null;
////        HttpServletRequest request = sra.getRequest();
////
////        String url = request.getRequestURL().toString();
////        String method = request.getMethod();
////        String uri = request.getRequestURI();
////        String queryString = request.getQueryString();
////        //这里可以获取到get请求的参数和其他信息
////        log.info("请求开始, 各个参数, url: {}, method: {}, uri: {}, params: {}", url, method, uri, queryString);
////        //重点 这里就是获取@RequestBody参数的关键  调试的情况下 可以看到o变量已经获取到了请求的参数
////        Object[] o = joinPoint.getArgs();
////        MethodInvocationProceedingJoinPoint mipjp = (MethodInvocationProceedingJoinPoint) joinPoint;
////        // result的值就是被拦截方法的返回值
////        Object result = joinPoint.proceed();
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = null;
//        if (signature instanceof MethodSignature) {
//            methodSignature = (MethodSignature) signature;
//            Object target = joinPoint.getTarget();
//            Method method = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
//            Class<?>[] parameterTypes = method.getParameterTypes();
//            for (Class<?> parameterType : parameterTypes) {
//            }
//            Annotation[][] annotations = method.getParameterAnnotations();
////            for (Annotation[] annotation : annotations) {
////                for (Annotation anno : annotation) {
////                    Validated validated = AnnotationUtils.getAnnotation(anno, Validated.class);
////                    if (validated != null) {
////                        InvocationHandler invocationHandler = Proxy.getInvocationHandler(Validated.class);
////                        Field value = invocationHandler.getClass().getDeclaredField("memberValues");
////                        value.setAccessible(true);
////                        Map map = (Map) value.get(invocationHandler);
////                        map.put("value", Default.class);
////                    }
////                }
////            }
//        }
//
//    }
//}
