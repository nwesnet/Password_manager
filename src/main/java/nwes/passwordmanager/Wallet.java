package nwes.passwordmanager;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Wallet {
    private String resource;
    private String[] twelveWords;
    private String address;
    private String password;
    private LocalDateTime dateAdded;

    public Wallet(String resource, String[] twelveWords, String address, String password, LocalDateTime dateAdded) {
        this.resource = resource;
        this.twelveWords = twelveWords;
        this.address = address;
        this.password = password;
        this.dateAdded = dateAdded;
    }

    public String getResource() {
        return resource;
    }

    public String[] getTwelveWords() {
        return twelveWords;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setTwelveWords(String[] twelveWords) {
        this.twelveWords = twelveWords;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 🔐 Decrypted Getters
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

    public String getAddressDecrypted() {
        try {
            return EncryptionUtils.decrypt(address);
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

    // 🔐 Encrypted Setters
    public void setResourceEncrypted(String resource) {
        try {
            this.resource = EncryptionUtils.encrypt(resource);
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

    public void setAddressEncrypted(String address) {
        try {
            this.address = EncryptionUtils.encrypt(address);
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

    public String getTwelveWordsRaw() {
        return String.join(",", twelveWords);
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "resource='" + resource + '\'' +
                ", twelveWords=" + Arrays.toString(twelveWords) +
                ", password='" + password + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
