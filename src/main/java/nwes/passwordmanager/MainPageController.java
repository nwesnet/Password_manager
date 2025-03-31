package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private Button editLoginBtn;
    @FXML
    private Button showPsswdBtn;
    @FXML
    private Button showPinBtn;
    @FXML
    private Button doubleConfirmBtn;
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
        }
    }
    @FXML
    protected void onAccountInfoButtonClick(){
        accountInfoVBoxVisible = !accountInfoVBoxVisible;
        if (accountInfoVBoxVisible) {
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
    protected void onDoubleConfirmationClick(){

    }
    @FXML
    protected void onThemeButtonClick(){
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

    private void DisplayAccountsDetails(Account account, DatabaseManager dbManager){
        // create vbox to display accounts
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));
        // resource row
        Label resourceLabel = new Label("Resource: ");
        TextField resourceField = new TextField(account.getResource());
        resourceField.setPrefColumnCount(16);
        resourceField.setEditable(false);

        Button editBtn = new Button();
        editBtn.setOnAction(e -> openEditAccountDialog(account));
        editBtn.getStyleClass().add("edit-button");

        Button deleteBtn = new Button();
        deleteBtn.setOnAction(e -> openDeleteAccountDialog(account));
        deleteBtn.getStyleClass().add("delete-button");

        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, resourceField, editBtn, deleteBtn);
        // login row
        Label loginLabel = new Label("Login:        ");
        TextField loginField = new TextField(account.getUsername());
        loginField.setPrefColumnCount(16);
        loginField.setEditable(false);

        Button copyLoginBtn = new Button();
        copyLoginBtn.setOnAction(e -> copyToClipboard(loginField.getText()));
        copyLoginBtn.getStyleClass().add("copy-button");

        HBox hbLogin = new HBox(5);
        hbLogin.getChildren().addAll(loginLabel, loginField, copyLoginBtn);
        // password row
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(account.getPassword());
        passwordField.setPrefColumnCount(16);
        passwordField.setEditable(false);

        Button showPasswordBtn = new Button();
        showPasswordBtn.setOnAction(e -> togglePasswordVisibility(passwordField, showPasswordBtn));
        showPasswordBtn.getStyleClass().add("show-button");

        Button copyPsswdBtn = new Button();
        copyPsswdBtn.setOnAction(e -> copyToClipboard(passwordField.getText()));
        copyPsswdBtn.getStyleClass().add("copy-button");

        HBox hbPassword = new HBox(5);
        hbPassword.getChildren().addAll(passwordLabel, passwordField, showPasswordBtn, copyPsswdBtn);
        // add every thing in showlistcontentvbox
        vb.getChildren().addAll(hbResource, hbLogin, hbPassword);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayCardsDetails(Card card, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));
        // resource row
        Label resourceLabel = new Label("Resource: ");
        TextField resourceField = new TextField(card.getResource());
        resourceField.setEditable(false);

        Button editBtn = new Button("edit");
        editBtn.setOnAction( e -> openEditCardDialog(card));

        Button deleteBtn = new Button("delete");
        deleteBtn.setOnAction( e -> openDeleteCardDialog(card));

        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, resourceField, editBtn, deleteBtn);
        // card number row
        TextField cardNumberField = new TextField(card.getCardNumber());
        cardNumberField.setEditable(false);
        cardNumberField.setPrefColumnCount(18);

        Button copyCardNumberBtn = new Button("copy");
        copyCardNumberBtn.setOnAction( e -> copyToClipboard(cardNumberField.getText()));

        HBox hbCardNumber = new HBox(1);
        hbCardNumber.getChildren().addAll(cardNumberField, copyCardNumberBtn);
        // card date and cvv row
        TextField cardExpiryDate = new TextField(card.getExpiryDate());
        cardExpiryDate.setEditable(false);
        cardExpiryDate.setPrefColumnCount(4);
        PasswordField cardCVVField = new PasswordField();
        cardCVVField.setText(card.getCvv());
        cardCVVField.setEditable(false);
        cardCVVField.setPrefColumnCount(4);

        Button showBtn = new Button("show");
        showBtn.setOnAction( e -> togglePasswordVisibility(cardCVVField, showBtn));

        HBox hbDateAndCvv = new HBox(1);
        hbDateAndCvv.getChildren().addAll(cardExpiryDate, cardCVVField, showBtn);
        // card owner name row
        TextField cardOwnerNameField = new TextField(card.getOnwerName());
        cardOwnerNameField.setEditable(false);
        cardOwnerNameField.setPrefColumnCount(18);

        Button copyNameBtn = new Button("copy");
        copyNameBtn.setOnAction( e -> copyToClipboard(cardOwnerNameField.getText()));

        HBox hbCardName = new HBox(1);
        hbCardName.getChildren().addAll(cardOwnerNameField, copyNameBtn);

        // Assemble all
        vb.getChildren().addAll(hbResource, hbCardNumber, hbDateAndCvv, hbCardName);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayLinksDetails(Link link, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));
        // resource row
        Label resourceLabel = new Label("Resource: ");
        TextField resourceField = new TextField(link.getResource());
        resourceField.setEditable(false);

        Button editLinkBtn = new Button("edit");
        editLinkBtn.setOnAction( e -> openEditLinkDialog(link));

        Button deleteLinkBtn = new Button("delete");
        deleteLinkBtn.setOnAction( e -> openDeleteLinkDialog(link));

        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, resourceField, editLinkBtn, deleteLinkBtn);
        // link row
        TextField linkField = new TextField(link.getLink());
        linkField.setEditable(false);
        linkField.setPrefColumnCount(18);

        Button copyLinkBtn = new Button("copy");
        copyLinkBtn.setOnAction( e -> copyToClipboard(linkField.getText()));

        HBox hbLink = new HBox(1);
        hbLink.getChildren().addAll(linkField, copyLinkBtn);
        // add everything in showlistcontentvbox
        vb.getChildren().addAll(hbResource, hbLink);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayWalletsDetails(Wallet wallet, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));
        // resource row
        Label resourceLabel = new Label("Resource: ");
        TextField resourceField = new TextField(wallet.getResource());

        Button editWalletBtn = new Button("edit");
        editWalletBtn.setOnAction( e -> openEditWalletDialog(wallet));

        Button deleteWalletBtn = new Button("delete");
        deleteWalletBtn.setOnAction( e -> openDeleteWalletDialog(wallet));

        HBox hbResource = new HBox(5, resourceLabel, resourceField, editWalletBtn, deleteWalletBtn);
        // twelve words and pin rows
        Label twelveWordsLabel = new Label("twelve words: ");

        TextField addressField = new TextField(wallet.getAddress());
        addressField.setPrefColumnCount(24);
        TextField passwordField = new PasswordField();
        passwordField.setText(wallet.getPassword());

        FlowPane twelveWordsBox = new FlowPane(6,3);
        // The cycle that adds words to a page
        for (String word : wallet.getTwelveWords()){
            Label wordLabel = new Label(word);
            twelveWordsBox.getChildren().add(wordLabel);
        }
        // add everything on showlistcontentvbox
        vb.getChildren().addAll(hbResource, twelveWordsLabel, twelveWordsBox, addressField, passwordField);
        showListContentVBox.getChildren().add(vb);
    }
    private void openEditAccountDialog(Account account){
        String oldResource = account.getResource();
        String oldUsername = account.getUsername();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit account: " + account.getResource());

        TextField resourceField = new TextField(account.getResource());
        TextField loginField = new TextField(account.getUsername());
        TextField passwordField = new TextField(account.getPassword());

        ThemeManager.applyThemeToDialog(dialog);

        VBox vb = new VBox(10,
                new HBox(5, new Label("Resource:"), resourceField),
                new HBox(5, new Label("Login:"), loginField),
                new HBox(5, new Label("Password:"), passwordField)
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
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

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit card: " + card.getResource());

        TextField resourceField = new TextField(card.getResource());
        TextField cardNumberField = new TextField(card.getCardNumber());
        TextField expiryDateField = new TextField(card.getExpiryDate());
        TextField cvvField = new TextField(card.getCvv());
        TextField ownerNameField = new TextField(card.getOnwerName());

        ThemeManager.applyThemeToDialog(dialog);

        VBox vb = new VBox(10,
                new HBox(5, new Label("Resource:"), resourceField),
                new HBox(5, new Label("Card Number:"), cardNumberField),
                new HBox(5, new Label("Expiry Date:"), expiryDateField),
                new HBox(5, new Label("CVV:"), cvvField),
                new HBox(5, new Label("Owner Name:"), ownerNameField)
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                card.setResource(resourceField.getText());
                card.setCardNumber(cardNumberField.getText());
                card.setExpiryDate(expiryDateField.getText());
                card.setCvv(cvvField.getText());
                card.setOnwerName(ownerNameField.getText());
                new DatabaseManager().updateCard(card, oldResource, oldCardNumber, oldCardName, oldExpiryDate);
                LogsManager.logEdit("Card", card.getResource());
                onShowCards(); // Refresh
            }
        });
    }
    private void openEditLinkDialog(Link link){
        String oldResource = link.getResource();
        String oldLink = link.getLink();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit link " + link.getResource());

        TextField resourceField = new TextField(link.getResource());
        TextField linkField = new TextField(link.getLink());

        ThemeManager.applyThemeToDialog(dialog);

        VBox vb = new VBox(10,
                new HBox(5, new Label("Resource: "), resourceField),
                new HBox(5, new Label("Link:     "), linkField)
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
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

        TextField resourceField = new TextField(wallet.getResource());
        TextField pinField = new TextField(wallet.getPassword());

        FlowPane fp = new FlowPane(6,3);
        for(String word : wallet.getTwelveWords()){
            TextField wordField = new TextField(word);
            fp.getChildren().add(wordField);
        }

        ThemeManager.applyThemeToDialog(dialog);

        VBox vb = new VBox(5,
                new HBox(new Label("Resource: "), resourceField),
                new VBox(new Label("Key words: "), fp),
                new HBox(new Label("Pin:      "), pinField)
        );
        vb.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent( result -> {
            if(result == ButtonType.OK){
                wallet.setResource(resourceField.getText());
                wallet.setPassword(pinField.getText());
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

        HBox parent = (HBox) passwordField.getParent();
        int index = parent.getChildren().indexOf(passwordField);
        parent.getChildren().set(index, plainField);

        toggleBtn.setText("hide");
        toggleBtn.setOnAction( e -> {
            passwordField.setText(plainField.getText());
            parent.getChildren().set(index, passwordField);
            toggleBtn.setText("show");
            toggleBtn.setOnAction( ev -> togglePasswordVisibility(passwordField, toggleBtn));
        });
    }
}
