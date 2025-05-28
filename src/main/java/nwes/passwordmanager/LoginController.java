package nwes.passwordmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private CheckBox connectToServerCheckbox;

    @FXML
    public void initialize() {
        connectToServerCheckbox.selectedProperty().addListener((observer, oldValue, newValue) -> {
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
        boolean connectToServer = connectToServerCheckbox.isSelected();
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
            String error = registerOnServer(newEmail, newUser, newPass, confirmPass);
            if (error == null) {
                // Create new account
                PreferencesManager.createNewPreferences(newUser, newPass, confirmPass, String.valueOf(connectToServer));
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
    public static String registerOnServer(String email, String username, String password, String confirmPass) {
        try {
            URL url = new URL("http://localhost:8080/api/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format(
                    "{\"email\":\"%s\", \"username\":\"%s\", \"password\":\"%s\", \"additionalPassword\":\"%s\"}",
                    email, username, password, confirmPass
            );
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return new String(err.readAllBytes());
                }
            }
        } catch(Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

}

