package io.strongfoundation.joinfaces.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionHandler {

    private static final Map<String, HttpSession> activeSessions = new ConcurrentHashMap<>();

    public HttpSession currentSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return session;
    }

    public void closeSession() {
        HttpSession session = currentSession();
        if (session != null) {
            session.invalidate();
        }
    }

    public void startSesssion(SessionData sessionData) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute(SessionEnum.USERNAME.getKey(), sessionData.username());
        session.setAttribute(SessionEnum.ROLE.getKey(), sessionData.role());
        session.setAttribute(SessionEnum.BEGIN_AT.getKey(), sessionData.beginAT());
        session.setAttribute(SessionEnum.EXPIRATION_AT.getKey(), sessionData.expirationAT());
        session.setAttribute(SessionEnum.UUID.getKey(), sessionData.uuid());
        registerSession(sessionData.uuid(), session);
    }

    public void invalidateSessions(String uuid) {
        for (HttpSession session : activeSessions.values()) {
            if (uuid.equals(session.getAttribute(SessionEnum.UUID.getKey()))) {
                session.invalidate();
            }
        }
    }

    public void invalidateAllSessions(String username) {
        for (HttpSession session : activeSessions.values()) {
            if (username.equals(session.getAttribute(SessionEnum.USERNAME.getKey()))) {
                session.invalidate();
            }
        }
    }

    public void registerSession(String uuid, HttpSession session) {
        activeSessions.put(uuid, session);
    }

    public void unregisterSession(String uuid) {
        activeSessions.remove(uuid);
    }

}
