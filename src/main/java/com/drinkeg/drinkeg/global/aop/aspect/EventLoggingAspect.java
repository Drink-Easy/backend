package com.drinkeg.drinkeg.global.aop.aspect;

import com.drinkeg.drinkeg.global.aop.util.LoggingUtil;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.drinkeg.drinkeg.global.aop.util.LoggingUtil.*;

@Aspect
@Component
@Slf4j
public class EventLoggingAspect {

    @Pointcut(
            "execution(public * org.springframework.context.ApplicationEventPublisher+.publishEvent(..))")
    public void publishEventMethods() {}

    @Pointcut(
            "@annotation(org.springframework.context.event.EventListener)")
    public void eventListenerMethods() {}

    @Around("publishEventMethods()")
    public Object logEventPublishing(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = getMethodSignature(method);

        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error(
                    "[EVENT-PUBLISH-ERROR] Method: {}, Exception: {}, Message: {}, Duration: {}ms",
                    methodName,
                    e.getClass().getSimpleName(),
                    getShortErrorMessage(e.getMessage()),
                    calculateDuration(start));
            throw e;
        }
    }

    @Around("eventListenerMethods()")
    public Object logEventHandlers(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = getMethodSignature(method);

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } catch (GeneralException generalException) {
            log.info(
                    "[EVENT-LISTENER-CUSTOM] Method: {}, Code: {}, Message: {}, Duration: {}ms",
                    methodName,
                    generalException.getErrorStatus().getCode(),
                    generalException.getMessage(),
                    calculateDuration(start));
            throw generalException;

        } catch (Exception e) {
            log.error(
                    "[EVENT-LISTENER-ERROR] Method: {}, Exception: {}, Message: {}, Duration: {}ms",
                    methodName,
                    e.getClass().getSimpleName(),
                    getShortErrorMessage(e.getMessage()),
                    calculateDuration(start));
            throw e;
        }
    }
}