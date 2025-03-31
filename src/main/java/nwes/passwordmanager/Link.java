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

    @Override
    public String toString() {
        return "Link{" +
                "resource='" + resource + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
