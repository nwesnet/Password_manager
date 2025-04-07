package nwes.passwordmanager;

import javax.crypto.SecretKey;
import java.sql.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

            // System.out.println("✅ Database initialized successfully!");

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
                + "card_pin TEXT NOT NULL, "
                + "network_type TEXT NOT NULL, "
                + "card_type TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        stmt.execute("CREATE TABLE IF NOT EXISTS Wallets ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL, "
                + "twelve_words TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        stmt.execute("CREATE TABLE IF NOT EXISTS Links ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "resource TEXT NOT NULL, "
                + "link TEXT NOT NULL, "
                + "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
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
            // System.out.println("✅ Account successfully inserted: " + username + "on" + date);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }
    public void writeCardTodb(String resource, String cardNumber, String expiryDate, String cvv, String ownerName, String cardPin, String networkType, String cardType, LocalDateTime date){
        String sql = "INSERT INTO Cards (resource, card_number, expiry_date, cvv, owner_name, card_pin, network_type, card_type, date_added) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, cardNumber);
            pstmt.setString(3, expiryDate);
            pstmt.setString(4, cvv);
            pstmt.setString(5, ownerName);
            pstmt.setString(6, cardPin);
            pstmt.setString(7, networkType);
            pstmt.setString(8, cardType);
            pstmt.setTimestamp(9, Timestamp.valueOf(date));

            pstmt.executeUpdate();
            // System.out.println("✅ Card successfully inserted: " + resource + " on " + date);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }
    public void writeLinkTodb(String resource, String link, LocalDateTime date) {
        String sql = "INSERT INTO Links (resource, link, date_added) VALUES (?,?,?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, link);
            pstmt.setTimestamp(3, Timestamp.valueOf(date));
            pstmt.executeUpdate();
            // System.out.println("✅ Link successfully inserted: " + resource + " and " + link);

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

    public void writeWalletTodb(String resource, String words, String address, String password, LocalDateTime date) {
        String sql = "INSERT INTO Wallets (resource, twelve_words, address, password, date_added) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, words);
            pstmt.setString(3, address);
            pstmt.setString(4, password);
            pstmt.setTimestamp(5, Timestamp.valueOf(date));

            pstmt.executeUpdate();
            // System.out.println("✅ Wallet successfully inserted: " + resource + " on " + date);

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
                long timestampMillis = rs.getLong("date_added");
                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime();

                accounts.add(new Account(resource, username, password, dateAdded));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error: " + e.getMessage());
        }

        return accounts;
    }

    public List<Card> getAllCards(){
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT resource, card_number, expiry_date, cvv, owner_name, card_pin, network_type, card_type, date_added FROM Cards";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                String resource = rs.getString("resource");
                String cardNumber = rs.getString("card_number");
                String expiryDate = rs.getString("expiry_date");
                String cvv = rs.getString("cvv");
                String ownerName = rs.getString("owner_name");
                String cardPin = rs.getString("card_pin");
                String networkType = rs.getString("network_type");
                String cardType = rs.getString("card_type");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));
                long timestampMillis = rs.getLong("date_added");
                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                cards.add(new Card(resource, cardNumber, expiryDate, cvv, ownerName, cardPin, networkType, cardType, dateAdded));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Cards): " + e.getMessage());
        }
        return cards;
    }
    public List<Link> getAllLinks(){
        List<Link> links = new ArrayList<>();
        String sql = "SELECT resource, link, date_added FROM Links";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                long timestampMillis = rs.getLong("date_added");
                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                links.add(new Link(rs.getString("resource"), rs.getString("link"), dateAdded));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Links): " + e.getMessage());
        }
        return links;
    }
    public List<Wallet> getAllWallets() {
        List<Wallet> wallets = new ArrayList<>();
        String sql = "SELECT resource, twelve_words, address, password, date_added FROM Wallets";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String resource = rs.getString("resource");
                String wordsString = rs.getString("twelve_words");
                String address = rs.getString("address");
                String password = rs.getString("password");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));
                long timestampMillis = rs.getLong("date_added");
                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                String[] wordsArray = wordsString.split(",");

                wallets.add(new Wallet(resource, wordsArray, address, password, dateAdded));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Wallets): " + e.getMessage());
        }
        return wallets;
    }
    public void updateAccount(Account account, String oldResource, String oldUsername) {
        String sql = "UPDATE Accounts SET resource = ?, username = ?, password = ?, date_added = ? WHERE resource = ? AND username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getResource());
            pstmt.setString(2, account.getUsername());
            pstmt.setString(3, account.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(account.getDate()));
            pstmt.setString(5, oldResource);
            pstmt.setString(6, oldUsername);
            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println("Failed to update account: " + e.getMessage());
        }
    }
    public void updateCard(Card card, String oldResource, String oldCardNumber, String oldCardName, String oldCardDate) {
        String sql = "UPDATE Cards SET resource = ?, card_number = ?, expiry_date = ?, cvv = ?, owner_name = ?, card_pin = ?, network_type = ?, card_type = ?, date_added = ? WHERE resource = ? AND card_number = ? AND owner_name = ? AND expiry_date = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getExpiryDate());
            pstmt.setString(4, card.getCvv());
            pstmt.setString(5, card.getOnwerName());
            pstmt.setString(6, card.getCardPincode());
            pstmt.setString(7, card.getCardNetworkType());
            pstmt.setString(8, card.getCardType());
            pstmt.setTimestamp(9, Timestamp.valueOf(card.getDateAdded()));
            pstmt.setString(10, oldResource);
            pstmt.setString(11, oldCardNumber);
            pstmt.setString(12, oldCardName);
            pstmt.setString(13, oldCardDate);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update card: " + e.getMessage());
        }
    }
    public void updateLink(Link link, String oldResource, String oldLink) {
        String sql = "UPDATE Links SET resource = ?, link = ?, date_added = ? WHERE resource = ? AND link = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getResource());
            pstmt.setString(2, link.getLink());
            pstmt.setTimestamp(3, Timestamp.valueOf(link.getDate()));
            pstmt.setString(4, oldResource);
            pstmt.setString(5, oldLink);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update link: " + e.getMessage());
        }
    }
    public void updateWallet(Wallet wallet, String oldResource, String oldAddress) {
        String sql = "UPDATE Wallets SET resource = ?, twelve_words = ?, address = ?, password = ?, date_added = ? WHERE resource = ? AND address = ?";
        String wordText = String.join(",", wallet.getTwelveWords());

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.setString(2, wordText);
            pstmt.setString(3, wallet.getAddress());
            pstmt.setString(4, wallet.getPassword());
            pstmt.setTimestamp(5, Timestamp.valueOf(wallet.getDateAdded()));
            pstmt.setString(6, oldResource);
            pstmt.setString(7, oldAddress);
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
        String sql = "DELETE FROM Cards WHERE resource = ? AND card_number = ? AND owner_name = ? AND expiry_date = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getOnwerName());
            pstmt.setString(4, card.getExpiryDate());
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
        String sql = "DELETE FROM Wallets WHERE resource = ? AND address = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.setString(2, wallet.getAddress());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete wallet: " + e.getMessage());
        }

    }
    public static void reencryptDatabase(SecretKey oldKey, SecretKey newKey) {
        DatabaseManager db = new DatabaseManager();

        // Reencrypt Accounts
        List<Account> accounts = db.getAllAccounts();
        for (Account acc : accounts) {
            try {
                // Save old encrypted values before modifying
                String oldEncryptedResource = acc.getResource();
                String oldEncryptedUsername = acc.getUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(oldEncryptedResource, oldKey);
                String username = EncryptionUtils.decrypt(oldEncryptedUsername, oldKey);
                String password = EncryptionUtils.decrypt(acc.getPassword(), oldKey);
                // 🔐 Encrypt fields using the new key
                acc.setResource(EncryptionUtils.encrypt(resource, newKey));
                acc.setUsername(EncryptionUtils.encrypt(username, newKey));
                acc.setPassword(EncryptionUtils.encrypt(password, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateAccount(acc, oldEncryptedResource, oldEncryptedUsername);

            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting account: " + e.getMessage());
            }
        }
        // Reenrypt cards
        List<Card> cards = db.getAllCards();
        for(Card card : cards) {
            try {
                // Save old encrypted values before modify
                String oldResource = card.getResource();
                String oldNumber = card.getCardNumber();
                String oldName = card.getOnwerName();
                String oldDate = card.getExpiryDate();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(card.getResource(), oldKey);
                String cardNumber = EncryptionUtils.decrypt(card.getCardNumber(), oldKey);
                String cardDate = EncryptionUtils.decrypt(card.getExpiryDate(), oldKey);
                String cardCvv = EncryptionUtils.decrypt(card.getCvv(), oldKey);
                String cardOwnerName = EncryptionUtils.decrypt(card.getOnwerName(), oldKey);
                String cardPin = EncryptionUtils.decrypt(card.getCardPincode(), oldKey);
                String cardNetworkType = EncryptionUtils.decrypt(card.getCardNetworkType(), oldKey);
                String cardType = EncryptionUtils.decrypt(card.getCardType(), oldKey);
                // 🔐 Encrypt fields using the new key
                card.setResource(EncryptionUtils.encrypt(resource, newKey));
                card.setCardNumber(EncryptionUtils.encrypt(cardNumber, newKey));
                card.setExpiryDate(EncryptionUtils.encrypt(cardDate, newKey));
                card.setCvv(EncryptionUtils.encrypt(cardCvv, newKey));
                card.setOnwerName(EncryptionUtils.encrypt(cardOwnerName, newKey));
                card.setCardPincode(EncryptionUtils.encrypt(cardPin, newKey));
                card.setCardNetworkType(EncryptionUtils.encrypt(cardNetworkType, newKey));
                card.setCardType(EncryptionUtils.encrypt(cardType, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateCard(card, oldResource, oldNumber, oldName, oldDate);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting card: " + e.getMessage());
            }
        }
        // Reencrypt links
        List<Link> links = db.getAllLinks();
        for(Link link : links) {
            try {
                // Save old encrypted values before modify
                String oldResource = link.getResource();
                String oldLink = link.getLink();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(link.getResource(), oldKey);
                String linkData = EncryptionUtils.decrypt(link.getLink(), oldKey);
                // 🔐 Encrypt fields using the new key
                link.setResource(EncryptionUtils.encrypt(resource, newKey));
                link.setLink(EncryptionUtils.encrypt(linkData, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateLink(link, oldResource, oldLink);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting link: " + e.getMessage());
            }
        }
        // Reencrypt wallets
        List<Wallet> wallets = db.getAllWallets();
        for (Wallet wallet : wallets) {
            try {
                // Save old encrypted values before modify
                String oldResource = wallet.getResource();
                String oldAddress = wallet.getAddress();
                // Decrypt with old key
                String resource = EncryptionUtils.decrypt(wallet.getResource(), oldKey);
                String address = EncryptionUtils.decrypt(wallet.getAddress(), oldKey);
                String password = EncryptionUtils.decrypt(wallet.getPassword(), oldKey);
                // Decrypt each twelve word individually
                String[] decryptedWords = new String[wallet.getTwelveWords().length];
                for (int i = 0; i < wallet.getTwelveWords().length; i++) {
                    decryptedWords[i] = EncryptionUtils.decrypt(wallet.getTwelveWords()[i], oldKey);
                }
                // Re-encrypt with new key
                String newEncryptedResource = EncryptionUtils.encrypt(resource, newKey);
                String newEncryptedAddress = EncryptionUtils.encrypt(address, newKey);
                String newEncryptedPassword = EncryptionUtils.encrypt(password, newKey);
                String[] reencryptedWords = new String[decryptedWords.length];
                for (int i = 0; i < decryptedWords.length; i++) {
                    reencryptedWords[i] = EncryptionUtils.encrypt(decryptedWords[i], newKey);
                }
                // Build new Wallet and update
                Wallet newWallet = new Wallet(
                        newEncryptedResource,
                        reencryptedWords,
                        newEncryptedAddress,
                        newEncryptedPassword,
                        wallet.getDateAdded()
                );
                // ✅ Pass original encrypted identifiers to update
                db.updateWallet(newWallet, oldResource, oldAddress);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting wallet: " + e.getMessage());
            }
        }

    }
}


