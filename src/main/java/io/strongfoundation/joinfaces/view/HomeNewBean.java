package io.strongfoundation.joinfaces.view;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import io.strongfoundation.joinfaces.config.StatelessView;
import jakarta.annotation.PostConstruct;

@StatelessView("homeNewBean")
public class HomeNewBean extends BaseBean {

    private final LoginSession loginSession;

    private String message = "";
    private int count = 0;

    @PostConstruct
    public void init() {
        count++;
        message = getCurrentUser() + " (" + count + "): " + LocalDateTime.now();
    }

    @Autowired
    public HomeNewBean(LoginSession loginSession) {
        this.loginSession = loginSession;
    }

    public String execLogout() {
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
