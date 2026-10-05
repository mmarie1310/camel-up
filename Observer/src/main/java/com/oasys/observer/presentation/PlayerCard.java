package com.oasys.observer.presentation;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a player card in the user interface.
 * Each player card displays the name, coins, rank, and includes a button to show bets.
 */

public class PlayerCard extends StackPane {

    // bet button
    private final Button betButton;

    // Name of the player
    private final String playerName;

    // ID of the player
    private final int playerID;



    /**
     * Constructor for creating a player card with the given player information.
     *
     * @param playerID       ID of the player.
     * @param playerName     Name of the player.
     * @param coins          Number of coins the player has.
     * @param hasPlacedTile  Status indicating whether the player has placed a tile.
     * @param turnOrder           The turnOrder of the player.
     */

    public PlayerCard(int playerID, String playerName, int coins, boolean hasPlacedTile, int turnOrder) {
        this.playerName = playerName;
        this.playerID = playerID;

        // Rectangle background
        Rectangle background = new Rectangle(120, 120);
        background.setArcWidth(15);
        background.setArcHeight(15);
        background.setFill(Color.FIREBRICK);
        background.setStroke(Color.FIREBRICK);

        // Hover-Effect
        DropShadow hoverShadow = new DropShadow(10, Color.FIREBRICK);
        this.setOnMouseEntered(event -> background.setEffect(hoverShadow));
        this.setOnMouseExited(event -> {
            background.setFill(Color.FIREBRICK);
            background.setEffect(null);
        });

        // Turn Order Label in Circle
        Circle turnOrderCircle = new Circle(10);
        turnOrderCircle.setFill(Color.BURLYWOOD);
        turnOrderCircle.setStroke(Color.BURLYWOOD);
        turnOrderCircle.setStrokeWidth(1);

        Label turnOrderLabel = new Label(String.valueOf(turnOrder));
        turnOrderLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: black;");
        turnOrderLabel.setAlignment(Pos.CENTER);

        StackPane turnOrderStack = new StackPane(turnOrderCircle, turnOrderLabel);
        turnOrderStack.setAlignment(Pos.CENTER);
        turnOrderStack.setTranslateX(44);
        turnOrderStack.setTranslateY(-45);

        // Label for playernmames
        Label nameLabel = new Label(playerName);
        nameLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold;-fx-text-fill: #eec399;");
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTranslateY(-30);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: coral;");
        separator.setPrefHeight(1);
        separator.setTranslateY(-16);
        separator.setMaxWidth(Double.MAX_VALUE);

        // Grid for coins, tiles and bets
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(15);
        infoGrid.setVgap(3);

        // Labels for coins and tiles
        Label coinsLabel = new Label("Münzen:");
        coinsLabel.setStyle("-fx-font-size: 12;-fx-text-fill: #eec399;");
        Label cardsLabel = new Label("Plättchen:");
        cardsLabel.setStyle("-fx-font-size: 12;-fx-text-fill: #eec399;");
        Label betLabel = new Label("Wetten:");
        betLabel.setStyle("-fx-font-size: 12;-fx-text-fill: #eec399;");

        // Coins
        Label coinValueLabel = new Label(String.valueOf(coins));
        coinValueLabel.setStyle("-fx-font-size: 12;-fx-text-fill: #eec399;");

        // Checkbox for tiles
        CheckBox tileCheckbox = new CheckBox();
        tileCheckbox.setDisable(true);
        tileCheckbox.setSelected(hasPlacedTile); // Status wird basierend auf Konstruktorwert gesetzt
        tileCheckbox.setStyle("-fx-opacity: 1; -fx-font-size: 12; -fx-text-fill: black; -fx-background-color: burlywood;");

        // Button for bets
        betButton = new Button();
        betButton.setStyle(
                "-fx-padding: 10px 20px; " +
                        "-fx-background-color: darkgray;" +
                        "-fx-text-fill: white; " +
                        "-fx-border-radius: 1px; " +
                        "-fx-border: 2px;" +
                        "-fx-border-color: #ffffff;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 4, 0, 0, 4);"
        );


        betButton.setMinSize(17, 17);
        betButton.setMaxSize(17, 17);

        // Adding Labels and Values to Grid
        infoGrid.add(coinsLabel, 0, 0);
        infoGrid.add(cardsLabel, 0, 1);
        infoGrid.add(betLabel, 0, 2);
        infoGrid.add(coinValueLabel, 1, 0);
        infoGrid.add(tileCheckbox, 1, 1);
        infoGrid.add(betButton, 1, 2);
        infoGrid.setAlignment(Pos.TOP_LEFT);

        // VBox for infoGrid
        VBox infoBox = new VBox(10, infoGrid);
        infoBox.setAlignment(Pos.TOP_CENTER);
        infoBox.setTranslateY(52);
        infoBox.setTranslateX(14);

        this.getChildren().addAll(background, turnOrderStack, nameLabel, separator, infoBox);
    }

    /**
     * Returns the bet button.
     *
     * @return The bet button.
     */
    public Button getBetButton() {
        return betButton;
    }

    /**
     * Returns the player ID.
     *
     * @return The ID of the player.
     */
    public int getPlayerID() {
        return playerID;
    }

    // Getter methods for test compatibility
    public String getPlayerName() {
        return playerName;
    }
}