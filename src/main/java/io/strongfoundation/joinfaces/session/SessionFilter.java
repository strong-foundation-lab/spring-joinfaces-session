package io.strongfoundation.joinfaces.session;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String contextPath = httpRequest.getContextPath();
        String loginURL = contextPath + "/app/login.xhtml";
        String requestURI = httpRequest.getRequestURI();

        // 1. Definir exclusiones más robustas
        // Usamos .endsWith para no tener problemas con parámetros de URL
        // (?faces-redirect=true)
        boolean isLoginPage = requestURI.endsWith("login.xhtml") || requestURI.endsWith("/login");

        // 2. Verificaciones de estado
        boolean loggedIn = (session != null && session.getAttribute(SessionEnum.UUID.getKey()) != null);
        boolean isResourceRequest = requestURI.startsWith(contextPath + "/jakarta.faces.resource");

        // 3. Lógica de Decisión corregida
        if (isLoginPage || isResourceRequest) {
            // Si voy al login o recursos, paso siempre.
            // Pero si ya estoy logueado y voy al login, redirijo al home.
            if (loggedIn && isLoginPage) {
                httpResponse.sendRedirect(contextPath + "/app/home.xhtml");
                return; // IMPORTANTE: Cortar la ejecución aquí
            }
            chain.doFilter(request, response);
        } else if (loggedIn) {
            // Si estoy logueado y voy a cualquier otra página (como home.xhtml)
            chain.doFilter(request, response);
        } else {
            // Si no estoy logueado y no es una página pública -> Al Login
            handleRedirect(httpRequest, httpResponse, loginURL);
        }

    }

    private void handleRedirect(HttpServletRequest request, HttpServletResponse response, String url)
            throws IOException {
        //
        // Si es una petición AJAX de JSF/PrimeFaces
        if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"%s\"></redirect></partial-response>",
                    url);
        } else {
            // Petición normal de navegador
            response.sendRedirect(url);
        }
    }

}
