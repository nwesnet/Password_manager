package nwes.passwordmanager;

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
                onAccountInfoButtonClick();
            }
            if(securityVBoxVisible) {
                onSecurityClick();
            }
        }
    }
    @FXML
    protected void onAccountInfoButtonClick(){
        accountInfoVBoxVisible = !accountInfoVBoxVisible;
        if (accountInfoVBoxVisible) {
            if(securityVBoxVisible) {
                onSecurityClick();
            }
            usernameField.setText(PreferencesManager.getUsername());
            passwordField.setText(PreferencesManager.getPassword());
            pinField.setText(PreferencesManager.getPincode());
            accountInfoVBox.setVisible(true);
        } else {
            accountInfoVBox.setVisible(false);
        }
    }
    @FXML
    protected void onEditLoginDialogClick() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit login info");

        TextField usernameField = new TextField(PreferencesManager.getUsername());
        TextField passwordField = new TextField(PreferencesManager.getPassword());
        TextField pincodeField = new TextField(PreferencesManager.getPincode());

        VBox vb = new VBox(5,
                new HBox(5, new Label("Username: "), usernameField),
                new HBox(5, new Label("Password: "), passwordField),
                new HBox(5, new Label("Pin code: "), pincodeField)
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK) {
                PreferencesManager.setUsername(usernameField.getText());
                PreferencesManager.setPassword(passwordField.getText());
                PreferencesManager.setPincode(pincodeField.getText());
                onAccountInfoButtonClick();
            }
        });
    }
    @FXML
    protected void onShowPsswdBtnClick() {
        togglePasswordVisibility(passwordField, showPsswdBtn);
    }
    @FXML
    protected void onShowPinBtnClick() {
        togglePasswordVisibility(pinField, showPinBtn);
    }
    @FXML
    protected void onSecurityClick() {
        securityVBoxVisible = !securityVBoxVisible;
        if(securityVBoxVisible) {
            if(accountInfoVBoxVisible) {
                onAccountInfoButtonClick();
            }
            securityVBox.setVisible(true);
        } else {
            securityVBox.setVisible(false);
        }
    }
    @FXML
    protected void onEncryptClick() {
        boolean currentEncryptionStatus = PreferencesManager.isEncryptionEnable();
        boolean newEncryptionStatus = (currentEncryptionStatus == true) ? false : true;

        PreferencesManager.setEncryptionEnable(newEncryptionStatus);
    }
    @FXML
    protected void onDoubleConfirmationClick() {
        boolean currentDoubleConfirmStatus = PreferencesManager.isDoubleConfirmationEnabled();
        boolean newDoubleConfirmStatus = (currentDoubleConfirmStatus == true) ? false : true;

        PreferencesManager.setDoubleConfirmation(newDoubleConfirmStatus);
    }
    @FXML
    protected void onStoreLogsClick() {
        boolean currentStoreLogsStatus = PreferencesManager.isStoreLogsEnabled();
        boolean newStoreLogsStatus = (currentStoreLogsStatus == true) ? false : true;

        PreferencesManager.setStoreLogsEnabled(newStoreLogsStatus);
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
                    if(acc.getResource().toLowerCase().contains(query) ||
                       acc.getUsername().toLowerCase().contains(query)){
                        DisplayAccountsDetails(acc, dbManager);
                    }
                }
                break;
            case CARDS:
                for(Card card : allCards){
                    if(card.getResource().toLowerCase().contains(query)){
                        DisplayCardsDetails(card, dbManager);
                    }
                }
                break;
            case LINKS:
                for(Link link : allLinks){
                    if(link.getResource().toLowerCase().contains(query)){
                        DisplayLinksDetails(link, dbManager);
                    }
                }
                break;
            case WALLETS:
                for(Wallet wallet : allWallets){
                    if(wallet.getResource().toLowerCase().contains(query)){
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
        TextField resourceField = new TextField(account.getResource());
        resourceField.setPrefColumnCount(24);
        resourceField.setEditable(false);

        Button editBtn = new Button();
        editBtn.setOnAction(e -> openEditAccountDialog(account));
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button();
        deleteBtn.setOnAction(e -> openDeleteAccountDialog(account));
        deleteBtn.getStyleClass().add("delete-button");
        // Row 2: Login
        TextField loginField = new TextField(account.getUsername());
        loginField.setPrefColumnCount(24);
        loginField.setEditable(false);

        Button copyLoginBtn = new Button();
        copyLoginBtn.setOnAction(e -> copyToClipboard(loginField.getText()));
        copyLoginBtn.getStyleClass().add("copy-button");
        // Row 3: Password
        PasswordField passwordField = new PasswordField();
        passwordField.setText(account.getPassword());
        passwordField.setPrefColumnCount(24);
        passwordField.setEditable(false);

        Button showPasswordBtn = new Button();
        showPasswordBtn.setOnAction(e -> togglePasswordVisibility(passwordField, showPasswordBtn));
        showPasswordBtn.getStyleClass().add("show-button");

        Button copyPsswdBtn = new Button();
        copyPsswdBtn.setOnAction(e -> copyToClipboard(passwordField.getText()));
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
        TextField resourceField = new TextField(card.getResource());
        resourceField.setEditable(false);

        Button editBtn = new Button();
        editBtn.setOnAction( e -> openEditCardDialog(card));
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button();
        deleteBtn.setOnAction( e -> openDeleteCardDialog(card));
        deleteBtn.getStyleClass().add("delete-button");
        // Row 2: Card number
        TextField cardNumberField = new TextField(card.getCardNumber());
        cardNumberField.setEditable(false);
        cardNumberField.setPrefColumnCount(18);

        Button copyCardNumberBtn = new Button();
        copyCardNumberBtn.setOnAction( e -> copyToClipboard(cardNumberField.getText()));
        copyCardNumberBtn.getStyleClass().add("copy-button");
        // Row 3: Expiry date
        TextField cardExpiryDate = new TextField(card.getExpiryDate());
        cardExpiryDate.setEditable(false);
        cardExpiryDate.setPrefColumnCount(4);
        // Row 4: Owner name
        TextField cardOwnerNameField = new TextField(card.getOnwerName());
        cardOwnerNameField.setEditable(false);
        cardOwnerNameField.setPrefColumnCount(18);

        Button copyNameBtn = new Button();
        copyNameBtn.setOnAction( e -> copyToClipboard(cardOwnerNameField.getText()));
        copyNameBtn.getStyleClass().add("copy-button");
        // Row 5: CVV
        PasswordField cardCVVField = new PasswordField();
        cardCVVField.setText(card.getCvv());
        cardCVVField.setEditable(false);
        cardCVVField.setPrefColumnCount(4);

        Button showCVVBtn = new Button();
        showCVVBtn.setOnAction( e -> togglePasswordVisibility(cardCVVField, showCVVBtn));
        showCVVBtn.getStyleClass().add("show-button");
        // Row 6: Pincode
        PasswordField cardPincodeField = new PasswordField();
        cardPincodeField.setText(card.getCardPincode());
        cardPincodeField.setEditable(false);
        cardPincodeField.setPrefColumnCount(10);

        Button showPincodeBtn = new Button();
        showPincodeBtn.setOnAction( e -> togglePasswordVisibility(cardPincodeField, showPincodeBtn));
        showPincodeBtn.getStyleClass().add("show-button");
        // Row 7: Network type
        TextField cardNetworkField = new TextField(card.getCardNetworkType());
        cardNetworkField.setEditable(false);
        cardNetworkField.setPrefColumnCount(10);
        // Row 8: Card type
        TextField cardTypeField = new TextField(card.getCardType());
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
        TextField resourceField = new TextField(link.getResource());
        resourceField.setEditable(false);

        Button editLinkBtn = new Button();
        editLinkBtn.setOnAction( e -> openEditLinkDialog(link));
        editLinkBtn.getStyleClass().add("edit-button");

        Button deleteLinkBtn = new Button();
        deleteLinkBtn.setOnAction( e -> openDeleteLinkDialog(link));
        deleteLinkBtn.getStyleClass().add("delete-button");
        // Row 2: Link
        TextField linkField = new TextField(link.getLink());
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

        TextField resourceField = new TextField(wallet.getResource());

        Button editWalletBtn = new Button();
        editWalletBtn.setOnAction( e -> openEditWalletDialog(wallet));
        editWalletBtn.getStyleClass().add("edit-button");

        Button deleteWalletBtn = new Button();
        deleteWalletBtn.setOnAction( e -> openDeleteWalletDialog(wallet));
        deleteWalletBtn.getStyleClass().add("delete-button");

        TextField addressField = new TextField(wallet.getAddress());
        addressField.setEditable(false);
        addressField.setPrefColumnCount(24);

        Button copyAddressBtn = new Button();
        copyAddressBtn.setOnAction( e -> copyToClipboard(addressField.getText()));
        copyAddressBtn.getStyleClass().add("copy-button");

        PasswordField passwordField = new PasswordField();
        passwordField.setText(wallet.getPassword());
        passwordField.setEditable(false);
        passwordField.setPrefColumnCount(24);

        Button showPsswdBtn = new Button();
        showPsswdBtn.setOnAction( e -> togglePasswordVisibility(passwordField, showPsswdBtn));
        showPsswdBtn.getStyleClass().add("show-button");

        Button copyPsswdBtn = new Button();
        copyPsswdBtn.setOnAction( e -> copyToClipboard(passwordField.getText()));
        copyPsswdBtn.getStyleClass().add("copy-button");

        FlowPane twelveWordsBox = new FlowPane(6,3);
        // The cycle that adds words to a page
        for (String word : wallet.getTwelveWords()){
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
    // Change it when will be ready: ( mb grid.. ) All edit and delete windows
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
        dialog.setTitle("Edit account: " + account.getResource());
        dialog.getDialogPane().setPrefSize(350, 220);

        TextField resourceField = new TextField(account.getResource());
        resourceField.setPrefColumnCount(20);
        TextField loginField = new TextField(account.getUsername());
        loginField.setPrefColumnCount(20);
        TextField passwordField = new TextField(account.getPassword());
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
                account.setResource(resourceField.getText());
                account.setUsername(loginField.getText());
                account.setPassword(passwordField.getText());
                new DatabaseManager().updateAccount(account, oldResource, oldUsername);
                LogsManager.logEdit("Account", account.getResource());
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
        dialog.setTitle("Edit card: " + card.getResource());
        dialog.getDialogPane().setPrefSize(550, 220);

        TextField resourceField = new TextField(card.getResource());
        TextField cardNumberField = new TextField(card.getCardNumber());
        TextField expiryDateField = new TextField(card.getExpiryDate());
        TextField ownerNameField = new TextField(card.getOnwerName());

        TextField cvvField = new TextField(card.getCvv());
        TextField pinField = new TextField(card.getCardPincode());
        TextField networkField = new TextField(card.getCardNetworkType());
        TextField typeField = new TextField(card.getCardType());

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
                card.setResource(resourceField.getText());
                card.setCardNumber(cardNumberField.getText());
                card.setExpiryDate(expiryDateField.getText());
                card.setOnwerName(ownerNameField.getText());
                card.setCvv(cvvField.getText());
                card.setCardPincode(pinField.getText());
                card.setCardNetworkType(networkField.getText());
                card.setCardType(typeField.getText());
                new DatabaseManager().updateCard(card, oldResource, oldCardNumber, oldCardName, oldExpiryDate);
                LogsManager.logEdit("Card", card.getResource());
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
        dialog.setTitle("Edit link " + link.getResource());
        dialog.getDialogPane().setPrefSize(350, 220);

        TextField resourceField = new TextField(link.getResource());
        TextField linkField = new TextField(link.getLink());

        ThemeManager.applyThemeToDialog(dialog);

        grid.add(new Label("Resource:"), 0, 0);
        grid.add(resourceField, 1, 0);

        grid.add(new Label("Link:"), 0, 1);
        grid.add(linkField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK){
                link.setResource(resourceField.getText());
                link.setLink(linkField.getText());
                new DatabaseManager().updateLink(link, oldResource, oldLink);
                LogsManager.logEdit("Link", link.getResource());
                onShowLinks();
            }
        });
    }
    private void openEditWalletDialog(Wallet wallet){
        String oldResource = wallet.getResource();
        String oldAddress = wallet.getAddress();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit wallet " + wallet.getResource());
        dialog.getDialogPane().setPrefSize(450, 220);

        TextField resourceField = new TextField(wallet.getResource());
        TextField addressField = new TextField(wallet.getAddress());
        TextField pinField = new TextField(wallet.getPassword());

        List<TextField> wordFields = new ArrayList<>();
        FlowPane fp = new FlowPane(6,3);
        for(String word : wallet.getTwelveWords()){
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
                wallet.setResource(resourceField.getText());
                wallet.setAddress(addressField.getText());
                wallet.setPassword(pinField.getText());
                String [] updateWords = new String[wordFields.size()];
                for( int i = 0; i < updateWords.length; i++) {
                    updateWords[i] = wordFields.get(i).getText().trim();
                }
                wallet.setTwelveWords(updateWords);
                new DatabaseManager().updateWallet(wallet, oldResource, oldAddress);
                LogsManager.logEdit("Wallet", wallet.getResource());
                onShowWallets();
            }
        });
    }
    private void openDeleteAccountDialog(Account account){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete account");
        alert.setHeaderText("Are you sure you want to delete \"" + account.getResource() + "\"?");
        alert.setContentText("This action cannot be undone.");

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteAccount(account);
            LogsManager.logDelete("Account", account.getResource());
            onShowAccounts();
        }
    }
    private void openDeleteCardDialog(Card card){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete card");
        alert.setHeaderText("Are you sure you want to delete this card?");
        alert.setContentText("Resource: " + card.getResource());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteCard(card);
            LogsManager.logDelete("Card", card.getResource());
            onShowCards();
        }
    }
    private void openDeleteLinkDialog(Link link){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete link");
        alert.setHeaderText("Are you sure you want to delete this resource?");
        alert.setContentText("Resource: " + link.getResource());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteLink(link);
            LogsManager.logDelete("Link", link.getResource());
            onShowLinks();
        }
    }
    private void openDeleteWalletDialog(Wallet wallet){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete wallet");
        alert.setHeaderText("Are you sure you want to delete this wallet?");
        alert.setContentText("Resource: " + wallet.getResource());

        ThemeManager.applyThemeToDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            new DatabaseManager().deleteWallet(wallet);
            LogsManager.logDelete("Wallet", wallet.getResource());
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
}
