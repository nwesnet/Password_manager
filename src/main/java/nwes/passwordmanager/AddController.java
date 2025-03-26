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

    private TextField accountResourceField;
    private TextField accountUsernameField;
    private PasswordField accountPasswordField;
    private Button saveButton;
    private Button cancelButton;

    private TextField linkField;

    private TextField cardResourceField;
    private TextField cardNumberField;
    private TextField cardDateField;
    private TextField cardCVVField;
    private TextField cardNameField;
    private TextField cardTypePayField;

    private TextField walletResourceField;
    private TextArea walletWordsField;
    private PasswordField walletPasswordField;

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

        accountResourceField = new TextField();
        accountUsernameField = new TextField();
        accountPasswordField = new PasswordField();

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                accountResourceField.getText(),
                accountUsernameField.getText(),
                accountPasswordField.getText()
        ));
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        HBox hbResource = new HBox();
        HBox hbUsername = new HBox();
        HBox hbPassword = new HBox();
        HBox hbButtons = new HBox();

        hbResource.getChildren().addAll(resourceLabel, accountResourceField);
        hbUsername.getChildren().addAll(usrenameLabel, accountUsernameField);
        hbPassword.getChildren().addAll(passwordLabel, accountPasswordField);
        hbButtons.getChildren().addAll(saveButton, cancelButton);
        vbAdd.getChildren().addAll(hbResource, hbUsername, hbPassword, hbButtons);
    }
    private void showLinkForm() {
        vbAdd.getChildren().clear();

        Label resourceLabel = new Label("Resource:");
        TextField resourceField = new TextField();

        Label linkLabel = new Label("Link:");
        TextField linkField = new TextField();

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String resource = resourceField.getText();
            String url = linkField.getText();
            if (!resource.isEmpty() && !url.isEmpty()) {
                onSaveButtonClick(resource, url);
                onCancelButtonClick(); // or clear fields
            }
        });

        cancelButton.setOnAction(e -> onCancelButtonClick());

        VBox formBox = new VBox(10,
                new HBox(5, resourceLabel, resourceField),
                new HBox(5, linkLabel, linkField),
                new HBox(10, saveButton, cancelButton)
        );

        vbAdd.getChildren().add(formBox);
    }

    private void showCardForm(){
        // I clear the vbox that I use for flexible interface
        vbAdd.getChildren().clear();
        // I create the labels for all fields
        Label resourceLabel = new Label("Resource");
        Label numberLabel = new Label("Number");
        Label dateLabel = new Label("Date");
        Label CVVLabel = new Label("CVV");
        Label nameLabel = new Label("Name");
        Label typepayLabel = new Label("Pay system");
        // I define the fields and set up it
        cardResourceField = new TextField();

        cardNumberField = new TextField();
        cardNumberField.setPrefColumnCount(24);

        cardDateField = new TextField();

        cardCVVField = new TextField();

        cardNameField = new TextField();
        cardNameField.setPrefColumnCount(24);

        cardTypePayField = new TextField();

        saveButton = new Button("save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                cardResourceField.getText(),
                cardNumberField.getText(),
                cardDateField.getText(),
                cardCVVField.getText(),
                cardNameField.getText(),
                cardTypePayField.getText()
        ));
        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        // I create HBox for all fields and buttons to make layout looks better
        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, cardResourceField);
        HBox hbNumber = new HBox(5);
        hbNumber.getChildren().addAll(numberLabel, cardNumberField);
        HBox hbDateAndCVV = new HBox(40);
        hbDateAndCVV.getChildren().addAll(dateLabel, cardDateField, CVVLabel, cardCVVField);
        HBox hbName = new HBox(5);
        hbName.getChildren().addAll(nameLabel, cardNameField);
        HBox hbTypepay = new HBox(5);
        hbTypepay.getChildren().addAll(typepayLabel, cardTypePayField);
        HBox hbButtons = new HBox(5);
        hbButtons.getChildren().addAll(saveButton, cancelButton);
        // I add the items in VBox
        vbAdd.getChildren().addAll(hbResource, hbNumber, hbDateAndCVV, hbName, hbTypepay, hbButtons);
    }
    private void showWalletForm(){
        vbAdd.getChildren().clear();

        Label resourceLabel = new Label("Resource");
        Label wordsLabel = new Label("12 words");
        Label passwordLabel = new Label("Pin");

        walletResourceField = new TextField();
        walletWordsField = new TextArea();
        walletPasswordField = new PasswordField();

        saveButton = new Button("save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                walletResourceField.getText(),
                walletWordsField,
                walletPasswordField.getText()
        ));
        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, walletResourceField);
        HBox hbWords = new HBox(5);
        hbWords.getChildren().addAll(wordsLabel, walletWordsField);
        HBox hbPassword = new HBox(5);
        hbPassword.getChildren().addAll(passwordLabel, walletPasswordField);
        HBox hbButtons = new HBox(5);
        hbButtons.getChildren().addAll(saveButton, cancelButton);

        vbAdd.getChildren().addAll(hbResource, hbWords, hbPassword, hbButtons);
    }
    private void showTextForm(){
        vbAdd.getChildren().clear();
        Label label = new Label("text");
        vbAdd.getChildren().addAll(label);
    }
    private void onSaveButtonClick(String resource, String username, String password) {
        DatabaseManager dm = new DatabaseManager();
        LocalDateTime date = LocalDateTime.now();
        if (!resource.isEmpty() || !username.isEmpty() || !password.isEmpty()) {
            dm.writeAccountTodb(resource, username, password, date);
        }
    }
    private void onSaveButtonClick(String link, String url){
        DatabaseManager dm = new DatabaseManager();
        if (!link.isEmpty()){
            dm.writeLinkTodb(link, url);
        }
    }
    private void onSaveButtonClick(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String systemType){
        DatabaseManager dm = new DatabaseManager();
        LocalDateTime date = LocalDateTime.now();
        if (!resource.isEmpty() || !cardNumber.isEmpty() || !expiryDate.isEmpty() || !cvv.isEmpty() || !ownerName.isEmpty()){
            dm.writeCardTodb(resource, cardNumber, expiryDate, cvv, ownerName, systemType, date);
        }
    }
    private void onSaveButtonClick(String resource, TextArea words, String password){
        DatabaseManager dm = new DatabaseManager();
        LocalDateTime date = LocalDateTime.now();

        String wordsText = words.getText().trim();
        String[] wordsArray = wordsText.split("\\s+");

        if (wordsArray.length < 12 || wordsArray.length > 24) {
            System.out.println("❌ Invalid number of words. Must be between 12 and 24.");
            return;
        }
        String wordsString = String.join(",", wordsArray);

        if (!resource.isEmpty() || !wordsString.isEmpty() || !password.isEmpty()){
            dm.writeWalletTodb(resource, wordsString, password, date);
        }
    }
    private void onCancelButtonClick(){
        Stage addStage = (Stage) cancelButton.getScene().getWindow();
        addStage.close();
    }
}
