package br.com.roboticsmind.products.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Around("@annotation(br.com.roboticsmind.products.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.nanoTime();

        Object result = joinPoint.proceed();

        long duration = (System.nanoTime() - start) / 1_000_000;
        log.info("@ExecutionTimeAspect - Execution time {}: {} ms", joinPoint.getSignature(), duration);
        return result;
    }
}
