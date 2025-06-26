package nwes.passwordmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
    public static void main(String[] args){
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        PreferencesManager.loadPreferences(true);
        DatabaseManager.initializeDatabase();
        LogsManager.initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        ThemeManager.applyCurrentTheme(scene);
        stage.setOnCloseRequest( e -> ThemeManager.unregisterScene(scene));

        stage.setTitle("Password manager");
        stage.setScene(scene);
        stage.show();
    }
}
