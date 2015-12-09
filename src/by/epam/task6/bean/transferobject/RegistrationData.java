package by.epam.task6.bean.transferobject;

import java.io.Serializable;

public class RegistrationData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String login;
    private String password;
    private String firstName;
    private String secondName;

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
