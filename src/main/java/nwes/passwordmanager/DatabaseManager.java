package nwes.passwordmanager;

import javax.crypto.SecretKey;
import java.sql.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

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
                + "id TEXT PRIMARY KEY, "
                + "resource TEXT NOT NULL, "
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "owner_username TEXT NOT NULL,"
                + "date_added TIMESTAMP NOT NULL,"
                + "last_modified TIMESTAMP NOT NULL,"
                + "deleted TEXT NOT NULL,"
                + "sync TEXT NOT NULL"
                + ");"
        );
        stmt.execute("CREATE TABLE IF NOT EXISTS Cards ("
                + "id TEXT PRIMARY KEY, "
                + "resource TEXT NOT NULL, "
                + "card_number TEXT NOT NULL, "
                + "expiry_date TEXT NOT NULL, "
                + "cvv TEXT NOT NULL, "
                + "owner_name TEXT NOT NULL, "
                + "card_pin TEXT NOT NULL, "
                + "network_type TEXT NOT NULL, "
                + "card_type TEXT NOT NULL, "
                + "owner_username TEXT NOT NULL,"
                + "date_added TIMESTAMP NOT NULL,"
                + "last_modified TIMESTAMP NOT NULL,"
                + "deleted TEXT NOT NULL,"
                + "sync TEXT NOT NULL"
                + ");"
        );
        stmt.execute("CREATE TABLE IF NOT EXISTS Links ("
                + "id TEXT PRIMARY KEY, "
                + "resource TEXT NOT NULL, "
                + "link TEXT NOT NULL, "
                + "owner_username TEXT NOT NULL,"
                + "date_added TIMESTAMP NOT NULL,"
                + "last_modified TIMESTAMP NOT NULL,"
                + "deleted TEXT NOT NULL,"
                + "sync TEXT NOT NULL"
                + ");"
        );
        stmt.execute("CREATE TABLE IF NOT EXISTS Wallets ("
                + "id TEXT PRIMARY KEY, "
                + "resource TEXT NOT NULL, "
                + "twelve_words TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "owner_username TEXT NOT NULL,"
                + "date_added TIMESTAMP NOT NULL,"
                + "last_modified TIMESTAMP NOT NULL,"
                + "deleted TEXT NOT NULL,"
                + "sync TEXT NOT NULL"
                + ");"
        );
    }

    /**
     * Inserts a new account into the database.
     */
    public void writeAccountTodb(
            String id,
            String resource, String username, String password,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ) {
        String sql = "INSERT INTO Accounts (id, resource, username, password, owner_username, date_added, last_modified, deleted, sync) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 🔹 Set the values dynamically
            pstmt.setString(1, id); // IdForItem
            pstmt.setString(2, resource);  // resource
            pstmt.setString(3, username);  // login (username)
            pstmt.setString(4, password);  // password
            pstmt.setString(5, ownerUsername); // ownerUsername
            pstmt.setTimestamp(6, Timestamp.valueOf(dateAdded)); // ✅ Date added as TIMESTAMP
            pstmt.setTimestamp(7, Timestamp.valueOf(lastModified)); // Last modified as TIMESTAMP
            pstmt.setString(8, deleted); // deleted status
            pstmt.setString(9, sync); // sync status

            // ✅ Execute the insert query
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }
    public void writeCardTodb(
            String id,
            String resource, String cardNumber, String expiryDate, String cvv, String ownerName,
            String cardPin,
            String networkType, String cardType,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ){
        String sql = "INSERT INTO Cards (id, resource, card_number, expiry_date, cvv, owner_name, card_pin, network_type, card_type, owner_username, date_added, last_modified, deleted, sync) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set the values
            pstmt.setString(1, id);
            pstmt.setString(2, resource);
            pstmt.setString(3, cardNumber);
            pstmt.setString(4, expiryDate);
            pstmt.setString(5, cvv);
            pstmt.setString(6, ownerName);
            pstmt.setString(7, cardPin);
            pstmt.setString(8, networkType);
            pstmt.setString(9, cardType);
            pstmt.setString(10, ownerUsername);
            pstmt.setTimestamp(11, Timestamp.valueOf(dateAdded));
            pstmt.setTimestamp(12, Timestamp.valueOf(lastModified));
            pstmt.setString(13, deleted);
            pstmt.setString(14, sync);

            // ✅ Execute the insert query
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }
    public void writeLinkTodb(
            String id,
            String resource, String link,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ) {
        String sql = "INSERT INTO Links (id, resource, link, owner_username, date_added, last_modified, deleted, sync) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, resource);
            pstmt.setString(3, link);
            pstmt.setString(4, ownerUsername);
            pstmt.setTimestamp(5, Timestamp.valueOf(dateAdded));
            pstmt.setTimestamp(6, Timestamp.valueOf(lastModified));
            pstmt.setString(7, deleted);
            pstmt.setString(8, sync);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

    public void writeWalletTodb(
            String id,
            String resource, String words, String address, String password,
            String ownerUsername,
            LocalDateTime dateAdded, LocalDateTime lastModified,
            String deleted, String sync
    ) {
        String sql = "INSERT INTO Wallets (id, resource, twelve_words, address, password, owner_username, date_added, last_modified, deleted, sync) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.setString(2, resource);
            pstmt.setString(3, words);
            pstmt.setString(4, address);
            pstmt.setString(5, password);
            pstmt.setString(6, ownerUsername);
            pstmt.setTimestamp(7, Timestamp.valueOf(dateAdded));
            pstmt.setTimestamp(8, Timestamp.valueOf(lastModified));
            pstmt.setString(9, deleted);
            pstmt.setString(10, sync);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Database Write Error: " + e.getMessage());
        }
    }

    public Set<Account> getAllAccounts(){
        Set<Account> accounts = new HashSet<>();
        String sql = "SELECT id, resource, username, password, owner_username, date_added, last_modified, deleted, sync FROM Accounts";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while (rs.next()){
                String id = rs.getString("id");
                String resource = rs.getString("resource");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String ownerUsername = rs.getString("owner_username");
//                long timestampMillis = rs.getLong("date_added");
//                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
//                                                .atZone(ZoneId.systemDefault())
//                                                .toLocalDateTime();
                LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                String deleted = rs.getString("deleted");
                String sync = rs.getString("sync");

                accounts.add(new Account(
                        id, resource, username, password, ownerUsername, dateAdded, lastModified, deleted, sync
                ));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error: " + e.getMessage());
        }

        return accounts;
    }

    public Set<Card> getAllCards(){
        Set<Card> cards = new HashSet<>();
        String sql = "SELECT id, resource, card_number, expiry_date, cvv, owner_name, card_pin, network_type, card_type, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Cards";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                String id = rs.getString("id");
                String resource = rs.getString("resource");
                String cardNumber = rs.getString("card_number");
                String expiryDate = rs.getString("expiry_date");
                String cvv = rs.getString("cvv");
                String ownerName = rs.getString("owner_name");
                String cardPin = rs.getString("card_pin");
                String networkType = rs.getString("network_type");
                String cardType = rs.getString("card_type");
                String ownerUsername = rs.getString("owner_username");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));
