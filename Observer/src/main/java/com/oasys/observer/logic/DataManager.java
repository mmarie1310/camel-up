package com.oasys.observer.logic;
import com.oasys.observer.communication.packets.*;
import com.oasys.observer.data.*;
import com.oasys.observer.data.FinalBet;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the data of the game.
 */
public class DataManager {
    private GameState gameState;

    private ArrayList<BoardSpace> currentBoardSpaces;
    private ArrayList<RolledDice>  rolledDices;
    private ArrayList<BoardSpace> boardSpaces;
    private ArrayList<Camel> camels;

    private BoardUpdater boardUpdater;
    private MethodHandler methodHandler;

    private int clientId;
    private LobbyList lobbyList;
    private RecentGames recentGames;
    private ArrayList<Player> players;
    private ArrayList<PlayerData> playerDataList;
    private int stageWinner = -3;
    private int stageSecond = -3;
    private int stageLoser = -3;
    private ArrayList<Player> stageScoringPlayers;
    private SuccessFeedback successFeedback;
    private ArrayList<Integer> leaderboard;
    private HashMap<FinalBet, Integer> winnerBetValues;
    private HashMap<FinalBet, Integer> loserBetValues;
    private Player lastPlayerActed;
    private RolledDice lastDiceRoll;
    private BoardSpace lastPlacedPlayerCard;
    private BettingCard lastPlacedStageBet;
    private GamePhase gamePhase;

    public DataManager(BoardUpdater boardUpdater, MethodHandler methodHandler) {

        this.boardUpdater = boardUpdater;
        this.methodHandler = methodHandler;

        boardSpaces = new ArrayList<BoardSpace>();
        playerDataList = new ArrayList<PlayerData>();
        leaderboard = new ArrayList<Integer>();
        winnerBetValues = new HashMap<>();
        loserBetValues = new HashMap<>();
    }

    /**
     * Save the current board spaces.
     */
    private void saveCurrentBoardSpaces() {
        currentBoardSpaces = gameState.getBoardSpaces();
    }

    /**
     * Searches for a board space.
     * @param spaceId The ID of the board space to search for.
     * @return The board space with the specified ID.
     */
    public BoardSpace getCurrentBoardSpace(int spaceId) {
        for (BoardSpace boardSpace : currentBoardSpaces) {
            if (boardSpace.getSpaceId() == spaceId) {
                return boardSpace;
            }
        }
        return null;
    }

    /**
     * Get the hex code of a camels color as a String.
     * @param camelId The ID of the camel.
     * @return The hex code of the camel's color.
     */
    public String getHexOfCamel(int camelId) {
        for (Camel camel : gameState.getGameConfig().getCamels()) {
            if (camel.getId() == camelId) {
                return camel.getColor();
            }
        }
        return null;
    }

    /**
     * Returns the color of a camel as a hexadecimal number.
     * @param camelId The camel whose color to search for.
     * @return the color of a camel. -1 if the camel doesn't exist.
     */
    public int getHexIntOfCamel(int camelId) {
        String hex = getHexOfCamel(camelId);
        if (hex != null) {
            return Integer.parseInt(hex.substring(1), 16);
        }
        return -1;
    }

    /**
     * Get the current rolled dices.
     * @return The current rolled dices.
     */
    public ArrayList<RolledDice> getCurrentRolledDice() {
        return gameState.getRolledDice();
    }

    /**
     * Get the board size.
     * @return The board size.
     */
    public int getBoardSize() {
        return gameState.getGameConfig().getNumberOfSpaces();
    }

    /**
     * Get the game config.
     * @return The game config.
     */
    public GameConfig getGameConfig() {
        return gameState.getGameConfig();
    }

    /**
     * Clear the data of a game after exiting.
     */
    public void clearGameBoard() {
        gameState = null;
        players = null;
        boardSpaces = new ArrayList<BoardSpace>();
        playerDataList = new ArrayList<PlayerData>();
        leaderboard = new ArrayList<Integer>();
        winnerBetValues = new HashMap<>();
        loserBetValues = new HashMap<>();
        stageWinner = -3;
        stageSecond = -3;
        stageLoser = -3;
        stageScoringPlayers = null;
        successFeedback = null;
        methodHandler.getGameTimer().stopTimer();
        lastPlayerActed = null;
        lastDiceRoll = null;
        lastPlacedPlayerCard = null;
        lastPlacedStageBet = null;
        gamePhase = null;
    }

    /**
     * Process a clientAck packet.
     * @param clientAck The clientAck packet to process.
     */
    public void processClientAck(ClientAck clientAck) {
        setClientId(clientAck.getClientId());
        boardUpdater.notify(BoardEvent.CLIENT_ACK);
    }

