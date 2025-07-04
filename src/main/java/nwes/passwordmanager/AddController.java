package nwes.passwordmanager;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class AddController {
    @FXML
    private ComboBox<String> selectDataType;
    @FXML
    private CheckBox cbSync;
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
        cbSync.setSelected(PreferencesManager.isSyncEnabled());
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
        saveButton.setOnAction(e -> {
            boolean saved = onSaveButtonClick(
                    accountResourceField.getText(),
                    accountUsernameField.getText(),
                    accountPasswordField.getText()
            );
            if(saved) {
                accountResourceField.clear();
                accountUsernameField.clear();
                accountPasswordField.clear();
                saveButton.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            } else {
                saveButton.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> saveButton.setStyle(null));
            pause.play();
        });
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
        saveButton.setOnAction(e -> {
            boolean saved = onSaveButtonClick(
                    cardResourceField.getText(),
                    cardNumberField.getText(),
                    cardDateField.getText(),
                    cardCVVField.getText(),
                    cardNameField.getText(),
                    cardPinField.getText(),
                    cardTypePayField.getText(),
                    cardTypeField.getText()
            );
            if (saved) {
                cardResourceField.clear();
                cardNumberField.clear();
                cardDateField.clear();
                cardCVVField.clear();
                cardNameField.clear();
                cardPinField.clear();
                cardTypePayField.clear();
                cardTypeField.clear();
                saveButton.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            } else {
                saveButton.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> saveButton.setStyle(null));
            pause.play();
        });
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
            boolean saved = onSaveButtonClick(
                    resourceField.getText(),
                    linkField.getText()
            );
            if (saved) {
                resourceField.clear();
                linkField.clear();
                saveButton.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            } else {
                saveButton.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> saveButton.setStyle(null));
            pause.play();
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
    private void showWalletForm() {
        vbAdd.getChildren().clear();

        walletResourceField = new TextField();
        walletResourceField.setPrefColumnCount(24);
        walletWordsField = new TextArea();
        walletWordsField.setPrefColumnCount(23);
        walletWordsField.setPrefRowCount(4);
        walletWordsField.setWrapText(true);
        walletAddressField = new TextField();
        walletAddressField.setPrefColumnCount(24);
        walletPasswordField = new PasswordField();
        walletPasswordField.setPrefColumnCount(24);

        saveButton = new Button("save");
        saveButton.setOnAction(e -> {
            boolean saved = onSaveButtonClick(
                    walletResourceField.getText(),
                    walletWordsField.getText().trim(),
                    walletAddressField.getText(),
                    walletPasswordField.getText()
            );
            if (saved) {
                walletResourceField.clear();
                walletWordsField.clear();
                walletAddressField.clear();
                walletPasswordField.clear();
                saveButton.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
            } else {
                saveButton.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> saveButton.setStyle(null));
            pause.play();
        });
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
    private boolean onSaveButtonClick(String resource, String username, String password) {
        try {
            if (!resource.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                DatabaseManager dm = new DatabaseManager();

                String encryptedResource = EncryptionUtils.encrypt(resource);
                String encryptedUsername = EncryptionUtils.encrypt(username);
                String encryptedPassword = EncryptionUtils.encrypt(password);
                String ownerUsername = EncryptionUtils.encrypt(PreferencesManager.getUsername());

                if (dm.accountExists(encryptedResource, encryptedUsername, encryptedPassword, ownerUsername)) {
                    System.out.println("Account already exists, not saving");
                    return false;
                }

                LocalDateTime now = LocalDateTime.now();
                String id = java.util.UUID.randomUUID().toString();
                String deleted = "false";
                String sync = String.valueOf(cbSync.isSelected());


                dm.writeAccountTodb(id, encryptedResource, encryptedUsername, encryptedPassword, ownerUsername, now, now, deleted, sync);
                if ("true".equals(sync)) {
                    String err = SyncManager.addAccountOnServer(new Account(id, encryptedResource, encryptedUsername, encryptedPassword, ownerUsername, now, now, deleted, sync), PreferencesManager.getUsername(), PreferencesManager.getPassword());
                    if (err != null) {
                        System.out.println("Server add failed: " + err);
                    }
                }

                if (SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logAdd("Account", resource);
                }

                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Error encrypting data: " + e.getMessage());
        }
        return false;
    }


    private boolean onSaveButtonClick(
            String resource, String cardNumber, String expiryDate, String cvv, String ownerName,
            String pincode, String networkType, String cardType
    ) {
        try {
            if (!resource.isEmpty() && !cardNumber.isEmpty() && !expiryDate.isEmpty() &&
                    !cvv.isEmpty() && !ownerName.isEmpty() && !pincode.isEmpty() &&
                    !networkType.isEmpty() && !cardType.isEmpty()) {

                DatabaseManager dm = new DatabaseManager();
                LocalDateTime now = LocalDateTime.now();

                // Encrypt all fields
                String id = java.util.UUID.randomUUID().toString();
                String encryptedResource = EncryptionUtils.encrypt(resource);
                String encryptedCardNumber = EncryptionUtils.encrypt(cardNumber);
                String encryptedExpiryDate = EncryptionUtils.encrypt(expiryDate);
                String encryptedCvv = EncryptionUtils.encrypt(cvv);
                String encryptedOwnerName = EncryptionUtils.encrypt(ownerName);
                String encryptedPincode = EncryptionUtils.encrypt(pincode);
                String encryptedNetworkType = EncryptionUtils.encrypt(networkType);
                String encryptedCardType = EncryptionUtils.encrypt(cardType);
                String encryptedOwnerUsername = EncryptionUtils.encrypt(PreferencesManager.getUsername());
                String deleted = "false";
                String sync = String.valueOf(cbSync.isSelected());

                // Uniqueness check
                if (dm.cardExists(
                        encryptedResource, encryptedCardNumber, encryptedExpiryDate, encryptedOwnerName, encryptedCvv, encryptedOwnerUsername
                )) {
                    System.out.println("Card already exists, not saving");
                    return false;
                }

                dm.writeCardTodb(
                        id,
                        encryptedResource,
                        encryptedCardNumber,
                        encryptedExpiryDate,
                        encryptedCvv,
                        encryptedOwnerName,
                        encryptedPincode,
                        encryptedNetworkType,
                        encryptedCardType,
                        encryptedOwnerUsername,
                        now, now,
                        deleted,
                        sync
                );

                if ("true".equals(sync)) {
                    Card card = new Card(
                            id,
                            encryptedResource,
                            encryptedCardNumber,
                            encryptedExpiryDate,
                            encryptedCvv,
                            encryptedOwnerName,
                            encryptedPincode,
                            encryptedNetworkType,
                            encryptedCardType,
                            encryptedOwnerUsername,
                            now, now, deleted, sync
                    );
                    String err = SyncManager.addCardOnServer(card, PreferencesManager.getUsername(), PreferencesManager.getPassword());
                    if (err != null) {
                        System.out.println("Server add failed: " + err);
                    }
                }

                if (SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logAdd("Card", resource);
                }

                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Error encrypting card data: " + e.getMessage());
        }
        return false;
    }

    private boolean onSaveButtonClick(String resource, String link) {
        try {
            if (!resource.isEmpty() || !link.isEmpty()) {
                DatabaseManager dm = new DatabaseManager();

                // 🔐 Encrypt both fields
                String encryptedResource = EncryptionUtils.encrypt(resource);
                String encryptedLink = EncryptionUtils.encrypt(link);
                String encryptedOwnerUsername = EncryptionUtils.encrypt(PreferencesManager.getUsername());

                if (dm.linkExists(encryptedResource, encryptedLink, encryptedOwnerUsername)) {
                    System.out.println("Link already exists");
                    return false;
                }

                String id = java.util.UUID.randomUUID().toString();
                LocalDateTime now = LocalDateTime.now();
                String deleted = "false";
                String sync = String.valueOf(cbSync.isSelected());

                dm.writeLinkTodb(id, encryptedResource, encryptedLink, encryptedOwnerUsername, now, now, deleted, sync);

                if ("true".equals(sync)) {
                    String err = SyncManager.addLinkOnServer(
                            new Link(id, encryptedResource, encryptedLink, encryptedOwnerUsername, now, now, deleted, sync),
                            PreferencesManager.getUsername(), PreferencesManager.getPassword()
                    );
                    if (err != null) {
                        System.out.println("Server add failed: " + err);
                    }
                }

                if (SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logAdd("Link", resource); // log original name
                }
                return true;
            }

        } catch (Exception e) {
            System.out.println("❌ Error encrypting link: " + e.getMessage());
        }
        return false;
    }
    private boolean onSaveButtonClick(String resource, String keyWords, String address, String password) {
        try {
            if (!resource.isEmpty() || !keyWords.isEmpty() || !address.isEmpty() || !password.isEmpty()) {
                DatabaseManager dm = new DatabaseManager();

                String encryptedResource = EncryptionUtils.encrypt(resource);
                String encryptedKeyWords = EncryptionUtils.encrypt(keyWords);
                String encryptedAddress = EncryptionUtils.encrypt(address);
                String encryptedPassword = EncryptionUtils.encrypt(password);
                String encryptedOwnerUsername = EncryptionUtils.encrypt(PreferencesManager.getUsername());

                if (dm.walletExists(encryptedResource, encryptedKeyWords, encryptedAddress, encryptedPassword, encryptedOwnerUsername)) {
                    System.out.println("Wallet already exists");
                    return false;
                }

                String id = java.util.UUID.randomUUID().toString();
                LocalDateTime now = LocalDateTime.now();
                String deleted = "false";
                String sync = String.valueOf(cbSync.isSelected());

                dm.writeWalletTodb(
                        id,
                        encryptedResource, encryptedKeyWords, encryptedAddress, encryptedPassword,
                        encryptedOwnerUsername,
                        now, now,
                        deleted, sync
                );

                if ("true".equals(sync)) {
                    String err = SyncManager.addWalletOnServer(
                            new Wallet(id, encryptedResource, encryptedKeyWords, encryptedAddress, encryptedPassword, encryptedOwnerUsername, now, now, deleted, sync),
                            PreferencesManager.getUsername(), PreferencesManager.getPassword()
                    );
                    if (err != null) {
                        System.out.println("Server add failed: " + err);
                    }
                }

                if (SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logAdd("Wallet", resource);
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Error encrypting wallet data: " + e.getMessage());
        }
        return false;
    }
    private void onCancelButtonClick(){
        Stage addStage = (Stage) cancelButton.getScene().getWindow();
        addStage.close();
    }
}
