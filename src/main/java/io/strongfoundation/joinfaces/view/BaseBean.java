package io.strongfoundation.joinfaces.view;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.strongfoundation.joinfaces.config.JwtUtils;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class BaseBean implements Serializable {

    @Autowired
    protected JwtUtils jwtUtils;

    // Obtener el nombre del usuario logueado desde el token validado
    public String getCurrentUser() {
        // Intento 1: SecurityContextHolder (Tradicional)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        /*
         * // Intento 2: HttpServletRequest (El búnker de respaldo)
         * // Spring Security envuelve el request original. Si el filtro pasó, el
         * // "RemoteUser" está aquí.
         * try {
         * HttpServletRequest request = (HttpServletRequest)
         * FacesContext.getCurrentInstance()
         * .getExternalContext().getRequest();
         * String user = request.getRemoteUser();
         * if (user != null) {
         * return user;
         * }
         * } catch (Exception e) {
         * // Fallback para hilos que no son de Faces
         * }
         */
        return "ANONYMOUS";
    }

    // Método rápido para mostrar mensajes en la UI
    protected void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    protected void info(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    protected void error(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, "ERROR CRÍTICO: " + summary, detail);
    }

}
