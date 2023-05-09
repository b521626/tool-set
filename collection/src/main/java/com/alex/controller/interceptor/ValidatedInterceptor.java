package com.alex.controller.interceptor;

import com.alex.model.GroupA;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

@Component
public class ValidatedInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestWrapper requestWrapper = new RequestWrapper(request);
        final String body = requestWrapper.getBody();
        final JSONObject jsonObject = JSONObject.parseObject(body);
        final Object name = jsonObject.get("name");
        if ("alex".equals(name)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    Validated validated = AnnotationUtils.getAnnotation(annotation, Validated.class);
                    if (validated != null) {
                        InvocationHandler invocationHandler = Proxy.getInvocationHandler(validated);
                        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
                        memberValues.setAccessible(true);
                        Map map = (Map) memberValues.get(invocationHandler);
                        map.put("value", GroupA.class);
                    }

                }
            }
        }
        return true;
//        String intetype = null;
//        final Parameter[] parameters = method.getParameters();
//        for (Parameter parameter : parameters) {
////            Class<? extends Parameter> aClass = parameter.getClass();
//            final String typeName = parameter.getParameterizedType().getTypeName();
//            final Class<?> aClass1 = Class.forName(typeName);
//            Field[] declaredFields = aClass1.getDeclaredFields();
//            for (Field declaredField : declaredFields) {
//                declaredField.setAccessible(true);
//                final String name = declaredField.getName();
//                final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(declaredField.getName(), aClass1);
//                final Method readMethod = propertyDescriptor.getReadMethod();
//                final Object invoke = readMethod.invoke(aClass1.newInstance());
//                System.out.println(invoke);
//
//                Object o = declaredField.get(aClass1);
//                System.out.println(o);
//                if ("intetype".equals(name)) {
//                    o = declaredField.get(aClass1);
//                    System.out.println(o);
//                    intetype = (String) o;
//                    System.out.println(intetype);
//                }
//            }
//        }
    }
}
