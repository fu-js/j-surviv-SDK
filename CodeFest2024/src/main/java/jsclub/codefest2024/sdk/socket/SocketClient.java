/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsclub.codefest2024.sdk.socket;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.socket.event.onTestGameReceive;
import jsclub.codefest2024.sdk.util.MsgPackUtil;
import jsclub.codefest2024.sdk.util.SocketUtil;

import java.io.IOException;

/**
 * SocketClient for connecting to a server using Socket.IO.
 * Author: AD
 */
public class SocketClient {
    private final String defaultUrl = "http://localhost:3000/sdk";
    private Socket socket;

    /**
     * Connects to the server at the specified URL.
     *
     * @param serverUrl The URL of the server to connect to.
     * @return true if connection was initiated successfully, false otherwise.
     */
    public Boolean connectToServer(String serverUrl) {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }

        socket = SocketUtil.init(serverUrl);

        if (socket == null) {
            return false;
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to the server");
                socket.on(EventName.ON_TEST_GAME_RECEIVE, new onTestGameReceive(socket));
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected from the server");
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.err.println("Connection error: " + args[0]);
            }
        });

        socket.connect();
        return true;
    }

    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Disconnects from the currently connected server.
     */
    public void disconnectFromServer() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        }
    }
}
