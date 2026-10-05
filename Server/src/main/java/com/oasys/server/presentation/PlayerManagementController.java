package com.oasys.server.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * This class allows adding and removing players for a game.
 * The list of available players and the list of added players are displayed using Listviews,
 * providing players with an easy way to manage participants.
 */
public class PlayerManagementController {
    @FXML
    public ListView<String> availablePlayersListView;
    @FXML
    public ListView<String> addedPlayersListView;
    private ObservableList<String> availablePlayers = FXCollections.observableArrayList();
    private ObservableList<String> addedPlayers = FXCollections.observableArrayList();

    // Getter method to access the list
    public ObservableList<String> getAddedPlayers() {
        return addedPlayers;
    }

    // Initialize the lists with sample data
    public void initialize() {
        availablePlayers.addAll("Spieler 1", "Spieler 2", "Spieler 3", "Spieler 4", "Spieler 5");
        availablePlayersListView.setItems(availablePlayers);
        addedPlayersListView.setItems(addedPlayers);
    }

    // Add selected player to the game
    @FXML
    public void addPlayerToGame() {
        String selectedPlayer = availablePlayersListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            availablePlayers.remove(selectedPlayer);
            addedPlayers.add(selectedPlayer);
        }
    }

    // Remove selected player from the game
    @FXML
    public void removePlayerFromGame() {
        String selectedPlayer = addedPlayersListView.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            addedPlayers.remove(selectedPlayer);
            availablePlayers.add(selectedPlayer);
        }
    }

    // Randomize the added players
    @FXML
    public void randomizePlayers() {
        FXCollections.shuffle(addedPlayers); // Shuffle the added players list
    }
}
