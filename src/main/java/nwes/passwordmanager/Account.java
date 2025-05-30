package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Account {
    private String resource;
    private String username;
    private String password;
    private LocalDateTime dateAdded;

    public Account(String resource, String username, String password, LocalDateTime dateAdded){
        this.resource = resource;
        this.username = username;
        this.password = password;
        this.dateAdded = dateAdded;
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

    public LocalDateTime getDateAdded() {
        return dateAdded;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return resource.equals(account.resource) &&
                username.equals(account.username) &&
                password.equals(account.password);
    }
    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();

        return result;
    }
}
