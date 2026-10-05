package com.oasys.engine.logic;


import com.oasys.engine.communication.Communication;
import com.oasys.engine.communication.PacketManager;
import com.oasys.engine.communication.packets.GameState;
import com.oasys.engine.data.GamePhase;


/**
 * This class manages the important data of an engine participant. It calls making a move depending on if it's the player's
 * turn or not.
 */
public class DataManager {
    private static DataManager dataManagerInstance;

    private int currentLobby = -1;
    private boolean myTurn = false;
    private boolean visualizing = false;
    private GameState gameState;

    private DataManager() {}

    /**
     * Creates a unique instance of the class DataManager.
     * @return object of class DataManager.
     */
    public static synchronized DataManager getInstance(){
        if (DataManager.dataManagerInstance == null){
            DataManager.dataManagerInstance = new DataManager();
        }
        return DataManager.dataManagerInstance;
    }

    /**
     * Gets the game state.
     * @return game state.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Sets game state and changes the value of turn depending on the current turn.
     * @param newGameState
     */
    public void setGameState(GameState newGameState) {
        this.gameState = newGameState;

        this.checkTurn(newGameState);
        this.checkVisualizing(newGameState);
    }

    public int getCurrentLobby() {
        return this.currentLobby;
    }

    public void setCurrentLobby(int currentLobby) {
        if (this.currentLobby != currentLobby) {
            this.gameState = null; // invalidate current game state
        }
        this.currentLobby = currentLobby;
    }

    /**
     * Updates whether it is the turn of the engine participant or not.
     * @param gameState the new received game state.
     */
    public void checkTurn(GameState gameState) {
        int clientId = Communication.getInstance().getClientId();
        int currentPlayerId = gameState.getPlayers().getFirst().getPlayerId();

        // make sure to only send one turn (even if multiple updates during the engine's turn)
        if (!this.myTurn) {
            // previously not the engine participant's turn
            if ((gameState.getGamePhase() == GamePhase.PLAYING) && (currentPlayerId == clientId)) {
                // now it's the engine participant's turn
                TurnManager.getInstance().makeTurn(gameState);
                this.myTurn = true;
            }
        } else if ((gameState.getGamePhase() != GamePhase.PLAYING) || (currentPlayerId != clientId)) {
            // previously was engine participant's turn but now not anymore
            this.myTurn = false;
        }
    }

    /**
     * Manages the visualizing time by sending MoveVisualized Packet and updating visualizing.
     * @param gameState
     */
    public void checkVisualizing(GameState gameState) {
        // make sure to only send one moveVisualized (even if multiple updates during the visualization)
        if (!this.visualizing) {
            if (gameState.getGamePhase() == GamePhase.VISUALIZING) {
                PacketManager.getInstance().sendMoveVisualized();
                this.visualizing = true;
            }
        } else if (gameState.getGamePhase() != GamePhase.VISUALIZING) {
            this.visualizing = false;
        }
    }
}
