package me.redstoner2019;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class Server extends WebSocketServer {
    public static void main(String[] args) throws Exception {
        //DiffieHellmanServer server = new DiffieHellmanServer();
        //DiffieHellmanServer client = new DiffieHellmanServer();
        //String clientSecret = client.generateSharedSecret(server.getPublicKey());
        //String serverSecret = server.generateSharedSecret(client.getPublicKey());
        //System.out.println(server.getPublicKey().equals(client.getPublicKey()));
        //System.out.println(server.getPublicKey());
        //System.out.println(client.getPublicKey());
        //System.out.println(clientSecret);
        //System.out.println(serverSecret);


        WebSocketServer server = new Server(new InetSocketAddress(8010));
        server.start();
    }

    public DiffieHellmanServer server;

    public Server(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
        try {
            server = new DiffieHellmanServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println(webSocket.getRemoteSocketAddress() + " connected");
        webSocket.send(server.getPublicKey());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println(webSocket.getRemoteSocketAddress() + " disconnected");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(s);
        try {
            String sharedSecret = server.generateSharedSecret(s);
            System.out.println("Secret: " + sharedSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {

    }
}
