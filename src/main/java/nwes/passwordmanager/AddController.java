package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AddController {
    @FXML
    private ComboBox<String> selectDataType;
    @FXML
    private BorderPane BPAdd;
    @FXML
    private VBox vbAdd;
    private TextField resourceField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button saveButton;
    private Button cancelButton;
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
        Label resourceLabel = new Label("Resource");
        Label usrenameLabel = new Label("Username");
        Label passwordLabel = new Label("Password");
        resourceField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        HBox hbResource = new HBox();
        HBox hbUsername = new HBox();
        HBox hbPassword = new HBox();
        HBox hbButtons = new HBox();
        saveButton = new Button("Save");
        saveButton.setOnAction(e -> onSaveButtonClick());
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());
        hbResource.getChildren().addAll(resourceLabel, resourceField);
        hbUsername.getChildren().addAll(usrenameLabel, usernameField);
        hbPassword.getChildren().addAll(passwordLabel, passwordField);
        hbButtons.getChildren().addAll(saveButton, cancelButton);
        vbAdd.getChildren().addAll(hbResource, hbUsername, hbPassword, hbButtons);
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
    private void onSaveButtonClick() {
        DatabaseManager dm = new DatabaseManager();
        String resource = resourceField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        LocalDateTime date = LocalDateTime.now();
        if (!resource.isEmpty() || !username.isEmpty() || !password.isEmpty()) {
            dm.writeAccountTodb(resource, username, password, date);
        }
    }
    private void onCancelButtonClick(){
        Stage addStage = (Stage) cancelButton.getScene().getWindow();
        addStage.close();
    }
}
