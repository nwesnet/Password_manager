package nwes.passwordmanager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptionUtils {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    private static final IvParameterSpec iv = new IvParameterSpec("1234567890abcdef".getBytes());

    public static SecretKey getKeyFromString(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(input.getBytes(CHARSET));
        byte[] key = new byte[16];
        System.arraycopy(keyBytes, 0, key, 0, key.length);
        return new SecretKeySpec(key, SECRET_ALGORITHM);
    }
    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encrypted);
    }
    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, CHARSET);
    }


}
