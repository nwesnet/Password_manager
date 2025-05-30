package nwes.passwordmanager;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Wallet {
    private String id;
    private String resource;
    private String[] twelveWords;
    private String address;
    private String password;
    private String ownerUsername;
    private LocalDateTime dateAdded;
    private LocalDateTime lastModified;
    private String deleted;
    private String sync;

    public Wallet(String id,
                  String resource, String[] twelveWords, String address, String password,
                  String ownerUsername,
                  LocalDateTime dateAdded, LocalDateTime lastModified,
                  String deleted, String sync
    ) {
        this.id = id;
        this.resource = resource;
        this.twelveWords = twelveWords;
        this.address = address;
        this.password = password;
        this.ownerUsername = ownerUsername;
        this.dateAdded = dateAdded;
        this.lastModified = lastModified;
        this.deleted = deleted;
        this.sync = sync;
    }
    // Getters and Setters
    // id
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
    public void setResource(String resource) {
        this.resource = resource;
    }
    // Keywords array
    public String[] getTwelveWords() {
        return twelveWords;
    }
    public void setTwelveWords(String[] twelveWords) {
        this.twelveWords = twelveWords;
    }
    // Address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    // Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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
    // Deleted
    public String getDeleted() {
        return deleted;
    }
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    // Sync
    public String getSync() {
        return sync;
    }
    public void setSync(String sync) {
        this.sync = sync;
    }

    // Encrypted & Decrypted Getters and Setter
    // resource
    public String getResourceDecrypted() {
        try {
            return EncryptionUtils.decrypt(resource);
        } catch (Exception e) {
            System.out.println("❌ Wallet decryption failed: " + e.getMessage());
            System.out.println("🔐 Current appKey: " + EncryptionUtils.getAppKey());
            System.out.println("🔐 Resource Encrypted Value: " + resource);
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
    // keywords array
    public String[] getTwelveWordsDecrypted() {
        try {
            String[] decrypted = new String[twelveWords.length];
            for (int i = 0; i < twelveWords.length; i++) {
                decrypted[i] = EncryptionUtils.decrypt(twelveWords[i]);
            }
            return decrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setTwelveWordsEncrypted(String[] twelveWords) {
        try {
            String[] encrypted = new String[twelveWords.length];
            for (int i = 0; i < twelveWords.length; i++) {
                encrypted[i] = EncryptionUtils.encrypt(twelveWords[i]);
            }
            this.twelveWords = encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // address
    public String getAddressDecrypted() {
        try {
            return EncryptionUtils.decrypt(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setAddressEncrypted(String address) {
        try {
            this.address = EncryptionUtils.encrypt(address);
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

    public String getTwelveWordsRaw() {
        return String.join(",", twelveWords);
    }

    // equals & hashcode redirect
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return resource.equals(wallet.resource) &&
                Arrays.equals(twelveWords, wallet.twelveWords) &&
                address.equals(wallet.address) &&
                password.equals(wallet.password);
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + Arrays.hashCode(twelveWords);
        result = 31 * result + address.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }


}
