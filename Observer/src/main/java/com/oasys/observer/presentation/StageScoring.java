package com.oasys.observer.presentation;

import com.oasys.observer.data.BettingCard;
import com.oasys.observer.data.FinalBet;
import com.oasys.observer.data.Player;
import com.oasys.observer.logic.BoardEvent;
import com.oasys.observer.logic.EventListener;
import com.oasys.observer.logic.MethodHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This class controls the graphical stage scoring and final scoring tables.
 */
public class StageScoring implements EventListener {
    private final MethodHandler methodHandler;

    @FXML
    private GridPane stageScoringGrid;
    @FXML
    private AnchorPane stageScoringAnchor;

    private Label[] camelRank;
    @FXML
    private Label stageWinner;
    @FXML
    private Label stageSecond;
    @FXML
    private Label stageLoser;

    @FXML
    private Text betLabel;

    private Label[] names;
    @FXML
    private Label name1;
    @FXML
    private Label name2;
    @FXML
    private Label name3;
    @FXML
    private Label name4;
    @FXML
    private Label name5;
    @FXML
    private Label name6;

    private ListView<String>[] countList;
    @FXML
    private ListView<String> countList1;
    @FXML
    private ListView<String> countList2;
    @FXML
    private ListView<String> countList3;
    @FXML
    private ListView<String> countList4;
    @FXML
    private ListView<String> countList5;
    @FXML
    private ListView<String> countList6;
    @FXML
    private Label[] moneyPlayer;
    @FXML
    private Label moneyPlayer1;
    @FXML
    private Label moneyPlayer2;
    @FXML
    private Label moneyPlayer3;
    @FXML
    private Label moneyPlayer4;
    @FXML
    private Label moneyPlayer5;
    @FXML
    private Label moneyPlayer6;

    private boolean finalScoring = false;

    /**
     * Constructs the stage scoring table.
     * @param methodHandler
     */
    public StageScoring(MethodHandler methodHandler){
        this.methodHandler = methodHandler;
        methodHandler.getBoardUpdater().subscribe(BoardEvent.STAGE_SCORING, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.GAME_FINISH, this);
    }

