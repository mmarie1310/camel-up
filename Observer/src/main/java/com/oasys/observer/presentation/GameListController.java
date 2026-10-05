package com.oasys.observer.presentation;

import com.oasys.observer.communication.packets.GameEnd;
import com.oasys.observer.data.Lobby;
import com.oasys.observer.logic.BoardEvent;
import com.oasys.observer.logic.EventListener;
import com.oasys.observer.logic.MethodHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controls the game list/lobby list GUI.
 */
public class GameListController implements EventListener {
    @FXML
    private ListView<String> gameList;

    @FXML
    private Button refreshButton;

    @FXML
    private Button joinButton;

    @FXML
    private Text gameListText;

    @FXML
    private Button modeButton;

    private final SceneController sceneController;

    private final MethodHandler methodHandler;

    private boolean showRecentGames = false;

    public GameListController(SceneController sceneController, MethodHandler methodHandler) {
        this.sceneController = sceneController;
        this.methodHandler = methodHandler;
    }

    /**
     * Initialize the GameList.
     */
    public void initialize() {
        sendRequest();
        methodHandler.getBoardUpdater().subscribe(BoardEvent.LOBBY_LIST, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.RECENT_GAMES, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.CLIENT_ACK, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.FEEDBACK_SUCCESS, this);
    }

    /**
     * Enter the game.
     * @throws IOException
     */
    @FXML
    private void switchToGameScene() throws IOException {
        String item = gameList.getSelectionModel().getSelectedItem();
        if (item != null) {
            if (!showRecentGames) {
                int lobbyID = Integer.parseInt(item.substring(0, item.indexOf(" ")));
                methodHandler.enterGame(lobbyID);
                sceneController.showGameMenu();
            }
            else {
                System.out.println(gameList.getSelectionModel().getSelectedIndex());
                GameEnd gameEnd = new GameEnd(
                         methodHandler.getDataManager()
                            .getRecentGames()
                            .getLobbies()
                            .get(gameList.getSelectionModel().getSelectedIndex())
                            .getLobby()
                        ,methodHandler.getDataManager()
                            .getRecentGames()
                            .getLobbies()
                            .get(gameList.getSelectionModel().getSelectedIndex())
                            .getLeaderboard());
                methodHandler.getDataManager().processGameEnd(gameEnd);
                sceneController.showGameMenu();
            }
        }
    }

    /**
     * Go back to the server menu.
     * @throws IOException
     */
    @FXML
    private void switchToServerMenu() throws IOException {
        sceneController.showJoinServerMenu();
    }

    /**
     * Switch to showing the recent games or to showing the running/starting games.
     */
    @FXML
    private void switchMode() {
        if (!showRecentGames) {
            showRecentGames = true;
            modeButton.setText("Laufende Spiele");
            gameListText.setText("Vergangene Spiele");
            refreshList();
        }
        else {
            showRecentGames = false;
            modeButton.setText("Vergangene Spiele");
            gameListText.setText("Laufende Spiele");
            refreshList();
        }
        sendRequest();
    }

    /**
     * Get and show the current list of games.
     */
    public void refreshList() {
        if (!showRecentGames) {
            ArrayList<String> gameEntries = methodHandler.getGameListEntries(false);
            gameList.getItems().clear();
            if (gameEntries != null) {
                for (String gameEntry : gameEntries) {
                    gameList.getItems().add(gameEntry);
                }
            }
        }
        else {
            ArrayList<String> recentGameEntries = methodHandler.getGameListEntries(true);
            gameList.getItems().clear();
            if (recentGameEntries != null) {
                for (String gameEntry : recentGameEntries) {
                    gameList.getItems().add(gameEntry);
                }
            }
        }
    }

    /**
     * Send a lobbyListRequest to the server.
     */
    @FXML
    public void sendRequest(){
        if(!showRecentGames) {
            methodHandler.requestGameListEntries();
        }
        else {
            methodHandler.requestRecentGameListEntries(10);
        }
    }

    /**
     * Wait for the initial game state after joining a game.
     */
    public void toWaitingScreen(){
        gameListText.setText("Spiel wurde beigetreten. \n Bitte warten.");
        gameList.setVisible(false);
        refreshButton.setVisible(false);
        joinButton.setVisible(false);
        modeButton.setVisible(false);
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case LOBBY_LIST:
                refreshList();
                break;
            case RECENT_GAMES:
                refreshList();
                break;
            case FEEDBACK_SUCCESS:
                if(sceneController.getWaitingType() == WaitingType.ENTER_GAME_BOARD) {
                    toWaitingScreen();
                }
        }
    }
}
