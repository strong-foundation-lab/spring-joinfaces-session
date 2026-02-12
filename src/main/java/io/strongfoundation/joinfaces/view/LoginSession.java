package io.strongfoundation.joinfaces.view;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.strongfoundation.joinfaces.config.JwtUtils;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSession {

    private final JwtUtils jwtUtils;

    @Autowired
    public LoginSession(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public boolean login(String username) {
        createCookie(username);
        return true;
    }

    public Cookie generateCookie(String username) {
        String roles = "DEFAULT_ROLE";
        String token = jwtUtils.generateJwtToken(username, Arrays.asList(roles));

        Cookie cookie = new Cookie("AUTH-TOKEN", token);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Solo viaja por HTTPS
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        return cookie;
    }

    public void createCookie(String username) {

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        Cookie cookie = generateCookie(username);

        response.addCookie(cookie);
    }

    public void closeCurrentSession(String username) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        Cookie cookie = generateCookie(username);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();
    }

    public String logout() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        // 1. Crear una cookie con el mismo nombre
        Cookie cookie = new Cookie("AUTH-TOKEN", null); // Valor nulo

        // 2. ATRIBUTOS IDÉNTICOS A LA ORIGINAL
        // Es vital que el path sea el mismo que usaste al crearla (ej. "/" o
        // contextPath)
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());

        // 3. Matar la cookie
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);

        // 4. Agregarla a la respuesta para que el navegador la borre
        response.addCookie(cookie);

        // 5. Invalidar el SecurityContext por seguridad extra en el hilo actual
        SecurityContextHolder.clearContext();

        // 6. Redirigir al login
        return "/app/login.xhtml?faces-redirect=true";
    }

}
