package com.oasys.observer.presentation;
import com.oasys.observer.communication.packets.JoinLobby;
import com.oasys.observer.data.PlayerCard;
import com.oasys.observer.logic.*;
import com.oasys.observer.data.*;

import de.articdive.jnoise.JNoise;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game board.
 */
public class BoardPane implements EventListener {

    private final Stage stage;

    private final MethodHandler methodHandler;

    private final SceneController sceneController;

    private Image image;

    private Image pyramidImage;

    private Image carpetsImage;

    private Image die_carpetImage;

    /**
     * The HBox of the placer cards.
     */
    @FXML
    private Pane playerCardBox;

    /**
     * The HBox of the timer view.
     */
    @FXML
    private Pane timerBox;

    /**
     * The VBox of the carpets.
     */
    @FXML
    private Pane carpetsBox;

    /**
     * The anchor of the board.
     */
    @FXML
    private Pane anchorPane;

    /**
     * The pane for every game-related content.
     */
    @FXML
    private Pane gamePane;

    /**
     * The button for toggling the stage score view.
     */
    @FXML
    private Button toggleStageScore;

    /**
     * The button for returning to the lobby.
     */
    @FXML
    private Button exitButton;

    /**
     * The pane for scoring views.
     */
    @FXML
    private Pane scorePane;

    /**
     * The pane including all individual board fields.
     */
    @FXML
    private Pane boardPane;

    /**
     * The number of fields on the board.
     */
    private final int boardSize;

    /**
     * The radius of the board's circle.
     */
    private final double boardRadius;

    /**
     * The list of all fields of the board.
     */
    private final List<Field> fields = new ArrayList<>();

    /**
     * Used for the mouse drag mechanic. Saves the position of the mouse when the mouse is first pressed.
     */
    private double mouseDragStartX, mouseDragStartY;

    /**
     * The position of the game pane.
     */
    private double translationX, translationY;

    private double zoom;

    private ArrayList<ImageView> camelImages = new ArrayList<>();

    private ArrayList<Pane> spectatorTiles = new ArrayList<>();

    private ArrayList<Label> diceLabels = new ArrayList<>();

    private HBox currentDiceBox;

    private ArrayList<Field> allFields = new ArrayList<>();

    /**
     * Initializes the board pane.
     */
    public BoardPane(Stage stage, MethodHandler methodHandler, SceneController sceneController) {
        this.stage = stage;
        this.methodHandler = methodHandler;
        this.sceneController = sceneController;
        this.boardSize = methodHandler.getNumberOfSpaces();
        this.boardRadius = 600 * boardSize / 30.0;
        this.translationX = 0;
        this.translationY = 0;
        this.zoom = 1;

        this.image = new Image(getClass().getResource("images/camel0.png").toExternalForm());
        this.pyramidImage = new Image(getClass().getResource("images/pyramid.png").toExternalForm());
        this.carpetsImage = new Image(getClass().getResource("images/carpets.png").toExternalForm());
        this.die_carpetImage = new Image(getClass().getResource("images/die_carpet.png").toExternalForm());
    }

    /**
     * Update zoom by relative value.
     * @param delta The value to update the zoom by. zoom is multiplied by (1 + delta).
     */
    private void updateZoom(double delta) {
        zoom *= 1.0 + delta;
        if (zoom <= 0.1) {
            zoom = 0.1;
        } else if (zoom >= 10.0) {
            zoom = 10.0;
        }
        anchorPane.setScaleX(zoom);
        anchorPane.setScaleY(zoom);
    }

    /**
     * Set the position of the board pane on the screen.
     * @param x Position on x-axis.
     * @param y Position on y-axis.
     */
    private void setTranslation(double x, double y) {
        gamePane.setTranslateX(x);
        gamePane.setTranslateY(y);
    }

