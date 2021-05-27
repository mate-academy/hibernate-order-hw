package mate.academy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
    public static final String HASH_ALGORITHM = "SHA-512";

    public static String hashPassword(String password, byte[] salt) {
        StringBuilder hashedPwd = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITHM);
            messageDigest.update(salt);
            byte[] digest = messageDigest.digest(password.getBytes());
            for (byte b : digest) {
                hashedPwd.append(String.format("%02x", b));
            }
            return hashedPwd.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can`t hash password!", e);
        }
    }

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
