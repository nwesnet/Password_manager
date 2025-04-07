package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Link {
    private String resource;
    private String link;
    private LocalDateTime date;
    public Link(String resource, String link, LocalDateTime date){
        this.resource = resource;
        this.link = link;
        this.date = date;
    }

    public String getResource() {
        return resource;
    }
    public String getLink(){
        return link;
    }
    public LocalDateTime getDate() {
        return date;
    }

    public void setResource(String resource){
        this.resource = resource;
    }
    public void setLink(String link){
        this.link = link;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // 🔐 Decrypted Getters
    public String getResourceDecrypted() {
        try {
            return EncryptionUtils.decrypt(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getLinkDecrypted() {
        try {
            return EncryptionUtils.decrypt(link);
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

    public void setLinkEncrypted(String link) {
        try {
            this.link = EncryptionUtils.encrypt(link);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Link{" +
                "resource='" + resource + '\'' +
                ", link='" + link + '\'' +
                ", date=" + date +
                '}';
    }
}
