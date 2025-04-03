package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        // clear vbox
        vbAdd.getChildren().clear();
        // create gridpane, set spacing and padding, implement column sizes
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(5);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints(80);
        ColumnConstraints inputColumn = new ColumnConstraints(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);

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

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(accountResourceField, 1, 0);

        grid.add(new Label("Login:"), 0, 1);
        grid.add(accountUsernameField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(accountPasswordField, 1, 2);

        HBox hb = new HBox(5,
                saveButton, cancelButton
        );
        hb.setAlignment(Pos.CENTER_LEFT);

        vbAdd.getChildren().addAll(grid, hb);
    }

    private void showCardForm(){
        // I clear the vbox that I use for flexible interface
        vbAdd.getChildren().clear();
        // Create gridpane, set spacing and padding, implement column size
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        ColumnConstraints labelColumn = new ColumnConstraints(80);
        ColumnConstraints inputColumn = new ColumnConstraints(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);
        // I define the fields and set up it
        cardResourceField = new TextField();
        cardResourceField.setPrefColumnCount(24);

        cardNumberField = new TextField();
        cardNumberField.setPrefColumnCount(20);

        cardDateField = new TextField();
        cardDateField.setPrefColumnCount(6);

        cardCVVField = new TextField();
        cardCVVField.setPrefColumnCount(10);

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

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(cardResourceField, 1, 0);

        grid.add(new Label("Number:"), 0, 1);
        grid.add(cardNumberField, 1, 1);

        grid.add(new Label("Date:"), 0, 2);
        grid.add(cardDateField, 1, 2);

        grid.add(new Label("Owner:"), 0, 3);
        grid.add(cardNameField, 1, 3);

        VBox formBox = new VBox(5,
                new HBox(5, new Label("CVV:                   "), cardCVVField),
                new HBox(5,new Label("Pin:                    "), cardPinField),
                new HBox(5, new Label("Pay network:   "), cardTypePayField),
                new HBox(5, new Label("Card type:        "), cardTypeField),
                new HBox(5, saveButton, cancelButton)
        );

        // I add the items in VBox
        vbAdd.getChildren().addAll(grid, formBox);
    }

    private void showLinkForm() {
        // Clear vbox
        vbAdd.getChildren().clear();
        // Create gridpane, set spacing and padding, implement column size
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(5);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints(80);
        ColumnConstraints inputColumn = new ColumnConstraints(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);

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

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);

        grid.add(new Label("Link:"), 0, 1);
        grid.add(linkField, 1, 1);

        VBox formBox = new VBox(10,
                new HBox(10, saveButton, cancelButton)
        );


        vbAdd.getChildren().addAll(grid, formBox);
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
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logAdd("Account", resource);
            }
        }
    }

    private void onSaveButtonClick(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String pincode, String networkType, String cardType){
        DatabaseManager dm = new DatabaseManager();
        LocalDateTime date = LocalDateTime.now();
        if (!resource.isEmpty() || !cardNumber.isEmpty() || !expiryDate.isEmpty() || !cvv.isEmpty() || !ownerName.isEmpty()){
            dm.writeCardTodb(resource, cardNumber, expiryDate, cvv, ownerName, pincode, networkType, cardType, date);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logAdd("Card", resource);
            }
        }
    }
    private void onSaveButtonClick(String resource, String link){
        DatabaseManager dm = new DatabaseManager();
        if (!resource.isEmpty() || !link.isEmpty()){
            dm.writeLinkTodb(resource, link);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logAdd("Link", resource);
            }
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
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logAdd("Wallet", resource);
            }
        }
    }
    private void onCancelButtonClick(){
        Stage addStage = (Stage) cancelButton.getScene().getWindow();
        addStage.close();
    }
}
