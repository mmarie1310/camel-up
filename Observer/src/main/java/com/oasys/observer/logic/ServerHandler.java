package com.oasys.observer.logic;

import com.oasys.observer.communication.packets.*;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.util.Objects;


/**
 * Handles the connection with the server, send and receive messages
 */
public class ServerHandler implements Runnable {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean closed = false;
    private StringBuilder jsonBuilder = new StringBuilder();
    private MethodHandler methodHandler;

    public String ip;
    public int port;


    public ServerHandler(String ip, int port, MethodHandler methodHandler) {
        this.ip = ip;
        this.port = port;
        this.methodHandler = methodHandler;
    }

    @Override
    public void run() {
        /** Connects Client to the Server **/
        try {
            if (port < 0 || port > 65535) {
                methodHandler.getBoardUpdater().notify(BoardEvent.CONNECTION_FAILED);
                return;
            }
            this.clientSocket = new Socket(ip, port);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connected to server " + ip + " on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
            methodHandler.getBoardUpdater().notify(BoardEvent.CONNECTION_FAILED);
            return;
        }

        /** While client is connected, It awaits messages (JSON Strings) from the server, saves them
         *  into parameter line and prints the received String into the console **/
        while (clientSocket.isConnected()) {
            try {
                String line = in.readLine();
                jsonBuilder.append(line);
                try {
                    if (Objects.equals(line, "}")){
                        System.out.println(jsonBuilder.toString());
                        unpackPacket(jsonBuilder.toString());
                        jsonBuilder = new StringBuilder();
                    }
                } catch (Exception e) {
                    System.out.println("error during message decoding");
                    e.printStackTrace();
                }

            } catch (IOException e) {
                if (closed) return;
                e.printStackTrace();
                close();
                break;
            }
        }

    }

    /**
     * send a message to the server
     **/
    public void sendMessage(String msg) {
            try {
                this.out.println(msg);
                System.out.println(msg + " sent");
            } catch (Exception e) {
                System.out.println("error during message decoding");
            }
    }

    /**
     * close the server connection
     */
    public void close() {
        try {
            System.out.println("connection closed");
            closed = true;
            if (clientSocket != null) clientSocket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * is the current socket not existing or closed?
     *
     * @return closed
     */
    public boolean isClosed() {
        if (clientSocket != null) return clientSocket.isClosed();
        return true;
    }

    /**
     * Process the data of a received json.
     * @param json The json to process.
     */
    public void unpackPacket(String json) {
        Packet packet = Packet.convertFromJson(json);
        String packetClass = packet.getClass().getSimpleName();

        switch (packetClass) {
            case "ClientAck":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processClientAck((ClientAck) packet ));
                break;
            case "GameState":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processGameState((GameState) packet));
                break;
            case "LobbyList":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processLobbyList((LobbyList) packet));
                break;
            case "RecentGames":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processRecentGames((RecentGames) packet));
                break;
            case "SuccessFeedback":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processSuccessFeedback((SuccessFeedback) packet));
                break;
            case "GameEnd":
                Platform.runLater(() ->
                        methodHandler.getDataManager().processGameEnd((GameEnd) packet));
                break;
            default:
                System.out.println("Unknown packet type: " + packetClass);
        }
    }
}

