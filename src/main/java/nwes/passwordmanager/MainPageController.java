package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Button logs;
    @FXML
    private VBox leftVBox;
    @FXML
    private ScrollPane logsScrollPane;
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
    }
    @FXML
    protected void onAddButtonClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-view.fxml"));
            Scene addScene = new Scene(fxmlLoader.load());
            Stage addStage = new Stage();
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
        } else {
            showListVBox.setVisible(false);
            showListContentVBox.getChildren().clear();
        }
    }
    // add this later: showlistsearchbar.setText("");
    @FXML
    protected void onShowAccounts(){
        currentCategory = Category.ACCOUNTS;
        showListContentVBox.getChildren().clear();

        DatabaseManager dbManager = new DatabaseManager();
        allAccounts = dbManager.getAllAccounts();

        for (Account account : allAccounts){
            DisplayAccountsDetails(account,dbManager);
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
        } else {
            preferencesHBox.setVisible(false);
            preferencesVBox.setVisible(false);
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
    protected void onDoubleConfirmationClick(){

    }
    @FXML
    protected void onThemeButtonClick(){

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

    private void DisplayAccountsDetails(Account account, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));

        Label resourceLabel = new Label("Resource: ");
        TextField resourceField = new TextField(account.getResource());
        resourceField.setPrefColumnCount(16);
        resourceField.setEditable(false);
        HBox hbResource = new HBox(5);
        hbResource.getChildren().addAll(resourceLabel, resourceField);

        Label loginLabel = new Label("Login:        ");
        TextField loginField = new TextField(account.getUsername());
        loginField.setPrefColumnCount(16);
        loginField.setEditable(false);
        HBox hbLogin = new HBox(5);
        hbLogin.getChildren().addAll(loginLabel, loginField);

        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setText(account.getPassword());
        passwordField.setPrefColumnCount(16);
        passwordField.setEditable(false);
        HBox hbPassword = new HBox(5);
        hbPassword.getChildren().addAll(passwordLabel, passwordField);

        vb.getChildren().addAll(hbResource, hbLogin, hbPassword);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayCardsDetails(Card card, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));

        Label resourceLabel = new Label("Resource: " + card.getResource());
        TextField cardNumberField = new TextField(card.getCardNumber());
        TextField cardExpiryDate = new TextField(card.getExpiryDate());
        TextField cardCVVField = new TextField(card.getCvv());
        TextField cardOwnerNameField = new TextField(card.getOnwerName());

        HBox hbDateAndCVV = new HBox(5);
        hbDateAndCVV.getChildren().addAll(cardExpiryDate, cardCVVField);

        vb.getChildren().addAll(resourceLabel, cardNumberField, hbDateAndCVV, cardOwnerNameField);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayLinksDetails(Link link, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));

        Label description = new Label("Resource: " + link.getResource());

        vb.getChildren().add(description);
        showListContentVBox.getChildren().add(vb);
    }
    private void DisplayWalletsDetails(Wallet wallet, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));

        Label resourceLabel = new Label("Resource: " + wallet.getResource());
        Label twelveWordsLabel = new Label("twelve words: ");
        TextField passwordField = new PasswordField();
        passwordField.setText(wallet.getPassword());

        FlowPane twelveWordsBox = new FlowPane(6,3);

        for (String word : wallet.getTwelveWords()){
            Label wordLabel = new Label(word);
            twelveWordsBox.getChildren().add(wordLabel);
        }
        vb.getChildren().addAll(resourceLabel, twelveWordsLabel, twelveWordsBox, passwordField);
        showListContentVBox.getChildren().add(vb);
    }
}