    /**
     * Initializes the stage scoring table.
     */
    @FXML
    public void initialize() {
        camelRank = new Label[]{stageWinner, stageSecond, stageLoser};
        names = new Label[]{name1, name2, name3, name4, name5, name6};
        countList = new ListView[]{countList1, countList2, countList3, countList4, countList5, countList6};
        moneyPlayer = new Label[]{moneyPlayer1, moneyPlayer2, moneyPlayer3, moneyPlayer4, moneyPlayer5, moneyPlayer6};

        // set initial values
        refreshContent();

        // set CellFactory for the betting lists
        for (int i = 0; i<6; i++){
            countList[i].setCellFactory(countList -> new StageBet());
        }


        // Assign pressing "E" to toggling the view of the table
        stageScoringAnchor.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.E) {
                toggleStageTableVisible();
            }
        });
        toggleStageTableVisible();
    }


    /**
     * Refresh all labels and lists to the current state of the game.
     */
    public void refreshContent() {
            for(int i = 0; i < 6; i++) {
                countList[i].getItems().clear();
                if (!finalScoring) {
                    if (methodHandler.getDataManager().getStageScoringPlayers().size() > i) {
                        Player player = methodHandler.getDataManager().getStageScoringPlayers().get(i);
                        if (player != null) {
                            // player labels
                            names[i].setText(player.getName());
                            // stage bets
                            int moneyWon = 0;
                            for (BettingCard bet : player.getBettingCards()) {
                                addBet(i, methodHandler.getDataManager().getHexIntOfCamel(bet.getCamelId()), methodHandler.calculateBetCoins(bet), 0);
                                moneyWon = moneyWon + methodHandler.calculateBetCoins(bet);
                            }
                            //  amount of money
                            setMoney(i, moneyWon);
                        }
                    } else {
                        // default text
                        names[i].setText("---");
                    }
                }
                else
                {
                    // final scoring
                    if (methodHandler.getDataManager().getLeaderboard().size() > i) {
                        Player player = methodHandler.getDataManager().getPlayerById(methodHandler.getDataManager().getLeaderboard().get(i));
                        if (player != null) {
                            // player labels with rank
                            names[i].setText(Integer.toString(i+1) + ". "  + player.getName());
                            // stage bets
                            for (BettingCard bet : player.getBettingCards()) {
                                addBet(i, methodHandler.getDataManager().getHexIntOfCamel(bet.getCamelId()), methodHandler.calculateBetCoins(bet), 0);
                            }
                            // winner bets
                            for (FinalBet winnerBet : methodHandler.getDataManager().getWinnerBets()) {
                                if(winnerBet.getPlayerId() == player.getPlayerId()) {
                                    if (methodHandler.getDataManager().getWinnerBetValues().get(winnerBet) != null) {
                                        addBet(i, 0, 0, 1);
                                    }
                                }
                            }
                            // loser bets
                            for (FinalBet loserBet : methodHandler.getDataManager().getLoserBets()) {
                                if(loserBet.getPlayerId() == player.getPlayerId()) {
                                    if (methodHandler.getDataManager().getLoserBetValues().get(loserBet) != null) {
                                        addBet(i, 0, 0, 2);
                                    }
                                }
                            }
                            // final amount of money
                            setMoney(i, player.getMoney());
                        }
                    }
                }
                    // camel ranking
                    int winner = methodHandler.getDataManager().getStageWinner();
                    int second = methodHandler.getDataManager().getStageSecond();
                    int loser = methodHandler.getDataManager().getStageLoser();
                    if (winner >= 0) {
                        setCamelRank(0, methodHandler.getDataManager().getHexIntOfCamel(winner));
                    }
                    if (second >= 0) {
                        setCamelRank(1, methodHandler.getDataManager().getHexIntOfCamel(second));
                    }
                    if (loser >= 0) {
                        setCamelRank(2, methodHandler.getDataManager().getHexIntOfCamel(loser));
                    }
        }

    }

    /**
     * Toggles the visibility of the scoring table.
     */
    @FXML
    public void toggleStageTableVisible(){
        if (stageScoringAnchor.getScaleX() > 0) {
            setStageTableVisible(false);
        }
        else {
            setStageTableVisible(true);
        }
    }

    /**
     * Sets the visibility of the scoring table.
     * @param visible If true, the scoring table will be shown. Otherwise it will be hidden.
     */
    public void setStageTableVisible(boolean visible){
        if (!visible) {
            stageScoringAnchor.setScaleX(0);
            stageScoringAnchor.setScaleY(0);
            stageScoringAnchor.setMouseTransparent(true);
        }
        else {
            stageScoringAnchor.setScaleX(1);
            stageScoringAnchor.setScaleY(1);
            stageScoringAnchor.setMouseTransparent(false);
        }
    }

    /**
     *   Changes the labels for the placements of the camels. Rank 0: Winner, Rank 1: Second, Rank 2: Loser
     */
    public void setCamelRank(int rank, int color) {
        String colorHex = String.format("#%06X", color);
        camelRank[rank].setStyle("-fx-border-color: " + colorHex + ";" + "-fx-border-width: 2;" + "-fx-background-color: " + "white" + ";");
        camelRank[rank].setText(colorHex);
    }

    /**
     * Set the final amount of money shown on the scoring table.
     * @param player The player whose amount of money gets set.
     * @param value The amount of money that gets set.
     */
    public void setMoney(int player, int value){
        if (value >= 0 && !finalScoring) {
            moneyPlayer[player].setText("+" + String.valueOf(value));
        }
        else if (value < 0 && !finalScoring) {
            moneyPlayer[player].setText(String.valueOf(value));
        }
        else {
            moneyPlayer[player].setText(String.valueOf(value));
        }
    }

    /**
     * Adds a bet to the list.
     * @param player The player the bet belongs to.
     * @param color The color of the camel of the bet.
     * @param value The value of the bet.
     * @param betType The type of the bet. 0: Stage bet,    1: Final Bet (Winner),  2: Final Bet(Loser)
     */
    public void addBet(int player, int color, int value, int betType){
        String colorHex = String.format("#%06X", color);
        if (!finalScoring) {
            countList[player].getItems().add(colorHex + " " + String.valueOf(value));
        }
        else {
            countList[player].getItems().add(colorHex + " " + String.valueOf(value) + " " + Integer.toString(betType));
        }
    }

    /**
     * Switch to final scoring mode.
     */
    public void toGameEnd() {
        betLabel.setText("SIEGERREIHENFOLGE UND WETTEN");
        finalScoring = true;
        // change CellFactory for the betting lists
        for (int i = 0; i<6; i++){
            countList[i].getItems().clear();
            countList[i].setCellFactory(countList -> new AllBets());
        }
        refreshContent();
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case STAGE_SCORING:
                refreshContent();
                setStageTableVisible(true);
                break;
            case GAME_FINISH:
                toGameEnd();
        }
    }

    /**
     * The style of a stage bet entry.
     */
    static class StageBet extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Label colorLabel = new Label();
            Label valueLabel = new Label();
            VBox bet = new VBox();
            Rectangle rectangle = new Rectangle(20,10);
            String colorValue;
            String betValue;
            if (item != null) {
                colorValue = item.substring(0, item.indexOf(" "));
                betValue = item.substring(item.indexOf(" ") + 1);
                rectangle.setFill(Color.web(colorValue));
                bet.getChildren().add(rectangle);
                colorLabel.setText(colorValue);
        //        colorLabel.setStyle("-fx-border-color: " + colorValue + ";" + "-fx-border-width: 2;" + "-fx-background-color: " + "white" + ";");
                bet.getChildren().add(colorLabel);
                valueLabel.setText(betValue);
                bet.getChildren().add(valueLabel);
                setGraphic(bet);
            }


        }
    }

    /**
     * The style of a stage bet entry.
     */
    static class AllBets extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            Label colorLabel = new Label();
            Label valueLabel = new Label();
            Label typeLabel = new Label();
            VBox bet = new VBox();
            Rectangle rectangle = new Rectangle(20,10);
            String colorValue;
            String betValue;
            String betType;
            if (item != null) {
                colorValue = item.substring(0, item.indexOf(" "));
                betValue = item.substring(item.indexOf(" ") + 1, item.lastIndexOf(" "));
                betType = item.substring(item.lastIndexOf(" ") + 1);
                switch (betType) {
                    case "0":
                        typeLabel.setText("Etappenwette:");
                        break;
                    case "1":
                        typeLabel.setText("Gewinnerwette");
                        break;
                    case "2":
                        typeLabel.setText("Verliererwette");
                        break;
                    default:
                        break;
                }
                item = item.substring(0, item.lastIndexOf(" "));
                bet.getChildren().add(typeLabel);
                if (betType.equals("0")) {
                    rectangle.setFill(Color.web(colorValue));
                    bet.getChildren().add(rectangle);
                    colorLabel.setText(colorValue);
                    bet.getChildren().add(colorLabel);
                    valueLabel.setText(betValue);
                    bet.getChildren().add(valueLabel);
                }
                setGraphic(bet);
            }
        }
    }
}
