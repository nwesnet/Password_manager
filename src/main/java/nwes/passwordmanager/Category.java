package nwes.passwordmanager;

public enum Category {
    ACCOUNTS("Accounts"),
    CARDS("Cards"),
    LINKS("Links"),
    WALLETS("Wallets");

    private final String displayName;

    Category(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