    /**
     * Process a gameState packet.
     * @param gameState The gameState packet to process.
     */
    public void processGameState(GameState gameState) {
        gamePhase = gameState.getGamePhase();
        players = gameState.getPlayers();

        // initial game state
        if (this.gameState == null) {
            this.gameState = gameState;
            stageScoringPlayers = gameState.getPlayers();
            methodHandler.createGameTimer();
            saveCurrentBoardSpaces();
            boardUpdater.notify(BoardEvent.BOARD_READY);
        }
        if (!this.gameState.getPlayers().isEmpty()) {
            lastPlayerActed = this.gameState.getPlayers().getFirst();
        }

        // synchronize time
        int moveTimeRemaining = (int) gameState.getMoveTimeRemaining();
        methodHandler.getGameTimer().setGameTimeLeft(gameState.getGameConfig().getMaxGameDuration() - (int) gameState.getGameDuration());
        if (moveTimeRemaining >= 0) {
            methodHandler.getGameTimer().setThinkingTimeLeft((int) gameState.getMoveTimeRemaining());
        }
        switch (gamePhase){
            case PLAYING:
                methodHandler.getGameTimer().setGameTimeRunning(true);
                methodHandler.getGameTimer().setThinkingTimeRunning(true);
                methodHandler.getGameTimer().setVisualizationTimeRunning(false);
                break;
            case VISUALIZING:
                methodHandler.getGameTimer().setGameTimeRunning(true);
                methodHandler.getGameTimer().setThinkingTimeRunning(false);
                methodHandler.getGameTimer().setVisualizationTimeRunning(true);
                break;
            case PAUSED:
                methodHandler.getBoardUpdater().notify(BoardEvent.PAUSE);
                case null, default:
                methodHandler.getGameTimer().setGameTimeRunning(false);
                methodHandler.getGameTimer().setThinkingTimeRunning(false);
                methodHandler.getGameTimer().setVisualizationTimeRunning(false);
        }

        // stage scoring
        if (gameState.getRolledDice().size() < this.gameState.getRolledDice().size()) {
            stageLoser = -3;
            for (BoardSpace space : gameState.getBoardSpaces()) {
                // find loser
                if(stageLoser <= -3 && !space.getCamelIds().isEmpty()){
                    if(space.getCamelIds().getFirst() >= 0){
                        stageLoser = space.getCamelIds().getFirst();
                    }
                }
                // find winner and second
                for(int c : space.getCamelIds()){
                    if(c >= 0 ) {
                        stageSecond = stageWinner;
                        stageWinner = c;
                    }
                }
            }
            stageScoringPlayers = this.gameState.getPlayers();
            methodHandler.getBoardUpdater().notify(BoardEvent.STAGE_SCORING);
        }

        // dice roll
        if (gameState.getRolledDice().size() > this.gameState.getRolledDice().size()) {
            lastDiceRoll = gameState.getRolledDice().getLast();
            boardUpdater.notify(BoardEvent.DICE_ROLL);
        }

        // player cards
        for (int i = 0; i < gameState.getBoardSpaces().size(); i++) {
            if (gameState.getBoardSpaces().get(i).getPlayerCard() != null && this.gameState.getBoardSpaces().get(i).getPlayerCard() == null) {
                lastPlacedPlayerCard = gameState.getBoardSpaces().get(i);
                methodHandler.getBoardUpdater().notify(BoardEvent.PLAYER_CARD);
            }
        }

        // stage bet
        if(!gameState.getPlayers().isEmpty() && !this.gameState.getPlayers().isEmpty()) {
            if (gameState.getPlayers().getFirst().getBettingCards().size() > this.gameState.getPlayers().getFirst().getBettingCards().size() && gamePhase == GamePhase.VISUALIZING) {
                lastPlacedStageBet = gameState.getPlayers().getFirst().getBettingCards().getLast();
                methodHandler.getBoardUpdater().notify(BoardEvent.STAGE_BET);
            }
        }

        // final bet winner
        if (gameState.getFinalBets().firstCamel.size() > this.gameState.getFinalBets().firstCamel.size()){
            methodHandler.getBoardUpdater().notify(BoardEvent.WINNER_BET);
        }

        // final bet loser
        if (gameState.getFinalBets().lastCamel.size() > this.gameState.getFinalBets().lastCamel.size()){
            methodHandler.getBoardUpdater().notify(BoardEvent.LOSER_BET);
        }

        // synchronize player data
        setPlayers(gameState.getPlayers());
        for(int i = 0; i < players.size(); i++){
            Player player = players.get(i);
            boolean alreadyExists = false;
            for(PlayerData playerData : playerDataList){
                if(playerData.getPlayerIdFromServer() == player.getPlayerId()){
                    alreadyExists = true;
                    playerData.setCoins(player.getMoney());
                    playerData.setTurnOrder(i+1);
                    playerData.setHasPlacedTile(hasPlacedPlayerCard(player.getPlayerId(), gameState));
                    playerData.setWinnerBetCount(getFinalBetCount(player.getPlayerId(), true, gameState));
                    playerData.setLoserBetCount(getFinalBetCount(player.getPlayerId(), false, gameState));
                }
            }
            if(!alreadyExists){
                playerDataList.add(
                        new PlayerData(
                            player.getPlayerId(),
                            player.getName(),
                            player.getMoney(),
                            getFinalBetCount(player.getPlayerId(), true, gameState),
                            getFinalBetCount(player.getPlayerId(), false, gameState),
                            i+1,
                            hasPlacedPlayerCard(player.getPlayerId(), gameState),
                            methodHandler
                        )
                );
            }
        }
        boardUpdater.notify(BoardEvent.PLAYERS_UPDATED);

        this.gameState = gameState;
        saveCurrentBoardSpaces();

        boardUpdater.notify(BoardEvent.ROUND_FINISH);
        boardUpdater.notify(BoardEvent.CAMEL);
    }

