package com.oasys.observer.presentation;
import com.oasys.observer.data.BettingCard;
import com.oasys.observer.data.Player;
import com.oasys.observer.logic.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Manages the player display in the user interface.
 * Creates, updates, and maintains player information.
 */
public class PlayerCardController implements EventListener {

    // Reference to the Method Handler
    private MethodHandler methodHandler;

    // Container for the player cards
    @FXML
    private HBox playerCardBox;

    // Stage for the overlay window displaying the bets
    private Stage overlayStage;

    // Currently selected player card
    private PlayerCard selectedPlayerCard;

    // List to manage player data
    private List<PlayerData> players;

    public PlayerCardController(MethodHandler methodHandler) {
        this.methodHandler = methodHandler;
        methodHandler.getBoardUpdater().subscribe(BoardEvent.ROUND_FINISH, this);
    }

    /**
     * Invoked during the initialization of the controller instance.
     * (Currently also loads test data and initializes the display.)
     */
    @FXML
    public void initialize() {
        players = methodHandler.getDataManager().getPlayerDataList();
        updatePlayerCards();
       // loadTestPlayers();
    }

    /**
     * Adds a new player and updates the display.
     *
     * @param playerData The data of the player to be added.
     */
    public void addPlayerData(PlayerData playerData) {
        players.add(playerData);
        updatePlayerCards();
    }

    /**
     * Sorts the players based on their coin count and then updates their ranks.
     */
    private void updatePlayerRanks() {
        List<PlayerData> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.comparingInt(PlayerData::getCoins).reversed());

