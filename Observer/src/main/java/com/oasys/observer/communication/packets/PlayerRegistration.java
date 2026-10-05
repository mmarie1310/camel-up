package com.oasys.observer.communication.packets;

import java.util.Objects;

/**
 * The packet PlayerRegistration is sent from client to server if a player wants to join the game.
 */
public class PlayerRegistration extends Packet {
    private String playerName;

    /**
     * Initilaizes an object of PlayerRegistration.
     * @param playerName Is the name of the player, who registers for the game.
     */
    public PlayerRegistration(String playerName) {
        this.playerName = playerName;

    }

    /**
     * Gets the player's name.
     * @return Player's name.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Sets player's name.
     * @param playerName Player's name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRegistration that = (PlayerRegistration) o;
        return Objects.equals(playerName, that.playerName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerName);
    }
}
