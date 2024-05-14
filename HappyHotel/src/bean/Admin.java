package bean;

import java.io.Serializable;
//使用接口Serializable，使得该类可以被序列化
public class Admin implements Serializable {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "username=" + username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin() {
    }

    public Admin(String admin, String number) {
        this.username = admin;
        this.password = number;
    }
}
