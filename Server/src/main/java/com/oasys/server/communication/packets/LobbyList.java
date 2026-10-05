package com.oasys.server.communication.packets;

import com.oasys.server.data.Lobby;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The lobbyList packet is sent from server to client as a response to the RequestLobbyList
 * packet. It provides a list of all open lobbies.
 */
public class LobbyList extends Packet {
    public ArrayList<Lobby> lobbies;

    /**
     * Initializes a LobbyList packet.
     * @param lobbies All lobbies that can be joined.
     */
    public LobbyList(ArrayList<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    /**
     * Gets all open lobbies.
     * @return all open lobbies.
     */
    public ArrayList<Lobby> getLobbies() {
        return this.lobbies;
    }

    /**
     * Sets all open lobbies.
     * @param lobbies All open lobbies.
     */
    public void setLobbies(ArrayList<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LobbyList lobbyList = (LobbyList) o;
        return Objects.equals(lobbies, lobbyList.lobbies);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lobbies);
    }
}
