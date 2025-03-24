module nwes.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;


    opens nwes.passwordmanager to javafx.fxml, com.google.gson;
    exports nwes.passwordmanager;
}