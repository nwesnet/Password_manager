package nwes.passwordmanager;

import java.time.LocalDateTime;
import java.util.Arrays;

public class Wallet {
    private String resource;
    private String[] twelveWords;
    private String password;
    private LocalDateTime dateAdded;

    public Wallet(String resource, String[] twelveWords, String password, LocalDateTime dateAdded) {
        this.resource = resource;
        this.twelveWords = twelveWords;
        this.password = password;
        this.dateAdded = dateAdded;
    }

    public String getResource() {
        return resource;
    }

    public String[] getTwelveWords() {
        return twelveWords;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
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
