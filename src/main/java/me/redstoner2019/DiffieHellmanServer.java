package me.redstoner2019;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DiffieHellmanServer {
    private KeyPair keyPair;
    private KeyAgreement keyAgreement;

    public DiffieHellmanServer() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC");
        keyPairGen.initialize(256);  // Key size
        this.keyPair = keyPairGen.generateKeyPair();

        // Initialize KeyAgreement with private key
        keyAgreement = KeyAgreement.getInstance("ECDH");
        keyAgreement.init(keyPair.getPrivate());
    }

    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    public String generateSharedSecret(String clientPublicKeyStr) throws Exception {
        byte[] clientPublicKeyBytes = Base64.getDecoder().decode(clientPublicKeyStr);

        PublicKey clientPublicKey = KeyFactory.getInstance("EC")
                .generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));

        keyAgreement.doPhase(clientPublicKey, true);

        // Generate shared secret
        byte[] sharedSecret = keyAgreement.generateSecret();
        SecretKey secretKey = new SecretKeySpec(sharedSecret, 0, 16, "AES");

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
