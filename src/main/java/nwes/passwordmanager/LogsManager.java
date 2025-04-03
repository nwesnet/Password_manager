package nwes.passwordmanager;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogsManager {
    private static final String LOG_FILE = "history.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        try {
            Path logsPath = Paths.get(LOG_FILE);
            if(Files.notExists(logsPath)) {
                Files.createFile(logsPath);
                System.out.println("History.txt file created");
            }

        } catch (IOException e) {
            System.out.println("❌ Failed to create log file: " + e.getMessage());
        }
    }

    private static void writeLog(String message) {
        try(FileWriter writer = new FileWriter(LOG_FILE, true);
            BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            bw.write(message + " [" + timestamp + "]");
            bw.newLine();

        } catch (IOException e) {
            System.out.println("❌ Failed to write log: " + e.getMessage());
        }
    }
    public static String readLogs() {
        try {
            return new String(Files.readAllBytes(Paths.get(LOG_FILE)));
        } catch (IOException e) {
            System.out.println("❌ Failed to read logs: " + e.getMessage());
            return "";
        }
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
        try(FileWriter writer = new FileWriter(LOG_FILE, false);
            BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            bw.write("The history was cleared at [ " + timestamp + " ]");
            bw.newLine();

        } catch (IOException e) {
            System.out.println("❌ Failed to write log: " + e.getMessage());
        }
    }
    public static void writeOptionalLog(String message) {
        try(FileWriter writer = new FileWriter(LOG_FILE, false);
            BufferedWriter bw = new BufferedWriter(writer)) {

            String timestamp = LocalDateTime.now().format(formatter);
            bw.write(message + " [ " + timestamp + " ]");
            bw.newLine();

        } catch (IOException e) {
            System.out.println("❌ Failed to write log: " + e.getMessage());
        }
    }
}
