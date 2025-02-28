package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AddController {
    @FXML
    private ComboBox<String> selectDataType;
    @FXML
    private BorderPane BPAdd;
    @FXML
    private VBox vbAdd;
    @FXML
    public void initialize(){
        selectDataType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
           handleSelection(newValue);
        });
        handleSelection(selectDataType.getValue());
    }
    private void handleSelection(String selectedItem){
        switch (selectedItem){
            case "Account":
                showAccountForm();
                break;
            case "Link":
                showLinkForm();
                break;
            case "Card":
                showCardForm();
                break;
            case "Wallet":
                showWalletForm();
                break;
            case "Text":
                showTextForm();
                break;
        }
    }
    private void showAccountForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("account");
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        HBox hb = new HBox();
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");
        hb.getChildren().addAll(addButton,cancelButton);
        vbAdd.getChildren().addAll(label,usernameField,passwordField,hb);
    }
    private void showLinkForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("link");
        vbAdd.getChildren().addAll(label);
    }
    private void showCardForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("card");
        vbAdd.getChildren().addAll(label);
    }
    private void showWalletForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("wallet");
        vbAdd.getChildren().addAll(label);
    }
    private void showTextForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("text");
        vbAdd.getChildren().addAll(label);
    }
}
