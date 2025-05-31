package nwes.passwordmanager;

import java.time.LocalDateTime;

public interface ItemEntity {
    String getId();
    void setId(String id);

    String getOwnerUsername();
    void setOwnerUsername(String ownerUsername);

    LocalDateTime getLastModified();
    void setLastModified(LocalDateTime lastModified);

    String getDeleted();
    void setDeleted(String deleted);

    String getSync();
    void setSync(String sync);
}
