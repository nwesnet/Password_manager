package nwes.passwordmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load());
            MainPageController mainPageController = fxmlLoader.getController();
            Stage mainStage = new Stage();

            ThemeManager.registerScene(mainScene);
            mainStage.setOnCloseRequest( e -> ThemeManager.unregisterScene(mainScene));

            mainStage.setTitle("Password manager");
            mainStage.setScene(mainScene);
            mainStage.show();
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            LogsManager.logLogin();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    protected void onCreateAccountButtonClick(){
        welcomeText.setText("It works too!");
    }
}
