package com.oasys.engine.data;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The class player contains all the players of a game with all their attributes relevant to the game.
 */
public class Player {
    private int playerId;
    private String name;
    private int money;
    private ArrayList<BettingCard> bettingCards;
    private PlayerState state; //TODO make sure that is serialized and deserialized to the camelCase used in the interface

    /**
     * Initializes an object of Player.
     * @param playerId Unique player ID.
     * @param name name of player.
     * @param money current amount of player's money.
     * @param bettingCards all the bettingcards the player owns.
     * @param state current state of player. Must be in enum PlayerState.
     */

    public Player(int playerId, String name, int money, ArrayList<BettingCard> bettingCards, PlayerState state){
        this.playerId = playerId;
        this.name = name;
        this.money = money;
        this.bettingCards = bettingCards;
        this.state = state;
    }

    /**
     * Adds money to Player.
     * @param number Of money that is added.
     */
    public void addMoney(int number) {
        this.money += number;
    }

    /**
     * Reduces money of player.
     * @param number Of lost money.
     */
    public void loseMoney(int number) {
        if ((this.money - number) < 0) {
            this.money = 0;
        } else {
            this.money -= number;
        }
    }

    /**
     * Gets money.
     * @return money.
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets money.
     * @param money Updates player's money.
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Gets player's ID.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Sets player's ID.
     * @param playerId Unique ID of player.
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets name.
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     * @param name Player's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets bettingcards.
     * @return bettingCards.
     */
    public ArrayList<BettingCard> getBettingCards() {
        return bettingCards;
    }

    /**
     * Sets bettingcards.
     * @param bettingCards The player owns.
     */
    public void setBettingCards(ArrayList<BettingCard> bettingCards) {
        this.bettingCards = bettingCards;
    }

    /**
     * Gets player's state.
     * @return player's state.
     */
    public PlayerState getState() {
        return this.state;
    }

    /**
     * Sets player's state.
     * @param state The player is in. Must be in enum PlayerState.
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return this.playerId == player.playerId && Objects.equals(this.name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.playerId, this.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
