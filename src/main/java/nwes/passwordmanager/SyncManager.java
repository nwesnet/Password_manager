package nwes.passwordmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class SyncManager {
    public static Set<Account> syncAccounts(Set<Account> localAccounts, String username, String password) throws IOException {
        URL url = new URL("http://localhost:8080/api/sync-accounts?username=" + username + "&password=" + password);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-type", "application/json; utf-8");
        conn.setDoOutput(true);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String json = gson.toJson(localAccounts);
        System.out.println("SYNC JSON: " + json);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        // ---- This is crucial! ----
        int responseCode = conn.getResponseCode();
        System.out.println("SYNC RESPONSE CODE: " + responseCode);

        // Optional: read the response
        Set<Account> accountsFromServer = null;
        try (InputStream is = conn.getInputStream()) {
            String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("SYNC RESPONSE: " + response);

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
}