    public void processLobbyList(LobbyList lobbyList) {
        setLobbyList(lobbyList);
        boardUpdater.notify(BoardEvent.LOBBY_LIST);
    }

    public void processRecentGames(RecentGames recentGames) {
        setRecentGames(recentGames);
        boardUpdater.notify(BoardEvent.RECENT_GAMES);
    }

    public void processSuccessFeedback(SuccessFeedback successFeedback) {
        this.successFeedback = successFeedback;
        if(successFeedback.isSuccess()){
            methodHandler.getBoardUpdater().notify(BoardEvent.FEEDBACK_SUCCESS);
        }
        else {
            System.err.println("Error: " + successFeedback.getError());
            methodHandler.getBoardUpdater().notify(BoardEvent.FEEDBACK_FAILED);
        }
    }

    /**
     * Process a gameEnd packet.
     * @param gameEnd The gameEnd packet to process.
     */
    public void processGameEnd(GameEnd gameEnd) {

        processGameState(gameEnd.getLobby().getGameState());
        leaderboard = gameEnd.getLeaderboard();
        calculateFinalBetValues(gameEnd.getLobby().getGameState().getFinalBets().firstCamel, gameEnd.getLobby().getGameState().getFinalBets().lastCamel);
        methodHandler.getBoardUpdater().notify(BoardEvent.GAME_FINISH);
    }

    /**
     * Calculate the final bet values and save them in a hashmap.
     * @param winnerBets The final winner bets.
     * @param loserBets The final loser bets.
     */
    public void calculateFinalBetValues(ArrayList<FinalBet> winnerBets, ArrayList<FinalBet> loserBets){
        HashMap<Integer, Integer> camelCount = new HashMap<>();
        for(FinalBet bet : winnerBets){
            if (!camelCount.containsKey(bet.getCamelId())) {
                camelCount.put(bet.getCamelId(), 1);
            }
            else {
                camelCount.put(bet.getCamelId(), camelCount.get(bet.getCamelId()) + 1);
            }
            if (bet.getCamelId() == stageWinner) {
                if (!winnerBetValues.containsKey(bet)) {
                    switch (camelCount.get(bet.getCamelId())) {
                        case 0:
                            break;
                        case 1:
                            winnerBetValues.put(bet, 8);
                            break;
                        case 2:
                            winnerBetValues.put(bet, 5);
                            break;
                        case 3:
                            winnerBetValues.put(bet, 3);
                            break;
                        default:
                            winnerBetValues.put(bet, 1);
                            break;
                    }
                }
            }
            else {
                if (!winnerBetValues.containsKey(bet)) {
                    winnerBetValues.put(bet, -1);
                }
            }
        }

        camelCount = new HashMap<>();
        for(FinalBet bet : loserBets) {
            if (!camelCount.containsKey(bet.getCamelId())) {
                camelCount.put(bet.getCamelId(), 1);
            } else {
                camelCount.put(bet.getCamelId(), camelCount.get(bet.getCamelId()) + 1);
            }
            if (bet.getCamelId() == stageLoser) {
                if (!loserBetValues.containsKey(bet)) {
                    switch (camelCount.get(bet.getCamelId())) {
                        case 0:
                            break;
                        case 1:
                            loserBetValues.put(bet, 8);
                            break;
                        case 2:
                            loserBetValues.put(bet, 5);
                            break;
                        case 3:
                            loserBetValues.put(bet, 3);
                            break;
                        default:
                            loserBetValues.put(bet, 1);
                            break;
                    }
                }
            }
            else {
                if (!loserBetValues.containsKey(bet)) {
                    loserBetValues.put(bet, -1);
                }
            }
        }
    }

