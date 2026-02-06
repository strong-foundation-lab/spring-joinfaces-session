package io.strongfoundation.joinfaces.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.strongfoundation.joinfaces.session.SessionData;
import io.strongfoundation.joinfaces.session.SessionHandler;

@Component
public class LoginSession {

    private SessionHandler sessionHandler;

    @Autowired
    public LoginSession(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    public boolean login(String username) {
        SessionData sessionData = createSession(username);
        sessionHandler.startSesssion(sessionData);

        return (sessionData != null);
    }

    public SessionData createSession(String username) {

        return new SessionData(
                username,
                "DEFAULT_ROLE",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                UUID.randomUUID().toString());

    }

    public void closeCurrentSession() {
        sessionHandler.closeSession();
    }

    public void invalidateHttpSessionsByUsername(String username) {
        if (username == null) {
            return;
        }
        sessionHandler.invalidateAllSessions(username);

    }

    public void invalidateHttpSessionsByUUID(List<String> uuids) {
        if (uuids == null || uuids.size() == 0) {
            return;
        }
        for (String uuid : uuids) {
            sessionHandler.invalidateSessions(uuid);
        }
    }

}
