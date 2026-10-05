package com.oasys.observer.presentation;

import com.oasys.observer.logic.PlayerData;
import com.oasys.observer.logic.StageBet;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Manages the bet display for the respective player.
 * It shows the winner and loser bets as well as the stage bets.
 */
public class BetsViewController {

    // Label of winner bets
    @FXML
    public Label winnerBetLabel;

    // Label of loser bets
    @FXML
    public Label loserBetLabel;

    // TableView of bets placed during a stage
    @FXML
    public TableView<StageBet> betsTable;

    // TableColumn for displaying the camel associated with each bet
    @FXML
    private TableColumn<StageBet, String> camelColumn;

    // TableColumn for displaying the value of each bet
    @FXML
    private TableColumn<StageBet, Integer> valueColumn;

    // Button to close the current window or overlay
    @FXML
    private Button closeButton;
    /**
     * Called when the controller instance is initialized.
     * Configures the table and its columns.
     */
    @FXML
    public void initialize() {
        camelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCamel()));
        valueColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        camelColumn.setCellFactory(factory);
        betsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Sets the player's data and populates the table and the labels.
     *
     * @param playerData The data of the selected player.
     */
    public void setPlayerData(PlayerData playerData) {
        if (playerData != null) {
            // Populate the labels with the player's bet values
            winnerBetLabel.setText(String.valueOf(playerData.getWinnerBetCount()));
            loserBetLabel.setText(String.valueOf(playerData.getLoserBetCount()));

            // Insert the player's stage bets into the table
            betsTable.getItems().setAll(playerData.getStageBets());
        }
    }

    /**
     * Closes the overlay window.
     */
    @FXML
    private void closeOverlay() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Make cells colored to the camel colors.
     */
    Callback<TableColumn<StageBet, String>, TableCell<StageBet, String>> factory = new Callback<TableColumn<StageBet, String>, TableCell<StageBet, String>>() {
        @Override
        public TableCell<StageBet, String> call(TableColumn<StageBet, String> stageBetIntegerTableColumn) {
            return new TableCell<StageBet, String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    }
                    else {
                        setText(item);
                        setStyle("-fx-background-color: " + item + ";");
                    }
                }
            };
        }
    };
}

