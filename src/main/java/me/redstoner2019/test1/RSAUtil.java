package me.redstoner2019.test1;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class RSAUtil {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";

    public static String encrypt(String message, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(getKey(key), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // Generate a random IV
        byte[] iv = new byte[16];
        new java.security.SecureRandom().nextBytes(iv); // Use SecureRandom to generate IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encrypted = cipher.doFinal(message.getBytes());

        // Encode both IV and encrypted message to Base64
        String ivBase64 = Base64.getEncoder().encodeToString(iv);
        String encryptedBase64 = Base64.getEncoder().encodeToString(encrypted);

        return ivBase64 + ":" + encryptedBase64;
    }

    public static String decrypt(String encryptedMessage, String key) throws Exception {
        String[] parts = encryptedMessage.split(":");
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

        SecretKeySpec secretKey = new SecretKeySpec(getKey(key), AES);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        byte[] original = cipher.doFinal(encryptedBytes);
        return new String(original);
    }

    private static byte[] getKey(String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(key.getBytes("UTF-8"));
        return keyBytes;
    }

    public static BigInteger[] generateRSAKeyPair(int bitLength) {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, random);
        return generateRSAKeyPair(p, q);
    }

    public static BigInteger[] generateRSAKeyPair(BigInteger p, BigInteger q) {
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        BigInteger d = e.modInverse(phi);
        return new BigInteger[]{n, e, d};
    }

    public static BigInteger encryptSessionKey(BigInteger sessionKey, BigInteger n, BigInteger e) {
        return sessionKey.modPow(e, n);
    }

    public static BigInteger decryptSessionKey(BigInteger ciphertext, BigInteger n, BigInteger d) {
        return ciphertext.modPow(d, n);
    }
}
