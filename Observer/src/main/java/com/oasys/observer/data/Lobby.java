package com.oasys.observer.data;

import com.google.gson.annotations.Expose;
import com.oasys.observer.communication.packets.GameState;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A lobby that contains all players and observers of a game.
 */
public class Lobby {
    private int lobbyId;
    private String name;
    private GameState gameState;
    private ArrayList<Integer> observerIds;


    /**
     * Initializes a lobby.
     * @param lobbyId The unique identifier of a lobby.
     * @param name The name of the lobby.
     * @param gameState The current state of the corresponding game.
     * @param observerIds A list of IDs of all observers in the lobby. IDs must be non-negative.
     * @throws IllegalArgumentException If an observerId is negative.
     */
    public Lobby(int lobbyId, String name, GameState gameState, ArrayList<Integer> observerIds)
                  throws IllegalArgumentException {
        for (int observerID: observerIds) {
            if (observerID < 0) {
                throw new IllegalArgumentException("observer ID must be non-negative");
            }
        }

        this.lobbyId = lobbyId;
        this.name = name;
        this.gameState = gameState;
        this.observerIds = observerIds;
    }

    /**
     * Initializes an empty lobby.
     * @param lobbyId The unique identifier of a lobby.
     * @param name The name of the lobby.
     * @param gameState The current state of the corresponding game.
     */
    public Lobby(int lobbyId, String name, GameState gameState) {
        this.lobbyId = lobbyId;
        this.name = name;
        this.gameState = gameState;
        this.observerIds = new ArrayList<>();
    }

    /**
     * Gets the lobby's unique identifier.
     * @return The lobby's unique identifier.
     */
    public int getLobbyId() {
        return this.lobbyId;
    }

    /**
     * Sets the lobby's unique identifier.
     * @param lobbyId The lobby's unique identifier.
     */
    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Gets the lobby's name.
     * @return The lobby's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the lobby's name.
     * @param lobbyName The lobby's name.
     */
    public void setName(String lobbyName) {
        this.name = name;
    }

    /**
     * Gets the current state of the corresponding game.
     * @return The current state of the corresponding game.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Sets the current state of the corresponding game.
     * @param gameState The current state of the corresponding game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets all observers in the lobby.
     * @return The IDs of all observers in the lobby.
     */
    public ArrayList<Integer> getObserverIds() {
        return this.observerIds;
    }

    /**
     * Sets the observers in the lobby.
     * @param observerIds A list of IDs of all observers in the lobby. IDs must be non-negative.
     * @throws IllegalArgumentException  If an observerId is negative.
     */
    public void setObserverIds(ArrayList<Integer> observerIds) throws IllegalArgumentException {
        for (int observerID: observerIds) {
            if (observerID < 0) {
                throw new IllegalArgumentException("observer ID must be non-negative");
            }
        }
        this.observerIds = observerIds;
    }

    /**
     * Checks equality of another object with the lobby.
     * @param o Another object.
     * @return True iff the ID and name of the lobby are identical.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return this.getLobbyId() == lobby.getLobbyId() && this.getName().equals(lobby.getName());
    }

    /**
     * Hashes the lobby by using the lobby's ID and name.
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), this.getName());
    }

    /**
     * Converts the lobby into a string i.e. its name.
     * @return The name of the lobby.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
