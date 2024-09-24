package me.redstoner2019.test1;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import static me.redstoner2019.test1.RSAUtil.encryptSessionKey;
import static me.redstoner2019.test1.RSAUtil.generateRSAKeyPair;

public class TestServer extends WebSocketServer {
    public static void main(String[] args) {
        TestServer server = new TestServer(8010);
        server.start();
    }

    public static HashMap<String,String> sessionKeys = new HashMap<>();

    public TestServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        int bitLength = 1024;

        BigInteger[] serverKeyPair = generateRSAKeyPair(bitLength);
        BigInteger server_n = serverKeyPair[0];  // modulus
        BigInteger server_e = serverKeyPair[1];  // public exponent
        BigInteger server_d = serverKeyPair[2];  // private exponent

        JSONObject data = new JSONObject();
        data.put("header","server-info");
        data.put("n", server_n.toString());
        data.put("e", server_e.toString());

        webSocket.send(data.toString());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        JSONObject data = new JSONObject(s);
        if(data.has("header")){
            if(data.getString("header").equals("connection")){
                BigInteger sessionKey = new BigInteger(512, new SecureRandom());
                BigInteger encryptedSessionKey = encryptSessionKey(sessionKey, new BigInteger(data.getString("n")), new BigInteger(data.getString("e")));

                String sessionKeyString = sessionKey.toString();
                sessionKeys.put(webSocket.getRemoteSocketAddress().toString(), sessionKeyString);
                System.out.println(sessionKeyString);

                JSONObject result = new JSONObject();
                result.put("sessionKey", encryptedSessionKey.toString());
                result.put("header","connection-result");
                webSocket.send(result.toString());
            }
            if(data.getString("header").equals("encrypted")){
                String encryption = data.getString("encryption");
                if(encryption.equals("AES")){
                    try {
                        data = new JSONObject(RSAUtil.decrypt(data.getString("data"),sessionKeys.get(webSocket.getRemoteSocketAddress().toString())));
                        JSONObject toSend = new JSONObject();
                        toSend.put("header","encrypted");
                        toSend.put("encryption",encryption);
                        toSend.put("data",RSAUtil.encrypt(data.toString(),sessionKeys.get(webSocket.getRemoteSocketAddress().toString())));
                        webSocket.send(toSend.toString());
                        System.out.println("sending " + toSend.toString(3));
                    } catch (Exception e) {
                        System.out.println(data.toString(3));
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Invalid encryption");
                    return;
                }
            }
            System.out.println(data.toString(3));
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }
}