    /**
     * Initialize the board pane.
     */
    @FXML
    public void initialize() throws IOException {
        methodHandler.getBoardUpdater().subscribe(BoardEvent.DICE_ROLL, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.CAMEL, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.GAME_FINISH, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.PLAYER_CARD, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.STAGE_SCORING, this);

        drawBoard();

        setupEvents();

        gamePane.setTranslateX(translationX);
        gamePane.setTranslateY(translationY);

        // Initialize player cards
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-card.fxml"));
        fxmlLoader.setController(new PlayerCardController(methodHandler));
        Parent root = fxmlLoader.load();
        playerCardBox.getChildren().add(root);

        // Initialize the timer
        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("game-time.fxml"));
        fxmlLoader2.setController(new GameTimeController(methodHandler));
        Parent root2 = fxmlLoader2.load();
        timerBox.getChildren().add(root2);

        // Initialize the stage scoring view
        StageScoring stageScoring = new StageScoring(methodHandler);
        FXMLLoader fxmlLoader3 = new FXMLLoader(getClass().getResource("stage-scoring.fxml"));
        fxmlLoader3.setController(stageScoring);
        Parent root3 = fxmlLoader3.load();
        scorePane.getChildren().add(root3);

        // Initialize the button for toggling the stage score view
        toggleStageScore.setOnAction(event -> {stageScoring.toggleStageTableVisible();});

