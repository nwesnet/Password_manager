package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Card implements ItemEntity{
    private String id;
    private String resource;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String ownerName;
    private String cardPin;
    private String cardNetwork;
    private String cardType;
    private String ownerUsername;
    private LocalDateTime dateAdded;
    private LocalDateTime lastModified;
    private String deleted;
    private String sync;

    public Card(String id,
                String resource, String cardNumber, String expiryDate, String cvv, String ownerName,
                String cardPin,
                String cardNetwork, String cardType,
                String ownerUsername,
                LocalDateTime dateAdded,
                LocalDateTime lastModified,
                String deleted, String sync
    ) {
        this.id = id;
        this.resource = resource;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.ownerName = ownerName;
        this.cardPin = cardPin;
        this.cardNetwork = cardNetwork;
        this.cardType = cardType;
        this.ownerUsername = ownerUsername;
        this.dateAdded = dateAdded;
        this.lastModified = lastModified;
        this.deleted = deleted;
        this.sync = sync;
    }

    // Getters and setters
    // idForItem
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
    // Card number
    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    // Expiry date
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    // CVV
    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    // Owner name
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String onwerName) {
        this.ownerName = onwerName;
    }
    // Pin code
    public String getCardPin() {
        return cardPin;
    }
    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }
    // Card network type
    public String getCardNetwork() {
        return cardNetwork;
    }
    public void setCardNetwork(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }
    // Card type
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    // Encrypt & Decrypted Getters and Setters
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
    // card number
    public String getCardNumberDecrypted() {
        try {
            return EncryptionUtils.decrypt(cardNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCardNumberEncrypted(String cardNumber) {
        try {
            this.cardNumber = EncryptionUtils.encrypt(cardNumber);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // expiry date
    public String getExpiryDateDecrypted() {
        try {
            return EncryptionUtils.decrypt(expiryDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setExpiryDateEncrypted(String expiryDate) {
        try {
            this.expiryDate = EncryptionUtils.encrypt(expiryDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // cvv
    public String getCvvDecrypted() {
        try {
            return EncryptionUtils.decrypt(cvv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCvvEncrypted(String cvv) {
        try {
            this.cvv = EncryptionUtils.encrypt(cvv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // owner name
    public String getOwnerNameDecrypted() {
        try {
            return EncryptionUtils.decrypt(ownerName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setOwnerNameEncrypted(String ownerName) {
        try {
            this.ownerName = EncryptionUtils.encrypt(ownerName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // pin code
    public String getCardPincodeDecrypted() {
        try {
            return EncryptionUtils.decrypt(cardPin);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCardPincodeEncrypted(String cardPincode) {
        try {
            this.cardPin = EncryptionUtils.encrypt(cardPincode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // card network type
    public String getCardNetworkTypeDecrypted() {
        try {
            return EncryptionUtils.decrypt(cardNetwork);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCardNetworkTypeEncrypted(String cardNetworkType) {
        try {
            this.cardNetwork = EncryptionUtils.encrypt(cardNetworkType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // card type
    public String getCardTypeDecrypted() {
        try {
            return EncryptionUtils.decrypt(cardType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void setCardTypeEncrypted(String cardType) {
        try {
            this.cardType = EncryptionUtils.encrypt(cardType);
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

        Card card = (Card) o;

        return resource.equals(card.resource) &&
                cardNumber.equals(card.cardNumber) &&
                expiryDate.equals(card.expiryDate) &&
                cvv.equals(card.cvv) &&
                ownerName.equals(card.ownerName) &&
                cardNetwork.equals(card.cardNetwork) &&
                cardType.equals(card.cardType);
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + cardNumber.hashCode();
        result = 31 * result + expiryDate.hashCode();
        result = 31 * result + cvv.hashCode();
        result = 31 * result + ownerName.hashCode();
        result = 31 * result + cardNetwork.hashCode();
        result = 31 * result + cardType.hashCode();
        return result;
    }

}
