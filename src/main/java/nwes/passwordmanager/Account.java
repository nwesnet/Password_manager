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
    public void setResource(String resource){ this.resource = resource; }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){ this.username = username; }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getDate() {
        return date;
    }

    public String getResourceDecrypted() {
        try {
            return EncryptionUtils.decrypt(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setResourceEncrypted(String resource) {
        try {
            this.resource = EncryptionUtils.encrypt(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getUsernameDecrypted() {
        try {
            return EncryptionUtils.decrypt(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setUsernameEncrypted(String username) {
        try {
            this.username = EncryptionUtils.encrypt(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String getPasswordDecrypted() {
        try {
            return EncryptionUtils.decrypt(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setPasswordEncrypted(String password) {
        try {
            this.password = EncryptionUtils.encrypt(password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
