package com.example.admin_project.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;


@Aspect
@Component
public class UserLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger("USER_ACTION");

    @Pointcut("execution(* com.example.admin_project..controller..*(..))")
    public void userLoggerPointCut() {}

    @Around("userLoggerPointCut()")
    public Object methodUserLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String httpMethod = request.getMethod();
        LocalDateTime currentTime = LocalDateTime.now();

        logger.info("URI={}, IP={}, httpMethod={}, currentTime={}", uri, ip, httpMethod, currentTime);

        // 실제 메서드 실행
        return joinPoint.proceed();
    }
}
