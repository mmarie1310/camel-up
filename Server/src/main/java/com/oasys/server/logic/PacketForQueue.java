package com.oasys.server.logic;

import com.google.gson.JsonObject;

public class PacketForQueue {
    JsonObject packet;
    ServerConnector.ClientHandler client;

    public PacketForQueue(JsonObject packet, ServerConnector.ClientHandler client) {
        this.packet = packet;
        this.client = client;
    }
    public JsonObject getPacket() {return packet;}
    public ServerConnector.ClientHandler getClient() {return client;}
}
