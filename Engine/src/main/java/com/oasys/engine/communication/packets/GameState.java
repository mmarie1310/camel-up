package com.oasys.engine.communication.packets;


import com.oasys.engine.data.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This packet represents the game state. It is sent from server to client everytime the game gets updated
 * during an active game.
 */
public class GameState extends Packet {
    private GamePhase gamePhase;
    private ArrayList<BoardSpace> boardSpaces;
    private GameConfig gameConfig;
    private ArrayList<RolledDice> rolledDice;
    private ArrayList<BettingCards> bettingCards;
    private ArrayList<Player> players;
    private int turns;
    private int gameDuration;
    private int moveTimeRemaining;
    private FinalBets finalBets;

    /**
     * Initializes an object of gameState.
     * @param gamePhase The possible phases of a game. Cannot be null.
     * @param boardSpaces Represents a space (field) on the board. Cannot be null or empty.
     * @param gameConfig The configuration of the game. Cannot be null.
     * @param rolledDice An array list of rolled dices in a game. Cannot be null.
     * @param bettingCards An array list of bettingCards, a player can bet on camel with. Cannot be null.
     * @param players An array list of lobbyParticipants that are part of the game. Cannot be null or empty.
     * @param turns The number of rounds that have already been played. Cannot be negative.
     * @param gameDuration The number of ms that the game has already been played. Cannot be negative.
     * @param moveTimeRemaining The remaining time a player has for his move.(in ms) Cannot be negative.
     * @param finalBets The two stacks of cards with the final bets. Cannot be null.
     */
    public GameState(GamePhase gamePhase, ArrayList<BoardSpace> boardSpaces, GameConfig gameConfig,
                     ArrayList<RolledDice> rolledDice, ArrayList<BettingCards> bettingCards,
                     ArrayList<Player> players, int turns, int gameDuration, int moveTimeRemaining,
                     FinalBets finalBets) {

        if (gamePhase == null) {
            throw new IllegalArgumentException("gamePhase cannot be null");
        } else if (boardSpaces == null || boardSpaces.isEmpty()) {
            throw new IllegalArgumentException("boardSpaces cannot be null or empty.");
        } else if (gameConfig == null) {
            throw new IllegalArgumentException("gameConfiguration cannot be null.");
        } else if (rolledDice == null) {
            throw new IllegalArgumentException("The list of rolled dices cannot be null.");
        } else if (bettingCards == null) {
            throw new IllegalArgumentException("The list of bettingCards cannot be null.");
        } else if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("The list of Player cannot be null or empty.");
        } else if (turns < 0) {
            throw new IllegalArgumentException("rounds cannot be negative.");
        } else if (gameDuration < 0) {
            throw new IllegalArgumentException("gameDuration cannot be negative.");
        } else if (moveTimeRemaining < 0) {
             throw new IllegalArgumentException("moveTimeRemaining cannot be negative..");
        } else if (finalBets == null) {
            throw new IllegalArgumentException("finalBets cannot be null.");
        }

