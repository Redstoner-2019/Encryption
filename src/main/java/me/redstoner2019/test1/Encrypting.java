package me.redstoner2019.test1;

import java.math.BigInteger;
import java.security.SecureRandom;

import static me.redstoner2019.test1.RSAUtil.*;

public class Encrypting {
    // Method to generate RSA key pair
    public static void main(String[] args) {
        int bitLength = 2048;  // RSA key size in bits

        // Step 1: Alice generates her RSA key pair
        BigInteger[] serverKeyPair = generateRSAKeyPair(bitLength);
        BigInteger server_n = serverKeyPair[0];  // modulus
        BigInteger server_e = serverKeyPair[1];  // public exponent
        BigInteger server_d = serverKeyPair[2];  // private exponent
        System.out.println("Alice's public key: (n = " + server_n + ", e = " + server_e + ")");
        System.out.println("Alice's private key: (n = " + server_n + ", d = " + server_d + ")");

        // Step 2: Bob generates his RSA key pair
        BigInteger[] clientKeyPair = generateRSAKeyPair(bitLength);
        BigInteger client_n = clientKeyPair[0];  // modulus
        BigInteger client_e = clientKeyPair[1];  // public exponent
        BigInteger client_d = clientKeyPair[2];  // private exponent
        System.out.println("Bob's public key: (n = " + client_n + ", e = " + client_e + ")");
        System.out.println("Bob's private key: (n = " + client_n + ", d = " + client_d + ")");

        // Step 3: Alice generates a random session key (e.g., a 256-bit key for AES)
        BigInteger sessionKey = new BigInteger(32, new SecureRandom()); // Random 256-bit session key
        System.out.println("Alice's session key: " + sessionKey);

        // Step 4: Alice encrypts the session key using Bob's public key and sends it to Bob
        BigInteger encryptedSessionKey = encryptSessionKey(sessionKey, client_n, client_e);
        System.out.println("Encrypted session key (sent to Bob): " + encryptedSessionKey);

        // Step 5: Bob decrypts the session key using his private key
        BigInteger decryptedSessionKey = decryptSessionKey(encryptedSessionKey, client_n, client_d);
        System.out.println(client_n + " - " + client_d);
        System.out.println("Decrypted session key (Bob received): " + decryptedSessionKey);

        // Step 6: Verify that the decrypted session key matches the original session key
        if (sessionKey.equals(decryptedSessionKey)) {
            System.out.println("Success: Bob successfully decrypted the session key.");
        } else {
            System.out.println("Error: The decrypted session key does not match the original.");
        }
    }
}
