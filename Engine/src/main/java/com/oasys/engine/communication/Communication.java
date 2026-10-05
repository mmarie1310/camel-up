package com.oasys.engine.communication;

import com.google.gson.JsonParseException;
import com.oasys.engine.communication.packets.Packet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *Represents the connection between engine participant and server.
 */
public class Communication {
    private static Communication communicationInstance;

    private int port;
    private String host;

    private boolean acknowledged = false;
    private int clientId;
    private String playerName;

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running = true;

    private Communication() {

    }

    public static synchronized Communication getInstance() {
        if (Communication.communicationInstance == null) {
            Communication.communicationInstance = new Communication();
        }
        return Communication.communicationInstance;
    }

    /**
     * Sends packet to server.
     * @param packet sent packet.
     */
    public void sendPacket(Packet packet) {
        if (!this.acknowledged) {
            return; // not allowed sending packets before being acknowledged by server
        }
        if (this.output == null) {
            return; // not connected yet
        }

        String json = packet.convertTojson();

        this.output.println(json);
    }

    /**
     * Connects participant via port and host.
     * @param port needed for connection.
     * @param host needed for connection.
     */
    public void connect(int port, String host) {
        this.port = port;
        this.host = host;

        try {
            System.out.println("connecting to server ...");
            this.socket = new Socket(host, port);

            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new PrintWriter(this.socket.getOutputStream(), true);

            new Thread(this::readMessages).start();
            System.out.println("successfully connected to server");
        } catch (IOException e) {
            System.out.println("Connection to server failed: " + e.getMessage());
        }
    }

    /**
     * Reads json string.
     */
    private void readMessages() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            boolean readingJson = false;

            while (this.running) {
                int counter = 0;
                while ((character = this.input.read()) != -1) {
                    char c = (char) character;

                    if (!readingJson && c == '{') {
                        readingJson = true;
                    }

                    if (readingJson) {
                        stringBuilder.append(c);
                    }

                    if (readingJson && c == '{') {
                        counter++;
                    }

                    if (readingJson && c == '}') {
                        counter--;
                    }

                    if (counter == 0) {
                        if (readingJson) {
                            readingJson = false;
                            break;
                        }
                    }
                }

                String json = stringBuilder.toString();
                if (!json.isEmpty()) {
                    this.handleIncomingJson(json);
                }

                stringBuilder.setLength(0);
            }

        } catch (IOException e) {
            if (running) {
                System.out.println("Error while reading messages: " + e.getMessage());
                this.disconnect();
            }
        }
    }


    /**
     * Processes packets sent by server.
     * @param json strings sent to engine participant.
     */
    public void handleIncomingJson(String json) throws IOException {
        System.out.println(json);

        try {
            Packet packet = Packet.convertFromJson(json);
        } catch (JsonParseException e) {
            System.out.println("Can't convert json. " + e.getMessage());
        }

        try {
            Packet packet = Packet.convertFromJson(json);
            PacketManager.getInstance().handleIncomingPacket(packet);
            System.out.println(packet);
        } catch (JsonParseException e) {
            System.out.println("Can't handle incoming packet. " + e.getMessage());
        }
    }

    /**
     * Disconnects engine participant from server.
     */
    public void disconnect() {
        running = false;
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Disconnected from the server.");
            }
        } catch (IOException e) {
            System.out.println("Error disconnecting: " + e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getPort() {
        return this.port;
    }

    public String getHost() {
        return this.host;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isAcknowledged() {
        return this.acknowledged;
    }

    public void setAcknowledged() {
        this.acknowledged = true;
    }
}