        this.gamePhase = gamePhase;
        this.boardSpaces = boardSpaces;
        this.gameConfig = gameConfig;
        this.rolledDice = rolledDice;
        this.bettingCards = bettingCards;
        this.players = players;
        this.turns = turns;
        this.gameDuration = gameDuration;
        this.moveTimeRemaining = moveTimeRemaining;
        this.finalBets = finalBets;
    }

    /**
     * Gets gamePhase.
     * @return gamePhase.
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * Sets gamePhase.
     * @param gamePhase The possible phases of a game.
     * @throws IllegalArgumentException If game phase is null.
     */
    public void setGamePhase(GamePhase gamePhase) throws IllegalArgumentException {
        if (gamePhase == null) {
            throw new IllegalArgumentException("Game phase cannot be null.");
        }
        this.gamePhase = gamePhase;
    }

    /**
     * Gets boardSpaces.
     * @return boardSpaces.
     */
    public ArrayList<BoardSpace> getBoardSpaces() {
        return boardSpaces;
    }

    /**
     * Sets boardSpaces.
     * @param boardSpaces Represents a space (field) on the board.
     * @throws IllegalArgumentException If boardSpaces is null.
     */
    public void setBoardSpaces(ArrayList<BoardSpace> boardSpaces) throws IllegalArgumentException {
        if (boardSpaces == null || boardSpaces.isEmpty()) {
            throw new IllegalArgumentException("Board spaces cannot be null or empty.");
        }
        this.boardSpaces = boardSpaces;
    }

    /**
     * Gets gameConfig.
     * @return gameConfig.
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * Sets gemConfig.
     * @param gameConfig The configuration of the game.
     * @throws IllegalArgumentException If game config is null.
     */
    public void setGameConfig(GameConfig gameConfig) throws IllegalArgumentException {
        if (gameConfig == null) {
            throw new IllegalArgumentException("Game config cannot be null.");
        }
        this.gameConfig = gameConfig;
    }

    /**
     * Gets rolledDice.
     * @return rolledDice.
     */
    public ArrayList<RolledDice> getRolledDice() {
        return rolledDice;
    }

    /**
     * Sets rolledDice.
     * @param rolledDice An array list of rolled dices in a game.
     * @throws IllegalArgumentException If rolledDice is null.
     */
    public void setRolledDice(ArrayList<RolledDice> rolledDice) throws IllegalArgumentException {
        if (rolledDice == null) {
            throw new IllegalArgumentException("Rolled dice list cannot be null.");
        }
        this.rolledDice = rolledDice;
    }

    /**
     * Gets bettingCards.
     * @return bettingCards.
     */
    public ArrayList<BettingCards> getBettingCards() {
        return bettingCards;
    }

    /**
     * Sets bettingCards.
     * @param bettingCards An array list of bettingCards, a player can bet on camel with.
     * @throws IllegalArgumentException If bettingCards is null.
     */
    public void setBettingCards(ArrayList<BettingCards> bettingCards) throws IllegalArgumentException {
        if (bettingCards == null) {
            throw new IllegalArgumentException("Betting cards cannot be null.");
        }
        this.bettingCards = bettingCards;
    }

    /**
     * Gets bettingCards.
     * @return bettingCards.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Sets players.
     * @param players An array list of lobbyParticipants that are part of the game.
     * @throws IllegalArgumentException If players is null.
     */
    public void setPlayers(ArrayList<Player> players) throws IllegalArgumentException {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Players list cannot be null or empty.");
        }
        this.players = players;
    }

    /**
     * Gets rounds.
     * @return rounds.
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Sets rounds.
     * @param turns The number of rounds that have already been played.
     * @throws IllegalArgumentException If rounds is negative.
     */
    public void setTurns(int turns) throws IllegalArgumentException {
        if (turns < 0) {
            throw new IllegalArgumentException("Rounds must be >= 0.");
        }
        this.turns = turns;
    }

    /**
     * Gets gameDuration.
     * @return gameDuration.
     */
    public long getGameDuration() {
        return gameDuration;
    }

    /**
     * Sets gameDuration.
     * @param gameDuration The number of ms that the game has already been played.
     * @throws IllegalArgumentException If gameDuration is negative.
     */
    public void setGameDuration(int gameDuration) throws IllegalArgumentException {
        if (gameDuration < 0) {
            throw new IllegalArgumentException("Game duration must be >= 0.");
        }
        this.gameDuration = gameDuration;
    }

    /**
     * Gets moveTimeRemaining.
     * @return moveTimeRemaining.
     */
    public long getMoveTimeRemaining() {
        return moveTimeRemaining;
    }

    /**
     * Sets moveTimeRemaining.
     * @param moveTimeRemaining The remaining time a player has for his move.(in ms)
     * @throws IllegalArgumentException If moveTimeRemaining is negative.
     */
    public void setMoveTimeRemaining(int moveTimeRemaining) throws IllegalArgumentException {
        if (moveTimeRemaining < 0) {
            throw new IllegalArgumentException("Move time remaining must be >= 0.");
        }
        this.moveTimeRemaining = moveTimeRemaining;
    }

    /**
     * Gets finalBets.
     * @return finalBets.
     */
    public FinalBets getFinalBets() {
        return finalBets;
    }

    /**
     * Sets finalBets.
     * @param finalBets The two stacks of cards with the final bets.
     * @throws IllegalArgumentException If fianlBets is null.
     */
    public void setFinalBets(FinalBets finalBets) throws IllegalArgumentException{
        if (finalBets == null) {
            throw new IllegalArgumentException("Final bets cannot be null.");
        }
        this.finalBets = finalBets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return turns == gameState.turns && gameDuration == gameState.gameDuration && moveTimeRemaining == gameState.moveTimeRemaining && gamePhase == gameState.gamePhase && Objects.equals(boardSpaces, gameState.boardSpaces) && Objects.equals(gameConfig, gameState.gameConfig) && Objects.equals(rolledDice, gameState.rolledDice) && Objects.equals(bettingCards, gameState.bettingCards) && Objects.equals(players, gameState.players) && Objects.equals(finalBets, gameState.finalBets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gamePhase, boardSpaces, gameConfig, rolledDice, bettingCards, players, turns, gameDuration, moveTimeRemaining, finalBets);
    }
}



