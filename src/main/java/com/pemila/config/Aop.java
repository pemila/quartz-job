package com.pemila.config;

import com.pemila.util.JSON;
import com.pemila.util.Logs;
import com.pemila.util.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author 月在未央
 * @date 2019/4/3 12:14
 */
@Aspect
@Component
public class Aop {

    @Around("execution(* com.pemila.controller..*.*(..))")
    public Object doAroundService(ProceedingJoinPoint pJoinPoint){
        Object object;
        try {
            object = pJoinPoint.proceed();
        } catch (Throwable e) {
            object = Result.fail("请求失败");
            Logs.error(e.getMessage(),e.getCause());
        }
        return object;
    }
}
