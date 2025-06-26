package nwes.passwordmanager;

import javax.crypto.SecretKey;
import java.sql.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
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
                + "key_words TEXT NOT NULL, "
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
        String sql = "INSERT INTO Wallets (id, resource, key_words, address, password, owner_username, date_added, last_modified, deleted, sync) " +
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

    public Set<Account> getAllAccounts(boolean excludeDeleted, boolean onlySync, String currentUsername){
        Set<Account> accounts = new HashSet<>();
        StringBuilder sql = new StringBuilder("SELECT id, resource, username, password, owner_username, date_added, last_modified, deleted, sync FROM Accounts WHERE owner_username = ?");

        if (excludeDeleted) sql.append(" AND deleted = 'false'");
        if (onlySync) sql.append(" AND sync = 'true'");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setString(1, currentUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String resource = rs.getString("resource");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String ownerUsername = rs.getString("owner_username");
                    LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                    LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                    String deleted = rs.getString("deleted");
                    String sync = rs.getString("sync");

                    accounts.add(new Account(
                            id, resource, username, password, ownerUsername, dateAdded, lastModified, deleted, sync
                    ));
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Database Read Error: " + e.getMessage());
        }

        return accounts;
    }

    public Set<Card> getAllCards(boolean excludeDeleted, boolean onlySync, String currentUsername){
        Set<Card> cards = new HashSet<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, resource, card_number, expiry_date, cvv, owner_name, card_pin, network_type, card_type, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Cards WHERE owner_username = ?"
        );

        if (excludeDeleted) sql.append(" AND deleted = 'false'");
        if (onlySync) sql.append(" AND sync = 'true'");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, currentUsername);

             try (ResultSet rs = pstmt.executeQuery()) {
                 while(rs.next()) {
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
                    LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                    LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                    String deleted = rs.getString("deleted");
                    String sync = rs.getString("sync");

                    cards.add(new Card(
                            id, resource, cardNumber, expiryDate, cvv, ownerName, cardPin, networkType, cardType, ownerUsername,
                            dateAdded, lastModified, deleted, sync
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Cards): " + e.getMessage());
        }
        return cards;
    }
    public Set<Link> getAllLinks(boolean excludeDeleted, boolean onlySync, String currentUsername){
        Set<Link> links = new HashSet<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, resource, link, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Links WHERE owner_username = ?"
        );

        if (excludeDeleted) sql.append(" AND deleted = 'false'");
        if (onlySync) sql.append(" AND sync = 'true'");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, currentUsername);

             try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
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
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Links): " + e.getMessage());
        }
        return links;
    }
    public Set<Wallet> getAllWallets(boolean excludeDeleted, boolean onlySync, String currentUsername) {
        Set<Wallet> wallets = new HashSet<>();
        StringBuilder sql = new StringBuilder(
                "SELECT id, resource, key_words, address, password, owner_username, date_added, last_modified, deleted, sync " +
                "FROM Wallets WHERE owner_username = ?"
        );

        if (excludeDeleted) sql.append(" AND deleted = 'false'");
        if (onlySync) sql.append(" AND sync = 'true'");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            pstmt.setString(1, currentUsername);

             try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String resource = rs.getString("resource");
                    String keyWords = rs.getString("key_words");
                    String address = rs.getString("address");
                    String password = rs.getString("password");
                    String ownerUsername = rs.getString("owner_username");
                    LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
                    LocalDateTime lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
                    String deleted = rs.getString("deleted");
                    String sync = rs.getString("sync");

                    wallets.add(new Wallet(
                            id, resource, keyWords, address, password, ownerUsername, dateAdded, lastModified, deleted, sync
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Database Read Error (Wallets): " + e.getMessage());
        }
        return wallets;
    }
    public void updateAccount(Account account, String ownerUsername) {
        String sql = "UPDATE Accounts SET resource = ?, username = ?, password = ?, owner_username = ?, last_modified = ?, deleted = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getResource());
            pstmt.setString(2, account.getUsername());
            pstmt.setString(3, account.getPassword());
            pstmt.setString(4, account.getOwnerUsername());
            pstmt.setTimestamp(5, Timestamp.valueOf(account.getLastModified()));
            pstmt.setString(6, account.getDeleted());

            pstmt.setString(7, account.getId());
            pstmt.setString(8, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println("Failed to update account: " + e.getMessage());
        }
    }
    public void updateCard(Card card, String ownerUsername) {
        String sql = "UPDATE Cards SET resource = ?, card_number = ?, expiry_date = ?, cvv = ?, owner_name = ?, card_pin = ?, network_type = ?, card_type = ?, owner_username = ?, last_modified = ?, deleted = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getResource());
            pstmt.setString(2, card.getCardNumber());
            pstmt.setString(3, card.getExpiryDate());
            pstmt.setString(4, card.getCvv());
            pstmt.setString(5, card.getOwnerName());
            pstmt.setString(6, card.getCardPin());
            pstmt.setString(7, card.getCardNetwork());
            pstmt.setString(8, card.getCardType());
            pstmt.setString(9, card.getOwnerUsername());
            pstmt.setTimestamp(10, Timestamp.valueOf(card.getLastModified()));
            pstmt.setString(11, card.getDeleted());

            pstmt.setString(12, card.getId());
            pstmt.setString(13, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update card: " + e.getMessage());
        }
    }
    public void updateLink(Link link, String ownerUsername) {
        String sql = "UPDATE Links SET resource = ?, link = ?, owner_username = ?, last_modified = ?, deleted = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, link.getResource());
            pstmt.setString(2, link.getLink());
            pstmt.setString(3, link.getOwnerUsername());
            pstmt.setTimestamp(4, Timestamp.valueOf(link.getLastModified()));
            pstmt.setString(5, link.getDeleted());

            pstmt.setString(6, link.getId());
            pstmt.setString(7, ownerUsername);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to update link: " + e.getMessage());
        }
    }
    public void updateWallet(Wallet wallet, String ownerUsername) {
        String sql = "UPDATE Wallets SET resource = ?, key_words = ?, address = ?, password = ?, owner_username = ?, last_modified = ?, deleted = ? " +
                "WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getResource());
            pstmt.setString(2, wallet.getKeyWords());
            pstmt.setString(3, wallet.getAddress());
            pstmt.setString(4, wallet.getPassword());
            pstmt.setString(5, wallet.getOwnerUsername());
            pstmt.setTimestamp(6, Timestamp.valueOf(wallet.getLastModified()));
            pstmt.setString(7, wallet.getDeleted());

            pstmt.setString(8, wallet.getId());
            pstmt.setString(9, ownerUsername);

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
        String sql = "DELETE FROM Wallets WHERE id = ? AND owner_username = ?";

        try(Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, wallet.getId());
            pstmt.setString(2, wallet.getOwnerUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Failed to delete wallet: " + e.getMessage());
        }

    }
    public boolean accountExists(String resource, String username, String password, String ownerUsername) {
        String sql = "SELECT COUNT(*) FROM Accounts " +
                "WHERE resource = ? AND username = ? AND password = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, ownerUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }
        return false;
    }
    public boolean accountExistsExceptId(String resource, String username, String password, String ownerUsername, String excludeId) {
        String sql = "SELECT COUNT(*) FROM Accounts " +
                "WHERE resource = ? AND username = ? AND password = ? AND owner_username = ? AND id <> ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, ownerUsername);
            pstmt.setString(5, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }
        return false;
    }
    public boolean cardExists(
            String resource, String cardNumber, String expiryDate, String ownerName, String cvv, String ownerUsername
    ) {
        String sql = "SELECT COUNT(*) FROM Cards " +
                "WHERE resource = ? AND card_number = ? AND expiry_date = ? AND owner_name = ? AND cvv = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, cardNumber);
            pstmt.setString(3, expiryDate);
            pstmt.setString(4, ownerName);
            pstmt.setString(5, cvv);
            pstmt.setString(6, ownerUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database check error (Cards): " + e.getMessage());
        }
        return false;
    }
    public boolean cardExistsExceptId(
            String resource, String cardNumber, String expiryDate, String ownerName, String cvv,
            String ownerUsername, String excludeId
    ) {
        String sql = "SELECT COUNT(*) FROM Cards " +
                "WHERE resource = ? AND card_number = ? AND expiry_date = ? AND owner_name = ? AND cvv = ? AND owner_username = ? AND id <> ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, cardNumber);
            pstmt.setString(3, expiryDate);
            pstmt.setString(4, ownerName);
            pstmt.setString(5, cvv);
            pstmt.setString(6, ownerUsername);
            pstmt.setString(7, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database check error (Cards): " + e.getMessage());
        }
        return false;
    }

    public boolean linkExists(String resource, String link, String ownerUsername) {
        String sql = "SELECT COUNT(*) FROM Links " +
                "WHERE resource = ? AND link = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resource);
            pstmt.setString(2, link);
            pstmt.setString(3, ownerUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }

        return false;
    }
    public boolean linkExistsExceptId(String resource, String link, String ownerUsername,String excludeId) {
        String sql = "SELECT COUNT(*) FROM Links" +
                "WHERE resource = ? AND link = ? AND owner_username = ? AND id <> ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resource);
            pstmt.setString(2, link);
            pstmt.setString(3, ownerUsername);
            pstmt.setString(4, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }
        return false;
    }
    public boolean walletExists(String resource, String keyWords, String address, String password, String ownerUsername) {
        String sql = "SELECT COUNT(*) FROM Wallets " +
                "WHERE resource = ? AND key_words = ? AND address =? AND password = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, keyWords);
            pstmt.setString(3, address);
            pstmt.setString(4, password);
            pstmt.setString(5, ownerUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }
        return false;
    }
    public boolean walletExistsExceptId(String resource, String keyWords, String address, String password, String ownerUsername, String excludeId) {
        String sql = "SELECT COUNT(*) FROM Wallets " +
                "WHERE resource = ? AND key_words = ? AND address =? AND password = ? AND owner_username = ? AND id <> ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource);
            pstmt.setString(2, keyWords);
            pstmt.setString(3, address);
            pstmt.setString(4, password);
            pstmt.setString(5, ownerUsername);
            pstmt.setString(6, excludeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Database check error: " + e.getMessage());
        }
        return false;
    }
    public static void reencryptDatabase(SecretKey oldKey, SecretKey newKey, String newUsername) {
        DatabaseManager db = new DatabaseManager();

        // Reencrypt Accounts
        Set<Account> accounts = db.getAllAccounts(false, false, PreferencesManager.getUsernameEncrypted());
        for (Account acc : accounts) {
            try {
                String oldOwnerUsername = acc.getOwnerUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(acc.getResource(), oldKey);
                String username = EncryptionUtils.decrypt(acc.getUsername(), oldKey);
                String password = EncryptionUtils.decrypt(acc.getPassword(), oldKey);
//                String ownerUsername = EncryptionUtils.decrypt(acc.getOwnerUsername(), oldKey);
                // Add it for deleted and sync columns later ( Decrypt & Encrypt )
                // 🔐 Encrypt fields using the new key
                acc.setResource(EncryptionUtils.encrypt(resource, newKey));
                acc.setUsername(EncryptionUtils.encrypt(username, newKey));
                acc.setPassword(EncryptionUtils.encrypt(password, newKey));
                acc.setOwnerUsername(EncryptionUtils.encrypt(newUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateAccount(acc, oldOwnerUsername);

            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting account: " + e.getMessage());
            }
        }
        // Reencrypt cards
        Set<Card> cards = db.getAllCards(false, false, PreferencesManager.getUsernameEncrypted());
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
                String cardPin = EncryptionUtils.decrypt(card.getCardPin(), oldKey);
                String cardNetworkType = EncryptionUtils.decrypt(card.getCardNetwork(), oldKey);
                String cardType = EncryptionUtils.decrypt(card.getCardType(), oldKey);

                // 🔐 Encrypt fields using the new key
                card.setResource(EncryptionUtils.encrypt(resource, newKey));
                card.setCardNumber(EncryptionUtils.encrypt(cardNumber, newKey));
                card.setExpiryDate(EncryptionUtils.encrypt(cardDate, newKey));
                card.setCvv(EncryptionUtils.encrypt(cardCvv, newKey));
                card.setOwnerName(EncryptionUtils.encrypt(cardOwnerName, newKey));
                card.setCardPin(EncryptionUtils.encrypt(cardPin, newKey));
                card.setCardNetwork(EncryptionUtils.encrypt(cardNetworkType, newKey));
                card.setCardType(EncryptionUtils.encrypt(cardType, newKey));
                card.setOwnerUsername(EncryptionUtils.encrypt(newUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateCard(card, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting card: " + e.getMessage());
            }
        }
        // Reencrypt links
        Set<Link> links = db.getAllLinks(false, false, PreferencesManager.getUsernameEncrypted());
        for(Link link : links) {
            try {
                // Save old encrypted values before modify
                String oldOwnerUsername = link.getOwnerUsername();
                // 🔓 Decrypt fields using old key
                String resource = EncryptionUtils.decrypt(link.getResource(), oldKey);
                String linkData = EncryptionUtils.decrypt(link.getLink(), oldKey);

                // 🔐 Encrypt fields using the new key
                link.setResource(EncryptionUtils.encrypt(resource, newKey));
                link.setLink(EncryptionUtils.encrypt(linkData, newKey));
                link.setOwnerUsername(EncryptionUtils.encrypt(newUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateLink(link, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting link: " + e.getMessage());
            }
        }
        // Reencrypt wallets
        Set<Wallet> wallets = db.getAllWallets(false, false, PreferencesManager.getUsernameEncrypted());
        for (Wallet wallet : wallets) {
            try {
                // Save old encrypted values before modify
                String oldOwnerUsername = wallet.getOwnerUsername();
                // Decrypt with old key
                String resource = EncryptionUtils.decrypt(wallet.getResource(), oldKey);
                String keyWords = EncryptionUtils.decrypt(wallet.getKeyWords(), oldKey);
                String address = EncryptionUtils.decrypt(wallet.getAddress(), oldKey);
                String password = EncryptionUtils.decrypt(wallet.getPassword(), oldKey);
                // Re-encrypt with new key
                wallet.setResource(EncryptionUtils.encrypt(resource, newKey));
                wallet.setKeyWords(EncryptionUtils.encrypt(keyWords, newKey));
                wallet.setAddress(EncryptionUtils.encrypt(address, newKey));
                wallet.setPassword(EncryptionUtils.encrypt(password, newKey));
                wallet.setOwnerUsername(EncryptionUtils.encrypt(newUsername, newKey));
                // ✅ Pass original encrypted identifiers to update
                db.updateWallet(wallet, oldOwnerUsername);
            } catch (Exception e) {
                System.out.println("❌ Error re-encrypting wallet: " + e.getMessage());
            }
        }

    }
    public void mergeServerAccounts(Set<Account> serverAccounts) {
        Set<Account> localAccounts = new HashSet<>(getAllAccounts(false, true, PreferencesManager.getUsernameEncrypted()));

        for (Account serverAcc : serverAccounts) {
            Optional<Account> localOpt = localAccounts.stream()
                    .filter(acc -> acc.getId().equals(serverAcc.getId()))
                    .findFirst();
            if (Boolean.parseBoolean(serverAcc.getDeleted())) {
                if (localOpt.isPresent()) {
//                    System.out.println("Hard deleting account " + serverAcc.getId() + " (was deleted on server)");
                    deleteAccount(serverAcc);
                }
                continue;
            }
            if (localOpt.isPresent()) {
                Account localAcc = localOpt.get();

                if (serverAcc.getLastModified().isAfter(localAcc.getLastModified())) {
//                    System.out.println("Updating local account " + serverAcc.getId() + " from server (server newer)");
                    updateAccount(serverAcc, serverAcc.getOwnerUsername());
                } else {
//                    System.out.println("Keeping local account " + localAcc.getId() + " (local newer)");
                }
            } else {
//                System.out.println("Inserting new account " + serverAcc.getId() + " from server");
                writeAccountTodb(
                        serverAcc.getId(),
                        serverAcc.getResource(), serverAcc.getUsername(), serverAcc.getPassword(),
                        serverAcc.getOwnerUsername(),
                        serverAcc.getDateAdded(), serverAcc.getLastModified(),
                        serverAcc.getDeleted(), serverAcc.getSync()
                );
            }
        }
    }
    public void mergeServerCards(Set<Card> serverCards) {
        Set<Card> localCards = new HashSet<>(getAllCards(false, true, PreferencesManager.getUsernameEncrypted()));
        for (Card serverCard : serverCards) {
            Optional<Card> localOpt = localCards.stream()
                    .filter(c -> c.getId().equals(serverCard.getId()))
                    .findFirst();

            if (Boolean.parseBoolean(serverCard.getDeleted())) {
                if (localOpt.isPresent()) {
//                    System.out.println("Hard deleting card " + serverCard.getId() + " (was deleted on server)");
                    deleteCard(serverCard);
                }
                continue;
            }

            if (localOpt.isPresent()) {
                Card localCard = localOpt.get();
                if (serverCard.getLastModified().isAfter(localCard.getLastModified())) {
//                    System.out.println("Updating local card " + serverCard.getId() + " from server (server newer)");
                    updateCard(serverCard, serverCard.getOwnerUsername());
                } else {
//                    System.out.println("Keeping local card " + localCard.getId() + " (local newer)");
                }
            } else {
//                System.out.println("Inserting new card " + serverCard.getId() + " from server");
                writeCardTodb(
                        serverCard.getId(),
                        serverCard.getResource(), serverCard.getCardNumber(), serverCard.getExpiryDate(),
                        serverCard.getCvv(), serverCard.getOwnerName(),
                        serverCard.getCardPin(), serverCard.getCardNetwork(), serverCard.getCardType(),
                        serverCard.getOwnerUsername(),
                        serverCard.getDateAdded(), serverCard.getLastModified(),
                        serverCard.getDeleted(), serverCard.getSync()
                );
            }
        }
    }

    public void mergeServerLinks(Set<Link> serverLinks) {
        Set<Link> localLinks = new HashSet<>(getAllLinks(false, true, PreferencesManager.getUsernameEncrypted()));
        for (Link serverLink : serverLinks) {
            Optional<Link> localOpt = localLinks.stream()
                    .filter(link -> link.getId().equals(serverLink.getId()))
                    .findFirst();
            if (Boolean.parseBoolean(serverLink.getDeleted())) {
                if (localOpt.isPresent()) {
//                    System.out.println("Hard deleting link " + serverLink.getId() + " (was deleted on server)");
                    deleteLink(serverLink);
                }
                continue;
            }
            if (localOpt.isPresent()) {
                Link localLink = localOpt.get();

                if (serverLink.getLastModified().isAfter(localLink.getLastModified())) {
//                    System.out.println("Updating local link " + serverLink.getId() + " from server (server newer)");
                    updateLink(serverLink, serverLink.getOwnerUsername());
                } else {
//                    System.out.println("Keeping local link " + localLink.getId() + " (local newer)");
                }
            } else {
//                System.out.println("Inserting new link " + serverLink.getId() + " from server");
                writeLinkTodb(
                        serverLink.getId(),
                        serverLink.getResource(), serverLink.getLink(),
                        serverLink.getOwnerUsername(),
                        serverLink.getDateAdded(), serverLink.getLastModified(),
                        serverLink.getDeleted(), serverLink.getSync()
                );
            }
        }
    }

    public void mergeServerWallets(Set<Wallet> serverWallets) {
        Set<Wallet> localWallets = new HashSet<>(getAllWallets(false, true, PreferencesManager.getUsernameEncrypted()));
        for (Wallet serverWallet : serverWallets) {
            Optional<Wallet> localOpt = localWallets.stream()
                    .filter(wallet -> wallet.getId().equals(serverWallet.getId()))
                    .findFirst();
            if (Boolean.parseBoolean(serverWallet.getDeleted())) {
                if (localOpt.isPresent()) {
//                    System.out.println("Hard deleting wallet " + serverWallet.getId() + " (was deleted on server)");
                    deleteWallet(serverWallet);
                }
                continue;
            }
            if (localOpt.isPresent()) {
                Wallet localWallet = localOpt.get();

                if (serverWallet.getLastModified().isAfter(localWallet.getLastModified())) {
//                    System.out.println("Updating local wallet " + serverWallet.getId() + " from server (server newer)");
                    updateWallet(serverWallet, serverWallet.getOwnerUsername());
                } else {
//                    System.out.println("Keeping local wallet " + localWallet.getId() + " (local newer)");
                }
            } else {
//                System.out.println("Inserting new wallet " + serverWallet.getId() + " from server");
                writeWalletTodb(
                        serverWallet.getId(),
                        serverWallet.getResource(), serverWallet.getKeyWords(), serverWallet.getAddress(), serverWallet.getPassword(),
                        serverWallet.getOwnerUsername(),
                        serverWallet.getDateAdded(), serverWallet.getLastModified(),
                        serverWallet.getDeleted(), serverWallet.getSync()
                );
            }
        }
    }


    public <T extends ItemEntity> void softDelete(T item, String tableName) {
        String sql = "UPDATE " + tableName + " SET deleted = 'true', last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            pstmt.setString(2, item.getId());
            pstmt.setString(3, item.getOwnerUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Failed to set delete status: " + e.getMessage());
        }
    }
    public <T extends ItemEntity> void syncStatus(T item, String tableName) {
        String sql = "UPDATE " + tableName + " SET sync = ?, last_modified = ? " +
                "WHERE id = ? AND owner_username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getSync());
            pstmt.setTimestamp(2, Timestamp.valueOf(item.getLastModified()));

            pstmt.setString(3, item.getId());
            pstmt.setString(4, item.getOwnerUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to set sync status: " + e.getMessage());
        }
    }

}


