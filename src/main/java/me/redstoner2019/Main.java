package me.redstoner2019;

import me.redstoner2019.test1.RSAUtil;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

public class Main {
    private static final String ALGORITHM = "AES";
    private static final String TOKEN = "ABCDEFG";

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.toString(RSAUtil.generateRSAKeyPair(new BigInteger("3796365361"),new BigInteger("2439732773"))));
        System.out.println(RSAUtil.decryptSessionKey(new BigInteger("6634489232834333879"),new BigInteger("9262116989513676053"),new BigInteger("3122751069815544833")));
        /*String message = "Dies ist eine Test Nachicht. Fisch.";

        JSONObject messageJSON = new JSONObject();

        messageJSON.put("message", message);
        messageJSON.put("signature", signMessage(message,TOKEN));

        System.out.println(messageJSON.toString(3));




        messageJSON = new JSONObject();

        message = "Dies ist eine weitere Test Nachicht. Fisch.";

        messageJSON.put("message", message);
        messageJSON.put("signature", signMessage(message,TOKEN));

        System.out.println(messageJSON.toString(3));*/
    }

    public static String encryptToken(String token, String secretKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(token.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String signMessage(String message, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}