//                long timestampMillis = rs.getLong("date_added");
//                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();
                LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                String deleted = rs.getString("deleted");
                String sync = rs.getString("sync");

                cards.add(new Card(
                        id, resource, cardNumber, expiryDate, cvv, ownerName, cardPin, networkType, cardType, ownerUsername,
                        dateAdded, lastModified, deleted, sync
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Cards): " + e.getMessage());
        }
        return cards;
    }
    public Set<Link> getAllLinks(){
        Set<Link> links = new HashSet<>();
        String sql = "SELECT id, resource, link, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Links";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                String id = rs.getString("id");
                String resource = rs.getString("resource");
                String link = rs.getString("link");
                String ownerUsername = rs.getString("owner_username");
                LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                String deleted = rs.getString("deleted");
                String sync = rs.getString("sync");

                links.add(new Link(
                        id, resource, link, ownerUsername, dateAdded, lastModified, deleted, sync
                ));
//                long timestampMillis = rs.getLong("date_added");
//                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();
//                links.add(new Link(rs.getString("resource"), rs.getString("link"), dateAdded));
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Links): " + e.getMessage());
        }
        return links;
    }
    public Set<Wallet> getAllWallets() {
        Set<Wallet> wallets = new HashSet<>();
        String sql = "SELECT id, resource, twelve_words, address, password, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Wallets";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String resource = rs.getString("resource");
                String wordsString = rs.getString("twelve_words");
                String address = rs.getString("address");
                String password = rs.getString("password");
                String ownerUsername = rs.getString("owner_username");
                LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                String deleted = rs.getString("deleted");
                String sync = rs.getString("sync");
//                LocalDateTime dateAdded = LocalDateTime.parse(rs.getString("date_added").replace(" ", "T"));
//                long timestampMillis = rs.getLong("date_added");
//                LocalDateTime dateAdded = Instant.ofEpochMilli(timestampMillis)
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();
                String[] wordsArray = wordsString.split(",");

                wallets.add(new Wallet(
                        id, resource, wordsArray, address, password, ownerUsername, dateAdded, lastModified, deleted, sync
                ));
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Wallets): " + e.getMessage());
        }
        return wallets;
    }
    public void updateAccount(Account account, String ownerUsername) {
        String sql = "UPDATE Accounts SET resource = ?, username = ?, password = ?, last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getResource());
            pstmt.setString(2, account.getUsername());
            pstmt.setString(3, account.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(account.getLastModified()));

            pstmt.setString(5, account.getId());
            pstmt.setString(6, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println("Failed to update account: " + e.getMessage());
        }
    }
    public void updateCard(Card card, String ownerUsername) {
        String sql = "UPDATE Cards SET resource = ?, card_number = ?, expiry_date = ?, cvv = ?, owner_name = ?, card_pin = ?, network_type = ?, card_type = ?, last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getExpiryDate());
            pstmt.setString(4, card.getCvv());
            pstmt.setString(5, card.getOwnerName());
            pstmt.setString(6, card.getCardPincode());
            pstmt.setString(7, card.getCardNetworkType());
            pstmt.setString(8, card.getCardType());
            pstmt.setTimestamp(9, Timestamp.valueOf(card.getLastModified()));

            pstmt.setString(10, card.getId());
            pstmt.setString(11, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update card: " + e.getMessage());
        }
    }
    public void updateLink(Link link, String ownerUsername) {
        String sql = "UPDATE Links SET resource = ?, link = ?, last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getResource());
            pstmt.setString(2, link.getLink());
            pstmt.setTimestamp(3, Timestamp.valueOf(link.getLastModified()));

            pstmt.setString(4, link.getId());
            pstmt.setString(5, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update link: " + e.getMessage());
        }
    }
    public void updateWallet(Wallet wallet, String ownerUsername) {
        String sql = "UPDATE Wallets SET resource = ?, twelve_words = ?, address = ?, password = ?, last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";
        String wordText = String.join(",", wallet.getTwelveWords());

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.setString(2, wordText);
            pstmt.setString(3, wallet.getAddress());
            pstmt.setString(4, wallet.getPassword());
            pstmt.setTimestamp(5, Timestamp.valueOf(wallet.getLastModified()));

            pstmt.setString(6, wallet.getId());
            pstmt.setString(7, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update wallet: " + e.getMessage());
        }
    }
    public void deleteAccount(Account account) {
        String sql = "DELETE FROM Accounts WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getId());
            pstmt.setString(2, account.getOwnerUsername());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete account: " + e.getMessage());
        }
    }
    public void deleteCard(Card card) {
        String sql = "DELETE FROM Cards WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getId());
            pstmt.setString(2, card.getOwnerUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete card: " + e.getMessage());
        }
    }
    public void deleteLink(Link link) {
        String sql = "DELETE FROM Links WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getId());
            pstmt.setString(2, link.getOwnerUsername());
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
        Set<Account> accounts = db.getAllAccounts();
        for (Account acc : accounts) {
            try {
                String oldOwnerUsername = acc.getOwnerUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(acc.getResource(), oldKey);
                String username = EncryptionUtils.decrypt(acc.getUsername(), oldKey);
                String password = EncryptionUtils.decrypt(acc.getPassword(), oldKey);
                String ownerUsername = EncryptionUtils.decrypt(acc.getOwnerUsername(), oldKey);
                // Add it for deleted and sync columns later ( Decrypt & Encrypt )
                // 🔐 Encrypt fields using the new key
                acc.setResource(EncryptionUtils.encrypt(resource, newKey));
                acc.setUsername(EncryptionUtils.encrypt(username, newKey));
                acc.setPassword(EncryptionUtils.encrypt(password, newKey));
                acc.setOwnerUsername(EncryptionUtils.encrypt(ownerUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateAccount(acc, oldOwnerUsername);

            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting account: " + e.getMessage());
            }
        }
        // Reencrypt cards
        Set<Card> cards = db.getAllCards();
        for(Card card : cards) {
            try {
                // Save old encrypted values before modify
                String oldOwnerUsername = card.getOwnerUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(card.getResource(), oldKey);
                String cardNumber = EncryptionUtils.decrypt(card.getCardNumber(), oldKey);
                String cardDate = EncryptionUtils.decrypt(card.getExpiryDate(), oldKey);
                String cardCvv = EncryptionUtils.decrypt(card.getCvv(), oldKey);
                String cardOwnerName = EncryptionUtils.decrypt(card.getOwnerName(), oldKey);
                String cardPin = EncryptionUtils.decrypt(card.getCardPincode(), oldKey);
                String cardNetworkType = EncryptionUtils.decrypt(card.getCardNetworkType(), oldKey);
                String cardType = EncryptionUtils.decrypt(card.getCardType(), oldKey);
                String ownerUsername = EncryptionUtils.decrypt(card.getOwnerUsername(), oldKey);
                // 🔐 Encrypt fields using the new key
                card.setResource(EncryptionUtils.encrypt(resource, newKey));
                card.setCardNumber(EncryptionUtils.encrypt(cardNumber, newKey));
                card.setExpiryDate(EncryptionUtils.encrypt(cardDate, newKey));
                card.setCvv(EncryptionUtils.encrypt(cardCvv, newKey));
                card.setOwnerName(EncryptionUtils.encrypt(cardOwnerName, newKey));
                card.setCardPincode(EncryptionUtils.encrypt(cardPin, newKey));
                card.setCardNetworkType(EncryptionUtils.encrypt(cardNetworkType, newKey));
                card.setCardType(EncryptionUtils.encrypt(cardType, newKey));
                card.setOwnerUsername(EncryptionUtils.encrypt(ownerUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateCard(card, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting card: " + e.getMessage());
            }
        }
        // Reencrypt links
        Set<Link> links = db.getAllLinks();
        for(Link link : links) {
            try {
                // Save old encrypted values before modify
                String oldOwnerUsername = link.getOwnerUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(link.getResource(), oldKey);
                String linkData = EncryptionUtils.decrypt(link.getLink(), oldKey);
                String ownerUsername = EncryptionUtils.decrypt(link.getOwnerUsername(), oldKey);
                // 🔐 Encrypt fields using the new key
                link.setResource(EncryptionUtils.encrypt(resource, newKey));
                link.setLink(EncryptionUtils.encrypt(linkData, newKey));
                link.setOwnerUsername(EncryptionUtils.decrypt(ownerUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateLink(link, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting link: " + e.getMessage());
            }
        }
        // Reencrypt wallets
        Set<Wallet> wallets = db.getAllWallets();
        for (Wallet wallet : wallets) {
            try {
                // Save old encrypted values before modify
                String oldOwnerUsername = wallet.getOwnerUsername();
                // Decrypt with old key
                String resource = EncryptionUtils.decrypt(wallet.getResource(), oldKey);
                String address = EncryptionUtils.decrypt(wallet.getAddress(), oldKey);
                String password = EncryptionUtils.decrypt(wallet.getPassword(), oldKey);
                String ownerUsername = EncryptionUtils.decrypt(wallet.getOwnerUsername(), oldKey);
                // Decrypt each twelve word individually
                String[] decryptedWords = new String[wallet.getTwelveWords().length];
                for (int i = 0; i < wallet.getTwelveWords().length; i++) {
                    decryptedWords[i] = EncryptionUtils.decrypt(wallet.getTwelveWords()[i], oldKey);
                }
                // Re-encrypt with new key
                String newEncryptedResource = EncryptionUtils.encrypt(resource, newKey);
                String newEncryptedAddress = EncryptionUtils.encrypt(address, newKey);
                String newEncryptedPassword = EncryptionUtils.encrypt(password, newKey);
                String newOwnerUsername = EncryptionUtils.encrypt(ownerUsername, newKey);
                String[] reencryptedWords = new String[decryptedWords.length];
                for (int i = 0; i < decryptedWords.length; i++) {
                    reencryptedWords[i] = EncryptionUtils.encrypt(decryptedWords[i], newKey);
                }
                // Build new Wallet and update
                Wallet newWallet = new Wallet(
                        wallet.getId(),
                        newEncryptedResource,
                        reencryptedWords,
                        newEncryptedAddress,
                        newEncryptedPassword,
                        wallet.getOwnerUsername(),
                        wallet.getDateAdded(),
                        wallet.getLastModified(),
                        wallet.getDeleted(),
                        wallet.getSync()
                );
                // ✅ Pass original encrypted identifiers to update
                db.updateWallet(newWallet, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting wallet: " + e.getMessage());
            }
        }

    }
    public void mergeServerAccounts(Set<Account> serverAccounts) {
        Set<Account> localAccounts = new HashSet<>(getAllAccounts());
        for (Account account : serverAccounts) {
            if (!localAccounts.contains(account)) {
                writeAccountTodb(
                        account.getId(),
                        account.getResource(), account.getUsername(), account.getPassword(),
                        account.getOwnerUsername(),
                        account.getDateAdded(), account.getLastModified(),
                        account.isDeleted(), account.isSync()
                );
            }
        }
    }
    public static void sofrDelete(Object object, String name) {
        String sql = "UPDATE " + name + " SET deleted = 'true', last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
//            pstmt.setString(2, object.getId());

        } catch (SQLException e) {
            System.out.println("Failed to soft-delete item: " + e.getMessage());
        }

    }

}


