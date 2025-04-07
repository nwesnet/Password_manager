package nwes.passwordmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MainPageController {
    @FXML
    private Button add;
    @FXML
    private Button generatePassword;
    @FXML
    private Button showList;
    @FXML
    private Button preferences;
    @FXML
    private Button accountInfoBtn;
    @FXML
    private Button encryptBtn;
    @FXML
    private Button doubleConfirmBtn;
    @FXML
    private Button storeLogsBtn;
    @FXML
    private Button clearLogsBtn;
    @FXML
    private Button editLoginBtn;
    @FXML
    private Button showPsswdBtn;
    @FXML
    private Button showPinBtn;
    @FXML
    private Button securityBtn;
    @FXML
    private Button themeBtn;
    @FXML
    private Button logs;
    @FXML
    private VBox leftVBox;
    @FXML
    private ScrollPane logsScrollPane;
    @FXML
    private TextField logsSearchBar;
    @FXML
    private TextArea logsTextArea;
    @FXML
    private HBox preferencesHBox;
    @FXML
    private VBox preferencesVBox;
    @FXML
    private VBox accountInfoVBox;
    @FXML
    private VBox securityVBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField pinField;
    @FXML
    private VBox showListVBox;
    @FXML
    private TextField showListSearchBar;
    @FXML
    private VBox showListContentVBox;

    private List<Account> allAccounts = new ArrayList<>();
    private List<Card> allCards = new ArrayList<>();
    private List<Link> allLinks = new ArrayList<>();
    private List<Wallet> allWallets = new ArrayList<>();
    private List<String> allLogLines = new ArrayList<>();

    private Category currentCategory;

    private boolean showListVisible = false;
    private boolean preferencesHBoxVisible = false;
    private boolean preferencesVBoxVisible = false;
    private boolean accountInfoVBoxVisible = false;
    private boolean securityVBoxVisible = false;
    private boolean logsVisible = false;

    @FXML
    private void initialize(){
        showListSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCurrentCategory(newValue.trim().toLowerCase());
        });
        logsSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLogs(newValue);
        });
    }
    @FXML
    protected void onAddButtonClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-view.fxml"));
            Scene addScene = new Scene(fxmlLoader.load());
            Stage addStage = new Stage();

            ThemeManager.registerScene(addScene);
            addStage.setOnCloseRequest( e -> ThemeManager.unregisterScene(addScene));

            addStage.setTitle("Add information");
            addStage.setScene(addScene);
            addStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onGeneratePasswordClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("genpsswd-view.fxml"));
            Scene genpsswdScene = new Scene(fxmlLoader.load());
            Stage genpsswdStage = new Stage();

            ThemeManager.registerScene(genpsswdScene);
            genpsswdStage.setOnCloseRequest( e -> ThemeManager.unregisterScene(genpsswdScene));

            genpsswdStage.setTitle("Generate password");
            genpsswdStage.setScene(genpsswdScene);
            genpsswdStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onLogsButtonClick() {
        logsVisible = !logsVisible;
        if (logsVisible){
            logsScrollPane.setVisible(true);
            String logs = LogsManager.readLogs();
            allLogLines = Arrays.asList(logs.split("\n"));
            logsTextArea.setText(logs);
        } else {
            logsScrollPane.setVisible(false);
        }
    }
    @FXML
    protected void onShowListButtonClick(){
        showListVisible = !showListVisible;
        if (showListVisible){
            showListVBox.setVisible(true);
            onShowAccounts();
            if(preferencesVBoxVisible){
                onPreferencesButtonClick();
            }
        } else {
            showListVBox.setVisible(false);
            showListContentVBox.getChildren().clear();
        }
    }
    @FXML
    protected void onShowAccounts(){
        currentCategory = Category.ACCOUNTS;
        showListContentVBox.getChildren().clear();

        DatabaseManager dbManager = new DatabaseManager();
        allAccounts = dbManager.getAllAccounts();

        for (Account account : allAccounts){
            DisplayAccountsDetails(account, dbManager);
        }
    }
    @FXML
    protected void onShowCards(){
        currentCategory = Category.CARDS;
        showListContentVBox.getChildren().clear();

        DatabaseManager dbManager = new DatabaseManager();
        allCards = dbManager.getAllCards();

        for(Card card : allCards){
            DisplayCardsDetails(card, dbManager);
        }
    }
    @FXML
    protected void onShowLinks(){
        currentCategory = Category.LINKS;
        showListContentVBox.getChildren().clear();

        DatabaseManager dbManager = new DatabaseManager();
        allLinks = dbManager.getAllLinks();

        for(Link link : allLinks){
            DisplayLinksDetails(link, dbManager);
        }
    }
    @FXML
    protected void onShowWallets(){
        currentCategory = Category.WALLETS;
        showListContentVBox.getChildren().clear();

        DatabaseManager dbManager = new DatabaseManager();
        allWallets = dbManager.getAllWallets();

        for(Wallet wallet : allWallets){
            DisplayWalletsDetails(wallet, dbManager);
        }
    }
    @FXML
    protected void onPreferencesButtonClick(){
        preferencesHBoxVisible = !preferencesHBoxVisible;
        preferencesVBoxVisible = !preferencesVBoxVisible;
        if (preferencesVBoxVisible){
            preferencesHBox.setVisible(true);
            preferencesVBox.setVisible(true);
            if(showListVisible){
                onShowListButtonClick();
            }
        } else {
            preferencesHBox.setVisible(false);
            preferencesVBox.setVisible(false);
            if(accountInfoVBoxVisible){
                DisplayAccountInfoDetails();
            }
            if(securityVBoxVisible) {
                DisplaySecurityDetails();
            }
        }
    }
    @FXML
    protected void onAccountInfoButtonClick(){
        if(SecurityManager.isDoubleConfirmationEnabled()) {
            showDoubleConfirmDialog(() -> DisplayAccountInfoDetails());
        } else {
            DisplayAccountInfoDetails();
        }
    }
    @FXML
    protected void onEditLoginDialogClick() {
        if(SecurityManager.isDoubleConfirmationEnabled()) {
            showDoubleConfirmDialog(() -> openEditLoginDialog());
        } else {
            openEditLoginDialog();
        }
    }
    @FXML
    protected void onShowPsswdBtnClick() {
        if(SecurityManager.isDoubleConfirmationEnabled()) {
            showDoubleConfirmDialog(() -> togglePasswordVisibility(passwordField, showPsswdBtn));
        } else {
            togglePasswordVisibility(passwordField, showPsswdBtn);
        }
    }
    @FXML
    protected void onShowPinBtnClick() {
        if(SecurityManager.isDoubleConfirmationEnabled()) {
            showDoubleConfirmDialog(() -> togglePasswordVisibility(pinField, showPinBtn));
        } else {
            togglePasswordVisibility(pinField, showPinBtn);
        }
    }
    @FXML
    protected void onSecurityClick() {
        if(SecurityManager.isDoubleConfirmationEnabled()) {
            showDoubleConfirmDialog(() -> DisplaySecurityDetails());
        } else {
            DisplaySecurityDetails();
        }
    }
    @FXML
    protected void onDoubleConfirmationClick() {
        boolean current = PreferencesManager.isDoubleConfirmationEnabled();
        boolean updated = !current;

        SecurityManager.setDoubleConfirmationEnabled(updated);
    }
    @FXML
    protected void onStoreLogsClick() {
        boolean current = SecurityManager.isStoreLogsEnabled();
        boolean updated = !current;

        SecurityManager.setStoreLogsEnabled(updated);
    }
    @FXML
    protected void onClearLogsClick() {
        LogsManager.logClear();
    }
    @FXML
    protected void onThemeButtonClick() {
        String currentTheme = PreferencesManager.getTheme();
        String newTheme = currentTheme.equals("light") ? "dark" : "light";

        PreferencesManager.setTheme(newTheme);
        ThemeManager.registerScene(themeBtn.getScene());
        ThemeManager.applyThemeToAll();
    }

    private void filterCurrentCategory(String query){
        showListContentVBox.getChildren().clear();
        DatabaseManager dbManager = new DatabaseManager();

        switch (currentCategory){
            case ACCOUNTS:
                for(Account acc : allAccounts){
                    if(acc.getResourceDecrypted().toLowerCase().contains(query) ||
                       acc.getUsernameDecrypted().toLowerCase().contains(query)){
                        DisplayAccountsDetails(acc, dbManager);
                    }
                }
                break;
            case CARDS:
                for(Card card : allCards){
                    if(card.getResourceDecrypted().toLowerCase().contains(query)){
                        DisplayCardsDetails(card, dbManager);
                    }
                }
                break;
            case LINKS:
                for(Link link : allLinks){
                    if(link.getResourceDecrypted().toLowerCase().contains(query)){
                        DisplayLinksDetails(link, dbManager);
                    }
                }
                break;
            case WALLETS:
                for(Wallet wallet : allWallets){
                    if(wallet.getResourceDecrypted().toLowerCase().contains(query)){
                        DisplayWalletsDetails(wallet, dbManager);
                    }
                }
                break;
        }
    }
    private void filterLogs(String query) {
        if(query == null || query.isEmpty()) {
            logsTextArea.setText(String.join("\n", allLogLines));
            return;
        }
        String lowerQuery = query.toLowerCase();

        List<String> filtered = allLogLines.stream()
                .filter(line -> line.toLowerCase().contains(lowerQuery))
                .toList();

        logsTextArea.setText(String.join("\n", filtered));
    }
    private void DisplayAccountInfoDetails() {
        accountInfoVBoxVisible = !accountInfoVBoxVisible;
        if (accountInfoVBoxVisible) {
            if(securityVBoxVisible) {
                DisplaySecurityDetails();
            }
            usernameField.setText(PreferencesManager.getUsername());
            passwordField.setText(PreferencesManager.getPassword());
            pinField.setText(PreferencesManager.getPincode());
            accountInfoVBox.setVisible(true);
        } else {
            accountInfoVBox.setVisible(false);
        }
    }
    private void DisplaySecurityDetails() {
        securityVBoxVisible = !securityVBoxVisible;
        if(securityVBoxVisible) {
            if(accountInfoVBoxVisible) {
                DisplayAccountInfoDetails();
            }
            securityVBox.setVisible(true);
        } else {
            securityVBox.setVisible(false);
        }
    }

    private void DisplayAccountsDetails(Account account, DatabaseManager dbManager) {
        // Create gridpane, set spacing and padding, implement the column width
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(70);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn, new ColumnConstraints(), new ColumnConstraints());
        // Row 1: Resource
        TextField resourceField = new TextField(account.getResourceDecrypted());
        resourceField.setPrefColumnCount(24);
        resourceField.setEditable(false);

        Button editBtn = new Button();
        editBtn.setOnAction(e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openEditAccountDialog(account));
            } else {
                openEditAccountDialog(account);
            }
        });
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button();
        deleteBtn.setOnAction(e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openDeleteAccountDialog(account));
            } else {
                openDeleteAccountDialog(account);
            }
        });
        deleteBtn.getStyleClass().add("delete-button");
        // Row 2: Login
        TextField loginField = new TextField(account.getUsernameDecrypted());
        loginField.setPrefColumnCount(24);
        loginField.setEditable(false);

        Button copyLoginBtn = new Button();
        copyLoginBtn.setOnAction(e -> copyToClipboard(loginField.getText()));
        copyLoginBtn.getStyleClass().add("copy-button");
        // Row 3: Password
        PasswordField passwordField = new PasswordField();
        passwordField.setText(account.getPasswordDecrypted());
        passwordField.setPrefColumnCount(24);
        passwordField.setEditable(false);

        Button showPasswordBtn = new Button();
        showPasswordBtn.setOnAction(e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> togglePasswordVisibility(passwordField, showPasswordBtn));
            } else {
                togglePasswordVisibility(passwordField, showPasswordBtn);
            }
        });
        showPasswordBtn.getStyleClass().add("show-button");

        Button copyPsswdBtn = new Button();
        copyPsswdBtn.setOnAction(e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> copyToClipboard(passwordField.getText()));
            } else {
                copyToClipboard(passwordField.getText());
            }
        });
        copyPsswdBtn.getStyleClass().add("copy-button");
        // Add everything into gridpane
        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);
        grid.add(editBtn, 2, 0);
        grid.add(deleteBtn, 3, 0);

        grid.add(new Label("Login:"), 0, 1);
        grid.add(loginField, 1, 1);
        grid.add(copyLoginBtn, 2, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(showPasswordBtn, 2, 2);
        grid.add(copyPsswdBtn, 3, 2);
        // Add grid into showlistcontentvbox
        showListContentVBox.getChildren().add(grid);
    }
    private void DisplayCardsDetails(Card card, DatabaseManager dbManager) {
        // Create gridpane, implement column size and add it
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn, new ColumnConstraints(), new ColumnConstraints());
        // Row 1: Resource
        TextField resourceField = new TextField(card.getResourceDecrypted());
        resourceField.setEditable(false);

        Button editBtn = new Button();
        editBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openEditCardDialog(card));
            } else {
                openEditCardDialog(card);
            }
        });
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button();
        deleteBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openDeleteCardDialog(card));
            } else {
                openDeleteCardDialog(card);
            }
        });
        deleteBtn.getStyleClass().add("delete-button");
        // Row 2: Card number
        TextField cardNumberField = new TextField(card.getCardNumberDecrypted());
        cardNumberField.setEditable(false);
        cardNumberField.setPrefColumnCount(18);

        Button copyCardNumberBtn = new Button();
        copyCardNumberBtn.setOnAction( e -> copyToClipboard(cardNumberField.getText()));
        copyCardNumberBtn.getStyleClass().add("copy-button");
        // Row 3: Expiry date
        TextField cardExpiryDate = new TextField(card.getExpiryDateDecrypted());
        cardExpiryDate.setEditable(false);
        cardExpiryDate.setPrefColumnCount(4);
        // Row 4: Owner name
        TextField cardOwnerNameField = new TextField(card.getOwnerNameDecrypted());
        cardOwnerNameField.setEditable(false);
        cardOwnerNameField.setPrefColumnCount(18);

        Button copyNameBtn = new Button();
        copyNameBtn.setOnAction( e -> copyToClipboard(cardOwnerNameField.getText()));
        copyNameBtn.getStyleClass().add("copy-button");
        // Row 5: CVV
        PasswordField cardCVVField = new PasswordField();
        cardCVVField.setText(card.getCvvDecrypted());
        cardCVVField.setEditable(false);
        cardCVVField.setPrefColumnCount(4);

        Button showCVVBtn = new Button();
        showCVVBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> togglePasswordVisibility(cardCVVField, showCVVBtn));
            } else {
                togglePasswordVisibility(cardCVVField, showCVVBtn);
            }
        });
        showCVVBtn.getStyleClass().add("show-button");
        // Row 6: Pincode
        PasswordField cardPincodeField = new PasswordField();
        cardPincodeField.setText(card.getCardPincodeDecrypted());
        cardPincodeField.setEditable(false);
        cardPincodeField.setPrefColumnCount(10);

        Button showPincodeBtn = new Button();
        showPincodeBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> togglePasswordVisibility(cardPincodeField, showPincodeBtn));
            } else {
                togglePasswordVisibility(cardPincodeField, showPincodeBtn);
            }
        });
        showPincodeBtn.getStyleClass().add("show-button");
        // Row 7: Network type
        TextField cardNetworkField = new TextField(card.getCardNetworkTypeDecrypted());
        cardNetworkField.setEditable(false);
        cardNetworkField.setPrefColumnCount(10);
        // Row 8: Card type
        TextField cardTypeField = new TextField(card.getCardTypeDecrypted());
        cardTypeField.setEditable(false);
        cardTypeField.setPrefColumnCount(10);
        // Add every thing into gridpane
        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);
        grid.add(editBtn, 2, 0);
        grid.add(deleteBtn, 3, 0);

        grid.add(new Label("Number:"), 0, 1);
        grid.add(cardNumberField, 1, 1);
        grid.add(copyCardNumberBtn, 2, 1);

        grid.add(new Label("Date:"), 0, 2);
        grid.add(cardExpiryDate, 1, 2);

        grid.add(new Label("Owner:"), 0, 3);
        grid.add(cardOwnerNameField, 1, 3);
        grid.add(copyNameBtn, 2, 3);

        grid.add(new Label("Additional details"), 1, 4);

        grid.add(new Label("CVV:"), 0, 5);
        grid.add(cardCVVField, 1, 5);
        grid.add(showCVVBtn, 2, 5);

        grid.add(new Label("Pincode:"), 0, 6);
        grid.add(cardPincodeField, 1, 6);
        grid.add(showPincodeBtn, 2, 6);

        grid.add(new Label("Network:"), 0, 7);
        grid.add(cardNetworkField, 1, 7);

        grid.add(new Label("Type:"), 0, 8);
        grid.add(cardTypeField, 1, 8);
        // Add gridpane into showlistcontentvbox
        showListContentVBox.getChildren().add(grid);
    }

    private void DisplayLinksDetails(Link link, DatabaseManager dbManager){
        // Create gridpane, set spacing and padding, implement the column width
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColunm = new ColumnConstraints();
        inputColunm.setPrefWidth(240);
        // Row 1: Resource
        TextField resourceField = new TextField(link.getResourceDecrypted());
        resourceField.setEditable(false);

        Button editLinkBtn = new Button();
        editLinkBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openEditLinkDialog(link));
            } else {
                openEditLinkDialog(link);
            }
        });
        editLinkBtn.getStyleClass().add("edit-button");

        Button deleteLinkBtn = new Button();
        deleteLinkBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openDeleteLinkDialog(link));
            } else {
                openDeleteLinkDialog(link);
            }
        });
        deleteLinkBtn.getStyleClass().add("delete-button");
        // Row 2: Link
        TextField linkField = new TextField(link.getLinkDecrypted());
        linkField.setEditable(false);
        linkField.setPrefColumnCount(24);

        Button copyLinkBtn = new Button();
        copyLinkBtn.setOnAction( e -> copyToClipboard(linkField.getText()));
        copyLinkBtn.getStyleClass().add("copy-button");
        // Add everything into gridpane
        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);
        grid.add(editLinkBtn, 2, 0);
        grid.add(deleteLinkBtn, 3, 0);

        grid.add(new Label("Link:"), 0, 1);
        grid.add(linkField, 1, 1);
        grid.add(copyLinkBtn, 2, 1);
        // Add gridpane into showlistcontentvbox
        showListContentVBox.getChildren().add(grid);
    }
    // Change it when you will be ready ( mb grid... )
    private void DisplayWalletsDetails(Wallet wallet, DatabaseManager dbManager){
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);

        ColumnConstraints labelColumn = new ColumnConstraints(80);
        ColumnConstraints inputColumn = new ColumnConstraints(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn, new ColumnConstraints(), new ColumnConstraints());

        TextField resourceField = new TextField(wallet.getResourceDecrypted());

        Button editWalletBtn = new Button();
        editWalletBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openEditWalletDialog(wallet));
            } else {
                openEditWalletDialog(wallet);
            }
        });
        editWalletBtn.getStyleClass().add("edit-button");

        Button deleteWalletBtn = new Button();
        deleteWalletBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> openDeleteWalletDialog(wallet));
            } else {
                openDeleteWalletDialog(wallet);
            }
        });
        deleteWalletBtn.getStyleClass().add("delete-button");

        TextField addressField = new TextField(wallet.getAddressDecrypted());
        addressField.setEditable(false);
        addressField.setPrefColumnCount(24);

        Button copyAddressBtn = new Button();
        copyAddressBtn.setOnAction( e -> copyToClipboard(addressField.getText()));
        copyAddressBtn.getStyleClass().add("copy-button");

        PasswordField passwordField = new PasswordField();
        passwordField.setText(wallet.getPasswordDecrypted());
        passwordField.setEditable(false);
        passwordField.setPrefColumnCount(24);

        Button showPsswdBtn = new Button();
        showPsswdBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> togglePasswordVisibility(passwordField, showPsswdBtn));
            } else {
                togglePasswordVisibility(passwordField, showPsswdBtn);
            }
        });
        showPsswdBtn.getStyleClass().add("show-button");

        Button copyPsswdBtn = new Button();
        copyPsswdBtn.setOnAction( e -> {
            if(SecurityManager.isDoubleConfirmationEnabled()) {
                showDoubleConfirmDialog(() -> copyToClipboard(passwordField.getText()));
            } else {
                copyToClipboard(passwordField.getText());
            }
        });
        copyPsswdBtn.getStyleClass().add("copy-button");

        FlowPane twelveWordsBox = new FlowPane(6,3);
        // The cycle that adds words to a page
        for (String word : wallet.getTwelveWordsDecrypted()){
            Label wordLabel = new Label(word);
            twelveWordsBox.getChildren().add(wordLabel);
        }

        grid.add(new Label("Address:"), 0, 0);
        grid.add(addressField, 1, 0);
        grid.add(copyAddressBtn, 2, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(showPsswdBtn, 2, 1);
        grid.add(copyPsswdBtn, 3, 1);

        VBox formBox = new VBox(5,
                new HBox(5, new Label("Resource: "), resourceField, editWalletBtn, deleteWalletBtn),
                new Label("key words: "),
                twelveWordsBox,
                grid
        );
        formBox.setPadding(new Insets(20));
        showListContentVBox.getChildren().add(formBox);
    }
    private void openEditLoginDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit login info");

        String oldUsername = PreferencesManager.getUsername();
        String oldPassword = PreferencesManager.getPassword();

        TextField usernameField = new TextField(PreferencesManager.getUsername());
        TextField passwordField = new TextField(PreferencesManager.getPassword());
        TextField pincodeField = new TextField(PreferencesManager.getPincode());
        // Create and configure GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(5);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);
        // Add labels and fields to the grid
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        grid.add(new Label("Pin code:"), 0, 2);
        grid.add(pincodeField, 1, 2);

        ThemeManager.applyThemeToDialog(dialog);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK) {
                boolean DC = PreferencesManager.isDoubleConfirmationEnabled();
                boolean SL = PreferencesManager.isStoreLogsEnabled();
                EncryptionUtils.reencryptAllData(
                        oldUsername,
                        oldPassword,
                        usernameField.getText(),
                        passwordField.getText()
                );

                PreferencesManager.setUsername(usernameField.getText());
                PreferencesManager.setPassword(passwordField.getText());
                PreferencesManager.setPincode(pincodeField.getText());
                PreferencesManager.setDoubleConfirmation(DC);
                PreferencesManager.setStoreLogsEnabled(SL);
            }
        });
    }

    private void openEditAccountDialog(Account account){
        String oldResource = account.getResource();
        String oldUsername = account.getUsername();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(20));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit account: " + account.getResourceDecrypted());

        TextField resourceField = new TextField(account.getResourceDecrypted());
        resourceField.setPrefColumnCount(20);
        TextField loginField = new TextField(account.getUsernameDecrypted());
        loginField.setPrefColumnCount(20);
        TextField passwordField = new TextField(account.getPasswordDecrypted());
        passwordField.setPrefColumnCount(20);

        ThemeManager.applyThemeToDialog(dialog);

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);

        grid.add(new Label("Login:"), 0, 1);
        grid.add(loginField, 1, 1);

        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // Save updated values
                account.setResourceEncrypted(resourceField.getText());
                account.setUsernameEncrypted(loginField.getText());
                account.setPasswordEncrypted(passwordField.getText());
                new DatabaseManager().updateAccount(account, oldResource, oldUsername);
                if(SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logEdit("Account", account.getResourceDecrypted());
                }
                onShowAccounts(); // Refresh
            }
        });
    }
    private void openEditCardDialog(Card card){
        String oldResource = card.getResource();
        String oldCardNumber = card.getCardNumber();
        String oldCardName = card.getOnwerName();
        String oldExpiryDate = card.getExpiryDate();

        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(10));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit card: " + card.getResourceDecrypted());

        TextField resourceField = new TextField(card.getResourceDecrypted());
        TextField cardNumberField = new TextField(card.getCardNumberDecrypted());
        TextField expiryDateField = new TextField(card.getExpiryDateDecrypted());
        TextField ownerNameField = new TextField(card.getOwnerNameDecrypted());

        TextField cvvField = new TextField(card.getCvvDecrypted());
        TextField pinField = new TextField(card.getCardPincodeDecrypted());
        TextField networkField = new TextField(card.getCardNetworkTypeDecrypted());
        TextField typeField = new TextField(card.getCardTypeDecrypted());

        ThemeManager.applyThemeToDialog(dialog);

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);

        grid.add(new Label("Number:"), 0, 1);
        grid.add(cardNumberField, 1, 1);

        grid.add(new Label("Date:"), 0, 2);
        grid.add(expiryDateField, 1, 2);

        grid.add(new Label("Owner:"), 0, 3);
        grid.add(ownerNameField, 1, 3);

        VBox formBox = new VBox(5,
                new HBox(5, new Label("CVV:                   "), cvvField),
                new HBox(5,new Label("Pin:                    "), pinField),
                new HBox(5, new Label("Pay network:   "), networkField),
                new HBox(5, new Label("Card type:        "), typeField)
        );

        VBox vb = new VBox(10, grid, formBox);

        dialog.getDialogPane().setContent(vb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                card.setResourceEncrypted(resourceField.getText());
                card.setCardNumberEncrypted(cardNumberField.getText());
                card.setExpiryDateEncrypted(expiryDateField.getText());
                card.setOwnerNameEncrypted(ownerNameField.getText());
                card.setCvvEncrypted(cvvField.getText());
                card.setCardPincodeEncrypted(pinField.getText());
                card.setCardNetworkTypeEncrypted(networkField.getText());
                card.setCardTypeEncrypted(typeField.getText());
                new DatabaseManager().updateCard(card, oldResource, oldCardNumber, oldCardName, oldExpiryDate);
                if(SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logEdit("Card", card.getResourceDecrypted());
                }
                onShowCards(); // Refresh
            }
        });
    }
    private void openEditLinkDialog(Link link){
        String oldResource = link.getResource();
        String oldLink = link.getLink();

        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(10));

        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPrefWidth(80);
        ColumnConstraints inputColumn = new ColumnConstraints();
        inputColumn.setPrefWidth(240);
        grid.getColumnConstraints().addAll(labelColumn, inputColumn);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit link " + link.getResourceDecrypted());

        TextField resourceField = new TextField(link.getResourceDecrypted());
        TextField linkField = new TextField(link.getLinkDecrypted());

        ThemeManager.applyThemeToDialog(dialog);

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);

        grid.add(new Label("Link:"), 0, 1);
        grid.add(linkField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK){
                link.setResourceEncrypted(resourceField.getText());
                link.setLinkEncrypted(linkField.getText());
                new DatabaseManager().updateLink(link, oldResource, oldLink);
                if(SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logEdit("Link", link.getResourceDecrypted());
                }
                onShowLinks();
            }
        });
    }
    private void openEditWalletDialog(Wallet wallet){
        String oldResource = wallet.getResource();
        String oldAddress = wallet.getAddress();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit wallet " + wallet.getResourceDecrypted());

        TextField resourceField = new TextField(wallet.getResourceDecrypted());
        TextField addressField = new TextField(wallet.getAddressDecrypted());
        TextField pinField = new TextField(wallet.getPasswordDecrypted());

        List<TextField> wordFields = new ArrayList<>();
        FlowPane fp = new FlowPane(6,3);
        for(String word : wallet.getTwelveWordsDecrypted()){
            TextField wordField = new TextField(word);
            wordFields.add(wordField);
            fp.getChildren().add(wordField);
        }

        ThemeManager.applyThemeToDialog(dialog);

        VBox vb = new VBox(5,
                new HBox(new Label("Resource: "), resourceField),
                new VBox(new Label("Key words: "), fp),
                new VBox(new HBox(new Label("Address:"), addressField),
                new HBox(new Label("Pincode:"), pinField))
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK){
                wallet.setResourceEncrypted(resourceField.getText());
                wallet.setAddressEncrypted(addressField.getText());
                wallet.setPasswordEncrypted(pinField.getText());
                String [] updateWords = new String[wordFields.size()];
                for( int i = 0; i < updateWords.length; i++) {
                    updateWords[i] = wordFields.get(i).getText().trim();
                }
                wallet.setTwelveWordsEncrypted(updateWords);
                new DatabaseManager().updateWallet(wallet, oldResource, oldAddress);
                if(SecurityManager.isStoreLogsEnabled()) {
                    LogsManager.logEdit("Wallet", wallet.getResourceDecrypted());
                }
                onShowWallets();
            }
        });
    }
    private void openDeleteAccountDialog(Account account){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Are you sure you want to delete \"" + account.getResourceDecrypted() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteAccount(account);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logDelete("Account", account.getResourceDecrypted());
            }
            onShowAccounts();
        }
    }
    private void openDeleteCardDialog(Card card){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete card");
        alert.setHeaderText("Are you sure you want to delete this card?");
        alert.setContentText("Resource: " + card.getResourceDecrypted());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteCard(card);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logDelete("Card", card.getResourceDecrypted());
            }
            onShowCards();
        }
    }
    private void openDeleteLinkDialog(Link link){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete link");
        alert.setHeaderText("Are you sure you want to delete this resource?");
        alert.setContentText("Resource: " + link.getResourceDecrypted());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteLink(link);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logDelete("Link", link.getResourceDecrypted());
            }
            onShowLinks();
        }
    }
    private void openDeleteWalletDialog(Wallet wallet){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete wallet");
        alert.setHeaderText("Are you sure you want to delete this wallet?");
        alert.setContentText("Resource: " + wallet.getResourceDecrypted());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteWallet(wallet);
            if(SecurityManager.isStoreLogsEnabled()) {
                LogsManager.logDelete("Wallet", wallet.getResourceDecrypted());
            }
            onShowWallets();
        }
    }
    private void copyToClipboard(String text){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
    private void togglePasswordVisibility(PasswordField passwordField, Button toggleBtn){
        String current = passwordField.getText();
        TextField plainField = new TextField(current);

        GridPane parent = (GridPane) passwordField.getParent();

        Integer col = GridPane.getColumnIndex(passwordField);
        Integer row = GridPane.getRowIndex(passwordField);

        parent.getChildren().remove(passwordField);

        GridPane.setColumnIndex(plainField, col);
        GridPane.setRowIndex(plainField, row);

        toggleBtn.getStyleClass().remove("show-button");
        toggleBtn.getStyleClass().add("hide-button");

        parent.getChildren().add(plainField);

        toggleBtn.setOnAction( e -> {
            passwordField.setText(plainField.getText());
            parent.getChildren().remove(plainField);

            GridPane.setColumnIndex(passwordField, col);
            GridPane.setRowIndex(passwordField, row);

            toggleBtn.getStyleClass().remove("hide-button");
            toggleBtn.getStyleClass().add("show-button");

            parent.getChildren().add(passwordField);

            toggleBtn.setOnAction( ev -> togglePasswordVisibility(passwordField, toggleBtn));
        });
    }
    private void showDoubleConfirmDialog(Runnable onSuccess) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Double confirmation");

        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        Label label = new Label("Enter your PIN to confirm this action:");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN code");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        ThemeManager.applyThemeToDialog(dialog);

        box.getChildren().addAll(label, pinField, errorLabel);

        dialog.getDialogPane().setContent(box);
        ButtonType okButtonType = ButtonType.OK;
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        // Get the actual OK button node to hook into
        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        // Track retries
        final int[] attemptsLeft = {3};

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String inputPin = pinField.getText();

            if (SecurityManager.validatePin(inputPin)) {
                SecurityManager.temporarilyDisableDoubleConfirmation(3);
                onSuccess.run();
                dialog.setResult(null); // Close the dialog
            } else {
                attemptsLeft[0]--;
                if (attemptsLeft[0] > 0) {
                    errorLabel.setText("❌ Incorrect PIN. Attempts left: " + attemptsLeft[0]);
                    event.consume(); // Keep dialog open
                } else {
                    errorLabel.setText("❌ Too many failed attempts.");
                    dialog.setResult(null); // Close the dialog
                }
            }
        });
        dialog.showAndWait();
    }
}
