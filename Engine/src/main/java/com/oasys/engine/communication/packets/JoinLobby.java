package com.oasys.engine.communication.packets;

import java.util.Objects;

/**
 * Is sent from client to server, if client wants to join or exit a lobby.
 * Or from server to client, if server adds client to lobby.
 */
public class JoinLobby extends Packet {
    int lobbyId;
    boolean joinAsPlayer;

    /**
     * Initializes an object of JoinLobby.
     * @param lobbyId The ID of the lobby.
     * @param joinAsPlayer The status of the player.
     */
    public JoinLobby(int lobbyId, boolean joinAsPlayer) {
        if (lobbyId == -1) {
            this.lobbyId = lobbyId;
            this.joinAsPlayer = false;
        } else {
            this.lobbyId = lobbyId;
            this.joinAsPlayer = joinAsPlayer;
        }
    }

    /**
     * Gets lobbyId.
     * @return lobbyId.
     */
    public int getLobbyId() {
        return lobbyId;
    }

    /**
     * Sets lobbyId.
     * @param lobbyId The ID of the lobby.
     */
    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Gets joinAsPlayer.
     * @return joinAsPlayer.
     */
    public boolean isJoinAsPlayer() {
        return joinAsPlayer;
    }

    /**
     * Sets joinAsPlayer.
     * @param joinAsPlayer The status of the player.
     */
    public void setJoinAsPlayer(boolean joinAsPlayer) {
        this.joinAsPlayer = joinAsPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinLobby joinLobby = (JoinLobby) o;
        return lobbyId == joinLobby.lobbyId && joinAsPlayer == joinLobby.joinAsPlayer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lobbyId);
    }

}
