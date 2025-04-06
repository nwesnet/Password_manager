package nwes.passwordmanager;

public class Link {
    private String resource;
    private String link;
    public Link(String resource, String link){
        this.resource = resource;
        this.link = link;
    }

    public String getResource() {
        return resource;
    }
    public String getLink(){
        return link;
    }

    public void setResource(String resource){
        this.resource = resource;
    }
    public void setLink(String link){
        this.link = link;
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
                '}';
    }
}
