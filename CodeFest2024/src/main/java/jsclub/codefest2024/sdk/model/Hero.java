/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jsclub.codefest2024.sdk.model;

import io.socket.client.Socket;
import jsclub.codefest2024.sdk.socket.EventName;
import jsclub.codefest2024.sdk.socket.SocketClient;
import jsclub.codefest2024.sdk.socket.data.emitData.*;
import jsclub.codefest2024.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.Date;

/**
 *
 * @author Son Duong
 */
public class Hero {
    private String player_name = "";
    private String gameID = "";
    private final SocketClient socketClient;
    
    public Hero(String player_name, String gameID) {
        this.player_name = player_name;
        this.gameID = gameID;

        this.socketClient = new SocketClient();
    }

    public void start(String serverURL) {
        socketClient.connectToServer(serverURL + "/sdk");
    }


    public String getPlayerID() {
        return player_name;
    }
    public String getGameID() {
        return gameID;
    }

    public void move() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            BotMove botMove = new BotMove("llru");

            byte[] bytes = MsgPackUtil.encodeFromObject(botMove);
            socket.emit(EventName.EMIT_MOVE, bytes);
        }
    }

    public void shoot() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            String data = "{}";
            byte[] bytes = MsgPackUtil.encodeFromObject(data);
            socket.emit(EventName.EMIT_SHOOT, bytes);
        }
    }

    public void attack() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            String data = "{}";
            byte[] bytes = MsgPackUtil.encodeFromObject(data);
            socket.emit(EventName.EMIT_ATTACK, bytes);
        }
    }

    public void Throw() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            BotThrow botThrow = new BotThrow(1, 0, 0);

            byte[] bytes = MsgPackUtil.encodeFromObject(botThrow);
            socket.emit(EventName.EMIT_THROW, bytes);
        }
    }

    public void pickupItem() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            String data = "{}";
            byte[] bytes = MsgPackUtil.encodeFromObject(data);
            socket.emit(EventName.EMIT_PICKUP_ITEM, bytes);
        }
    }

    public void useItem() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            BotUseItem botUseItem = new BotUseItem(1);

            byte[] bytes = MsgPackUtil.encodeFromObject(botUseItem);
            socket.emit(EventName.EMIT_USE_ITEM, bytes);
        }
    }

    public void revokeItem() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            BotRevokeItem botRevokeItem = new BotRevokeItem(1);

            byte[] bytes = MsgPackUtil.encodeFromObject(botRevokeItem);
            socket.emit(EventName.EMIT_REVOKE_ITEM, bytes);
        }
    }
}