        for (int i = 0; i < sortedPlayers.size(); i++) {
            PlayerData player = sortedPlayers.get(i);
            player.setRank(i + 1);
        }
    }

    /**
     * Updates the display of player cards based on the current data in the `players` list.
     */
    public void updatePlayerCards() {
        playerCardBox.getChildren().clear();
       // updatePlayerRanks();

        for (PlayerData playerData : players) {
            PlayerCard playerCard = new PlayerCard(
                    playerData.getPlayerID(),
                    playerData.getPlayerName(),
                    playerData.getCoins(),
                    playerData.isHasPlacedTile(),
                    playerData.getTurnOrder());

            setupBetButton(playerCard);
            playerCardBox.getChildren().add(playerCard);
        }
    }

    /**
     * Links the bet button of a player card to the corresponding betting view.
     *
     * @param playerCard The player card whose button is being configured.
     */
    private void setupBetButton(PlayerCard playerCard) {
        Button betButton = playerCard.getBetButton();
        betButton.setOnAction(event -> handleBetButton(playerCard));
    }

    /**
     * Event handler for clicking the bet button.
     * Opens the overlay to display the betting view.
     *
     * @param playerCard The player card for which the button was clicked.
     */
    private void handleBetButton(PlayerCard playerCard) {
        System.out.println("Wett-Button geklickt für Spieler: " + playerCard);
        selectedPlayerCard = playerCard;
        showBetOverlay();
    }

    /**
     * Displays the overlay window for placing bets.
     */
    private void showBetOverlay() {
        try {
            if (overlayStage == null || !overlayStage.isShowing()) {
                // If no overlay exists or the current overlay is closed, create and show a new one
                createAndShowOverlay();
            } else {
                // If the overlay is still open, close it and create a new one
                overlayStage.close();
                createAndShowOverlay();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and displays a new overlay window.
     *
     * @throws IOException If loading the FXML file fails.
     */
    private void createAndShowOverlay() throws IOException {
        // FXML-Datei laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/oasys/observer/presentation/bets-view.fxml"));
        Parent betsView = loader.load();
        BetsViewController betsViewController = loader.getController();

        if (selectedPlayerCard != null) {
            PlayerData playerData = findPlayerData(selectedPlayerCard);
            betsViewController.setPlayerData(playerData);
        }

        // Create a new overlay window
        overlayStage = new Stage(StageStyle.TRANSPARENT);
        overlayStage.initModality(Modality.NONE); // Modalität deaktiviert, damit Hauptfenster interaktiv bleibt
        overlayStage.setAlwaysOnTop(true);
        overlayStage.setWidth(250);
        overlayStage.setHeight(290);

        Scene scene = new Scene(betsView);
        overlayStage.setScene(scene);

        // Add a listener for the position and size of the main window
        Scene mainScene = playerCardBox.getScene();
        mainScene.getWindow().xProperty().addListener((observable, oldValue, newValue) -> updateOverlayPosition());
        mainScene.getWindow().yProperty().addListener((observable, oldValue, newValue) -> updateOverlayPosition());
        mainScene.getWindow().widthProperty().addListener((observable, oldValue, newValue) -> updateOverlayPosition());
        mainScene.getWindow().heightProperty().addListener((observable, oldValue, newValue) -> updateOverlayPosition());

        updateOverlayPosition();

        overlayStage.show();

        overlayStage.setOnCloseRequest(event -> overlayStage = null);
    }

    /**
     * Updates the position of the overlay window based on the main window.
     */
    private void updateOverlayPosition() {
        if (overlayStage != null && playerCardBox.getScene() != null) {
            Scene mainScene = playerCardBox.getScene();
            double mainWindowX = mainScene.getWindow().getX();
            double mainWindowY = mainScene.getWindow().getY();
            double mainWindowWidth = mainScene.getWindow().getWidth();
            double mainWindowHeight = mainScene.getWindow().getHeight();

            double overlayWidth = overlayStage.getWidth();
            double overlayHeight = overlayStage.getHeight();

            double overlayX = mainWindowX + (mainWindowWidth - overlayWidth) / 2;
            double overlayY = mainWindowY + (mainWindowHeight - overlayHeight) / 2 - 70;

            overlayStage.setX(overlayX);
            overlayStage.setY(overlayY);
        }
    }

    /**
     * Searches for the `PlayerData` that corresponds to the given `PlayerCard`.
     *
     * @param playerCard The player card for which data is being searched.
     * @return The corresponding `PlayerData`, or `null` if none was found.
     */
    private PlayerData findPlayerData(PlayerCard playerCard) {

        return players.stream()
                .filter(player -> player.getPlayerID() == playerCard.getPlayerID())
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the currently selected player card.
     *
     * @return The selected player card.
     */
    public PlayerCard getSelectedPlayerCard() {
        return selectedPlayerCard;
    }

    /**
     * Fills the stage bet tables with data from the current game state.
     */
    public void updateStageBets(){
        for (PlayerData playerData : players) {
            playerData.getStageBets().clear();
            for (Player player : methodHandler.getDataManager().getPlayers()) {
                if(playerData.getPlayerIdFromServer() == player.getPlayerId()) {
                    for (BettingCard bet : player.getBettingCards()) {
                        playerData.getStageBets().add(new StageBet(methodHandler.getHexColorOfCamel(bet.getCamelId()), bet.getWorth()));
                    }
                }
            }
        }
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case ROUND_FINISH:
                players = methodHandler.getDataManager().getPlayerDataList();
                updateStageBets();
                updatePlayerCards();
        }
    }

    // Getter and Setter for Testing
    public MethodHandler getMethodHandler() {
        return methodHandler;
    }

    public void setMethodHandler(MethodHandler methodHandler) {
        this.methodHandler = methodHandler;
    }

    public HBox getPlayerCardBox() {
        return playerCardBox;
    }

    public void setPlayerCardBox(HBox playerCardBox) {
        this.playerCardBox = playerCardBox;
    }

    public Stage getOverlayStage() {
        return overlayStage;
    }

    public void setOverlayStage(Stage overlayStage) {
        this.overlayStage = overlayStage;
    }

    public void setSelectedPlayerCard(PlayerCard selectedPlayerCard) {
        this.selectedPlayerCard = selectedPlayerCard;
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerData> players) {
        this.players = players;
    }
}
