package nwes.passwordmanager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXML;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label welcomeText;
    @FXML
    protected void onLoginButtonClick(){
        welcomeText.setText("It works!");
    }
    @FXML
    protected void onCreateAccountButtonClick(){
        welcomeText.setText("It works too!");
    }
}
