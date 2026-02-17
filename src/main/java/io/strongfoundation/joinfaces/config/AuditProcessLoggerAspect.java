package io.strongfoundation.joinfaces.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuditProcessLoggerAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(processAuditLogger)")
    public Object aroundProcessInvoke(ProceedingJoinPoint joinPoint, ProcessAuditLogger processAuditLogger)
            throws Throwable {

        String businessMethod = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();

        // RESCATE DE USUARIO (Jerarquía de seguridad)
        String username = resolveUsername();

        // MAPEO DE PARÁMETROS A JSON
        String jsonArgs = resolveArgsToJson(joinPoint);

        try {
            return joinPoint.proceed();
        } finally {
            registrarAuditoriaAsync(businessMethod, jsonArgs, className, username, processAuditLogger);
        }
    }

    private String resolveUsername() {
        // 1. Intentar SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }

        // 2. Intentar RequestContext (Si es una petición HTTP)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();
            if (request.getRemoteUser() != null)
                return request.getRemoteUser();
        }

        return "SYSTEM";
    }

    private String resolveArgsToJson(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < parameterNames.length; i++) {
                params.put(parameterNames[i], args[i]);
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
        } catch (Exception e) {
            return "[Error al serializar parámetros]";
        }
    }

    private void registrarAuditoriaAsync(String method, String jsonArgs, String className, String user,
            ProcessAuditLogger annotation) {
        // Aquí imprimirás el JSON bonito
        System.out.println("\n--- AUDIT LOG ---");
        System.out.println("User: " + user);
        System.out.println("Process: " + annotation.process());
        System.out.println("Method: " + className + "." + method);
        System.out.println("Params: " + jsonArgs);
        System.out.println("-----------------\n");
    }
}