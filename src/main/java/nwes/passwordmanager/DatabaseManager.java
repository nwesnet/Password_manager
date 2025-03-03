package nwes.passwordmanager;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";
    private static final String ENCRYPTION_KEY = "your_secret_password";

    public static void connectAndInitialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA key = '" + ENCRYPTION_KEY + "';");
            stmt.execute("PRAGMA cipher_memory_security = ON;");
            createTable(stmt);

            System.out.print("Database initialized and tables are ready!");
        } catch (Exception e) {
            System.out.println("❌ SQLCipher Connection Error: " + e.getMessage());
        }
    }
    private static void createTable(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS Account ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL, "
                + "login TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
        stmt.execute("CREATE TABLE IF NOT EXISTS Cards ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL, "
                + "card_number TEXT NOT NULL, "
                + "expiry_date TEXT NOT NULL, "
                + "cvv TEXT NOT NULL, "
                + "owner_name TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        stmt.execute("CREATE TABLE IF NOT EXISTS Wallets ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL, "
                + "twelve_words TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        stmt.execute("CREATE TABLE IF NOT EXISTS Links ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL);");
    }
    public void writeAccountTodb(String username) {
        String sql = "INSERT INTO Accounts (resource, login, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 🔐 Apply SQLCipher encryption key
            conn.createStatement().execute("PRAGMA key = '" + ENCRYPTION_KEY + "';");

            // Sample values for testing
            String resource = "TestResource";
            String password = "TestPassword123";

            // 🔹 Set the values dynamically
            pstmt.setString(1, resource);  // resource
            pstmt.setString(2, username);  // login (username)
            pstmt.setString(3, password);  // password

            // ✅ Execute the insert query
            pstmt.executeUpdate();
            System.out.println("✅ Account successfully inserted: " + username);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

}
