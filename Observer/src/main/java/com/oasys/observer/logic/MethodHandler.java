package com.oasys.observer.logic;

import com.oasys.observer.communication.packets.*;
import com.oasys.observer.data.*;

import java.util.ArrayList;

/**
 * This class connects most classes. Passed as a reference to most classes.
 */
public class MethodHandler {
    private ClientConnector connector;

    private GameTimer gameTimer;

    private BoardUpdater boardUpdater;

    private DataManager dataManager;

    private ClientConnector clientConnector;

    public MethodHandler() {
        boardUpdater = new BoardUpdater();
        dataManager = new DataManager(boardUpdater, this);
    }

    /**
     * Creates the timer.
     */
    public void createGameTimer(){
        GameConfig gameConfig = dataManager.getGameConfig();
        int thinkingTime = gameConfig.getThinkingTime();
        int visualizationTime = gameConfig.getVisualizationTime();
        int maxGameDuration = gameConfig.getMaxGameDuration();

        gameTimer = new GameTimer(thinkingTime, visualizationTime, maxGameDuration, this);
        gameTimer.StartGameLoop();
    }

    /**
     * Sends a request to the server to get the lobby list items.
     */
    public void requestGameListEntries() {
        RequestLobbyList requestLobbyList = new RequestLobbyList();
        clientConnector.getServerHandler().sendMessage(requestLobbyList.convertTojson());
    }

    /**
     * Send a request to the server to get the recent finished games.
     * @param entries The amount of recent games to request
     */
    public void requestRecentGameListEntries(int entries) {
        RequestRecentGames requestRecentGames = new RequestRecentGames(entries);
        clientConnector.getServerHandler().sendMessage(requestRecentGames.convertTojson());
    }

    /**
     * Get the number of board spaces.
     * @return The number of board spaces.
     */
    public int getNumberOfSpaces() {
        return dataManager.getBoardSize();
    }

    /**
     * Prepares data to be sent to the game list as a presentable String.
     * @param lobbyID Unique identifier of a lobby.
     * @param name Display name of the lobby.
     * @param currPlayer The current amount of players in the lobby.
     * @param maxPlayer The maximum amount of players in the lobby.
     * @param status The status of the lobby.
     */
    private String createGameListString(int lobbyID, String name, int currPlayer, int maxPlayer, String status) {
        return lobbyID + "   -   " + name + "   -   (" + currPlayer + "/" + maxPlayer + ")   -   " + status;
    }

    /**
     * Get all games from the data layer as presentable strings.
     * @param showRecentGames If true, returns the recent games instead.
     * @return The game list entries as presentable strings.
     */
    public ArrayList<String> getGameListEntries(boolean showRecentGames) {
        ArrayList<String> gameListEntries;
        gameListEntries = new ArrayList<>();
        LobbyList lobbyList;
        RecentGames recentGames;
        if(!showRecentGames) {
            if ((lobbyList = dataManager.getLobbyList()) != null) {
                for (Lobby lobby : lobbyList.getLobbies()) {
                    gameListEntries.add(
                            createGameListString(
                                    lobby.getLobbyId(),
                                    lobby.getName(),
                                    lobby.getGameState().getPlayers().size(),
                                    lobby.getGameState().getGameConfig().getPlayerCount(),
                                    lobby.getGameState().getGamePhase().toString()
                            )
                    );
                }
            }
        }
        else {
            if ((recentGames = dataManager.getRecentGames()) != null) {
                for (RecentGame lobby : recentGames.getLobbies()) {
                    gameListEntries.add(
                            createGameListString(
                                    lobby.getLobby().getLobbyId(),
                                    lobby.getLobby().getName(),
                                    lobby.getLobby().getGameState().getPlayers().size(),
                                    lobby.getLobby().getGameState().getGameConfig().getPlayerCount(),
                                    lobby.getLobby().getGameState().getGamePhase().toString()
                            )
                    );
                }
            }
        }
        return gameListEntries;
    }

    /**
     * Enter a lobby as an observer.
     * @param lobbyID The lobby to enter.
     */
    public void enterGame(int lobbyID) {
        if (clientConnector != null) {
            JoinLobby joinLobby = new JoinLobby(lobbyID, false);
            clientConnector.getServerHandler().sendMessage(joinLobby.convertTojson());
        }
    }

    /**
     * Get the camels from a specified field.
     * @param spaceId The ID of the BoardSpace to search in.
     * @return The camels in BoardSpace wit the ID spaceID.
     */
    public ArrayList<Camel> getCamelsInField(int spaceId) {
        ArrayList<Camel> camels = new ArrayList<>();
        BoardSpace boardSpace = dataManager.getCurrentBoardSpace(spaceId);
        if (boardSpace != null) {
            for (int camelId : boardSpace.getCamelIds()) {
                System.out.println(camelId);
                camels.add(new Camel(camelId, "#FF0000"));
            }
        }
        return camels;
    }

    /**
     * Get the spectator tile from a specified field
     * @param spaceId The ID of the BoardSpace to search in
     * @return
     */
    public PlayerCard getSpectatorTile(int spaceId) {
        BoardSpace boardSpace = dataManager.getCurrentBoardSpace(spaceId);
        if (boardSpace != null) {
            if (boardSpace.getPlayerCard() != null) {
                return boardSpace.getPlayerCard();
            }
            return null;
        }
        return null;
    }

    public ArrayList<RolledDice> getAllRolledDice() {
        return dataManager.getCurrentRolledDice();
    }

    public String getHexColorOfCamel(int camelId) {
        return dataManager.getHexOfCamel(camelId);
    }

    public void moveCamel(int camelId, int value) {
        // TODO:
    }

    /**
     * Returns the coin value of a bet after a stage.
     * @param bet The bet whose coin value to calculate.
     * @return The value of a stage bet after a stage.
     */
    public int calculateBetCoins(BettingCard bet) {
        int coinsWon = -1;
        if (getDataManager().getStageWinner() == bet.getCamelId()) {
            coinsWon = bet.getWorth();
        } else if (getDataManager().getStageSecond() == bet.getCamelId()) {
            coinsWon = 1;
        }
        return coinsWon;
    }

    /**
     * Get the board updater.
     * @return The board updater.
     */
    public BoardUpdater getBoardUpdater() {
        return boardUpdater;
    }

    /**
     * Get the game timer.
     * @return The game timer.
     */
    public GameTimer getGameTimer() {
        return gameTimer;
    }

    /**
     * Get the data manager.
     * @return The data manager.
     */
    public DataManager getDataManager() { return dataManager; }

    /**
     * Get the client connector.
     * @return The client connector.
     */
    public ClientConnector getClientConnector() { return clientConnector; }

    /**
     * Set the client connector.
     * @param clientConnector The client connector to set to.
     */
    public  void setClientConnector(ClientConnector clientConnector) { this.clientConnector = clientConnector; }
}

