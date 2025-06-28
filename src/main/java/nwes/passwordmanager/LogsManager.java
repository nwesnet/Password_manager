package nwes.passwordmanager;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogsManager {
    private static final String LOG_FILE_PATH;
    private static final String LOG_FILE;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    static {
        String os = System.getProperty("os.name").toLowerCase();
        Path logsPath;
        if (os.contains("win")) {
            String appData = System.getenv("LOCALAPPDATA");
            if (appData == null) appData = System.getProperty("user.home");
            logsPath = Paths.get(appData, "PasswordManager", "history.txt");
        } else {
            String userHome = System.getProperty("user.home");
            logsPath = Paths.get(userHome, ".local", "share", "PasswordManager", "history.txt");
        }
        File dir = logsPath.getParent().toFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LOG_FILE_PATH = logsPath.toString();
        LOG_FILE = LOG_FILE_PATH;
    }

    public static void initialize() {
        try {
            Path logsPath = Paths.get(LOG_FILE);
            if (Files.notExists(logsPath)) {
                Files.createFile(logsPath);
                // You can log this later after key is ready
            }
        } catch (IOException e) {
            // This should be silent or logged once logging is ready
        }
    }

    private static void writeLog(String message) {
        try(FileWriter writer = new FileWriter(LOG_FILE, true);
            BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = message + " [" + timestamp + "]";
            String encryptedLog = EncryptionUtils.encrypt(logEntry);
            bw.write(encryptedLog);
            bw.newLine();

        } catch (Exception e) {
            System.out.println("❌ Failed to write encrypted log: " + e.getMessage());
        }
    }
    public static String readLogs() {
        StringBuilder decryptedLogs = new StringBuilder();
        try {
            Path path = Paths.get(LOG_FILE);
            if (!Files.exists(path)) return "";

            for (String line : Files.readAllLines(path)) {
                try {
                    String decrypted = EncryptionUtils.decrypt(line); // 🔓
                    decryptedLogs.append(decrypted).append("\n");
                } catch (Exception e) {
                    decryptedLogs.append("❌ Failed to decrypt log line\n");
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Failed to read logs: " + e.getMessage());
        }
        return decryptedLogs.toString();
    }
    public static void logLogin() {
        writeLog("Login");
    }
    public static void logAdd(String itemType, String resource) {
        writeLog("Added " + itemType + " for " + resource);
    }
    public static void logEdit(String itemType, String resource) {
        writeLog(itemType + " data of " + resource + " was changed");
    }
    public static void logDelete(String itemType, String resource) {
        writeLog(itemType + " " + resource + " was deleted");
    }
    public static void logClear() {
        try (FileWriter writer = new FileWriter(LOG_FILE, false);
             BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = "The history was cleared at [ " + timestamp + " ]";

            bw.write(EncryptionUtils.encrypt(logEntry)); // 🔐
            bw.newLine();

        } catch (Exception e) {
            System.out.println("❌ Failed to write clear log: " + e.getMessage());
        }
    }
    public static void writeOptionalLog(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, false);
             BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            String logEntry = message + " [ " + timestamp + " ]";

            bw.write(EncryptionUtils.encrypt(logEntry)); // 🔐
            bw.newLine();

        } catch (Exception e) {
            System.out.println("❌ Failed to write optional log: " + e.getMessage());
        }
    }
    public static void reencryptLogs(SecretKey oldKey, SecretKey newKey) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("history.txt"));
            List<String> reencrypted = new ArrayList<>();
            for (String line : lines) {
                String decrypted = EncryptionUtils.decrypt(line, oldKey);
                String encrypted = EncryptionUtils.encrypt(decrypted, newKey);
                reencrypted.add(encrypted);
            }
            Files.write(Paths.get("history.txt"), reencrypted);
        } catch (Exception e) {
            System.out.println("❌ Failed to reencrypt logs: " + e.getMessage());
        }
    }
}
