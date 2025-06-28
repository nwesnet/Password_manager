package nwes.passwordmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.crypto.SecretKey;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;

public class SyncManager {
    private static final String BASE_URL = "https://[2a01:4f9:c013:c9bb::1]:8443/api/";

    public static String registerOnServer(String email, String username, String password, String confirmPass) {
        try {
            URL url = new URL(BASE_URL + "register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format(
                    "{\"email\":\"%s\", \"username\":\"%s\", \"password\":\"%s\", \"additionalPassword\":\"%s\"}",
                    email, username, password, confirmPass
            );
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return new String(err.readAllBytes());
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static PreferencesManager.Preferences loginOnServer(String username, String password) {
        try {
            URL url = new URL(BASE_URL + "login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);
            conn.setRequestProperty("Accept", "application/json");

            int code = conn.getResponseCode();
            if (code == 200) {
                String json = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))
                        .lines().collect(java.util.stream.Collectors.joining("\n"));

                com.google.gson.JsonObject obj = new com.google.gson.JsonParser().parse(json).getAsJsonObject();
                if (obj.has("migration_needed") && obj.get("migration_needed").getAsBoolean()) {
                    com.google.gson.JsonObject current = obj.getAsJsonObject("current");
                    com.google.gson.JsonObject old = obj.getAsJsonObject("old");

                    SecretKey migrationKey = EncryptionUtils.getKeyFromString(username + password);

                    String newUsername = EncryptionUtils.decrypt(current.get("username").getAsString(), migrationKey);
                    String newPassword = EncryptionUtils.decrypt(current.get("password").getAsString(), migrationKey);

                    String oldUsername = EncryptionUtils.decrypt(old.get("username").getAsString(), migrationKey);
                    String oldPassword = EncryptionUtils.decrypt(old.get("password").getAsString(), migrationKey);
                    String oldAdditionalPassword = EncryptionUtils.decrypt(old.get("additionalPassword").getAsString(), migrationKey);

                    PreferencesManager.createNewPreferences(oldUsername, oldPassword, oldAdditionalPassword, "true");

                    EncryptionUtils.reencryptAllData(oldUsername, oldPassword, newUsername, newPassword);

                    finishMigrationOnServer(newUsername, newPassword);

                    return loginOnServer(newUsername, newPassword);
                } else {
                    Gson gson = new Gson();
                    return gson.fromJson(obj, PreferencesManager.Preferences.class);
                }

            } else {
                System.out.println("Login failed, code: " + code);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }

    public static Set<Account> syncAccounts(Set<Account> localAccounts, String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "sync-accounts");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json; utf-8");

        String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);

        conn.setDoOutput(true);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(localAccounts);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        // ---- This is crucial! ----
        int responseCode = conn.getResponseCode();

        // Optional: read the response
        Set<Account> accountsFromServer = null;
        try (InputStream is = conn.getInputStream()) {
            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            Type accountListType = new TypeToken<Set<Account>>() {}.getType();
            accountsFromServer = gson.fromJson(response, accountListType);
        } catch (IOException e) {
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    String error = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                    System.err.println("SYNC ERROR: " + error);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return accountsFromServer;
    }
    public static Set<Card> syncCards(Set<Card> localCards, String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "sync-cards");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json; utf-8");

        String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);

        conn.setDoOutput(true);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(localCards);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();

        Set<Card> cardsFromServer = null;
        try (InputStream is = conn.getInputStream()) {
            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            Type cardListType = new TypeToken<Set<Card>>() {}.getType();
            cardsFromServer = gson.fromJson(response, cardListType);
        } catch (Exception e) {
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    String err = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                    System.err.println("SYNC ERROR: " + err);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return cardsFromServer;
    }

    public static Set<Link> syncLinks(Set<Link> localLinks, String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "sync-links");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-type", "application/json; utf-8");
        String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);

        conn.setDoOutput(true);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(localLinks);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();

