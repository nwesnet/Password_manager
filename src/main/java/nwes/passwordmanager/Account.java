package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Account {
    private String resource;
    private String username;
    private String password;
    private LocalDateTime date;

    public Account(String resource, String username, String password, LocalDateTime date){
        this.resource = resource;
        this.username = username;
        this.password = password;
        this.date = date;
    }

    public String getResource() {
        return resource;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Account{" +
                "resource='" + resource + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date +
                '}';
    }
}
