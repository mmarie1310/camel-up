package com.oasys.server.data;

import com.oasys.server.communication.packets.GameState;
import com.oasys.server.communication.packets.PlacePlayerCard;

import java.util.*;

/**
 * A lobby that contains all players and observers of a game.
 */
public class Lobby {
    private int lobbyId;
    private String name;
    private GameState gameState;
    private ArrayList<Integer> observerIds;
    private Map<String, PlacePlayerCard> playerCards;


    /**
     * Initializes a lobby.
     *
     * @param lobbyId     The unique identifier of a lobby.
     * @param name        The name of the lobby.
     * @param gameState   The current state of the corresponding game.
     * @param observerIds A list of IDs of all observers in the lobby. IDs must be non-negative.
     * @throws IllegalArgumentException If an observerId is negative.
     */
    public Lobby(int lobbyId, String name, GameState gameState, ArrayList<Integer> observerIds)
            throws IllegalArgumentException {
        for (int observerID : observerIds) {
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
     *
     * @param lobbyId   The unique identifier of a lobby.
     * @param name      The name of the lobby.
     * @param gameState The current state of the corresponding game.
     */
    public Lobby(int lobbyId, String name, GameState gameState) {
        this.lobbyId = lobbyId;
        this.name = name;
        this.gameState = gameState;
        this.observerIds = new ArrayList<>();
        this.playerCards = new HashMap<>();
    }

    public boolean hasPlayerCard(String player) {
        return playerCards.containsKey(player);
    }

    public PlacePlayerCard getPlayerCard(String player) {
        return playerCards.get(player);
    }

    public void setPlayerCard(String player, PlacePlayerCard card) {
        playerCards.put(player, card);
    }

    /**
     * Gets the lobby's unique identifier.
     *
     * @return The lobby's unique identifier.
     */
    public int getLobbyId() {
        return this.lobbyId;
    }

    /**
     * Sets the lobby's unique identifier.
     *
     * @param lobbyId The lobby's unique identifier.
     */
    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Gets the lobby's name.
     *
     * @return The lobby's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the lobby's name.
     *
     * @param lobbyName The lobby's name.
     */
    public void setName(String lobbyName) {
        this.name = name;
    }

    /**
     * Gets the current state of the corresponding game.
     *
     * @return The current state of the corresponding game.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Sets the current state of the corresponding game.
     *
     * @param gameState The current state of the corresponding game.
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets all observers in the lobby.
     *
     * @return The IDs of all observers in the lobby.
     */
    public ArrayList<Integer> getObserverIds() {
        return this.observerIds;
    }

    /**
     * Sets the observers in the lobby.
     *
     * @param observerIds A list of IDs of all observers in the lobby. IDs must be non-negative.
     * @throws IllegalArgumentException If an observerId is negative.
     */
    public void setObserverIds(ArrayList<Integer> observerIds) throws IllegalArgumentException {
        for (int observerID : observerIds) {
            if (observerID < 0) {
                throw new IllegalArgumentException("observer ID must be non-negative");
            }
        }
        this.observerIds = observerIds;
    }

    /**
     * Checks equality of another object with the lobby.
     *
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
     *
     * @return The hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId(), this.getName());
    }



    /**
     * Converts the lobby into a string i.e. its name.
     *
     * @return The name of the lobby.
     */
    /*
    @Override
    public String toString() {
        return this.name;
    }
}

// Override the toString() method
    @Override
    public String toString() {
        return "Lobby ID: " + this.lobbyId + ", Game: " + this.getName() + ", State: " + gameState.getGameConfig().toString() + ", Players: " + this.gameState.getGameConfig().getPlayerCount();
    }*/

    // Override toString() to display lobby details including game state and player count

    @Override
    public String toString() {
        // Retrieve all details from GameState
        GameConfig gameConfig = gameState.getGameConfig();
        int playerCount = gameConfig.getPlayerCount();
        String gamePhase = gameState.getGamePhase().toString();
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        List<RolledDice> rolledDice = gameState.getRolledDice();
        List<BettingCards> bettingCards = gameState.getBettingCards();
        List<Player> players = gameState.getPlayers();
        int turns = gameState.getTurns();
        int gameDuration =(int) gameState.getGameDuration();
        int moveTimeRemaining = (int) gameState.getMoveTimeRemaining();
        FinalBets finalBets = gameState.getFinalBets();

        // Formatting the string output for each element
        StringBuilder sb = new StringBuilder();
        sb.append("Lobby ID: ").append(this.lobbyId)
                .append(", Game: ").append(this.getName())
                .append(", State: ").append(gamePhase)
                .append(", Mode: ").append(gameConfig.toString())
                .append(", Players: ").append(playerCount)
                .append(", Turns: ").append(turns)
                .append(", Duration: ").append(gameDuration).append(" ms")
                .append(", Move Time Remaining: ").append(moveTimeRemaining).append(" ms")
                .append("\nBoard Spaces: ").append(boardSpaces)
                .append("\nRolled Dice: ").append(rolledDice)
                .append("\nBetting Cards: ").append(bettingCards)
                .append("\nPlayers: ").append(players);

        // If needed, include final bets
        sb.append("\nFinal Bets: ").append(finalBets);

        return sb.toString();
    }
}


