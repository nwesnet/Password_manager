package nwes.passwordmanager;

import java.time.LocalDateTime;

public class Link implements ItemEntity {
    private String id;
    private String resource;
    private String link;
    private String ownerUsername;
    private LocalDateTime dateAdded;
    private LocalDateTime lastModified;
    private String deleted;
    private String sync;

    public Link(
            String id,
            String resource, String link,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ){
        this.id = id;
        this.resource = resource;
        this.link = link;
        this.ownerUsername = ownerUsername;
        this.dateAdded = dateAdded;
        this.lastModified = lastModified;
        this.deleted = deleted;
        this.sync = sync;
    }

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
    public void setResource(String resource){
        this.resource = resource;
    }
    // Link URL
    public String getLink(){
        return link;
    }
    public void setLink(String link){
        this.link = link;
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
    // link url
    public String getLinkDecrypted() {
        try {
            return EncryptionUtils.decrypt(link);
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

        Link linkObj = (Link) o;

        return resource.equals(linkObj.resource) &&
                link.equals(linkObj.link);
    }

    @Override
    public int hashCode() {
        int result = resource.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }

}
