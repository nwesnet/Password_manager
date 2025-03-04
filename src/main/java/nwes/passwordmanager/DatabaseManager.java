package nwes.passwordmanager;

import java.sql.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:passwords.db";  // Standard SQLite database

    /**
     * Creates a new SQLite database and initializes tables.
     */
    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // ✅ Create tables inside the SQLite database
            createTables(stmt);

            System.out.println("✅ Database initialized successfully!");

        } catch (SQLException e) {
            System.out.println("❌ SQLite Error: " + e.getMessage());
        }
    }

    /**
     * Creates necessary tables inside the database.
     */
    private static void createTables(Statement stmt) throws SQLException {
        stmt.execute("CREATE TABLE IF NOT EXISTS Accounts ("
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

    /**
     * Inserts a new account into the database.
     */
    public void writeAccountTodb(String resource, String username, String password, LocalDateTime date) {
        String sql = "INSERT INTO Accounts (resource, login, password, date_added) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 🔹 Set the values dynamically
            pstmt.setString(1, resource);  // resource
            pstmt.setString(2, username);  // login (username)
            pstmt.setString(3, password);  // password
            pstmt.setTimestamp(4, Timestamp.valueOf(date)); // ✅ Store as TIMESTAMP

            // ✅ Execute the insert query
            pstmt.executeUpdate();
            System.out.println("✅ Account successfully inserted: " + username + "on" + date);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }
    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT resource, login, password, date_added FROM Accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                String resource = rs.getString("resource");
                String username = rs.getString("login");
                String password = rs.getString("password");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added"));

                accounts.add(new Account(resource, username, password, LocalDateTime.now()));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error: " + e.getMessage());
        }

        return accounts;
    }
}


