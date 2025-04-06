package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;

public class LoginController {

    @FXML private VBox loginVBox;
    @FXML private VBox createAccountVBox;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label welcomeText;

    @FXML private TextField newUsernameField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label createText;

    @FXML private Button loginButton;

    @FXML
    protected void onLoginButtonClick() throws Exception{
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            welcomeText.setText("Please enter both username and password.");
            return;
        }

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
        // Get input data
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
        PreferencesManager.createNewPreferences(newUser, newPass, confirmPass);
        createText.setText("Account created");
        createAccountVBox.setVisible(false);
        loginVBox.setVisible(true);
    }
}

