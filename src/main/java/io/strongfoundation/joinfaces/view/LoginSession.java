package io.strongfoundation.joinfaces.view;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.strongfoundation.joinfaces.config.JwtUtils;
import io.strongfoundation.joinfaces.config.ProcessAuditLogger;
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

    @ProcessAuditLogger(process = "BP_LOGIN", validationUser = true)
    public boolean login(String username) {
        createCookie(username);
        return true;
    }

    public Cookie generateCookie(String username, boolean onlyHttps, HttpServletRequest request) {
        String path = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
        String roles = "DEFAULT_ROLE";
        String token = jwtUtils.generateJwtToken(username, Arrays.asList(roles));

        Cookie cookie = new Cookie("AUTH-TOKEN", token);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true); // Solo viaja por HTTPS
        cookie.setSecure(onlyHttps);
        cookie.setPath(path);
        cookie.setMaxAge(3600);

        return cookie;
    }

    public void createCookie(String username) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        Cookie cookie = generateCookie(username, false, request);

        response.addCookie(cookie);
    }

    public void closeCurrentSession(String username) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();

        Cookie cookie = generateCookie(username, false, request);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();
    }

}
