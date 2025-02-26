package br.com.roboticsmind.products.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogginAspect {
    
    @Before("execution(* br.com.roboticsmind.products.controllers.*.*(..))")
    public void logBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Método chamado BEFORE: {}", joinPoint.getSignature());
    }
}
