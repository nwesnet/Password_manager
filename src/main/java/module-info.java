module nwes.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens nwes.passwordmanager to javafx.fxml;
    exports nwes.passwordmanager;
}