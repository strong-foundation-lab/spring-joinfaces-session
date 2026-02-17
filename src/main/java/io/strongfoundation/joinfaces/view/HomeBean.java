package io.strongfoundation.joinfaces.view;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.strongfoundation.joinfaces.config.ProcessAuditLogger;
import io.strongfoundation.joinfaces.config.StatelessView;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@StatelessView("homeBean")
public class HomeBean extends BaseBean {

    private final LoginSession loginSession;

    private String message = "";
    private int count = 0;

    @PostConstruct
    public void init() {
        count++;
        message = getAuthenticatedUser() + " (" + count + "): " + LocalDateTime.now();
    }

    public String getAuthenticatedUser() {
        // 1. Intento por SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }

        // 2. Intento por RequestContextHolder de Spring (Más confiable que FacesContext
        // en el init)
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();

            // A. Ver si el filtro ya puso el usuario en el request
            if (request.getRemoteUser() != null) {
                return request.getRemoteUser();
            }

            // B. Re-validación manual desde la cookie usando el request de Spring
            String token = jwtUtils.recoverTokenFromCookie(request);
            if (token != null && jwtUtils.validateJwtToken(token)) {
                return jwtUtils.getAuthentication(token).getName();
            }
        }

        return "ANONYMOUS";
    }

    @Autowired
    public HomeBean(LoginSession loginSession) {
        this.loginSession = loginSession;
    }

    @ProcessAuditLogger(process = "BP_LOGOUT", validationUser = true)
    public String execLogout() {
        System.out.println("********* getCurrentUser():" + getCurrentUser());
        System.out.println("********* getAuthenticatedUser():" + getAuthenticatedUser());

        loginSession.closeCurrentSession("admin");
        return "login";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
