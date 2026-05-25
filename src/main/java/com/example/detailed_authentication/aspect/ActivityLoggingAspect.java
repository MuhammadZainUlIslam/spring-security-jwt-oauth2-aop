package com.example.detailed_authentication.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLoggingAspect {

    private final HttpServletRequest request;

    // =========================================================
    // COMMON HELPERS
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            return authentication.getName();
        }

        return "anonymous";
    }

    private String getClientIp() {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    // =========================================================
    // AUTH API ACCESS
    // =========================================================

    @Before("""
            execution(* com.example.detailed_authentication.controller.AuthController.*(..))
            """)
    public void logAuthApiAccess(JoinPoint joinPoint) {

        log.info(
                "AUTH_API_ACCESS | endpoint={} | uri={} | method={} | ip={}",
                joinPoint.getSignature().getName(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIp()
        );
    }

    // =========================================================
    // LOGIN SUCCESS
    // =========================================================

    @AfterReturning("""
            execution(* com.example.detailed_authentication.service.AuthService.login(..))
            """)
    public void logLoginSuccess(JoinPoint joinPoint) {

        log.info(
                "LOGIN_SUCCESS | user={} | ip={} | serviceMethod={}",
                getCurrentUsername(),
                getClientIp(),
                joinPoint.getSignature().getName()
        );
    }

    // =========================================================
    // REGISTRATION SUCCESS
    // =========================================================

    @AfterReturning("""
            execution(* com.example.detailed_authentication.service.AuthService.registerLocalUser(..))
            """)
    public void logRegistrationSuccess(JoinPoint joinPoint) {

        log.info(
                "REGISTRATION_SUCCESS | ip={} | serviceMethod={}",
                getClientIp(),
                joinPoint.getSignature().getName()
        );
    }

    // =========================================================
    // OAUTH2 SUCCESS
    // =========================================================

    @AfterReturning("""
            execution(* com.example.detailed_authentication.service.OAuth2Service.*(..))
            """)
    public void logOAuth2Success(JoinPoint joinPoint) {

        log.info(
                "OAUTH2_SUCCESS | user={} | ip={} | serviceMethod={}",
                getCurrentUsername(),
                getClientIp(),
                joinPoint.getSignature().getName()
        );
    }

    // =========================================================
    // SECURED API ACCESS
    // =========================================================

    @Before("""
            execution(* com.example.detailed_authentication.controller..*(..))
            && !execution(* com.example.detailed_authentication.controller.AuthController.*(..))
            """)
    public void logSecureApiAccess(JoinPoint joinPoint) {

        log.info(
                "SECURE_API_ACCESS | user={} | uri={} | method={} | controllerMethod={} | ip={}",
                getCurrentUsername(),
                request.getRequestURI(),
                request.getMethod(),
                joinPoint.getSignature().toShortString(),
                getClientIp()
        );
    }

    // =========================================================
    // EXECUTION TIME
    // =========================================================

    @Around("""
            execution(* com.example.detailed_authentication.service..*(..))
            && !within(com.example.detailed_authentication.filter..*)
            """)
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info(
                "METHOD_EXECUTION | method={} | executionTimeMs={}",
                joinPoint.getSignature().toShortString(),
                executionTime
        );

        return result;
    }

    // =========================================================
    // EXCEPTION LOGGING
    // =========================================================

    @AfterThrowing(
            pointcut = """
                    execution(* com.example.detailed_authentication..*(..))
                    && !within(com.example.detailed_authentication.filter..*)
                    """,
            throwing = "ex"
    )
    public void logException(JoinPoint joinPoint, Exception ex) {

        log.error(
                "APPLICATION_EXCEPTION | user={} | uri={} | method={} | exception={} | message={}",
                getCurrentUsername(),
                request.getRequestURI(),
                joinPoint.getSignature().toShortString(),
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }
}