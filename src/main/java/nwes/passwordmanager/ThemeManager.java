package nwes.passwordmanager;

import javafx.scene.Scene;
import javafx.scene.control.Dialog;

import java.util.HashSet;
import java.util.Set;

public class ThemeManager {
    private static final Set<Scene> scenes = new HashSet<>();

    public static void registerScene(Scene scene) {
        scenes.add(scene);
        applyCurrentTheme(scene);
    }
    public static void unregisterScene(Scene scene) {
        scenes.remove(scene);
    }
    public static void applyCurrentTheme(Scene scene) {
        scene.getStylesheets().clear();
        String css = ThemeManager.class.getResource(PreferencesManager.getThemeCssPath()).toExternalForm();
        scene.getStylesheets().add(css);
    }
    public static void applyThemeToAll() {
        for (Scene scene : scenes) {
            applyCurrentTheme(scene);
        }
    }
    public static void applyThemeToDialog(Dialog<?> dialog) {
        dialog.setOnShown( e -> {
            Scene scene = dialog.getDialogPane().getScene();
            if(scene != null) registerScene(scene);
        });
        dialog.setOnHidden( e -> {
            Scene scene = dialog.getDialogPane().getScene();
            if(scene != null) unregisterScene(scene);
        });
    }

}
