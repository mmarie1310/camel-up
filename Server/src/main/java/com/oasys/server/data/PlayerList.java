package com.oasys.server.data;
import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    // List to store player names
    private List<String> players;

    // Constructor
    public  PlayerList() {
        players = new ArrayList<>();
    }

    // Method to add a new player
    public void addPlayer(String playerName) {
        players.add(playerName);
    }

    // Method to get the list of all players
    public List<String> getPlayers() {
        return players;
    }
}
