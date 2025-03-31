package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Card {
    private String resource;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String ownerName;
    private String cardPincode;
    private String cardNetworkType;
    private String cardType;
    private LocalDateTime dateAdded;

    public Card(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String cardPincode, String cardNetworkType, String cardType, LocalDateTime dateAdded) {
        this.resource = resource;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.ownerName = ownerName;
        this.cardPincode = cardPincode;
        this.cardNetworkType = cardNetworkType;
        this.cardType = cardType;
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
        return ownerName;
    }

    public String getCardPincode() {
        return cardPincode;
    }

    public String getCardNetworkType() {
        return cardNetworkType;
    }

    public String getCardType() {
        return cardType;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setOnwerName(String onwerName) {
        this.ownerName = onwerName;
    }

    public void setCardPincode(String cardPincode) {
        this.cardPincode = cardPincode;
    }

    public void setCardNetworkType(String cardNetworkType) {
        this.cardNetworkType = cardNetworkType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Card{" +
                "resource='" + resource + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", onwerName='" + ownerName + '\'' +
                ", cardPincode='" + cardPincode + '\'' +
                ", cardNetworkType='" + cardNetworkType + '\'' +
                ", cardType='" + cardType + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
