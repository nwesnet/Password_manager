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
    private TextField cardPinField;
    private TextField cardTypePayField;
    private TextField cardTypeField;

    private TextField walletResourceField;
    private TextArea walletWordsField;
    private TextField walletAddressField;
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

        accountResourceField = new TextField();
        accountResourceField.setPrefColumnCount(24);
        accountUsernameField = new TextField();
        accountUsernameField.setPrefColumnCount(24);
        accountPasswordField = new PasswordField();
        accountPasswordField.setPrefColumnCount(24);

        saveButton = new Button("Save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                accountResourceField.getText(),
                accountUsernameField.getText(),
                accountPasswordField.getText()
        ));
        cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        VBox formBox = new VBox(10,
                new HBox(5, new Label("Resource:   "), accountResourceField),
                new HBox(5, new Label("Username: "), accountUsernameField),
                new HBox(5, new Label("Password:  "), accountPasswordField),
                new HBox(10, saveButton, cancelButton)
        );

        vbAdd.getChildren().addAll(formBox);
    }

    private void showCardForm(){
        // I clear the vbox that I use for flexible interface
        vbAdd.getChildren().clear();
        // I define the fields and set up it
        cardResourceField = new TextField();
        cardResourceField.setPrefColumnCount(24);

        cardNumberField = new TextField();
        cardNumberField.setPrefColumnCount(20);

        cardDateField = new TextField();
        cardDateField.setPrefColumnCount(6);

        cardCVVField = new TextField();
        cardCVVField.setPrefColumnCount(6);

        cardNameField = new TextField();
        cardNameField.setPrefColumnCount(20);

        cardPinField = new TextField();
        cardPinField.setPrefColumnCount(10);

        cardTypePayField = new TextField();
        cardTypePayField.setPrefColumnCount(10);

        cardTypeField = new TextField();
        cardTypeField.setPrefColumnCount(10);

        saveButton = new Button("save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                cardResourceField.getText(),
                cardNumberField.getText(),
                cardDateField.getText(),
                cardCVVField.getText(),
                cardNameField.getText(),
                cardPinField.getText(),
                cardTypePayField.getText(),
                cardTypeField.getText()
        ));
        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        VBox formBox = new VBox(10,
                new HBox(5, new Label("Resource: "), cardResourceField),
                new HBox(5, new Label("Card number: "), cardNumberField),
                new HBox(5, new Label("Date:                 "), cardDateField, new Label("          CVV: "), cardCVVField),
                new HBox(5, new Label("Name:              "), cardNameField),
                new HBox(5,new Label("Pin:                    "), cardPinField),
                new HBox(5, new Label("Pay network:   "), cardTypePayField),
                new HBox(5, new Label("Card type:        "), cardTypeField),
                new HBox(10, saveButton, cancelButton)
        );

        // I add the items in VBox
        vbAdd.getChildren().addAll(formBox);
    }

    private void showLinkForm() {
        vbAdd.getChildren().clear();

        TextField resourceField = new TextField();
        TextField linkField = new TextField();

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");

        saveButton.setOnAction(e -> {
            String resource = resourceField.getText();
            String url = linkField.getText();
            if (!resource.isEmpty() || !url.isEmpty()) {
                onSaveButtonClick(resource, url);
                onCancelButtonClick(); // or clear fields
            }
        });

        cancelButton.setOnAction(e -> onCancelButtonClick());

        VBox formBox = new VBox(10,
                new HBox(5, new Label("Resource: "), resourceField),
                new HBox(5, new Label("Link:          "), linkField),
                new HBox(10, saveButton, cancelButton)
        );

        vbAdd.getChildren().add(formBox);
    }
    private void showWalletForm(){
        vbAdd.getChildren().clear();

        walletResourceField = new TextField();
        walletResourceField.setPrefColumnCount(24);
        walletWordsField = new TextArea();
        walletWordsField.setPrefColumnCount(23);
        walletWordsField.setPrefRowCount(4);
        walletAddressField = new TextField();
        walletAddressField.setPrefColumnCount(24);
        walletPasswordField = new PasswordField();
        walletPasswordField.setPrefColumnCount(24);

        saveButton = new Button("save");
        saveButton.setOnAction(e -> onSaveButtonClick(
                walletResourceField.getText(),
                walletWordsField,
                walletAddressField.getText(),
                walletPasswordField.getText()
        ));
        cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> onCancelButtonClick());

        VBox formBox = new VBox(10,
                new HBox(5, new Label("Resource:   "), walletResourceField),
                new HBox(5, new Label("Key words: "), walletWordsField),
                new HBox(5, new Label("Address:     "), walletAddressField),
                new HBox(5, new Label("Password:  "), walletPasswordField),
                new HBox(10, saveButton, cancelButton)
                );

        vbAdd.getChildren().addAll(formBox);
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
            LogsManager.logAdd("Account", resource);
        }
    }

    private void onSaveButtonClick(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String pincode, String networkType, String cardType){
        DatabaseManager dm = new DatabaseManager();
        LocalDateTime date = LocalDateTime.now();
        if (!resource.isEmpty() || !cardNumber.isEmpty() || !expiryDate.isEmpty() || !cvv.isEmpty() || !ownerName.isEmpty()){
            dm.writeCardTodb(resource, cardNumber, expiryDate, cvv, ownerName, pincode, networkType, cardType, date);
            LogsManager.logAdd("Card", resource);
        }
    }
    private void onSaveButtonClick(String resource, String link){
        DatabaseManager dm = new DatabaseManager();
        if (!resource.isEmpty() || !link.isEmpty()){
            dm.writeLinkTodb(resource, link);
            LogsManager.logAdd("Link", resource);
        }
    }
    private void onSaveButtonClick(String resource, TextArea words, String address, String password){
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
            dm.writeWalletTodb(resource, wordsString, address, password, date);
            LogsManager.logAdd("Wallet", resource);
        }
    }
    private void onCancelButtonClick(){
        Stage addStage = (Stage) cancelButton.getScene().getWindow();
        addStage.close();
    }
}
