package mindmerge.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2-HMAC-SHA256 password hashing, stored in the user table's two columns:
 *   password_hash = "pbkdf2$<iterations>$<base64 hash>"
 *   salt          = "<base64 salt>"   (16 random bytes per user)
 * The iteration count travels with the hash, so it can be raised later without breaking old passwords.
 */
public final class PasswordUtil {

    private static final String SCHEME = "pbkdf2";
    private static final int ITERATIONS = 210_000;
    private static final int SALT_BYTES = 16;
    private static final int KEY_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    /** A freshly hashed password: both values go into the user row. */
    public static final class Hashed {
        public final String hash;
        public final String salt;

        private Hashed(String hash, String salt) {
            this.hash = hash;
            this.salt = salt;
        }
    }

    private PasswordUtil() {
    }

    public static Hashed hash(String password) {
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);

        byte[] hash = derive(password, salt, ITERATIONS);

        return new Hashed(
                SCHEME + "$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(hash),
                Base64.getEncoder().encodeToString(salt));
    }

    /** False for anything that isn't a hash this class made, such as the seed data's placeholders. */
    public static boolean matches(String password, String storedHash, String storedSalt) {
        if (password == null || storedHash == null || storedSalt == null) {
            return false;
        }

        String[] parts = storedHash.split("\\$");

        if (parts.length != 3 || !SCHEME.equals(parts[0])) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] expected = Base64.getDecoder().decode(parts[2]);
            byte[] salt = Base64.getDecoder().decode(storedSalt);
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
