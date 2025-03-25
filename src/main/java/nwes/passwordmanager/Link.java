package nwes.passwordmanager;

public class Link {
    private String resource;
    public Link(String resource){
        this.resource=resource;
    }

    public String getResource() {
        return resource;
    }
    public void setResource(String resource){
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Link{" +
                "resource='" + resource  + "'}";
    }
}
