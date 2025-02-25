module nwes.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens nwes.passwordmanager to javafx.fxml;
    exports nwes.passwordmanager;
}