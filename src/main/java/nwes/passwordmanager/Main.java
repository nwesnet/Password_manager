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
        DatabaseManager.connectAndInitialize();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Password manager");
        stage.setScene(scene);
        stage.show();
    }
}
