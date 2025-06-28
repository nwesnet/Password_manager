package nwes.passwordmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PreferencesManager {
    private static final String PREFS_FILE_PATH;
    private static final String PREFS_FILE;
    private static Preferences preferences;
    private static boolean usingServerPreferences = false;
    static {
        String os = System.getProperty("os.name").toLowerCase();
        Path presPath;

        if (os.contains("win")) {
            String appData = System.getenv("LOCALAPPDATA");
            if (appData == null) appData = System.getProperty("user.home");
            presPath = Paths.get(appData, "PasswordManager", "preferences.json");
        } else {
            String userHome = System.getProperty("user.home");
            presPath = Paths.get(userHome, ".local", "share", "PasswordManager", "preferences.json");
        }
        File dir = presPath.getParent().toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        PREFS_FILE_PATH = presPath.toString();
        PREFS_FILE = PREFS_FILE_PATH;
    }
    // Load preferences when the app starts
    static {
        loadPreferences(true);
    }
    // Class to hold settings
    public static class Preferences {
        public LoginInfo login_info = new LoginInfo();
        public Security security = new Security();
        public String theme = "light";
        public String sync = "false";
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
    public static void createNewPreferences(String username, String password, String pincode, String sync) {
        try {
            SecretKey key = EncryptionUtils.getKeyFromString(username + password);

            if (preferences == null) preferences = new Preferences();
            preferences.login_info.username = EncryptionUtils.encrypt(username, key);
            preferences.login_info.password = EncryptionUtils.encrypt(password, key);
            preferences.login_info.pincode = EncryptionUtils.encrypt(pincode, key);
            preferences.security.double_confirmation = EncryptionUtils.encrypt("true" , key);
            preferences.security.store_logs = EncryptionUtils.encrypt("true" , key);
            preferences.sync = EncryptionUtils.encrypt(sync, key);

            savePreferences();

        } catch (Exception e) {
            System.out.println("❌ Error encrypting preferences: " + e.getMessage());
        }
    }
    // Load JSON preferences from file
    public static void loadPreferences(boolean loadFromFile) {
        if (loadFromFile) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(PREFS_FILE)) {
                preferences = gson.fromJson(reader, Preferences.class);
                // System.out.println("✅ Preferences loaded: " + gson.toJson(preferences));
            } catch (IOException e) {
                System.out.println("⚠ Preferences file not found. Using default settings.");
                preferences = new Preferences();
                savePreferences();
            }
        } else {
            preferences = new Preferences();
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
    public static void setPreferencesInMemory(Preferences prefs) {
        preferences = prefs; // Just sets the static variable in memory
        usingServerPreferences = true;
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
    // get username encrypted
    public static String getUsernameEncrypted() {
        return preferences.login_info.username;
    }
    public static void setUsername(String username) {
        try {
            preferences.login_info.username = EncryptionUtils.encrypt(username);
            if (!usingServerPreferences){
                savePreferences();
            }
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
            if (!usingServerPreferences) {
                savePreferences();
            }
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
            if (!usingServerPreferences) {
                savePreferences();
            }
        } catch (Exception e) {
            System.out.println("❌ Pincode encrypt failed: " + e.getMessage());
        }
    }
    public static String getTheme() {
        return preferences.theme;
    }
    public static void setTheme(String theme) {
        preferences.theme = theme;
        if (!usingServerPreferences) {
            savePreferences();
        }
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
            if (!usingServerPreferences) {
                savePreferences();
            }
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
            if (!usingServerPreferences) {
                savePreferences();
            }
        } catch (Exception e) {
            System.out.println("❌ Store logs encrypt failed: " + e.getMessage());
        }
    }

    public static String getThemeCssPath() {
        String theme = getTheme();
        return "/css/" + theme + "-theme.css";
    }

    public static boolean isSyncEnabled() {
        try {
            return Boolean.parseBoolean(EncryptionUtils.decrypt(preferences.sync));
        } catch (Exception e) {
            System.out.println("Sync decyrpt failed:" + e.getMessage());
            return false;
        }
    }
    public static void setSyncEnabled(boolean enabled) {
        try {
            preferences.sync = EncryptionUtils.encrypt(String.valueOf(enabled));
            if (!usingServerPreferences) {
                savePreferences();
            }
        } catch (Exception e) {
            System.out.println("Sync encrypt failed: " + e.getMessage());
        }
    }
}

