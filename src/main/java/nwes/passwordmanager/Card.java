package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Card {
    private String resource;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String onwerName;
    private LocalDateTime dateAdded;

    public Card(String resource, String cardNumber, String expiryDate, String cvv, String onwerName, LocalDateTime dateAdded) {
        this.resource = resource;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.onwerName = onwerName;
        this.dateAdded = dateAdded;
    }

    public String getResource() {
        return resource;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public String getOnwerName() {
        return onwerName;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    @Override
    public String toString() {
        return "Card{" +
                "resource='" + resource + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", onwerName='" + onwerName + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
