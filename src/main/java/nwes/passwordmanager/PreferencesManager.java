package nwes.passwordmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.crypto.SecretKey;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PreferencesManager {
    private static final String PREFS_FILE = "preferences.json";
    private static Preferences preferences;
    // Load preferences when the app starts
    static {
        loadPreferences();
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
        public String double_confirmation = "true";
        public String store_logs = "true";
    }
    public static void createNewPreferences(String username, String password, String pincode) {
        try {
            SecretKey key = EncryptionUtils.getKeyFromString(username + password);

            if (preferences == null) preferences = new Preferences();
            preferences.login_info.username = EncryptionUtils.encrypt(username, key);
            preferences.login_info.password = EncryptionUtils.encrypt(password, key);
            preferences.login_info.pincode = EncryptionUtils.encrypt(pincode, key);
            preferences.security.double_confirmation = EncryptionUtils.encrypt("true" , key);
            preferences.security.store_logs = EncryptionUtils.encrypt("true" , key);

            savePreferences();

        } catch (Exception e) {
            System.out.println("❌ Error encrypting preferences: " + e.getMessage());
        }
    }
    // Load JSON preferences from file
    public static void loadPreferences() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PREFS_FILE)) {
            preferences = gson.fromJson(reader, Preferences.class);
            // System.out.println("✅ Preferences loaded: " + gson.toJson(preferences));
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
            // System.out.println("✅ Preferences saved: " + gson.toJson(preferences));
        } catch (IOException e) {
            System.out.println("❌ Error saving preferences: " + e.getMessage());
        }
    }
    // Getters & Setters
    public static String getUsername() {
        try {
            return EncryptionUtils.decrypt(preferences.login_info.username);
        } catch (Exception e) {
            System.out.println("❌ Username decrypt failed: " + e.getMessage());
            return "";
        }
    }
    public static void setUsername(String username) {
        try {
            preferences.login_info.username = EncryptionUtils.encrypt(username);
            savePreferences();
        } catch (Exception e) {
            System.out.println("❌ Username encrypt failed: " + e.getMessage());
        }
    }
    public static String getPassword() {
        try {
            return EncryptionUtils.decrypt(preferences.login_info.password);
        } catch (Exception e) {
            System.out.println("❌ Password decrypt failed: " + e.getMessage());
            return "";
        }
    }
    public static void setPassword(String password) {
        try {
            preferences.login_info.password = EncryptionUtils.encrypt(password);
            savePreferences();
        } catch (Exception e) {
            System.out.println("❌ Password encrypt failed: " + e.getMessage());
        }
    }
    public static String getPincode(){
        try {
            return EncryptionUtils.decrypt(preferences.login_info.pincode);
        } catch (Exception e) {
            System.out.println("❌ Pincode decrypt failed: " + e.getMessage());
            return "";
        }
    }
    public static void setPincode(String pincode){
        try {
            preferences.login_info.pincode = EncryptionUtils.encrypt(pincode);
            savePreferences();
        } catch (Exception e) {
            System.out.println("❌ Pincode encrypt failed: " + e.getMessage());
        }
    }
    public static String getTheme() {
        return preferences.theme;
    }
    public static void setTheme(String theme) {
        preferences.theme = theme;
        savePreferences();
    }

    public static boolean isDoubleConfirmationEnabled() {
        try {
            return Boolean.parseBoolean(EncryptionUtils.decrypt(preferences.security.double_confirmation));
        } catch (Exception e) {
            System.out.println("❌ Double confirm decrypt failed: " + e.getMessage());
            return true;
        }
    }
    public static void setDoubleConfirmation(boolean enabled) {
        try {
            preferences.security.double_confirmation = EncryptionUtils.encrypt(String.valueOf(enabled));
            savePreferences();
        } catch (Exception e) {
            System.out.println("❌ Double confirm encrypt failed: " + e.getMessage());
        }
    }

    public static boolean isStoreLogsEnabled() {
        try {
            return Boolean.parseBoolean(EncryptionUtils.decrypt(preferences.security.store_logs));
        } catch (Exception e) {
            System.out.println("❌ Store logs decrypt failed: " + e.getMessage());
            return true;
        }
    }
    public static void setStoreLogsEnabled(boolean enabled) {
        try {
            preferences.security.store_logs = EncryptionUtils.encrypt(String.valueOf(enabled));
            savePreferences();
        } catch (Exception e) {
            System.out.println("❌ Store logs encrypt failed: " + e.getMessage());
        }
    }

    public static String getThemeCssPath() {
        String theme = getTheme();
        return "/css/" + theme + "-theme.css";
    }
}

