package com.oasys.observer.presentation;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class SpectatorTile {
    @FXML
    Label numberLabel;

    @FXML
    Label playerNameLabel;

    /**
     * Set the value of a player card/spectator tile.
     * @param value The value to set to. Must be 1 or -1.
     */
    public void setPlus(int value) {
        if (value == 1) {
            numberLabel.setText("+1");
        }  else if (value == -1) {
            numberLabel.setText("-1");
        }
    }

    /**
     * Set the player name of a player card/spectator tile.
     * @param playerName The neme to set to.
     */
    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }
}
