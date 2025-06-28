package nwes.passwordmanager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Set;

public class LoginController {

    @FXML private VBox loginVBox;
    @FXML private VBox createAccountVBox;
    @FXML private VBox dynamicalFieldsVBox;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label welcomeText;

    @FXML private TextField emailField;
    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label createText;

    @FXML private Button loginButton;

    @FXML
    private CheckBox connectToServerCheckboxL;
    @FXML
    private CheckBox connectToServerCheckboxC;

    @FXML
    public void initialize() {
        connectToServerCheckboxC.selectedProperty().addListener((observer, oldValue, newValue) -> {
            emailField.setVisible(newValue);
        });
    }

    @FXML
    protected void onLoginButtonClick() throws Exception{
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            welcomeText.setText("Please enter both username and password.");
            return;
        }

        if (connectToServerCheckboxL.isSelected()) {
            PreferencesManager.Preferences pref = SyncManager.loginOnServer(username, password);
            if (pref != null) {
                PreferencesManager.setPreferencesInMemory(pref);
                try {
                    SecretKey key = EncryptionUtils.getKeyFromString(username + password);
                    EncryptionUtils.setAppKey(key);
                    // Prepare main page scene
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                    Scene mainScene = new Scene(fxmlLoader.load());
                    ThemeManager.registerScene(mainScene);
                    // Create the stage for main page and show it
                    Stage mainStage = new Stage();
                    mainStage.setScene(mainScene);
                    mainStage.setTitle("Password manager");
                    mainStage.setOnCloseRequest(e -> ThemeManager.unregisterScene(mainScene));
                    mainStage.show();
                    Platform.runLater(() -> {
                        mainStage.setMinWidth(650);
                        mainStage.setMinHeight(550);
                        mainStage.setWidth(850);
                        mainStage.setHeight(750);
                    });
                    // Set login action to logs
                    LogsManager.logLogin();
                    // Close login stage
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();
                    // Sync with website
                    if (PreferencesManager.isSyncEnabled()) {
                        DatabaseManager dm = new DatabaseManager();
                        Set<Account> localAccounts = dm.getAllAccounts(false, true, PreferencesManager.getUsernameEncrypted());
                        Set<Account> serverAccounts = SyncManager.syncAccounts(localAccounts, username, password);
                        if (serverAccounts != null) {
                            dm.mergeServerAccounts(serverAccounts);
                        }
                        Set<Card> localCards = dm.getAllCards(false, true, PreferencesManager.getUsernameEncrypted());
                        Set<Card> serverCards = SyncManager.syncCards(localCards, username, password);
                        if (serverCards != null) {
                            dm.mergeServerCards(serverCards);
                        }
                        Set<Link> localLinks = dm.getAllLinks(false, true, PreferencesManager.getUsernameEncrypted());
                        Set<Link> serverLinks = SyncManager.syncLinks(localLinks, username, password);
                        if (serverLinks != null) {
                            dm.mergeServerLinks(serverLinks);
                        }
                        Set<Wallet> localWallets = dm.getAllWallets(false, true, PreferencesManager.getUsernameEncrypted());
                        Set<Wallet> serverWallets = SyncManager.syncWallets(localWallets, username, password);
                        if (serverWallets != null) {
                            dm.mergeServerWallets(serverWallets);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    welcomeText.setText("Failed to load main page.");
                }

            }
        } else {
            SecretKey key = EncryptionUtils.getKeyFromString(username + password);
            EncryptionUtils.setAppKey(key);
            // Compare the input data with login
            if (username.equals(PreferencesManager.getUsername()) &&
                    password.equals(PreferencesManager.getPassword())) {
                try {
                    // Prepare main page scene
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                    Scene mainScene = new Scene(fxmlLoader.load());
                    ThemeManager.registerScene(mainScene);
                    // Create the stage for main page and show it
                    Stage mainStage = new Stage();
                    mainStage.setScene(mainScene);
                    mainStage.setTitle("Password manager");
                    mainStage.setOnCloseRequest(e -> ThemeManager.unregisterScene(mainScene));
                    mainStage.show();
                    Platform.runLater(() -> {
                        mainStage.setMinWidth(650);
                        mainStage.setMinHeight(550);
                        mainStage.setWidth(850);
                        mainStage.setHeight(750);
                    });
                    // Set login action to logs
                    LogsManager.logLogin();
                    // Close login stage
                    Stage loginStage = (Stage) loginButton.getScene().getWindow();
                    loginStage.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    welcomeText.setText("Failed to load main page.");
                }
            } else {
                welcomeText.setText("Incorrect username or password.");
            }
        }
    }
    // Hide login vbox and show create account vbox
    @FXML
    protected void onCreateAccountButtonClick() {
        loginVBox.setVisible(false);
        createAccountVBox.setVisible(true);
    }
    // Hide create account vbox and show login vbox
    @FXML
    protected void onBackToLogin() {
        createAccountVBox.setVisible(false);
        loginVBox.setVisible(true);
        welcomeText.setText("Welcome");
    }
    // Crete account logic
    @FXML
    protected void onCreateNewAccount() {
        boolean connectToServer = connectToServerCheckboxC.isSelected();
        // Get input data
        if (connectToServer) {
            String newEmail = emailField.getText().trim();
            String newUser = newUsernameField.getText().trim();
            String newPass = newPasswordField.getText().trim();
            String confirmPass = confirmPasswordField.getText().trim();
            if (newEmail.isEmpty() || newUser.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                createText.setText("All fields should be required.");
                return;
            }
            if (!isValidEmail(newEmail)) {
                createText.setText("Invalid email format.");
                return;
            }
            if (newPass.equals(confirmPass)) {
                createText.setText("Passwords matches");
                return;
            }
            String error = SyncManager.registerOnServer(newEmail, newUser, newPass, confirmPass);
            if (error == null) {
                createText.setText("Account created");
                createAccountVBox.setVisible(false);
                loginVBox.setVisible(true);
            } else {
                createText.setText("Server Error: " + error);
            }

        } else {
            String newUser = newUsernameField.getText().trim();
            String newPass = newPasswordField.getText().trim();
            String confirmPass = confirmPasswordField.getText().trim();
            // Check if isn't empty
            if (newUser.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                createText.setText("All fields are required.");
                return;
            }

            if (newPass.equals(confirmPass)) {
                createText.setText("Passwords matches.");
                return;
            }
            // Create new account
            PreferencesManager.createNewPreferences(newUser, newPass, confirmPass, String.valueOf(connectToServer));
            createText.setText("Account created");
            createAccountVBox.setVisible(false);
            loginVBox.setVisible(true);
        }

    }
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

}

