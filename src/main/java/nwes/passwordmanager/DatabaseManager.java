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

    public void writeLinkTodb(String link) {
        String sql = "INSERT INTO Links (resource) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link);
            pstmt.executeUpdate();
            System.out.println("✅ Link successfully inserted: " + link);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

    public void writeCardTodb(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String systemType,LocalDateTime date){
        String sql = "INSERT INTO Cards (resource, card_number, expiry_date, cvv, owner_name, date_added) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, cardNumber);
            pstmt.setString(3, expiryDate);
            pstmt.setString(4, cvv);
            pstmt.setString(5, ownerName);
            pstmt.setTimestamp(6, Timestamp.valueOf(date));

            pstmt.executeUpdate();
            System.out.println("✅ Card successfully inserted: " + resource + " on " + date);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

    public void writeWalletTodb(String resource, String words, String password, LocalDateTime date){
        String sql = "INSERT INTO Wallets (resource, twelve_words, password, address, date_added) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, words);
            pstmt.setString(3, password);
            pstmt.setTimestamp(4, Timestamp.valueOf(date));

            pstmt.executeUpdate();
            System.out.println("✅ Wallet successfully inserted: " + resource + " on " + date);

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
    public List<Link> getAllLinks(){
        List<Link> links = new ArrayList<>();
        String sql = "SELECT resource FROM Links";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                links.add(new Link(rs.getString("resource")));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Links): " + e.getMessage());
        }
        return links;
    }
    public List<Card> getAllCards(){
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT resource, card_number, expiry_date, cvv, owner_name, date_added FROM Cards";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                String resource = rs.getString("resource");
                String cardNumber = rs.getString("card_number");
                String expiryDate = rs.getString("expiry_date");
                String cvv = rs.getString("cvv");
                String ownerName = rs.getString("owner_name");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));

                cards.add(new Card(resource, cardNumber, expiryDate, cvv, ownerName, LocalDateTime.now()));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Cards): " + e.getMessage());
        }
        return cards;
    }
    public List<Wallet> getAllWallets(){
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT resource, twelve_words, password, date_added FROM Wallets";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String resource = rs.getString("resource");
                String wordsString = rs.getString("twelve_words");
                String password = rs.getString("password");
                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));

                String[] wordsArray = wordsString.split(",");

                wallets.add(new Wallet(resource, wordsArray, password, dateAdded));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Wallets): " + e.getMessage());
        }
        return wallets;
    }
}


