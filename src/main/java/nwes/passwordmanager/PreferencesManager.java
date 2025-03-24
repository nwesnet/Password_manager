package nwes.passwordmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PreferencesManager {
    private static final String PREFS_FILE = "preferences.json";
    private static Preferences preferences;

    static {
        loadPreferences();  // Load preferences when the app starts
    }

    // Class to hold settings
    public static class Preferences {
        public LoginInfo login_info = new LoginInfo();
        public Security security = new Security();
        public String theme = "light";
    }

    public static class LoginInfo {
        public String username = "default_user";
        public String password = "default_pass";
        public String pincode = "default_pincode";
    }

    public static class Security {
        public boolean double_confirmation = true;
    }

    // Load JSON preferences from file
    public static void loadPreferences() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PREFS_FILE)) {
            preferences = gson.fromJson(reader, Preferences.class);
            System.out.println("✅ Preferences loaded: " + gson.toJson(preferences));
        } catch (IOException e) {
            System.out.println("⚠ Preferences file not found. Using default settings.");
            preferences = new Preferences();
            savePreferences();
        }
    }

    // Save JSON preferences to file
    public static void savePreferences() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(PREFS_FILE)) {
            gson.toJson(preferences, writer);
            System.out.println("✅ Preferences saved: " + gson.toJson(preferences));
        } catch (IOException e) {
            System.out.println("❌ Error saving preferences: " + e.getMessage());
        }
    }

    // Getters & Setters
    public static String getUsername() {
        return preferences.login_info.username;
    }

    public static void setUsername(String username) {
        preferences.login_info.username = username;
        savePreferences();
    }

    public static String getPassword() {
        return preferences.login_info.password;
    }

    public static void setPassword(String password) {
        preferences.login_info.password = password;
        savePreferences();
    }

    public static String getPincode(){
        return preferences.login_info.pincode;
    }

    public static void setPincode(String pincode){
        preferences.login_info.pincode = pincode;
        savePreferences();
    }

    public static boolean isDoubleConfirmationEnabled() {
        return preferences.security.double_confirmation;
    }

    public static void setDoubleConfirmation(boolean enabled) {
        preferences.security.double_confirmation = enabled;
        savePreferences();
    }

    public static String getTheme() {
        return preferences.theme;
    }

    public static void setTheme(String theme) {
        preferences.theme = theme;
        savePreferences();
    }
}

