package com.oasys.observer.presentation;

import com.oasys.observer.data.GamePhase;
import com.oasys.observer.logic.BoardEvent;
import com.oasys.observer.logic.ClientConnector;
import com.oasys.observer.logic.EventListener;
import com.oasys.observer.logic.MethodHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controls switching between scenes by loading correct FXML files and updating the stage.
 */
public class SceneController implements EventListener {
    private final Stage stage;

    private BoardPane boardPane;

    private ClientConnector client1;

    private MethodHandler methodHandler;

    private ActionEvent lastEvent;
    private WaitingType waitingType;
    private boolean waitStop = false;

    /**
     * Gets the pane of the board game.
     * @return The pane.
     */
    public BoardPane getBoardPane() {
        if (boardPane == null) {
            System.out.println("BoardPane is not instantiated");
            return null;
        }
        return boardPane;
    }

    /**
     * Sets the pane of the board game.
     * @param boardPane The new board pane.
     */
    public void setBoardPane(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    /**
     * The input field for the ip address of the server.
     */
    @FXML
    private TextField ipAddress;

    /**
     * The input field for the port number of the server.
     */
    @FXML
    private TextField portNumber;

    /**
     * The text for error messages.
     */
    @FXML
    private Text errorText;

    /**
     * Initializes a scene controller.
     * @param stage Stage to load the scenes to.
     * @param methodHandler Instance of the method handling class.
     */
    public SceneController(Stage stage, MethodHandler methodHandler) {
        this.stage = stage;
        this.methodHandler = methodHandler;
        methodHandler.getBoardUpdater().subscribe(BoardEvent.BOARD_READY, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.CLIENT_ACK, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.FEEDBACK_SUCCESS, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.FEEDBACK_FAILED, this);
        methodHandler.getBoardUpdater().subscribe(BoardEvent.CONNECTION_FAILED, this);
    }

    /**
     * Displays the login screen to a server.
     * @throws IOException Failed loading the FXML file.
     */
    @FXML
    public void showJoinServerMenu() throws IOException {
        if(client1 != null) {
            client1.close();
            client1 = null;
        }
        setScene("join-server-menu.fxml", this);
        stage.show();
    }

    /**
     * Displays the scene showing all running games.
     * @throws IOException Failed loading the FXML file.
     */
    @FXML
    public void showGameList() throws IOException {
        switch (waitingType) {
            case SERVER_CONNECT:
                if(waitStop) {
                    waitingType = null;
                    GameListController gameListController = new GameListController(this, methodHandler);
                    setScene("list-of-games-menu.fxml", gameListController);
                    stage.show();
                    waitStop = false;
                }
                break;
            case null, default:
                waitingType = WaitingType.SERVER_CONNECT;
                if(client1 == null) {
                    connectToServer();
                }
        }
    }

    /**
     *
     * @throws IOException
     */
    @FXML
    public void returnToGameList() throws IOException {
        switch (waitingType) {
            case EXIT_GAME_BOARD:
                if (waitStop) {
                    waitingType = null;
                    methodHandler.getBoardUpdater().unsubscribeAll(boardPane);
                    boardPane = null;
                    methodHandler.getDataManager().clearGameBoard();
                    GameListController gameListController = new GameListController(this, methodHandler);
                    setScene("list-of-games-menu.fxml", gameListController);
                    stage.show();
                    waitStop = false;
                }
                break;
            case null, default:
                waitingType = WaitingType.EXIT_GAME_BOARD;
        }
    }

    /**
     * Displays the actual game chosen from the game list.
     * @throws IOException Failed loading the FXML file.
     */
    @FXML
    public void showGameMenu() throws IOException {
        switch (waitingType) {
            case ENTER_GAME_BOARD:
                if(waitStop) {
                    setBoardPane(new BoardPane(stage, methodHandler, this));
                    setScene("board-pane.fxml", boardPane);
                    stage.show();
                    waitingType = null;
                    waitStop = false;

                    // send end notification when finished game is loaded
                    if(methodHandler.getDataManager().getGamePhase() == GamePhase.FINISHED) {
                        methodHandler.getBoardUpdater().notify(BoardEvent.GAME_FINISH);
                    }
                }
                break;
            case null, default:
                waitingType = WaitingType.ENTER_GAME_BOARD;
        }
    }

    /**
     * Set the root of the main stage.
     * @param fxml The FXML file to load the root from.
     * @param controller Used as a controller by the given FXML file
     * @throws IOException Failed loading the FXML file.
     */
    private void setScene(String fxml, Object controller) throws IOException {
        Scene scene = getSceneFrom(fxml, controller);

        double savedWidth = stage.getWidth();
        double savedHeight = stage.getHeight();

        stage.setScene(scene);
        stage.setWidth(savedWidth);
        stage.setHeight(savedHeight);
    }

    /**
     * Get the scene given the FXML file and its corresponding controller.
     * @param fxml The FXML file to load the root from.
     * @param controller Used as a controller by the given FXML file
     * @return The loaded scene.
     * @throws IOException Failed loading the FXML file.
     */
    private Scene getSceneFrom(String fxml, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        return scene;
    }

    /**
     * Close the entire application.
     */
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Connect to a server.
     * @throws IOException
     */
    public void connectToServer() throws IOException {
        /* Connects client to the Server, waits 2 seconds for the connection to establish,
         * until a test message is sent.*/

        //ClientConnection happens in the next lines down
        client1 = new ClientConnector(methodHandler);
        try {
            client1.startConnection(ipAddress.getText(), Integer.parseInt(portNumber.getText()));
        }
        catch (Exception e) {
            errorText.setText("Verbindung fehlgeschlagen.");
            client1 = null;
            waitingType = null;
            return;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Switch to the server menu
     * @param event
     * @throws IOException
     */
    public void switchToServerMenu(ActionEvent event) throws IOException {
        Stage stage = MainView.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("join-server-menu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch to the game scene.
     * @param event
     * @throws IOException
     */
    public void switchToGameScene(ActionEvent event) throws IOException {
        Stage stage = MainView.getStage();
        Parent root = FXMLLoader.load(getClass().getResource("game-menu.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gets waitingType
     * @return waitingType
     */
    public WaitingType getWaitingType() {
        return waitingType;
    }

    /**
     * Sets waitingType
     * @param waitingType
     */
    public void setWaitingType(WaitingType waitingType) {
        this.waitingType = waitingType;
    }

    @Override
    public void update(BoardEvent event) {
        switch (event){
            case BOARD_READY:
                if (waitingType == WaitingType.ENTER_GAME_BOARD) {
                    try {
                        waitStop = true;
                        showGameMenu();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case CLIENT_ACK:
                if (waitingType == WaitingType.SERVER_CONNECT) {
                    try {
                        waitStop = true;
                        showGameList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case FEEDBACK_SUCCESS:
                if (waitingType == WaitingType.EXIT_GAME_BOARD) {
                    try {
                        waitStop = true;
                        returnToGameList();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            case CONNECTION_FAILED:
                client1 = null;
                waitingType = null;
                errorText.setText("Verbindung fehlgeschlagen.");
        }
    }
}
