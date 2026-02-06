package io.strongfoundation.joinfaces.view;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("loginBean")
@ViewScoped
public class LoginBean implements Serializable {

    private String USERNAME = "admin";
    private String PASWORD = "password";

    private final LoginSession loginSession;
    private LoginUI ui = new LoginUI();

    @Autowired
    public LoginBean(LoginSession loginSession) {
        this.loginSession = loginSession;
    }

    @PostConstruct
    public void postConstruct() {
        ui = new LoginUI();
    }

    public String execLogin() {
        try {
            System.out.println("username: " + ui.getUsername());
            System.out.println("password: " + ui.getPassword());

            validateLogin();

            if (loginProcess()) {
                System.out.println("go to home!");
                return "home";
            } else {
                System.out.println("Login Error");
            }

            return "";
        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
            return null;
        }
    }

    public void validateLogin() throws Exception {
        if (ui.getUsername() == null || ui.getPassword() == null) {
            throw new Exception("Campos Invalidos");
        }
    }

    private boolean loginProcess() {
        if (ui.getUsername() == null || ui.getPassword() == null ||
                ui.getUsername().isEmpty() || ui.getPassword().isEmpty()) {
            return false;
        }

        if (ui.getUsername().equalsIgnoreCase(USERNAME) && ui.getPassword().equalsIgnoreCase(PASWORD)) {
            return loginSession.login(ui.getUsername());
        }

        return false;
    }

    public LoginUI getUi() {
        return ui;
    }

    public void setUi(LoginUI ui) {
        this.ui = ui;
    }
}
