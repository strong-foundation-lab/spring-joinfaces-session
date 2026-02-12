package io.strongfoundation.joinfaces.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;

@Component("homeBean")
@ViewScoped
public class HomeBean {

    private final LoginSession loginSession;

    @PostConstruct
    public void init() {

    }

    @Autowired
    public HomeBean(LoginSession loginSession) {
        this.loginSession = loginSession;
    }

    public String execLogout() {
        loginSession.closeCurrentSession("admin");
        return "login";
    }
}
