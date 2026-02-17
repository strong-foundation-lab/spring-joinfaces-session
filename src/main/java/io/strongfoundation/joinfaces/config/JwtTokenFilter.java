package io.strongfoundation.joinfaces.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtils.recoverTokenFromCookie(request);

        // LOG DE DEPURACIÓN
        System.out.println("DEBUG: URI: " + request.getRequestURI());
        System.out.println("DEBUG: Token encontrado: " + (token != null));

        if (token != null && jwtUtils.validateJwtToken(token)) {
            Authentication auth = jwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("DEBUG: Usuario autenticado: " + auth.getName());
        }
        filterChain.doFilter(request, response);
    }

}
