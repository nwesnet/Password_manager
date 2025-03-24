package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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

    private boolean showListVisible = false;
    private boolean preferencesHBoxVisible = false;
    private boolean preferencesVBoxVisible = false;
    private boolean accountInfoVBoxVisible = false;
    private boolean logsVisible = false;
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
            DatabaseManager dbManager = new DatabaseManager();
            List<Account> accounts = dbManager.getAllAccounts();
            List<Card> cards = dbManager.getAllCards();
            if (accounts.isEmpty()){
                Label emptyMessage = new Label("You didn't add any account yet.");
                showListVBox.getChildren().add(emptyMessage);
                for (Account account : accounts) {
                    System.out.println(account);
                }
            } else {
                for (Account account : accounts) {
                    DisplayAccountsDetails(account, dbManager);
                }
                for (Card card : cards) {
                    DisplayCardsDetails(card, dbManager);
                }

            }
        } else {
            showListVBox.setVisible(false);
            showListVBox.getChildren().removeIf(node -> !(node instanceof TextField));
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
    private void DisplayAccountsDetails(Account account, DatabaseManager dbManager){
        VBox vb = new VBox(5);
        vb.setPadding(new Insets(20));

        Label resourceLabel = new Label("Resource" + account.getResource());
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

        vb.getChildren().addAll(resourceLabel, hbLogin, hbPassword);
        showListVBox.getChildren().add(vb);
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
        showListVBox.getChildren().add(vb);
    }
    private void DisplayLinksDetails(Link link, DatabaseManager dbManager){

    }
    private void DisplayWalletsDetails(Wallet wallte, DatabaseManager dbManager){

    }

}