        // Initialize the button for returning to the menu
        exitButton.setOnAction(event -> {
            try {
                returnToMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Initialize the carpets
        ImageView carpetsView = new ImageView(new Image(getClass().getResource("images/carpets.png").toExternalForm()));
        carpetsView.preserveRatioProperty().set(true);
        carpetsView.setFitWidth(170);
        carpetsView.setFitHeight(900);

        carpetsBox.getChildren().add(carpetsView);
    }

    /**
     * Sets up event listeners for mouse actions like dragging and scrolling.
     */
    private void setupEvents() {
        anchorPane.setOnMousePressed(event -> {
            mouseDragStartX = event.getX();
            mouseDragStartY = event.getY();
        });

        anchorPane.setOnMouseDragged(event -> {
            setTranslation(translationX + (event.getX() - mouseDragStartX), translationY + (event.getY() - mouseDragStartY));
        });

        anchorPane.setOnMouseReleased(event -> {
            translationX = gamePane.getTranslateX();
            translationY = gamePane.getTranslateY();
        });

        anchorPane.setOnScroll(event -> {
            updateZoom(event.getDeltaY() / 320);
        });
    }

    /**
     * Draw the board by calculating each field's position.
     */
    private void drawBoard() {
        // All fields are placed on a circle and get slightly displaced by noise
        double perVertexAngle = Math.PI*2 / boardSize;
        double fieldHeight = Math.sin(perVertexAngle) * boardRadius;
        JNoise noise = JNoise.newBuilder().perlin().setSeed(23213).setFrequency(1).build();

        for (int i = 0; i < boardSize; i++) {
            double noiseHeight1 = 100 * noise.getNoise(100 + Math.sin(perVertexAngle * i), Math.sin(100 + perVertexAngle * i));
            double noiseHeight2 = 100 * noise.getNoise(100 + Math.sin(perVertexAngle * (i+1)), Math.sin(100 + perVertexAngle * (i+1)));
            // Create a new board field. Each board field is made of 4 vertices
            fields.add(new Field(new Double[] {
                    // Point 1
                    Math.cos(perVertexAngle * i) * (boardRadius + noiseHeight1),
                    Math.sin(perVertexAngle * i ) * (boardRadius + noiseHeight1) * 0.3,
                    // Point 2
                    Math.cos(perVertexAngle * (i+1)) * (boardRadius + noiseHeight2),
                    Math.sin(perVertexAngle * (i+1)) * (boardRadius + noiseHeight2) * 0.3,
                    // Point 3
                    Math.cos(perVertexAngle * (i+1)) * (boardRadius + noiseHeight2 + fieldHeight),
                    Math.sin(perVertexAngle * (i+1)) * (boardRadius + noiseHeight2 + fieldHeight) * 0.3,
                    // Point 4
                    Math.cos(perVertexAngle * i) * (boardRadius + noiseHeight1 + fieldHeight),
                    Math.sin(perVertexAngle * i) * (boardRadius + noiseHeight1 + fieldHeight) * 0.3,
            }, boardPane, i));
        }

        // Draw the pyramid
        ImageView pyramidView = new ImageView(pyramidImage);
        pyramidView.preserveRatioProperty().set(true);
        pyramidView.setFitWidth(300);
        pyramidView.setFitHeight(300);
        pyramidView.setLayoutX(-150);
        pyramidView.setLayoutY(-150);
        pyramidView.setMouseTransparent(true);
        pyramidView.setViewOrder(-99999);
        boardPane.getChildren().add(pyramidView);

        // Draw die carpet
        ImageView dieView = new ImageView(die_carpetImage);
        dieView.preserveRatioProperty().set(true);
        dieView.setFitWidth(700);
        dieView.setFitHeight(700);
        dieView.setLayoutX(-345);
        dieView.setLayoutY(-280);
        dieView.setMouseTransparent(true);
        // Carpet is placed over camels, if wanted, change setViewOrder to place camels over carpet
        dieView.setViewOrder(-9);
        boardPane.getChildren().add(dieView);

        // Fixes a bug so that input is registered everywhere
        gamePane.getChildren().add(new Circle(-10000, -10000, 0));
        gamePane.getChildren().add(new Circle(10000, 10000, 0));

        showAllCamels();
        showAllDice();
        showAllSpectatorTiles();
    }

    /**
     * Display all dice on the field.
     */
    private void showAllDice() {
        deleteAllDice();

        HBox diceBox = new HBox();
        currentDiceBox = diceBox;
        diceBox.setLayoutY(16);
        diceBox.setLayoutX(-97);

        ArrayList<RolledDice> rolledDice = methodHandler.getAllRolledDice();

        for (int i = 0; i < rolledDice.size(); i++) {
            Label label = new Label(" " + rolledDice.get(i).getNumber() + " ");
            label.setTextFill(Color.WHITE);
            label.setBackground(new Background(new BackgroundFill(Color.web(methodHandler.getHexColorOfCamel(rolledDice.get(i).getCamelId())), CornerRadii.EMPTY, Insets.EMPTY)));
            diceLabels.add(label);
            diceBox.getChildren().add(label);
        }

        diceBox.setSpacing(10);
        diceBox.setViewOrder(-10);
        boardPane.getChildren().add(diceBox);
    }

    /**
     * Clear the dice box.
     */
    private void deleteAllDice() {
        boardPane.getChildren().removeAll(currentDiceBox);
    }

    /**
     * Changes elements for the final scoring.
     */
    private void toGameEnd() {
        toggleStageScore.setText("Endwertung");
    }

    @Override
    public void update(BoardEvent event) {
        switch (event) {
            case CAMEL:
                showAllCamels();
                break;
            case DICE_ROLL:
                showAllDice();
                break;
            case GAME_FINISH:
                toGameEnd();
                break;
            case PLAYER_CARD, STAGE_SCORING:
                showAllSpectatorTiles();
                break;
        }
    }

    /**
     * Show all current player cards on all board spaces.
     */
    private void showAllSpectatorTiles() {
        deleteAllSpectatorTiles();

        for (Field field : allFields) {
            field.showSpectatorTile();
        }
    }

    /**
     * Delete all current player cards on all board spaces.
     */
    private void deleteAllSpectatorTiles() {
        for (Pane pane : spectatorTiles) {
            boardPane.getChildren().remove(pane);
        }
        spectatorTiles = new ArrayList<>();
    }

    /**
     * Display all camels.
     */
    private void showAllCamels() {
        deleteAllCamels();

        for (Field field : allFields) {
            field.showCamel();
        }
    }

    /**
     * Remove all camels.
     */
    private void deleteAllCamels() {
        for (ImageView imageView : camelImages) {
            boardPane.getChildren().remove(imageView);
        }
        camelImages = new ArrayList<>();
    }

    /**
     * Return to the menu that lists all currently running games.
     * @throws IOException
     */
    public void returnToMenu() throws IOException {
        methodHandler.getBoardUpdater().notify(BoardEvent.BACK_TO_MENU);
        JoinLobby exitRequest = new JoinLobby(-1, false);
        methodHandler.getClientConnector().getServerHandler().sendMessage(exitRequest.convertTojson());
        sceneController.returnToGameList();
    }

    /**
     * A single field of the board.
     */
    private class Field {
        private Polygon polygon;
        private int spaceID;

        private final Double[] vertices;

        private double centerPositionX;
        private double centerPositionY;

        /**
         * Initialize a new field of the board.
         * @param vertices A field is made of 4 vertices to make a quadrilateral.
         * @param gamePane The pane to display the field on.
         */
        public Field(Double[] vertices, Pane gamePane, int spaceID) {
            this.vertices = vertices;
            this.polygon = new Polygon();
            this.spaceID = spaceID;
            polygon.getPoints().addAll(vertices);
            polygon.setFill(Color.TRANSPARENT);
            polygon.setStroke(Color.PERU);
            polygon.setStrokeWidth(1);
            gamePane.getChildren().add(polygon);

            // Light up the board field if the mouse is inside
            polygon.setOnMouseEntered(event -> {
               polygon.setFill(Color.BURLYWOOD);
            });
            polygon.setOnMouseExited(event -> {
                polygon.setFill(Color.TRANSPARENT);
            });

            calculateCenterPosition();

            allFields.add(this);
            //showCamel();
        }

        /**
         * Calculate the center of the field polygon.
         */
        private void calculateCenterPosition() {
            double positionX = this.vertices[0] + (this.vertices[2] - this.vertices[0]) / 2;
            double positionY = this.vertices[1] + (this.vertices[3] - this.vertices[1]) / 2;

            double heightX = (this.vertices[6] - this.vertices[0]) * 0.5;
            double heightY = (this.vertices[7] - this.vertices[1]) * 0.5;
            heightX += (this.vertices[4] - this.vertices[2]) * 0.5;
            heightY += (this.vertices[5] - this.vertices[3]) * 0.5;

            this.centerPositionX = positionX + heightX / 2;
            this.centerPositionY = positionY + heightY / 2;
        }

        /**
         * Show the camel at the center of this field.
         */
        public void showCamel() {
            // Configure and add the image of the camel
            ArrayList<Camel> camels = methodHandler.getCamelsInField(this.spaceID);

            // Show new Camels
            for (int i = 0; i < camels.size(); i++) {
                ImageView camelImage = new ImageView(image);
                camelImage.preserveRatioProperty().set(true);
                camelImage.setFitWidth(150);
                camelImage.setFitHeight(150);
                camelImage.setLayoutX(-75);
                camelImage.setLayoutY(-60);
                camelImage.setViewOrder(-99999 - centerPositionY);
                camelImage.setTranslateX(centerPositionX);
                camelImage.setTranslateY(centerPositionY - 25 * i);
                camelImage.setMouseTransparent(true);

                String hexColor = methodHandler.getHexColorOfCamel(camels.get(i).getId());

                Color color = Color.web(hexColor);

                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue(((color.getHue() / 360.0) - 0.5) * 2);
                colorAdjust.setBrightness(color.getBrightness() - 0.5);
                colorAdjust.setSaturation(color.getSaturation());

                camelImage.setEffect(colorAdjust);

                camelImages.add(camelImage);

                boardPane.getChildren().add(camelImage);
            }
        }

        /**
         * Show the player cards/spectator tiles.
         */
        public void showSpectatorTile() {
            PlayerCard playerCard = methodHandler.getSpectatorTile(this.spaceID);

            if (playerCard == null) {
                return;
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("spectator-tile.fxml"));
                Pane spectatorTile = fxmlLoader.load();
                SpectatorTile spectatorTileController = fxmlLoader.getController();

                spectatorTileController.setPlus(playerCard.getSpacesMoved());
                spectatorTileController.setPlayerName(methodHandler.getDataManager().getPlayerById(playerCard.getPlayerId()).getName());

                spectatorTile.setLayoutX(centerPositionX);
                spectatorTile.setLayoutY(centerPositionY);
                spectatorTile.setViewOrder(-99999 - centerPositionY);
                spectatorTile.setMouseTransparent(true);
                spectatorTile.setScaleX(0.2);
                spectatorTile.setScaleY(0.2);
                spectatorTile.setTranslateX(-200);
                spectatorTile.setTranslateY(-150);
                spectatorTiles.add(spectatorTile);

                boardPane.getChildren().add(spectatorTile);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Fehler beim Laden von spectator-tile.fxml");
            }
        }
    }
}