    /**
     * Get the number of final bets a player has placed on either winning or losing camels.
     * @param playerId The player whose final bets to search for.
     * @param winner If true, search for bets on winning camels. Otherwise, search for bets on losing camels.
     * @param gameState The game state to search in.
     * @return The amount of bets the player has placed on either winning or losing camels.
     */
    public int getFinalBetCount(int playerId, boolean winner, GameState gameState){
        int betCount = 0;
        if(winner) {
            for (FinalBet finalBet : gameState.getFinalBets().getFirstCamel()) {
                if(finalBet.getPlayerId() == playerId){
                    betCount++;
                }
            }
        }
        else {
            for (FinalBet finalBet : gameState.getFinalBets().getLastCamel()) {
                if(finalBet.getPlayerId() == playerId){
                    betCount++;
                }
            }
        }
        return betCount;
    }

    /**
     * Check if the player has placed a player card.
     * @param playerId The ID of the player.
     * @param gameState The game state to search in.
     * @return True, if the player has placed a player card in the current game state.
     */
    public boolean hasPlacedPlayerCard(int playerId, GameState gameState){
        boolean placedPlayerCard = false;
        for(BoardSpace boardSpace : gameState.getBoardSpaces()) {
            if(boardSpace.getPlayerCard() != null){
                if(boardSpace.getPlayerCard().getPlayerId() == playerId){
                    placedPlayerCard = true;
                }
            }
        }
        return placedPlayerCard;
    }

    /**
     * Find the player object by their ID.
     * @param playerId The ID of the player.
     * @return The player with matching ID or null.
     */
    public Player getPlayerById(int playerId){
        for(Player player : players){
            if(player.getPlayerId() == playerId){
                return player;
            }
        }
        return null;
    }

    /**
     * Get the clientId.
     * @return The clientId.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Set the clientId.
     * @param clientId The clientId to set to.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Get the lobbyList.
     * @return The lobbyList.
     */
    public LobbyList getLobbyList() {
        return lobbyList;
    }

    /**
     * Set the lobbyList.
     * @param lobbyList The lobbyList to set to.
     */
    public void setLobbyList(LobbyList lobbyList) {
        this.lobbyList = lobbyList;
    }

    /**
     * Get the recent games.
     * @return The recentGames.
     */
    public RecentGames getRecentGames() {
        return recentGames;
    }

    /**
     * Set the recentGames.
     * @param recentGames The recent games to set to.
     */
    public void setRecentGames(RecentGames recentGames) {
        this.recentGames = recentGames;
    }

    /**
     * Get the players.
     * @return The players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Set the players.
     * @param players The players to set to.
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Get the stageWinner.
     * @return The stage winner.
     */
    public int getStageWinner() {
        return stageWinner;
    }

    /**
     * Get the stageSecond.
     * @return The stage second.
     */
    public int getStageSecond() {
        return stageSecond;
    }

    /**
     * Get the stageLoser.
     * @return The stageLoser.
     */
    public int getStageLoser() {
        return stageLoser;
    }

    /**
     * Get the playerDataList.
     * @return The playerDataList.
     */
    public ArrayList<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    /**
     * Get the turn of the current game state.
     * @return the current turn of the game state.
     */
    public int getTurn() {
        return gameState.getTurns();
    }

    /**
     * Get the stageScoringPlayers.
     * @return The stageScoringPlayers.
     */
    public ArrayList<Player> getStageScoringPlayers() {
        return stageScoringPlayers;
    }

    /**
     * Get the leaderboard.
     * @return The leaderboard.
     */
    public ArrayList<Integer> getLeaderboard() {
        return leaderboard;
    }

    /**
     * Get the winner bets.
     * @return The winner bets.
     */
    public ArrayList<FinalBet> getWinnerBets() {
        return gameState.getFinalBets().getFirstCamel();
    }

    /**
     * Get the loser bets.
     * @return The loser bets.
     */
    public ArrayList<FinalBet> getLoserBets() {
        return gameState.getFinalBets().getLastCamel();
    }

    /**
     * Get the winner bet values.
     * @return The winner bet values.
     */
    public HashMap<FinalBet, Integer> getWinnerBetValues() {
        return winnerBetValues;
    }

    /**
     * Get the loser bet values.
     * @return The loser bet values.
     */
    public HashMap<FinalBet, Integer> getLoserBetValues() {
        return loserBetValues;
    }

    /**
     * Get the last dice roll.
     * @return The last dice roll.
     */
    public RolledDice getLastDiceRoll() {
        return lastDiceRoll;
    }

    /**
     * Get the LastPlayerActed.
     * @return The lastPlayerActed.
     */
    public Player getLastPlayerActed() {
        return lastPlayerActed;
    }

    /**
     * Get the lastPlacedStageBet.
     * @return The lastPlacedStageBet.
     */
    public BettingCard getLastPlacedStageBet() {
        return lastPlacedStageBet;
    }

    /**
     * Get the current gamePhase.
     * @return The current gamePhase.
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
