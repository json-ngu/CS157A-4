package mindmerge.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;

    private PasswordUtil() {
    }

    public static String hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);

        byte[] hash = derive(password, salt, ITERATIONS);

        return ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean matches(String password, String storedValue) {
        if (storedValue == null) {
            return false;
        }

        String[] parts = storedValue.split(":", 3);

        if (parts.length != 3) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] actual = derive(password, salt, iterations);

            return MessageDigest.isEqual(expected, actual);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derive(
            String password,
            byte[] salt,
            int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_BITS
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "Could not hash password", e);

        } finally {
            spec.clearPassword();
        }
    }
}