        Set<Link> linksFromServer = null;
        try (InputStream is = conn.getInputStream()) {
            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            Type linkListType = new TypeToken<Set<Link>>() {}.getType();
            linksFromServer = gson.fromJson(response, linkListType);
        } catch (Exception e) {
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    String err = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                    System.err.println("SYNC ERROR: " + err);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return linksFromServer;
    }
    public static Set<Wallet> syncWallets(Set<Wallet> localWallets, String username, String password) throws IOException {
        URL url = new URL(BASE_URL + "sync-wallets");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-type", "application/json; utf-8");
        String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);

        conn.setDoOutput(true);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(localWallets);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();

        Set<Wallet> walletsFromServer = null;
        try (InputStream is = conn.getInputStream()) {
            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            Type walletListType = new TypeToken<Set<Wallet>>() {}.getType();
            walletsFromServer = gson.fromJson(response, walletListType);
        } catch (Exception e) {
            try (InputStream es = conn.getErrorStream()) {
                if (es != null) {
                    String err = new String(es.readAllBytes(), StandardCharsets.UTF_8);
                    System.err.println("SYNC ERROR: " + err);
                } else {
                    e.printStackTrace();
                }
            }
        }
        return walletsFromServer;
    }
    public static String reencryptOnServer(String username, String password, String newUsername, String newPassword, String newAdditionalPassword) throws IOException{
        URL url = new URL(BASE_URL + "sync-reencrypt");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        String params = "username=" + URLEncoder.encode(newUsername, "UTF-8") +
                "&password=" + URLEncoder.encode(newPassword, "UTF-8") +
                "&additionalPassword=" + URLEncoder.encode(newAdditionalPassword, "UTF-8");

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        InputStream is = (responseCode >= 200 && responseCode < 300) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }

        return response.toString();

    }
    public static String addAccountOnServer(Account account, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "add-account");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson =new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(account);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }

        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String updateAccountOnServer(Account account, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "update-account");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(account);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String deleteAccountOnServer(Account account, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "delete-account?id=" + URLEncoder.encode(account.getId(), "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static String addCardOnServer(Card card, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "add-card");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(card);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }

        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String updateCardOnServer(Card card, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "update-card");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(card);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String deleteCardOnServer(Card card, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "delete-card?id=" + URLEncoder.encode(card.getId(), "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }


    public static String addLinkOnServer(Link link, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "add-link");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            String json = gson.toJson(link);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream es = conn.getErrorStream()) {
                    return es != null ? new String(es.readAllBytes()) : "Unknown server error.";
                }
            }

        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String updateLinkOnServer(Link link, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "update-link");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(link);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream es = conn.getErrorStream()) {
                    return es != null ? new String(es.readAllBytes()) : "Unknown server error";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }
    public static String deleteLinkOnServer(Link link, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "delete-link?id=" + URLEncoder.encode(link.getId(), "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static String addWalletOnServer(Wallet wallet, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "add-wallet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(wallet);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static String updateWalletOnServer(Wallet wallet, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "update-wallet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            conn.setDoOutput(true);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            String json = gson.toJson(wallet);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();

            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error.";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static String deleteWalletOnServer(Wallet wallet, String username, String password) {
        try {
            URL url = new URL(BASE_URL + "delete-wallet?id=" + URLEncoder.encode(wallet.getId(), "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            String basicAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basicAuth);

            int code = conn.getResponseCode();
            if (code == 200) {
                return null;
            } else {
                try (InputStream err = conn.getErrorStream()) {
                    return err != null ? new String(err.readAllBytes()) : "Unknown server error";
                }
            }
        } catch (Exception e) {
            return "Connection error: " + e.getMessage();
        }
    }

    public static void finishMigrationOnServer(String username, String password) {
        try {
            URL url = new URL(BASE_URL + "finish-migration");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String basichAuth = Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic " + basichAuth);
            int code = conn.getResponseCode();
            if (code == 200) System.out.println("Migration finished on server");
        } catch (Exception e) {
            System.out.println("Failed to finish migration: " + e.getMessage());
        }
    }

}

