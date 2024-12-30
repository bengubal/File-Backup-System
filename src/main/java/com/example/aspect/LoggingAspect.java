package com.example.aspect;

import com.example.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final LogService logService;

    public LoggingAspect(LogService logService) {
        this.logService = logService;
    }

    // Başarılı işlemleri loglama
    @AfterReturning(pointcut = "execution(* com.example.controller.*.*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        String username = getUsername(); // Dinamik olarak kullanıcı adı
        String methodName = joinPoint.getSignature().getName();
        String action = methodName.toUpperCase();
        String status = "SUCCESS";
        String details = "Method executed successfully: " + methodName;

        logService.log(username, action, status, details);
    }

    // Başarısız işlemleri loglama
    @AfterThrowing(pointcut = "execution(* com.example.controller.*.*(..))", throwing = "exception")
    public void logAfterMethodFailure(JoinPoint joinPoint, Throwable exception) {
        String username = getUsername(); // Dinamik olarak kullanıcı adı
        String methodName = joinPoint.getSignature().getName();
        String action = methodName.toUpperCase();
        String status = "FAILURE";
        String details = "Exception occurred in method: " + methodName + ", Message: " + exception.getMessage();

        logService.log(username, action, status, details);
    }

    // Kullanıcı adını dinamik olarak almak
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Kullanıcının adını al
        }
        return "Anonymous"; // Kimlik doğrulama yoksa 'Anonymous' döner
    }
}
