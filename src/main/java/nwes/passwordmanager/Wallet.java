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
