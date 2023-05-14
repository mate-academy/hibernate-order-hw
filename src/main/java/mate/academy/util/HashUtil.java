package mate.academy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
    private static HashUtil instance;
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "SHA-512";

    private HashUtil() {
    }

    public static HashUtil getInstance() {
        if (instance == null) {
            instance = new HashUtil();
        }
        return instance;
    }

    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public String hash(String password, byte[] salt) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find algorithm for hash creation: "
                    + ALGORITHM, e);
        }
        messageDigest.update(salt);
        return Base64.getEncoder().encodeToString(messageDigest.digest(password.getBytes()));
    }
}
