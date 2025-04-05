package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    @FXML private Button loginButton;

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            welcomeText.setText("⚠ Please enter both username and password.");
            return;
        }

        if (username.equals(PreferencesManager.getUsername()) &&
                password.equals(PreferencesManager.getPassword())) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
                Scene mainScene = new Scene(fxmlLoader.load());
                ThemeManager.registerScene(mainScene);

                Stage mainStage = new Stage();
                mainStage.setScene(mainScene);
                mainStage.setTitle("Password manager");
                mainStage.setOnCloseRequest(e -> ThemeManager.unregisterScene(mainScene));
                mainStage.show();

                LogsManager.logLogin();

                // Close login stage
                Stage loginStage = (Stage) loginButton.getScene().getWindow();
                loginStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                welcomeText.setText("❌ Failed to load main page.");
            }
        } else {
            welcomeText.setText("❌ Incorrect username or password.");
        }
    }

    @FXML
    protected void onCreateAccountButtonClick() {
        loginVBox.setVisible(false);
        createAccountVBox.setVisible(true);
    }

    @FXML
    protected void onBackToLogin() {
        createAccountVBox.setVisible(false);
        loginVBox.setVisible(true);
        welcomeText.setText("Welcome");
    }

    @FXML
    protected void onCreateNewAccount() {
        String newUser = newUsernameField.getText().trim();
        String newPass = newPasswordField.getText().trim();
        String confirmPass = confirmPasswordField.getText().trim();

        if (newUser.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            welcomeText.setText("⚠ All fields are required.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            welcomeText.setText("⚠ Passwords do not match.");
            return;
        }

        PreferencesManager.createNewPreferences(newUser, newPass, confirmPass);

        welcomeText.setText("✅ Account created. Please log in.");
        createAccountVBox.setVisible(false);
        loginVBox.setVisible(true);

    }
}

