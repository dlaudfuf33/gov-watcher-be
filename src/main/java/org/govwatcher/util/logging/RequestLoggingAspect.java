package org.govwatcher.util.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RequestLoggingAspect {
    @Around("execution(* org.govwatcher.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            log.info("[✅ {}] completed in {} ms", methodName, System.currentTimeMillis() - start);
            return result;
        } catch (Exception e) {
            log.error("[❌ {}] error: {}", methodName, e.getMessage());
            throw e;
        }
    }
}