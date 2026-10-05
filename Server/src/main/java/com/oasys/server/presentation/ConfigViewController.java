package com.oasys.server.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.oasys.server.communication.packets.GameState;
import com.oasys.server.data.*;
import com.oasys.server.logic.LobbyManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class allows game settings to be defined via a GUI.
 * Configurable parameters include player count, field size, number of camels etc.
 * Configurations can be saved as JSON files and loaded later.
 */

public class ConfigViewController {

    @FXML private Spinner<Integer> fieldCountSpinner; // new spinner for the amount of fields
    @FXML private Spinner<Integer> forwardCamelSpinner;
    @FXML private Spinner<Integer> bettingCardSpinner;

    /*public static ArrayList<Lobby> getLobbies() {
        return lobbies;
    }
    };*/







    private void initializeField() {
        fieldCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 100, 10));
        forwardCamelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, 4));
        bettingCardSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 5));
    }

    private void generateGameBoard(){
        int fieldCount = fieldCountSpinner.getValue(); //amount of fields are read here
        int forwardCamel = forwardCamelSpinner.getValue();
        int bettingCard = bettingCardSpinner.getValue();
    }

    // ObjectMapper for handling JSON files
    private ObjectMapper objectMapper = new ObjectMapper();

    // List to store game configuration
    private static List<Map<String, Object>> gameConfigs = new ArrayList<>();

    //public static ArrayList<Lobby> lobbies = new ArrayList<>();  // Store lobbies

    public static JsonArray lobbies = new JsonArray();


    //public static Map<Integer, Lobby> lobbies = new HashMap<>();


    // Create a list of board spaces based on the board size
    ArrayList<BoardSpace> boardSpaces = new ArrayList<>();  // Explicitly use ArrayList here


    // set a stage
    private Stage stage;

    // FXML fields for UI elements
    @FXML private Spinner<Integer> playerCountSpinner; // Spinner for number of players
    @FXML private Spinner<Integer> boardSizeSpinner; // Spinner for board size
    @FXML private Spinner<Integer> camelCountSpinner; // Spinner for number of camels
    @FXML private Spinner<Integer> diceMaxValueSpinner; // Spinner for max dice value
    @FXML private Spinner<Integer> bettingCardCountSpinner; // Spinner for number of betting cards
    @FXML private Spinner<Integer> thinkingTimeSpinner; // Spinner for thinking time per player
    @FXML private Spinner<Integer> moveVisualizationTimeSpinner; // Spinner for move visualization time
    @FXML private ChoiceBox<String> foulConsequencesChoiceBox; // ChoiceBox for foul consequences
    @FXML private Spinner<Integer> maxGameTimeSpinner; // Spinner for maximum game time
    @FXML private Spinner<Integer> maxTurnsSpinner; // Spinner for maximum number of turns
    @FXML private Pane mainPane; // Main pane (could be used for layout)


    // Method to set the stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Initialize method to set up the UI elements
    public void initialize() {
        // Set value factory (min, max, and default values) for each spinner
        playerCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 4)); // Number of players between 2 and 6, default 4
        boardSizeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 32767, 16)); // Board size between 3 and 32767, default 16
        camelCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(4, 32767, 5)); // Number of camels between 2 and 32767, default 5

        // Set dice max value depending on the board size (1/3 of the board size)
        diceMaxValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10922, 3));
        //boardSizeSpinner.valueProperty().addListener((obs, oldVal, newVal) ->
        //        diceMaxValueSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10922, 3))
        //); //newVal / 3

        // Set value factory for other spinners
        bettingCardCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 32767, 3)); // Number of betting cards, default 3
        thinkingTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2147483647, 30)); // Thinking time between 1 and 2147483647 seconds, default 30
        moveVisualizationTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2147483647, 1)); // Move visualization time between 1 and 2147483647, default 1

        // Set items for the ChoiceBox and default value
        foulConsequencesChoiceBox.getItems().addAll("Vom Spielabschnitt ausschließen", "Vom Spiel ausschließen"); // Options for foul consequences
        foulConsequencesChoiceBox.setValue("Vom Spielabschnitt ausschließen"); // Default value

        // Set value factory for other spinners
        maxGameTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2147483647, 10)); // Max game time between 1 and 32767 minutes, default 120
        maxTurnsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 32767, 100)); // Max turns between 10 and 32767, default 100
    }

    // Method to return the Game Configurations
    public static List<Map<String, Object>> getGameConfigs() {
        return gameConfigs;
    }

    @FXML

    private void exConfiguration() {
        Gson gson = new Gson();
        JsonArray lobbiesArray = new JsonArray(); // Initialize if not already done

        Map<String, Object> config = new HashMap<>();
        config.put("Anzahl der Spieler", playerCountSpinner.getValue());
        config.put("Spielfeldgröße", boardSizeSpinner.getValue());
        config.put("Anzahl der Kamele", camelCountSpinner.getValue());
        config.put("Maximaler Würfelwert", diceMaxValueSpinner.getValue());
        config.put("Anzahl der Wettscheine", bettingCardCountSpinner.getValue());
        config.put("Bedenkzeit", thinkingTimeSpinner.getValue());
        config.put("Visualisierungszeit pro Zug", moveVisualizationTimeSpinner.getValue());
        config.put("Konsequenz für Regelverstoß", foulConsequencesChoiceBox.getValue());
        config.put("Maximale Spieldauer", maxGameTimeSpinner.getValue());
        config.put("Maximale Züge", maxTurnsSpinner.getValue());

        // Prompt for a game name
        TextInputDialog dialog = new TextInputDialog("Game Name");
        dialog.setTitle("Spiel Name eingeben");
        dialog.setHeaderText("Bitte geben Sie einen Namen für das Spiel ein:");
        dialog.setContentText("Spielname:");

        dialog.showAndWait().ifPresent(gameName -> {
            // Add the game name to the configuration
            config.put("Spielname", gameName);

            // Add the configuration map to the list
            gameConfigs.add(config);

            // Initialize game components
            int numberOfCamels = (int) config.get("Anzahl der Kamele");
            List<String> camelColors = List.of("Red", "Blue", "Green", "Yellow", "Black", "White");
            Random random = new Random();

            ArrayList<Camel> camels = new ArrayList<>();
            for (int i = 1; i <= numberOfCamels; i++) {
                String color = camelColors.get(random.nextInt(camelColors.size()));
                camels.add(new Camel(i, color));
            }

            ArrayList<FinalBet> firstCamelBets = new ArrayList<>();
            ArrayList<FinalBet> lastCamelBets = new ArrayList<>();

            int boardSize = (int) config.get("Spielfeldgröße");

            ArrayList<BoardSpace> boardSpaces = new ArrayList<>();
            for (int i = 0; i < boardSize; i++) {
                boardSpaces.add(new BoardSpace(i, new ArrayList<>()));
            }

            GameConfig gameConfig = new GameConfig(
                    (int) config.get("Anzahl der Spieler"),
                    boardSize,
                    camels,
                    (int) config.get("Maximaler Würfelwert"),
                    (int) config.get("Anzahl der Wettscheine"),
                    (int) config.get("Bedenkzeit"),
                    (int) config.get("Visualisierungszeit pro Zug"),
                    IllegalMovePenalty.FORFEIT_GAME,
                    (int) config.get("Maximale Spieldauer"),
                    (int) config.get("Maximale Züge")
            );

            GameState gameState = new GameState(
                    GamePhase.CREATED,
                    boardSpaces,
                    gameConfig,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>() ,
                    0,
                    (int) config.get("Maximale Spieldauer"),
                    0,
                    new FinalBets(firstCamelBets, lastCamelBets)
            );

            Lobby lobby = new Lobby(
                    lobbies.size(),
                    gameName,
                    gameState,
                    new ArrayList<>()
            );

            // Serialize the lobby to JSON
            lobbies.add(gson.toJsonTree(lobby));
            lobbiesArray.add(gson.toJsonTree(lobby));
            // Load the existing lobbies, if any
            ArrayList<Lobby> Managerlobbies = LobbyManager.loadLobbies();
            Managerlobbies.add(lobby);

            // Write the lobbies to a file
            try (FileWriter writer = new FileWriter("lobbies.json")) {
                gson.toJson(Managerlobbies, writer);
                System.out.println("Lobbies successfully written to file!");
            } catch (IOException e) {
                System.err.println("Error writing lobbies to file: " + e.getMessage());
            }
            System.out.println(LobbyManager.loadLobbies());

            // Debug: Print the JSON representation of the lobbies
            System.out.println(lobbies.toString());
        });
    }

    // Method to save the configuration to a JSON file
    @FXML
    private void saveConfiguration() {
        // Create a file chooser dialog for saving JSON configuration
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Konfiguration speichern");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json")); // Set the file type filter to JSON
        File file = fileChooser.showSaveDialog(null); // Show the save dialog

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Create a map to hold the configuration values
                Map<String, Object> config = new HashMap<>();
                config.put("Anzahl der Spieler", playerCountSpinner.getValue()); // Add the player count
                config.put("Spielfeldgröße", boardSizeSpinner.getValue()); // Add the board size
                config.put("Anzahl der Kamele", camelCountSpinner.getValue()); // Add the number of camels
                config.put("Maximaler Würfelwert", diceMaxValueSpinner.getValue()); // Add the dice max value
                config.put("Anzahl der Wettscheine", bettingCardCountSpinner.getValue()); // Add the number of betting cards
                config.put("Bedenkzeit", thinkingTimeSpinner.getValue()); // Add the thinking time
                config.put("Visualisierungszeit pro Zug", moveVisualizationTimeSpinner.getValue()); // Add the move visualization time
                config.put("Konsequenz für Regelverstoß", foulConsequencesChoiceBox.getValue()); // Add the foul consequences choice
                config.put("Maximale Spieldauer", maxGameTimeSpinner.getValue()); // Add the max game time
                config.put("Maximale Züge", maxTurnsSpinner.getValue()); // Add the max number of turns

                // Write the configuration map to the file as JSON, using pretty-print format
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, config);

            } catch (IOException e) {
                e.printStackTrace(); // Handle any IOException that occurs
            }
        }
    }

    // Method to load the configuration from a JSON file
    @FXML
    private void loadConfiguration() {
        // Create a file chooser dialog for opening a JSON configuration
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Konfiguration laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json")); // Set the file type filter to JSON
        File file = fileChooser.showOpenDialog(null); // Show the open dialog

        if (file != null) {
            try {
                // Read the configuration from the selected file
                Map<String, Object> config = objectMapper.readValue(file, Map.class);

                // Set the values from the configuration file back to the spinners and choice box
                playerCountSpinner.getValueFactory().setValue((Integer) config.get("Anzahl der Spieler"));
                boardSizeSpinner.getValueFactory().setValue((Integer) config.get("Spielfeldgröße"));
                camelCountSpinner.getValueFactory().setValue((Integer) config.get("Anzahl der Kamele"));
                diceMaxValueSpinner.getValueFactory().setValue((Integer) config.get("Maximaler Würfelwert"));
                bettingCardCountSpinner.getValueFactory().setValue((Integer) config.get("Anzahl der Wettscheine"));
                thinkingTimeSpinner.getValueFactory().setValue((Integer) config.get("Bedenkzeit"));
                moveVisualizationTimeSpinner.getValueFactory().setValue((Integer) config.get("Visualisierungszeit pro Zug"));
                foulConsequencesChoiceBox.setValue((String) config.get("Konsequenz für Regelverstoß"));
                maxGameTimeSpinner.getValueFactory().setValue((Integer) config.get("Maximale Spieldauer"));
                maxTurnsSpinner.getValueFactory().setValue((Integer) config.get("Maximale Züge"));
            } catch (IOException e) {
                e.printStackTrace(); // Handle any IOException that occurs
            }
        }
    }

    // Method to open player management window
    public void openPlayerManagementView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("player-management-view.fxml"));
            Scene playerManagementScene = new Scene(fxmlLoader.load());

            // Create a new stage for the player management view
            Stage playerManagementStage = new Stage();
            playerManagementStage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal dialog
            playerManagementStage.setTitle("Spieler verwalten");
            playerManagementStage.setScene(playerManagementScene);
            playerManagementStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void navigateBack() {
        /*try {
            // Close the current stage (ConfigView)
            if (stage != null) {
                stage.close();
            }

            // Load the StartView
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameListView.fxml"));
            Scene startViewScene = new Scene(fxmlLoader.load(), 600, 600);
            Stage startStage = new Stage();
            StartViewController startController = fxmlLoader.getController();

            // Ensure the stage is set in the StartViewController before showing it
            startController.setStage(startStage);

            startStage.setTitle("Start View");  // Set the title for the start view
            startStage.setScene(startViewScene);
            stage.setMaximized(true);
            startStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            // Load the GameListView.fxml without creating a new stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameListView.fxml"));
            // Get the current stage from the scene of an existing element, such as gameListTable or the main pane
            Stage currentStage = (Stage) mainPane.getScene().getWindow(); // You can also use gameListTable.getScene().getWindow() if applicable

            // Set the new scene to the current stage
            currentStage.setScene(new Scene(fxmlLoader.load(), 600, 600));

            // Optionally maximize the stage
            currentStage.setMaximized(true);

            // Show the updated stage
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IOException that occurs
        }


    }

    @FXML
    private void openGameListView() {
        /*try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameListView.fxml"));
            Scene gameListScene = new Scene(fxmlLoader.load());

            Stage gameListStage = stage != null ? stage : new Stage();
            gameListStage.setScene(gameListScene);
            gameListStage.setTitle("Liste der konfigurierten Spiele");
            gameListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try {
            // Load the GameListView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameListView.fxml"));
            Scene gameListScene = new Scene(fxmlLoader.load());

            // Get the current stage from the scene's window
            Stage currentStage = (Stage) mainPane.getScene().getWindow();  // Alternatively, use any other component like a table or button

            // Set the new scene to the current stage
            currentStage.setScene(gameListScene);  // Set the scene for the current stage
            //currentStage.setTitle("Liste der konfigurierten Spiele");  // Optionally set a new title
            currentStage.setMaximized(true);  // Optionally, maximize the window
            currentStage.show();  // Show the updated stage
        } catch (IOException e) {
            e.printStackTrace(); // Handle any IOException that occurs
        }


    }

    @FXML
    private void handleCombinedAction() {
        // Call the existing methods in sequence
        exConfiguration();
        openGameListView();
    }

}