/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsclub.codefest2024.sdk.socket;

import jsclub.codefest2024.sdk.util.SocketUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 * SocketClient for connecting to a server using Socket.IO.
 * Author: AD
 */
public class SocketClient {
    private final String defaultUrl = "http://localhost:3000";
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
                socket.on("msgpack_event", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        byte[] data = (byte[]) args[0];
                        try {
                            String message = decodeMsgPackMessage(data);
                            System.out.println("Message from server: " + message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                try {
                    byte[] encodedMessage = encodeMsgPackMessage( "Hello server");
                    socket.emit("msgpack_event", encodedMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    /**
     * Disconnects from the currently connected server.
     */
    public void disconnectFromServer() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
        }
    }

    /**
     * Encodes a message into MsgPack format.
     *
     * @param messages The messages to encode.
     * @return The encoded MsgPack message.
     * @throws IOException If an I/O error occurs.
     */
    private byte[] encodeMsgPackMessage(String... messages) throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        for (String message : messages) {
            packer.packString(message);
        }

        packer.close();
        return packer.toByteArray();
    }

    /**
     * Decodes a MsgPack message.
     *
     * @param data The MsgPack message to decode.
     * @return The decoded message.
     * @throws IOException If an I/O error occurs.
     */
    private String decodeMsgPackMessage(byte[] data) throws IOException {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(new ByteArrayInputStream(data));

        StringBuilder result = new StringBuilder();
        while (unpacker.hasNext()) {
            result.append(unpacker.unpackString()).append(" ");
        }
        unpacker.close();

        return result.toString().trim();
    }

    public static void main(String[] args) {
        SocketClient cli = new SocketClient();
        cli.connectToServer(cli.defaultUrl);
    }
}
