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
    // COMMON METHODS
    // =========================================================

    private String getCurrentUsername() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            return authentication.getName();
        }

        return "Anonymous";
    }

    private String getClientIp() {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    // =========================================================
    // AUTH CONTROLLER LOGGING
    // =========================================================

    @Before("""
            execution(* com.example.detailed_authentication.controller.AuthController.*(..))
            """)
    public void logAuthControllerHit(JoinPoint joinPoint) {

        log.info("""    
                ================= AUTH API HIT =================
                Endpoint Method: {} ->> Request URI : {} ->> HTTP Method: {} ->>Client IP: {}
                =================================================
                """,
                joinPoint.getSignature().getName(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIp()
        );
    }

    // =========================================================
    // LOGIN SUCCESS
    // =========================================================

    @AfterReturning(
            pointcut = """
                    execution(* com.example.detailed_authentication.service.AuthService.login(..))
                    """,
            returning = "result"
    )
    public void logLoginSuccess(JoinPoint joinPoint, Object result) {

        log.info("""  
                ================= LOGIN SUCCESS =================
                Method: {} ->> Username: {} ->>IP Address      : {}
                =================================================
                """,
                joinPoint.getSignature().getName(),
                getCurrentUsername(),
                getClientIp()
        );
    }

    // =========================================================
    // REGISTRATION SUCCESS
    // =========================================================

    @AfterReturning(
            pointcut = """
                    execution(* com.example.detailed_authentication.service.AuthService.registerLocalUser(..))
                    """,
            returning = "result"
    )
    public void logRegistrationSuccess(JoinPoint joinPoint, Object result) {

        log.info("""
                        
                ============== REGISTRATION SUCCESS ==============
                Method          : {}
                IP Address      : {}
                =================================================
                """,
                joinPoint.getSignature().getName(),
                getClientIp()
        );
    }

    // =========================================================
    // OAUTH2 SUCCESS
    // =========================================================

    @AfterReturning(
            pointcut = """
                    execution(* com.example.detailed_authentication.service.OAuth2Service.*(..))
                    """,
            returning = "result"
    )
    public void logOAuth2Success(JoinPoint joinPoint, Object result) {

        log.info("""
                        
                ================= OAUTH2 SUCCESS =================
                Method          : {}
                Username        : {}
                IP Address      : {}
                =================================================
                """,
                joinPoint.getSignature().getName(),
                getCurrentUsername(),
                getClientIp()
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

        log.info("""
                        
                ================= SECURE API HIT =================
                API Method      : {}
                Username        : {}
                Request URI     : {}
                HTTP Method     : {}
                IP Address      : {}
                =================================================
                """,
                joinPoint.getSignature().toShortString(),
                getCurrentUsername(),
                request.getRequestURI(),
                request.getMethod(),
                getClientIp()
        );
    }

    // =========================================================
    // SERVICE EXECUTION TIME
    // =========================================================

    @Around("""
            execution(* com.example.detailed_authentication.service..*(..))
            && !within(com.example.detailed_authentication.filter..*)
            """)
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime =
                System.currentTimeMillis() - startTime;

        log.info("""
                        
                ================= METHOD EXECUTION =================
                Method          : {}
                Execution Time  : {} ms
                ====================================================
                """,
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
    public void logExceptions(JoinPoint joinPoint, Exception ex) {

        log.error("""
                        
                ================= EXCEPTION OCCURRED =================
                Method          : {}
                Username        : {}
                Request URI     : {}
                Error Message   : {}
                Exception Type  : {}
                ======================================================
                """,
                joinPoint.getSignature().toShortString(),
                getCurrentUsername(),
                request.getRequestURI(),
                ex.getMessage(),
                ex.getClass().getSimpleName()
        );
    }
}