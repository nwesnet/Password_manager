package nwes.passwordmanager;

import javafx.scene.control.Dialog;

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
                + "username TEXT NOT NULL, "
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
                + "resource TEXT NOT NULL, "
                + "link TEXT NOT NULL);");
    }

    /**
     * Inserts a new account into the database.
     */
    public void writeAccountTodb(String resource, String username, String password, LocalDateTime date) {
        String sql = "INSERT INTO Accounts (resource, username, password, date_added) VALUES (?, ?, ?, ?)";

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

    public void writeLinkTodb(String link, String url) {
        String sql = "INSERT INTO Links (resource, link) VALUES (?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link);
            pstmt.setString(2, url);
            pstmt.executeUpdate();
            System.out.println("✅ Link successfully inserted: " + link + " and " + url);

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
        String sql = "SELECT resource, username, password, date_added FROM Accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                String resource = rs.getString("resource");
                String username = rs.getString("username");
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
        String sql = "SELECT resource, link FROM Links";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                links.add(new Link(rs.getString("resource"), rs.getString("link")));
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
    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT resource, twelve_words, password, date_added FROM Wallets";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String resource = rs.getString("resource");
                String wordsString = rs.getString("twelve_words");
                String password = rs.getString("password");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));

                String[] wordsArray = wordsString.split(",");

                wallets.add(new Wallet(resource, wordsArray, password, LocalDateTime.now()));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Wallets): " + e.getMessage());
        }
        return wallets;
    }
    public void updateAccount(Account account, String oldResource, String oldUsername) {
        String sql = "UPDATE Accounts SET resource = ?, username = ?, password = ? WHERE resource = ? AND username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getResource());
            pstmt.setString(2, account.getUsername());
            pstmt.setString(3, account.getPassword());
            pstmt.setString(4, oldResource);
            pstmt.setString(5, oldUsername);
            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println("Failed to update account: " + e.getMessage());
        }
    }
    public void updateCard(Card card, String oldResource, String oldCardNumber) {
        String sql = "UPDATE Cards SET resource = ?, card_number = ?, expiry_date = ?, cvv = ?, owner_name = ? WHERE resource = ? AND card_number = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getExpiryDate());
            pstmt.setString(4, card.getCvv());
            pstmt.setString(5, card.getOnwerName());
            pstmt.setString(6, oldResource);
            pstmt.setString(7, oldCardNumber);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update card: " + e.getMessage());
        }
    }
    public void updateLink(Link link, String oldResource, String oldLink) {
        String sql = "UPDATE Links SET resource = ?, link = ? WHERE resource = ? AND link = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getResource());
            pstmt.setString(2, link.getLink());
            pstmt.setString(3, oldResource);
            pstmt.setString(4, oldLink);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update link: " + e.getMessage());
        }
    }
    public void updateWallet(Wallet wallet, String oldResource) {
        String sql = "UPDATE Wallets SET resource = ?, twelve_words = ?, password = ? WHERE resource = ?";
        String wordText = String.join(",", wallet.getTwelveWords());

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.setString(2, wordText);
            pstmt.setString(3, wallet.getPassword());
            pstmt.setString(4, oldResource);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update wallet: " + e.getMessage());
        }
    }
    public void deleteAccount(Account account) {
        String sql = "DELETE FROM Accounts WHERE resource = ? AND username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getResource());
            pstmt.setString(2, account.getUsername());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete account: " + e.getMessage());
        }
    }
    public void deleteCard(Card card) {
        String sql = "DELETE FROM Cards WHERE resource = ? AND card_number = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete card: " + e.getMessage());
        }
    }
    public void deleteLink(Link link) {
        String sql = "DELETE FROM Links WHERE resource = ? AND link = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getResource());
            pstmt.setString(2, link.getLink());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete link: " + e.getMessage());
        }
    }
    public void deleteWallet(Wallet wallet) {
        String sql = "DELETE FROM Wallets WHERE resource = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete wallet: " + e.getMessage());
        }

    }

}


