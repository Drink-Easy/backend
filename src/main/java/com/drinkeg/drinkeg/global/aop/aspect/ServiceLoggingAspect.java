package com.drinkeg.drinkeg.global.aop.aspect;

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
public class ServiceLoggingAspect {

    @Pointcut("execution(public * com.drinkeg..service..*.*(..))")
    public void allService() {}

    @Around("allService()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = getMethodSignature(method);

        long start = System.currentTimeMillis();

        log.info("[SERVICE] Method: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("[SERVICE] Method: {}, Duration: {}ms", methodName, calculateDuration(start));
            return result;
        } catch (GeneralException generalException) {
            log.info(
                    "[GeneralException] Method: {}, Code: {}, Message: {}, Duration: {}ms",
                    methodName,
                    generalException.getErrorStatus().getCode(),
                    generalException.getMessage(),
                    calculateDuration(start));

            throw generalException;
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