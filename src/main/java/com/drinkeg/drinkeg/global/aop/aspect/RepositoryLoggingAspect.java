package com.drinkeg.drinkeg.global.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.drinkeg.drinkeg.global.aop.util.LoggingUtil.*;
import static com.drinkeg.drinkeg.global.aop.util.LoggingUtil.calculateDuration;

@Aspect
@Component
@Slf4j
public class RepositoryLoggingAspect {

    @Pointcut("execution(public * com.drinkeg..repository..*.*(..))")
    public void allRepository() {}

    @Around("allRepository()")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = getMethodSignature(method);

        long start = System.currentTimeMillis();

        log.info("[REPOSITORY] Method: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("[REPOSITORY] Method: {}, Duration: {}ms", methodName, calculateDuration(start));
            return result;
        } catch (Exception e) {
            log.error(
                    "[UnhandledException] Method: {}, Exception: {}, Message: {}, Duration: {}ms",
                    methodName,
                    e.getClass().getSimpleName(),
                    getShortErrorMessage(e.getMessage()),
                    calculateDuration(start));
            throw e;
        }
    }
}