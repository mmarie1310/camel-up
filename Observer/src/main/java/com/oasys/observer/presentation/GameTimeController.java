package com.oasys.observer.presentation;

import com.oasys.observer.data.GamePhase;
import com.oasys.observer.logic.BoardEvent;
import com.oasys.observer.logic.EventListener;
import com.oasys.observer.logic.MethodHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

/**
 * This class controls the shown timers, turn indicator and the message box
 */
public class GameTimeController implements EventListener {
    private final MethodHandler methodHandler;

    @FXML
    private Label gameTime;

    @FXML
    private Label moveTime;

    @FXML
    private Label roundNumber;

    @FXML
    private Text messageText;

    public GameTimeController(MethodHandler methodHandler) {
        this.methodHandler = methodHandler;
        methodHandler.getBoardUpdater().subscribe(BoardEvent.GAME_TIME, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.THINKING_TIME, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.ROUND_FINISH, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.GAME_FINISH, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.DICE_ROLL, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.PAUSE, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.PLAYER_CARD, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.STAGE_BET, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.WINNER_BET, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.LOSER_BET, this);
        }

    /**
     * Set the message text back to its default style.
     */
    public void returnToDefaultStyle(){
        messageText.setFill(Paint.valueOf("#f5bf93"));
        messageText.setStrokeWidth(0);
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case GAME_TIME:
                if (gameTime != null) {
                        Platform.runLater(() ->
                                gameTime.setText(methodHandler.getGameTimer().getGameTimeLeftAsString())
                        );
                }
                break;
            case THINKING_TIME:
                if (moveTime != null) {
                        Platform.runLater(() ->
                                moveTime.setText(methodHandler.getGameTimer().getThinkingTimeLeftAsString())
                        );
                }
                break;
            case ROUND_FINISH:
                if (roundNumber != null) {
                    roundNumber.setText(Integer.toString(methodHandler.getDataManager().getTurn()));
                }
                break;
            case GAME_FINISH:
                returnToDefaultStyle();
                messageText.setText("Spiel beendet\n" + methodHandler.getDataManager().getPlayerById(methodHandler.getDataManager().getLeaderboard().getFirst()) + " gewinnt");
                break;
            case DICE_ROLL:
                String camelHexDice = methodHandler.getDataManager().getHexOfCamel(methodHandler.getDataManager().getLastDiceRoll().getCamelId());
                messageText.setFill(Paint.valueOf(camelHexDice));
                messageText.setStroke(Paint.valueOf("BLACK"));
                messageText.setStrokeWidth(1);
                messageText.setText(methodHandler.getDataManager().getLastPlayerActed().getName() + " würfelt eine " + methodHandler.getDataManager().getLastDiceRoll().getNumber() + " für Kamel " + camelHexDice);
                break;
            case PAUSE:
                returnToDefaultStyle();
                messageText.setText("Spiel pausiert");
                break;
            case PLAYER_CARD:
                returnToDefaultStyle();
                messageText.setText(methodHandler.getDataManager().getLastPlayerActed().getName() + " legt ein Zuschauerplättchen");
                break;
            case STAGE_BET:
                String camelHexBet = methodHandler.getDataManager().getHexOfCamel(methodHandler.getDataManager().getLastPlacedStageBet().getCamelId());
                messageText.setFill(Paint.valueOf(camelHexBet));
                messageText.setStrokeWidth(1);
                messageText.setText(methodHandler.getDataManager().getLastPlayerActed().getName() + " wettet auf Kamel " + camelHexBet);
                break;
            case WINNER_BET:
                returnToDefaultStyle();
                messageText.setText(methodHandler.getDataManager().getLastPlayerActed().getName() + " setzt eine Gewinner-Zielkarte");
                break;
            case LOSER_BET:
                returnToDefaultStyle();
                messageText.setText(methodHandler.getDataManager().getLastPlayerActed().getName() + " setzt eine Verlierer-Zielkarte");
        }
    }
}
