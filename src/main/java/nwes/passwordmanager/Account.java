package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Account implements ItemEntity{
    private String id; // UUID
    private String resource;
    private String username;
    private String password;
    private String ownerUsername;
    private LocalDateTime dateAdded;
    private LocalDateTime lastModified;
    private String deleted;
    private String sync;

    public Account(
            String id,
            String resource, String username, String password,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ){
        this.id = id;
        this.resource = resource;
        this.username = username;
        this.password = password;
        this.ownerUsername = ownerUsername;
        this.dateAdded = dateAdded;
        this.lastModified = lastModified;
        this.deleted = deleted;
        this.sync = sync;
    }
    // Mb I will use it
//    public Account(String resource, String username, String password, String ownerUsername) {
//        this.id = java.util.UUID.randomUUID().toString();
//        this.resource = resource;
//        this.username = username;
//        this.password = password;
//        this.ownerUsername = ownerUsername;
//        this.dateAdded = LocalDateTime.now();
//        this.lastModified = this.dateAdded;
//        this.deleted = false;
//        this.sync = false;
//    }
    // Getters and Setters
    // IdForItem
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    // Resource
    public String getResource() {
        return resource;
    }
    public void setResource(String resource){ this.resource = resource; }
    // Username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username){ this.username = username; }
    // Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) { this.password = password; }
    // OwnerUsername
    public String getOwnerUsername() {
        return ownerUsername;
    }
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
    // Date added
    public LocalDateTime getDateAdded() {
        return dateAdded;
    }
    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
    // Last modified date
    public LocalDateTime getLastModified() {
        return lastModified;
    }
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
    // Deleted status
    public String getDeleted() {
        return deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    // Sync status
    public String getSync() {
        return sync;
    }
    public void setSync(String sync) {
        this.sync = sync;
    }

    // Encrypted & Decrypted Getters and Setters
    // resource
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
    // username
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
    // password
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
    // owner username
    public String getOwnerUsernameDecrypted() {
        try {
            return EncryptionUtils.decrypt(ownerUsername);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setOwnerUsernameEncrypted(String ownerUsername) {
        try {
            this.ownerUsername = EncryptionUtils.encrypt(ownerUsername);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // equals & hashcode redirect
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
